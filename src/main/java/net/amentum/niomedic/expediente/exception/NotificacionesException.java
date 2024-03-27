package net.amentum.niomedic.expediente.exception;

import net.amentum.common.v2.GenericException;
import org.springframework.http.HttpStatus;

public class NotificacionesException extends GenericException {
   public static final String SERVER_ERROR = "No fue posible %s la Notificación";
   public static final String ITEM_NO_ENCONTRADO = "No se encontró ninguna Notificación para el idNotificacion: %s";
   public static final String ESTATUS_BORRADO = "No se puede modificar una Notificación id: %s, con estatus Borrado";
   public static final String ESTATUS_LEIDO = "No se puede modificar una Notificación id: %s, con estatus Leído";

//   public static final String NO_NULOS = "Los campos %s no pueden ser NULL ó Vacios";
//   public static final String NO_NULO = "El campo %s no pueden ser NULL ó Vacio";
//   public static final String RANGO_VALIDACION = "Los rangos de los valores son acendetes limiteBajo < rangoBajo < rangoAlto < limiteAlto";
//   public static final String ITEM_EXISTENTE = "Ya existe un control con el idPaciente %s";

   public NotificacionesException(HttpStatus status, String message) {
      super(status, message);
   }

}
