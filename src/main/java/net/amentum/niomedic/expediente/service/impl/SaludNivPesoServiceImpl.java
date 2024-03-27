package net.amentum.niomedic.expediente.service.impl;



import net.amentum.common.TimeUtils;
import net.amentum.niomedic.expediente.converter.SaludNivPesoConverter;
import net.amentum.niomedic.expediente.exception.ExceptionServiceCode;
import net.amentum.niomedic.expediente.exception.SaludNivPesoException;
import net.amentum.niomedic.expediente.model.SaludNivPeso;
import net.amentum.niomedic.expediente.persistence.SaludNivPesoRepository;
import net.amentum.niomedic.expediente.service.SaludNivPesoService;
import net.amentum.niomedic.expediente.views.SaludNivPesoView;
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

import java.text.SimpleDateFormat;
import java.util.*;

import static net.amentum.common.TimeUtils.parseDate;


@Service
@Transactional(readOnly = true)
public class SaludNivPesoServiceImpl implements SaludNivPesoService {

    private final Logger logger = LoggerFactory.getLogger(SaludNivPesoServiceImpl.class);
    private final Map<String, Object> colOrderNames = new HashMap<>();
    private SaludNivPesoRepository SaludNivPesoRepository;
    private SaludNivPesoConverter SaludNivPesoConverter;


    {
        colOrderNames.put("idnivelpeso","idnivelpeso");
        colOrderNames.put("medidfk","medidfk");
        colOrderNames.put("pacidfk","pacidfk");
        colOrderNames.put("pesoperiodo","pesoperiodo");
        colOrderNames.put("pesofechahora","pesofechahora");
        colOrderNames.put("pesomedida","pesomedida");
    }

    @Autowired
    public void setSaludNivPesoRepository(SaludNivPesoRepository SaludNivPesoRepository) {
        this.SaludNivPesoRepository = SaludNivPesoRepository;
    }

    @Autowired
    public void setSaludNivPesoConverter(SaludNivPesoConverter SaludNivPesoConverter) {
        this.SaludNivPesoConverter = SaludNivPesoConverter;
    }

    @Transactional(readOnly = false, rollbackFor = {SaludNivPesoException.class})
    @Override
    public void createSaludNivPeso(SaludNivPesoView SaludNivPesoView) throws SaludNivPesoException {
        try {
                 SaludNivPeso SaludNivPeso = SaludNivPesoConverter.toEntity(SaludNivPesoView, new SaludNivPeso(), Boolean.FALSE);
                logger.debug("Insertar nuevo Niveles Peso: {}", SaludNivPeso);
                SaludNivPesoRepository.save(SaludNivPeso);

        } catch (DataIntegrityViolationException dive) {
            SaludNivPesoException ncE = new SaludNivPesoException("No fue posible agregar Niveles Peso", SaludNivPesoException.LAYER_DAO, SaludNivPesoException.ACTION_INSERT);
            ncE.addError("Ocurrio un error al agregar Niveles Peso");
            logger.error("Error al insertar nuevo Niveles Peso - CODE {} - {}", ncE.getExceptionCode(), SaludNivPesoView, ncE);
            throw ncE;
        } catch (Exception ex) {
            SaludNivPesoException ncE = new SaludNivPesoException("Error inesperado al agregar Niveles Peso", SaludNivPesoException.LAYER_DAO, SaludNivPesoException.ACTION_INSERT);
            ncE.addError("Ocurrió un error al agregar Niveles Peso");
            logger.error("Error al insertar nuevo Niveles Peso - CODE {} - {}", ncE.getExceptionCode(), SaludNivPesoView, ncE);
            throw ncE;
        }
    }

    @Transactional(readOnly = false, rollbackFor = {SaludNivPesoException.class})
    @Override
    public void updateSaludNivPeso(SaludNivPesoView SaludNivPesoView) throws SaludNivPesoException {
        try {
           if (SaludNivPesoRepository.findByPacidfk(SaludNivPesoView.getPacidfk()) == null) {
                SaludNivPesoException ncE = new SaludNivPesoException("No se encuentra en el sistema Niveles Peso.", SaludNivPesoException.LAYER_DAO, SaludNivPesoException.ACTION_VALIDATE);
                ncE.addError("Niveles Peso no encontrado");
                throw ncE;
          }
            SaludNivPeso SaludNivPeso = SaludNivPesoRepository.findByPacidfk(SaludNivPesoView.getPacidfk());
            SaludNivPeso = SaludNivPesoConverter.toEntity(SaludNivPesoView, SaludNivPeso, Boolean.TRUE);
            logger.debug("Editar Niveles Peso: {}", SaludNivPeso);
            SaludNivPesoRepository.save(SaludNivPeso);
        } catch (DataIntegrityViolationException dive) {
            SaludNivPesoException ncE = new SaludNivPesoException("No fue posible editar Niveles Peso", SaludNivPesoException.LAYER_DAO, SaludNivPesoException.ACTION_UPDATE);
            ncE.addError("Ocurrió un error al editar Niveles Peso");
            logger.error("Error al editar Niveles Peso - CODE {} - {}", ncE.getExceptionCode(), SaludNivPesoView, ncE);
            throw ncE;
        } catch (Exception ex) {
            SaludNivPesoException ncE = new SaludNivPesoException("Error inesperado al editar Niveles Peso", SaludNivPesoException.LAYER_DAO, SaludNivPesoException.ACTION_UPDATE);
            ncE.addError("Ocurrió un error al editar Niveles Peso");
            logger.error("Error al editar Niveles Peso - CODE {} - {}", ncE.getExceptionCode(), SaludNivPesoView, ncE);
            throw ncE;
        }
    }

    @Transactional(readOnly = false, rollbackFor = {SaludNivPesoException.class})
    @Override
    public void deleteSaludNivPeso(String pacidfk) throws SaludNivPesoException {
        try {
            if (SaludNivPesoRepository.findByPacidfk(pacidfk) == null) {
                SaludNivPesoException ncE = new SaludNivPesoException("No se encuentra en el sistema Niveles Peso.", SaludNivPesoException.LAYER_DAO, SaludNivPesoException.ACTION_VALIDATE);
                ncE.addError("Niveles Peso no encontrado");
                throw ncE;
            }
            SaludNivPeso SaludNivPeso = SaludNivPesoRepository.findByPacidfk(pacidfk);
            SaludNivPesoRepository.save(SaludNivPeso);
        } catch (DataIntegrityViolationException dive) {
            SaludNivPesoException ncE = new SaludNivPesoException("No fue posible editar Niveles Peso", SaludNivPesoException.LAYER_DAO, SaludNivPesoException.ACTION_UPDATE);
            ncE.addError("Ocurrió un error al eliminar Niveles Peso");
            logger.error("Error al eliminar Niveles Peso - CODE {} - {}", ncE.getExceptionCode(), pacidfk, ncE);
            throw ncE;
        } catch (Exception ex) {
            SaludNivPesoException ncE = new SaludNivPesoException("Error inesperado al eliminar Niveles Peso", SaludNivPesoException.LAYER_DAO, SaludNivPesoException.ACTION_UPDATE);
            ncE.addError("Ocurrió un error al eliminar Niveles Peso");
            logger.error("Error al eliminar Niveles Peso - CODE {} - {}", ncE.getExceptionCode(), pacidfk, ncE);
            throw ncE;
        }
    }


    @Override
    public List<SaludNivPesoView> findAll() throws SaludNivPesoException {
        try {
            List<SaludNivPeso> SaludNivPesoList = SaludNivPesoRepository.findAll();
            List<SaludNivPesoView> SaludNivPesoViewList = new ArrayList<>();
            for (SaludNivPeso cdl : SaludNivPesoList) {
                SaludNivPesoViewList.add(SaludNivPesoConverter.toView(cdl, Boolean.TRUE));
            }
            return SaludNivPesoViewList;
        } catch (Exception ex) {
            SaludNivPesoException ncE = new SaludNivPesoException("Ocurrio un error al seleccionar lista Niveles Peso", SaludNivPesoException.LAYER_SERVICE, SaludNivPesoException.ACTION_SELECT);
            logger.error(ExceptionServiceCode.GROUP + "- Error al tratar de seleccionar lista Niveles Peso - CODE: {}-{}", ncE.getExceptionCode(), ex);
            throw ncE;
        }
    }

    @Override
    public Page<SaludNivPesoView> getSaludNivPesoSearch(String pacidfk, Integer page, Integer size, String orderColumn, String orderType) throws SaludNivPesoException {
        try {
            logger.info("===>>>getSaludNivPesoPage(): - pacidfk: {} - page: {} - size: {} - orderColumn: {} - orderType: {}",
                    pacidfk, page, size, orderColumn, orderType);
            if (pacidfk == null) {
                logger.error("===>>>pacidfk viene NULO/VACIO: {}", pacidfk);
                SaludNivPesoException pesoE = new SaludNivPesoException("No se encuentra en el sistema SaludNivPeso", SaludNivPesoException.LAYER_DAO, SaludNivPesoException.ACTION_VALIDATE);
                pesoE.addError("pacidfk viene NULO/VACIO: " + pacidfk);
                throw pesoE;
            }
            List<SaludNivPesoView> SaludNivPesoViewList = new ArrayList<>();
            Page<SaludNivPeso> SaludNivPesoPage = null;
            Sort sort = new Sort(Sort.Direction.DESC, (String) colOrderNames.get("idnivelpeso"));
            if (orderColumn != null && orderType != null) {
                if (orderType.equalsIgnoreCase("asc"))
                    sort = new Sort(Sort.Direction.ASC, (String) colOrderNames.get(orderColumn));
                else
                    sort = new Sort(Sort.Direction.DESC, (String) colOrderNames.get(orderColumn));
            }
            PageRequest request = new PageRequest(page, size, sort);
            final String patternSearch = pacidfk;
            Specifications<SaludNivPeso> spec = Specifications.where(
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
                SaludNivPesoPage = SaludNivPesoRepository.findAll(request);
            } else {
                SaludNivPesoPage = SaludNivPesoRepository.findAll(spec, request);
            }
            SaludNivPesoPage.getContent().forEach(SaludNivPeso -> {
                SaludNivPesoViewList.add(SaludNivPesoConverter.toView(SaludNivPeso, Boolean.FALSE));
            });
            PageImpl<SaludNivPesoView> SaludNivPesoViewPage = new PageImpl<SaludNivPesoView>(SaludNivPesoViewList, request, SaludNivPesoPage.getTotalElements());
            return SaludNivPesoViewPage;
        } catch (SaludNivPesoException pesoE) {
            throw pesoE;
        } catch (IllegalArgumentException iae) {
            logger.error("===>>>Algun parametro no es correcto");
            SaludNivPesoException pe = new SaludNivPesoException("Algun parametro no es correcto:", SaludNivPesoException.LAYER_SERVICE, SaludNivPesoException.ACTION_VALIDATE);
            pe.addError("Puede que sea null, vacio o valor incorrecto");
            throw pe;
        } catch (Exception ex) {
            SaludNivPesoException pesoE = new SaludNivPesoException("Ocurrio un error al seleccionar lista SaludNivPeso paginable", SaludNivPesoException.LAYER_SERVICE, SaludNivPesoException.ACTION_SELECT);
            logger.error(ExceptionServiceCode.GROUP + "===>>>Error al tratar de seleccionar lista SaludNivPeso paginable - CODE: {}", pesoE.getExceptionCode(), ex);
            throw pesoE;
        }


    }


    @Override
    public Page<SaludNivPesoView> getSaludNivPesofechaSearch(String pacidfk,int periodo,String fechaInicio, String fechaFin , Integer page, Integer size, String orderColumn, String orderType) throws SaludNivPesoException {
        try {
            logger.info("===>>>getSaludNivPesofechaPage(): - pacidfk: {} - periodo: {} - Fechainicio: {} - Fechafin: {}- page: {} - size: {} - orderColumn: {} - orderType: {}",
                    pacidfk,periodo,fechaFin, page, size, orderColumn, orderType);

            if (pacidfk == null) {
                logger.error("===>>>pacidfk viene NULO/VACIO: {}", pacidfk);
                SaludNivPesoException pesoE = new SaludNivPesoException("No se encuentra en el sistema SaludNivPeso", SaludNivPesoException.LAYER_DAO, SaludNivPesoException.ACTION_VALIDATE);
                pesoE.addError("pacidfk viene NULO/VACIO: " + pacidfk);
                throw pesoE;
            }

            List<SaludNivPesoView> SaludNivPesoViewList = new ArrayList<>();
            Page<SaludNivPeso> SaludNivPesofechaPage = null;
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

            Specifications<SaludNivPeso> spec = Specifications.where(
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
                                Date finalDate = sdf.parse(fechaFin);

                                tc = (tc != null) ?
                                        cb.and(tc, cb.greaterThanOrEqualTo(root.get("pesofechahora"), inicialDate), cb.lessThanOrEqualTo(root.get("pesofechahora"), finalDate)) :
                                        cb.and(cb.greaterThanOrEqualTo(root.get("pesofechahora"), inicialDate), cb.lessThanOrEqualTo(root.get("pesofechahora"), finalDate));
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
                                        cb.and(tc, cb.greaterThanOrEqualTo(root.get("pesoperiodo"), periodofecha), cb.lessThanOrEqualTo(root.get("pesoperiodo"), periodofecha2)) :
                                        cb.and(cb.greaterThanOrEqualTo(root.get("pesoperiodo"), periodofecha), cb.lessThanOrEqualTo(root.get("pesoperiodo"), periodofecha2));


                            }

                            else{
                                tc = (tc != null) ?
                                        cb.and(tc, cb.greaterThanOrEqualTo(root.get("pesoperiodo"), periodo), cb.lessThanOrEqualTo(root.get("pesoperiodo"), periodo)) :
                                        cb.and(cb.greaterThanOrEqualTo(root.get("pesoperiodo"), periodo), cb.lessThanOrEqualTo(root.get("pesoperiodo"), periodo));
                            }



                        }
                        //               if (active != null) {
//                  tc = (tc != null ? cb.and(tc, cb.equal(root.get("activo"), active)) : cb.equal(root.get("activo"), active));
//               }
                       return tc;
                    }
            );

            if (spec == null) {
                SaludNivPesofechaPage = SaludNivPesoRepository.findAll(request);
            } else {
                SaludNivPesofechaPage = SaludNivPesoRepository.findAll(spec, request);
            }

            SaludNivPesofechaPage.getContent().forEach(SaludNivPeso -> {
                SaludNivPesoViewList.add(SaludNivPesoConverter.toView(SaludNivPeso, Boolean.FALSE));
            });
            PageImpl<SaludNivPesoView> SaludNivPesoViewPage = new PageImpl<SaludNivPesoView>(SaludNivPesoViewList, request, SaludNivPesofechaPage.getTotalElements());
            return SaludNivPesoViewPage;
        } catch (SaludNivPesoException pesoE) {
            throw pesoE;
        } catch (IllegalArgumentException iae) {
            logger.error("===>>>Algun parametro no es correcto");
            SaludNivPesoException pe = new SaludNivPesoException("Algun parametro no es correcto:", SaludNivPesoException.LAYER_SERVICE, SaludNivPesoException.ACTION_VALIDATE);
            pe.addError("Puede que sea null, vacio o valor incorrecto");
            throw pe;
        } catch (Exception ex) {
            SaludNivPesoException pesoE = new SaludNivPesoException("Ocurrio un error al seleccionar lista SaludNivPeso paginable", SaludNivPesoException.LAYER_SERVICE, SaludNivPesoException.ACTION_SELECT);
            logger.error(ExceptionServiceCode.GROUP + "===>>>Error al tratar de seleccionar lista SaludNivPeso paginable - CODE: {}", pesoE.getExceptionCode(), ex);
            throw pesoE;
        }


    }

  }
