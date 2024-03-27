package net.amentum.niomedic.expediente.converter.historia_clinica_general.personales_no_patologicos;

import net.amentum.niomedic.expediente.model.historia_clinica.personales_no_patologicos.Consumo;
import net.amentum.niomedic.expediente.views.historia_clinica_general.personales_no_patologicos.ConsumoView;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;

@Component
public class ConsumoConverter {

    public Consumo toEntity(ConsumoView view) {
        Consumo entity = new Consumo();
        entity.setIdConsumo(view.getIdConsumo());
        entity.setIdHistoriaClinica(view.getIdHistoriaClinica());
        entity.setDroga(view.getDroga());
        entity.setNombre(view.getNombreDroga());
        entity.setFrecuencia(view.getFrecuencia());
        entity.setCantidad(view.getInputCantidad());
        entity.setEdadInicio(view.getEdadInicio());
        entity.setEdadAbandono(view.getEdadAbandono());
        return entity;
    }

    public ArrayList<Consumo> toArrayEntities(Collection<ConsumoView> viewList) {
        ArrayList<Consumo> consumoArrayList = new ArrayList<>();
        for(ConsumoView element : viewList)
            consumoArrayList.add(toEntity(element));
        return consumoArrayList;
    }

    public ConsumoView toView(Consumo entity) {
        ConsumoView view = new ConsumoView();
        view.setIdConsumo(entity.getIdConsumo());
        view.setIdHistoriaClinica(entity.getIdHistoriaClinica());
        view.setDroga(entity.getDroga());
        view.setNombreDroga(entity.getNombre());
        view.setFrecuencia(entity.getFrecuencia());
        view.setInputCantidad(entity.getCantidad());
        view.setEdadInicio(entity.getEdadInicio());
        view.setEdadAbandono(entity.getEdadAbandono());
        return view;
    }

    public ArrayList<ConsumoView> toArrayViews(Collection<Consumo> entitiesList) {
        ArrayList<ConsumoView> consumoArrayList = new ArrayList<>();
        for(Consumo element : entitiesList)
            consumoArrayList.add(toView(element));
        return consumoArrayList;
    }

}
