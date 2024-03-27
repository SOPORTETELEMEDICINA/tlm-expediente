package net.amentum.niomedic.expediente.service.impl;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import net.amentum.niomedic.expediente.converter.ControlesConverter;
import net.amentum.niomedic.expediente.exception.ControlesException;
import net.amentum.niomedic.expediente.exception.ExceptionServiceCode;
import net.amentum.niomedic.expediente.model.Controles;
import net.amentum.niomedic.expediente.persistence.ControlesRepository;
import net.amentum.niomedic.expediente.service.ControlesService;
import net.amentum.niomedic.expediente.views.ControlesView;
import net.amentum.niomedic.expediente.views.PeriodosControlesView;

@Service
@Transactional(readOnly = true)
@Slf4j
public class ControlesServiceImpl implements ControlesService {

	private ControlesRepository controlesRepository;
	
	private PeriodosControlesServiceImpl periodosControlesServiceImpl;

	@Autowired
	public void setControlesRepository(ControlesRepository controlesRepository) {
		this.controlesRepository = controlesRepository;
	}

	private ControlesConverter controlesConverter;

	@Autowired
	public void SetControlesConverter(ControlesConverter controlesConverter) {
		this.controlesConverter = controlesConverter;
	}
	
	@Autowired 
	public void setPeriodosControlesServiceImpl(PeriodosControlesServiceImpl periodosControlesServiceImpl) {
		this.periodosControlesServiceImpl = periodosControlesServiceImpl;
	}

	@Transactional(readOnly = false, rollbackFor = { ControlesException.class })
	@Override
	public ControlesView createControles(ControlesView controlesView) throws ControlesException {
		try {
			validaciones(controlesView, Boolean.FALSE);
			Controles controles = controlesConverter.toEntity(controlesView, new Controles(), Boolean.FALSE);
			controlesRepository.save(controles);
			log.info("createControles() - Los Controles se han Guardado Exitosamente");
			Set<PeriodosControlesView> pcv= new HashSet<>();
			if(!controlesView.getPeriodosControlesView().isEmpty()) {
				log.info("createControles() - Creando Periodos para el control con el id:{}",controles.getIdControl());
				pcv=periodosControlesServiceImpl.createPeriodosControles(controles.getIdControl(), controlesView.getPeriodosControlesView());
			}
			controlesView = controlesConverter.toViews(controlesView, controles);
			controlesView.setPeriodosControlesView(pcv);
			return controlesView;
		} catch (ControlesException ce) {
			throw ce;
		} catch (DataIntegrityViolationException e) {
			ControlesException ce = new ControlesException(HttpStatus.CONFLICT,
					String.format(ControlesException.ITEM_EXISTENTE, controlesView.getIdPaciente()));
			log.error(ExceptionServiceCode.CONTROLES
					+ " createControles() -  Ocurrio Un error al Guardar los Controles en la DB - error:{}", e);
			throw ce;
		} catch (Exception e) {
			ControlesException ce = new ControlesException(HttpStatus.INTERNAL_SERVER_ERROR,
					String.format(ControlesException.SERVER_ERROR, "Insertar"));
			log.error(ExceptionServiceCode.CONTROLES
					+ " createControles() -  Ocurrio Un error al  crear Controles - error:{}", e);
			throw ce;
		}
	}

	@Transactional(readOnly = false, rollbackFor = { ControlesException.class })
	@Override
	public ControlesView updateControles(ControlesView controlesView) throws ControlesException {
		try {
			Controles encontrado = buscarControl(controlesView.getIdPaciente());
			validaciones(controlesView, Boolean.TRUE);
			Controles controles = controlesConverter.toEntity(controlesView, new Controles(), Boolean.TRUE);
			controles.setIdControl(encontrado.getIdControl());
			controles.setNombreQuienCreo(encontrado.getNombreQuienCreo());
			controles.setIdUsuarioQuienCreo(encontrado.getIdUsuarioQuienCreo());
			controles.setFechaCreacion(encontrado.getFechaCreacion());
			controlesRepository.save(controles);
			log.info("updateControles() - Los Controles se han Guardado Exitosamente");
			Set<PeriodosControlesView> pcv= new HashSet<>();
			if(!controlesView.getPeriodosControlesView().isEmpty()) {
				log.info("updateControles() - Actualizando Periodos para el control con el id:{}",controles.getIdControl());
				pcv=periodosControlesServiceImpl.updatePeriodosControles(controles.getIdControl(), controlesView.getPeriodosControlesView());
			}
			return controlesConverter.toViews(controlesView, controles);
		} catch (ControlesException ce) {
			throw ce;
		} catch (Exception e) {
			ControlesException ce = new ControlesException(HttpStatus.INTERNAL_SERVER_ERROR,
					String.format(ControlesException.SERVER_ERROR, "Actualizar"));
			log.error(ExceptionServiceCode.CONTROLES
					+ " updateControles() -  Ocurrio Un error al  Actualizar Controles - error:{}", e);
			throw ce;
		}
	}

	@Override
	public ControlesView getControles(UUID idPaciente) throws ControlesException {
		try {
			Controles encontrando=buscarControl(idPaciente);
			log.info("getControles() - Regresando Controles para el paciente con el id:", idPaciente); 
			return controlesConverter.toViews(new ControlesView(), encontrando);
		} catch (ControlesException ce) {
			throw ce;
		} catch (Exception e) {
			ControlesException ce = new ControlesException(HttpStatus.INTERNAL_SERVER_ERROR,
					String.format(ControlesException.SERVER_ERROR, "Obtener"));
			log.error(ExceptionServiceCode.CONTROLES
					+ " getControles() -  Ocurrio Un error al  Obtener Obtener - error:{}", e);
			throw ce;
		}
	}
	@Transactional(readOnly = false, rollbackFor = { ControlesException.class })
	@Override
	public void deleteControles(UUID idPaciente) throws ControlesException {
		try {
			Controles encontrando=buscarControl(idPaciente);
			controlesRepository.delete(encontrando);
			log.info("deleteControles() - Control Eliminado para el paciente con el id:", idPaciente); 
		} catch (ControlesException ce) {
			throw ce;
		} catch (Exception e) {
			ControlesException ce = new ControlesException(HttpStatus.INTERNAL_SERVER_ERROR,
					String.format(ControlesException.SERVER_ERROR, "Eliminar"));
			log.error(ExceptionServiceCode.CONTROLES
					+ " deleteControles() -  Ocurrio Un error al  Eliminado Controles - error:{}", e);
			throw ce;
		}
	}
	
	private Controles buscarControl(UUID idPaciente) throws Exception,ControlesException {
		Controles encontrado;
		try {
			encontrado = controlesRepository.findByidPaciente(idPaciente);
		} catch (Exception e) {
			log.error(ExceptionServiceCode.CONTROLES
					+ " buscarControl() -  Ocurrio Un error al  buscar un Control por idPaciente - error:{}", e);
			throw e;
		}
		if (encontrado == null) {
			throw new ControlesException(HttpStatus.NOT_FOUND, String.format(ControlesException.ITEM_NO_ENCONTRADO,idPaciente));
		}
		return encontrado;
	}

	static void validaciones(ControlesView controlesView, Boolean Update) throws ControlesException {
		if (!controlesView.getDiabetes() && !controlesView.getHipertension() && !controlesView.getOximetria()) {
			throw new ControlesException(HttpStatus.BAD_REQUEST, "Por favor asigna un padecimiento");
		}
		// diabetes
		if (controlesView.getDiabetes()) {
			if (controlesView.getDLimiteRangoBajo() == null || controlesView.getDRangoBajo() == null
					|| controlesView.getDRangoAlto() == null || controlesView.getDLimiteRangoAlto() == null) {
				log.error(ExceptionServiceCode.CONTROLES
						+ " validaciones() - Los campos dLimiteRangoBajo, dRangoBajo, dRangoAlto, dLimiteRangoAlto no pueden ser NULL ó Vacios");
				throw new ControlesException(HttpStatus.BAD_REQUEST, String.format(ControlesException.NO_NULOS,
						"dLimiteRangoBajo, dRangoBajo, dRangoAlto, dLimiteRangoAlto"));
			}
			if (!((controlesView.getDLimiteRangoBajo() <= controlesView.getDRangoBajo())
					&& (controlesView.getDRangoBajo() <= controlesView.getDRangoAlto())
					&& (controlesView.getDRangoAlto() <= controlesView.getDLimiteRangoAlto()))) {
				log.error(ExceptionServiceCode.CONTROLES
						+ " validaciones() - Los rangos de los valores son acendetes limiteBajo < rangoBajo < rangoAlto < limiteAlto");
				throw new ControlesException(HttpStatus.BAD_REQUEST, ControlesException.RANGO_VALIDACION);
			}
		} else {
			controlesView.setDLimiteRangoBajo(null);
			controlesView.setDRangoBajo(null);
			controlesView.setDRangoAlto(null);
			controlesView.setDLimiteRangoAlto(null);
		}
		// oximetria
		if (controlesView.getOximetria()) {
			//Frecuencia de pulso
			if (controlesView.getOxPrLimiteRangoBajo() == null || controlesView.getOxPrRangoBajo() == null
					|| controlesView.getOxPrRangoAlto() == null || controlesView.getOxPrLimiteRangoAlto() == null) {
				log.error(ExceptionServiceCode.CONTROLES
						+ " validaciones() - Los campos oxPrLimiteRangoBajo, oxPrRangoBajo, oxPrRangoAlto, oxPrLimiteRangoAlto no pueden ser NULL ó Vacios");
				throw new ControlesException(HttpStatus.BAD_REQUEST, String.format(ControlesException.NO_NULOS,
						"oxPrLimiteRangoBajo, oxPrRangoBajo, oxPrRangoAlto, oxPrLimiteRangoAlto"));
			}
			//saturacion de oxigeno
			if (controlesView.getOxSpo2LimiteRangoBajo() == null || controlesView.getOxSpo2RangoBajo() == null
					|| controlesView.getOxSpo2RangoAlto() == null || controlesView.getOxSpo2LimiteRangoAlto() == null) {
				log.error(ExceptionServiceCode.CONTROLES
						+ " validaciones() - Los campos oxSpo2LimiteRangoBajo, oxSpo2RangoBajo, oxSpo2RangoAlto, oxSpo2LimiteRangoAlto no pueden ser NULL ó Vacios");
				throw new ControlesException(HttpStatus.BAD_REQUEST, String.format(ControlesException.NO_NULOS,
						"oxSpo2LimiteRangoBajo, oxSpo2RangoBajo, oxSpo2RangoAlto, oxSpo2LimiteRangoAlto"));
			}
			//Frecuencia de pulso
			if (!((controlesView.getOxPrLimiteRangoBajo() <= controlesView.getOxPrRangoBajo())
					&& (controlesView.getOxPrRangoBajo() <= controlesView.getOxPrRangoAlto())
					&& (controlesView.getOxPrRangoAlto() <= controlesView.getOxPrLimiteRangoAlto()))) {
				log.error(ExceptionServiceCode.CONTROLES
						+ " validaciones() - Los rangos de los valores son acendetes limiteBajo < rangoBajo < rangoAlto < limiteAlto");
				throw new ControlesException(HttpStatus.BAD_REQUEST, ControlesException.RANGO_VALIDACION);
			}
			//saturacion de oxigeno
			if (!((controlesView.getOxSpo2LimiteRangoBajo() <= controlesView.getOxSpo2RangoBajo())
					&& (controlesView.getOxSpo2RangoBajo() <= controlesView.getOxSpo2RangoAlto())
					&& (controlesView.getOxSpo2RangoAlto() <= controlesView.getOxSpo2LimiteRangoAlto()))) {
				log.error(ExceptionServiceCode.CONTROLES
						+ " validaciones() - Los rangos de los valores son acendetes limiteBajo < rangoBajo < rangoAlto < limiteAlto");
				throw new ControlesException(HttpStatus.BAD_REQUEST, ControlesException.RANGO_VALIDACION);
			}
		} else {
			//Frecuencia de pulso
			controlesView.setOxPrLimiteRangoBajo(null);
			controlesView.setOxPrRangoBajo(null);
			controlesView.setOxPrRangoAlto(null);
			controlesView.setOxPrLimiteRangoAlto(null);
			//saturacion de oxigeno
			controlesView.setOxSpo2LimiteRangoBajo(null);
			controlesView.setOxSpo2RangoBajo(null);
			controlesView.setOxSpo2RangoAlto(null);
			controlesView.setOxSpo2LimiteRangoAlto(null);
		}

		// hipertension
		if (controlesView.getHipertension()) {
			// pulso
			if (controlesView.getHpPLimiteRangoBajo() == null || controlesView.getHpPRangoBajo() == null
					|| controlesView.getHpPRangoAlto() == null || controlesView.getHpPLimiteRangoAlto() == null) {
				log.error(ExceptionServiceCode.CONTROLES
						+ " validaciones() - Los campos hpPLimiteRangoBajo, hpPRangoBajo, hpPRangoAlto, hpPLimiteRangoAlto no pueden ser NULL ó Vacios");
				throw new ControlesException(HttpStatus.BAD_REQUEST, String.format(ControlesException.NO_NULOS,
						"hpPLimiteRangoBajo, hpPRangoBajo, hpPRangoAlto, hpPLimiteRangoAlto"));
			}
			if (!((controlesView.getHpPLimiteRangoBajo() <= controlesView.getHpPRangoBajo())
					&& (controlesView.getHpPRangoBajo() <= controlesView.getHpPRangoAlto())
					&& (controlesView.getHpPRangoAlto() <= controlesView.getHpPLimiteRangoAlto()))) {
				log.error(ExceptionServiceCode.CONTROLES
						+ " validaciones() - Los rangos de los valores son acendetes limiteBajo < rangoBajo < rangoAlto < limiteAlto");
				throw new ControlesException(HttpStatus.BAD_REQUEST, ControlesException.RANGO_VALIDACION);
			}
			// diastolica
			if (controlesView.getHpDLimiteRangoBajo() == null || controlesView.getHpDRangoBajo() == null
					|| controlesView.getHpDRangoAlto() == null || controlesView.getHpDLimiteRangoAlto() == null) {
				log.error(ExceptionServiceCode.CONTROLES
						+ " validaciones() - Los campos hpPLimiteRangoBajo, hpPRangoBajo, hpPRangoAlto, hpPLimiteRangoAlto no pueden ser NULL ó Vacios");
				throw new ControlesException(HttpStatus.BAD_REQUEST, String.format(ControlesException.NO_NULOS,
						"hpDLimiteRangoBajo, hpDRangoBajo, hpDRangoAlto hpDLimiteRangoAlto"));
			}
			if (!((controlesView.getHpDLimiteRangoBajo() <= controlesView.getHpDRangoBajo())
					&& (controlesView.getHpDRangoBajo() <= controlesView.getHpDRangoAlto())
					&& (controlesView.getHpDRangoAlto() <= controlesView.getHpDLimiteRangoAlto()))) {
				log.error(ExceptionServiceCode.CONTROLES
						+ " validaciones() - Los rangos de los valores son acendetes limiteBajo < rangoBajo < rangoAlto < limiteAlto");
				throw new ControlesException(HttpStatus.BAD_REQUEST, ControlesException.RANGO_VALIDACION);
			}
			// sitolica
			if (controlesView.getHpSLimiteRangoBajo() == null || controlesView.getHpSRangoBajo() == null
					|| controlesView.getHpSRangoAlto() == null || controlesView.getHpSLimiteRangoAlto() == null) {
				log.error(ExceptionServiceCode.CONTROLES
						+ " validaciones() - Los campos hpDLimiteRangoBajo, hpDRangoBajo, hpDRangoAlto, hpDLimiteRangoAlto no pueden ser NULL ó Vacios");
				throw new ControlesException(HttpStatus.BAD_REQUEST, String.format(ControlesException.NO_NULOS,
						"hpDLimiteRangoBajo, hpDRangoBajo, hpDRangoAlto hpDLimiteRangoAlto"));
			}
			if (!((controlesView.getHpSLimiteRangoBajo() <= controlesView.getHpSRangoBajo())
					&& (controlesView.getHpSRangoBajo() <= controlesView.getHpSRangoAlto())
					&& (controlesView.getHpSRangoAlto() <= controlesView.getHpSLimiteRangoAlto()))) {
				log.error(ExceptionServiceCode.CONTROLES
						+ " validaciones() - Los rangos de los valores son acendetes limiteBajo < rangoBajo < rangoAlto < limiteAlto");
				throw new ControlesException(HttpStatus.BAD_REQUEST, ControlesException.RANGO_VALIDACION);
			}
		} else {
			controlesView.setHpPLimiteRangoBajo(null);
			controlesView.setHpPRangoBajo(null);
			controlesView.setHpPRangoAlto(null);
			controlesView.setHpPLimiteRangoAlto(null);

			controlesView.setHpDLimiteRangoBajo(null);
			controlesView.setHpDRangoBajo(null);
			controlesView.setHpDRangoAlto(null);
			controlesView.setHpDLimiteRangoAlto(null);

			controlesView.setHpSLimiteRangoBajo(null);
			controlesView.setHpSRangoBajo(null);
			controlesView.setHpSRangoAlto(null);
			controlesView.setHpSLimiteRangoAlto(null);
		}
		if (Update) {
			if (controlesView.getNombreQuienModifica() == null || controlesView.getNombreQuienModifica().isEmpty()) {
				log.error(ExceptionServiceCode.CONTROLES
						+ " validaciones() - EL campo NombreQuienModifica no pueden ser NULL ó Vacios");
				throw new ControlesException(HttpStatus.BAD_REQUEST,
						String.format(ControlesException.NO_NULO, "NombreQuienModifica"));
			}
			if (controlesView.getIdUsuarioQuienModificia() == null) {
				log.error(ExceptionServiceCode.CONTROLES
						+ " validaciones() - EL campo IdUsuarioQuienModificia no pueden ser NULL ó Vacios");
				throw new ControlesException(HttpStatus.BAD_REQUEST,
						String.format(ControlesException.NO_NULO, "IdUsuarioQuienModificia"));
			}
		} else {
			controlesView.setNombreQuienModifica(null);
			controlesView.setIdUsuarioQuienModificia(null);
			if (controlesView.getNombreQuienCreo() == null || controlesView.getNombreQuienCreo().isEmpty()) {
				log.error(ExceptionServiceCode.CONTROLES
						+ " validaciones() - EL campo NombreQuienCreo no pueden ser NULL ó Vacios");
				throw new ControlesException(HttpStatus.BAD_REQUEST,
						String.format(ControlesException.NO_NULO, "NombreQuienCreo"));
			}
			if (controlesView.getIdUsuarioQuienCreo() == null) {
				log.error(ExceptionServiceCode.CONTROLES
						+ " validaciones() - EL campo IdUsuarioQuienCreo no pueden ser NULL ó Vacios");
				throw new ControlesException(HttpStatus.BAD_REQUEST,
						String.format(ControlesException.NO_NULO, "IdUsuarioQuienCreo"));
			}
		}
	}

	

}
