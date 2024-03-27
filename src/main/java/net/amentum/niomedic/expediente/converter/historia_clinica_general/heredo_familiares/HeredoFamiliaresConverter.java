package net.amentum.niomedic.expediente.converter.historia_clinica_general.heredo_familiares;

import net.amentum.niomedic.expediente.model.historia_clinica.heredo_familiares.Enfermedades;
import net.amentum.niomedic.expediente.model.historia_clinica.heredo_familiares.HeredoFamiliares;
import net.amentum.niomedic.expediente.views.historia_clinica_general.heredo_familiares.HeredoFamiliaresView;
import net.amentum.niomedic.expediente.views.historia_clinica_general.heredo_familiares.HfEnfermedadesView;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class HeredoFamiliaresConverter {

    public HeredoFamiliares toEntity(HeredoFamiliaresView view) {
        HeredoFamiliares entity = new HeredoFamiliares();
        entity.setIdHeredoFamiliares(view.getIdHeredoFamiliares());
        entity.setIdHistoriaClinica(view.getIdHistoriaClinica());
        entity.setComentarios(view.getComentarios());
        entity.setNotas(view.getNotas());
        entity.setEnfermedadesList(toEntityList(view.getDatosTabla()));
        return entity;
    }

    public HeredoFamiliaresView toView(HeredoFamiliares entity) {
        HeredoFamiliaresView view = new HeredoFamiliaresView();
        view.setIdHeredoFamiliares(entity.getIdHeredoFamiliares());
        view.setIdHistoriaClinica(entity.getIdHistoriaClinica());
        view.setComentarios(entity.getComentarios());
        view.setNotas(entity.getNotas());
        view.setDatosTabla(toViewList(entity.getEnfermedadesList()));
        return view;
    }

    private List<Enfermedades> toEntityList(List<HfEnfermedadesView> enfermedadesViews) {
        List<Enfermedades> list = new ArrayList<>();
        for(HfEnfermedadesView element : enfermedadesViews)
            list.add(toEntity(element));
        return list;
    }

    private List<HfEnfermedadesView> toViewList(List<Enfermedades> enfermedadesList) {
        List<HfEnfermedadesView> list = new ArrayList<>();
        for(Enfermedades element : enfermedadesList)
            list.add(toView(element));
        return list;
    }

    private Enfermedades toEntity(HfEnfermedadesView view) {
        Enfermedades entity = new Enfermedades();
        entity.setIdHfEnfermedades(view.getIdHfEnfermedades());
        entity.setIdHistoriaClinica(view.getIdHistoriaClinica());
        entity.setTieneEnfermedad(view.getTieneEnfermedad());
        entity.setEnfermedad(view.getEnfermedad());
        entity.setPadre(view.getPadre());
        entity.setInfoPadre(view.getInfoPadre());
        entity.setMadre(view.getMadre());
        entity.setInfoMadre(view.getInfoMadre());
        entity.setHermanos(view.getHermanos());
        entity.setInfoHermanos(view.getInfoHermanos());
        entity.setAbuPaternos(view.getAbuPaternos());
        entity.setInfoAbuPaternos(view.getInfoAbuPaternos());
        entity.setAbuMaternos(view.getAbuMaternos());
        entity.setInfoAbuMaternos(view.getInfoAbuMaternos());
        entity.setTiosPaternos(view.getTiosPaternos());
        entity.setInfoTiosPaternos(view.getInfoTiosPaternos());
        entity.setTiosMaternos(view.getTiosMaternos());
        entity.setInfoTiosMaternos(view.getInfoTiosMaternos());
        return entity;
    }

    private HfEnfermedadesView toView(Enfermedades entity) {
        HfEnfermedadesView view = new HfEnfermedadesView();
        view.setIdHfEnfermedades(entity.getIdHfEnfermedades());
        view.setIdHistoriaClinica(entity.getIdHistoriaClinica());
        view.setTieneEnfermedad(entity.getTieneEnfermedad());
        view.setEnfermedad(entity.getEnfermedad());
        view.setPadre(entity.getPadre());
        view.setInfoPadre(entity.getInfoPadre());
        view.setMadre(entity.getMadre());
        view.setInfoMadre(entity.getInfoMadre());
        view.setHermanos(entity.getHermanos());
        view.setAbuPaternos(entity.getAbuPaternos());
        view.setInfoAbuPaternos(entity.getInfoAbuPaternos());
        view.setAbuMaternos(entity.getAbuMaternos());
        view.setInfoAbuMaternos(entity.getInfoAbuMaternos());
        view.setTiosPaternos(entity.getTiosPaternos());
        view.setInfoTiosPaternos(entity.getInfoTiosPaternos());
        view.setTiosMaternos(entity.getTiosMaternos());
        view.setInfoTiosMaternos(entity.getInfoTiosMaternos());
        return view;
    }
}
