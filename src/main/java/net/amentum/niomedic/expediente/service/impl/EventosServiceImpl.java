package net.amentum.niomedic.expediente.service.impl;

import net.amentum.common.TimeUtils;
import net.amentum.niomedic.catalogos.views.CatEstadoConsultaView;
import net.amentum.niomedic.expediente.converter.EventosConverter;
import net.amentum.niomedic.expediente.converter.InvitadosConverter;
import net.amentum.niomedic.expediente.exception.EventosException;
import net.amentum.niomedic.expediente.exception.ExceptionServiceCode;
import net.amentum.niomedic.expediente.model.Asuetos;
import net.amentum.niomedic.expediente.model.Eventos;
import net.amentum.niomedic.expediente.model.Invitados;
import net.amentum.niomedic.expediente.model.TurnosLaborales;
import net.amentum.niomedic.expediente.model.Vacaciones;
import net.amentum.niomedic.expediente.persistence.AsuetosRepository;
import net.amentum.niomedic.expediente.persistence.EventosRepository;
import net.amentum.niomedic.expediente.persistence.InvitadosRepository;
import net.amentum.niomedic.expediente.persistence.TurnosLaboralesRepository;
import net.amentum.niomedic.expediente.persistence.VacacionesRepository;
import net.amentum.niomedic.expediente.service.ConsultaService;
import net.amentum.niomedic.expediente.service.EventosService;
import net.amentum.niomedic.expediente.views.EventosView;
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
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static net.amentum.common.TimeUtils.parseDate;

@Service
@Transactional(readOnly = true)
public class EventosServiceImpl implements EventosService {
	private final Logger logger = LoggerFactory.getLogger(EventosServiceImpl.class);
	private final Map<String, Object> colOrderNames = new HashMap<>();
	private EventosRepository eventosRepository;
	private EventosConverter eventosConverter;
	private TurnosLaboralesRepository turnosLaboralesRepository;
	private VacacionesRepository vacacionesRepository;
	private AsuetosRepository asuetosRepository;
	private InvitadosConverter invitadosConverter;
	private InvitadosRepository invitadosRepository;
	private ConsultaService consultaService;

	{
		colOrderNames.put("idEventos", "idEventos");
		colOrderNames.put("idUsuario", "idUsuario");
		colOrderNames.put("inicio", "inicio");
		colOrderNames.put("fin", "fin");
		colOrderNames.put("ubicacion", "ubicacion");
		colOrderNames.put("conferencia", "conferencia");
		colOrderNames.put("descripcion", "descripcion");
		colOrderNames.put("alerta", "alerta");
		colOrderNames.put("visible", "visible");
		colOrderNames.put("idConsulta", "idConsulta");
		colOrderNames.put("titulo", "titulo");
	}

	@Autowired
	public void setEventosRepository(EventosRepository eventosRepository) {
		this.eventosRepository = eventosRepository;
	}

	@Autowired
	public void setEventosConverter(EventosConverter eventosConverter) {
		this.eventosConverter = eventosConverter;
	}

	@Autowired
	public void setTurnosLaboralesRepository(TurnosLaboralesRepository turnosLaboralesRepository) {
		this.turnosLaboralesRepository = turnosLaboralesRepository;
	}

	@Autowired
	public void setVacacionesRepository(VacacionesRepository vacacionesRepository) {
		this.vacacionesRepository = vacacionesRepository;
	}

	@Autowired
	public void setAsuetosRepository(AsuetosRepository asuetosRepository) {
		this.asuetosRepository = asuetosRepository;
	}

	@Autowired
	public void setInvitadosConverter(InvitadosConverter invitadosConverter) {
		this.invitadosConverter = invitadosConverter;
	}

	@Autowired
	public void setInvitadosRepository(InvitadosRepository invitadosRepository) {
		this.invitadosRepository = invitadosRepository;
	}

	@Autowired
	public void setConsultaService(ConsultaService consultaService) {
		this.consultaService = consultaService;
	}

	@Transactional(readOnly = false, rollbackFor = {EventosException.class})
	@Override
	public EventosView createEventos(EventosView eventosView) throws EventosException {
		try {
			//         que la fecha final no sea menor que la fecha inicial
			long inicioView = eventosView.getInicio().getTime();
			long finView = eventosView.getFin().getTime();

			if (inicioView >= finView) {
				String textoError = "El tiempo final NO puede ser MENOR que el tiempo inicial-> fechaInicio: " + eventosView.getInicio() + " - fechaFin: " + eventosView.getFin();
				logger.error("===>>>" + textoError);
				EventosException eveE = new EventosException("Existe un Error", EventosException.LAYER_DAO, EventosException.ACTION_VALIDATE);
				eveE.addError(textoError);
				throw eveE;
			}

			//         EVENTO es 1  IMPOSIBLE
			if (eventosView.getTipoEventoId() == 1) {
				String textoError = "El tipo de evento 1 no es elegible para utilizarlo ";
				logger.error("===>>>" + textoError);
				EventosException eveE = new EventosException("Existe un Error", EventosException.LAYER_DAO, EventosException.ACTION_VALIDATE);
				eveE.addError(textoError);
				throw eveE;
			}


			//         EL EVENTO ES UNA CITA
			if (eventosView.getTipoEventoId() == 2) {

				if (eventosView.getIdConsulta() == null) {
					String textoError = "Una Cita DEBE estar relacionada a una Consulta";
					logger.error("===>>>" + textoError);
					EventosException eveE = new EventosException("Existe un Error", EventosException.LAYER_DAO, EventosException.ACTION_VALIDATE);
					eveE.addError(textoError);
					throw eveE;
				}

				//         DONE: que la hora inicio no este duplicado en otro registro
				List<Eventos> duplicado = eventosRepository.buscarPorIdUsuarioCreaAndInicio(eventosView.getIdUsuario(), eventosView.getInicio(), 2);
				if (duplicado != null && !duplicado.isEmpty()) {
					String textoError = "Eventos DUPLICADO en otro registro: idEventos: " + duplicado.get(0).getIdEventos() +
							" - fechaInicio: " + duplicado.get(0).getInicio();
					logger.error("===>>>" + textoError);
					EventosException eveE = new EventosException("Existe un Error", EventosException.LAYER_DAO, EventosException.ACTION_VALIDATE);
					eveE.addError(textoError);
					throw eveE;
				}

				//         DONE: que no se cruce con otro registro
				List<Eventos> cruce = eventosRepository.buscarPorIdUsuarioCreaAndNuevaFechaEntreInicioFin(eventosView.getIdUsuario(), eventosView.getInicio(), 2);
				if (cruce != null && !cruce.isEmpty()) {
					if (eventosView.getIdEventos() != cruce.get(0).getIdEventos()) {
						String textoError = "Eventos CRUCE entre horas con otro registro: idEventos: " + cruce.get(0).getIdEventos() +
								" - fechaInicio: " + cruce.get(0).getInicio() +
								" - fechaFin: " + cruce.get(0).getFin();
						logger.error("===>>>" + textoError);
						EventosException eveE = new EventosException("Existe un Error", EventosException.LAYER_DAO, EventosException.ACTION_VALIDATE);
						eveE.addError(textoError);
						throw eveE;
					}
				}

				List<Eventos> cruceFinal = eventosRepository.buscarPorIdUsuarioCreaAndNuevaFechaEntreInicioFin(eventosView.getIdUsuario(), eventosView.getFin(), 2);
				if (cruceFinal != null && !cruceFinal.isEmpty()) {
					if (eventosView.getIdEventos() != cruceFinal.get(0).getIdEventos()) {
						String textoError = "Eventos CRUCE entre horas con otro registro: idEventos: " + cruceFinal.get(0).getIdEventos() +
								" - fechaInicio: " + cruceFinal.get(0).getInicio() +
								" - fechaFin: " + cruceFinal.get(0).getFin();
						logger.error("===>>>" + textoError);
						EventosException eveE = new EventosException("Existe un Error", EventosException.LAYER_DAO, EventosException.ACTION_VALIDATE);
						eveE.addError(textoError);
						throw eveE;
					}
				}


				//         DONE: checar turnos laborales
				TurnosLaborales turnosLaborales = turnosLaboralesRepository.findByIdUsuario(eventosView.getIdUsuario());
				//            if (turnosLaborales == null) {
				//               String textoError = "No existe un turno laboral para -> idUsuario: " + eventosView.getIdUsuario() + " - fechaInicio: " + eventosView.getInicio() + " - fechaFin: " + eventosView.getFin();
				//               logger.error("===>>>" + textoError);
				//               EventosException eveE = new EventosException("Existe un Error", EventosException.LAYER_DAO, EventosException.ACTION_VALIDATE);
				//               eveE.addError(textoError);
				//               throw eveE;
				//            }
				long valIni = 1L;
				long valFin = 1L;
				boolean flag = false;

				if (turnosLaborales != null) {
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(eventosView.getInicio());
					int dia = calendar.get(Calendar.DAY_OF_WEEK);
					valIni = quitarFecha(turnosLaborales.getInicio()).getTime();
					valFin = quitarFecha(turnosLaborales.getFin()).getTime();
					inicioView = quitarFecha(eventosView.getInicio()).getTime();
					finView = quitarFecha(eventosView.getFin()).getTime();
					flag = false;
					switch (dia) {
					case 1:
						if (turnosLaborales.getDomingo()) {
							flag = dentroRango(inicioView, finView, valIni, valFin);
						} else {
							flag = true;
						}
						break;
					case 2:
						if (turnosLaborales.getLunes()) {
							flag = dentroRango(inicioView, finView, valIni, valFin);
						} else {
							flag = true;
						}
						break;
					case 3:
						if (turnosLaborales.getMartes()) {
							flag = dentroRango(inicioView, finView, valIni, valFin);
						} else {
							flag = true;
						}
						break;
					case 4:
						if (turnosLaborales.getMiercoles()) {
							flag = dentroRango(inicioView, finView, valIni, valFin);
						} else {
							flag = true;
						}
						break;
					case 5:
						if (turnosLaborales.getJueves()) {
							flag = dentroRango(inicioView, finView, valIni, valFin);
						} else {
							flag = true;
						}
						break;
					case 6:
						if (turnosLaborales.getViernes()) {
							flag = dentroRango(inicioView, finView, valIni, valFin);
						} else {
							flag = true;
						}
						break;
					case 7:
						if (turnosLaborales.getSabado()) {
							flag = dentroRango(inicioView, finView, valIni, valFin);
						} else {
							flag = true;
						}
						break;
					}

					if (flag) {
						String textoError = "Eventos no esta en horario laboral-> idUsuario: " + eventosView.getIdUsuario() + " - fechaInicio: " + eventosView.getInicio() + " - fechaFin: " + eventosView.getFin();
						logger.error("===>>>" + textoError);
						EventosException eveE = new EventosException("Existe un Error", EventosException.LAYER_DAO, EventosException.ACTION_VALIDATE);
						eveE.addError(textoError);
						throw eveE;
					}
				}
				//         DONE: checar vacaciones
				List<Vacaciones> vacacionesList = vacacionesRepository.findByIdUsuario(eventosView.getIdUsuario());

				inicioView = eventosView.getInicio().getTime();
				finView = eventosView.getFin().getTime();
				flag = false;

				if (vacacionesList != null && !vacacionesList.isEmpty()) { // revision de vacacionesList NO vacio
					for (Vacaciones vaca : vacacionesList) {
						valIni = quitarTiempo(vaca.getInicio(), true).getTime();
						valFin = quitarTiempo(vaca.getFin(), false).getTime();
						flag = !dentroRango(inicioView, finView, valIni, valFin);
						break;
					}
				} else {
					//            String textoError = "No existen vacaciones para -> idUsuario: " + eventosView.getIdUsuario() + " - fechaInicio: " + eventosView.getInicio() + " - fechaFin: " + eventosView.getFin();
					//            logger.error("===>>>" + textoError);
					//            EventosException eveE = new EventosException("Existe un Error", EventosException.LAYER_DAO, EventosException.ACTION_VALIDATE);
					//            eveE.addError(textoError);
					//            throw eveE;
				} // revision de vacacionesList NO vacio

				if (flag) {
					String textoError = "Eventos esta en horario vacacional-> idUsuario: " + eventosView.getIdUsuario() + " - fechaInicio: " + eventosView.getInicio() + " - fechaFin: " + eventosView.getFin();
					logger.error("===>>>" + textoError);
					EventosException eveE = new EventosException("Existe un Error", EventosException.LAYER_DAO, EventosException.ACTION_VALIDATE);
					eveE.addError(textoError);
					throw eveE;
				}

				//         DONE: checar asuetos
				List<Asuetos> asuetosList = asuetosRepository.findAll();

				inicioView = quitarTiempo(eventosView.getInicio(), true).getTime();
				flag = false;

				if (asuetosList != null && !asuetosList.isEmpty()) { // revision de asuetosList NO vacio
					for (Asuetos asue : asuetosList) {
						valIni = quitarTiempo(asue.getFecha(), true).getTime();
						if (inicioView == valIni) {
							flag = true;
							break;
						}
					}
				} else {
					//            String textoError = "No existen asuetos para -> idUsuario: " + eventosView.getIdUsuario() + " - fechaInicio: " + eventosView.getInicio() + " - fechaFin: " + eventosView.getFin();
					//            logger.error("===>>>" + textoError);
					//            EventosException eveE = new EventosException("Existe un Error", EventosException.LAYER_DAO, EventosException.ACTION_VALIDATE);
					//            eveE.addError(textoError);
					//            throw eveE;
				} // revision de asuetosList NO vacio

				if (flag) {
					String textoError = "Eventos esta en dia de asueto-> idUsuario: " + eventosView.getIdUsuario() + " - fechaInicio: " + eventosView.getInicio() + " - fechaFin: " + eventosView.getFin();
					logger.error("===>>>" + textoError);
					EventosException eveE = new EventosException("Existe un Error", EventosException.LAYER_DAO, EventosException.ACTION_VALIDATE);
					eveE.addError(textoError);
					throw eveE;
				}

			}

			//         EL EVENTO ES UN RECORDATORIO
			if (eventosView.getTipoEventoId() == 3) {
				eventosView.setInicio(eventosView.getAlerta());
				eventosView.setFin(eventosView.getAlerta());
				//            eventosView.setInvitadosViewList(null);
				eventosView.setInvitadosViewList(new ArrayList<>());
				eventosView.setDescripcion(null);
				eventosView.setIdConsulta(null);
			}

			//         EL EVENTO ES UNA TAREA
			if (eventosView.getTipoEventoId() == 4) {
				eventosView.setInicio(eventosView.getAlerta());
				eventosView.setFin(eventosView.getAlerta());
				//            eventosView.setInvitadosViewList(null);
				eventosView.setInvitadosViewList(new ArrayList<>());
				eventosView.setIdConsulta(null);
			}

			Eventos eventos = eventosConverter.toEntity(eventosView, new Eventos(), Boolean.FALSE);
			logger.debug("Insertar nuevo Eventos: {}", eventos);
			eventosRepository.save(eventos);
			return eventosConverter.toView(eventos, Boolean.TRUE);

		} catch (EventosException eveE) {
			throw eveE;
		} catch (ConstraintViolationException cve) {
			logger.error("===>>>Error en la validacion");
			EventosException eveE = new EventosException("Error en la validacion", EventosException.LAYER_DAO, EventosException.ACTION_VALIDATE);
			final Set<ConstraintViolation<?>> violaciones = cve.getConstraintViolations();
			for (Iterator<ConstraintViolation<?>> iterator = violaciones.iterator(); iterator.hasNext(); ) {
				ConstraintViolation<?> siguiente = iterator.next();
				eveE.addError(siguiente.getPropertyPath() + " " + siguiente.getMessage());
			}
			throw eveE;
		} catch (DataIntegrityViolationException dive) {
			EventosException eveE = new EventosException("No fue posible agregar  Eventos", EventosException.LAYER_DAO, EventosException.ACTION_INSERT);
			eveE.addError("Ocurrio un error al agregar Eventos");
			logger.error("===>>>Error al insertar nuevo Eventos - CODE: {} - {}", eveE.getExceptionCode(), eventosView, dive);
			throw eveE;
		} catch (Exception ex) {
			EventosException eveE = new EventosException("Error inesperado al agregar  Eventos", EventosException.LAYER_DAO, EventosException.ACTION_INSERT);
			eveE.addError("Ocurrio un error al agregar Eventos");
			logger.error("===>>>Error al insertar nuevo Eventos - CODE: {} - {}", eveE.getExceptionCode(), eventosView, ex);
			throw eveE;
		}
	}

	@Transactional(readOnly = false, rollbackFor = {EventosException.class})
	@Override
	public EventosView updateEventos(EventosView eventosView) throws EventosException {
		try {
			//         DONE: que exista el registro
			if (!eventosRepository.exists(eventosView.getIdEventos())) {
				logger.error("===>>>idEventos no encontrado: {}", eventosView.getIdEventos());
				EventosException eveE = new EventosException("No se encuentra en el sistema Eventos", EventosException.LAYER_DAO, EventosException.ACTION_VALIDATE);
				eveE.addError("idEventos no encontrado: " + eventosView.getIdEventos());
				throw eveE;
			}
			//         DONE: que la fecha final no sea menor que la fecha inicial
			long inicioView = eventosView.getInicio().getTime();
			long finView = eventosView.getFin().getTime();

			if (inicioView >= finView) {
				String textoError = "El tiempo final NO puede ser MENOR que el tiempo inicial-> fechaInicio: " + eventosView.getInicio() + " - fechaFin: " + eventosView.getFin();
				logger.error("===>>>" + textoError);
				EventosException eveE = new EventosException("Existe un Error", EventosException.LAYER_DAO, EventosException.ACTION_VALIDATE);
				eveE.addError(textoError);
				throw eveE;
			}

			Eventos eventos = eventosRepository.findOne(eventosView.getIdEventos());

			//         NO PERMITE CAMBIAR EL TIPO DE EVENTO
			if (eventos.getCatalogoTipoEvento().getIdTipoEvento() != eventosView.getTipoEventoId()) {
				String textoError = "NO puedes cambiar el tipo de Evento: " + eventosView.getIdEventos() + " - fechaInicio: " + eventosView.getInicio();
				logger.error("===>>>" + textoError);
				EventosException eveE = new EventosException("Existe un Error", EventosException.LAYER_DAO, EventosException.ACTION_VALIDATE);
				eveE.addError(textoError);
				throw eveE;
			}

			//         EVENTO es 1  IMPOSIBLE
			if (eventosView.getTipoEventoId() == 1) {
				String textoError = "El tipo de evento 1 no es elegible para utilizarlo ";
				logger.error("===>>>" + textoError);
				EventosException eveE = new EventosException("Existe un Error", EventosException.LAYER_DAO, EventosException.ACTION_VALIDATE);
				eveE.addError(textoError);
				throw eveE;
			}

			//         EL EVENTO ES UNA CITA
			if (eventosView.getTipoEventoId() == 2) {

				if (eventosView.getIdConsulta() == null) {
					String textoError = "Una Cita DEBE estar relacionada a una Consulta";
					logger.error("===>>>" + textoError);
					EventosException eveE = new EventosException("Existe un Error", EventosException.LAYER_DAO, EventosException.ACTION_VALIDATE);
					eveE.addError(textoError);
					throw eveE;
				}

				//         DONE: que la hora inicio no este duplicado en otro registro
				List<Eventos> duplicado = eventosRepository.buscarPorIdUsuarioCreaAndInicio(eventosView.getIdUsuario(), eventosView.getInicio(), 2);
				if (duplicado != null && !duplicado.isEmpty()) {
					for (Eventos eveDup : duplicado) {
						//               if (eventosView.getIdEventos() != duplicado.get(0).getIdEventos()) {
						if (eventosView.getIdEventos() != eveDup.getIdEventos()) {
							String textoError = "Eventos DUPLICADO en otro registro: idEventos: " + duplicado.get(0).getIdEventos() +
									" - fechaInicio: " + duplicado.get(0).getInicio();
							logger.error("===>>>" + textoError);
							EventosException eveE = new EventosException("Existe un Error", EventosException.LAYER_DAO, EventosException.ACTION_VALIDATE);
							eveE.addError(textoError);
							throw eveE;
						} //if
					} //for
				}//if

				//         DONE: que no se cruce con otro registro
				List<Eventos> cruce = eventosRepository.buscarPorIdUsuarioCreaAndNuevaFechaEntreInicioFin(eventosView.getIdUsuario(), eventosView.getInicio(), 2);
				if (cruce != null && !cruce.isEmpty()) {
					if (eventosView.getIdEventos() != cruce.get(0).getIdEventos()) {
						String textoError = "Eventos CRUCE entre horas con otro registro: idEventos: " + cruce.get(0).getIdEventos() +
								" - fechaInicio: " + cruce.get(0).getInicio() +
								" - fechaFin: " + cruce.get(0).getFin();
						logger.error("===>>>" + textoError);
						EventosException eveE = new EventosException("Existe un Error", EventosException.LAYER_DAO, EventosException.ACTION_VALIDATE);
						eveE.addError(textoError);
						throw eveE;
					}
				}

				List<Eventos> cruceFinal = eventosRepository.buscarPorIdUsuarioCreaAndNuevaFechaEntreInicioFin(eventosView.getIdUsuario(), eventosView.getFin(), 2);
				if (cruceFinal != null && !cruceFinal.isEmpty()) {
					if (eventosView.getIdEventos() != cruceFinal.get(0).getIdEventos()) {
						String textoError = "Eventos CRUCE entre horas con otro registro: idEventos: " + cruceFinal.get(0).getIdEventos() +
								" - fechaInicio: " + cruceFinal.get(0).getInicio() +
								" - fechaFin: " + cruceFinal.get(0).getFin();
						logger.error("===>>>" + textoError);
						EventosException eveE = new EventosException("Existe un Error", EventosException.LAYER_DAO, EventosException.ACTION_VALIDATE);
						eveE.addError(textoError);
						throw eveE;
					}
				}


				//            los elimino de la DB
				Collection<Long> noExistenView = invitadosConverter.obtenerIDNoExistentesInvitados(eventos, eventosView);
				if (noExistenView != null && !noExistenView.isEmpty()) {
					Invitados inv;
					for (Long IDinv : noExistenView) {
						inv = invitadosRepository.findOne(IDinv);
						inv.setEventos(null);
						invitadosRepository.delete(IDinv);
					}

					eventos.setInvitadosList(new ArrayList<>());
				}


				//         DONE: checar turnos laborales
				TurnosLaborales turnosLaborales = turnosLaboralesRepository.findByIdUsuario(eventosView.getIdUsuario());
				//            if (turnosLaborales == null) {
				//               String textoError = "No existe un turno laboral para -> idUsuario: " + eventosView.getIdUsuario() + " - fechaInicio: " + eventosView.getInicio() + " - fechaFin: " + eventosView.getFin();
				//               logger.error("===>>>" + textoError);
				//               EventosException eveE = new EventosException("Existe un Error", EventosException.LAYER_DAO, EventosException.ACTION_VALIDATE);
				//               eveE.addError(textoError);
				//               throw eveE;
				//            }

				long valIni = 1L;
				long valFin = 1L;
				//            long valIdEventos = 0L;
				//            long idEventosView = eventosView.getIdEventos();
				boolean flag = false;

				if (turnosLaborales != null) {
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(eventosView.getInicio());
					int dia = calendar.get(Calendar.DAY_OF_WEEK);
					valIni = quitarFecha(turnosLaborales.getInicio()).getTime();
					valFin = quitarFecha(turnosLaborales.getFin()).getTime();
					inicioView = quitarFecha(eventosView.getInicio()).getTime();
					finView = quitarFecha(eventosView.getFin()).getTime();
					flag = false;
					switch (dia) {
					case 1:
						if (turnosLaborales.getDomingo()) {
							flag = dentroRango(inicioView, finView, valIni, valFin);
						} else {
							flag = true;
						}
						break;
					case 2:
						if (turnosLaborales.getLunes()) {
							flag = dentroRango(inicioView, finView, valIni, valFin);
						} else {
							flag = true;
						}
						break;
					case 3:
						if (turnosLaborales.getMartes()) {
							flag = dentroRango(inicioView, finView, valIni, valFin);
						} else {
							flag = true;
						}
						break;
					case 4:
						if (turnosLaborales.getMiercoles()) {
							flag = dentroRango(inicioView, finView, valIni, valFin);
						} else {
							flag = true;
						}
						break;
					case 5:
						if (turnosLaborales.getJueves()) {
							flag = dentroRango(inicioView, finView, valIni, valFin);
						} else {
							flag = true;
						}
						break;
					case 6:
						if (turnosLaborales.getViernes()) {
							flag = dentroRango(inicioView, finView, valIni, valFin);
						} else {
							flag = true;
						}
						break;
					case 7:
						if (turnosLaborales.getSabado()) {
							flag = dentroRango(inicioView, finView, valIni, valFin);
						} else {
							flag = true;
						}
						break;
					}

					if (flag) {
						String textoError = "Eventos no esta en horario laboral-> idUsuario: " + eventosView.getIdUsuario() + " - fechaInicio: " + eventosView.getInicio() + " - fechaFin: " + eventosView.getFin();
						logger.error("===>>>" + textoError);
						EventosException eveE = new EventosException("Existe un Error", EventosException.LAYER_DAO, EventosException.ACTION_VALIDATE);
						eveE.addError(textoError);
						throw eveE;
					}
				}
				//         DONE: checar vacaciones
				List<Vacaciones> vacacionesList = vacacionesRepository.findByIdUsuario(eventosView.getIdUsuario());

				inicioView = eventosView.getInicio().getTime();
				finView = eventosView.getFin().getTime();
				flag = false;
				if (vacacionesList != null && !vacacionesList.isEmpty()) { // revision de vacacionesList NO vacio
					for (Vacaciones vaca : vacacionesList) {
						valIni = quitarTiempo(vaca.getInicio(), true).getTime();
						valFin = quitarTiempo(vaca.getFin(), false).getTime();
						flag = !dentroRango(inicioView, finView, valIni, valFin);
						break;
					}
				} else {
					//            String textoError = "No existen vacaciones para -> idUsuario: " + eventosView.getIdUsuario() + " - fechaInicio: " + eventosView.getInicio() + " - fechaFin: " + eventosView.getFin();
					//            logger.error("===>>>" + textoError);
					//            EventosException eveE = new EventosException("Existe un Error", EventosException.LAYER_DAO, EventosException.ACTION_VALIDATE);
					//            eveE.addError(textoError);
					//            throw eveE;
				} // revision de vacacionesList NO vacio

				if (flag) {
					String textoError = "Eventos esta en horario vacacional-> idUsuario: " + eventosView.getIdUsuario() + " - fechaInicio: " + eventosView.getInicio() + " - fechaFin: " + eventosView.getFin();
					logger.error("===>>>" + textoError);
					EventosException eveE = new EventosException("Existe un Error", EventosException.LAYER_DAO, EventosException.ACTION_VALIDATE);
					eveE.addError(textoError);
					throw eveE;
				}

				//         DONE: checar asuetos
				List<Asuetos> asuetosList = asuetosRepository.findAll();

				inicioView = quitarTiempo(eventosView.getInicio(), true).getTime();
				flag = false;
				if (asuetosList != null && !asuetosList.isEmpty()) { // revision de asuetosList NO vacio
					for (Asuetos asue : asuetosList) {
						valIni = quitarTiempo(asue.getFecha(), true).getTime();
						if (inicioView == valIni) {
							flag = true;
							break;
						}
					}
				} else {
					//            String textoError = "No existen asuetos para -> idUsuario: " + eventosView.getIdUsuario() + " - fechaInicio: " + eventosView.getInicio() + " - fechaFin: " + eventosView.getFin();
					//            logger.error("===>>>" + textoError);
					//            EventosException eveE = new EventosException("Existe un Error", EventosException.LAYER_DAO, EventosException.ACTION_VALIDATE);
					//            eveE.addError(textoError);
					//            throw eveE;
				} // revision de asuetosList NO vacio

				if (flag) {
					String textoError = "Eventos esta en dia de asueto-> idUsuario: " + eventosView.getIdUsuario() + " - fechaInicio: " + eventosView.getInicio() + " - fechaFin: " + eventosView.getFin();
					logger.error("===>>>" + textoError);
					EventosException eveE = new EventosException("Existe un Error", EventosException.LAYER_DAO, EventosException.ACTION_VALIDATE);
					eveE.addError(textoError);
					throw eveE;
				}
			}

			//         EL EVENTO ES UN RECORDATORIO
			if (eventosView.getTipoEventoId() == 3) {
				eventosView.setInicio(eventosView.getAlerta());
				eventosView.setFin(eventosView.getAlerta());
				//            eventosView.setInvitadosViewList(null);
				eventosView.setInvitadosViewList(new ArrayList<>());
				eventosView.setDescripcion(null);
				eventosView.setIdConsulta(null);
			}

			//         EL EVENTO ES UNA TAREA
			if (eventosView.getTipoEventoId() == 4) {
				eventosView.setInicio(eventosView.getAlerta());
				eventosView.setFin(eventosView.getAlerta());
				//            eventosView.setInvitadosViewList(null);
				eventosView.setInvitadosViewList(new ArrayList<>());
				eventosView.setIdConsulta(null);
			}

			eventos = eventosConverter.toEntity(eventosView, eventos, Boolean.TRUE);
			logger.debug("===>>>Editar Eventos: {}", eventos);
			eventosRepository.save(eventos);
			return eventosConverter.toView(eventos, Boolean.TRUE);

		} catch (EventosException eveE) {
			throw eveE;
		} catch (ConstraintViolationException cve) {
			logger.error("===>>>Error en la validacion");
			EventosException eveE = new EventosException("Error en la validacion", EventosException.LAYER_DAO, EventosException.ACTION_VALIDATE);
			final Set<ConstraintViolation<?>> violaciones = cve.getConstraintViolations();
			for (Iterator<ConstraintViolation<?>> iterator = violaciones.iterator(); iterator.hasNext(); ) {
				ConstraintViolation<?> siguiente = iterator.next();
				eveE.addError(siguiente.getPropertyPath() + " " + siguiente.getMessage());
			}
			throw eveE;
		} catch (DataIntegrityViolationException dive) {
			EventosException eveE = new EventosException("No fue posible modificar  Eventos", EventosException.LAYER_DAO, EventosException.ACTION_INSERT);
			eveE.addError("Ocurrio un error al modificar Eventos");
			logger.error("===>>>Error al modificar Eventos - CODE: {} - {}", eveE.getExceptionCode(), eventosView, dive);
			throw eveE;
		} catch (Exception ex) {
			EventosException eveE = new EventosException("Error inesperado al modificar  Eventos", EventosException.LAYER_DAO, EventosException.ACTION_INSERT);
			eveE.addError("Ocurrio un error al modificar Eventos");
			logger.error("===>>>Error al modificar Eventos - CODE: {} - {}", eveE.getExceptionCode(), eventosView, ex);
			throw eveE;
		}
	}

	private static Date quitarTiempo(Date date, boolean inicial) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		if (inicial) {
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
		} else {
			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE, 59);
			cal.set(Calendar.SECOND, 59);
			cal.set(Calendar.MILLISECOND, 59);
		}
		return cal.getTime();
	}

	private static Date quitarFecha(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(0, 0, 0);
		return cal.getTime();
	}

	private static boolean dentroRango(long inicioView, long finView, long valIni, long valFin) {
		if ((inicioView >= valIni) && (finView <= valFin)) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public EventosView getDetailsByIdEventos(Long idEventos) throws EventosException {
		try {
			if (!eventosRepository.exists(idEventos)) {
				logger.error("===>>>idEventos no encontrado: {}", idEventos);
				EventosException eveE = new EventosException("No se encuentra en el sistema Eventos", EventosException.LAYER_DAO, EventosException.ACTION_VALIDATE);
				eveE.addError("idEventos no encontrado: " + idEventos);
				throw eveE;
			}
			Eventos eventos = eventosRepository.findOne(idEventos);
			return eventosConverter.toView(eventos, Boolean.TRUE);
		} catch (EventosException eveE) {
			throw eveE;
		} catch (ConstraintViolationException cve) {
			logger.error("===>>>Error en la validacion");
			EventosException eveE = new EventosException("Error en la validacion", EventosException.LAYER_DAO, EventosException.ACTION_VALIDATE);
			final Set<ConstraintViolation<?>> violaciones = cve.getConstraintViolations();
			for (Iterator<ConstraintViolation<?>> iterator = violaciones.iterator(); iterator.hasNext(); ) {
				ConstraintViolation<?> siguiente = iterator.next();
				eveE.addError(siguiente.getPropertyPath() + ": " + siguiente.getMessage());
			}
			throw eveE;
		} catch (DataIntegrityViolationException dive) {
			EventosException eveE = new EventosException("No fue posible obtener  Eventos", EventosException.LAYER_DAO, EventosException.ACTION_INSERT);
			eveE.addError("Ocurrio un error al obtener Eventos");
			logger.error("===>>>Error al obtener nuevo Eventos - CODE: {} - {}", eveE.getExceptionCode(), idEventos, dive);
			throw eveE;
		} catch (Exception ex) {
			EventosException eveE = new EventosException("Error inesperado al obtener  Eventos", EventosException.LAYER_DAO, EventosException.ACTION_INSERT);
			eveE.addError("Ocurrio un error al obtener Eventos");
			logger.error("===>>>Error al obtener nuevo Eventos - CODE: {} - {}", eveE.getExceptionCode(), idEventos, ex);
			throw eveE;
		}
	}

	@Override
	public Page<EventosView> getEventosPage(List<Long> idUsuario, List<Integer> idTipoEvento, String titulo,
											  Long startDate, Long endDate, List<Long> idUsuarioRecibe, List<String> idPaciente, List<String> especialidad,
											  List<Long> regionSanitaria, List<String> unidadMedica, List<Integer> status,
											  Integer page, Integer size, String orderColumn, String orderType) throws EventosException {
		try {
			if(!colOrderNames.containsKey(orderColumn)) {
				logger.info("getEventosPage() - No existen ordenamiento por la columna: {}, asignando una por defecto", orderColumn);
				orderColumn = "titulo";
			}
			logger.info("getEventosPage(): - idUsuario: {} - idTipoEvento: {} - startDate: {} - endDate: {} - page: {} - size: {} - orderColumn: {} - orderType: {}",
					idUsuario, idTipoEvento, startDate, endDate, page, size, orderColumn, orderType);
			List<EventosView> eventosViewList = new ArrayList<>();
			Page<Eventos> eventosPage = null;
			Sort sort;

			if (orderType.equalsIgnoreCase("asc"))
				sort = new Sort(Sort.Direction.ASC, (String) colOrderNames.get(orderColumn));
			else
				sort = new Sort(Sort.Direction.DESC, (String) colOrderNames.get(orderColumn));
			PageRequest request = new PageRequest(page, size, sort);
			Specifications<Eventos> spec = Specifications.where(
					(root, query, cb) -> {
						Predicate tc = null;
						Expression<Long> usuario = root.get("idUsuarioCrea");
						Expression<Long> tipoEvento = root.get("catalogoTipoEvento");
						if(especialidad != null)
							if(!especialidad.isEmpty()) {
								for(String element : especialidad) {
									String especialidadPattern = "%" + sinAcentos(element.toLowerCase()) + "%";
									tc = (tc != null ? cb.or(tc, cb.like(cb.function("unaccent", String.class, cb.lower(root.get("especialidad"))), especialidadPattern)) :
											cb.like(cb.function("unaccent", String.class, cb.lower(root.get("especialidad"))), especialidadPattern));
								}
							}
						if(idUsuario != null)
							if(!idUsuario.isEmpty())
								tc = (tc != null ? cb.and(tc, usuario.in(idUsuario)) : usuario.in(idUsuario));
						if(idTipoEvento != null)
							if(!idTipoEvento.isEmpty())
								tc = (tc != null ? cb.and(tc, tipoEvento.in(idTipoEvento)) : tipoEvento.in(idTipoEvento));
						if(titulo != null)
							if(!titulo.isEmpty()) {
								String pattern = "%" + sinAcentos(titulo.toLowerCase()) + "%";
								tc = (tc != null ? cb.and(tc, cb.like(cb.function("unaccent", String.class, cb.lower(root.get("titulo"))), pattern)) :
										cb.like(cb.function("unaccent", String.class, cb.lower(root.get("titulo"))), pattern));
							}
						if (startDate != null && endDate != null) {
							try {
								SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
								Date inicialDate = parseDate(sdf.format(startDate) + " 00:00:00", TimeUtils.LONG_DATE);
								Date finalDate = parseDate(sdf.format(endDate) + " 23:59:59", TimeUtils.LONG_DATE);
								tc = (tc != null) ?
										cb.and(tc, cb.greaterThanOrEqualTo(root.get("inicio"), inicialDate), cb.lessThanOrEqualTo(root.get("fin"), finalDate)) :
										cb.and(cb.greaterThanOrEqualTo(root.get("inicio"), inicialDate), cb.lessThanOrEqualTo(root.get("fin"), finalDate));
							} catch (Exception ex) {
								logger.warn("Error al convertir fechas", ex);
							}
						}
						if(idUsuarioRecibe != null)
							if(!idUsuarioRecibe.isEmpty())
								tc = (tc != null ? cb.and(tc, root.get("idUsuarioRecibe").in(idUsuarioRecibe)) :  root.get("idUsuarioRecibe").in(idUsuarioRecibe));
						if(idPaciente != null)
							if(!idPaciente.isEmpty())
								tc = (tc != null ? cb.and(tc, root.get("idPaciente").in(idPaciente)) : root.get("idPaciente").in(idPaciente));
						if(regionSanitaria != null)
							if(!regionSanitaria.isEmpty())
								tc = (tc != null ? cb.and(tc, root.get("regionSanitaria").in(regionSanitaria)) : root.get("regionSanitaria").in(regionSanitaria));
						if(unidadMedica != null)
							if(!unidadMedica.isEmpty())
								tc = (tc != null ? cb.and(tc, root.get("unidadMedica").in(unidadMedica)) : root.get("unidadMedica").in(unidadMedica));
						if(status != null)
							if(!status.isEmpty())
								tc = (tc != null ? cb.and(tc, root.get("status").in(status)) : root.get("status").in(status));
						return tc;
					}
			);
			eventosPage = eventosRepository.findAll(spec, request);
			eventosPage.getContent().forEach(eventos -> eventosViewList.add(eventosConverter.toView(eventos, Boolean.TRUE)));
			return new PageImpl<>(eventosViewList, request, eventosPage.getTotalElements());
		} catch (Exception ex) {
			EventosException eveE = new EventosException("Ocurrio un error al seleccionar lista Eventos paginable", EventosException.LAYER_SERVICE, EventosException.ACTION_SELECT);
			logger.error(ExceptionServiceCode.GROUP + "===>>>Error al tratar de seleccionar lista Eventos paginable - error: ", ex);
			throw eveE;
		}
	}

	@Override
	public List<EventosView> getEventosSearch(List<Long> idUsuario, List<Integer> idTipoEvento,
											  String titulo, Long startDate, Long endDate, List<Long> idUsuarioRecibe,
											  List<String> idPaciente, List<String> especialidad, List<Long> regionSanitaria,
											  List<String> unidadMedica, List<Integer> status, String orderColumn, String orderType) throws EventosException {
		try {
			if(!colOrderNames.containsKey(orderColumn)) {
				logger.info("getEventosSearch() - No existen ordenamiento por la columna: {}, asignando una por defecto", orderColumn);
				orderColumn = "titulo";
			}
			logger.info("getEventosSearch(): - idUsuario: {} - idTipoEvento: {} - startDate: {} - endDate: {} - orderColumn: {} - orderType: {}",
					idUsuario, idTipoEvento, startDate, endDate, orderColumn, orderType);
			List<EventosView> eventosViewList = new ArrayList<>();
			List<Eventos> eventosList = null;
			Sort sort;
			if (orderType.equalsIgnoreCase("asc"))
				sort = new Sort(Sort.Direction.ASC, (String) colOrderNames.get(orderColumn));
			else
				sort = new Sort(Sort.Direction.DESC, (String) colOrderNames.get(orderColumn));
			Specifications<Eventos> spec = Specifications.where(
					(root, query, cb) -> {
						Predicate tc = null;
						Expression<Long> usuario = root.get("idUsuarioCrea");
						Expression<Long> tipoEvento = root.get("catalogoTipoEvento");
						if(especialidad != null)
							if(!especialidad.isEmpty()) {
								for(String element : especialidad) {
									String especialidadPattern = "%" + sinAcentos(element.toLowerCase()) + "%";
									tc = (tc != null ? cb.or(tc, cb.like(cb.function("unaccent", String.class, cb.lower(root.get("especialidad"))), especialidadPattern)) :
											cb.like(cb.function("unaccent", String.class, cb.lower(root.get("especialidad"))), especialidadPattern));
								}
							}
						if(idUsuario != null)
							if(!idUsuario.isEmpty())
								tc = (tc != null ? cb.and(tc, usuario.in(idUsuario)) : usuario.in(idUsuario));
						if(idTipoEvento != null)
							if(!idTipoEvento.isEmpty())
								tc = (tc != null ? cb.and(tc, tipoEvento.in(idTipoEvento)) : tipoEvento.in(idTipoEvento));
						if(titulo != null)
							if(!titulo.isEmpty()) {
								String pattern = "%" + sinAcentos(titulo.toLowerCase()) + "%";
								tc = (tc != null ? cb.and(tc, cb.like(cb.function("unaccent", String.class, cb.lower(root.get("titulo"))), pattern)) :
										cb.like(cb.function("unaccent", String.class, cb.lower(root.get("titulo"))), pattern));
							}
						if (startDate != null && endDate != null) {
							try {
								SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
								Date inicialDate = parseDate(sdf.format(startDate) + " 00:00:00", TimeUtils.LONG_DATE);
								Date finalDate = parseDate(sdf.format(endDate) + " 23:59:59", TimeUtils.LONG_DATE);
								tc = (tc != null) ?
										cb.and(tc, cb.greaterThanOrEqualTo(root.get("inicio"), inicialDate), cb.lessThanOrEqualTo(root.get("fin"), finalDate)) :
										cb.and(cb.greaterThanOrEqualTo(root.get("inicio"), inicialDate), cb.lessThanOrEqualTo(root.get("fin"), finalDate));
							} catch (Exception ex) {
								logger.warn("Error al convertir fechas", ex);
							}
						}
						if(idUsuarioRecibe != null)
							if(!idUsuarioRecibe.isEmpty())
								tc = (tc != null ? cb.and(tc, root.get("idUsuarioRecibe").in(idUsuarioRecibe)) :  root.get("idUsuarioRecibe").in(idUsuarioRecibe));
						if(idPaciente != null)
							if(!idPaciente.isEmpty())
								tc = (tc != null ? cb.and(tc, root.get("idPaciente").in(idPaciente)) : root.get("idPaciente").in(idPaciente));
						if(regionSanitaria != null)
							if(!regionSanitaria.isEmpty())
								tc = (tc != null ? cb.and(tc, root.get("regionSanitaria").in(regionSanitaria)) : root.get("regionSanitaria").in(regionSanitaria));
						if(unidadMedica != null)
							if(!unidadMedica.isEmpty())
								tc = (tc != null ? cb.and(tc, root.get("unidadMedica").in(unidadMedica)) : root.get("unidadMedica").in(unidadMedica));
						if(status != null)
							if(!status.isEmpty())
								tc = (tc != null ? cb.and(tc, root.get("status").in(status)) : root.get("status").in(status));
						return tc;
					}
			);
			eventosList = eventosRepository.findAll(spec, sort);
			eventosList.forEach(eventos -> eventosViewList.add(eventosConverter.toView(eventos, Boolean.TRUE)));
			return eventosViewList;
		} catch (Exception ex) {
			EventosException eveE = new EventosException("Ocurrió un error al seleccionar lista Eventos", EventosException.LAYER_SERVICE, EventosException.ACTION_SELECT);
			eveE.addError(ex.toString());
			logger.error(ExceptionServiceCode.GROUP + "===>>>Error al tratar de seleccionar lista Eventos - error: ", ex);
			throw eveE;
		}
	}

	private String sinAcentos(String cadena) {
		return Normalizer.normalize(cadena, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
	}

	@Transactional(readOnly = false, rollbackFor = {EventosException.class})
	@Override
	public void deleteEventos(Long idEventos) throws EventosException {
		try {
			if (!eventosRepository.exists(idEventos)) {
				logger.error("===>>>idEventos no encontrado: {}", idEventos);
				EventosException eveE = new EventosException("No se encuentra en el sistema Eventos", EventosException.LAYER_DAO, EventosException.ACTION_VALIDATE);
				eveE.addError("idEventos no encontrado: " + idEventos);
				throw eveE;
			}
			Eventos eventos = eventosRepository.findOne(idEventos);
			eventos.setCatalogoTipoEvento(null);
			eventosRepository.delete(idEventos);
		} catch (EventosException eveE) {
			throw eveE;
		} catch (ConstraintViolationException cve) {
			logger.error("===>>>Error en la validacion");
			EventosException eveE = new EventosException("Error en la validacion", EventosException.LAYER_DAO, EventosException.ACTION_VALIDATE);
			final Set<ConstraintViolation<?>> violaciones = cve.getConstraintViolations();
			for (Iterator<ConstraintViolation<?>> iterator = violaciones.iterator(); iterator.hasNext(); ) {
				ConstraintViolation<?> siguiente = iterator.next();
				eveE.addError(siguiente.getPropertyPath() + ": " + siguiente.getMessage());
			}
			throw eveE;
		} catch (DataIntegrityViolationException dive) {
			EventosException eveE = new EventosException("No fue posible obtener  Eventos", EventosException.LAYER_DAO, EventosException.ACTION_INSERT);
			eveE.addError("Ocurrio un error al obtener Eventos");
			logger.error("===>>>Error al obtener nuevo Eventos - CODE: {} - {}", eveE.getExceptionCode(), idEventos, dive);
			throw eveE;
		} catch (Exception ex) {
			EventosException eveE = new EventosException("Error inesperado al obtener  Eventos", EventosException.LAYER_DAO, EventosException.ACTION_INSERT);
			eveE.addError("Ocurrio un error al obtener Eventos");
			logger.error("===>>>Error al obtener nuevo Eventos - CODE: {} - {}", eveE.getExceptionCode(), idEventos, ex);
			throw eveE;
		}
	}

	@Transactional(readOnly = false, rollbackFor = {EventosException.class})
	@Override
	public void rescheduleEventos(Long idEventos, Date nuevaFechaEvento, Date nuevaFechaFin) throws EventosException {
		try {
			List<Eventos> eventosList = new ArrayList<>();
			long inicioView = nuevaFechaEvento.getTime();
			long finView = nuevaFechaFin.getTime();

			if (inicioView >= finView) {
				String textoError = "El tiempo final NO puede ser MENOR que el tiempo inicial-> fechaInicio: " + nuevaFechaEvento + " - fechaFin: " + nuevaFechaFin;
				logger.error("===>>>" + textoError);
				EventosException eveE = new EventosException("Existe un Error", EventosException.LAYER_DAO, EventosException.ACTION_VALIDATE);
				eveE.addError(textoError);
				throw eveE;
			}

			if (!eventosRepository.exists(idEventos)) {
				logger.error("===>>>idEventos no encontrado: {}", idEventos);
				EventosException eveE = new EventosException("No se encuentra en el sistema Eventos", EventosException.LAYER_DAO, EventosException.ACTION_VALIDATE);
				eveE.addError("idEventos no encontrado: " + idEventos);
				throw eveE;
			}
			Eventos eventos = eventosRepository.findOne(idEventos);

			//         EL EVENTO ES UNA CITA
			if (eventos.getCatalogoTipoEvento().getIdTipoEvento() == 2) {
				//         DONE: que la hora inicio no este duplicado en otro registro
				Long idUsuario = eventos.getIdUsuarioCrea();

				List<Eventos> duplicado = eventosRepository.buscarPorIdUsuarioCreaAndInicio(idUsuario, nuevaFechaEvento, 2);
				if (duplicado != null && !duplicado.isEmpty()) {
					for (Eventos eveDup : duplicado) {
						if (eventos.getIdEventos() != eveDup.getIdEventos()) {
							String textoError = "Eventos DUPLICADO en otro registro: idEventos: " + eveDup.getIdEventos() +
									" - fechaInicio: " + eveDup.getInicio();
							logger.error("===>>>" + textoError);
							EventosException eveE = new EventosException("Existe un Error", EventosException.LAYER_DAO, EventosException.ACTION_VALIDATE);
							eveE.addError(textoError);
							throw eveE;
						}
					}
				}

				//         DONE: que no se cruce con otro registro
				List<Eventos> cruce = eventosRepository.buscarPorIdUsuarioCreaAndNuevaFechaEntreInicioFin(idUsuario, nuevaFechaEvento, 2);
				if (cruce != null && !cruce.isEmpty()) {
					if (eventos.getIdEventos() != cruce.get(0).getIdEventos()) {
						String textoError = "Eventos CRUCE entre horas con otro registro: idEventos: " + cruce.get(0).getIdEventos() +
								" - fechaInicio: " + cruce.get(0).getInicio() +
								" - fechaFin: " + cruce.get(0).getFin();
						logger.error("===>>>" + textoError);
						EventosException eveE = new EventosException("Existe un Error", EventosException.LAYER_DAO, EventosException.ACTION_VALIDATE);
						eveE.addError(textoError);
						throw eveE;
					}
				}

				eventos.setInicio(nuevaFechaEvento);
				eventos.setFin(nuevaFechaFin);

				List<Eventos> cruceFinal = eventosRepository.buscarPorIdUsuarioCreaAndNuevaFechaEntreInicioFin(idUsuario, eventos.getFin(), 2);
				if (cruceFinal != null && !cruceFinal.isEmpty()) {
					if (eventos.getIdEventos() != cruceFinal.get(0).getIdEventos()) {
						String textoError = "Eventos CRUCE entre horas con otro registro: idEventos: " + cruceFinal.get(0).getIdEventos() +
								" - fechaInicio: " + cruceFinal.get(0).getInicio() +
								" - fechaFin: " + cruceFinal.get(0).getFin();
						logger.error("===>>>" + textoError);
						EventosException eveE = new EventosException("Existe un Error", EventosException.LAYER_DAO, EventosException.ACTION_VALIDATE);
						eveE.addError(textoError);
						throw eveE;
					}
				}

				List<Eventos> eventosOtro = eventosRepository.findByIdConsultaAndIdEventosIsNot(eventos.getIdConsulta(), eventos.getIdEventos());
				if (eventosOtro != null) {
					for(Eventos e: eventosOtro) {
						e.setInicio(nuevaFechaEvento);
						e.setFin(nuevaFechaFin);
						e.setAlerta(nuevaFechaEvento);
						eventosList.add(e);
					}
				}
				eventosList.add(eventos);

				CatEstadoConsultaView catEstadoConsultaView = new CatEstadoConsultaView();
				catEstadoConsultaView.setIdEstadoConsulta(1);
				catEstadoConsultaView.setActivo(Boolean.TRUE);
				catEstadoConsultaView.setDescripcion("Nueva");
				catEstadoConsultaView.setColor("#FF0000");

				consultaService.consultreschedule(eventos.getIdConsulta(), nuevaFechaEvento, nuevaFechaFin, catEstadoConsultaView);
			}

			//         EL EVENTO ES UN RECORDATORIO
			//         if (eventos.getTipoEventoId() == 3) {
			if (eventos.getCatalogoTipoEvento().getIdTipoEvento() == 3) {
				eventos.setInicio(nuevaFechaEvento);
				eventos.setFin(nuevaFechaEvento);
				eventos.setAlerta(nuevaFechaEvento);
				eventos.setInvitadosList(new ArrayList<>());
				//            eventos.setInvitadosList(null);
				eventos.setDescripcion(null);
				eventos.setIdConsulta(null);
				eventosList.add(eventos);
			}

			//         EL EVENTO ES UNA TAREA
			//         if (eventos.getTipoEventoId() == 4) {
			if (eventos.getCatalogoTipoEvento().getIdTipoEvento() == 4) {
				eventos.setInicio(nuevaFechaEvento);
				eventos.setFin(nuevaFechaEvento);
				eventos.setAlerta(nuevaFechaEvento);
				eventos.setInvitadosList(new ArrayList<>());
				//            eventos.setInvitadosList(null);
				eventos.setIdConsulta(null);
				eventosList.add(eventos);
			}

			//         eventos = eventosConverter.toEntity(eventosView, eventos, Boolean.TRUE);
			logger.debug("===>>>Reagendar Eventos: {}", eventos);
			eventos.setAlerta(nuevaFechaEvento);
			eventosRepository.save(eventosList);
			//         return eventosConverter.toView(eventos, Boolean.TRUE);
			//         eventosConverter.toView(eventos, Boolean.TRUE);

		} catch (EventosException eveE) {
			throw eveE;
		} catch (ConstraintViolationException cve) {
			logger.error("===>>>Error en la validacion");
			EventosException eveE = new EventosException("Error en la validacion", EventosException.LAYER_DAO, EventosException.ACTION_VALIDATE);
			final Set<ConstraintViolation<?>> violaciones = cve.getConstraintViolations();
			for (Iterator<ConstraintViolation<?>> iterator = violaciones.iterator(); iterator.hasNext(); ) {
				ConstraintViolation<?> siguiente = iterator.next();
				eveE.addError(siguiente.getPropertyPath() + ": " + siguiente.getMessage());
			}
			throw eveE;
		} catch (DataIntegrityViolationException dive) {
			EventosException eveE = new EventosException("No fue posible obtener  Eventos", EventosException.LAYER_DAO, EventosException.ACTION_INSERT);
			eveE.addError("Ocurrio un error al obtener Eventos");
			logger.error("===>>>Error al obtener nuevo Eventos - CODE: {} - {}", eveE.getExceptionCode(), idEventos, dive);
			throw eveE;
		} catch (Exception ex) {
			EventosException eveE = new EventosException("Error inesperado al obtener  Eventos", EventosException.LAYER_DAO, EventosException.ACTION_INSERT);
			eveE.addError("Ocurrio un error al obtener Eventos");
			logger.error("===>>>Error al obtener nuevo Eventos - CODE: {} - {}", eveE.getExceptionCode(), idEventos, ex);
			throw eveE;
		}
	}


	@Override
	public List<EventosView> getDetailsByIdConsulta(Long idConsulta) throws EventosException {
		try {
			List<Eventos> eventosList = eventosRepository.findByIdConsulta(idConsulta);
			if(eventosList==null || eventosList.isEmpty()){
				logger.error("===>>>Eventos no encontrado por idConsulta: {}", idConsulta);
				EventosException eveE = new EventosException("No se encuentra en el sistema Eventos", EventosException.LAYER_DAO, EventosException.ACTION_VALIDATE);
				eveE.addError("Eventos no encontrado por idConsulta:" + idConsulta);
				throw eveE;
			}
			List<EventosView> eventosViewList= new ArrayList<>();
			for(Eventos ev: eventosList){
				eventosViewList.add(eventosConverter.toView(ev,Boolean.TRUE));
			}
			return eventosViewList;
		} catch (EventosException eveE) {
			throw eveE;
		} catch (ConstraintViolationException cve) {
			logger.error("===>>>Error en la validacion");
			EventosException eveE = new EventosException("Error en la validacion", EventosException.LAYER_DAO, EventosException.ACTION_VALIDATE);
			final Set<ConstraintViolation<?>> violaciones = cve.getConstraintViolations();
			for (Iterator<ConstraintViolation<?>> iterator = violaciones.iterator(); iterator.hasNext(); ) {
				ConstraintViolation<?> siguiente = iterator.next();
				eveE.addError(siguiente.getPropertyPath() + ": " + siguiente.getMessage());
			}
			throw eveE;
		} catch (DataIntegrityViolationException dive) {
			EventosException eveE = new EventosException("No fue posible obtener  Eventos", EventosException.LAYER_DAO, EventosException.ACTION_INSERT);
			eveE.addError("Ocurrio un error al obtener Eventos");
			logger.error("===>>>Error al obtener nuevo Eventos - CODE: {} - {}", eveE.getExceptionCode(), idConsulta, dive);
			throw eveE;
		} catch (Exception ex) {
			EventosException eveE = new EventosException("Error inesperado al obtener  Eventos", EventosException.LAYER_DAO, EventosException.ACTION_INSERT);
			eveE.addError("Ocurrio un error al obtener Eventos");
			logger.error("===>>>Error al obtener nuevo Eventos - CODE: {} - {}", eveE.getExceptionCode(), idConsulta, ex);
			throw eveE;
		}
	}

	@Transactional(rollbackFor = {EventosException.class})
	@Override
	public void updateEstatusEvento(Long idEvento, Integer status) throws EventosException {
		try {
			Eventos evento = eventosRepository.findOne(idEvento);
			if(evento == null) {
				logger.error("===>>>Evento no encontrado: {}", idEvento);
				EventosException eveE = new EventosException("No se encuentra en el sistema Eventos", EventosException.LAYER_DAO, EventosException.ACTION_VALIDATE);
				eveE.addError("Evento no encontrado:" + idEvento);
				throw eveE;
			}
			evento.setStatus(status);
			eventosRepository.save(evento);
		} catch (Exception ex) {
			EventosException eveE = new EventosException("Error inesperado al actualiar Eventos", EventosException.LAYER_DAO, EventosException.ACTION_INSERT);
			eveE.addError("Ocurrió un error al obtener Eventos");
			logger.error("===>>>Error al obtener Eventos - CODE: {} - {}", eveE.getExceptionCode(), idEvento, ex);
			throw eveE;
		}
	}

	@Transactional(rollbackFor = {EventosException.class})
	@Override
	public Long getEventosValue() throws EventosException {
		return eventosRepository.nextValueSeq();
	}
}
