package net.amentum.niomedic.expediente.converter.historia_clinica_general.interrogatorio_aparatos;

import net.amentum.niomedic.expediente.model.historia_clinica.interrogatorio_aparatos.SentidoGusto;
import net.amentum.niomedic.expediente.model.historia_clinica.interrogatorio_aparatos.SentidoTacto;
import net.amentum.niomedic.expediente.views.historia_clinica_general.interrogatorio_aparatos.SentidoTactoView;
import org.springframework.stereotype.Component;

@Component
public class SentidoTactoConverter {

    public SentidoTacto toEntity(SentidoTactoView view) {
        SentidoTacto entity = new SentidoTacto();
        entity.setIdInterrogatorio(view.getIdInterrogatorio());
        entity.setIdHistoriaClinica(view.getIdHistoriaClinica());
        entity.setNormal(view.getNormal());
        entity.setHiposensible(view.getHiposensible());
        entity.setHiposensibleLocalizacion(view.getHiposensibleLocalizacion());
        entity.setHipersensible(view.getHipersensible());
        entity.setHipersensibleLocalizacion(view.getHiposensibleLocalizacion());
        entity.setLocalizacion(view.getLocalizacion());
        entity.setTiempoEvolucion(view.getTactoTiempoEvol());
        entity.setEsterognosia(view.getEsterognosia());
        entity.setEsterogInput(view.getEsterogInput());
        entity.setComentarios(view.getComentarios());
        return entity;
    }


    public SentidoTactoView toView(SentidoTacto entity) {
        SentidoTactoView view = new SentidoTactoView();
        view.setIdInterrogatorio(entity.getIdInterrogatorio());
        view.setIdHistoriaClinica(entity.getIdHistoriaClinica());
        view.setNormal(entity.getNormal());
        view.setHiposensible(entity.getHiposensible());
        view.setHiposensibleLocalizacion(entity.getHiposensibleLocalizacion());
        view.setHipersensible(entity.getHipersensible());
        view.setHipersensibleLocalizacion(entity.getHiposensibleLocalizacion());
        view.setLocalizacion(entity.getLocalizacion());
        view.setTactoTiempoEvol(entity.getTiempoEvolucion());
        view.setEsterognosia(entity.getEsterognosia());
        view.setEsterogInput(entity.getEsterogInput());
        view.setComentarios(entity.getComentarios());
        return view;
    }

}
