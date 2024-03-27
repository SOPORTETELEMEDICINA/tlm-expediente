package net.amentum.niomedic.expediente.converter;

import net.amentum.niomedic.expediente.model.SaludIndCovid;
import net.amentum.niomedic.expediente.views.SaludIndCovidView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;



@Component
public class SaludIndCovidConverter {

    private final Logger logger = LoggerFactory.getLogger(SaludIndCovidConverter.class);

    public SaludIndCovid toEntity(SaludIndCovidView SaludIndCovidView, SaludIndCovid SaludIndCovid, Boolean update) {

        SaludIndCovid.setIdindic(SaludIndCovidView.getIdindic());
        SaludIndCovid.setMedidfk(SaludIndCovidView.getMedidfk());
        SaludIndCovid.setPacidfk(SaludIndCovidView.getPacidfk());
        SaludIndCovid.setP1hora(SaludIndCovidView.getP1hora());
        SaludIndCovid.setCovid1lu(SaludIndCovidView.getCovid1lu());
        SaludIndCovid.setCovid1ma(SaludIndCovidView.getCovid1ma());
        SaludIndCovid.setCovid1mi(SaludIndCovidView.getCovid1mi());
        SaludIndCovid.setCovid1ju(SaludIndCovidView.getCovid1ju());
        SaludIndCovid.setCovid1vi(SaludIndCovidView.getCovid1vi());
        SaludIndCovid.setCovid1sa(SaludIndCovidView.getCovid1sa());
        SaludIndCovid.setCovid1do(SaludIndCovidView.getCovid1do());
        SaludIndCovid.setP2hora(SaludIndCovidView.getP2hora());
        SaludIndCovid.setCovid2lu(SaludIndCovidView.getCovid2lu());
        SaludIndCovid.setCovid2ma(SaludIndCovidView.getCovid2ma());
        SaludIndCovid.setCovid2mi(SaludIndCovidView.getCovid2mi());
        SaludIndCovid.setCovid2ju(SaludIndCovidView.getCovid2ju());
        SaludIndCovid.setCovid2vi(SaludIndCovidView.getCovid2vi());
        SaludIndCovid.setCovid2sa(SaludIndCovidView.getCovid2sa());
        SaludIndCovid.setCovid2do(SaludIndCovidView.getCovid2do());
        SaludIndCovid.setP3hora(SaludIndCovidView.getP3hora());
        SaludIndCovid.setCovid3lu(SaludIndCovidView.getCovid3lu());
        SaludIndCovid.setCovid3ma(SaludIndCovidView.getCovid3ma());
        SaludIndCovid.setCovid3mi(SaludIndCovidView.getCovid3mi());
        SaludIndCovid.setCovid3ju(SaludIndCovidView.getCovid3ju());
        SaludIndCovid.setCovid3vi(SaludIndCovidView.getCovid3vi());
        SaludIndCovid.setCovid3sa(SaludIndCovidView.getCovid3sa());
        SaludIndCovid.setCovid3do(SaludIndCovidView.getCovid3do());
        SaludIndCovid.setP4hora(SaludIndCovidView.getP4hora());
        SaludIndCovid.setCovid4lu(SaludIndCovidView.getCovid4lu());
        SaludIndCovid.setCovid4ma(SaludIndCovidView.getCovid4ma());
        SaludIndCovid.setCovid4mi(SaludIndCovidView.getCovid4mi());
        SaludIndCovid.setCovid4ju(SaludIndCovidView.getCovid4ju());
        SaludIndCovid.setCovid4vi(SaludIndCovidView.getCovid4vi());
        SaludIndCovid.setCovid4sa(SaludIndCovidView.getCovid4sa());
        SaludIndCovid.setCovid4do(SaludIndCovidView.getCovid4do());


        logger.debug("convertir SaludIndCovidView to Entity: {}", SaludIndCovid);
        return SaludIndCovid;
    }

    public SaludIndCovidView toView(SaludIndCovid SaludIndCovid, Boolean complete) {

        SaludIndCovidView SaludIndCovidView = new SaludIndCovidView();
        SaludIndCovidView.setIdindic(SaludIndCovid.getIdindic());
        SaludIndCovidView.setMedidfk(SaludIndCovid.getMedidfk());
        SaludIndCovidView.setPacidfk(SaludIndCovid.getPacidfk());
        SaludIndCovidView.setP1hora(SaludIndCovid.getP1hora());
        SaludIndCovidView.setCovid1lu(SaludIndCovid.getCovid1lu());
        SaludIndCovidView.setCovid1ma(SaludIndCovid.getCovid1ma());
        SaludIndCovidView.setCovid1mi(SaludIndCovid.getCovid1mi());
        SaludIndCovidView.setCovid1ju(SaludIndCovid.getCovid1ju());
        SaludIndCovidView.setCovid1vi(SaludIndCovid.getCovid1vi());
        SaludIndCovidView.setCovid1sa(SaludIndCovid.getCovid1sa());
        SaludIndCovidView.setCovid1do(SaludIndCovid.getCovid1do());
        SaludIndCovidView.setP2hora(SaludIndCovid.getP2hora());
        SaludIndCovidView.setCovid2lu(SaludIndCovid.getCovid2lu());
        SaludIndCovidView.setCovid2ma(SaludIndCovid.getCovid2ma());
        SaludIndCovidView.setCovid2mi(SaludIndCovid.getCovid2mi());
        SaludIndCovidView.setCovid2ju(SaludIndCovid.getCovid2ju());
        SaludIndCovidView.setCovid2vi(SaludIndCovid.getCovid2vi());
        SaludIndCovidView.setCovid2sa(SaludIndCovid.getCovid2sa());
        SaludIndCovidView.setCovid2do(SaludIndCovid.getCovid2do());
        SaludIndCovidView.setP3hora(SaludIndCovid.getP3hora());
        SaludIndCovidView.setCovid3lu(SaludIndCovid.getCovid3lu());
        SaludIndCovidView.setCovid3ma(SaludIndCovid.getCovid3ma());
        SaludIndCovidView.setCovid3mi(SaludIndCovid.getCovid3mi());
        SaludIndCovidView.setCovid3ju(SaludIndCovid.getCovid3ju());
        SaludIndCovidView.setCovid3vi(SaludIndCovid.getCovid3vi());
        SaludIndCovidView.setCovid3sa(SaludIndCovid.getCovid3sa());
        SaludIndCovidView.setCovid3do(SaludIndCovid.getCovid3do());
        SaludIndCovidView.setP4hora(SaludIndCovid.getP4hora());
        SaludIndCovidView.setCovid4lu(SaludIndCovid.getCovid4lu());
        SaludIndCovidView.setCovid4ma(SaludIndCovid.getCovid4ma());
        SaludIndCovidView.setCovid4mi(SaludIndCovid.getCovid4mi());
        SaludIndCovidView.setCovid4ju(SaludIndCovid.getCovid4ju());
        SaludIndCovidView.setCovid4vi(SaludIndCovid.getCovid4vi());
        SaludIndCovidView.setCovid4sa(SaludIndCovid.getCovid4sa());
        SaludIndCovidView.setCovid4do(SaludIndCovid.getCovid4do());

        logger.debug("convertir SaludIndCovid to View: {}", SaludIndCovidView);
        return SaludIndCovidView;
    }
}
