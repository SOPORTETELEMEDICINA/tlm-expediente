package net.amentum.niomedic.expediente.service;

import net.amentum.niomedic.expediente.exception.DatosClinicosException;
import net.amentum.niomedic.expediente.views.DatosClinicosView;
import org.springframework.data.domain.Page;

import java.util.List;

public interface DatosClinicosService {
    void createDatosClinicos(DatosClinicosView heredoFamiliarView) throws DatosClinicosException;

    void updateDatosClinicos(DatosClinicosView heredoFamiliarView) throws DatosClinicosException;

    void deleteDatosClinicos(String idPaciente) throws DatosClinicosException;

    DatosClinicosView getDetailsByIdDatosClinicos(String idPaciente) throws DatosClinicosException;

    List<DatosClinicosView> findAll(Boolean active) throws DatosClinicosException;

    Page<DatosClinicosView> getDatosClinicosPage(Boolean active, String name, Integer page, Integer size, String orderColumn, String orderType) throws DatosClinicosException;
}
