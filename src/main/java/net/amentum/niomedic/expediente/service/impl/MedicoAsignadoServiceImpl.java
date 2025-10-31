package net.amentum.niomedic.expediente.service.impl;

import java.util.UUID;

import net.amentum.niomedic.expediente.persistence.ConsultaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import net.amentum.niomedic.expediente.service.MedicoAsignadoService;

@Service
public class MedicoAsignadoServiceImpl implements MedicoAsignadoService {

    private static final Logger log = LoggerFactory.getLogger(MedicoAsignadoServiceImpl.class);

    private final ConsultaRepository consultaRepository;

    public MedicoAsignadoServiceImpl(ConsultaRepository consultaRepository) {
        this.consultaRepository = consultaRepository;
    }

    @Override
    public UUID obtenerUltimoMedicoDelPaciente(String pacIdStr) {
        try {
            if (pacIdStr == null || pacIdStr.trim().isEmpty()) {
                log.warn("pacIdStr es nulo o vacío");
                return null;
            }

            UUID pacId;
            try {
                pacId = UUID.fromString(pacIdStr);
            } catch (IllegalArgumentException iae) {
                log.warn("El pacIdStr no es un UUID válido: {}", pacIdStr);
                return null;
            }

            return consultaRepository.findUltimoMedicoPorPaciente(pacId).orElse(null);

        } catch (Exception ex) {
            log.warn("No se pudo obtener último médico de paciente {}: {}", pacIdStr, ex.getMessage());
            return null;
        }
    }
}