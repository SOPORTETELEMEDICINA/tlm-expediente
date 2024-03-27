package net.amentum.niomedic.expediente.converter;

import net.amentum.niomedic.expediente.model.SaludIndGluc;
import net.amentum.niomedic.expediente.views.SaludIndGlucView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SaludIndGlucConverter {

    private final Logger logger = LoggerFactory.getLogger(SaludIndGlucConverter.class);

    public SaludIndGluc toEntity(SaludIndGlucView SaludIndGlucView, SaludIndGluc SaludIndGluc, Boolean update) {

        SaludIndGluc.setIdindic(SaludIndGlucView.getIdindic());
        SaludIndGluc.setMedidfk(SaludIndGlucView.getMedidfk());
        SaludIndGluc.setPacidfk(SaludIndGlucView.getPacidfk());
        SaludIndGluc.setUrgenciabaja(SaludIndGlucView.getUrgenciabaja());
        SaludIndGluc.setAlertaalta(SaludIndGlucView.getAlertaalta());
        SaludIndGluc.setUrgenciaalta(SaludIndGlucView.getUrgenciaalta());
        SaludIndGluc.setGlu1hora(SaludIndGlucView.getGlu1hora());
        SaludIndGluc.setGlu1lu(SaludIndGlucView.getGlu1lu());
        SaludIndGluc.setGlu1ma(SaludIndGlucView.getGlu1ma());
        SaludIndGluc.setGlu1mi(SaludIndGlucView.getGlu1mi());
        SaludIndGluc.setGlu1ju(SaludIndGlucView.getGlu1ju());
        SaludIndGluc.setGlu1vi(SaludIndGlucView.getGlu1vi());
        SaludIndGluc.setGlu1sa(SaludIndGlucView.getGlu1sa());
        SaludIndGluc.setGlu1do(SaludIndGlucView.getGlu1do());
        SaludIndGluc.setGlu2hora(SaludIndGlucView.getGlu2hora());
        SaludIndGluc.setGlu2lu(SaludIndGlucView.getGlu2lu());
        SaludIndGluc.setGlu2ma(SaludIndGlucView.getGlu2ma());
        SaludIndGluc.setGlu2mi(SaludIndGlucView.getGlu2mi());
        SaludIndGluc.setGlu2ju(SaludIndGlucView.getGlu2ju());
        SaludIndGluc.setGlu2vi(SaludIndGlucView.getGlu2vi());
        SaludIndGluc.setGlu2sa(SaludIndGlucView.getGlu2sa());
        SaludIndGluc.setGlu2do(SaludIndGlucView.getGlu2do());
        SaludIndGluc.setGlu3hora(SaludIndGlucView.getGlu3hora());
        SaludIndGluc.setGlu3lu(SaludIndGlucView.getGlu3lu());
        SaludIndGluc.setGlu3ma(SaludIndGlucView.getGlu3ma());
        SaludIndGluc.setGlu3mi(SaludIndGlucView.getGlu3mi());
        SaludIndGluc.setGlu3ju(SaludIndGlucView.getGlu3ju());
        SaludIndGluc.setGlu3vi(SaludIndGlucView.getGlu3vi());
        SaludIndGluc.setGlu3sa(SaludIndGlucView.getGlu3sa());
        SaludIndGluc.setGlu3do(SaludIndGlucView.getGlu3do());
        SaludIndGluc.setGlu4hora(SaludIndGlucView.getGlu4hora());
        SaludIndGluc.setGlu4lu(SaludIndGlucView.getGlu4lu());
        SaludIndGluc.setGlu4ma(SaludIndGlucView.getGlu4ma());
        SaludIndGluc.setGlu4mi(SaludIndGlucView.getGlu4mi());
        SaludIndGluc.setGlu4ju(SaludIndGlucView.getGlu4ju());
        SaludIndGluc.setGlu4vi(SaludIndGlucView.getGlu4vi());
        SaludIndGluc.setGlu4sa(SaludIndGlucView.getGlu4sa());
        SaludIndGluc.setGlu4do(SaludIndGlucView.getGlu4do());
        SaludIndGluc.setGlu5hora(SaludIndGlucView.getGlu5hora());
        SaludIndGluc.setGlu5lu(SaludIndGlucView.getGlu5lu());
        SaludIndGluc.setGlu5ma(SaludIndGlucView.getGlu5ma());
        SaludIndGluc.setGlu5mi(SaludIndGlucView.getGlu5mi());
        SaludIndGluc.setGlu5ju(SaludIndGlucView.getGlu5ju());
        SaludIndGluc.setGlu5vi(SaludIndGlucView.getGlu5vi());
        SaludIndGluc.setGlu5sa(SaludIndGlucView.getGlu5sa());
        SaludIndGluc.setGlu5do(SaludIndGlucView.getGlu5do());
        SaludIndGluc.setGlu6hora(SaludIndGlucView.getGlu6hora());
        SaludIndGluc.setGlu6lu(SaludIndGlucView.getGlu6lu());
        SaludIndGluc.setGlu6ma(SaludIndGlucView.getGlu6ma());
        SaludIndGluc.setGlu6mi(SaludIndGlucView.getGlu6mi());
        SaludIndGluc.setGlu6ju(SaludIndGlucView.getGlu6ju());
        SaludIndGluc.setGlu6vi(SaludIndGlucView.getGlu6vi());
        SaludIndGluc.setGlu6sa(SaludIndGlucView.getGlu6sa());
        SaludIndGluc.setGlu6do(SaludIndGlucView.getGlu6do());
        SaludIndGluc.setGlu7hora(SaludIndGlucView.getGlu7hora());
        SaludIndGluc.setGlu7lu(SaludIndGlucView.getGlu7lu());
        SaludIndGluc.setGlu7ma(SaludIndGlucView.getGlu7ma());
        SaludIndGluc.setGlu7mi(SaludIndGlucView.getGlu7mi());
        SaludIndGluc.setGlu7ju(SaludIndGlucView.getGlu7ju());
        SaludIndGluc.setGlu7vi(SaludIndGlucView.getGlu7vi());
        SaludIndGluc.setGlu7sa(SaludIndGlucView.getGlu7sa());
        SaludIndGluc.setGlu7do(SaludIndGlucView.getGlu7do());
        SaludIndGluc.setGlu8hora(SaludIndGlucView.getGlu8hora());
        SaludIndGluc.setGlu8lu(SaludIndGlucView.getGlu8lu());
        SaludIndGluc.setGlu8ma(SaludIndGlucView.getGlu8ma());
        SaludIndGluc.setGlu8mi(SaludIndGlucView.getGlu8mi());
        SaludIndGluc.setGlu8ju(SaludIndGlucView.getGlu8ju());
        SaludIndGluc.setGlu8vi(SaludIndGlucView.getGlu8vi());
        SaludIndGluc.setGlu8sa(SaludIndGlucView.getGlu8sa());
        SaludIndGluc.setGlu8do(SaludIndGlucView.getGlu8do());



        logger.debug("convertir SaludIndGlucView to Entity: {}", SaludIndGluc);
        return SaludIndGluc;
    }

    public SaludIndGlucView toView(SaludIndGluc SaludIndGluc, Boolean complete) {

        SaludIndGlucView SaludIndGlucView = new SaludIndGlucView();
        SaludIndGlucView.setIdindic(SaludIndGluc.getIdindic());
        SaludIndGlucView.setMedidfk(SaludIndGluc.getMedidfk());
        SaludIndGlucView.setPacidfk(SaludIndGluc.getPacidfk());
        SaludIndGlucView.setUrgenciabaja(SaludIndGluc.getUrgenciabaja());
        SaludIndGlucView.setAlertaalta(SaludIndGluc.getAlertaalta());
        SaludIndGlucView.setUrgenciaalta(SaludIndGluc.getUrgenciaalta());
        SaludIndGlucView.setGlu1hora(SaludIndGluc.getGlu1hora());
        SaludIndGlucView.setGlu1lu(SaludIndGluc.getGlu1lu());
        SaludIndGlucView.setGlu1ma(SaludIndGluc.getGlu1ma());
        SaludIndGlucView.setGlu1mi(SaludIndGluc.getGlu1mi());
        SaludIndGlucView.setGlu1ju(SaludIndGluc.getGlu1ju());
        SaludIndGlucView.setGlu1vi(SaludIndGluc.getGlu1vi());
        SaludIndGlucView.setGlu1sa(SaludIndGluc.getGlu1sa());
        SaludIndGlucView.setGlu1do(SaludIndGluc.getGlu1do());
        SaludIndGlucView.setGlu2hora(SaludIndGluc.getGlu2hora());
        SaludIndGlucView.setGlu2lu(SaludIndGluc.getGlu2lu());
        SaludIndGlucView.setGlu2ma(SaludIndGluc.getGlu2ma());
        SaludIndGlucView.setGlu2mi(SaludIndGluc.getGlu2mi());
        SaludIndGlucView.setGlu2ju(SaludIndGluc.getGlu2ju());
        SaludIndGlucView.setGlu2vi(SaludIndGluc.getGlu2vi());
        SaludIndGlucView.setGlu2sa(SaludIndGluc.getGlu2sa());
        SaludIndGlucView.setGlu2do(SaludIndGluc.getGlu2do());
        SaludIndGlucView.setGlu3hora(SaludIndGluc.getGlu3hora());
        SaludIndGlucView.setGlu3lu(SaludIndGluc.getGlu3lu());
        SaludIndGlucView.setGlu3ma(SaludIndGluc.getGlu3ma());
        SaludIndGlucView.setGlu3mi(SaludIndGluc.getGlu3mi());
        SaludIndGlucView.setGlu3ju(SaludIndGluc.getGlu3ju());
        SaludIndGlucView.setGlu3vi(SaludIndGluc.getGlu3vi());
        SaludIndGlucView.setGlu3sa(SaludIndGluc.getGlu3sa());
        SaludIndGlucView.setGlu3do(SaludIndGluc.getGlu3do());
        SaludIndGlucView.setGlu4hora(SaludIndGluc.getGlu4hora());
        SaludIndGlucView.setGlu4lu(SaludIndGluc.getGlu4lu());
        SaludIndGlucView.setGlu4ma(SaludIndGluc.getGlu4ma());
        SaludIndGlucView.setGlu4mi(SaludIndGluc.getGlu4mi());
        SaludIndGlucView.setGlu4ju(SaludIndGluc.getGlu4ju());
        SaludIndGlucView.setGlu4vi(SaludIndGluc.getGlu4vi());
        SaludIndGlucView.setGlu4sa(SaludIndGluc.getGlu4sa());
        SaludIndGlucView.setGlu4do(SaludIndGluc.getGlu4do());
        SaludIndGlucView.setGlu5hora(SaludIndGluc.getGlu5hora());
        SaludIndGlucView.setGlu5lu(SaludIndGluc.getGlu5lu());
        SaludIndGlucView.setGlu5ma(SaludIndGluc.getGlu5ma());
        SaludIndGlucView.setGlu5mi(SaludIndGluc.getGlu5mi());
        SaludIndGlucView.setGlu5ju(SaludIndGluc.getGlu5ju());
        SaludIndGlucView.setGlu5vi(SaludIndGluc.getGlu5vi());
        SaludIndGlucView.setGlu5sa(SaludIndGluc.getGlu5sa());
        SaludIndGlucView.setGlu5do(SaludIndGluc.getGlu5do());
        SaludIndGlucView.setGlu6hora(SaludIndGluc.getGlu6hora());
        SaludIndGlucView.setGlu6lu(SaludIndGluc.getGlu6lu());
        SaludIndGlucView.setGlu6ma(SaludIndGluc.getGlu6ma());
        SaludIndGlucView.setGlu6mi(SaludIndGluc.getGlu6mi());
        SaludIndGlucView.setGlu6ju(SaludIndGluc.getGlu6ju());
        SaludIndGlucView.setGlu6vi(SaludIndGluc.getGlu6vi());
        SaludIndGlucView.setGlu6sa(SaludIndGluc.getGlu6sa());
        SaludIndGlucView.setGlu6do(SaludIndGluc.getGlu6do());
        SaludIndGlucView.setGlu7hora(SaludIndGluc.getGlu7hora());
        SaludIndGlucView.setGlu7lu(SaludIndGluc.getGlu7lu());
        SaludIndGlucView.setGlu7ma(SaludIndGluc.getGlu7ma());
        SaludIndGlucView.setGlu7mi(SaludIndGluc.getGlu7mi());
        SaludIndGlucView.setGlu7ju(SaludIndGluc.getGlu7ju());
        SaludIndGlucView.setGlu7vi(SaludIndGluc.getGlu7vi());
        SaludIndGlucView.setGlu7sa(SaludIndGluc.getGlu7sa());
        SaludIndGlucView.setGlu7do(SaludIndGluc.getGlu7do());
        SaludIndGlucView.setGlu8hora(SaludIndGluc.getGlu8hora());
        SaludIndGlucView.setGlu8lu(SaludIndGluc.getGlu8lu());
        SaludIndGlucView.setGlu8ma(SaludIndGluc.getGlu8ma());
        SaludIndGlucView.setGlu8mi(SaludIndGluc.getGlu8mi());
        SaludIndGlucView.setGlu8ju(SaludIndGluc.getGlu8ju());
        SaludIndGlucView.setGlu8vi(SaludIndGluc.getGlu8vi());
        SaludIndGlucView.setGlu8sa(SaludIndGluc.getGlu8sa());
        SaludIndGlucView.setGlu8do(SaludIndGluc.getGlu8do());

        logger.debug("convertir SaludIndGluc to View: {}", SaludIndGlucView);
        return SaludIndGlucView;
    }
}
