package net.amentum.niomedic.expediente.rest;

import net.amentum.common.BaseController;
import net.amentum.niomedic.expediente.exception.CatalogoEspecialidadException;
import net.amentum.niomedic.expediente.service.CatalogoEspecialidadService;
import net.amentum.niomedic.expediente.views.CatalogoEspecialidadView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("catalogo-especialidad")
public class CatalogoEspecialidadRest extends BaseController {
    private final Logger logger = LoggerFactory.getLogger(CatalogoEspecialidadRest.class);
    private CatalogoEspecialidadService catalogoEspecialidadService;

    @Autowired
    public void setCatalogoEspecialidadService(CatalogoEspecialidadService catalogoEspecialidadService) {
        this.catalogoEspecialidadService = catalogoEspecialidadService;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void createdCatalogoEspecialidad(@RequestBody @Valid CatalogoEspecialidadView catalogoEspecialidadView) throws CatalogoEspecialidadException {
        try {
            logger.info("===>>>Guardar nuevo Catálogo de Especialidad: {}", catalogoEspecialidadView);
            catalogoEspecialidadService.createCatalogoEspecialidad(catalogoEspecialidadView);
        } catch (CatalogoEspecialidadException cee) {
            throw cee;
        } catch (Exception ex) {
            CatalogoEspecialidadException cee = new CatalogoEspecialidadException("No fue posible agregar al Catálogo del Especialidad", CatalogoEspecialidadException.LAYER_REST, CatalogoEspecialidadException.ACTION_INSERT);
            cee.addError("Ocurrio un error al agregar el Catálogo de Especialidad");
            logger.error("===>>>Error al insertar nuevo Catálogo de Especialidad - CODE {} - {}", cee.getExceptionCode(), cee);
            throw cee;
        }
    }

    @RequestMapping(value = "{idCatalogoEspecialidad}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void updateCatalogoEspecialidad(@PathVariable() Long idCatalogoEspecialidad, @RequestBody @Valid CatalogoEspecialidadView catalogoEspecialidadView) throws CatalogoEspecialidadException {
        try {
            catalogoEspecialidadView.setIdCatalogoEspecialidad(idCatalogoEspecialidad);
            logger.info("===>>>Editar Catálogo de Especialidad: {}", catalogoEspecialidadView);
            catalogoEspecialidadService.updateCatalogoEspecialidad(catalogoEspecialidadView);
        } catch (CatalogoEspecialidadException cee) {
            throw cee;
        } catch (Exception ex) {
            CatalogoEspecialidadException cee = new CatalogoEspecialidadException("No fue posible modificar al Catálogo del Especialidad", CatalogoEspecialidadException.LAYER_REST, CatalogoEspecialidadException.ACTION_UPDATE);
            cee.addError("Ocurrio un error al modificar el Catálogo de Especialidad");
            logger.error("===>>>Error al modificar Catálogo de Especialidad - CODE {} - {}", cee.getExceptionCode(), cee);
            throw cee;
        }
    }

    @RequestMapping(value = "{idCatalogoEspecialidad}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void deleteCatalogoEspecialidad(@PathVariable() Long idCatalogoEspecialidad) throws CatalogoEspecialidadException {
        logger.info("===>>>Eliminar Catálogo de Especialidad: {}", idCatalogoEspecialidad);
        catalogoEspecialidadService.deleteCatalogoEspecialidad(idCatalogoEspecialidad);
    }

    @RequestMapping(value = "{idCatalogoEspecialidad}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public CatalogoEspecialidadView getDetailsByIdCatalogoEspecialidad(@PathVariable() Long idCatalogoEspecialidad) throws CatalogoEspecialidadException {
        try {
            logger.info("===>>>Obtener detalles del Catalogo de Especialidad por Id: {}", idCatalogoEspecialidad);
            return catalogoEspecialidadService.getDetailsByIdCatalogoEspecialidad(idCatalogoEspecialidad);
        } catch (CatalogoEspecialidadException cee) {
            throw cee;
        } catch (Exception ex) {
            CatalogoEspecialidadException cee = new CatalogoEspecialidadException("No fue posible obtener los detalles  del Catálogo de Especialidad por Id", CatalogoEspecialidadException.LAYER_REST, CatalogoEspecialidadException.ACTION_SELECT);
            cee.addError("Ocurrio un error al obtener los detalles del Catálogo de Especialidad por Id");
            logger.error("===>>>Error al obtener los detalles del  Catálogo de Especialidad por Id - CODE {} - {} ", cee.getExceptionCode(), cee);
            throw cee;
        }
    }

    @RequestMapping(value = "findAll", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<CatalogoEspecialidadView> findAll(@RequestParam(required = false) Boolean active) throws CatalogoEspecialidadException {
       logger.info("===>>>Obtener todas las especialidades, con campo activo: {}", active);
        return catalogoEspecialidadService.findAll(active);
    }

    @RequestMapping(value = "page", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Page<CatalogoEspecialidadView> getCatalogoEspecialidadPage(@RequestParam(required = false) Boolean active,
                                                                      @RequestParam(required = false, defaultValue = "") String name,
                                                                      @RequestParam(required = false) Integer page,
                                                                      @RequestParam(required = false) Integer size,
                                                                      @RequestParam(required = false) String orderColumn,
                                                                      @RequestParam(required = false) String orderType) throws CatalogoEspecialidadException {

       logger.info("===>>>Obtener listado de Catálogo de Especialidad paginable: active: {} - name {} - page: {} - size: {} - orderColumn: {} - orderType: {}",
          active, name, page, size, orderColumn, orderType);
       if (page == null)
          page = 0;
       if (size == null)
          size = 10;
       if (orderType == null || orderType.isEmpty())
          orderType = "asc";
       if (orderColumn == null || orderColumn.isEmpty())
          orderColumn = "descripcionEspecialidad";

        return catalogoEspecialidadService.getCatalogoEspecialidadPage(active, name != null ? name : "", page, size, orderColumn, orderType);
    }

}
