package net.amentum.niomedic.expediente.converter.historia_clinica_general.personales_no_patologicos;

import net.amentum.niomedic.expediente.model.historia_clinica.personales_no_patologicos.HabitoHigienico;
import net.amentum.niomedic.expediente.views.historia_clinica_general.personales_no_patologicos.HabitosHigienicosView;
import org.springframework.stereotype.Component;

@Component
public class HabitoHigienicoConverter {

    public HabitoHigienico toEntity(HabitosHigienicosView view) {
        HabitoHigienico entity = new HabitoHigienico();
        entity.setIdHabito(view.getIdHabito());
        entity.setIdHistoriaClinica(view.getIdHistoriaClinica());
        entity.setAseoBucal(view.getAseoBucal());
        entity.setBanio(view.getBanio());
        return entity;
    }

    public HabitosHigienicosView toView(HabitoHigienico entity) {
        HabitosHigienicosView view = new HabitosHigienicosView();
        view.setIdHabito(entity.getIdHabito());
        view.setIdHistoriaClinica(entity.getIdHistoriaClinica());
        view.setAseoBucal(entity.getAseoBucal());
        view.setBanio(entity.getBanio());
        return view;
    }
}
