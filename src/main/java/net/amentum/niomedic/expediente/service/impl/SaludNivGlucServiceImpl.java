package net.amentum.niomedic.expediente.service.impl;

import net.amentum.niomedic.expediente.converter.SaludNivGlucConverter;
import net.amentum.niomedic.expediente.exception.ExceptionServiceCode;
import net.amentum.niomedic.expediente.exception.SaludNivGlucException;
import net.amentum.niomedic.expediente.model.SaludNivGluc;
import net.amentum.niomedic.expediente.persistence.SaludNivGlucRepository;
import net.amentum.niomedic.expediente.service.SaludNivGlucService;
import net.amentum.niomedic.expediente.views.SaludNivGlucView;

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

import javax.persistence.criteria.Predicate;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
// NUEVO (si no los tienes):
import java.time.LocalDateTime;
import java.time.ZoneId;


@Service
@Transactional(readOnly = true)
public class SaludNivGlucServiceImpl implements SaludNivGlucService {

    private final Logger logger = LoggerFactory.getLogger(SaludNivGlucServiceImpl.class);
    private final Map<String, Object> colOrderNames = new HashMap<>();
    private SaludNivGlucRepository SaludNivGlucRepository;
    private net.amentum.niomedic.expediente.converter.SaludNivGlucConverter SaludNivGlucConverter;


    {
        colOrderNames.put("idnivelglucosa","idnivelglucosa");
        colOrderNames.put("medidfk","medidfk");
        colOrderNames.put("pacidfk","pacidfk");
        colOrderNames.put("gluperiodo","gluperiodo");
        colOrderNames.put("glufechahora","glufechahora");
        colOrderNames.put("glumedida","glumedida");
    }

    @Autowired
    public void setSaludNivGlucRepository(SaludNivGlucRepository SaludNivGlucRepository) {
        this.SaludNivGlucRepository = SaludNivGlucRepository;
    }

    @Autowired
    public void setSaludNivGlucConverter(SaludNivGlucConverter SaludNivGlucConverter) {
        this.SaludNivGlucConverter = SaludNivGlucConverter;
    }

    // NUEVO:
    @Autowired
    private net.amentum.niomedic.expediente.persistence.SaludIndGlucRepository indGlucRepo;

    @Autowired
    private net.amentum.niomedic.expediente.service.notifications.TelemetryAlertService telemetryAlertService;


    @Transactional(readOnly = false, rollbackFor = {SaludNivGlucException.class})
    @Override
    public void createSaludNivGluc(SaludNivGlucView SaludNivGlucView) throws SaludNivGlucException {
        try {
            SaludNivGluc SaludNivGluc = SaludNivGlucConverter.toEntity(SaludNivGlucView, new SaludNivGluc(), Boolean.FALSE);
            logger.debug("Insertar nuevo Niveles Glucosa: {}", SaludNivGluc);
            SaludNivGlucRepository.save(SaludNivGluc);
            // === ALERTA TELEMETRÍA: GLUCOSA ===
            try {
                // 1) Paciente (en tu BD es VARCHAR)
                final String pacIdStr = SaludNivGluc.getPacidfk();

                // 2) Fecha del nivel (tu entidad guarda java.util.Date)
                final Date fechaMedicion = SaludNivGluc.getGlufechahora();
                final LocalDateTime fecha = (fechaMedicion != null)
                        ? LocalDateTime.ofInstant(fechaMedicion.toInstant(), ZoneId.systemDefault())
                        : LocalDateTime.now();

                // 3) Valor medido
                final Integer valor = (SaludNivGluc.getGlumedida() != null) ? SaludNivGluc.getGlumedida().intValue() : null;
                if (valor == null) {
                    logger.debug("Nivel de glucosa sin 'glumedida' — no se evalúa alerta.");
                    return;
                }

                // 4) Umbrales de INDICACIONES (última por paciente)
                final Integer[] urgBaja = {null};
                final Integer[] alertaAlta = {null};
                final Integer[] urgAlta = {null};

                indGlucRepo.findUltimaPorPaciente(pacIdStr).ifPresent(ig -> {
                    // Usa LOS GETTERS QUE TENGA TU ENTIDAD real
                    urgBaja[0]    = ig.getUrgenciabaja();  // int o Integer
                    alertaAlta[0] = ig.getAlertaalta();
                    urgAlta[0]    = ig.getUrgenciaalta();
                });

                if (urgBaja[0] != null || alertaAlta[0] != null || urgAlta[0] != null) {
                    telemetryAlertService.evaluarGlucosa(
                            pacIdStr,
                            valor,
                            urgBaja[0],
                            alertaAlta[0],
                            urgAlta[0],
                            fecha,
                            null /*idGroup*/
                    );
                } else {
                    logger.debug("Sin indicaciones de glucosa para paciente {} — no se emite alerta.", pacIdStr);
                }
            } catch (Exception ex) {
                logger.warn("No se pudo evaluar/emitir alerta GLUCOSA (create).", ex);
            }

        } catch (DataIntegrityViolationException dive) {
            SaludNivGlucException ncE = new SaludNivGlucException("No fue posible agregar Niveles Glucosa", SaludNivGlucException.LAYER_DAO, SaludNivGlucException.ACTION_INSERT);
            ncE.addError("Ocurrio un error al agregar Niveles Glucosa");
            logger.error("Error al insertar nuevo Niveles Glucosa - CODE {} - {}", ncE.getExceptionCode(), SaludNivGlucView, ncE);
            throw ncE;
        } catch (Exception ex) {
            SaludNivGlucException ncE = new SaludNivGlucException("Error inesperado al agregar Niveles Glucosa", SaludNivGlucException.LAYER_DAO, SaludNivGlucException.ACTION_INSERT);
            ncE.addError("Ocurrió un error al agregar Niveles Glucosa");
            logger.error("Error al insertar nuevo Niveles Glucosa - CODE {} - {}", ncE.getExceptionCode(), SaludNivGlucView, ncE);
            throw ncE;
        }
    }

    @Transactional(readOnly = false, rollbackFor = {SaludNivGlucException.class})
    @Override
    public void updateSaludNivGluc(SaludNivGlucView SaludNivGlucView) throws SaludNivGlucException {
        try {
            if (SaludNivGlucRepository.findByPacidfk(SaludNivGlucView.getPacidfk()) == null) {
                SaludNivGlucException ncE = new SaludNivGlucException("No se encuentra en el sistema Niveles Glucosa.", SaludNivGlucException.LAYER_DAO, SaludNivGlucException.ACTION_VALIDATE);
                ncE.addError("Niveles Glucosa no encontrado");
                throw ncE;
            }
            SaludNivGluc SaludNivGluc = SaludNivGlucRepository.findByPacidfk(SaludNivGlucView.getPacidfk());
            SaludNivGluc = SaludNivGlucConverter.toEntity(SaludNivGlucView, SaludNivGluc, Boolean.TRUE);
            logger.debug("Editar Niveles Glucosa: {}", SaludNivGluc);
            SaludNivGlucRepository.save(SaludNivGluc);

            // === ALERTA TELEMETRÍA: GLUCOSA ===
            try {
                final String pacIdStr = SaludNivGluc.getPacidfk();
                final Date fechaMedicion = SaludNivGluc.getGlufechahora();
                final LocalDateTime fecha = (fechaMedicion != null)
                        ? LocalDateTime.ofInstant(fechaMedicion.toInstant(), ZoneId.systemDefault())
                        : LocalDateTime.now();

                final Integer valor = (SaludNivGluc.getGlumedida() != null) ? SaludNivGluc.getGlumedida().intValue() : null;
                if (valor == null) {
                    logger.debug("Nivel de glucosa sin 'glumedida' — no se evalúa alerta.");
                    return;
                }

                final Integer[] urgBaja = {null};
                final Integer[] alertaAlta = {null};
                final Integer[] urgAlta = {null};

                indGlucRepo.findUltimaPorPaciente(pacIdStr).ifPresent(ig -> {
                    urgBaja[0]    = ig.getUrgenciabaja();
                    alertaAlta[0] = ig.getAlertaalta();
                    urgAlta[0]    = ig.getUrgenciaalta();
                });

                if (urgBaja[0] != null || alertaAlta[0] != null || urgAlta[0] != null) {
                    telemetryAlertService.evaluarGlucosa(pacIdStr, valor, urgBaja[0], alertaAlta[0], urgAlta[0], fecha, null);
                } else {
                    logger.debug("Sin indicaciones de glucosa para paciente {} — no se emite alerta.", pacIdStr);
                }
            } catch (Exception ex) {
                logger.warn("No se pudo evaluar/emitir alerta GLUCOSA (update).", ex);
            }

        } catch (DataIntegrityViolationException dive) {
            SaludNivGlucException ncE = new SaludNivGlucException("No fue posible editar Niveles Glucosa", SaludNivGlucException.LAYER_DAO, SaludNivGlucException.ACTION_UPDATE);
            ncE.addError("Ocurrió un error al editar Niveles Glucosa");
            logger.error("Error al editar Niveles Glucosa - CODE {} - {}", ncE.getExceptionCode(), SaludNivGlucView, ncE);
            throw ncE;
        } catch (Exception ex) {
            SaludNivGlucException ncE = new SaludNivGlucException("Error inesperado al editar Niveles Glucosa", SaludNivGlucException.LAYER_DAO, SaludNivGlucException.ACTION_UPDATE);
            ncE.addError("Ocurrió un error al editar Niveles Glucosa");
            logger.error("Error al editar Niveles Glucosa - CODE {} - {}", ncE.getExceptionCode(), SaludNivGlucView, ncE);
            throw ncE;
        }
    }

    @Transactional(readOnly = false, rollbackFor = {SaludNivGlucException.class})
    @Override
    public void deleteSaludNivGluc(String pacidfk) throws SaludNivGlucException {
        try {
            if (SaludNivGlucRepository.findByPacidfk(pacidfk) == null) {
                SaludNivGlucException ncE = new SaludNivGlucException("No se encuentra en el sistema Niveles Glucosa.", SaludNivGlucException.LAYER_DAO, SaludNivGlucException.ACTION_VALIDATE);
                ncE.addError("Niveles Glucosa no encontrado");
                throw ncE;
            }
            SaludNivGluc SaludNivGluc = SaludNivGlucRepository.findByPacidfk(pacidfk);
            SaludNivGlucRepository.save(SaludNivGluc);
        } catch (DataIntegrityViolationException dive) {
            SaludNivGlucException ncE = new SaludNivGlucException("No fue posible editar Niveles Glucosa", SaludNivGlucException.LAYER_DAO, SaludNivGlucException.ACTION_UPDATE);
            ncE.addError("Ocurrió un error al eliminar Niveles Glucosa");
            logger.error("Error al eliminar Niveles Glucosa - CODE {} - {}", ncE.getExceptionCode(), pacidfk, ncE);
            throw ncE;
        } catch (Exception ex) {
            SaludNivGlucException ncE = new SaludNivGlucException("Error inesperado al eliminar Niveles Glucosa", SaludNivGlucException.LAYER_DAO, SaludNivGlucException.ACTION_UPDATE);
            ncE.addError("Ocurrió un error al eliminar Niveles Glucosa");
            logger.error("Error al eliminar Niveles Glucosa - CODE {} - {}", ncE.getExceptionCode(), pacidfk, ncE);
            throw ncE;
        }
    }



    @Override
    public List<SaludNivGlucView> findAll() throws SaludNivGlucException {
        try {
            List<SaludNivGluc> SaludNivGlucList = SaludNivGlucRepository.findAll();
            List<SaludNivGlucView> SaludNivGlucViewList = new ArrayList<>();
           for (SaludNivGluc cdl : SaludNivGlucList) {
                SaludNivGlucViewList.add(SaludNivGlucConverter.toView(cdl, Boolean.TRUE));
            }
            return SaludNivGlucViewList;
        } catch (Exception ex) {
            SaludNivGlucException ncE = new SaludNivGlucException("Ocurrio un error al seleccionar lista Niveles Glucosa", SaludNivGlucException.LAYER_SERVICE, SaludNivGlucException.ACTION_SELECT);
            logger.error(ExceptionServiceCode.GROUP + "- Error al tratar de seleccionar lista Niveles Glucosa - CODE: {}-{}", ncE.getExceptionCode(), ex);
            throw ncE;
        }
    }

    @Override
    public Page<SaludNivGlucView> getSaludNivGlucSearch(String pacidfk, Integer page, Integer size, String orderColumn, String orderType) throws SaludNivGlucException {
        try {
            logger.info("===>>>getSaludNivGlucPage(): - pacidfk: {} - page: {} - size: {} - orderColumn: {} - orderType: {}",
                    pacidfk, page, size, orderColumn, orderType);
            if (pacidfk == null) {
                logger.error("===>>>pacidfk viene NULO/VACIO: {}", pacidfk);
                SaludNivGlucException pesoE = new SaludNivGlucException("No se encuentra en el sistema SaludNivGluc", SaludNivGlucException.LAYER_DAO, SaludNivGlucException.ACTION_VALIDATE);
                pesoE.addError("pacidfk viene NULO/VACIO: " + pacidfk);
                throw pesoE;
            }
            List<SaludNivGlucView> SaludNivGlucViewList = new ArrayList<>();
            Page<SaludNivGluc> SaludNivGlucPage = null;
            Sort sort = new Sort(Sort.Direction.DESC, (String) colOrderNames.get("idnivelglucosa"));
            if (orderColumn != null && orderType != null) {
                if (orderType.equalsIgnoreCase("DESC"))
                    sort = new Sort(Sort.Direction.DESC, (String) colOrderNames.get(orderColumn));
                else
                    sort = new Sort(Sort.Direction.ASC, (String) colOrderNames.get(orderColumn));
            }
            PageRequest request = new PageRequest(page, size, sort);
            final String patternSearch = pacidfk;
            Specifications<SaludNivGluc> spec = Specifications.where(
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
                SaludNivGlucPage = SaludNivGlucRepository.findAll(request);
            } else {
                SaludNivGlucPage = SaludNivGlucRepository.findAll(spec, request);
            }
            SaludNivGlucPage.getContent().forEach(SaludNivGluc -> {
                SaludNivGlucViewList.add(SaludNivGlucConverter.toView(SaludNivGluc, Boolean.FALSE));
            });
            PageImpl<SaludNivGlucView> SaludNivGlucViewPage = new PageImpl<SaludNivGlucView>(SaludNivGlucViewList, request, SaludNivGlucPage.getTotalElements());
            return SaludNivGlucViewPage;
        } catch (SaludNivGlucException pesoE) {
            throw pesoE;
        } catch (IllegalArgumentException iae) {
            logger.error("===>>>Algun parametro no es correcto");
            SaludNivGlucException pe = new SaludNivGlucException("Algun parametro no es correcto:", SaludNivGlucException.LAYER_SERVICE, SaludNivGlucException.ACTION_VALIDATE);
            pe.addError("Puede que sea null, vacio o valor incorrecto");
            throw pe;
        } catch (Exception ex) {
            SaludNivGlucException pesoE = new SaludNivGlucException("Ocurrio un error al seleccionar lista SaludNivGluc paginable", SaludNivGlucException.LAYER_SERVICE, SaludNivGlucException.ACTION_SELECT);
            logger.error(ExceptionServiceCode.GROUP + "===>>>Error al tratar de seleccionar lista SaludNivGluc paginable - CODE: {}", pesoE.getExceptionCode(), ex);
            throw pesoE;
        }


    }


    @Override
    public Page<SaludNivGlucView> getSaludNivGlucfechaSearch(String pacidfk,int periodo,String fechaInicio, String fechaFin , Integer page, Integer size, String orderColumn, String orderType) throws SaludNivGlucException {
        try {
            logger.info("===>>>getSaludNivGlucfechaPage(): - pacidfk: {} - periodo: {} - Fechainicio: {} - Fechafin: {}- page: {} - size: {} - orderColumn: {} - orderType: {}",
                    pacidfk,periodo,fechaFin, page, size, orderColumn, orderType);

            if (pacidfk == null) {
                logger.error("===>>>pacidfk viene NULO/VACIO: {}", pacidfk);
                SaludNivGlucException pesoE = new SaludNivGlucException("No se encuentra en el sistema SaludNivGluc", SaludNivGlucException.LAYER_DAO, SaludNivGlucException.ACTION_VALIDATE);
                pesoE.addError("pacidfk viene NULO/VACIO: " + pacidfk);
                throw pesoE;
            }

            List<SaludNivGlucView> SaludNivGlucViewList = new ArrayList<>();
            Page<SaludNivGluc> SaludNivGlucfechaPage = null;
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

            Specifications<SaludNivGluc> spec = Specifications.where(
                    (root, query, cb) -> {
                        Predicate tc = null;


                        if (pacidfk != null) {
                            tc = (tc != null ?
                                    cb.and(tc, cb.equal(root.get("pacidfk"), patternSearch)) : cb.equal(root.get("pacidfk"), patternSearch));
                        }
                        if (fechaInicio != null && fechaFin != null) {
                            try {
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                Date inicialDate = sdf.parse(fechaInicio);

                                // mover fechaFin al día siguiente 00:00:00 para usar "<"
                                Calendar cal = Calendar.getInstance();
                                cal.setTime(sdf.parse(fechaFin));
                                cal.add(Calendar.DATE, 1);
                                Date finalExclusive = cal.getTime();

                                // ...y arma el predicado con >= inicio y < (fin + 1 día)
                                tc = (tc != null)
                                        ? cb.and(tc,
                                        cb.greaterThanOrEqualTo(root.get("glufechahora"), inicialDate),
                                        cb.lessThan(root.get("glufechahora"), finalExclusive))
                                        : cb.and(
                                        cb.greaterThanOrEqualTo(root.get("glufechahora"), inicialDate),
                                        cb.lessThan(root.get("glufechahora"), finalExclusive));
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
                                        cb.and(tc, cb.greaterThanOrEqualTo(root.get("gluperiodo"), periodofecha), cb.lessThanOrEqualTo(root.get("gluperiodo"), periodofecha2)) :
                                        cb.and(cb.greaterThanOrEqualTo(root.get("gluperiodo"), periodofecha), cb.lessThanOrEqualTo(root.get("gluperiodo"), periodofecha2));


                            }

                            else{
                                tc = (tc != null) ?
                                        cb.and(tc, cb.greaterThanOrEqualTo(root.get("gluperiodo"), periodo), cb.lessThanOrEqualTo(root.get("gluperiodo"), periodo)) :
                                        cb.and(cb.greaterThanOrEqualTo(root.get("gluperiodo"), periodo), cb.lessThanOrEqualTo(root.get("gluperiodo"), periodo));
                            }



                        }
                        //               if (active != null) {
//                  tc = (tc != null ? cb.and(tc, cb.equal(root.get("activo"), active)) : cb.equal(root.get("activo"), active));
//               }
                        return tc;
                    }
            );

            if (spec == null) {
                SaludNivGlucfechaPage = SaludNivGlucRepository.findAll(request);
            } else {
                SaludNivGlucfechaPage = SaludNivGlucRepository.findAll(spec, request);
            }

            SaludNivGlucfechaPage.getContent().forEach(SaludNivGluc -> {
                SaludNivGlucViewList.add(SaludNivGlucConverter.toView(SaludNivGluc, Boolean.FALSE));
            });
            PageImpl<SaludNivGlucView> SaludNivGlucViewPage = new PageImpl<SaludNivGlucView>(SaludNivGlucViewList, request, SaludNivGlucfechaPage.getTotalElements());
            return SaludNivGlucViewPage;
        } catch (SaludNivGlucException pesoE) {
            throw pesoE;
        } catch (IllegalArgumentException iae) {
            logger.error("===>>>Algun parametro no es correcto");
            SaludNivGlucException pe = new SaludNivGlucException("Algun parametro no es correcto:", SaludNivGlucException.LAYER_SERVICE, SaludNivGlucException.ACTION_VALIDATE);
            pe.addError("Puede que sea null, vacio o valor incorrecto");
            throw pe;
        } catch (Exception ex) {
            SaludNivGlucException pesoE = new SaludNivGlucException("Ocurrio un error al seleccionar lista SaludNivGluc paginable", SaludNivGlucException.LAYER_SERVICE, SaludNivGlucException.ACTION_SELECT);
            logger.error(ExceptionServiceCode.GROUP + "===>>>Error al tratar de seleccionar lista SaludNivGluc paginable - CODE: {}", pesoE.getExceptionCode(), ex);
            throw pesoE;
        }


    }


}
