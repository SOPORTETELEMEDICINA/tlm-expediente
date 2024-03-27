package net.amentum.niomedic.expediente.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;
import net.amentum.common.GenericException;

public class ConsultaException extends GenericException {
    private final ExceptionServiceCode MODULE_CODE = ExceptionServiceCode.EXPEDIENTE;
    @Getter
    @Setter
    private String layer;
    @Getter
    @Setter
    private String action;

    public ConsultaException(Exception ex, String message, String layer, String action){
        super(ex,message);
        this.layer = layer;
        this.action = action;
    }

    public ConsultaException(String message, String layer, String action){
        super(message);
        this.layer = layer;
        this.action = action;
    }

	@Override
    public String getExceptionCode() {
        return new StringBuffer(layer).append(MODULE_CODE).append(action).toString();
    }
}
