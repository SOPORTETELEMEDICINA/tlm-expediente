package net.amentum.niomedic.expediente.service.impl;

import static net.amentum.common.TimeUtils.parseDate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import net.amentum.common.TimeUtils;
import net.amentum.niomedic.expediente.converter.PeriodosControlesConverter;
import net.amentum.niomedic.expediente.exception.ExceptionServiceCode;
import net.amentum.niomedic.expediente.exception.PeriodosControlesException;
import net.amentum.niomedic.expediente.model.Controles;
import net.amentum.niomedic.expediente.model.MedicionesPaciente;
import net.amentum.niomedic.expediente.model.PeriodosControles;
import net.amentum.niomedic.expediente.persistence.PeriodosControlesRepository;
import net.amentum.niomedic.expediente.persistence.ControlesRepository;
import net.amentum.niomedic.expediente.persistence.MedicionesPacienteRepository;
import net.amentum.niomedic.expediente.service.PeriodosControlesService;
import net.amentum.niomedic.expediente.views.PeriodosControlesView;

@Service
@Transactional(readOnly = true)
@Slf4j
public class PeriodosControlesServiceImpl implements PeriodosControlesService{
	private MedicionesPacienteRepository medicionesPacienteRepository;

	@Autowired
	public void setMedicionesPacienteRepository(MedicionesPacienteRepository medicionesPacienteRepository) {
		this.medicionesPacienteRepository = medicionesPacienteRepository;
	}

	private ControlesRepository controlesRepository;

	@Autowired
	public void setControlesRepository(ControlesRepository controlesRepository) {
		this.controlesRepository = controlesRepository;
	}

	private PeriodosControlesConverter periodosControlesConverter;

	@Autowired
	public void SetPeriodosControlesConverter(PeriodosControlesConverter periodosControlesConverter) {
		this.periodosControlesConverter = periodosControlesConverter;
	}
	private PeriodosControlesRepository periodosControlesRepository;

	@Autowired
	public void setPeriodosControlesRepository(PeriodosControlesRepository periodosControlesRepository) {
		this.periodosControlesRepository = periodosControlesRepository;
	}


	@Transactional(readOnly = false, rollbackFor = { PeriodosControlesException.class })
	@Override
	public Set<PeriodosControlesView> createPeriodosControles(Long idControl,Set<PeriodosControlesView> periodosControlesView) throws PeriodosControlesException {
		try {
			Controles encontrando= buscarControles(idControl);
			periodosControlesView=validaciones(periodosControlesView, encontrando);
			List<PeriodosControles> pc=periodosControlesConverter.toEntity(periodosControlesView, encontrando);
			periodosControlesRepository.save(pc);
			log.info("createPeriodosControles() - Creando periodos para el control con el id:{}",idControl);
			pc= new ArrayList<PeriodosControles>();
			pc=buscarPeriodosPorControles(encontrando);
			return periodosControlesConverter.toEntView(pc,Boolean.TRUE);

		} catch (PeriodosControlesException ce) {
			throw ce;
		} catch	(ConstraintViolationException cve) {
			log.error("createPeriodosControles() - Ocurrio un error al Crear los periodos en la DB - error :{}", cve);
			throw new PeriodosControlesException(HttpStatus.BAD_REQUEST,
					String.format(PeriodosControlesException.SERVER_ERROR, "Crear"));
		} catch	(DataIntegrityViolationException dive) {
			log.error("createPeriodosControles() - Ocurrio un error al Crear los periodos en la DB - error :{}", dive);
			throw new PeriodosControlesException(HttpStatus.CONFLICT,
					String.format(PeriodosControlesException.SERVER_ERROR, "Crear"));
		} catch (Exception e) {
			log.error(ExceptionServiceCode.CONTROLES
					+ " createControles() -  Ocurrio Un error al  crear los Periodos - error:{}", e);

			throw new PeriodosControlesException(HttpStatus.INTERNAL_SERVER_ERROR,
					String.format(PeriodosControlesException.SERVER_ERROR, "Crear"));

		}
	}

	@Transactional(readOnly = false, rollbackFor = { PeriodosControlesException.class })
	@Override
	public Set<PeriodosControlesView> updatePeriodosControles(Long idControl,
			Set<PeriodosControlesView> periodosControlesView) throws PeriodosControlesException {
		try {
			Controles encontrando= buscarControles(idControl);
			periodosControlesView=validaciones(periodosControlesView, encontrando);
			//buscando periodos por controles
			List<PeriodosControles> PeriodosControlesList=buscarPeriodosPorControles(encontrando);
			//buscando ids No existentes
			Set<Long> idNoExistente=idNoExistente(PeriodosControlesList,periodosControlesView);
			//eliminando ids No Existentes
			for(Long id:idNoExistente) {
				PeriodosControles periodosControles =periodosControlesRepository.findOne(id);
				periodosControles.setControles(null);
				periodosControlesRepository.delete(periodosControles);
			}
			PeriodosControlesList= periodosControlesConverter.toEntity(periodosControlesView, encontrando);
			log.info("updatePeriodosControles() - Guardando las Actualizando en los periodos para el control con el id:{}",idControl);
			periodosControlesRepository.save(PeriodosControlesList);
			return periodosControlesConverter.toEntView(PeriodosControlesList, Boolean.TRUE);
		} catch (PeriodosControlesException ce) {
			throw ce;
		} catch	(ConstraintViolationException cve) {
			log.error("updatePeriodosControles() - Ocurrio un error al Actualizar los periodos en la DB - error :{}", cve);
			throw new PeriodosControlesException(HttpStatus.BAD_REQUEST,
					String.format(PeriodosControlesException.SERVER_ERROR, "Actualizar"));
		} catch	(DataIntegrityViolationException dive) {
			log.error("updatePeriodosControles() - Ocurrio un error al Actualizar los periodos en la DB - error :{}", dive);
			throw new PeriodosControlesException(HttpStatus.CONFLICT,
					String.format(PeriodosControlesException.SERVER_ERROR, "Actualizar"));
		} catch (Exception e) {
			log.error(ExceptionServiceCode.CONTROLES
					+ " createControles() -  Ocurrio Un error al  crear los Periodos - error:{}", e);
			throw new PeriodosControlesException(HttpStatus.INTERNAL_SERVER_ERROR,
					String.format(PeriodosControlesException.SERVER_ERROR, "Actualizar"));

		}
	}

	@Transactional(readOnly = false, rollbackFor = { PeriodosControlesException.class })
	@Override
	public Set<PeriodosControlesView> getPeriodosControles(Long idControl) throws PeriodosControlesException {
		try {
			Controles control= buscarControles(idControl);
			log.info("getPeriodosControles() - Obteniendo la lista de periodos");
			List<PeriodosControles> periodosControlesList=buscarPeriodosPorControles(control);
			return periodosControlesConverter.toEntView(periodosControlesList, Boolean.FALSE);
		} catch (PeriodosControlesException ce) {
			throw ce;
		} catch (Exception e) {
			log.error(ExceptionServiceCode.CONTROLES
					+ " getPeriodosControles() -  Ocurrio Un error al  Obtener los Periodos - error:{}", e);
			throw new PeriodosControlesException(HttpStatus.INTERNAL_SERVER_ERROR,
					String.format(PeriodosControlesException.SERVER_ERROR, "Obtener"));

		}
	}
	@Transactional(readOnly = false, rollbackFor = { PeriodosControlesException.class })
	@Override
	public void deletePeriodosControles(Long idControl) throws PeriodosControlesException {
		try {
			Controles encontrando= buscarControles(idControl);
			List<PeriodosControles> PeriodosControlesList=buscarPeriodosPorControles(encontrando);
			log.info("deletePeriodosControles() - Eliminando la Lista de periodos: {}",PeriodosControlesList);
			for(PeriodosControles pc:PeriodosControlesList) {
				pc.setControles(null);
				periodosControlesRepository.delete(pc);
			}
		} catch (PeriodosControlesException ce) {
			throw ce;
		} catch	(DataIntegrityViolationException dive) {
			log.error("deletePeriodosControles() - Ocurrio un error al Eliminar los periodos en la DB - error :{}", dive);
			throw new PeriodosControlesException(HttpStatus.CONFLICT,
					String.format(PeriodosControlesException.SERVER_ERROR, "Eliminar"));
		} catch (Exception e) {
			log.error(ExceptionServiceCode.CONTROLES
					+ " createControles() -  Ocurrio Un error al  ELiminar los Periodos - error:{}", e);
			throw new PeriodosControlesException(HttpStatus.INTERNAL_SERVER_ERROR,
					String.format(PeriodosControlesException.SERVER_ERROR, "Eliminar"));

		}

	}

	public List<PeriodosControles> buscarPeriodosPorControles(Controles control)throws Exception,PeriodosControlesException {
		List<PeriodosControles> PeriodosControlesList;
		try {
			log.info("buscarPeriodosPorControles() - Buscando Lista de periodos para el control con el id:{}",control.getIdControl());
			PeriodosControlesList=periodosControlesRepository.findByControles(control);
		} catch (Exception e) {
			log.error(ExceptionServiceCode.CONTROLES
					+ " buscarPeriodosPorControles() -  Ocurrio Un error al  buscar un Control por idPaciente - error:{}", e);
			throw e;
		}
		if (PeriodosControlesList.isEmpty()) {
			throw new PeriodosControlesException(HttpStatus.NOT_FOUND, String.format(PeriodosControlesException.ITEM_LIST_NO_ENCONTRADO,control.getIdControl()));
		}
		return PeriodosControlesList;
	}

	public Controles buscarControles(Long idControl) throws PeriodosControlesException {
		log.info("buscarControles() - Buscando control con el id:{}",idControl);
		Controles encontrado=controlesRepository.findOne(idControl);
		if (encontrado == null) {
			log.info("buscarControles() - No se encontro el control con el id:{}",idControl);
			throw new PeriodosControlesException(HttpStatus.NOT_FOUND, String.format(PeriodosControlesException.ITEM_NO_ENCONTRADO,idControl));
		}
		return encontrado;

	}

	public Set<PeriodosControlesView> validaciones(Set<PeriodosControlesView> periodosControlesView, Controles controles)throws PeriodosControlesException {
		log.info("validaciones() -  Validando  Periodos {}", periodosControlesView);
		while(periodosControlesView.contains(null)) {
			periodosControlesView.remove(null);
		}
		Integer count=0;
		Set<PeriodosControlesView> pcvDiabetes= new HashSet<PeriodosControlesView>();
		Set<PeriodosControlesView> pcvHipertension = new HashSet<PeriodosControlesView>();
		Set<PeriodosControlesView> pcvOximetria = new HashSet<PeriodosControlesView>();
		for(PeriodosControlesView pcv:periodosControlesView) {
			if(pcv.getDiabetes()) {
				pcvDiabetes.add(pcv);
				count++;
			}
			if(pcv.getHipertension()) {
				count++;
				pcvHipertension.add(pcv);
			} if(pcv.getOximetria()) {
				count++;
				pcvOximetria.add(pcv);
			}
			if(count>=2) {
				throw new PeriodosControlesException(HttpStatus.BAD_REQUEST, "Solo se puede asignar un padecimiento como “true” por periodo");
			}else {
				count=0;
			}
		}
		if(pcvDiabetes.isEmpty() && pcvHipertension.isEmpty() && pcvOximetria.isEmpty()) {
			throw new PeriodosControlesException(HttpStatus.BAD_REQUEST, String.format(PeriodosControlesException.PADECIMIENTO,"al menos"));
		}
		periodosControlesView = new HashSet<PeriodosControlesView>();
		if(controles.getDiabetes()) {
			periodosControlesView.addAll(pcvDiabetes);
		}
		if(controles.getHipertension()) {

			periodosControlesView.addAll(pcvHipertension);
		}
		if(controles.getOximetria()) {
			periodosControlesView.addAll(pcvOximetria);
		}
		return periodosControlesView;
	}

	public  Set<Long> idNoExistente(List<PeriodosControles> pc, Set<PeriodosControlesView> pcV) {
		Set<Long> idpc = new HashSet<>();
		idpc.addAll(
				pc.stream().map(pc1 ->{
					Long id = pc1.getIdPeriodoControl();
					return id;
				}).collect(Collectors.toList()));

		Set<Long> idpcV = new HashSet<>();

		pcV.forEach( pcV1 -> {
			if(pcV1.getIdPeriodoControl()!=null) {
				idpcV.add(pcV1.getIdPeriodoControl());
			}
		});

		Set<Long> noExiste = new HashSet<>(idpc);
		noExiste.removeAll(idpcV);
		return noExiste;
	}


	@Override
	public Set<PeriodosControlesView> getPeriodosPorPaciente(UUID idPaciente) throws PeriodosControlesException {
		try {
			Date hoy = new Date();

			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			Date fechaInicio=  parseDate(sdf.format(hoy) + " 00:00:00", TimeUtils.LONG_DATE);
			Date fechaFin= parseDate(sdf.format(hoy) + " 23:59:59", TimeUtils.LONG_DATE);
			List<MedicionesPaciente> mpList = medicionesPacienteRepository.findByFechaCreacionAndIdPaciente(idPaciente,fechaInicio,fechaFin);
			List<Long> idPeriodo=new ArrayList<Long>();
			for(MedicionesPaciente mp:mpList) {
				idPeriodo.add(mp.getIdPeriodoControles());
			}
			Calendar calendario = Calendar.getInstance();
			calendario.setTime(hoy);
			Integer diaSemana= calendario.get(Calendar.DAY_OF_WEEK);
			log.info("getPeriodosPorPaciente() -  Obteniendo Periodos del dia:{} para el idPaciente:{}",diaSemana,idPaciente );
			List<PeriodosControles> periodosControlesList= new ArrayList<PeriodosControles>();
			if(idPeriodo.isEmpty()) {
				periodosControlesList= periodosControlesRepository.findByIdPaciente(idPaciente, diaSemana);
			}else {
				periodosControlesList= periodosControlesRepository.findByIdPacienteAndIdPeriodoList(idPaciente, diaSemana, idPeriodo);
			}

			return periodosControlesConverter.toEntView(periodosControlesList, Boolean.FALSE);
		} catch	(DataIntegrityViolationException dive) {
			log.error("getPeriodosPorPaciente() - Ocurrio un error al Obtener los periodos en la DB - error :{}", dive);
			throw new PeriodosControlesException(HttpStatus.CONFLICT,
					String.format(PeriodosControlesException.SERVER_ERROR, "Obtener"));
		} catch (Exception e) {
			log.error(ExceptionServiceCode.CONTROLES
					+ " getPeriodosPorPaciente() -  Ocurrio Un error al Obtener los Periodos - error:{}", e);
			throw new PeriodosControlesException(HttpStatus.INTERNAL_SERVER_ERROR,
					String.format(PeriodosControlesException.SERVER_ERROR, "Obtener"));

		}
	}

}
