package net.amentum.niomedic.expediente.rest;

import lombok.extern.slf4j.Slf4j;
import net.amentum.common.v2.RestBaseController;
import net.amentum.niomedic.expediente.exception.NotificacionesException;
import net.amentum.niomedic.expediente.service.NotificacionesService;
import net.amentum.niomedic.expediente.views.NotificacionesView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
//import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("notificaciones")
@Slf4j
public class NotificacionesRest extends RestBaseController {
   @Autowired
   NotificacionesService notificacionesService;

   @RequestMapping(method = RequestMethod.POST)
   @ResponseStatus(HttpStatus.CREATED)
   public NotificacionesView createNotificaciones(@RequestBody @Valid NotificacionesView notificacionesView) throws NotificacionesException {
      log.info("===>>>createNotificaciones() - POST");
      return notificacionesService.createNotificaciones(notificacionesView);
   }

   @RequestMapping(value = "{idNotificaciones}", method = RequestMethod.PUT)
   @ResponseStatus(HttpStatus.OK)
   public NotificacionesView updateNotificaciones(@PathVariable("idNotificaciones") Long idNotificaciones, @RequestBody @Valid NotificacionesView notificacionesView) throws NotificacionesException {
      log.info("===>>>updateNotificaciones() - PUT - idNotificaciones: {}", idNotificaciones);
      notificacionesView.setIdNotificaciones(idNotificaciones);
      return notificacionesService.updateNotificaciones(notificacionesView);
   }

   @RequestMapping(value = "{idNotificaciones}", method = RequestMethod.GET)
   @ResponseStatus(HttpStatus.OK)
   public NotificacionesView getDetailsByIdNotificaciones(@PathVariable("idNotificaciones") Long idNotificaciones) throws NotificacionesException {
      log.info("===>>>updateNotificaciones() - GET - idNotificaciones: {}", idNotificaciones);
      return notificacionesService.getDetailsByIdNotificaciones(idNotificaciones);
   }

   @RequestMapping(value = "search", method = RequestMethod.GET)
   @ResponseStatus(HttpStatus.OK)
   public List<NotificacionesView> getNotificacionesSearch(@RequestParam(required = true) Long idUsuario,
                                                           @RequestParam(required = true) Integer estatus) throws NotificacionesException {
      log.info("===>>>getNotificacionesSearch()  - idUsuario: {} - estatus: {}", idUsuario, estatus);
      return notificacionesService.getNotificacionesSearch(idUsuario, estatus);
   }

   @RequestMapping(value = "{idNotificaciones}", method = RequestMethod.DELETE)
   @ResponseStatus(HttpStatus.OK)
   public void deleteNotificaciones(@PathVariable("idNotificaciones") Long idNotificaciones) throws NotificacionesException {
      log.info("===>>>deleteNotificaciones() - DELETE - idNotificaciones: {}", idNotificaciones);
      notificacionesService.deleteNotificaciones(idNotificaciones);
   }

   @RequestMapping(value = "/leido/{idNotificaciones}", method = RequestMethod.PUT)
   @ResponseStatus(HttpStatus.OK)
   public void readedNotificaciones(@PathVariable("idNotificaciones") Long idNotificaciones) throws NotificacionesException {
      log.info("===>>>readedNotificaciones() - PUT - idNotificaciones: {}", idNotificaciones);
      notificacionesService.readedNotificaciones(idNotificaciones);
   }
}
