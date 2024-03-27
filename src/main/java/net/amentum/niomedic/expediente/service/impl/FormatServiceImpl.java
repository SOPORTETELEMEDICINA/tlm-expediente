package net.amentum.niomedic.expediente.service.impl;


import lombok.extern.slf4j.Slf4j;
import net.amentum.niomedic.expediente.converter.FormatConverter;
import net.amentum.niomedic.expediente.exception.ExceptionServiceCode;
import net.amentum.niomedic.expediente.exception.FormatException;
import net.amentum.niomedic.expediente.model.Consulta;
import net.amentum.niomedic.expediente.model.Format;
import net.amentum.niomedic.expediente.model.FormatResult;
import net.amentum.niomedic.expediente.persistence.FormatRepository;
import net.amentum.niomedic.expediente.persistence.FormatResultRepository;
import net.amentum.niomedic.expediente.service.FormatService;
import net.amentum.niomedic.expediente.views.FormatResultListView;
import net.amentum.niomedic.expediente.views.FormatResultView;
import net.amentum.niomedic.expediente.views.FormatView;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * {@inheritDoc}
 *
 * @author by marellano on 14/06/17.
 */
@Service
@Transactional(readOnly = true)
@Slf4j
public class FormatServiceImpl implements FormatService {

//	private final Logger logger = LoggerFactory.getLogger(FormatServiceImpl.class);

   private FormatRepository formatRepository;

   private FormatConverter formatConverter;

   @Autowired
   public void setFormatRepository(FormatRepository formatRepository) {
      this.formatRepository = formatRepository;
   }

   private FormatResultRepository formatResultRepository;

   @Autowired
   public void setFormatResultRepository(FormatResultRepository formatResultRepository) {
      this.formatResultRepository = formatResultRepository;
   }


   @Autowired
   public void setFormatConverter(FormatConverter formatConverter) {
      this.formatConverter = formatConverter;
   }

   private final Map<String, Object> colOrderNames = new HashMap<>();

   {
      colOrderNames.put("idFormat", "idFormat");
      colOrderNames.put("title", "title");
      colOrderNames.put("version", "version");
      colOrderNames.put("active", "active");
      colOrderNames.put("categoryName", "categoryName");
   }

   /**
    * {@inheritDoc}
    */
   @Override
   @Transactional(readOnly = false, rollbackFor = {FormatException.class})
   public FormatView addFormat(FormatView formatView) throws FormatException {
      try {
         log.info(ExceptionServiceCode.FORMAT + " - Inicia proceso para guardar formato");
         log.debug(ExceptionServiceCode.FORMAT + " - Inicia proceso para guardar formato");

         Long version = null;
         //version = validations(formatView, version, Boolean.FALSE);
         log.info(ExceptionServiceCode.FORMAT + " - Se validó que no exista otro formato activo para la categoria");
         log.debug(ExceptionServiceCode.FORMAT + " - Se validó que no exista otro formato activo para la categoria");
         Format format = formatConverter.convertViewToEntity(formatView, false, version);
//         formatRepository.save(formatConverter.convertViewToEntity(formatView, false, version));
         formatRepository.save(format);

         log.info(ExceptionServiceCode.FORMAT + " - Se guardo exitosamente el formato.");
         log.debug(ExceptionServiceCode.FORMAT + " - Se guardo exitosamente el formato: {}", formatView);
         log.info(ExceptionServiceCode.FORMAT + " - Finaliza proceso para guardar formato");
         log.debug(ExceptionServiceCode.FORMAT + " - Finaliza proceso para guardar formato");

         return formatConverter.convertEntityToView(format, true);
//      } catch (FormatException e) {
//         throw e;
      } catch (Exception e) {
//         FormatException formatException = new FormatException("Error al insertar nuevo format", FormatException.LAYER_SERVICE, FormatException.ACTION_INSERT);
//         logger.error(ExceptionServiceCode.FORMAT + " - Error al insertar nuevo formato - formato: {} - CODE: {}", formatView, formatException.getExceptionCode(), e);
//         throw formatException;
         log.error("===>>>" + ExceptionServiceCode.FORMAT + " addFormat() - Error al insertar nuevo formato - formato: {}", formatView);
         throw new FormatException(HttpStatus.INTERNAL_SERVER_ERROR, String.format(FormatException.SERVER_ERROR, "Insertar"));
      }
   }

   /**
    * {@inheritDoc}
    */
   @Override
   @Transactional(readOnly = false, rollbackFor = {FormatException.class})
   public FormatView editFormat(FormatView formatView) throws FormatException {
      try {
         log.info(ExceptionServiceCode.FORMAT + " - Inicia proceso para modificar formato");
         log.debug(ExceptionServiceCode.FORMAT + " - Inicia proceso para modificar formato");
         if (!formatRepository.exists(formatView.getIdFormat())) {
//            FormatException formatException = new FormatException("El id format: " + formatView.getIdFormat() + " no existe. ", FormatException.LAYER_SERVICE, FormatException.ACTION_VALIDATE);
//            formatException.addError("El id format: " + formatView.getIdFormat() + "  no fue encontrado.");
//            throw formatException;
            log.error("===>>> editFormat() - El id format: {} no existe.", formatView.getIdFormat());
            throw new FormatException(HttpStatus.NOT_FOUND, String.format(FormatException.ITEM_NO_ENCONTRADO, formatView.getIdFormat()));
         }
         Long version = null;
         // verificar si se encuentra en uso
         Specification<FormatResult> spec = Specifications.where(
            (root, query, cb) -> {
               Join<FormatResult, Format> join = root.join("format");
               return cb.and(
                  cb.equal(join.get("idFormat"), formatView.getIdFormat())
               );
            }
         );
         Long formatCount = formatResultRepository.count(Specifications.where(spec));
         //logger.debug("FORMATcount: {}", formatCount);
         if (formatCount > 0) {
//            FormatException formatException = new FormatException(" - No es posible modificar el formato se encuentra en uso ", FormatException.LAYER_SERVICE, FormatException.ACTION_VALIDATE);
//            formatException.addError("-  No es posible modificar el formato se encuentra en uso ");
//            throw formatException;
            log.error("===>>> editFormat() - No es posible modificar el formato se encuentra en uso");
            throw new FormatException(HttpStatus.CONFLICT, "No es posible modificar el formato se encuentra en uso");
         }
         // verificar que no exista otra categoria activa
         log.info(ExceptionServiceCode.FORMAT + " - Se validó que no exista otro formato activo para la categoria");
         log.debug(ExceptionServiceCode.FORMAT + " - Se validó que no exista otro formato activo para la categoria");
         formatRepository.save(formatConverter.convertViewToEntity(formatView, true, null));

         log.info(ExceptionServiceCode.FORMAT + " - Se edito exitosamente el formato.");
         log.debug(ExceptionServiceCode.FORMAT + " - Se edito exitosamente el formato.", formatView);
         log.info(ExceptionServiceCode.FORMAT + " - Finaliza proceso para editar formato");
         log.debug(ExceptionServiceCode.FORMAT + " - Finaliza proceso para editar formato");

         return formatView;

      } catch (FormatException e) {
         throw e;
      } catch (Exception e) {
//         FormatException formatException = new FormatException("Error al editar formato", FormatException.LAYER_SERVICE, FormatException.ACTION_INSERT);
//         logger.error(ExceptionServiceCode.FORMAT + " - Error al editar formato - formato: {} - CODE: {}", formatView, formatException.getExceptionCode(), e);
//         throw formatException;
         log.error("===>>>" + ExceptionServiceCode.FORMAT + " editFormat() - Error al editar formato - formato: {}", formatView);
         throw new FormatException(HttpStatus.INTERNAL_SERVER_ERROR, String.format(FormatException.SERVER_ERROR, "Editar"));
      }
   }

   /**
    * {@inheritDoc}
    */
   @Override
   @Transactional(readOnly = false, rollbackFor = {FormatException.class})
   public void deleteFormat(Long idFormat) throws FormatException {
      try {
         log.info(ExceptionServiceCode.FORMAT + " - Inicia proceso para eliminar formato");
         log.debug(ExceptionServiceCode.FORMAT + " - Inicia proceso para eliminar formato");

         if (!formatRepository.exists(idFormat)) {
//            FormatException formatException = new FormatException("El id format: " + idFormat + " no existe. ", FormatException.LAYER_SERVICE, FormatException.ACTION_VALIDATE);
//            formatException.addError("El id format: " + idFormat + "  no fue encontrado.");
//            throw formatException;
            log.error("===>>> editFormat() - El id format: {} no existe.", idFormat);
            throw new FormatException(HttpStatus.NOT_FOUND, String.format(FormatException.ITEM_NO_ENCONTRADO, idFormat));
         }
         // verificar si se encuentra en uso
         Specification<FormatResult> spec = Specifications.where(
            (root, query, cb) -> {
               Join<FormatResult, Format> join = root.join("format");
               return cb.and(
                  cb.equal(join.get("idFormat"), idFormat)
               );
            }
         );

         Long formatCount = formatResultRepository.count(Specifications.where(spec));

         if (formatCount > 0) {
//            FormatException formatException = new FormatException(" - No es posible eliminar el formato se encuentra en uso ", FormatException.LAYER_SERVICE, FormatException.ACTION_VALIDATE);
//            formatException.addError("-  No es posible eliminar el formato se encuentra en uso ");
//            throw formatException;
            log.error("===>>> editFormat() - No es posible modificar el formato se encuentra en uso");
            throw new FormatException(HttpStatus.CONFLICT, "No es posible modificar el formato se encuentra en uso");
         }

         log.info(ExceptionServiceCode.FORMAT + " - Se verificó que no se encuentre en uso el formato.");
         log.debug(ExceptionServiceCode.FORMAT + " - Se verificó que no se encuentre en uso el formato");

         formatRepository.delete(idFormat);

         log.info(ExceptionServiceCode.FORMAT + " - Se eliminó exitosamente el formato.");
         log.debug(ExceptionServiceCode.FORMAT + " - Se eliminó exitosamente el formato.");
         log.info(ExceptionServiceCode.FORMAT + " - Finaliza proceso para eliminar formato");
         log.debug(ExceptionServiceCode.FORMAT + " - Finaliza proceso para eliminar formato");

      } catch (FormatException e) {
         throw e;
      } catch (ConstraintViolationException e) {
//         FormatException formatException = new FormatException("No es posible eliminar el formato.", FormatException.LAYER_DAO, FormatException.ACTION_DELETE);
//         formatException.addError("El formato: " + idFormat + ", cuenta con referencias.");
//         logger.error(ExceptionServiceCode.FORMAT + " - No es posible eliminar el formato: - idFormat: {} - CODE: {}", idFormat, formatException.getExceptionCode(), e);
//         throw formatException;
         log.error("===>>>" + ExceptionServiceCode.FORMAT + " deleteFormat() - No es posible eliminar el formato: {}, cuenta con referencias", idFormat);
         throw new FormatException(HttpStatus.CONFLICT, String.format("No es posible eliminar el formato: %s, cuenta con referencias", idFormat));
      } catch (DataIntegrityViolationException e) {
//         FormatException formatException = new FormatException("No es posible eliminar el formato.", FormatException.LAYER_DAO, FormatException.ACTION_DELETE);
//         formatException.addError("Error al eliminar formato id: " + idFormat);
//         logger.error(ExceptionServiceCode.FORMAT + " - No es posible eliminar el formato: - idFormat: {} - CODE: {}", idFormat, formatException.getExceptionCode(), e);
//         throw formatException;
         log.error("===>>>" + ExceptionServiceCode.FORMAT + " deleteFormat() - No es posible eliminar el formato: - idFormat: {}", idFormat);
         throw new FormatException(HttpStatus.CONFLICT, String.format("No es posible eliminar el formato: %s", idFormat));
      } catch (Exception e) {
//         FormatException formatException = new FormatException("Error al eliminar el formato", FormatException.LAYER_DAO, FormatException.ACTION_UPDATE);
//         logger.error(ExceptionServiceCode.FORMAT + " - No es posible eliminar el formato: - idFormat: {} - CODE: {}", idFormat, formatException.getExceptionCode(), e);
//         throw formatException;
         log.error("===>>>" + ExceptionServiceCode.FORMAT + " deleteFormat() - No es posible eliminar el formato: - idFormat: {}", idFormat);
         throw new FormatException(HttpStatus.INTERNAL_SERVER_ERROR, String.format(FormatException.SERVER_ERROR, "Eliminar"));
      }
   }

   /**
    * {@inheritDoc}
    */
   @Override
   @Transactional(readOnly = false, rollbackFor = {FormatException.class})
   public void updateStatusFormat(Long idFormat) throws FormatException {
      try {
         log.info(ExceptionServiceCode.FORMAT + " - Inicia proceso para modificar estatus de  formato");
         log.debug(ExceptionServiceCode.FORMAT + " - Inicia proceso para modificar estatus de  formato");
         if (!formatRepository.exists(idFormat)) {
//            FormatException formatException = new FormatException("El id format: " + idFormat + " no existe. ", FormatException.LAYER_SERVICE, FormatException.ACTION_VALIDATE);
//            formatException.addError("El id format: " + idFormat + "  no fue encontrado.");
//            throw formatException;
            log.error("===>>> updateStatusFormat() - El id format: {} no existe.", idFormat);
            throw new FormatException(HttpStatus.NOT_FOUND, String.format(FormatException.ITEM_NO_ENCONTRADO, idFormat));
         }
         Long version = null;
         Format format = formatRepository.findOne(idFormat);
         log.debug("Update status to format: {}", format);
         if (format.getActive() == Boolean.TRUE) {
            format.setActive(Boolean.FALSE);
         } else {
            format.setActive(Boolean.TRUE);
         }
         formatRepository.save(format);

         log.info(ExceptionServiceCode.FORMAT + " - Se modificó el estatus exitosamente del formato {}", format.toStringResume());
         log.debug(ExceptionServiceCode.FORMAT + " - Se modificó el estatus exitosamente del formato: {}" + format);
         log.info(ExceptionServiceCode.FORMAT + " - Finaliza proceso para modificar el estatus del formato");
         log.debug(ExceptionServiceCode.FORMAT + " - Finaliza proceso para modificar el estatus del formato");

      } catch (FormatException e) {
         throw e;
      } catch (Exception e) {
//         FormatException formatException = new FormatException("Error al editar el estatus del formato id:{}" + idFormat, FormatException.LAYER_SERVICE, FormatException.ACTION_INSERT);
//         logger.error(ExceptionServiceCode.FORMAT + " - Error al editar formato - idformato: {} - CODE: {}", idFormat, formatException.getExceptionCode(), e);
//         throw formatException;
         log.error("===>>>" + ExceptionServiceCode.FORMAT + " updateStatusFormat() - Error al editar formato - idformato: {}", idFormat);
         throw new FormatException(HttpStatus.INTERNAL_SERVER_ERROR, String.format(FormatException.SERVER_ERROR, "Editar estatus"));
      }
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public FormatView getDetailsFormat(Long idFormat) throws FormatException {
      try {
         log.info(ExceptionServiceCode.FORMAT + " - Inicia proceso para obtener detalles del formato");
         log.debug(ExceptionServiceCode.FORMAT + " - Inicia proceso para obtener detalles del formato");
         if (!formatRepository.exists(idFormat)) {
//            FormatException formatException = new FormatException("El id format: " + idFormat + " no existe. ", FormatException.LAYER_SERVICE, FormatException.ACTION_VALIDATE);
//            formatException.addError("El id estatus: " + idFormat + "  no fue encontrado.");
//            throw formatException;
            log.error("===>>> updateStatusFormat() - El id format: {} no existe.", idFormat);
            throw new FormatException(HttpStatus.NOT_FOUND, String.format(FormatException.ITEM_NO_ENCONTRADO, idFormat));
         }
         Format f = formatRepository.findOne(idFormat);

         log.info(ExceptionServiceCode.FORMAT + " - Se obtuvo exitosamente los detalles del formato: " + f.toStringResume());
         log.debug(ExceptionServiceCode.FORMAT + " - Se obtuvo exitosamente los detalles del formato: " + f);
         log.info(ExceptionServiceCode.FORMAT + " - Finaliza proceso para obtener detalles del formato.");
         log.debug(ExceptionServiceCode.FORMAT + " - Finaliza proceso para obtener detalles del formato.");

         return formatConverter.convertEntityToView(f, Boolean.TRUE);
      } catch (FormatException e) {
         throw e;
      } catch (Exception e) {
//         FormatException formatException = new FormatException("Error al obtener detalles de formato", FormatException.LAYER_SERVICE, FormatException.ACTION_INSERT);
//         logger.error(ExceptionServiceCode.FORMAT + " - Error al obtener detalles de formato - idFormat: {} - CODE: {}", idFormat, formatException.getExceptionCode(), e);
//         throw formatException;
         log.error("===>>>" + ExceptionServiceCode.FORMAT + " getDetailsFormat() - Error al obtener detalles de formato - idFormat: {}", idFormat);
         throw new FormatException(HttpStatus.INTERNAL_SERVER_ERROR, String.format(FormatException.SERVER_ERROR, "Obtener detalles"));

      }
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public List<FormatView> getAllFormats() throws FormatException {
      try {
         log.info(ExceptionServiceCode.FORMAT + " - Inicia proceso para obtener formatos");
         log.debug(ExceptionServiceCode.FORMAT + " - Inicia proceso para obtener formatos");
         List<FormatView> formatView = new ArrayList<>();
         List<Format> formatList = new ArrayList<>();
         formatRepository.findAll().stream().forEach(format -> {
            formatView.add(formatConverter.convertEntityToView(format, Boolean.TRUE));
         });

         log.info(ExceptionServiceCode.FORMAT + " - Se obtuvo {} formatos. " + formatView.size());
         log.debug(ExceptionServiceCode.FORMAT + " - Se obtuvo los siguientes formatos: " + formatView);
         log.info(ExceptionServiceCode.FORMAT + " - Finaliza proceso para obtener todos los formatos.");
         log.debug(ExceptionServiceCode.FORMAT + " - Finaliza proceso para obtener todos los formatos.");

         return formatView;
      } catch (Exception e) {
//         FormatException formatException = new FormatException("Ocurrio un error al seleccionar la lista de formatos.", FormatException.LAYER_DAO, FormatException.ACTION_SELECT);
//         logger.error(ExceptionServiceCode.FORMAT + " - No es posible seleccionar la lista de formatos - CODE: {}", formatException.getExceptionCode(), e);
//         throw formatException;
         log.error("===>>>" + ExceptionServiceCode.FORMAT + " getAllFormats() - Error al obtener la lista de formatos");
         throw new FormatException(HttpStatus.INTERNAL_SERVER_ERROR, String.format(FormatException.SERVER_ERROR, "Obtener Todos"));
      }
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public Page<FormatView> getFormatsPage(String title, Integer page, Integer size, String orderColumn, String orderType,
                                          Boolean active, Integer version, Boolean general, String search) throws FormatException {
      try {
         log.info(ExceptionServiceCode.FORMAT + " - Inicia proceso para obtener formatos de forma paginable");
         log.debug(ExceptionServiceCode.FORMAT + " - Inicia proceso para obtener formatos de forma paginable");
         List<FormatView> formatViews = new ArrayList<>();
         Page<Format> formatPage = null;

         Sort sort = new Sort(Sort.Direction.ASC, (String) colOrderNames.get("idFormat"));
         if (orderColumn != null && !orderColumn.isEmpty() && orderType != null && !orderType.isEmpty() && !orderColumn.equals("categoryName")) {
            if (orderType.equalsIgnoreCase("asc")) {
               sort = new Sort(Sort.Direction.ASC, (String) colOrderNames.get(orderColumn));
            } else {
               sort = new Sort(Sort.Direction.DESC, (String) colOrderNames.get(orderColumn));
            }
         }

         final String patternSearch = "%" + search.toLowerCase() + "%";

         PageRequest request = new PageRequest(page, size, sort);

         Specifications<Format> spec = Specifications.where(
            (root, query, cb) -> {
               Predicate tc = null;
               if (version != null)
                  tc = cb.equal(root.get("version"), version);
               if (active != null)
//                  tc = cb.equal(root.get("active"), active);
                  tc = (tc != null ? cb.and(tc, cb.equal(root.get("active"), active)) : cb.equal(root.get("active"), active));
               if (general) {
                  return cb.or(cb.like(cb.lower(root.get("title")), patternSearch));
               } else {
                  if (title != null && !title.isEmpty())
                     tc = (tc != null ? cb.and(tc, cb.equal(root.get("title"), patternSearch)) : cb.equal(root.get("title"), patternSearch));
               }
               return tc;
            }
         );


         if (spec == null) {
            formatPage = formatRepository.findAll(request);
         } else {
            formatPage = formatRepository.findAll(spec, request);
         }
         // llenar vista con los campos obtenidos de la bd
         formatPage.getContent().forEach(format -> {

            formatViews.add(formatConverter.convertEntityToView(format, Boolean.TRUE));
         });
         if (orderColumn != null && !orderColumn.isEmpty() && orderType != null && !orderType.isEmpty() && orderColumn.equals("categoryName")) {
            //ordenamiento
            if (orderType.equalsIgnoreCase("asc")) {
               formatViews.sort(Comparator.comparing(FormatView::getCategoryName).reversed());
            } else {
               formatViews.sort(Comparator.comparing(FormatView::getCategoryName));
            }
         }
         PageImpl<FormatView> pageStatus = new PageImpl<FormatView>(formatViews, request, formatPage.getTotalElements());

         log.info(ExceptionServiceCode.FORMAT + " - Se obtuvo {} formatos. " + pageStatus.getTotalElements());
         log.debug(ExceptionServiceCode.FORMAT + " - Se obtuvo los siguientes formatos de forma paginable: " + pageStatus);
         log.info(ExceptionServiceCode.FORMAT + " - Finaliza proceso para obtener formatos de forma paginable.");
         log.debug(ExceptionServiceCode.FORMAT + " - Finaliza proceso para obtener formatos de forma paginable.");

         return pageStatus;
      } catch (Exception e) {
//         FormatException formatException = new FormatException("Ocurrio un error al seleccionar formatos.", FormatException.LAYER_DAO, FormatException.ACTION_SELECT);
//         logger.error(ExceptionServiceCode.FORMAT + " - No es posible seleccionar los formatos - CODE: {}", formatException.getExceptionCode(), e);
//         throw formatException;
         log.error("===>>>" + ExceptionServiceCode.FORMAT + " getFormatsPage() - No es posible seleccionar los formatos");
         throw new FormatException(HttpStatus.INTERNAL_SERVER_ERROR, String.format(FormatException.SERVER_ERROR, "Obtener Page"));
      }
   }

   /**
    * {@inheritDoc}
    */
   @Override
   @Transactional(readOnly = false, rollbackFor = {FormatException.class})
//   public void addFormatResult(FormatResultView formatResultView) throws FormatException {
   public FormatResultView addFormatResult(FormatResultView formatResultView) throws FormatException {
      try {
         log.info(ExceptionServiceCode.FORMAT + " - Inicia proceso para guardar el resultado de un formato.");
         log.debug(ExceptionServiceCode.FORMAT + " - Inicia proceso para guardar el resultado de un formato.");
         List<Long> idTask = new ArrayList<>();
         if (!formatRepository.exists(formatResultView.getFormatView().getIdFormat())) {
//            FormatException formatException = new FormatException("El formato con id: " + formatResultView.getFormatView().getIdFormat() + " no existe.", FormatException.LAYER_SERVICE, FormatException.ACTION_VALIDATE);
//            formatException.addError("Formato no encontrado.");
//            throw formatException;
            log.error("===>>> addFormatResult() - El id format: {}  no existe.", formatResultView.getFormatView().getIdFormat());
            throw new FormatException(HttpStatus.NOT_FOUND, String.format(FormatException.ITEM_NO_ENCONTRADO, formatResultView.getFormatView().getIdFormat()));
         }


         Timestamp createdDate = new Timestamp(System.currentTimeMillis());
         if (formatResultView.getCreatedDate() == null) {
            formatResultView.setCreatedDate(createdDate);
         }
         FormatResult formatResult = formatConverter.convertFormatResultViewToEntity(formatResultView);
         formatResultRepository.save(formatResult);

         log.info(ExceptionServiceCode.FORMAT + " - Se guardó el resulado de formato exitosamente.");
         log.debug(ExceptionServiceCode.FORMAT + " - Se guardó el resulado de formato exitosamente: {}", formatResult);
         log.info(ExceptionServiceCode.FORMAT + " - Finaliza proceso para guardar el resultado de un formato..");
         log.debug(ExceptionServiceCode.FORMAT + " - Finaliza proceso para guardar el resultado de un formato..");

         return formatConverter.convertFormatResultToView(formatResult, true);

      } catch (FormatException e) {
         throw e;
      } catch (Exception e) {
//         FormatException formatException = new FormatException("Error al insertar el resultado del formato", FormatException.LAYER_SERVICE, FormatException.ACTION_INSERT);
//         logger.error(ExceptionServiceCode.FORMAT + " - Error al insertar el resultado del formato - formatResult: {} - CODE: {}", formatResultView, formatException.getExceptionCode(), e);
//         throw formatException;
         log.error("===>>>" + ExceptionServiceCode.FORMAT + " addFormatResult() - Error al insertar el resultado del formato - formatResult: {}", formatResultView);
         throw new FormatException(HttpStatus.INTERNAL_SERVER_ERROR, String.format(FormatException.SERVER_ERROR, "Insertar el resultado"));
      }
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public List<FormatResultView> getFormatResult(Long idConsulta, Boolean active) throws FormatException {
      try {
         log.info(ExceptionServiceCode.FORMAT + " - Inicia proceso para obtener resultado de formato ya sea por ticket o tarea.");
         log.debug(ExceptionServiceCode.FORMAT + " - Inicia proceso para obtener resultado de formato ya sea por ticket o tarea.");

         List<FormatResultView> formatResultViewList = new ArrayList<>();
         List<FormatResult> formatResults = new ArrayList<>();
         Specifications<FormatResult> spec = Specifications.where(
            (root, query, cb) -> {
               Predicate tc = null;
               Join<FormatResult, Consulta> consultaJoin = root.join("consulta", JoinType.LEFT);

               Join<FormatResult, Format> formatJoin = root.join("format", JoinType.LEFT);
               if (idConsulta != null) {
                  tc = (tc != null ? cb.and(tc, cb.equal(consultaJoin.get("idConsulta"), idConsulta)) : cb.equal(consultaJoin.get("idConsulta"), idConsulta));
               }
               if (active != null) {
                  tc = (tc != null ? cb.and(tc, cb.equal(formatJoin.get("active"), active)) : cb.equal(formatJoin.get("active"), active));
               }
               return tc;
            }
         );

         formatResults = formatResultRepository.findAll(spec);
         formatResults.forEach(formatResult -> {
            formatResultViewList.add(formatConverter.convertFormatResultToView(formatResult, Boolean.TRUE));
         });

         log.info(ExceptionServiceCode.FORMAT + " - Se obtuvo {} resultados de formatos. " + formatResultViewList.size());
         log.debug(ExceptionServiceCode.FORMAT + " - Se obtuvo los siguientes resultados de formatos: " + formatResultViewList);
         log.info(ExceptionServiceCode.FORMAT + " - Finaliza proceso para obtener resultado de formato ya sea por ticket o tarea.");
         log.debug(ExceptionServiceCode.FORMAT + " - Finaliza proceso para obtener resultado de formato ya sea por ticket o tarea.");

         return formatResultViewList;
      } catch (Exception e) {
//         FormatException formatException = new FormatException("Error al obtener los resultados de formato", FormatException.LAYER_SERVICE, FormatException.ACTION_SELECT);
//         logger.error(ExceptionServiceCode.FORMAT + " - Error al obtener los resultados de formato - idConsulta: {} - CODE: {}", idConsulta, formatException.getExceptionCode(), e);
//         throw formatException;
         log.error("===>>>" + ExceptionServiceCode.FORMAT + " getFormatResult() - Error al obtener los resultados de formato - idConsulta: {}", idConsulta);
         throw new FormatException(HttpStatus.INTERNAL_SERVER_ERROR, String.format(FormatException.SERVER_ERROR, "Obtener los resultados"));
      }
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public FormatResultView getFormatResultDetails(Long idFormatResult) throws FormatException {
      try {
         log.info(ExceptionServiceCode.FORMAT + " - Inicia proceso para obtener detalles del resultado de un formato.");
         log.debug(ExceptionServiceCode.FORMAT + " - Inicia proceso para obtener detalles del resultado de un formato.");
         if (!formatResultRepository.exists(idFormatResult)) {
//            FormatException formatException = new FormatException("El resultado de formato con id: " + idFormatResult + " no existe.", FormatException.LAYER_SERVICE, FormatException.ACTION_VALIDATE);
//            formatException.addError("Resultado de formato no encontrado");
//            throw formatException;
            log.error("===>>> addFormatResult() - El resultado de formato con id: {} no existe.", idFormatResult);
            throw new FormatException(HttpStatus.NOT_FOUND, String.format(FormatException.ITEM_NO_ENCONTRADO, idFormatResult));
         }

         FormatResultView resultView = formatConverter.convertFormatResultToView(formatResultRepository.findOne(idFormatResult), Boolean.TRUE);

         log.info(ExceptionServiceCode.FORMAT + " - Se obtuvo exitosamente los detalles del resultado de formato: " + resultView.toStringResume());
         log.debug(ExceptionServiceCode.FORMAT + " - Se obtuvo exitosamente los detalles del resultado de formato: " + resultView);
         log.info(ExceptionServiceCode.FORMAT + " - Finaliza proceso para obtener detalles del resultado de formato.");
         log.debug(ExceptionServiceCode.FORMAT + " - Finaliza proceso para obtener detalles del resultado de formato.");

         return resultView;
      } catch (FormatException e) {
         throw e;
      } catch (Exception e) {
//         FormatException formatException = new FormatException("Error al obtener los resultados de formato", FormatException.LAYER_SERVICE, FormatException.ACTION_SELECT);
//         log.error(ExceptionServiceCode.FORMAT + " - Error al obtener los resultados de formato - idFormatResult: {} - CODE: {}", idFormatResult, formatException.getExceptionCode(), e);
//         throw formatException;
         log.error("===>>>" + ExceptionServiceCode.FORMAT + " getFormatResultDetails() - Error al obtener los resultados de formato - idFormatResult: {}", idFormatResult);
         throw new FormatException(HttpStatus.INTERNAL_SERVER_ERROR, String.format(FormatException.SERVER_ERROR, "Obtener los detalles de resultados"));

      }
   }

   /**
    * {@inheritDoc}
    */
	/*@Override
	public List<FormatView> getFormatByCategory(Long idTaskCategory, Long idTicketCategory) throws FormatException {
		try{
			/*logger.info(ExceptionServiceCode.FORMAT+" - Inicia proceso para obtener formatos de acuerdo a la categoria.");
			logger.debug(ExceptionServiceCode.FORMAT+" - Inicia proceso para obtener formatos de acuerdo a la categoria.");
			if(idTaskCategory != null ){
				if(!taskCategoryRepository.exists(idTaskCategory)) {
					FormatException formatException = new FormatException("La categoria de la tarea con id: "+idTaskCategory+" no existe.", FormatException.LAYER_SERVICE, FormatException.ACTION_VALIDATE);
					formatException.addError("Categoria de tarea no encontrada.");
					throw formatException;
				}
			}
			if(idTicketCategory != null ){
				if(!ticketCategoryRepository.exists(idTicketCategory)) {
					FormatException formatException = new FormatException("La categoria del ticket con id: "+idTicketCategory+" no existe.", FormatException.LAYER_SERVICE, FormatException.ACTION_VALIDATE);
					formatException.addError("Categoria del Ticket no encontrada.");
					throw formatException;
				}
			}
			List<FormatView> formatViewList = new ArrayList<>();
			List<Format> formats = new ArrayList<>();

			Specifications<Format> spec = Specifications.where(
					(root, query, cb) -> {
						Predicate tc=null;
						Join<Format, TicketCategory> ticketCategoryJoin = root.join("ticketCategory", JoinType.LEFT);
						Join<Format, TaskCategory> taskCategoryJoin = root.join("taskCategory", JoinType.LEFT);
						if(idTaskCategory != null){
							tc = (tc != null ? cb.and(tc, cb.equal(taskCategoryJoin.get("idTaskCategory"), idTaskCategory)) : cb.equal(taskCategoryJoin.get("idTaskCategory"), idTaskCategory));
							tc = (tc != null ? cb.and(tc, cb.equal(root.get("active"), Boolean.TRUE)) : cb.equal(root.get("active"), Boolean.TRUE));
						}else if (idTicketCategory != null){
							tc = (tc != null ? cb.and(tc, cb.equal(ticketCategoryJoin.get("ticketCategoryId"), idTicketCategory)) : cb.equal(ticketCategoryJoin.get("ticketCategoryId"), idTicketCategory));
							tc = (tc != null ? cb.and(tc, cb.equal(root.get("active"), Boolean.TRUE)) : cb.equal(root.get("active"), Boolean.TRUE));
						}
						return tc;
					}
					);

			formats = formatRepository.findAll(spec);
			//logger.info("fr re "+formats);
			formats.forEach(format -> {
				formatViewList.add(formatConverter.convertEntityToView(format, Boolean.FALSE));
			});

			logger.info(ExceptionServiceCode.FORMAT+" - Se obtuvo {} formatos de acuerdo a la categoria. "+ formatViewList.size());
			logger.debug(ExceptionServiceCode.FORMAT+" - Se obtuvo los siguientes formatos de acuerdo a la categoria: "+formatViewList);
			logger.info(ExceptionServiceCode.FORMAT+" - Finaliza proceso para obtener formatos de acuerdo a la categoria de ticket o tarea.");
			logger.debug(ExceptionServiceCode.FORMAT+" - Finaliza proceso para obtener formatos de acuerdo a la categoria de ticket o tarea.");

			return formatViewList;
			return null;
		//}catch(FormatException e){
			//throw e;
		}catch(Exception e){
			FormatException formatException = new FormatException("Error al obtener los formatos por categoria", FormatException.LAYER_SERVICE, FormatException.ACTION_SELECT);
			logger.error(ExceptionServiceCode.FORMAT+ " - Error al obtener los formatos por categoria - idTaskCategory: {} - idTicketCategory: {} - CODE: {}", idTaskCategory, idTicketCategory, formatException.getExceptionCode(), e);
			throw formatException;
		}
	}*/
   @Override
   public List<FormatResultListView> getFormatResultListView(Long idConsulta) throws FormatException {
      try {
         Specification<FormatResult> spec = Specifications.where(
            (root, query, cb) -> {
               Predicate tc = null;
               Join<FormatResult, Consulta> ConsultaJoin = root.join("consulta", JoinType.LEFT);
               if (idConsulta != null) {
                  tc = cb.equal(ConsultaJoin.get("idConsulta"), idConsulta);
               }

               return tc;
            }
         );
         log.info(ExceptionServiceCode.FORMAT + " - getFormatResultListView () - Obteniendo listado de respuestas, filtrado por idConsulta:{} ", idConsulta);
         log.debug(ExceptionServiceCode.FORMAT + " - getFormatResultListView () - Obteniendo listado de respuestas, filtrado por idConsulta:{} ", idConsulta);
         List<FormatResultListView> resultList = new ArrayList<FormatResultListView>();
         List<FormatResult> formatResult = formatResultRepository.findAll(spec);
         formatResult.forEach(result -> {
            resultList.add(formatConverter.entityToViewResultList(result, new FormatResultListView()));
         });
         log.info(ExceptionServiceCode.FORMAT + " - getFormatResultListView () - finalizando conversión de entidad a vista");
         log.debug(ExceptionServiceCode.FORMAT + " - getFormatResultListView () - finalizando conversión de entidad a vista");

         return resultList;
      } catch (Exception e) {
//         FormatException formatException = new FormatException("Error al obtener los resultados de un formato ", FormatException.LAYER_SERVICE, FormatException.ACTION_SELECT);
//         logger.error(ExceptionServiceCode.FORMAT + " - Error al obtener los resultados de un formato - idConsulta: {} - CODE: {}", idConsulta, formatException.getExceptionCode(), e);
//         throw formatException;
         log.error("===>>>" + ExceptionServiceCode.FORMAT + " getFormatResultListView() - Error al obtener los resultados de un formato - idConsulta: {}", idConsulta);
         throw new FormatException(HttpStatus.INTERNAL_SERVER_ERROR, String.format(FormatException.SERVER_ERROR, "Obtener los resultados por idConsulta"));

      }
   }

}
