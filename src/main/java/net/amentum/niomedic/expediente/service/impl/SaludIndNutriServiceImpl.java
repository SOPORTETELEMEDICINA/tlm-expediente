package net.amentum.niomedic.expediente.service.impl;

import net.amentum.niomedic.expediente.converter.SaludIndNutriConverter;
import net.amentum.niomedic.expediente.exception.ExceptionServiceCode;
import net.amentum.niomedic.expediente.exception.SaludIndNutriException;
import net.amentum.niomedic.expediente.model.SaludIndNutri;
import net.amentum.niomedic.expediente.persistence.SaludIndNutriRepository;
import net.amentum.niomedic.expediente.service.SaludIndNutriService;
import net.amentum.niomedic.expediente.views.SaludIndNutriView;
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
public class SaludIndNutriServiceImpl implements SaludIndNutriService {

    private final Logger logger = LoggerFactory.getLogger(SaludIndNutriServiceImpl.class);
    private final Map<String, Object> colOrderNames = new HashMap<>();
    private SaludIndNutriRepository SaludIndNutriRepository;
    private SaludIndNutriConverter SaludIndNutriConverter;


    {
        colOrderNames.put("idindic", "idindic");
        colOrderNames.put("medidfk", "medidfk");
        colOrderNames.put("pacidfk", "pacidfk");
        colOrderNames.put("pesoinicial", "pesoinicial");
        colOrderNames.put("tallainicial", "tallainicial");
        colOrderNames.put("p1hora", "p1hora");
        colOrderNames.put("nutri1lu", "nutri1lu");
        colOrderNames.put("nutri1ma", "nutri1ma");
        colOrderNames.put("nutri1mi", "nutri1mi");
        colOrderNames.put("nutri1ju", "nutri1ju");
        colOrderNames.put("nutri1vi", "nutri1vi");
        colOrderNames.put("nutri1sa", "nutri1sa");
        colOrderNames.put("nutri1do", "nutri1do");
        colOrderNames.put("p2hora", "p2hora");
        colOrderNames.put("nutri2lu", "nutri2lu");
        colOrderNames.put("nutri2ma", "nutri2ma");
        colOrderNames.put("nutri2mi", "nutri2mi");
        colOrderNames.put("nutri2ju", "nutri2ju");
        colOrderNames.put("nutri2vi", "nutri2vi");
        colOrderNames.put("nutri2sa", "nutri2sa");
        colOrderNames.put("nutri2do", "nutri2do");
        colOrderNames.put("p3hora", "p3hora");
        colOrderNames.put("nutri3lu", "nutri3lu");
        colOrderNames.put("nutri3ma", "nutri3ma");
        colOrderNames.put("nutri3mi", "nutri3mi");
        colOrderNames.put("nutri3ju", "nutri3ju");
        colOrderNames.put("nutri3vi", "nutri3vi");
        colOrderNames.put("nutri3sa", "nutri3sa");
        colOrderNames.put("nutri3do", "nutri3do");


    }

    @Autowired
    public void setSaludIndNutriRepository(SaludIndNutriRepository SaludIndNutriRepository) {
        this.SaludIndNutriRepository = SaludIndNutriRepository;
    }

    @Autowired
    public void setSaludIndNutriConverter(SaludIndNutriConverter SaludIndNutriConverter) {
        this.SaludIndNutriConverter = SaludIndNutriConverter;
    }

    @Transactional(readOnly = false, rollbackFor = {SaludIndNutriException.class})
    @Override
    public void createSaludIndNutri(SaludIndNutriView SaludIndNutriView) throws SaludIndNutriException {
        try {
            if (SaludIndNutriRepository.findByPacidfk(SaludIndNutriView.getPacidfk()) == null) {
                SaludIndNutri SaludIndNutri = SaludIndNutriConverter.toEntity(SaludIndNutriView, new SaludIndNutri(), Boolean.FALSE);
                logger.debug("Insertar nuevo Indicadores Nutricion: {}", SaludIndNutri);
                SaludIndNutriRepository.save(SaludIndNutri);
            } else {
                logger.info("createSaludIndNutri() - ya existe", SaludIndNutriView.getPacidfk());
            }
        } catch (DataIntegrityViolationException dive) {
            SaludIndNutriException sin = new SaludIndNutriException("No fue posible agregar Indicadores Nutricion", SaludIndNutriException.LAYER_DAO, SaludIndNutriException.ACTION_INSERT);
            sin.addError("Ocurrio un error al agregar Indicadores Nutricion");
            logger.error("Error al insertar nuevo Indicadores Nutricion - CODE {} - {}", sin.getExceptionCode(), SaludIndNutriView, sin);
            throw sin;
        } catch (Exception ex) {
            SaludIndNutriException sin = new SaludIndNutriException("Error inesperado al agregar Indicadores Nutricion", SaludIndNutriException.LAYER_DAO, SaludIndNutriException.ACTION_INSERT);
            sin.addError("Ocurrió un error al agregar Indicadores Nutricion");
            logger.error("Error al insertar nuevo Indicadores Nutricion - CODE {} - {}", sin.getExceptionCode(), SaludIndNutriView, sin);
            throw sin;
        }
    }

    @Transactional(readOnly = false, rollbackFor = {SaludIndNutriException.class})
    @Override
    public void updateSaludIndNutri(SaludIndNutriView SaludIndNutriView) throws SaludIndNutriException {
        try {
            if (SaludIndNutriRepository.findByPacidfk(SaludIndNutriView.getPacidfk()) == null) {
                SaludIndNutriException sin = new SaludIndNutriException("No se encuentra en el sistema Indicadores Nutricion.", SaludIndNutriException.LAYER_DAO, SaludIndNutriException.ACTION_VALIDATE);
                sin.addError("Indicadores Nutricion no encontrado");
                throw sin;
            }
            SaludIndNutri SaludIndNutri = SaludIndNutriRepository.findByPacidfk(SaludIndNutriView.getPacidfk());
            SaludIndNutri = SaludIndNutriConverter.toEntity(SaludIndNutriView, SaludIndNutri, Boolean.TRUE);
            logger.debug("Editar Indicadores Nutricion: {}", SaludIndNutri);
            SaludIndNutriRepository.save(SaludIndNutri);
        } catch (DataIntegrityViolationException dive) {
            SaludIndNutriException sin = new SaludIndNutriException("No fue posible editar Indicadores Nutricion", SaludIndNutriException.LAYER_DAO, SaludIndNutriException.ACTION_UPDATE);
            sin.addError("Ocurrió un error al editar Indicadores Nutricion");
            logger.error("Error al editar Indicadores Nutricion - CODE {} - {}", sin.getExceptionCode(), SaludIndNutriView, sin);
            throw sin;
        } catch (Exception ex) {
            SaludIndNutriException sin = new SaludIndNutriException("Error inesperado al editar Indicadores Nutricion", SaludIndNutriException.LAYER_DAO, SaludIndNutriException.ACTION_UPDATE);
            sin.addError("Ocurrió un error al editar Indicadores Nutricion");
            logger.error("Error al editar Indicadores Nutricion - CODE {} - {}", sin.getExceptionCode(), SaludIndNutriView, sin);
            throw sin;
        }
    }

    @Transactional(readOnly = false, rollbackFor = {SaludIndNutriException.class})
    @Override
    public void deleteSaludIndNutri(String pacidfk) throws SaludIndNutriException {
        try {
            if (SaludIndNutriRepository.findByPacidfk(pacidfk) == null) {
                SaludIndNutriException sin = new SaludIndNutriException("No se encuentra en el sistema Indicadores Nutricion.", SaludIndNutriException.LAYER_DAO, SaludIndNutriException.ACTION_VALIDATE);
                sin.addError("Indicadores Nutricion no encontrado");
                throw sin;
            }
            SaludIndNutri SaludIndNutri = SaludIndNutriRepository.findByPacidfk(pacidfk);
            SaludIndNutriRepository.save(SaludIndNutri);
        } catch (DataIntegrityViolationException dive) {
            SaludIndNutriException sin = new SaludIndNutriException("No fue posible editar Indicadores Nutricion", SaludIndNutriException.LAYER_DAO, SaludIndNutriException.ACTION_UPDATE);
            sin.addError("Ocurrió un error al eliminar Indicadores Nutricion");
            logger.error("Error al eliminar Indicadores Nutricion - CODE {} - {}", sin.getExceptionCode(), pacidfk, sin);
            throw sin;
        } catch (Exception ex) {
            SaludIndNutriException sin = new SaludIndNutriException("Error inesperado al eliminar Indicadores Nutricion", SaludIndNutriException.LAYER_DAO, SaludIndNutriException.ACTION_UPDATE);
            sin.addError("Ocurrió un error al eliminar Indicadores Nutricion");
            logger.error("Error al eliminar Indicadores Nutricion - CODE {} - {}", sin.getExceptionCode(), pacidfk, sin);
            throw sin;
        }
    }

    @Override
    public SaludIndNutriView getDetailsByPacidfk(String pacidfk) throws SaludIndNutriException {
        try {
            if (SaludIndNutriRepository.findByPacidfk(pacidfk) == null) {
                logger.error("No se encontró datos clínicos para el paciente con el ID: {}", pacidfk);
                return null;
            }
            SaludIndNutri SaludIndNutri = SaludIndNutriRepository.findByPacidfk(pacidfk);
            return SaludIndNutriConverter.toView(SaludIndNutri, Boolean.TRUE);
        } catch (DataIntegrityViolationException dive) {
            SaludIndNutriException sin = new SaludIndNutriException("No fue posible editar Indicadores Nutricion", SaludIndNutriException.LAYER_DAO, SaludIndNutriException.ACTION_SELECT);
            sin.addError("No fue posible obtener detalles Indicadores Nutricion");
            logger.error("Error al obtener detalles Indicadores Nutricion - CODE {} - {}", sin.getExceptionCode(), pacidfk, sin);
            throw sin;
        } catch (Exception ex) {
            SaludIndNutriException sin = new SaludIndNutriException("Error inesperado al obtener detalles Indicadores Nutricion", SaludIndNutriException.LAYER_DAO, SaludIndNutriException.ACTION_SELECT);
            sin.addError("Ocurrió un error al obtener detalles Indicadores Nutricion");
            logger.error("Error al obtener detalles Indicadores Nutricion - CODE {} - {}", sin.getExceptionCode(), pacidfk, sin);
            throw sin;
        }
    }

    @Override
    public List<SaludIndNutriView> findAll() throws SaludIndNutriException {
        try {
            List<SaludIndNutri> SaludIndNutriList = SaludIndNutriRepository.findAll();
            List<SaludIndNutriView> SaludIndNutriViewList = new ArrayList<>();
            for (SaludIndNutri cdl : SaludIndNutriList) {
                SaludIndNutriViewList.add(SaludIndNutriConverter.toView(cdl, Boolean.TRUE));
            }
            return SaludIndNutriViewList;
        } catch (Exception ex) {
            SaludIndNutriException sin = new SaludIndNutriException("Ocurrio un error al seleccionar lista Indicadores Nutricion", SaludIndNutriException.LAYER_SERVICE, SaludIndNutriException.ACTION_SELECT);
            logger.error(ExceptionServiceCode.GROUP + "- Error al tratar de seleccionar lista Indicadores Nutricion - CODE: {}-{}", sin.getExceptionCode(), ex);
            throw sin;
        }
    }

    @Override
    public Page<SaludIndNutriView> getSaludIndNutriPage(Boolean active, String name, Integer page, Integer size, String orderColumn, String orderType) throws SaludIndNutriException {
        try {
            logger.info("- Obtener listado Indicadores Nutricion paginable: {} - page {} - size: {} - orderColumn: {} - orderType: {} - active: {}", name, page, size, orderColumn, orderType, active);
            List<SaludIndNutriView> SaludIndNutriViewList = new ArrayList<>();
            Page<SaludIndNutri> SaludIndNutriPage = null;
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
            Specifications<SaludIndNutri> spec = Specifications.where(
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
                SaludIndNutriPage = SaludIndNutriRepository.findAll(request);
            } else {
                SaludIndNutriPage = SaludIndNutriRepository.findAll(spec, request);
            }

            SaludIndNutriPage.getContent().forEach(SaludIndNutri -> {
                SaludIndNutriViewList.add(SaludIndNutriConverter.toView(SaludIndNutri, Boolean.TRUE));
            });
            PageImpl<SaludIndNutriView> SaludIndNutriViewPage = new PageImpl<SaludIndNutriView>(SaludIndNutriViewList, request, SaludIndNutriPage.getTotalElements());
            return SaludIndNutriViewPage;
        } catch (Exception ex) {
            SaludIndNutriException sin = new SaludIndNutriException("Ocurrio un error al seleccionar lista Indicadores Nutricion paginable", SaludIndNutriException.LAYER_SERVICE, SaludIndNutriException.ACTION_SELECT);
            logger.error(ExceptionServiceCode.GROUP + "- Error al tratar de seleccionar lista Indicadores Nutricion paginable - CODE: {}-{}", sin.getExceptionCode(), ex);
            throw sin;
        }

    }

}
