package net.amentum.niomedic.expediente.service;

import net.amentum.niomedic.expediente.exception.SaludIndNutriException;
import net.amentum.niomedic.expediente.views.SaludIndNutriView;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SaludIndNutriService {

    void createSaludIndNutri(SaludIndNutriView saludIndNutriView) throws SaludIndNutriException;

    void updateSaludIndNutri(SaludIndNutriView saludIndNutriView) throws SaludIndNutriException;

    void deleteSaludIndNutri(String pacidfk) throws SaludIndNutriException;

    SaludIndNutriView getDetailsByPacidfk(String pacidfk) throws SaludIndNutriException;

    List<SaludIndNutriView> findAll() throws SaludIndNutriException;

    Page<SaludIndNutriView> getSaludIndNutriPage(Boolean active, String name, Integer page, Integer size, String orderColumn, String orderType) throws SaludIndNutriException;

}
