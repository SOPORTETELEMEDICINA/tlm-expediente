package net.amentum.niomedic.expediente.service.historia_clinica.interrogatorio_aparatos;

import net.amentum.niomedic.expediente.exception.HistoriaClinicaGeneralException;
import net.amentum.niomedic.expediente.views.historia_clinica_general.interrogatorio_aparatos.InterrogatorioAparatosView;

import javax.validation.constraints.NotNull;

public interface InterrogatorioAparatosService {
    void createInterrogatorioAparatos(InterrogatorioAparatosView view, @NotNull Long idHistoriaClinica) throws HistoriaClinicaGeneralException;

    void updateInterrogatorioAparatos(InterrogatorioAparatosView view, @NotNull Long idHistoriaClinica) throws HistoriaClinicaGeneralException;

    InterrogatorioAparatosView getInterrogatorioAparatos(@NotNull Long idHistoriaClinica) throws HistoriaClinicaGeneralException;

    void deleteInterrogatorioAparatos(@NotNull Long idHistoriaClinica) throws HistoriaClinicaGeneralException;
}
