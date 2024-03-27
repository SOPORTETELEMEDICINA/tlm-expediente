package net.amentum.niomedic.expediente.converter;

import net.amentum.niomedic.expediente.model.CatalogoDia;
import net.amentum.niomedic.expediente.model.CatalogoMomento;
import net.amentum.niomedic.expediente.model.HorariosMedicion;
import net.amentum.niomedic.expediente.persistence.CatalogoDiaRepository;
import net.amentum.niomedic.expediente.persistence.CatalogoMomentoRepository;
import net.amentum.niomedic.expediente.views.HorariosMedicionView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HorariosMedicionConverter {
   private final Logger logger = LoggerFactory.getLogger(HorariosMedicionConverter.class);

   @Autowired
   CatalogoMomentoRepository catalogoMomentoRepository;

   @Autowired
   CatalogoDiaRepository catalogoDiaRepository;

   public HorariosMedicion toEntity(HorariosMedicionView horariosMedicionView, HorariosMedicion horariosMedicion, Boolean update) {
      horariosMedicion.setHora(horariosMedicionView.getHora());
      horariosMedicion.setIdPaciente(horariosMedicionView.getIdPaciente());

      CatalogoMomento catalogoMomento = catalogoMomentoRepository.findOne(horariosMedicionView.getCatalogoDiaId());
      if (catalogoMomento != null) {
         horariosMedicion.setCatalogoMomento(catalogoMomento);
      } else {
         horariosMedicion.setCatalogoMomento(null);
      }

      CatalogoDia catalogoDia = catalogoDiaRepository.findOne(horariosMedicionView.getCatalogoDiaId());
      if (catalogoDia != null) {
         horariosMedicion.setCatalogoDia(catalogoDia);
      } else {
         horariosMedicion.setCatalogoDia(null);
      }

      logger.debug("convertir HorariosMedicionView to Entity: {}", horariosMedicion);
      return horariosMedicion;
   }

   public HorariosMedicionView toView(HorariosMedicion horariosMedicion, Boolean completeCoversion) {
      HorariosMedicionView horariosMedicionView = new HorariosMedicionView();
      horariosMedicionView.setIdHorariosMedicion(horariosMedicion.getIdHorariosMedicion());
      horariosMedicionView.setHora(horariosMedicion.getHora());
      horariosMedicionView.setIdPaciente(horariosMedicion.getIdPaciente());

      if (completeCoversion) {
         CatalogoMomento catalogoMomento = horariosMedicion.getCatalogoMomento();
         horariosMedicionView.set_descripcionMomento((catalogoMomento == null) ? "No existe" : catalogoMomento.getDescripcionMomento());
         horariosMedicionView.setCatalogoMomentoId((catalogoMomento == null) ? -1 : catalogoMomento.getIdCatalogoMomento());

         CatalogoDia catalogoDia = horariosMedicion.getCatalogoDia();
         horariosMedicionView.set_descripcionDia((catalogoDia == null) ? "No existe" : catalogoDia.getDescripcionDia());
         horariosMedicionView.setCatalogoDiaId((catalogoDia == null) ? -1 : catalogoDia.getIdCatalogoDia());
      }

      logger.debug("convertir HorariosMedicion to View: {}", horariosMedicionView);
      return horariosMedicionView;

   }

}
