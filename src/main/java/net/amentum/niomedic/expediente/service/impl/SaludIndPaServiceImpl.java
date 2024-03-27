package net.amentum.niomedic.expediente.service.impl;


import net.amentum.niomedic.expediente.converter.SaludIndPaConverter;
import net.amentum.niomedic.expediente.exception.ExceptionServiceCode;
import net.amentum.niomedic.expediente.exception.SaludIndPaException;
import net.amentum.niomedic.expediente.model.SaludIndPa;
import net.amentum.niomedic.expediente.persistence.SaludIndPaRepository;
import net.amentum.niomedic.expediente.service.SaludIndPaService;
import net.amentum.niomedic.expediente.views.SaludIndPaView;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class SaludIndPaServiceImpl implements SaludIndPaService {

    private final Logger logger = LoggerFactory.getLogger(SaludIndPaServiceImpl.class);
    private final Map<String, Object> colOrderNames = new HashMap<>();
    private SaludIndPaRepository SaludIndPaRepository;
    private SaludIndPaConverter SaludIndPaConverter;


    {
        colOrderNames.put("idindic","idindic");
        colOrderNames.put("medidfk","medidfk");
        colOrderNames.put("pacidfk","pacidfk");
        colOrderNames.put("alertabajasys","alertabajasys");
        colOrderNames.put("alertabajadia","alertabajadia");
        colOrderNames.put("urgenciaaltasys","urgenciaaltasys");
        colOrderNames.put("urgenciaaltadia","urgenciaaltadia");
        colOrderNames.put("p1hora","p1hora");
        colOrderNames.put("sys1lu","sys1lu");
        colOrderNames.put("sys1ma","sys1ma");
        colOrderNames.put("sys1mi","sys1mi");
        colOrderNames.put("sys1ju","sys1ju");
        colOrderNames.put("sys1vi","sys1vi");
        colOrderNames.put("sys1sa","sys1sa");
        colOrderNames.put("sys1do","sys1do");
        colOrderNames.put("p2hora","p2hora");
        colOrderNames.put("sys2lu","sys2lu");
        colOrderNames.put("sys2ma","sys2ma");
        colOrderNames.put("sys2mi","sys2mi");
        colOrderNames.put("sys2ju","sys2ju");
        colOrderNames.put("sys2vi","sys2vi");
        colOrderNames.put("sys2sa","sys2sa");
        colOrderNames.put("sys2do","sys2do");
        colOrderNames.put("p3hora","p3hora");
        colOrderNames.put("sys3lu","sys3lu");
        colOrderNames.put("sys3ma","sys3ma");
        colOrderNames.put("sys3mi","sys3mi");
        colOrderNames.put("sys3ju","sys3ju");
        colOrderNames.put("sys3vi","sys3vi");
        colOrderNames.put("sys3sa","sys3sa");
        colOrderNames.put("sys3do","sys3do");
        colOrderNames.put("p4hora","p4hora");
        colOrderNames.put("sys4lu","sys4lu");
        colOrderNames.put("sys4ma","sys4ma");
        colOrderNames.put("sys4mi","sys4mi");
        colOrderNames.put("sys4ju","sys4ju");
        colOrderNames.put("sys4vi","sys4vi");
        colOrderNames.put("sys4sa","sys4sa");
        colOrderNames.put("sys4do","sys4do");
        colOrderNames.put("p5hora","p5hora");
        colOrderNames.put("sys5lu","sys5lu");
        colOrderNames.put("sys5ma","sys5ma");
        colOrderNames.put("sys5mi","sys5mi");
        colOrderNames.put("sys5ju","sys5ju");
        colOrderNames.put("sys5vi","sys5vi");
        colOrderNames.put("sys5sa","sys5sa");
        colOrderNames.put("sys5do","sys5do");
        colOrderNames.put("p6hora","p6hora");
        colOrderNames.put("sys6lu","sys6lu");
        colOrderNames.put("sys6ma","sys6ma");
        colOrderNames.put("sys6mi","sys6mi");
        colOrderNames.put("sys6ju","sys6ju");
        colOrderNames.put("sys6vi","sys6vi");
        colOrderNames.put("sys6sa","sys6sa");
        colOrderNames.put("sys6do","sys6do");
        colOrderNames.put("p7hora","p7hora");
        colOrderNames.put("sys7lu","sys7lu");
        colOrderNames.put("sys7ma","sys7ma");
        colOrderNames.put("sys7mi","sys7mi");
        colOrderNames.put("sys7ju","sys7ju");
        colOrderNames.put("sys7vi","sys7vi");
        colOrderNames.put("sys7sa","sys7sa");
        colOrderNames.put("sys7do","sys7do");



    }

    @Autowired
    public void setSaludIndPaRepository(SaludIndPaRepository SaludIndPaRepository) {
        this.SaludIndPaRepository = SaludIndPaRepository;
    }

    @Autowired
    public void setSaludIndPaConverter(SaludIndPaConverter SaludIndPaConverter) {
        this.SaludIndPaConverter = SaludIndPaConverter;
    }

    @Transactional(readOnly = false, rollbackFor = {SaludIndPaException.class})
    @Override
    public void createSaludIndPa(SaludIndPaView SaludIndPaView) throws SaludIndPaException {
        try {
            if (SaludIndPaRepository.findByPacidfk(SaludIndPaView.getPacidfk()) == null) {
                SaludIndPa SaludIndPa = SaludIndPaConverter.toEntity(SaludIndPaView, new SaludIndPa(), Boolean.FALSE);
                logger.debug("Insertar nuevo Indicadores Presion Arterial: {}", SaludIndPa);
                SaludIndPaRepository.save(SaludIndPa);
            } else {
                logger.info("createSaludIndPa() - ya existe", SaludIndPaView.getPacidfk());
            }
        } catch (DataIntegrityViolationException dive) {
            SaludIndPaException sipaE = new SaludIndPaException("No fue posible agregar Indicadores Presion Arterial", SaludIndPaException.LAYER_DAO, SaludIndPaException.ACTION_INSERT);
            sipaE.addError("Ocurrio un error al agregar Indicadores Presion Arterial");
            logger.error("Error al insertar nuevo Indicadores Presion Arterial - CODE {} - {}", sipaE.getExceptionCode(), SaludIndPaView, sipaE);
            throw sipaE;
        } catch (Exception ex) {
            SaludIndPaException sipaE = new SaludIndPaException("Error inesperado al agregar Indicadores Presion Arterial", SaludIndPaException.LAYER_DAO, SaludIndPaException.ACTION_INSERT);
            sipaE.addError("Ocurrió un error al agregar Indicadores Presion Arterial");
            logger.error("Error al insertar nuevo Indicadores Presion Arterial - CODE {} - {}", sipaE.getExceptionCode(), SaludIndPaView, sipaE);
            throw sipaE;
        }
    }

    @Transactional(readOnly = false, rollbackFor = {SaludIndPaException.class})
    @Override
    public void updateSaludIndPa(SaludIndPaView SaludIndPaView) throws SaludIndPaException {
        try {
            if (SaludIndPaRepository.findByPacidfk(SaludIndPaView.getPacidfk()) == null) {
                SaludIndPaException sipaE = new SaludIndPaException("No se encuentra en el sistema Indicadores Presion Arterial.", SaludIndPaException.LAYER_DAO, SaludIndPaException.ACTION_VALIDATE);
                sipaE.addError("Indicadores Presion Arterial no encontrado");
                throw sipaE;
            }
            SaludIndPa SaludIndPa = SaludIndPaRepository.findByPacidfk(SaludIndPaView.getPacidfk());
            SaludIndPa = SaludIndPaConverter.toEntity(SaludIndPaView, SaludIndPa, Boolean.TRUE);
            logger.debug("Editar Indicadores Presion Arterial: {}", SaludIndPa);
            SaludIndPaRepository.save(SaludIndPa);
        } catch (DataIntegrityViolationException dive) {
            SaludIndPaException sipaE = new SaludIndPaException("No fue posible editar Indicadores Presion Arterial", SaludIndPaException.LAYER_DAO, SaludIndPaException.ACTION_UPDATE);
            sipaE.addError("Ocurrió un error al editar Indicadores Presion Arterial");
            logger.error("Error al editar Indicadores Presion Arterial - CODE {} - {}", sipaE.getExceptionCode(), SaludIndPaView, sipaE);
            throw sipaE;
        } catch (Exception ex) {
            SaludIndPaException sipaE = new SaludIndPaException("Error inesperado al editar Indicadores Presion Arterial", SaludIndPaException.LAYER_DAO, SaludIndPaException.ACTION_UPDATE);
            sipaE.addError("Ocurrió un error al editar Indicadores Presion Arterial");
            logger.error("Error al editar Indicadores Presion Arterial - CODE {} - {}", sipaE.getExceptionCode(), SaludIndPaView, sipaE);
            throw sipaE;
        }
    }

    @Transactional(readOnly = false, rollbackFor = {SaludIndPaException.class})
    @Override
    public void deleteSaludIndPa(String pacidfk) throws SaludIndPaException {
        try {
            if (SaludIndPaRepository.findByPacidfk(pacidfk) == null) {
                SaludIndPaException sipaE = new SaludIndPaException("No se encuentra en el sistema Indicadores Presion Arterial.", SaludIndPaException.LAYER_DAO, SaludIndPaException.ACTION_VALIDATE);
                sipaE.addError("Indicadores Presion Arterial no encontrado");
                throw sipaE;
            }
            SaludIndPa SaludIndPa = SaludIndPaRepository.findByPacidfk(pacidfk);
            SaludIndPaRepository.delete(SaludIndPa);
        } catch (DataIntegrityViolationException dive) {
            SaludIndPaException sipaE = new SaludIndPaException("No fue posible editar Indicadores Presion Arterial", SaludIndPaException.LAYER_DAO, SaludIndPaException.ACTION_UPDATE);
            sipaE.addError("Ocurrió un error al eliminar Indicadores Presion Arterial");
            logger.error("Error al eliminar Indicadores Presion Arterial - CODE {} - {}", sipaE.getExceptionCode(), pacidfk, sipaE);
            throw sipaE;
        } catch (Exception ex) {
            SaludIndPaException sipaE = new SaludIndPaException("Error inesperado al eliminar Indicadores Presion Arterial", SaludIndPaException.LAYER_DAO, SaludIndPaException.ACTION_UPDATE);
            sipaE.addError("Ocurrió un error al eliminar Indicadores Presion Arterial");
            logger.error("Error al eliminar Indicadores Presion Arterial - CODE {} - {}", sipaE.getExceptionCode(), pacidfk, sipaE);
            throw sipaE;
        }
    }

    @Override
    public SaludIndPaView getDetailsByPacidfk(String pacidfk) throws SaludIndPaException {
        try {
            if (SaludIndPaRepository.findByPacidfk(pacidfk) == null) {
                logger.error("No se encontró datos clínicos para el paciente con el ID: {}",pacidfk);
                return null;
            }
            SaludIndPa SaludIndPa = SaludIndPaRepository.findByPacidfk(pacidfk);
            return SaludIndPaConverter.toView(SaludIndPa, Boolean.TRUE);
        } catch (DataIntegrityViolationException dive) {
            SaludIndPaException sipaE = new SaludIndPaException("No fue posible editar Indicadores Presion Arterial", SaludIndPaException.LAYER_DAO, SaludIndPaException.ACTION_SELECT);
            sipaE.addError("No fue posible obtener detalles Indicadores Presion Arterial");
            logger.error("Error al obtener detalles Indicadores Presion Arterial - CODE {} - {}", sipaE.getExceptionCode(), pacidfk, sipaE);
            throw sipaE;
        } catch (Exception ex) {
            SaludIndPaException sipaE = new SaludIndPaException("Error inesperado al obtener detalles Indicadores Presion Arterial", SaludIndPaException.LAYER_DAO, SaludIndPaException.ACTION_SELECT);
            sipaE.addError("Ocurrió un error al obtener detalles Indicadores Presion Arterial");
            logger.error("Error al obtener detalles Indicadores Presion Arterial - CODE {} - {}", sipaE.getExceptionCode(), pacidfk, sipaE);
            throw sipaE;
        }
    }

    @Override
    public List<SaludIndPaView> findAll() throws SaludIndPaException {
        try {
            List<SaludIndPa> SaludIndPaList = SaludIndPaRepository.findAll();
            List<SaludIndPaView> SaludIndPaViewList = new ArrayList<>();
           for (SaludIndPa cdl : SaludIndPaList) {
                SaludIndPaViewList.add(SaludIndPaConverter.toView(cdl, Boolean.TRUE));
            }
            return SaludIndPaViewList;
        } catch (Exception ex) {
            SaludIndPaException sipaE = new SaludIndPaException("Ocurrio un error al seleccionar lista Indicadores Presion Arterial", SaludIndPaException.LAYER_SERVICE, SaludIndPaException.ACTION_SELECT);
            logger.error(ExceptionServiceCode.GROUP + "- Error al tratar de seleccionar lista Indicadores Presion Arterial - CODE: {}-{}", sipaE.getExceptionCode(), ex);
            throw sipaE;
        }
    }

    @Override
    public Page<SaludIndPaView> getSaludIndPaPage(Boolean active, String name, Integer page, Integer size, String orderColumn, String orderType) throws SaludIndPaException {
        try {
            logger.info("- Obtener listado Indicadores Presion Arterial paginable: {} - page {} - size: {} - orderColumn: {} - orderType: {} - active: {}", name, page, size, orderColumn, orderType, active);
            List<SaludIndPaView> SaludIndPaViewList = new ArrayList<>();
            Page<SaludIndPa> SaludIndPaPage = null;
            Sort sort = new Sort(Sort.Direction.ASC, (String) colOrderNames.get("pacidfk"));

            if (orderColumn != null && orderType != null) {
                if (orderType.equalsIgnoreCase("asc")) {
                    sort = new Sort(Sort.Direction.ASC, (String) colOrderNames.get(orderColumn));
                } else {
                    sort = new Sort(Sort.Direction.DESC, (String) colOrderNames.get(orderColumn));
                }

            }
            PageRequest request = new PageRequest(page, size, sort);
            final String patternSearch = "%" + name.toLowerCase() + "%";
            Specifications<SaludIndPa> spec = Specifications.where(
                    (root, query, cb) -> {
                        Predicate tc = null;
                        if (name != null && !name.isEmpty()) {
                            tc = (tc != null ? cb.and(tc, cb.like(cb.lower(root.get("pacidfk")), patternSearch)) : cb.like(cb.lower(root.get("pacidfk")), patternSearch));
                        }
                        if (active != null) {
                            tc = (tc != null ? cb.and(tc, cb.equal(root.get("activo"), active)) : cb.equal(root.get("activo"), active));
                        }
                        return tc;
                    }
            );

            if (spec == null) {
                SaludIndPaPage = SaludIndPaRepository.findAll(request);
            } else {
                SaludIndPaPage = SaludIndPaRepository.findAll(spec, request);
            }

            SaludIndPaPage.getContent().forEach(SaludIndPa -> {
                SaludIndPaViewList.add(SaludIndPaConverter.toView(SaludIndPa, Boolean.TRUE));
            });
            PageImpl<SaludIndPaView> SaludIndPaViewPage = new PageImpl<SaludIndPaView>(SaludIndPaViewList, request, SaludIndPaPage.getTotalElements());
            return SaludIndPaViewPage;
        } catch (Exception ex) {
            SaludIndPaException sipaE = new SaludIndPaException("Ocurrio un error al seleccionar lista Indicadores Presion Arterial paginable", SaludIndPaException.LAYER_SERVICE, SaludIndPaException.ACTION_SELECT);
            logger.error(ExceptionServiceCode.GROUP + "- Error al tratar de seleccionar lista Indicadores Presion Arterial paginable - CODE: {}-{}", sipaE.getExceptionCode(), ex);
            throw sipaE;
        }
    }


}
