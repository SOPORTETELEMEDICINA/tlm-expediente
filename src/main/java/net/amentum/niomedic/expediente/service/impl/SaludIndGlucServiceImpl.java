package net.amentum.niomedic.expediente.service.impl;

import net.amentum.niomedic.expediente.converter.SaludIndGlucConverter;
import net.amentum.niomedic.expediente.exception.ExceptionServiceCode;
import net.amentum.niomedic.expediente.exception.SaludIndGlucException;
import net.amentum.niomedic.expediente.model.SaludIndGluc;
import net.amentum.niomedic.expediente.persistence.SaludIndGlucRepository;
import net.amentum.niomedic.expediente.service.SaludIndGlucService;
import net.amentum.niomedic.expediente.views.SaludIndGlucView;
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
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class SaludIndGlucServiceImpl implements SaludIndGlucService {

    private final Logger logger = LoggerFactory.getLogger(SaludIndGlucServiceImpl.class);
    private final Map<String, Object> colOrderNames = new HashMap<>();
    private SaludIndGlucRepository SaludIndGlucRepository;
    private SaludIndGlucConverter SaludIndGlucConverter;


    {
        colOrderNames.put("idindic","idindic");
        colOrderNames.put("medidfk","medidfk");
        colOrderNames.put("pacidfk","pacidfk");
        colOrderNames.put("urgenciabaja","urgenciabaja");
        colOrderNames.put("alertaalta","alertaalta");
        colOrderNames.put("urgenciaalta","urgenciaalta");
        colOrderNames.put("glu1hora","glu1hora");
        colOrderNames.put("glu1lu","glu1lu");
        colOrderNames.put("glu1ma","glu1ma");
        colOrderNames.put("glu1mi","glu1mi");
        colOrderNames.put("glu1ju","glu1ju");
        colOrderNames.put("glu1vi","glu1vi");
        colOrderNames.put("glu1sa","glu1sa");
        colOrderNames.put("glu1do","glu1do");
        colOrderNames.put("glu2hora","glu2hora");
        colOrderNames.put("glu2lu","glu2lu");
        colOrderNames.put("glu2ma","glu2ma");
        colOrderNames.put("glu2mi","glu2mi");
        colOrderNames.put("glu2ju","glu2ju");
        colOrderNames.put("glu2vi","glu2vi");
        colOrderNames.put("glu2sa","glu2sa");
        colOrderNames.put("glu2do","glu2do");
        colOrderNames.put("glu3hora","glu3hora");
        colOrderNames.put("glu3lu","glu3lu");
        colOrderNames.put("glu3ma","glu3ma");
        colOrderNames.put("glu3mi","glu3mi");
        colOrderNames.put("glu3ju","glu3ju");
        colOrderNames.put("glu3vi","glu3vi");
        colOrderNames.put("glu3sa","glu3sa");
        colOrderNames.put("glu3do","glu3do");
        colOrderNames.put("glu4hora","glu4hora");
        colOrderNames.put("glu4lu","glu4lu");
        colOrderNames.put("glu4ma","glu4ma");
        colOrderNames.put("glu4mi","glu4mi");
        colOrderNames.put("glu4ju","glu4ju");
        colOrderNames.put("glu4vi","glu4vi");
        colOrderNames.put("glu4sa","glu4sa");
        colOrderNames.put("glu4do","glu4do");
        colOrderNames.put("glu5hora","glu5hora");
        colOrderNames.put("glu5lu","glu5lu");
        colOrderNames.put("glu5ma","glu5ma");
        colOrderNames.put("glu5mi","glu5mi");
        colOrderNames.put("glu5ju","glu5ju");
        colOrderNames.put("glu5vi","glu5vi");
        colOrderNames.put("glu5sa","glu5sa");
        colOrderNames.put("glu5do","glu5do");
        colOrderNames.put("glu6hora","glu6hora");
        colOrderNames.put("glu6lu","glu6lu");
        colOrderNames.put("glu6ma","glu6ma");
        colOrderNames.put("glu6mi","glu6mi");
        colOrderNames.put("glu6ju","glu6ju");
        colOrderNames.put("glu6vi","glu6vi");
        colOrderNames.put("glu6sa","glu6sa");
        colOrderNames.put("glu6do","glu6do");
        colOrderNames.put("glu7hora","glu7hora");
        colOrderNames.put("glu7lu","glu7lu");
        colOrderNames.put("glu7ma","glu7ma");
        colOrderNames.put("glu7mi","glu7mi");
        colOrderNames.put("glu7ju","glu7ju");
        colOrderNames.put("glu7vi","glu7vi");
        colOrderNames.put("glu7sa","glu7sa");
        colOrderNames.put("glu7do","glu7do");
        colOrderNames.put("glu8hora","glu8hora");
        colOrderNames.put("glu8lu","glu8lu");
        colOrderNames.put("glu8ma","glu8ma");
        colOrderNames.put("glu8mi","glu8mi");
        colOrderNames.put("glu8ju","glu8ju");
        colOrderNames.put("glu8vi","glu8vi");
        colOrderNames.put("glu8sa","glu8sa");
        colOrderNames.put("glu8do","glu8do");


    }

    @Autowired
    public void setSaludIndGlucRepository(SaludIndGlucRepository SaludIndGlucRepository) {
        this.SaludIndGlucRepository = SaludIndGlucRepository;
    }

    @Autowired
    public void setSaludIndGlucConverter(SaludIndGlucConverter SaludIndGlucConverter) {
        this.SaludIndGlucConverter = SaludIndGlucConverter;
    }

    @Transactional(readOnly = false, rollbackFor = {SaludIndGlucException.class})
    @Override
    public void createSaludIndGluc(SaludIndGlucView SaludIndGlucView) throws SaludIndGlucException {
        try {
            if (SaludIndGlucRepository.findByPacidfk(SaludIndGlucView.getPacidfk()) == null) {
                SaludIndGluc SaludIndGluc = SaludIndGlucConverter.toEntity(SaludIndGlucView, new SaludIndGluc(), Boolean.FALSE);
                logger.debug("Insertar nuevo Indicadores Glucosa: {}", SaludIndGluc);
                SaludIndGlucRepository.save(SaludIndGluc);
            } else {
                logger.info("createSaludIndGluc() - ya existe", SaludIndGlucView.getPacidfk());
            }
        } catch (DataIntegrityViolationException dive) {
            SaludIndGlucException sig = new SaludIndGlucException("No fue posible agregar Indicadores Glucosa", SaludIndGlucException.LAYER_DAO, SaludIndGlucException.ACTION_INSERT);
            sig.addError("Ocurrio un error al agregar Indicadores Glucosa");
            logger.error("Error al insertar nuevo Indicadores Glucosa - CODE {} - {}", sig.getExceptionCode(), SaludIndGlucView, sig);
            throw sig;
        } catch (Exception ex) {
            SaludIndGlucException sig = new SaludIndGlucException("Error inesperado al agregar Indicadores Glucosa", SaludIndGlucException.LAYER_DAO, SaludIndGlucException.ACTION_INSERT);
            sig.addError("Ocurrió un error al agregar Indicadores Glucosa");
            logger.error("Error al insertar nuevo Indicadores Glucosa - CODE {} - {}", sig.getExceptionCode(), SaludIndGlucView, sig);
            throw sig;
        }
    }

    @Transactional(readOnly = false, rollbackFor = {SaludIndGlucException.class})
    @Override
    public void updateSaludIndGluc(SaludIndGlucView SaludIndGlucView) throws SaludIndGlucException {
        try {
            if (SaludIndGlucRepository.findByPacidfk(SaludIndGlucView.getPacidfk()) == null) {
                SaludIndGlucException sig = new SaludIndGlucException("No se encuentra en el sistema Indicadores Glucosa.", SaludIndGlucException.LAYER_DAO, SaludIndGlucException.ACTION_VALIDATE);
                sig.addError("Indicadores Glucosa no encontrado");
                throw sig;
            }
            SaludIndGluc SaludIndGluc = SaludIndGlucRepository.findByPacidfk(SaludIndGlucView.getPacidfk());
            SaludIndGluc = SaludIndGlucConverter.toEntity(SaludIndGlucView, SaludIndGluc, Boolean.TRUE);
            logger.debug("Editar Indicadores Glucosa: {}", SaludIndGluc);
            SaludIndGlucRepository.save(SaludIndGluc);
        } catch (DataIntegrityViolationException dive) {
            SaludIndGlucException sig = new SaludIndGlucException("No fue posible editar Indicadores Glucosa", SaludIndGlucException.LAYER_DAO, SaludIndGlucException.ACTION_UPDATE);
            sig.addError("Ocurrió un error al editar Indicadores Glucosa");
            logger.error("Error al editar Indicadores Glucosa - CODE {} - {}", sig.getExceptionCode(), SaludIndGlucView, sig);
            throw sig;
        } catch (Exception ex) {
            SaludIndGlucException sig = new SaludIndGlucException("Error inesperado al editar Indicadores Glucosa", SaludIndGlucException.LAYER_DAO, SaludIndGlucException.ACTION_UPDATE);
            sig.addError("Ocurrió un error al editar Indicadores Glucosa");
            logger.error("Error al editar Indicadores Glucosa - CODE {} - {}", sig.getExceptionCode(), SaludIndGlucView, sig);
            throw sig;
        }
    }

    @Transactional(readOnly = false, rollbackFor = {SaludIndGlucException.class})
    @Override
    public void deleteSaludIndGluc(String pacidfk) throws SaludIndGlucException {
        try {
            if (SaludIndGlucRepository.findByPacidfk(pacidfk) == null) {
                SaludIndGlucException sig = new SaludIndGlucException("No se encuentra en el sistema Indicadores Glucosa.", SaludIndGlucException.LAYER_DAO, SaludIndGlucException.ACTION_VALIDATE);
                sig.addError("Indicadores Glucosa no encontrado");
                throw sig;
            }
            SaludIndGluc SaludIndGluc = SaludIndGlucRepository.findByPacidfk(pacidfk);
            SaludIndGlucRepository.delete(SaludIndGluc);
        } catch (DataIntegrityViolationException dive) {
            SaludIndGlucException sig = new SaludIndGlucException("No fue posible editar Indicadores Glucosa", SaludIndGlucException.LAYER_DAO, SaludIndGlucException.ACTION_UPDATE);
            sig.addError("Ocurrió un error al eliminar Indicadores Glucosa");
            logger.error("Error al eliminar Indicadores Glucosa - CODE {} - {}", sig.getExceptionCode(), pacidfk, sig);
            throw sig;
        } catch (Exception ex) {
            SaludIndGlucException sig = new SaludIndGlucException("Error inesperado al eliminar Indicadores Glucosa", SaludIndGlucException.LAYER_DAO, SaludIndGlucException.ACTION_UPDATE);
            sig.addError("Ocurrió un error al eliminar Indicadores Glucosa");
            logger.error("Error al eliminar Indicadores Glucosa - CODE {} - {}", sig.getExceptionCode(), pacidfk, sig);
            throw sig;
        }
    }

    @Override
    public SaludIndGlucView getDetailsByPacidfk( String pacidfk) throws SaludIndGlucException {
        try {
            if (SaludIndGlucRepository.findByPacidfk(pacidfk) == null) {
                logger.error("No se encontró datos clínicos para el paciente con el ID: {}",pacidfk);
                return null;
            }
            SaludIndGluc SaludIndGluc = SaludIndGlucRepository.findByPacidfk(pacidfk);
            return SaludIndGlucConverter.toView(SaludIndGluc, Boolean.TRUE);
        } catch (DataIntegrityViolationException dive) {
            SaludIndGlucException sig = new SaludIndGlucException("No fue posible editar Indicadores Glucosa", SaludIndGlucException.LAYER_DAO, SaludIndGlucException.ACTION_SELECT);
            sig.addError("No fue posible obtener detalles Indicadores Glucosa");
            logger.error("Error al obtener detalles Indicadores Glucosa - CODE {} - {}", sig.getExceptionCode(), pacidfk, sig);
            throw sig;
        } catch (Exception ex) {
            SaludIndGlucException sig = new SaludIndGlucException("Error inesperado al obtener detalles Indicadores Glucosa", SaludIndGlucException.LAYER_DAO, SaludIndGlucException.ACTION_SELECT);
            sig.addError("Ocurrió un error al obtener detalles Indicadores Glucosa");
            logger.error("Error al obtener detalles Indicadores Glucosa - CODE {} - {}", sig.getExceptionCode(), pacidfk, sig);
            throw sig;
        }
    }

    @Override
    public List<SaludIndGlucView> findAll() throws SaludIndGlucException {
        try {
            List<SaludIndGluc> SaludIndGlucList = SaludIndGlucRepository.findAll();
            List<SaludIndGlucView> SaludIndGlucViewList = new ArrayList<>();
            for (SaludIndGluc cdl : SaludIndGlucList) {
                SaludIndGlucViewList.add(SaludIndGlucConverter.toView(cdl, Boolean.TRUE));
            }
            return SaludIndGlucViewList;
        } catch (Exception ex) {
            SaludIndGlucException sig = new SaludIndGlucException("Ocurrio un error al seleccionar lista Indicadores Glucosa", SaludIndGlucException.LAYER_SERVICE, SaludIndGlucException.ACTION_SELECT);
            logger.error(ExceptionServiceCode.GROUP + "- Error al tratar de seleccionar lista Indicadores Glucosa - CODE: {}-{}", sig.getExceptionCode(), ex);
            throw sig;
        }
    }

    @Override
    public Page<SaludIndGlucView> getSaludIndGlucPage(Boolean active, String name, Integer page, Integer size, String orderColumn, String orderType) throws SaludIndGlucException {
        try {
            logger.info("- Obtener listado Indicadores Glucosa paginable: {} - page {} - size: {} - orderColumn: {} - orderType: {} - active: {}", name, page, size, orderColumn, orderType, active);
            List<SaludIndGlucView> SaludIndGlucViewList = new ArrayList<>();
            Page<SaludIndGluc> SaludIndGlucPage = null;
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
            Specifications<SaludIndGluc> spec = Specifications.where(
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
                SaludIndGlucPage = SaludIndGlucRepository.findAll(request);
            } else {
                SaludIndGlucPage = SaludIndGlucRepository.findAll(spec, request);
            }

            SaludIndGlucPage.getContent().forEach(SaludIndGluc -> {
                SaludIndGlucViewList.add(SaludIndGlucConverter.toView(SaludIndGluc, Boolean.TRUE));
            });
            PageImpl<SaludIndGlucView> SaludIndGlucViewPage = new PageImpl<SaludIndGlucView>(SaludIndGlucViewList, request, SaludIndGlucPage.getTotalElements());
            return SaludIndGlucViewPage;
        } catch (Exception ex) {
            SaludIndGlucException sig = new SaludIndGlucException("Ocurrio un error al seleccionar lista Indicadores Glucosa paginable", SaludIndGlucException.LAYER_SERVICE, SaludIndGlucException.ACTION_SELECT);
            logger.error(ExceptionServiceCode.GROUP + "- Error al tratar de seleccionar lista Indicadores Glucosa paginable - CODE: {}-{}", sig.getExceptionCode(), ex);
            throw sig;
        }
    }





}
