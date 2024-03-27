package net.amentum.niomedic.expediente.service;

import net.amentum.niomedic.expediente.exception.TurnosLaboralesException;
import net.amentum.niomedic.expediente.views.TurnosLaboralesView;

import java.util.UUID;

public interface TurnosLaboralesService {
   void createTurnosLaborales(TurnosLaboralesView turnosLaboralesView) throws TurnosLaboralesException;

   void updateTurnosLaborales(TurnosLaboralesView turnosLaboralesView) throws TurnosLaboralesException;

//   TurnosLaboralesView getDetailsByMedicoId(UUID medicoId) throws TurnosLaboralesException;
   TurnosLaboralesView getDetailsByIdUsuario(Long idUsuario) throws TurnosLaboralesException;
}
