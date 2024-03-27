package net.amentum.niomedic.expediente.service.historia_clinica.personales_no_patologicos;

import net.amentum.niomedic.expediente.exception.HistoriaClinicaGeneralException;
import net.amentum.niomedic.expediente.views.historia_clinica_general.personales_no_patologicos.PersonalesNoPatologicosView;

public interface PersonalesNoPatologicosService {

    void createPersonalesNoPatologicos(PersonalesNoPatologicosView view, Long idHistoriaClinica) throws HistoriaClinicaGeneralException;

    void updatePersonalesNoPatologicos(PersonalesNoPatologicosView view, Long idHistoriaClinica) throws HistoriaClinicaGeneralException;

    PersonalesNoPatologicosView getPersonalesNoPatologicos(Long idHistoriaClinica) throws HistoriaClinicaGeneralException;

    void deletePersonalesNoPatologicos(Long idHistoriaClinica);

}
