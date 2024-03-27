package net.amentum.niomedic.expediente.service.historia_clinica.vacunacion;

import net.amentum.niomedic.expediente.exception.HistoriaClinicaGeneralException;
import net.amentum.niomedic.expediente.views.historia_clinica_general.vacunacion.VacunacionView;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;

public interface VacunacionService {
    void createVacunacion(ArrayList<VacunacionView> view, @NotNull Long idHistoriaClinica) throws HistoriaClinicaGeneralException;

    void updateVacunacion(ArrayList<VacunacionView> view, @NotNull Long idHistoriaClinica) throws HistoriaClinicaGeneralException;

    VacunacionView getVacunacion(@NotNull Long idHistoriaClinica) throws HistoriaClinicaGeneralException;

    void deleteVacunacion(@NotNull Long idHistoriaClinica) throws HistoriaClinicaGeneralException;
}
