package net.amentum.niomedic.expediente.exception;

import lombok.Getter;
import lombok.Setter;
import net.amentum.common.GenericException;

public class MedicionesPacienteException extends GenericException {
    private final ExceptionServiceCode MODULE_CODE = ExceptionServiceCode.EXPEDIENTE;
    @Getter
    @Setter
    private String layer;
    @Getter
    @Setter
    private String action;

    public MedicionesPacienteException(Exception ex, String message, String layer, String action){
        super(ex,message);
        this.layer = layer;
        this.action = action;
    }

    public MedicionesPacienteException(String message, String layer, String action){
        super(message);
        this.layer = layer;
        this.action = action;
    }

    @Override
    public String getExceptionCode() {
        return new StringBuffer(layer).append(MODULE_CODE).append(action).toString();
    }
}
