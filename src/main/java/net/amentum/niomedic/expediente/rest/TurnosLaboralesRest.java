package net.amentum.niomedic.expediente.rest;

import net.amentum.common.BaseController;
import net.amentum.niomedic.expediente.exception.TurnosLaboralesException;
import net.amentum.niomedic.expediente.service.TurnosLaboralesService;
import net.amentum.niomedic.expediente.views.TurnosLaboralesView;
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

@RestController
@RequestMapping("turnos-laborales")
public class TurnosLaboralesRest extends BaseController {
   private final Logger logger = LoggerFactory.getLogger(TurnosLaboralesRest.class);

   private TurnosLaboralesService turnosLaboralesService;

   @Autowired
   public void setTurnosLaboralesService(TurnosLaboralesService turnosLaboralesService) {
      this.turnosLaboralesService = turnosLaboralesService;
   }

   @RequestMapping(method = RequestMethod.POST)
   @ResponseStatus(HttpStatus.CREATED)
   public void createTurnosLaborales(@RequestBody @Validated TurnosLaboralesView turnosLaboralesView) throws TurnosLaboralesException {
      try {
         logger.info("===>>>Guardar nuevo TurnosLaborales: {}", turnosLaboralesView);
         turnosLaboralesService.createTurnosLaborales(turnosLaboralesView);
      } catch (TurnosLaboralesException tlE) {
         throw tlE;
      } catch (Exception ex) {
         TurnosLaboralesException tlE = new TurnosLaboralesException("No fue posible insertar  TurnosLaborales", TurnosLaboralesException.LAYER_REST, TurnosLaboralesException.ACTION_INSERT);
         logger.error("===>>>Error al insertar  TurnosLaborales- CODE: {} - ", tlE.getExceptionCode(), ex);
         throw tlE;
      }
   }

   @RequestMapping(value = "{idTurnosLaborales}", method = RequestMethod.PUT)
   @ResponseStatus(HttpStatus.OK)
   public void updateTurnosLaborales(@PathVariable() Long idTurnosLaborales, @RequestBody @Validated TurnosLaboralesView turnosLaboralesView) throws TurnosLaboralesException {
      try {
         turnosLaboralesView.setIdTurnosLaborales(idTurnosLaborales);
         logger.info("===>>>Editar turnosLaborales: {}", turnosLaboralesView);
         turnosLaboralesService.updateTurnosLaborales(turnosLaboralesView);
      } catch (TurnosLaboralesException tlE) {
         throw tlE;
      } catch (Exception ex) {
         TurnosLaboralesException tlE = new TurnosLaboralesException("No fue posible modificar turnosLaborales", TurnosLaboralesException.LAYER_REST, TurnosLaboralesException.ACTION_UPDATE);
         logger.error("===>>>Error al modificar turnosLaborales- CODE: {} - ", tlE.getExceptionCode(), ex);
         throw tlE;
      }
   }

   @RequestMapping(value = "{idUsuario}", method = RequestMethod.GET)
   @ResponseStatus(HttpStatus.OK)
   public TurnosLaboralesView getDetailsByIdUsuario(@PathVariable() Long idUsuario) throws TurnosLaboralesException {
      try {
         logger.info("===>>>Obtener los detalles del turnosLaborales por idUsuario: {}", idUsuario);
         return turnosLaboralesService.getDetailsByIdUsuario(idUsuario);
      } catch (TurnosLaboralesException tlE) {
         throw tlE;
      } catch (Exception ex) {
         TurnosLaboralesException tlE = new TurnosLaboralesException("No fue posible obtener los detalles del turnosLaborales por idUsuario", TurnosLaboralesException.LAYER_REST, TurnosLaboralesException.ACTION_SELECT);
         logger.error("===>>>Error al obtener los detalles del turnosLaborales por idUsuario- CODE: {} - ", tlE.getExceptionCode(), ex);
         throw tlE;
      }
   }


}
