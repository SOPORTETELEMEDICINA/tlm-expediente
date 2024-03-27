package net.amentum.niomedic.expediente.converter;

import net.amentum.niomedic.expediente.model.CatalogoInstitucionMedica;
import net.amentum.niomedic.expediente.views.CatalogoInstitucionMedicaView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CatalogoInstitucionMedicaConverter {
    private final Logger logger = LoggerFactory.getLogger(CatalogoInstitucionMedicaConverter.class);

    public CatalogoInstitucionMedica toEntity(CatalogoInstitucionMedicaView catalogoInstitucionMedicaView, CatalogoInstitucionMedica catalogoInstitucionMedica, Boolean update) {
        catalogoInstitucionMedica.setClave(catalogoInstitucionMedicaView.getClave());
        catalogoInstitucionMedica.setClaveCorta(catalogoInstitucionMedicaView.getClaveCorta());
        catalogoInstitucionMedica.setNombre(catalogoInstitucionMedicaView.getNombre());
        catalogoInstitucionMedica.setActivo(catalogoInstitucionMedicaView.getActivo());

        logger.debug("convertir CatalogoInstitucionMedicaView to Entity: {}", catalogoInstitucionMedica);
        return catalogoInstitucionMedica;
    }

    public CatalogoInstitucionMedicaView toView(CatalogoInstitucionMedica catalogoInstitucionMedica, Boolean complete) {
        CatalogoInstitucionMedicaView catalogoInstitucionMedicaView = new CatalogoInstitucionMedicaView();
        catalogoInstitucionMedicaView.setIdCatalogoInstitucionMedica(catalogoInstitucionMedica.getIdCatalogoInstitucionMedica());
        catalogoInstitucionMedicaView.setClave(catalogoInstitucionMedica.getClave());
        catalogoInstitucionMedicaView.setClaveCorta(catalogoInstitucionMedica.getClaveCorta());
        catalogoInstitucionMedicaView.setNombre(catalogoInstitucionMedica.getNombre());
        catalogoInstitucionMedicaView.setActivo(catalogoInstitucionMedica.getActivo());

        logger.debug("convertir CatalogoInstitucionMedica to View: {}", catalogoInstitucionMedicaView);
        return catalogoInstitucionMedicaView;

    }

}
