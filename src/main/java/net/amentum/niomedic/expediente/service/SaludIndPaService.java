package net.amentum.niomedic.expediente.service;

import net.amentum.niomedic.expediente.exception.SaludIndPaException;
import net.amentum.niomedic.expediente.views.SaludIndPaView;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SaludIndPaService {


    void createSaludIndPa(SaludIndPaView saludIndPaView) throws SaludIndPaException;

    void updateSaludIndPa(SaludIndPaView saludIndPaView) throws SaludIndPaException;

    void deleteSaludIndPa(String pacidfk) throws SaludIndPaException;

    SaludIndPaView getDetailsByPacidfk(String pacidfk) throws SaludIndPaException;

    List<SaludIndPaView> findAll() throws SaludIndPaException;

    Page<SaludIndPaView> getSaludIndPaPage(Boolean active, String name, Integer page, Integer size, String orderColumn, String orderType) throws SaludIndPaException;

}
