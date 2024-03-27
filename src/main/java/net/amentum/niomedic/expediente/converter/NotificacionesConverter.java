package net.amentum.niomedic.expediente.converter;

import net.amentum.niomedic.expediente.model.Notificaciones;
import net.amentum.niomedic.expediente.views.NotificacionesView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Date;

@Component
public class NotificacionesConverter {
   private final Logger logger = LoggerFactory.getLogger(NotificacionesConverter.class);

   public Notificaciones toEntity(NotificacionesView notificacionesView, Notificaciones notificaciones, Boolean update) {
      notificaciones.setTipo(notificacionesView.getTipo());
      notificaciones.setIdUsuario(notificacionesView.getIdUsuario());
      notificaciones.setSubject(notificacionesView.getSubject());
      notificaciones.setDescripcion(notificacionesView.getDescripcion());
      notificaciones.set_fechaCreacion((update) ? notificaciones.get_fechaCreacion() : new Date());
      notificaciones.setFechaEnvio(notificacionesView.getFechaEnvio());
      notificaciones.setFechaLeido(notificacionesView.getFechaLeido());
      notificaciones.setFechaBorrado(notificacionesView.getFechaBorrado());
      notificaciones.setAccion(notificacionesView.getAccion());
      notificaciones.setPrioridad(notificacionesView.getPrioridad());
      notificaciones.setEstatus(notificacionesView.getEstatus());
//      byte[] decodedString = Base64.getDecoder().decode(new String(notificacionesView.getAdjuntos()).getBytes("UTF-8"));
      try {
         byte[] stringToByte = Base64.getDecoder().decode(notificacionesView.getAdjuntos().getBytes("UTF-8"));
         notificaciones.setAdjuntos(stringToByte);
      } catch (UnsupportedEncodingException e) {
         e.printStackTrace();
      }

      logger.debug("convertir notificacionesView to Entity: {}", notificaciones);
      return notificaciones;
   }

   public NotificacionesView toView(Notificaciones notificaciones, Boolean completeCoversion) {
      NotificacionesView notificacionesView = new NotificacionesView();
      notificacionesView.setIdNotificaciones(notificaciones.getIdNotificaciones());
      notificacionesView.setTipo(notificaciones.getTipo());
      notificacionesView.setIdUsuario(notificaciones.getIdUsuario());
      notificacionesView.setSubject(notificaciones.getSubject());
      notificacionesView.setDescripcion(notificaciones.getDescripcion());
      notificacionesView.set_fechaCreacion(notificaciones.get_fechaCreacion());
      notificacionesView.setFechaEnvio(notificaciones.getFechaEnvio());
      notificacionesView.setFechaLeido(notificaciones.getFechaLeido());
      notificacionesView.setFechaBorrado(notificaciones.getFechaBorrado());
      notificacionesView.setAccion(notificaciones.getAccion());
      notificacionesView.setPrioridad(notificaciones.getPrioridad());
      notificacionesView.setEstatus(notificaciones.getEstatus());
      byte[] encoded = Base64.getEncoder().encode(notificaciones.getAdjuntos());
      notificacionesView.setAdjuntos(new String(encoded));

      logger.debug("convertir MedicionesPaciente to View: {}", notificacionesView);
      return notificacionesView;
   }


}
