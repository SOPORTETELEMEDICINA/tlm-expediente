package net.amentum.niomedic.expediente.rest;

import net.amentum.common.BaseController;
import net.amentum.niomedic.expediente.exception.ImagenLaboratorioException;
import net.amentum.niomedic.expediente.service.ImagenLaboratorioService;
import net.amentum.niomedic.expediente.views.ArchivoView;
import net.amentum.niomedic.expediente.views.ImagenLaboratorioListView;
import net.amentum.niomedic.expediente.views.ImagenLaboratorioView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

@RestController
@RequestMapping("imagen-laboratorio")
public class ImagenLaboratorioRest extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(ImagenLaboratorioRest.class);
    private ImagenLaboratorioService imagenLaboratorioService;

    @Autowired
    public void setImagenLaboratorioService(ImagenLaboratorioService imagenLaboratorioService) {
        this.imagenLaboratorioService = imagenLaboratorioService;
    }

   @RequestMapping(value = "page", method = RequestMethod.GET)
   @ResponseStatus(HttpStatus.OK)
   public Page<ImagenLaboratorioListView> getImagenLaboratorioPage(@RequestParam(required = false, defaultValue = "") String idPaciente,
                                                     @RequestParam(required = false, defaultValue = "") Long idConsulta,
                                                     @RequestParam(required = false) Boolean active,
                                                     @RequestParam(required = false) Integer page,
                                                     @RequestParam(required = false) Integer size,
                                                     @RequestParam(required = false) String orderColumn,
                                                     @RequestParam(required = false) String orderType) throws ImagenLaboratorioException {
      logger.info("- Obtener listado imagen laboratorio paginable: - active {} - idPaciente {} - idConsulta {} - page {} - size: {} - orderColumn: {} - orderType: {}",
         active, idPaciente, idConsulta, page, size, orderColumn, orderType);
      if (page == null)
         page = 0;
      if (size == null)
         size = 10;
      if (orderType == null && orderType.isEmpty()) {
         orderType = "asc";
      }
      active=true;
      return imagenLaboratorioService.getImagenLaboratorioPage(active,idPaciente != null ? idPaciente : "", idConsulta != null ? idConsulta : 0,page, size, orderColumn, orderType);
   }


//   @RequestMapping(value = "subir/{idPaciente}", method = RequestMethod.POST)
//   public void subirImagenes(@RequestParam("file") MultipartFile[] file, @PathVariable() String idPaciente) {
//      for (MultipartFile imagen : file) {
//         String nombreArchivoAlmacenar = idPaciente + imagen.getOriginalFilename();
////         String filePath = ".." + File.separator + "imagenLaboratorio" + File.separator;
//         String filePath = File.separator + "var" + File.separator + "www" + File.separator + "html" + File.separator + File.separator + "niomedic" + File.separator + "imagenesLaboratorio" + File.separator;
//         if (!imagen.isEmpty()) {
//            try {
//               imagen.transferTo(new File(filePath + nombreArchivoAlmacenar));
//               logger.debug("---> Va para aca ---> {}", filePath);
//            } catch (Exception e) {
//               logger.debug("--->Error al subir imagen en REST: ---> {}", e.getCause());
//            }
//         }
//      }
//   }
   
   @Value("${imagenesFS}")
   private String direccion;
//   @RequestMapping(value = "subir-archivo/{idPaciente}", method = RequestMethod.POST)
//   public void subirArchivo(@PathVariable() String idPaciente, @RequestBody ArchivoView  archivobase64) throws ImagenLaboratorioException{
//	   imagenLaboratorioService.surbirArchivo(idPaciente, archivobase64, direccion);
//   }


   @RequestMapping(value = "subir-archivo/{idPaciente}", method = RequestMethod.POST)
   @ResponseStatus(HttpStatus.CREATED)
   public void subirArchivo(@PathVariable() String idPaciente, @RequestBody @Valid ImagenLaboratorioView  archivobase64) throws ImagenLaboratorioException{
      logger.info("subirArchivo() - Agregado imagen idPaciente:{}, consulta:{}",idPaciente,archivobase64.getConsultaId() ); 
	   imagenLaboratorioService.surbirArchivo(idPaciente, archivobase64, direccion);
   }


   @RequestMapping(value = "subir/{idPaciente}/consulta/{idConsulta}", method = RequestMethod.POST)
   @ResponseStatus(HttpStatus.CREATED)
   public void subirImagenes(@PathVariable("file") MultipartFile file, @PathVariable("idPaciente") String idPaciente, @PathVariable("idConsulta") Long idConsulta) throws ImagenLaboratorioException {

      String nombreArchivoAlmacenar = idPaciente + "-" + file.getOriginalFilename();
//         String filePath = ".." + File.separator + "file" + File.separator;
      String filePath = File.separator + "var" + File.separator + "www" + File.separator + "html" + File.separator + "niomedic" + File.separator + "imagenesLaboratorio" + File.separator;
      if (!file.isEmpty()) {
         try {
            file.transferTo(new File(filePath + nombreArchivoAlmacenar));
            logger.info("======> Se almacena en {}", filePath);
            ImagenLaboratorioView imagenLaboratorioView = new ImagenLaboratorioView();
            imagenLaboratorioView.setContentType(file.getContentType());
//            imagenLaboratorioView.setImageName(filePath + nombreArchivoAlmacenar);
            imagenLaboratorioView.setImageName("http://dev.niomedic.com/niomedic/imagenesLaboratorio/" + nombreArchivoAlmacenar);
            imagenLaboratorioView.setFechaCreacion(new Date());
            imagenLaboratorioView.setIdPaciente(idPaciente);
            imagenLaboratorioView.setConsultaId(idConsulta);
            imagenLaboratorioService.createImagenLaboratorio(imagenLaboratorioView);
            logger.info("=======> Se guardaron los datos del Imagen Laboratorio en la DB");
         } catch (ImagenLaboratorioException imle) {
            imle = new ImagenLaboratorioException("======> No fue posible agregar ImagenLaboratorio", ImagenLaboratorioException.LAYER_DAO, ImagenLaboratorioException.ACTION_INSERT);
            imle.addError("======> Error al insertar nuevo Imagen Laboratorio");
            logger.error("======>Error al insertar nuevo Imagen Laboratorio - CODE {} - {}", imle.getExceptionCode(), imle);
            throw imle;
         } catch (FileNotFoundException fe) {
            logger.debug("======> Error al subir documento  NO existe: {}", fe.getCause());
         } catch (IOException ioe){
            logger.debug("======> Error al subir documento  I/O: {}", ioe.getCause());
         }
      }
   }
   
   @RequestMapping(value="{idImagenLaboratorio}", method=RequestMethod.DELETE)
   @ResponseStatus(HttpStatus.OK)
   public void eliminarImagenLaboratorio(@PathVariable("idImagenLaboratorio") Long idImagenLaboratorio) throws ImagenLaboratorioException {
	   logger.info("eliminarImagenLaboratorio() - Eliminando imagen con el id:{}",idImagenLaboratorio);
	   imagenLaboratorioService.eliminarImagen(idImagenLaboratorio);
   }


   @RequestMapping(value= "por-padecimiento/{idPadecimiento}", method = RequestMethod.GET)
   @ResponseStatus(HttpStatus.OK)
   public List<ImagenLaboratorioListView> getDocumentosPorPadecimiento(@PathVariable() Long idPadecimiento) throws ImagenLaboratorioException {
      logger.info("GET - getDocumentosPorPadecimiento() - Obteniendo documentos por padecimiento");
      return  imagenLaboratorioService.getImagenLaboratorioPorPadecimiento(idPadecimiento);
   }


}
