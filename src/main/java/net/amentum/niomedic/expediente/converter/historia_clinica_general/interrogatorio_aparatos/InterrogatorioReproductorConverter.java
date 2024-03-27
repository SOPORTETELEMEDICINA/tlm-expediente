package net.amentum.niomedic.expediente.converter.historia_clinica_general.interrogatorio_aparatos;

import net.amentum.niomedic.expediente.model.historia_clinica.interrogatorio_aparatos.InterrogatorioReproductor;
import net.amentum.niomedic.expediente.views.historia_clinica_general.interrogatorio_aparatos.InterrogatorioReproductorView;
import org.springframework.stereotype.Component;

@Component
public class InterrogatorioReproductorConverter {

    public InterrogatorioReproductor toEntity(InterrogatorioReproductorView view) {
        InterrogatorioReproductor entity = new InterrogatorioReproductor();
        entity.setIdInterrogatorio(view.getIdInterrogatorio());
        entity.setIdHistoriaClinica(view.getIdHistoriaClinica());
        entity.setDesechos(view.getDesechos());
        entity.setDolorGenital(view.getDolorGenital());
        entity.setPoliuria(view.getPoliuria());
        entity.setComentarios(view.getComentarios());
        return entity;
    }

    public InterrogatorioReproductorView toView(InterrogatorioReproductor entity) {
        InterrogatorioReproductorView view = new InterrogatorioReproductorView();
        view.setIdInterrogatorio(entity.getIdInterrogatorio());
        view.setIdHistoriaClinica(entity.getIdHistoriaClinica());
        view.setDesechos(entity.getDesechos());
        view.setDolorGenital(entity.getDolorGenital());
        view.setPoliuria(entity.getPoliuria());
        view.setComentarios(entity.getComentarios());
        return view;
    }

}
