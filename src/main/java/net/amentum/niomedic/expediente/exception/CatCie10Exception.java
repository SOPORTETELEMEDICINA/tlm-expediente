package net.amentum.niomedic.expediente.exception;

import net.amentum.common.v2.GenericException;
import org.springframework.http.HttpStatus;

public class CatCie10Exception extends GenericException{
   public static final String BAD_REQUEST = "Error de validación: \n%s";
   public static final String SERVER_ERROR = "No fue posible %s el CatCie10";
   public static final String ITEM_NOT_FOUND = "No se encontró ningún Cie9 con el id: %s";

   public CatCie10Exception(HttpStatus status, String message){
      super(status,message);
   }
}
