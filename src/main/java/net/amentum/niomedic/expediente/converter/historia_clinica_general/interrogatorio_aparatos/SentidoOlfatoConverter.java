package net.amentum.niomedic.expediente.converter.historia_clinica_general.interrogatorio_aparatos;

import net.amentum.niomedic.expediente.model.historia_clinica.interrogatorio_aparatos.SentidoOlfato;
import net.amentum.niomedic.expediente.views.historia_clinica_general.interrogatorio_aparatos.SentidoOlfatoView;
import org.springframework.stereotype.Component;

@Component
public class SentidoOlfatoConverter {

    public SentidoOlfato toEntity(SentidoOlfatoView view) {
        SentidoOlfato entity = new SentidoOlfato();
        entity.setIdInterrogatorio(view.getIdInterrogatorio());
        entity.setIdHistoriaClinica(view.getIdHistoriaClinica());
        entity.setAlterado(view.getAlterado());
        entity.setNormal(view.getNormal());
        entity.setTiempoEvolucion(view.getTiempoEvol());
        entity.setAnosmia(view.getAnosmia());
        entity.setCacosomia(view.getCacosomia());
        entity.setHiposmia(view.getHiposmia());
        entity.setParosmia(view.getParosmia());
        entity.setCongestion(view.getCongestion());
        entity.setRinorrea(view.getRinorrea());
        entity.setEpitaxis(view.getEpitaxis());
        entity.setPruritoNasal(view.getPruritoNasal());
        entity.setComentarios(view.getComentarios());
        return entity;
    }

    public SentidoOlfatoView toView(SentidoOlfato entity) {
        SentidoOlfatoView view = new SentidoOlfatoView();
        view.setIdInterrogatorio(entity.getIdInterrogatorio());
        view.setIdHistoriaClinica(entity.getIdHistoriaClinica());
        view.setAlterado(entity.getAlterado());
        view.setNormal(entity.getNormal());
        view.setTiempoEvol(entity.getTiempoEvolucion());
        view.setAnosmia(entity.getAnosmia());
        view.setCacosomia(entity.getCacosomia());
        view.setHiposmia(entity.getHiposmia());
        view.setParosmia(entity.getParosmia());
        view.setCongestion(entity.getCongestion());
        view.setRinorrea(entity.getRinorrea());
        view.setEpitaxis(entity.getEpitaxis());
        view.setPruritoNasal(entity.getPruritoNasal());
        view.setComentarios(entity.getComentarios());
        return view;
    }

}
