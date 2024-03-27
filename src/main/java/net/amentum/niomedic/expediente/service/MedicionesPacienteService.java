package net.amentum.niomedic.expediente.service;

import net.amentum.niomedic.expediente.exception.MedicionesPacienteException;
import net.amentum.niomedic.expediente.views.MedicionesPacienteView;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface MedicionesPacienteService {
   MedicionesPacienteView createMedicionesPaciente(MedicionesPacienteView medicionesPacienteView) throws MedicionesPacienteException;

   MedicionesPacienteView updateMedicionesPaciente(MedicionesPacienteView medicionesPacienteView) throws MedicionesPacienteException;

   Page<MedicionesPacienteView> getMedicionesPacienteSearch(UUID idPaciente, Long startDate, Long endDate, Integer page, Integer size, String orderColumn, String orderType) throws MedicionesPacienteException;
}
