package net.amentum.niomedic.expediente.converter.historia_clinica_general.personales_no_patologicos;

import net.amentum.niomedic.expediente.model.historia_clinica.personales_no_patologicos.ActividadDeportiva;
import net.amentum.niomedic.expediente.views.historia_clinica_general.personales_no_patologicos.ActividadDeportivaView;
import org.springframework.stereotype.Component;

@Component
public class ActividadDeportivaConverter {

    public ActividadDeportiva toEntity(ActividadDeportivaView view) {
        ActividadDeportiva entity = new ActividadDeportiva();
        entity.setIdActivadDeportiva(view.getIdActivadDeportiva());
        entity.setIdHistoriaClinica(view.getIdHistoriaClinica());
        if(view.getAcDeportivaSi())
            entity.setHaceActividad(true);
        else
            entity.setHaceActividad(false);
        entity.setTipo(view.getTipoActividadInput());
        entity.setFrecuencia(view.getAcDeportivaFrecuencia());
        return entity;
    }

    public ActividadDeportivaView toView(ActividadDeportiva entity) {
        ActividadDeportivaView view = new ActividadDeportivaView();
        view.setIdActivadDeportiva(entity.getIdActivadDeportiva());
        view.setIdHistoriaClinica(entity.getIdHistoriaClinica());
        if(entity.getHaceActividad()) {
            view.setAcDeportivaSi(true);
            view.setAcDeportivaNo(false);
        } else {
            view.setAcDeportivaNo(true);
            view.setAcDeportivaSi(false);
        }
        view.setTipoActividadInput(entity.getTipo());
        view.setAcDeportivaFrecuencia(entity.getFrecuencia());
        return view;
    }

}
