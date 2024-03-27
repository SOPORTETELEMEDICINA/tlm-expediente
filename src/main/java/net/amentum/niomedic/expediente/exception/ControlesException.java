package net.amentum.niomedic.expediente.exception;

import net.amentum.common.v2.GenericException;
import org.springframework.http.HttpStatus;

public class ControlesException extends GenericException {

	private static final long serialVersionUID = 5267501119661639531L;
	public static final String ITEM_EXISTENTE="Ya existe un control con el idPaciente %s";
	public static final String SERVER_ERROR = "No fue posible %s el Control";
	public static final String NO_NULOS= "Los campos %s no pueden ser NULL ó Vacios";
	public static final String NO_NULO= "El campo %s no pueden ser NULL ó Vacio";
	public static final String RANGO_VALIDACION ="Los rangos de los valores son acendetes limiteBajo < rangoBajo < rangoAlto < limiteAlto"; 
	public static final String ITEM_NO_ENCONTRADO= "No se encontro ningun Control para el idPaciente: %s";
	
	public ControlesException(HttpStatus status, String message) {
		super(status, message);
	}

}
