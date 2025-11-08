package net.amentum.niomedic.expediente.service;

import net.amentum.niomedic.expediente.views.AlertaNotificacionCreateView;
import net.amentum.niomedic.expediente.views.AlertaNotificacionView;

import java.util.List;

public interface AlertaNotificacionService {
    AlertaNotificacionView createAndReturn(AlertaNotificacionCreateView view);
    void markAsSeen(Long id);
    List<AlertaNotificacionView> listActivas(String idMedico);
    long countActivas(String idMedico);
    List<AlertaNotificacionView> listActivasPorGrupo(Integer idGroup);
    long countActivasPorGrupo(Integer idGroup);

}