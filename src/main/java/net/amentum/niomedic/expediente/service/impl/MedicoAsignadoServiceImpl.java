package net.amentum.niomedic.expediente.service.impl;

import net.amentum.niomedic.expediente.persistence.ConsultaRepository;
import net.amentum.niomedic.expediente.service.MedicoAsignadoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

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
            return consultaRepository.findUltimoMedicoPorPaciente(pacIdStr);
        } catch (Exception ex) {
            log.warn("No se pudo obtener último médico de paciente {}: {}", pacIdStr, ex.toString());
            return null;
        }
    }
}
