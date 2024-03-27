package net.amentum.niomedic.expediente.exception;

//import net.amentum.common.GenericException;
import net.amentum.common.v2.GenericException;
import org.springframework.http.HttpStatus;

/**
 * @author  by marellano on 14/06/17.
 */
public class FormatException extends GenericException {

//    private final ExceptionServiceCode MODULE_CODE = ExceptionServiceCode.EXPEDIENTE;
//
//    private String layer;
//    private String action;
//
//    public FormatException (Exception ex, String message, String layer, String action){
//        super(ex, message);
//        this.layer = layer;
//        this.action = action;
//    }
//
//    public FormatException (String message, String layer, String action){
//        super(message);
//        this.layer = layer;
//        this.action = action;
//    }
//
//    public String getLayer() {
//        return layer;
//    }
//
//    public void setLayer(String layer) {
//        this.layer = layer;
//    }
//
//    public String getAction() {
//        return action;
//    }
//
//    public void setAction(String action) {
//        this.action = action;
//    }
//
//    @Override
//    public String getExceptionCode() { return new StringBuffer(layer).append(MODULE_CODE).append(action).toString(); }

   public static final String ITEM_NO_ENCONTRADO = "No se encontró ningun Formato para el id: %s";
   public static final String SERVER_ERROR = "No fue posible %s -> Formato";

   public FormatException(HttpStatus status, String message) {
      super(status, message);
   }
}
