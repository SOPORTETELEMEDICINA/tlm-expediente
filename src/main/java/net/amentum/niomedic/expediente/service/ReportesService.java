package net.amentum.niomedic.expediente.service;

import net.amentum.niomedic.expediente.exception.ConsultaException;

import java.util.UUID;

public interface ReportesService {

   String getSolicitudServicios(Long idConsulta) throws ConsultaException;

   String getReferencia(Long idConsulta) throws ConsultaException;

   String getNotasMedicasPresc(Long idConsulta, Long idGroup) throws ConsultaException;

   String getNotasEvolucion(Long idConsulta) throws ConsultaException;

//   String getSolicitudEstudios(Long idConsulta) throws ConsultaException;
   // GGR20200803 agrego idGroup
   String getSolicitudEstudios(Long idConsulta, Long folio, Long idGroup) throws ConsultaException;

   String getReceta(Long idConsulta, Long idGroup) throws ConsultaException;

   String getContrarreferencia(Long idConsulta) throws ConsultaException;

   String getConsentimiento(Long idConsulta) throws ConsultaException;

   String getNotasInterconsulta(Long idConsulta) throws ConsultaException;

   String getHistoriaClinica(String idPaciente) throws ConsultaException;
}
