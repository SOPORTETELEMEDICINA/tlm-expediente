package net.amentum.niomedic.expediente.converter.historia_clinica_general.interrogatorio_aparatos;

import net.amentum.niomedic.expediente.model.historia_clinica.interrogatorio_aparatos.InterrogatorioHematologico;
import net.amentum.niomedic.expediente.views.historia_clinica_general.interrogatorio_aparatos.InterrogatorioHematologicoView;
import org.springframework.stereotype.Component;

@Component
public class InterrogatorioHematologicoConverter {

    public InterrogatorioHematologico toEntity(InterrogatorioHematologicoView view) {
        InterrogatorioHematologico entity = new InterrogatorioHematologico();
        entity.setIdInterrogatorio(view.getIdInterrogatorio());
        entity.setIdHistoriaClinica(view.getIdHistoriaClinica());
        entity.setAnemia(view.getAnemia());
        entity.setAnemiaTiempoEvol(view.getAnemiaTiempoEvol());
        entity.setAnemiaCaracteristicas(view.getAnemiaCaracteristicas());
        entity.setSangrados(view.getSangrados());
        entity.setSangradosLocalizacion(view.getSangradosLocalizacion());
        entity.setSangradosTiempoEvol(view.getSangradosTiempoEvol());
        entity.setSangradosCaracteristicas(view.getSangradosCaracteristicas());
        entity.setCoagulos(view.getCoagulos());
        entity.setCoagulosTiempoEvol(view.getCoagulosTiempoEvol());
        entity.setCoagulosCaracteristicas(view.getCoagulosCaracteristicas());
        entity.setHematomas(view.getHematomas());
        entity.setHematomasLocalizacion(view.getHematomasLocalizacion());
        entity.setHematomasTiempoEvol(view.getHematomasTiempoEvol());
        entity.setHematomasCaracteristicas(view.getHematomasCaracteristicas());
        entity.setComentarios(view.getComentarios());
        return entity;
    }

    public InterrogatorioHematologicoView toView(InterrogatorioHematologico entity) {
        InterrogatorioHematologicoView view = new InterrogatorioHematologicoView();
        view.setIdInterrogatorio(entity.getIdInterrogatorio());
        view.setIdHistoriaClinica(entity.getIdHistoriaClinica());
        view.setAnemia(entity.getAnemia());
        view.setAnemiaTiempoEvol(entity.getAnemiaTiempoEvol());
        view.setAnemiaCaracteristicas(entity.getAnemiaCaracteristicas());
        view.setSangrados(entity.getSangrados());
        view.setSangradosLocalizacion(entity.getSangradosLocalizacion());
        view.setSangradosTiempoEvol(entity.getSangradosTiempoEvol());
        view.setSangradosCaracteristicas(entity.getSangradosCaracteristicas());
        view.setCoagulos(entity.getCoagulos());
        view.setCoagulosTiempoEvol(entity.getCoagulosTiempoEvol());
        view.setCoagulosCaracteristicas(entity.getCoagulosCaracteristicas());
        view.setHematomas(entity.getHematomas());
        view.setHematomasLocalizacion(entity.getHematomasLocalizacion());
        view.setHematomasTiempoEvol(entity.getHematomasTiempoEvol());
        view.setHematomasCaracteristicas(entity.getHematomasCaracteristicas());
        view.setComentarios(entity.getComentarios());
        return view;
    }

}
