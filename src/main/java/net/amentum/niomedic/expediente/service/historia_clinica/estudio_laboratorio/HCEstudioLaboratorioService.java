package net.amentum.niomedic.expediente.service.historia_clinica.estudio_laboratorio;

import net.amentum.niomedic.expediente.exception.HistoriaClinicaGeneralException;
import net.amentum.niomedic.expediente.views.historia_clinica_general.estudio_laboratorio.HCEstudioLaboratorioView;

import javax.validation.constraints.NotNull;

public interface HCEstudioLaboratorioService {

    void createEstudioLaboratorio(HCEstudioLaboratorioView view, @NotNull Long idHistoriaClinica) throws HistoriaClinicaGeneralException;

    void updateEstudioLaboratorio(HCEstudioLaboratorioView view, @NotNull Long idHistoriaClinica) throws HistoriaClinicaGeneralException;

    HCEstudioLaboratorioView getEstudioLaboratorio(@NotNull Long idHistoriaClinica) throws HistoriaClinicaGeneralException;

    void deleteEstudioLaboratorio(@NotNull Long idHistoriaClinica) throws HistoriaClinicaGeneralException;

}