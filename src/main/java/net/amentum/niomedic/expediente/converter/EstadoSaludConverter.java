package net.amentum.niomedic.expediente.converter;

import net.amentum.niomedic.expediente.model.EstadoSalud;
import net.amentum.niomedic.expediente.persistence.CatalogoTipoEventoRepository;
import net.amentum.niomedic.expediente.views.EstadoSaludView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class EstadoSaludConverter {
   private Logger logger = LoggerFactory.getLogger(EstadoSaludConverter.class);

   @Autowired
   CatalogoTipoEventoRepository catalogoTipoEventoRepository;

   @Autowired
   InvitadosConverter invitadosConverter;

   public EstadoSalud toEntity(EstadoSaludView estadoSaludView, EstadoSalud estadoSalud, Boolean update) {
      estadoSalud.setMotivo(estadoSaludView.getMotivo());
      estadoSalud.setDolometroId(estadoSaludView.getDolometroId());
      estadoSalud.setIdUsuario(estadoSaludView.getIdUsuario());
      estadoSalud.setIdPaciente(estadoSaludView.getIdPaciente());
      estadoSalud.setFechaCreacion((update) ? estadoSalud.getFechaCreacion() : new Date());
//      para los campos relacionados
      estadoSalud.set_doloDescripcion(estadoSaludView.get_doloDescripcion());

      logger.debug("convertir estadoSaludView to Entity: {}", estadoSalud);
      return estadoSalud;
   }

   public EstadoSaludView toView(EstadoSalud estadoSalud, Boolean completeCoversion) {
      EstadoSaludView estadoSaludView = new EstadoSaludView();
      estadoSaludView.setIdEstadoSalud(estadoSalud.getIdEstadoSalud());
      estadoSaludView.setMotivo(estadoSalud.getMotivo());
      estadoSaludView.setDolometroId(estadoSalud.getDolometroId());
      estadoSaludView.setIdUsuario(estadoSalud.getIdUsuario());
      estadoSaludView.setIdPaciente(estadoSalud.getIdPaciente());
      estadoSaludView.setFechaCreacion(estadoSalud.getFechaCreacion());
//      para los campos relacionados
      estadoSaludView.set_doloDescripcion(estadoSalud.get_doloDescripcion());

      logger.debug("convertir estadoSaludView to Entity: {}", estadoSaludView);
      return estadoSaludView;
   }

}
