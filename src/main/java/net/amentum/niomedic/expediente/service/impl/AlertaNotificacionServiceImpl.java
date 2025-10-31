package net.amentum.niomedic.expediente.service.impl;

import net.amentum.niomedic.expediente.model.AlertaNotificacion;
import net.amentum.niomedic.expediente.persistence.AlertaNotificacionRepository;
import net.amentum.niomedic.expediente.service.AlertaNotificacionService;
import net.amentum.niomedic.expediente.views.AlertaNotificacionCreateView;
import net.amentum.niomedic.expediente.views.AlertaNotificacionView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AlertaNotificacionServiceImpl implements AlertaNotificacionService {

    private static final Logger log = LoggerFactory.getLogger(AlertaNotificacionServiceImpl.class);

    private final AlertaNotificacionRepository repo;

    public AlertaNotificacionServiceImpl(AlertaNotificacionRepository repo) {
        this.repo = repo;
    }

    @Override
    @Transactional
    public AlertaNotificacionView createAndReturn(AlertaNotificacionCreateView v) {
        AlertaNotificacion n = new AlertaNotificacion();
        n.setIdMedico(v.idMedico);
        n.setIdPaciente(v.idPaciente);
        n.setTipoNotificacion(v.tipoNotificacion);
        n.setSeveridad(v.severidad);
        n.setMensaje(v.mensaje);
        n.setIdGroup(v.idGroup);
        n.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
        n.setEstatus("ACTIVA");

        n = repo.save(n);
        log.debug("AlertaNotificacion creada id={}", n.getId());
        return toView(n);
    }

    @Override
    @Transactional
    public void markAsSeen(Long id) {
        AlertaNotificacion n = repo.findOne(id);
        if (n != null) {
            n.setEstatus("VISTO");
            repo.save(n);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<AlertaNotificacionView> listActivas(String idMedico) {
        return repo.findTop20ByIdMedicoAndEstatusOrderByFechaCreacionDesc(idMedico, "ACTIVA")
                .stream().map(this::toView).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public long countActivas(String idMedico) {
        return repo.countByIdMedicoAndEstatus(idMedico, "ACTIVA");
    }

    private AlertaNotificacionView toView(AlertaNotificacion n) {
        AlertaNotificacionView v = new AlertaNotificacionView();
        v.id = n.getId();
        v.idMedico = n.getIdMedico();
        v.idPaciente = n.getIdPaciente();
        v.tipoNotificacion = n.getTipoNotificacion();
        v.severidad = n.getSeveridad();
        v.mensaje = n.getMensaje();
        v.fechaCreacion = n.getFechaCreacion();
        v.estatus = n.getEstatus();
        return v;
    }
}