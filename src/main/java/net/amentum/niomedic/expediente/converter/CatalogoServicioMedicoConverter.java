package net.amentum.niomedic.expediente.converter;

import net.amentum.niomedic.expediente.model.CatalogoServicioMedico;
import net.amentum.niomedic.expediente.views.CatalogoServicioMedicoView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CatalogoServicioMedicoConverter {
    private final Logger logger = LoggerFactory.getLogger(CatalogoServicioMedicoConverter.class);

    public CatalogoServicioMedico toEntity(CatalogoServicioMedicoView catalogoServicioMedicoView, CatalogoServicioMedico catalogoServicioMedico, Boolean update) {
        catalogoServicioMedico.setNombre(catalogoServicioMedicoView.getNombre());
        catalogoServicioMedico.setActivo(catalogoServicioMedicoView.getActivo());

        logger.debug("convertir CatalogoServicioMedicoView to Entity: {}", catalogoServicioMedico);
        return catalogoServicioMedico;
    }

    public CatalogoServicioMedicoView toView(CatalogoServicioMedico catalogoServicioMedico, Boolean complete) {
        CatalogoServicioMedicoView catalogoServicioMedicoView = new CatalogoServicioMedicoView();
        catalogoServicioMedicoView.setIdCatalogoServicioMedico(catalogoServicioMedico.getIdCatalogoServicioMedico());
        catalogoServicioMedicoView.setNombre(catalogoServicioMedico.getNombre());
        catalogoServicioMedicoView.setActivo(catalogoServicioMedico.getActivo());

        logger.debug("convertir CatalogoServicioMedico to View: {}", catalogoServicioMedicoView);
        return catalogoServicioMedicoView;

    }

}
