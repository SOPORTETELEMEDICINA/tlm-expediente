package net.amentum.niomedic.expediente.rest;

import net.amentum.common.BaseController;
import net.amentum.niomedic.expediente.exception.PadecimientoException;
import net.amentum.niomedic.expediente.service.PadecimientoService;
import net.amentum.niomedic.expediente.views.ConsultaView;
import net.amentum.niomedic.expediente.views.PadecimientoView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("cursoclinico")
public class CursoClinicoRest extends BaseController {

   private final Logger logger = LoggerFactory.getLogger(CursoClinicoRest.class);
   private PadecimientoService padecimientoService;

   @Autowired
   public void setPadecimientoService(PadecimientoService padecimientoService) {
      this.padecimientoService = padecimientoService;
   }

   @RequestMapping(value = "actualizar-estatus", method = RequestMethod.PUT)
   @ResponseStatus(HttpStatus.OK)
   public void updateStatusPadecimiento(@RequestParam() Long idPadecimiento) throws PadecimientoException {
      logger.info("Update estatus padecimiento - {}", idPadecimiento);
      if(idPadecimiento == null || idPadecimiento <= 0) {
         PadecimientoException pe = new PadecimientoException("Ocurrió un error al actualizar el Padecimiento por Consulta", PadecimientoException.LAYER_REST, PadecimientoException.ACTION_VALIDATE);
         pe.addError("Id Padecimiento null/vacío");
         logger.error("Id Padecimiento null/vacío");
         throw pe;
      }
      padecimientoService.updateStatusPadecimiento(idPadecimiento);
   }

   @RequestMapping(value = "search", method = RequestMethod.GET)
   @ResponseStatus(HttpStatus.OK)
   public Page<PadecimientoView> getCursoClinicoSearch(@RequestParam(required = true) String idPaciente,
                                                     @RequestParam(required = false, defaultValue = "") String datosBusqueda,
                                                     @RequestParam(required = false) Integer page,
                                                     @RequestParam(required = false) Integer size,
                                                     @RequestParam(required = false) String orderColumn,
                                                     @RequestParam(required = false) String orderType) throws PadecimientoException {
      logger.info(
         "- Obtener listado Padecimiento paginable: {} - page {} - size: {} - orderColumn: {} - orderType: {} - active: {}",
         idPaciente, page, size, orderColumn, orderType);
      if (page == null)
         page = 0;
      if (size == null)
         size = 10;
      if (orderType == null || orderType.isEmpty()) {
         orderType = "asc";
      }
      return padecimientoService.getCursoClinicoSearch(idPaciente != null ? idPaciente : "", datosBusqueda != null ? datosBusqueda : "", page, size, orderColumn, orderType);

   }

   @RequestMapping(value = "lista-consultas", method = RequestMethod.GET)
   @ResponseStatus(HttpStatus.OK)
   public Page<ConsultaView> listaConsulta(@RequestParam(required = true) Long idPadecimiento,
                                           @RequestParam(required = false) Integer page,
                                           @RequestParam(required = false) Integer size, @RequestParam(required = false) String orderColumn,
                                           @RequestParam(required = false) String orderType,
                                           @RequestParam(required = false, defaultValue = "") String datosBusqueda) throws PadecimientoException {
      if (page == null) {
         page = 0;
      }
      if (size == null) {
         size = 10;
      }
      if (orderType == null || orderType.isEmpty()) {
         orderType = "asc";
      }
      if(orderColumn==null || orderColumn.isEmpty()) {
    	  orderColumn="idConsulta";
      }
      return padecimientoService.listaConsulta(idPadecimiento, page, size, orderColumn, orderType, datosBusqueda == null ? "" : datosBusqueda);
   }
}
