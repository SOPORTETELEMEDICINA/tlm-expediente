package net.amentum.niomedic.expediente.converter.historia_clinica_general.personales_no_patologicos;

import net.amentum.niomedic.expediente.model.historia_clinica.personales_no_patologicos.TablaLaboral;
import net.amentum.niomedic.expediente.views.historia_clinica_general.personales_no_patologicos.TablaLaboralView;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;

@Component
public class TablaLaboralConverter {

    public TablaLaboral toEntity(TablaLaboralView view) {
        TablaLaboral entity = new TablaLaboral();
        entity.setIdTablaLaboral(view.getIdTablaLaboral());
        entity.setIdHistoriaClinica(view.getIdHistoriaClinica());
        entity.setAgentesQuimicos(view.getAgentesQuimicos());
        entity.setPeriodo(view.getAntiguedad());
        entity.setPuesto(view.getPuesto());
        entity.setEmpresa(view.getEmpresa());
        return entity;
    }

    public ArrayList<TablaLaboral> toArrayEntities(Collection<TablaLaboralView> viewList) {
        ArrayList<TablaLaboral> tablaLaboralArrayList = new ArrayList<>();
        for(TablaLaboralView element : viewList)
            tablaLaboralArrayList.add(toEntity(element));
        return tablaLaboralArrayList;
    }

    public TablaLaboralView toView(TablaLaboral entity) {
        TablaLaboralView view = new TablaLaboralView();
        view.setIdTablaLaboral(entity.getIdTablaLaboral());
        view.setIdHistoriaClinica(entity.getIdHistoriaClinica());
        view.setAgentesQuimicos(entity.getAgentesQuimicos());
        view.setAntiguedad(entity.getPeriodo());
        view.setPuesto(entity.getPuesto());
        view.setEmpresa(entity.getEmpresa());
        return view;
    }

    public ArrayList<TablaLaboralView> toArrayView(Collection<TablaLaboral> entityList) {
        ArrayList<TablaLaboralView> tablaLaboralArrayList = new ArrayList<>();
        for(TablaLaboral element : entityList)
            tablaLaboralArrayList.add(toView(element));
        return tablaLaboralArrayList;
    }

}
