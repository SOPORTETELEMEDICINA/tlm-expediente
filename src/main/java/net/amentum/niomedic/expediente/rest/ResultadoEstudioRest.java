package net.amentum.niomedic.expediente.rest;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import net.amentum.common.v2.RestBaseController;
import net.amentum.niomedic.expediente.exception.ResultadoEstudioException;
import net.amentum.niomedic.expediente.service.ResultadoEstudioService;
import net.amentum.niomedic.expediente.views.EstudiosYResultadosView;
import net.amentum.niomedic.expediente.views.ResultadoEstudioView;

@RestController
@RequestMapping("resultado-estudio")
@Slf4j
public class ResultadoEstudioRest extends RestBaseController {
	
//	@Autowired
//	private ResultadoEstudioService resultadoEstudioService;
//
//	@RequestMapping(method = RequestMethod.POST)
//	@ResponseStatus(HttpStatus.CREATED)
//	public ResultadoEstudioView createResultadoEstudio(@RequestBody @Valid ResultadoEstudioView resultadoEstudioView) throws ResultadoEstudioException {
//		log.info("POST - createResultadoEstudio() - Agregando un nuevo resultado de estudios: {}",resultadoEstudioView);
//		return resultadoEstudioService.createResultadoEstudio(resultadoEstudioView);
//	}
//
//
//	@RequestMapping(value="por-consulta/{idConsulta}", method = RequestMethod.GET)
//	@ResponseStatus(HttpStatus.OK)
//	public EstudiosYResultadosView getResultadoEstudioPorConsulta(@PathVariable Long idConsulta) throws ResultadoEstudioException {
//		log.info("GET - getResultadoEstudioPorConsulta() - Obteniendo prescripciones y resultado de estudios por consultaId: {}",idConsulta);
//		return resultadoEstudioService.getResultadoEstudio(idConsulta);
//	}
}
