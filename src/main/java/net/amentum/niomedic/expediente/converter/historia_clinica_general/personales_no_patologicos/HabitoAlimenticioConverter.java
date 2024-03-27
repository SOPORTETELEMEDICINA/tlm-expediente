package net.amentum.niomedic.expediente.converter.historia_clinica_general.personales_no_patologicos;

import net.amentum.niomedic.expediente.model.historia_clinica.personales_no_patologicos.HabitoAlimenticio;
import net.amentum.niomedic.expediente.views.historia_clinica_general.personales_no_patologicos.HabitoAlimenticioView;
import org.springframework.stereotype.Component;

@Component
public class HabitoAlimenticioConverter {

    public HabitoAlimenticio toEntity(HabitoAlimenticioView view) {
        HabitoAlimenticio entity = new HabitoAlimenticio();
        entity.setIdHabito(view.getIdHabito());
        entity.setIdHistoriaClinica(view.getIdHistoriaClinica());
        entity.setCantidad(view.getCantidad());
        entity.setCalidad(view.getCalidad());
        entity.setComentarios(view.getComentarios());
        return entity;
    }

    public HabitoAlimenticioView toView(HabitoAlimenticio entity) {
        HabitoAlimenticioView view = new HabitoAlimenticioView();
        view.setIdHabito(entity.getIdHabito());
        view.setIdHistoriaClinica(entity.getIdHistoriaClinica());
        view.setCantidad(entity.getCantidad());
        view.setCalidad(entity.getCalidad());
        view.setComentarios(entity.getComentarios());
        return view;
    }
}
