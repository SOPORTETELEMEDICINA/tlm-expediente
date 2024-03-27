package net.amentum.niomedic.expediente.service;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

import net.amentum.niomedic.expediente.exception.PeriodosControlesException;
import net.amentum.niomedic.expediente.views.PeriodosControlesView;

public interface PeriodosControlesService {
	Set<PeriodosControlesView> createPeriodosControles(Long idControl, Set<PeriodosControlesView>periodosControlesView )throws PeriodosControlesException;

	Set<PeriodosControlesView> updatePeriodosControles(Long idControl, Set<PeriodosControlesView>periodosControlesView )throws PeriodosControlesException;
	
	Set<PeriodosControlesView>  getPeriodosControles(Long idControl)throws PeriodosControlesException;
	
	void deletePeriodosControles(Long idControl)throws PeriodosControlesException;
	
	Set<PeriodosControlesView> getPeriodosPorPaciente(UUID idPaciente)throws PeriodosControlesException;
}
