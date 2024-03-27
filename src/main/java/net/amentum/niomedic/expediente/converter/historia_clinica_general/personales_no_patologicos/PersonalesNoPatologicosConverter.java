package net.amentum.niomedic.expediente.converter.historia_clinica_general.personales_no_patologicos;

import net.amentum.niomedic.expediente.model.historia_clinica.personales_no_patologicos.PersonalesNoPatologicos;
import net.amentum.niomedic.expediente.views.historia_clinica_general.personales_no_patologicos.PersonalesNoPatologicosView;
import org.springframework.stereotype.Component;

@Component
public class PersonalesNoPatologicosConverter {

    public PersonalesNoPatologicos toEntity(PersonalesNoPatologicosView view) {
        PersonalesNoPatologicos entity = new PersonalesNoPatologicos();
        entity.setId(view.getIdPersonalesNoPatologicos());
        entity.setIdHistoriaClinica(view.getIdHistoriaClinica());
        entity.setNotas(view.getNotas());
        return entity;
    }


    public PersonalesNoPatologicosView toView (PersonalesNoPatologicos entity) {
        PersonalesNoPatologicosView view = new PersonalesNoPatologicosView();
        view.setIdPersonalesNoPatologicos(entity.getId());
        view.setIdHistoriaClinica(entity.getIdHistoriaClinica());
        view.setNotas(entity.getNotas());
        return view;
    }
}
