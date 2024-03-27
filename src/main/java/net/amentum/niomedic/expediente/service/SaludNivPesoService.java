package net.amentum.niomedic.expediente.service;

import net.amentum.niomedic.expediente.exception.SaludNivPesoException;
import net.amentum.niomedic.expediente.views.SaludNivPesoView;

import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;


public interface SaludNivPesoService {

    void createSaludNivPeso(SaludNivPesoView saludNivPesoView) throws SaludNivPesoException;

    void updateSaludNivPeso(SaludNivPesoView saludNivPesoView) throws SaludNivPesoException;

    void deleteSaludNivPeso(String pacidfk) throws SaludNivPesoException;


    List<SaludNivPesoView> findAll() throws SaludNivPesoException;

     Page<SaludNivPesoView> getSaludNivPesoSearch(String pacidfk, Integer page, Integer size, String orderColumn, String orderType) throws SaludNivPesoException;
    Page<SaludNivPesoView> getSaludNivPesofechaSearch(String pacidfk,int periodo, String fechaInicio, String fechaFin , Integer page, Integer size, String orderColumn, String orderType) throws SaludNivPesoException;
}
