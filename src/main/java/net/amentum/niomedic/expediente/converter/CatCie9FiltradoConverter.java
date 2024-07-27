package net.amentum.niomedic.expediente.converter;

import net.amentum.niomedic.expediente.model.CatCie9;
import net.amentum.niomedic.expediente.views.CatCie9FiltradoView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CatCie9FiltradoConverter {
   private final Logger logger = LoggerFactory.getLogger(CatCie9FiltradoConverter.class);
   /*
   no hay toEntity porque no se añade nada al catalogo
    */

   public CatCie9FiltradoView toView(CatCie9 catCie9, Boolean complete) {

      CatCie9FiltradoView catCie9FiltradoView = new CatCie9FiltradoView();
      catCie9FiltradoView.setIdCie9(catCie9.getIdCie9());
      catCie9FiltradoView.setCatalogKey(catCie9.getCatalogKey());
      catCie9FiltradoView.setProNombre(catCie9.getProNombre());
      catCie9FiltradoView.setProcedimientoType(catCie9.getProcedimientoType());
      catCie9FiltradoView.setActivo(catCie9.getActivo());

      logger.debug("convertir CatCie9 to FiltradoView: {}", catCie9FiltradoView);
      return catCie9FiltradoView;
   }
}
