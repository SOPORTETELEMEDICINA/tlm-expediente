package net.amentum.niomedic.expediente.service;

import net.amentum.niomedic.expediente.exception.ControlNotificacionException;
import net.amentum.niomedic.expediente.views.ControlNotificacionSimpView;
import net.amentum.niomedic.expediente.views.ControlNotificacionViewList;

import java.util.HashMap;
import java.util.List;

public interface ControlNotificacionService {
    HashMap<String, List<ControlNotificacionViewList>> getNotificationsByPaciente(String idPaciente) throws ControlNotificacionException;
    HashMap<String, List<ControlNotificacionSimpView>> getNotificationsByDay() throws ControlNotificacionException;
}
