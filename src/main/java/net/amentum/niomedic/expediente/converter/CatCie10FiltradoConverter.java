package net.amentum.niomedic.expediente.converter;

import net.amentum.niomedic.expediente.model.CatCie10;
import net.amentum.niomedic.expediente.views.CatCie10FiltradoView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CatCie10FiltradoConverter {
   private final Logger logger = LoggerFactory.getLogger(CatCie10FiltradoConverter.class);
   /*
   no hay toEntity porque no se añade nada al catalogo
    */

   public CatCie10FiltradoView toView(CatCie10 catCie10, Boolean complete) {

      CatCie10FiltradoView catCie10FiltradoView = new CatCie10FiltradoView();
      catCie10FiltradoView.setIdCie10(catCie10.getIdCie10());
      catCie10FiltradoView.setCatalogKey(catCie10.getCatalogKey());
      catCie10FiltradoView.setNombre(catCie10.getNombre());
      catCie10FiltradoView.setDescripcionCapitulo(catCie10.getDescripcionCapitulo());
      catCie10FiltradoView.setActivo(catCie10.getActivo());

      logger.debug("convertir CatCie10 to FiltradoView: {}", catCie10FiltradoView);
      return catCie10FiltradoView;
   }
}
