package net.amentum.niomedic.expediente.service;

import net.amentum.niomedic.expediente.exception.EstudioLaboratorioException;
import net.amentum.niomedic.expediente.views.EstudioLaboratorioListView;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface EstudioLaboratorioService {
   Page<EstudioLaboratorioListView> getEstudioLaboratorioPage(Boolean active, String name, Integer page, Integer size, String orderColumn, String orderType) throws EstudioLaboratorioException;

   List<List<Map<String, Object>>> getEstudioLaboratorioPorPadecimiento(Long idPadecimiento) throws EstudioLaboratorioException;
}
