package net.amentum.niomedic.expediente.exception;

import net.amentum.common.v2.GenericException;
import org.springframework.http.HttpStatus;

public class PeriodosControlesException extends GenericException{
	public static final String ITEM_NO_ENCONTRADO="No se encontro ningun control con el id: %s";
	public static final String PADECIMIENTO="Por favor indique %s un Padecimiento por periodo";
	public static final String SERVER_ERROR = "No fue posible %s el Periodo";
	public static final String HORARIO_REPETIDO="se repiten la hora para el día %s";
	public static final String ITEM_LIST_NO_ENCONTRADO="No se encontro ningun Lista de periodos para el control con el id: %s";
	public PeriodosControlesException(HttpStatus status, String message) {
		super(status, message);
	}
}
