package net.amentum.niomedic.expediente.converter;

import net.amentum.niomedic.expediente.model.CatalogoEspecialidad;
import net.amentum.niomedic.expediente.views.CatalogoEspecialidadView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CatalogoEspecialidadConverter {
    private final Logger logger = LoggerFactory.getLogger(CatalogoEspecialidadConverter.class);

    public CatalogoEspecialidad toEntity(CatalogoEspecialidadView catalogoEspecialidadView, CatalogoEspecialidad catalogoEspecialidad, Boolean update) {
        catalogoEspecialidad.setClaveEspecialidad(catalogoEspecialidadView.getClaveEspecialidad());
        catalogoEspecialidad.setDescripcionEspecialidad(catalogoEspecialidadView.getDescripcionEspecialidad());
        catalogoEspecialidad.setActivo(catalogoEspecialidadView.getActivo());

        logger.debug("convertir CatalogoEspecialidadView to Entity: {}", catalogoEspecialidad);
        return catalogoEspecialidad;
    }

    public CatalogoEspecialidadView toView(CatalogoEspecialidad catalogoEspecialidad, Boolean complete) {
        CatalogoEspecialidadView catalogoEspecialidadView = new CatalogoEspecialidadView();
        catalogoEspecialidadView.setIdCatalogoEspecialidad(catalogoEspecialidad.getIdCatalogoEspecialidad());
        catalogoEspecialidadView.setClaveEspecialidad(catalogoEspecialidad.getClaveEspecialidad());
        catalogoEspecialidadView.setDescripcionEspecialidad(catalogoEspecialidad.getDescripcionEspecialidad());
        catalogoEspecialidadView.setActivo(catalogoEspecialidad.getActivo());

        logger.debug("convertir CatalogoEspecialidad to View: {}", catalogoEspecialidadView);
        return catalogoEspecialidadView;

    }

}
