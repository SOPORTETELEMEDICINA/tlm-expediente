package net.amentum.niomedic.expediente.rest;

import net.amentum.common.BaseController;
import net.amentum.niomedic.expediente.exception.EstadoSaludException;
import net.amentum.niomedic.expediente.service.EstadoSaludService;
import net.amentum.niomedic.expediente.views.EstadoSaludView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("estado-salud")
public class EstadoSaludRest extends BaseController {
   private final Logger logger = LoggerFactory.getLogger(EstadoSaludRest.class);

   private EstadoSaludService estadoSaludService;

   @Autowired
   public void setEstadoSaludService(EstadoSaludService estadoSaludService) {
      this.estadoSaludService = estadoSaludService;
   }

   @RequestMapping(method = RequestMethod.POST)
   @ResponseStatus(HttpStatus.CREATED)
   public EstadoSaludView createEstadoSalud(@RequestBody @Validated EstadoSaludView estadoSaludView) throws EstadoSaludException {
      try {
         logger.info("===>>>Guardar nuevo EstadoSalud: {}", estadoSaludView);
         return estadoSaludService.createEstadoSalud(estadoSaludView);
      } catch (EstadoSaludException edoSalE) {
         throw edoSalE;
      } catch (Exception ex) {
         EstadoSaludException edoSalE = new EstadoSaludException("No fue posible insertar  EstadoSalud", EstadoSaludException.LAYER_REST, EstadoSaludException.ACTION_INSERT);
         logger.error("===>>>Error al insertar  EstadoSalud- CODE: {} - ", edoSalE.getExceptionCode(), ex);
         throw edoSalE;
      }
   }

   @RequestMapping(value = "{idEstadoSalud}", method = RequestMethod.PUT)
   @ResponseStatus(HttpStatus.OK)
   public EstadoSaludView updateEstadoSalud(@PathVariable() Long idEstadoSalud, @RequestBody @Validated EstadoSaludView estadoSaludView) throws EstadoSaludException {
      try {
         estadoSaludView.setIdEstadoSalud(idEstadoSalud);
         logger.info("===>>>Editar estadoSalud: {}", estadoSaludView);
         return estadoSaludService.updateEstadoSalud(estadoSaludView);
      } catch (EstadoSaludException edoSalE) {
         throw edoSalE;
      } catch (Exception ex) {
         EstadoSaludException edoSalE = new EstadoSaludException("No fue posible modificar estadoSalud", EstadoSaludException.LAYER_REST, EstadoSaludException.ACTION_UPDATE);
         logger.error("===>>>Error al modificar estadoSalud- CODE: {} - ", edoSalE.getExceptionCode(), ex);
         throw edoSalE;
      }
   }

   @RequestMapping(value = "page", method = RequestMethod.GET)
   @ResponseStatus(HttpStatus.OK)
   public Page<EstadoSaludView> getEstadoSaludPage(@RequestParam(required = true) String idPaciente,
                                                   @RequestParam(required = false) Long startDate,
                                                   @RequestParam(required = false) Long endDate,
                                                   @RequestParam(required = false) Integer page,
                                                   @RequestParam(required = false) Integer size,
                                                   @RequestParam(required = false) String orderColumn,
                                                   @RequestParam(required = false) String orderType) throws EstadoSaludException {

      logger.info("===>>>getEstadoSaludSearch(): - idPaciente: {} - idTipoEvento: {} - startDate: {} - endDate: {} - page: {} - size: {} - orderColumn: {} - orderType: {}",
         idPaciente, startDate, endDate, page, size, orderColumn, orderType);

      UUID uidPaciente = null;
      try {
         if (idPaciente != null && !idPaciente.isEmpty()) {
            uidPaciente = UUID.fromString(idPaciente);
         }
      } catch (IllegalArgumentException iae) {
         logger.error("===>>>idPaciente tiene valores incorrectos", idPaciente);
         EstadoSaludException edoSalE = new EstadoSaludException("Ocurrio un error al obtener EstadoSalud", EstadoSaludException.LAYER_REST, EstadoSaludException.ACTION_VALIDATE);
         edoSalE.addError("idPaciente tiene valores incorrectos: " + idPaciente);
         throw edoSalE;
      }

      if (page == null)
         page = 0;
      if (size == null)
         size = 10;
      if (orderType == null || orderType.isEmpty())
         orderType = "asc";
      if (orderColumn == null || orderColumn.isEmpty())
         orderColumn = "fechaCreacion";

      return estadoSaludService.getEstadoSaludPage(uidPaciente, startDate, endDate, page, size, orderColumn, orderType);
   }


   @RequestMapping(value = "ultimo/{idPaciente}", method = RequestMethod.GET)
   @ResponseStatus(HttpStatus.OK)
   public EstadoSaludView getLastEstadoSalud(@PathVariable() String idPaciente) throws EstadoSaludException {
      try {
         UUID uidPaciente = null;
         try {
            if (idPaciente != null && !idPaciente.isEmpty()) {
               uidPaciente = UUID.fromString(idPaciente);
            }
         } catch (IllegalArgumentException iae) {
            logger.error("===>>>idPaciente tiene valores incorrectos", idPaciente);
            EstadoSaludException edoSalE = new EstadoSaludException("Ocurrio un error al obtener EstadoSalud", EstadoSaludException.LAYER_REST, EstadoSaludException.ACTION_VALIDATE);
            edoSalE.addError("idPaciente tiene valores incorrectos: " + idPaciente);
            throw edoSalE;
         }
         logger.info("===>>>Obtener los detalles del estadoSalud por IdPaciente: {}", idPaciente);
         return estadoSaludService.getLastEstadoSalud(uidPaciente);
      } catch (EstadoSaludException edoSalE) {
         throw edoSalE;
      } catch (Exception ex) {
         EstadoSaludException edoSalE = new EstadoSaludException("No fue posible obtener los detalles del estadoSalud por Id", EstadoSaludException.LAYER_REST, EstadoSaludException.ACTION_SELECT);
         logger.error("===>>>Error al obtener los detalles del estadoSalud por Id- CODE: {} - ", edoSalE.getExceptionCode(), ex);
         throw edoSalE;
      }
   }


}
