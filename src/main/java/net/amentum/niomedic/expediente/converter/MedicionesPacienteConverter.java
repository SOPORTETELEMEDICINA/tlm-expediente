package net.amentum.niomedic.expediente.converter;

import net.amentum.niomedic.expediente.model.MedicionesPaciente;
import net.amentum.niomedic.expediente.views.MedicionesPacienteView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class MedicionesPacienteConverter {
   private final Logger logger = LoggerFactory.getLogger(MedicionesPacienteConverter.class);

   public MedicionesPaciente toEntity(MedicionesPacienteView medicionesPacienteView, MedicionesPaciente medicionesPaciente, Boolean update) {
      medicionesPaciente.setFechaCreacion((update) ? medicionesPaciente.getFechaCreacion() : new Date());
      medicionesPaciente.setIdPaciente(medicionesPacienteView.getIdPaciente());
      medicionesPaciente.setDiabetes(medicionesPacienteView.getDiabetes());
//      medicionesPaciente.setOximetria(medicionesPacienteView.getOximetria());
      medicionesPaciente.setOxPr(medicionesPacienteView.getOxPr());
      medicionesPaciente.setOxSpo2(medicionesPacienteView.getOxSpo2());
      medicionesPaciente.setHiperSistolica(medicionesPacienteView.getHiperSistolica());
      medicionesPaciente.setHiperDiastolica(medicionesPacienteView.getHiperDiastolica());
      medicionesPaciente.setHiperPulso(medicionesPacienteView.getHiperPulso());
      medicionesPaciente.setIdUsuario(medicionesPacienteView.getIdUsuario());
      medicionesPaciente.setNombrePaciente(medicionesPacienteView.getNombrePaciente());
      medicionesPaciente.setNombreUsuario(medicionesPacienteView.getNombreUsuario());
      medicionesPaciente.setFueraLimites(medicionesPacienteView.getFueraLimites());
      medicionesPaciente.setIdPeriodoControles(medicionesPacienteView.getIdPeriodoControles());

      logger.debug("convertir MedicionesPacienteView to Entity: {}", medicionesPaciente);
      return medicionesPaciente;
   }

   public MedicionesPacienteView toView(MedicionesPaciente medicionesPaciente, Boolean completeCoversion) {
      MedicionesPacienteView medicionesPacienteView = new MedicionesPacienteView();
      medicionesPacienteView.setIdMedicionesPaciente(medicionesPaciente.getIdMedicionesPaciente());
      medicionesPacienteView.setFechaCreacion(medicionesPaciente.getFechaCreacion());
      medicionesPacienteView.setIdPaciente(medicionesPaciente.getIdPaciente());
      medicionesPacienteView.setDiabetes(medicionesPaciente.getDiabetes());
//      medicionesPacienteView.setOximetria(medicionesPaciente.getOximetria());
      medicionesPaciente.setOxPr(medicionesPacienteView.getOxPr());
      medicionesPaciente.setOxSpo2(medicionesPacienteView.getOxSpo2());
      medicionesPacienteView.setHiperSistolica(medicionesPaciente.getHiperSistolica());
      medicionesPacienteView.setHiperDiastolica(medicionesPaciente.getHiperDiastolica());
      medicionesPacienteView.setHiperPulso(medicionesPaciente.getHiperPulso());
      medicionesPacienteView.setIdUsuario(medicionesPaciente.getIdUsuario());
      medicionesPacienteView.setNombrePaciente(medicionesPaciente.getNombrePaciente());
      medicionesPacienteView.setNombreUsuario(medicionesPaciente.getNombreUsuario());
      medicionesPacienteView.setFueraLimites(medicionesPaciente.getFueraLimites());
      medicionesPacienteView.setIdPeriodoControles(medicionesPaciente.getIdPeriodoControles());
      logger.debug("convertir MedicionesPaciente to View: {}", medicionesPacienteView);
      return medicionesPacienteView;

   }

}
