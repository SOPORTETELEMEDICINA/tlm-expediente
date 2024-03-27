package net.amentum.niomedic.expediente.service;

import net.amentum.niomedic.expediente.exception.CatalogoInstitucionMedicaException;
import net.amentum.niomedic.expediente.views.CatalogoInstitucionMedicaView;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CatalogoInstitucionMedicaService {
    void createCatalogoInstitucionMedica(CatalogoInstitucionMedicaView catalogoInstitucionMedicaView) throws CatalogoInstitucionMedicaException;

    void updateCatalogoInstitucionMedica(CatalogoInstitucionMedicaView catalogoInstitucionMedicaView) throws CatalogoInstitucionMedicaException;

    void deleteCatalogoInstitucionMedica(Long idCatalogoInstitucionMedica) throws CatalogoInstitucionMedicaException;

    CatalogoInstitucionMedicaView getDetailsByIdCatalogoInstitucionMedica(Long idCatalogoInstitucionMedica) throws CatalogoInstitucionMedicaException;

    List<CatalogoInstitucionMedicaView> findAll(Boolean active) throws CatalogoInstitucionMedicaException;

    Page<CatalogoInstitucionMedicaView> getCatalogoInstitucionMedicaPage(Boolean active, String name, Integer page, Integer size, String orderColumn, String orderType) throws CatalogoInstitucionMedicaException;
}
