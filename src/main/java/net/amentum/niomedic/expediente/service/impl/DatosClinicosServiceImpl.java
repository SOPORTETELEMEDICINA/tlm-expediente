package net.amentum.niomedic.expediente.service.impl;

import net.amentum.niomedic.expediente.exception.ExceptionServiceCode;
import net.amentum.niomedic.expediente.service.DatosClinicosService;
import net.amentum.niomedic.expediente.converter.DatosClinicosConverter;
import net.amentum.niomedic.expediente.exception.DatosClinicosException;
import net.amentum.niomedic.expediente.model.DatosClinicos;

import net.amentum.niomedic.expediente.persistence.DatosClinicosRepository;

import net.amentum.niomedic.expediente.views.DatosClinicosView;
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
public class DatosClinicosServiceImpl implements DatosClinicosService {
    private final Logger logger = LoggerFactory.getLogger(DatosClinicosServiceImpl.class);
    private final Map<String, Object> colOrderNames = new HashMap<>();
    private DatosClinicosRepository datosClinicosRepository;
    private DatosClinicosConverter datosClinicosConverter;


    {
        colOrderNames.put("creadoPor", "creadoPor");
        colOrderNames.put("ultimModificacion", "ultimModificacion");
        colOrderNames.put("fechaCreacion", "fechaCreacion");
        colOrderNames.put("idPaciente", "idPaciente");
    }

    @Autowired
    public void setDatosClinicosRepository(DatosClinicosRepository datosClinicosRepository) {
        this.datosClinicosRepository = datosClinicosRepository;
    }

    @Autowired
    public void setDatosClinicosConverter(DatosClinicosConverter datosClinicosConverter) {
        this.datosClinicosConverter = datosClinicosConverter;
    }

    @Transactional(readOnly = false, rollbackFor = {DatosClinicosException.class})
    @Override
    public void createDatosClinicos(DatosClinicosView datosClinicosView) throws DatosClinicosException {
        try {
        	if (datosClinicosRepository.findByIdPaciente(datosClinicosView.getIdPaciente()) == null) {
        		DatosClinicos datosClinicos = datosClinicosConverter.toEntity(datosClinicosView, new DatosClinicos(), Boolean.FALSE);
        		logger.debug("Insertar nuevo Datos Clinicos: {}", datosClinicos);
        		datosClinicosRepository.save(datosClinicos);
        	} else {
        		logger.info("createDatosClinicos() - ya existe datos-clinicos para el pacienteId {}", datosClinicosView.getIdPaciente());
        	}   
        } catch (DataIntegrityViolationException dive) {
            DatosClinicosException dce = new DatosClinicosException("No fue posible agregar Datos Clinicos", DatosClinicosException.LAYER_DAO, DatosClinicosException.ACTION_INSERT);
            dce.addError("Ocurrio un error al agregar Datos Clinicos");
            logger.error("Error al insertar nuevo Datos Clinicos - CODE {} - {}", dce.getExceptionCode(), datosClinicosView, dce);
            throw dce;
        } catch (Exception ex) {
            DatosClinicosException dce = new DatosClinicosException("Error inesperado al agregar Datos Clinicos", DatosClinicosException.LAYER_DAO, DatosClinicosException.ACTION_INSERT);
            dce.addError("Ocurrió un error al agregar Datos Clinicos");
            logger.error("Error al insertar nuevo Datos Clinicos - CODE {} - {}", dce.getExceptionCode(), datosClinicosView, dce);
            throw dce;
        }
    }

    @Transactional(readOnly = false, rollbackFor = {DatosClinicosException.class})
    @Override
    public void updateDatosClinicos(DatosClinicosView datosClinicosView) throws DatosClinicosException {
        try {
            if (datosClinicosRepository.findByIdPaciente(datosClinicosView.getIdPaciente()) == null) {
                DatosClinicosException dce = new DatosClinicosException("No se encuentra en el sistema Datos Clinicos.", DatosClinicosException.LAYER_DAO, DatosClinicosException.ACTION_VALIDATE);
                dce.addError("Datos Clinicos no encontrado");
                throw dce;
            }
            DatosClinicos datosClinicos = datosClinicosRepository.findByIdPaciente(datosClinicosView.getIdPaciente());
            datosClinicos = datosClinicosConverter.toEntity(datosClinicosView, datosClinicos, Boolean.TRUE);
            logger.debug("Editar Datos Clinicos: {}", datosClinicos);
            datosClinicosRepository.save(datosClinicos);
        } catch (DataIntegrityViolationException dive) {
            DatosClinicosException dce = new DatosClinicosException("No fue posible editar Datos Clinicos", DatosClinicosException.LAYER_DAO, DatosClinicosException.ACTION_UPDATE);
            dce.addError("Ocurrió un error al editar Datos Clinicos");
            logger.error("Error al editar Datos Clinicos - CODE {} - {}", dce.getExceptionCode(), datosClinicosView, dce);
            throw dce;
        } catch (Exception ex) {
            DatosClinicosException dce = new DatosClinicosException("Error inesperado al editar Datos Clinicos", DatosClinicosException.LAYER_DAO, DatosClinicosException.ACTION_UPDATE);
            dce.addError("Ocurrió un error al editar Datos Clinicos");
            logger.error("Error al editar Datos Clinicos - CODE {} - {}", dce.getExceptionCode(), datosClinicosView, dce);
            throw dce;
        }
    }

    @Transactional(readOnly = false, rollbackFor = {DatosClinicosException.class})
    @Override
    public void deleteDatosClinicos(String idPaciente) throws DatosClinicosException {
        try {
            if (datosClinicosRepository.findByIdPaciente(idPaciente) == null) {
                DatosClinicosException dce = new DatosClinicosException("No se encuentra en el sistema Datos Clinicos.", DatosClinicosException.LAYER_DAO, DatosClinicosException.ACTION_VALIDATE);
                dce.addError("Datos Clinicos no encontrado");
                throw dce;
            }
            DatosClinicos datosClinicos = datosClinicosRepository.findByIdPaciente(idPaciente);
            datosClinicos.setActivo(Boolean.FALSE);
            datosClinicosRepository.save(datosClinicos);
        } catch (DataIntegrityViolationException dive) {
            DatosClinicosException dce = new DatosClinicosException("No fue posible editar Datos Clinicos", DatosClinicosException.LAYER_DAO, DatosClinicosException.ACTION_UPDATE);
            dce.addError("Ocurrió un error al eliminar Datos Clinicos");
            logger.error("Error al eliminar Datos Clinicos - CODE {} - {}", dce.getExceptionCode(), idPaciente, dce);
            throw dce;
        } catch (Exception ex) {
            DatosClinicosException dce = new DatosClinicosException("Error inesperado al eliminar Datos Clinicos", DatosClinicosException.LAYER_DAO, DatosClinicosException.ACTION_UPDATE);
            dce.addError("Ocurrió un error al eliminar Datos Clinicos");
            logger.error("Error al eliminar Datos Clinicos - CODE {} - {}", dce.getExceptionCode(), idPaciente, dce);
            throw dce;
        }
    }

    @Override
    public DatosClinicosView getDetailsByIdDatosClinicos(String idPaciente) throws DatosClinicosException {
        try {
            if (datosClinicosRepository.findByIdPaciente(idPaciente) == null) {
            	logger.error("No se encontró datos clínicos para el paciente con el ID: {}",idPaciente);
            	return null;
            }
            DatosClinicos datosClinicos = datosClinicosRepository.findByIdPaciente(idPaciente);
            return datosClinicosConverter.toView(datosClinicos, Boolean.TRUE);
        } catch (DataIntegrityViolationException dive) {
            DatosClinicosException dce = new DatosClinicosException("No fue posible editar Datos Clinicos", DatosClinicosException.LAYER_DAO, DatosClinicosException.ACTION_SELECT);
            dce.addError("No fue posible obtener detalles Datos Clinicos");
            logger.error("Error al obtener detalles Datos Clinicos - CODE {} - {}", dce.getExceptionCode(), idPaciente, dce);
            throw dce;
        } catch (Exception ex) {
            DatosClinicosException dce = new DatosClinicosException("Error inesperado al obtener detalles Datos Clinicos", DatosClinicosException.LAYER_DAO, DatosClinicosException.ACTION_SELECT);
            dce.addError("Ocurrió un error al obtener detalles Datos Clinicos");
            logger.error("Error al obtener detalles Datos Clinicos - CODE {} - {}", dce.getExceptionCode(), idPaciente, dce);
            throw dce;
        }
    }

    @Override
    public List<DatosClinicosView> findAll(Boolean active) throws DatosClinicosException {
        try {
            List<DatosClinicos> datosClinicosList = new ArrayList<>();
            List<DatosClinicosView> datosClinicosViewList = new ArrayList<>();
            if (active == null) {
                datosClinicosList = datosClinicosRepository.findAll();
            } else {
                datosClinicosList = datosClinicosRepository.findAll().stream().filter(datosClinicos -> datosClinicos.getActivo().compareTo(active) == 0).collect(Collectors.toList());
            }
            for (DatosClinicos cdl : datosClinicosList) {
                datosClinicosViewList.add(datosClinicosConverter.toView(cdl, Boolean.TRUE));
            }
            return datosClinicosViewList;
        } catch (Exception ex) {
            DatosClinicosException dce = new DatosClinicosException("Ocurrio un error al seleccionar lista Datos Clinicos", DatosClinicosException.LAYER_SERVICE, DatosClinicosException.ACTION_SELECT);
            logger.error(ExceptionServiceCode.GROUP + "- Error al tratar de seleccionar lista Datos Clinicos - CODE: {}-{}", dce.getExceptionCode(), ex);
            throw dce;
        }
    }

    @Override
    public Page<DatosClinicosView> getDatosClinicosPage(Boolean active, String name, Integer page, Integer size, String orderColumn, String orderType) throws DatosClinicosException {
        try {
            logger.info("- Obtener listado Datos Clinicos paginable: {} - page {} - size: {} - orderColumn: {} - orderType: {} - active: {}", name, page, size, orderColumn, orderType, active);
            List<DatosClinicosView> datosClinicosViewList = new ArrayList<>();
            Page<DatosClinicos> datosClinicosPage = null;
            Sort sort = new Sort(Sort.Direction.ASC, (String) colOrderNames.get("idPaciente"));

            if (orderColumn != null && orderType != null) {
                if (orderType.equalsIgnoreCase("asc")) {
                    sort = new Sort(Sort.Direction.ASC, (String) colOrderNames.get(orderColumn));
                } else {
                    sort = new Sort(Sort.Direction.DESC, (String) colOrderNames.get(orderColumn));
                }

            }
            PageRequest request = new PageRequest(page, size, sort);
            final String patternSearch = "%" + name.toLowerCase() + "%";
            Specifications<DatosClinicos> spec = Specifications.where(
                    (root, query, cb) -> {
                        Predicate tc = null;
                        if (name != null && !name.isEmpty()) {
                            tc = (tc != null ? cb.and(tc, cb.like(cb.lower(root.get("idPaciente")), patternSearch)) : cb.like(cb.lower(root.get("idPaciente")), patternSearch));
                        }
                        if (active != null) {
                            tc = (tc != null ? cb.and(tc, cb.equal(root.get("activo"), active)) : cb.equal(root.get("activo"), active));
                        }
                        return tc;
                    }
            );

            if (spec == null) {
                datosClinicosPage = datosClinicosRepository.findAll(request);
            } else {
                datosClinicosPage = datosClinicosRepository.findAll(spec, request);
            }

            datosClinicosPage.getContent().forEach(datosClinicos -> {
                datosClinicosViewList.add(datosClinicosConverter.toView(datosClinicos, Boolean.TRUE));
            });
            PageImpl<DatosClinicosView> datosClinicosViewPage = new PageImpl<DatosClinicosView>(datosClinicosViewList, request, datosClinicosPage.getTotalElements());
            return datosClinicosViewPage;
        } catch (Exception ex) {
            DatosClinicosException dce = new DatosClinicosException("Ocurrio un error al seleccionar lista Datos Clinicos paginable", DatosClinicosException.LAYER_SERVICE, DatosClinicosException.ACTION_SELECT);
            logger.error(ExceptionServiceCode.GROUP + "- Error al tratar de seleccionar lista Datos Clinicos paginable - CODE: {}-{}", dce.getExceptionCode(), ex);
            throw dce;
        }
    }


}
