package net.amentum.niomedic.expediente.converter;

import net.amentum.niomedic.expediente.model.CatalogoDosisVacuna;
import net.amentum.niomedic.expediente.views.CatalogoDosisVacunaView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CatalogoDosisVacunaConverter {
    private final Logger logger = LoggerFactory.getLogger(CatalogoDosisVacunaConverter.class);

    public CatalogoDosisVacuna toEntity(CatalogoDosisVacunaView catalogoDosisVacunaView, CatalogoDosisVacuna catalogoDosisVacuna, Boolean update) {
        catalogoDosisVacuna.setDosis(catalogoDosisVacunaView.getDosis());
        catalogoDosisVacuna.setActivo(catalogoDosisVacunaView.getActivo());
        logger.info("---> convertir CatalogoDosisVacunaView to Entity: {}", catalogoDosisVacuna);
        return catalogoDosisVacuna;
    }

    public CatalogoDosisVacunaView toView(CatalogoDosisVacuna catalogoDosisVacuna, Boolean complete) {
        CatalogoDosisVacunaView catalogoDosisVacunaView = new CatalogoDosisVacunaView();
        catalogoDosisVacunaView.setIdCatalogoDosisVacuna(catalogoDosisVacuna.getIdCatalogoDosisVacuna());
        catalogoDosisVacunaView.setDosis(catalogoDosisVacuna.getDosis());
        catalogoDosisVacunaView.setActivo(catalogoDosisVacuna.getActivo());
        logger.debug("convertir CatalogoDosisVacuna to View: {}", catalogoDosisVacunaView);
        return catalogoDosisVacunaView;

    }

}
