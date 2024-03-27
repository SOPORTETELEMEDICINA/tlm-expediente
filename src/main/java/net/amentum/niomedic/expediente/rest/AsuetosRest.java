package net.amentum.niomedic.expediente.rest;

import net.amentum.common.BaseController;
import net.amentum.niomedic.expediente.exception.AsuetosException;
import net.amentum.niomedic.expediente.service.AsuetosService;
import net.amentum.niomedic.expediente.views.AsuetosView;
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
@RequestMapping("asuetos")
public class AsuetosRest extends BaseController {
   private final Logger logger = LoggerFactory.getLogger(AsuetosRest.class);

   private AsuetosService asuetosService;

   @Autowired
   public void setAsuetosService(AsuetosService asuetosService) {
      this.asuetosService = asuetosService;
   }

   @RequestMapping(method = RequestMethod.POST)
   @ResponseStatus(HttpStatus.CREATED)
   public void createAsuetos(@RequestBody @Validated AsuetosView asuetosView) throws AsuetosException {
      try {
         logger.info("===>>>Guardar nuevo Asuetos: {}", asuetosView);
         asuetosService.createAsuetos(asuetosView);
      } catch (AsuetosException asE) {
         throw asE;
      } catch (Exception ex) {
         AsuetosException asE = new AsuetosException("No fue posible insertar  Asuetos", AsuetosException.LAYER_REST, AsuetosException.ACTION_INSERT);
         logger.error("===>>>Error al insertar  Asuetos- CODE: {} - ", asE.getExceptionCode(), ex);
         throw asE;
      }
   }

   @RequestMapping(value = "{idAsuetos}", method = RequestMethod.PUT)
   @ResponseStatus(HttpStatus.OK)
   public void updateAsuetos(@PathVariable() Long idAsuetos, @RequestBody @Validated AsuetosView asuetosView) throws AsuetosException {
      try {
         asuetosView.setIdAsuetos(idAsuetos);
         logger.info("===>>>Editar asuetos: {}", asuetosView);
         asuetosService.updateAsuetos(asuetosView);
      } catch (AsuetosException asE) {
         throw asE;
      } catch (Exception ex) {
         AsuetosException asE = new AsuetosException("No fue posible modificar asuetos", AsuetosException.LAYER_REST, AsuetosException.ACTION_UPDATE);
         logger.error("===>>>Error al modificar asuetos- CODE: {} - ", asE.getExceptionCode(), ex);
         throw asE;
      }
   }

   @RequestMapping(value = "{idAsuetos}", method = RequestMethod.GET)
   @ResponseStatus(HttpStatus.OK)
   public AsuetosView getDetailsByIdAsuetos(@PathVariable() Long idAsuetos) throws AsuetosException {
      try {
         logger.info("===>>>Obtener los detalles del asuetos por Id: {}", idAsuetos);
         return asuetosService.getDetailsByIdAsuetos(idAsuetos);
      } catch (AsuetosException asE) {
         throw asE;
      } catch (Exception ex) {
         AsuetosException asE = new AsuetosException("No fue posible obtener los detalles del asuetos por Id", AsuetosException.LAYER_REST, AsuetosException.ACTION_SELECT);
         logger.error("===>>>Error al obtener los detalles del asuetos por Id- CODE: {} - ", asE.getExceptionCode(), ex);
         throw asE;
      }
   }

   @RequestMapping(value = "{idAsuetos}", method = RequestMethod.DELETE)
   @ResponseStatus(HttpStatus.OK)
   public void deleteAsuetos(@PathVariable() Long idAsuetos) throws AsuetosException {
      try {
         logger.info("Eliminar asuetos: {}", idAsuetos);
         asuetosService.deleteAsuetos(idAsuetos);
      } catch (AsuetosException asE) {
         throw asE;
      } catch (Exception ex) {
         AsuetosException asE = new AsuetosException("No fue posible eliminar asuetos", AsuetosException.LAYER_REST, AsuetosException.ACTION_UPDATE);
         logger.error("===>>>Error al eliminar asuetos- CODE: {} - ", asE.getExceptionCode(), ex);
         throw asE;
      }
   }

   @RequestMapping(value = "findAll", method = RequestMethod.GET)
   @ResponseStatus(HttpStatus.OK)
   public List<AsuetosView> findAll() throws AsuetosException {
      return asuetosService.findAll();
   }

}
