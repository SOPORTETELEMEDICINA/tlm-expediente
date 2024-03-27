package net.amentum.niomedic.expediente.service.historia_clinica.tratamiento;

import net.amentum.niomedic.expediente.exception.HistoriaClinicaGeneralException;
import net.amentum.niomedic.expediente.views.historia_clinica_general.tratamiento.HCTratamientoView;

import javax.validation.constraints.NotNull;

public interface HCTratamientoService {
    void createTratamiento(HCTratamientoView view, @NotNull Long idHistoriaClinica) throws HistoriaClinicaGeneralException;
    void updateTratamiento(HCTratamientoView view, @NotNull Long idHistoriaClinica) throws HistoriaClinicaGeneralException;
    HCTratamientoView getTratamiento(@NotNull Long idHistoriaClinica) throws HistoriaClinicaGeneralException;
    void deleteTratamiento(@NotNull Long idHistoriaClinica) throws HistoriaClinicaGeneralException;
}
