package net.amentum.niomedic.expediente.rest;

import net.amentum.common.BaseController;
import net.amentum.niomedic.expediente.exception.MedicionesPacienteException;
import net.amentum.niomedic.expediente.service.MedicionesPacienteService;
import net.amentum.niomedic.expediente.views.MedicionesPacienteView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("mediciones-paciente")
public class MedicionesPacienteRest extends BaseController {
   private final Logger logger = LoggerFactory.getLogger(MedicionesPacienteRest.class);

   private MedicionesPacienteService medicionesPacienteService;

   @Autowired
   public void setMedicionesPacienteService(MedicionesPacienteService medicionesPacienteService) {
      this.medicionesPacienteService = medicionesPacienteService;
   }

   @RequestMapping(method = RequestMethod.POST)
   @ResponseStatus(HttpStatus.CREATED)
   public MedicionesPacienteView createMedicionesPaciente(@RequestBody @Validated MedicionesPacienteView medicionesPacienteView) throws MedicionesPacienteException {
      try {
         logger.info("===>>>Guardar nuevo MedicionesPaciente: {}", medicionesPacienteView);
         return medicionesPacienteService.createMedicionesPaciente(medicionesPacienteView);
      } catch (MedicionesPacienteException medPacE) {
         throw medPacE;
      } catch (Exception ex) {
         MedicionesPacienteException medPacE = new MedicionesPacienteException("No fue posible insertar  MedicionesPaciente", MedicionesPacienteException.LAYER_REST, MedicionesPacienteException.ACTION_INSERT);
         logger.error("===>>>Error al insertar  MedicionesPaciente- CODE: {} - ", medPacE.getExceptionCode(), ex);
         throw medPacE;
      }
   }

   @RequestMapping(value = "{idMedicionesPaciente}", method = RequestMethod.PUT)
   @ResponseStatus(HttpStatus.OK)
   public MedicionesPacienteView updateMedicionesPaciente(@PathVariable() Long idMedicionesPaciente, @RequestBody @Validated MedicionesPacienteView medicionesPacienteView) throws MedicionesPacienteException {
      try {
         medicionesPacienteView.setIdMedicionesPaciente(idMedicionesPaciente);
         logger.info("===>>>Editar medicionesPaciente: {}", medicionesPacienteView);
         return medicionesPacienteService.updateMedicionesPaciente(medicionesPacienteView);
      } catch (MedicionesPacienteException medPacE) {
         throw medPacE;
      } catch (Exception ex) {
         MedicionesPacienteException medPacE = new MedicionesPacienteException("No fue posible modificar medicionesPaciente", MedicionesPacienteException.LAYER_REST, MedicionesPacienteException.ACTION_UPDATE);
         logger.error("===>>>Error al modificar medicionesPaciente- CODE: {} - ", medPacE.getExceptionCode(), ex);
         throw medPacE;
      }
   }

   @RequestMapping(value = "search", method = RequestMethod.GET)
   @ResponseStatus(HttpStatus.OK)
   public Page<MedicionesPacienteView> getMedicionesPacienteSearch(@RequestParam(required = true) String idPaciente,
                                                                   @RequestParam(required = false) Long startDate,
                                                                   @RequestParam(required = false) Long endDate,
                                                                   @RequestParam(required = false) Integer page,
                                                                   @RequestParam(required = false) Integer size,
                                                                   @RequestParam(required = false) String orderColumn,
                                                                   @RequestParam(required = false) String orderType) throws MedicionesPacienteException {

      logger.info("===>>>getMedicionesPacienteSearch(): - idPaciente: {} - idTipoEvento: {} - startDate: {} - endDate: {} - page: {} - size: {} - orderColumn: {} - orderType: {}",
         idPaciente, startDate, endDate, page, size, orderColumn, orderType);

      UUID uidPaciente = null;
      try {
         if (idPaciente != null && !idPaciente.isEmpty()) {
            uidPaciente = UUID.fromString(idPaciente);
         }
      } catch (IllegalArgumentException iae) {
         logger.error("===>>>idPaciente tiene valores incorrectos", idPaciente);
         MedicionesPacienteException medPacE = new MedicionesPacienteException("Ocurrio un error al obtener MedicionesPaciente", MedicionesPacienteException.LAYER_REST, MedicionesPacienteException.ACTION_VALIDATE);
         medPacE.addError("idPaciente tiene valores incorrectos: " + idPaciente);
         throw medPacE;
      }

      if (page == null)
         page = 0;
      if (size == null)
         size = 10;
      if (orderType == null || orderType.isEmpty())
         orderType = "asc";
      if (orderColumn == null || orderColumn.isEmpty())
         orderColumn = "fechaCreacion";

      return medicionesPacienteService.getMedicionesPacienteSearch(uidPaciente, startDate, endDate, page, size, orderColumn, orderType);
   }


}
