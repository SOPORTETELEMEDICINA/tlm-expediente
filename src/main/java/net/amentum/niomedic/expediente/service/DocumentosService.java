package net.amentum.niomedic.expediente.service;

import net.amentum.niomedic.expediente.exception.DocumentosException;
import net.amentum.niomedic.expediente.model.Documentos;
import net.amentum.niomedic.expediente.views.DocumentosListView;
import net.amentum.niomedic.expediente.views.DocumentosView;
import org.springframework.data.domain.Page;

import java.util.List;

public interface DocumentosService {
//   Page<DocumentosListView> getDocumentosPage(Boolean active, String name, Integer page, Integer size, String orderColumn, String orderType) throws DocumentosException;
   Page<DocumentosListView> getDocumentosPage(Boolean active, String idPaciente, Long idConsulta, Integer page, Integer size, String orderColumn, String orderType) throws DocumentosException;

   void createDocumentos(DocumentosView documentosView) throws DocumentosException;
   
   void createDocumentoBase64(DocumentosView documentosView) throws DocumentosException;
   
   void eliminarDocumento(Long idDocumento)  throws DocumentosException;
   
   List<DocumentosListView> getDocumentosPorPadecimiento(Long idPadecimiento)throws DocumentosException;
}
