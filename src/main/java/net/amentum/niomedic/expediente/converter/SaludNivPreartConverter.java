package net.amentum.niomedic.expediente.converter;

import net.amentum.niomedic.expediente.model.SaludNivPreart;
import net.amentum.niomedic.expediente.views.SaludNivPreartView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SaludNivPreartConverter {

    private final Logger logger = LoggerFactory.getLogger(SaludNivPreartConverter.class);

    public SaludNivPreart toEntity(SaludNivPreartView SaludNivPreartView, SaludNivPreart SaludNivPreart, Boolean update) {
        SaludNivPreart.setIdnivelpa(SaludNivPreartView.getIdnivelpa());
        SaludNivPreart.setMedidfk(SaludNivPreartView.getMedidfk());
        SaludNivPreart.setPacidfk(SaludNivPreartView.getPacidfk());
        SaludNivPreart.setPaperiodo(SaludNivPreartView.getPaperiodo());
        SaludNivPreart.setPafechahora(SaludNivPreartView.getPafechahora());
        SaludNivPreart.setPasysmedida(SaludNivPreartView.getPasysmedida());
        SaludNivPreart.setPadiamedida(SaludNivPreartView.getPadiamedida());
        SaludNivPreart.setPafcmedida(SaludNivPreartView.getPafcmedida());




        logger.debug("convertir SaludNivPreartView to Entity: {}", SaludNivPreart);
        return SaludNivPreart;
    }

    public SaludNivPreartView toView(SaludNivPreart SaludNivPreart, Boolean complete) {

        SaludNivPreartView SaludNivPreartView = new SaludNivPreartView();
        SaludNivPreartView.setIdnivelpa(SaludNivPreart.getIdnivelpa());
        SaludNivPreartView.setMedidfk(SaludNivPreart.getMedidfk());
        SaludNivPreartView.setPacidfk(SaludNivPreart.getPacidfk());
        SaludNivPreartView.setPaperiodo(SaludNivPreart.getPaperiodo());
        SaludNivPreartView.setPafechahora(SaludNivPreart.getPafechahora());
        SaludNivPreartView.setPasysmedida(SaludNivPreart.getPasysmedida());
        SaludNivPreartView.setPadiamedida(SaludNivPreart.getPadiamedida());
        SaludNivPreartView.setPafcmedida(SaludNivPreart.getPafcmedida());


        logger.debug("convertir SaludNivPreart to View: {}", SaludNivPreartView);
        return SaludNivPreartView;
    }


}
