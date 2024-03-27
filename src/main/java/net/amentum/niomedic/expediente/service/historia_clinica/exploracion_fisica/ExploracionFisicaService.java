package net.amentum.niomedic.expediente.service.historia_clinica.exploracion_fisica;

import net.amentum.niomedic.expediente.exception.HistoriaClinicaGeneralException;
import net.amentum.niomedic.expediente.views.historia_clinica_general.exploracion_fisica.ExploracionFisicaView;

import javax.validation.constraints.NotNull;

public interface ExploracionFisicaService {

    void createExploracionFisica(ExploracionFisicaView view, @NotNull Long idHistoriaClinica) throws HistoriaClinicaGeneralException;

    void updateExploracionFisica(ExploracionFisicaView view, @NotNull Long idHistoriaClinica) throws HistoriaClinicaGeneralException;

    ExploracionFisicaView getExploracionFisica(@NotNull Long idHistoriaClinica) throws HistoriaClinicaGeneralException;

    void deleteExploracionFisica(@NotNull Long idHistoriaClinica) throws HistoriaClinicaGeneralException;
}
