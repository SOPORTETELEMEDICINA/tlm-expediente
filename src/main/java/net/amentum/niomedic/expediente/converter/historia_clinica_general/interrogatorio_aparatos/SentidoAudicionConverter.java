package net.amentum.niomedic.expediente.converter.historia_clinica_general.interrogatorio_aparatos;

import net.amentum.niomedic.expediente.model.historia_clinica.interrogatorio_aparatos.SentidoAudicion;
import net.amentum.niomedic.expediente.views.historia_clinica_general.interrogatorio_aparatos.SentidoAudicionView;
import org.springframework.stereotype.Component;

@Component
public class SentidoAudicionConverter {

    public SentidoAudicion toEntity(SentidoAudicionView view) {
        SentidoAudicion entity = new SentidoAudicion();
        entity.setIdInterrogatorio(view.getIdInterrogatorio());
        entity.setIdHistoriaClinica(view.getIdHistoriaClinica());
        entity.setAudicionNormal(view.getAudicionNormal());
        entity.setAudicionNormalOido(view.getAudicionNormalOido());
        entity.setAudicionHipo(view.getAudicionHipo());
        entity.setAudicionHipoOido(view.getAudicionHipoOido());
        entity.setAudicionHiper(view.getAudicionHiper());
        entity.setAudicionHiperOido(view.getAudicionHiperOido());
        entity.setAudicionAnacusia(view.getAudicionAnacusia());
        entity.setAudicioAnacusiaOido(view.getAudicionAnacusiaOido());
        entity.setAudicionOtalgia(view.getAudicionOtalgia());
        entity.setAudicionOtalgiaOido(view.getAudicionOtalgiaOido());
        entity.setAudicionTinitus(view.getAudicionTinitus());
        entity.setAudicionTinitusOido(view.getAudicionTinitusOido());
        entity.setSecrecionOtorrea(view.getSecrecionOtorrea());
        entity.setSecrecionOtorreaOido(view.getSecrecionOtorreaOido());
        entity.setSecrecionOtorraquia(view.getSecrecionOtorraquia());
        entity.setSecrecionOtorraquiaOido(view.getSecrecionOtorraquiaOido());
        entity.setSecrecionOtorragia(view.getSecrecionOtorragia());
        entity.setSecrecionOtorragiaOido(view.getSecrecionOtorragiaOido());
        entity.setSecrecionInput(view.getSecrecionInput());
        entity.setComentarios(view.getComentarios());
        return entity;
    }


    public SentidoAudicionView toView(SentidoAudicion entity) {
        SentidoAudicionView view = new SentidoAudicionView();
        view.setIdInterrogatorio(entity.getIdInterrogatorio());
        view.setIdHistoriaClinica(entity.getIdHistoriaClinica());
        view.setAudicionNormal(entity.getAudicionNormal());
        view.setAudicionNormalOido(entity.getAudicionNormalOido());
        view.setAudicionHipo(entity.getAudicionHipo());
        view.setAudicionHipoOido(entity.getAudicionHipoOido());
        view.setAudicionHiper(entity.getAudicionHiper());
        view.setAudicionHiperOido(entity.getAudicionHiperOido());
        view.setAudicionAnacusia(entity.getAudicionAnacusia());
        view.setAudicionAnacusiaOido(entity.getAudicioAnacusiaOido());
        view.setAudicionOtalgia(entity.getAudicionOtalgia());
        view.setAudicionOtalgiaOido(entity.getAudicionOtalgiaOido());
        view.setAudicionTinitus(entity.getAudicionTinitus());
        view.setAudicionTinitusOido(entity.getAudicionTinitusOido());
        view.setSecrecionOtorrea(entity.getSecrecionOtorrea());
        view.setSecrecionOtorreaOido(entity.getSecrecionOtorreaOido());
        view.setSecrecionOtorraquia(entity.getSecrecionOtorraquia());
        view.setSecrecionOtorraquiaOido(entity.getSecrecionOtorraquiaOido());
        view.setSecrecionOtorragia(entity.getSecrecionOtorragia());
        view.setSecrecionOtorragiaOido(entity.getSecrecionOtorragiaOido());
        view.setSecrecionInput(entity.getSecrecionInput());
        view.setComentarios(entity.getComentarios());
        return view;
    }

}
