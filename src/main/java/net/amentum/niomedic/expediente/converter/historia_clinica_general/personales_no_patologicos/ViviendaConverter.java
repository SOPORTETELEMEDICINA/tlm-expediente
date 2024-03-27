package net.amentum.niomedic.expediente.converter.historia_clinica_general.personales_no_patologicos;

import net.amentum.niomedic.expediente.model.historia_clinica.personales_no_patologicos.Vivienda;
import net.amentum.niomedic.expediente.views.historia_clinica_general.personales_no_patologicos.ViviendaView;
import org.springframework.stereotype.Component;

@Component
public class ViviendaConverter {

    public Vivienda toEntity(ViviendaView view) {
        Vivienda entity = new Vivienda();
        entity.setIdVivienda(view.getIdVivienda());
        entity.setIdHistoriaClinica(view.getIdHistoriaClinica());
        entity.setServicioAgua(view.getAguaPotable());
        entity.setServicioAlcantarillado(view.getAlcantarillado());
        entity.setServicioElectricidad(view.getElectricidad());
        entity.setServicioRecoleccionBasura(view.getRecoleccionBasura());
        entity.setPisoTierra(view.getPisoTierra());
        entity.setPisoAzulejo(view.getPisoAzulejo());
        entity.setPisoCemento(view.getPisoCemento());
        entity.setPisoOtro(view.getPisoOtro());
        entity.setTechoLadrillo(view.getTechoLadrillo());
        entity.setTechoEnjarradas(view.getTechoEnjarradas());
        entity.setTechoOtro(view.getTechoOtro());
        entity.setSinBanio(view.getSinBanio());
        entity.setBanio1(view.getBanio1());
        entity.setBanio2(view.getBanio2());
        entity.setBanioMas(view.getBanioMas());
        entity.setHabitaciones1(view.getHabitaciones1());
        entity.setHabitaciones2(view.getHabitaciones2());
        entity.setHabitacionesMas(view.getHabitacionesMas());
        entity.setCocinaConCarbon(view.getCocinarCarbon());
        entity.setCocinaConGas(view.getCocinarGas());
        entity.setCocinaConLenia(view.getCocinarLenia());
        if(view.getProductosToxicosSi())
            entity.setProductosToxicos(true);
        else
            entity.setProductosToxicos(false);
        entity.setProductosToxicosInput(view.getProductosToxicosInput());
        if(view.getFaunaNocivaNo())
            entity.setFaunaNociva(false);
        else
            entity.setFaunaNociva(true);
        entity.setFaunaAranias(view.getFaunaAranias());
        entity.setFaunasAlacranes(view.getFaunaAlacranes());
        entity.setFaunaSerpientes(view.getFaunaSerpiente());
        entity.setFaunaOtro(view.getFaunaOtro());
        entity.setFaunaOtroInput(view.getFaunaOtroTxt());
        return entity;
    }

    public ViviendaView toView(Vivienda entity) {
        ViviendaView view = new ViviendaView();
        view.setIdVivienda(entity.getIdVivienda());
        view.setIdHistoriaClinica(entity.getIdHistoriaClinica());
        view.setAguaPotable(entity.getServicioAgua());
        view.setAlcantarillado(entity.getServicioAlcantarillado());
        view.setElectricidad(entity.getServicioElectricidad());
        view.setRecoleccionBasura(entity.getServicioRecoleccionBasura());
        view.setPisoTierra(entity.getPisoTierra());
        view.setPisoAzulejo(entity.getPisoAzulejo());
        view.setPisoCemento(entity.getPisoCemento());
        view.setPisoOtro(entity.getPisoOtro());
        view.setTechoLadrillo(entity.getTechoLadrillo());
        view.setTechoEnjarradas(entity.getTechoEnjarradas());
        view.setTechoOtro(entity.getTechoOtro());
        view.setSinBanio(entity.getSinBanio());
        view.setBanio1(entity.getBanio1());
        view.setBanio2(entity.getBanio2());
        view.setBanioMas(entity.getBanioMas());
        view.setHabitaciones1(entity.getHabitaciones1());
        view.setHabitaciones2(entity.getHabitaciones2());
        view.setHabitacionesMas(entity.getHabitacionesMas());
        view.setCocinarCarbon(entity.getCocinaConCarbon());
        view.setCocinarGas(entity.getCocinaConGas());
        view.setCocinarLenia(entity.getCocinaConLenia());
        if(entity.getProductosToxicos()) {
            view.setProductosToxicosSi(true);
            view.setProductosToxicosNo(false);
        } else {
            view.setProductosToxicosSi(false);
            view.setProductosToxicosNo(true);
        }
        view.setProductosToxicosInput(entity.getProductosToxicosInput());
        view.setFaunaNocivaNo(entity.getFaunaNociva());
        view.setFaunaAranias(entity.getFaunaAranias());
        view.setFaunaAlacranes(entity.getFaunasAlacranes());
        view.setFaunaSerpiente(entity.getFaunaSerpientes());
        view.setFaunaOtro(entity.getFaunaOtro());
        view.setFaunaOtroTxt(entity.getFaunaOtroInput());
        return view;
    }

}
