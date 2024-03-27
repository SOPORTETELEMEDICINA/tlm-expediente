package net.amentum.niomedic.expediente.service;

import net.amentum.niomedic.expediente.exception.SaludNivPreartException;
import net.amentum.niomedic.expediente.exception.SaludNivPreartException;
import net.amentum.niomedic.expediente.views.SaludNivPreartView;
import net.amentum.niomedic.expediente.views.SaludNivPreartView;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SaludNivPreartService {

    void createSaludNivPreart(SaludNivPreartView saludNivPreartView) throws SaludNivPreartException;

    void updateSaludNivPreart(SaludNivPreartView saludNivPreartView) throws SaludNivPreartException;

    void deleteSaludNivPreart(String pacidfk) throws SaludNivPreartException;

   
    List<SaludNivPreartView> findAll() throws SaludNivPreartException;

    Page<SaludNivPreartView> getSaludNivPreartSearch(String pacidfk, Integer page, Integer size, String orderColumn, String orderType) throws SaludNivPreartException;
    Page<SaludNivPreartView> getSaludNivPreartfechaSearch(String pacidfk,int periodo, String fechaInicio, String fechaFin , Integer page, Integer size, String orderColumn, String orderType) throws SaludNivPreartException;

}
