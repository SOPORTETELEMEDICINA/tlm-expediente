package net.amentum.niomedic.expediente.rest;

import net.amentum.common.BaseController;
import net.amentum.niomedic.catalogos.views.CatEstadoConsultaView;
import net.amentum.niomedic.expediente.exception.ConsultaException;
import net.amentum.niomedic.expediente.service.ConsultaService;
import net.amentum.niomedic.expediente.views.ConsultaView;
import net.amentum.niomedic.expediente.views.SignosVitalesView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("consulta")
public class ConsultaRest extends BaseController {
	private final Logger logger = LoggerFactory.getLogger(ConsultaRest.class);
	private ConsultaService consultaService;

	@Autowired
	public void setConsultaService(ConsultaService consultaService) {
		this.consultaService = consultaService;
	}

	/*@RequestMapping(method = RequestMethod.POST)
   @ResponseStatus(HttpStatus.CREATED)
   public void createdConsulta(@RequestBody @Valid ConsultaView consultaView) throws ConsultaException {
      try {
    	  logger.info("Guardar nuevo Consulta: {}", consultaView);
         consultaService.createConsulta(consultaView);
      } catch (ConsultaException cee) {
         throw cee;
      } catch (Exception ex) {
         ConsultaException cee = new ConsultaException("No fue posible agregar Consulta", ConsultaException.LAYER_DAO, ConsultaException.ACTION_INSERT);
         cee.addError("Ocurrio un error al agregar Consulta");
         logger.error("Error al insertar nuevo Consulta - CODE {} - {}", cee.getExceptionCode(), cee);
         throw cee;
      }
   }

//   @RequestMapping(value = "{idPaciente}", method = RequestMethod.PUT)
   @RequestMapping(value = "{numeroConsulta}", method = RequestMethod.PUT)
   @ResponseStatus(HttpStatus.OK)
//   public void updateConsulta(@PathVariable() String idPaciente, @RequestBody @Valid ConsultaView consultaView) throws ConsultaException {
   public void updateConsulta(@PathVariable() Long numeroConsulta, @RequestBody @Valid ConsultaView consultaView) throws ConsultaException {
      try {
//         consultaView.setIdPaciente(idPaciente);
         //consultaView.setNumeroConsulta(numeroConsulta);
         //logger.info("Editar Consulta: {}", consultaView);
         consultaService.updateConsulta(consultaView);
      } catch (ConsultaException ee) {
         throw ee;
      } catch (Exception ex) {
         ConsultaException ee = new ConsultaException("No fue posible modificar Consulta", ConsultaException.LAYER_DAO, ConsultaException.ACTION_UPDATE);
         ee.addError("Ocurrio un error al modificar Consulta");
         logger.error("Error al modificar Consulta - CODE {} - {}", ee.getExceptionCode(), ee);
         throw ee;
      }
   }

//   @RequestMapping(value = "{idPaciente}", method = RequestMethod.GET)
   @RequestMapping(value = "{numeroConsulta}", method = RequestMethod.GET)
   @ResponseStatus(HttpStatus.OK)
//   public ConsultaView getDetailsByIdConsulta(@PathVariable() String idPaciente) throws ConsultaException {
   public ConsultaView getDetailsByNumeroConsulta(@PathVariable() Long  numeroConsulta) throws ConsultaException {
      try {
         logger.info("Obtener detalles Consulta por Id: {}", numeroConsulta);
         return consultaService.getDetailsByNumeroConsulta(numeroConsulta);
      } catch (ConsultaException ee) {
         throw ee;
      } catch (Exception ex) {
         ConsultaException ee = new ConsultaException("No fue posible obtener los detalles Consulta por Id", ConsultaException.LAYER_DAO, ConsultaException.ACTION_SELECT);
         ee.addError("Ocurrio un error al obtener los detalles Consulta por Id");
         logger.error("Error al obtener los detalles Consulta por Id - CODE {} - {} ", ee.getExceptionCode(), ee);
         throw ee;
      }
   }

   @RequestMapping(value = "por-padecimiento/{idPadecimiento}", method = RequestMethod.GET)
   @ResponseStatus(HttpStatus.OK)
   public List<ConsultaView> getAllByIdPadecimiento(@PathVariable() Long idPadecimiento) throws ConsultaException {
      return consultaService.getAllByPadecimiento(idPadecimiento);
   }


   @RequestMapping(value = "findAll", method = RequestMethod.GET)
   @ResponseStatus(HttpStatus.OK)
   public List<ConsultaView> findAll() throws ConsultaException {
      return consultaService.findAll();
   }*/

	@RequestMapping(value = "page", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public Page<ConsultaView> getConsultaPage(@RequestParam(required = false, defaultValue = "") String idPaciente,
			@RequestParam(required = false) List<Long> idUsuario,
			@RequestParam(required = false) String idMedico,
			@RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer size,
			@RequestParam(required = false) String orderColumn,
			@RequestParam(required = false) String orderType,
			@RequestParam(required = false) Long startDate,
			@RequestParam(required = false) Long endDate,
			@RequestParam(required = false) Integer idGroup,
		    @RequestParam(required = false) String name) throws ConsultaException {
		logger.info("- Obtener listado Consulta paginable: - idPaciente {} - idMedico {} - page {} - size: {} - orderColumn: {} - orderType: {} - startDate: {}  - endDate: {} - idGroup: {}, mane: {}",
				idPaciente, idMedico, page, size, orderColumn, orderType, startDate, endDate, idGroup, name);
		if (page == null)
			page = 0;
		if (size == null)
			size = 10;
		if (orderType == null || orderType.isEmpty()) {
			orderType = "asc";
		}
		UUID uidMedico =null;
		UUID uidPaciente= null;
		if(idPaciente!=null && !idPaciente.isEmpty()) {
			uidPaciente = UUID.fromString(idPaciente);
		}
		if(idMedico!=null && !idMedico.isEmpty()) {
			uidMedico = UUID.fromString(idMedico);
		}
		return consultaService.getConsultaPage(uidPaciente, idUsuario, uidMedico ,page, size, orderColumn, orderType, startDate, endDate, idGroup, name);
	}
	/*
   @RequestMapping(value = "{numeroConsulta}/estatus/{idEstatus}", method = RequestMethod.PUT)
   @ResponseStatus(HttpStatus.OK)
//   public void updateConsulta(@PathVariable() String idPaciente, @RequestBody @Valid ConsultaView consultaView) throws ConsultaException {
   public void updateEstatus(@PathVariable() Long numeroConsulta, @PathVariable() String idEstatus, @RequestBody @Valid ConsultaView consultaView) throws ConsultaException {
      try {
         //consultaView.setEstatusConsultaId(idEstatus);
         //consultaView.setNumeroConsulta(numeroConsulta);
         logger.info("Editar Consulta: {}", consultaView);
         consultaService.updateConsultaEstatus(consultaView);
      } catch (ConsultaException ee) {
         throw ee;
      } catch (Exception ex) {
         ConsultaException ee = new ConsultaException("No fue posible modificar Consulta", ConsultaException.LAYER_DAO, ConsultaException.ACTION_UPDATE);
         ee.addError("Ocurrio un error al modificar Consulta");
         logger.error("Error al modificar Consulta - CODE {} - {}", ee.getExceptionCode(), ee);
         throw ee;
      }
   }*/
	/************************Nuevos Rest Consulta**********************************/
	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public ConsultaView createdConsulta(@RequestBody @Valid ConsultaView consultaView) throws ConsultaException {
		try {
			logger.info("Guardar nuevo Consulta: {}", consultaView);
			return consultaService.createConsulta(consultaView);
		} catch (ConsultaException cee) {
			throw cee;
		} catch (Exception ex) {
			ConsultaException cee = new ConsultaException("No fue posible agregar Consulta", ConsultaException.LAYER_DAO, ConsultaException.ACTION_INSERT);
			cee.addError("Ocurrio un error al agregar Consulta");
			logger.error("Error al insertar nuevo Consulta - CODE {} - {}", cee.getExceptionCode(), cee);
			throw cee;
		}
	}
	@RequestMapping(method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	public void updateConsulta(@RequestBody @Valid ConsultaView consultaView)throws ConsultaException {
		try {
			logger.info("updateConsulta() - put - Actualizando consulta idconsulta: {}", consultaView.getIdConsulta());
			consultaService.updateConsulta(consultaView);
		} catch (ConsultaException cee) {
			throw cee;
		} catch (Exception ex) {
			ConsultaException cee = new ConsultaException("No fue posible agregar Consulta", ConsultaException.LAYER_DAO, ConsultaException.ACTION_INSERT);
			cee.addError("Ocurrio un error al Actualizar Consulta");
			logger.error("Error al Actualizar nuevo Consulta - CODE {} - {}", cee.getExceptionCode(), cee);
			throw cee;
		}
	}


    @RequestMapping(value = "search", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Page<ConsultaView> getConsultasearch(
            @RequestParam(required = false, defaultValue = "") String idPaciente,
            @RequestParam(required = false) List<Long> idUsuario,
            @RequestParam(required = false) String idMedico,
            @RequestParam(required = false) List<Integer> idEstadoConsulta,
            @RequestParam(required = false) Integer idTipoConsulta,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String orderColumn,
            @RequestParam(required = false) String orderType,
            @RequestParam(required = false) Long startDate,
            @RequestParam(required = false) Long endDate,
            @RequestParam(required = false) Integer idGroup // <-- NUEVO
    ) throws ConsultaException {
        logger.info("- Obtener listado Consulta (search): - idPaciente {} - idMedico {} - page {} - size: {} - orderColumn: {} - orderType: {} - startDate: {} - endDate: {} - idGroup: {}",
                idPaciente, idMedico, page, size, orderColumn, orderType, startDate, endDate, idGroup);

        if (page == null) page = 0;
        if (size == null) size = 10;
        if (orderType == null || orderType.isEmpty()) {
            orderType = "asc";
        }

        UUID uidMedico = null;
        UUID uidPaciente = null;
        if (idPaciente != null && !idPaciente.isEmpty()) {
            uidPaciente = UUID.fromString(idPaciente);
        }
        if (idMedico != null && !idMedico.isEmpty()) {
            uidMedico = UUID.fromString(idMedico);
        }

        // ahora pasamos idGroup para que filtre y regrese el mismo JSON que "page"
        return consultaService.getConsultaSearch(
                uidPaciente, idUsuario, uidMedico, idTipoConsulta, idEstadoConsulta,
                page, size, orderColumn, orderType, startDate, endDate, idGroup
        );
    }


    @RequestMapping(value = "{idConsulta}", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public ConsultaView getConsultaById(@PathVariable() Long  idConsulta) throws ConsultaException {
		try {
			logger.info("getConsultaById() - Obtener detalles Consulta - Id Consulta: {}", idConsulta);
			return consultaService.getConsultaById(idConsulta);
		} catch (ConsultaException ee) {
			throw ee;
		} catch (Exception ex) {
			ConsultaException ee = new ConsultaException("No fue posible obtener los detalles Consulta por Id", ConsultaException.LAYER_DAO, ConsultaException.ACTION_SELECT);
			ee.addError("Ocurrio un error al obtener los detalles Consulta por Id");
			logger.error("Error al obtener los detalles Consulta por Id - CODE {} - {} ", ee.getExceptionCode(), ee);
			throw ee;
		}
	}

	@RequestMapping(value="iniciar/{idConsulta}", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	public void consultaStart(@PathVariable() Long idConsulta, @RequestBody @Valid CatEstadoConsultaView catEstadoConsultaView)throws ConsultaException  {
		logger.info("put - consultaStart() - modificar estadoConsulta - idConsulta: {}", idConsulta);
		consultaService.consultaStart(idConsulta, catEstadoConsultaView);
	}

	@RequestMapping(value="cancelar/{idConsulta}", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	public void consultaCancel(@PathVariable() Long idConsulta, @RequestBody @Valid CatEstadoConsultaView catEstadoConsultaView)throws ConsultaException  {
		logger.info("put - consultaCancel() - modificar estadoConsulta - idConsulta: {}", idConsulta);
		consultaService.consultaCancel(idConsulta, catEstadoConsultaView);
	}

	@RequestMapping(value="reagendar/{idConsulta}", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	public void consultareschedule(@PathVariable() Long idConsulta, 
			@RequestParam(required = true) Long fechaConsulta,@RequestParam(required = false) Long fechaConsultaFin,
			@RequestBody @Valid	CatEstadoConsultaView catEstadoConsultaView)throws ConsultaException  {
		Date newFecha= new Date(fechaConsulta);
		Date newFechaFin= null;
		if(fechaConsultaFin!=null) {
			newFechaFin= new Date(fechaConsultaFin);
		}
		logger.info("put - consultareschedule() - Reagendadon consulta - fechaConsulta:{} - fechaConsultaFin:{} - idConsulta: {}", newFecha, idConsulta, newFechaFin);
		consultaService.consultreschedule(idConsulta, newFecha,newFechaFin, catEstadoConsultaView);
	}

	@RequestMapping(value="terminar/{idConsulta}", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)   
	public void consultaFinish(@PathVariable() Long idConsulta, 
			@RequestBody @Valid ConsultaView consultaView)throws ConsultaException  {
		logger.info("put - consultaFinish() - modificar estadoConsulta - idConsulta: {} ", idConsulta);
		consultaService.consultaFinish(idConsulta, consultaView);
	}

	@RequestMapping(value="{idConsulta}/signosVitales", method= RequestMethod.PUT)
	@ResponseStatus(HttpStatus.CREATED)
	public void signosVitales(@PathVariable("idConsulta") Long idConsulta,@RequestBody @Valid SignosVitalesView signosVitales) throws ConsultaException {
		logger.info("put - signosVitales() - Guardando signos vitales - idConsulta: {} - SignosVitales:{}", idConsulta,signosVitales);
		consultaService.signosVitales(idConsulta, signosVitales);
	}

    @RequestMapping(value = "ultima/{idPaciente}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public ConsultaView ultimaConsulta(@PathVariable("idPaciente") String idPaciente,
                                       @RequestParam("idGroup") Integer idGroup) throws ConsultaException {
        UUID uidPaciente = null;
        if (idPaciente != null && !idPaciente.isEmpty()) {
            uidPaciente = UUID.fromString(idPaciente);
        }
        logger.info("get - ultimaConsulta() [penúltima en realidad] - idPaciente:{} - idGroup:{}", idPaciente, idGroup);

        // Ahora devolvemos la PENÚLTIMA
        return consultaService.getPenultimaConsulta(uidPaciente, idGroup);
    }


    @RequestMapping(value="confirmar/{idConsulta}", method=RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	public void confirmarConsulta(@PathVariable("idConsulta")Long idConsulta,@RequestBody @Valid CatEstadoConsultaView catEstadoConsultaView) throws ConsultaException {
		logger.info("put - confirmarConsulta() - Cambiando la consulta a estado Confirmado, idConsulta:{}",idConsulta);
		consultaService.confirmarConsulta(idConsulta, catEstadoConsultaView);
	}
	@RequestMapping(value="siguiente/{idPaciente}", method=RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public ConsultaView siguienteConsulta(@PathVariable() String idPaciente) throws ConsultaException {
		UUID uidPaciente= null;
		if(idPaciente!=null && !idPaciente.isEmpty()) {
			uidPaciente = UUID.fromString(idPaciente);
		}
		logger.info("get - siguienteConsulta() - Obteniendo Siguiente consulta para el idPaciente:{}",idPaciente);
		return consultaService.getSiguienteConsulta(uidPaciente);
	}
	@RequestMapping(value="enfermeria/{idConsulta}", method=RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	public void enfermeriaConsulta(@PathVariable() Long idConsulta, @RequestBody @Valid CatEstadoConsultaView catEstadoConsultaView) throws ConsultaException {
		logger.info("put - enfermeriaConsulta() - Cambiando el estado de la consulta con el id:{}",idConsulta);
		consultaService.enfermeriaConsulta(idConsulta,catEstadoConsultaView);
	}

	@RequestMapping(value="CDA/{idConsulta}", method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public void createCDA(@PathVariable() Long idConsulta) throws ConsultaException {
		logger.info("post - creado CDA para la consulta con el id:{}",idConsulta);
		consultaService.createCDA(idConsulta);
	}

	@RequestMapping(value="{idConsulta}/urlImagen", method=RequestMethod.GET)
	@ResponseStatus(HttpStatus.CREATED)
	public String geturlImagen(@PathVariable() Long idConsulta) throws ConsultaException {
		logger.info("REST - GET - geturlImagen:{}",idConsulta);
		return  consultaService.getUrlImagen(idConsulta);
	}

	@RequestMapping(value = "findAll", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public Page<HashMap<String, Object>> getConsultaByEstatus(@RequestParam(required = false) String idMedico,
															  @RequestParam(required = false, defaultValue = "0") Integer page,
															  @RequestParam(required = false, defaultValue = "10") Integer size,
															  @RequestParam(required = false, defaultValue = "estadoConsulta") String orderColumn,
															  @RequestParam(required = false, defaultValue = "asc") String orderType,
															  @RequestParam() Integer idGroup) throws ConsultaException {
		try {
			logger.info("Consulta mediante estatus por idMedico {}", idMedico);
			if(page == null)
				page = 0;
			if(size == null)
				size = 10;
			if(orderType == null || orderType.isEmpty())
				orderType = "asc";
			if (orderColumn == null || orderColumn.isEmpty())
				orderColumn = "estadoConsulta";
			if(idMedico == null || idMedico.isEmpty())
				return consultaService.getConsultaByEstatus(page, size, orderColumn, orderType, idGroup);
			else
				return consultaService.getConsultaByEstatusMedico(UUID.fromString(idMedico), page, size, orderColumn, orderType, idGroup);
		} catch (ConsultaException ee) {
			throw ee;
		} catch (Exception ex) {
			ConsultaException ee = new ConsultaException("No fue posible obtener las consultas por estatus", ConsultaException.LAYER_SERVICE, ConsultaException.ACTION_SELECT);
			ee.addError("Ocurrió un error al obtener las consultas por estatus");
			logger.error("Error al obtener las consultas por estatus - CODE {} - {} ", ee.getExceptionCode(), ee);
			throw ee;
		}
	}

}