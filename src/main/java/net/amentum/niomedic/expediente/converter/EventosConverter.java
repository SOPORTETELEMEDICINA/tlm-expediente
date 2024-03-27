package net.amentum.niomedic.expediente.converter;

import net.amentum.niomedic.expediente.model.CatalogoTipoEvento;
import net.amentum.niomedic.expediente.model.Eventos;
import net.amentum.niomedic.expediente.persistence.CatalogoTipoEventoRepository;
import net.amentum.niomedic.expediente.views.EventosView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EventosConverter {
   private Logger logger = LoggerFactory.getLogger(EventosConverter.class);

   @Autowired
   CatalogoTipoEventoRepository catalogoTipoEventoRepository;

   @Autowired
   InvitadosConverter invitadosConverter;

   public Eventos toEntity(EventosView eventosView, Eventos eventos, Boolean update) {
      eventos.setIdUsuarioCrea(eventosView.getIdUsuario());
      eventos.setTitulo(eventosView.getTitulo());
      eventos.setInicio(eventosView.getInicio());
      eventos.setFin(eventosView.getFin());
      eventos.setUbicacion(eventosView.getUbicacion());
      eventos.setConferencia(eventosView.getConferencia());
      eventos.setDescripcion(eventosView.getDescripcion());
      eventos.setAlerta(eventosView.getAlerta());
      eventos.setVisible(eventosView.getVisible());
      eventos.setIdConsulta(eventosView.getIdConsulta());
      eventos.setRegionSanitaria(eventosView.getRegionSanitaria());
      eventos.setUnidadMedica(eventosView.getUnidadMedica());
      eventos.setStatus(eventosView.getStatus());
      eventos.setIdUsuarioRecibe(eventosView.getIdUsuarioRecibe());
      eventos.setIdPaciente(eventosView.getIdPaciente());
      eventos.setEspecialidad(eventosView.getEspecialidad());

//      relacion OneToOne a catalogoTipoEvento
      CatalogoTipoEvento catalogoTipoEvento = catalogoTipoEventoRepository.findOne(eventosView.getTipoEventoId());
      if (catalogoTipoEvento != null) {
         eventos.setCatalogoTipoEvento(catalogoTipoEvento);
      } else {
         eventos.setCatalogoTipoEvento(null);
      }

//      relacion OneToMany a invitados
      if (eventosView.getInvitadosViewList() != null && !eventosView.getInvitadosViewList().isEmpty()) {
         eventos.setInvitadosList(invitadosConverter.toEntity(
            eventosView.getInvitadosViewList(),
            eventos.getInvitadosList(),
            eventos,
            update
         ));
      }



      logger.debug("convertir eventosView to Entity: {}", eventos);
      return eventos;
   }

   public EventosView toView(Eventos eventos, Boolean completeCoversion) {
      EventosView eventosView = new EventosView();
      eventosView.setIdEventos(eventos.getIdEventos());
      eventosView.setIdUsuario(eventos.getIdUsuarioCrea());
      eventosView.setInicio(eventos.getInicio());
      eventosView.setFin(eventos.getFin());
      eventosView.setIdConsulta(eventos.getIdConsulta());
      eventosView.setRegionSanitaria(eventos.getRegionSanitaria());
      eventosView.setUnidadMedica(eventos.getUnidadMedica());
      eventosView.setStatus(eventos.getStatus());
      eventosView.setIdUsuarioRecibe(eventos.getIdUsuarioRecibe());
      eventosView.setIdPaciente(eventos.getIdPaciente());
      eventosView.setEspecialidad(eventos.getEspecialidad());
      if (completeCoversion) {
         CatalogoTipoEvento catalogoTipoEvento = eventos.getCatalogoTipoEvento();
         eventosView.set_eventoDescripcion((catalogoTipoEvento == null) ? "No existe" : catalogoTipoEvento.getDescripcion());
         eventosView.set_eventoColor((catalogoTipoEvento == null) ? "No existe" : catalogoTipoEvento.getColor());
         eventosView.setTipoEventoId(catalogoTipoEvento.getIdTipoEvento());

         if (eventos.getInvitadosList() != null && !eventos.getInvitadosList().isEmpty()) {
            eventosView.setInvitadosViewList(invitadosConverter.toView(eventos.getInvitadosList(), true));
         }
      }
//      son visibles
      if(eventos.getVisible()) {
         eventosView.setTitulo(eventos.getTitulo());
         eventosView.setUbicacion(eventos.getUbicacion());
         eventosView.setConferencia(eventos.getConferencia());
         eventosView.setDescripcion(eventos.getDescripcion());
         eventosView.setAlerta(eventos.getAlerta());
         eventosView.setVisible(eventos.getVisible());
      } else {
//         NO son visibles
         CatalogoTipoEvento catalogoTipoEvento = catalogoTipoEventoRepository.findByDescripcion("No Visible");
         eventosView.set_eventoDescripcion((catalogoTipoEvento == null) ? "No existe" : catalogoTipoEvento.getDescripcion());
         eventosView.set_eventoColor((catalogoTipoEvento == null) ? "No existe" : catalogoTipoEvento.getColor());
         eventosView.setInvitadosViewList(null);
      }



      logger.debug("convertir eventosView to Entity: {}", eventosView);
      return eventosView;
   }

}
