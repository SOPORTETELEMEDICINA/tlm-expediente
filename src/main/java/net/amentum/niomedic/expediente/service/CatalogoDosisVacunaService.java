package net.amentum.niomedic.expediente.service;

import net.amentum.niomedic.expediente.exception.CatalogoDosisVacunaException;
import net.amentum.niomedic.expediente.views.CatalogoDosisVacunaView;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CatalogoDosisVacunaService {
    void createCatalogoDosisVacuna(CatalogoDosisVacunaView catalogoEspecialidadView, Long idCatalogoVacuna) throws CatalogoDosisVacunaException;

    void updateCatalogoDosisVacuna(CatalogoDosisVacunaView catalogoEspecialidadView, Long idCatalogoVacuna) throws CatalogoDosisVacunaException;

    void deleteCatalogoDosisVacuna(Long idCatalogoVacuna) throws CatalogoDosisVacunaException;

    CatalogoDosisVacunaView getDetailsByIdCatalogoDosisVacuna(Long idCatalogoVacuna) throws CatalogoDosisVacunaException;

    List<CatalogoDosisVacunaView> findAll(Boolean active) throws CatalogoDosisVacunaException;

    Page<CatalogoDosisVacunaView> getCatalogoDosisVacunaPage(Boolean active, String name, Integer page, Integer size, String orderColumn, String orderType) throws CatalogoDosisVacunaException;
}
