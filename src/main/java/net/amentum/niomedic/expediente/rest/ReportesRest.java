package net.amentum.niomedic.expediente.rest;

import net.amentum.common.BaseController;
import net.amentum.niomedic.expediente.exception.ConsultaException;
import net.amentum.niomedic.expediente.service.ReportesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("reportes")
public class ReportesRest extends BaseController {
   private ReportesService reportesService;

   @Autowired
   public void setReportesService(ReportesService reportesService) {
      this.reportesService = reportesService;
   }

   @RequestMapping(value = "solicitud-servicios/{idConsulta}", method = RequestMethod.GET)
   @ResponseStatus(HttpStatus.OK)
   public String getSolicitudServicios(@PathVariable("idConsulta") Long idConsulta) throws ConsultaException {
      return reportesService.getSolicitudServicios(idConsulta);
   }

   @RequestMapping(value = "referencia/{idConsulta}", method = RequestMethod.GET)
   @ResponseStatus(HttpStatus.OK)
   public String getReferencia(@PathVariable("idConsulta") Long idConsulta) throws ConsultaException {
      return reportesService.getReferencia(idConsulta);
   }

   // GGR20200622 Agrego idGroup
   @RequestMapping(value = "notas-medicas/{idConsulta}/{idGroup}", method = RequestMethod.GET)
   @ResponseStatus(HttpStatus.OK)
   public String getNotasMedicasPresc(@PathVariable("idConsulta") Long idConsulta, @PathVariable("idGroup") Long idGroup) throws ConsultaException {
      return reportesService.getNotasMedicasPresc(idConsulta, idGroup);
   }

   @RequestMapping(value = "notas-evolucion/{idConsulta}", method = RequestMethod.GET)
   @ResponseStatus(HttpStatus.OK)
   public String getNotasEvolucion(@PathVariable("idConsulta") Long idConsulta) throws ConsultaException {
      return reportesService.getNotasEvolucion(idConsulta);
   }

   //   @RequestMapping(value = "solicitud-estudios/{idConsulta}", method = RequestMethod.GET)
//   @ResponseStatus(HttpStatus.OK)
//   public String getSolicitudEstudios(@PathVariable("idConsulta") Long idConsulta) throws ConsultaException {
//      return reportesService.getSolicitudEstudios(idConsulta);
//   }
   // GGR20200803 Agrego idGroup
   @RequestMapping(value = "solicitud-estudios/{idConsulta}/{folio}/{idGroup}", method = RequestMethod.GET)
   @ResponseStatus(HttpStatus.OK)
   public String getSolicitudEstudios(@PathVariable("idConsulta") Long idConsulta,@PathVariable("folio") Long folio, @PathVariable("idGroup") Long idGroup ) throws ConsultaException {
      return reportesService.getSolicitudEstudios(idConsulta,folio, idGroup);
   }

   // GGR20200622 Agrego idGroup
   @RequestMapping(value = "receta/{idConsulta}/{idGroup}", method = RequestMethod.GET)
   @ResponseStatus(HttpStatus.OK)
   public String getReceta(@PathVariable("idConsulta") Long idConsulta, @PathVariable("idGroup") Long idGroup) throws ConsultaException {
      return reportesService.getReceta(idConsulta, idGroup);
   }

   @RequestMapping(value = "contrarreferencia/{idConsulta}", method = RequestMethod.GET)
   @ResponseStatus(HttpStatus.OK)
   public String getContrarreferencia(@PathVariable("idConsulta") Long idConsulta) throws ConsultaException {
      return reportesService.getContrarreferencia(idConsulta);
   }

   @RequestMapping(value = "consentimiento-informado/{idConsulta}", method = RequestMethod.GET)
   @ResponseStatus(HttpStatus.OK)
   public String getConsentimiento(@PathVariable("idConsulta") Long idConsulta) throws ConsultaException {
      return reportesService.getConsentimiento(idConsulta);
   }

   @RequestMapping(value = "notas-interconsulta/{idConsulta}", method = RequestMethod.GET)
   @ResponseStatus(HttpStatus.OK)
   public String getNotasInterconsulta(@PathVariable("idConsulta") Long idConsulta) throws ConsultaException {
      return reportesService.getNotasInterconsulta(idConsulta);
   }

   //Reporte Historial Clínico
   @RequestMapping(value = "historia-clinica/{idPaciente}/{idGroup}", method = RequestMethod.GET)
   @ResponseStatus(HttpStatus.OK)
   public String getHistoriaClinica(@PathVariable("idPaciente") String idPaciente, @PathVariable("idGroup") Long idGroup) throws ConsultaException {
      return reportesService.getHistoriaClinica(idPaciente, idGroup);
   }
}
