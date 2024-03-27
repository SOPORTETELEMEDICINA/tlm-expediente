package net.amentum.niomedic.expediente.service;

import java.util.UUID;

import net.amentum.niomedic.expediente.exception.ControlesException;
import net.amentum.niomedic.expediente.views.ControlesView;

public interface ControlesService {
	/**
	*Método para crear Controles
	*@param controlesView contiene la informacion a guardar
	*@Return ControlesView regresa el mismo objeto de entrada pero con los ids
	**/
	ControlesView createControles(ControlesView controlesView)throws ControlesException;
	
	
	/**
	*Método para actrualizar Controles
	*@param controlesView contiene la informacion a guardar
	*@Return ControlesView regresa el mismo objeto de entrada pero con los ids
	**/
	ControlesView updateControles(ControlesView controlesView)throws ControlesException;
	
	/**
	*Método para Obtener Controles
	*@param idPaciente por el cual se buscara el control
	*@Return ControlesView regresa la vista del objeto registro encotrado
	**/
	ControlesView getControles(UUID idPaciente) throws ControlesException;
	
	/**
	*Método para Eliminar Controles
	*@param idPaciente por el cual se buscara el control a Eliminar
	**/
	void deleteControles(UUID idPaciente) throws ControlesException;
}
