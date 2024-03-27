package net.amentum.niomedic.expediente.converter.historia_clinica_general.personales_patologicos;

import net.amentum.niomedic.expediente.model.historia_clinica.personales_patologicos.PersonalesPatologicos;
import net.amentum.niomedic.expediente.views.historia_clinica_general.personales_patologicos.PersonalesPatologicosView;
import org.springframework.stereotype.Component;

@Component
public class PersonalesPatologicosConverter {

    public PersonalesPatologicos toEntity(PersonalesPatologicosView view) {
        PersonalesPatologicos entity = new PersonalesPatologicos();
        entity.setId(view.getId());
        entity.setIdHistoriaClinica(view.getIdHistoriaClinica());
        entity.setAlergias(view.getAlergias());
        entity.setInputAlergias(view.getTextAlergias());
        entity.setCirugias(view.getCirugias());
        entity.setInputCirugias(view.getTextCirugias());
        entity.setHospitalizacion(view.getHospitalizacion());
        entity.setInputHospitalizacion(view.getTextHospitalizacion());
        entity.setTransfusiones(view.getTransfusiones());
        entity.setInputTransfusiones(view.getTextTransfuciones());
        entity.setComentarios(view.getComentarios());
        entity.setNotas(view.getNotas());
        return entity;
    }

    public PersonalesPatologicosView toView(PersonalesPatologicos entity) {
        PersonalesPatologicosView view = new PersonalesPatologicosView();
        view.setId(entity.getId());
        view.setIdHistoriaClinica(entity.getIdHistoriaClinica());
        view.setAlergias(entity.getAlergias());
        view.setTextAlergias(entity.getInputAlergias());
        view.setCirugias(entity.getCirugias());
        view.setTextCirugias(entity.getInputCirugias());
        view.setHospitalizacion(entity.getHospitalizacion());
        view.setTextHospitalizacion(entity.getInputHospitalizacion());
        view.setTransfusiones(entity.getTransfusiones());
        view.setTextTransfuciones(entity.getInputTransfusiones());
        view.setComentarios(entity.getComentarios());
        view.setNotas(entity.getNotas());
        return view;
    }

}
