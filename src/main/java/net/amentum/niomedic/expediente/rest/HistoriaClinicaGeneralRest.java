package net.amentum.niomedic.expediente.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.amentum.niomedic.expediente.exception.HistoriaClinicaGeneralException;
import net.amentum.niomedic.expediente.service.HistoriaClinicaGeneralService;
import net.amentum.niomedic.expediente.views.HistoriaClinicaGeneralView;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("historia-clinica")
public class HistoriaClinicaGeneralRest {
   private final Logger logger = LoggerFactory.getLogger(HistoriaClinicaGeneralRest.class);
   private HistoriaClinicaGeneralService historiaClinicaGeneralService;

   @Autowired
   private ObjectMapper objectMapper;

   @Autowired
   public void setHistoriaClinicaGeneralService(HistoriaClinicaGeneralService historiaClinicaGeneralService) {
      this.historiaClinicaGeneralService = historiaClinicaGeneralService;
   }

   @RequestMapping(method = RequestMethod.POST)
   @ResponseStatus(HttpStatus.CREATED)
   public void createHistoriaClinicaGeneral(@RequestBody @Valid String json) throws HistoriaClinicaGeneralException {
      try {
         JSONObject jsonObj = new JSONObject(json);
         HistoriaClinicaGeneralView hcgV = new HistoriaClinicaGeneralView();
         if (jsonObj.getString("idPaciente").isEmpty() || jsonObj.getString("idPaciente").equals("")) {
            logger.error("===>>>No debe venir vacio el idPaciente");
            HistoriaClinicaGeneralException hcge = new HistoriaClinicaGeneralException("No debe venir vacio el idPaciente", HistoriaClinicaGeneralException.LAYER_REST, HistoriaClinicaGeneralException.ACTION_INSERT);
            hcge.addError("No debe venir vacio el idPaciente");
            throw hcge;
         }
         UUID uid = UUID.fromString(jsonObj.getString("idPaciente"));
         hcgV.setIdPaciente(uid);
         Long fechaCreacion = jsonObj.getLong("fechaCreacion");
         Long vigencia = jsonObj.getLong("vigencia");
         Long ultimaModificacion = jsonObj.getLong("ultimaModificacion");
         hcgV.setFechaCreacion(new Date(fechaCreacion));
         hcgV.setVersion(jsonObj.getFloat("version"));
         hcgV.setVigencia(new Date(vigencia));
         hcgV.setUltimaModificacion(new Date(ultimaModificacion));
         hcgV.setActivo(jsonObj.getBoolean("activo"));
         hcgV.setCreadoPor(jsonObj.getString("creadoPor"));
         String temporal = jsonObj.getJSONObject("hcg").toString();
         hcgV.setHcg(temporal);
         logger.info("Guardar nuevo Historia Clinica General: {}", hcgV);
         historiaClinicaGeneralService.createHistoriaClinicaGeneral(hcgV);
      } catch (HistoriaClinicaGeneralException hcge) {
         throw hcge;
      } catch (Exception ex) {
         HistoriaClinicaGeneralException hcge = new HistoriaClinicaGeneralException("No fue posible agregar Historia Clinica General", HistoriaClinicaGeneralException.LAYER_REST, HistoriaClinicaGeneralException.ACTION_INSERT);
         hcge.addError("Ocurrio un error al agregar Historia Clinica General");
         logger.error("===>>>Error al insertar nuevo Historia Clinica General - CODE {} - {}", hcge.getExceptionCode(), hcge);
         throw hcge;
      }
   }

   @RequestMapping(value = "{idPaciente}", method = RequestMethod.PUT)
   @ResponseStatus(HttpStatus.OK)
   public void updateHistoriaClinicaGeneral(@PathVariable() String idPaciente, @RequestBody @Valid String json) throws HistoriaClinicaGeneralException {
      try {
         JSONObject jsonObj = new JSONObject(json);
         HistoriaClinicaGeneralView hcgV = new HistoriaClinicaGeneralView();
         Long fechaCreacion = jsonObj.getLong("fechaCreacion");
         Long vigencia = jsonObj.getLong("vigencia");
         Long ultimaModificacion = jsonObj.getLong("ultimaModificacion");
         hcgV.setFechaCreacion(new Date(fechaCreacion));
         hcgV.setVersion(jsonObj.getFloat("version"));
         hcgV.setVigencia(new Date(vigencia));
         hcgV.setUltimaModificacion(new Date(vigencia));
         hcgV.setActivo(jsonObj.getBoolean("activo"));
         hcgV.setCreadoPor(jsonObj.getString("creadoPor"));
         String temporal = jsonObj.getJSONObject("hcg").toString();
         hcgV.setHcg(temporal);
         if (jsonObj.getString("idPaciente").isEmpty() || jsonObj.getString("idPaciente").equals("")) {
            logger.error("===>>>No debe venir vacio el idPaciente");
            HistoriaClinicaGeneralException hcge = new HistoriaClinicaGeneralException("No debe venir vacio el idPaciente", HistoriaClinicaGeneralException.LAYER_REST, HistoriaClinicaGeneralException.ACTION_UPDATE);
            hcge.addError("No debe venir vacio el idPaciente");
            throw hcge;
         }
         UUID uuid = UUID.fromString(idPaciente);
         hcgV.setIdPaciente(uuid);
         logger.info("Editar Historia Clinica General: {}", hcgV);
         historiaClinicaGeneralService.updateHistoriaClinicaGeneral(hcgV);
      } catch (HistoriaClinicaGeneralException hcge) {
         throw hcge;
      } catch (IllegalArgumentException iae) {
         logger.error("El idPaciente no cumple con formato adecuado");
         HistoriaClinicaGeneralException hcge = new HistoriaClinicaGeneralException("El idPaciente no cumple con formato adecuado", HistoriaClinicaGeneralException.LAYER_REST, HistoriaClinicaGeneralException.ACTION_UPDATE);
         hcge.addError("El idPaciente no cumple con formato adecuado");
         throw hcge;
      } catch (Exception ex) {
         HistoriaClinicaGeneralException hcge = new HistoriaClinicaGeneralException("No fue posible modificar Historia Clinica General", HistoriaClinicaGeneralException.LAYER_REST, HistoriaClinicaGeneralException.ACTION_UPDATE);
         hcge.addError("Ocurrio un error al modificar Historia Clinica General");
         logger.error("===>>>Error al modificar nuevo Historia Clinica General - CODE {} - {}", hcge.getExceptionCode(), hcge);
         throw hcge;
      }
   }

   @RequestMapping(value = "{idPaciente}", method = RequestMethod.GET)
   @ResponseStatus(HttpStatus.OK)
   public HistoriaClinicaGeneralView getDetailsByIdPaciente(@PathVariable() String idPaciente) throws HistoriaClinicaGeneralException {
      try {
         UUID uuid = UUID.fromString(idPaciente);
         logger.info("Obtener detalles Historia Clinica General por idPaciente: {}", idPaciente);
         HistoriaClinicaGeneralView hcgV = historiaClinicaGeneralService.getDetailsByIdPaciente(uuid);
         return hcgV;
      } catch (HistoriaClinicaGeneralException hcge) {
         throw hcge;
      } catch (IllegalArgumentException iae) {
         logger.error("El idPaciente no cumple con formato adecuado");
         HistoriaClinicaGeneralException hcge = new HistoriaClinicaGeneralException("El idPaciente no cumple con formato adecuado", HistoriaClinicaGeneralException.LAYER_REST, HistoriaClinicaGeneralException.ACTION_SELECT);
         hcge.addError("El idPaciente no cumple con formato adecuado");
         throw hcge;
      } catch (Exception ex) {
         HistoriaClinicaGeneralException hcge = new HistoriaClinicaGeneralException("No fue posible obtener detalles Historia Clinica General", HistoriaClinicaGeneralException.LAYER_REST, HistoriaClinicaGeneralException.ACTION_SELECT);
         hcge.addError("Ocurrio un error al obtener detalles Historia Clinica General");
         logger.error("===>>>Error al obtener detalles Historia Clinica General - CODE {} - {}", hcge.getExceptionCode(), hcge);
         throw hcge;
      }
   }

}
