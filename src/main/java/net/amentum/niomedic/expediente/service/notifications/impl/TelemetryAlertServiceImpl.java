package net.amentum.niomedic.expediente.service.notifications.impl;

import net.amentum.niomedic.expediente.rest.client.PushAlertaGatewayClient;
import net.amentum.niomedic.expediente.service.AlertaNotificacionService;
import net.amentum.niomedic.expediente.service.MedicoAsignadoService;
import net.amentum.niomedic.expediente.service.notifications.TelemetryAlertService;
import net.amentum.niomedic.expediente.views.AlertaNotificacionCreateView;
import net.amentum.niomedic.expediente.views.AlertaNotificacionView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class TelemetryAlertServiceImpl implements TelemetryAlertService {
    private static final Logger log = LoggerFactory.getLogger(TelemetryAlertServiceImpl.class);

    private final AlertaNotificacionService notiService;
    private final PushAlertaGatewayClient pushGateway;
    private final MedicoAsignadoService medicoAsignadoService;

    public TelemetryAlertServiceImpl(AlertaNotificacionService notiService,
                                     PushAlertaGatewayClient pushGateway,
                                     MedicoAsignadoService medicoAsignadoService) {
        this.notiService = notiService;
        this.pushGateway = pushGateway;
        this.medicoAsignadoService = medicoAsignadoService;
    }

    @Override
    public void evaluarGlucosa(String pacIdStr, int valor,
                               Integer urgBaja, Integer alertaAlta, Integer urgAlta,
                               LocalDateTime fecha, Integer idGroup) {
        UUID idMedico = medicoAsignadoService.obtenerUltimoMedicoDelPaciente(pacIdStr);
        if (idMedico == null) { log.debug("Sin médico asignado para {}", pacIdStr); return; }

        String sev = null;
        if (urgBaja != null && valor <= urgBaja) sev = "URGENCIA_BAJA";
        else if (alertaAlta != null && urgAlta != null && valor >= alertaAlta && valor < urgAlta) sev = "ALERTA_ALTA";
        else if (urgAlta != null && valor >= urgAlta) sev = "URGENCIA_ALTA";

        if (sev != null) {
            String msg = "Glucosa " + valor + " (" + sev + ") " + fecha;
            emitir(idMedico, pacIdStr, "GLUCOSA", sev, msg, fecha, idGroup);
        }
    }

    @Override
    public void evaluarPresion(String pacIdStr, Integer sys, Integer dia,
                               Integer alertaBajaSys, Integer alertaBajaDia,
                               Integer urgAltaSys, Integer urgAltaDia,
                               LocalDateTime fecha, Integer idGroup) {
        UUID idMedico = medicoAsignadoService.obtenerUltimoMedicoDelPaciente(pacIdStr);
        if (idMedico == null) { log.debug("Sin médico asignado para {}", pacIdStr); return; }

        String sev = null;
        // URGENCIA ALTA si cualquiera supera el umbral alto
        if ((urgAltaSys != null && sys != null && sys >= urgAltaSys) ||
                (urgAltaDia != null && dia != null && dia >= urgAltaDia)) {
            sev = "URGENCIA_ALTA";
        }
        // URGENCIA BAJA si cualquiera está por debajo del umbral bajo
        else if ((alertaBajaSys != null && sys != null && sys <= alertaBajaSys) ||
                (alertaBajaDia != null && dia != null && dia <= alertaBajaDia)) {
            sev = "URGENCIA_BAJA";
        }
        // No hay ALERTA_ALTA en PA (tu tabla no la maneja)

        if (sev != null) {
            StringBuilder val = new StringBuilder("PA ");
            if (sys != null) val.append(sys);
            if (dia != null) val.append("/").append(dia);
            String msg = val + " (" + sev + ") " + fecha;
            emitir(idMedico, pacIdStr, "PRESION", sev, msg, fecha, idGroup);
        }
    }

    @Override
    public void evaluarCovid(String pacIdStr, Double temp, Integer spo2, Integer pulso,
                             LocalDateTime fecha, Integer idGroup) {
        UUID idMedico = medicoAsignadoService.obtenerUltimoMedicoDelPaciente(pacIdStr);
        if (idMedico == null) { log.debug("Sin médico asignado para {}", pacIdStr); return; }

        boolean alarma = false; StringBuilder sb = new StringBuilder();
        if (temp  != null && temp  >= 39.0) { alarma = true; sb.append("Temp=").append(temp).append(" "); }
        if (spo2  != null && spo2  <  90  ) { alarma = true; sb.append("SpO2=").append(spo2).append("% "); }
        if (pulso != null && pulso >= 120 ) { alarma = true; sb.append("Pulso=").append(pulso); }

        if (alarma) {
            String msg = "COVID fuera de rango: " + sb.toString().trim() + " " + fecha;
            emitir(idMedico, pacIdStr, "COVID", "URGENCIA_ALTA", msg, fecha, idGroup);
        }
    }

    private void emitir(UUID idMedico, String pacIdStr, String tipo, String sev,
                        String msg, LocalDateTime fecha, Integer idGroup) {
        AlertaNotificacionCreateView c = new AlertaNotificacionCreateView();
        c.idMedico = idMedico;
        c.idPaciente = UUID.fromString(pacIdStr);
        c.tipoNotificacion = tipo;
        c.severidad = sev;
        c.mensaje = msg;
        c.idGroup = idGroup;

        AlertaNotificacionView v = notiService.createAndReturn(c);
        try {
            pushGateway.push(v);
        } catch (Exception ex) {
            log.warn("No se pudo empujar la alerta al gateway: {}", ex.toString());
        }
    }
}
