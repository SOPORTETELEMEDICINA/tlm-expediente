package net.amentum.niomedic.expediente.converter;

import net.amentum.niomedic.expediente.model.EstudioLaboratorio;
import net.amentum.niomedic.expediente.views.EstudioLaboratorioListView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class EstudioLaboratorioConverter {
   private final Logger logger = LoggerFactory.getLogger(EstudioLaboratorioConverter.class);
   /*
   no hay toEntity porque no se TODAVIA NO añade nada
    */

   public EstudioLaboratorioListView toView(EstudioLaboratorio estudioLaboratorio, Boolean complete) {

      EstudioLaboratorioListView estudioLaboratorioListView = new EstudioLaboratorioListView();
      estudioLaboratorioListView.setIdEstudioLaboratorio(estudioLaboratorio.getIdEstudioLaboratorio());
      estudioLaboratorioListView.setIdMedico(estudioLaboratorio.getIdMedico());
      estudioLaboratorioListView.setTipoEstudio(estudioLaboratorio.getTipoEstudio());
      estudioLaboratorioListView.setObservaciones(estudioLaboratorio.getObservaciones());
      estudioLaboratorioListView.setNombreLaboratorio(estudioLaboratorio.getNombreLaboratorio());
      estudioLaboratorioListView.setIdPaciente(estudioLaboratorio.getIdPaciente());
      estudioLaboratorioListView.setFechaCreacion(estudioLaboratorio.getFechaCreacion());
      estudioLaboratorioListView.setConsultaId((estudioLaboratorio.getConsulta() != null) ? estudioLaboratorio.getConsulta().getIdConsulta() : 0);

      logger.debug("convertir EstudioLaboratorio to View: {}", estudioLaboratorioListView);
      return estudioLaboratorioListView;
   }
}
