package net.amentum.niomedic.expediente.converter;

import net.amentum.niomedic.expediente.model.CatCuestionario;
import net.amentum.niomedic.expediente.views.CatCuestionarioView;
import org.springframework.stereotype.Component;

@Component
public class CatCuestionariosConverter {

    public CatCuestionario toEntity(CatCuestionarioView view) {
        CatCuestionario entity = new CatCuestionario();
        entity.setIdCuestionario(view.getIdCuestionario());
        entity.setNombre(view.getNombre());
        entity.setCreadoPor(view.getCreadoPor());
        entity.setSort(view.getSort());
        return entity;
    }

    public CatCuestionarioView toView(CatCuestionario entity) {
        CatCuestionarioView view = new CatCuestionarioView();
        view.setIdCuestionario(entity.getIdCuestionario());
        view.setNombre(entity.getNombre());
        view.setCreadoPor(entity.getCreadoPor());
        view.setCreatedDate(entity.getCreatedDate());
        view.setActive(entity.getActive());
        view.setSort(entity.getSort());
        return view;
    }

}
