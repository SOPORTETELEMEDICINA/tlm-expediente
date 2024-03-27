package net.amentum.niomedic.expediente.converter;

import net.amentum.niomedic.expediente.model.CatalogoTipoEvento;
import net.amentum.niomedic.expediente.views.CatalogoTipoEventoView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CatalogoTipoEventoConverter {
   private Logger logger = LoggerFactory.getLogger(CatalogoTipoEventoConverter.class);

   public CatalogoTipoEvento toEntity(CatalogoTipoEventoView catalogoTipoEventoView, CatalogoTipoEvento catalogoTipoEvento, Boolean update){
      catalogoTipoEvento.setDescripcion(catalogoTipoEventoView.getDescripcion());
      catalogoTipoEvento.setColor(catalogoTipoEventoView.getColor());

      logger.debug("convertir catalogoTipoEventoView to Entity: {}", catalogoTipoEvento);
      return catalogoTipoEvento;
   }

   public CatalogoTipoEventoView toView(CatalogoTipoEvento catalogoTipoEvento,Boolean completeCoversion){
      CatalogoTipoEventoView catalogoTipoEventoView = new CatalogoTipoEventoView();
      catalogoTipoEventoView.setIdTipoEvento(catalogoTipoEvento.getIdTipoEvento());
      catalogoTipoEventoView.setDescripcion(catalogoTipoEvento.getDescripcion());
      catalogoTipoEventoView.setColor(catalogoTipoEvento.getColor());

      logger.debug("convertir catalogoTipoEventoView to Entity: {}", catalogoTipoEventoView);
      return catalogoTipoEventoView;
   }

}
