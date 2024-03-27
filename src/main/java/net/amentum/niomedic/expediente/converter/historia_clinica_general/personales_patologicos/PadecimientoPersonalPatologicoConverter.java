package net.amentum.niomedic.expediente.converter.historia_clinica_general.personales_patologicos;

import net.amentum.niomedic.expediente.model.historia_clinica.personales_patologicos.PadecimientoPersonalPatologico;
import net.amentum.niomedic.expediente.views.historia_clinica_general.personales_patologicos.PadecimientoPersonalPatologicoView;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;

@Component
public class PadecimientoPersonalPatologicoConverter {

    public PadecimientoPersonalPatologico toEntity(PadecimientoPersonalPatologicoView view) {
        PadecimientoPersonalPatologico entity = new PadecimientoPersonalPatologico();
        entity.setId(view.getIdPadecimiento());
        entity.setIdHistoriaClinica(view.getIdHistoriaClinica());
        entity.setTieneEnfermedad(view.getTieneEnfermedad());
        entity.setEnfermedad(view.getEnfermedad());
        entity.setTiempoEvolucion(view.getTiempoEvolucion());
        entity.setTratamiento(view.getTratamiento());
        entity.setNotas(view.getNotas());
        return entity;
    }

    public ArrayList<PadecimientoPersonalPatologico> toArrayEntity(Collection<PadecimientoPersonalPatologicoView> viewList) {
        ArrayList<PadecimientoPersonalPatologico> arrayList = new ArrayList<>();
        for(PadecimientoPersonalPatologicoView element : viewList)
            arrayList.add(toEntity(element));
        return arrayList;
    }


    public PadecimientoPersonalPatologicoView toView(PadecimientoPersonalPatologico entity) {
        PadecimientoPersonalPatologicoView view = new PadecimientoPersonalPatologicoView();
        view.setIdPadecimiento(entity.getId());
        view.setIdHistoriaClinica(entity.getIdHistoriaClinica());
        view.setTieneEnfermedad(entity.getTieneEnfermedad());
        view.setEnfermedad(entity.getEnfermedad());
        view.setTiempoEvolucion(entity.getTiempoEvolucion());
        view.setTratamiento(entity.getTratamiento());
        view.setNotas(entity.getNotas());
        return view;
    }

    public ArrayList<PadecimientoPersonalPatologicoView> toArrayView(Collection<PadecimientoPersonalPatologico> entityList) {
        ArrayList<PadecimientoPersonalPatologicoView> arrayList = new ArrayList<>();
        for(PadecimientoPersonalPatologico element : entityList)
            arrayList.add(toView(element));
        return arrayList;
    }
}
