package net.amentum.niomedic.expediente.converter;

import net.amentum.niomedic.expediente.model.CatalogoMomento;
import net.amentum.niomedic.expediente.views.CatalogoMomentoView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class CatalogoMomentoConverter {
   private final Logger logger = LoggerFactory.getLogger(CatalogoMomentoConverter.class);

   public CatalogoMomento toEntity(CatalogoMomentoView catalogoMomentoView, CatalogoMomento catalogoMomento, Boolean update) {
      catalogoMomento.setDescripcionMomento(catalogoMomentoView.getDescripcionMomento());
      catalogoMomento.setActivo(catalogoMomentoView.getActivo());

      logger.debug("convertir CatalogoMomentoView to Entity: {}", catalogoMomento);
      return catalogoMomento;
   }

   public CatalogoMomentoView toView(CatalogoMomento catalogoMomento, Boolean completeCoversion) {
      CatalogoMomentoView catalogoMomentoView = new CatalogoMomentoView();
      catalogoMomentoView.setIdCatalogoMomento(catalogoMomento.getIdCatalogoMomento());
      catalogoMomentoView.setDescripcionMomento(catalogoMomento.getDescripcionMomento());
      catalogoMomentoView.setActivo(catalogoMomento.getActivo());

      logger.debug("convertir CatalogoMomento to View: {}", catalogoMomentoView);
      return catalogoMomentoView;

   }

}
