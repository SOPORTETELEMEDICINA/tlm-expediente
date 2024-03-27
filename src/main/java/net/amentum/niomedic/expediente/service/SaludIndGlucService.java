package net.amentum.niomedic.expediente.service;

import net.amentum.niomedic.expediente.exception.SaludIndGlucException;
import net.amentum.niomedic.expediente.views.SaludIndGlucView;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SaludIndGlucService {

    void createSaludIndGluc(SaludIndGlucView saludIndGlucView) throws SaludIndGlucException;

    void updateSaludIndGluc(SaludIndGlucView saludIndGlucView) throws SaludIndGlucException;

    void deleteSaludIndGluc(String pacidfk) throws SaludIndGlucException;

    SaludIndGlucView getDetailsByPacidfk(String pacidfk) throws SaludIndGlucException;

    List<SaludIndGlucView> findAll() throws SaludIndGlucException;

    Page<SaludIndGlucView> getSaludIndGlucPage(Boolean active, String name, Integer page, Integer size, String orderColumn, String orderType) throws SaludIndGlucException;

}
