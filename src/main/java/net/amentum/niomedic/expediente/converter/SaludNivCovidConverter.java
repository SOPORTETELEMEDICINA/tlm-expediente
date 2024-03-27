package net.amentum.niomedic.expediente.converter;


import net.amentum.niomedic.expediente.model.Controles;
import net.amentum.niomedic.expediente.model.SaludNivCovid;
import net.amentum.niomedic.expediente.model.SaludNivCovid;
import net.amentum.niomedic.expediente.model.SaludNivCovid;
import net.amentum.niomedic.expediente.views.SaludNivCovidView;
import net.amentum.niomedic.expediente.views.SaludNivCovidView;
import net.amentum.niomedic.expediente.views.SaludNivCovidView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Component
public class SaludNivCovidConverter {

    private final Logger logger = LoggerFactory.getLogger(SaludNivCovidConverter.class);

    public SaludNivCovid toEntity(SaludNivCovidView SaludNivCovidView, SaludNivCovid SaludNivCovid, Boolean update) {
        SaludNivCovid.setIdnivelcovid(SaludNivCovidView.getIdnivelcovid());
        SaludNivCovid.setMedidfk(SaludNivCovidView.getMedidfk());
        SaludNivCovid.setPacidfk(SaludNivCovidView.getPacidfk());
        SaludNivCovid.setCovidperiodo(SaludNivCovidView.getCovidperiodo());
        SaludNivCovid.setCovidfechahora(SaludNivCovidView.getCovidfechahora());
        SaludNivCovid.setCovidtempmedida(SaludNivCovidView.getCovidtempmedida());
        SaludNivCovid.setCovidspomedida(SaludNivCovidView.getCovidspomedida());
        SaludNivCovid.setCovidpulsomedida(SaludNivCovidView.getCovidpulsomedida());


        logger.debug("convertir SaludNivCovidView to Entity: {}", SaludNivCovid);
        return SaludNivCovid;
        
    }


    public SaludNivCovidView toView(SaludNivCovid SaludNivCovid, Boolean complete) {

        SaludNivCovidView SaludNivCovidView = new SaludNivCovidView();
        SaludNivCovidView.setIdnivelcovid(SaludNivCovid.getIdnivelcovid());
        SaludNivCovidView.setMedidfk(SaludNivCovid.getMedidfk());
        SaludNivCovidView.setPacidfk(SaludNivCovid.getPacidfk());
        SaludNivCovidView.setCovidperiodo(SaludNivCovid.getCovidperiodo());
        SaludNivCovidView.setCovidfechahora(SaludNivCovid.getCovidfechahora());
        SaludNivCovidView.setCovidtempmedida(SaludNivCovid.getCovidtempmedida());
        SaludNivCovidView.setCovidspomedida(SaludNivCovid.getCovidspomedida());
        SaludNivCovidView.setCovidpulsomedida(SaludNivCovid.getCovidpulsomedida());

        logger.debug("convertir SaludNivCovid to View: {}", SaludNivCovidView);
        return SaludNivCovidView;
    }


}
