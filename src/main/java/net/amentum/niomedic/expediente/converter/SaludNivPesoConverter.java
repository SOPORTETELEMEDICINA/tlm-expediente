package net.amentum.niomedic.expediente.converter;

import net.amentum.niomedic.expediente.model.SaludNivPeso;
import net.amentum.niomedic.expediente.views.SaludNivPesoView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class SaludNivPesoConverter {

    private final Logger logger = LoggerFactory.getLogger(SaludNivPesoConverter.class);

    public SaludNivPeso toEntity(SaludNivPesoView SaludNivPesoView, SaludNivPeso SaludNivPeso, Boolean update) {
        SaludNivPeso.setIdnivelpeso(SaludNivPesoView.getIdnivelpeso());
        SaludNivPeso.setMedidfk(SaludNivPesoView.getMedidfk());
        SaludNivPeso.setPacidfk(SaludNivPesoView.getPacidfk());
        SaludNivPeso.setPesoperiodo(SaludNivPesoView.getPesoperiodo());
        SaludNivPeso.setPesofechahora(SaludNivPesoView.getPesofechahora());
        SaludNivPeso.setPesomedida(SaludNivPesoView.getPesomedida());




        logger.debug("convertir SaludNivPesoView to Entity: {}", SaludNivPeso);
        return SaludNivPeso;
    }

    public SaludNivPesoView toView(SaludNivPeso SaludNivPeso, Boolean complete) {

        SaludNivPesoView SaludNivPesoView = new SaludNivPesoView();
        SaludNivPesoView.setIdnivelpeso(SaludNivPeso.getIdnivelpeso());
        SaludNivPesoView.setMedidfk(SaludNivPeso.getMedidfk());
        SaludNivPesoView.setPacidfk(SaludNivPeso.getPacidfk());
        SaludNivPesoView.setPesoperiodo(SaludNivPeso.getPesoperiodo());
        SaludNivPesoView.setPesofechahora(SaludNivPeso.getPesofechahora());
        SaludNivPesoView.setPesomedida(SaludNivPeso.getPesomedida());

        logger.debug("convertir SaludNivPeso to View: {}", SaludNivPesoView);
        return SaludNivPesoView;
    }


}
