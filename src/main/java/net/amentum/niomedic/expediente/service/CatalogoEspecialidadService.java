package net.amentum.niomedic.expediente.service;

import net.amentum.niomedic.expediente.exception.CatalogoEspecialidadException;
import net.amentum.niomedic.expediente.views.CatalogoEspecialidadView;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CatalogoEspecialidadService {
    void createCatalogoEspecialidad(CatalogoEspecialidadView catalogoEspecialidadView) throws CatalogoEspecialidadException;

    void updateCatalogoEspecialidad(CatalogoEspecialidadView catalogoEspecialidadView) throws CatalogoEspecialidadException;

    void deleteCatalogoEspecialidad(Long idCatalogoEspecialidad) throws CatalogoEspecialidadException;

    CatalogoEspecialidadView getDetailsByIdCatalogoEspecialidad(Long idCatalogoEspecialidad) throws CatalogoEspecialidadException;

    List<CatalogoEspecialidadView> findAll(Boolean active) throws CatalogoEspecialidadException;

    Page<CatalogoEspecialidadView> getCatalogoEspecialidadPage(Boolean active, String name, Integer page, Integer size, String orderColumn, String orderType) throws CatalogoEspecialidadException;
}
