package net.amentum.niomedic.expediente.converter;

import net.amentum.niomedic.expediente.model.CatCuestionarioPaciente;
import net.amentum.niomedic.expediente.views.CatCuestionarioPacienteView;
import org.springframework.stereotype.Component;

@Component
public class CatCuestionarioPacienteConverter {

    public CatCuestionarioPaciente toEntity(CatCuestionarioPacienteView view) {
        CatCuestionarioPaciente entity = new CatCuestionarioPaciente();
        entity.setIdCatCuestionarioPaciente(view.getIdCatCuestionarioPaciente());
        entity.setIdPaciente(view.getIdPaciente());
        entity.setStatus(view.getStatus());
        entity.setIdCatCuestionario(view.getIdCatCuestionario());
        return entity;
    }

    public CatCuestionarioPacienteView toView(CatCuestionarioPaciente entity) {
        CatCuestionarioPacienteView view = new CatCuestionarioPacienteView();
        view.setIdCatCuestionarioPaciente(entity.getIdCatCuestionarioPaciente());
        view.setIdPaciente(entity.getIdPaciente());
        view.setStatus(entity.getStatus());
        view.setIdCatCuestionario(entity.getIdCatCuestionario());
        return view;
    }
}
