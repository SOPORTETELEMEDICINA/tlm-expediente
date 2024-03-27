package net.amentum.niomedic.expediente.rest;
import net.amentum.common.BaseController;

import net.amentum.niomedic.expediente.exception.CatalogoTipoEventoException;
import net.amentum.niomedic.expediente.service.CatalogoTipoEventoService;
import net.amentum.niomedic.expediente.views.CatalogoTipoEventoView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("catalogo-tipo-evento")
public class CatalogoTipoEventoRest extends BaseController {
   private final Logger logger = LoggerFactory.getLogger(CatalogoTipoEventoRest.class);

   private CatalogoTipoEventoService catalogoTipoEventoService;

   @Autowired
   public void setCatalogoTipoEventoService(CatalogoTipoEventoService catalogoTipoEventoService) {
      this.catalogoTipoEventoService = catalogoTipoEventoService;
   }

   @RequestMapping(method = RequestMethod.POST)
   @ResponseStatus(HttpStatus.CREATED)
   public void createCatalogoTipoEvento(@RequestBody @Validated CatalogoTipoEventoView catalogoTipoEventoView) throws CatalogoTipoEventoException {
      try {
         logger.info("===>>>Guardar nuevo CatalogoTipoEvento: {}", catalogoTipoEventoView);
         catalogoTipoEventoService.createCatalogoTipoEvento(catalogoTipoEventoView);
      } catch (CatalogoTipoEventoException cteE) {
         throw cteE;
      } catch (Exception ex) {
         CatalogoTipoEventoException cteE = new CatalogoTipoEventoException("No fue posible insertar  CatalogoTipoEvento", CatalogoTipoEventoException.LAYER_REST, CatalogoTipoEventoException.ACTION_INSERT);
         logger.error("===>>>Error al insertar  CatalogoTipoEvento- CODE: {} - ", cteE.getExceptionCode(), ex);
         throw cteE;
      }
   }

   @RequestMapping(value = "{idCatalogoTipoEvento}", method = RequestMethod.PUT)
   @ResponseStatus(HttpStatus.OK)
   public void updateCatalogoTipoEvento(@PathVariable() Integer idCatalogoTipoEvento, @RequestBody @Validated CatalogoTipoEventoView catalogoTipoEventoView) throws CatalogoTipoEventoException {
      try {
         catalogoTipoEventoView.setIdTipoEvento(idCatalogoTipoEvento);
         logger.info("===>>>Editar catalogoTipoEvento: {}", catalogoTipoEventoView);
         catalogoTipoEventoService.updateCatalogoTipoEvento(catalogoTipoEventoView);
      } catch (CatalogoTipoEventoException cteE) {
         throw cteE;
      } catch (Exception ex) {
         CatalogoTipoEventoException cteE = new CatalogoTipoEventoException("No fue posible modificar catalogoTipoEvento", CatalogoTipoEventoException.LAYER_REST, CatalogoTipoEventoException.ACTION_UPDATE);
         logger.error("===>>>Error al modificar catalogoTipoEvento- CODE: {} - ", cteE.getExceptionCode(), ex);
         throw cteE;
      }
   }

   @RequestMapping(value = "{idCatalogoTipoEvento}", method = RequestMethod.GET)
   @ResponseStatus(HttpStatus.OK)
   public CatalogoTipoEventoView getDetailsByIdCatalogoTipoEvento(@PathVariable() Integer idCatalogoTipoEvento) throws CatalogoTipoEventoException {
      try {
         logger.info("===>>>Obtener los detalles del catalogoTipoEvento por Id: {}", idCatalogoTipoEvento);
         return catalogoTipoEventoService.getDetailsByIdCatalogoTipoEvento(idCatalogoTipoEvento);
      } catch (CatalogoTipoEventoException cteE) {
         throw cteE;
      } catch (Exception ex) {
         CatalogoTipoEventoException cteE = new CatalogoTipoEventoException("No fue posible obtener los detalles del catalogoTipoEvento por Id", CatalogoTipoEventoException.LAYER_REST, CatalogoTipoEventoException.ACTION_SELECT);
         logger.error("===>>>Error al obtener los detalles del catalogoTipoEvento por Id- CODE: {} - ", cteE.getExceptionCode(), ex);
         throw cteE;
      }
   }

   @RequestMapping(value = "findAll", method = RequestMethod.GET)
   @ResponseStatus(HttpStatus.OK)
   public List<CatalogoTipoEventoView> findAll() throws CatalogoTipoEventoException {
      return catalogoTipoEventoService.findAll();
   }

}
