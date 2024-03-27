package net.amentum.niomedic.expediente.converter;

import net.amentum.niomedic.expediente.model.CatalogoDrogas;
import net.amentum.niomedic.expediente.views.CatalogoDrogasView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CatalogoDrogasConverter {
    private final Logger logger = LoggerFactory.getLogger(CatalogoDrogasConverter.class);

    public CatalogoDrogas toEntity(CatalogoDrogasView catalogoDrogasView, CatalogoDrogas catalogoDrogas, Boolean update) {
        catalogoDrogas.setNombreDroga(catalogoDrogasView.getNombreDroga());
        catalogoDrogas.setUnidadMedida(catalogoDrogasView.getUnidadMedida());
        catalogoDrogas.setActivo(catalogoDrogasView.getActivo());

        logger.debug("convertir CatalogoDrogasView to Entity: {}", catalogoDrogas);
        return catalogoDrogas;
    }

    public CatalogoDrogasView toView(CatalogoDrogas catalogoDrogas, Boolean complete) {
        CatalogoDrogasView catalogoDrogasView = new CatalogoDrogasView();
        catalogoDrogasView.setIdCatalogoDrogas(catalogoDrogas.getIdCatalogoDrogas());
        catalogoDrogasView.setNombreDroga(catalogoDrogas.getNombreDroga());
        catalogoDrogasView.setUnidadMedida(catalogoDrogas.getUnidadMedida());
        catalogoDrogasView.setActivo(catalogoDrogas.getActivo());

        logger.debug("convertir CatalogoDrogas to View: {}", catalogoDrogasView);
        return catalogoDrogasView;

    }

}
