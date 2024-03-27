package net.amentum.niomedic.expediente.converter;

import net.amentum.niomedic.expediente.model.CatPregunta;
import net.amentum.niomedic.expediente.views.CatPreguntaView;
import org.springframework.stereotype.Component;

@Component
public class CatPreguntasConverter {

    public CatPregunta toEntity(CatPreguntaView view) {
        CatPregunta entity = new CatPregunta();
        entity.setIdPregunta(view.getIdPregunta());
        entity.setPregunta(view.getPregunta());
        entity.setIdCuestionario(view.getIdCuestionario());
        entity.setSort(view.getSort());
        entity.setTipoPregunta(view.getTipoPregunta());
        return entity;
    }

    public CatPreguntaView toView(CatPregunta entity) {
        CatPreguntaView view = new CatPreguntaView();
        view.setIdPregunta(entity.getIdPregunta());
        view.setIdCuestionario(entity.getIdCuestionario());
        view.setPregunta(entity.getPregunta());
        view.setCreatedDate(entity.getCreatedDate());
        view.setSort(entity.getSort());
        view.setTipoPregunta(entity.getTipoPregunta());
        view.setActive(entity.getActive());
        return view;
    }

}
