package net.amentum.niomedic.expediente.converter;

import net.amentum.niomedic.expediente.exception.DocumentosException;
import net.amentum.niomedic.expediente.model.Consulta;
import net.amentum.niomedic.expediente.model.Documentos;
import net.amentum.niomedic.expediente.views.DocumentosListView;
import net.amentum.niomedic.expediente.views.DocumentosView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Base64;
import java.util.UUID;

@Component
public class DocumentosConverter {
   private final Logger logger = LoggerFactory.getLogger(DocumentosConverter.class);
   @Value("${documentosFS}")
   String direccion;

   public Documentos toEntity(DocumentosView documentosView, Documentos documentos, Boolean update)
      throws DocumentosException {
      documentos.setContentType(documentosView.getContentType());
      if (documentosView.getDocumentoName().contains("data:")) {
         documentos
            .setDocumentoName(guardarArchivo(documentosView.getDocumentoName()));
      } else {
         documentos.setDocumentoName(documentosView.getDocumentoName());
      }
      documentos.setIdPaciente(documentosView.getIdPaciente());
      documentos.setFechaCreacion(documentosView.getFechaCreacion());
      Consulta consulta = new Consulta();
      consulta.setIdConsulta(documentosView.getConsultaId());
      documentos.setConsulta(consulta);

      logger.debug("convertir Documentos to Entity: {}", documentos);
      return documentos;
   }

   public DocumentosListView toView(Documentos documentos, Boolean complete) {

      DocumentosListView documentosListView = new DocumentosListView();
      documentosListView.setIdDocumentos(documentos.getIdDocumentos());
      documentosListView.setContentType(documentos.getContentType());
      String archivo = obtenerArchivo(documentos.getDocumentoName());
      if (archivo != null) {
         documentosListView.setDocumentoName(archivo);
      } else {
         return null;
      }
      documentosListView.setIdPaciente(documentos.getIdPaciente());
      documentosListView.setFechaCreacion(documentos.getFechaCreacion());
      documentosListView.setConsultaId((documentos.getConsulta() != null) ? documentos.getConsulta().getIdConsulta() : 0);

      logger.debug("convertir Documentos to View: {}", documentosListView);
      return documentosListView;
   }

   static public UUID idImgane() {
      UUID idOne = UUID.randomUUID();
      return idOne;
   }

   private String guardarArchivo(String archivoB64) throws DocumentosException {
      try {
         String archivoBase64 = archivoB64;
         String fotmatoarchivo = archivoBase64.substring(archivoBase64.indexOf('/') + 1, archivoBase64.indexOf(';'));
         String temporal = archivoBase64.substring(archivoBase64.indexOf(',') + 1, archivoBase64.length());
         String idimage = idImgane() + "." + fotmatoarchivo;
         String dir = direccion + "" + idimage;
         byte[] decodedBytes = Base64.getDecoder().decode(temporal.getBytes());
         FileOutputStream out;
         out = new FileOutputStream(dir);
         out.write(decodedBytes);
         out.close();
         return dir;
      } catch (Exception ex) {
         DocumentosException dce = new DocumentosException("Ocurrio un error al subir el archivo",
            DocumentosException.LAYER_CONVERTER, DocumentosException.ACTION_INSERT);
         logger.error("Formato de base64 incorrecto para documentos - Error: {}", ex);
         throw dce;
      }
   }

   private String obtenerArchivo(String imagenBase64) {

      String encodedBase64;
      try {
         // se obtiene la imagen
         File inputFile = new File(imagenBase64);
         if (inputFile.exists()) {
            FileInputStream fileInputStreamReader = new FileInputStream(inputFile);
            // se conbierte a bytes
            byte[] bytes = new byte[(int) inputFile.length()];
            fileInputStreamReader.read(bytes);
            // el conjunto de bytes se le da formato base64
            encodedBase64 = Base64.getEncoder().encodeToString(bytes);

            return encodedBase64;
         } else {
            logger.error("No existe el archivo en la ruta: {}", imagenBase64);
         }
      } catch (Exception ex) {
         logger.error(" Ocurrio un error al obtener - Error: {}", ex);
      }
      return null;
   }
}
