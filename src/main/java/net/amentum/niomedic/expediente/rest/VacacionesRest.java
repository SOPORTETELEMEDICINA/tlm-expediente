package net.amentum.niomedic.expediente.rest;

import net.amentum.common.BaseController;
import net.amentum.niomedic.expediente.exception.VacacionesException;
import net.amentum.niomedic.expediente.service.VacacionesService;
import net.amentum.niomedic.expediente.views.VacacionesView;
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
@RequestMapping("vacaciones")
public class VacacionesRest extends BaseController {
   private final Logger logger = LoggerFactory.getLogger(VacacionesRest.class);

   private VacacionesService vacacionesService;

   @Autowired
   public void setVacacionesService(VacacionesService vacacionesService) {
      this.vacacionesService = vacacionesService;
   }

   @RequestMapping(method = RequestMethod.POST)
   @ResponseStatus(HttpStatus.CREATED)
   public void createVacaciones(@RequestBody @Validated VacacionesView vacacionesView) throws VacacionesException {
      try {
         logger.info("===>>>Guardar nuevo Vacaciones: {}", vacacionesView);
         vacacionesService.createVacaciones(vacacionesView);
      } catch (VacacionesException vaE) {
         throw vaE;
      } catch (Exception ex) {
         VacacionesException vaE = new VacacionesException("No fue posible insertar  Vacaciones", VacacionesException.LAYER_REST, VacacionesException.ACTION_INSERT);
         logger.error("===>>>Error al insertar  Vacaciones- CODE: {} - ", vaE.getExceptionCode(), ex);
         throw vaE;
      }
   }

   @RequestMapping(value = "{idVacaciones}", method = RequestMethod.PUT)
   @ResponseStatus(HttpStatus.OK)
   public void updateVacaciones(@PathVariable() Long idVacaciones, @RequestBody @Validated VacacionesView vacacionesView) throws VacacionesException {
      try {
         vacacionesView.setIdVacaciones(idVacaciones);
         logger.info("===>>>Editar vacaciones: {}", vacacionesView);
         vacacionesService.updateVacaciones(vacacionesView);
      } catch (VacacionesException vaE) {
         throw vaE;
      } catch (Exception ex) {
         VacacionesException vaE = new VacacionesException("No fue posible modificar vacaciones", VacacionesException.LAYER_REST, VacacionesException.ACTION_UPDATE);
         logger.error("===>>>Error al modificar vacaciones- CODE: {} - ", vaE.getExceptionCode(), ex);
         throw vaE;
      }
   }

   @RequestMapping(value = "{idUsuario}", method = RequestMethod.GET)
   @ResponseStatus(HttpStatus.OK)
   public List<VacacionesView> getDetailsByIdUsuario(@PathVariable() Long idUsuario) throws VacacionesException {
      try {
         logger.info("===>>>Obtener los detalles del vacaciones por idUsuario: {}", idUsuario);
         return vacacionesService.getDetailsByIdUsuario(idUsuario);
      } catch (VacacionesException vaE) {
         throw vaE;
      } catch (Exception ex) {
         VacacionesException vaE = new VacacionesException("No fue posible obtener los detalles del vacaciones por idUsuario", VacacionesException.LAYER_REST, VacacionesException.ACTION_SELECT);
         logger.error("===>>>Error al obtener los detalles del vacaciones por idUsuario- CODE: {} - ", vaE.getExceptionCode(), ex);
         throw vaE;
      }
   }


}
