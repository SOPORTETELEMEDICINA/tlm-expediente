package net.amentum.niomedic.expediente.service;

import net.amentum.niomedic.expediente.exception.CatalogoDrogasException;
import net.amentum.niomedic.expediente.views.CatalogoDrogasView;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CatalogoDrogasService {
    void createCatalogoDrogas(CatalogoDrogasView catalogoDrogasView) throws CatalogoDrogasException;

    void updateCatalogoDrogas(CatalogoDrogasView catalogoDrogasView) throws CatalogoDrogasException;

    void deleteCatalogoDrogas(Long idCatalogoDroga) throws CatalogoDrogasException;

    CatalogoDrogasView getDetailsByIdCatalogoDrogas(Long idCatalogoDroga) throws CatalogoDrogasException;

    List<CatalogoDrogasView> findAll(Boolean active) throws CatalogoDrogasException;

    Page<CatalogoDrogasView> getCatalogoDrogasPage(Boolean active, String name, Integer page, Integer size, String orderColumn, String orderType) throws CatalogoDrogasException;
}
