package net.amentum.niomedic.expediente.service;

import net.amentum.niomedic.catalogos.views.CatDolometroView;
import net.amentum.niomedic.expediente.exception.EstadoSaludException;
import net.amentum.niomedic.expediente.views.EstadoSaludView;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface EstadoSaludService {
   EstadoSaludView createEstadoSalud(EstadoSaludView estadoSaludView) throws EstadoSaludException;

   EstadoSaludView updateEstadoSalud(EstadoSaludView estadoSaludView) throws EstadoSaludException;

   Page<EstadoSaludView> getEstadoSaludPage(UUID idPaciente, Long startDate, Long endDate, Integer page, Integer size, String orderColumn, String orderType) throws EstadoSaludException;

   EstadoSaludView getLastEstadoSalud(UUID idPaciente) throws EstadoSaludException;
}
