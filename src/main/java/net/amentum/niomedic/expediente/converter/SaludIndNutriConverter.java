package net.amentum.niomedic.expediente.converter;

import net.amentum.niomedic.expediente.model.SaludIndNutri;
import net.amentum.niomedic.expediente.views.SaludIndNutriView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SaludIndNutriConverter {

    private final Logger logger = LoggerFactory.getLogger(SaludIndNutriConverter.class);

    public SaludIndNutri toEntity(SaludIndNutriView SaludIndNutriView, SaludIndNutri SaludIndNutri, Boolean update) {

        SaludIndNutri.setIdindic(SaludIndNutriView.getIdindic());
        SaludIndNutri.setMedidfk(SaludIndNutriView.getMedidfk());
        SaludIndNutri.setPacidfk(SaludIndNutriView.getPacidfk());
        SaludIndNutri.setPesoinicial(SaludIndNutriView.getPesoinicial());
        SaludIndNutri.setTallainicial(SaludIndNutriView.getTallainicial());
        SaludIndNutri.setP1hora(SaludIndNutriView.getP1hora());
        SaludIndNutri.setNutri1lu(SaludIndNutriView.getNutri1lu());
        SaludIndNutri.setNutri1ma(SaludIndNutriView.getNutri1ma());
        SaludIndNutri.setNutri1mi(SaludIndNutriView.getNutri1mi());
        SaludIndNutri.setNutri1ju(SaludIndNutriView.getNutri1ju());
        SaludIndNutri.setNutri1vi(SaludIndNutriView.getNutri1vi());
        SaludIndNutri.setNutri1sa(SaludIndNutriView.getNutri1sa());
        SaludIndNutri.setNutri1do(SaludIndNutriView.getNutri1do());
        SaludIndNutri.setP2hora(SaludIndNutriView.getP2hora());
        SaludIndNutri.setNutri2lu(SaludIndNutriView.getNutri2lu());
        SaludIndNutri.setNutri2ma(SaludIndNutriView.getNutri2ma());
        SaludIndNutri.setNutri2mi(SaludIndNutriView.getNutri2mi());
        SaludIndNutri.setNutri2ju(SaludIndNutriView.getNutri2ju());
        SaludIndNutri.setNutri2vi(SaludIndNutriView.getNutri2vi());
        SaludIndNutri.setNutri2sa(SaludIndNutriView.getNutri2sa());
        SaludIndNutri.setNutri2do(SaludIndNutriView.getNutri2do());
        SaludIndNutri.setP3hora(SaludIndNutriView.getP3hora());
        SaludIndNutri.setNutri3lu(SaludIndNutriView.getNutri3lu());
        SaludIndNutri.setNutri3ma(SaludIndNutriView.getNutri3ma());
        SaludIndNutri.setNutri3mi(SaludIndNutriView.getNutri3mi());
        SaludIndNutri.setNutri3ju(SaludIndNutriView.getNutri3ju());
        SaludIndNutri.setNutri3vi(SaludIndNutriView.getNutri3vi());
        SaludIndNutri.setNutri3sa(SaludIndNutriView.getNutri3sa());
        SaludIndNutri.setNutri3do(SaludIndNutriView.getNutri3do());



        logger.debug("convertir SaludIndNutriView to Entity: {}", SaludIndNutri);
        return SaludIndNutri;
    }

    public SaludIndNutriView toView(SaludIndNutri SaludIndNutri, Boolean complete) {

        SaludIndNutriView SaludIndNutriView = new SaludIndNutriView();
        SaludIndNutriView.setIdindic(SaludIndNutri.getIdindic());
        SaludIndNutriView.setMedidfk(SaludIndNutri.getMedidfk());
        SaludIndNutriView.setPacidfk(SaludIndNutri.getPacidfk());
        SaludIndNutriView.setPesoinicial(SaludIndNutri.getPesoinicial());
        SaludIndNutriView.setTallainicial(SaludIndNutri.getTallainicial());
        SaludIndNutriView.setP1hora(SaludIndNutri.getP1hora());
        SaludIndNutriView.setNutri1lu(SaludIndNutri.getNutri1lu());
        SaludIndNutriView.setNutri1ma(SaludIndNutri.getNutri1ma());
        SaludIndNutriView.setNutri1mi(SaludIndNutri.getNutri1mi());
        SaludIndNutriView.setNutri1ju(SaludIndNutri.getNutri1ju());
        SaludIndNutriView.setNutri1vi(SaludIndNutri.getNutri1vi());
        SaludIndNutriView.setNutri1sa(SaludIndNutri.getNutri1sa());
        SaludIndNutriView.setNutri1do(SaludIndNutri.getNutri1do());
        SaludIndNutriView.setP2hora(SaludIndNutri.getP2hora());
        SaludIndNutriView.setNutri2lu(SaludIndNutri.getNutri2lu());
        SaludIndNutriView.setNutri2ma(SaludIndNutri.getNutri2ma());
        SaludIndNutriView.setNutri2mi(SaludIndNutri.getNutri2mi());
        SaludIndNutriView.setNutri2ju(SaludIndNutri.getNutri2ju());
        SaludIndNutriView.setNutri2vi(SaludIndNutri.getNutri2vi());
        SaludIndNutriView.setNutri2sa(SaludIndNutri.getNutri2sa());
        SaludIndNutriView.setNutri2do(SaludIndNutri.getNutri2do());
        SaludIndNutriView.setP3hora(SaludIndNutri.getP3hora());
        SaludIndNutriView.setNutri3lu(SaludIndNutri.getNutri3lu());
        SaludIndNutriView.setNutri3ma(SaludIndNutri.getNutri3ma());
        SaludIndNutriView.setNutri3mi(SaludIndNutri.getNutri3mi());
        SaludIndNutriView.setNutri3ju(SaludIndNutri.getNutri3ju());
        SaludIndNutriView.setNutri3vi(SaludIndNutri.getNutri3vi());
        SaludIndNutriView.setNutri3sa(SaludIndNutri.getNutri3sa());
        SaludIndNutriView.setNutri3do(SaludIndNutri.getNutri3do());


        logger.debug("convertir SaludIndNutri to View: {}", SaludIndNutriView);
        return SaludIndNutriView;
    }

}
