package net.amentum.niomedic.expediente.converter;

import net.amentum.niomedic.expediente.exception.ImagenLaboratorioException;
import net.amentum.niomedic.expediente.model.Consulta;
import net.amentum.niomedic.expediente.model.ImagenLaboratorio;
import net.amentum.niomedic.expediente.views.ImagenLaboratorioListView;
import net.amentum.niomedic.expediente.views.ImagenLaboratorioView;
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
public class ImagenLaboratorioConverter {
   private final Logger logger = LoggerFactory.getLogger(ImagenLaboratorioConverter.class);
   @Value("${imagenesFS}")
   String direccion;

   public ImagenLaboratorio toEntity(ImagenLaboratorioView imagenLaboratorioView, ImagenLaboratorio imagenLaboratorio, Boolean update) throws ImagenLaboratorioException {
      imagenLaboratorio.setContentType(imagenLaboratorioView.getContentType());

      if (imagenLaboratorioView.getImageName().contains("data:")) {
//         imagenLaboratorio.setImageName(guardarArchivo(imagenLaboratorioView.getImageName(), imagenLaboratorioView.getDireccion()));
         imagenLaboratorio.setImageName(guardarArchivo(imagenLaboratorioView.getImageName()));
      } else {
         imagenLaboratorio.setImageName(imagenLaboratorioView.getImageName());
      }

      imagenLaboratorio.setIdPaciente(imagenLaboratorioView.getIdPaciente());
      imagenLaboratorio.setFechaCreacion(imagenLaboratorioView.getFechaCreacion());
      Consulta consulta = new Consulta();
      consulta.setIdConsulta(imagenLaboratorioView.getConsultaId());
      imagenLaboratorio.setConsulta(consulta);

      logger.debug("convertir ImagenLaboratorio to Entity: {}", imagenLaboratorio);
      return imagenLaboratorio;
   }

   public ImagenLaboratorioListView toView(ImagenLaboratorio imagenLaboratorio, Boolean complete) {

      ImagenLaboratorioListView imagenLaboratorioListView = new ImagenLaboratorioListView();
      imagenLaboratorioListView.setIdImagenLaboratorio(imagenLaboratorio.getIdImagenLaboratorio());
      imagenLaboratorioListView.setContentType(imagenLaboratorio.getContentType());

//      if (imagenLaboratorio.getImageName().contains(imagenLaboratorio.getIdPaciente())) {
//         imagenLaboratorioListView.setImageName(imagenLaboratorio.getImageName());
//      } else {
//         try {
//            imagenLaboratorioListView.setImageName(obtenerArchivo(imagenLaboratorio.getImageName()));
//         } catch (ImagenLaboratorioException imle) {
//            logger.error("======>no se pudo obtener el archivo");
//         }
//      }

      String archivo = obtenerArchivo(imagenLaboratorio.getImageName());
      if (archivo != null) {
         imagenLaboratorioListView.setImageName(archivo);
      } else {
         return null;
      }

      imagenLaboratorioListView.setIdPaciente(imagenLaboratorio.getIdPaciente());
      imagenLaboratorioListView.setFechaCreacion(imagenLaboratorio.getFechaCreacion());
      imagenLaboratorioListView.setConsultaId((imagenLaboratorio.getConsulta() != null) ? imagenLaboratorio.getConsulta().getIdConsulta() : 0);

      logger.debug("convertir ImagenLaboratorio to View: {}", imagenLaboratorioListView);
      return imagenLaboratorioListView;
   }

   //   private String guardarArchivo(String archivoB64, String direccion) throws ImagenLaboratorioException {
   private String guardarArchivo(String archivoB64) throws ImagenLaboratorioException {

      try {
         String archivoBase64 = archivoB64;
//         final String mostrardir = "http://dev.niomedic.com/niomedic/imagenesLaboratorio/";
         String fotmatoarchivo = archivoBase64.substring(archivoBase64.indexOf('/') + 1, archivoBase64.indexOf(';'));
         String temporal = archivoBase64.substring(archivoBase64.indexOf(',') + 1, archivoBase64.length());
         String idimage = idImgane() + "." + fotmatoarchivo;
         String dir = direccion + "" + idimage;
         byte[] decodedBytes = Base64.getDecoder().decode(temporal.getBytes());
         FileOutputStream out;
         out = new FileOutputStream(dir);
//         String temp = mostrardir + "" + idimage;
         out.write(decodedBytes);
         out.close();
         return dir;
      } catch (Exception ex) {
         ImagenLaboratorioException ile = new ImagenLaboratorioException("Ocurrio un error al subir el archivo",
            ImagenLaboratorioException.LAYER_CONVERTER, ImagenLaboratorioException.ACTION_INSERT);
         logger.error("Formato de base64 incorrecto - Error: {}", ex);
         throw ile;
      }
   }

   static public UUID idImgane() {
      UUID idOne = UUID.randomUUID();
      return idOne;
   }

   //   private String obtenerArchivo(String imagenBase64) throws ImagenLaboratorioException {
   private String obtenerArchivo(String imagenBase64) {

      String encodedBase64;
      try {
         //se obtiene la imagen
         File inputFile = new File(imagenBase64);
//         FileInputStream fileInputStreamReader = new FileInputStream(inputFile);
//         //se conbierte a bytes
//         byte[] bytes = new byte[(int) inputFile.length()];
//         fileInputStreamReader.read(bytes);
//         //el conjunto de bytes se le da formato base64
//         encodedBase64 = Base64.getEncoder().encodeToString(bytes);
//
//         return encodedBase64;
//      } catch (Exception ex) {
//         ImagenLaboratorioException ile = new ImagenLaboratorioException(
//            "Ocurrio un error al obtener el archivo",
//            ImagenLaboratorioException.LAYER_SERVICE, ImagenLaboratorioException.ACTION_SELECT);
//         logger.error(
//            ExceptionServiceCode.GROUP
//               + "- Ocurrio un error al obtener - CODE: {}-{}",
//            ile.getExceptionCode(), ex);
//         throw ile;
//      }

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
