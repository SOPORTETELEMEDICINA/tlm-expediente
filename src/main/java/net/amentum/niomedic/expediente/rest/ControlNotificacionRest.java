package net.amentum.niomedic.expediente.rest;

import net.amentum.common.BaseController;
import net.amentum.common.v2.GenericException;
import net.amentum.niomedic.expediente.exception.ControlNotificacionException;
import net.amentum.niomedic.expediente.service.ControlNotificacionService;
import net.amentum.niomedic.expediente.views.ControlNotificacionSimpView;
import net.amentum.niomedic.expediente.views.ControlNotificacionViewList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("notificaciones-paciente")
public class ControlNotificacionRest extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(ControlNotificacionRest.class);

    @Autowired
    ControlNotificacionService service;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public HashMap<String, List<ControlNotificacionSimpView>> getNotificationsByDay() throws ControlNotificacionException {
        try {
            return service.getNotificationsByDay();
        } catch (ControlNotificacionException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ControlNotificacionException("Error al obtener horarios para notificaciones - " + ex.getLocalizedMessage(), GenericException.LAYER_REST, GenericException.ACTION_SELECT);
        }
    }

    @RequestMapping(value = "{idPaciente}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public HashMap<String, List<ControlNotificacionViewList>> getNotificationsByIdPaciente(@PathVariable() String idPaciente) throws ControlNotificacionException {
        try {
            return service.getNotificationsByPaciente(idPaciente);
        } catch (ControlNotificacionException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ControlNotificacionException("Error al obtener horarios para notificaciones - " + ex.getLocalizedMessage(), GenericException.LAYER_REST, GenericException.ACTION_SELECT);
        }
    }
}
