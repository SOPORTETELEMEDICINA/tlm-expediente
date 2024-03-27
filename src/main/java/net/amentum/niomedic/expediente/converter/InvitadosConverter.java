package net.amentum.niomedic.expediente.converter;

import net.amentum.niomedic.expediente.model.Eventos;
import net.amentum.niomedic.expediente.model.Invitados;
import net.amentum.niomedic.expediente.views.EventosView;
import net.amentum.niomedic.expediente.views.InvitadosView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@Component
public class InvitadosConverter {
   private Logger logger = LoggerFactory.getLogger(InvitadosConverter.class);

   public Collection<Invitados> toEntity(Collection<InvitadosView> invitadosViewArrayList, Collection<Invitados> invitadosArrayList, Eventos eventos, Boolean update) {
      for (InvitadosView invV : invitadosViewArrayList) {
         Invitados invitados = new Invitados();
         if (update) {
            invitados.setIdInvitados(invV.getIdInvitados());
         }
         invitados.setIdUsuario(invV.getIdUsuario());
         invitados.setNombreCompleto(invV.getNombreCompleto());
         invitados.setEventos(eventos);
         invitadosArrayList.add(invitados);
      }
      logger.debug("converter invitadosView to Entity: {}", invitadosArrayList);
      return invitadosArrayList;
   }

   public Collection<InvitadosView> toView(Collection<Invitados> invitadosArrayList, Boolean completeConversion) {
      Collection<InvitadosView> invitadosViews = new ArrayList<>();
      for (Invitados inv : invitadosArrayList) {
         InvitadosView invV = new InvitadosView();
         invV.setIdInvitados(inv.getIdInvitados());
         invV.setIdUsuario(inv.getIdUsuario());
         invV.setNombreCompleto(inv.getNombreCompleto());
         invitadosViews.add(invV);
      }
      logger.debug("converter invitados to View: {}", invitadosViews);
      return invitadosViews;
   }

   public Collection<Long> obtenerIDNoExistentesInvitados(Eventos eventos, EventosView eventosView) {
//      IDs de DB
      Collection<Long> ids = new ArrayList<>();
      ids.addAll(
         eventos.getInvitadosList().stream()
            .map(tempo -> {
               Long id = tempo.getIdInvitados();
               return id;
            }).collect(Collectors.toList())
      );

//      IDs de View
      Collection<Long> idsView = new ArrayList<>();
      idsView.addAll(
         eventosView.getInvitadosViewList().stream()
            .map(tempoV -> {
               Long idV = tempoV.getIdInvitados();
               return idV;
            }).collect(Collectors.toList())
      );

//      Obtener los no existentes
      Collection<Long> noExisten = new ArrayList<>(ids);
      noExisten.removeAll(idsView);

//      logger.info("--->ids--->{}", ids);
//      logger.info("--->idsView--->{}", idsView);
//      logger.info("--->noExisten--->{}", noExisten);

      return noExisten;
   }
}
