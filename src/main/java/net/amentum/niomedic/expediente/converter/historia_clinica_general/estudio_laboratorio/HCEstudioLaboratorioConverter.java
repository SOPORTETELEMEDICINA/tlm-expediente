package net.amentum.niomedic.expediente.converter.historia_clinica_general.estudio_laboratorio;

import net.amentum.niomedic.expediente.model.historia_clinica.estudio_laboratorio.HCEstudioLaboratorio;
import net.amentum.niomedic.expediente.views.EstudioLaboratorioListView;
import net.amentum.niomedic.expediente.views.historia_clinica_general.estudio_laboratorio.HCEstudioLaboratorioView;
import org.springframework.stereotype.Component;

@Component
public class HCEstudioLaboratorioConverter {

   public HCEstudioLaboratorio toEntity(HCEstudioLaboratorioView view) {
      HCEstudioLaboratorio entity = new HCEstudioLaboratorio();
      entity.setIdEstudioLaboratorio(view.getIdEstudioLaboratorio());
      entity.setIdHistoriaClinica(view.getIdHistoriaClinica());
      entity.setObservaciones(view.getNotas());
      return entity;
   }

   public HCEstudioLaboratorioView toView(HCEstudioLaboratorio entity) {
      HCEstudioLaboratorioView view = new HCEstudioLaboratorioView();
      view.setIdEstudioLaboratorio(entity.getIdEstudioLaboratorio());
      view.setIdHistoriaClinica(entity.getIdHistoriaClinica());
      view.setNotas(entity.getObservaciones());
      return view;
   }


   public EstudioLaboratorioListView toView(HCEstudioLaboratorio estudioLaboratorio, Boolean complete) {
      EstudioLaboratorioListView estudioLaboratorioListView = new EstudioLaboratorioListView();
      estudioLaboratorioListView.setIdEstudioLaboratorio(estudioLaboratorio.getIdEstudioLaboratorio());
      estudioLaboratorioListView.setObservaciones(estudioLaboratorio.getObservaciones());
      return estudioLaboratorioListView;
   }
}
