package net.amentum.niomedic.expediente.exception;

import org.springframework.http.HttpStatus;

import net.amentum.common.v2.GenericException;

public class ResultadoEstudioException extends GenericException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5335356652873999266L;
	
	public static final String CONSULTA_NOT_FOUNT = "No se encontró consulta con el id: %s";
	public static final String SERVER_ERROR = "No se pudo efectuar la operación %s  resultado de estudio";
	public static final String BASE64_ERROR = "base64: contiene un formato de BASE64 incorrecto";
	public static final String ARCHIVO_ERROR = "Error inesperado al guardar el archivo";
	

	public ResultadoEstudioException(HttpStatus status, String message) {
		super(status, message);
	}

}
