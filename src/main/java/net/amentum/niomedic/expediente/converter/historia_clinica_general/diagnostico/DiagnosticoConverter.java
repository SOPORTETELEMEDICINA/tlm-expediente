package net.amentum.niomedic.expediente.converter.historia_clinica_general.diagnostico;

import net.amentum.niomedic.expediente.converter.CatCie10Converter;
import net.amentum.niomedic.expediente.model.CatCie10;
import net.amentum.niomedic.expediente.model.historia_clinica.diagnostico.Diagnostico;
import net.amentum.niomedic.expediente.model.historia_clinica.diagnostico.DiagnosticoCie10;
import net.amentum.niomedic.expediente.persistence.CatCie10Repository;
import net.amentum.niomedic.expediente.views.CatCie10View;
import net.amentum.niomedic.expediente.views.historia_clinica_general.diagnostico.DiagnosticoHCGView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DiagnosticoConverter {

    @Autowired
    CatCie10Repository catRepository;

    @Autowired
    CatCie10Converter cie10Converter;

    public Diagnostico toEntity(DiagnosticoHCGView view) {
        Diagnostico entity = new Diagnostico();
        entity.setIdDiagnostico(view.getIdDiagnosticoHC());
        entity.setIdHistoriaClinica(view.getIdHistoriaClinica());
        entity.setComentarios(view.getComentarios());
        entity.setNotas(view.getNotas());
        entity.setDiagnosticoCie10List(toArrayEntities(entity, view.getLstCie10()));
        return entity;
    }

    private ArrayList<DiagnosticoCie10> toArrayEntities(Diagnostico entity, List<CatCie10View> list) {
        ArrayList<DiagnosticoCie10> arrayList = new ArrayList<>();
        for(CatCie10View id : list) {
            CatCie10 element = catRepository.findOne(id.getIdCie10());
            if(element != null) {
                DiagnosticoCie10 catEntity = new DiagnosticoCie10();
                catEntity.setDiagnostico(entity);
                catEntity.setCie10(element);
                arrayList.add(catEntity);
            }
        }
        return arrayList;
    }

    public DiagnosticoHCGView toView(Diagnostico entity) {
        DiagnosticoHCGView view = new DiagnosticoHCGView();
        view.setIdDiagnosticoHC(entity.getIdDiagnostico());
        view.setIdHistoriaClinica(entity.getIdHistoriaClinica());
        view.setComentarios(entity.getComentarios());
        view.setNotas(entity.getNotas());
        view.setLstCie10(toArrayView(entity.getDiagnosticoCie10List()));
        return view;
    }

    private ArrayList<CatCie10View> toArrayView(List<DiagnosticoCie10> cie10List) {
        ArrayList<CatCie10View> arrayList = new ArrayList<>();
        for(DiagnosticoCie10 element : cie10List)
            arrayList.add(cie10Converter.toView(element.getCie10(), false));
        return arrayList;
    }

}
