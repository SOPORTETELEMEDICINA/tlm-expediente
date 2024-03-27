package net.amentum.niomedic.expediente.rest;

import net.amentum.common.BaseController;
import net.amentum.niomedic.expediente.exception.PadecimientoException;
import net.amentum.niomedic.expediente.service.PadecimientoService;
import net.amentum.niomedic.expediente.views.PadecimientoView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.util.List;

@RestController
@RequestMapping("padecimiento")
public class PadecimientoRest extends BaseController {

   private final Logger logger = LoggerFactory.getLogger(PadecimientoRest.class);
   private PadecimientoService padecimientoService;

   @Autowired
   public void setPadecimientoService(PadecimientoService padecimientoService) {
      this.padecimientoService = padecimientoService;
   }

   @RequestMapping(method = RequestMethod.POST)
   @ResponseStatus(HttpStatus.CREATED)
   public void createPadecimiento(@RequestBody @Valid PadecimientoView padecimientoView) throws PadecimientoException {
      try {
         logger.info("Guardar nuevo Padecimiento: {}", padecimientoView);
         padecimientoService.createPadecimiento(padecimientoView);
      } catch (PadecimientoException pe) {
         throw pe;
      } catch (Exception ex) {
         PadecimientoException pe = new PadecimientoException("No fue posible agregar Padecimiento", PadecimientoException.LAYER_DAO, PadecimientoException.ACTION_INSERT);
         pe.addError("Ocurrio un error al agregar Padecimiento");
         logger.error("Error al insertar nuevo Padecimiento - CODE {} - {}", pe.getExceptionCode(), pe);
         throw pe;
      }
   }

//   @RequestMapping(value = "{idPaciente}", method = RequestMethod.PUT)
//   @ResponseStatus(HttpStatus.OK)
//   public void updatePadecimiento(@PathVariable() String idPaciente, @RequestBody @Valid PadecimientoView padecimientoView) throws PadecimientoException {
//      try {
//         padecimientoView.setIdPaciente(idPaciente);
//         logger.info("Editar Padecimiento: {}", padecimientoView);
//         padecimientoService.updatePadecimiento(padecimientoView);
//      } catch (PadecimientoException pe) {
//         throw pe;
//      } catch (Exception ex) {
//         PadecimientoException pe = new PadecimientoException("No fue posible modificar Padecimiento", PadecimientoException.LAYER_DAO, PadecimientoException.ACTION_UPDATE);
//         pe.addError("Ocurrio un error al modificar Padecimiento");
//         logger.error("Error al modificar Padecimiento - CODE {} - {}", pe.getExceptionCode(), pe);
//         throw pe;
//      }
//   }

   @RequestMapping(value = "{idPadecimiento}", method = RequestMethod.PUT)
   @ResponseStatus(HttpStatus.OK)
   public void updatePadecimiento(@PathVariable() Long idPadecimiento, @RequestBody @Valid PadecimientoView padecimientoView) throws PadecimientoException {
      try {
//         padecimientoView.setIdPaciente(idPadecimiento);
         padecimientoView.setIdPadecimiento(idPadecimiento);
         logger.info("Editar Padecimiento: {}", padecimientoView);
         padecimientoService.updatePadecimiento(padecimientoView);
      } catch (PadecimientoException pe) {
         throw pe;
      } catch (Exception ex) {
         PadecimientoException pe = new PadecimientoException("No fue posible modificar Padecimiento", PadecimientoException.LAYER_DAO, PadecimientoException.ACTION_UPDATE);
         pe.addError("Ocurrio un error al modificar Padecimiento");
         logger.error("Error al modificar Padecimiento - CODE {} - {}", pe.getExceptionCode(), pe);
         throw pe;
      }
   }

   @RequestMapping(value = "{idPadecimiento}", method = RequestMethod.GET)
   @ResponseStatus(HttpStatus.OK)
//   public PadecimientoView getDetailsByIdPadecimiento(@PathVariable() String idPadecimiento) throws PadecimientoException {
   public PadecimientoView getDetailsByIdPadecimiento(@PathVariable() Long idPadecimiento) throws PadecimientoException {
      try {
         logger.info("Obtener detalles Padecimiento por Id: {}", idPadecimiento);
         return padecimientoService.getDetailsByIdPadecimiento(idPadecimiento);
      } catch (PadecimientoException pe) {
         throw pe;
      } catch (Exception ex) {
         PadecimientoException pe = new PadecimientoException("No fue posible obtener los detalles Padecimiento por Id", PadecimientoException.LAYER_DAO, PadecimientoException.ACTION_SELECT);
         pe.addError("Ocurrio un error al obtener los detalles Padecimiento por Id");
         logger.error("Error al obtener los detalles Padecimiento por Id - CODE {} - {} ", pe.getExceptionCode(), pe);
         throw pe;
      }
   }


   @RequestMapping(value = "{idPadecimiento}/{idPaciente}", method = RequestMethod.GET)
   @ResponseStatus(HttpStatus.OK)
   public PadecimientoView getDetailsByIdPAdecimientoAndIdPaciente(@PathVariable() Long idPadecimiento, @PathVariable() String idPaciente) throws  PadecimientoException {
      try {
         logger.info("Obtener detalles Padecimiento por Id: {}", idPadecimiento);
         return padecimientoService.getDetailsByIdPAdecimientoAndIdPaciente(idPadecimiento,idPaciente);
      } catch (PadecimientoException pe) {
         throw pe;
      } catch (Exception ex) {
         PadecimientoException pe = new PadecimientoException("No fue posible obtener los detalles Padecimiento por Id", PadecimientoException.LAYER_DAO, PadecimientoException.ACTION_SELECT);
         pe.addError("Ocurrio un error al obtener los detalles Padecimiento por Id");
         logger.error("Error al obtener los detalles Padecimiento por Id - CODE {} - {} ", pe.getExceptionCode(), pe);
         throw pe;
      }
   }


   @RequestMapping(value = "findAll", method = RequestMethod.GET)
   @ResponseStatus(HttpStatus.OK)
   public List<PadecimientoView> findAll() throws PadecimientoException {
      return padecimientoService.findAll();
   }

   @RequestMapping(value = "page", method = RequestMethod.GET)
   @ResponseStatus(HttpStatus.OK)
   public Page<PadecimientoView> getPadecimientoPage(@RequestParam(required = false, defaultValue = "") String name,
                                                     @RequestParam(required = false) Integer page,
                                                     @RequestParam(required = false) Integer size,
                                                     @RequestParam(required = false) String orderColumn,
                                                     @RequestParam(required = false) String orderType) throws PadecimientoException {
      logger.info("- Obtener listado Padecimiento paginable: {} - page {} - size: {} - orderColumn: {} - orderType: {} - active: {}", name, page, size, orderColumn, orderType);
      if (page == null)
         page = 0;
      if (size == null)
         size = 10;
      if (orderType == null && orderType.isEmpty()) {
         orderType = "asc";
      }
      return padecimientoService.getPadecimientoPage(name != null ? name : "", page, size, orderColumn, orderType);
   }

}
