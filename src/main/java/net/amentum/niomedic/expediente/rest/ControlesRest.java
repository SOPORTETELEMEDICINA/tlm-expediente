package net.amentum.niomedic.expediente.rest;

import java.util.UUID;

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
import net.amentum.common.v2.GenericException;
import net.amentum.common.v2.RestBaseController;
import net.amentum.niomedic.expediente.exception.ControlesException;
import net.amentum.niomedic.expediente.service.ControlesService;
import net.amentum.niomedic.expediente.views.ControlesView;

@RestController
@RequestMapping("controles")
@Slf4j
public class ControlesRest extends RestBaseController{
	@Autowired
	ControlesService controlesService;
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public ControlesView createControles(@RequestBody @Valid ControlesView controlesView) throws ControlesException {
		log.info("createControles() - POST - Creando Controles idPacinete:{}",controlesView.getIdPaciente());
		return controlesService.createControles(controlesView);
		
	}
	@RequestMapping(value="{idPaciente}",method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	public ControlesView updateControles(@PathVariable() String idPaciente, @RequestBody @Valid ControlesView controlesView) throws ControlesException {
		log.info("updateControles() - PUT - Actualizando Controles idPacinete:{}",controlesView.getIdPaciente());
		controlesView.setIdControl(10L);
		UUID uuidPaciente=null;
		try {
			uuidPaciente=UUID.fromString(idPaciente);
		}catch (Exception e) {
			log.error("updateControles() - idPaciente debe ser un tipo de dato UUID");
			throw new ControlesException(HttpStatus.BAD_REQUEST, "idPaciente debe ser un tipo de dato UUID");
		}
		controlesView.setIdPaciente(uuidPaciente);
		return controlesService.updateControles(controlesView);	
	}

	@RequestMapping(value="{idPaciente}",method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public ControlesView getControles(@PathVariable() String idPaciente) throws GenericException {
		log.info("getControles() - GET - Obteniendo Controles por idPacinete:{}",idPaciente);
		UUID uuidPaciente=null;
		try {
			uuidPaciente=UUID.fromString(idPaciente);
		}catch (Exception e) {
			log.error("getControles() - idPaciente debe ser un tipo de dato UUID");
			throw new ControlesException(HttpStatus.BAD_REQUEST, "idPaciente debe ser un tipo de dato UUID");	
		}
		return controlesService.getControles(uuidPaciente);	
	}

	@RequestMapping(value="{idPaciente}",method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public void deleteControles(@PathVariable() String idPaciente) throws ControlesException {
		log.info("deleteControles() - DELETE - Eliminando Controles por idPacinete:{}",idPaciente);
		UUID uuidPaciente=null;
		try {
			uuidPaciente=UUID.fromString(idPaciente);
		}catch (Exception e) {
			log.error("deleteControles() - idPaciente debe ser un tipo de dato UUID");
			throw new ControlesException(HttpStatus.BAD_REQUEST, "idPaciente debe ser un tipo de dato UUID");	
		}
		controlesService.deleteControles(uuidPaciente);	
	}
	
	
}
