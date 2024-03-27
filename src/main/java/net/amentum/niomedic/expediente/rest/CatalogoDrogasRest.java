package net.amentum.niomedic.expediente.rest;

import net.amentum.common.BaseController;
import net.amentum.niomedic.expediente.exception.CatalogoDrogasException;
import net.amentum.niomedic.expediente.service.CatalogoDrogasService;
import net.amentum.niomedic.expediente.views.CatalogoDrogasView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("catalogo-drogas")
public class CatalogoDrogasRest extends BaseController {
    private final Logger logger = LoggerFactory.getLogger(CatalogoDrogasRest.class);
    private CatalogoDrogasService catalogoDrogasService;

    @Autowired
    public void setCatalogoDrogasService(CatalogoDrogasService catalogoDrogasService) {
        this.catalogoDrogasService = catalogoDrogasService;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void createdCatalogoDrogas(@RequestBody @Valid CatalogoDrogasView catalogoDrogasView) throws CatalogoDrogasException {
        try {
            logger.info("===>>>Guardar nuevo Catálogo Drogas: {}", catalogoDrogasView);
            catalogoDrogasService.createCatalogoDrogas(catalogoDrogasView);
        } catch (CatalogoDrogasException cde) {
            throw cde;
        } catch (Exception ex) {
            CatalogoDrogasException cde = new CatalogoDrogasException("No fue posible agregar Catálogo Drogas", CatalogoDrogasException.LAYER_REST, CatalogoDrogasException.ACTION_INSERT);
            cde.addError("Ocurrio un error al agregar Catálogo Drogas");
            logger.error("===>>>Error al insertar nuevo Catálogo Drogas - CODE {} - {}", cde.getExceptionCode(), cde);
            throw cde;
        }
    }

    @RequestMapping(value = "{idCatalogoDroga}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void updateCatalogoDrogas(@PathVariable() Long idCatalogoDroga, @RequestBody @Valid CatalogoDrogasView catalogoDrogasView) throws CatalogoDrogasException {
        try {
            catalogoDrogasView.setIdCatalogoDrogas(idCatalogoDroga);
            logger.info("===>>>Editar Catálogo Drogas: {}", catalogoDrogasView);
            catalogoDrogasService.updateCatalogoDrogas(catalogoDrogasView);
        } catch (CatalogoDrogasException cde) {
            throw cde;
        } catch (Exception ex) {
            CatalogoDrogasException cde = new CatalogoDrogasException("No fue posible modificar Catálogo Drogas", CatalogoDrogasException.LAYER_REST, CatalogoDrogasException.ACTION_UPDATE);
            cde.addError("Ocurrio un error al modificar Catálogo Drogas");
            logger.error("===>>>Error al modificar Catálogo Drogas - CODE {} - {}", cde.getExceptionCode(), cde);
            throw cde;
        }
    }

    @RequestMapping(value = "{idCatalogoDroga}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void deleteCatalogoDrogas(@PathVariable() Long idCatalogoDroga) throws CatalogoDrogasException {
        logger.info("===>>>Eliminar Catálogo Drogas: {}", idCatalogoDroga);
        catalogoDrogasService.deleteCatalogoDrogas(idCatalogoDroga);
    }

    @RequestMapping(value = "{idCatalogoDroga}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public CatalogoDrogasView getDetailsByIdCatalogoDrogas(@PathVariable() Long idCatalogoDroga) throws CatalogoDrogasException {
        try {
            logger.info("===>>>Obtener detalles Catálogo de Drogas por Id: {}", idCatalogoDroga);
            return catalogoDrogasService.getDetailsByIdCatalogoDrogas(idCatalogoDroga);
        } catch (CatalogoDrogasException cde) {
            throw cde;
        } catch (Exception ex) {
            CatalogoDrogasException cde = new CatalogoDrogasException("No fue posible obtener los detalles Catálogo Drogas por Id", CatalogoDrogasException.LAYER_REST, CatalogoDrogasException.ACTION_SELECT);
            cde.addError("Ocurrio un error al obtener los detalles Catálogo Drogas por Id");
            logger.error("===>>>Error al obtener los detalles Catálogo Drogas por Id - CODE {} - {} ", cde.getExceptionCode(), cde);
            throw cde;
        }
    }

    @RequestMapping(value = "findAll", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<CatalogoDrogasView> findAll(@RequestParam(required = false) Boolean active) throws CatalogoDrogasException {
        return catalogoDrogasService.findAll(active);
    }

    @RequestMapping(value = "page", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Page<CatalogoDrogasView> getCatalogoDrogasPage(@RequestParam(required = false) Boolean active,
                                                          @RequestParam(required = false, defaultValue = "") String name,
                                                          @RequestParam(required = false) Integer page,
                                                          @RequestParam(required = false) Integer size,
                                                          @RequestParam(required = false) String orderColumn,
                                                          @RequestParam(required = false) String orderType) throws CatalogoDrogasException {
       logger.info("===>>>Obtener listado de Catálogo de Vacuna paginable: active: {} - name {} - page: {} - size: {} - orderColumn: {} - orderType: {}",
          active, name, page, size, orderColumn, orderType);
       if (page == null)
          page = 0;
       if (size == null)
          size = 10;
       if (orderType == null || orderType.isEmpty())
          orderType = "asc";
       if (orderColumn == null || orderColumn.isEmpty())
          orderColumn = "nombreDroga";

        return catalogoDrogasService.getCatalogoDrogasPage(active, name != null ? name : "", page, size, orderColumn, orderType);
    }
}
