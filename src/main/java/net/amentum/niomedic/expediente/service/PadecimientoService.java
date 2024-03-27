package net.amentum.niomedic.expediente.service;

import net.amentum.niomedic.expediente.exception.PadecimientoException;
import net.amentum.niomedic.expediente.views.ConsultaView;
import net.amentum.niomedic.expediente.views.PadecimientoView;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PadecimientoService {
   void createPadecimiento(PadecimientoView PadecimientoView) throws PadecimientoException;

   void updatePadecimiento(PadecimientoView PadecimientoView) throws PadecimientoException;/* PadecimientoView getDetailsByIdPadecimiento(String idPaciente) throws PadecimientoException;*/

   PadecimientoView getDetailsByIdPadecimiento(Long idPadecimiento) throws PadecimientoException;

   List<PadecimientoView> findAll() throws PadecimientoException;

   Page<PadecimientoView> getPadecimientoPage(String name, Integer page, Integer size, String orderColumn, String orderType) throws PadecimientoException;

   Page<PadecimientoView> getCursoClinicoSearch(String idPaciente, String datosBusqueda, Integer page, Integer size, String orderColumn, String orderType) throws PadecimientoException;

   PadecimientoView getDetailsByIdPAdecimientoAndIdPaciente(Long idPadecimiento, String idPaciente) throws PadecimientoException;

   Page<ConsultaView> listaConsulta(Long idPadecimiento, Integer page, Integer size, String orderColumn, String orderType, String datosBusqueda) throws PadecimientoException;

   void updateStatusPadecimiento(Long idPadecimiento) throws PadecimientoException;
}
