package net.amentum.niomedic.expediente.converter.historia_clinica_general.personales_no_patologicos;

import net.amentum.niomedic.expediente.model.historia_clinica.personales_no_patologicos.Mascotas;
import net.amentum.niomedic.expediente.views.historia_clinica_general.personales_no_patologicos.MascotasView;
import org.springframework.stereotype.Component;

@Component
public class MascotasConverter {

    public Mascotas toEntity(MascotasView view) {
        Mascotas entity = new Mascotas();
        entity.setIdMascotas(view.getIdMascotas());
        entity.setIdHistoriaClinica(view.getIdHistoriaClinica());
        if(view.getMascotasSi())
            entity.setMascotas(true);
        else
            entity.setMascotas(false);
        entity.setPerro(view.getPerro());
        entity.setGato(view.getGato());
        entity.setAve(view.getAve());
        entity.setConejo(view.getConejo());
        entity.setTortuga(view.getTortuga());
        entity.setReptil(view.getReptil());
        entity.setRoedor(view.getRoedor());
        entity.setAnimalCorral(view.getAnimalCorral());
        entity.setNivelConvivencia(view.getNivelConvivencia());
        entity.setComentarios(view.getComentarios());
        return entity;
    }


    public MascotasView toEntity(Mascotas entity) {
        MascotasView view = new MascotasView();
        view.setIdMascotas(entity.getIdMascotas());
        view.setIdHistoriaClinica(entity.getIdHistoriaClinica());
        if(entity.getMascotas()) {
            view.setMascotasSi(true);
            view.setMascotasNo(false);
        } else {
            view.setMascotasSi(false);
            view.setMascotasNo(true);
        }
        view.setPerro(entity.getPerro());
        view.setGato(entity.getGato());
        view.setAve(entity.getAve());
        view.setConejo(entity.getConejo());
        view.setTortuga(entity.getTortuga());
        view.setReptil(entity.getReptil());
        view.setRoedor(entity.getRoedor());
        view.setAnimalCorral(entity.getAnimalCorral());
        view.setNivelConvivencia(entity.getNivelConvivencia());
        view.setComentarios(entity.getComentarios());
        return view;
    }
}
