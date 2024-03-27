package net.amentum.niomedic.expediente.converter.historia_clinica_general.interrogatorio_aparatos;

import net.amentum.niomedic.expediente.model.historia_clinica.interrogatorio_aparatos.InterrogatorioUrinario;
import net.amentum.niomedic.expediente.views.historia_clinica_general.interrogatorio_aparatos.InterrogatorioUrinarioView;
import org.springframework.stereotype.Component;

@Component
public class InterrogatorioUrinarioConverter {

    public InterrogatorioUrinario toEntity(InterrogatorioUrinarioView view) {
        InterrogatorioUrinario entity = new InterrogatorioUrinario();
        entity.setIdInterrogatorio(view.getIdInterrogatorio());
        entity.setIdHistoriaClinica(view.getIdHistoriaClinica());
        entity.setDisuria(view.getDisuria());
        entity.setHematuria(view.getHematuria());
        entity.setPoliuria(view.getPoliuria());
        entity.setNicturia(view.getNicturia());
        entity.setIncontinencia(view.getIncontinencia());
        entity.setComentarios(view.getComentarios());
        return entity;
    }

    public InterrogatorioUrinarioView toView(InterrogatorioUrinario entity) {
        InterrogatorioUrinarioView view = new InterrogatorioUrinarioView();
        view.setIdInterrogatorio(entity.getIdInterrogatorio());
        view.setIdHistoriaClinica(entity.getIdHistoriaClinica());
        view.setDisuria(entity.getDisuria());
        view.setHematuria(entity.getHematuria());
        view.setPoliuria(entity.getPoliuria());
        view.setNicturia(entity.getNicturia());
        view.setIncontinencia(entity.getIncontinencia());
        view.setComentarios(entity.getComentarios());
        return view;
    }

}
