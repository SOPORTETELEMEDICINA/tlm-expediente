package net.amentum.niomedic.expediente.service.historia_clinica.personales_patologicos;

import net.amentum.niomedic.expediente.exception.HistoriaClinicaGeneralException;
import net.amentum.niomedic.expediente.views.historia_clinica_general.personales_patologicos.PersonalesPatologicosView;

import javax.validation.constraints.NotNull;

public interface PersonalesPatologicosService {
    void createPersonalesPatologicos(PersonalesPatologicosView view, @NotNull Long idHistoriaClinica) throws HistoriaClinicaGeneralException;

    void updatePersonalesPatologicos(PersonalesPatologicosView view, @NotNull Long idHistoriaClinica) throws HistoriaClinicaGeneralException;

    PersonalesPatologicosView getPersonalesPatologicos(@NotNull Long idHistoriaClinica) throws HistoriaClinicaGeneralException;

    void deletePersonalesPatologicos(@NotNull Long idHistoriaClinica);
}
