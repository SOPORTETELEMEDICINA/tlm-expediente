package net.amentum.niomedic.expediente.service.notifications;

import java.time.LocalDateTime;

public interface TelemetryAlertService {
    void evaluarGlucosa(String pacIdStr, int valor,
                        Integer urgBaja, Integer alertaAlta, Integer urgAlta,
                        LocalDateTime fecha, Integer idGroup);

    // Para PRESIÓN evaluamos sistólica y diastólica por separado
    void evaluarPresion(String pacIdStr, Integer sys, Integer dia,
                        Integer alertaBajaSys, Integer alertaBajaDia,
                        Integer urgAltaSys, Integer urgAltaDia,
                        LocalDateTime fecha, Integer idGroup);

    void evaluarCovid(String pacIdStr, Double temp, Integer spo2, Integer pulso,
                      LocalDateTime fecha, Integer idGroup);
}
