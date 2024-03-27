package net.amentum.niomedic.expediente.converter;


import net.amentum.niomedic.expediente.model.SaludNivGluc;
import net.amentum.niomedic.expediente.views.SaludNivGlucView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SaludNivGlucConverter {

    private final Logger logger = LoggerFactory.getLogger(SaludNivGlucConverter.class);

    public SaludNivGluc toEntity(SaludNivGlucView SaludNivGlucView, SaludNivGluc SaludNivGluc, Boolean update) {
        SaludNivGluc.setIdnivelglucosa(SaludNivGlucView.getIdnivelglucosa());
        SaludNivGluc.setMedidfk(SaludNivGlucView.getMedidfk());
        SaludNivGluc.setPacidfk(SaludNivGlucView.getPacidfk());
        SaludNivGluc.setGluperiodo(SaludNivGlucView.getGluperiodo());
        SaludNivGluc.setGlufechahora(SaludNivGlucView.getGlufechahora());
        SaludNivGluc.setGlumedida(SaludNivGlucView.getGlumedida());



        logger.debug("convertir SaludNivGlucView to Entity: {}", SaludNivGluc);
        return SaludNivGluc;
    }

    public SaludNivGlucView toView(SaludNivGluc SaludNivGluc, Boolean complete) {

        SaludNivGlucView SaludNivGlucView = new SaludNivGlucView();
        SaludNivGlucView.setIdnivelglucosa(SaludNivGluc.getIdnivelglucosa());
        SaludNivGlucView.setMedidfk(SaludNivGluc.getMedidfk());
        SaludNivGlucView.setPacidfk(SaludNivGluc.getPacidfk());
        SaludNivGlucView.setGluperiodo(SaludNivGluc.getGluperiodo());
        SaludNivGlucView.setGlufechahora(SaludNivGluc.getGlufechahora());
        SaludNivGlucView.setGlumedida(SaludNivGluc.getGlumedida());

        logger.debug("convertir SaludNivGluc to View: {}", SaludNivGlucView);
        return SaludNivGlucView;
    }

}
