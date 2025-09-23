package net.amentum.niomedic.expediente.service;

import net.amentum.niomedic.catalogos.views.CatEstadoConsultaView;
import net.amentum.niomedic.expediente.exception.ConsultaException;
import net.amentum.niomedic.expediente.views.ConsultaView;
import net.amentum.niomedic.expediente.views.SignosVitalesView;

import org.apache.xpath.operations.Bool;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public interface ConsultaService {
	ConsultaView createConsulta(ConsultaView consultaView) throws ConsultaException;

   void updateConsulta(ConsultaView consultaView) throws ConsultaException;

//   ConsultaView getDetailsByIdConsulta(String idPaciente) throws ConsultaException;
   ConsultaView getDetailsByNumeroConsulta(Long numeroConsulta) throws ConsultaException;

   List<ConsultaView> findAll() throws ConsultaException;

   //    Page<ConsultaView> getConsultaPage(String name, Integer page, Integer size, String orderColumn, String orderType) throws ConsultaException;
//    Page<ConsultaView> getConsultaPage(String name, Integer page, Integer size, String orderColumn, String orderType, String startDate, String endDate) throws ConsultaException;
   Page<ConsultaView> getConsultaPage(UUID idPaciente, List<Long> idUsuario,UUID idMedico,Integer page, Integer size, String orderColumn, String orderType, Long startDate, Long endDate, Integer idGroup, String name) throws ConsultaException;

   List<ConsultaView> getAllByPadecimiento(Long idPadecimiento) throws ConsultaException;

   void updateConsultaEstatus(ConsultaView consultaView) throws ConsultaException;
   
   //nuevas rest
   ConsultaView getConsultaById(Long idConsulta)throws ConsultaException;

    Page<ConsultaView> getConsultaSearch(
            UUID idPaciente,
            List<Long> idUsuario,
            UUID idMedico,
            Integer idTipoConsulta,
            List<Integer> idStatusConsulta,
            Integer page,
            Integer size,
            String orderColumn,
            String orderType,
            Long startDate,
            Long endDate,
            Integer idGroup // <-- NUEVO
    ) throws ConsultaException;

    void consultaStart(Long idConsulta,CatEstadoConsultaView catEstadoConsultaView) throws ConsultaException;
   
   void consultaCancel(Long idConsulta, CatEstadoConsultaView catEstadoConsultaView) throws ConsultaException;
   
   void consultreschedule(Long idConsulta, Date nuevaFechaConsulta,Date fechaConsultaFin, CatEstadoConsultaView catEstadoConsultaView) throws ConsultaException;
   
   void consultaFinish(Long idConsulta,ConsultaView consultaView) throws ConsultaException;

   void signosVitales(Long idConsulta, SignosVitalesView signosVitales) throws ConsultaException;
   
   ConsultaView getUltimaConsulta(UUID idPacinete, Integer idGroup) throws ConsultaException;
   
   void confirmarConsulta(Long idConsulta, CatEstadoConsultaView catEstadoConsultaView)throws ConsultaException;
   
   ConsultaView getSiguienteConsulta(UUID idPaciente)throws ConsultaException;
   
   void enfermeriaConsulta(Long idConsulta,CatEstadoConsultaView catEstadoConsultaView)throws ConsultaException;
   
   void createCDA(Long idConsulta)throws ConsultaException;
   
   String getUrlImagen(Long idConsulta)throws ConsultaException;

   Page<HashMap<String, Object>> getConsultaByEstatusMedico(UUID idMedico, Integer page, Integer size, String orderColumn, String orderType, Integer idGroup) throws ConsultaException;

   Page<HashMap<String, Object>> getConsultaByEstatus(Integer page, Integer size, String orderColumn, String orderType, Integer idGroup) throws ConsultaException;
    // Firma nueva para obtener la penúltima
    ConsultaView getPenultimaConsulta(UUID idPaciente, Integer idGroup) throws ConsultaException;


}
