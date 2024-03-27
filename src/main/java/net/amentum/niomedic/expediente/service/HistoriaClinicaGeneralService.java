package net.amentum.niomedic.expediente.service;

import net.amentum.niomedic.expediente.exception.HistoriaClinicaGeneralException;
import net.amentum.niomedic.expediente.views.HistoriaClinicaGeneralView;

import java.util.UUID;

public interface HistoriaClinicaGeneralService {
   void createHistoriaClinicaGeneral(HistoriaClinicaGeneralView historiaClinicaGeneralView) throws HistoriaClinicaGeneralException;

   void updateHistoriaClinicaGeneral(HistoriaClinicaGeneralView historiaClinicaGeneralView) throws HistoriaClinicaGeneralException;

   HistoriaClinicaGeneralView getDetailsByIdPaciente(UUID idPaciente) throws HistoriaClinicaGeneralException;
}
