package net.amentum.niomedic.expediente.service;

import net.amentum.niomedic.expediente.exception.CatalogoServicioMedicoException;
import net.amentum.niomedic.expediente.views.CatalogoServicioMedicoView;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CatalogoServicioMedicoService {
    void createCatalogoServicioMedico(CatalogoServicioMedicoView catalogoServicioMedicoView) throws CatalogoServicioMedicoException;

    void updateCatalogoServicioMedico(CatalogoServicioMedicoView catalogoServicioMedicoView) throws CatalogoServicioMedicoException;

    void deleteCatalogoServicioMedico(Long idCatalogoServicioMedico) throws CatalogoServicioMedicoException;

    CatalogoServicioMedicoView getDetailsByIdCatalogoServicioMedico(Long idCatalogoServicioMedico) throws CatalogoServicioMedicoException;

    List<CatalogoServicioMedicoView> findAll(Boolean active) throws CatalogoServicioMedicoException;

    Page<CatalogoServicioMedicoView> getCatalogoServicioMedicoPage(Boolean active, String name, Integer page, Integer size, String orderColumn, String orderType) throws CatalogoServicioMedicoException;

}
