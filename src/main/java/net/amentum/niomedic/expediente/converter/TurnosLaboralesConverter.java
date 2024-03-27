package net.amentum.niomedic.expediente.converter;

import net.amentum.niomedic.expediente.model.TurnosLaborales;
import net.amentum.niomedic.expediente.views.TurnosLaboralesView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class TurnosLaboralesConverter {
   private Logger logger = LoggerFactory.getLogger(TurnosLaboralesConverter.class);

   public TurnosLaborales toEntity(TurnosLaboralesView turnosLaboralesView, TurnosLaborales turnosLaborales, Boolean update){
//      turnosLaborales.setMedicoId(turnosLaboralesView.getMedicoId());
//      turnosLaborales.setNombreMedico(turnosLaboralesView.getNombreMedico());
      turnosLaborales.setIdUsuario(turnosLaboralesView.getIdUsuario());
      turnosLaborales.setNombreUsuario(turnosLaboralesView.getNombreUsuario());
      turnosLaborales.setInicio(turnosLaboralesView.getInicio());
      turnosLaborales.setFin(turnosLaboralesView.getFin());
      turnosLaborales.setLunes(turnosLaboralesView.getLunes());
      turnosLaborales.setMartes(turnosLaboralesView.getMartes());
      turnosLaborales.setMiercoles(turnosLaboralesView.getMiercoles());
      turnosLaborales.setJueves(turnosLaboralesView.getJueves());
      turnosLaborales.setViernes(turnosLaboralesView.getViernes());
      turnosLaborales.setSabado(turnosLaboralesView.getSabado());
      turnosLaborales.setDomingo(turnosLaboralesView.getDomingo());
      
      logger.debug("convertir turnosLaboralesView to Entity: {}", turnosLaborales);
      return turnosLaborales;
   }

   public TurnosLaboralesView toView(TurnosLaborales turnosLaborales,Boolean completeCoversion){
      TurnosLaboralesView turnosLaboralesView = new TurnosLaboralesView();
      turnosLaboralesView.setIdTurnosLaborales(turnosLaborales.getIdTurnosLaborales());
//      turnosLaboralesView.setMedicoId(turnosLaborales.getMedicoId());
//      turnosLaboralesView.setNombreMedico(turnosLaborales.getNombreMedico());
      turnosLaboralesView.setIdUsuario(turnosLaborales.getIdUsuario());
      turnosLaboralesView.setNombreUsuario(turnosLaborales.getNombreUsuario());
      turnosLaboralesView.setInicio(turnosLaborales.getInicio());
      turnosLaboralesView.setFin(turnosLaborales.getFin());
      turnosLaboralesView.setLunes(turnosLaborales.getLunes());
      turnosLaboralesView.setMartes(turnosLaborales.getMartes());
      turnosLaboralesView.setMiercoles(turnosLaborales.getMiercoles());
      turnosLaboralesView.setJueves(turnosLaborales.getJueves());
      turnosLaboralesView.setViernes(turnosLaborales.getViernes());
      turnosLaboralesView.setSabado(turnosLaborales.getSabado());
      turnosLaboralesView.setDomingo(turnosLaborales.getDomingo());

      logger.debug("convertir turnosLaboralesView to Entity: {}", turnosLaboralesView);
      return turnosLaboralesView;
   }

}
