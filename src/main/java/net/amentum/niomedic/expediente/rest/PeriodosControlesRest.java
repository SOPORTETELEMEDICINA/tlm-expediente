package net.amentum.niomedic.expediente.rest;

import java.util.Set;
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
import net.amentum.common.v2.RestBaseController;
import net.amentum.niomedic.expediente.exception.PeriodosControlesException;
import net.amentum.niomedic.expediente.service.PeriodosControlesService;
import net.amentum.niomedic.expediente.views.PeriodosControlesView;

@RestController
@RequestMapping("controles")
@Slf4j
public class PeriodosControlesRest extends RestBaseController{
	@Autowired
	PeriodosControlesService periodosControlesService;

	@RequestMapping(value="{idControl}/periodos",method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public Set<PeriodosControlesView> createPeriodosControles(@PathVariable()Long idControl,@RequestBody @Valid Set<PeriodosControlesView>periodosControlesView) throws PeriodosControlesException {
		log.info("createPeriodosControles() - POST - Creando Periodos para el control:{}",idControl);
		return periodosControlesService.createPeriodosControles(idControl, periodosControlesView);
	}

	@RequestMapping(value="{idControl}/periodos",method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.CREATED)
	public Set<PeriodosControlesView> updatePeriodosControles(@PathVariable()Long idControl,@RequestBody @Valid Set<PeriodosControlesView>periodosControlesView) throws PeriodosControlesException {
		log.info("updatePeriodosControles() - PUT - Actualizando Periodos para el control:{}",idControl);
		return periodosControlesService.updatePeriodosControles(idControl, periodosControlesView);
	}

	@RequestMapping(value="{idControl}/periodos",method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public Set<PeriodosControlesView> getPeriodosControles(@PathVariable()Long idControl) throws PeriodosControlesException {
		log.info("getPeriodosControles() - GET - Obteniendo Periodos para el control:{}",idControl);
		return periodosControlesService.getPeriodosControles(idControl);
	}


	@RequestMapping(value="{idControl}/periodos",method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public void deletePeriodosControles(@PathVariable()Long idControl) throws PeriodosControlesException {
		log.info("deletePeriodosControles() - DELETE - ELiminando Periodos para el control:{}",idControl);
		periodosControlesService.deletePeriodosControles(idControl);
	}

	@RequestMapping(value="/periodos/{idPaciente}", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public Set<PeriodosControlesView> getPeriodosPorPaciente(@PathVariable()String idPaciente) throws PeriodosControlesException {
		log.info("getPeriodosPorPaciente() - GET - Obteniendo Periodos por idPaciente:{}",idPaciente);
		UUID uuidPaciente=null;
		try {
			uuidPaciente=UUID.fromString(idPaciente);
		}catch (Exception e) {
			log.error("getPeriodosPorPaciente() - idPaciente debe ser un tipo de dato UUID");
			throw new PeriodosControlesException(HttpStatus.BAD_REQUEST, "idPaciente debe ser un tipo de dato UUID");
		}
		return periodosControlesService.getPeriodosPorPaciente(uuidPaciente);
	}

}
