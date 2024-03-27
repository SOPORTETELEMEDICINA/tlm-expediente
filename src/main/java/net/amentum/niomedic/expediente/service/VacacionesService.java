package net.amentum.niomedic.expediente.service;

import net.amentum.niomedic.expediente.exception.VacacionesException;
import net.amentum.niomedic.expediente.views.VacacionesView;

import java.util.List;
import java.util.UUID;

public interface VacacionesService {
   void createVacaciones(VacacionesView vacacionesView) throws VacacionesException;

   void updateVacaciones(VacacionesView vacacionesView) throws VacacionesException;

//   List<VacacionesView> getDetailsByMedicoId(UUID medicoId) throws VacacionesException;
   List<VacacionesView> getDetailsByIdUsuario(Long idUsuario) throws VacacionesException;
}
