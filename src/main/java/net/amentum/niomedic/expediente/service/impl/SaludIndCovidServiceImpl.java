package net.amentum.niomedic.expediente.service.impl;

import net.amentum.niomedic.expediente.converter.SaludIndCovidConverter;
import net.amentum.niomedic.expediente.exception.SaludIndCovidException;
import net.amentum.niomedic.expediente.exception.ExceptionServiceCode;
import net.amentum.niomedic.expediente.model.SaludIndCovid;
import net.amentum.niomedic.expediente.persistence.SaludIndCovidRepository;
import net.amentum.niomedic.expediente.service.SaludIndCovidService;
import net.amentum.niomedic.expediente.views.SaludIndCovidView;
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
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


@Service
@Transactional(readOnly = true)
public class SaludIndCovidServiceImpl implements SaludIndCovidService {

    private final Logger logger = LoggerFactory.getLogger(SaludIndCovidServiceImpl.class);
    private final Map<String, Object> colOrderNames = new HashMap<>();
   
    private SaludIndCovidRepository SaludIndCovidRepository;
    private SaludIndCovidConverter SaludIndCovidConverter;


    {
        colOrderNames.put("idindic","idindic");
        colOrderNames.put("medidfk","medidfk");
        colOrderNames.put("pacidfk","pacidfk");
        colOrderNames.put("p1hora","p1hora");
        colOrderNames.put("covid1lu","covid1lu");
        colOrderNames.put("covid1ma","covid1ma");
        colOrderNames.put("covid1mi","covid1mi");
        colOrderNames.put("covid1ju","covid1ju");
        colOrderNames.put("covid1vi","covid1vi");
        colOrderNames.put("covid1sa","covid1sa");
        colOrderNames.put("covid1do","covid1do");
        colOrderNames.put("p2hora","p2hora");
        colOrderNames.put("covid2lu","covid2lu");
        colOrderNames.put("covid2ma","covid2ma");
        colOrderNames.put("covid2mi","covid2mi");
        colOrderNames.put("covid2ju","covid2ju");
        colOrderNames.put("covid2vi","covid2vi");
        colOrderNames.put("covid2sa","covid2sa");
        colOrderNames.put("covid2do","covid2do");
        colOrderNames.put("p3hora","p3hora");
        colOrderNames.put("covid3lu","covid3lu");
        colOrderNames.put("covid3ma","covid3ma");
        colOrderNames.put("covid3mi","covid3mi");
        colOrderNames.put("covid3ju","covid3ju");
        colOrderNames.put("covid3vi","covid3vi");
        colOrderNames.put("covid3sa","covid3sa");
        colOrderNames.put("covid3do","covid3do");
        colOrderNames.put("p4hora","p4hora");
        colOrderNames.put("covid4lu","covid4lu");
        colOrderNames.put("covid4ma","covid4ma");
        colOrderNames.put("covid4mi","covid4mi");
        colOrderNames.put("covid4ju","covid4ju");
        colOrderNames.put("covid4vi","covid4vi");
        colOrderNames.put("covid4sa","covid4sa");
        colOrderNames.put("covid4do","covid4do");

    }

    @Autowired
    public void setSaludIndCovidRepository(SaludIndCovidRepository SaludIndCovidRepository) {
        this.SaludIndCovidRepository = SaludIndCovidRepository;
    }

    @Autowired
    public void setSaludIndCovidConverter(SaludIndCovidConverter SaludIndCovidConverter) {
        this.SaludIndCovidConverter = SaludIndCovidConverter;
    }

    @Transactional(readOnly = false, rollbackFor = {SaludIndCovidException.class})
    @Override
    public void createSaludIndCovid(SaludIndCovidView SaludIndCovidView) throws SaludIndCovidException {
        try {
            if (SaludIndCovidRepository.findByPacidfk(SaludIndCovidView.getPacidfk()) == null) {
                SaludIndCovid SaludIndCovid = SaludIndCovidConverter.toEntity(SaludIndCovidView, new SaludIndCovid(), Boolean.FALSE);
                logger.debug("Insertar nuevo Indicadores Covid: {}", SaludIndCovid);
                SaludIndCovidRepository.save(SaludIndCovid);
            } else {
                logger.info("createSaludIndCovid() - ya existe", SaludIndCovidView.getPacidfk());
            }
        } catch (DataIntegrityViolationException dive) {
            SaludIndCovidException sic = new SaludIndCovidException("No fue posible agregar Indicadores Covid", SaludIndCovidException.LAYER_DAO, SaludIndCovidException.ACTION_INSERT);
            sic.addError("Ocurrio un error al agregar Indicadores Covid");
            logger.error("Error al insertar nuevo Indicadores Covid - CODE {} - {}", sic.getExceptionCode(), SaludIndCovidView, sic);
            throw sic;
        } catch (Exception ex) {
            SaludIndCovidException sic = new SaludIndCovidException("Error inesperado al agregar Indicadores Covid", SaludIndCovidException.LAYER_DAO, SaludIndCovidException.ACTION_INSERT);
            sic.addError("Ocurrió un error al agregar Indicadores Covid");
            logger.error("Error al insertar nuevo Indicadores Covid - CODE {} - {}", sic.getExceptionCode(), SaludIndCovidView, sic);
            throw sic;
        }
    }

    @Transactional(readOnly = false, rollbackFor = {SaludIndCovidException.class})
    @Override
    public void updateSaludIndCovid(SaludIndCovidView SaludIndCovidView) throws SaludIndCovidException {
        try {
            if (SaludIndCovidRepository.findByPacidfk(SaludIndCovidView.getPacidfk()) == null) {
                SaludIndCovidException sic = new SaludIndCovidException("No se encuentra en el sistema Indicadores Covid.", SaludIndCovidException.LAYER_DAO, SaludIndCovidException.ACTION_VALIDATE);
                sic.addError("Indicadores Covid no encontrado");
                throw sic;
            }
            SaludIndCovid SaludIndCovid = SaludIndCovidRepository.findByPacidfk(SaludIndCovidView.getPacidfk());
            SaludIndCovid = SaludIndCovidConverter.toEntity(SaludIndCovidView, SaludIndCovid, Boolean.TRUE);
            logger.debug("Editar Indicadores Covid: {}", SaludIndCovid);
            SaludIndCovidRepository.save(SaludIndCovid);
        } catch (DataIntegrityViolationException dive) {
            SaludIndCovidException sic = new SaludIndCovidException("No fue posible editar Indicadores Covid", SaludIndCovidException.LAYER_DAO, SaludIndCovidException.ACTION_UPDATE);
            sic.addError("Ocurrió un error al editar Indicadores Covid");
            logger.error("Error al editar Indicadores Covid - CODE {} - {}", sic.getExceptionCode(), SaludIndCovidView, sic);
            throw sic;
        } catch (Exception ex) {
            SaludIndCovidException sic = new SaludIndCovidException("Error inesperado al editar Indicadores Covid", SaludIndCovidException.LAYER_DAO, SaludIndCovidException.ACTION_UPDATE);
            sic.addError("Ocurrió un error al editar Indicadores Covid");
            logger.error("Error al editar Indicadores Covid - CODE {} - {}", sic.getExceptionCode(), SaludIndCovidView, sic);
            throw sic;
        }
    }

    @Transactional(readOnly = false, rollbackFor = {SaludIndCovidException.class})
    @Override
    public void deleteSaludIndCovid(String pacidfk) throws SaludIndCovidException {
        try {
            if (SaludIndCovidRepository.findByPacidfk(pacidfk) == null) {
                SaludIndCovidException sic = new SaludIndCovidException("No se encuentra en el sistema Indicadores Covid.", SaludIndCovidException.LAYER_DAO, SaludIndCovidException.ACTION_VALIDATE);
                sic.addError("Indicadores Covid no encontrado");
                throw sic;
            }
            SaludIndCovid SaludIndCovid = SaludIndCovidRepository.findByPacidfk(pacidfk);
            SaludIndCovidRepository.delete(SaludIndCovid);
        } catch (DataIntegrityViolationException dive) {
            SaludIndCovidException sic = new SaludIndCovidException("No fue posible editar Indicadores Covid", SaludIndCovidException.LAYER_DAO, SaludIndCovidException.ACTION_UPDATE);
            sic.addError("Ocurrió un error al eliminar Indicadores Covid");
            logger.error("Error al eliminar Indicadores Covid - CODE {} - {}", sic.getExceptionCode(), pacidfk, sic);
            throw sic;
        } catch (Exception ex) {
            SaludIndCovidException sic = new SaludIndCovidException("Error inesperado al eliminar Indicadores Covid", SaludIndCovidException.LAYER_DAO, SaludIndCovidException.ACTION_UPDATE);
            sic.addError("Ocurrió un error al eliminar Indicadores Covid");
            logger.error("Error al eliminar Indicadores Covid - CODE {} - {}", sic.getExceptionCode(), pacidfk, sic);
            throw sic;
        }
    }

    @Override
    public SaludIndCovidView getDetailsByPacidfk(String pacidfk) throws SaludIndCovidException {
        try {
            if (SaludIndCovidRepository.findByPacidfk(pacidfk) == null) {
                logger.error("No se encontró datos clínicos para el paciente con el ID: {}",pacidfk);
                return null;
            }
            SaludIndCovid SaludIndCovid = SaludIndCovidRepository.findByPacidfk(pacidfk);
            return SaludIndCovidConverter.toView(SaludIndCovid, Boolean.TRUE);
        } catch (DataIntegrityViolationException dive) {
            SaludIndCovidException sic = new SaludIndCovidException("No fue posible editar Indicadores Covid", SaludIndCovidException.LAYER_DAO, SaludIndCovidException.ACTION_SELECT);
            sic.addError("No fue posible obtener detalles Indicadores Covid");
            logger.error("Error al obtener detalles Indicadores Covid - CODE {} - {}", sic.getExceptionCode(), pacidfk, sic);
            throw sic;
        } catch (Exception ex) {
            SaludIndCovidException sic = new SaludIndCovidException("Error inesperado al obtener detalles Indicadores Covid", SaludIndCovidException.LAYER_DAO, SaludIndCovidException.ACTION_SELECT);
            sic.addError("Ocurrió un error al obtener detalles Indicadores Covid");
            logger.error("Error al obtener detalles Indicadores Covid - CODE {} - {}", sic.getExceptionCode(), pacidfk, sic);
            throw sic;
        }
    }

    @Override
    public List<SaludIndCovidView> findAll() throws SaludIndCovidException {
        try {
            List<SaludIndCovid> SaludIndCovidList = SaludIndCovidRepository.findAll();
            List<SaludIndCovidView> SaludIndCovidViewList = new ArrayList<>();
                for (SaludIndCovid cdl : SaludIndCovidList) {
                SaludIndCovidViewList.add(SaludIndCovidConverter.toView(cdl, Boolean.TRUE));
            }
            return SaludIndCovidViewList;
        } catch (Exception ex) {
            SaludIndCovidException sic = new SaludIndCovidException("Ocurrio un error al seleccionar lista Indicadores Covid", SaludIndCovidException.LAYER_SERVICE, SaludIndCovidException.ACTION_SELECT);
            logger.error(ExceptionServiceCode.GROUP + "- Error al tratar de seleccionar lista Indicadores Covid - CODE: {}-{}", sic.getExceptionCode(), ex);
            throw sic;
        }
    }

    @Override
    public Page<SaludIndCovidView> getSaludIndCovidPage(Boolean active, String name, Integer page, Integer size, String orderColumn, String orderType) throws SaludIndCovidException {
        try {
            logger.info("- Obtener listado Indicadores Covid paginable: {} - page {} - size: {} - orderColumn: {} - orderType: {} - active: {}", name, page, size, orderColumn, orderType, active);
            List<SaludIndCovidView> SaludIndCovidViewList = new ArrayList<>();
            Page<SaludIndCovid> SaludIndCovidPage = null;
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
            Specifications<SaludIndCovid> spec = Specifications.where(
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
                SaludIndCovidPage = SaludIndCovidRepository.findAll(request);
            } else {
                SaludIndCovidPage = SaludIndCovidRepository.findAll(spec, request);
            }

            SaludIndCovidPage.getContent().forEach(SaludIndCovid -> {
                SaludIndCovidViewList.add(SaludIndCovidConverter.toView(SaludIndCovid, Boolean.TRUE));
            });
            PageImpl<SaludIndCovidView> SaludIndCovidViewPage = new PageImpl<SaludIndCovidView>(SaludIndCovidViewList, request, SaludIndCovidPage.getTotalElements());
            return SaludIndCovidViewPage;
        } catch (Exception ex) {
            SaludIndCovidException sic = new SaludIndCovidException("Ocurrio un error al seleccionar lista Indicadores Covid paginable", SaludIndCovidException.LAYER_SERVICE, SaludIndCovidException.ACTION_SELECT);
            logger.error(ExceptionServiceCode.GROUP + "- Error al tratar de seleccionar lista Indicadores Covid paginable - CODE: {}-{}", sic.getExceptionCode(), ex);
            throw sic;
        }
    }



}
