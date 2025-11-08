package net.amentum.niomedic.expediente.service.impl;


import net.amentum.niomedic.expediente.converter.SaludNivCovidConverter;
import net.amentum.niomedic.expediente.exception.ExceptionServiceCode;
import net.amentum.niomedic.expediente.exception.SaludNivCovidException;
import net.amentum.niomedic.expediente.model.SaludNivCovid;
import net.amentum.niomedic.expediente.model.SaludNivGluc;
import net.amentum.niomedic.expediente.persistence.SaludIndNutriRepository;
import net.amentum.niomedic.expediente.persistence.SaludNivCovidRepository;
import net.amentum.niomedic.expediente.service.SaludNivCovidService;
import net.amentum.niomedic.expediente.views.SaludNivCovidView;

import org.hibernate.Query;
import org.hibernate.criterion.BetweenExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import java.text.SimpleDateFormat;
import java.util.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.sql.Timestamp;
import java.util.Date;



@Service
@Transactional(readOnly = true)
public class SaludNivCovidServiceImpl implements SaludNivCovidService {

    private final Logger logger = LoggerFactory.getLogger(SaludNivCovidServiceImpl.class);
    private final Map<String, Object> colOrderNames = new HashMap<>();
    private SaludNivCovidRepository SaludNivCovidRepository;
    private net.amentum.niomedic.expediente.converter.SaludNivCovidConverter SaludNivCovidConverter;


    {
        colOrderNames.put("idnivelcovid","idnivelcovid");
        colOrderNames.put("medidfk","medidfk");
        colOrderNames.put("pacidfk","pacidfk");
        colOrderNames.put("covidperiodo","covidperiodo");
        colOrderNames.put("covidfechahora","covidfechahora");
        colOrderNames.put("covidtempmedida","covidtempmedida");
        colOrderNames.put("covidspomedida","covidspomedida");
        colOrderNames.put("covidpulsomedida","covidpulsomedida");


    }

    @org.springframework.beans.factory.annotation.Autowired
    private net.amentum.niomedic.expediente.service.notifications.TelemetryAlertService telemetryAlertService;


    @Autowired
    public void setSaludNivCovidRepository(SaludNivCovidRepository SaludNivCovidRepository) {
        this.SaludNivCovidRepository = SaludNivCovidRepository;
    }

    @Autowired
    public void setSaludNivCovidConverter(SaludNivCovidConverter SaludNivCovidConverter) {
        this.SaludNivCovidConverter = SaludNivCovidConverter;
    }

    @Transactional(readOnly = false, rollbackFor = {SaludNivCovidException.class})
    @Override
    public void createSaludNivCovid(SaludNivCovidView SaludNivCovidView) throws SaludNivCovidException {
        try {
            SaludNivCovid SaludNivCovid = SaludNivCovidConverter.toEntity(SaludNivCovidView, new SaludNivCovid(), Boolean.FALSE);
            logger.debug("Insertar nuevo Niveles Covid: {}", SaludNivCovid);
            SaludNivCovidRepository.save(SaludNivCovid);
            // === ALERTA TELEMETRÍA: COVID ===
            // Reglas fijas: temp >= 39  |  SpO2 < 90  |  pulso >= 120
            try {
                // 1) Paciente (VARCHAR en BD)
                final String pacIdStr = SaludNivCovid.getPacidfk();

                // 2) Fecha/hora del registro — intentamos nombre de getter más común:
                LocalDateTime fecha = LocalDateTime.now();
                try {
                    // Variante 1: getCovidfechahora()
                    Object fh = SaludNivCovid.getClass().getMethod("getCovidfechahora").invoke(SaludNivCovid);
                    if (fh instanceof Timestamp) fecha = ((Timestamp) fh).toLocalDateTime();
                    else if (fh instanceof Date) fecha = LocalDateTime.ofInstant(((Date) fh).toInstant(), ZoneId.systemDefault());
                } catch (NoSuchMethodException nsme) {
                    try {
                        // Variante 2: getCovidFechaHora()
                        Object fh = SaludNivCovid.getClass().getMethod("getCovidFechaHora").invoke(SaludNivCovid);
                        if (fh instanceof Timestamp) fecha = ((Timestamp) fh).toLocalDateTime();
                        else if (fh instanceof Date) fecha = LocalDateTime.ofInstant(((Date) fh).toInstant(), ZoneId.systemDefault());
                    } catch (Exception ignore) { /* se queda fecha = now */ }
                }

                // 3) Lectura de valores (nombres más usados en tu código base)
                Double temp = null;
                Integer spo2 = null;
                Integer pulso = null;

                try {
                    Object v = SaludNivCovid.getClass().getMethod("getCovidtempmedida").invoke(SaludNivCovid);
                    if (v instanceof Number) temp = ((Number) v).doubleValue();
                } catch (NoSuchMethodException ignore) {
                    // alternativa camelCase
                    try {
                        Object v = SaludNivCovid.getClass().getMethod("getCovidTempMedida").invoke(SaludNivCovid);
                        if (v instanceof Number) temp = ((Number) v).doubleValue();
                    } catch (Exception ignore2) {}
                }

                try {
                    Object v = SaludNivCovid.getClass().getMethod("getCovidspomedida").invoke(SaludNivCovid);
                    if (v instanceof Number) spo2 = ((Number) v).intValue();
                } catch (NoSuchMethodException ignore) {
                    try {
                        Object v = SaludNivCovid.getClass().getMethod("getCovidSpoMedida").invoke(SaludNivCovid);
                        if (v instanceof Number) spo2 = ((Number) v).intValue();
                    } catch (Exception ignore2) {}
                }

                try {
                    Object v = SaludNivCovid.getClass().getMethod("getCovidpulsomedida").invoke(SaludNivCovid);
                    if (v instanceof Number) pulso = ((Number) v).intValue();
                } catch (NoSuchMethodException ignore) {
                    try {
                        Object v = SaludNivCovid.getClass().getMethod("getCovidPulsoMedida").invoke(SaludNivCovid);
                        if (v instanceof Number) pulso = ((Number) v).intValue();
                    } catch (Exception ignore2) {}
                }

                // 4) Dispara evaluación (el servicio resuelve al médico por la tabla consulta)
                telemetryAlertService.evaluarCovid(pacIdStr, temp, spo2, pulso, fecha, null);

            } catch (Exception ex) {
                logger.warn("No se pudo evaluar/emitir alerta COVID.", ex);
            }

        } catch (DataIntegrityViolationException dive) {
            SaludNivCovidException ncE = new SaludNivCovidException("No fue posible agregar Niveles Covid", SaludNivCovidException.LAYER_DAO, SaludNivCovidException.ACTION_INSERT);
            ncE.addError("Ocurrio un error al agregar Niveles Covid");
            logger.error("Error al insertar nuevo Niveles Covid - CODE {} - {}", ncE.getExceptionCode(), SaludNivCovidView, ncE);
            throw ncE;
        } catch (Exception ex) {
            SaludNivCovidException ncE = new SaludNivCovidException("Error inesperado al agregar Niveles Covid", SaludNivCovidException.LAYER_DAO, SaludNivCovidException.ACTION_INSERT);
            ncE.addError("Ocurrió un error al agregar Niveles Covid");
            logger.error("Error al insertar nuevo Niveles Covid - CODE {} - {}", ncE.getExceptionCode(), SaludNivCovidView, ncE);
            throw ncE;
        }
    }

    @Transactional(readOnly = false, rollbackFor = {SaludNivCovidException.class})
    @Override
    public void updateSaludNivCovid(SaludNivCovidView SaludNivCovidView) throws SaludNivCovidException {
        try {
            if (SaludNivCovidRepository.findByPacidfk(SaludNivCovidView.getPacidfk()) == null) {
                SaludNivCovidException ncE = new SaludNivCovidException("No se encuentra en el sistema Niveles Covid.", SaludNivCovidException.LAYER_DAO, SaludNivCovidException.ACTION_VALIDATE);
                ncE.addError("Niveles Covid no encontrado");
                throw ncE;
            }
            SaludNivCovid SaludNivCovid = SaludNivCovidRepository.findByPacidfk(SaludNivCovidView.getPacidfk());
            SaludNivCovid = SaludNivCovidConverter.toEntity(SaludNivCovidView, SaludNivCovid, Boolean.TRUE);
            logger.debug("Editar Niveles Covid: {}", SaludNivCovid);
            SaludNivCovidRepository.save(SaludNivCovid);
            // === ALERTA TELEMETRÍA: COVID ===
// Reglas fijas: temp >= 39  |  SpO2 < 90  |  pulso >= 120
            try {
                // 1) Paciente (VARCHAR en BD)
                final String pacIdStr = SaludNivCovid.getPacidfk();

                // 2) Fecha/hora del registro — intentamos nombre de getter más común:
                LocalDateTime fecha = LocalDateTime.now();
                try {
                    // Variante 1: getCovidfechahora()
                    Object fh = SaludNivCovid.getClass().getMethod("getCovidfechahora").invoke(SaludNivCovid);
                    if (fh instanceof Timestamp) fecha = ((Timestamp) fh).toLocalDateTime();
                    else if (fh instanceof Date) fecha = LocalDateTime.ofInstant(((Date) fh).toInstant(), ZoneId.systemDefault());
                } catch (NoSuchMethodException nsme) {
                    try {
                        // Variante 2: getCovidFechaHora()
                        Object fh = SaludNivCovid.getClass().getMethod("getCovidFechaHora").invoke(SaludNivCovid);
                        if (fh instanceof Timestamp) fecha = ((Timestamp) fh).toLocalDateTime();
                        else if (fh instanceof Date) fecha = LocalDateTime.ofInstant(((Date) fh).toInstant(), ZoneId.systemDefault());
                    } catch (Exception ignore) { /* se queda fecha = now */ }
                }

                // 3) Lectura de valores (nombres más usados en tu código base)
                Double temp = null;
                Integer spo2 = null;
                Integer pulso = null;

                try {
                    Object v = SaludNivCovid.getClass().getMethod("getCovidtempmedida").invoke(SaludNivCovid);
                    if (v instanceof Number) temp = ((Number) v).doubleValue();
                } catch (NoSuchMethodException ignore) {
                    // alternativa camelCase
                    try {
                        Object v = SaludNivCovid.getClass().getMethod("getCovidTempMedida").invoke(SaludNivCovid);
                        if (v instanceof Number) temp = ((Number) v).doubleValue();
                    } catch (Exception ignore2) {}
                }

                try {
                    Object v = SaludNivCovid.getClass().getMethod("getCovidspomedida").invoke(SaludNivCovid);
                    if (v instanceof Number) spo2 = ((Number) v).intValue();
                } catch (NoSuchMethodException ignore) {
                    try {
                        Object v = SaludNivCovid.getClass().getMethod("getCovidSpoMedida").invoke(SaludNivCovid);
                        if (v instanceof Number) spo2 = ((Number) v).intValue();
                    } catch (Exception ignore2) {}
                }

                try {
                    Object v = SaludNivCovid.getClass().getMethod("getCovidpulsomedida").invoke(SaludNivCovid);
                    if (v instanceof Number) pulso = ((Number) v).intValue();
                } catch (NoSuchMethodException ignore) {
                    try {
                        Object v = SaludNivCovid.getClass().getMethod("getCovidPulsoMedida").invoke(SaludNivCovid);
                        if (v instanceof Number) pulso = ((Number) v).intValue();
                    } catch (Exception ignore2) {}
                }

                // 4) Dispara evaluación (el servicio resuelve al médico por la tabla consulta)
                telemetryAlertService.evaluarCovid(pacIdStr, temp, spo2, pulso, fecha, null);

            } catch (Exception ex) {
                logger.warn("No se pudo evaluar/emitir alerta COVID.", ex);
            }

        } catch (DataIntegrityViolationException dive) {
            SaludNivCovidException ncE = new SaludNivCovidException("No fue posible editar Niveles Covid", SaludNivCovidException.LAYER_DAO, SaludNivCovidException.ACTION_UPDATE);
            ncE.addError("Ocurrió un error al editar Niveles Covid");
            logger.error("Error al editar Niveles Covid - CODE {} - {}", ncE.getExceptionCode(), SaludNivCovidView, ncE);
            throw ncE;
        } catch (Exception ex) {
            SaludNivCovidException ncE = new SaludNivCovidException("Error inesperado al editar Niveles Covid", SaludNivCovidException.LAYER_DAO, SaludNivCovidException.ACTION_UPDATE);
            ncE.addError("Ocurrió un error al editar Niveles Covid");
            logger.error("Error al editar Niveles Covid - CODE {} - {}", ncE.getExceptionCode(), SaludNivCovidView, ncE);
            throw ncE;
        }
    }

    @Transactional(readOnly = false, rollbackFor = {SaludNivCovidException.class})
    @Override
    public void deleteSaludNivCovid(String pacidfk) throws SaludNivCovidException {
        try {
            if (SaludNivCovidRepository.findByPacidfk(pacidfk) == null) {
                SaludNivCovidException ncE = new SaludNivCovidException("No se encuentra en el sistema Niveles Covid.", SaludNivCovidException.LAYER_DAO, SaludNivCovidException.ACTION_VALIDATE);
                ncE.addError("Niveles Covid no encontrado");
                throw ncE;
            }
            SaludNivCovid SaludNivCovid = SaludNivCovidRepository.findByPacidfk(pacidfk);
            SaludNivCovidRepository.delete(SaludNivCovid);
        } catch (DataIntegrityViolationException dive) {
            SaludNivCovidException ncE = new SaludNivCovidException("No fue posible editar Niveles Covid", SaludNivCovidException.LAYER_DAO, SaludNivCovidException.ACTION_UPDATE);
            ncE.addError("Ocurrió un error al eliminar Niveles Covid");
            logger.error("Error al eliminar Niveles Covid - CODE {} - {}", ncE.getExceptionCode(), pacidfk, ncE);
            throw ncE;
        } catch (Exception ex) {
            SaludNivCovidException ncE = new SaludNivCovidException("Error inesperado al eliminar Niveles Covid", SaludNivCovidException.LAYER_DAO, SaludNivCovidException.ACTION_UPDATE);
            ncE.addError("Ocurrió un error al eliminar Niveles Covid");
            logger.error("Error al eliminar Niveles Covid - CODE {} - {}", ncE.getExceptionCode(), pacidfk, ncE);
            throw ncE;
        }
    }




    @Override
    public List<SaludNivCovidView> findAll() throws SaludNivCovidException {
        try {
            List<SaludNivCovid> SaludNivCovidList = SaludNivCovidRepository.findAll();
            List<SaludNivCovidView> SaludNivCovidViewList = new ArrayList<>();
            for (SaludNivCovid cdl : SaludNivCovidList) {
                SaludNivCovidViewList.add(SaludNivCovidConverter.toView(cdl, Boolean.TRUE));
            }
            return SaludNivCovidViewList;
        } catch (Exception ex) {
            SaludNivCovidException ncE = new SaludNivCovidException("Ocurrio un error al seleccionar lista Niveles Covid", SaludNivCovidException.LAYER_SERVICE, SaludNivCovidException.ACTION_SELECT);
            logger.error(ExceptionServiceCode.GROUP + "- Error al tratar de seleccionar lista Niveles Covid - CODE: {}-{}", ncE.getExceptionCode(), ex);
            throw ncE;
        }
    }


    @Override
    public Page<SaludNivCovidView> getSaludNivCovidSearch(String pacidfk, Integer page, Integer size, String orderColumn, String orderType) throws SaludNivCovidException {
        try {
            logger.info("===>>>getSaludNivCovidPage(): - pacidfk: {} - page: {} - size: {} - orderColumn: {} - orderType: {}",
                    pacidfk, page, size, orderColumn, orderType);

            if (pacidfk == null) {
                logger.error("===>>>pacidfk viene NULO/VACIO: {}", pacidfk);
                SaludNivCovidException pesoE = new SaludNivCovidException("No se encuentra en el sistema SaludNivCovid", SaludNivCovidException.LAYER_DAO, SaludNivCovidException.ACTION_VALIDATE);
                pesoE.addError("pacidfk viene NULO/VACIO: " + pacidfk);
                throw pesoE;
            }

            List<SaludNivCovidView> SaludNivCovidViewList = new ArrayList<>();
            Page<SaludNivCovid> SaludNivCovidPage = null;
            Sort sort = new Sort(Sort.Direction.DESC, (String) colOrderNames.get("idnivelcovid"));
            if (orderColumn != null && orderType != null) {
                if (orderType.equalsIgnoreCase("asc"))
                    sort = new Sort(Sort.Direction.ASC, (String) colOrderNames.get(orderColumn));
                else
                    sort = new Sort(Sort.Direction.DESC, (String) colOrderNames.get(orderColumn));
            }
            PageRequest request = new PageRequest(page, size, sort);
            final String patternSearch = pacidfk;
            Specifications<SaludNivCovid> spec = Specifications.where(
                    (root, query, cb) -> {
                        Predicate tc = null;

                        if (pacidfk != null) {
                            tc = (tc != null ?
                                    cb.and(tc, cb.equal(root.get("pacidfk"), patternSearch)) : cb.equal(root.get("pacidfk"), patternSearch));
                        }
                        return tc;
                    }
            );

            if (spec == null) {
                SaludNivCovidPage = SaludNivCovidRepository.findAll(request);
            } else {
                SaludNivCovidPage = SaludNivCovidRepository.findAll(spec, request);
            }

            SaludNivCovidPage.getContent().forEach(SaludNivCovid -> {
                SaludNivCovidViewList.add(SaludNivCovidConverter.toView(SaludNivCovid, Boolean.FALSE));
            });
            PageImpl<SaludNivCovidView> SaludNivCovidViewPage = new PageImpl<SaludNivCovidView>(SaludNivCovidViewList, request, SaludNivCovidPage.getTotalElements());
            return SaludNivCovidViewPage;
        } catch (SaludNivCovidException pesoE) {
            throw pesoE;
        } catch (IllegalArgumentException iae) {
            logger.error("===>>>Algun parametro no es correcto");
            SaludNivCovidException pe = new SaludNivCovidException("Algun parametro no es correcto:", SaludNivCovidException.LAYER_SERVICE, SaludNivCovidException.ACTION_VALIDATE);
            pe.addError("Puede que sea null, vacio o valor incorrecto");
            throw pe;
        } catch (Exception ex) {
            SaludNivCovidException pesoE = new SaludNivCovidException("Ocurrio un error al seleccionar lista SaludNivCovid paginable", SaludNivCovidException.LAYER_SERVICE, SaludNivCovidException.ACTION_SELECT);
            logger.error(ExceptionServiceCode.GROUP + "===>>>Error al tratar de seleccionar lista SaludNivCovid paginable - CODE: {}", pesoE.getExceptionCode(), ex);
            throw pesoE;
        }


    }


    @Override
    public Page<SaludNivCovidView> getSaludNivCovidfechaSearch(String pacidfk,int periodo,String fechaInicio, String fechaFin , Integer page, Integer size, String orderColumn, String orderType) throws SaludNivCovidException {
        try {
            logger.info("===>>>getSaludNivCovidfechaPage(): - pacidfk: {} - periodo: {} - Fechainicio: {} - Fechafin: {}- page: {} - size: {} - orderColumn: {} - orderType: {}",
                    pacidfk,periodo,fechaFin, page, size, orderColumn, orderType);

            if (pacidfk == null) {
                logger.error("===>>>pacidfk viene NULO/VACIO: {}", pacidfk);
                SaludNivCovidException pesoE = new SaludNivCovidException("No se encuentra en el sistema SaludNivCovid", SaludNivCovidException.LAYER_DAO, SaludNivCovidException.ACTION_VALIDATE);
                pesoE.addError("pacidfk viene NULO/VACIO: " + pacidfk);
                throw pesoE;
            }

            List<SaludNivCovidView> SaludNivCovidViewList = new ArrayList<>();
            Page<SaludNivCovid> SaludNivCovidfechaPage = null;
            Sort sort = new Sort(Sort.Direction.ASC, (String) colOrderNames.get("pacidfk"));

            if (orderColumn != null && orderType != null) {
                if (orderType.equalsIgnoreCase("asc")) {
                    sort = new Sort(Sort.Direction.ASC, (String) colOrderNames.get(orderColumn));
                } else {
                    sort = new Sort(Sort.Direction.DESC, (String) colOrderNames.get(orderColumn));
                }

            }
            PageRequest request = new PageRequest(page, size, sort);
            final String patternSearch = pacidfk;

            Specifications<SaludNivCovid> spec = Specifications.where(
                    (root, query, cb) -> {
                        Predicate tc = null;

                        if (pacidfk != null) {
                            tc = (tc != null ?
                                    cb.and(tc, cb.equal(root.get("pacidfk"), patternSearch)) : cb.equal(root.get("pacidfk"), patternSearch));
                        }
                        if (fechaInicio != null && fechaFin != null) {
                            try {

                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                Date start = sdf.parse(fechaInicio);
                                Calendar c = Calendar.getInstance();
                                c.setTime(sdf.parse(fechaFin));
                                c.add(Calendar.DAY_OF_MONTH, 1);
                                Date endExclusive = c.getTime();

                                tc = (tc != null)
                                        ? cb.and(tc,
                                        cb.greaterThanOrEqualTo(root.get("covidfechahora"), start),
                                        cb.lessThan(root.get("covidfechahora"), endExclusive))
                                        : cb.and(
                                        cb.greaterThanOrEqualTo(root.get("covidfechahora"), start),
                                        cb.lessThan(root.get("covidfechahora"), endExclusive));
                            } catch (Exception ex) {
                                logger.warn("Error al convertir fechas", ex);
                            }
                        }
                        if (periodo != 0) {
                            logger.info("parametros:  - periodo: {}",
                                    periodo);
                            if(periodo==8){
                                int periodofecha =1;
                                int periodofecha2=9;
                                tc = (tc != null) ?
                                        cb.and(tc, cb.greaterThanOrEqualTo(root.get("covidperiodo"), periodofecha), cb.lessThanOrEqualTo(root.get("covidperiodo"), periodofecha2)) :
                                        cb.and(cb.greaterThanOrEqualTo(root.get("covidperiodo"), periodofecha), cb.lessThanOrEqualTo(root.get("covidperiodo"), periodofecha2));


                            }

                            else{
                                tc = (tc != null) ?
                                        cb.and(tc, cb.greaterThanOrEqualTo(root.get("covidperiodo"), periodo), cb.lessThanOrEqualTo(root.get("covidperiodo"), periodo)) :
                                        cb.and(cb.greaterThanOrEqualTo(root.get("covidperiodo"), periodo), cb.lessThanOrEqualTo(root.get("covidperiodo"), periodo));
                            }



                        }
                        //               if (active != null) {
//                  tc = (tc != null ? cb.and(tc, cb.equal(root.get("activo"), active)) : cb.equal(root.get("activo"), active));
//               }
                        return tc;
                    }
            );

            if (spec == null) {
                SaludNivCovidfechaPage = SaludNivCovidRepository.findAll(request);
            } else {
                SaludNivCovidfechaPage = SaludNivCovidRepository.findAll(spec, request);
            }

            SaludNivCovidfechaPage.getContent().forEach(SaludNivCovid -> {
                SaludNivCovidViewList.add(SaludNivCovidConverter.toView(SaludNivCovid, Boolean.FALSE));
            });
            PageImpl<SaludNivCovidView> SaludNivCovidViewPage = new PageImpl<SaludNivCovidView>(SaludNivCovidViewList, request, SaludNivCovidfechaPage.getTotalElements());
            return SaludNivCovidViewPage;
        } catch (SaludNivCovidException pesoE) {
            throw pesoE;
        } catch (IllegalArgumentException iae) {
            logger.error("===>>>Algun parametro no es correcto");
            SaludNivCovidException pe = new SaludNivCovidException("Algun parametro no es correcto:", SaludNivCovidException.LAYER_SERVICE, SaludNivCovidException.ACTION_VALIDATE);
            pe.addError("Puede que sea null, vacio o valor incorrecto");
            throw pe;
        } catch (Exception ex) {
            SaludNivCovidException pesoE = new SaludNivCovidException("Ocurrio un error al seleccionar lista SaludNivCovid paginable", SaludNivCovidException.LAYER_SERVICE, SaludNivCovidException.ACTION_SELECT);
            logger.error(ExceptionServiceCode.GROUP + "===>>>Error al tratar de seleccionar lista SaludNivCovid paginable - CODE: {}", pesoE.getExceptionCode(), ex);
            throw pesoE;
        }


    }


    @Override
    public Page<SaludNivCovidView> getSaludNivCovidperiodoSearch(String pacidfk,int periodo,int PeriodoF, Integer page, Integer size, String orderColumn, String orderType) throws SaludNivCovidException {
        try {
            logger.info("===>>>getSaludNivCovidperiodoPage(): - pacidfk: {} - periodo: {} - TipoPeriodo: {} - page: {} - size: {} - orderColumn: {} - orderType: {}",
                    pacidfk,periodo,PeriodoF, page, size, orderColumn, orderType);

            if (pacidfk == null) {
                logger.error("===>>>pacidfk viene NULO/VACIO: {}", pacidfk);
                SaludNivCovidException pesoE = new SaludNivCovidException("No se encuentra en el sistema SaludNivCovid", SaludNivCovidException.LAYER_DAO, SaludNivCovidException.ACTION_VALIDATE);
                pesoE.addError("pacidfk viene NULO/VACIO: " + pacidfk);
                throw pesoE;
            }

            List<SaludNivCovidView> SaludNivCovidViewList = new ArrayList<>();
            Page<SaludNivCovid> SaludNivCovidperiodoPage = null;
            Sort sort = new Sort(Sort.Direction.ASC, (String) colOrderNames.get("pacidfk"));

            if (orderColumn != null && orderType != null) {
                if (orderType.equalsIgnoreCase("asc")) {
                    sort = new Sort(Sort.Direction.ASC, (String) colOrderNames.get(orderColumn));
                } else {
                    sort = new Sort(Sort.Direction.DESC, (String) colOrderNames.get(orderColumn));
                }

            }
            PageRequest request = new PageRequest(page, size, sort);
            final String patternSearch = pacidfk;


            Specifications<SaludNivCovid> spec = Specifications.where(
                    (root, query, cb) -> {
                        Predicate tc = null;

                        if (pacidfk != null) {
                            tc = (tc != null ?
                                    cb.and(tc, cb.equal(root.get("pacidfk"), patternSearch)) : cb.equal(root.get("pacidfk"), patternSearch));
                        }

                        if (PeriodoF != 0) {
                            logger.info("parametros:  - periodo: {}",
                                    PeriodoF);
                            if(PeriodoF==1){
                               String dia="days";
                               String sentencia="- interval '7 days'";
                                tc = (tc != null )?
                                cb.and(tc, cb.greaterThanOrEqualTo(root.get("covidfechahora"), dia), cb.lessThanOrEqualTo(root.get("covidfechahora"), sentencia)) :
                                        cb.and(cb.greaterThanOrEqualTo(root.get("covidfechahora"), dia), cb.lessThanOrEqualTo(root.get("covidfechahora"), sentencia));
                            }

                            if(PeriodoF==2){
                               String dia="month";
                                String sentencia="";
                                tc = (tc != null) ?
                                        cb.and(tc, cb.greaterThanOrEqualTo(root.get("covidfechahora"),dia), cb.lessThanOrEqualTo(root.get("covidfechahora"), sentencia)) :
                                        cb.and(cb.greaterThanOrEqualTo(root.get("covidfechahora"), dia), cb.lessThanOrEqualTo(root.get("covidfechahora"), sentencia));
                                        }

                            if(PeriodoF==3){
                               String dia="year";
                                String sentencia="";
                                tc = (tc != null) ?
                                        cb.and(tc, cb.greaterThanOrEqualTo(root.get("covidfechahora"), dia), cb.lessThanOrEqualTo(root.get("covidfechahora"), sentencia)) :
                                        cb.and(cb.greaterThanOrEqualTo(root.get("covidfechahora"), dia), cb.lessThanOrEqualTo(root.get("covidfechahora"), sentencia));
                            }



                        }


                        if (periodo != 0) {
                            logger.info("parametros:  - periodo: {}",
                                    periodo);
                            if(periodo==8){
                                int periodofecha =1;
                                int periodofecha2=9;
                                tc = (tc != null) ?
                                        cb.and(tc, cb.greaterThanOrEqualTo(root.get("covidperiodo"), periodofecha), cb.lessThanOrEqualTo(root.get("covidperiodo"), periodofecha2)) :
                                        cb.and(cb.greaterThanOrEqualTo(root.get("covidperiodo"), periodofecha), cb.lessThanOrEqualTo(root.get("covidperiodo"), periodofecha2));


                            }

                            else{
                                tc = (tc != null) ?
                                        cb.and(tc, cb.greaterThanOrEqualTo(root.get("covidperiodo"), periodo), cb.lessThanOrEqualTo(root.get("covidperiodo"), periodo)) :
                                        cb.and(cb.greaterThanOrEqualTo(root.get("covidperiodo"), periodo), cb.lessThanOrEqualTo(root.get("covidperiodo"), periodo));
                            }



                        }

                        //               if (active != null) {
//                  tc = (tc != null ? cb.and(tc, cb.equal(root.get("activo"), active)) : cb.equal(root.get("activo"), active));
//               }

                        return tc;
                    }
            );


            if (spec == null) {
                SaludNivCovidperiodoPage = SaludNivCovidRepository.findAll(request);
            } else {
                SaludNivCovidperiodoPage = SaludNivCovidRepository.findAll(spec, request);
            }

            SaludNivCovidperiodoPage.getContent().forEach(SaludNivCovid -> {
                SaludNivCovidViewList.add(SaludNivCovidConverter.toView(SaludNivCovid, Boolean.FALSE));
            });
            PageImpl<SaludNivCovidView> SaludNivCovidViewPage = new PageImpl<SaludNivCovidView>(SaludNivCovidViewList, request, SaludNivCovidperiodoPage.getTotalElements());
            return SaludNivCovidViewPage;
        } catch (SaludNivCovidException pesoE) {
            throw pesoE;
        } catch (IllegalArgumentException iae) {
            logger.error("===>>>Algun parametro no es correcto");
            SaludNivCovidException pe = new SaludNivCovidException("Algun parametro no es correcto:", SaludNivCovidException.LAYER_SERVICE, SaludNivCovidException.ACTION_VALIDATE);
            pe.addError("Puede que sea null, vacio o valor incorrecto");
            throw pe;
        } catch (Exception ex) {
            SaludNivCovidException pesoE = new SaludNivCovidException("Ocurrio un error al seleccionar lista SaludNivCovid paginable", SaludNivCovidException.LAYER_SERVICE, SaludNivCovidException.ACTION_SELECT);
            logger.error(ExceptionServiceCode.GROUP + "===>>>Error al tratar de seleccionar lista SaludNivCovid paginable - CODE: {}", pesoE.getExceptionCode(), ex);
            throw pesoE;
        }


    }


}
