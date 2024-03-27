package net.amentum.niomedic.expediente.converter;

import net.amentum.niomedic.expediente.model.CatalogoDosisVacuna;
import net.amentum.niomedic.expediente.model.CatalogoVacuna;
import net.amentum.niomedic.expediente.views.CatalogoDosisVacunaView;
import net.amentum.niomedic.expediente.views.CatalogoVacunaView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CatalogoVacunaConverter {
    private final Logger logger = LoggerFactory.getLogger(CatalogoVacunaConverter.class);

    public CatalogoVacuna toEntity(CatalogoVacunaView catalogoVacunaView, CatalogoVacuna catalogoVacuna, Boolean update) {
        catalogoVacuna.setNombreVacuna(catalogoVacunaView.getNombreVacuna());
        catalogoVacuna.setEnfermedades(catalogoVacunaView.getEnfermedades());
        catalogoVacuna.setActivo(catalogoVacunaView.getActivo());

        if (catalogoVacunaView.getCatalogoDosisVacunaView() != null) {
            CatalogoDosisVacunaView cdvView = catalogoVacunaView.getCatalogoDosisVacunaView();
            CatalogoDosisVacuna cdv = new CatalogoDosisVacuna();
            if (cdvView.getIdCatalogoDosisVacuna() != null)
                cdv.setIdCatalogoDosisVacuna(cdvView.getIdCatalogoDosisVacuna());
            cdv.setDosis(cdvView.getDosis());
            cdv.setActivo(cdvView.getActivo());
            cdv.setCatalogoVacunaId(catalogoVacuna);
            catalogoVacuna.setCatalogoDosisVacuna(cdv);
        }
/*
        if (catalogoVacunaView.getEsquemaVacunacionView() != null) {
            EsquemaVacunacionView evView = catalogoVacunaView.getEsquemaVacunacionView();
            EsquemaVacunacion ev = new EsquemaVacunacion();
            if (evView.getIdEsquemaVacunacion() != null)
                ev.setIdEsquemaVacunacion(evView.getIdEsquemaVacunacion());
            ev.setDosis(evView.getDosis());
            ev.setEdad(evView.getEdad());
            ev.setFechaCreacion((!update) ? new Date() : evView.getFechaCreacion());
            ev.setFechaEvaluacion((!update) ? new Date() : evView.getFechaEvaluacion());
            ev.setActivo(evView.getActivo());
            ev.setIdPaciente(evView.getIdPaciente());
            ev.setCatalogoVacunaId(catalogoVacuna);
            catalogoVacuna.setEsquemaVacunacion(ev);
        }
*/
        logger.debug("convertir CatalogoVacunaView to Entity: {}", catalogoVacuna);
        return catalogoVacuna;
    }

    public CatalogoVacunaView toView(CatalogoVacuna catalogoVacuna, Boolean complete) {
        CatalogoVacunaView catalogoVacunaView = new CatalogoVacunaView();
        catalogoVacunaView.setIdCatalogoVacuna(catalogoVacuna.getIdCatalogoVacuna());
        catalogoVacunaView.setNombreVacuna(catalogoVacuna.getNombreVacuna());
        catalogoVacunaView.setEnfermedades(catalogoVacuna.getEnfermedades());
        catalogoVacunaView.setActivo(catalogoVacuna.getActivo());

        CatalogoDosisVacuna cdv = catalogoVacuna.getCatalogoDosisVacuna();
        CatalogoDosisVacunaView cdvView = new CatalogoDosisVacunaView();
        cdvView.setIdCatalogoDosisVacuna(cdv.getIdCatalogoDosisVacuna()); // nuevo
        cdvView.setDosis(cdv.getDosis());
        cdvView.setActivo(cdv.getActivo());
        catalogoVacunaView.setCatalogoDosisVacunaView(cdvView);
/*
        EsquemaVacunacion ev = catalogoVacuna.getEsquemaVacunacion();
        EsquemaVacunacionView evView = new EsquemaVacunacionView();
        evView.setIdEsquemaVacunacion(ev.getIdEsquemaVacunacion()); //nuevo
        evView.setDosis(ev.getDosis());
        evView.setEdad(ev.getEdad());
        evView.setFechaEvaluacion(ev.getFechaEvaluacion());
        evView.setFechaCreacion(ev.getFechaCreacion());
        evView.setActivo(ev.getActivo());
        evView.setIdPaciente(ev.getIdPaciente());
        catalogoVacunaView.setEsquemaVacunacionView(evView);
*/
        logger.debug("convertir CatalogoVacuna to View: {}", catalogoVacunaView);
        return catalogoVacunaView;

    }

}
