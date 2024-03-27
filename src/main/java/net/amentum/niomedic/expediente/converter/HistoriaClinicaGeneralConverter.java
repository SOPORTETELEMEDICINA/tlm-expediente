package net.amentum.niomedic.expediente.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.amentum.niomedic.expediente.model.HistoriaClinicaGeneral;
import net.amentum.niomedic.expediente.views.HistoriaClinicaGeneralView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class HistoriaClinicaGeneralConverter {
   private final Logger logger = LoggerFactory.getLogger(HistoriaClinicaGeneral.class);

   @Autowired
   private ObjectMapper objectMapper;

   public HistoriaClinicaGeneral toEntity(HistoriaClinicaGeneralView historiaClinicaGeneralView, HistoriaClinicaGeneral historiaClinicaGeneral, Boolean update){
      historiaClinicaGeneral.setIdPaciente(historiaClinicaGeneralView.getIdPaciente());
      historiaClinicaGeneral.setFechaCreacion((update)?historiaClinicaGeneral.getFechaCreacion():new Date());
      historiaClinicaGeneral.setVersion(historiaClinicaGeneralView.getVersion());
      historiaClinicaGeneral.setVigencia(historiaClinicaGeneralView.getVigencia());
      historiaClinicaGeneral.setUltimaModificacion(new Date());
      historiaClinicaGeneral.setActivo(historiaClinicaGeneralView.getActivo());
      historiaClinicaGeneral.setCreadoPor(historiaClinicaGeneralView.getCreadoPor());
      historiaClinicaGeneral.setHcg(historiaClinicaGeneralView.getHcg());
      historiaClinicaGeneral.setIdGroup(historiaClinicaGeneral.getIdGroup());
      logger.debug("convertir HistoriaClinicaGeneralView to Entity: {}", historiaClinicaGeneral);
      return historiaClinicaGeneral;
   }

   public HistoriaClinicaGeneralView toView(HistoriaClinicaGeneral historiaClinicaGeneral, Boolean complete){
      HistoriaClinicaGeneralView historiaClinicaGeneralView = new HistoriaClinicaGeneralView();
      historiaClinicaGeneralView.setIdHistoriaClinicaGeneral(historiaClinicaGeneral.getIdHistoriaClinicaGeneral());
      historiaClinicaGeneralView.setIdPaciente(historiaClinicaGeneral.getIdPaciente());
      historiaClinicaGeneralView.setFechaCreacion(historiaClinicaGeneral.getFechaCreacion());
      historiaClinicaGeneralView.setVersion(historiaClinicaGeneral.getVersion());
      historiaClinicaGeneralView.setVigencia(historiaClinicaGeneral.getVigencia());
      historiaClinicaGeneralView.setUltimaModificacion(historiaClinicaGeneral.getUltimaModificacion());
      historiaClinicaGeneralView.setActivo(historiaClinicaGeneral.getActivo());
      historiaClinicaGeneralView.setCreadoPor(historiaClinicaGeneral.getCreadoPor());
      historiaClinicaGeneralView.setHcg(historiaClinicaGeneral.getHcg());
      historiaClinicaGeneralView.setIdGroup(historiaClinicaGeneral.getIdGroup());
      logger.debug("convertir HistoriaClinicaGeneral to View: {}", historiaClinicaGeneralView);
      return historiaClinicaGeneralView;

   }
}
