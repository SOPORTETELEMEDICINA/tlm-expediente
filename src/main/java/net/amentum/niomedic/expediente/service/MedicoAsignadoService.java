package net.amentum.niomedic.expediente.service;

import java.util.UUID;

public interface MedicoAsignadoService {
    UUID obtenerUltimoMedicoDelPaciente(String pacIdStr);

    Integer obtenerUltimoGrupoDelPaciente(String pacIdStr);
}
