package net.amentum.niomedic.expediente.service;

import net.amentum.niomedic.expediente.exception.SaludNivGlucException;
import net.amentum.niomedic.expediente.views.SaludNivGlucView;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SaludNivGlucService {

    void createSaludNivGluc(SaludNivGlucView saludNivGlucView) throws SaludNivGlucException;

    void updateSaludNivGluc(SaludNivGlucView saludNivGlucView) throws SaludNivGlucException;

    void deleteSaludNivGluc(String pacidfk) throws SaludNivGlucException;

   

    List<SaludNivGlucView> findAll() throws SaludNivGlucException;
    
    Page<SaludNivGlucView> getSaludNivGlucSearch(String pacidfk, Integer page, Integer size, String orderColumn, String orderType) throws SaludNivGlucException;
    Page<SaludNivGlucView> getSaludNivGlucfechaSearch(String pacidfk,int periodo, String fechaInicio, String fechaFin , Integer page, Integer size, String orderColumn, String orderType) throws SaludNivGlucException;

}
