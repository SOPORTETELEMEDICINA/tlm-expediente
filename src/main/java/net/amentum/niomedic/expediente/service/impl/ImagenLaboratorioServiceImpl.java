package net.amentum.niomedic.expediente.service.impl;

import net.amentum.niomedic.expediente.converter.ImagenLaboratorioConverter;
import net.amentum.niomedic.expediente.exception.ExceptionServiceCode;
import net.amentum.niomedic.expediente.exception.ImagenLaboratorioException;
import net.amentum.niomedic.expediente.model.Consulta;
import net.amentum.niomedic.expediente.model.ImagenLaboratorio;
import net.amentum.niomedic.expediente.persistence.ConsultaRepository;
import net.amentum.niomedic.expediente.persistence.EstudioLaboratorioRepository;
import net.amentum.niomedic.expediente.persistence.ImagenLaboratorioRepository;
import net.amentum.niomedic.expediente.service.ImagenLaboratorioService;
import net.amentum.niomedic.expediente.views.ImagenLaboratorioListView;
import net.amentum.niomedic.expediente.views.ImagenLaboratorioView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@Transactional(readOnly = true)
public class ImagenLaboratorioServiceImpl implements ImagenLaboratorioService {
   private final Logger logger = LoggerFactory.getLogger(ImagenLaboratorioServiceImpl.class);
   private final Map<String, Object> colOrderNames = new HashMap<>();
   private ImagenLaboratorioRepository imagenLaboratorioRepository;
   private ImagenLaboratorioConverter imagenLaboratorioConverter;
   private ConsultaRepository consultaRepository;
   private EstudioLaboratorioRepository estudioLaboratorioRepository;

   {
      colOrderNames.put("consultaId", "consultaId");
      colOrderNames.put("fechaCreacion", "fechaCreacion");
      colOrderNames.put("idPaciente", "idPaciente");
   }

   @Autowired
   public void setImagenLaboratorioRepository(ImagenLaboratorioRepository imagenLaboratorioRepository) {
      this.imagenLaboratorioRepository = imagenLaboratorioRepository;
   }

   @Autowired
   public void setImagenLaboratorioConverter(ImagenLaboratorioConverter imagenLaboratorioConverter) {
      this.imagenLaboratorioConverter = imagenLaboratorioConverter;
   }

   @Autowired
   public void setConsultaRepository(ConsultaRepository consultaRepository) {
      this.consultaRepository = consultaRepository;
   }

   @Autowired
   public void setEstudioLaboratorioRepository(EstudioLaboratorioRepository estudioLaboratorioRepository) {
      this.estudioLaboratorioRepository = estudioLaboratorioRepository;
   }

   @Override
   //    public Page<ImagenLaboratorioListView> getImagenLaboratorioPage(Boolean active, String name, Integer page, Integer size, String orderColumn, String orderType, String idPaciente) throws ImagenLaboratorioException {
   public Page<ImagenLaboratorioListView> getImagenLaboratorioPage(Boolean active, String idPaciente, Long idConsulta, Integer page, Integer size, String orderColumn, String orderType) throws ImagenLaboratorioException {
      try {
         logger.info("- Obtener listado imagen laboratorio paginable: - active {} - idPaciente {} - idConsulta {} - page {} - size: {} - orderColumn: {} - orderType: {}",
            active, idPaciente, idConsulta, page, size, orderColumn, orderType);
         //            List<ImagenLaboratorioView> imagenLaboratorioViewList = new ArrayList<>();
         List<ImagenLaboratorioListView> imagenLaboratorioViewList = new ArrayList<>();
         Page<ImagenLaboratorio> imagenLaboratorioPage = null;
         Sort sort = new Sort(Sort.Direction.ASC, (String) colOrderNames.get("idPaciente"));

         if (orderColumn != null && orderType != null) {
            if (orderType.equalsIgnoreCase("asc")) {
               sort = new Sort(Sort.Direction.ASC, (String) colOrderNames.get(orderColumn));
            } else {
               sort = new Sort(Sort.Direction.DESC, (String) colOrderNames.get(orderColumn));
            }

         }
         PageRequest request = new PageRequest(page, size, sort);
         final String patternSearch = "%" + idPaciente.toLowerCase() + "%";

         Specifications<ImagenLaboratorio> spec = Specifications.where(
            (root, query, cb) -> {
               Predicate tc = null;
               if (idPaciente != null && !idPaciente.isEmpty()) {
                  tc = (tc != null ?
                     cb.and(tc, cb.like(cb.lower(root.get("idPaciente")), patternSearch)) :
                     cb.like(cb.lower(root.get("idPaciente")), patternSearch));
               }

               if (idConsulta != null && idConsulta != 0) {
                  tc = (tc != null ?
                     cb.and(tc, cb.equal(root.get("consulta"), idConsulta)) :
                     cb.equal(root.get("consulta"), idConsulta));
               }
               tc = (tc != null ? cb.and(tc, cb.equal(root.get("activo"), active)) : cb.equal(root.get("activo"), active));
               return tc;
            }
         );

         if (spec == null) {
            imagenLaboratorioPage = imagenLaboratorioRepository.findAll(request);

         } else {
            imagenLaboratorioPage = imagenLaboratorioRepository.findAll(spec, request);
         }

         imagenLaboratorioPage.getContent().forEach(imagenLaboratorio -> {
            // imagenLaboratorioViewList.add(imagenLaboratorioConverter.toViewPage(imagenLaboratorio));
            imagenLaboratorioViewList.add(imagenLaboratorioConverter.toView(imagenLaboratorio, Boolean.TRUE));
         });

         //            PageImpl<ImagenLaboratorioView> imagenLaboratorioViewPage = new PageImpl<ImagenLaboratorioView>(imagenLaboratorioViewList, request, imagenLaboratorioPage.getTotalElements());
         PageImpl<ImagenLaboratorioListView> imagenLaboratorioViewPage = new PageImpl<ImagenLaboratorioListView>(imagenLaboratorioViewList, request, imagenLaboratorioPage.getTotalElements());
         return imagenLaboratorioViewPage;
      } catch (Exception ex) {
         ImagenLaboratorioException ile = new ImagenLaboratorioException("Ocurrio un error al seleccionar lista Imagen Laboratorio paginable", ImagenLaboratorioException.LAYER_SERVICE, ImagenLaboratorioException.ACTION_SELECT);
         logger.error(ExceptionServiceCode.GROUP + "- Error al tratar de seleccionar lista Imagen Laboratorio paginable - CODE: {}-{}", ile.getExceptionCode(), ex);
         throw ile;
      }
   }

//    @Override
//	public void surbirArchivo(String idPaciente, ArchivoView archivoB64, String direccion) throws  ImagenLaboratorioException{
//		try {
//			String archivoBase64=archivoB64.getArchivoBase64();
//			String nomArchivo=null;
//			if(archivoB64.getNombreArchivo()!=null) {
//			 nomArchivo=  archivoB64.getNombreArchivo().trim();
//			}
//			String fotmatoarchivo = archivoBase64.substring(archivoBase64.indexOf('/') + 1, archivoBase64.indexOf(';'));
//			String temporal = archivoBase64.substring(archivoBase64.indexOf(',') + 1, archivoBase64.length() );
//
//			byte[] decodedBytes = Base64.getDecoder().decode(temporal.getBytes());
//			FileOutputStream out;
//			if(nomArchivo!=null) {
//				out = new FileOutputStream(direccion + "" + idPaciente + "_" + nomArchivo + "." + fotmatoarchivo);
//			}else {
//				out = new FileOutputStream(direccion + "" + idPaciente + "." + fotmatoarchivo);
//			}
//			out.write(decodedBytes);
//			out.close();
//		} catch (Exception ex) {
//			ImagenLaboratorioException ile = new ImagenLaboratorioException(
//					"Ocurrio un error al subir el archivo",
//					ImagenLaboratorioException.LAYER_SERVICE, ImagenLaboratorioException.ACTION_SELECT);
//			logger.error(
//					ExceptionServiceCode.GROUP
//							+ "- Ocurrio un error al subir el archivo - CODE: {}-{}",
//					ile.getExceptionCode(), ex);
//			throw ile;
//		}
//
//	}

   @Transactional(readOnly = false, rollbackFor = {ImagenLaboratorioException.class})
   @Override
   public void surbirArchivo(String idPaciente, ImagenLaboratorioView imgLabView, String direccion) throws ImagenLaboratorioException {
      try {
         logger.info("Creando imagen para el paciente:" + idPaciente);
         if (!consultaRepository.exists(imgLabView.getConsultaId())) {
            ImagenLaboratorioException esp = new ImagenLaboratorioException("No se encuentra en el sistema la consulta.", ImagenLaboratorioException.LAYER_DAO, ImagenLaboratorioException.ACTION_VALIDATE);
            esp.addError("consulta no encontrado");
            throw esp;
         }
         Consulta consulta = consultaRepository.findOne(imgLabView.getConsultaId());
         imgLabView.setIdPaciente(idPaciente);
         imgLabView.setDireccion(direccion);
         ImagenLaboratorio img = imagenLaboratorioConverter.toEntity(imgLabView, new ImagenLaboratorio(), Boolean.FALSE);
         img.setActivo(Boolean.TRUE);
         imagenLaboratorioRepository.save(img);
      } catch (Exception e) {
         ImagenLaboratorioException esp = new ImagenLaboratorioException("Ocurrio un error al Subir la imagen", ImagenLaboratorioException.LAYER_DAO, ImagenLaboratorioException.ACTION_VALIDATE);
         logger.info("surbirArchivo() - Ocurrio un error al subir la Imagen - {}", e);
         throw esp;
      }
   }


   @Transactional(readOnly = false, rollbackFor = {ImagenLaboratorioException.class})
   @Override
   public void createImagenLaboratorio(ImagenLaboratorioView imagenLaboratorioView) throws ImagenLaboratorioException {
      try {
         ImagenLaboratorio encontrado = imagenLaboratorioRepository.findByIdPacienteAndImageName(imagenLaboratorioView.getIdPaciente(), imagenLaboratorioView.getImageName());
         if (encontrado == null) {
            ImagenLaboratorio imagenLaboratorio = imagenLaboratorioConverter.toEntity(imagenLaboratorioView, new ImagenLaboratorio(), Boolean.FALSE);
            logger.debug("Insertar nueva Imagen laboratorio: {}", imagenLaboratorio);
            imagenLaboratorioRepository.save(imagenLaboratorio);
         } else {
            ImagenLaboratorioException imle = new ImagenLaboratorioException("======> Ya existe un archivo con ese nombre en la DB", ImagenLaboratorioException.LAYER_DAO, ImagenLaboratorioException.ACTION_INSERT);
            imle.addError("======> Ya existe un archivo con ese nombre en la DB");
            logger.error("======> Ya existe un archivo con ese nombre en la DB - CODE {} - {}", imle.getExceptionCode(), imagenLaboratorioView, imle);
            throw imle;
         }

      } catch (DataIntegrityViolationException dive) {
         ImagenLaboratorioException imle = new ImagenLaboratorioException("No fue posible agregar Imagen Laboratorio", ImagenLaboratorioException.LAYER_DAO, ImagenLaboratorioException.ACTION_INSERT);
         imle.addError("Ocurrio un error al agregar Imagen Laboratorio");
         logger.error("Error al insertar nuevo Imagen Laboratorio - CODE {} - {}", imle.getExceptionCode(), imagenLaboratorioView, imle);
         throw imle;
      }
//      catch (Exception ex) {
//         ImagenLaboratorioException imle = new ImagenLaboratorioException("Error inesperado al agregar Imagen Laboratorio", ImagenLaboratorioException.LAYER_DAO, ImagenLaboratorioException.ACTION_INSERT);
//         imle.addError("Ocurrió un error al agregar ImagenLaboratorio");
//         logger.error("Error al insertar nuevo Imagen Laboratorio - CODE {} - {}", imle.getExceptionCode(), imagenLaboratorioView, imle);
////         throw imle;
//      }
   }

   @Transactional(readOnly = false, rollbackFor = {ImagenLaboratorioException.class})
   @Override
   public void eliminarImagen(Long idImagenLaboratorio) throws ImagenLaboratorioException {
      try {
         ImagenLaboratorio imgLab = imagenLaboratorioRepository.findOne(idImagenLaboratorio);
         if (imgLab == null) {
            ImagenLaboratorioException imle = new ImagenLaboratorioException("No fue posible Eliminar la imagen", ImagenLaboratorioException.LAYER_DAO, ImagenLaboratorioException.ACTION_INSERT);
            imle.addError("La imagen no existe");
            throw imle;
         }
         imgLab.setActivo(Boolean.FALSE);
         logger.info("eliminarImagen() -  Eliminado lógico de imagen con el id:{}", idImagenLaboratorio);
         imagenLaboratorioRepository.save(imgLab);
      } catch (ImagenLaboratorioException imgLab) {
         throw imgLab;
      } catch (Exception e) {
         ImagenLaboratorioException imle = new ImagenLaboratorioException("No fue posible Eliminar la imagen", ImagenLaboratorioException.LAYER_DAO, ImagenLaboratorioException.ACTION_INSERT);
         logger.info("eliminarImagen() - No fue posible eliminar la imagen - {}", e);
         throw imle;
      }
   }

   @Override
   public List<ImagenLaboratorioListView> getImagenLaboratorioPorPadecimiento(Long idPadecimiento) throws ImagenLaboratorioException {
      try {
         logger.info("getImagenLaboratorioPorPadecimiento() -  Obteniendo imagenLaboratorio por idPadecimiento: {}", idPadecimiento);
         List<ImagenLaboratorio> imagenLaboratorio = imagenLaboratorioRepository.imagenLaboratorioByIdPadecimiento(idPadecimiento);
         List<ImagenLaboratorioListView> documentoList = new ArrayList<>();
         imagenLaboratorio.forEach(documento -> {
            ImagenLaboratorioListView doc = imagenLaboratorioConverter.toView(documento, true);
            if (doc != null) {
               documentoList.add(doc);
            }
         });
         if (documentoList.isEmpty()) {
            logger.info("getImagenLaboratorioPorPadecimiento() - No se encotraron imagen laboratorio relacionados al idPadecimiento: {}", idPadecimiento);
         }
         return documentoList;
      } catch (Exception e) {
         logger.info("getImagenLaboratorioPorPadecimiento - Ocurrio un error inesperdo al obtener imagen laboratorio por idPadecimiento: {} - error: {}", idPadecimiento, e);
         ImagenLaboratorioException de = new ImagenLaboratorioException("Ocurrio un error al obtener los imagen laboratorio", ImagenLaboratorioException.LAYER_DAO, ImagenLaboratorioException.ACTION_SELECT);
         throw de;
      }
   }


}
