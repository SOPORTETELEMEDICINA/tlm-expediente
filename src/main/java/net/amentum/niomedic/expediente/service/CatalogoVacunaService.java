package net.amentum.niomedic.expediente.service;

import net.amentum.niomedic.expediente.exception.CatalogoVacunaException;
import net.amentum.niomedic.expediente.views.CatalogoVacunaView;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CatalogoVacunaService {
    void createCatalogoVacuna(CatalogoVacunaView catalogoVacunaView) throws CatalogoVacunaException;

    void updateCatalogoVacuna(CatalogoVacunaView catalogoVacunaView) throws CatalogoVacunaException;

    void deleteCatalogoVacuna(Long idCatalogoVacuna) throws CatalogoVacunaException;

    CatalogoVacunaView getDetailsByIdCatalogoVacuna(Long idCatalogoVacuna) throws CatalogoVacunaException;

    List<CatalogoVacunaView> findAll(Boolean active) throws CatalogoVacunaException;

    Page<CatalogoVacunaView> getCatalogoVacunaPage(Boolean active, String name, Integer page, Integer size, String orderColumn, String orderType) throws CatalogoVacunaException;
}
