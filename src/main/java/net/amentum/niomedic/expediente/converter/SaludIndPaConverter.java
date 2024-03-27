package net.amentum.niomedic.expediente.converter;

import net.amentum.niomedic.expediente.model.SaludIndPa;
import net.amentum.niomedic.expediente.views.SaludIndPaView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SaludIndPaConverter {

        private final Logger logger = LoggerFactory.getLogger(SaludIndPaConverter.class);

        public SaludIndPa toEntity(SaludIndPaView SaludIndPaView, SaludIndPa SaludIndPa, Boolean update) {

                SaludIndPa.setIdindic(SaludIndPaView.getIdindic());
                SaludIndPa.setMedidfk(SaludIndPaView.getMedidfk());
                SaludIndPa.setPacidfk(SaludIndPaView.getPacidfk());
                SaludIndPa.setAlertabajasys(SaludIndPaView.getAlertabajasys());
                SaludIndPa.setAlertabajadia(SaludIndPaView.getAlertabajadia());
                SaludIndPa.setUrgenciaaltasys(SaludIndPaView.getUrgenciaaltasys());
                SaludIndPa.setUrgenciaaltadia(SaludIndPaView.getUrgenciaaltadia());
                SaludIndPa.setP1hora(SaludIndPaView.getP1hora());
                SaludIndPa.setSys1lu(SaludIndPaView.getSys1lu());
                SaludIndPa.setSys1ma(SaludIndPaView.getSys1ma());
                SaludIndPa.setSys1mi(SaludIndPaView.getSys1mi());
                SaludIndPa.setSys1ju(SaludIndPaView.getSys1ju());
                SaludIndPa.setSys1vi(SaludIndPaView.getSys1vi());
                SaludIndPa.setSys1sa(SaludIndPaView.getSys1sa());
                SaludIndPa.setSys1do(SaludIndPaView.getSys1do());
                SaludIndPa.setP2hora(SaludIndPaView.getP2hora());
                SaludIndPa.setSys2lu(SaludIndPaView.getSys2lu());
                SaludIndPa.setSys2ma(SaludIndPaView.getSys2ma());
                SaludIndPa.setSys2mi(SaludIndPaView.getSys2mi());
                SaludIndPa.setSys2ju(SaludIndPaView.getSys2ju());
                SaludIndPa.setSys2vi(SaludIndPaView.getSys2vi());
                SaludIndPa.setSys2sa(SaludIndPaView.getSys2sa());
                SaludIndPa.setSys2do(SaludIndPaView.getSys2do());
                SaludIndPa.setP3hora(SaludIndPaView.getP3hora());
                SaludIndPa.setSys3lu(SaludIndPaView.getSys3lu());
                SaludIndPa.setSys3ma(SaludIndPaView.getSys3ma());
                SaludIndPa.setSys3mi(SaludIndPaView.getSys3mi());
                SaludIndPa.setSys3ju(SaludIndPaView.getSys3ju());
                SaludIndPa.setSys3vi(SaludIndPaView.getSys3vi());
                SaludIndPa.setSys3sa(SaludIndPaView.getSys3sa());
                SaludIndPa.setSys3do(SaludIndPaView.getSys3do());
                SaludIndPa.setP4hora(SaludIndPaView.getP4hora());
                SaludIndPa.setSys4lu(SaludIndPaView.getSys4lu());
                SaludIndPa.setSys4ma(SaludIndPaView.getSys4ma());
                SaludIndPa.setSys4mi(SaludIndPaView.getSys4mi());
                SaludIndPa.setSys4ju(SaludIndPaView.getSys4ju());
                SaludIndPa.setSys4vi(SaludIndPaView.getSys4vi());
                SaludIndPa.setSys4sa(SaludIndPaView.getSys4sa());
                SaludIndPa.setSys4do(SaludIndPaView.getSys4do());
                SaludIndPa.setP5hora(SaludIndPaView.getP5hora());
                SaludIndPa.setSys5lu(SaludIndPaView.getSys5lu());
                SaludIndPa.setSys5ma(SaludIndPaView.getSys5ma());
                SaludIndPa.setSys5mi(SaludIndPaView.getSys5mi());
                SaludIndPa.setSys5ju(SaludIndPaView.getSys5ju());
                SaludIndPa.setSys5vi(SaludIndPaView.getSys5vi());
                SaludIndPa.setSys5sa(SaludIndPaView.getSys5sa());
                SaludIndPa.setSys5do(SaludIndPaView.getSys5do());
                SaludIndPa.setP6hora(SaludIndPaView.getP6hora());
                SaludIndPa.setSys6lu(SaludIndPaView.getSys6lu());
                SaludIndPa.setSys6ma(SaludIndPaView.getSys6ma());
                SaludIndPa.setSys6mi(SaludIndPaView.getSys6mi());
                SaludIndPa.setSys6ju(SaludIndPaView.getSys6ju());
                SaludIndPa.setSys6vi(SaludIndPaView.getSys6vi());
                SaludIndPa.setSys6sa(SaludIndPaView.getSys6sa());
                SaludIndPa.setSys6do(SaludIndPaView.getSys6do());
                SaludIndPa.setP7hora(SaludIndPaView.getP7hora());
                SaludIndPa.setSys7lu(SaludIndPaView.getSys7lu());
                SaludIndPa.setSys7ma(SaludIndPaView.getSys7ma());
                SaludIndPa.setSys7mi(SaludIndPaView.getSys7mi());
                SaludIndPa.setSys7ju(SaludIndPaView.getSys7ju());
                SaludIndPa.setSys7vi(SaludIndPaView.getSys7vi());
                SaludIndPa.setSys7sa(SaludIndPaView.getSys7sa());
                SaludIndPa.setSys7do(SaludIndPaView.getSys7do());




                logger.debug("convertir SaludIndPaView to Entity: {}", SaludIndPa);
                return SaludIndPa;
        }

        public SaludIndPaView toView(SaludIndPa SaludIndPa, Boolean complete) {

                SaludIndPaView SaludIndPaView = new SaludIndPaView();
                SaludIndPaView.setIdindic(SaludIndPa.getIdindic());
                SaludIndPaView.setMedidfk(SaludIndPa.getMedidfk());
                SaludIndPaView.setPacidfk(SaludIndPa.getPacidfk());
                SaludIndPaView.setAlertabajasys(SaludIndPa.getAlertabajasys());
                SaludIndPaView.setAlertabajadia(SaludIndPa.getAlertabajadia());
                SaludIndPaView.setUrgenciaaltasys(SaludIndPa.getUrgenciaaltasys());
                SaludIndPaView.setUrgenciaaltadia(SaludIndPa.getUrgenciaaltadia());
                SaludIndPaView.setP1hora(SaludIndPa.getP1hora());
                SaludIndPaView.setSys1lu(SaludIndPa.getSys1lu());
                SaludIndPaView.setSys1ma(SaludIndPa.getSys1ma());
                SaludIndPaView.setSys1mi(SaludIndPa.getSys1mi());
                SaludIndPaView.setSys1ju(SaludIndPa.getSys1ju());
                SaludIndPaView.setSys1vi(SaludIndPa.getSys1vi());
                SaludIndPaView.setSys1sa(SaludIndPa.getSys1sa());
                SaludIndPaView.setSys1do(SaludIndPa.getSys1do());
                SaludIndPaView.setP2hora(SaludIndPa.getP2hora());
                SaludIndPaView.setSys2lu(SaludIndPa.getSys2lu());
                SaludIndPaView.setSys2ma(SaludIndPa.getSys2ma());
                SaludIndPaView.setSys2mi(SaludIndPa.getSys2mi());
                SaludIndPaView.setSys2ju(SaludIndPa.getSys2ju());
                SaludIndPaView.setSys2vi(SaludIndPa.getSys2vi());
                SaludIndPaView.setSys2sa(SaludIndPa.getSys2sa());
                SaludIndPaView.setSys2do(SaludIndPa.getSys2do());
                SaludIndPaView.setP3hora(SaludIndPa.getP3hora());
                SaludIndPaView.setSys3lu(SaludIndPa.getSys3lu());
                SaludIndPaView.setSys3ma(SaludIndPa.getSys3ma());
                SaludIndPaView.setSys3mi(SaludIndPa.getSys3mi());
                SaludIndPaView.setSys3ju(SaludIndPa.getSys3ju());
                SaludIndPaView.setSys3vi(SaludIndPa.getSys3vi());
                SaludIndPaView.setSys3sa(SaludIndPa.getSys3sa());
                SaludIndPaView.setSys3do(SaludIndPa.getSys3do());
                SaludIndPaView.setP4hora(SaludIndPa.getP4hora());
                SaludIndPaView.setSys4lu(SaludIndPa.getSys4lu());
                SaludIndPaView.setSys4ma(SaludIndPa.getSys4ma());
                SaludIndPaView.setSys4mi(SaludIndPa.getSys4mi());
                SaludIndPaView.setSys4ju(SaludIndPa.getSys4ju());
                SaludIndPaView.setSys4vi(SaludIndPa.getSys4vi());
                SaludIndPaView.setSys4sa(SaludIndPa.getSys4sa());
                SaludIndPaView.setSys4do(SaludIndPa.getSys4do());
                SaludIndPaView.setP5hora(SaludIndPa.getP5hora());
                SaludIndPaView.setSys5lu(SaludIndPa.getSys5lu());
                SaludIndPaView.setSys5ma(SaludIndPa.getSys5ma());
                SaludIndPaView.setSys5mi(SaludIndPa.getSys5mi());
                SaludIndPaView.setSys5ju(SaludIndPa.getSys5ju());
                SaludIndPaView.setSys5vi(SaludIndPa.getSys5vi());
                SaludIndPaView.setSys5sa(SaludIndPa.getSys5sa());
                SaludIndPaView.setSys5do(SaludIndPa.getSys5do());
                SaludIndPaView.setP6hora(SaludIndPa.getP6hora());
                SaludIndPaView.setSys6lu(SaludIndPa.getSys6lu());
                SaludIndPaView.setSys6ma(SaludIndPa.getSys6ma());
                SaludIndPaView.setSys6mi(SaludIndPa.getSys6mi());
                SaludIndPaView.setSys6ju(SaludIndPa.getSys6ju());
                SaludIndPaView.setSys6vi(SaludIndPa.getSys6vi());
                SaludIndPaView.setSys6sa(SaludIndPa.getSys6sa());
                SaludIndPaView.setSys6do(SaludIndPa.getSys6do());
                SaludIndPaView.setP7hora(SaludIndPa.getP7hora());
                SaludIndPaView.setSys7lu(SaludIndPa.getSys7lu());
                SaludIndPaView.setSys7ma(SaludIndPa.getSys7ma());
                SaludIndPaView.setSys7mi(SaludIndPa.getSys7mi());
                SaludIndPaView.setSys7ju(SaludIndPa.getSys7ju());
                SaludIndPaView.setSys7vi(SaludIndPa.getSys7vi());
                SaludIndPaView.setSys7sa(SaludIndPa.getSys7sa());
                SaludIndPaView.setSys7do(SaludIndPa.getSys7do());


                logger.debug("convertir SaludIndPa to View: {}", SaludIndPaView);
                return SaludIndPaView;
        }

}
