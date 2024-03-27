package net.amentum.niomedic.expediente.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import net.amentum.common.TimeUtils;
import net.amentum.niomedic.catalogos.views.CatEstadoConsultaView;
import net.amentum.niomedic.expediente.converter.ConsultaConverter;
import net.amentum.niomedic.expediente.converter.TratamientoConverter;
import net.amentum.niomedic.expediente.exception.ConsultaException;
import net.amentum.niomedic.expediente.exception.ExceptionServiceCode;
import net.amentum.niomedic.expediente.model.CatCie10;
import net.amentum.niomedic.expediente.model.Consulta;
import net.amentum.niomedic.expediente.model.Padecimiento;
import net.amentum.niomedic.expediente.model.Tratamiento;
import net.amentum.niomedic.expediente.persistence.CatCie10Repository;
import net.amentum.niomedic.expediente.persistence.CatCie9Repository;
import net.amentum.niomedic.expediente.persistence.ConsultaRepository;
import net.amentum.niomedic.expediente.persistence.PadecimientoRepository;
import net.amentum.niomedic.expediente.persistence.TratamientoRepository;
import net.amentum.niomedic.expediente.rest.ConsultaRest;
import net.amentum.niomedic.expediente.service.ConsultaService;
import net.amentum.niomedic.expediente.views.ConsultaView;
import net.amentum.niomedic.expediente.views.PadecimientoView;
import net.amentum.niomedic.expediente.views.SignosVitalesView;
import net.amentum.niomedic.expediente.views.TratamientoView;
import org.hibernate.exception.ConstraintViolationException;
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
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static net.amentum.common.TimeUtils.parseDate;


@Service
@Transactional(readOnly = true)
public class ConsultaServiceImpl implements ConsultaService {
	private final Logger logger = LoggerFactory.getLogger(ConsultaServiceImpl.class);
	private final Map<String, Object> colOrderNames = new HashMap<>();
	private ConsultaRepository consultaRepository;
	private ConsultaConverter consultaConverter;
	private PadecimientoRepository padecimientoRepository;
	private PadecimientoServiceImpl padecimientoServiceImpl;
	private CatCie10Repository catCie10Repository;
	private CatCie9Repository catCie9Repository;
	private TratamientoConverter tratamientoConverter;
	private TratamientoRepository tratamientoRepository;

	{
		colOrderNames.put("idPaciente", "idPaciente");
		colOrderNames.put("fechaCreacion", "fechaCreacion");
		colOrderNames.put("fechaConsulta", "fechaConsulta");
		colOrderNames.put("creadoPor", "creadoPor");
		colOrderNames.put("tipoConsulta", "tipoConsulta");
		colOrderNames.put("estadoConsulta", "estadoConsulta");
	}

	private final Map<String, Object> colOrderSearch = new HashMap<>();

	{
		colOrderSearch.put("idConsulta", "idConsulta");
		colOrderSearch.put("idPaciente", "idPaciente");
		colOrderSearch.put("nombrePaciente", "nombrePaciente");
		colOrderSearch.put("idMedico", "idMedico");
		colOrderSearch.put("nombreMedico", "nombreMedico");
		colOrderSearch.put("fechaConsulta", "fechaConsulta");
		colOrderSearch.put("idEstadoConsulta", "idEstadoConsulta");
		colOrderSearch.put("estadoConsulta", "estadoConsulta");
		colOrderSearch.put("idTipoConsulta", "idTipoConsulta");
		colOrderSearch.put("tipoConsulta", "tipoConsulta");
		colOrderSearch.put("canal", "canal");
		colOrderSearch.put("motivoConsulta", "motivoConsulta");
		colOrderSearch.put("numeroConsulta", "numeroConsulta");
		colOrderSearch.put("especialidad", "especialidad");
	}

	@Autowired
	public void setTratamientoRepository(TratamientoRepository tratamientoRepository) {
		this.tratamientoRepository = tratamientoRepository;
	}

	@Autowired
	public void setTrataminetoConverter(TratamientoConverter tratamientoConverter) {
		this.tratamientoConverter = tratamientoConverter;
	}

	@Autowired
	public void setCatCie9Repository(CatCie9Repository catCie9Repository) {
		this.catCie9Repository = catCie9Repository;
	}

	@Autowired
	public void setConsultaRepository(ConsultaRepository consultaRepository) {
		this.consultaRepository = consultaRepository;
	}

	@Autowired
	public void setConsultaConverter(ConsultaConverter consultaConverter) {
		this.consultaConverter = consultaConverter;
	}

	@Autowired
	public void setPadecimientoRepository(PadecimientoRepository padecimientoRepository) {
		this.padecimientoRepository = padecimientoRepository;
	}

	@Autowired
	public void setPadecimientoConverter(PadecimientoServiceImpl padecimientoServiceImpl) {
		this.padecimientoServiceImpl = padecimientoServiceImpl;
	}

	@Autowired
	public void setCatCie10Repository(CatCie10Repository catCie10Repository) {
		this.catCie10Repository = catCie10Repository;
	}

	@Transactional(readOnly = false, rollbackFor = {ConsultaException.class})
	@Override
	public ConsultaView createConsulta(ConsultaView consultaView) throws ConsultaException {
		try {
			ConsultaException ee = null;
			final Integer idEstadoConsulta = 1;
			if (consultaView.getIdEstadoConsulta() != 1) {
				ee = new ConsultaException("No se pudo cambiar el cambiar el estado de la consulta", ConsultaException.LAYER_DAO, ConsultaException.ACTION_VALIDATE);
				ee.addError("El catEstadoConsulta que se le quiere asignar no coincide con la acción de Crear");
				logger.error("createConsulta() - El idestadoConsulta para crear es:{}, se le quiere asginar otro estado:{}", idEstadoConsulta, consultaView.getIdEstadoConsulta());
				throw ee;
			}
			if (consultaView.getIdConsulta() != null) {
				if (consultaRepository.exists(consultaView.getIdConsulta())) {
					ee = new ConsultaException("Error Inesperado al agregar Consulta.", ConsultaException.LAYER_DAO, ConsultaException.ACTION_VALIDATE);
					ee.addError("Ya existe una Consulta con el id:" + consultaView.getIdConsulta());
					throw ee;
				}
			}
			validaciones(consultaView);
			consultaView.setIdEstadoConsulta(idEstadoConsulta);
			Consulta consulta = consultaConverter.toEntity(consultaView, new Consulta(), Boolean.FALSE);
			if(consultaView.getFechaConsultaFin()==null) {
				Date fechaConsultaFin=crearFechaConsultaFin(consultaView.getFechaConsulta());
				consulta.setFechaConsultaFin(fechaConsultaFin);
			}else {
				consulta.setFechaConsultaFin(consultaView.getFechaConsultaFin());
			}
			if (consultaView.getConsultaHis() == true) {
				consulta.setSubjetivo(consultaView.getSubjetivo());
				consulta.setFechaConsultaFin(consultaView.getFeachaFin());
				consulta.setFechaConsulta(consultaView.getFechaInicio());
			}
			logger.debug("Insertar nuevo Consulta: {}", consulta);
			consultaRepository.save(consulta);
			return consultaView = consultaConverter.toView(consulta, Boolean.TRUE);
		} catch (ConsultaException ee) {
			throw ee;
		} catch (DataIntegrityViolationException dive) {
			ConsultaException ee = new ConsultaException("No fue posible agregar Consulta", ConsultaException.LAYER_DAO, ConsultaException.ACTION_INSERT);
			ee.addError("Ocurrio un error al agregar Consulta");
			logger.error("createConsulta() - Error al insertar nuevo Consulta - CODE {} - {}", ee.getExceptionCode(), consultaView, ee);
			throw ee;
		} catch (ConstraintViolationException cve) {
			ConsultaException ee = new ConsultaException("Error inesperado al agregar Consulta", ConsultaException.LAYER_DAO, ConsultaException.ACTION_INSERT);
			ee.addError("Ocurrió un error al agregar Consulta:");
			ee.addError("" + cve);
			logger.error("createConsulta() - Error al insertar nuevo Consulta - CODE {} - {}", ee.getExceptionCode(), consultaView, cve.getMessage());
			throw ee;
		} catch (Exception ex) {
            logger.error("Error al insertar nueva Consulta", ex); // Sre22052020 Para que salga en el log porque no se puede ver el error
			ConsultaException ee = new ConsultaException("Error inesperado al agregar Consulta", ConsultaException.LAYER_DAO, ConsultaException.ACTION_INSERT);
			ee.addError("Ocurrió un error al agregar Consulta:" + ex.getMessage());
			logger.error("Error al insertar nuevo Consulta - CODE {} - {}", ee.getExceptionCode(), consultaView, ee);
			throw ee;
		}
	}

	private Date crearFechaConsultaFin(Date fechaConsulta) {
		Calendar calendar = Calendar.getInstance();
		final long ONE_MINUTE_IN_MILLIS=60000;//millisecs
		calendar.setTime(fechaConsulta);
		Long temporal = calendar.getTimeInMillis();
		Date afterAddingMins = new Date(temporal + (30 * ONE_MINUTE_IN_MILLIS));
		return afterAddingMins;
	}

	/********************************************************* DEBERIA QUITARSE *********************************************************(INICIO)*/
	@Transactional(readOnly = false, rollbackFor = {ConsultaException.class})
	@Override
	public void updateConsultaEstatus(ConsultaView consultaView) throws ConsultaException {
		//      try {
		//         if (consultaRepository.findByNumeroConsulta(consultaView.getNumeroConsulta()) == null) {
		//            ConsultaException pe = new ConsultaException("No se encuentra en el sistema Consulta.", ConsultaException.LAYER_DAO, ConsultaException.ACTION_VALIDATE);
		//            pe.addError("Consulta no encontrado");
		//            throw pe;
		//         }
		//
		//         Consulta consulta = consultaRepository.findByNumeroConsulta(consultaView.getNumeroConsulta());
		//
		//         Padecimiento padecimiento = null;
		//
		//
		////         Padecimiento padecimiento = padecimientoRepository.findByIdPadecimiento()
		////         if (consultaView.getPadecimientoId() != null || consultaView.getPadecimientoId() != 0) {
		////            padecimiento = padecimientoRepository.findByIdPadecimiento(consultaView.getPadecimientoId());
		////         } else {
		////            padecimiento = null;
		////         }
		//
		//         logger.info("updateConsultaEstatus - {}", consultaView);
		//         if (consultaView.getPadecimientoId() == null || consultaView.getPadecimientoId() == 0) {
		//            padecimiento = null;
		//         } else {
		//            padecimiento = padecimientoRepository.findByIdPadecimiento(consultaView.getPadecimientoId());
		//
		//         }
		//
		//
		////         consulta = consultaConverter.toEntity(consultaView, consulta, null, Boolean.TRUE);
		//         consulta = consultaConverter.toEntity(consultaView, consulta, padecimiento, Boolean.TRUE);
		//         logger.debug("Editar Consulta su ESTATUS: {}", consulta);
		//
		//         consultaRepository.save(consulta);
		//
		//
		//      } catch (DataIntegrityViolationException dive) {
		//         ConsultaException ee = new ConsultaException("No fue posible editar Consulta", ConsultaException.LAYER_DAO, ConsultaException.ACTION_UPDATE);
		//         ee.addError("Ocurrió un error al editar Consulta");
		//         logger.error("Error al editar Consulta - CODE {} - {}", ee.getExceptionCode(), consultaView, ee);
		//         throw ee;
		//      } catch (Exception ex) {
		//         ConsultaException ee = new ConsultaException("Error inesperado al editar Consulta", ConsultaException.LAYER_DAO, ConsultaException.ACTION_UPDATE);
		//         ee.addError("Ocurrió un error al editar Consulta");
		//         logger.error("Error al editar Consulta - CODE {} - {}", ee.getExceptionCode(), consultaView, ee);
		//         throw ee;
		//      }
	}

	/********************************************************* DEBERIA QUITARSE *********************************************************(FIN)*/

	/*@Transactional(readOnly = false, rollbackFor = {ConsultaException.class})
	@Override
	public void updateConsulta(ConsultaView consultaView) throws ConsultaException {
		try {
			//         if (consultaRepository.findByIdPaciente(consultaView.getIdPaciente()) == null) {
			if (consultaRepository.findByNumeroConsulta(consultaView.getNumeroConsulta()) == null) {
            ConsultaException pe = new ConsultaException("No se encuentra en el sistema Consulta.", ConsultaException.LAYER_DAO, ConsultaException.ACTION_VALIDATE);
            pe.addError("Consulta no encontrado");
            throw pe;
         }
			Set<Long>temP=new HashSet<>();
			//         ciclo para recorrer todos los cie10 que vengan
			for(Long cie10: consultaView.getListaPadecimientos()){
//         se verifica que no exista un padecimiento con mismo CIE10 y idPadecimiento
            CatCie10 catCie10 = catCie10Repository.findOne(cie10);
            Padecimiento encontrado = padecimientoRepository.findByCatCie10AndIdPaciente(catCie10, consultaView.getIdPaciente());

            if(encontrado==null){
//            se crea padecimiento y se le dan los valores iniciales
               PadecimientoView padecimientoView = new PadecimientoView();
               padecimientoView.setCie10Id(cie10);
               padecimientoView.setIdPaciente(consultaView.getIdPaciente());
               padecimientoView.setIdMedico(consultaView.getIdMedico());
               padecimientoView.setEstatus(Boolean.TRUE);
               padecimientoView.setNombreMedicoTratante(consultaView.getNombreMedicoTratante());
               padecimientoView.setIdMedicoTratante(consultaView.getIdMedicoTratante());
               padecimientoView.setConsultaId(consultaView.getIdConsulta());
               padecimientoView.setCreadoPor(consultaView.getCreadoPor());
               Padecimiento padecimiento = padecimientoConverter.toEntity(padecimientoView, new Padecimiento(), Boolean.FALSE);
               padecimientoRepository.save(padecimiento);
               encontrado = padecimientoRepository.findByCatCie10AndIdPaciente(catCie10, consultaView.getIdPaciente());
               temP.add(encontrado.getIdPadecimiento());

            }else{
            	temP.add(encontrado.getIdPadecimiento());
            }
         }
         consultaView.setListaPadecimientos(temP);
			//Consulta consulta = consultaRepository.findByNumeroConsulta(consultaView.getNumeroConsulta());
			consulta = consultaConverter.toEntity(consultaView, consulta, Boolean.TRUE);
         logger.debug("Editar Consulta: {}", consulta);
         consultaRepository.save(consulta);

		} catch (DataIntegrityViolationException dive) {
			ConsultaException ee = new ConsultaException("No fue posible editar Consulta", ConsultaException.LAYER_DAO, ConsultaException.ACTION_UPDATE);
			ee.addError("Ocurrió un error al editar Consulta");
			logger.error("Error al editar Consulta - CODE {} - {}", ee.getExceptionCode(), consultaView, ee);
			throw ee;
		} catch (Exception ex) {
			ConsultaException ee = new ConsultaException("Error inesperado al editar Consulta", ConsultaException.LAYER_DAO, ConsultaException.ACTION_UPDATE);
			ee.addError("Ocurrió un error al editar Consulta");
			logger.error("Error al editar Consulta - CODE {} - {}", ee.getExceptionCode(), consultaView, ee);
			throw ee;
		}
	}*/
	@Override
	//   public ConsultaView getDetailsByIdConsulta(String idPaciente) throws ConsultaException {
	public ConsultaView getDetailsByNumeroConsulta(Long numeroConsulta) throws ConsultaException {
		try {
			//         if (consultaRepository.findByIdPaciente(idPaciente) == null) {
			if (consultaRepository.findByNumeroConsulta(numeroConsulta) == null) {
				ConsultaException ee = new ConsultaException("No se encuentra en el sistema Consulta.", ConsultaException.LAYER_DAO, ConsultaException.ACTION_VALIDATE);
				ee.addError("Consulta no encontrado");
				throw ee;
			}
			//         Consulta consulta = consultaRepository.findByIdPaciente(idPaciente);
			Consulta consulta = consultaRepository.findByNumeroConsulta(numeroConsulta);
			return consultaConverter.toView(consulta, Boolean.TRUE);
		} catch (DataIntegrityViolationException dive) {
			ConsultaException ee = new ConsultaException("No fue posible editar Consulta", ConsultaException.LAYER_DAO, ConsultaException.ACTION_SELECT);
			ee.addError("No fue posible obtener detalles Consulta");
			logger.error("Error al obtener detalles Consulta - CODE {} - {}", ee.getExceptionCode(), numeroConsulta, ee);
			throw ee;
		} catch (Exception ex) {
			ConsultaException ee = new ConsultaException("Error inesperado al obtener detalles Consulta", ConsultaException.LAYER_DAO, ConsultaException.ACTION_SELECT);
			ee.addError("Ocurrió un error al obtener detalles Consulta");
			logger.error("Error al obtener detalles Consulta - CODE {} - {}", ee.getExceptionCode(), numeroConsulta, ee);
			throw ee;
		}
	}

	@Override
	public List<ConsultaView> getAllByPadecimiento(Long idPadecimiento) throws ConsultaException {
		try {
			List<Consulta> consultaList = new ArrayList<>();
			List<ConsultaView> consultaViewList = new ArrayList<>();
			consultaList = consultaRepository.findAllByPadecimiento_IdPadecimiento(idPadecimiento);
			for (Consulta con : consultaList) {
				consultaViewList.add(consultaConverter.toView(con, Boolean.TRUE));
			}
			return consultaViewList;
		} catch (Exception ex) {
			ConsultaException cee = new ConsultaException("Ocurrio un error al seleccionar lista Consulta", ConsultaException.LAYER_SERVICE, ConsultaException.ACTION_SELECT);
			logger.error(ExceptionServiceCode.GROUP + "- Error al tratar de seleccionar lista Consulta - CODE: {}-{}", cee.getExceptionCode(), ex);
			throw cee;
		}
	}

	@Override
	public List<ConsultaView> findAll() throws ConsultaException {
		try {
			List<Consulta> consultaList = new ArrayList<>();
			List<ConsultaView> consultaViewList = new ArrayList<>();
			consultaList = consultaRepository.findAll();
			for (Consulta eve : consultaList) {
				consultaViewList.add(consultaConverter.toView(eve, Boolean.TRUE));
			}
			return consultaViewList;
		} catch (Exception ex) {
			ConsultaException cee = new ConsultaException("Ocurrio un error al seleccionar lista Consulta", ConsultaException.LAYER_SERVICE, ConsultaException.ACTION_SELECT);
			logger.error(ExceptionServiceCode.GROUP + "- Error al tratar de seleccionar lista Consulta - CODE: {}-{}", cee.getExceptionCode(), ex);
			throw cee;
		}
	}

	@Override
	public Page<ConsultaView> getConsultaPage(UUID idPaciente, List<Long> idUsuario, UUID idMedico, Integer page, Integer size,
											  String orderColumn, String orderType, Long startDate, Long endDate, Integer idGroup, String name) throws ConsultaException {
		try {

			logger.info("- Obtener listado Consulta paginable: - idPaciente {} idUsuario:{} - idMedico {} - page {} - size: {} - orderColumn: {} - orderType: {} - startDate: {}  - endDate: {} - idGroup: {}",
					idPaciente, idUsuario, idMedico, page, size, orderColumn, orderType, startDate, endDate, idGroup);
			List<ConsultaView> consultaViewList = new ArrayList<>();
			Page<Consulta> consultaPage = null;
			Sort sort = new Sort(Sort.Direction.ASC, (String) colOrderNames.get("idPaciente"));

			if ((orderColumn != null && !orderColumn.isEmpty()) && orderType != null) {
				if (orderType.equalsIgnoreCase("asc"))
					sort = new Sort(Sort.Direction.ASC, (String) colOrderNames.get(orderColumn));
				else
					sort = new Sort(Sort.Direction.DESC, (String) colOrderNames.get(orderColumn));
			}
			PageRequest request = new PageRequest(page, size, sort);
			final UUID patternSearch = idPaciente;
			final UUID patternSearchMedico = idMedico;
			Specifications<Consulta> spec = Specifications.where(
					(root, query, cb) -> {
						Predicate tc = null;
						//  POR NOMBRE, EXPEDIENTE, TIPO DE CONSULTA, ESTATUS
						if(name != null) {
							String namePattern = "%" + name + "%";
							Predicate datosBusqueda = cb.like(cb.function("unaccent", String.class, cb.lower(root.get("datosBusqueda"))), namePattern);
							Predicate tipoConsulta = cb.like(cb.function("unaccent", String.class, cb.lower(root.get("tipoConsulta"))), namePattern);
							Predicate estadoConsulta = cb.like(cb.function("unaccent", String.class, cb.lower(root.get("estadoConsulta"))), namePattern);
							tc = cb.or(datosBusqueda, tipoConsulta, estadoConsulta);
						}
						//             paciente
						if (idPaciente != null) {
							tc = (tc != null ?
									cb.and(tc, cb.equal(root.get("idPaciente"), patternSearch)) :
										cb.equal(root.get("idPaciente"), patternSearch));
						}
						//             medico
						if (idMedico != null) {

							tc = (tc != null ?
									cb.and(tc, cb.equal(root.get("idMedico"), patternSearchMedico)) :
										cb.equal(root.get("idMedico"), patternSearchMedico));
						}
						//             fechas
						if (startDate != null && endDate != null) {
							//             if (startDate != null && endDate != null && !endDate.isEmpty()) {
							try {
								SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
								Date inicialDate = parseDate(sdf.format(startDate) + " 00:00:00", TimeUtils.LONG_DATE);
								Date finalDate = parseDate(sdf.format(endDate) + " 23:59:59", TimeUtils.LONG_DATE);
								tc = (tc != null) ?
										cb.and(tc, cb.greaterThanOrEqualTo(root.get("fechaConsulta"), inicialDate), cb.lessThanOrEqualTo(root.get("fechaConsulta"), finalDate)) :
											cb.and(cb.greaterThanOrEqualTo(root.get("fechaConsulta"), inicialDate), cb.lessThanOrEqualTo(root.get("fechaConsulta"), finalDate));
							} catch (Exception ex) {
								logger.warn("Error al convertir fechas", ex);
							}
						}
						if (idUsuario != null) {
							Expression<Long> usuario = root.get("idUsuario");
							tc = (tc != null ? cb.and(tc, usuario.in(idUsuario)) : usuario.in(idUsuario));
						}
						if (idGroup != null)
							tc = (tc != null ? cb.and(tc, cb.equal(root.get("idGroup"), idGroup)) : cb.equal(root.get("idGroup"), idGroup));
						return tc;
					}
			);

			if (spec == null) {
				consultaPage = consultaRepository.findAll(request);
			} else {
				consultaPage = consultaRepository.findAll(spec, request);
			}

			consultaPage.getContent().forEach(consulta -> {
				// consultaViewList.add(consultaConverter.toViewPage(consulta));
				consultaViewList.add(consultaConverter.toView(consulta, Boolean.TRUE));
			});
			PageImpl<ConsultaView> consultaViewPage = new PageImpl<ConsultaView>(consultaViewList, request, consultaPage.getTotalElements());
			return consultaViewPage;
		} catch (Exception ex) {
			ConsultaException cee = new ConsultaException("Ocurrio un error al seleccionar lista Consulta paginable", ConsultaException.LAYER_SERVICE, ConsultaException.ACTION_SELECT);
			logger.error(ExceptionServiceCode.GROUP + "- Error al tratar de seleccionar lista Consulta paginable - CODE: {}-{}", cee.getExceptionCode(), ex);
			throw cee;
		}
	}

	private String sinAcentos(String cadena) {
		return Normalizer.normalize(cadena, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
	}

	@Override
	public ConsultaView getConsultaById(Long idConsulta) throws ConsultaException {
		try {
			if (consultaRepository.findByIdConsulta(idConsulta) == null) {
				ConsultaException ee = new ConsultaException("No se encuentra en el sistema Consulta.", ConsultaException.LAYER_DAO, ConsultaException.ACTION_VALIDATE);
				ee.addError("No existe la Consulta con el Id:" + idConsulta);
				throw ee;
			}
			Consulta consulta = consultaRepository.findByIdConsulta(idConsulta);
			return consultaConverter.toView(consulta, Boolean.TRUE);
		} catch (ConsultaException ee) {
			throw ee;
		} catch (DataIntegrityViolationException dive) {
			ConsultaException ee = new ConsultaException("No fue posible editar Consulta", ConsultaException.LAYER_DAO, ConsultaException.ACTION_SELECT);
			ee.addError("No fue posible obtener detalles Consulta");
			logger.error("Error al obtener detalles Consulta - CODE {} - {}", ee.getExceptionCode(), idConsulta, ee);
			throw ee;
		} catch (Exception ex) {
			ConsultaException ee = new ConsultaException("Error inesperado al obtener detalles Consulta", ConsultaException.LAYER_DAO, ConsultaException.ACTION_SELECT);
			ee.addError("Ocurrió un error al obtener detalles Consulta");
			logger.error("Error al obtener detalles Consulta - CODE {} - {}", ee.getExceptionCode(), idConsulta, ee);
			throw ee;
		}
	}

	public Page<ConsultaView> getConsultaSearch(UUID idPaciente, List<Long> idUsuario, UUID idMedico, Integer idTipoConsulta, List<Integer> idStatusConsulta, Integer page, Integer size, String orderColumn, String orderType, Long startDate, Long endDate) throws ConsultaException {
		try {
			logger.info("getConsultaSearch() - Obtener listado Consulta paginable: - idPaciente {} - idUsuario:{} - idMedico {} - page {} - size: {} - orderColumn: {} - orderType: {} - startDate: {}  - endDate: {}",
					idPaciente, idUsuario, idMedico, page, size, orderColumn, orderType, startDate, endDate);
			List<ConsultaView> consultaViewList = new ArrayList<>();
			Page<Consulta> consultaPage = null;
			Sort sort = new Sort(Sort.Direction.ASC, (String) colOrderSearch.get("idPaciente"));
			if ((orderColumn != null && !orderColumn.isEmpty()) && orderType != null) {
				if (orderType.equalsIgnoreCase("asc")) {
					sort = new Sort(Sort.Direction.ASC, (String) colOrderSearch.get(orderColumn));
				} else {
					sort = new Sort(Sort.Direction.DESC, (String) colOrderSearch.get(orderColumn));
				}

			}
			PageRequest request = new PageRequest(page, size, sort);
			final UUID patternSearch = idPaciente;
			final UUID patternSearchMedico = idMedico;
			Specifications<Consulta> spec = Specifications.where(
					(root, query, cb) -> {
						Predicate tc = null;
						Expression<Long> usuario = root.get("idUsuario");
						Expression<Long> estadoConsulta = root.get("idEstadoConsulta");
						//		             paciente
						if (idPaciente != null) {
							tc = cb.equal(root.get("idPaciente"), patternSearch);
						}
						//		             medico
						if (idMedico != null) {

							tc = (tc != null ?
									cb.and(tc, cb.equal(root.get("idMedico"), patternSearchMedico)) :
										cb.equal(root.get("idMedico"), patternSearchMedico));
						}
						//		             fechas
						if (startDate != null && endDate != null) {
							try {
								SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
								Date inicialDate = parseDate(sdf.format(startDate) + " 00:00:00", TimeUtils.LONG_DATE);
								Date finalDate = parseDate(sdf.format(endDate) + " 23:59:59", TimeUtils.LONG_DATE);
								tc = (tc != null) ?
										cb.and(tc, cb.greaterThanOrEqualTo(root.get("fechaConsulta"), inicialDate), cb.lessThanOrEqualTo(root.get("fechaConsulta"), finalDate)) :
											cb.and(cb.greaterThanOrEqualTo(root.get("fechaConsulta"), inicialDate), cb.lessThanOrEqualTo(root.get("fechaConsulta"), finalDate));
							} catch (Exception ex) {
								logger.warn("Error al convertir fechas", ex);
							}
						}
						if (idUsuario != null && !idUsuario.isEmpty()) {
							tc = (tc != null ? cb.and(tc, usuario.in(idUsuario)) : usuario.in(idUsuario));
						}
						if (idStatusConsulta != null && !idStatusConsulta.isEmpty()) {

							tc = (tc != null ? cb.and(tc, estadoConsulta.in(idStatusConsulta)) : estadoConsulta.in(idStatusConsulta));
						}
						if (idTipoConsulta != null) {
							tc = (tc != null ? cb.and(tc, cb.equal(root.get("idTipoConsulta"), idTipoConsulta)) : cb.equal(root.get("idTipoConsulta"), idTipoConsulta));
						}
						return tc;
					}
					);

			if (spec == null) {
				consultaPage = consultaRepository.findAll(request);
			} else {
				consultaPage = consultaRepository.findAll(spec, request);
			}

			consultaPage.getContent().forEach(consulta -> {
				consultaViewList.add(consultaConverter.toView(consulta, Boolean.FALSE));
			});
			PageImpl<ConsultaView> consultaViewPage = new PageImpl<ConsultaView>(consultaViewList, request, consultaPage.getTotalElements());
			return consultaViewPage;
		} catch (Exception ex) {
			ConsultaException cee = new ConsultaException("Ocurrio un error al seleccionar lista Consulta paginable", ConsultaException.LAYER_SERVICE, ConsultaException.ACTION_SELECT);
			logger.error(ExceptionServiceCode.GROUP + "- Error al tratar de seleccionar lista Consulta paginable - CODE: {}-{}", cee.getExceptionCode(), ex);
			throw cee;
		}
	}

	@Transactional(readOnly = false, rollbackFor = {ConsultaException.class})
	@Override
	public void consultaStart(Long idConsulta, CatEstadoConsultaView catEstadoConsultaView) throws ConsultaException {
		try {
			final Integer idEstadoConsulta = 2;
			ConsultaException ee;
			Consulta encontrado = consultaRepository.findByIdConsulta(idConsulta);
			if (encontrado != null) {
				if (2 != catEstadoConsultaView.getIdEstadoConsulta()) {
					ee = new ConsultaException("No se pudo cambiar el cambiar el estado de la consulta", ConsultaException.LAYER_DAO, ConsultaException.ACTION_VALIDATE);
					ee.addError("El catEstadoConsulta que se le quiere asignar no coincide con la acción de Iniciar");
					logger.error("consultaStart() - El idestadoConsulta para Iniciar es:{}, se le quiere asginar otro estado - {}", idEstadoConsulta, catEstadoConsultaView);
					throw ee;
				}
				if (!catEstadoConsultaView.getActivo()) {
					ee = new ConsultaException("No se pudo cambiar el cambiar el estado de la consulta", ConsultaException.LAYER_DAO, ConsultaException.ACTION_VALIDATE);
					ee.addError("La acción de confirmar consulta a sido desactivada");
					logger.error("consultaStart() - El estadoConsulta que se le quiere asignar esta desactivado: {}", catEstadoConsultaView);
					throw ee;
				}
				if (encontrado.getIdEstadoConsulta() != 1 && encontrado.getIdEstadoConsulta() != 5 && encontrado.getIdEstadoConsulta() != 7) {
					ee = new ConsultaException("No se pudo cambiar el estado de la consulta", ConsultaException.LAYER_DAO, ConsultaException.ACTION_VALIDATE);
					ee.addError("La consulta esta: " + encontrado.getEstadoConsulta());
					logger.error("consultaStart() - La consulta con el id:{} esta en un estado:{} ", idConsulta, encontrado.getEstadoConsulta());
					throw ee;
				}

				encontrado.setFechaInicio(new Date());
				encontrado.setEstadoConsulta(catEstadoConsultaView.getDescripcion());
				encontrado.setIdEstadoConsulta(catEstadoConsultaView.getIdEstadoConsulta());
				consultaRepository.save(encontrado);
				logger.info("consultaStart() - idConsulta: {} - estadConsulta: {} ", idConsulta, catEstadoConsultaView.getDescripcion());

			} else {
				ee = new ConsultaException("No se encuentra en el sistema Consulta.", ConsultaException.LAYER_DAO, ConsultaException.ACTION_VALIDATE);
				ee.addError("Consulta no encontrado");
				throw ee;
			}
		} catch (ConsultaException ee) {
			throw ee;
		} catch (DataIntegrityViolationException dive) {
			ConsultaException ee = new ConsultaException("Ocurrio un error en la  DB al cambiar de estado la consulta", ConsultaException.LAYER_DAO, ConsultaException.ACTION_SELECT);
			ee.addError("" + ee);
			logger.error("consultaStart() - idConsulta: {} - CODE {} - {}", idConsulta, ee.getExceptionCode(), ee);
			throw ee;
		} catch (Exception e) {
			ConsultaException ee = new ConsultaException("Ocurrio un error", ConsultaException.LAYER_DAO, ConsultaException.ACTION_VALIDATE);
			ee.addError("" + e);
			logger.error("consultaStart() - idConsulta: {} - CODE {} - {}", idConsulta, ee.getExceptionCode(), ee);
			throw ee;
		}

	}

	@Transactional(readOnly = false, rollbackFor = {ConsultaException.class})
	@Override
	public void consultaCancel(Long idConsulta, CatEstadoConsultaView catEstadoConsultaView) throws ConsultaException {
		try {
			ConsultaException ee = null;
			Consulta consulta = consultaRepository.findByIdConsulta(idConsulta);
			if (consulta != null) {
				if (3 != catEstadoConsultaView.getIdEstadoConsulta()) {
					ee = new ConsultaException("No se pudo cambiar el cambiar el estado de la consulta", ConsultaException.LAYER_DAO, ConsultaException.ACTION_VALIDATE);
					ee.addError("El catEstadoConsulta que se le quiere asignar no coincide con la acción de Cancelar");
					logger.error("consultaStart() - El idestadoConsulta para Cancelar es:{}, se le quiere asginar otro estado - {}", 3, catEstadoConsultaView);
					throw ee;
				}
				if (!catEstadoConsultaView.getActivo()) {
					ee = new ConsultaException("No se pudo cambiar el cambiar el estado de la consulta", ConsultaException.LAYER_DAO, ConsultaException.ACTION_VALIDATE);
					ee.addError("La acción de confirmar consulta a sido desactivada");
					logger.error("consultaStart() - El estadoConsulta que se le quiere asignar esta desactivado: {}", catEstadoConsultaView);
					throw ee;
				}
				if (consulta.getIdEstadoConsulta() == 4 || consulta.getIdEstadoConsulta() == 3) {
					ee = new ConsultaException("No se pudo cambiar el estado de la consulta", ConsultaException.LAYER_DAO, ConsultaException.ACTION_VALIDATE);
					ee.addError("La consulta esta: " + consulta.getEstadoConsulta());
					logger.error("consultaCancel() - La consulta con el id:{} esta en un estado:{} ", idConsulta, consulta.getEstadoConsulta());
					throw ee;
				}
				consulta.setEstadoConsulta(catEstadoConsultaView.getDescripcion());
				consulta.setIdEstadoConsulta(catEstadoConsultaView.getIdEstadoConsulta());
				consulta.setFechaCancelacion(new Date());
				logger.info("{}", consulta);
				consultaRepository.save(consulta);
				logger.info("consultaStart() - idConsulta: {} - estadConsulta: {} ", idConsulta, catEstadoConsultaView.getDescripcion());
			} else {
				ee = new ConsultaException("No se encuentra en el sistema Consulta.", ConsultaException.LAYER_DAO, ConsultaException.ACTION_VALIDATE);
				ee.addError("Consulta no encontrado");
				throw ee;
			}
		} catch (ConsultaException ee) {
			throw ee;
		} catch (DataIntegrityViolationException dive) {
			ConsultaException ee = new ConsultaException("Ocurrio un error al cambia el estado de la consulta en la DB", ConsultaException.LAYER_DAO, ConsultaException.ACTION_SELECT);
			ee.addError("" + ee);
			logger.error("consultaCancel() - idConsulta: {} - CODE {} - {}", idConsulta, ee.getExceptionCode(), ee);
			throw ee;
		} catch (Exception e) {
			ConsultaException ee = new ConsultaException("Ocurrio un error", ConsultaException.LAYER_DAO, ConsultaException.ACTION_VALIDATE);
			ee.addError("" + e);
			logger.error("consultaCancel() - idConsulta: {} - CODE {} - {}", idConsulta, ee.getExceptionCode(), ee);
			throw ee;
		}

	}

	@Transactional(readOnly = false, rollbackFor = {ConsultaException.class})
	@Override
	public void consultreschedule(Long idConsulta, Date nuevaFechaConsulta,Date FechaConsultaFin, CatEstadoConsultaView catEstadoConsultaView) throws ConsultaException {
		try {
			ConsultaException ee;
			Consulta consulta = consultaRepository.findByIdConsulta(idConsulta);
			if (consulta == null) {
				ee = new ConsultaException("Error al reagendar la consulta.", ConsultaException.LAYER_DAO, ConsultaException.ACTION_VALIDATE);
				ee.addError("La consulta NO Existe en la DB");
				throw ee;
			}
			if (1 != catEstadoConsultaView.getIdEstadoConsulta()) {
				ee = new ConsultaException("Existe un Error", ConsultaException.LAYER_DAO, ConsultaException.ACTION_VALIDATE);
				ee.addError("El estado de la consula NO PERMITE que se pueda reagendar");
				logger.error("El estado de la consula NO PERMITE que se pueda reagendar", 1, catEstadoConsultaView);
				throw ee;
			}
			if (!catEstadoConsultaView.getActivo()) {
				ee = new ConsultaException("No se pudo cambiar el cambiar el estado de la consulta", ConsultaException.LAYER_DAO, ConsultaException.ACTION_VALIDATE);
				ee.addError("La acción de confirmar consulta a sido desactivada");
				logger.error("consultreschedule() - El estadoConsulta que se le quiere asignar esta desactivado: {}", catEstadoConsultaView);
				throw ee;
			}
			if (consulta.getIdEstadoConsulta() == 4 || consulta.getIdEstadoConsulta() == 3) {
				ee = new ConsultaException("No se pudo cambiar el estado de la consulta", ConsultaException.LAYER_DAO, ConsultaException.ACTION_VALIDATE);
				ee.addError("La consulta esta: " + consulta.getEstadoConsulta());
				logger.error("consultreschedule() - La consulta con el id:{} esta en un estado:{} ", idConsulta, consulta.getEstadoConsulta());
				throw ee;
			}
			consulta.setEstadoConsulta(catEstadoConsultaView.getDescripcion());
			consulta.setIdEstadoConsulta(catEstadoConsultaView.getIdEstadoConsulta());
			consulta.setFechaInicio(null);
			consulta.setFeachaFin(null);
			consulta.setFechaConsulta(nuevaFechaConsulta);
			if(FechaConsultaFin==null) {
				Date newfechaConsultaFin= crearFechaConsultaFin(nuevaFechaConsulta);
				consulta.setFechaConsultaFin(newfechaConsultaFin);
			}else {
				if(FechaConsultaFin.getTime() <= nuevaFechaConsulta.getTime()) {
					ee = new ConsultaException("No se pudo Reagendar la consulta", ConsultaException.LAYER_DAO, ConsultaException.ACTION_VALIDATE);
					ee.addError("La fecha consulta es mayor a la fecha consulta fin");
					logger.error("consultreschedule() - La consulta con el id:{} esta en un estado:{} ", idConsulta, consulta.getEstadoConsulta());
					throw ee;
				}
				consulta.setFechaConsultaFin(FechaConsultaFin);

			}
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			consulta.setDatosBusqueda(sdf.format(consulta.getFechaConsulta()) + " " + consulta.getNombreMedico()
			+ " " + consulta.getTipoConsulta());
			consultaRepository.save(consulta);
			logger.info("consultreschedule() - reagendadar consulta - idConsulta: {} - cambio estadConsulta: {} ", idConsulta, catEstadoConsultaView.getDescripcion());

		} catch (ConsultaException ee) {
			throw ee;
		} catch (DataIntegrityViolationException dive) {
			ConsultaException ee = new ConsultaException("Ocurrio un error", ConsultaException.LAYER_DAO, ConsultaException.ACTION_SELECT);
			ee.addError("No se pudo reagendar la consulta");
			logger.error("consultreschedule() - idConsulta: {} - CODE {} - {}", idConsulta, ee.getExceptionCode(), ee);
			throw ee;
		} catch (Exception e) {
			ConsultaException ee = new ConsultaException("Ocurrio un error", ConsultaException.LAYER_DAO, ConsultaException.ACTION_VALIDATE);
			ee.addError("No se pudo reagendar la consulta");
			logger.error("consultreschedule() - idConsulta: {} - CODE {} - {}", idConsulta, ee.getExceptionCode(), ee);
			throw ee;
		}
	}

	@Transactional(readOnly = false, rollbackFor = {ConsultaException.class})
	@Override
	public void consultaFinish(Long idConsulta, ConsultaView consultaView) throws ConsultaException {
		try {
			final String estadoConsulta = "Finalizada";
			final Integer idEstadoConsulta = 4;
			ConsultaException ee;
			ArrayList<PadecimientoView> temp = new ArrayList<>();
			Consulta encontrado = consultaRepository.findByIdConsulta(idConsulta);
			if (encontrado != null) {
				if (encontrado.getIdEstadoConsulta() != 2) {
					ee = new ConsultaException("No se pudo cambiar el estado de la consulta", ConsultaException.LAYER_DAO, ConsultaException.ACTION_VALIDATE);
					ee.addError("La consulta esta: " + encontrado.getEstadoConsulta());
					logger.error("consultaCancel() - La consulta con el id:{} esta en un estado:{} ", idConsulta, encontrado.getEstadoConsulta());
					throw ee;
				}
				if (4 != consultaView.getIdEstadoConsulta()) {
					ee = new ConsultaException("No se pudo cambiar el cambiar el estado de la consulta", ConsultaException.LAYER_DAO, ConsultaException.ACTION_VALIDATE);
					ee.addError("El EstadoConsulta que se le quiere asignar no coincide con la acción de Terminar");
					logger.error("consultaCancel() - El idestadoConsulta para Terminar es:{}, se le quiere asginar otro estado - {}", idEstadoConsulta, consultaView.getEstadoConsulta());
					throw ee;
				}
				consultaView.setEstadoConsulta(estadoConsulta);
				consultaView.setIdEstadoConsulta(idEstadoConsulta);
				for (PadecimientoView padecimientoView : consultaView.getListaPadecimiento()) {
					if (padecimientoView != null) {
						CatCie10 catCie10 = catCie10Repository.findOne(padecimientoView.getCie10Id());
						if (catCie10 == null) {
							ee = new ConsultaException("Ocurrio un error al Finalizar la consulta", ConsultaException.LAYER_DAO, ConsultaException.ACTION_VALIDATE);
							ee.addError("No existe el sistema el catCie10 con el Id " + padecimientoView.getCie10Id());
							throw ee;
						}
						Padecimiento padecimientoEncontrado = padecimientoRepository.findByCatCie10AndIdPaciente(catCie10, consultaView.getIdPaciente() + "");
						if (padecimientoEncontrado == null) {
							padecimientoView.setConsultaId(encontrado.getIdConsulta());
							padecimientoView.setIdPaciente(consultaView.getIdPaciente() + "");
							padecimientoView.setIdMedico(consultaView.getIdMedico() + "");
							padecimientoView.setNombreMedicoTratante(consultaView.getNombreMedico());
							padecimientoView.setIdMedicoTratante(consultaView.getIdMedico() + "");
							padecimientoView.setConsultaId(consultaView.getIdConsulta());
							padecimientoView.setCreadoPor(consultaView.getCreadoPor());
							padecimientoServiceImpl.createPadecimiento(padecimientoView);
							padecimientoEncontrado = padecimientoRepository.findByCatCie10AndIdPaciente(catCie10, consultaView.getIdPaciente() + "");
							padecimientoView.setIdPadecimiento(padecimientoEncontrado.getIdPadecimiento());
							temp.add(padecimientoView);
						} else {
							temp.add(padecimientoServiceImpl.getDetailsByIdPadecimiento(padecimientoEncontrado.getIdPadecimiento()));
						}
					} else {
						ee = new ConsultaException("Error Inesperado al Finalizar Consulta.", ConsultaException.LAYER_DAO, ConsultaException.ACTION_VALIDATE);
						ee.addError("La lista de padecimiento contiene un objeto NULO");
						logger.error("consultaFinish()-  El arreglo de Padecimiento contiene un Objeto NULL {}", consultaView.getListaPadecimiento());
						throw ee;
					}
				}
				for (TratamientoView tratamientoView : consultaView.getListaTartamiento()) {
					if (tratamientoView != null) {
						if (!catCie9Repository.exists(tratamientoView.getCatCie9Id())) {
							ee = new ConsultaException("Ocurrio un error al finalizar la consulta", ConsultaException.LAYER_DAO, ConsultaException.ACTION_VALIDATE);
							ee.addError("No existe el sistema el CactCie9 con el Id " + tratamientoView.getCatCie9Id());
							throw ee;
						}

						Tratamiento tr = tratamientoRepository.findByConsultaAndCatCie9(encontrado, catCie9Repository.findOne(tratamientoView.getCatCie9Id()));
						if (tr == null) {
							tratamientoView.setConsultaId(encontrado.getIdConsulta());
							tr = tratamientoConverter.toEntity(tratamientoView, new Tratamiento());
							tratamientoRepository.save(tr);
						}
					} else {
						ee = new ConsultaException("Error Inesperado al Finalizar la Consulta.", ConsultaException.LAYER_DAO, ConsultaException.ACTION_VALIDATE);
						ee.addError("La lista de Tratamiento contiene un objeto NULO");
						logger.error("consultaFinish()-  El arreglo de Tratamiento contiene un Objeto NULL {}", consultaView.getListaTartamiento());
						throw ee;
					}
				}
				List<Tratamiento> ltr = tratamientoRepository.findByConsulta(encontrado);
				if (consultaView.getListaTartamiento().size() == 0) {
					tratamientoRepository.delete(ltr);
					ltr.removeAll(ltr);
				}
				ltr.forEach(tratamiento -> {
					Boolean bandera = false;
					for (TratamientoView tratamientoView : consultaView.getListaTartamiento()) {
						if (tratamiento.getCatCie9().getIdCie9() == tratamientoView.getCatCie9Id()) {
							bandera = true;
						}
					}
					if (!bandera) {
						tratamientoRepository.delete(tratamiento);
					}
				});
				//se utiliza ListCacie10para guardar los idPadecimientos para hacer la ralacion consulta_padecimiento
				consultaView.setListaPadecimiento(temp);
				Consulta consulta = consultaConverter.toEntity(consultaView, encontrado, Boolean.TRUE);
				consulta.setFeachaFin(new Date());
				consulta.setEstadoConsulta(estadoConsulta);
				consulta.setIdEstadoConsulta(idEstadoConsulta);
				consultaRepository.save(consulta);

			} else {
				ee = new ConsultaException("No se encuentra en el sistema Consulta.", ConsultaException.LAYER_DAO, ConsultaException.ACTION_VALIDATE);
				ee.addError("Consulta no encontrado");
				throw ee;
			}
		} catch (ConsultaException ee) {
			throw ee;
		} catch (DataIntegrityViolationException dive) {
			ConsultaException ee = new ConsultaException("Ocurrio un error al finalizar la Consulta", ConsultaException.LAYER_DAO, ConsultaException.ACTION_SELECT);
			ee.addError("No se pudo terminar la la consulta");
			logger.error("consultaFinish() - idConsulta: {} - CODE {} - {}", idConsulta, ee.getExceptionCode(), dive);
			throw ee;
		} catch (Exception e) {
			ConsultaException ee = new ConsultaException("Ocurrio un error al finalizar la Consulta", ConsultaException.LAYER_DAO, ConsultaException.ACTION_VALIDATE);
			ee.addError("No se pudo terminar la la consulta");
			logger.error("consultaFinish() - idConsulta: {} - CODE {} - {}", idConsulta, ee.getExceptionCode(), e);
			throw ee;
		}
	}


	@Transactional(readOnly = false, rollbackFor = {ConsultaException.class})
	@Override
	public void updateConsulta(ConsultaView consultaView) throws ConsultaException {
		try {
			ConsultaException ee = null;
			Consulta encontrado = consultaRepository.findByIdConsulta(consultaView.getIdConsulta());
			if (encontrado == null) {
				ee = new ConsultaException("Ocurrio un erro al actualizar la consulta", ConsultaException.LAYER_DAO, ConsultaException.ACTION_VALIDATE);
				ee.addError("No se encuente en el sistema la consulta con el id: " + consultaView.getIdConsulta());
				throw ee;
			}
			if (encontrado.getIdEstadoConsulta() != 2) {
				ee = new ConsultaException("No se pudo cambiar el estado de la consulta", ConsultaException.LAYER_DAO, ConsultaException.ACTION_VALIDATE);
				ee.addError("La consulta esta: " + encontrado.getEstadoConsulta());
				logger.error("updateConsulta() - La consulta con el id:{} esta en un estado:{} ", encontrado.getIdConsulta(), encontrado.getEstadoConsulta());
				throw ee;
			}
			List<Padecimiento> listToDelete = padecimientoRepository.findAllByIdPaciente(consultaView.getIdPaciente().toString());
			logger.info("Lista de padecimientos a borrar: {}", listToDelete);
			padecimientoRepository.delete(listToDelete);
			for (PadecimientoView padecimientoView : consultaView.getListaPadecimiento()) {
				if (padecimientoView != null) {
					CatCie10 catCie10 = catCie10Repository.findOne(padecimientoView.getCie10Id());
					if (catCie10 == null) {
						ee = new ConsultaException("Ocurrio un error al Finalizar la consulta", ConsultaException.LAYER_DAO, ConsultaException.ACTION_VALIDATE);
						ee.addError("No existe el sistema el catCie10 con el Id " + padecimientoView.getCie10Id());
						throw ee;
					}
					Padecimiento padecimientoEncontrado = padecimientoRepository.findByCatCie10AndIdPaciente(catCie10, consultaView.getIdPaciente() + "");
					if (padecimientoEncontrado == null) {
						padecimientoView.setConsultaId(encontrado.getIdConsulta());
						padecimientoView.setIdPaciente(consultaView.getIdPaciente() + "");
						padecimientoView.setIdMedico(consultaView.getIdMedico() + "");
						padecimientoView.setNombreMedicoTratante(consultaView.getNombreMedico());
						padecimientoView.setIdMedicoTratante(consultaView.getIdMedico() + "");
						padecimientoView.setConsultaId(consultaView.getIdConsulta());
						padecimientoView.setCreadoPor(consultaView.getCreadoPor());
						padecimientoServiceImpl.createPadecimiento(padecimientoView);
					}
				} else {
					ee = new ConsultaException("Error Inesperado al Finalizar Consulta.", ConsultaException.LAYER_DAO, ConsultaException.ACTION_VALIDATE);
					ee.addError("La lista de padecimiento contiene un objeto NULO");
					logger.error("consultaFinish()-  El arreglo de Padecimiento contiene un Objeto NULL {}", consultaView.getListaPadecimiento());
					throw ee;
				}
			}
			for (TratamientoView tratamientoView : consultaView.getListaTartamiento()) {
				if (tratamientoView != null) {
					if (!catCie9Repository.exists(tratamientoView.getCatCie9Id())) {
						ee = new ConsultaException("Ocurrio un erro al actualizar la consulta", ConsultaException.LAYER_DAO, ConsultaException.ACTION_VALIDATE);
						ee.addError("No existe el sistema el CactCie9 con el Id: " + tratamientoView.getCatCie9Id());
						throw ee;
					}

					Tratamiento tr = tratamientoRepository.findByConsultaAndCatCie9(encontrado, catCie9Repository.findOne(tratamientoView.getCatCie9Id()));
					if (tr == null) {
						tratamientoView.setConsultaId(encontrado.getIdConsulta());
						tr = tratamientoConverter.toEntity(tratamientoView, new Tratamiento());
						tratamientoRepository.save(tr);
					}
				} else {
					ee = new ConsultaException("Error Inesperado al Actualizar la Consulta.", ConsultaException.LAYER_DAO, ConsultaException.ACTION_VALIDATE);
					ee.addError("La lista de Tratamiento contiene un objeto NULO");
					logger.error("updateConsulta()-  El arreglo de Tratamiento contiene un Objeto NULL {}", consultaView.getListaTartamiento());
					throw ee;
				}
			}
			List<Tratamiento> ltr = tratamientoRepository.findByConsulta(encontrado);
			if (consultaView.getListaTartamiento().size() == 0) {
				tratamientoRepository.delete(ltr);
				ltr.removeAll(ltr);
			}
			ltr.forEach(tratamiento -> {
				Boolean bandera = false;
				for (TratamientoView tratamientoView : consultaView.getListaTartamiento()) {
					if (tratamiento.getCatCie9().getIdCie9() == tratamientoView.getCatCie9Id()) {
						bandera = true;
					}
				}
				if (!bandera) {
					tratamientoRepository.delete(tratamiento);
				}
			});
			Consulta consulta = consultaConverter.toEntity(consultaView, encontrado, Boolean.TRUE);
			consultaRepository.save(consulta);
		} catch (ConsultaException ee) {
			throw ee;
		} catch (DataIntegrityViolationException dive) {
			ConsultaException ee = new ConsultaException("Ocurrio un erro al actualizar la consulta", ConsultaException.LAYER_DAO, ConsultaException.ACTION_SELECT);
			ee.addError("No fue posible actualizar la consulta");
			logger.error("updateConsulta() - idConsulta: {} - CODE {} - {}", consultaView.getIdConsulta(), ee.getExceptionCode(), ee);
			throw ee;
		} catch (Exception e) {
			ConsultaException ee = new ConsultaException("Ocurrio un erro al actualizar la consulta", ConsultaException.LAYER_DAO, ConsultaException.ACTION_VALIDATE);
			ee.addError("No fue posible actualizar la consulta");
			logger.error("updateConsulta() - idConsulta: {} - CODE {} - {}", consultaView.getIdConsulta(), ee.getExceptionCode(), ee);
			throw ee;
		}
	}

	@Transactional(readOnly = false, rollbackFor = {ConsultaException.class})
	@Override
	public void signosVitales(Long idConsulta, SignosVitalesView signosVitales) throws ConsultaException {
		try {
			Consulta encontrado = consultaRepository.findByIdConsulta(idConsulta);
			final Integer estadoConsulta = 7;
			if (!signosVitales.getCatEstadoConsultaView().getActivo()) {
				ConsultaException ee = new ConsultaException("Ocurrio un erro al agregar los signos Vitales a la consulta", ConsultaException.LAYER_DAO, ConsultaException.ACTION_VALIDATE);
				ee.addError("La acción " + signosVitales.getCatEstadoConsultaView().getDescripcion() + " esta desabilitada");
				logger.info("signosVitales-() - La acción:{} esta desabilitada - catEstadoConsulta:{} ", signosVitales.getCatEstadoConsultaView().getDescripcion(), signosVitales.getCatEstadoConsultaView());
				throw ee;
			}
			if (estadoConsulta != signosVitales.getCatEstadoConsultaView().getIdEstadoConsulta()) {
				ConsultaException ee = new ConsultaException("Ocurrio un erro al agregar los signos Vitales a la consulta", ConsultaException.LAYER_DAO, ConsultaException.ACTION_VALIDATE);
				ee.addError("El EstadoConsulta que se le quiere asignar no coincide con esta operación");
				logger.info("signosVitales-() - El idEstadoConsulta que se le quiere asignar no coincide con el idEstadoConsulta de esta operacion ", signosVitales.getCatEstadoConsultaView().getIdEstadoConsulta(), estadoConsulta);
				throw ee;
			}
			if (encontrado == null) {
				ConsultaException ee = new ConsultaException("Ocurrio un erro al agregar los signos Vitales a la consulta", ConsultaException.LAYER_DAO, ConsultaException.ACTION_VALIDATE);
				ee.addError("No existe la consulta");
				logger.info("No existe la consulta con el id:{}", idConsulta);
				throw ee;
			}
			Integer estadoCosulta = encontrado.getIdEstadoConsulta();
			if (estadoCosulta != 5 && estadoCosulta != 6) {
				ConsultaException ee = new ConsultaException("Ocurrio un erro al agregar los signos Vitales a la consulta", ConsultaException.LAYER_DAO, ConsultaException.ACTION_VALIDATE);
				ee.addError("La consulta esta " + encontrado.getEstadoConsulta());
				logger.info("La consulta esta {}", encontrado.getEstadoConsulta());
				throw ee;
			}

			JsonNode sV = consultaConverter.SignosVitalesToEntity(signosVitales.getSignosVitales());
			logger.info("signosVitales() - Guardando los signos vitales para la consulta con el Id:{}", idConsulta);
			encontrado.setSignosVitales(sV);
			encontrado.setIdEstadoConsulta(estadoConsulta);
			encontrado.setEstadoConsulta(signosVitales.getCatEstadoConsultaView().getDescripcion());
			consultaRepository.save(encontrado);
		} catch (ConsultaException ee) {
			throw ee;
		} catch (DataIntegrityViolationException dive) {
			ConsultaException ee = new ConsultaException("Ocurrio un erro al agregar los signos Vitales a la consulta", ConsultaException.LAYER_DAO, ConsultaException.ACTION_SELECT);
			ee.addError("No fue posible guardar los signos vitales");
			logger.error("updateConsulta() - idConsulta: {} - CODE {} - {}", idConsulta, ee.getExceptionCode(), dive.getMessage());
			throw ee;
		} catch (Exception e) {
			ConsultaException ee = new ConsultaException("Ocurrio un erro al agregar los signos Vitales a la consulta", ConsultaException.LAYER_DAO, ConsultaException.ACTION_VALIDATE);
			ee.addError("No fue posible guardar los signos vitales");
			logger.error("updateConsulta() - idConsulta: {} - CODE {} - {}", idConsulta, ee.getExceptionCode(), e);
			throw ee;
		}

	}

	@Override
	public ConsultaView getUltimaConsulta(UUID idPaciente, Integer idGroup) throws ConsultaException {
		try {
			final Integer idEstadoConsulta = 2;
			Consulta consulta = consultaRepository.findConsulta(idPaciente, idEstadoConsulta, idGroup);
			if (consulta == null) {
				logger.info("getUltimaConsulta() - idPaciente:{} no tiene consultas en turno", idPaciente);
				return null;
			}
			ConsultaView consultaView = consultaConverter.toView(consulta, Boolean.TRUE);
			logger.info("getUltimaConsulta() - ultima {}", consulta);
			return consultaView;
		} catch (Exception e) {
			ConsultaException ee = new ConsultaException("Ocurrio un problema al obtener la ultima consulta", ConsultaException.LAYER_DAO, ConsultaException.ACTION_VALIDATE);
			logger.error("getUltimaConsulta() - idPaciente: {} - CODE {} - {}", idPaciente, ee.getExceptionCode(), ee);
			throw ee;
		}
	}

	@Transactional(readOnly = false, rollbackFor = {ConsultaException.class})
	@Override
	public void confirmarConsulta(Long idConsulta, CatEstadoConsultaView catEstadoConsultaView) throws ConsultaException {
		try {
			final Integer idEstadoConsulta = 5;
			Consulta consulta = consultaRepository.findOne(idConsulta);
			if (idEstadoConsulta != catEstadoConsultaView.getIdEstadoConsulta()) {
				ConsultaException ee = new ConsultaException("No se pudo cambiar el cambiar el estado de la consulta", ConsultaException.LAYER_DAO, ConsultaException.ACTION_VALIDATE);
				ee.addError("El catEstadoConsulta que se le quiere asignar no coincide con la acción de confirmar");
				logger.error("confirmarConsulta() - El idestadoConsulta para confirmar es:{}, se le quiere asginar otro estado:{}", idEstadoConsulta, catEstadoConsultaView);
				throw ee;
			}
			if (!catEstadoConsultaView.getActivo()) {
				ConsultaException ee = new ConsultaException("No se pudo cambiar el cambiar el estado de la consulta", ConsultaException.LAYER_DAO, ConsultaException.ACTION_VALIDATE);
				ee.addError("La acción de confirmar consulta a sido desactivada");
				logger.error("confirmarConsulta() - El estadoConsulta que se le quiere asignar esta desactivado: {}", catEstadoConsultaView);
				throw ee;
			}
			if (consulta == null) {
				ConsultaException ee = new ConsultaException("No se pudo cambiar el cambiar el estado de la consulta", ConsultaException.LAYER_DAO, ConsultaException.ACTION_VALIDATE);
				ee.addError("La consulta no existe");
				logger.error("confirmarConsulta() - La consulta con el id:{} no existe en la DB ", idConsulta);
				throw ee;
			}
			if (consulta.getIdEstadoConsulta() != 1) {
				ConsultaException ee = new ConsultaException("No se pudo cambiar el estado de la consulta", ConsultaException.LAYER_DAO, ConsultaException.ACTION_VALIDATE);
				ee.addError("La consulta esta: " + consulta.getEstadoConsulta());
				logger.error("confirmarConsulta() - La consulta con el id:{} esta en un estado:{} ", idConsulta, consulta.getEstadoConsulta());
				throw ee;
			}
			consulta.setIdEstadoConsulta(idEstadoConsulta);
			consulta.setEstadoConsulta(catEstadoConsultaView.getDescripcion());
			logger.info("confirmarConsulta() - cambiando estatus de consulta con el id:{} a Confirmada", idConsulta);
			consultaRepository.save(consulta);
		} catch (ConsultaException ce) {
			throw ce;
		} catch (Exception e) {
			ConsultaException ee = new ConsultaException("No se pudo cambiar el estado de la consulta", ConsultaException.LAYER_DAO, ConsultaException.ACTION_VALIDATE);
			logger.info("confirmarConsulta() - Ocurrio un erro al cambiar el estado de la consulta - error:{}", e);
			throw ee;
		}
	}

	@Override
	public ConsultaView getSiguienteConsulta(UUID idPaciente) throws ConsultaException {
		try {
			Consulta consulta = consultaRepository.findConsultaSiguiente(idPaciente, new Date());
			if (consulta == null) {
				logger.info("getSiguienteConsulta() - idPaciente:{} no tiene consultas Nuevas o En curso", idPaciente);
				return null;
			}
			ConsultaView consultaView = consultaConverter.toView(consulta, Boolean.TRUE);
			logger.info("getSiguienteConsulta() - siguinte consulta {} para el idPaciente:{}", consulta, idPaciente);
			return consultaView;
		} catch (DataIntegrityViolationException dv) {
			ConsultaException ee = new ConsultaException("Ocurrio un proble al obtener la siguiete consulta", ConsultaException.LAYER_DAO, ConsultaException.ACTION_VALIDATE);
			logger.error("getSiguienteConsulta() -Ocurrio un erro al obtener la siguite consulte del idPaciente: {} - CODE {} - {}", idPaciente, ee.getExceptionCode(), dv.getMessage());
			throw ee;
		} catch (Exception e) {
			ConsultaException ee = new ConsultaException("Ocurrio un proble al Obtener la siguiente consulta", ConsultaException.LAYER_DAO, ConsultaException.ACTION_VALIDATE);
			logger.error("getSiguienteConsulta() - Ocurrio un erro al obtener la siguite consulte del idPaciente:{} - CODE {} - {}", idPaciente, ee.getExceptionCode(), e);
			throw ee;
		}
	}

	@Transactional(readOnly = false, rollbackFor = {ConsultaException.class})
	@Override
	public void enfermeriaConsulta(Long idConsulta, CatEstadoConsultaView catEstadoConsultaView) throws ConsultaException {
		try {
			Consulta consulta = consultaRepository.findOne(idConsulta);
			final Integer idEstadoConsulta = 6;
			if (idEstadoConsulta != catEstadoConsultaView.getIdEstadoConsulta()) {
				ConsultaException ee = new ConsultaException("No se pudo cambiar el cambiar el estado de la consulta", ConsultaException.LAYER_DAO, ConsultaException.ACTION_VALIDATE);
				ee.addError("El catEstadoConsulta que se le quiere asignar no coincide con la acción enfermería");
				logger.error("enfermeriaConsulta() - El idestadoConsulta para enfermería es:{}, se le quiere asginar otro estado:{}", idEstadoConsulta, catEstadoConsultaView);
				throw ee;
			}
			if (!catEstadoConsultaView.getActivo()) {
				ConsultaException ee = new ConsultaException("No se pudo cambiar el cambiar el estado de la consulta", ConsultaException.LAYER_DAO, ConsultaException.ACTION_VALIDATE);
				ee.addError("La acción de confirmar consulta a sido desactivada");
				logger.error("enfermeriaConsulta() - El estadoConsulta que se le quiere asignar esta desactivado: {}", catEstadoConsultaView);
				throw ee;
			}
			if (consulta == null) {
				ConsultaException ee = new ConsultaException("No se pudo cambiar el estado de la consulta", ConsultaException.LAYER_DAO, ConsultaException.ACTION_VALIDATE);
				ee.addError("La consulta no se encuentra en la Base de Datos");
				logger.error("enfermeriaConsulta() - La consulta con el id:{} no existe ", idConsulta);
				throw ee;
			}
			if (consulta.getIdEstadoConsulta() != 1 && consulta.getIdEstadoConsulta() != 5) {
				ConsultaException ee = new ConsultaException("No se pudo cambiar el estado de la consulta", ConsultaException.LAYER_DAO, ConsultaException.ACTION_VALIDATE);
				ee.addError("La consulta esta: " + consulta.getEstadoConsulta());
				logger.error("confirmarConsulta() - La consulta con el id:{} esta en un estado:{} ", idConsulta, consulta.getEstadoConsulta());
				throw ee;
			}
			consulta.setIdEstadoConsulta(idEstadoConsulta);
			consulta.setEstadoConsulta(catEstadoConsultaView.getDescripcion());
			consultaRepository.save(consulta);
			logger.info("enfermeriaConsulta() - La consulta con el id:{} ahora tiene un estatus:{}", idConsulta, catEstadoConsultaView.getDescripcion());
		} catch (ConsultaException ce) {
			throw ce;
		} catch (DataIntegrityViolationException dive) {
			ConsultaException ee = new ConsultaException("Ocurrio un error al cambiar el estatus de la consulta en la  Base de Datos", ConsultaException.LAYER_DAO, ConsultaException.ACTION_SELECT);
			logger.error("updateConsulta() - idConsulta: {} - CODE {} - {}", idConsulta, ee.getExceptionCode(), dive);
			throw ee;
		} catch (Exception e) {
			ConsultaException ee = new ConsultaException("Ocurrio un proble al Cambiar de estatus la consulta", ConsultaException.LAYER_DAO, ConsultaException.ACTION_VALIDATE);
			logger.error("enfermeriaConsulta() - Ocurrio un problema al Cambiar de estatus la consulta:{} - CODE {} - {}", idConsulta, ee.getExceptionCode(), e);
			throw ee;
		}
	}
	private void validaciones(ConsultaView view) throws ConsultaException {
		ConsultaException ce;
		if(view.getIdTipoConsulta()== 2 || view.getIdTipoConsulta()==3 ) {
			if(view.getNombreMedicoSolicitado() == null || view.getNombreMedicoSolicitado().isEmpty()) {
				ce = new ConsultaException("Errores de validación.", ConsultaException.LAYER_DAO, ConsultaException.ACTION_VALIDATE);
				ce.addError("EL nombre del Medico Solicitado  no puede ser Null/Vacio");
				logger.info("validaciones() -  EL nombre del Medico Solicitante  no puede ser Null/Vacio");
				throw ce;
			}
			if(view.getIdMedicoSolicitado()== null) {
				ce = new ConsultaException("Errores de validación.", ConsultaException.LAYER_DAO, ConsultaException.ACTION_VALIDATE);
				ce.addError("EL ID del Medico Solicitado  no puede ser Null");
				logger.info("validaciones() -  EL ID del Medico Solicitante  no puede ser Null/Vacio");
				throw ce;
			}
			if(view.getEspecialidadMedicoSolicitado() == null || view.getEspecialidadMedicoSolicitado().isEmpty()) {
				ce = new ConsultaException("Errores de validación.", ConsultaException.LAYER_DAO, ConsultaException.ACTION_VALIDATE);
				ce.addError("La especialidad del Medico Solicitado  no puede ser Null/Vacio");
				logger.info("validaciones() -  La Especialidad del Medico Solicitante  no puede ser Null/Vacio");
				throw ce;
			}
		}
		if(view.getIdTipoConsulta() !=3) {
			view.setIncapacidadTemporal(null);
		}
		if(view.getIdTipoConsulta()==3) {
			if(view.getIdServicio() == null) {
				ce = new ConsultaException("Errores de validación.", ConsultaException.LAYER_DAO, ConsultaException.ACTION_VALIDATE);
				ce.addError("El ID Servicio  no puede ser Null/Vacio");
				logger.info("validaciones() - El id Servicio  no puede ser Null/Vacio");
				throw ce; 
			}
			if(view.getServicio() == null || view.getServicio().isEmpty()) {
				ce = new ConsultaException("Errores de validación.", ConsultaException.LAYER_DAO, ConsultaException.ACTION_VALIDATE);
				ce.addError("El Servicio  no puede ser Null/Vacio");
				logger.info("validaciones() - El Servicio  no puede ser Null/Vacio");
				throw ce;
			}
			if(view.getIdMotivoEnvio() == null) {
				ce = new ConsultaException("Errores de validación.", ConsultaException.LAYER_DAO, ConsultaException.ACTION_VALIDATE);
				ce.addError("El ID motivo envio  no puede ser Null/Vacio");
				logger.info("validaciones() - El ID motivo envio  no puedee ser Null/Vacio");
				throw ce;
			}
			if(view.getMotivoEnvio() == null || view.getMotivoEnvio().isEmpty()) {
				ce = new ConsultaException("Errores de validación.", ConsultaException.LAYER_DAO, ConsultaException.ACTION_VALIDATE);
				ce.addError("El motivo envio  no puede ser Null/Vacio");
				logger.info("validaciones() - El motivo envio  no puedee ser Null/Vacio");
				throw ce;
			}
		}
	}

	@Override
	public void createCDA(Long idConsulta) throws ConsultaException {
		try {
			logger.info("createCDA() - Mapeando los dotos de la conslta al XML"); 
			String xml="{\n" + 
					"    \"idConsulta\" : \"PARAM1\",\n" + 
					"    \"numeroExpediente\" : \"PARAM2\",\n" + 
					"    \"nota\" : \"<?xml-stylesheet type=\\\"text/xsl\\\" href=\\\"cda/CDATC.xsl\\\"?>\n" + 
					"  <ClinicalDocument xmlns=\\\"urn:hl7-org:v3\\\" xmlns:xsi=\\\"http://www.w3.org/2001/XMLSchema-instance\\\" xsi:schemaLocation=\\\"urn:hl7-org:v3 cda/CDA.xsd\\\">\n" + 
					"     <realmCode code=\\\"MX\\\" />\n" + 
					"     <typeId root=\\\"2.16.840.1.113883.1.3\\\" extension=\\\"POCD_HD000040\\\" />\n" + 
					"     <templateId root=\\\"2.16.840.1.113883.3.215.3.11.1.2\\\" />\n" + 
					"     <id root=\\\"2.16.840.1.113883.3.215.5.62.3\\\" extension=\\\"805db849-1281-4fc6-9c88-c1cc1f122950\\\" />\n" + 
					"     <code code=\\\"57133-1\\\" codeSystem=\\\"2.16.840.1.113883.6.1\\\" codeSystemName=\\\"LOINC\\\" displayName=\\\"Referral note\\\" />\n" + 
					"     <title>Nota de Referencia</title>\n" + 
					"     <effectiveTime value=\\\"20190115172434\\\" />\n" + 
					"     <confidentialityCode code=\\\"N\\\" codeSystem=\\\"2.16.840.1.113883.5.25\\\" />\n" + 
					"     <languageCode code=\\\"es-MX\\\" />\n" + 
					"     <setId root=\\\"2.16.840.1.113883.3.215.5.62.3\\\" extension=\\\"805db849-1281-4fc6-9c88-c1cc1f122950\\\" />\n" + 
					"     <versionNumber value=\\\"1\\\" />\n" + 
					"     <!-- [1..1] El Paciente -->\n" + 
					"     <recordTarget>\n" + 
					"        <patientRole>\n" + 
					"           <!-- [1..1] Identificador ÚNICO del paciente en el sistema solicitante. - root: OID de la tabla de paciente en el sistema solicitante. - extension: identificador de paciente en el sistema solicitante. -->\n" + 
					"           <id root=\\\"2.16.840.1.113883.3.215.5.62.1\\\" extension=\\\"d5c2962c-1da3-4a1e-b0f3-c85523970739\\\" assigningAuthorityName=\\\"d5c2962c-1da3-4a1e-b0f3-c85523970739\\\" />\n" + 
					"         <patient>\n" + 
					"              <!-- [1..1] Nombre del paciente, segundo apellido opcional -->\n" + 
					"              <name>\n" + 
					"                 <given>PARAM_NOMBRE_PACIENTE</given>\n" + 
					"                 <family>PARAM_APELLIDO</family>\n" + 
					"                 <!-- [0..1] -->\n" + 
					"                 <family>PARAM_SEGUNDO_APELLIDO</family>               \n" + 
					"              </name>\n" + 
					"              \n" + 
					"              <!-- [1..1] Sexo del paciente. Posibles valores: F->Femenino; M->Masculino; U->Indefinido; UNK->Desconocido -->\n" + 
					"              <administrativeGenderCode codeSystem=\\\"2.16.840.1.113883.5.1\\\" code=\\\"PARAM_SEXO\\\" />\n" + 
					"              <!-- [1..1] Fecha de nacimiento del paciente. -->\n" + 
					"              <birthTime value=\\\"19890723000000\\\" />\n" + 
					"              <!-- [0..1] Estado Civil según vocabulario HL7 V3 MaritalStatus A Annulled D Divorced I Interlocutory L Legally Separated M Married P Polygamous S Never Married T Domestic partner W Widowed -->\n" + 
					"              <maritalStatusCode code=\\\"\\\" codeSystem=\\\"2.16.840.1.113883.5.2\\\" codeSystemName=\\\"MaritalStatusCode\\\" />\n" + 
					"              <!-- [0..1] Religión de acuerdo al catálogo de INEGI-->\n" + 
					"              <religiousAffiliationCode codeSystem=\\\"2.16.840.1.113883.3.215.12.11\\\" codeSystemName=\\\"Religiones INEGI\\\" code=\\\"\\\" />\n" + 
					"              <!-- [0..1] Etnia según catálogo INEGI de lenguas indígenas -->\n" + 
					"              <ethnicGroupCode codeSystem=\\\"2.16.840.1.113883.3.215.12.10\\\" codeSystemName=\\\"Lenguas Indígenas INEGI\\\" code=\\\"\\\" />\n" + 
					"              <birthplace>\n" + 
					"                 <place>\n" + 
					"                    <addr>\n" + 
					"                       <!-- [1..1] Clave de la Entidad federativa de nacimiento de acuerdo al catálogo de INEGI-->\n" + 
					"                       <state>PARAM_ESTADO</state>\n" + 
					"                       <!--[1..1] Clave de NACIONALIDAD de acuerdo al catálogo de nacionalidades RENAPO-->\n" + 
					"                       <country />\n" + 
					"                    </addr>\n" + 
					"                 </place>\n" + 
					"              </birthplace>\n" + 
					"           </patient>\n" + 
					"        </patientRole>\n" + 
					"     </recordTarget>\n" + 
					"     <!-- [1..1] Autor del documento (médico solicitante) -->\n" + 
					"     <author>\n" + 
					"        <!-- [1..1] Fecha de firma el documento. Es la misma que el atributo ClinicalDocument/effectiveTime/@value-->\n" + 
					"        <time value=\\\"20190115232434\\\" />\n" + 
					"        <assignedAuthor>\n" + 
					"           <!-- [1..1] Identificación por usuario en sistema solicitante -->\n" + 
					"           <id root=\\\"2.16.840.1.113883.3.215.5.62.2\\\" extension=\\\"843372ca-0bd3-452e-b46e-3f47e3e88b50\\\" />\n" + 
					"           <!-- [1..1] Identificación del médico por cédula profesional -->\n" + 
					"           <id root=\\\"2.16.840.1.113883.3.215.12.18\\\" extension=\\\"2123009032\\\" />\n" + 
					"           <!-- [0..1] Especialidad médica. -->\n" + 
					"           <code code=\\\"71115-03\\\" codeSystem=\\\"2.16.840.1.113883.3.215.3.12.9\\\" displayName=\\\"Medico General\\\" />\n" + 
					"           <!-- [1..1] Dirección del autor (Valor Fijo a null). -->\n" + 
					"           <addr nullFlavor=\\\"UNK\\\" />\n" + 
					"           <!-- [1..1] Nombre completo del solicitante. Importante para poder mostrarlo en la cabecera del documento renderizado a html/pdf-->\n" + 
					"           <assignedPerson>\n" + 
					"              <name>\n" + 
					"                 <given>Celene </given>\n" + 
					"                 <family>Muñoz</family>\n" + 
					"                 <!-- [0..1] -->\n" + 
					"                 <family>Martinez</family>\n" + 
					"              </name>\n" + 
					"           </assignedPerson>\n" + 
					"           <!-- [1..1] Organización del autor -->\n" + 
					"           <representedOrganization>\n" + 
					"              <!-- [1..1] OID de la organización a la que pertenece el autor del sistema-->\n" + 
					"              <id root=\\\"2.16.840.1.113883.3.215.5.50\\\" />\n" + 
					"              <name>IMSS</name>\n" + 
					"           </representedOrganization>\n" + 
					"        </assignedAuthor>\n" + 
					"     </author>\n" + 
					"     <!-- [1..1] Software que genera el CDA -->\n" + 
					"     <author>\n" + 
					"        <!-- [1..1] Fecha de firma el documento. Es la misma que el atributo ClinicalDocument/effectiveTime/@value-->\n" + 
					"        <time value=\\\"20190115172434\\\" />\n" + 
					"        <assignedAuthor>\n" + 
					"           <!-- [1..1] OID del Software -->\n" + 
					"           <id root=\\\"2.16.840.1.113883.3.215.5.62\\\" />\n" + 
					"           <!-- [1..1] Dirección del autor (Valor Fijo a null). -->\n" + 
					"           <addr nullFlavor=\\\"UNK\\\" />\n" + 
					"           <assignedAuthoringDevice>\n" + 
					"              <!-- [1..1] Nombre del sistema (Fabricante, Producto, Versión) que genera el documento electrónico -->\n" + 
					"              <softwareName>Niomedic 1.0.1</softwareName>\n" + 
					"           </assignedAuthoringDevice>\n" + 
					"           <representedOrganization>\n" + 
					"              <!-- OID de la institución responsable de la autoría del documento electrónico -->\n" + 
					"              <id root=\\\"2.16.840.1.113883.3.215.5.62\\\" />\n" + 
					"              <!-- Nombre de la institución responsable de la autoría del documento -->\n" + 
					"              <name>IMSS Telemedicina</name>\n" + 
					"           </representedOrganization>\n" + 
					"        </assignedAuthor>\n" + 
					"     </author>\n" + 
					"     <!-- [0..1] Capturista -->\n" + 
					"     <dataEnterer>\n" + 
					"        <!-- [1..1] Fecha de firma el documento. Es la misma que el atributo ClinicalDocument/effectiveTime/@value-->\n" + 
					"        <time value=\\\"PARAM_DATENOW\\\" />\n" + 
					"        <assignedEntity>\n" + 
					"           <!-- [1..1] Identificación por usuario en sistema solicitante -->\n" + 
					"           <id root=\\\"2.16.840.1.113883.3.215.5.62.2\\\" extension=\\\"843372ca-0bd3-452e-b46e-3f47e3e88b50\\\" />\n" + 
					"           <assignedPerson>\n" + 
					"              <name>\n" + 
					"                 <given>Celene</given>\n" + 
					"                 <family>Muñoz</family>\n" + 
					"                 <!-- [0..1] -->\n" + 
					"                 <family>Martinez</family>\n" + 
					"              </name>\n" + 
					"           </assignedPerson>\n" + 
					"        </assignedEntity>\n" + 
					"     </dataEnterer>\n" + 
					"    <!-- [1..1] Responsable de custodiar el CDA (CENETEC en este proyecto). -->\n" + 
					"     <custodian>\n" + 
					"        <assignedCustodian>\n" + 
					"           <representedCustodianOrganization>\n" + 
					"              <id root=\\\"2.16.840.1.113883.3.215.5.50\\\" />\n" + 
					"              <name>IMSS Telemedicina</name>\n" + 
					"              <telecom value=\\\"\\\" use=\\\"WP\\\" />\n" + 
					"              <addr use=\\\"WP\\\">\n" + 
					"                 <precinct />\n" + 
					"                 <county />\n" + 
					"                 <state />\n" + 
					"                 <postalCode />\n" + 
					"                 <country />\n" + 
					"              </addr>\n" + 
					"           </representedCustodianOrganization>\n" + 
					"        </assignedCustodian>\n" + 
					"     </custodian>\n" + 
					"     <!-- [1..1] Destinatario de esta nota (Medico consultado) -->\n" + 
					"     <informationRecipient>\n" + 
					"        <intendedRecipient>\n" + 
					"           <!-- [0..1] Identificación del médico por cédula profesional -->\n" + 
					"           <id root=\\\"2.16.840.1.113883.3.215.12.18\\\" extension=\\\"IB123\\\" />\n" + 
					"           <!-- [1..1] Nombre completo del médico consultado. Importante para poder mostrarlo en la cabecera del documento renderizado a html/pdf-->\n" + 
					"           <informationRecipient>\n" + 
					"              <name>\n" + 
					"                 <given></given>\n" + 
					"                 <family></family>\n" + 
					"                 <!-- [0..1] -->\n" + 
					"                 <family></family>\n" + 
					"              </name>\n" + 
					"           </informationRecipient>\n" + 
					"           <!-- [1..1] Establecimiento donde está el médico consultado-->\n" + 
					"           <receivedOrganization>\n" + 
					"              <!-- [1..1] OID de la organziación a la que pertenece el médico consultado -->\n" + 
					"              <id root=\\\"2.16.840.1.113883.3.215.5.50\\\" />\n" + 
					"              <!-- Nombre de la organziación a la que pertenece el médico consultado -->\n" + 
					"              <name>IMSS Tapachula</name>\n" + 
					"           </receivedOrganization>\n" + 
					"        </intendedRecipient>\n" + 
					"    </informationRecipient>\n" + 
					"     <!-- [1..1] Responsable legal \\\"firmante\\\" del documento (médico solicitante)-->\n" + 
					"     <legalAuthenticator>\n" + 
					"        <!-- [1..1] Fecha de firma el documento.Es la misma que el atributo ClinicalDocument/effectiveTime/@value-->\n" + 
					"        <time value=\\\"PARAM_DATENOW\\\" />\n" + 
					"        <!-- [1..1] Documento está firmado (Valor Fijo). -->\n" + 
					"        <signatureCode code=\\\"S\\\" />\n" + 
					"        <!-- Médico solicitante -->\n" + 
					"        <assignedEntity>\n" + 
					"           <!-- [1..1] Identificación por usuario en sistema solicitante -->\n" + 
					"           <id root=\\\"2.16.840.1.113883.3.215.5.62.2\\\" extension=\\\"843372ca-0bd3-452e-b46e-3f47e3e88b50\\\" />\n" + 
					"           <!-- [1..1] Identificación del médico por cédula profesional -->\n" + 
					"           <id root=\\\"2.16.840.1.113883.3.215.12.18\\\" extension=\\\"2123009032\\\" />\n" + 
					"           <!-- [0..1] Especialidad médica. -->\n" + 
					"           <code code=\\\"71115-03\\\" codeSystem=\\\"2.16.840.1.113883.3.215.3.12.9\\\" displayName=\\\"Medico General\\\" />\n" + 
					"           <!-- [1..1] Dirección del autor (Valor Fijo a null). -->\n" + 
					"           <addr nullFlavor=\\\"UNK\\\" />\n" + 
					"           <!-- [1..1] Mail del solicitante. Si no se conoce, eliminar value y añadir: nullFlavor=\\\"UNK\\\" <telecom use=\\\"WP\\\" value=\\\"mailto:autor@doamin.com\\\"/> -->\n" + 
					"           <telecom nullFlavor=\\\"UNK\\\" />\n" + 
					"           <!-- [1..1] Nombre completo del solicitante. Importante para poder mostrarlo en la cabecera del documento renderizado a html/pdf-->\n" + 
					"           <assignedPerson>\n" + 
					"              <name>\n" + 
					"                 <given>Celene</given>\n" + 
					"                 <family>Muñoz</family>\n" + 
					"                 <!-- [0..1] -->\n" + 
					"                 <family>Martinez</family>\n" + 
					"              </name>\n" + 
					"           </assignedPerson>\n" + 
					"           <!-- [1..1] Organización del autor -->\n" + 
					"           <representedOrganization>\n" + 
					"              <!-- [1..1] OID de la organización a la que pertenece el autor del sistema-->\n" + 
					"              <id root=\\\"2.16.840.1.113883.3.215.5.50\\\" />\n" + 
					"              <name>IMSS Tapachula</name>\n" + 
					"           </representedOrganization>\n" + 
					"        </assignedEntity>\n" + 
					"     </legalAuthenticator>\n" + 
					"     <!-- [1..1] Responsable legal \\\"firmante\\\" del documento -->\n" + 
					"     <authenticator>\n" + 
					"        <!-- [1..1] Fecha de firma el documento. Es la misma que el atributo ClinicalDocument/effectiveTime/@value-->\n" + 
					"        <time value=\\\"PARAM_DATENOW\\\" />\n" + 
					"        <!-- [1..1] Documento está firmado (Valor Fijo). -->\n" + 
					"        <signatureCode code=\\\"S\\\" />\n" + 
					"        <!-- Médico solicitante -->\n" + 
					"        <assignedEntity>\n" + 
					"           <!-- [1..1] Identificación por usuario en sistema solicitante -->\n" + 
					"           <id root=\\\"2.16.840.1.113883.3.215.5.62.2\\\" extension=\\\"843372ca-0bd3-452e-b46e-3f47e3e88b50\\\" />\n" + 
					"           <!-- [1..1] Identificación del médico por cédula profesional -->\n" + 
					"           <id root=\\\"2.16.840.1.113883.3.215.12.18\\\" extension=\\\"2123009032\\\" />\n" + 
					"           <!-- [0..1] Especialidad médica. -->\n" + 
					"           <code code=\\\"71115-03\\\" codeSystem=\\\"2.16.840.1.113883.3.215.3.12.9\\\" displayName=\\\"Medicina General\\\" />\n" + 
					"           <!-- [1..1] Dirección del autor (Valor Fijo a null). -->\n" + 
					"           <addr nullFlavor=\\\"UNK\\\" />\n" + 
					"           <!-- [1..1] Mail del solicitante. Si no se conoce, eliminar value y añadir: nullFlavor=\\\"UNK\\\" <telecom use=\\\"WP\\\" value=\\\"mailto:autor@doamin.com\\\"/> -->\n" + 
					"           <telecom nullFlavor=\\\"UNK\\\" />\n" + 
					"           <!-- [1..1] Nombre completo del solicitante. Importante para poder mostrarlo en la cabecera del documento renderizado a html/pdf-->\n" + 
					"           <assignedPerson>\n" + 
					"              <name>\n" + 
					"                 <given>Celene</given>\n" + 
					"                 <family>Muñoz</family>\n" + 
					"                 <!-- [0..1] -->\n" + 
					"                 <family>Martinez</family>\n" + 
					"              </name>\n" + 
					"           </assignedPerson>\n" + 
					"           <!-- [1..1] Organización del autor -->\n" + 
					"           <representedOrganization>\n" + 
					"              <!-- [1..1] OID de la organización a la que pertenece el autor del sistema-->\n" + 
					"              <id root=\\\"TAPACHULA\\\" />\n" + 
					"              <name>IMSS Tapachula</name>\n" + 
					"           </representedOrganization>\n" + 
					"        </assignedEntity>\n" + 
					"     </authenticator>\n" + 
					"     <!-- [1..1] Solicitud de servicio relacionada -->\n" + 
					"     <inFulfillmentOf>\n" + 
					"        <order>\n" + 
					"           <!-- [1..1] Identificación en el sistema solicitante -->\n" + 
					"           <id root=\\\"2.16.840.1.113883.3.215.5.62.5\\\" extension=\\\"d4ce3fe6-b30b-4cd5-953b-2ecd90de9cd6\\\" />\n" + 
					"        </order>\n" + 
					"     </inFulfillmentOf>\n" + 
					"     <!-- [1..1] Datos del encuentro (previo a la interconsulta)-->\n" + 
					"     <componentOf>\n" + 
					"        <encompassingEncounter>\n" + 
					"           <!-- [1..1] Identificador del encuentro en el sistema solicitante-->\n" + 
					"           <id root=\\\"\\\" extension=\\\"HIS EHCOS\\\" />\n" + 
					"           <!-- Tipo de atencion que se le esta brindando al paciente en el establecimiento solicitante: AMB - Ambulatorio EMER - Emergencia IMP - Hospitalización SS - Corta Estancia HH - Casero FLD - Fuera del establecimiento de salud VR - Virtual (Telesalud) -->\n" + 
					"           <code codeSystem=\\\"2.16.840.1.113883.5.4\\\" codeSystemName=\\\"actCode\\\" code=\\\"VR\\\" />\n" + 
					"           <effectiveTime>\n" + 
					"              <!-- [1..1] Momento de inicio -->\n" + 
					"              <low value=\\\"20190115172019\\\" />\n" + 
					"           </effectiveTime>\n" + 
					"           <!-- Datos del responsable de la atencion en la institucion solicitante-->\n" + 
					"           <responsibleParty>\n" + 
					"              <assignedEntity>\n" + 
					"                 <!-- [1..1] Identificación por usuario en sistema solicitante -->\n" + 
					"                 <id root=\\\"2.16.840.1.113883.3.215.5.62.2\\\" extension=\\\"843372ca-0bd3-452e-b46e-3f47e3e88b50\\\" />\n" + 
					"                 <!-- [1..1] Identificación del médico por cédula profesional -->\n" + 
					"                 <id root=\\\"2.16.840.1.113883.3.215.12.18\\\" extension=\\\"2123009032\\\" />\n" + 
					"                 <!-- [1..1] Nombre completo del solicitante. Importante para poder mostrarlo en la cabecera del documento renderizado a html/pdf-->\n" + 
					"                 <assignedPerson>\n" + 
					"                    <name>\n" + 
					"                       <given>Celene</given>\n" + 
					"                       <family>Muñoz</family>\n" + 
					"                       <!-- [0..1] -->\n" + 
					"                       <family>Martinez</family>\n" + 
					"                    </name>\n" + 
					"                 </assignedPerson>\n" + 
					"                 <!-- [1..1] Organización a la que pertenece el medico -->\n" + 
					"                 <representedOrganization>\n" + 
					"                    <!-- [1..1] OID de la organización a la que pertenece el médico -->\n" + 
					"                    <id root=\\\"2.16.840.1.113883.3.215.5.50\\\" />\n" + 
					"                    <!-- [1..1] Nombre de la organización a la que pertenece el médico -->\n" + 
					"                    <name>IMSS Telemedicina</name>\n" + 
					"                 </representedOrganization>\n" + 
					"              </assignedEntity>\n" + 
					"           </responsibleParty>\n" + 
					"           <!-- [1..1] Establecimiento donde están el médico solicitante y el paciente -->\n" + 
					"           <location>\n" + 
					"              <healthCareFacility>\n" + 
					"                 <!-- [1..1] CLUES del establecimiento -->\n" + 
					"                 <id root=\\\"2.16.840.1.113883.4.631\\\" extension=\\\"CLINICA\\\" />\n" + 
					"                 <location>\n" + 
					"                    <!-- Nombre del establecimiento -->\n" + 
					"                    <name>IMSS Tapachula</name>\n" + 
					"                    <!-- [1..1] Dirección del establecimiento -->\n" + 
					"                    <addr>\n" + 
					"                       <state>Chiapas</state>\n" + 
					"                       <postalCode>CP 30700</postalCode>\n" + 
					"                       <country>México</country>\n" + 
					"                    </addr>\n" + 
					"                 </location>\n" + 
					"              </healthCareFacility>\n" + 
					"           </location>\n" + 
					"        </encompassingEncounter>\n" + 
					"     </componentOf>\n" + 
					"     <!-- CUERPO ESTRUCTURADO. El texto narrativo debe codificarse en formato HL7 Narrative Block. Revisar el estándar para más detalle: HL7 CDA Release 2 (http://www.hl7.org/documentcenter/private/standards/cda/r2/cda_r2_normativewebedition2010.zip) Si no se requieren decoraciones puede insertarse directamente el texto \\\"plano\\\". Si por algunas de las secciones no se dispone del contenido, ya sea porque se desconoce o no se ha preguntado al paciente, indicar esta situación como texto de la sección. -->\n" + 
					"     <component>\n" + 
					"        <structuredBody>\n" + 
					"           <!-- [1..1] SECCIÓN MOTIVO DE LA INTERCONSULTA -->\n" + 
					"           <component>\n" + 
					"              <section>\n" + 
					"                 <!-- [1..1] Tipo de sección LOINC -->\n" + 
					"                 <code code=\\\"42349-1\\\" codeSystem=\\\"2.16.840.1.113883.6.1\\\" codeSystemName=\\\"LOINC\\\" displayName=\\\"Reason of referal\\\" />\n" + 
					"                 <!-- [1..1] Título de la sección -->\n" + 
					"                 <title>Motivo de la interconsulta</title>\n" + 
					"                 <!-- [1..1] Texto -->\n" + 
					"                 <text>PARAM_MOTIVOCONSULTA</text>\n" + 
					"              </section>\n" + 
					"           </component>\n" + 
					"           <!-- [1..1] SECCIÓN MOTIVO RESUMEN DEL INTERROGATORIO -->\n" + 
					"           <component>\n" + 
					"              <section>\n" + 
					"                 <!-- [1..1] Tipo de sección LOINC -->\n" + 
					"                 <code code=\\\"10164-2\\\" codeSystem=\\\"2.16.840.1.113883.6.1\\\" codeSystemName=\\\"LOINC\\\" displayName=\\\"Hisotry of Present Ilness\\\" />\n" + 
					"                 <!-- [1..1] Título de la sección -->\n" + 
					"                 <title>Nota de Interconsulta</title>\n" + 
					"                 <!-- [1..1] Texto -->\n" + 
					"                 <text>PARAM_SUBJETIVO</text>\n" + 
					"              </section>\n" + 
					"           </component>\n" + 
					"           <!-- [1..1] SECCIÓN PROCEDIMINETOS -->\n" + 
					"          <component>\n" + 
					"              <section>\n" + 
					"                 <!-- [1..1] Tipo de sección LOINC -->\n" + 
					"                 <code code=\\\"47519-4\\\" codeSystem=\\\"2.16.840.1.113883.6.1\\\" codeSystemName=\\\"LOINC\\\" displayName=\\\"Procedures\\\" />\n" + 
					"                 <!-- [1..1] Título de la sección -->\n" + 
					"                 <title>Procedimientos quirúrgicos y terapéuticos</title>\n" + 
					"                 <!-- [1..1] Texto -->\n" + 
					"                 <text>PARAM_PLAN(SOAP)</text>\n" + 
					"              </section>\n" + 
					"           </component>\n" + 
					"           <!-- [1..1] SECCIÓN TRATAMINETO FARMACOLÓGICO -->\n" + 
					"           <component>\n" + 
					"              <section>\n" + 
					"                 <!-- [1..1] Tipo de sección LOINC -->\n" + 
					"                 <code code=\\\"10160-0\\\" codeSystem=\\\"2.16.840.1.113883.6.1\\\" codeSystemName=\\\"LOINC\\\" displayName=\\\"Medications\\\" />\n" + 
					"                 <!-- [1..1] Título de la sección -->\n" + 
					"                 <title>Tratamiento farmacológico</title>\n" + 
					"                 <!-- [1..1] Texto -->\n" + 
					"                 <text>PARAM_CIE9</text>\n" + 
					"              </section>\n" + 
					"           </component>\n" + 
					"           <!-- [1..1] SECCIÓN EXPLORACIÓN FÍSICA -->\n" + 
					"           <component>\n" + 
					"              <section>\n" + 
					"                 <!-- [1..1] Tipo de sección LOINC -->\n" + 
					"                 <code code=\\\"29545-1\\\" codeSystem=\\\"2.16.840.1.113883.6.1\\\" codeSystemName=\\\"LOINC\\\" displayName=\\\"Physical Exam\\\" />\n" + 
					"                 <!-- [1..1] Título de la sección -->\n" + 
					"                 <title>Exploración Física</title>\n" + 
					"                 <!-- [1..1] Texto -->\n" + 
					"                 <text>PARAM_OBJETIVO</text>\n" + 
					"              </section>\n" + 
					"           </component>\n" + 
					"           <!-- [1..1] SECCIÓN SIGNOS VITALES -->\n" + 
					"           <component>\n" + 
					"              <section>\n" + 
					"                 <!-- [1..1] Tipo de sección LOINC -->\n" + 
					"                 <code code=\\\"8716-3\\\" codeSystem=\\\"2.16.840.1.113883.6.1\\\" codeSystemName=\\\"LOINC\\\" displayName=\\\"Vital Signs\\\" />\n" + 
					"                 <!-- [1..1] Título de la sección -->\n" + 
					"                 <title>Signos vitales</title>\n" + 
					"                 <!-- [1..1] Texto -->\n" + 
					"                 <text>PARAM_SIGNOSVITALES_JSON</text>\n" + 
					"              </section>\n" + 
					"           </component>\n" + 
					"           <!-- [1..1] SECCIÓN IMPRESIÓN DIAGNÓSTICA -->\n" + 
					"           <component>\n" + 
					"              <section>\n" + 
					"                 <!-- [1..1] Tipo de sección LOINC -->\n" + 
					"                 <code code=\\\"51848-0\\\" codeSystem=\\\"2.16.840.1.113883.6.1\\\" codeSystemName=\\\"LOINC\\\" displayName=\\\"Assesment\\\" />\n" + 
					"                 <!-- [1..1] Título de la sección -->\n" + 
					"                 <title>Impresión diagnóstica</title>\n" + 
					"                 <!-- [1..1] Texto -->\n" + 
					"                 <text>PARAM_DIAGNOSTICO</text>\n" + 
					"              </section>\n" + 
					"           </component>\n" + 
					"        </structuredBody>\n" + 
					"     </component>\n" + 
					"  </ClinicalDocument>\"\n" + 
					"  }";
			Consulta consulta = consultaRepository.findByIdConsulta(idConsulta);
			if(consulta==null) {
				ConsultaException ee = new ConsultaException("La Consulta no se encuentra en el sistema.", ConsultaException.LAYER_DAO, ConsultaException.ACTION_VALIDATE);
				ee.addError("No existe la Consulta con el Id:" + idConsulta);
				throw ee;
			}
			String param = (consulta.getReferencia1() == null ? "" : consulta.getReferencia1() );
			xml = xml.replace("\"PARAM1\"", "\""+param+"\"");

			param = (consulta.getReferencia1() == null ? "" : consulta.getReferencia2() );
			xml = xml.replace("\"PARAM2\"", "\""+param+"\"");

			param = (consulta.getNombrePaciente());
			xml=xml.replace("PARAM_NOMBRE_PACIENTE", param);

			xml=xml.replace("PARAM_APELLIDO", "");

			xml=xml.replace("PARAM_SEGUNDO_APELLIDO", "");

			xml = xml.replace("\"PARAM_SEXO\"", "\"Indefinido\"");

			param = "";
			xml = xml.replace("PARAM_ESTADO", "");


			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHMMSS");
			param=sdf.format(new Date());
			xml = xml.replaceAll("PARAM_DATENOW", param);


			param = (consulta.getMotivoConsulta() == null ? "" : consulta.getMotivoConsulta());
			xml = xml.replace("PARAM_MOTIVOCONSULTA", param);

			param = (consulta.getSubjetivo() == null ? "" : consulta.getSubjetivo());
			xml = xml.replace("PARAM_SUBJETIVO", param);
			//soap
			//		   param = "Subjetivo: "+(consulta.getSubjetivo() == null ? "" : consulta.getSubjetivo())+"\n";
			//		   param = param + "Objetivo: " + (consulta.getObjetivo() == null ? "" : consulta.getObjetivo())+"\n";
			//		   param = param + "Análisis: " + (consulta.getAnalisis() == null ? "" : consulta.getAnalisis())+"\n"; 
			//		   param =  + "Plan Terapéutico y tratamiento" + (consulta.getPlanTerapeutico() == null ? "" : consulta.getPlanTerapeutico())+"\n";
			param = (consulta.getPlanTerapeutico() == null ? "" : consulta.getPlanTerapeutico())+"\n";
			xml = xml.replace("PARAM_PLAN", param);

			param="";
			if(consulta.getTratamientoList() != null ) {
				Collection <Tratamiento> tratamientoList = consulta.getTratamientoList();
				for(Tratamiento tratamiento:tratamientoList) {
					String aux=null;
					aux = (tratamiento.getProNombre() == null ? "" : tratamiento.getProNombre());
					if(tratamientoList.size()!=1 && !aux.isEmpty()) {
						param = param +", ";
					}
				}
			}
			xml = xml.replace("PARAM_CIE9", param);

			param = (consulta.getObjetivo() == null ? "" : consulta.getObjetivo());
			xml = xml.replace("PARAM_OBJETIVO", param);

			param = (consulta.getSignosVitales() == null ? "" : consulta.getSignosVitales().toString());
			xml = xml.replace("PARAM_SIGNOSVITALES_JSON", "");

			param = (consulta.getAnalisis() == null ? "" : consulta.getAnalisis());
			xml = xml.replaceAll("PARAM_DIAGNOSTICO", param);

			System.out.println(xml);

			URL url = new URL("http://177.231.255.234:8094/summary/");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("PUT");
			conn.setRequestProperty("Content-Type", "application/json");
			OutputStream os = conn.getOutputStream();
			os.write(xml.getBytes());
			os.flush();
			if(conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
				ConsultaException ee = new ConsultaException("Ocurrio un error al crear CDA Consulta", ConsultaException.LAYER_DAO, ConsultaException.ACTION_SELECT);
				ee.addError(conn.getErrorStream().toString());
				logger.error("Ocurrio un error al crear CDA Consulta - CODE {} - {}", ee.getExceptionCode(), conn.getResponseCode());
				throw ee;
			}
		} catch (ConsultaException ee) {
			throw ee;
		} catch (DataIntegrityViolationException dive) {
			ConsultaException ee = new ConsultaException("Ocurrio un error al crear CDA Consulta", ConsultaException.LAYER_DAO, ConsultaException.ACTION_SELECT);
			ee.addError("No fue posible obtener detalles Consulta");
			logger.error("Error al obtener detalles Consulta - CODE {} - {}", ee.getExceptionCode(), idConsulta, dive);
			throw ee;
		} catch (Exception ex) {
			ConsultaException ee = new ConsultaException("Ocurrio un error al crear CDA Consulta", ConsultaException.LAYER_DAO, ConsultaException.ACTION_SELECT);
			ee.addError("Ocurrió un error al obtener detalles Consulta");
			logger.error("Error al obtener detalles Consulta - CODE {} - {}", ee.getExceptionCode(), idConsulta, ex);
			throw ee;
		}



	}

	@Override
	public String getUrlImagen(Long idConsulta) throws ConsultaException {
		try {
			logger.info("IMPL - geturlImagen() - Obteniendo url imagen por idConsulta:{}",idConsulta);
			Consulta consulta = consultaRepository.findByIdConsulta(idConsulta);
			if(consulta==null){
				logger.info("IMPL - geturlImagen() - No existe ninguna consulta con el  idConsulta:{}",idConsulta);
				return null;
			}
			String idPacienteHis= consulta.getReferencia2();
			String url="";
			if(idPacienteHis!=null && !idPacienteHis.isEmpty()) {
				url ="http://172.16.50.222/portal/default.aspx?server_name=dsxxi&user_name=medico&password=medico&patient_id="+idPacienteHis+"&close_on_exit=true";
			}else {
				url=null;
			}
			return url;
		} catch (DataIntegrityViolationException dive) {
			ConsultaException ee = new ConsultaException("Ocurrio un error obtener la urlImagen", ConsultaException.LAYER_DAO, ConsultaException.ACTION_SELECT);
			ee.addError("No fue posible obtener los detalles de la Consulta");
			logger.error("Error al obtener detalles Consulta - CODE {} - {}", ee.getExceptionCode(), idConsulta, dive);
			throw ee;
		} catch (Exception ex) {
			ConsultaException ee = new ConsultaException("Ocurrio un error obtener la urlImagen", ConsultaException.LAYER_DAO, ConsultaException.ACTION_SELECT);
			ee.addError("Ocurrió un error al obtener detalles Consulta");
			logger.error("Error al obtener detalles Consulta - CODE {} - {}", ee.getExceptionCode(), idConsulta, ex);
			throw ee;
		}
	}

	@Override
	public Page<HashMap<String, Object>> getConsultaByEstatusMedico(UUID idMedico, Integer page, Integer size, String orderColumn, String orderType, Integer idGroup) throws ConsultaException {
		try {
			List<HashMap<String, Object>> responseList = new ArrayList<>();
			HashMap<String, Object> response = new HashMap<>();
			Sort sort = new Sort(Sort.Direction.ASC, (String) colOrderNames.get(orderColumn));
			if(orderColumn != null && orderType != null) {
				if(orderType.equalsIgnoreCase("asc"))
					sort = new Sort(Sort.Direction.ASC, (String) colOrderNames.get(orderColumn));
				else
					sort = new Sort(Sort.Direction.DESC, (String) colOrderNames.get(orderColumn));
			}
			PageRequest request = new PageRequest(page, size, sort);
			Specifications<Consulta> spec = Specifications.where((root, query, cb) -> {
				Predicate tc = null;
				tc = cb.notLike(root.get("estadoConsulta"), "Finalizada");
				tc = cb.and(tc, cb.equal(root.get("idGroup"), idGroup));
				if(idMedico != null)
					tc = (tc != null ?
							cb.and(tc, cb.equal(root.get("idMedico"), idMedico)) :
							cb.equal(root.get("idMedico"), idMedico));
				return tc;
			});
			logger.info("Despues del specification");
			Page<Consulta> consultaPage = consultaRepository.findAll(spec, request);
			List<ConsultaView> consultaViewsNuevas = new ArrayList<>();
			consultaPage.getContent().forEach(entity -> consultaViewsNuevas.add(consultaConverter.toView(entity, false)));
			logger.info("Despues del primer query. - {}", consultaViewsNuevas);
			response.put("nuevas", consultaViewsNuevas);

			spec = Specifications.where((root, query, cb) -> {
				Predicate tc = null;
				tc = cb.like(root.get("estadoConsulta"), "Finalizada");
				tc = cb.and(tc, cb.equal(root.get("idGroup"), idGroup));
				if(idMedico != null)
					tc = (tc != null ?
							cb.and(tc, cb.equal(root.get("idMedico"), idMedico)) :
							cb.equal(root.get("idMedico"), idMedico));
				return tc;
			});
			List<ConsultaView> consultaViewsOtras = new ArrayList<>();
			consultaPage = consultaRepository.findAll(spec, request);
			consultaPage.getContent().forEach(entity -> consultaViewsOtras.add(consultaConverter.toView(entity, false)));
			response.put("otras", consultaViewsOtras);
			logger.info("Despues del segundo query. {}", consultaViewsOtras);

			Integer countNuevas = consultaRepository.countEstadoConsultaNuevaByMedico(idMedico, idGroup);
			Integer countOtras = consultaRepository.countEstadoConsultaOtrasByMedico(idMedico, idGroup);
			response.put("nuevas-count", countNuevas);
			response.put("otras-count", countOtras);
			logger.info("Totales nuevas - {}, totales otras - {}", countNuevas, countOtras);
			responseList.add(response);
			return new PageImpl<>(responseList, request, consultaPage.getTotalElements());
		} catch (Exception ex) {
			logger.error("Error al obtener consultas - {}", ex.getMessage());
			throw new ConsultaException(ex.getMessage(), ConsultaException.LAYER_SERVICE, ConsultaException.ACTION_SELECT);
		}
	}

	@Override
	public Page<HashMap<String, Object>> getConsultaByEstatus(Integer page, Integer size, String orderColumn, String orderType, Integer idGroup) throws ConsultaException {
		try {
			List<HashMap<String, Object>> responseList = new ArrayList<>();
			HashMap<String, Object> response = new HashMap<>();
			logger.info(" page {} - size {} - orderColumn {} - orderType {}", page, size, (String) colOrderNames.get(orderColumn), orderType);
			Sort sort = new Sort(Sort.Direction.ASC, (String) colOrderNames.get(orderColumn));
			if(orderColumn != null && orderType != null) {
				if(orderType.equalsIgnoreCase("asc"))
					sort = new Sort(Sort.Direction.ASC, (String) colOrderNames.get(orderColumn));
				else
					sort = new Sort(Sort.Direction.DESC, (String) colOrderNames.get(orderColumn));
			}
			PageRequest request = new PageRequest(page, size, sort);
			Specifications<Consulta> spec = Specifications.where((root, query, cb) -> cb.or(
					cb.notLike(root.get("estadoConsulta"), "Finalizada"),
					cb.equal(root.get("idGroup"), idGroup)
			));
			logger.info("Despues del specification");
			Page<Consulta> consultaPage = consultaRepository.findAll(spec, request);
			List<ConsultaView> consultaViewsNuevas = new ArrayList<>();
			consultaPage.getContent().forEach(entity -> consultaViewsNuevas.add(consultaConverter.toView(entity, false)));
			response.put("nuevas", consultaViewsNuevas);
			logger.info("Respuesta : " + consultaViewsNuevas);

			List<ConsultaView> consultaViewsOtras = new ArrayList<>();
			logger.info("Respuesta : " + consultaViewsOtras);
			spec = Specifications.where((root, query, cb) -> cb.or(
					cb.like(root.get("estadoConsulta"), "Finalizada"),
					cb.equal(root.get("idGroup"), idGroup)
			));
			logger.info("Despues del specification 2");
			consultaPage = consultaRepository.findAll(spec, request);
			consultaPage.getContent().forEach(entity -> consultaViewsOtras.add(consultaConverter.toView(entity, false)));
			logger.info("Respuesta : " + consultaViewsOtras);
			response.put("otras", consultaViewsOtras);

			Integer countNuevas = consultaRepository.countEstadoConsultaNueva(idGroup);
			Integer countOtras = consultaRepository.countEstadoConsultaOtras(idGroup);
			response.put("nuevas-count", countNuevas);
			response.put("otras-count", countOtras);
			logger.info("Totales nuevas - {}, totales otras - {}", countNuevas, countOtras);

			responseList.add(response);
			return new PageImpl<>(responseList, request, consultaPage.getTotalElements());
		} catch (Exception ex) {
			logger.error("Error al obtener consultas - {}", ex.getMessage());
			throw new ConsultaException(ex.getMessage(), ConsultaException.LAYER_SERVICE, ConsultaException.ACTION_SELECT);
		}
	}


}