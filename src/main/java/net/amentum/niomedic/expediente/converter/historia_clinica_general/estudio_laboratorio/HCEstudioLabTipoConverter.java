package net.amentum.niomedic.expediente.converter.historia_clinica_general.estudio_laboratorio;

import net.amentum.niomedic.expediente.model.historia_clinica.estudio_laboratorio.HCEstudioLabTipo;
import net.amentum.niomedic.expediente.views.historia_clinica_general.estudio_laboratorio.HCEstudioLabEstudioView;
import org.springframework.stereotype.Component;

@Component
public class HCEstudioLabTipoConverter {

    public HCEstudioLabTipo toEntity(HCEstudioLabEstudioView view) {
        HCEstudioLabTipo entity = new HCEstudioLabTipo();
        entity.setIdTipoEstudio(view.getId());
        entity.setIdHistoriaClinica(view.getIdHistoriaClinica());
        entity.setIdEstudioLaboratorio(view.getIdEstudioLaboratorio());
        entity.setTipo(view.getTipo());
        entity.setComentarios(view.getComentarios());
        entity.setDescripcion(view.getDescripcion());
        return entity;
    }

    public HCEstudioLabEstudioView toView(HCEstudioLabTipo entity) {
        HCEstudioLabEstudioView view = new HCEstudioLabEstudioView();
        view.setId(entity.getIdTipoEstudio());
        view.setIdHistoriaClinica(entity.getIdHistoriaClinica());
        view.setIdEstudioLaboratorio(entity.getIdEstudioLaboratorio());
        view.setTipo(entity.getTipo());
        view.setComentarios(entity.getComentarios());
        view.setDescripcion(entity.getDescripcion());
        return view;
    }

}
