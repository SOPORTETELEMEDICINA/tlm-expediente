package net.amentum.niomedic.expediente.service;

import net.amentum.niomedic.expediente.exception.SaludIndCovidException;
import net.amentum.niomedic.expediente.views.SaludIndCovidView;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SaludIndCovidService {

    void createSaludIndCovid(SaludIndCovidView saludIndCovidView) throws SaludIndCovidException;

    void updateSaludIndCovid(SaludIndCovidView saludIndCovidView) throws SaludIndCovidException;

    void deleteSaludIndCovid(String pacidfk) throws SaludIndCovidException;

    SaludIndCovidView getDetailsByPacidfk(String pacidfk) throws SaludIndCovidException;

    List<SaludIndCovidView> findAll() throws SaludIndCovidException;

    Page<SaludIndCovidView> getSaludIndCovidPage(Boolean active, String name, Integer page, Integer size, String orderColumn, String orderType) throws SaludIndCovidException;


}
