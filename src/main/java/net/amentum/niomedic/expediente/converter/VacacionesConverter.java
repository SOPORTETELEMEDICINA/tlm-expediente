package net.amentum.niomedic.expediente.converter;

import net.amentum.niomedic.expediente.model.Vacaciones;
import net.amentum.niomedic.expediente.views.VacacionesView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class VacacionesConverter {
   private Logger logger = LoggerFactory.getLogger(VacacionesConverter.class);

   public Vacaciones toEntity(VacacionesView vacacionesView, Vacaciones vacaciones, Boolean update){
//      vacaciones.setMedicoId(vacacionesView.getMedicoId());
//      vacaciones.setNombreMedico(vacacionesView.getNombreMedico());
      vacaciones.setIdUsuario(vacacionesView.getIdUsuario());
      vacaciones.setNombreUsuario(vacacionesView.getNombreUsuario());
      vacaciones.setInicio(vacacionesView.getInicio());
      vacaciones.setFin(vacacionesView.getFin());
      
      logger.debug("convertir vacacionesView to Entity: {}", vacaciones);
      return vacaciones;
   }

   public VacacionesView toView(Vacaciones vacaciones,Boolean completeCoversion){
      VacacionesView vacacionesView = new VacacionesView();
      vacacionesView.setIdVacaciones(vacaciones.getIdVacaciones());
//      vacacionesView.setMedicoId(vacaciones.getMedicoId());
//      vacacionesView.setNombreMedico(vacaciones.getNombreMedico());
      vacacionesView.setIdUsuario(vacaciones.getIdUsuario());
      vacacionesView.setNombreUsuario(vacaciones.getNombreUsuario());
      vacacionesView.setInicio(vacaciones.getInicio());
      vacacionesView.setFin(vacaciones.getFin());

      logger.debug("convertir vacacionesView to Entity: {}", vacacionesView);
      return vacacionesView;
   }

}
