package net.amentum.niomedic.expediente.converter.historia_clinica_general.interrogatorio_aparatos;

import net.amentum.niomedic.expediente.model.historia_clinica.interrogatorio_aparatos.SentidoGusto;
import net.amentum.niomedic.expediente.views.historia_clinica_general.interrogatorio_aparatos.SentidoGustoView;
import org.springframework.stereotype.Component;

@Component
public class SentidoGustoConverter {

    public SentidoGusto toEntity(SentidoGustoView view) {
        SentidoGusto entity = new SentidoGusto();
        entity.setIdInterrogatorio(view.getIdInterrogatorio());
        entity.setIdHistoriaClinica(view.getIdHistoriaClinica());
        entity.setNormal(view.getNormal());
        entity.setAlterado(view.getAlterado());
        entity.setArdosa(view.getArdosa());
        entity.setAftas(view.getAftas());
        entity.setAgeusia(view.getAgeusia());
        entity.setHipoageusia(view.getHipoageusia());
        entity.setHalitosis(view.getHalitosis());
        entity.setGingivitis(view.getGingivitis());
        entity.setGingivorrea(view.getGingivorrea());
        entity.setGingivorragia(view.getGingivorragia());
        entity.setGlositis(view.getGlositis());
        entity.setTiempoEvolucion(view.getTiempoEvol());
        entity.setComentarios(view.getComentarios());
        return entity;
    }


    public SentidoGustoView toView(SentidoGusto entity) {
        SentidoGustoView view = new SentidoGustoView();
        view.setIdInterrogatorio(entity.getIdInterrogatorio());
        view.setIdHistoriaClinica(entity.getIdHistoriaClinica());
        view.setNormal(entity.getNormal());
        view.setAlterado(entity.getAlterado());
        view.setArdosa(entity.getArdosa());
        view.setAftas(entity.getAftas());
        view.setAgeusia(entity.getAgeusia());
        view.setHipoageusia(entity.getHipoageusia());
        view.setHalitosis(entity.getHalitosis());
        view.setGingivitis(entity.getGingivitis());
        view.setGingivorrea(entity.getGingivorrea());
        view.setGingivorragia(entity.getGingivorragia());
        view.setGlositis(entity.getGlositis());
        view.setTiempoEvol(entity.getTiempoEvolucion());
        view.setComentarios(entity.getComentarios());
        return view;
    }
}
