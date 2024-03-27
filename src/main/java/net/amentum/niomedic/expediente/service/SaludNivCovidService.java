package net.amentum.niomedic.expediente.service;

import net.amentum.niomedic.expediente.exception.SaludNivCovidException;
import net.amentum.niomedic.expediente.views.SaludNivCovidView;
import org.springframework.data.domain.Page;


import java.util.List;

public interface SaludNivCovidService {

    void createSaludNivCovid(SaludNivCovidView saludNivCovidView) throws SaludNivCovidException;

    void updateSaludNivCovid(SaludNivCovidView saludNivCovidView) throws SaludNivCovidException;

    void deleteSaludNivCovid(String pacidfk) throws SaludNivCovidException;


    List<SaludNivCovidView> findAll() throws SaludNivCovidException;

    Page<SaludNivCovidView> getSaludNivCovidSearch(String pacidfk, Integer page, Integer size, String orderColumn, String orderType) throws SaludNivCovidException;
    Page<SaludNivCovidView> getSaludNivCovidfechaSearch(String pacidfk,int periodo, String fechaInicio, String fechaFin , Integer page, Integer size, String orderColumn, String orderType) throws SaludNivCovidException;
    Page<SaludNivCovidView> getSaludNivCovidperiodoSearch(String pacidfk,int periodo,int PeriodoF, Integer page, Integer size, String orderColumn, String orderType) throws SaludNivCovidException;

}
