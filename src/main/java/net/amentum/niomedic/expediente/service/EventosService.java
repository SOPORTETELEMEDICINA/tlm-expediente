package net.amentum.niomedic.expediente.service;

import net.amentum.niomedic.expediente.exception.EventosException;
import net.amentum.niomedic.expediente.views.EventosView;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.List;

public interface EventosService {
   EventosView createEventos(EventosView eventosView) throws EventosException;

   EventosView updateEventos(EventosView eventosView) throws EventosException;

   EventosView getDetailsByIdEventos(Long idEventos) throws EventosException;

   Page<EventosView> getEventosPage(List<Long> idUsuario, List<Integer> idTipoEvento, String titulo,
                                      Long startDate, Long endDate, List<Long> idUsuarioRecibe, List<String> idPaciente, List<String> especialidad,
                                      List<Long> regionSanitaria, List<String> unidadMedica, List<Integer> status,
                                      Integer page, Integer size, String orderColumn, String orderType) throws EventosException;

   List<EventosView> getEventosSearch(List<Long> idUsuario, List<Integer> idTipoEvento, String titulo,
                                      Long startDate, Long endDate, List<Long> idUsuarioRecibe, List<String> idPaciente, List<String> especialidad,
                                      List<Long> regionSanitaria, List<String> unidadMedica, List<Integer> status,
                                      String orderColumn, String orderType) throws EventosException;

   void deleteEventos(Long idEventos) throws EventosException;

   void rescheduleEventos(Long idEventos, Date nuevaFechaEvento, Date nuevaFechaFin) throws EventosException;

//   EventosView getDetailsByIdConsulta(Long idConsulta) throws EventosException;
   List<EventosView> getDetailsByIdConsulta(Long idConsulta) throws EventosException;

   void updateEstatusEvento(Long idEvento, Integer status) throws EventosException;

   Long getEventosValue() throws EventosException;
}
