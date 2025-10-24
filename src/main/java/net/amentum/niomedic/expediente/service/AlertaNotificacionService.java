package net.amentum.niomedic.expediente.service;

import net.amentum.niomedic.expediente.views.AlertaNotificacionCreateView;
import net.amentum.niomedic.expediente.views.AlertaNotificacionView;

import java.util.List;
import java.util.UUID;

public interface AlertaNotificacionService {
    AlertaNotificacionView createAndReturn(AlertaNotificacionCreateView view);
    void markAsSeen(Long id);
    List<AlertaNotificacionView> listActivas(UUID idMedico);
    long countActivas(UUID idMedico);
}
