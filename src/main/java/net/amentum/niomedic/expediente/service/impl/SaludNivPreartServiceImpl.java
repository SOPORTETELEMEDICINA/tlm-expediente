package net.amentum.niomedic.expediente.service.impl;


import net.amentum.niomedic.expediente.converter.SaludNivPreartConverter;
import net.amentum.niomedic.expediente.exception.ExceptionServiceCode;
import net.amentum.niomedic.expediente.exception.SaludNivPreartException;
import net.amentum.niomedic.expediente.model.SaludNivPreart;
import net.amentum.niomedic.expediente.persistence.SaludNivPreartRepository;
import net.amentum.niomedic.expediente.service.SaludNivPreartService;
import net.amentum.niomedic.expediente.views.SaludNivPreartView;
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

@Service
@Transactional(readOnly = true)
public class SaludNivPreartServiceImpl implements SaludNivPreartService {

    private final Logger logger = LoggerFactory.getLogger(SaludNivPreartServiceImpl.class);
    private final Map<String, Object> colOrderNames = new HashMap<>();
    private SaludNivPreartRepository SaludNivPreartRepository;
    private net.amentum.niomedic.expediente.converter.SaludNivPreartConverter SaludNivPreartConverter;


    {
        colOrderNames.put("idnivelpa","idnivelpa");
        colOrderNames.put("medidfk","medidfk");
        colOrderNames.put("pacidfk","pacidfk");
        colOrderNames.put("paperiodo","paperiodo");
        colOrderNames.put("pafechahora","pafechahora");
        colOrderNames.put("pasysmedida","pasysmedida");
    }

    @Autowired
    public void setSaludNivPreartRepository(SaludNivPreartRepository SaludNivPreartRepository) {
        this.SaludNivPreartRepository = SaludNivPreartRepository;
    }

    @Autowired
    public void setSaludNivPreartConverter(SaludNivPreartConverter SaludNivPreartConverter) {
        this.SaludNivPreartConverter = SaludNivPreartConverter;
    }

    @Transactional(readOnly = false, rollbackFor = {SaludNivPreartException.class})
    @Override
    public void createSaludNivPreart(SaludNivPreartView SaludNivPreartView) throws SaludNivPreartException {
        try {
            SaludNivPreart SaludNivPreart = SaludNivPreartConverter.toEntity(SaludNivPreartView, new SaludNivPreart(), Boolean.FALSE);
            logger.debug("Insertar nuevo Niveles Presion Arterial: {}", SaludNivPreart);
            SaludNivPreartRepository.save(SaludNivPreart);
        } catch (DataIntegrityViolationException dive) {
            SaludNivPreartException ncE = new SaludNivPreartException("No fue posible agregar Niveles Presion Arterial", SaludNivPreartException.LAYER_DAO, SaludNivPreartException.ACTION_INSERT);
            ncE.addError("Ocurrio un error al agregar Niveles Presion Arterial");
            logger.error("Error al insertar nuevo Niveles Presion Arterial - CODE {} - {}", ncE.getExceptionCode(), SaludNivPreartView, ncE);
            throw ncE;
        } catch (Exception ex) {
            SaludNivPreartException ncE = new SaludNivPreartException("Error inesperado al agregar Niveles Presion Arterial", SaludNivPreartException.LAYER_DAO, SaludNivPreartException.ACTION_INSERT);
            ncE.addError("Ocurrió un error al agregar Niveles Presion Arterial");
            logger.error("Error al insertar nuevo Niveles Presion Arterial - CODE {} - {}", ncE.getExceptionCode(), SaludNivPreartView, ncE);
            throw ncE;
        }
    }

    @Transactional(readOnly = false, rollbackFor = {SaludNivPreartException.class})
    @Override
    public void updateSaludNivPreart(SaludNivPreartView SaludNivPreartView) throws SaludNivPreartException {
        try {
            if (SaludNivPreartRepository.findByPacidfk(SaludNivPreartView.getPacidfk()) == null) {
                SaludNivPreartException ncE = new SaludNivPreartException("No se encuentra en el sistema Niveles Presion Arterial.", SaludNivPreartException.LAYER_DAO, SaludNivPreartException.ACTION_VALIDATE);
                ncE.addError("Niveles Presion Arterial no encontrado");
                throw ncE;
            }
            SaludNivPreart SaludNivPreart = SaludNivPreartRepository.findByPacidfk(SaludNivPreartView.getPacidfk());
            SaludNivPreart = SaludNivPreartConverter.toEntity(SaludNivPreartView, SaludNivPreart, Boolean.TRUE);
            logger.debug("Editar Niveles Presion Arterial: {}", SaludNivPreart);
            SaludNivPreartRepository.save(SaludNivPreart);
        } catch (DataIntegrityViolationException dive) {
            SaludNivPreartException ncE = new SaludNivPreartException("No fue posible editar Niveles Presion Arterial", SaludNivPreartException.LAYER_DAO, SaludNivPreartException.ACTION_UPDATE);
            ncE.addError("Ocurrió un error al editar Niveles Presion Arterial");
            logger.error("Error al editar Niveles Presion Arterial - CODE {} - {}", ncE.getExceptionCode(), SaludNivPreartView, ncE);
            throw ncE;
        } catch (Exception ex) {
            SaludNivPreartException ncE = new SaludNivPreartException("Error inesperado al editar Niveles Presion Arterial", SaludNivPreartException.LAYER_DAO, SaludNivPreartException.ACTION_UPDATE);
            ncE.addError("Ocurrió un error al editar Niveles Presion Arterial");
            logger.error("Error al editar Niveles Presion Arterial - CODE {} - {}", ncE.getExceptionCode(), SaludNivPreartView, ncE);
            throw ncE;
        }
    }

    @Transactional(readOnly = false, rollbackFor = {SaludNivPreartException.class})
    @Override
    public void deleteSaludNivPreart(String pacidfk) throws SaludNivPreartException {
        try {
            if (SaludNivPreartRepository.findByPacidfk(pacidfk) == null) {
                SaludNivPreartException ncE = new SaludNivPreartException("No se encuentra en el sistema Niveles Presion Arterial.", SaludNivPreartException.LAYER_DAO, SaludNivPreartException.ACTION_VALIDATE);
                ncE.addError("Niveles Presion Arterial no encontrado");
                throw ncE;
            }
            SaludNivPreart SaludNivPreart = SaludNivPreartRepository.findByPacidfk(pacidfk);
            SaludNivPreartRepository.save(SaludNivPreart);
        } catch (DataIntegrityViolationException dive) {
            SaludNivPreartException ncE = new SaludNivPreartException("No fue posible editar Niveles Presion Arterial", SaludNivPreartException.LAYER_DAO, SaludNivPreartException.ACTION_UPDATE);
            ncE.addError("Ocurrió un error al eliminar Niveles Presion Arterial");
            logger.error("Error al eliminar Niveles Presion Arterial - CODE {} - {}", ncE.getExceptionCode(), pacidfk, ncE);
            throw ncE;
        } catch (Exception ex) {
            SaludNivPreartException ncE = new SaludNivPreartException("Error inesperado al eliminar Niveles Presion Arterial", SaludNivPreartException.LAYER_DAO, SaludNivPreartException.ACTION_UPDATE);
            ncE.addError("Ocurrió un error al eliminar Niveles Presion Arterial");
            logger.error("Error al eliminar Niveles Presion Arterial - CODE {} - {}", ncE.getExceptionCode(), pacidfk, ncE);
            throw ncE;
        }
    }


    @Override
    public List<SaludNivPreartView> findAll() throws SaludNivPreartException {
        try {
            List<SaludNivPreart> SaludNivPreartList = SaludNivPreartRepository.findAll();
            List<SaludNivPreartView> SaludNivPreartViewList = new ArrayList<>();
            for (SaludNivPreart cdl : SaludNivPreartList) {
                SaludNivPreartViewList.add(SaludNivPreartConverter.toView(cdl, Boolean.TRUE));
            }
            return SaludNivPreartViewList;
        } catch (Exception ex) {
            SaludNivPreartException ncE = new SaludNivPreartException("Ocurrio un error al seleccionar lista Niveles Presion Arterial", SaludNivPreartException.LAYER_SERVICE, SaludNivPreartException.ACTION_SELECT);
            logger.error(ExceptionServiceCode.GROUP + "- Error al tratar de seleccionar lista Niveles Presion Arterial - CODE: {}-{}", ncE.getExceptionCode(), ex);
            throw ncE;
        }
    }

    @Override
    public Page<SaludNivPreartView> getSaludNivPreartSearch(String pacidfk, Integer page, Integer size, String orderColumn, String orderType) throws SaludNivPreartException {
        try {
            logger.info("===>>>getSaludNivPreartPage(): - pacidfk: {} - page: {} - size: {} - orderColumn: {} - orderType: {}",
                    pacidfk, page, size, orderColumn, orderType);

            if (pacidfk == null) {
                logger.error("===>>>pacidfk viene NULO/VACIO: {}", pacidfk);
                SaludNivPreartException pesoE = new SaludNivPreartException("No se encuentra en el sistema SaludNivPreart", SaludNivPreartException.LAYER_DAO, SaludNivPreartException.ACTION_VALIDATE);
                pesoE.addError("pacidfk viene NULO/VACIO: " + pacidfk);
                throw pesoE;
            }
            List<SaludNivPreartView> SaludNivPreartViewList = new ArrayList<>();
            Page<SaludNivPreart> SaludNivPreartPage = null;
            Sort sort = new Sort(Sort.Direction.DESC, (String) colOrderNames.get("idnivelpa"));
            if (orderColumn != null && orderType != null) {
                if (orderType.equalsIgnoreCase("asc"))
                    sort = new Sort(Sort.Direction.ASC, (String) colOrderNames.get(orderColumn));
                else
                    sort = new Sort(Sort.Direction.DESC, (String) colOrderNames.get(orderColumn));
            }
            PageRequest request = new PageRequest(page, size, sort);
            final String patternSearch = pacidfk;
            Specifications<SaludNivPreart> spec = Specifications.where(
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
                SaludNivPreartPage = SaludNivPreartRepository.findAll(request);
            } else {
                SaludNivPreartPage = SaludNivPreartRepository.findAll(spec, request);
            }

            SaludNivPreartPage.getContent().forEach(SaludNivPreart -> {
                SaludNivPreartViewList.add(SaludNivPreartConverter.toView(SaludNivPreart, Boolean.FALSE));
            });
            PageImpl<SaludNivPreartView> SaludNivPreartViewPage = new PageImpl<SaludNivPreartView>(SaludNivPreartViewList, request, SaludNivPreartPage.getTotalElements());
            return SaludNivPreartViewPage;
        } catch (SaludNivPreartException pesoE) {
            throw pesoE;
        } catch (IllegalArgumentException iae) {
            logger.error("===>>>Algun parametro no es correcto");
            SaludNivPreartException pe = new SaludNivPreartException("Algun parametro no es correcto:", SaludNivPreartException.LAYER_SERVICE, SaludNivPreartException.ACTION_VALIDATE);
            pe.addError("Puede que sea null, vacio o valor incorrecto");
            throw pe;
        } catch (Exception ex) {
            SaludNivPreartException pesoE = new SaludNivPreartException("Ocurrio un error al seleccionar lista SaludNivPreart paginable", SaludNivPreartException.LAYER_SERVICE, SaludNivPreartException.ACTION_SELECT);
            logger.error(ExceptionServiceCode.GROUP + "===>>>Error al tratar de seleccionar lista SaludNivPreart paginable - CODE: {}", pesoE.getExceptionCode(), ex);
            throw pesoE;
        }


    }


    @Override
    public Page<SaludNivPreartView> getSaludNivPreartfechaSearch(String pacidfk,int periodo,String fechaInicio, String fechaFin , Integer page, Integer size, String orderColumn, String orderType) throws SaludNivPreartException {
        try {
            logger.info("===>>>getSaludNivPreartfechaPage(): - pacidfk: {} - periodo: {} - Fechainicio: {} - Fechafin: {}- page: {} - size: {} - orderColumn: {} - orderType: {}",
                    pacidfk,periodo,fechaInicio, fechaFin, page, size, orderColumn, orderType);

            if (pacidfk == null) {
                logger.error("===>>>pacidfk viene NULO/VACIO: {}", pacidfk);
                SaludNivPreartException pesoE = new SaludNivPreartException("No se encuentra en el sistema SaludNivPreart", SaludNivPreartException.LAYER_DAO, SaludNivPreartException.ACTION_VALIDATE);
                pesoE.addError("pacidfk viene NULO/VACIO: " + pacidfk);
                throw pesoE;
            }

            List<SaludNivPreartView> SaludNivPreartViewList = new ArrayList<>();
            Page<SaludNivPreart> SaludNivPreartfechaPage = null;
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

            Specifications<SaludNivPreart> spec = Specifications.where(
                    (root, query, cb) -> {
                        Predicate tc = null;
                        if (pacidfk != null)
                            tc = (tc != null ?
                                    cb.and(tc, cb.equal(root.get("pacidfk"), patternSearch)) : cb.equal(root.get("pacidfk"), patternSearch));
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
                                        cb.greaterThanOrEqualTo(root.get("pafechahora"), start),
                                        cb.lessThan(root.get("pafechahora"), endExclusive))
                                        : cb.and(
                                        cb.greaterThanOrEqualTo(root.get("pafechahora"), start),
                                        cb.lessThan(root.get("pafechahora"), endExclusive));

                            } catch (Exception ex) {
                                logger.warn("Error al convertir fechas", ex);
                            }
                        }
                        if (periodo != 0) {
                            logger.info("parametros:  - periodo: {}",
                                    periodo);
                            if(periodo==8) {
                                int periodofecha =1;
                                int periodofecha2=9;
                                tc = (tc != null) ?
                                        cb.and(tc, cb.greaterThanOrEqualTo(root.get("paperiodo"), periodofecha), cb.lessThanOrEqualTo(root.get("paperiodo"), periodofecha2)) :
                                        cb.and(cb.greaterThanOrEqualTo(root.get("paperiodo"), periodofecha), cb.lessThanOrEqualTo(root.get("paperiodo"), periodofecha2));
                            }
                            else
                                tc = (tc != null) ?
                                        cb.and(tc, cb.greaterThanOrEqualTo(root.get("paperiodo"), periodo), cb.lessThanOrEqualTo(root.get("paperiodo"), periodo)) :
                                        cb.and(cb.greaterThanOrEqualTo(root.get("paperiodo"), periodo), cb.lessThanOrEqualTo(root.get("paperiodo"), periodo));
                        }
                        return tc;
                    }
            );

            if (spec == null) {
                SaludNivPreartfechaPage = SaludNivPreartRepository.findAll(request);
            } else {
                SaludNivPreartfechaPage = SaludNivPreartRepository.findAll(spec, request);
            }

            SaludNivPreartfechaPage.getContent().forEach(SaludNivPreart -> {
                SaludNivPreartViewList.add(SaludNivPreartConverter.toView(SaludNivPreart, Boolean.FALSE));
            });
            PageImpl<SaludNivPreartView> SaludNivPreartViewPage = new PageImpl<SaludNivPreartView>(SaludNivPreartViewList, request, SaludNivPreartfechaPage.getTotalElements());
            return SaludNivPreartViewPage;
        } catch (SaludNivPreartException pesoE) {
            throw pesoE;
        } catch (IllegalArgumentException iae) {
            logger.error("===>>>Algun parametro no es correcto");
            SaludNivPreartException pe = new SaludNivPreartException("Algun parametro no es correcto:", SaludNivPreartException.LAYER_SERVICE, SaludNivPreartException.ACTION_VALIDATE);
            pe.addError("Puede que sea null, vacio o valor incorrecto");
            throw pe;
        } catch (Exception ex) {
            SaludNivPreartException pesoE = new SaludNivPreartException("Ocurrio un error al seleccionar lista SaludNivPreart paginable", SaludNivPreartException.LAYER_SERVICE, SaludNivPreartException.ACTION_SELECT);
            logger.error(ExceptionServiceCode.GROUP + "===>>>Error al tratar de seleccionar lista SaludNivPreart paginable - CODE: {}", pesoE.getExceptionCode(), ex);
            throw pesoE;
        }
    }
}
