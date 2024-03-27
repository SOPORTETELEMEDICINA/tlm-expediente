package net.amentum.niomedic.expediente.rest;

import net.amentum.common.BaseController;
import net.amentum.niomedic.expediente.exception.DocumentosException;
import net.amentum.niomedic.expediente.service.DocumentosService;
import net.amentum.niomedic.expediente.views.DocumentosListView;
import net.amentum.niomedic.expediente.views.DocumentosView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("documentos")
public class DocumentosRest extends BaseController {

   private final Logger logger = LoggerFactory.getLogger(DocumentosRest.class);
   private DocumentosService documentosService;
   
   @Autowired
   public void setDocumentosService(DocumentosService documentosService) {
      this.documentosService = documentosService;
   }

   @RequestMapping(value = "page", method = RequestMethod.GET)
   @ResponseStatus(HttpStatus.OK)
   public Page<DocumentosListView> getDocumentosPage(@RequestParam(required = false, defaultValue = "") String idPaciente,
                                                     @RequestParam(required = false, defaultValue = "") Long idConsulta,
                                                     @RequestParam(required = false) Boolean active,
                                                     @RequestParam(required = false) Integer page,
                                                     @RequestParam(required = false) Integer size,
                                                     @RequestParam(required = false) String orderColumn,
                                                     @RequestParam(required = false) String orderType) throws DocumentosException {
      logger.info("- Obtener listado documentos paginable: - active {} - idPaciente {} - idConsulta {} - page {} - size: {} - orderColumn: {} - orderType: {}",
         active, idPaciente, idConsulta, page, size, orderColumn, orderType);
      if (page == null)
         page = 0;
      if (size == null)
         size = 10;
      if (orderType == null && orderType.isEmpty()) {
         orderType = "asc";
      }
      active=Boolean.TRUE;
      return documentosService.getDocumentosPage(active,idPaciente != null ? idPaciente : "", idConsulta != null ? idConsulta : 0,page, size, orderColumn, orderType);
   }

   @RequestMapping(value = "subir/{idPaciente}/consulta/{idConsulta}", method = RequestMethod.POST)
   @ResponseStatus(HttpStatus.CREATED)
  public void subirDocumentos(@PathVariable("file") MultipartFile file, @PathVariable("idPaciente") String idPaciente, @PathVariable("idConsulta") Long idConsulta) throws DocumentosException {
      String nombreArchivoAlmacenar = idPaciente + "-" + file.getOriginalFilename();
      String filePath = File.separator + "var" + File.separator + "www" + File.separator + "html" + File.separator + "niomedic" + File.separator + "documentos" + File.separator;
      if (!file.isEmpty()) {
         try {
            file.transferTo(new File(filePath + nombreArchivoAlmacenar));
            logger.info("======> Se almacena en {}", filePath);
            DocumentosView documentosView = new DocumentosView();
            documentosView.setContentType(file.getContentType());
            documentosView.setDocumentoName("http://dev.niomedic.com/niomedic/documentos/" + nombreArchivoAlmacenar);
            documentosView.setFechaCreacion(new Date());
            documentosView.setIdPaciente(idPaciente);
            documentosView.setConsultaId(idConsulta);
            documentosService.createDocumentos(documentosView);
            logger.info("=======> Se guardaron los datos del Documento en la DB");
         } catch (DocumentosException doce) {
            doce = new DocumentosException("======> No fue posible agregar Documentos", DocumentosException.LAYER_DAO, DocumentosException.ACTION_INSERT);
            doce.addError("======> Error al insertar nuevo Documento");
            logger.error("======>Error al insertar nuevo Documento - CODE {} - {}", doce.getExceptionCode(), doce);
            throw doce;
         } catch (FileNotFoundException fe) {
            logger.debug("======> Error al subir documento  NO existe: {}", fe.getCause());
         } catch (IOException ioe){
            logger.debug("======> Error al subir documento  I/O: {}", ioe.getCause());
         }
      }
   }
   
   @RequestMapping(value = "subir-documento/{idPaciente}", method = RequestMethod.POST)
   @ResponseStatus(HttpStatus.CREATED)
   public void subirDocumentosBase64(@PathVariable("idPaciente") String idPaciente,@RequestBody @Valid DocumentosView documentosView) throws DocumentosException {	   
	   logger.info("subirDocumentosBase64() - Subiendo Documentos para {}", idPaciente);
	   documentosView.setIdPaciente(idPaciente);
	   documentosService.createDocumentoBase64(documentosView);
   }
   
   @RequestMapping(value="{idDocumento}", method = RequestMethod.DELETE)
   @ResponseStatus(HttpStatus.OK)
   public void eliminarDocumento(@PathVariable("idDocumento")Long idDocumento) throws DocumentosException {
	   logger.info("eliminarDocumento() -  Eliminando documento con el id:{}",idDocumento);
	   documentosService.eliminarDocumento(idDocumento);
   }
   
   @RequestMapping(value= "por-padecimiento/{idPadecimiento}", method = RequestMethod.GET)
   @ResponseStatus(HttpStatus.OK)
   public List<DocumentosListView> getDocumentosPorPadecimiento(@PathVariable() Long idPadecimiento) throws DocumentosException {
	   logger.info("GET - getDocumentosPorPadecimiento() - Obteniendo documentos por padecimiento");
	  return  documentosService.getDocumentosPorPadecimiento(idPadecimiento);
   }
   
}





