package net.amentum.niomedic.expediente.converter.historia_clinica_general.tratamiento;

import net.amentum.niomedic.expediente.converter.CatCie9Converter;
import net.amentum.niomedic.expediente.model.CatCie9;
import net.amentum.niomedic.expediente.model.historia_clinica.tratamiento.HCTratamiento;
import net.amentum.niomedic.expediente.model.historia_clinica.tratamiento.TratamientoCie9;
import net.amentum.niomedic.expediente.persistence.CatCie9Repository;
import net.amentum.niomedic.expediente.views.CatCie9View;
import net.amentum.niomedic.expediente.views.historia_clinica_general.tratamiento.HCTratamientoView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class HCTratamientoConverter {

    @Autowired
    CatCie9Repository catRepository;

    @Autowired
    CatCie9Converter cie9Converter;

    public HCTratamiento toEntity(HCTratamientoView view) {
        HCTratamiento entity = new HCTratamiento();
        entity.setIdTratamiento(view.getIdTratamiento());
        entity.setIdHistoriaClinica(view.getIdHistoriaClinica());
        entity.setComentarios(view.getComentarios());
        entity.setNotas(view.getNotas());
        entity.setTratamientoCie9s(toArrayEntities(entity, view.getLstCie9()));
        return entity;
    }

    private ArrayList<TratamientoCie9> toArrayEntities(HCTratamiento entity, List<CatCie9View> cie9Views) {
        ArrayList<TratamientoCie9> arrayList = new ArrayList<>();
        for(CatCie9View cie9View : cie9Views) {
            CatCie9 element = catRepository.findOne(cie9View.getIdCie9());
            if(element != null) {
                TratamientoCie9 catEntity = new TratamientoCie9();
                catEntity.setTratamiento(entity);
                catEntity.setCatCie9(element);
                arrayList.add(catEntity);
            }
        }
        return arrayList;
    }

    public HCTratamientoView toView(HCTratamiento entity) {
        HCTratamientoView view = new HCTratamientoView();
        view.setIdTratamiento(entity.getIdTratamiento());
        view.setIdHistoriaClinica(entity.getIdHistoriaClinica());
        view.setComentarios(entity.getComentarios());
        view.setNotas(entity.getNotas());
        view.setLstCie9(toArrayView(entity.getTratamientoCie9s()));
        return view;
    }

    private ArrayList<CatCie9View> toArrayView(List<TratamientoCie9> tratamientoCie9s) {
        ArrayList<CatCie9View> arrayList = new ArrayList<>();
        for(TratamientoCie9 element : tratamientoCie9s)
            arrayList.add(cie9Converter.toView(element.getCatCie9(), false));
        return arrayList;
    }

}
