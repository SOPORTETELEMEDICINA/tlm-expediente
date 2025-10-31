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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
@Transactional(readOnly = true)
public class SaludNivGlucServiceImpl implements SaludNivGlucService {

    private final Logger logger = LoggerFactory.getLogger(SaludNivGlucServiceImpl.class);
    private final Map<String, Object> colOrderNames = new HashMap<>();

    private SaludNivGlucRepository saludNivGlucRepository;
    private SaludNivGlucConverter saludNivGlucConverter;

    @Autowired(required = false)
    private net.amentum.niomedic.expediente.persistence.SaludIndGlucRepository indGlucRepo;

    @Autowired(required = false)
    private net.amentum.niomedic.expediente.service.notifications.TelemetryAlertService telemetryAlertService;

    {
        colOrderNames.put("idnivelglucosa", "idnivelglucosa");
        colOrderNames.put("medidfk", "medidfk");
        colOrderNames.put("pacidfk", "pacidfk");
        colOrderNames.put("gluperiodo", "gluperiodo");
        colOrderNames.put("glufechahora", "glufechahora");
        colOrderNames.put("glumedida", "glumedida");
    }

    @Autowired
    public void setSaludNivGlucRepository(SaludNivGlucRepository saludNivGlucRepository) {
        this.saludNivGlucRepository = saludNivGlucRepository;
    }

    @Autowired
    public void setSaludNivGlucConverter(SaludNivGlucConverter saludNivGlucConverter) {
        this.saludNivGlucConverter = saludNivGlucConverter;
    }

    @Transactional(readOnly = false, rollbackFor = {SaludNivGlucException.class})
    @Override
    public void createSaludNivGluc(SaludNivGlucView saludNivGlucView) throws SaludNivGlucException {
        try {
            SaludNivGluc saludNivGluc = saludNivGlucConverter.toEntity(saludNivGlucView, new SaludNivGluc(), Boolean.FALSE);
            logger.debug("Insertar nuevo Niveles Glucosa: {}", saludNivGluc);
            saludNivGlucRepository.save(saludNivGluc);
            logger.info("Glucosa guardada exitosamente");

            // === ALERTA TELEMETRÍA: GLUCOSA (EN HILO SEPARADO) ===
            emitirAlertaGlucosaAsync(saludNivGluc);

        } catch (DataIntegrityViolationException dive) {
            SaludNivGlucException ncE = new SaludNivGlucException(
                    "No fue posible agregar Niveles Glucosa",
                    SaludNivGlucException.LAYER_DAO,
                    SaludNivGlucException.ACTION_INSERT
            );
            ncE.addError("Ocurrió un error al agregar Niveles Glucosa");
            logger.error("Error al insertar nuevo Niveles Glucosa - CODE {}", ncE.getExceptionCode(), dive);
            throw ncE;
        } catch (Exception ex) {
            SaludNivGlucException ncE = new SaludNivGlucException(
                    "Error inesperado al agregar Niveles Glucosa",
                    SaludNivGlucException.LAYER_DAO,
                    SaludNivGlucException.ACTION_INSERT
            );
            ncE.addError("Ocurrió un error al agregar Niveles Glucosa");
            logger.error("Error al insertar nuevo Niveles Glucosa - CODE {}", ncE.getExceptionCode(), ex);
            throw ncE;
        }
    }

    @Transactional(readOnly = false, rollbackFor = {SaludNivGlucException.class})
    @Override
    public void updateSaludNivGluc(SaludNivGlucView saludNivGlucView) throws SaludNivGlucException {
        try {
            if (saludNivGlucRepository.findByPacidfk(saludNivGlucView.getPacidfk()) == null) {
                SaludNivGlucException ncE = new SaludNivGlucException(
                        "No se encuentra en el sistema Niveles Glucosa",
                        SaludNivGlucException.LAYER_DAO,
                        SaludNivGlucException.ACTION_VALIDATE
                );
                ncE.addError("Niveles Glucosa no encontrado");
                throw ncE;
            }

            SaludNivGluc saludNivGluc = saludNivGlucRepository.findByPacidfk(saludNivGlucView.getPacidfk());
            saludNivGluc = saludNivGlucConverter.toEntity(saludNivGlucView, saludNivGluc, Boolean.TRUE);
            logger.debug("Editar Niveles Glucosa: {}", saludNivGluc);
            saludNivGlucRepository.save(saludNivGluc);
            logger.info("Glucosa actualizada exitosamente");

            // === ALERTA TELEMETRÍA: GLUCOSA (EN HILO SEPARADO) ===
            emitirAlertaGlucosaAsync(saludNivGluc);

        } catch (DataIntegrityViolationException dive) {
            SaludNivGlucException ncE = new SaludNivGlucException(
                    "No fue posible editar Niveles Glucosa",
                    SaludNivGlucException.LAYER_DAO,
                    SaludNivGlucException.ACTION_UPDATE
            );
            ncE.addError("Ocurrió un error al editar Niveles Glucosa");
            logger.error("Error al editar Niveles Glucosa - CODE {}", ncE.getExceptionCode(), dive);
            throw ncE;
        } catch (Exception ex) {
            SaludNivGlucException ncE = new SaludNivGlucException(
                    "Error inesperado al editar Niveles Glucosa",
                    SaludNivGlucException.LAYER_DAO,
                    SaludNivGlucException.ACTION_UPDATE
            );
            ncE.addError("Ocurrió un error al editar Niveles Glucosa");
            logger.error("Error al editar Niveles Glucosa - CODE {}", ncE.getExceptionCode(), ex);
            throw ncE;
        }
    }

    @Transactional(readOnly = false, rollbackFor = {SaludNivGlucException.class})
    @Override
    public void deleteSaludNivGluc(String pacidfk) throws SaludNivGlucException {
        try {
            SaludNivGluc saludNivGluc = saludNivGlucRepository.findByPacidfk(pacidfk);
            if (saludNivGluc == null) {
                SaludNivGlucException ncE = new SaludNivGlucException(
                        "No se encuentra en el sistema Niveles Glucosa",
                        SaludNivGlucException.LAYER_DAO,
                        SaludNivGlucException.ACTION_VALIDATE
                );
                ncE.addError("Niveles Glucosa no encontrado");
                throw ncE;
            }
            saludNivGlucRepository.delete(saludNivGluc);
        } catch (DataIntegrityViolationException dive) {
            SaludNivGlucException ncE = new SaludNivGlucException(
                    "No fue posible eliminar Niveles Glucosa",
                    SaludNivGlucException.LAYER_DAO,
                    SaludNivGlucException.ACTION_UPDATE
            );
            ncE.addError("Ocurrió un error al eliminar Niveles Glucosa");
            logger.error("Error al eliminar Niveles Glucosa - CODE {}", ncE.getExceptionCode(), dive);
            throw ncE;
        } catch (Exception ex) {
            SaludNivGlucException ncE = new SaludNivGlucException(
                    "Error inesperado al eliminar Niveles Glucosa",
                    SaludNivGlucException.LAYER_DAO,
                    SaludNivGlucException.ACTION_UPDATE
            );
            ncE.addError("Ocurrió un error al eliminar Niveles Glucosa");
            logger.error("Error al eliminar Niveles Glucosa - CODE {}", ncE.getExceptionCode(), ex);
            throw ncE;
        }
    }

    @Override
    public List<SaludNivGlucView> findAll() throws SaludNivGlucException {
        try {
            List<SaludNivGluc> saludNivGlucList = saludNivGlucRepository.findAll();
            List<SaludNivGlucView> saludNivGlucViewList = new ArrayList<>();
            for (SaludNivGluc item : saludNivGlucList) {
                saludNivGlucViewList.add(saludNivGlucConverter.toView(item, Boolean.TRUE));
            }
            return saludNivGlucViewList;
        } catch (Exception ex) {
            SaludNivGlucException ncE = new SaludNivGlucException(
                    "Ocurrió un error al seleccionar lista Niveles Glucosa",
                    SaludNivGlucException.LAYER_SERVICE,
                    SaludNivGlucException.ACTION_SELECT
            );
            logger.error(ExceptionServiceCode.GROUP + "- Error al tratar de seleccionar lista Niveles Glucosa - CODE: {}", ncE.getExceptionCode(), ex);
            throw ncE;
        }
    }

    @Override
    public Page<SaludNivGlucView> getSaludNivGlucSearch(String pacidfk, Integer page, Integer size, String orderColumn, String orderType) throws SaludNivGlucException {
        try {
            logger.info("getSaludNivGlucSearch(): pacidfk={}, page={}, size={}, orderColumn={}, orderType={}", pacidfk, page, size, orderColumn, orderType);

            if (pacidfk == null) {
                SaludNivGlucException pesoE = new SaludNivGlucException(
                        "No se encuentra en el sistema SaludNivGluc",
                        SaludNivGlucException.LAYER_DAO,
                        SaludNivGlucException.ACTION_VALIDATE
                );
                pesoE.addError("pacidfk viene NULO/VACÍO");
                throw pesoE;
            }

            List<SaludNivGlucView> saludNivGlucViewList = new ArrayList<>();
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
                        if (pacidfk != null) {
                            return cb.equal(root.get("pacidfk"), patternSearch);
                        }
                        return null;
                    }
            );

            Page<SaludNivGluc> saludNivGlucPage = (spec == null)
                    ? saludNivGlucRepository.findAll(request)
                    : saludNivGlucRepository.findAll(spec, request);

            saludNivGlucPage.getContent().forEach(item ->
                    saludNivGlucViewList.add(saludNivGlucConverter.toView(item, Boolean.FALSE))
            );

            return new PageImpl<>(saludNivGlucViewList, request, saludNivGlucPage.getTotalElements());
        } catch (SaludNivGlucException pesoE) {
            throw pesoE;
        } catch (Exception ex) {
            SaludNivGlucException pesoE = new SaludNivGlucException(
                    "Ocurrió un error al seleccionar lista SaludNivGluc paginable",
                    SaludNivGlucException.LAYER_SERVICE,
                    SaludNivGlucException.ACTION_SELECT
            );
            logger.error(ExceptionServiceCode.GROUP + "===>>>Error - CODE: {}", pesoE.getExceptionCode(), ex);
            throw pesoE;
        }
    }

    @Override
    public Page<SaludNivGlucView> getSaludNivGlucfechaSearch(String pacidfk, int periodo, String fechaInicio, String fechaFin, Integer page, Integer size, String orderColumn, String orderType) throws SaludNivGlucException {
        try {
            logger.info("getSaludNivGlucfechaSearch(): pacidfk={}, periodo={}, fechaInicio={}, fechaFin={}", pacidfk, periodo, fechaInicio, fechaFin);

            if (pacidfk == null) {
                SaludNivGlucException pesoE = new SaludNivGlucException(
                        "No se encuentra en el sistema SaludNivGluc",
                        SaludNivGlucException.LAYER_DAO,
                        SaludNivGlucException.ACTION_VALIDATE
                );
                pesoE.addError("pacidfk viene NULO/VACÍO");
                throw pesoE;
            }

            List<SaludNivGlucView> saludNivGlucViewList = new ArrayList<>();
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
                            tc = cb.equal(root.get("pacidfk"), patternSearch);
                        }

                        if (fechaInicio != null && fechaFin != null) {
                            try {
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                Date inicialDate = sdf.parse(fechaInicio);

                                Calendar cal = Calendar.getInstance();
                                cal.setTime(sdf.parse(fechaFin));
                                cal.add(Calendar.DATE, 1);
                                Date finalExclusive = cal.getTime();

                                Predicate datePredicate = cb.and(
                                        cb.greaterThanOrEqualTo(root.get("glufechahora"), inicialDate),
                                        cb.lessThan(root.get("glufechahora"), finalExclusive)
                                );

                                tc = (tc != null) ? cb.and(tc, datePredicate) : datePredicate;
                            } catch (Exception ex) {
                                logger.warn("Error al convertir fechas", ex);
                            }
                        }

                        if (periodo != 0) {
                            if (periodo == 8) {
                                Predicate periodoPredicate = cb.and(
                                        cb.greaterThanOrEqualTo(root.get("gluperiodo"), 1),
                                        cb.lessThanOrEqualTo(root.get("gluperiodo"), 9)
                                );
                                tc = (tc != null) ? cb.and(tc, periodoPredicate) : periodoPredicate;
                            } else {
                                Predicate periodoPredicate = cb.equal(root.get("gluperiodo"), periodo);
                                tc = (tc != null) ? cb.and(tc, periodoPredicate) : periodoPredicate;
                            }
                        }

                        return tc;
                    }
            );

            Page<SaludNivGluc> saludNivGlucPage = (spec == null)
                    ? saludNivGlucRepository.findAll(request)
                    : saludNivGlucRepository.findAll(spec, request);

            saludNivGlucPage.getContent().forEach(item ->
                    saludNivGlucViewList.add(saludNivGlucConverter.toView(item, Boolean.FALSE))
            );

            return new PageImpl<>(saludNivGlucViewList, request, saludNivGlucPage.getTotalElements());
        } catch (SaludNivGlucException pesoE) {
            throw pesoE;
        } catch (Exception ex) {
            SaludNivGlucException pesoE = new SaludNivGlucException(
                    "Ocurrió un error al seleccionar lista SaludNivGluc paginable",
                    SaludNivGlucException.LAYER_SERVICE,
                    SaludNivGlucException.ACTION_SELECT
            );
            logger.error(ExceptionServiceCode.GROUP + "===>>>Error - CODE: {}", pesoE.getExceptionCode(), ex);
            throw pesoE;
        }
    }

    /**
     * Emite alerta de glucosa de forma asincrónica en nueva transacción
     * para que no afecte la inserción principal
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    private void emitirAlertaGlucosaAsync(SaludNivGluc saludNivGluc) {
        try {
            if (telemetryAlertService == null || indGlucRepo == null) {
                logger.debug("Servicios de alerta no disponibles");
                return;
            }

            String pacIdStr = saludNivGluc.getPacidfk();
            if (pacIdStr == null || pacIdStr.trim().isEmpty()) {
                logger.debug("pacidfk nulo/vacío - no se emite alerta");
                return;
            }

            try {
                UUID.fromString(pacIdStr);
            } catch (IllegalArgumentException e) {
                logger.warn("pacidfk no es UUID válido: {}", pacIdStr);
                return;
            }

            final Date fechaMedicion = saludNivGluc.getGlufechahora();
            final LocalDateTime fecha = (fechaMedicion != null)
                    ? LocalDateTime.ofInstant(fechaMedicion.toInstant(), ZoneId.systemDefault())
                    : LocalDateTime.now();

            final Integer valor = (saludNivGluc.getGlumedida() != null) ? saludNivGluc.getGlumedida().intValue() : null;
            if (valor == null) {
                logger.debug("Glucosa sin valor - no se emite alerta");
                return;
            }

            final Integer[] urgBaja = {null};
            final Integer[] alertaAlta = {null};
            final Integer[] urgAlta = {null};

            try {
                indGlucRepo.findUltimaPorPaciente(pacIdStr).ifPresent(ig -> {
                    urgBaja[0] = ig.getUrgenciabaja();
                    alertaAlta[0] = ig.getAlertaalta();
                    urgAlta[0] = ig.getUrgenciaalta();
                });

                if (urgBaja[0] != null || alertaAlta[0] != null || urgAlta[0] != null) {
                    telemetryAlertService.evaluarGlucosa(pacIdStr, valor, urgBaja[0], alertaAlta[0], urgAlta[0], fecha, null);
                    logger.info("Alerta de glucosa emitida para paciente: {}", pacIdStr);
                } else {
                    logger.debug("Sin indicaciones de glucosa para paciente {}", pacIdStr);
                }
            } catch (Exception ex) {
                logger.warn("Error al evaluar indicaciones de glucosa: {}", ex.getMessage());
            }

        } catch (Exception ex) {
            logger.warn("Error en emitirAlertaGlucosaAsync: {}", ex.getMessage(), ex);
        }
    }
}