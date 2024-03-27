package net.amentum.niomedic.expediente.converter.historia_clinica_general.vacunacion;

import net.amentum.niomedic.expediente.model.historia_clinica.vacunacion.Vacunacion;
import net.amentum.niomedic.expediente.views.historia_clinica_general.vacunacion.VacunacionView;
import org.springframework.stereotype.Component;

@Component
public class VacunacionConverter {

    public Vacunacion toEntity(VacunacionView view) {
        Vacunacion entity = new Vacunacion();
        entity.setIdVacunacion(view.getIdVacunacion());
        entity.setIdHistoriaClinica(view.getIdHistoriaClinica());
        entity.setNombre(view.getNombre());
        entity.setEnfermedad(view.getEnfermedad());
        entity.setDosisOrdinal(view.getDosisOrdinal());
        entity.setDosisEdad(view.getDosisEdad());
        entity.setFecha(view.getFecha());
        entity.setNotas(view.getNotas());
        return entity;
    }

    public VacunacionView toView(Vacunacion entity) {
        VacunacionView view = new VacunacionView();
        view.setIdVacunacion(entity.getIdVacunacion());
        view.setIdHistoriaClinica(entity.getIdHistoriaClinica());
        view.setNombre(entity.getNombre());
        view.setEnfermedad(entity.getEnfermedad());
        view.setDosisOrdinal(entity.getDosisOrdinal());
        view.setDosisEdad(entity.getDosisEdad());
        view.setFecha(entity.getFecha());
        view.setNotas(entity.getNotas());
        return view;
    }

}
