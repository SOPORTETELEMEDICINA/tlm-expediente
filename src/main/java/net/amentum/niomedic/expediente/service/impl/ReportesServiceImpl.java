package net.amentum.niomedic.expediente.service.impl;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.amentum.niomedic.expediente.beanReports.Medicamento;
import net.amentum.niomedic.expediente.configuration.ApiConfiguration;
import net.amentum.niomedic.expediente.converter.ConsultaConverter;
import net.amentum.niomedic.expediente.exception.ConsultaException;
import net.amentum.niomedic.expediente.model.Consulta;
import net.amentum.niomedic.expediente.model.Padecimiento;
import net.amentum.niomedic.expediente.model.Tratamiento;
import net.amentum.niomedic.expediente.persistence.ConsultaRepository;
import net.amentum.niomedic.expediente.persistence.PadecimientoRepository;
import net.amentum.niomedic.expediente.service.ReportesService;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.beans.factory.annotation.Value;

//import java.util.Base64;

@Service
@Transactional(readOnly = true)
@Slf4j
public class ReportesServiceImpl implements ReportesService {
   final Logger logger = LoggerFactory.getLogger(ReportesServiceImpl.class);
   private ConsultaRepository consultaRepository;
   private ConsultaConverter consultaConverter;
   private ObjectMapper mapp = new ObjectMapper();
   private ApiConfiguration apiConfiguration;

   @Value("${reportes.historiaClinica:../reportes/HistoriaClinica.jrxml}")
   private String reporteHistoriaClinica;

   @Value("${reportes.receta:/reportes/Receta.jrxml}")
   private String reportesReceta;

   // Más adelante en tu código...
   //InputStream jrxmlArchivo = getClass().getResourceAsStream("${reportes.receta}");
   
   // Sre24062020 Agrego nueva variable de ambiente para Reporte a usar de nota evol con default
   @Value("${reportes.notaevol:/reportes/NotasEvolucion.jrxml}")
   private String reportesNotaevol;
   
   // Sre24062020 Agrego nueva variable de ambiente para Reporte a usar de nota medica con default
   @Value("${reportes.notamedica:/reportes/NotasMedicasPrescr.jrxml}")
   private String reportesNotamedica;

   @Autowired
   private PadecimientoRepository padecimientoRepository;

   @Autowired
   public void setConsultaRepository(ConsultaRepository consultaRepository) {
      this.consultaRepository = consultaRepository;
   }

   @Autowired
   public void setConsultaConverter(ConsultaConverter consultaConverter) {
      this.consultaConverter = consultaConverter;
   }

   @Autowired
   public void setApiConfiguration(ApiConfiguration apiConfiguration) {
      this.apiConfiguration = apiConfiguration;
   }

   @Override
//   public String getSolicitudServicios(Long idConsulta) throws ConsultaException, JRException, IOException {
   public String getSolicitudServicios(Long idConsulta) throws ConsultaException {
      try {
         Consulta consulta = consultaRepository.findByIdConsulta(idConsulta);
         if (consulta == null) {
            ConsultaException consE = new ConsultaException("No se encuentra en el sistema Consulta.", ConsultaException.LAYER_DAO, ConsultaException.ACTION_VALIDATE);
            consE.addError("No existe la Consulta con el Id:" + idConsulta);
            throw consE;
         }

         Map<String, Object> parametros = new HashMap<>();
         parametros.put("txtOrdinario", (consulta.getUrgente() == null) ? Boolean.FALSE : !consulta.getUrgente());
         parametros.put("txtUrgente", (consulta.getUrgente() == null) ? Boolean.FALSE : consulta.getUrgente());
         parametros.put("txtNombre", (consulta.getNombrePaciente() == null) ? "" : consulta.getNombrePaciente());
         parametros.put("txtNss", "");
         parametros.put("txtAtendidoPor", (consulta.getNombreMedico() == null) ? "" : consulta.getNombreMedico());
         parametros.put("txtEnviadoPor", (consulta.getNombreMedicoSolicitante() == null) ? "" : consulta.getNombreMedicoSolicitante());
         parametros.put("txtFecha", (consulta.getFechaConsulta() == null) ? null : consulta.getFechaConsulta());

         InputStream jrxmlArchivo = getClass().getResourceAsStream("/reportes/SolicitudServicios.jrxml");
         JasperReport jasperArchivo = JasperCompileManager.compileReport(jrxmlArchivo);
         byte[] byteReporte = JasperRunManager.runReportToPdf(jasperArchivo, parametros, new JREmptyDataSource());

         byte[] codificado = Base64.encodeBase64(byteReporte);
         String pdfFile = IOUtils.toString(codificado, "UTF-8");

         return pdfFile;
      } catch (ConsultaException consE) {
         throw consE;
      } catch (DataIntegrityViolationException dive) {
         ConsultaException consE = new ConsultaException("No fue posible editar Consulta", ConsultaException.LAYER_DAO, ConsultaException.ACTION_SELECT);
         consE.addError("No fue posible obtener detalles Consulta");
         log.error("Error al obtener detalles Consulta - CODE {} - {}", consE.getExceptionCode(), idConsulta, consE);
         throw consE;
      } catch (JRException jreE) {
         ConsultaException consE = new ConsultaException("No se pudo construir el reporte", ConsultaException.LAYER_DAO, ConsultaException.ACTION_SELECT);
//         log.error("Error al obtener detalles Consulta - CODE {} - {}", consE.getExceptionCode(), idConsulta, consE); //tira TODA la exception
         if (jreE.getMessage().contains("java.net.MalformedURLException")) {
            log.error("===>>>No existe el archivo jrxml");
            consE.addError("No existe el archivo jrxml");
         } else {
            log.error("===>>>{}", jreE.getMessage());
            consE.addError("" + jreE.getMessage());
         }
         throw consE;
      } catch (Exception ex) {
         ConsultaException consE = new ConsultaException("Error inesperado al obtener detalles Consulta", ConsultaException.LAYER_DAO, ConsultaException.ACTION_SELECT);
         consE.addError("Ocurrió un error al obtener detalles Consulta");
         log.error("Error al obtener detalles Consulta - CODE {} - {}", consE.getExceptionCode(), idConsulta, consE);
         log.error(ex.getMessage());
         throw consE;
      }
   }

   @Override
   public String getReferencia(Long idConsulta) throws ConsultaException {
      try {
         Consulta consulta = consultaRepository.findByIdConsulta(idConsulta);
         if (consulta == null) {
            ConsultaException consE = new ConsultaException("No se encuentra en el sistema Consulta.", ConsultaException.LAYER_DAO, ConsultaException.ACTION_VALIDATE);
            consE.addError("No existe la Consulta con el Id:" + idConsulta);
            throw consE;
         }

         Map<String, Object> parametros = new HashMap<>();
         parametros.put("txtOrdinario", (consulta.getUrgente() == null) ? Boolean.FALSE : !consulta.getUrgente());
         parametros.put("txtUrgente", (consulta.getUrgente() == null) ? Boolean.FALSE : consulta.getUrgente());
         parametros.put("txtFolio", "Folio: " + (consulta.getNumeroConsulta() == null ? "" : consulta.getNumeroConsulta()));
         parametros.put("txtFechaSolicitud", (consulta.getFechaCrecion() == null) ? null : consulta.getFechaCrecion());
         parametros.put("txtEnvioEspecialidad", (consulta.getEspecialidad() == null) ? "" : consulta.getEspecialidad());

         try {
            Map<String, Object> paciente = apiConfiguration.getPacieteByid(consulta.getIdPaciente().toString());
            parametros.put("txtNss", ((String) paciente.get("numeroAfiliacion")) == null ? "" : (String) paciente.get("numeroAfiliacion"));
            parametros.put("txtCurp", ((String) paciente.get("curp")) == null ? "" : (String) paciente.get("curp"));
         } catch (Exception ex) {
            parametros.put("txtNss", "");
            parametros.put("txtCurp", "");
         }

         try {
            Map<String, Object> medico = apiConfiguration.getMedicoByid(consulta.getIdMedico().toString());
            Long idUnidadMedica = ((Integer) medico.get("idUnidadMedica")).longValue();
            Map<String, Object> unidadMedica = apiConfiguration.getUnidadMeicaByid(idUnidadMedica);

            String nombreTipologia = ((String) unidadMedica.get("nombreTipologia")) == null ? "" : (String) unidadMedica.get("nombreTipologia");
            String nombreUnidad = ((String) unidadMedica.get("nombreUnidad")) == null ? "" : (String) unidadMedica.get("nombreUnidad");

            parametros.put("txtUnidadAlaEnvia", nombreTipologia + "-" + nombreUnidad);
            parametros.put("txtUAEdelegacion", ((String) unidadMedica.get("nombreJurisdiccion")) == null ? "" : (String) unidadMedica.get("nombreJurisdiccion"));
            ////
            parametros.put("txtUnidadEnvia", nombreTipologia + "-" + nombreUnidad);
            parametros.put("txtUEdelegacion", ((String) unidadMedica.get("nombreJurisdiccion")) == null ? "" : (String) unidadMedica.get("nombreJurisdiccion"));
         } catch (Exception ex) {
            parametros.put("txtUnidadAlaEnvia", "");
            parametros.put("txtUAEdelegacion", "");
            ////
            parametros.put("txtUnidadEnvia", "");
            parametros.put("txtUEdelegacion", "");
         }

//         try {
//            Map<String, Object> medico = apiConfiguration.getMedicoByid(consulta.getIdMedico().toString());
//            Long idUnidadMedica = ((Integer) medico.get("idUnidadMedica")).longValue();
//            Map<String, Object> unidadMedica = apiConfiguration.getUnidadMeicaByid(idUnidadMedica);
//            parametros.put("txtUnidadEnvia", ((String) unidadMedica.get("nombreUnidad")) == null ? "" : (String) unidadMedica.get("nombreUnidad"));
//            parametros.put("txtUEdelegacion", ((String) unidadMedica.get("nombreJurisdiccion")) == null ? "" : (String) unidadMedica.get("nombreJurisdiccion"));
//         } catch (Exception ex) {
//            parametros.put("txtUnidadEnvia", "");
//            parametros.put("txtUEdelegacion", "");
//         }

         parametros.put("txtFechaCitaPrimera", (consulta.getFechaConsulta() == null) ? null : consulta.getFechaConsulta());
         parametros.put("txtNombrePaciente", (consulta.getNombrePaciente() == null) ? "" : consulta.getNombrePaciente());


         Set<Padecimiento> diagnosticosSet = consulta.getPadecimiento();
         if (diagnosticosSet != null && !diagnosticosSet.isEmpty()) {
            String diagnosticos = "";
            for (Padecimiento pade : diagnosticosSet) {
               diagnosticos += pade.getNombrePadecimiento() + ",  ";
            }
            diagnosticos = diagnosticos.substring(0, diagnosticos.length() - 3);
            parametros.put("txtDiagnosticoEnvio", diagnosticos);
         } else {
            parametros.put("txtDiagnosticoEnvio", "");
         }



//         parametros.put("txtResumenClinico", (consulta.getResumen() == null) ? "" : consulta.getResumen());
         parametros.put("txtResumenClinico", (consulta.getMotivoConsulta() == null) ? "" : consulta.getMotivoConsulta());
         if (consulta.getIdMotivoEnvio() == 1) {
            parametros.put("txtMotivo1", Boolean.TRUE);
            parametros.put("txtMotivo2", Boolean.FALSE);
            parametros.put("txtMotivo3", Boolean.FALSE);
            parametros.put("txtMotivo4", Boolean.FALSE);
            parametros.put("txtMotivo5", Boolean.FALSE);
            parametros.put("txtMotivo6", Boolean.FALSE);
            parametros.put("txtMotivo7", "");
            parametros.put("txtMotivo8", Boolean.FALSE);
            parametros.put("txtMotivoDetalle8", "");
         }
         if (consulta.getIdMotivoEnvio() == 2) {
            parametros.put("txtMotivo1", Boolean.FALSE);
            parametros.put("txtMotivo2", Boolean.TRUE);
            parametros.put("txtMotivo3", Boolean.FALSE);
            parametros.put("txtMotivo4", Boolean.FALSE);
            parametros.put("txtMotivo5", Boolean.FALSE);
            parametros.put("txtMotivo6", Boolean.FALSE);
            parametros.put("txtMotivo7", "");
            parametros.put("txtMotivo8", Boolean.FALSE);
            parametros.put("txtMotivoDetalle8", "");
         }
         if (consulta.getIdMotivoEnvio() == 3) {
            parametros.put("txtMotivo1", Boolean.FALSE);
            parametros.put("txtMotivo2", Boolean.FALSE);
            parametros.put("txtMotivo3", Boolean.TRUE);
            parametros.put("txtMotivo4", Boolean.FALSE);
            parametros.put("txtMotivo5", Boolean.FALSE);
            parametros.put("txtMotivo6", Boolean.FALSE);
            parametros.put("txtMotivo7", "");
            parametros.put("txtMotivo8", Boolean.FALSE);
            parametros.put("txtMotivoDetalle8", "");
         }
         if (consulta.getIdMotivoEnvio() == 4) {
            parametros.put("txtMotivo1", Boolean.FALSE);
            parametros.put("txtMotivo2", Boolean.FALSE);
            parametros.put("txtMotivo3", Boolean.FALSE);
            parametros.put("txtMotivo4", Boolean.TRUE);
            parametros.put("txtMotivo5", Boolean.FALSE);
            parametros.put("txtMotivo6", Boolean.FALSE);
            parametros.put("txtMotivo7", "");
            parametros.put("txtMotivo8", Boolean.FALSE);
            parametros.put("txtMotivoDetalle8", "");
         }
         if (consulta.getIdMotivoEnvio() == 5) {
            parametros.put("txtMotivo1", Boolean.FALSE);
            parametros.put("txtMotivo2", Boolean.FALSE);
            parametros.put("txtMotivo3", Boolean.FALSE);
            parametros.put("txtMotivo4", Boolean.FALSE);
            parametros.put("txtMotivo5", Boolean.TRUE);
            parametros.put("txtMotivo6", Boolean.FALSE);
            parametros.put("txtMotivo7", "");
            parametros.put("txtMotivo8", Boolean.FALSE);
            parametros.put("txtMotivoDetalle8", "");
         }
         if (consulta.getIdMotivoEnvio() == 6) {
            parametros.put("txtMotivo1", Boolean.FALSE);
            parametros.put("txtMotivo2", Boolean.FALSE);
            parametros.put("txtMotivo3", Boolean.FALSE);
            parametros.put("txtMotivo4", Boolean.FALSE);
            parametros.put("txtMotivo5", Boolean.FALSE);
            parametros.put("txtMotivo6", Boolean.TRUE);
            parametros.put("txtMotivo7", "");
            parametros.put("txtMotivo8", Boolean.FALSE);
            parametros.put("txtMotivoDetalle8", "");
         }
         if (consulta.getIdMotivoEnvio() == 7) {
            parametros.put("txtMotivo1", Boolean.FALSE);
            parametros.put("txtMotivo2", Boolean.FALSE);
            parametros.put("txtMotivo3", Boolean.FALSE);
            parametros.put("txtMotivo4", Boolean.FALSE);
            parametros.put("txtMotivo5", Boolean.FALSE);
            parametros.put("txtMotivo6", Boolean.FALSE);
            parametros.put("txtMotivo7", (consulta.getMotivoEnvio() == null) ? null : consulta.getMotivoEnvio());
            parametros.put("txtMotivo8", Boolean.FALSE);
            parametros.put("txtMotivoDetalle8", "");
         }
         if (consulta.getIdMotivoEnvio() == 8) {
            parametros.put("txtMotivo1", Boolean.FALSE);
            parametros.put("txtMotivo2", Boolean.FALSE);
            parametros.put("txtMotivo3", Boolean.FALSE);
            parametros.put("txtMotivo4", Boolean.FALSE);
            parametros.put("txtMotivo5", Boolean.FALSE);
            parametros.put("txtMotivo6", Boolean.FALSE);
            parametros.put("txtMotivo7", "");
            parametros.put("txtMotivo8", Boolean.TRUE);
            parametros.put("txtMotivoDetalle8", "No se especifica");
         }

         try {
            Map<String, Object> incapacidad = mapp.convertValue(consulta.getIncapacidadTemporal(), Map.class);

            parametros.put("txtNumFolio", ((Integer) incapacidad.get("folio") == null) ? "" : "" + (Integer) incapacidad.get("folio"));
            parametros.put("txtDias", ((Integer) incapacidad.get("days") == null) ? "" : "" + (Integer) incapacidad.get("days"));
            try {
//               Date fechaInicio = new Date((String) incapacidad.get("startDate"));
               Date fechaInicio = new SimpleDateFormat("yyyy-MM-dd").parse((String) incapacidad.get("startDate"));
               parametros.put("txtFechaInicio", fechaInicio);
            } catch (Exception ex) {
               parametros.put("txtFechaInicio", null);
            }
            String tipo = (String) incapacidad.get("type");
//            por default
            parametros.put("txtInicial", Boolean.FALSE);
            parametros.put("txtSubsecuente", Boolean.FALSE);
            if (tipo.equalsIgnoreCase("inicial")) {
               parametros.put("txtInicial", Boolean.TRUE);
               parametros.put("txtSubsecuente", Boolean.FALSE);
            } else {
               parametros.put("txtInicial", Boolean.FALSE);
               parametros.put("txtSubsecuente", Boolean.TRUE);
            }
            parametros.put("txtRamoSeguro", ((String) incapacidad.get("insuranceType") == null) ? "" : (String) incapacidad.get("insuranceType"));
            String razon = (String) incapacidad.get("reason");
//            por default
            parametros.put("txtEnfermedadGral", Boolean.FALSE);
            parametros.put("txtRiesgoTrabajo", Boolean.FALSE);
            parametros.put("txtMaternidad", Boolean.FALSE);
            if (razon.equalsIgnoreCase("Enfermedad general")) {
               parametros.put("txtEnfermedadGral", Boolean.TRUE);
               parametros.put("txtRiesgoTrabajo", Boolean.FALSE);
               parametros.put("txtMaternidad", Boolean.FALSE);
            }
            if (razon.equalsIgnoreCase("Riesgo de Trabajo")) {
               parametros.put("txtEnfermedadGral", Boolean.FALSE);
               parametros.put("txtRiesgoTrabajo", Boolean.TRUE);
               parametros.put("txtMaternidad", Boolean.FALSE);
            }
            if (razon.equalsIgnoreCase("Maternidad")) {
               parametros.put("txtEnfermedadGral", Boolean.FALSE);
               parametros.put("txtRiesgoTrabajo", Boolean.FALSE);
               parametros.put("txtMaternidad", Boolean.TRUE);
            }
            parametros.put("txtDiasAcumulados", ((Integer) incapacidad.get("accumulatedDays") == null) ? "" : "---" + (Integer) incapacidad.get("accumulatedDays") + "---");

            try {
               Map<String, Object> medTratante = mapp.convertValue(consulta.getIncapacidadTemporal().get("attendingPhysician"), Map.class);
               parametros.put("txtMedicoTratante", ((String) medTratante.get("name") == null) ? "" : (String) medTratante.get("name"));
               parametros.put("txtMatriculaTratante", ((String) medTratante.get("license") == null) ? "" : (String) medTratante.get("license"));
            } catch (Exception ex) {
               parametros.put("txtMedicoTratante", "");
               parametros.put("txtMatriculaTratante", "");
            }

            try {
               Map<String, Object> medDirectivo = mapp.convertValue(consulta.getIncapacidadTemporal().get("authorizerPhysician"), Map.class);
               parametros.put("txtMedicoDirectivo", ((String) medDirectivo.get("name") == null) ? "" : (String) medDirectivo.get("name"));
               parametros.put("txtMatriculaDirectivo", ((String) medDirectivo.get("license") == null) ? "" : (String) medDirectivo.get("license"));
            } catch (Exception ex) {
               parametros.put("txtMedicoDirectivo", "");
               parametros.put("txtMatriculaDirectivo", "");
            }

         } catch (Exception ex) {
            parametros.put("txtNumFolio", "");
            parametros.put("txtDias", "");
            parametros.put("txtFechaInicio", null);
            parametros.put("txtInicial", Boolean.FALSE);
            parametros.put("txtSubsecuente", Boolean.FALSE);
            parametros.put("txtRamoSeguro", "");
            parametros.put("txtEnfermedadGral", Boolean.FALSE);
            parametros.put("txtRiesgoTrabajo", Boolean.FALSE);
            parametros.put("txtMaternidad", Boolean.FALSE);
            parametros.put("txtDiasAcumulados", "");
            parametros.put("txtMedicoTratante", "");
            parametros.put("txtMatriculaTratante", "");
            parametros.put("txtMedicoDirectivo", "");
            parametros.put("txtMatriculaDirectivo", "");
         }

         InputStream jrxmlArchivo = getClass().getResourceAsStream("/reportes/Referencia.jrxml");
         JasperReport jasperArchivo = JasperCompileManager.compileReport(jrxmlArchivo);
         byte[] byteReporte = JasperRunManager.runReportToPdf(jasperArchivo, parametros, new JREmptyDataSource());

         byte[] codificado = Base64.encodeBase64(byteReporte);
         String pdfFile = IOUtils.toString(codificado, "UTF-8");

         return pdfFile;
      } catch (ConsultaException consE) {
         throw consE;
      } catch (DataIntegrityViolationException dive) {
         ConsultaException consE = new ConsultaException("No fue posible editar Consulta", ConsultaException.LAYER_DAO, ConsultaException.ACTION_SELECT);
         consE.addError("No fue posible obtener detalles Consulta");
         log.error("Error al obtener detalles Consulta - CODE {} - {}", consE.getExceptionCode(), idConsulta, consE);
         throw consE;
      } catch (JRException jreE) {
         ConsultaException consE = new ConsultaException("No se pudo construir el reporte", ConsultaException.LAYER_DAO, ConsultaException.ACTION_SELECT);
//         log.error("Error al obtener detalles Consulta - CODE {} - {}", consE.getExceptionCode(), idConsulta, consE); //tira TODA la exception
         if (jreE.getMessage().contains("java.net.MalformedURLException")) {
            log.error("===>>>No existe el archivo jrxml");
            consE.addError("No existe el archivo jrxml");
         } else {
            log.error("===>>>{}", jreE.getMessage());
            consE.addError("" + jreE.getMessage());
         }
         throw consE;
      } catch (Exception ex) {
         ConsultaException consE = new ConsultaException("Error inesperado al obtener detalles Consulta", ConsultaException.LAYER_DAO, ConsultaException.ACTION_SELECT);
         consE.addError("Ocurrió un error al obtener detalles Consulta");
         log.error("Error al obtener detalles Consulta - CODE {} - {}", consE.getExceptionCode(), idConsulta, consE);
         log.error(ex.getMessage());
         throw consE;
      }
   }

   @Override
   public String getNotasMedicasPresc(Long idConsulta, Long idGroup) throws ConsultaException {
      try {
         log.info("getNotasMedicas(): recibo idConsulta {} - idGroup: {}", idConsulta, idGroup);
         Consulta consulta = consultaRepository.findByIdConsulta(idConsulta);
         if (consulta == null) {
            ConsultaException consE = new ConsultaException("No se encuentra en el sistema Consulta.", ConsultaException.LAYER_DAO, ConsultaException.ACTION_VALIDATE);
            consE.addError("No existe la Consulta con el Id:" + idConsulta);
            throw consE;
         }

         Map<String, Object> parametros = new HashMap<>();

         parametros.put("txtTipoDocumento", "NOTA MÉDICA Y PRESCRIPCIÓN");
         parametros.put("txtFolio", "Folio: " + ((consulta.getNumeroConsulta() == null) ? "" : consulta.getNumeroConsulta()));
         parametros.put("datFechaCreacion", consulta.getFechaCrecion());
         parametros.put("txtNombre", (consulta.getNombrePaciente() == null) ? "" : consulta.getNombrePaciente());

         try {
            Map<String, Object> medico = apiConfiguration.getMedicoByid(consulta.getIdMedico().toString());

            if (medico.get("idUnidadMedica") != null) {
               Long idUnidadMedica = ((Integer) medico.get("idUnidadMedica")).longValue();
               Map<String, Object> unidadMedica = apiConfiguration.getUnidadMeicaByid(idUnidadMedica);
               log.info("UM(): UM UM {}", unidadMedica);

               String nombreTipologia = ((String) unidadMedica.get("nombreTipologia")) == null ? "" : (String) unidadMedica.get("nombreTipologia");
               String nombreUnidad = ((String) unidadMedica.get("nombreUnidad")) == null ? "" : (String) unidadMedica.get("nombreUnidad");

               parametros.put("txtNombreUM", nombreTipologia + "-" + nombreUnidad);

               String vialidad = ((String) unidadMedica.get("vialidad")) == null ? "" : (String) unidadMedica.get("vialidad");
               String numeroExterior = ((String) unidadMedica.get("numeroExterior")) == null ? "" : (String) unidadMedica.get("numeroExterior");
               String cp = ((String) unidadMedica.get("codigoPostal")) == null ? "" : (String) unidadMedica.get("codigoPostal");
               String nombreJurisdiccion = ((String) unidadMedica.get("nombreJurisdiccion")) == null ? "" : (String) unidadMedica.get("nombreJurisdiccion");
               String nombreEntidad = ((String) unidadMedica.get("nombreEntidad")) == null ? "" : (String) unidadMedica.get("nombreEntidad");


               String direccion = vialidad + " " + numeroExterior + " C.P. " + cp + " " + nombreJurisdiccion + " " + nombreEntidad;
               parametros.put("txtDireccionUM", direccion);
               parametros.put("txtLicSanitariaUM", ((String) unidadMedica.get("numeroLicenciaSanitaria")) == null ? "Licencia Sanitaria:" : "Licencia Sanitaria: " + (String) unidadMedica.get("numeroLicenciaSanitaria"));
            }

            //! THIS
            parametros.put("txtNombreMedico", (consulta.getNombreMedico() == null) ? "No registrada" : consulta.getNombreMedico());
            parametros.put("txtDR", (medico.get("sexo") == null) ? "No registrada" :
                    (medico.get("sexo").toString().equalsIgnoreCase("hombre") ? "DR. " : "DRA. "));
            //! THIS.END

            ArrayList especialidad = (ArrayList) medico.get("especialidadViewList");
            if (especialidad != null && !especialidad.isEmpty()) {
               Map<String, Object> esp = mapp.convertValue(especialidad.get(0), Map.class);
               parametros.put("txtCedulaMedico", "CED. PROF. " + (String) esp.get("cedula"));
               // Inicio GGR20200709 datos medico agrego Universidad y Especialidad
               String especialidadTxt = (esp.get("especialidad") == null ? "N/A" : (String) esp.get("especialidad"));
               String universidadTxt = (esp.get("universidad") == null ? "N/A" : (String) esp.get("universidad"));
               parametros.put("txtEspecialidadMedico", especialidadTxt);
               parametros.put("txtUniversidadMedico", universidadTxt);
               // Fin GGR20200709 datos medico
            }

            // Inicio GGR20200709 datos domicilio medico agrego Universidad y Especialidad, y horario de atención
            agregaDatosMedico(medico, parametros);
            // Fin GGR20200709 datos domicilio
            parametros.put("datFechaImpre", new Date());

         } catch (Exception ex) {
            parametros.put("txtNombreUM", "N/A");
            parametros.put("txtDireccionUM", "N/A");
            parametros.put("txtLicSanitariaUM", "N/A");
            parametros.put("txtNombreMedico", "N/A");
            parametros.put("txtCedulaMedico", "N/A");
            parametros.put("datFechaImpre", new Date());
            // Inicio GGR20200709 Datos adicionales del medico
            parametros.put("txtEspecialidadMedico", "N/A");
            parametros.put("txtUniversidadMedico", "N/A");
            parametros.put("txtDireccionMedico", "N/A");
            // Fin GGR20200709 Datos adicionales del medico

         }

         try {
            Map<String, Object> paciente = apiConfiguration.getPacieteByid(consulta.getIdPaciente().toString());

            parametros.put("txtSexo", ((String) paciente.get("sexo")) == null ? "" : (String) paciente.get("sexo"));
            Integer numExpe = ((Integer) paciente.get("numeroExpediente") == null) ? 0 : (Integer) paciente.get("numeroExpediente");
            parametros.put("txtExpediente", numExpe + "");
//            Integer numExpeHis = ((Integer) paciente.get("numeroExpediente") == null) ? 0 : (Integer) paciente.get("numeroExpediente");
//            parametros.put("txtExpedienteHis", "N/A");
            parametros.put("txtExpedienteHis", (consulta.getReferencia2() == null) ? "" : consulta.getReferencia2());
            parametros.put("txtCurp", ((String) paciente.get("curp")) == null ? "" : (String) paciente.get("curp"));

            try {
               Date fechaNac = new Date((Long) paciente.get("fechaNacimiento"));
               parametros.put("datFechaNac", fechaNac);

               Date today = new Date();
               Instant instant = Instant.ofEpochMilli(today.getTime());
               LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
               LocalDate localDate = localDateTime.toLocalDate();

               Instant instant2 = Instant.ofEpochMilli(fechaNac.getTime());
               LocalDateTime localDateTime2 = LocalDateTime.ofInstant(instant2, ZoneId.systemDefault());
               LocalDate localDate2 = localDateTime2.toLocalDate();

               Integer edad = Period.between(localDate2, localDate).getYears();
               parametros.put("txtEdad", edad + " años");

            } catch (Exception ex) {
               parametros.put("datFechaNac", null);
               parametros.put("txtEdad", 0 + " años");
            }

         } catch (Exception ex) {
            parametros.put("txtSexo", "");
            parametros.put("txtExpediente", "");
         }

         parametros.put("datFechaHora", consulta.getFechaConsulta());
         parametros.put("txtSubjetivo", (consulta.getSubjetivo() == null) ? "" : consulta.getSubjetivo());
         parametros.put("txtObjetivo", (consulta.getObjetivo() == null) ? "" : consulta.getObjetivo());

         try {
            Map<String, Object> signosVitales = mapp.convertValue(consulta.getSignosVitales(), Map.class);

            // Obtener y convertir temperatura
            String tempStr = String.valueOf(signosVitales.get("temperatura"));
            Integer temp = 0;
            try {
               temp = Integer.parseInt(tempStr);
            } catch (NumberFormatException e) {
               // Valor predeterminado en caso de error en la conversión
               temp = 0;
            }
            parametros.put("txtTemperatura", "Temp.: " + temp + " °C");

            // Obtener y convertir peso
            String pesoStr = String.valueOf(signosVitales.get("_peso"));
            Integer peso = 0;
            try {
               peso = Integer.parseInt(pesoStr);
            } catch (NumberFormatException e) {
               peso = 0; // Valor predeterminado en caso de error en la conversión
            }
            parametros.put("txtPeso", "Peso: " + peso + " kg");

            // Obtener y convertir talla
            String tallaStr = String.valueOf(signosVitales.get("_talla"));
            Integer talla = 0;
            try {
               talla = Integer.parseInt(tallaStr);
            } catch (NumberFormatException e) {
               talla = 0; // Valor predeterminado en caso de error en la conversión
            }
            parametros.put("txtTalla", "Talla: " + talla + " cm");

            // Obtener y convertir IMC (considerando que IMC es un valor Double)
            String imcStr = String.valueOf(signosVitales.get("_imc"));
            Double imc = 0.0;
            try {
               imc = Double.parseDouble(imcStr);
            } catch (NumberFormatException e) {
               imc = 0.0; // Valor predeterminado en caso de error en la conversión
            }
            parametros.put("txtImc", "IMC: " + imc);

            Map<String, Object> tensionArterial = mapp.convertValue(consulta.getSignosVitales().get("_tensionArterial"), Map.class);
            String sistolicaStr = String.valueOf(tensionArterial.get("sistolica"));
            Integer sistolica = 0;
            try {
               sistolica = Integer.parseInt(sistolicaStr);
            } catch (NumberFormatException e) {
               sistolica = 0; // Valor por defecto en caso de error en la conversión
            }
            String diastolicaStr = String.valueOf(tensionArterial.get("diastolica"));
            Integer diastolica = 0;
            try {
               diastolica = Integer.parseInt(diastolicaStr);
            } catch (NumberFormatException e) {
               diastolica = 0; // Valor por defecto en caso de error en la conversión
            }
            parametros.put("txtPresArte", "P.A.: " + sistolica + " / " + diastolica);

            // Convertir y manejar frecuencia cardíaca
            String frecCardiacaStr = String.valueOf(signosVitales.get("frecCardiaca"));
            Integer frecCardiaca = 0;
            try {
               frecCardiaca = Integer.parseInt(frecCardiacaStr);
            } catch (NumberFormatException e) {
               frecCardiaca = 0; // Valor por defecto en caso de error en la conversión
            }
            parametros.put("txtFrecCardiaca", "F.C.: " + frecCardiaca + " x min.");

            // Convertir y manejar frecuencia respiratoria
            String frecRespiratoriaStr = String.valueOf(signosVitales.get("frecRespiratoria"));
            Integer frecRespiratoria = 0;
            try {
               frecRespiratoria = Integer.parseInt(frecRespiratoriaStr);
            } catch (NumberFormatException e) {
               frecRespiratoria = 0; // Valor por defecto en caso de error en la conversión
            }
            parametros.put("txtFrecRespiratoria", "F.R.: " + frecRespiratoria + " x min.");

            // Establecer otros valores que son constantes o no necesitan conversión
            parametros.put("txtSatOxigeno", "Saturación Oxígeno: " + "N/A");
            parametros.put("txtEvn", "EVN: " + "N/A");
         } catch (Exception ex) {
            log.error("===>>>Algo fallo con los datos de signos vitales/tension arterial: {}", ex);
            parametros.put("txtTemperatura", "Temp.: 0 °C");
            parametros.put("txtPeso", "Peso: 0 kg");
            parametros.put("txtTalla", "Talla: 0 cm");
            parametros.put("txtImc", "IMC: ");
            parametros.put("txtPresArte", "P.A.: 0 / 0");
            parametros.put("txtFrecCardiaca", "F.C.: 0 x min.");
            parametros.put("txtFrecRespiratoria", "F.R.: 0 x min.");
            parametros.put("txtSatOxigeno", "Saturación Oxígeno: " + "N/A");
            parametros.put("txtEvn", "EVN: " + "N/A");

         }

         parametros.put("txtAnalisis", (consulta.getAnalisis() == null) ? "" : consulta.getAnalisis());

//         Set<Padecimiento> diagnosticosSet = consulta.getPadecimiento();
//         if (diagnosticosSet != null && !diagnosticosSet.isEmpty()) {
//            String diagnosticos = " ";
//            for (Padecimiento pade : diagnosticosSet) {
//               diagnosticos += pade.getNombrePadecimiento() + "\n";
//            }
//            parametros.put("txtDiagnosticos", diagnosticos);
//         } else {
//            parametros.put("txtDiagnosticos", "");
//         }
         // GGR20200709 una sola función
         agregaPadecimientosConsulta(consulta, parametros);

         parametros.put("txtPlan", (consulta.getPlanTerapeutico() == null) ? "" : consulta.getPlanTerapeutico());

         Collection<Tratamiento> tratamientoList = consulta.getTratamientoList();
         if (tratamientoList != null && !tratamientoList.isEmpty()) {
            String tratamientos = "";
            for (Tratamiento trat : tratamientoList) {
               tratamientos += trat.getProNombre() + "\n";
            }
            parametros.put("txtTratamientos", tratamientos);
         } else {
            parametros.put("txtTratamientos", "");
         }

         parametros.put("txtResumen", (consulta.getResumen() == null) ? "" : consulta.getResumen());
         parametros.put("txtPronostico", (consulta.getPronostico() == null ? "" : consulta.getPronostico()));

         try {
            Map<String, Object> receta = apiConfiguration.getRecetaByidConsulta(consulta.getIdConsulta());
            ArrayList detalleReceta = (ArrayList) receta.get("detalleRecetaViewList");
            if (detalleReceta != null && !detalleReceta.isEmpty()) {
               StringBuilder detaReceta = new StringBuilder("");
               StringBuilder detalle = new StringBuilder("");
               detalleReceta.forEach((deta) -> {
                  LinkedHashMap tempo = (LinkedHashMap) deta;
                  detalle.append((String) tempo.get("denominacionDistintiva") + " - " +
                     (String) tempo.get("denominacionGenerica") + ", " +
                     (String) tempo.get("dosis") + ", " +
                     (String) tempo.get("unidad") + ", " +
                     (String) tempo.get("viaAdministracion") + ", " +
                     (String) tempo.get("frecuencia") + ", " +
                     (String) tempo.get("periodo") + "\n");
                  detaReceta.append(detalle);
                  detalle.delete(0, detalle.length());
               });
               parametros.put("txtReceta", "" + detaReceta);
            } else {
               parametros.put("txtReceta", "");
            }
         } catch (Exception ex) {
            parametros.put("txtReceta", "");
         }

//         try {
//            List<Map<String, Object>> estudioList = apiConfiguration.getEstudioByIdConsulta(consulta.getIdConsulta());
////            System.out.println("===>>>" + estudioList);
//            if (estudioList != null || !estudioList.isEmpty()) {
//               StringBuilder estudioS = new StringBuilder();
//               StringBuilder estudio = new StringBuilder();
//               estudioList.forEach((estu) -> {
//                  LinkedHashMap tempo = (LinkedHashMap) estu;
//                  estudio.append((String) tempo.get("tipoEstudio") + ", " +
//                     (String) tempo.get("descripcionEstudio") + ", " +
//                     (String) tempo.get("preparacion") + "\n");
//                  estudioS.append(estudio);
//                  estudio.delete(0, estudio.length());
//               });
//               parametros.put("txtPrescripcion", "" + estudioS);
//            } else {
//               parametros.put("txtPrescripcion", "");
//            }
//         } catch (Exception ex) {
//            parametros.put("txtPrescripcion", "");
//         }

         try {
            List<Map<String, Object>> estudioList = apiConfiguration.getEstudioByIdConsulta(consulta.getIdConsulta());
            if (estudioList != null && !estudioList.isEmpty()) {
               StringBuilder estudioS = new StringBuilder();
               StringBuilder estudio = new StringBuilder();
               estudioList.forEach((estu) -> {
                  estudio.append("Folio: " + estu.get("folio") + "\n\t");
                  ArrayList detalle = (ArrayList) estu.get("detallesEstudioList");
                  AtomicInteger index = new AtomicInteger(1);
                  detalle.forEach((deta) -> {
                     Map<String, Object> tempo = mapp.convertValue(deta, Map.class);
                     System.out.println(deta);
//                     estudio.append(index.getAndIncrement() + ") " + (String) tempo.get("tipoEstudio") + ", " +
//                        (String) tempo.get("descripcionEstudio") + ", " +
//                        (String) tempo.get("preparacion") + "\n\n\t");
                     estudio.append(index.getAndIncrement() + ") " +
                        ((String) tempo.get("tipoEstudio") == null ? "" : (String) tempo.get("tipoEstudio")) +
                        ((String) tempo.get("descripcionEstudio") == null ? "" : ", " + (String) tempo.get("descripcionEstudio")) +
                        ((String) tempo.get("preparacion") == null ? "" : ", " + (String) tempo.get("preparacion")) + "\n\n\t");
                  });
                  estudioS.append(estudio + "\n");
                  estudio.delete(0, estudio.length());
               });
               parametros.put("txtPrescripcion", "" + estudioS);
            } else {
               parametros.put("txtPrescripcion", "");
            }
         } catch (Exception ex) {
            parametros.put("txtPrescripcion", "");
         }
         // GGR20200619 Inicia cambios para cambiar nota medica por logo de grupo o default Imagen IMSS
         try {
            String img = apiConfiguration.getImgColor(idGroup, "negro");
            parametros.put("txtImage", img);
         } catch (Exception e) {
            parametros.put("imagen", null);
         }
         // Fin GGR20200619
         // Sre24062020 Inicia Dejo configurable el reporte a usar
         InputStream jrxmlArchivo = getClass().getResourceAsStream("/reportes/NotasMedicasPrescr.jrxml");
//         InputStream jrxmlArchivo = getClass().getResourceAsStream(reportesNotamedica);
         // Sre24062020 Termina
         JasperReport jasperArchivo = JasperCompileManager.compileReport(jrxmlArchivo);
         byte[] byteReporte = JasperRunManager.runReportToPdf(jasperArchivo, parametros, new JREmptyDataSource());

         /*// Inicio pruebas GGR20200619 lo escribo en disco por que en mi entorno me da timeout
         FileOutputStream fop = new FileOutputStream(new File("C:\\Users\\Ggarcia\\Documents\\notamedica.pdf"));
         fop.write(byteReporte);
         fop.flush();
         fop.close();
         // Fin pruebas GGR20200619 */

         byte[] codificado = Base64.encodeBase64(byteReporte);
         String pdfFile = IOUtils.toString(codificado, "UTF-8");

         return pdfFile;
      } catch (ConsultaException consE) {
         throw consE;
      } catch (DataIntegrityViolationException dive) {
         ConsultaException consE = new ConsultaException("No fue posible editar Consulta", ConsultaException.LAYER_DAO, ConsultaException.ACTION_SELECT);
         consE.addError("No fue posible obtener detalles Consulta");
         log.error("Error al obtener detalles Consulta - CODE {} - {}", consE.getExceptionCode(), idConsulta, consE);
         throw consE;
      } catch (JRException jreE) {
         ConsultaException consE = new ConsultaException("No se pudo construir el reporte", ConsultaException.LAYER_DAO, ConsultaException.ACTION_SELECT);
//         log.error("Error al obtener detalles Consulta - CODE {} - {}", consE.getExceptionCode(), idConsulta, consE); //tira TODA la exception
         if (jreE.getMessage().contains("java.net.MalformedURLException")) {
            log.error("===>>>No existe el archivo jrxml");
            consE.addError("No existe el archivo jrxml");
         } else {
            log.error("===>>>{}", jreE.getMessage());
            consE.addError("" + jreE.getMessage());
            jreE.printStackTrace();
         }
         throw consE;
      } catch (Exception ex) {
         ConsultaException consE = new ConsultaException("Error inesperado al obtener detalles Consulta", ConsultaException.LAYER_DAO, ConsultaException.ACTION_SELECT);
         consE.addError("Ocurrió un error al obtener detalles Consulta");
         log.error("Error al obtener detalles Consulta - CODE {} - {}", consE.getExceptionCode(), idConsulta, consE);
         log.error(ex.getMessage());
         throw consE;
      }
   }

   @Override
   public String getNotasEvolucion(Long idConsulta) throws ConsultaException {
      try {
         Consulta consulta = consultaRepository.findByIdConsulta(idConsulta);
         if (consulta == null) {
            ConsultaException consE = new ConsultaException("No se encuentra en el sistema Consulta.", ConsultaException.LAYER_DAO, ConsultaException.ACTION_VALIDATE);
            consE.addError("No existe la Consulta con el Id:" + idConsulta);
            throw consE;
         }

         Map<String, Object> parametros = new HashMap<>();

         parametros.put("txtTipoDocumento", "NOTA DE EVOLUCIÓN");
         parametros.put("txtFolio", "Folio: " + ((consulta.getNumeroConsulta() == null) ? "" : consulta.getNumeroConsulta()));
         parametros.put("datFechaCreacion", consulta.getFechaCrecion());
         parametros.put("txtNombre", (consulta.getNombrePaciente() == null) ? "" : consulta.getNombrePaciente());

         try {
            Map<String, Object> medico = apiConfiguration.getMedicoByid(consulta.getIdMedico().toString());
            Long idUnidadMedica = ((Integer) medico.get("idUnidadMedica")).longValue();
            Map<String, Object> unidadMedica = apiConfiguration.getUnidadMeicaByid(idUnidadMedica);
//            parametros.put("txtNombreUM", ((String) unidadMedica.get("nombreUnidad")) == null ? "" : (String) unidadMedica.get("nombreUnidad"));
            String nombreTipologia = ((String) unidadMedica.get("nombreTipologia")) == null ? "" : (String) unidadMedica.get("nombreTipologia");
            String nombreUnidad = ((String) unidadMedica.get("nombreUnidad")) == null ? "" : (String) unidadMedica.get("nombreUnidad");

            parametros.put("txtNombreUM", nombreTipologia + "-" + nombreUnidad);
//            String direccion = (String) unidadMedica.get("vialidad") + " " +
//               (String) unidadMedica.get("numeroExterior") + " " +
//               "C.P. " + (String) unidadMedica.get("codigoPostal") + " " +
//               (String) unidadMedica.get("nombreJurisdiccion") + ", " +
//               "CHIAPAS";
            String vialidad = ((String) unidadMedica.get("vialidad")) == null ? "" : (String) unidadMedica.get("vialidad");
            String numeroExterior = ((String) unidadMedica.get("numeroExterior")) == null ? "" : (String) unidadMedica.get("numeroExterior");
            String cp = ((String) unidadMedica.get("codigoPostal")) == null ? "" : (String) unidadMedica.get("codigoPostal");
            String nombreJurisdiccion = ((String) unidadMedica.get("nombreJurisdiccion")) == null ? "" : (String) unidadMedica.get("nombreJurisdiccion");

            String direccion = vialidad + " " + numeroExterior + " C.P. " + cp + " " + nombreJurisdiccion + ", CHIAPAS";
//               (String) unidadMedica.get("fkEntidad");
            parametros.put("txtDireccionUM", direccion);
            parametros.put("txtLicSanitariaUM", ((String) unidadMedica.get("numeroLicenciaSanitaria")) == null ? "Licencia Sanitaria:" : "Licencia Sanitaria: " + (String) unidadMedica.get("numeroLicenciaSanitaria"));

            parametros.put("txtNombreMedico", (consulta.getNombreMedico() == null) ? "" : consulta.getNombreMedico());
            ArrayList especialidad = (ArrayList) medico.get("especialidadViewList");
            if (especialidad != null && !especialidad.isEmpty()) {
               Map<String, Object> esp = mapp.convertValue(especialidad.get(0), Map.class);
               parametros.put("txtCedulaMedico", "CED. PROF. " + (String) esp.get("cedula"));
            }
            parametros.put("datFechaImpre", new Date());

         } catch (Exception ex) {
            parametros.put("txtNombreUM", "");
            parametros.put("txtDireccionUM", "");
            parametros.put("txtLicSanitariaUM", "");
            parametros.put("txtNombreMedico", "");
            parametros.put("txtCedulaMedico", "");
            parametros.put("datFechaImpre", new Date());
         }

         try {
            Map<String, Object> paciente = apiConfiguration.getPacieteByid(consulta.getIdPaciente().toString());

            parametros.put("txtSexo", ((String) paciente.get("sexo")) == null ? "" : (String) paciente.get("sexo"));
            Integer numExpe = ((Integer) paciente.get("numeroExpediente") == null) ? 0 : (Integer) paciente.get("numeroExpediente");
            parametros.put("txtExpediente", numExpe + "");
//            Integer numExpeHis = ((Integer) paciente.get("numeroExpediente") == null) ? 0 : (Integer) paciente.get("numeroExpediente");
//            parametros.put("txtExpedienteHis", "N/A");
            parametros.put("txtExpedienteHis", (consulta.getReferencia2() == null) ? "" : consulta.getReferencia2());
            parametros.put("txtCurp", ((String) paciente.get("curp")) == null ? "" : (String) paciente.get("curp"));

            try {
               Date fechaNac = new Date((Long) paciente.get("fechaNacimiento"));
               parametros.put("datFechaNac", fechaNac);

               Date today = new Date();
               Instant instant = Instant.ofEpochMilli(today.getTime());
               LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
               LocalDate localDate = localDateTime.toLocalDate();

               Instant instant2 = Instant.ofEpochMilli(fechaNac.getTime());
               LocalDateTime localDateTime2 = LocalDateTime.ofInstant(instant2, ZoneId.systemDefault());
               LocalDate localDate2 = localDateTime2.toLocalDate();

               Integer edad = Period.between(localDate2, localDate).getYears();
               parametros.put("txtEdad", edad + " años");

            } catch (Exception ex) {
               parametros.put("datFechaNac", null);
               parametros.put("txtEdad", 0 + " años");
            }

         } catch (Exception ex) {
            parametros.put("txtSexo", "");
            parametros.put("txtExpediente", "");
         }

         parametros.put("datFechaHora", consulta.getFechaConsulta());
         parametros.put("txtSubjetivo", (consulta.getSubjetivo() == null) ? "" : consulta.getSubjetivo());
         parametros.put("txtObjetivo", (consulta.getObjetivo() == null) ? "" : consulta.getObjetivo());

         try {
            Map<String, Object> signosVitales = mapp.convertValue(consulta.getSignosVitales(), Map.class);

            Integer temp = ((Integer) signosVitales.get("temperatura") == null) ? 0 : (Integer) signosVitales.get("temperatura");
            parametros.put("txtTemperatura", "Temp.: " + temp + " °C");
            Integer peso = ((Integer) signosVitales.get("_peso") == null) ? 0 : (Integer) signosVitales.get("_peso");
            parametros.put("txtPeso", "Peso: " + peso + " kg");
            Integer talla = ((Integer) signosVitales.get("_talla") == null) ? 0 : (Integer) signosVitales.get("_talla");
            parametros.put("txtTalla", "Talla: " + talla + " cm");
            Double imc = ((Double) signosVitales.get("_imc") == null) ? 0.0 : (Double) signosVitales.get("_imc");
            parametros.put("txtImc", "IMC: " + imc);

            Map<String, Object> tensionArterial = mapp.convertValue(consulta.getSignosVitales().get("_tensionArterial"), Map.class);
            Integer sistolica = ((Integer) tensionArterial.get("sistolica") == null) ? 0 : (Integer) tensionArterial.get("sistolica");
            Integer diastolica = ((Integer) tensionArterial.get("diastolica") == null) ? 0 : (Integer) tensionArterial.get("diastolica");
            parametros.put("txtPresArte", "P.A.: " + sistolica + " / " + diastolica);
            Integer frecCardiaca = ((Integer) signosVitales.get("frecCardiaca") == null) ? 0 : (Integer) signosVitales.get("frecCardiaca");
            parametros.put("txtFrecCardiaca", "F.C.: " + frecCardiaca + " x min.");
            Integer frecRespiratoria = ((Integer) signosVitales.get("frecRespiratoria") == null) ? 0 : (Integer) signosVitales.get("frecRespiratoria");
            parametros.put("txtFrecRespiratoria", "F.R.: " + frecRespiratoria + " x min.");
            parametros.put("txtSatOxigeno", "Saturación Oxígeno: " + "N/A");
            parametros.put("txtEvn", "EVN: " + "N/A");
         } catch (Exception ex) {
            log.error("===>>>Algo fallo con los datos de signos vitales/tension arterial: {}", ex);
            parametros.put("txtTemperatura", "Temp.: 0 °C");
            parametros.put("txtPeso", "Peso: 0 kg");
            parametros.put("txtTalla", "Talla: 0 cm");
            parametros.put("txtImc", "IMC: ");
            parametros.put("txtPresArte", "P.A.: 0 / 0");
            parametros.put("txtFrecCardiaca", "F.C.: 0 x min.");
            parametros.put("txtFrecRespiratoria", "F.R.: 0 x min.");
            parametros.put("txtSatOxigeno", "Saturación Oxígeno: " + "N/A");
            parametros.put("txtEvn", "EVN: " + "N/A");
         }

         parametros.put("txtAnalisis", (consulta.getAnalisis() == null) ? "" : consulta.getAnalisis());

//         Set<Padecimiento> diagnosticosSet = consulta.getPadecimiento();
//         if (diagnosticosSet != null && !diagnosticosSet.isEmpty()) {
//            String diagnosticos = " ";
//            for (Padecimiento pade : diagnosticosSet) {
//               diagnosticos += pade.getNombrePadecimiento() + "\n";
//            }
//            parametros.put("txtDiagnosticos", diagnosticos);
//         } else {
//            parametros.put("txtDiagnosticos", "");
//         }
         // GGR20200709 una sola funcion
         agregaPadecimientosConsulta(consulta, parametros);

         parametros.put("txtPlan", (consulta.getPlanTerapeutico() == null) ? "" : consulta.getPlanTerapeutico());

         Collection<Tratamiento> tratamientoList = consulta.getTratamientoList();
         if (tratamientoList != null && !tratamientoList.isEmpty()) {
            String tratamientos = "";
            for (Tratamiento trat : tratamientoList) {
               tratamientos += trat.getProNombre() + "\n";
            }
            parametros.put("txtTratamientos", tratamientos);
         } else {
            parametros.put("txtTratamientos", "");
         }

         parametros.put("txtResumen", (consulta.getResumen() == null) ? "" : consulta.getResumen());
         parametros.put("txtPronostico", (consulta.getPronostico() == null ? "" : consulta.getPronostico()));

         try {
            Map<String, Object> receta = apiConfiguration.getRecetaByidConsulta(consulta.getIdConsulta());
            ArrayList detalleReceta = (ArrayList) receta.get("detalleRecetaViewList");
            if (detalleReceta != null && !detalleReceta.isEmpty()) {
               StringBuilder detaReceta = new StringBuilder("");
               StringBuilder detalle = new StringBuilder("");
               detalleReceta.forEach((deta) -> {
                  LinkedHashMap tempo = (LinkedHashMap) deta;
                  detalle.append((String) tempo.get("denominacionDistintiva") + " - " +
                     (String) tempo.get("denominacionGenerica") + ", " +
                     (String) tempo.get("dosis") + ", " +
                     (String) tempo.get("unidad") + ", " +
                     (String) tempo.get("viaAdministracion") + ", " +
                     (String) tempo.get("frecuencia") + ", " +
                     (String) tempo.get("periodo") + "\n");
                  detaReceta.append(detalle);
                  detalle.delete(0, detalle.length());
               });
               parametros.put("txtReceta", "" + detaReceta);
            } else {
               parametros.put("txtReceta", "");
            }
         } catch (Exception ex) {
            parametros.put("txtReceta", "");
         }

//         try {
//            List<Map<String, Object>> estudioList = apiConfiguration.getEstudioByIdConsulta(consulta.getIdConsulta());
////            System.out.println("===>>>" + estudioList);
//            if (estudioList != null || !estudioList.isEmpty()) {
//               StringBuilder estudioS = new StringBuilder();
//               StringBuilder estudio = new StringBuilder();
//               estudioList.forEach((estu) -> {
//                  LinkedHashMap tempo = (LinkedHashMap) estu;
//                  estudio.append((String) tempo.get("tipoEstudio") + ", " +
//                     (String) tempo.get("descripcionEstudio") + ", " +
//                     (String) tempo.get("preparacion") + "\n");
//                  estudioS.append(estudio);
//                  estudio.delete(0, estudio.length());
//               });
//               parametros.put("txtPrescripcion", "" + estudioS);
//            } else {
//               parametros.put("txtPrescripcion", "");
//            }
//         } catch (Exception ex) {
//            parametros.put("txtPrescripcion", "");
//         }

         try {
            List<Map<String, Object>> estudioList = apiConfiguration.getEstudioByIdConsulta(consulta.getIdConsulta());
            if (estudioList != null && !estudioList.isEmpty()) {
               StringBuilder estudioS = new StringBuilder();
               StringBuilder estudio = new StringBuilder();
               estudioList.forEach((estu) -> {
                  estudio.append("Folio: " + estu.get("folio") + "\n\t");
                  ArrayList detalle = (ArrayList) estu.get("detallesEstudioList");
                  AtomicInteger index = new AtomicInteger(1);
                  detalle.forEach((deta) -> {
                     Map<String, Object> tempo = mapp.convertValue(deta, Map.class);
                     System.out.println(deta);
//                     estudio.append(index.getAndIncrement() + ") " + (String) tempo.get("tipoEstudio") + ", " +
//                        (String) tempo.get("descripcionEstudio") + ", " +
//                        (String) tempo.get("preparacion") + "\n\n\t");
                     estudio.append(index.getAndIncrement() + ") " +
                        ((String) tempo.get("tipoEstudio") == null ? "" : (String) tempo.get("tipoEstudio")) +
                        ((String) tempo.get("descripcionEstudio") == null ? "" : ", " + (String) tempo.get("descripcionEstudio")) +
                        ((String) tempo.get("preparacion") == null ? "" : ", " + (String) tempo.get("preparacion")) + "\n\n\t");
                  });
                  estudioS.append(estudio + "\n");
                  estudio.delete(0, estudio.length());
               });
               parametros.put("txtPrescripcion", "" + estudioS);
            } else {
               parametros.put("txtPrescripcion", "");
            }
         } catch (Exception ex) {
            parametros.put("txtPrescripcion", "");
         }


         // Sre24062020 Inicia Dejo configurable el reporte a usar
         //InputStream jrxmlArchivo = getClass().getResourceAsStream("/reportes/NotasEvolucion.jrxml");
         InputStream jrxmlArchivo = getClass().getResourceAsStream(reportesNotaevol);
         // Sre24062020 Termina

         JasperReport jasperArchivo = JasperCompileManager.compileReport(jrxmlArchivo);
         byte[] byteReporte = JasperRunManager.runReportToPdf(jasperArchivo, parametros, new JREmptyDataSource());

         byte[] codificado = Base64.encodeBase64(byteReporte);
         String pdfFile = IOUtils.toString(codificado, "UTF-8");

         return pdfFile;
      } catch (ConsultaException consE) {
         throw consE;
      } catch (DataIntegrityViolationException dive) {
         ConsultaException consE = new ConsultaException("No fue posible editar Consulta", ConsultaException.LAYER_DAO, ConsultaException.ACTION_SELECT);
         consE.addError("No fue posible obtener detalles Consulta");
         log.error("Error al obtener detalles Consulta - CODE {} - {}", consE.getExceptionCode(), idConsulta, consE);
         throw consE;
      } catch (JRException jreE) {
         ConsultaException consE = new ConsultaException("No se pudo construir el reporte", ConsultaException.LAYER_DAO, ConsultaException.ACTION_SELECT);
//         log.error("Error al obtener detalles Consulta - CODE {} - {}", consE.getExceptionCode(), idConsulta, consE); //tira TODA la exception
         if (jreE.getMessage().contains("java.net.MalformedURLException")) {
            log.error("===>>>No existe el archivo jrxml");
            consE.addError("No existe el archivo jrxml");
         } else {
            log.error("===>>>{}", jreE.getMessage());
            consE.addError("" + jreE.getMessage());
         }
         throw consE;
      } catch (Exception ex) {
         ConsultaException consE = new ConsultaException("Error inesperado al obtener detalles Consulta", ConsultaException.LAYER_DAO, ConsultaException.ACTION_SELECT);
         consE.addError("Ocurrió un error al obtener detalles Consulta");
         log.error("Error al obtener detalles Consulta - CODE {} - {}", consE.getExceptionCode(), idConsulta, consE);
         log.error(ex.getMessage());
         throw consE;
      }
   }

   // GGR202000803 agrego idGroup
   @Override
   public String getSolicitudEstudios(Long idConsulta, Long folio, Long idGroup) throws ConsultaException {
      log.info("getSolicitudEstudios(): recibo idConsulta {} - folio{} -idGroup: {}", idConsulta, folio, idGroup);
      try {
         Consulta consulta = consultaRepository.findByIdConsulta(idConsulta);
         if (consulta == null) {
            ConsultaException consE = new ConsultaException("No se encuentra en el sistema Consulta.", ConsultaException.LAYER_DAO, ConsultaException.ACTION_VALIDATE);
            consE.addError("No existe la Consulta con el Id:" + idConsulta);
            throw consE;
         }

         Map<String, Object> parametros = new HashMap<>();
         // GGR20200709 una sola funcion
         agregaPadecimientosConsulta(consulta, parametros);
         // Fin GGR20200709
         // Inicio GGR20200803 agreo fecha de consulta y folio
         parametros.put("datFechaHora", consulta.getFechaConsulta());
         parametros.put("txtFolio", "Folio: " + ((consulta.getNumeroConsulta() == null) ? "" : consulta.getNumeroConsulta()));
         // Fin GGR20200803
         parametros.put("txtTipoDocumento", "SOLICITUD DE LABORATORIO E IMAGENOLOGÍA");
         parametros.put("datFechaCreacion", consulta.getFechaCrecion());
         parametros.put("txtNombre", (consulta.getNombrePaciente() == null) ? "" : consulta.getNombrePaciente());

         try {
            Map<String, Object> medico = apiConfiguration.getMedicoByid(consulta.getIdMedico().toString());

            if (medico.get("idUnidadMedica") != null) {
               Long idUnidadMedica = ((Integer) medico.get("idUnidadMedica")).longValue();
               Map<String, Object> unidadMedica = apiConfiguration.getUnidadMeicaByid(idUnidadMedica);
//            parametros.put("txtNombreUM", ((String) unidadMedica.get("nombreUnidad")) == null ? "" : (String) unidadMedica.get("nombreUnidad"));
               String nombreTipologia = ((String) unidadMedica.get("nombreTipologia")) == null ? "" : (String) unidadMedica.get("nombreTipologia");
               String nombreUnidad = ((String) unidadMedica.get("nombreUnidad")) == null ? "" : (String) unidadMedica.get("nombreUnidad");

               parametros.put("txtNombreUM", nombreTipologia + "-" + nombreUnidad);
//            String direccion = (String) unidadMedica.get("vialidad") + " " +
//               (String) unidadMedica.get("numeroExterior") + " " +
//               "C.P. " + (String) unidadMedica.get("codigoPostal") + " " +
//               (String) unidadMedica.get("nombreJurisdiccion") + ", " +
//               "CHIAPAS";
               String vialidad = ((String) unidadMedica.get("vialidad")) == null ? "" : (String) unidadMedica.get("vialidad");
               String numeroExterior = ((String) unidadMedica.get("numeroExterior")) == null ? "" : (String) unidadMedica.get("numeroExterior");
               String cp = ((String) unidadMedica.get("codigoPostal")) == null ? "" : (String) unidadMedica.get("codigoPostal");
               String nombreJurisdiccion = ((String) unidadMedica.get("nombreJurisdiccion")) == null ? "" : (String) unidadMedica.get("nombreJurisdiccion");

               String direccion = vialidad + " " + numeroExterior + " C.P. " + cp + " " + nombreJurisdiccion + ", CHIAPAS";
//               (String) unidadMedica.get("fkEntidad");
               parametros.put("txtDireccionUM", direccion);
               parametros.put("txtLicSanitariaUM", ((String) unidadMedica.get("numeroLicenciaSanitaria")) == null ? "Licencia Sanitaria:" : "Licencia Sanitaria: " + (String) unidadMedica.get("numeroLicenciaSanitaria"));
            }
            parametros.put("txtNombreMedico", (consulta.getNombreMedico() == null) ? "" : consulta.getNombreMedico());
            //            parametros.put("txtCedulaMedico", "N/A");
            ArrayList especialidad = (ArrayList) medico.get("especialidadViewList");
            if (especialidad != null && !especialidad.isEmpty()) {
               Map<String, Object> esp = mapp.convertValue(especialidad.get(0), Map.class);
               parametros.put("txtCedulaMedico", "CED. PROF. " + (String) esp.get("cedula"));
               // Inicio GGR20200803 datos medico agrego Universidad y Especialidad
               String especialidadTxt = (esp.get("especialidad") == null ? "" : (String) esp.get("especialidad"));
               String universidadTxt = (esp.get("universidad") == null ? "" : (String) esp.get("universidad"));
               parametros.put("txtEspecialidadMedico", especialidadTxt);
               parametros.put("txtUniversidadMedico", universidadTxt);
               // Fin GGR20200803 datos medico
            }
            parametros.put("datFechaImpre", new Date());

            // Inicio GGR20200803 datos domicilio medico y horario de atención
            agregaDatosMedico(medico, parametros);
            // Fin GGR20200803

         } catch (Exception ex) {
            parametros.put("txtNombreUM", "");
            parametros.put("txtDireccionUM", "");
            parametros.put("txtLicSanitariaUM", "");
            parametros.put("txtNombreMedico", "");
            parametros.put("txtCedulaMedico", "");
            parametros.put("datFechaImpre", new Date());
            parametros.put("txtEspecialidadMedico", "");
            parametros.put("txtUniversidadMedico", "");
            parametros.put("txtHorarioAtencion", "");
            parametros.put("txtDireccionMedico", "");

         }

         try {
            Map<String, Object> paciente = apiConfiguration.getPacieteByid(consulta.getIdPaciente().toString());

            parametros.put("txtSexo", ((String) paciente.get("sexo")) == null ? "" : (String) paciente.get("sexo"));
            Integer numExpe = ((Integer) paciente.get("numeroExpediente") == null) ? 0 : (Integer) paciente.get("numeroExpediente");
            parametros.put("txtExpediente", numExpe + "");
//            Integer numExpeHis = ((Integer) paciente.get("numeroExpediente") == null) ? 0 : (Integer) paciente.get("numeroExpediente");
//            parametros.put("txtExpedienteHis", "N/A");
            parametros.put("txtExpedienteHis", (consulta.getReferencia2() == null) ? "" : consulta.getReferencia2());
            parametros.put("txtCurp", ((String) paciente.get("curp")) == null ? "" : (String) paciente.get("curp"));

            try {
               Date fechaNac = new Date((Long) paciente.get("fechaNacimiento"));
               parametros.put("datFechaNac", fechaNac);

               Date today = new Date();
               Instant instant = Instant.ofEpochMilli(today.getTime());
               LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
               LocalDate localDate = localDateTime.toLocalDate();

               Instant instant2 = Instant.ofEpochMilli(fechaNac.getTime());
               LocalDateTime localDateTime2 = LocalDateTime.ofInstant(instant2, ZoneId.systemDefault());
               LocalDate localDate2 = localDateTime2.toLocalDate();

               Integer edad = Period.between(localDate2, localDate).getYears();
               parametros.put("txtEdad", edad + " años");

            } catch (Exception ex) {
               parametros.put("datFechaNac", null);
               parametros.put("txtEdad", 0 + " años");
            }

         } catch (Exception ex) {
            parametros.put("txtSexo", "");
            parametros.put("txtExpediente", "");
         }


         try {
            Map<String, Object> estudioList = apiConfiguration.getEstudioByFolio(folio);
            System.out.println(estudioList);
            if (estudioList != null && !estudioList.isEmpty()) {
               StringBuilder estudio = new StringBuilder();
               ArrayList detalle = (ArrayList) estudioList.get("detallesEstudioList");
               AtomicInteger index = new AtomicInteger(1);
               detalle.forEach((deta) -> {
                  Map<String, Object> tempo = mapp.convertValue(deta, Map.class);
//                  System.out.println(deta);
                  estudio.append(index.getAndIncrement() + ") " +
                     ((String) tempo.get("tipoEstudio") == null ? "" : (String) tempo.get("tipoEstudio")) +
                     ((String) tempo.get("descripcionEstudio") == null ? "" : ", " + (String) tempo.get("descripcionEstudio")) +
                     ((String) tempo.get("preparacion") == null ? "" : ", " + (String) tempo.get("preparacion")) + "\n\n");
               });
               parametros.put("txtPrescripcion", "" + estudio);
//               System.out.println(estudio);
            } else {
               parametros.put("txtPrescripcion", "");
            }
         } catch (Exception ex) {
            parametros.put("txtPrescripcion", "");
         }

         // GGR20200619 Inicia cambios para cambiar imagen por logo de grupo o default Imagen IMSS
         try {
            Map<String, Object> grupo = apiConfiguration.getGrupoById(idGroup);
            parametros.put("imagen", (String) grupo.get("imagen"));
         } catch (Exception e) {
            parametros.put("imagen", null);
         }

         InputStream jrxmlArchivo = getClass().getResourceAsStream("/reportes/SolicitudEstudios.jrxml");
         JasperReport jasperArchivo = JasperCompileManager.compileReport(jrxmlArchivo);
         byte[] byteReporte = JasperRunManager.runReportToPdf(jasperArchivo, parametros, new JREmptyDataSource());
         /*// Inicio pruebas GGR20200803 lo escribo en disco por que en mi entorno me da timeout
         FileOutputStream fop = new FileOutputStream(new File("C:\\Users\\Ggarcia\\Documents\\solicitud.pdf"));
         fop.write(byteReporte);
         fop.flush();
         fop.close();
         // Fin pruebas GGR20200803*/
         byte[] codificado = Base64.encodeBase64(byteReporte);
         String pdfFile = IOUtils.toString(codificado, "UTF-8");


         return pdfFile;
      } catch (ConsultaException consE) {
         throw consE;
      } catch (DataIntegrityViolationException dive) {
         ConsultaException consE = new ConsultaException("No fue posible editar Consulta", ConsultaException.LAYER_DAO, ConsultaException.ACTION_SELECT);
         consE.addError("No fue posible obtener detalles Consulta");
         log.error("Error al obtener detalles Consulta - CODE {} - {}", consE.getExceptionCode(), idConsulta, consE);
         throw consE;
      } catch (JRException jreE) {
         ConsultaException consE = new ConsultaException("No se pudo construir el reporte", ConsultaException.LAYER_DAO, ConsultaException.ACTION_SELECT);
//         log.error("Error al obtener detalles Consulta - CODE {} - {}", consE.getExceptionCode(), idConsulta, consE); //tira TODA la exception
         if (jreE.getMessage().contains("java.net.MalformedURLException")) {
            log.error("===>>>No existe el archivo jrxml");
            consE.addError("No existe el archivo jrxml");
         } else {
            log.error("===>>>{}", jreE.getMessage());
            consE.addError("" + jreE.getMessage());
         }
         throw consE;
      } catch (Exception ex) {
         ConsultaException consE = new ConsultaException("Error inesperado al obtener detalles Consulta", ConsultaException.LAYER_DAO, ConsultaException.ACTION_SELECT);
         consE.addError("Ocurrió un error al obtener detalles Consulta");
         log.error("Error al obtener detalles Consulta - CODE {} - {}", consE.getExceptionCode(), idConsulta, consE);
         log.error(ex.getMessage());
         throw consE;
      }
   }




   @Override
   public String getReceta(Long idConsulta, Long idGroup) throws ConsultaException {
      log.info("getReceta(): recibo idConsulta {} - idGroup: {}", idConsulta, idGroup);
      try {
         Consulta consulta = consultaRepository.findByIdConsulta(idConsulta);
         System.err.println(consulta);
         if (consulta == null) {
            ConsultaException consE = new ConsultaException("No se encuentra en el sistema Consulta.", ConsultaException.LAYER_DAO, ConsultaException.ACTION_VALIDATE);
            consE.addError("No existe la Consulta con el Id:" + idConsulta);
            throw consE;
         }

         String diagnosticos = "";
         Map<String, Object> parametros = new HashMap<>();
         parametros.put("datFechaCreacion", consulta.getFechaCrecion());
         parametros.put("txtNombre", (consulta.getNombrePaciente() == null) ? "" : consulta.getNombrePaciente());
         logger.info("Objecto consulta - {}", consulta);

         Set<Padecimiento> padecimientos = consulta.getPadecimiento();
         if (padecimientos != null && !padecimientos.isEmpty()) {
            // Obtenemos el primer padecimiento o uno específico según algún criterio
            Padecimiento padecimientoActual = padecimientos.iterator().next();
            String nombrePadecimiento = padecimientoActual.getNombrePadecimiento();
            if (nombrePadecimiento != null && !nombrePadecimiento.isEmpty()) {
               parametros.put("txtDiagnostico", nombrePadecimiento);
            } else {
               parametros.put("txtDiagnostico", "Diagnóstico no especificado");
            }
         } else {
            // Manejo de caso donde no hay padecimientos disponibles
            parametros.put("txtDiagnostico", "Diagnóstico no disponible");
         }

//         try {
//
//            List<Padecimiento> padecimientosList = padecimientoRepository.findAllByIdPaciente(consulta.getIdPaciente().toString());
//            if (!padecimientosList.isEmpty()) {
//               logger.info("Padecimientos para agregar - {}", padecimientosList);
//               for (Padecimiento padecimiento : padecimientosList) {
//                  diagnosticos += padecimiento.getNombrePadecimiento() + ", ";
//               }
//               if (diagnosticos.endsWith(", ")) {
//                  diagnosticos = diagnosticos.substring(0, diagnosticos.length() - 2);
//                  parametros.put("txtDiagnostico", diagnosticos);
//               }
//            } else {
////               ConsultaException consE = new ConsultaException("No se pudo construir el reporte, debes especificar un diagnostico primero.", ConsultaException.LAYER_DAO, ConsultaException.ACTION_VALIDATE);
////               consE.addError("No se pudo construir el reporte, debes especificar un diagnostico primero");
////               throw consE;
//               parametros.put("txtDiagnostico", diagnosticos);
//            }
//
//            /* if(padecimientosList.isEmpty())
//               diagnosticos = "No especificados";
//            else {
//               logger.info("Padecimientos para agregar - {}", padecimientosList);
//               for (Padecimiento padecimiento : padecimientosList)
//                  diagnosticos += padecimiento.getNombrePadecimiento() + ", ";
//            }
//            if (diagnosticos.endsWith(", ")) {
//               diagnosticos = diagnosticos.substring(0, diagnosticos.length() - 2);
//            }
//            //diagnosticos = diagnosticos.substring(0, diagnosticos.length() - ", ".length());
//            parametros.put("txtDiagnostico", diagnosticos);*/
//         } catch (Exception ex) {
////            ConsultaException consE = new ConsultaException("No se pudo construir el reporte, debes especificar un diagnostico primero.", ConsultaException.LAYER_DAO, ConsultaException.ACTION_VALIDATE);
////            consE.addError("No se pudo construir el reporte, debes especificar un diagnostico primero");
////            throw consE;
//            parametros.put("txtDiagnostico", diagnosticos);
//         }

         try {
            Map<String, Object> medico = apiConfiguration.getMedicoByid(consulta.getIdMedico().toString());
            logger.info("Objecto medico - {}", medico);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(consulta.getFechaCrecion());
            calendar.add(Calendar.DAY_OF_YEAR, 3);
            parametros.put("datFechaVigencia", calendar.getTime());
            parametros.put("datFechaConsulta", consulta.getFechaConsulta());
            parametros.put("txtNombreMedico", (consulta.getNombreMedico() == null) ? "No registrada" : consulta.getNombreMedico());
            parametros.put("txtRFC", (medico.get("curp") == null) ? "No registrada" : medico.get("curp"));
            parametros.put("txtDR", (medico.get("sexo") == null) ? "No registrada" :
                    (medico.get("sexo").toString().equalsIgnoreCase("hombre") ? "DR. " : "DRA. "));
            parametros.put("txtCorreoMedico", (medico.get("email") == null) ? "No registrada" : medico.get("email"));
            parametros.put("txtTelefonoMedico", (medico.get("telefonoFijo") == null) ? "No registrada" : medico.get("telefonoFijo"));


            ArrayList especialidad = (ArrayList) medico.get("especialidadViewList");
            if (especialidad != null && !especialidad.isEmpty()) {
               Map<String, Object> esp = mapp.convertValue(especialidad.get(0), Map.class);
               parametros.put("txtCedulaMedico", esp.get("cedula"));
               String especialidadTxt = (esp.get("especialidad") == null ? "" : (String) esp.get("especialidad"));
               String universidadTxt = (esp.get("universidad") == null ? "" : (String) esp.get("universidad"));
               parametros.put("txtEspecialidad", especialidadTxt);
               parametros.put("txtInstitucion", universidadTxt);
               parametros.put("txtFooter", "cct.telemedicina.lat");
            }

         } catch (Exception ex) {
            parametros.put("txtNombreUM", "");
            parametros.put("txtDireccionUM", "");
            parametros.put("txtLicSanitariaUM", "");
            parametros.put("txtNombreMedico", "");
            parametros.put("txtCedulaMedico", "");
            parametros.put("datFechaImpre", new Date());
            parametros.put("txtEspecialidad", "");
            parametros.put("txtUniversidad", "");
            parametros.put("txtDireccionMedico", "");
            parametros.put("txtFooter", "cct.telemedicina.lat");
         }

         try {
            Map<String, Object> paciente = apiConfiguration.getPacieteByid(consulta.getIdPaciente().toString());
            logger.info("Objecto paciente - {}", paciente);
            parametros.put("txtSexo", ((String) paciente.get("sexo")) == null ? "" : (String) paciente.get("sexo"));
            Integer numExpe = ((Integer) paciente.get("numeroExpediente") == null) ? 0 : (Integer) paciente.get("numeroExpediente");
            parametros.put("txtExpediente", numExpe + "");
            parametros.put("txtExpedienteHis", (consulta.getReferencia2() == null) ? "" : consulta.getReferencia2());
            parametros.put("txtCurp", ((String) paciente.get("curp")) == null ? "" : (String) paciente.get("curp"));

            try {
               Date fechaNac = new Date((Long) paciente.get("fechaNacimiento"));
               parametros.put("datFechaNac", fechaNac);

               Date today = new Date();
               Instant instant = Instant.ofEpochMilli(today.getTime());
               LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
               LocalDate localDate = localDateTime.toLocalDate();

               Instant instant2 = Instant.ofEpochMilli(fechaNac.getTime());
               LocalDateTime localDateTime2 = LocalDateTime.ofInstant(instant2, ZoneId.systemDefault());
               LocalDate localDate2 = localDateTime2.toLocalDate();

               Integer edad = Period.between(localDate2, localDate).getYears();
               parametros.put("txtEdad", edad + " años");

            } catch (Exception ex) {
               parametros.put("datFechaNac", null);
               parametros.put("txtEdad", 0 + " años");
            }

         } catch (Exception ex) {
            parametros.put("txtSexo", "");
            parametros.put("txtExpediente", "");
         }

         List<Medicamento> medicamentoList = new ArrayList<>();
         try {
            Map<String, Object> receta = apiConfiguration.getRecetaByidConsulta(consulta.getIdConsulta());
            logger.info("Objeto receta - {}", receta);
            parametros.put("txtFolio", (receta.get("numeroFolio") == null) ? "" : String.valueOf(receta.get("numeroFolio")));
            List<Map<String, Object>> tratamientosReporte = new ArrayList<>();
            try {
               Map<String, Object> itemPlan = new HashMap<>();
               itemPlan.put("txtPlan", (receta.get("cuidadosGenerales") == null) ? "" : receta.get("cuidadosGenerales"));
               tratamientosReporte.add(itemPlan);
            } catch (Exception ex) {
               Map<String, Object> itemPlan = new HashMap<>();
               itemPlan.put("txtPlan", "N/A");
               tratamientosReporte.add(itemPlan);
            }

            JRBeanCollectionDataSource itemsJRBeanTratamiento = new JRBeanCollectionDataSource(tratamientosReporte);
            parametros.put("CollectionBeanParamTratamiento", itemsJRBeanTratamiento);
            ArrayList detalleReceta = (ArrayList) receta.get("detalleRecetaViewList");
            if (detalleReceta != null && !detalleReceta.isEmpty()) {
               detalleReceta.forEach((deta) -> {
                  try {
                     LinkedHashMap tempo = (LinkedHashMap) deta;
                     StringBuilder detalles = new StringBuilder(tempo.get("presentacion") == null ? "-" : removeSpaces(tempo.get("presentacion").toString()));
                     ArrayList sustancias = (ArrayList) tempo.get("substancias");
                     if (sustancias != null) {
                        for (Object sustancia : sustancias) {
                           LinkedHashMap temp = (LinkedHashMap) sustancia;
                           detalles.append(temp.get("Description") == null ? "-" : removeSpaces(temp.get("Description").toString())).append(", ");
                        }
                     }
                     if (detalles.toString().endsWith(", ")) {
                        detalles = new StringBuilder(detalles.substring(0, detalles.length() - 2));
                     }
                     Medicamento item = new Medicamento();
                     item.setNombreComercial(tempo.get("denominacionGenerica") == null ? "-" : removeSpaces(tempo.get("denominacionGenerica").toString()));
                     item.setDetalles(detalles.toString());
                     item.setDosis(tempo.get("dosis") == null ? "-" : removeSpaces(tempo.get("dosis").toString()));
                     item.setUnidad(tempo.get("unidad") == null ? "-" : removeSpaces(tempo.get("unidad").toString()));
                     item.setVia(tempo.get("viaAdministracion") == null ? "-" : removeSpaces(tempo.get("viaAdministracion").toString()));
                     item.setFrecuencia(tempo.get("frecuencia") == null ? "-" : removeSpaces(tempo.get("frecuencia").toString()) + " HORAS");
                     item.setPeriodo(tempo.get("periodo") == null ? "-" : removeSpaces(tempo.get("periodo").toString()));
                     item.setRecomendaciones(tempo.get("indicacionesMedicas") == null ? "-" : removeSpaces(tempo.get("indicacionesMedicas").toString()));
                     medicamentoList.add(item);
                     logger.info("Objeto deta - {}", deta);
                     logger.info("Objeto medicamento - {}", item);
                  } catch (Exception ex) {
                     logger.error("Error - {}", ex.getMessage());
                  }

                  /*try {
                     LinkedHashMap tempo = (LinkedHashMap) deta;
                     String detalles = tempo.get("presentacion") == null ? "-" : removeSpaces(tempo.get("presentacion").toString());
                     ArrayList sustancias = (ArrayList) tempo.get("substancias");
                     if(sustancias != null) {
                        for (Object sustancia : sustancias) {
                           LinkedHashMap temp = (LinkedHashMap) sustancia;
                           detalles += temp.get("Description") == null ? "-" : removeSpaces(temp.get("Description").toString() + ", ");
                        }
                     }
                     detalles = detalles.substring(0, detalles.length() - ", ".length());
                     Medicamento item = new Medicamento();
                     item.setNombreComercial(tempo.get("denominacionGenerica") == null ? "-" : removeSpaces(tempo.get("denominacionGenerica").toString()));
                     item.setDetalles(detalles);
                     item.setDosis(tempo.get("dosis") == null ? "-" : removeSpaces(tempo.get("dosis").toString()));
                     item.setUnidad(tempo.get("unidad") == null ? "-" : removeSpaces(tempo.get("unidad").toString()));
                     item.setVia(tempo.get("viaAdministracion") == null ? "-" : removeSpaces(tempo.get("viaAdministracion").toString()));
                     item.setFrecuencia(tempo.get("frecuencia") == null ? "-" : removeSpaces(tempo.get("frecuencia").toString()) + " HORAS");
                     item.setPeriodo(tempo.get("periodo") == null ? "-" : removeSpaces(tempo.get("periodo").toString()));
                     item.setRecomendaciones(tempo.get("indicacionesMedicas") == null ? "-" : removeSpaces(tempo.get("indicacionesMedicas").toString()));
                     medicamentoList.add(item);
                     logger.info("Objeto deta - {}", deta);
                     logger.info("Objeto medicamento - {}", item);
                  } catch(Exception ex) {
                     logger.error("Error - {}", ex.getMessage());
                  }*/
               });
            } else {
               Medicamento item = new Medicamento();
               item.setNombreComercial("No encontrado");
               medicamentoList.add(item);
            }
         } catch (Exception ex) {
            Medicamento item = new Medicamento();
            item.setNombreComercial("Error al buscar medicamentos");
            medicamentoList.add(item);
         }

         try {
//            Map<String, Object> grupo = apiConfiguration.getGrupoById(idGroup);
            String img = apiConfiguration.getImgColor(idGroup, "Rnegro");
            parametros.put("txtImage", img);
            
            Map<String, Object> medico = apiConfiguration.getMedicoByid(consulta.getIdMedico().toString());
            log.info("Objeto de medico: {}", medico);

            if (medico.get("id_medico_firma") != null) {
               Map<String, Object> firma = apiConfiguration.getMedicoFirmaByIdFirma((String) medico.get("id_medico_firma"));
               log.info("Firma del medico {}", firma);

               parametros.put("txtImageFirma", firma.get("contenido"));
            }
         } catch (Exception e) {
            parametros.put("txtImageFirma", null);
         }

//         List<Map<String, Object>> tratamientosReporte = new ArrayList<>();
//         try {
//            Map<String, Object> itemPlan = new HashMap<>();
//            itemPlan.put("txtPlan", (receta.getPlanTerapeutico() == null) ? "" : consulta.getPlanTerapeutico());
//            tratamientosReporte.add(itemPlan);
//         } catch (Exception ex) {
//            Map<String, Object> itemPlan = new HashMap<>();
//            itemPlan.put("txtPlan", "N/A");
//            tratamientosReporte.add(itemPlan);
//         }
//
//         JRBeanCollectionDataSource itemsJRBeanTratamiento = new JRBeanCollectionDataSource(tratamientosReporte);
//         parametros.put("CollectionBeanParamTratamiento", itemsJRBeanTratamiento);

         JRBeanCollectionDataSource itemsJRBean = new JRBeanCollectionDataSource(medicamentoList);
         parametros.put("CollectionBeanParam", itemsJRBean);
         //InputStream jrxmlArchivo = getClass().getClassLoader().getResourceAsStream(reportesReceta);
         InputStream jrxmlArchivo = getClass().getResourceAsStream("/reportes/Receta.jrxml");
         //InputStream jrxmlArchivo = "/" + getClass().getResourceAsStream(reportesReceta);
         JasperReport jasperArchivo = JasperCompileManager.compileReport(jrxmlArchivo);
         byte[] byteReporte = JasperRunManager.runReportToPdf(jasperArchivo, parametros, new JREmptyDataSource());
         byte[] codificado = Base64.encodeBase64(byteReporte);
         String pdfFile = IOUtils.toString(codificado, "UTF-8");
         return pdfFile;
      } catch (ConsultaException consE) {
         throw consE;
      } catch (DataIntegrityViolationException dive) {
         ConsultaException consE = new ConsultaException("No fue posible editar Consulta", ConsultaException.LAYER_DAO, ConsultaException.ACTION_SELECT);
         consE.addError("No fue posible obtener detalles Consulta");
         log.error("Error al obtener detalles Consulta - CODE {} - {}", consE.getExceptionCode(), idConsulta, consE);
         throw consE;
      } catch (JRException jreE) {
         ConsultaException consE = new ConsultaException("No se pudo construir el reporte", ConsultaException.LAYER_DAO, ConsultaException.ACTION_SELECT);
//         log.error("Error al obtener detalles Consulta - CODE {} - {}", consE.getExceptionCode(), idConsulta, consE); //tira TODA la exception
         if (jreE.getMessage().contains("java.net.MalformedURLException")) {
            log.error("===>>>No existe el archivo jrxml");
            consE.addError("No existe el archivo jrxml");
         } else {
            log.error("===>>>{}", jreE.getMessage());
            consE.addError("" + jreE.getMessage());
         }
         jreE.printStackTrace();
         throw consE;
      } catch (Exception ex) {
         ConsultaException consE = new ConsultaException("Error inesperado al obtener detalles Consulta", ConsultaException.LAYER_DAO, ConsultaException.ACTION_SELECT);
         consE.addError("Ocurrió un error al obtener detalles Consulta");
         log.error("Error al obtener detalles Consulta - CODE {} - {}", consE.getExceptionCode(), idConsulta, consE);
         log.error(ex.getMessage());
         ex.printStackTrace();
         throw consE;
      }
   }

//   @Override
//   public String getReceta(Long idConsulta, Long idGroup) throws ConsultaException {
//      log.info("getReceta(): recibo idConsulta {} - idGroup: {}", idConsulta, idGroup);
//      try {
//         Consulta consulta = consultaRepository.findByIdConsulta(idConsulta);
//         if (consulta == null) {
//            ConsultaException consE = new ConsultaException("No se encuentra en el sistema Consulta.", ConsultaException.LAYER_DAO, ConsultaException.ACTION_VALIDATE);
//            consE.addError("No existe la Consulta con el Id:" + idConsulta);
//            throw consE;
//         }
//
//         Map<String, Object> parametros = new HashMap<>();
//         parametros.put("datFechaCreacion", consulta.getFechaCrecion());
//         parametros.put("txtNombre", (consulta.getNombrePaciente() == null) ? "" : consulta.getNombrePaciente());
//         logger.info("Objecto consulta - {}", consulta);
//
//         try {
//            String diagnosticos = "";
//            List<Padecimiento> padecimientosList = padecimientoRepository.findAllByIdPaciente(consulta.getIdPaciente().toString());
//            if(padecimientosList.isEmpty())
//               diagnosticos = "No especificados";
//            else {
//               logger.info("Padecimientos para agregar - {}", padecimientosList);
//               for (Padecimiento padecimiento : padecimientosList)
//                  diagnosticos += padecimiento.getNombrePadecimiento() + ", ";
//            }
//            diagnosticos = diagnosticos.substring(0, diagnosticos.length() - ", ".length());
//            parametros.put("txtDiagnostico", diagnosticos);
//         } catch (Exception ex) {
//            parametros.put("txtDiagnostico", "No especificados");
//         }
//
//         try {
//            Map<String, Object> medico = apiConfiguration.getMedicoByid(consulta.getIdMedico().toString());
//            logger.info("Objecto medico - {}", medico);
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTime(consulta.getFechaCrecion());
//            calendar.add(Calendar.DAY_OF_YEAR, 3);
//            parametros.put("datFechaVigencia", calendar.getTime());
//            parametros.put("datFechaConsulta", consulta.getFechaConsulta());
//            parametros.put("txtNombreMedico", (consulta.getNombreMedico() == null) ? "No registrada" : consulta.getNombreMedico());
//            parametros.put("txtRFC", (medico.get("curp") == null) ? "No registrada" : medico.get("curp"));
//            parametros.put("txtDR", (medico.get("sexo") == null) ? "No registrada" :
//                    (medico.get("sexo").toString().equalsIgnoreCase("hombre") ? "DR. " : "DRA. "));
//
//            ArrayList especialidad = (ArrayList) medico.get("especialidadViewList");
//            if (especialidad != null && !especialidad.isEmpty()) {
//               Map<String, Object> esp = mapp.convertValue(especialidad.get(0), Map.class);
//               parametros.put("txtCedulaMedico", esp.get("cedula"));
//               String especialidadTxt = (esp.get("especialidad") == null ? "" : (String) esp.get("especialidad"));
//               String universidadTxt = (esp.get("universidad") == null ? "" : (String) esp.get("universidad"));
//               parametros.put("txtEspecialidad", especialidadTxt);
//               parametros.put("txtInstitucion", universidadTxt);
//            }
//
//         } catch (Exception ex) {
//            parametros.put("txtNombreUM", "");
//            parametros.put("txtDireccionUM", "");
//            parametros.put("txtLicSanitariaUM", "");
//            parametros.put("txtNombreMedico", "");
//            parametros.put("txtCedulaMedico", "");
//            parametros.put("datFechaImpre", new Date());
//            parametros.put("txtEspecialidad", "");
//            parametros.put("txtUniversidad", "");
//            parametros.put("txtDireccionMedico", "");
//         }
//
//         try {
//            Map<String, Object> paciente = apiConfiguration.getPacieteByid(consulta.getIdPaciente().toString());
//            logger.info("Objecto paciente - {}", paciente);
//            parametros.put("txtSexo", ((String) paciente.get("sexo")) == null ? "" : (String) paciente.get("sexo"));
//            Integer numExpe = ((Integer) paciente.get("numeroExpediente") == null) ? 0 : (Integer) paciente.get("numeroExpediente");
//            parametros.put("txtExpediente", numExpe + "");
//            parametros.put("txtExpedienteHis", (consulta.getReferencia2() == null) ? "" : consulta.getReferencia2());
//            parametros.put("txtCurp", ((String) paciente.get("curp")) == null ? "" : (String) paciente.get("curp"));
//
//            try {
//               Date fechaNac = new Date((Long) paciente.get("fechaNacimiento"));
//               parametros.put("datFechaNac", fechaNac);
//
//               Date today = new Date();
//               Instant instant = Instant.ofEpochMilli(today.getTime());
//               LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
//               LocalDate localDate = localDateTime.toLocalDate();
//
//               Instant instant2 = Instant.ofEpochMilli(fechaNac.getTime());
//               LocalDateTime localDateTime2 = LocalDateTime.ofInstant(instant2, ZoneId.systemDefault());
//               LocalDate localDate2 = localDateTime2.toLocalDate();
//
//               Integer edad = Period.between(localDate2, localDate).getYears();
//               parametros.put("txtEdad", edad + " años");
//
//            } catch (Exception ex) {
//               parametros.put("datFechaNac", null);
//               parametros.put("txtEdad", 0 + " años");
//            }
//
//         } catch (Exception ex) {
//            parametros.put("txtSexo", "");
//            parametros.put("txtExpediente", "");
//         }
//
//         List<Medicamento> medicamentoList = new ArrayList<>();
//         try {
//            Map<String, Object> receta = apiConfiguration.getRecetaByidConsulta(consulta.getIdConsulta());
//            logger.info("Objeto receta - {}", receta);
//            parametros.put("txtFolio", (receta.get("numeroFolio") == null) ? "" : String.valueOf(receta.get("numeroFolio")));
//            ArrayList detalleReceta = (ArrayList) receta.get("detalleRecetaViewList");
//            if (detalleReceta != null && !detalleReceta.isEmpty()) {
//               detalleReceta.forEach((deta) -> {
//                  try {
//                     LinkedHashMap tempo = (LinkedHashMap) deta;
//                     String detalles = tempo.get("presentacion") == null ? "-" : removeSpaces(tempo.get("presentacion").toString());
//                     ArrayList sustancias = (ArrayList) tempo.get("substancias");
//                     if(sustancias != null) {
//                        for (Object sustancia : sustancias) {
//                           LinkedHashMap temp = (LinkedHashMap) sustancia;
//                           detalles += temp.get("Description") == null ? "-" : removeSpaces(temp.get("Description").toString() + ", ");
//                        }
//                     }
//                     detalles = detalles.substring(0, detalles.length() - ", ".length());
//                     Medicamento item = new Medicamento();
//                     item.setNombreComercial(tempo.get("denominacionGenerica") == null ? "-" : removeSpaces(tempo.get("denominacionGenerica").toString()));
//                     item.setDetalles(detalles);
//                     item.setDosis(tempo.get("dosis") == null ? "-" : removeSpaces(tempo.get("dosis").toString()));
//                     item.setUnidad(tempo.get("unidad") == null ? "-" : removeSpaces(tempo.get("unidad").toString()));
//                     item.setVia(tempo.get("viaAdministracion") == null ? "-" : removeSpaces(tempo.get("viaAdministracion").toString()));
//                     item.setFrecuencia(tempo.get("frecuencia") == null ? "-" : removeSpaces(tempo.get("frecuencia").toString()) + " HORAS");
//                     item.setPeriodo(tempo.get("periodo") == null ? "-" : removeSpaces(tempo.get("periodo").toString()));
//                     item.setRecomendaciones(tempo.get("indicacionesMedicas") == null ? "-" : removeSpaces(tempo.get("indicacionesMedicas").toString()));
//                     medicamentoList.add(item);
//                     logger.info("Objeto deta - {}", deta);
//                     logger.info("Objeto medicamento - {}", item);
//                  } catch(Exception ex) {
//                     logger.error("Error - {}", ex.getMessage());
//                  }
//               });
//            } else {
//               Medicamento item = new Medicamento();
//               item.setNombreComercial("No encontrado");
//               medicamentoList.add(item);
//            }
//         } catch (Exception ex) {
//            Medicamento item = new Medicamento();
//            item.setNombreComercial("Error al buscar medicamentos");
//            medicamentoList.add(item);
//         }
//
//         try {
//            Map<String, Object> grupo = apiConfiguration.getGrupoById(idGroup);
//            parametros.put("txtImage", String.valueOf(grupo.get("imagen")));
//         } catch (Exception e) {
//            parametros.put("imagen", null);
//         }
//
//         JRBeanCollectionDataSource itemsJRBean = new JRBeanCollectionDataSource(medicamentoList);
//         parametros.put("CollectionBeanParam", itemsJRBean);
//         InputStream jrxmlArchivo = getClass().getResourceAsStream(reportesReceta);
//         JasperReport jasperArchivo = JasperCompileManager.compileReport(jrxmlArchivo);
//         byte[] byteReporte = JasperRunManager.runReportToPdf(jasperArchivo, parametros, new JREmptyDataSource());
//         byte[] codificado = Base64.encodeBase64(byteReporte);
//         String pdfFile = IOUtils.toString(codificado, "UTF-8");
//         return pdfFile;
//      } catch (ConsultaException consE) {
//         throw consE;
//      } catch (DataIntegrityViolationException dive) {
//         ConsultaException consE = new ConsultaException("No fue posible editar Consulta", ConsultaException.LAYER_DAO, ConsultaException.ACTION_SELECT);
//         consE.addError("No fue posible obtener detalles Consulta");
//         log.error("Error al obtener detalles Consulta - CODE {} - {}", consE.getExceptionCode(), idConsulta, consE);
//         throw consE;
//      } catch (JRException jreE) {
//         ConsultaException consE = new ConsultaException("No se pudo construir el reporte", ConsultaException.LAYER_DAO, ConsultaException.ACTION_SELECT);
////         log.error("Error al obtener detalles Consulta - CODE {} - {}", consE.getExceptionCode(), idConsulta, consE); //tira TODA la exception
//         if (jreE.getMessage().contains("java.net.MalformedURLException")) {
//            log.error("===>>>No existe el archivo jrxml");
//            consE.addError("No existe el archivo jrxml");
//         } else {
//            log.error("===>>>{}", jreE.getMessage());
//            consE.addError("" + jreE.getMessage());
//         }
//         jreE.printStackTrace();
//         throw consE;
//      } catch (Exception ex) {
//         ConsultaException consE = new ConsultaException("Error inesperado al obtener detalles Consulta", ConsultaException.LAYER_DAO, ConsultaException.ACTION_SELECT);
//         consE.addError("Ocurrió un error al obtener detalles Consulta");
//         log.error("Error al obtener detalles Consulta - CODE {} - {}", consE.getExceptionCode(), idConsulta, consE);
//         log.error(ex.getMessage());
//         ex.printStackTrace();
//         throw consE;
//      }
//   }

   private String removeSpaces(String cad) {
      return cad.replace("\n", "").replace("\r", "");
   }

   @Override
   public String getContrarreferencia(Long idConsulta) throws ConsultaException {
      try {
         Consulta consulta = consultaRepository.findByIdConsulta(idConsulta);
         if (consulta == null) {
            ConsultaException consE = new ConsultaException("No se encuentra en el sistema Consulta.", ConsultaException.LAYER_DAO, ConsultaException.ACTION_VALIDATE);
            consE.addError("No existe la Consulta con el Id:" + idConsulta);
            throw consE;
         }

         Map<String, Object> parametros = new HashMap<>();

         parametros.put("txtTipoDocumento", "CONTRARREFERENCIA");
         parametros.put("txtFolio", "Folio: " + (consulta.getNumeroConsulta() == null ? "" : consulta.getNumeroConsulta()));
         parametros.put("datFechaCreacion", consulta.getFechaCrecion());
         parametros.put("txtNombre", (consulta.getNombrePaciente() == null) ? "" : consulta.getNombrePaciente());

         try {
            Map<String, Object> medico = apiConfiguration.getMedicoByid(consulta.getIdMedico().toString());
            Long idUnidadMedica = ((Integer) medico.get("idUnidadMedica")).longValue();
            Map<String, Object> unidadMedica = apiConfiguration.getUnidadMeicaByid(idUnidadMedica);
//            parametros.put("txtNombreUM", ((String) unidadMedica.get("nombreUnidad")) == null ? "" : (String) unidadMedica.get("nombreUnidad"));
            String nombreTipologia = ((String) unidadMedica.get("nombreTipologia")) == null ? "" : (String) unidadMedica.get("nombreTipologia");
            String nombreUnidad = ((String) unidadMedica.get("nombreUnidad")) == null ? "" : (String) unidadMedica.get("nombreUnidad");

            parametros.put("txtNombreUM", nombreTipologia + "-" + nombreUnidad);
//            String direccion = (String) unidadMedica.get("vialidad") + " " +
//               (String) unidadMedica.get("numeroExterior") + " " +
//               "C.P. " + (String) unidadMedica.get("codigoPostal") + " " +
//               (String) unidadMedica.get("nombreJurisdiccion") + ", " +
//               "CHIAPAS";
            String vialidad = ((String) unidadMedica.get("vialidad")) == null ? "" : (String) unidadMedica.get("vialidad");
            String numeroExterior = ((String) unidadMedica.get("numeroExterior")) == null ? "" : (String) unidadMedica.get("numeroExterior");
            String cp = ((String) unidadMedica.get("codigoPostal")) == null ? "" : (String) unidadMedica.get("codigoPostal");
            String nombreJurisdiccion = ((String) unidadMedica.get("nombreJurisdiccion")) == null ? "" : (String) unidadMedica.get("nombreJurisdiccion");

            String direccion = vialidad + " " + numeroExterior + " C.P. " + cp + " " + nombreJurisdiccion + ", CHIAPAS";
//               (String) unidadMedica.get("fkEntidad");
            parametros.put("txtDireccionUM", direccion);
            parametros.put("txtLicSanitariaUM", ((String) unidadMedica.get("numeroLicenciaSanitaria")) == null ? "Licencia Sanitaria:" : "Licencia Sanitaria: " + (String) unidadMedica.get("numeroLicenciaSanitaria"));
            parametros.put("txtNombreMedico", (consulta.getNombreMedico() == null) ? "" : consulta.getNombreMedico());
            ArrayList especialidad = (ArrayList) medico.get("especialidadViewList");
            if (especialidad != null && !especialidad.isEmpty()) {
               Map<String, Object> esp = mapp.convertValue(especialidad.get(0), Map.class);
               parametros.put("txtCedulaMedico", "CED. PROF. " + (String) esp.get("cedula"));
            }
            parametros.put("datFechaImpre", new Date());
         } catch (Exception ex) {
            parametros.put("txtNombreUM", "");
            parametros.put("txtDireccionUM", "");
            parametros.put("txtLicSanitariaUM", "");
            parametros.put("txtNombreMedico", "");
            parametros.put("txtCedulaMedico", "");
            parametros.put("datFechaImpre", new Date());
         }

         try {
//            Map<String, Object> medico = apiConfiguration.getMedicoByid(consulta.getIdMedicoSolicitante().toString());
            Map<String, Object> medico = apiConfiguration.getMedicoByid(consulta.getIdMedico().toString());
            Long idUnidadMedica = ((Integer) medico.get("idUnidadMedica")).longValue();
            Map<String, Object> unidadMedica = apiConfiguration.getUnidadMeicaByid(idUnidadMedica);
            parametros.put("txtUniMedica", ((String) unidadMedica.get("nombreUnidad")) == null ? "" : (String) unidadMedica.get("nombreUnidad"));
//            parametros.put("txtDelegacion", ((String) unidadMedica.get("nombreJurisdiccion")) == null ? "" : (String) unidadMedica.get("nombreJurisdiccion"));
            parametros.put("txtDelegacion", "");
            parametros.put("txtEspecialidad", ((String) consulta.getEspecialidad()) == null ? "" : (String) consulta.getEspecialidad());
//            parametros.put("datFechaIngreso", new Date());
            parametros.put("datFechaIngreso", consulta.getFechaCrecion());
            parametros.put("datFechaEgreso", consulta.getFechaConsultaFin());
            parametros.put("txtDiagIngreso", "");
//            parametros.put("txtDiagEgreso", "falta dato");
         } catch (Exception ex) {
            parametros.put("txtUniMedica", "");
            parametros.put("txtDelegacion", "");
            parametros.put("txtEspecialidad", "");
            parametros.put("datFechaIngreso", new Date());
            parametros.put("datFechaEgreso", new Date());
            parametros.put("txtDiagIngreso", "");
//            parametros.put("txtDiagEgreso", "falta dato");
         }

         Set<Padecimiento> diagnosticosEgresoSet = consulta.getPadecimiento();
         if (diagnosticosEgresoSet != null && !diagnosticosEgresoSet.isEmpty()) {
            String diagnosticos = " ";
            for (Padecimiento pade : diagnosticosEgresoSet) {
               diagnosticos += pade.getNombrePadecimiento() + ",  ";
            }
            diagnosticos = diagnosticos.substring(0, diagnosticos.length() - 3);
            parametros.put("txtDiagEgreso", diagnosticos);
         } else {
            parametros.put("txtDiagEgreso", "");
         }


         try {
            Map<String, Object> paciente = apiConfiguration.getPacieteByid(consulta.getIdPaciente().toString());

            parametros.put("txtSexo", ((String) paciente.get("sexo")) == null ? "" : (String) paciente.get("sexo"));
            Integer numExpe = ((Integer) paciente.get("numeroExpediente") == null) ? 0 : (Integer) paciente.get("numeroExpediente");
            parametros.put("txtExpediente", numExpe + "");
//            Integer numExpeHis = ((Integer) paciente.get("numeroExpediente") == null) ? 0 : (Integer) paciente.get("numeroExpediente");
//            parametros.put("txtExpedienteHis", "falta dato");
            parametros.put("txtExpedienteHis", (consulta.getReferencia2() == null) ? "" : consulta.getReferencia2());
            parametros.put("txtCurp", ((String) paciente.get("curp")) == null ? "" : (String) paciente.get("curp"));

            try {
               Date fechaNac = new Date((Long) paciente.get("fechaNacimiento"));
               parametros.put("datFechaNac", fechaNac);

               Date today = new Date();
               Instant instant = Instant.ofEpochMilli(today.getTime());
               LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
               LocalDate localDate = localDateTime.toLocalDate();

               Instant instant2 = Instant.ofEpochMilli(fechaNac.getTime());
               LocalDateTime localDateTime2 = LocalDateTime.ofInstant(instant2, ZoneId.systemDefault());
               LocalDate localDate2 = localDateTime2.toLocalDate();

               Integer edad = Period.between(localDate2, localDate).getYears();
               parametros.put("txtEdad", edad + " años");

            } catch (Exception ex) {
               parametros.put("datFechaNac", null);
               parametros.put("txtEdad", 0 + " años");
            }

         } catch (Exception ex) {
            parametros.put("txtSexo", "");
            parametros.put("txtExpediente", "");
         }

//         parametros.put("datFechaHora", consulta.getFechaConsulta());
         parametros.put("txtSubjetivo", (consulta.getSubjetivo() == null) ? "" : consulta.getSubjetivo());
         parametros.put("txtObjetivo", (consulta.getObjetivo() == null) ? "" : consulta.getObjetivo());

         try {
            Map<String, Object> signosVitales = mapp.convertValue(consulta.getSignosVitales(), Map.class);

            Integer temp = ((Integer) signosVitales.get("temperatura") == null) ? 0 : (Integer) signosVitales.get("temperatura");
            parametros.put("txtTemperatura", "Temp.: " + temp + " °C");
            Integer peso = ((Integer) signosVitales.get("_peso") == null) ? 0 : (Integer) signosVitales.get("_peso");
            parametros.put("txtPeso", "Peso: " + peso + " kg");
            Integer talla = ((Integer) signosVitales.get("_talla") == null) ? 0 : (Integer) signosVitales.get("_talla");
            parametros.put("txtTalla", "Talla: " + talla + " cm");
            Double imc = ((Double) signosVitales.get("_imc") == null) ? 0.0 : (Double) signosVitales.get("_imc");
            parametros.put("txtImc", "IMC: " + imc);

            Map<String, Object> tensionArterial = mapp.convertValue(consulta.getSignosVitales().get("_tensionArterial"), Map.class);
            Integer sistolica = ((Integer) tensionArterial.get("sistolica") == null) ? 0 : (Integer) tensionArterial.get("sistolica");
            Integer diastolica = ((Integer) tensionArterial.get("diastolica") == null) ? 0 : (Integer) tensionArterial.get("diastolica");
            parametros.put("txtPresArte", "P.A.: " + sistolica + " / " + diastolica);
            Integer frecCardiaca = ((Integer) signosVitales.get("frecCardiaca") == null) ? 0 : (Integer) signosVitales.get("frecCardiaca");
            parametros.put("txtFrecCardiaca", "F.C.: " + frecCardiaca + " x min.");
            Integer frecRespiratoria = ((Integer) signosVitales.get("frecRespiratoria") == null) ? 0 : (Integer) signosVitales.get("frecRespiratoria");
            parametros.put("txtFrecRespiratoria", "F.R.: " + frecRespiratoria + " x min.");
            parametros.put("txtSatOxigeno", "Saturación Oxígeno: " + "falta dato");
            parametros.put("txtEvn", "EVN: " + "falta dato");
         } catch (Exception ex) {
            log.error("===>>>Algo paso con los signos vitales: {}", ex);
         }

         parametros.put("txtAnalisis", (consulta.getAnalisis() == null) ? "" : consulta.getAnalisis());

//         Set<Padecimiento> diagnosticosSet = consulta.getPadecimiento();
//         if (diagnosticosSet != null && !diagnosticosSet.isEmpty()) {
//            String diagnosticos = " ";
//            for (Padecimiento pade : diagnosticosSet) {
//               diagnosticos += pade.getNombrePadecimiento() + "\n";
//            }
//            parametros.put("txtDiagnosticos", diagnosticos);
//         } else {
//            parametros.put("txtDiagnosticos", "");
//         }
         // GGR20200709 una sola funcion
         agregaPadecimientosConsulta(consulta, parametros);

         parametros.put("txtPlan", (consulta.getPlanTerapeutico() == null) ? "" : consulta.getPlanTerapeutico());

         Collection<Tratamiento> tratamientoList = consulta.getTratamientoList();
         if (tratamientoList != null && !tratamientoList.isEmpty()) {
            String tratamientos = "";
            for (Tratamiento trat : tratamientoList) {
               tratamientos += trat.getProNombre() + "\n";
            }
            parametros.put("txtTratamientos", tratamientos);
         } else {
            parametros.put("txtTratamientos", "");
         }

         parametros.put("txtResumen", (consulta.getResumen() == null) ? "" : consulta.getResumen());
         parametros.put("txtPronostico", (consulta.getPronostico() == null ? "" : consulta.getPronostico()));

         try {
            Map<String, Object> receta = apiConfiguration.getRecetaByidConsulta(consulta.getIdConsulta());
            ArrayList detalleReceta = (ArrayList) receta.get("detalleRecetaViewList");
            if (detalleReceta != null && !detalleReceta.isEmpty()) {
               StringBuilder detaReceta = new StringBuilder("");
               StringBuilder detalle = new StringBuilder("");
               detalleReceta.forEach((deta) -> {
                  LinkedHashMap tempo = (LinkedHashMap) deta;
                  detalle.append((String) tempo.get("denominacionDistintiva") + " - " +
                     (String) tempo.get("denominacionGenerica") + ", " +
                     (String) tempo.get("dosis") + ", " +
                     (String) tempo.get("unidad") + ", " +
                     (String) tempo.get("viaAdministracion") + ", " +
                     (String) tempo.get("frecuencia") + ", " +
                     (String) tempo.get("periodo") + "\n");
                  detaReceta.append(detalle);
                  detalle.delete(0, detalle.length());
               });
               parametros.put("txtReceta", "" + detaReceta);
            } else {
               parametros.put("txtReceta", "");
            }
         } catch (Exception ex) {
            parametros.put("txtReceta", "");
         }

//         parametros.put("bolAmerita", Boolean.TRUE);
//         parametros.put("txtTiempoProbable", "XX dias");
//         parametros.put("bolContinuarSi", Boolean.TRUE);
//         parametros.put("bolContinuarNo", Boolean.TRUE);
//         parametros.put("txtCuantoTiempo", "XX semanas");
//         parametros.put("bolRequiereSi", Boolean.TRUE);
//         parametros.put("bolRequiereNo", Boolean.TRUE);
//         parametros.put("txtDiasAmpara", "XX meses");
//         parametros.put("datUltimaIncapacidad", new Date());
//         parametros.put("bolEnfermedadGral", Boolean.TRUE);
//         parametros.put("bolRiesgoTrab", Boolean.TRUE);
//         parametros.put("bolEnlace", Boolean.TRUE);
//         parametros.put("bolPostNatal", Boolean.TRUE);

         try {
            Map<String, Object> incapacidad = mapp.convertValue(consulta.getIncapacidadTemporal(), Map.class);

            parametros.put("txtNumFolio", ((Integer) incapacidad.get("folio") == null) ? "" : "" + (Integer) incapacidad.get("folio"));
            parametros.put("txtDias", ((Integer) incapacidad.get("days") == null) ? "" : "" + (Integer) incapacidad.get("days"));
            try {
//               Date fechaInicio = new Date((String) incapacidad.get("startDate"));
               Date fechaInicio = new SimpleDateFormat("yyyy-MM-dd").parse((String) incapacidad.get("startDate"));
               parametros.put("txtFechaInicio", fechaInicio);
            } catch (Exception ex) {
               parametros.put("txtFechaInicio", null);
            }
            String tipo = (String) incapacidad.get("type");
//            por default
            parametros.put("txtInicial", Boolean.FALSE);
            parametros.put("txtSubsecuente", Boolean.FALSE);
            if (tipo.equalsIgnoreCase("inicial")) {
               parametros.put("txtInicial", Boolean.TRUE);
               parametros.put("txtSubsecuente", Boolean.FALSE);
            } else {
               parametros.put("txtInicial", Boolean.FALSE);
               parametros.put("txtSubsecuente", Boolean.TRUE);
            }
            parametros.put("txtRamoSeguro", ((String) incapacidad.get("insuranceType") == null) ? "" : (String) incapacidad.get("insuranceType"));
            String razon = (String) incapacidad.get("reason");
//            por default
            parametros.put("txtEnfermedadGral", Boolean.FALSE);
            parametros.put("txtRiesgoTrabajo", Boolean.FALSE);
            parametros.put("txtMaternidad", Boolean.FALSE);
            if (razon.equalsIgnoreCase("Enfermedad general")) {
               parametros.put("txtEnfermedadGral", Boolean.TRUE);
               parametros.put("txtRiesgoTrabajo", Boolean.FALSE);
               parametros.put("txtMaternidad", Boolean.FALSE);
            }
            if (razon.equalsIgnoreCase("Riesgo de Trabajo")) {
               parametros.put("txtEnfermedadGral", Boolean.FALSE);
               parametros.put("txtRiesgoTrabajo", Boolean.TRUE);
               parametros.put("txtMaternidad", Boolean.FALSE);
            }
            if (razon.equalsIgnoreCase("Maternidad")) {
               parametros.put("txtEnfermedadGral", Boolean.FALSE);
               parametros.put("txtRiesgoTrabajo", Boolean.FALSE);
               parametros.put("txtMaternidad", Boolean.TRUE);
            }
            parametros.put("txtDiasAcumulados", ((Integer) incapacidad.get("accumulatedDays") == null) ? "" : "---" + (Integer) incapacidad.get("accumulatedDays") + "---");

            try {
               Map<String, Object> medTratante = mapp.convertValue(consulta.getIncapacidadTemporal().get("attendingPhysician"), Map.class);
               parametros.put("txtMedicoTratante", ((String) medTratante.get("name") == null) ? "" : (String) medTratante.get("name"));
               parametros.put("txtMatriculaTratante", ((String) medTratante.get("license") == null) ? "" : (String) medTratante.get("license"));
            } catch (Exception ex) {
               parametros.put("txtMedicoTratante", "");
               parametros.put("txtMatriculaTratante", "");
            }

            try {
               Map<String, Object> medDirectivo = mapp.convertValue(consulta.getIncapacidadTemporal().get("authorizerPhysician"), Map.class);
               parametros.put("txtMedicoDirectivo", ((String) medDirectivo.get("name") == null) ? "" : (String) medDirectivo.get("name"));
               parametros.put("txtMatriculaDirectivo", ((String) medDirectivo.get("license") == null) ? "" : (String) medDirectivo.get("license"));
               parametros.put("txtCargo", "");
            } catch (Exception ex) {
               parametros.put("txtMedicoDirectivo", "");
               parametros.put("txtMatriculaDirectivo", "");
               parametros.put("txtCargo", "");
            }

         } catch (Exception ex) {
            parametros.put("txtNumFolio", "");
            parametros.put("txtDias", "");
            parametros.put("txtFechaInicio", null);
            parametros.put("txtInicial", Boolean.FALSE);
            parametros.put("txtSubsecuente", Boolean.FALSE);
            parametros.put("txtRamoSeguro", "");
            parametros.put("txtEnfermedadGral", Boolean.FALSE);
            parametros.put("txtRiesgoTrabajo", Boolean.FALSE);
            parametros.put("txtMaternidad", Boolean.FALSE);
            parametros.put("txtDiasAcumulados", "");
            parametros.put("txtMedicoTratante", "");
            parametros.put("txtMatriculaTratante", "");
            parametros.put("txtMedicoDirectivo", "");
            parametros.put("txtMatriculaDirectivo", "");
         }

         InputStream jrxmlArchivo = getClass().getResourceAsStream("/reportes/Contrarreferencia.jrxml");
         JasperReport jasperArchivo = JasperCompileManager.compileReport(jrxmlArchivo);
         byte[] byteReporte = JasperRunManager.runReportToPdf(jasperArchivo, parametros, new JREmptyDataSource());

         byte[] codificado = Base64.encodeBase64(byteReporte);
         String pdfFile = IOUtils.toString(codificado, "UTF-8");

         return pdfFile;
      } catch (ConsultaException consE) {
         throw consE;
      } catch (DataIntegrityViolationException dive) {
         ConsultaException consE = new ConsultaException("No fue posible editar Consulta", ConsultaException.LAYER_DAO, ConsultaException.ACTION_SELECT);
         consE.addError("No fue posible obtener detalles Consulta");
         log.error("Error al obtener detalles Consulta - CODE {} - {}", consE.getExceptionCode(), idConsulta, consE);
         throw consE;
      } catch (JRException jreE) {
         ConsultaException consE = new ConsultaException("No se pudo construir el reporte", ConsultaException.LAYER_DAO, ConsultaException.ACTION_SELECT);
//         log.error("Error al obtener detalles Consulta - CODE {} - {}", consE.getExceptionCode(), idConsulta, consE); //tira TODA la exception
         if (jreE.getMessage().contains("java.net.MalformedURLException")) {
            log.error("===>>>No existe el archivo jrxml");
            consE.addError("No existe el archivo jrxml");
         } else {
            log.error("===>>>{}", jreE.getMessage());
            consE.addError("" + jreE.getMessage());
         }
         throw consE;
      } catch (Exception ex) {
         ConsultaException consE = new ConsultaException("Error inesperado al obtener detalles Consulta", ConsultaException.LAYER_DAO, ConsultaException.ACTION_SELECT);
         consE.addError("Ocurrió un error al obtener detalles Consulta");
         log.error("Error al obtener detalles Consulta - CODE {} - {}", consE.getExceptionCode(), idConsulta, consE);
         log.error(ex.getMessage());
         throw consE;
      }
   }

   @Override
   public String getConsentimiento(Long idConsulta) throws ConsultaException {
      try {
         Consulta consulta = consultaRepository.findByIdConsulta(idConsulta);
         if (consulta == null) {
            ConsultaException consE = new ConsultaException("No se encuentra en el sistema Consulta.", ConsultaException.LAYER_DAO, ConsultaException.ACTION_VALIDATE);
            consE.addError("No existe la Consulta con el Id:" + idConsulta);
            throw consE;
         }

         Map<String, Object> parametros = new HashMap<>();

         parametros.put("txtTipoDocumento", "INFORMACIÓN Y CONSENTIMIENTO INFORMADO");
         parametros.put("txtFolio", "Folio: falta dato");
         parametros.put("datFechaCreacion", new Date());
//         parametros.put("datFechaCreacion", consulta.getFechaCrecion());

         InputStream jrxmlArchivo = getClass().getResourceAsStream("/reportes/ConsentimientoInformado.jrxml");
         JasperReport jasperArchivo = JasperCompileManager.compileReport(jrxmlArchivo);
         byte[] byteReporte = JasperRunManager.runReportToPdf(jasperArchivo, parametros, new JREmptyDataSource());

         byte[] codificado = Base64.encodeBase64(byteReporte);
         String pdfFile = IOUtils.toString(codificado, "UTF-8");

         return pdfFile;
      } catch (ConsultaException consE) {
         throw consE;
      } catch (DataIntegrityViolationException dive) {
         ConsultaException consE = new ConsultaException("No fue posible editar Consulta", ConsultaException.LAYER_DAO, ConsultaException.ACTION_SELECT);
         consE.addError("No fue posible obtener detalles Consulta");
         log.error("Error al obtener detalles Consulta - CODE {} - {}", consE.getExceptionCode(), idConsulta, consE);
         throw consE;
      } catch (JRException jreE) {
         ConsultaException consE = new ConsultaException("No se pudo construir el reporte", ConsultaException.LAYER_DAO, ConsultaException.ACTION_SELECT);
//         log.error("Error al obtener detalles Consulta - CODE {} - {}", consE.getExceptionCode(), idConsulta, consE); //tira TODA la exception
         if (jreE.getMessage().contains("java.net.MalformedURLException")) {
            log.error("===>>>No existe el archivo jrxml");
            consE.addError("No existe el archivo jrxml");
         } else {
            log.error("===>>>{}", jreE.getMessage());
            consE.addError("" + jreE.getMessage());
         }
         throw consE;
      } catch (Exception ex) {
         ConsultaException consE = new ConsultaException("Error inesperado al obtener detalles Consulta", ConsultaException.LAYER_DAO, ConsultaException.ACTION_SELECT);
         consE.addError("Ocurrió un error al obtener detalles Consulta");
         log.error("Error al obtener detalles Consulta - CODE {} - {}", consE.getExceptionCode(), idConsulta, consE);
         log.error(ex.getMessage());
         throw consE;
      }
   }

   @Override
   public String getNotasInterconsulta(Long idConsulta) throws ConsultaException {
      try {
         Consulta consulta = consultaRepository.findByIdConsulta(idConsulta);
         if (consulta == null) {
            ConsultaException consE = new ConsultaException("No se encuentra en el sistema Consulta.", ConsultaException.LAYER_DAO, ConsultaException.ACTION_VALIDATE);
            consE.addError("No existe la Consulta con el Id:" + idConsulta);
            throw consE;
         }

         Map<String, Object> parametros = new HashMap<>();

         parametros.put("txtTipoDocumento", "NOTA DE INTERCONSULTA");
         parametros.put("txtFolio", "Folio: " + ((consulta.getNumeroConsulta() == null) ? "" : consulta.getNumeroConsulta()));
         parametros.put("datFechaCreacion", consulta.getFechaCrecion());
         parametros.put("txtNombre", (consulta.getNombrePaciente() == null) ? "" : consulta.getNombrePaciente());

         try {
            Map<String, Object> medico = apiConfiguration.getMedicoByid(consulta.getIdMedico().toString());
            Long idUnidadMedica = ((Integer) medico.get("idUnidadMedica")).longValue();
            Map<String, Object> unidadMedica = apiConfiguration.getUnidadMeicaByid(idUnidadMedica);
//            parametros.put("txtNombreUM", ((String) unidadMedica.get("nombreUnidad")) == null ? "" : (String) unidadMedica.get("nombreUnidad"));
            String nombreTipologia = ((String) unidadMedica.get("nombreTipologia")) == null ? "" : (String) unidadMedica.get("nombreTipologia");
            String nombreUnidad = ((String) unidadMedica.get("nombreUnidad")) == null ? "" : (String) unidadMedica.get("nombreUnidad");

            parametros.put("txtNombreUM", nombreTipologia + "-" + nombreUnidad);
//            String direccion = (String) unidadMedica.get("vialidad") + " " +
//               (String) unidadMedica.get("numeroExterior") + " " +
//               "C.P. " + (String) unidadMedica.get("codigoPostal") + " " +
//               (String) unidadMedica.get("nombreJurisdiccion") + ", " +
//               "CHIAPAS";
            String vialidad = ((String) unidadMedica.get("vialidad")) == null ? "" : (String) unidadMedica.get("vialidad");
            String numeroExterior = ((String) unidadMedica.get("numeroExterior")) == null ? "" : (String) unidadMedica.get("numeroExterior");
            String cp = ((String) unidadMedica.get("codigoPostal")) == null ? "" : (String) unidadMedica.get("codigoPostal");
            String nombreJurisdiccion = ((String) unidadMedica.get("nombreJurisdiccion")) == null ? "" : (String) unidadMedica.get("nombreJurisdiccion");

            String direccion = vialidad + " " + numeroExterior + " C.P. " + cp + " " + nombreJurisdiccion + ", CHIAPAS";
//               (String) unidadMedica.get("fkEntidad");
            parametros.put("txtDireccionUM", direccion);
            parametros.put("txtLicSanitariaUM", ((String) unidadMedica.get("numeroLicenciaSanitaria")) == null ? "Licencia Sanitaria:" : "Licencia Sanitaria: " + (String) unidadMedica.get("numeroLicenciaSanitaria"));

            parametros.put("txtNombreMedico", (consulta.getNombreMedico() == null) ? "" : consulta.getNombreMedico());
            ArrayList especialidad = (ArrayList) medico.get("especialidadViewList");
            if (especialidad != null && !especialidad.isEmpty()) {
               Map<String, Object> esp = mapp.convertValue(especialidad.get(0), Map.class);
               parametros.put("txtCedulaMedico", "CED. PROF. " + (String) esp.get("cedula"));
            }
            parametros.put("datFechaImpre", new Date());

         } catch (Exception ex) {
            parametros.put("txtNombreUM", "");
            parametros.put("txtDireccionUM", "");
            parametros.put("txtLicSanitariaUM", "");
            parametros.put("txtNombreMedico", "");
            parametros.put("txtCedulaMedico", "");
            parametros.put("datFechaImpre", new Date());
         }

         try {
            Map<String, Object> paciente = apiConfiguration.getPacieteByid(consulta.getIdPaciente().toString());

            parametros.put("txtSexo", ((String) paciente.get("sexo")) == null ? "" : (String) paciente.get("sexo"));
            Integer numExpe = ((Integer) paciente.get("numeroExpediente") == null) ? 0 : (Integer) paciente.get("numeroExpediente");
            parametros.put("txtExpediente", numExpe + "");
//            Integer numExpeHis = ((Integer) paciente.get("numeroExpediente") == null) ? 0 : (Integer) paciente.get("numeroExpediente");
//            parametros.put("txtExpedienteHis", "N/A");
            parametros.put("txtExpedienteHis", (consulta.getReferencia2() == null) ? "" : consulta.getReferencia2());
            parametros.put("txtCurp", ((String) paciente.get("curp")) == null ? "" : (String) paciente.get("curp"));

            try {
               Date fechaNac = new Date((Long) paciente.get("fechaNacimiento"));
               parametros.put("datFechaNac", fechaNac);

               Date today = new Date();
               Instant instant = Instant.ofEpochMilli(today.getTime());
               LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
               LocalDate localDate = localDateTime.toLocalDate();

               Instant instant2 = Instant.ofEpochMilli(fechaNac.getTime());
               LocalDateTime localDateTime2 = LocalDateTime.ofInstant(instant2, ZoneId.systemDefault());
               LocalDate localDate2 = localDateTime2.toLocalDate();

               Integer edad = Period.between(localDate2, localDate).getYears();
               parametros.put("txtEdad", edad + " años");

            } catch (Exception ex) {
               parametros.put("datFechaNac", null);
               parametros.put("txtEdad", 0 + " años");
            }

         } catch (Exception ex) {
            parametros.put("txtSexo", "");
            parametros.put("txtExpediente", "");
         }

         parametros.put("datFechaHora", consulta.getFechaConsulta());
         parametros.put("txtSubjetivo", (consulta.getSubjetivo() == null) ? "" : consulta.getSubjetivo());
         parametros.put("txtObjetivo", (consulta.getObjetivo() == null) ? "" : consulta.getObjetivo());

         try {
            Map<String, Object> signosVitales = mapp.convertValue(consulta.getSignosVitales(), Map.class);

            Integer temp = ((Integer) signosVitales.get("temperatura") == null) ? 0 : (Integer) signosVitales.get("temperatura");
            parametros.put("txtTemperatura", "Temp.: " + temp + " °C");
            Integer peso = ((Integer) signosVitales.get("_peso") == null) ? 0 : (Integer) signosVitales.get("_peso");
            parametros.put("txtPeso", "Peso: " + peso + " kg");
            Integer talla = ((Integer) signosVitales.get("_talla") == null) ? 0 : (Integer) signosVitales.get("_talla");
            parametros.put("txtTalla", "Talla: " + talla + " cm");
            Double imc = ((Double) signosVitales.get("_imc") == null) ? 0.0 : (Double) signosVitales.get("_imc");
            parametros.put("txtImc", "IMC: " + imc);

            Map<String, Object> tensionArterial = mapp.convertValue(consulta.getSignosVitales().get("_tensionArterial"), Map.class);
            Integer sistolica = ((Integer) tensionArterial.get("sistolica") == null) ? 0 : (Integer) tensionArterial.get("sistolica");
            Integer diastolica = ((Integer) tensionArterial.get("diastolica") == null) ? 0 : (Integer) tensionArterial.get("diastolica");
            parametros.put("txtPresArte", "P.A.: " + sistolica + " / " + diastolica);
            Integer frecCardiaca = ((Integer) signosVitales.get("frecCardiaca") == null) ? 0 : (Integer) signosVitales.get("frecCardiaca");
            parametros.put("txtFrecCardiaca", "F.C.: " + frecCardiaca + " x min.");
            Integer frecRespiratoria = ((Integer) signosVitales.get("frecRespiratoria") == null) ? 0 : (Integer) signosVitales.get("frecRespiratoria");
            parametros.put("txtFrecRespiratoria", "F.R.: " + frecRespiratoria + " x min.");
            parametros.put("txtSatOxigeno", "Saturación Oxígeno: " + "N/A");
            parametros.put("txtEvn", "EVN: " + "N/A");
         } catch (Exception ex) {
            log.error("===>>>Algo fallo con los datos de signos vitales/tension arterial: {}", ex);
            parametros.put("txtTemperatura", "Temp.: 0 °C");
            parametros.put("txtPeso", "Peso: 0 kg");
            parametros.put("txtTalla", "Talla: 0 cm");
            parametros.put("txtImc", "IMC: ");
            parametros.put("txtPresArte", "P.A.: 0 / 0");
            parametros.put("txtFrecCardiaca", "F.C.: 0 x min.");
            parametros.put("txtFrecRespiratoria", "F.R.: 0 x min.");
            parametros.put("txtSatOxigeno", "Saturación Oxígeno: " + "N/A");
            parametros.put("txtEvn", "EVN: " + "N/A");
         }

         parametros.put("txtAnalisis", (consulta.getAnalisis() == null) ? "" : consulta.getAnalisis());

//         Set<Padecimiento> diagnosticosSet = consulta.getPadecimiento();
//         if (diagnosticosSet != null && !diagnosticosSet.isEmpty()) {
//            String diagnosticos = " ";
//            for (Padecimiento pade : diagnosticosSet) {
//               diagnosticos += pade.getNombrePadecimiento() + "\n";
//            }
//            parametros.put("txtDiagnosticos", diagnosticos);
//         } else {
//            parametros.put("txtDiagnosticos", "");
//         }
         // GGR20200709 una sola funcion
         agregaPadecimientosConsulta(consulta, parametros);

         parametros.put("txtPlan", (consulta.getPlanTerapeutico() == null) ? "" : consulta.getPlanTerapeutico());

         Collection<Tratamiento> tratamientoList = consulta.getTratamientoList();
         if (tratamientoList != null && !tratamientoList.isEmpty()) {
            String tratamientos = "";
            for (Tratamiento trat : tratamientoList) {
               tratamientos += trat.getProNombre() + "\n";
            }
            parametros.put("txtTratamientos", tratamientos);
         } else {
            parametros.put("txtTratamientos", "");
         }

         parametros.put("txtResumen", (consulta.getResumen() == null) ? "" : consulta.getResumen());
         parametros.put("txtPronostico", (consulta.getPronostico() == null ? "" : consulta.getPronostico()));


         try {
            Map<String, Object> receta = apiConfiguration.getRecetaByidConsulta(consulta.getIdConsulta());
            ArrayList detalleReceta = (ArrayList) receta.get("detalleRecetaViewList");
            if (detalleReceta != null && !detalleReceta.isEmpty()) {
               StringBuilder detaReceta = new StringBuilder("");
               StringBuilder detalle = new StringBuilder("");
               detalleReceta.forEach((deta) -> {
                  LinkedHashMap tempo = (LinkedHashMap) deta;
                  detalle.append((String) tempo.get("denominacionDistintiva") + " - " +
                     (String) tempo.get("denominacionGenerica") + ", " +
                     (String) tempo.get("dosis") + ", " +
                     (String) tempo.get("unidad") + ", " +
                     (String) tempo.get("viaAdministracion") + ", " +
                     (String) tempo.get("frecuencia") + ", " +
                     (String) tempo.get("periodo") + "\n");
                  detaReceta.append(detalle);
                  detalle.delete(0, detalle.length());
               });
               parametros.put("txtReceta", "" + detaReceta);
            } else {
               parametros.put("txtReceta", "");
            }
         } catch (Exception ex) {
            parametros.put("txtReceta", "");
         }

//         try {
//            List<Map<String, Object>> estudioList = apiConfiguration.getEstudioByIdConsulta(consulta.getIdConsulta());
////            System.out.println("===>>>" + estudioList);
//            if (estudioList != null || !estudioList.isEmpty()) {
//               StringBuilder estudioS = new StringBuilder();
//               StringBuilder estudio = new StringBuilder();
//               estudioList.forEach((estu) -> {
//                  LinkedHashMap tempo = (LinkedHashMap) estu;
//                  estudio.append((String) tempo.get("tipoEstudio") + ", " +
//                     (String) tempo.get("descripcionEstudio") + ", " +
//                     (String) tempo.get("preparacion") + "\n");
//                  estudioS.append(estudio);
//                  estudio.delete(0, estudio.length());
//               });
//               parametros.put("txtPrescripcion", "" + estudioS);
//            } else {
//               parametros.put("txtPrescripcion", "");
//            }
//         } catch (Exception ex) {
//            parametros.put("txtPrescripcion", "");
//         }

         try {
            List<Map<String, Object>> estudioList = apiConfiguration.getEstudioByIdConsulta(consulta.getIdConsulta());
            if (estudioList != null && !estudioList.isEmpty()) {
               StringBuilder estudioS = new StringBuilder();
               StringBuilder estudio = new StringBuilder();
               estudioList.forEach((estu) -> {
                  estudio.append("Folio: " + estu.get("folio") + "\n\t");
                  ArrayList detalle = (ArrayList) estu.get("detallesEstudioList");
                  AtomicInteger index = new AtomicInteger(1);
                  detalle.forEach((deta) -> {
                     Map<String, Object> tempo = mapp.convertValue(deta, Map.class);
                     System.out.println(deta);
//                     estudio.append(index.getAndIncrement() + ") " + (String) tempo.get("tipoEstudio") + ", " +
//                        (String) tempo.get("descripcionEstudio") + ", " +
//                        (String) tempo.get("preparacion") + "\n\n\t");
                     estudio.append(index.getAndIncrement() + ") " +
                        ((String) tempo.get("tipoEstudio") == null ? "" : (String) tempo.get("tipoEstudio")) +
                        ((String) tempo.get("descripcionEstudio") == null ? "" : ", " + (String) tempo.get("descripcionEstudio")) +
                        ((String) tempo.get("preparacion") == null ? "" : ", " + (String) tempo.get("preparacion")) + "\n\n\t");
                  });
                  estudioS.append(estudio + "\n");
                  estudio.delete(0, estudio.length());
               });
               parametros.put("txtPrescripcion", "" + estudioS);
            } else {
               parametros.put("txtPrescripcion", "");
            }
         } catch (Exception ex) {
            parametros.put("txtPrescripcion", "");
         }

         InputStream jrxmlArchivo = getClass().getResourceAsStream("/reportes/NotasInterconsulta.jrxml");
         JasperReport jasperArchivo = JasperCompileManager.compileReport(jrxmlArchivo);
         byte[] byteReporte = JasperRunManager.runReportToPdf(jasperArchivo, parametros, new JREmptyDataSource());

         byte[] codificado = Base64.encodeBase64(byteReporte);
         String pdfFile = IOUtils.toString(codificado, "UTF-8");

         return pdfFile;
      } catch (ConsultaException consE) {
         throw consE;
      } catch (DataIntegrityViolationException dive) {
         ConsultaException consE = new ConsultaException("No fue posible editar Consulta", ConsultaException.LAYER_DAO, ConsultaException.ACTION_SELECT);
         consE.addError("No fue posible obtener detalles Consulta");
         log.error("Error al obtener detalles Consulta - CODE {} - {}", consE.getExceptionCode(), idConsulta, consE);
         throw consE;
      } catch (JRException jreE) {
         ConsultaException consE = new ConsultaException("No se pudo construir el reporte", ConsultaException.LAYER_DAO, ConsultaException.ACTION_SELECT);
//         log.error("Error al obtener detalles Consulta - CODE {} - {}", consE.getExceptionCode(), idConsulta, consE); //tira TODA la exception
         if (jreE.getMessage().contains("java.net.MalformedURLException")) {
            log.error("===>>>No existe el archivo jrxml");
            consE.addError("No existe el archivo jrxml");
         } else {
            log.error("===>>>{}", jreE.getMessage());
            consE.addError("" + jreE.getMessage());
         }
         throw consE;
      } catch (Exception ex) {
         ConsultaException consE = new ConsultaException("Error inesperado al obtener detalles Consulta", ConsultaException.LAYER_DAO, ConsultaException.ACTION_SELECT);
         consE.addError("Ocurrió un error al obtener detalles Consulta");
         log.error("Error al obtener detalles Consulta - CODE {} - {}", consE.getExceptionCode(), idConsulta, consE);
         log.error(ex.getMessage());
         throw consE;
      }
   }

   @Override
   public String getHistoriaClinica(String idPaciente) throws ConsultaException {
      try {
         Map<String, Object> parametros = new HashMap<>();
         Integer idUnidadMedica = 0;

         //Bloque para obtener la informacion del paciente
         try {
            log.info("---------------------------------------------------------------------------------------------------------------------");
            Map<String, Object> paciente = apiConfiguration.getPacieteByid(idPaciente);
            StringBuilder nombre_completo = new StringBuilder();
            nombre_completo.append(paciente.get("nombre") == null ? "" : (String) paciente.get("nombre"));
            nombre_completo.append(" ");
            nombre_completo.append(paciente.get("apellidoPaterno") == null ? "" : (String) paciente.get("apellidoPaterno"));
            nombre_completo.append(" ");
            nombre_completo.append(paciente.get("apellidoMaterno") == null ? "" : (String) paciente.get("apellidoMaterno"));
            parametros.put("txtNombre", nombre_completo.toString());
            parametros.put("txtSexo", (paciente.get("sexo")) == null ? "" : (String) paciente.get("sexo"));
            parametros.put("txtExpediente", (paciente.get("numeroExpediente") == null) ? "0" : String.valueOf(paciente.get("numeroExpediente")));
            parametros.put("txtCurp", (paciente.get("curp")) == null ? "" : (String) paciente.get("curp"));
            parametros.put("txtEstadoCivil", (paciente.get("estadoCivil")) == null ? "" : (String) paciente.get("estadoCivil"));
            parametros.put("txtReligion", (paciente.get("religion")) == null ? "" : (String) paciente.get("religion"));
            log.info(" Nombre de paciente " + nombre_completo.toString());
            try {
               Date fechaNac = new Date((Long) paciente.get("fechaNacimiento"));
               parametros.put("datFechaNac", fechaNac);

               Date today = new Date();
               Instant instant = Instant.ofEpochMilli(today.getTime());
               LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
               LocalDate localDate = localDateTime.toLocalDate();

               Instant instant2 = Instant.ofEpochMilli(fechaNac.getTime());
               LocalDateTime localDateTime2 = LocalDateTime.ofInstant(instant2, ZoneId.systemDefault());
               LocalDate localDate2 = localDateTime2.toLocalDate();

               Integer edad = Period.between(localDate2, localDate).getYears();
               parametros.put("txtEdad", edad + " años");
            } catch (Exception ex) {
               parametros.put("datFechaNac", null);
               parametros.put("txtEdad", 0 + " años");
            }
            if(paciente.get("idUnidadMedica") != null)
               idUnidadMedica = (Integer) paciente.get("idUnidadMedica");
         } catch (Exception ex) {
            parametros.put("txtSexo", "");
            parametros.put("txtExpediente", "");
            parametros.put("txtNombre", "");
            parametros.put("txtSexo", "");
            parametros.put("txtExpediente", "");
            parametros.put("txtCurp", "");
            parametros.put("txtEstadoCivil", "");
            parametros.put("txtReligion", "");
         }

         //Bloque para obtener la unidad medica
         try {
            log.info("---------------------------------------------------------------------------------------------------------------------");
            if(idUnidadMedica == 0)
               log.info("Id de Unidad Médica no disponible para el paciente");
            else {
               Map<String, Object> unidadMedica = apiConfiguration.getUnidadMedicaByid(idUnidadMedica);
               log.info("Nombre Unidad: {}", unidadMedica.get("nombreUnidad"));
               log.info("Dirección UM: {}", unidadMedica.get("observacionesDireccion"));
               log.info("Licencia Sanitaria UM: {}", unidadMedica.get("numeroLicenciaSanitaria"));
               parametros.put("txtNombreUM", unidadMedica.get("nombreUnidad"));
               parametros.put("txtDireccionUM", unidadMedica.get("observacionesDireccion"));
               parametros.put("txtLicSanitariaUM", unidadMedica.get("numeroLicenciaSanitaria"));
            }
            log.info("---------------------------------------------------------------------------------------------------------------------");
         } catch (Exception ex) {
            log.error("Error al obtener unidad médica: ", ex);
            parametros.put("txtNombreUM", "");
            parametros.put("txtDireccionUM", "");
            parametros.put("txtLicSanitariaUM", "");
         }

         //Bloque para obtener la informacion de historia clinica
         try {
            //log.info("---------------------------------------------------------------------------------------------------------------------");
            log.info("getHistoriaClinica(): idPaciente: {}",idPaciente);
            Map<String, Object> historial = apiConfiguration.getHistoriaClinicaByIdPaciente(idPaciente);
            if(historial == null)
               throw new Exception("No se ha podido encontrar el historial clinico del paciente");
            /*historial.forEach((s, o) -> {
               log.info(s);
            });*/
            //log.info("------------------------------------------------");
            Map<String, Object> hcg = new ObjectMapper().readValue(String.valueOf(historial.get("hcg")), Map.class);
            if(hcg == null)
               throw new Exception("No se ha podido encontrar el objeto hcg del paciente");
            /*hcg.forEach((s, o) -> {
               log.info(s);
            });*/
            //log.info("------------------------------------------------");


            //Bloque para obtener la informacion de bloque Heredo Familiares - hfPadecimiento
            log.info("------------------------------------------------");
            try{
               StringBuilder hfPadecimiento = new StringBuilder();
               Map<String, Object> heredoFamiliares = (Map<String, Object>) hcg.get("heredoFamiliares");
               if(heredoFamiliares == null) {
                  log.error("No se ha podido encontrar el objeto her del paciente");
                  parametros.put("hfPadecimiento", " ----- ");
               } else {
                  ArrayList<Map<String, Object>> datosTabla = (ArrayList<Map<String, Object>>) heredoFamiliares.get("datosTabla");
                  if(datosTabla == null) {
                     log.error("No se ha podido encontrar los datos de tabla");
                     parametros.put("hfPadecimiento", " ----- ");
                  } else {
                     datosTabla.forEach(stringObjectMap -> {
                        hfPadecimiento.append("Padecimiento: " + stringObjectMap.get("enfermedad") + "\t");
                        hfPadecimiento.append( "¿Presenta enfermedad?: " + (((boolean) stringObjectMap.get("tieneEnfermedad")==false)?"No":"Si"));
                        hfPadecimiento.append("\nFamiliares: ");
                        hfPadecimiento.append("\n\n");
                     });
                     hfPadecimiento.append( "Comentarios: " + ((heredoFamiliares.get("comentarios")==null) ? "-": heredoFamiliares.get("comentarios")) + "\n\n");
                     parametros.put("hfPadecimiento", hfPadecimiento.toString());
                     datosTabla.clear();
                  }
                  heredoFamiliares.clear();
               }
            }catch(Exception ex) {
               log.error("Error en sección Heredo Familiares - hfPadecimientos - " + ex.toString());
               parametros.put("hfPadecimiento", "-----");
            }
            log.info("------------------------------------------------");

            //Bloque para obtener informacion de Personales No Patologicos - txtPersonalesNoPatologicos
            try {
               StringBuilder txtPersonalesNoPatologicos = new StringBuilder();
               Map<String, Object> personalesNoPatologicos = (Map<String, Object>) hcg.get("pantallaPersonalesNoPatologicos");
               if(personalesNoPatologicos == null) {
                  log.error("No se ha podido encontrar los datos patologicos");
                  parametros.put("txtPersonalesNoPatologicos", " ----- ");
               } else {
                  Map<String, Object> datosTabla = (Map<String, Object>) personalesNoPatologicos.get("personalesNoPatologicosVivienda");
                  if (datosTabla == null) {
                     log.error("No se ha podido encontrar los datos de tabla no patologicos vivienda");
                     txtPersonalesNoPatologicos.append("Datos no Patologicos de Vivienda no encontrados\n\n");
                  } else {
                     log.info("Servicios de vivienda");
                     txtPersonalesNoPatologicos.append("Vivienda\nServicios de Vivienda: \t");
                     txtPersonalesNoPatologicos.append(" Agua Potable: " + (datosTabla.get("aguaPotable")==null?"-": ((boolean)datosTabla.get("aguaPotable")?"Si":"No")) + "\t");
                     txtPersonalesNoPatologicos.append(" Electricidad: " + (datosTabla.get("electricidad")==null?"-": ((boolean)datosTabla.get("electricidad")?"Si":"No")) + "\t");
                     txtPersonalesNoPatologicos.append(" Recolección de basura: " + (datosTabla.get("recoleccionBasura")==null?"-": ((boolean)datosTabla.get("recoleccionBasura")?"Si":"No")) + "\t");
                     txtPersonalesNoPatologicos.append(" Alcantarillado: " + (datosTabla.get("alcantarillado")==null?"-": ((boolean)datosTabla.get("alcantarillado")?"Si":"No")) + "\n");

                     log.info("Piso de vivienda");
                     txtPersonalesNoPatologicos.append("\nPiso de Vivienda\n");
                     txtPersonalesNoPatologicos.append("Tierra: " + (datosTabla.get("pisoTierra")==null?"-": ((boolean)datosTabla.get("pisoTierra")?"Si":"No")));
                     txtPersonalesNoPatologicos.append("\tAzulejo: " + (datosTabla.get("pisoAzulejo")==null?"-": ((boolean)datosTabla.get("pisoAzulejo")?"Si":"No")));
                     txtPersonalesNoPatologicos.append("\tCemento: " + (datosTabla.get("pisoCemento")==null?"-": ((boolean)datosTabla.get("pisoCemento")?"Si":"No")));
                     txtPersonalesNoPatologicos.append("\tOtro: " + (datosTabla.get("pisoOtro")==null?"-": ((boolean)datosTabla.get("pisoOtro")?"Si":"No")));

                     log.info("Techo y paredes");
                     txtPersonalesNoPatologicos.append("\n\nTecho y Paredes\n");
                     txtPersonalesNoPatologicos.append("Ladrillo: " + (datosTabla.get("techoLadrillo")==null?"-": ((boolean)datosTabla.get("techoLadrillo")?"Si":"No")));
                     txtPersonalesNoPatologicos.append("\tEnjarradas: " + (datosTabla.get("techoEnjarradas")==null?"": ((boolean)datosTabla.get("techoEnjarradas")?"Si":"No")));
                     txtPersonalesNoPatologicos.append((datosTabla.get("techoOtro")==null?"": ((boolean)datosTabla.get("techoOtro")?" Otro ":"")));

                     log.info("Baños de casa");
                     txtPersonalesNoPatologicos.append("\nBaños de casa: ");
                     txtPersonalesNoPatologicos.append((datosTabla.get("sinBanio")==null?"": ((boolean)datosTabla.get("sinBanio")?"Sin baño":"")));
                     txtPersonalesNoPatologicos.append((datosTabla.get("banio1")==null?"": ((boolean)datosTabla.get("banio1")?"1 baño":"")));
                     txtPersonalesNoPatologicos.append((datosTabla.get("banio2")==null?"": ((boolean)datosTabla.get("banio2")?"2 baños":"")));
                     txtPersonalesNoPatologicos.append((datosTabla.get("banioMas")==null?"": ((boolean)datosTabla.get("banioMas")?"3 baños":"")));

                     log.info("Habitaciones");
                     txtPersonalesNoPatologicos.append("\tHabitaciones: ");
                     txtPersonalesNoPatologicos.append((datosTabla.get("habitaciones1")==null?"": ((boolean)datosTabla.get("habitaciones1")?"1":"")));
                     txtPersonalesNoPatologicos.append((datosTabla.get("habitaciones2")==null?"": ((boolean)datosTabla.get("habitaciones2")?"2":"")));
                     txtPersonalesNoPatologicos.append((datosTabla.get("habitacionesMas")==null?"": ((boolean)datosTabla.get("habitacionesMas")?"3 o más":"")));

                     log.info("Cocica alimentos");
                     txtPersonalesNoPatologicos.append("\nCocina alimentos con:    ");
                     txtPersonalesNoPatologicos.append("Gas: " + (datosTabla.get("cocinarGas")==null?"-": ((boolean)datosTabla.get("cocinarGas")?"Si":"No")));
                     txtPersonalesNoPatologicos.append("\tLeña: " + (datosTabla.get("cocinarLenia")==null?"-": ((boolean)datosTabla.get("cocinarLenia")?"Si":"No")));
                     txtPersonalesNoPatologicos.append("\tCarbón: " + (datosTabla.get("cocinarCarbon")==null?"-": ((boolean)datosTabla.get("cocinarCarbon")?"Si":"No")));

                     log.info("Productos toxicos");
                     txtPersonalesNoPatologicos.append("\n¿Resguarda productos tóxicos en casa? ");
                     txtPersonalesNoPatologicos.append((datosTabla.get("productosToxicosNo")==null?"": ((boolean)datosTabla.get("productosToxicosNo")?"No":"")));
                     txtPersonalesNoPatologicos.append((datosTabla.get("productosToxicosSi")==null?"": ((boolean)datosTabla.get("productosToxicosSi")?
                             "Si - " + (datosTabla.get("productosToxicosInput ")==null?"": (datosTabla.get("productosToxicosInput") == null ?"-----": datosTabla.get("productosToxicosInput")))
                             :"")));

                     log.info("Fauna nociva");
                     txtPersonalesNoPatologicos.append("\n¿Cuenta con fauna nociva? ");
                     if((boolean)datosTabla.get("faunaNocivaNo"))
                        txtPersonalesNoPatologicos.append("No");
                     else {
                        txtPersonalesNoPatologicos.append(" Si\t");
                        txtPersonalesNoPatologicos.append("Arañas: " + (datosTabla.get("faunaAranias")==null?"-": ((boolean)datosTabla.get("faunaAranias")?"Si":"No")));
                        txtPersonalesNoPatologicos.append("\tAlacranes: " + (datosTabla.get("faunaAlacranes")==null?"-": ((boolean)datosTabla.get("faunaAlacranes")?"Si":"No")));
                        txtPersonalesNoPatologicos.append("\tSerpientes: " + (datosTabla.get("faunaSerpiente")==null?"-": ((boolean)datosTabla.get("faunaSerpiente")?"Si":"No")));
                        txtPersonalesNoPatologicos.append("\tOtro: " + (datosTabla.get("faunaOtro")==null?"-": ((boolean)datosTabla.get("faunaOtro")?"Si":"No")));
                     }
                  }

                  datosTabla.clear();
                  datosTabla = (Map<String, Object>) personalesNoPatologicos.get("personalesNoPatologicosMascotas");
                  if (datosTabla == null) {
                     log.error("No se ha podido encontrar los datos de tabla no patologicos Mascotas");
                     txtPersonalesNoPatologicos.append("Datos no Patologicos de Vivienda no encontrados\n\n");
                  } else {
                     log.info("Mascotas");
                     txtPersonalesNoPatologicos.append("\n\nMascotas: ");
                     if((boolean) datosTabla.get("mascotasNo"))
                        txtPersonalesNoPatologicos.append("No");
                     else {
                        txtPersonalesNoPatologicos.append("Si");
                        txtPersonalesNoPatologicos.append("\tPerro: " + (datosTabla.get("perro")==null?"-": ((boolean)datosTabla.get("perro")?"Si":"No")));
                        txtPersonalesNoPatologicos.append("\tGato: " + (datosTabla.get("gato")==null?"-": ((boolean)datosTabla.get("gato")?"Si":"No")));
                        txtPersonalesNoPatologicos.append("\tAve: " + (datosTabla.get("ave")==null?"-": ((boolean)datosTabla.get("ave")?"Si":"No")));
                        txtPersonalesNoPatologicos.append("\tConejo: " + (datosTabla.get("conejo")==null?"-": ((boolean)datosTabla.get("conejo")?"Si":"No")));
                        txtPersonalesNoPatologicos.append("\tTortuga: " + (datosTabla.get("tortuga")==null?"-": ((boolean)datosTabla.get("tortuga")?"Si":"No")));
                        txtPersonalesNoPatologicos.append("\tReptil: " + (datosTabla.get("reptil")==null?"-": ((boolean)datosTabla.get("reptil")?"Si":"No")));
                        txtPersonalesNoPatologicos.append("\tRoedor: " + (datosTabla.get("roedor")==null?"-": ((boolean)datosTabla.get("roedor")?"Si":"No")));
                        txtPersonalesNoPatologicos.append("\tAnimal de corral: " + (datosTabla.get("animalCorral")==null?"-": ((boolean)datosTabla.get("animalCorral")?"Si":"No")));
                        txtPersonalesNoPatologicos.append("\tNivel de convivencia: " + (datosTabla.get("nivelConvivencia")==null?"-": datosTabla.get("nivelConvivencia")));
                        txtPersonalesNoPatologicos.append("\nComentarios: " + (datosTabla.get("comentarios")==null?"-":datosTabla.get("comentarios")));
                     }
                  }

                  datosTabla.clear();
                  datosTabla = (Map<String, Object>) personalesNoPatologicos.get("personalesNoPatologicosAcDeportiva");
                  if(datosTabla == null) {
                     log.error("No se ha podido encontrar los datos de tabla no patologicos Deportivo");
                     txtPersonalesNoPatologicos.append("Datos no Patologicos Deportivos no encontrados\n\n");
                  } else {
                     log.info("Deportivo");
                     txtPersonalesNoPatologicos.append("\n\nActividad física: ");
                     if((boolean) datosTabla.get("acDeportivaNo"))
                        txtPersonalesNoPatologicos.append("No");
                     else {
                        txtPersonalesNoPatologicos.append("Si");
                        txtPersonalesNoPatologicos.append("\nFrecuencia: " + (datosTabla.get("acDeportivaFrecuencia")==null?"-":datosTabla.get("acDeportivaFrecuencia")));
                        txtPersonalesNoPatologicos.append("\tTipo de actividad: " + (datosTabla.get("tipoActividadInput")==null?"-":datosTabla.get("tipoActividadInput")));
                     }
                  }

                  datosTabla.clear();
                  datosTabla = (Map<String, Object>) personalesNoPatologicos.get("personalesNoPatologicosHabAlimenticios");
                  if(datosTabla == null) {
                     log.error("No se ha podido encontrar los datos de tabla no patologicos habitos alimenticios");
                     txtPersonalesNoPatologicos.append("Datos no Patologicos Habitos Alimenticios no encontrados\n\n");
                  } else {
                     log.info("Habitos Alimenticios");
                     txtPersonalesNoPatologicos.append("\n\nHábitos Alimenticios\n");
                     txtPersonalesNoPatologicos.append("Calidad: " + (datosTabla.get("calidad")==null?"-":datosTabla.get("calidad")));
                     txtPersonalesNoPatologicos.append("\tCantidad: " + (datosTabla.get("cantidad")==null?"-":datosTabla.get("cantidad")));
                     txtPersonalesNoPatologicos.append("\nComentarios: " + (datosTabla.get("comentarios")==null?"-":datosTabla.get("comentarios")));
                  }

                  datosTabla.clear();
                  datosTabla = (Map<String, Object>) personalesNoPatologicos.get("personalesNoPatologicosHabHigienicos");
                  if(datosTabla == null) {
                     log.error("No se ha podido encontrar los datos de tabla no patologicos habitos higienicos");
                     txtPersonalesNoPatologicos.append("Datos no Patologicos Habitos Higienicos no encontrados\n\n");
                  } else {
                     log.info("Habitos Higienicos");
                     txtPersonalesNoPatologicos.append("\n\nHábitos Higiénicos\n");
                     txtPersonalesNoPatologicos.append("Baño: " + (datosTabla.get("banio")==null?"-":datosTabla.get("banio")));
                     txtPersonalesNoPatologicos.append("\tAseo bucal: " + (datosTabla.get("aseoBucal")==null?"-":datosTabla.get("aseoBucal")));
                  }

                  datosTabla.clear();
                  ArrayList<Map<String,Object>> tablaConsumos = (ArrayList<Map<String, Object>>) personalesNoPatologicos.get("PersonalesNoPatologicosTablaConsumo");
                  if(datosTabla == null) {
                     log.error("No se ha podido encontrar los datos de tabla no patologicos consumos");
                     txtPersonalesNoPatologicos.append("Datos no Patologicos Consumos no encontrados\n\n");
                  } else {
                     log.info("Tabla de Consumos");
                     txtPersonalesNoPatologicos.append("\n\nConsumos\n");
                     tablaConsumos.forEach(stringObjectMap -> {
                        txtPersonalesNoPatologicos.append((stringObjectMap.get("nombreDroga")==null?"-":stringObjectMap.get("nombreDroga")));
                        txtPersonalesNoPatologicos.append("\t¿Consume?: " + (stringObjectMap.get("droga")==null?"-": ((boolean)stringObjectMap.get("droga")?"Si":"No")));
                        if((boolean) stringObjectMap.get("droga")) {
                           txtPersonalesNoPatologicos.append("\tFrecuencia: " + (stringObjectMap.get("frecuencia")==null?"-":stringObjectMap.get("frecuencia")));
                           txtPersonalesNoPatologicos.append("\tCantidad: " + (stringObjectMap.get("inputCantidad")==null?"-":stringObjectMap.get("inputCantidad")));
                        }
                        txtPersonalesNoPatologicos.append("\n");
                     });
                  }

                  datosTabla.clear();
                  datosTabla = (Map<String, Object>) personalesNoPatologicos.get("personalesNoPatologicosHistoriaLaboral");
                  if(datosTabla == null) {
                     log.error("No se ha podido encontrar los datos de tabla no patologicos historial laboral");
                     txtPersonalesNoPatologicos.append("Datos no Patologicos Historial Laboral no encontrados\n\n");
                  } else {
                     log.info("Historial Laboral");
                     txtPersonalesNoPatologicos.append("\n\nHistorial Laboral\n");
                     txtPersonalesNoPatologicos.append("¿Actualmente Trabaja? " + ((datosTabla.get("trabajaSi")==null?"-": ((boolean)datosTabla.get("trabajaSi")?"Si":"No"))) );
                  }


                  parametros.put("txtPersonalesNoPatologicos", txtPersonalesNoPatologicos.toString());
                  datosTabla.clear();
                  personalesNoPatologicos.clear();
               }
            }catch (Exception ex) {
               log.error("Error en seccion Personales No Patologicos - " + ex.toString());
            }

            //Bloque para obtener informacion de Personales Patologicos - txtPersonalesPatologicos
            try {
               StringBuilder txtPersonalesPatologicos = new StringBuilder();
               Map<String, Object> personalesPatologicos = (Map<String, Object>) hcg.get("personalesPatologicos");
               if(personalesPatologicos == null) {
                  log.error("No se ha podido encontrar los datos patologicos");
                  parametros.put("txtPersonalesPatologicos", " ----- ");
               } else {
                  txtPersonalesPatologicos.append("Alergias: \n");
                  txtPersonalesPatologicos.append(  (personalesPatologicos.get("textAlergias") == null)? "-": personalesPatologicos.get("textAlergias")  );
                  txtPersonalesPatologicos.append("\nCirugias: \n");
                  txtPersonalesPatologicos.append(  (personalesPatologicos.get("textCirugias") == null)? "-": personalesPatologicos.get("textCirugias")  );
                  txtPersonalesPatologicos.append("\nTransfusiones: \n");
                  txtPersonalesPatologicos.append(  (personalesPatologicos.get("textTransfuciones") == null)? "-": personalesPatologicos.get("textTransfuciones")  );
                  txtPersonalesPatologicos.append("\nHospitalización: \n");
                  txtPersonalesPatologicos.append(  (personalesPatologicos.get("textHospitalizacion") == null)? "-": personalesPatologicos.get("textHospitalizacion")  );
                  txtPersonalesPatologicos.append("\n");
                  ArrayList<Map<String, Object>> datosTabla = (ArrayList<Map<String, Object>>) personalesPatologicos.get("datosTabla");
                  if(datosTabla == null) {
                     log.error("No se ha podido encontrar los datos de tabla");
                     parametros.put("txtPersonalesPatologicos", " ----- ");
                  } else {
                     datosTabla.forEach(stringObjectMap -> {
                        txtPersonalesPatologicos.append("Padecimiento: " + stringObjectMap.get("enfermedad") + "\t");
                        txtPersonalesPatologicos.append( "¿Presenta enfermedad?: " + (((boolean) stringObjectMap.get("tieneEnfermedad")==false)?"No":"Si"));
                        txtPersonalesPatologicos.append("\nFamiliares: ");
                        txtPersonalesPatologicos.append("\n\n");
                     });
                     txtPersonalesPatologicos.append( "Comentarios: " + ((personalesPatologicos.get("comentarios")==null) ? "-": personalesPatologicos.get("comentarios")) + "\n\n");
                     parametros.put("txtPersonalesPatologicos", txtPersonalesPatologicos.toString());
                     datosTabla.clear();
                     //log.info(txtPersonalesPatologicos.toString());
                  }
                  personalesPatologicos.clear();
               }
            }catch(Exception ex) {
               log.error("Error en sección Personales Patologicos - " + ex.toString());
               parametros.put("txtPersonalesPatologicos", "-----");
            }
            log.info("------------------------------------------------");

         }catch (Exception ex) {
            log.error(ex.toString());
         }
         log.info("------------------------------------------------");

         //Bloque para obtener la informacion de curso clinico
         try {
            log.info("----------------------------------------------");
            log.info("Solicitando datos de curso clinico");
            Map<String, Object> cursoClinico = apiConfiguration.getCursoClinico(idPaciente);
            ArrayList<Map<String, Object>> content = (ArrayList<Map<String, Object>>) cursoClinico.get("content");
            log.info("----------------------------------------------");

            StringBuilder cadena = new StringBuilder();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy");
            for(Map<String, Object> element : content) {
               String idPadecimiento = element.get("idPaciente")==null ? "": String.valueOf(element.get("idPadecimiento"));
               String nombrePadecimiento = element.get("nombrePadecimiento")== null ? "" : (String)element.get("nombrePadecimiento");
               log.info(" idPadecimiento: {} - nombrePadecimiento: {}, fechaCreacion: {}", idPadecimiento, nombrePadecimiento, element.get("fechaCreacion"));
               Date fechaCreacion = element.get("fechaCreacion")==null ? null: new Date((Long)element.get("fechaCreacion"));
               cadena.append(idPadecimiento + " - " + nombrePadecimiento);
               cadena.append("\nFecha de inicio: ");
               cadena.append(fechaCreacion==null? " - ": simpleDateFormat.format(fechaCreacion));
               cadena.append("\tFecha de Alta: ");
               cadena.append("\n");
               cadena.append("Status: ");
               if ((element.get("estatus") == null))
                  cadena.append(" - ");
               else
                  cadena.append((boolean) element.get("estatus") ? "Abierto" : "Cerrado");
               cadena.append("\n");
               cadena.append("Médico: ");
               if (element.get("creadoPor") == null )
                  cadena.append(" - ");
               else
                  cadena.append( (String)element.get("creadoPor"));
               cadena.append("\n");
               Map<String, Object> listaConsultas = apiConfiguration.getListaConsulta(idPaciente,idPadecimiento);
               ArrayList<Map<String, Object>> contentListaConsultas = (ArrayList<Map<String, Object>>) listaConsultas.get("content");
               int cont = 1;
               cadena.append("No.\tTipo\t\tFecha\t\t\tConsulta\tMédico\n");
               for (Map<String, Object> elem: contentListaConsultas) {
                  //log.info(elem.toString());
                  cadena.append(cont++ + "\t");
                  cadena.append(elem.get("estadoConsulta") + "\t\t");
                  Date fecha = elem.get("fechaConsulta") == null? null: new Date((Long)elem.get("fechaConsulta"));
                  cadena.append(simpleDateFormat.format(fecha) + "\t");
                  cadena.append(elem.get("idConsulta") + "\t\t");
                  cadena.append(elem.get("nombreMedico") + "\n");
               }
               cadena.append("\n\n");
            }
            log.info("----------------------------------------------");
            log.info("Consulta a curso clinico finalizada");
            parametros.remove("txtListaPadecimientos");
            parametros.put("txtListaPadecimientos", cadena.toString());
         } catch (Exception ex) {
            log.error("Error en seccion de Curso clinico - " + ex.toString());
            parametros.put("txtListaPadecimientos", "");
         }

         //InputStream jrxmlArchivo = getClass().getResourceAsStream("/reportes/HistoriaClinica.jrxml");
         InputStream jrxmlArchivo = getClass().getResourceAsStream(reporteHistoriaClinica);
         JasperReport jasperArchivo = JasperCompileManager.compileReport(jrxmlArchivo);
         byte[] byteReporte = JasperRunManager.runReportToPdf(jasperArchivo, parametros, new JREmptyDataSource());
         byte[] codificado = Base64.encodeBase64(byteReporte);
         String pdfFile = IOUtils.toString(codificado, "UTF-8");
         log.info("Consulta terminada correctamente");
         return pdfFile;
      }/* catch (ConsultaException consE) {
         throw consE;
      } */catch (DataIntegrityViolationException dive) {
         ConsultaException consE = new ConsultaException("No fue posible editar Consulta", ConsultaException.LAYER_DAO, ConsultaException.ACTION_SELECT);
         consE.addError("No fue posible obtener detalles Consulta");
         log.error("Error al obtener detalles Consulta - CODE {} - {}", consE.getExceptionCode(), idPaciente, consE);
         throw consE;
      } catch (JRException jreE) {
         ConsultaException consE = new ConsultaException("No se pudo construir el reporte", ConsultaException.LAYER_DAO, ConsultaException.ACTION_SELECT);
         if (jreE.getMessage().contains("java.net.MalformedURLException")) {
            log.error("===>>>No existe el archivo jrxml");
            consE.addError("No existe el archivo jrxml");
         } else {
            log.error("===>>>{}", jreE.getMessage());
            consE.addError("" + jreE.getMessage());
         }
         jreE.printStackTrace();
         throw consE;
      } catch (Exception ex) {
         ConsultaException consE = new ConsultaException("Error inesperado al obtener detalles Consulta", ConsultaException.LAYER_DAO, ConsultaException.ACTION_SELECT);
         consE.addError("Ocurrió un error al obtener detalles Consulta");
         log.error("Error al obtener detalles Consulta - CODE {} - {}", consE.getExceptionCode(), idPaciente, consE);
         log.error(ex.getMessage());
         ex.printStackTrace();
         throw consE;
      }
   }

   private String getTabla(){
      StringBuilder cad = new StringBuilder();
      cad.append("<columnHeader>\n" +
              "\t<band height=\"30\"  isSplitAllowed=\"true\" >\n" +
              "\t\t<staticText>\n" +
              "\t\t\t<reportElement\n" +
              "\t\t\t\tx=\"5\"\n" +
              "\t\t\t\ty=\"12\"\n" +
              "\t\t\t\twidth=\"94\"\n" +
              "\t\t\t\theight=\"18\"\n" +
              "\t\t\t\tkey=\"staticText-2\"/>\n" +
              "\t\t\t<box></box>\n" +
              "\t\t\t<textElement>\n" +
              "\t\t\t\t<font/>\n" +
              "\t\t\t</textElement>\n" +
              "\t\t<text>COLUMNA 1 DE PRUEBA</text>\n" +
              "\t\t</staticText>\n" +
              "\t\t<staticText>\n" +
              "\t\t\t<reportElement\n" +
              "\t\t\t\tx=\"112\"\n" +
              "\t\t\t\ty=\"12\"\n" +
              "\t\t\t\twidth=\"94\"\n" +
              "\t\t\t\theight=\"18\"\n" +
              "\t\t\t\tkey=\"staticText-3\"/>\n" +
              "\t\t\t<box></box>\n" +
              "\t\t\t<textElement>\n" +
              "\t\t\t\t<font/>\n" +
              "\t\t\t</textElement>\n" +
              "\t\t<text>COLUMNA 2 DE PRUEBA</text>\n" +
              "\t\t</staticText>");
      cad.append("</band>\n" +
              "</columnHeader>");
      cad.append("<detail>\n" +
              "\t<band height=\"30\"  isSplitAllowed=\"true\" >\n" +
              "\t\t<textField isStretchWithOverflow=\"false\" isBlankWhenNull=\"false\" evaluationTime=\"Now\" hyperlinkType=\"None\"  hyperlinkTarget=\"Self\" >\n" +
              "\t\t\t<reportElement\n" +
              "\t\t\t\tx=\"5\"\n" +
              "\t\t\t\ty=\"6\"\n" +
              "\t\t\t\twidth=\"94\"\n" +
              "\t\t\t\theight=\"18\"\n" +
              "\t\t\t\tkey=\"textField\"/>\n" +
              "\t\t\t<box></box>\n" +
              "\t\t\t<textElement>\n" +
              "\t\t\t\t<font/>\n" +
              "\t\t\t</textElement>\n" +
              "\t\t<textFieldExpression   class=\"java.lang.String\">NOMBRE DE PRUEBA PARA LA COLUMNA 1></textFieldExpression>\n" +
              "\t\t</textField>\n" +
              "\t\t<textField isStretchWithOverflow=\"false\" isBlankWhenNull=\"false\" evaluationTime=\"Now\" hyperlinkType=\"None\"  hyperlinkTarget=\"Self\" >\n" +
              "\t\t\t<reportElement\n" +
              "\t\t\t\tx=\"112\"\n" +
              "\t\t\t\ty=\"6\"\n" +
              "\t\t\t\twidth=\"94\"\n" +
              "\t\t\t\theight=\"18\"\n" +
              "\t\t\t\tkey=\"textField\"/>\n" +
              "\t\t\t<box></box>\n" +
              "\t\t\t<textElement>\n" +
              "\t\t\t\t<font/>\n" +
              "\t\t\t</textElement>\n" +
              "\t\t<textFieldExpression   class=\"java.lang.String\">APELLIDO DE PRUEBA PARA LA COLUMNA 2</textFieldExpression>\n" +
              "\t\t</textField>");
      cad.append("</band>\n" +
              "</detail>");
      return cad.toString();
   }

   // GGR202000709 Agrego función para buscar y agregar los padecimientos (Diagnosticos) registrados en una consulta
   private void agregaPadecimientosConsulta(Consulta consulta, Map<String, Object> parametros) {
      Set<Padecimiento> diagnosticosSet = consulta.getPadecimiento();
      if (diagnosticosSet != null && !diagnosticosSet.isEmpty()) {
         StringBuilder diagnosticos = new StringBuilder();
         for (Padecimiento pade : diagnosticosSet) {
            diagnosticos.append((String) pade.getNombrePadecimiento() + "\n");
         }
         parametros.put("txtDiagnosticos", diagnosticos.toString());
      } else {
         parametros.put("txtDiagnosticos", "");
      }
   }

   //GGR20200709 Agrego función para buscar y agregar los datos del domicilio del médico
   private void agregaDatosMedico(Map<String, Object> medico, Map<String, Object> parametros) {
      ArrayList domicilio = (ArrayList) medico.get("domicilioViewList");
      if (domicilio != null && !domicilio.isEmpty()) {
         Map<String, Object> dom = mapp.convertValue(domicilio.get(0), Map.class);
         String calleMed = (dom.get("domicilio") == null ? "" : (String) dom.get("domicilio"));
         String localidadMed = (dom.get("localidad") == null ? "" : (String) dom.get("localidad"));
         String cpMed = (dom.get("cp") == null ? "" : (String) dom.get("cp"));
         String municipioMed = (dom.get("municipio") == null ? "" : (String) dom.get("municipio"));
         String estadoMed = (dom.get("estado") == null ? "" : (String) dom.get("estado"));
         String paisMed = (dom.get("pais") == null ? "" : (String) dom.get("pais"));
         String direccionMedico = calleMed + ", " + localidadMed + ", " + cpMed + ", " + municipioMed + ", " + estadoMed + ", " + paisMed;
         parametros.put("txtDireccionMedico", direccionMedico);
         String horarioAtencion = (dom.get("horarioAtencion") == null ? "" : (String) dom.get("horarioAtencion"));
         parametros.put("txtHorarioAtencion", horarioAtencion);
      }

   }


   /**
    * GGR20200807 Función que transforma un SringBuilder a un array de bytes
    * @param sb StringBuilder con contenido en xml
    * @return array con los bytes si Ok o Null si hubo error
    */
   private byte[] getRenglonesXml(StringBuilder sb) {
      /*StringBuilder sb = new StringBuilder("");
      sb.append("<medicamentos><medicamento><partida>1</partida><nombre>Paracetamol</nombre><dosis>500</dosis><unidad>gramos</unidad><via>oral</via><frecuencia>12 horas</frecuencia><periodo>1 semana</periodo></medicamento><medicamento><partida>1</partida><nombre>Ibuprofeno</nombre><dosis>250</dosis><unidad>gramos</unidad><via>oral</via><frecuencia>6 horas</frecuencia><periodo>1 semana</periodo></medicamento></medicamentos>");
      */
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      try {
         String partidas = sb.toString();
         baos.write(partidas.getBytes("UTF-8"));
         baos.flush();
         byte[] bytesFile = baos.toByteArray();
         return bytesFile;
      } catch (Exception e) {
         e.printStackTrace();
      }
      return null;
   }

}
