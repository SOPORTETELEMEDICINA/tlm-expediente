package net.amentum.niomedic.expediente.converter;

import net.amentum.niomedic.expediente.model.Asuetos;
import net.amentum.niomedic.expediente.views.AsuetosView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class AsuetosConverter {
   private Logger logger = LoggerFactory.getLogger(AsuetosConverter.class);

   public Asuetos toEntity(AsuetosView asuetosView, Asuetos asuetos, Boolean update){
      asuetos.setFecha(asuetosView.getFecha());
//      asuetos.setFin(asuetosView.getFin());
      asuetos.setObligatorio(asuetosView.getObligatorio());

      logger.debug("convertir asuetosView to Entity: {}", asuetos);
      return asuetos;
   }

   public AsuetosView toView(Asuetos asuetos,Boolean completeCoversion){
      AsuetosView asuetosView = new AsuetosView();
      asuetosView.setIdAsuetos(asuetos.getIdAsuetos());
      asuetosView.setFecha(asuetos.getFecha());
//      asuetosView.setFin(asuetos.getFin());
      asuetosView.setObligatorio(asuetos.getObligatorio());

      logger.debug("convertir asuetosView to Entity: {}", asuetosView);
      return asuetosView;
   }

}
