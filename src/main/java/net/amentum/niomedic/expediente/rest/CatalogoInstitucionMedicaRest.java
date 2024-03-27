package net.amentum.niomedic.expediente.rest;

import net.amentum.common.BaseController;

import net.amentum.niomedic.expediente.exception.CatalogoInstitucionMedicaException;
import net.amentum.niomedic.expediente.service.CatalogoInstitucionMedicaService;
import net.amentum.niomedic.expediente.views.CatalogoInstitucionMedicaView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("catalogo-institucion-medica")
public class CatalogoInstitucionMedicaRest extends BaseController {
    private final Logger logger = LoggerFactory.getLogger(CatalogoInstitucionMedicaRest.class);
    private CatalogoInstitucionMedicaService catalogoInstitucionMedicaService;

    @Autowired
    public void setCatalogoInstitucionMedicaService(CatalogoInstitucionMedicaService catalogoInstitucionMedicaService) {
        this.catalogoInstitucionMedicaService = catalogoInstitucionMedicaService;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void createdCatalogoInstitucionMedica(@RequestBody @Valid CatalogoInstitucionMedicaView catalogoInstitucionMedicaView) throws CatalogoInstitucionMedicaException {
        try {
            logger.info("===>>>Guardar nuevo Catálogo de Institución Médica: {}", catalogoInstitucionMedicaView);
            catalogoInstitucionMedicaService.createCatalogoInstitucionMedica(catalogoInstitucionMedicaView);
        } catch (CatalogoInstitucionMedicaException cime) {
            throw cime;
        } catch (Exception ex) {
            CatalogoInstitucionMedicaException cime = new CatalogoInstitucionMedicaException("No fue posible agregar al Catálogo del Institución Médica", CatalogoInstitucionMedicaException.LAYER_REST, CatalogoInstitucionMedicaException.ACTION_INSERT);
            cime.addError("Ocurrio un error al agregar el Catálogo de Institución Médica");
            logger.error("===>>>Error al insertar nuevo Catálogo de Institución Médica - CODE {} - {}", cime.getExceptionCode(), cime);
            throw cime;
        }
    }

    @RequestMapping(value = "{idCatalogoInstitucionMedica}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void updateCatalogoInstitucionMedica(@PathVariable() Long idCatalogoInstitucionMedica, @RequestBody @Valid CatalogoInstitucionMedicaView catalogoInstitucionMedicaView) throws CatalogoInstitucionMedicaException {
        try {
            catalogoInstitucionMedicaView.setIdCatalogoInstitucionMedica(idCatalogoInstitucionMedica);
            logger.info("===>>>Editar Catálogo de Institución Médica: {}", catalogoInstitucionMedicaView);
            catalogoInstitucionMedicaService.updateCatalogoInstitucionMedica(catalogoInstitucionMedicaView);
        } catch (CatalogoInstitucionMedicaException cime) {
            throw cime;
        } catch (Exception ex) {
            CatalogoInstitucionMedicaException cime = new CatalogoInstitucionMedicaException("No fue posible modificar al Catálogo del Institución Médica", CatalogoInstitucionMedicaException.LAYER_REST, CatalogoInstitucionMedicaException.ACTION_UPDATE);
            cime.addError("Ocurrio un error al modificar el Catálogo de Institución Médica");
            logger.error("===>>>Error al modificar Catálogo de Institución Médica - CODE {} - {}", cime.getExceptionCode(), cime);
            throw cime;
        }
    }

    @RequestMapping(value = "{idCatalogoInstitucionMedica}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void deleteCatalogoInstitucionMedica(@PathVariable() Long idCatalogoInstitucionMedica) throws CatalogoInstitucionMedicaException {
        logger.info("===>>>Eliminar Catálogo de Institución Médica: {}", idCatalogoInstitucionMedica);
        catalogoInstitucionMedicaService.deleteCatalogoInstitucionMedica(idCatalogoInstitucionMedica);
    }

    @RequestMapping(value = "{idCatalogoInstitucionMedica}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public CatalogoInstitucionMedicaView getDetailsByIdCatalogoInstitucionMedica(@PathVariable() Long idCatalogoInstitucionMedica) throws CatalogoInstitucionMedicaException {
        try {
            logger.info("===>>>Obtener detalles del Catalogo de Institución Médica por Id: {}", idCatalogoInstitucionMedica);
            return catalogoInstitucionMedicaService.getDetailsByIdCatalogoInstitucionMedica(idCatalogoInstitucionMedica);
        } catch (CatalogoInstitucionMedicaException cime) {
            throw cime;
        } catch (Exception ex) {
            CatalogoInstitucionMedicaException cime = new CatalogoInstitucionMedicaException("No fue posible obtener los detalles  del Catálogo de Institución Médica por Id", CatalogoInstitucionMedicaException.LAYER_REST, CatalogoInstitucionMedicaException.ACTION_SELECT);
            cime.addError("Ocurrio un error al obtener los detalles del Catálogo de Institución Médica por Id");
            logger.error("===>>>Error al obtener los detalles del  Catálogo de Institución Médica por Id - CODE {} - {} ", cime.getExceptionCode(), cime);
            throw cime;
        }
    }

    @RequestMapping(value = "findAll", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<CatalogoInstitucionMedicaView> findAll(@RequestParam(required = false) Boolean active) throws CatalogoInstitucionMedicaException {
       logger.info("===>>>Obtener todas la Institucion Medica, con campo activo: {}", active);
        return catalogoInstitucionMedicaService.findAll(active);
    }

    @RequestMapping(value = "page", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Page<CatalogoInstitucionMedicaView> getCatalogoInstitucionMedicaPage(@RequestParam(required = false) Boolean active,
                                                                                @RequestParam(required = false, defaultValue = "") String name,
                                                                                @RequestParam(required = false) Integer page,
                                                                                @RequestParam(required = false) Integer size,
                                                                                @RequestParam(required = false) String orderColumn,
                                                                                @RequestParam(required = false) String orderType) throws CatalogoInstitucionMedicaException {

       logger.info("===>>>Obtener listado de Institución Médica paginable: active: {} - name {} - page: {} - size: {} - orderColumn: {} - orderType: {}",
          active, name, page, size, orderColumn, orderType);
       if (page == null)
          page = 0;
       if (size == null)
          size = 10;
       if (orderType == null || orderType.isEmpty())
          orderType = "asc";
       if (orderColumn == null || orderColumn.isEmpty())
          orderColumn = "nombre";

       return catalogoInstitucionMedicaService.getCatalogoInstitucionMedicaPage(active, name != null ? name : "", page, size, orderColumn, orderType);
    }
}
