package net.amentum.niomedic.expediente.service.impl;

import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import net.amentum.niomedic.expediente.configuration.ApiConfiguration;
import net.amentum.niomedic.expediente.converter.ResultadoEstudioConverter;
import net.amentum.niomedic.expediente.exception.ConsultaException;
import net.amentum.niomedic.expediente.exception.ResultadoEstudioException;
import net.amentum.niomedic.expediente.model.ResultadoEstudio;
import net.amentum.niomedic.expediente.persistence.ConsultaRepository;
import net.amentum.niomedic.expediente.persistence.ResultadoEstudioRepository;
import net.amentum.niomedic.expediente.service.ResultadoEstudioService;
import net.amentum.niomedic.expediente.views.EstudiosYResultadosView;
import net.amentum.niomedic.expediente.views.FormatoEstudioView;
import net.amentum.niomedic.expediente.views.ResultadoEstudioView;
import net.amentum.niomedic.expediente.service.ReportesService;

@Service
@Transactional(readOnly=true)
@Slf4j
public class ResultadoEstudioServiceImpl implements ResultadoEstudioService {

//
//	private ResultadoEstudioRepository resultadoEstudioRepository;
//
//	private ResultadoEstudioConverter resultadoEstudioConverter;
//
//	private ConsultaRepository consultaRepository;
//
//	private ReportesService reportesService;
//
//	private ApiConfiguration apiConfiguration;
//
//	@Autowired
//	public void setReportesService(ReportesService reportesService) {
//		this.reportesService  = reportesService;
//	}
//	@Autowired
//	public void setResultadoEstudioRepository(ResultadoEstudioRepository resultadoEstudioRepository) {
//		this.resultadoEstudioRepository = resultadoEstudioRepository;
//	}
//	@Autowired
//	public void setResultadoEstudioConverter(ResultadoEstudioConverter resultadoEstudioConverter) {
//		this.resultadoEstudioConverter = resultadoEstudioConverter;
//	}
//	@Autowired
//	public void setConsultaRepository(ConsultaRepository consultaRepository) {
//		this.consultaRepository = consultaRepository;
//	}
//	@Autowired
//	public void setApiConfiguration(ApiConfiguration apiConfiguration) {
//		this.apiConfiguration = apiConfiguration;
//	}
//
//
//	@Transactional(readOnly=false, rollbackFor=ResultadoEstudioException.class)
//	@Override
//	public ResultadoEstudioView createResultadoEstudio(ResultadoEstudioView resultadoEstudioView)
//			throws ResultadoEstudioException {
//		try {
//			existeConsula(resultadoEstudioView.getIdConsulta());
//			ResultadoEstudio entity = resultadoEstudioConverter.toEntity(new ResultadoEstudio(), resultadoEstudioView);
//			log.info("createResultadoEstudio() -  Guardando resultado estudio - {}",entity);
//			resultadoEstudioRepository.save(entity);
//			log.info("createResultadoEstudio() - Se guardó  exitosamente");
//			return resultadoEstudioConverter.toView(entity);
//		}catch(ResultadoEstudioException ree) {
//			throw ree;
//		}
//		catch(ConstraintViolationException ce) {
//			final Set<ConstraintViolation<?>> violaciones = ce.getConstraintViolations();
//			String errores="";
//			for (Iterator<ConstraintViolation<?>> iterator = violaciones.iterator(); iterator.hasNext(); ) {
//				ConstraintViolation<?> siguiente = iterator.next();
//				errores = errores + siguiente.getPropertyPath()+": "+siguiente.getMessage()+"\n";
//			}
//			log.error("createResultadoEstudio() - Error de validacion al  crear un nuevo registros en resultado_estudio - error: {}", errores);
//			throw new ResultadoEstudioException(HttpStatus.BAD_REQUEST, errores);
//		}catch(DataIntegrityViolationException de) {
//			log.error("createResultadoEstudio() - Ocurrio un erro de Integridad en la DB al querer insertar una nueva resultado_estudio - error{}", de.getCause().getMessage());
//			throw new ResultadoEstudioException(HttpStatus.CONFLICT, de.getCause().getCause().getMessage());
//		}catch(Exception e) {
//			log.error("createResultadoEstudio() - Ocurrio un error inesperado al crear una nueva resultado_estudio - error: {}",e);
//			throw new ResultadoEstudioException(HttpStatus.INTERNAL_SERVER_ERROR,String.format(ResultadoEstudioException.SERVER_ERROR, "crear una nueva") );
//		}
//	}
//
//	private void existeConsula(Long idConsulta) throws ResultadoEstudioException{
//		log.info("existeConsula() - Buscando consulta con el id: {}",idConsulta);
//		if(!consultaRepository.exists(idConsulta)){
//			log.error("existeConsula() - No se encontro la consulta con el id: {}",idConsulta);
//			throw new ResultadoEstudioException(HttpStatus.NOT_FOUND, String.format(ResultadoEstudioException.CONSULTA_NOT_FOUNT, idConsulta));
//		}
//	}
//
//	@Override
//	public EstudiosYResultadosView getResultadoEstudio(Long idConsulta) throws ResultadoEstudioException {
//		try {
//			existeConsula(idConsulta);
//			EstudiosYResultadosView estudioYrespuestaView = new EstudiosYResultadosView();
//			List<ResultadoEstudio> resultadoList = resultadoEstudioRepository.findByConsultaIdConsulta(idConsulta);
//
//			List<ResultadoEstudioView> resultadosView = new ArrayList<ResultadoEstudioView>();
//			for(ResultadoEstudio resultado : resultadoList) {
//				resultadosView.add(resultadoEstudioConverter.toView(resultado));
//			}
//			estudioYrespuestaView.setResultadoEstudioViewList(resultadosView);
//
//			String base64="";
//			try {
//				/*
//				 * #apiConfiguration.getEstudioByIdConsulta(idConsulta);
//				 * sino encuentra registros de esta consulta manda exception
//				 * esto hace que FormatoEstudioView vaya null
//				 * y no mande un base64 sin informacion
//				 */
//				apiConfiguration.getEstudioByIdConsulta(idConsulta);
//
//				FormatoEstudioView fev = new FormatoEstudioView();
//				fev.setIdConsulta(idConsulta);
//				fev.setMimeType("application/pdf");
////				base64 = reportesService.getSolicitudEstudios(idConsulta);
//				fev.setBase64(base64);
//				estudioYrespuestaView.setFormatoEstudioView(fev);
//			}catch (ConsultaException e) {
//				log.info("Error al crear el formato de prescripción - error:{}",e);
//			}
//			return estudioYrespuestaView;
//
//		}catch(ResultadoEstudioException ree) {
//			throw ree;
//		}catch(Exception e) {
//			log.error("createResultadoEstudio() - Ocurrio un error inesperado al Obtener los resultados de estudios y la prescripción - error: {}",e);
//			throw new ResultadoEstudioException(HttpStatus.INTERNAL_SERVER_ERROR,"No se pudo efectuar la operación obtener para resultados de estudios y la prescripción");
//		}
//	}
}
