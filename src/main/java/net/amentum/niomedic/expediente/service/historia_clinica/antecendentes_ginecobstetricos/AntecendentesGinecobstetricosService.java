package net.amentum.niomedic.expediente.service.historia_clinica.antecendentes_ginecobstetricos;

import net.amentum.niomedic.expediente.exception.HistoriaClinicaGeneralException;
import net.amentum.niomedic.expediente.views.historia_clinica_general.antecendentes_ginecobstetricos.AntecendentesGinecobstetricosView;

import javax.validation.constraints.NotNull;

public interface AntecendentesGinecobstetricosService {
    void createAntecendentesGinecobstetricos(AntecendentesGinecobstetricosView view, @NotNull Long idHistoriaClinica) throws HistoriaClinicaGeneralException;

    void updateAntecendentesGinecobstetricos(AntecendentesGinecobstetricosView view, @NotNull Long idHistoriaClinica) throws HistoriaClinicaGeneralException;

    AntecendentesGinecobstetricosView getAntecendentesGinecobstetricos(@NotNull Long idHistoriaClinica) throws HistoriaClinicaGeneralException;

    void deleteAntecendentesGinecobstetricos(@NotNull Long idHistoriaClinica) throws HistoriaClinicaGeneralException;
}
