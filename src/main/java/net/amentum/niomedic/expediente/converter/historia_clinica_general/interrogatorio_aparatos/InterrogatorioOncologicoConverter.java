package net.amentum.niomedic.expediente.converter.historia_clinica_general.interrogatorio_aparatos;

import net.amentum.niomedic.expediente.model.historia_clinica.interrogatorio_aparatos.InterrogatorioOncologico;
import net.amentum.niomedic.expediente.views.historia_clinica_general.interrogatorio_aparatos.InterrogatorioOncologicoView;
import org.springframework.stereotype.Component;

@Component
public class InterrogatorioOncologicoConverter {

    public InterrogatorioOncologico toEntity(InterrogatorioOncologicoView view) {
        InterrogatorioOncologico entity = new InterrogatorioOncologico();
        entity.setIdInterrogatorio(view.getIdInterrogatorio());
        entity.setIdHistoriaClinica(view.getIdHistoriaClinica());
        entity.setNeoplasias(view.getNeoplasias());
        entity.setNeoplasiasLocalizacion(view.getNeoplasiasLocalizacion());
        entity.setNeoplasiasTipo(view.getNeoplasiasTipo());
        entity.setNeoplasiasTiempoEvol(view.getNeoplasiasTiempoEvol());
        entity.setNeoplasiasTratamiento(view.getNeoplasiasTratamiento());
        entity.setNeoplasiasComplicaciones(view.getNeoplasiasComplicaciones());
        entity.setPronostico(view.getPronostico());
        entity.setDolorLocalizacion(view.getDolorLocalizacion());
        entity.setDolorOncologico(view.getDolorOncologico());
        entity.setDolorPorEnfermedad(view.getDolorPorEnfermedad());
        entity.setDolorPorCompresion(view.getDolorPorCompresion());
        entity.setDolorOtro(view.getDolorOtro());
        entity.setDolorEvn(view.getDolorEVN());
        entity.setEstadio(view.getEstadio());
        entity.setConociminetoPatologia(view.getConociminetoPatologia());
        entity.setEnfermedadesInmunoCuales(view.getEnfermedadesInmunoCuales());
        entity.setEnfermedadesInmunoTiempoEvol(view.getEnfermedadesInmunoTiempoEvol());
        entity.setEnfermedadesInmunoTratamiento(view.getEnfermedadesInmunoTratamiento());
        entity.setComentarios(view.getComentarios());
        return entity;
    }

    public InterrogatorioOncologicoView toView(InterrogatorioOncologico entity) {
        InterrogatorioOncologicoView view = new InterrogatorioOncologicoView();
        view.setIdInterrogatorio(entity.getIdInterrogatorio());
        view.setIdHistoriaClinica(entity.getIdHistoriaClinica());
        view.setNeoplasias(entity.getNeoplasias());
        view.setNeoplasiasLocalizacion(entity.getNeoplasiasLocalizacion());
        view.setNeoplasiasTipo(entity.getNeoplasiasTipo());
        view.setNeoplasiasTiempoEvol(entity.getNeoplasiasTiempoEvol());
        view.setNeoplasiasTratamiento(entity.getNeoplasiasTratamiento());
        view.setNeoplasiasComplicaciones(entity.getNeoplasiasComplicaciones());
        view.setPronostico(entity.getPronostico());
        view.setDolorLocalizacion(entity.getDolorLocalizacion());
        view.setDolorOncologico(entity.getDolorOncologico());
        view.setDolorPorEnfermedad(entity.getDolorPorEnfermedad());
        view.setDolorPorCompresion(entity.getDolorPorCompresion());
        view.setDolorOtro(entity.getDolorOtro());
        view.setDolorEVN(entity.getDolorEvn());
        view.setEstadio(entity.getEstadio());
        view.setConociminetoPatologia(entity.getConociminetoPatologia());
        view.setEnfermedadesInmunoCuales(entity.getEnfermedadesInmunoCuales());
        view.setEnfermedadesInmunoTiempoEvol(entity.getEnfermedadesInmunoTiempoEvol());
        view.setEnfermedadesInmunoTratamiento(entity.getEnfermedadesInmunoTratamiento());
        view.setComentarios(entity.getComentarios());
        return view;
    }

}
