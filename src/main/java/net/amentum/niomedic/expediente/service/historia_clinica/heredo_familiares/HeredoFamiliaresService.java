package net.amentum.niomedic.expediente.service.historia_clinica.heredo_familiares;

import net.amentum.niomedic.expediente.exception.HistoriaClinicaGeneralException;
import net.amentum.niomedic.expediente.views.historia_clinica_general.heredo_familiares.HeredoFamiliaresView;

import javax.validation.constraints.NotNull;

public interface HeredoFamiliaresService {

    void createHeredoFamiliares(HeredoFamiliaresView view, @NotNull Long idHistoriaClinica) throws HistoriaClinicaGeneralException;

    void updateHeredoFamiliares(HeredoFamiliaresView view, @NotNull Long idHistoriaClinica) throws HistoriaClinicaGeneralException;

    HeredoFamiliaresView getHeredoFamiliares(@NotNull Long idHistoriaClinica) throws HistoriaClinicaGeneralException;

    void deleteHeredoFamiliares(@NotNull Long idHistoriaClinica) throws HistoriaClinicaGeneralException;

}
