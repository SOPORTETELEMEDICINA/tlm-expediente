package net.amentum.niomedic.expediente.converter.historia_clinica_general.personales_no_patologicos;

import net.amentum.niomedic.expediente.model.historia_clinica.personales_no_patologicos.HistoriaLaboral;
import net.amentum.niomedic.expediente.views.historia_clinica_general.personales_no_patologicos.HistoriaLaboralView;
import org.springframework.stereotype.Component;

@Component
public class HistoriaLaboralConverter {

    public HistoriaLaboral toEntity(HistoriaLaboralView view) {
        HistoriaLaboral entity = new HistoriaLaboral();
        entity.setIdHistoriaLaboral(view.getIdHistoriaLaboral());
        entity.setIdHistoriaClinica(view.getIdHistoriaClinica());
        if(view.getTrabajaSi())
            entity.setTrabaja(true);
        else
            entity.setTrabaja(false);
        entity.setJubilado(view.getJubilado());
        entity.setJubilacionEdad(view.getJubilacionEdad());
        entity.setJubilacionEnfermedad(view.getJubilacionEnfermedad());
        entity.setJubilacionInput(view.getJubilacionInput());
        entity.setComentarios(view.getComentariosConsumo());
        return entity;
    }

    public HistoriaLaboralView toView(HistoriaLaboral entity) {
        HistoriaLaboralView view = new HistoriaLaboralView();
        view.setIdHistoriaLaboral(entity.getIdHistoriaLaboral());
        view.setIdHistoriaClinica(entity.getIdHistoriaClinica());
        if(entity.getTrabaja()) {
            view.setTrabajaSi(true);
            view.setTrabajaNo(false);
        } else {
            view.setTrabajaSi(false);
            view.setTrabajaNo(true);
        }
        view.setJubilado(entity.getJubilado());
        view.setJubilacionEdad(entity.getJubilacionEdad());
        view.setJubilacionEnfermedad(entity.getJubilacionEnfermedad());
        view.setJubilacionInput(entity.getJubilacionInput());
        view.setComentariosConsumo(entity.getComentarios());
        return view;
    }

}
