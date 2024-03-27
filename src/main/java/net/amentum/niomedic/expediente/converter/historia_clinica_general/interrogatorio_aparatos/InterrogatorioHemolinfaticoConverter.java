package net.amentum.niomedic.expediente.converter.historia_clinica_general.interrogatorio_aparatos;

import net.amentum.niomedic.expediente.model.historia_clinica.interrogatorio_aparatos.InterrogatorioHemolinfatico;
import net.amentum.niomedic.expediente.views.historia_clinica_general.interrogatorio_aparatos.InterrogatorioHemolinfaticoView;
import org.springframework.stereotype.Component;

@Component
public class InterrogatorioHemolinfaticoConverter {

    public InterrogatorioHemolinfatico toEntity(InterrogatorioHemolinfaticoView view) {
        InterrogatorioHemolinfatico entity = new InterrogatorioHemolinfatico();
        entity.setIdInterrogatorio(view.getIdInterrogatorio());
        entity.setIdHistoriaClinica(view.getIdHistoriaClinica());
        entity.setHemoAsintomatico(view.getHemoAsintomatico());
        entity.setNodulos(view.getNodulos());
        entity.setNodLocalizacion(view.getNodLocalizacion());
        entity.setPetequias(view.getPetequias());
        entity.setAstenia(view.getAstenia());
        entity.setAdinamia(view.getAdinamia());
        entity.setComentarios(view.getComentarios());
        return entity;
    }

    public InterrogatorioHemolinfaticoView toView(InterrogatorioHemolinfatico entity) {
        InterrogatorioHemolinfaticoView view = new InterrogatorioHemolinfaticoView();
        view.setIdInterrogatorio(entity.getIdInterrogatorio());
        view.setIdHistoriaClinica(entity.getIdHistoriaClinica());
        view.setHemoAsintomatico(entity.getHemoAsintomatico());
        view.setNodulos(entity.getNodulos());
        view.setNodLocalizacion(entity.getNodLocalizacion());
        view.setPetequias(entity.getPetequias());
        view.setAstenia(entity.getAstenia());
        view.setAdinamia(entity.getAdinamia());
        view.setComentarios(entity.getComentarios());
        return view;
    }


}
