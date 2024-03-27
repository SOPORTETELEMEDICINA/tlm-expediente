package net.amentum.niomedic.expediente.rest;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import net.amentum.common.v2.RestBaseController;
import net.amentum.niomedic.expediente.exception.ExceptionServiceCode;
import net.amentum.niomedic.expediente.exception.FormatException;
import net.amentum.niomedic.expediente.service.FormatService;
import net.amentum.niomedic.expediente.views.FormatResultListView;
import net.amentum.niomedic.expediente.views.FormatResultView;
import net.amentum.niomedic.expediente.views.FormatView;
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

//import org.springframework.validation.annotation.Validated;

/**
 * @author by marellano on 14/06/17.
 */
@RestController
@Api(value = "Format", description = "Servicio para crear formatos de tareas o tickets.")
@RequestMapping("formats")
@Slf4j
public class FormatRest extends RestBaseController {

//    private final Logger log = LoggerFactory.getLogger(FormatRest.class);

   private FormatService formatService;

   @Autowired
   public void setFormatService(FormatService formatService) {
      this.formatService = formatService;
   }


   @RequestMapping(method = RequestMethod.POST)
   @ResponseStatus(HttpStatus.CREATED)
   public FormatView addFormat(@RequestBody @Valid FormatView view) throws FormatException {
      log.info(ExceptionServiceCode.FORMAT + " - Agregar un nuevo formato: {}", view.toStringResume());
      log.debug(ExceptionServiceCode.FORMAT + " - Agregar un nuevo formato: {}", view);
      return formatService.addFormat(view);
   }


   @RequestMapping(value = "{idFormat}", method = RequestMethod.PUT)
   @ResponseStatus(HttpStatus.OK)
   public FormatView editFormat(@RequestBody @Valid FormatView view, @PathVariable() Long idFormat) throws FormatException {
      view.setIdFormat(idFormat);
      log.info(ExceptionServiceCode.FORMAT + " - Editar formato: ", view.toStringResume());
      log.debug(ExceptionServiceCode.FORMAT + " - Editar formato: ", view.toStringResume());
      return formatService.editFormat(view);
   }


   @RequestMapping(value = "{idFormat}", method = RequestMethod.DELETE)
   @ResponseStatus(HttpStatus.OK)
   public void deleteFormat(@PathVariable() Long idFormat) throws FormatException {
      log.info(ExceptionServiceCode.FORMAT + " - Eliminar formato con id: {}", idFormat);
      log.debug(ExceptionServiceCode.FORMAT + " - Eliminar formato con id: {}", idFormat);
      formatService.deleteFormat(idFormat);
   }


   @RequestMapping(value = "status/{idFormat}", method = RequestMethod.PUT)
   @ResponseStatus(HttpStatus.OK)
   public void updateStatusFormat(@PathVariable() Long idFormat) throws FormatException {
      log.info(ExceptionServiceCode.FORMAT + " - Modificar estatus del formato  con id: {}", idFormat);
      log.debug(ExceptionServiceCode.FORMAT + " - Modificar estatus del formato  con id: {}", idFormat);
      formatService.updateStatusFormat(idFormat);
   }


   @RequestMapping(value = "{idFormat}", method = RequestMethod.GET)
   @ResponseStatus(HttpStatus.OK)
   public FormatView getDetailsFormat(@PathVariable() Long idFormat) throws FormatException {
      log.info(ExceptionServiceCode.FORMAT + " - Obtener detalles del formato id: {}", idFormat);
      log.info(ExceptionServiceCode.FORMAT + " - Obtener detalles del formato id: {}", idFormat);
      return formatService.getDetailsFormat(idFormat);
   }


   @RequestMapping(value = "all", method = RequestMethod.GET)
   @ResponseStatus(HttpStatus.OK)
   public List<FormatView> getAllFormats() throws FormatException {
      log.info(ExceptionServiceCode.FORMAT + " - Obtener todos los formatos");
      log.debug(ExceptionServiceCode.FORMAT + " - Obtener todos los formatos");
      return formatService.getAllFormats();
   }


   @RequestMapping(method = RequestMethod.GET)
   @ResponseStatus(HttpStatus.OK)
   public Page<FormatView> getFormats(@RequestParam(required = false) String search,
                                      @RequestParam(required = false) String title,
                                      @RequestParam(required = false) Boolean active,
                                      @RequestParam(required = false) Integer version,
                                      @RequestParam(required = false) Integer page,
                                      @RequestParam(required = false) Integer size,
                                      @RequestParam(required = false) String orderColumn,
                                      @RequestParam(required = false) String orderType,
                                      @RequestParam(required = false, defaultValue = "false") Boolean general) throws FormatException {
      log.info(ExceptionServiceCode.FORMAT + " - Obtener lista de formatos de forma paginable se puede buscar por: - titulo: {}, - search: {}, - version: {}, " +
         "- estatus: {}", title, search, version, active);
      if (page == null) {
         page = 0;
      }
      if (size == null) {
         size = 10;
      }
      if (orderColumn == null || orderColumn.isEmpty()) {
         orderColumn = "idFormat";
      }
      if (orderType == null || orderType.isEmpty()) {
         orderType = "asc";
      }
      if (title == null) {
         title = "";
      }
      if (search == null) {
         search = "";
      }
      return formatService.getFormatsPage(title, page, size, orderColumn, orderType, active, version,
         general, search);
   }


   @RequestMapping(value = "result/{idFormat}", method = RequestMethod.POST)
   @ResponseStatus(HttpStatus.CREATED)
//   public void addFormatResult(@RequestBody @Valid FormatResultView formatResultView, @PathVariable() Long idFormat) throws FormatException {
   public FormatResultView addFormatResult(@RequestBody @Valid FormatResultView formatResultView, @PathVariable() Long idFormat) throws FormatException {
      log.info(ExceptionServiceCode.FORMAT + " - Agregar resultado de formato al formato: {} - date: {} ", idFormat, formatResultView.getCreatedDate());
      log.debug(ExceptionServiceCode.FORMAT + " - Agregar resultado de formato al formato: {} - view: {}", idFormat, formatResultView);
      FormatView formatView = new FormatView();
      formatView.setIdFormat(idFormat);
      formatResultView.setFormatView(formatView);
      return formatService.addFormatResult(formatResultView);
   }


   @RequestMapping(value = "result", method = RequestMethod.GET)
   @ResponseStatus(HttpStatus.OK)
   public List<FormatResultView> getFormatResult(@RequestParam(required = false) Long idConsulta, @RequestParam(required = false) Boolean active) throws FormatException {
      log.info(ExceptionServiceCode.FORMAT + " - Obtener resultados de un formato para la consulta con el id:{}", idConsulta);
      log.debug(ExceptionServiceCode.FORMAT + " - Obtener resultados de un formato para la consulta con el id:{}", idConsulta);
      return formatService.getFormatResult(idConsulta, active);
   }


   @RequestMapping(value = "result/{idFormatResult}", method = RequestMethod.GET)
   @ResponseStatus(HttpStatus.OK)
   public FormatResultView getFormatResultDetails(@PathVariable() Long idFormatResult) throws FormatException {
      log.info(ExceptionServiceCode.FORMAT + " - Obtener detalles del resultado de un formato por el id: {}", idFormatResult);
      log.debug(ExceptionServiceCode.FORMAT + " - Obtener detalles del resultado de un formato por el id: {}", idFormatResult);
      return formatService.getFormatResultDetails(idFormatResult);
   }

  
    /*@RequestMapping(value = "category", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<FormatView> getFormatByCategory(@RequestParam(required = false)Long idTaskCategory, @RequestParam(required = false) Long idTicketCategory) throws FormatException {
        log.info(ExceptionServiceCode.FORMAT+" - Obtener formatos de acuerdo a la categoria del ticket: {} o a la categoria de la tarea: {}",idTicketCategory, idTaskCategory);
        log.debug(ExceptionServiceCode.FORMAT+" - Obtener formatos de acuerdo a la categoria del ticket: {} o a la categoria de la tarea: {}",idTicketCategory, idTaskCategory);
        return formatService.getFormatByCategory(idTaskCategory, idTicketCategory);
    }*/


   @RequestMapping(value = "result/list", method = RequestMethod.GET)
   @ResponseStatus(HttpStatus.OK)
   public List<FormatResultListView> getFormatResultView(@RequestParam(required = false) Long idConsulta) throws FormatException {
      log.info(ExceptionServiceCode.FORMAT + " - Obtener listado de respuestas, filtrador por  idConsulta:{} ", idConsulta);
      log.debug(ExceptionServiceCode.FORMAT + " - Obtener listado de respuestas, filtrador por  idConsulta:{} ", idConsulta);
      return formatService.getFormatResultListView(idConsulta);
   }

   //////////// NIO-998
   @RequestMapping(value = "user/{idUsuario}", method = RequestMethod.GET)
   @ResponseStatus(HttpStatus.OK)
   public List<FormatView> getByIdUsuario(@PathVariable("idUsuario") Long idUsuario) throws FormatException {
      log.info(ExceptionServiceCode.FORMAT + " - Obtener todos los formatos");
      log.debug(ExceptionServiceCode.FORMAT + " - Obtener todos los formatos");
      return formatService.getAllFormats();
   }


}
