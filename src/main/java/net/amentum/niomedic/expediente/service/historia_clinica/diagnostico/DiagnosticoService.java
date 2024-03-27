package net.amentum.niomedic.expediente.service.historia_clinica.diagnostico;

import net.amentum.niomedic.expediente.exception.HistoriaClinicaGeneralException;
import net.amentum.niomedic.expediente.views.historia_clinica_general.diagnostico.DiagnosticoHCGView;

import javax.validation.constraints.NotNull;

public interface DiagnosticoService {
    void createDiagnostico(DiagnosticoHCGView view, @NotNull Long idHistoriaClinica) throws HistoriaClinicaGeneralException;

    void updateDiagnostico(DiagnosticoHCGView view, @NotNull Long idHistoriaClinica) throws HistoriaClinicaGeneralException;

    DiagnosticoHCGView getDiagnostico(@NotNull Long idHistoriaClinica) throws HistoriaClinicaGeneralException;

    void deleteDiagnostico(@NotNull Long idHistoriaClinica) throws HistoriaClinicaGeneralException;
}
