package net.amentum.niomedic.expediente.converter;

import net.amentum.niomedic.expediente.model.CatalogoDia;
import net.amentum.niomedic.expediente.views.CatalogoDiaView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CatalogoDiaConverter {
   private final Logger logger = LoggerFactory.getLogger(CatalogoDiaConverter.class);

//   public CatalogoDia toEntity(CatalogoDiaView catalogoDiaView, CatalogoDia catalogoDia, Boolean update) {
//   }

   public CatalogoDiaView toView(CatalogoDia catalogoDia, Boolean completeCoversion) {
      CatalogoDiaView catalogoDiaView = new CatalogoDiaView();
      catalogoDiaView.setIdCatalogoDia(catalogoDia.getIdCatalogoDia());
      catalogoDiaView.setDescripcionDia(catalogoDia.getDescripcionDia());

      logger.debug("convertir CatalogoDia to View: {}", catalogoDiaView);
      return catalogoDiaView;

   }

}
