package net.amentum.niomedic.expediente.rest;

import net.amentum.common.BaseController;
import net.amentum.niomedic.expediente.exception.CatalogoServicioMedicoException;
import net.amentum.niomedic.expediente.service.CatalogoServicioMedicoService;
import net.amentum.niomedic.expediente.views.CatalogoServicioMedicoView;
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
@RequestMapping("catalogo-servicio-medico")
public class CatalogoServicioMedicoRest extends BaseController {
   private final Logger logger = LoggerFactory.getLogger(CatalogoServicioMedicoRest.class);
   private CatalogoServicioMedicoService catalogoServicioMedicoService;

   @Autowired
   public void setCatalogoServicioMedicoService(CatalogoServicioMedicoService catalogoServicioMedicoService) {
      this.catalogoServicioMedicoService = catalogoServicioMedicoService;
   }

   @RequestMapping(method = RequestMethod.POST)
   @ResponseStatus(HttpStatus.CREATED)
   public void createdCatalogoServicioMedico(@RequestBody @Valid CatalogoServicioMedicoView catalogoServicioMedicoView) throws CatalogoServicioMedicoException {
      try {
         logger.info("===>>>Guardar nuevo Catálogo de Servicio Médico: {}", catalogoServicioMedicoView);
         catalogoServicioMedicoService.createCatalogoServicioMedico(catalogoServicioMedicoView);
      } catch (CatalogoServicioMedicoException csme) {
         throw csme;
      } catch (Exception ex) {
         CatalogoServicioMedicoException csme = new CatalogoServicioMedicoException("No fue posible agregar al Catálogo del Servicio Médico", CatalogoServicioMedicoException.LAYER_REST, CatalogoServicioMedicoException.ACTION_INSERT);
         csme.addError("Ocurrio un error al agregar el Catálogo de Servicio Médico");
         logger.error("===>>>Error al insertar nuevo Catálogo de Servicio Médico - CODE {} - {}", csme.getExceptionCode(), csme);
         throw csme;
      }
   }

   @RequestMapping(value = "{idCatalogoServicioMedico}", method = RequestMethod.PUT)
   @ResponseStatus(HttpStatus.OK)
   public void updateCatalogoServicioMedico(@PathVariable() Long idCatalogoServicioMedico, @RequestBody @Valid CatalogoServicioMedicoView catalogoServicioMedicoView) throws CatalogoServicioMedicoException {
      try {
         catalogoServicioMedicoView.setIdCatalogoServicioMedico(idCatalogoServicioMedico);
         logger.info("===>>>Editar Catálogo de Servicio Médico: {}", catalogoServicioMedicoView);
         catalogoServicioMedicoService.updateCatalogoServicioMedico(catalogoServicioMedicoView);
      } catch (CatalogoServicioMedicoException csme) {
         throw csme;
      } catch (Exception ex) {
         CatalogoServicioMedicoException csme = new CatalogoServicioMedicoException("No fue posible modificar al Catálogo del Servicio Médico", CatalogoServicioMedicoException.LAYER_REST, CatalogoServicioMedicoException.ACTION_INSERT);
         csme.addError("Ocurrio un error al modificar el Catálogo de Servicio Médico");
         logger.error("===>>>Error al modificar Catálogo de Servicio Médico - CODE {} - {}", csme.getExceptionCode(), csme);
         throw csme;
      }
   }

   @RequestMapping(value = "{idCatalogoServicioMedico}", method = RequestMethod.DELETE)
   @ResponseStatus(HttpStatus.OK)
   public void deleteCatalogoServicioMedico(@PathVariable() Long idCatalogoServicioMedico) throws CatalogoServicioMedicoException {
      logger.info("===>>>Eliminar Catálogo de Servicio Médico: {}", idCatalogoServicioMedico);
      catalogoServicioMedicoService.deleteCatalogoServicioMedico(idCatalogoServicioMedico);
   }

   @RequestMapping(value = "{idCatalogoServicioMedico}", method = RequestMethod.GET)
   @ResponseStatus(HttpStatus.OK)
   public CatalogoServicioMedicoView getDetailsByIdCatalogoServicioMedico(@PathVariable() Long idCatalogoServicioMedico) throws CatalogoServicioMedicoException {
      try {
         logger.info("===>>>Obtener detalles del Catalogo de Servicio Médico por Id: {}", idCatalogoServicioMedico);
         return catalogoServicioMedicoService.getDetailsByIdCatalogoServicioMedico(idCatalogoServicioMedico);
      } catch (CatalogoServicioMedicoException csme) {
         throw csme;
      } catch (Exception ex) {
         CatalogoServicioMedicoException csme = new CatalogoServicioMedicoException("No fue posible obtener los detalles  del Catálogo del Servicio Médico por Id", CatalogoServicioMedicoException.LAYER_REST, CatalogoServicioMedicoException.ACTION_SELECT);
         csme.addError("Ocurrio un error al obtener los detalles del Catálogo de Servicio Médico por Id");
         logger.error("===>>>Error al obtener los detalles del  Catálogo de Servicio Médico por Id - CODE {} - {} ", csme.getExceptionCode(), csme);
         throw csme;
      }
   }

   @RequestMapping(value = "findAll", method = RequestMethod.GET)
   @ResponseStatus(HttpStatus.OK)
   public List<CatalogoServicioMedicoView> findAll(@RequestParam(required = false) Boolean active) throws CatalogoServicioMedicoException {
      logger.info("===>>>Obtener todos los Servicios Medicos, con campo activo: {}", active);
      return catalogoServicioMedicoService.findAll(active);
   }

   @RequestMapping(value = "page", method = RequestMethod.GET)
   @ResponseStatus(HttpStatus.OK)
   public Page<CatalogoServicioMedicoView> getCatalogoServicioMedicoPage(@RequestParam(required = false) Boolean active,
                                                                         @RequestParam(required = false, defaultValue = "") String name,
                                                                         @RequestParam(required = false) Integer page,
                                                                         @RequestParam(required = false) Integer size,
                                                                         @RequestParam(required = false) String orderColumn,
                                                                         @RequestParam(required = false) String orderType) throws CatalogoServicioMedicoException {

      logger.info("===>>>Obtener listado de Catálogo de Servicio Medico paginable: active: {} - name {} - page: {} - size: {} - orderColumn: {} - orderType: {}",
         active, name, page, size, orderColumn, orderType);
      if (page == null)
         page = 0;
      if (size == null)
         size = 10;
      if (orderType == null || orderType.isEmpty())
         orderType = "asc";
      if (orderColumn == null || orderColumn.isEmpty())
         orderColumn = "nombre";

      return catalogoServicioMedicoService.getCatalogoServicioMedicoPage(active, name != null ? name : "", page, size, orderColumn, orderType);
   }

}
