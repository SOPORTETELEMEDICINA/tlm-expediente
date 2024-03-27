package net.amentum.niomedic.expediente.rest;

import net.amentum.common.BaseController;
import net.amentum.niomedic.expediente.exception.CatalogoDosisVacunaException;
import net.amentum.niomedic.expediente.service.CatalogoDosisVacunaService;
import net.amentum.niomedic.expediente.views.CatalogoDosisVacunaView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("catalogo-vacuna")
public class CatalogoDosisVacunaRest extends BaseController {
   private final Logger logger = LoggerFactory.getLogger(CatalogoDosisVacunaRest.class);
   private CatalogoDosisVacunaService catalogoDosisVacunaService;

   @Autowired
   public void setCatalogoDosisVacunaService(CatalogoDosisVacunaService catalogoDosisVacunaService) {
      this.catalogoDosisVacunaService = catalogoDosisVacunaService;
   }

   @RequestMapping(value = "{idCatalogoVacuna}/catalogo-dosis-vacuna", method = RequestMethod.POST)
   @ResponseStatus(HttpStatus.CREATED)
   public void createCatalogoDosisVacuna(@PathVariable() Long idCatalogoVacuna, @RequestBody @Valid CatalogoDosisVacunaView catalogoDosisVacunaView) throws CatalogoDosisVacunaException {
      try {
         logger.info("===>>>Guardar nuevo Catálogo Dosis Vacuna: {}", catalogoDosisVacunaView);
         catalogoDosisVacunaService.createCatalogoDosisVacuna(catalogoDosisVacunaView, idCatalogoVacuna);
      } catch (CatalogoDosisVacunaException cdve) {
         throw cdve;
      } catch (Exception ex) {
         CatalogoDosisVacunaException cdve = new CatalogoDosisVacunaException("No fue posible agregar al Catálogo Dosis Vacuna", CatalogoDosisVacunaException.LAYER_REST, CatalogoDosisVacunaException.ACTION_INSERT);
         cdve.addError("Ocurrio un error al agregar el Catálogo Dosis Vacuna");
         logger.error("===>>>Error al insertar nuevo Catálogo Dosis Vacuna - CODE {} - {}", cdve.getExceptionCode(), cdve);
         throw cdve;
      }
   }

   @RequestMapping(value = "{idCatalogoVacuna}/catalogo-dosis-vacuna", method = RequestMethod.PUT)
   @ResponseStatus(HttpStatus.OK)
   public void updateCatalogoDosisVacuna(@PathVariable() Long idCatalogoVacuna, @RequestBody @Valid CatalogoDosisVacunaView catalogoDosisVacunaView) throws CatalogoDosisVacunaException {
      try {
         logger.info("===>>>Editar Catálogo Dosis Vacuna: {}", catalogoDosisVacunaView);
         catalogoDosisVacunaService.updateCatalogoDosisVacuna(catalogoDosisVacunaView, idCatalogoVacuna);
      } catch (CatalogoDosisVacunaException cdve) {
         throw cdve;
      } catch (Exception ex) {
         CatalogoDosisVacunaException cdve = new CatalogoDosisVacunaException("No fue posible modificar al Catálogo Dosis Vacuna", CatalogoDosisVacunaException.LAYER_REST, CatalogoDosisVacunaException.ACTION_UPDATE);
         cdve.addError("Ocurrio un error al modificar el Catálogo Dosis Vacuna");
         logger.error("===>>>Error al modificar Catálogo Dosis Vacuna - CODE {} - {}", cdve.getExceptionCode(), cdve);
         throw cdve;
      }
   }

   @RequestMapping(value = "{idCatalogoVacuna}/catalogo-dosis-vacuna", method = RequestMethod.DELETE)
   @ResponseStatus(HttpStatus.OK)
   public void deleteCatalogoDosisVacuna(@PathVariable() Long idCatalogoVacuna) throws CatalogoDosisVacunaException {
      logger.info("===>>>Eliminar Catálogo de Dosis Vacuna: {}", idCatalogoVacuna);
      catalogoDosisVacunaService.deleteCatalogoDosisVacuna(idCatalogoVacuna);
   }

   @RequestMapping(value = "{idCatalogoVacuna}/catalogo-dosis-vacuna", method = RequestMethod.GET)
   @ResponseStatus(HttpStatus.OK)
   public CatalogoDosisVacunaView getDetailsByIdCatalogoDosisVacuna(@PathVariable() Long idCatalogoVacuna) throws CatalogoDosisVacunaException {
      try {
         logger.info("===>>>Obtener detalles del Catalogo de Dosis Vacuna por Id: {}", idCatalogoVacuna);
         return catalogoDosisVacunaService.getDetailsByIdCatalogoDosisVacuna(idCatalogoVacuna);
      } catch (CatalogoDosisVacunaException cdve) {
         throw cdve;
      } catch (Exception ex) {
         CatalogoDosisVacunaException cdve = new CatalogoDosisVacunaException("No fue posible obtener los detalles  del Catálogo de Dosis Vacuna por Id", CatalogoDosisVacunaException.LAYER_REST, CatalogoDosisVacunaException.ACTION_SELECT);
         cdve.addError("Ocurrio un error al obtener los detalles del Catálogo de Dosis Vacuna por Id");
         logger.error("===>>>Error al obtener los detalles del  Catálogo de Dosis Vacuna por Id - CODE {} - {} ", cdve.getExceptionCode(), cdve);
         throw cdve;
      }
   }

   @RequestMapping(value = "catalogo-dosis-vacuna/findAll", method = RequestMethod.GET)
   @ResponseStatus(HttpStatus.OK)
   public List<CatalogoDosisVacunaView> findAll(@RequestParam(required = false) Boolean active) throws CatalogoDosisVacunaException {
      logger.info("===>>>Obtener todas la dosis de vacunas, con campo activo: {}", active);
      return catalogoDosisVacunaService.findAll(active);
   }

   @RequestMapping(value = "catalogo-dosis-vacuna/page", method = RequestMethod.GET)
   @ResponseStatus(HttpStatus.OK)
   public Page<CatalogoDosisVacunaView> getCatalogoDosisVacunaPage(@RequestParam(required = false) Boolean active,
                                                                   @RequestParam(required = false, defaultValue = "") String name,
                                                                   @RequestParam(required = false) Integer page,
                                                                   @RequestParam(required = false) Integer size,
                                                                   @RequestParam(required = false) String orderColumn,
                                                                   @RequestParam(required = false) String orderType) throws CatalogoDosisVacunaException {
      logger.info("===>>>Obtener listado de Catálogo de Dosis Vacuna paginable: active: {} - name {} - page: {} - size: {} - orderColumn: {} - orderType: {}",
         active, name, page, size, orderColumn, orderType);
      if (page == null)
         page = 0;
      if (size == null)
         size = 10;
      if (orderType == null && orderType.isEmpty())
         orderType = "asc";
      if (orderColumn == null || orderColumn.isEmpty())
         orderColumn = "dosis";

      return catalogoDosisVacunaService.getCatalogoDosisVacunaPage(active, name != null ? name : "", page, size, orderColumn, orderType);
   }

}
