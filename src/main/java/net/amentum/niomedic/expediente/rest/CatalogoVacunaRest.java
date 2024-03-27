package net.amentum.niomedic.expediente.rest;

import net.amentum.common.BaseController;
import net.amentum.niomedic.expediente.exception.CatalogoVacunaException;
import net.amentum.niomedic.expediente.service.CatalogoVacunaService;
import net.amentum.niomedic.expediente.views.CatalogoVacunaView;
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
public class CatalogoVacunaRest extends BaseController {
   private final Logger logger = LoggerFactory.getLogger(CatalogoVacunaRest.class);
   private CatalogoVacunaService catalogoVacunaService;

   @Autowired
   public void setCatalogoVacunaService(CatalogoVacunaService catalogoVacunaService) {
      this.catalogoVacunaService = catalogoVacunaService;
   }

   @RequestMapping(method = RequestMethod.POST)
   @ResponseStatus(HttpStatus.CREATED)
   public void createdCatalogoVacuna(@RequestBody @Valid CatalogoVacunaView catalogoVacunaView) throws CatalogoVacunaException {
      try {
         logger.info("===>>>Guardar nuevo Catálogo de Vacuna: {}", catalogoVacunaView);
         catalogoVacunaService.createCatalogoVacuna(catalogoVacunaView);
      } catch (CatalogoVacunaException cve) {
         throw cve;
      } catch (Exception ex) {
         CatalogoVacunaException cve = new CatalogoVacunaException("No fue posible agregar al Catálogo Vacuna", CatalogoVacunaException.LAYER_REST, CatalogoVacunaException.ACTION_INSERT);
         cve.addError("Ocurrio un error al agregar el Catálogo de Vacuna");
         logger.error("===>>>Error al insertar nuevo Catálogo de Vacuna - CODE {} - {}", cve.getExceptionCode(), cve);
         throw cve;
      }
   }

   @RequestMapping(value = "{idCatalogoVacuna}", method = RequestMethod.PUT)
   @ResponseStatus(HttpStatus.OK)
   public void updateCatalogoVacuna(@PathVariable() Long idCatalogoVacuna, @RequestBody @Valid CatalogoVacunaView catalogoVacunaView) throws CatalogoVacunaException {
      try {
         catalogoVacunaView.setIdCatalogoVacuna(idCatalogoVacuna);
         logger.info("===>>>Editar Catálogo de Vacuna: {}", catalogoVacunaView);
         catalogoVacunaService.updateCatalogoVacuna(catalogoVacunaView);
      } catch (CatalogoVacunaException cve) {
         throw cve;
      } catch (Exception ex) {
         CatalogoVacunaException cve = new CatalogoVacunaException("No fue posible modificar al Catálogo Vacuna", CatalogoVacunaException.LAYER_REST, CatalogoVacunaException.ACTION_UPDATE);
         cve.addError("Ocurrio un error al modificar el Catálogo de Vacuna");
         logger.error("===>>>Error al modificar Catálogo de Vacuna - CODE {} - {}", cve.getExceptionCode(), cve);
         throw cve;
      }
   }

   @RequestMapping(value = "{idCatalogoVacuna}", method = RequestMethod.DELETE)
   @ResponseStatus(HttpStatus.OK)
   public void deleteCatalogoVacuna(@PathVariable() Long idCatalogoVacuna) throws CatalogoVacunaException {
      logger.info("===>>>Eliminar Catálogo de Vacuna: {}", idCatalogoVacuna);
      catalogoVacunaService.deleteCatalogoVacuna(idCatalogoVacuna);
   }

   @RequestMapping(value = "{idCatalogoVacuna}", method = RequestMethod.GET)
   @ResponseStatus(HttpStatus.OK)
   public CatalogoVacunaView getDetailsByIdCatalogoVacuna(@PathVariable() Long idCatalogoVacuna) throws CatalogoVacunaException {
      try {
         logger.info("===>>>Obtener detalles del Catalogo de Vacuna por Id: {}", idCatalogoVacuna);
         return catalogoVacunaService.getDetailsByIdCatalogoVacuna(idCatalogoVacuna);
      } catch (CatalogoVacunaException cve) {
         throw cve;
      } catch (Exception ex) {
         CatalogoVacunaException cve = new CatalogoVacunaException("No fue posible obtener los detalles  del Catálogo de Vacuna por Id", CatalogoVacunaException.LAYER_REST, CatalogoVacunaException.ACTION_SELECT);
         cve.addError("Ocurrio un error al obtener los detalles del Catálogo de Vacuna por Id");
         logger.error("===>>>Error al obtener los detalles del  Catálogo de Vacuna por Id - CODE {} - {} ", cve.getExceptionCode(), cve);
         throw cve;
      }
   }

   @RequestMapping(value = "findAll", method = RequestMethod.GET)
   @ResponseStatus(HttpStatus.OK)
   public List<CatalogoVacunaView> findAll(@RequestParam(required = false) Boolean active) throws CatalogoVacunaException {
      logger.info("===>>>Obtener todas la vacunas, con campo activo: {}", active);
      return catalogoVacunaService.findAll(active);
   }

   @RequestMapping(value = "page", method = RequestMethod.GET)
   @ResponseStatus(HttpStatus.OK)
   public Page<CatalogoVacunaView> getCatalogoVacunaPage(@RequestParam(required = false) Boolean active,
                                                         @RequestParam(required = false, defaultValue = "") String name,
                                                         @RequestParam(required = false) Integer page,
                                                         @RequestParam(required = false) Integer size,
                                                         @RequestParam(required = false) String orderColumn,
                                                         @RequestParam(required = false) String orderType) throws CatalogoVacunaException {

      logger.info("===>>>Obtener listado de Catálogo de Vacuna paginable: active: {} - name {} - page: {} - size: {} - orderColumn: {} - orderType: {}",
         active, name, page, size, orderColumn, orderType);
      if (page == null)
         page = 0;
      if (size == null)
         size = 10;
      if (orderType == null || orderType.isEmpty())
         orderType = "asc";
      if (orderColumn == null || orderColumn.isEmpty())
         orderColumn = "nombreVacuna";

      return catalogoVacunaService.getCatalogoVacunaPage(active, name != null ? name : "", page, size, orderColumn, orderType);
   }
}
