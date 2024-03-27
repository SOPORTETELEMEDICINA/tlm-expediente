package net.amentum.niomedic.expediente.rest;

import net.amentum.common.BaseController;
import net.amentum.niomedic.expediente.exception.EventosException;
import net.amentum.niomedic.expediente.service.EventosService;
import net.amentum.niomedic.expediente.views.EventosView;
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

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("eventos")
public class EventosRest extends BaseController {
   private final Logger logger = LoggerFactory.getLogger(EventosRest.class);

   private EventosService eventosService;

   @Autowired
   public void setEventosService(EventosService eventosService) {
      this.eventosService = eventosService;
   }

   @RequestMapping(method = RequestMethod.POST)
   @ResponseStatus(HttpStatus.CREATED)
   public EventosView createEventos(@RequestBody @Validated EventosView eventosView) throws EventosException {
      try {
         logger.info("===>>>Guardar nuevo Eventos: {}", eventosView);
         return eventosService.createEventos(eventosView);
      } catch (EventosException agE) {
         throw agE;
      } catch (Exception ex) {
         EventosException agE = new EventosException("No fue posible insertar  Eventos", EventosException.LAYER_REST, EventosException.ACTION_INSERT);
         logger.error("===>>>Error al insertar  Eventos- CODE: {} - ", agE.getExceptionCode(), ex);
         throw agE;
      }
   }

   @RequestMapping(value = "/modificar/{idEventos}", method = RequestMethod.PUT)
   @ResponseStatus(HttpStatus.OK)
   public EventosView updateEventos(@PathVariable() Long idEventos, @RequestBody @Validated EventosView eventosView) throws EventosException {
      try {
         eventosView.setIdEventos(idEventos);
         logger.info("===>>>Editar eventos: {}", eventosView);
         return eventosService.updateEventos(eventosView);
      } catch (EventosException agE) {
         throw agE;
      } catch (Exception ex) {
         EventosException agE = new EventosException("No fue posible modificar eventos", EventosException.LAYER_REST, EventosException.ACTION_UPDATE);
         logger.error("===>>>Error al modificar eventos- CODE: {} - ", agE.getExceptionCode(), ex);
         throw agE;
      }
   }


   @RequestMapping(value = "/reagendar/{idEventos}", method = RequestMethod.PUT)
   @ResponseStatus(HttpStatus.OK)
//   public void rescheduleEventos(@PathVariable("idEventos") Long idEventos, @RequestParam(required = true) Long nuevaFecha) throws EventosException {
   public void rescheduleEventos(@PathVariable("idEventos") Long idEventos, @RequestParam(required = true) Long nuevaFecha, @RequestParam(required = true) Long nuevaFechaFin) throws EventosException {
      try {
         Date fecha = new Date(nuevaFecha);
         Date fechaFin = new Date(nuevaFechaFin);
         logger.info("===>>>Editar eventos: {}", idEventos);
//         eventosService.rescheduleEventos(idEventos, fecha);
         eventosService.rescheduleEventos(idEventos, fecha,fechaFin);
      } catch (EventosException agE) {
         throw agE;
      } catch (Exception ex) {
         EventosException agE = new EventosException("No fue posible modificar eventos", EventosException.LAYER_REST, EventosException.ACTION_UPDATE);
         logger.error("===>>>Error al modificar eventos- CODE: {} - ", agE.getExceptionCode(), ex);
         throw agE;
      }
   }

   @RequestMapping(value = "{idEventos}", method = RequestMethod.GET)
   @ResponseStatus(HttpStatus.OK)
   public EventosView getDetailsByIdEventos(@PathVariable() Long idEventos) throws EventosException {
      try {
         logger.info("===>>>Obtener los detalles del eventos por Id: {}", idEventos);
         return eventosService.getDetailsByIdEventos(idEventos);
      } catch (EventosException agE) {
         throw agE;
      } catch (Exception ex) {
         EventosException agE = new EventosException("No fue posible obtener los detalles del eventos por Id", EventosException.LAYER_REST, EventosException.ACTION_SELECT);
         logger.error("===>>>Error al obtener los detalles del eventos por Id- CODE: {} - ", agE.getExceptionCode(), ex);
         throw agE;
      }
   }

   @RequestMapping(value = "page", method = RequestMethod.GET)
   @ResponseStatus(HttpStatus.OK)
   public Page<EventosView> getEventosPage(@RequestParam(required = false) List<Long> idUsuario,
                                             @RequestParam(required = false) List<Integer> idTipoEvento,
                                             @RequestParam(required = false) String titulo,
                                             @RequestParam(required = false) Long startDate,
                                             @RequestParam(required = false) Long endDate,
                                             @RequestParam(required = false) List<Long> idUsuarioRecibe,
                                             @RequestParam(required = false) List<String> idPaciente,
                                             @RequestParam(required = false) List<Long> regionSanitaria,
                                             @RequestParam(required = false) List<String> unidadMedica,
                                             @RequestParam(required = false) List<String> especialidad,
                                             @RequestParam(required = false) List<Integer> status,
                                             @RequestParam(required = false, defaultValue = "0") Integer page,
                                             @RequestParam(required = false, defaultValue = "10") Integer size,
                                             @RequestParam(required = false, defaultValue = "titulo") String orderColumn,
                                             @RequestParam(required = false, defaultValue = "asc") String orderType) throws EventosException {

      logger.info("===>>>getEventosSearch(): - idUsuarioCrea: {} - idTipoEvento: {} - titulo: {} - startDate: {} - endDate: {} - page: {} - size: {} - orderColumn: {} - orderType: {}",
         idUsuario, idTipoEvento, titulo, startDate, endDate, page, size, orderColumn, orderType);
      logger.info("idUsuarioRecibe: {} - idPaciente: {} - regionSanitaria: {} - unidadMedica: {} - status: {}",
              idUsuarioRecibe, idPaciente, regionSanitaria, unidadMedica, status);
      if(page < 0)
         page = 0;
      if(size < 0)
         size = 10;
      if(!orderType.equalsIgnoreCase("asc") && !orderType.equalsIgnoreCase("desc"))
         orderType = "asc";
      return eventosService.getEventosPage(idUsuario, idTipoEvento, titulo, startDate, endDate,
              idUsuarioRecibe, idPaciente, especialidad, regionSanitaria, unidadMedica, status,page, size, orderColumn, orderType.toLowerCase());
   }

   @RequestMapping(value = "search", method = RequestMethod.GET)
   @ResponseStatus(HttpStatus.OK)
   public List<EventosView> getEventosSearch(@RequestParam(required = false) List<Long> idUsuario,
                                             @RequestParam(required = false) List<Integer> idTipoEvento,
                                             @RequestParam(required = false) String titulo,
                                             @RequestParam(required = false) Long startDate,
                                             @RequestParam(required = false) Long endDate,
                                             @RequestParam(required = false) List<Long> idUsuarioRecibe,
                                             @RequestParam(required = false) List<String> idPaciente,
                                             @RequestParam(required = false) List<Long> regionSanitaria,
                                             @RequestParam(required = false) List<String> unidadMedica,
                                             @RequestParam(required = false) List<String> especialidad,
                                             @RequestParam(required = false) List<Integer> status,
                                             @RequestParam(required = false, defaultValue = "titulo") String orderColumn,
                                             @RequestParam(required = false, defaultValue = "asc") String orderType) throws EventosException {

      logger.info("===>>>getEventosSearch(): - idUsuarioCrea: {} - idTipoEvento: {} - titulo: {} - startDate: {} - endDate: {} - orderColumn: {} - orderType: {}",
              idUsuario, idTipoEvento, titulo, startDate, endDate, orderColumn, orderType);
      logger.info("idUsuarioRecibe: {} - idPaciente: {} - regionSanitaria: {} - unidadMedica: {} - status: {}",
              idUsuarioRecibe, idPaciente, regionSanitaria, unidadMedica, status);
      if(!orderType.equalsIgnoreCase("asc") && !orderType.equalsIgnoreCase("desc"))
         orderType = "asc";
      return eventosService.getEventosSearch(idUsuario, idTipoEvento, titulo, startDate, endDate,
              idUsuarioRecibe, idPaciente, especialidad, regionSanitaria, unidadMedica, status, orderColumn, orderType.toLowerCase());
   }

   @RequestMapping(value = "/cancelar/{idEventos}", method = RequestMethod.DELETE)
   @ResponseStatus(HttpStatus.OK)
   public void deleteEventos(@PathVariable() Long idEventos) throws EventosException {
      try {
         logger.info("Eliminar eventos: {}", idEventos);
         eventosService.deleteEventos(idEventos);
      } catch (EventosException agE) {
         throw agE;
      } catch (Exception ex) {
         EventosException agE = new EventosException("No fue posible eliminar eventos", EventosException.LAYER_REST, EventosException.ACTION_UPDATE);
         logger.error("===>>>Error al eliminar eventos- CODE: {} - ", agE.getExceptionCode(), ex);
         throw agE;
      }
   }

   @RequestMapping(value = "/por-consulta/{idConsulta}", method = RequestMethod.GET)
   @ResponseStatus(HttpStatus.OK)
   public List<EventosView> getDetailsByIdConsulta(@PathVariable() Long idConsulta) throws EventosException {
      try {
         logger.info("===>>>Obtener los detalles del eventos por Id: {}", idConsulta);
         return eventosService.getDetailsByIdConsulta(idConsulta);
      } catch (EventosException agE) {
         throw agE;
      } catch (Exception ex) {
         EventosException agE = new EventosException("No fue posible obtener los detalles del eventos por Id", EventosException.LAYER_REST, EventosException.ACTION_SELECT);
         logger.error("===>>>Error al obtener los detalles del eventos por Id- CODE: {} - ", agE.getExceptionCode(), ex);
         throw agE;
      }
   }

   @RequestMapping(value = "/status", method = RequestMethod.PUT)
   @ResponseStatus(HttpStatus.OK)
   public void updateEstatusEvento(@RequestParam() Long idEvento, @RequestParam() Integer status) throws EventosException {
      try {
         logger.info("===>>>Actualizar estatus del eventos: {} a estatus: {}", idEvento, status);
         if(idEvento == null) {
            logger.error("evento vacío/null");
            EventosException eveE = new EventosException("Campo evento vacío", EventosException.LAYER_DAO, EventosException.ACTION_VALIDATE);
            eveE.addError("evento vacío");
            throw eveE;
         }
         if(status == null) {
            logger.error("status vacío/null");
            EventosException eveE = new EventosException("Campo status vacío", EventosException.LAYER_DAO, EventosException.ACTION_VALIDATE);
            eveE.addError("Status vacío");
            throw eveE;
         }
         eventosService.updateEstatusEvento(idEvento, status);
      } catch (EventosException agE) {
         throw agE;
      } catch (Exception ex) {
         EventosException agE = new EventosException("No fue posible actualizar el evento por Id", EventosException.LAYER_REST, EventosException.ACTION_SELECT);
         logger.error("===>>>No fue posible actualizar el evento por Id- CODE: {} - ", agE.getExceptionCode(), ex);
         throw agE;
      }
   }

   @RequestMapping(value = "/getValue", method = RequestMethod.GET)
   @ResponseStatus(HttpStatus.OK)
   public Long getEventosValue() throws EventosException {
      try {
         return eventosService.getEventosValue();
      } catch (EventosException agE) {
         throw agE;
      } catch (Exception ex) {
         EventosException agE = new EventosException("No fue posible obtener el valor de evento", EventosException.LAYER_REST, EventosException.ACTION_SELECT);
         logger.error("===>>>Error al obtener el valor del evento - CODE: {} - ", agE.getExceptionCode(), ex);
         throw agE;
      }
   }
}
