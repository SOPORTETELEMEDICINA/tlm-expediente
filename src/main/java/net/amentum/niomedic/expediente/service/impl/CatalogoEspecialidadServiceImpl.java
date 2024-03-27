package net.amentum.niomedic.expediente.service.impl;

import net.amentum.niomedic.expediente.converter.CatalogoEspecialidadConverter;
import net.amentum.niomedic.expediente.exception.CatalogoEspecialidadException;
import net.amentum.niomedic.expediente.exception.ExceptionServiceCode;
import net.amentum.niomedic.expediente.model.CatalogoEspecialidad;
import net.amentum.niomedic.expediente.persistence.CatalogoEspecialidadRepository;
import net.amentum.niomedic.expediente.service.CatalogoEspecialidadService;
import net.amentum.niomedic.expediente.views.CatalogoEspecialidadView;
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
import java.util.stream.Collectors;


@Service
@Transactional(readOnly = true)
public class CatalogoEspecialidadServiceImpl implements CatalogoEspecialidadService {
   private final Logger logger = LoggerFactory.getLogger(CatalogoEspecialidadServiceImpl.class);
   private final Map<String, Object> colOrderNames = new HashMap<>();
   private CatalogoEspecialidadRepository catalogoEspecialidadRepository;
   private CatalogoEspecialidadConverter catalogoEspecialidadConverter;

   {
      colOrderNames.put("claveEspecialidad", "claveEspecialidad");
      colOrderNames.put("descripcionEspecialidad", "descripcionEspecialidad");
   }

   @Autowired
   public void setCatalogoEspecialidadRepository(CatalogoEspecialidadRepository catalogoEspecialidadRepository) {
      this.catalogoEspecialidadRepository = catalogoEspecialidadRepository;
   }

   @Autowired
   public void setCatalogoEspecialidadConverter(CatalogoEspecialidadConverter catalogoEspecialidadConverter) {
      this.catalogoEspecialidadConverter = catalogoEspecialidadConverter;
   }

   @Transactional(readOnly = false, rollbackFor = {CatalogoEspecialidadException.class})
   @Override
   public void createCatalogoEspecialidad(CatalogoEspecialidadView catalogoEspecialidadView) throws CatalogoEspecialidadException {
      try {
         CatalogoEspecialidad catalogoEspecialidad = catalogoEspecialidadConverter.toEntity(catalogoEspecialidadView, new CatalogoEspecialidad(), Boolean.FALSE);
         logger.debug("===>>>Insertar nuevo Catálogo de Especialidad: {}", catalogoEspecialidad);
         catalogoEspecialidadRepository.save(catalogoEspecialidad);
      } catch (DataIntegrityViolationException dive) {
         CatalogoEspecialidadException cee = new CatalogoEspecialidadException("No fue posible agregar al Catálogo de Especialidad", CatalogoEspecialidadException.LAYER_DAO, CatalogoEspecialidadException.ACTION_INSERT);
         cee.addError("Ocurrio un error al agregar el Catálogo de Especialidad");
         logger.error("===>>>Error al insertar nuevo Catálogo de Especialidad - CODE {} - {}", cee.getExceptionCode(), catalogoEspecialidadView, cee);
         throw cee;
      } catch (Exception ex) {
         CatalogoEspecialidadException cee = new CatalogoEspecialidadException("Error inesperado al agregar el Catálogo de Especialidad", CatalogoEspecialidadException.LAYER_DAO, CatalogoEspecialidadException.ACTION_INSERT);
         cee.addError("Ocurrió un error al agregar el Catálogo de Especialidad");
         logger.error("===>>>Error al insertar nuevo Catálogo de Especialidad - CODE {} - {}", cee.getExceptionCode(), catalogoEspecialidadView, cee);
         throw cee;
      }
   }

   @Transactional(readOnly = false, rollbackFor = {CatalogoEspecialidadException.class})
   @Override
   public void updateCatalogoEspecialidad(CatalogoEspecialidadView catalogoEspecialidadView) throws CatalogoEspecialidadException {
      try {
         if (!catalogoEspecialidadRepository.exists(catalogoEspecialidadView.getIdCatalogoEspecialidad())) {
            logger.error("===>>>No se encuentra el idCatalogoEspecialidad");
            CatalogoEspecialidadException cee = new CatalogoEspecialidadException("No se encuentra en el sistema el Catálogo Especialidad.", CatalogoEspecialidadException.LAYER_DAO, CatalogoEspecialidadException.ACTION_VALIDATE);
            cee.addError("Catálogo de Especialidad no encontrado");
            throw cee;
         }
         CatalogoEspecialidad catalogoEspecialidad = catalogoEspecialidadRepository.findOne(catalogoEspecialidadView.getIdCatalogoEspecialidad());
         catalogoEspecialidad = catalogoEspecialidadConverter.toEntity(catalogoEspecialidadView, catalogoEspecialidad, Boolean.TRUE);
         logger.debug("===>>>Editar Catálogo de Especialidad: {}", catalogoEspecialidad);
         catalogoEspecialidadRepository.save(catalogoEspecialidad);
      } catch (CatalogoEspecialidadException cee) {
         throw cee;
      } catch (DataIntegrityViolationException dive) {
         CatalogoEspecialidadException cee = new CatalogoEspecialidadException("No fue posible editar al Catálogo de Especialidad", CatalogoEspecialidadException.LAYER_DAO, CatalogoEspecialidadException.ACTION_UPDATE);
         cee.addError("Ocurrió un error al editar el Catálogo de Especialidad");
         logger.error("===>>>Error al editar Catálogo de Especialidad - CODE {} - {}", cee.getExceptionCode(), catalogoEspecialidadView, cee);
         throw cee;
      } catch (Exception ex) {
         CatalogoEspecialidadException cee = new CatalogoEspecialidadException("Error inesperado al editar el Catálogo de Especialidad", CatalogoEspecialidadException.LAYER_DAO, CatalogoEspecialidadException.ACTION_UPDATE);
         cee.addError("Ocurrió un error al editar el Catálogo de Especialidad");
         logger.error("===>>>Error al editar Catálogo de Especialidad - CODE {} - {}", cee.getExceptionCode(), catalogoEspecialidadView, cee);
         throw cee;
      }
   }

   @Transactional(readOnly = false, rollbackFor = {CatalogoEspecialidadException.class})
   @Override
   public void deleteCatalogoEspecialidad(Long idCatalogoEspecialidad) throws CatalogoEspecialidadException {
      try {
         if (!catalogoEspecialidadRepository.exists(idCatalogoEspecialidad)) {
            logger.error("===>>>No se encuentra el idCatalogoEspecialidad");
            CatalogoEspecialidadException cee = new CatalogoEspecialidadException("No se encuentra en el sistema el Catálogo Especialidad.", CatalogoEspecialidadException.LAYER_DAO, CatalogoEspecialidadException.ACTION_VALIDATE);
            cee.addError("Catálogo de Especialidad no encontrado");
            throw cee;
         }
         CatalogoEspecialidad catalogoEspecialidad = catalogoEspecialidadRepository.findOne(idCatalogoEspecialidad);
         catalogoEspecialidad.setActivo(Boolean.FALSE);
         catalogoEspecialidadRepository.save(catalogoEspecialidad);
      } catch (CatalogoEspecialidadException cee) {
         throw cee;
      } catch (DataIntegrityViolationException dive) {
         CatalogoEspecialidadException cee = new CatalogoEspecialidadException("No fue posible eliminar al Catálogo de Especialidad", CatalogoEspecialidadException.LAYER_DAO, CatalogoEspecialidadException.ACTION_UPDATE);
         cee.addError("Ocurrió un error al eliminar el Catálogo de Especialidad");
         logger.error("===>>>Error al eliminar Catálogo de Especialidad - CODE {} - {}", cee.getExceptionCode(), idCatalogoEspecialidad, cee);
         throw cee;
      } catch (Exception ex) {
         CatalogoEspecialidadException cee = new CatalogoEspecialidadException("Error inesperado al eliminar el Catálogo de Especialidad", CatalogoEspecialidadException.LAYER_DAO, CatalogoEspecialidadException.ACTION_UPDATE);
         cee.addError("Ocurrió un error al eliminar el Catálogo de Especialidad");
         logger.error("===>>>Error al eliminar Catálogo de Especialidad - CODE {} - {}", cee.getExceptionCode(), idCatalogoEspecialidad, cee);
         throw cee;
      }
   }

   @Override
   public CatalogoEspecialidadView getDetailsByIdCatalogoEspecialidad(Long idCatalogoEspecialidad) throws CatalogoEspecialidadException {
      try {
         if (!catalogoEspecialidadRepository.exists(idCatalogoEspecialidad)) {
            logger.error("===>>>No se encuentra el idCatalogoEspecialidad");
            CatalogoEspecialidadException cee = new CatalogoEspecialidadException("No se encuentra el el sistema el Catálogo del Especialidad.", CatalogoEspecialidadException.LAYER_DAO, CatalogoEspecialidadException.ACTION_VALIDATE);
            cee.addError("Catálogo del Especialidad no encontrado");
            throw cee;
         }
         CatalogoEspecialidad catalogoEspecialidad = catalogoEspecialidadRepository.findOne(idCatalogoEspecialidad);
         return catalogoEspecialidadConverter.toView(catalogoEspecialidad, Boolean.TRUE);
      } catch (CatalogoEspecialidadException cee) {
         throw cee;
      } catch (DataIntegrityViolationException dive) {
         CatalogoEspecialidadException cee = new CatalogoEspecialidadException("No fue posible editar al Catálogo de Especialidad", CatalogoEspecialidadException.LAYER_DAO, CatalogoEspecialidadException.ACTION_SELECT);
         cee.addError("No fue posible obtener detalles del Catálogo de Especialidad");
         logger.error("===>>>Error al obtener detalles del  Catálogo de Especialidad - CODE {} - {}", cee.getExceptionCode(), idCatalogoEspecialidad, cee);
         throw cee;
      } catch (Exception ex) {
         CatalogoEspecialidadException cee = new CatalogoEspecialidadException("Error inesperado al obtener detalles del Catálogo de Especialidad", CatalogoEspecialidadException.LAYER_DAO, CatalogoEspecialidadException.ACTION_SELECT);
         cee.addError("Ocurrió un error al obtener detalles  del Catálogo de Especialidad");
         logger.error("===>>>Error al obtener detalles del  Catálogo de Especialidad - CODE {} - {}", cee.getExceptionCode(), idCatalogoEspecialidad, cee);
         throw cee;
      }
   }

   @Override
   public List<CatalogoEspecialidadView> findAll(Boolean active) throws CatalogoEspecialidadException {
      try {
         List<CatalogoEspecialidad> catalogoEspecialidadList = new ArrayList<>();
         List<CatalogoEspecialidadView> catalogoEspecialidadViewList = new ArrayList<>();
         if (active == null) {
            catalogoEspecialidadList = catalogoEspecialidadRepository.findAll();
         } else {
            catalogoEspecialidadList = catalogoEspecialidadRepository.findAll().stream().filter(catalogoEspecialidad -> catalogoEspecialidad.getActivo().compareTo(active) == 0).collect(Collectors.toList());
         }
         for (CatalogoEspecialidad csm : catalogoEspecialidadList) {
            catalogoEspecialidadViewList.add(catalogoEspecialidadConverter.toView(csm, Boolean.TRUE));
         }
         return catalogoEspecialidadViewList;
      } catch (Exception ex) {
         CatalogoEspecialidadException cee = new CatalogoEspecialidadException("Ocurrio un error al seleccionar lista del Catálogo de Especialidad", CatalogoEspecialidadException.LAYER_SERVICE, CatalogoEspecialidadException.ACTION_SELECT);
         logger.error(ExceptionServiceCode.GROUP + "===>>>Error al tratar de seleccionar lista del Catálogo de Especialidad - CODE: {}-{}", cee.getExceptionCode(), ex);
         throw cee;
      }
   }

   @Override
   public Page<CatalogoEspecialidadView> getCatalogoEspecialidadPage(Boolean active, String name, Integer page, Integer size, String orderColumn, String orderType) throws CatalogoEspecialidadException {
      try {
         logger.info("===>>>Obtener listado de Catálogo de Especialidad paginable: active: {} - name {} - page: {} - size: {} - orderColumn: {} - orderType: {}",
            active, name, page, size, orderColumn, orderType);
         List<CatalogoEspecialidadView> catalogoEspecialidadViewList = new ArrayList<>();
         Page<CatalogoEspecialidad> catalogoEspecialidadPage = null;
         Sort sort = new Sort(Sort.Direction.ASC, (String) colOrderNames.get("descripcionEspecialidad"));

         if (orderColumn != null && orderType != null) {
            if (orderType.equalsIgnoreCase("asc")) {
               sort = new Sort(Sort.Direction.ASC, (String) colOrderNames.get(orderColumn));
            } else {
               sort = new Sort(Sort.Direction.DESC, (String) colOrderNames.get(orderColumn));
            }

         }
         PageRequest request = new PageRequest(page, size, sort);
         final String patternSearch = "%" + name.toLowerCase() + "%";
         Specifications<CatalogoEspecialidad> spec = Specifications.where(
            (root, query, cb) -> {
               Predicate tc = null;
               if (name != null && !name.isEmpty()) {
                  tc = (tc != null ? cb.and(tc, cb.like(cb.lower(root.get("descripcionEspecialidad")), patternSearch)) : cb.like(cb.lower(root.get("descripcionEspecialidad")), patternSearch));
               }
               if (active != null) {
                  tc = (tc != null ? cb.and(tc, cb.equal(root.get("activo"), active)) : cb.equal(root.get("activo"), active));
               }
               return tc;
            }
         );

         if (spec == null) {
            catalogoEspecialidadPage = catalogoEspecialidadRepository.findAll(request);
         } else {
            catalogoEspecialidadPage = catalogoEspecialidadRepository.findAll(spec, request);
         }

         catalogoEspecialidadPage.getContent().forEach(catalogoEspecialidad -> {
            // catalogoEspecialidadViewList.add(catalogoEspecialidadConverter.toViewPage(catalogoEspecialidad));
            catalogoEspecialidadViewList.add(catalogoEspecialidadConverter.toView(catalogoEspecialidad, Boolean.TRUE));
         });
         PageImpl<CatalogoEspecialidadView> catalogoEspecialidadViewPage = new PageImpl<CatalogoEspecialidadView>(catalogoEspecialidadViewList, request, catalogoEspecialidadPage.getTotalElements());
         return catalogoEspecialidadViewPage;
      }catch (IllegalArgumentException iae) {
         logger.error("===>>>Algun parametro no es correcto");
         CatalogoEspecialidadException cee = new CatalogoEspecialidadException("Algun parametro no es correcto", CatalogoEspecialidadException.LAYER_SERVICE, CatalogoEspecialidadException.ACTION_VALIDATE);
         cee.addError("Puede que sea null, vacio o valor incorrecto");
         throw cee;
      } catch (Exception ex) {
         CatalogoEspecialidadException cee = new CatalogoEspecialidadException("Ocurrio un error al seleccionar lista del Catálogo de Especialidad paginable", CatalogoEspecialidadException.LAYER_SERVICE, CatalogoEspecialidadException.ACTION_SELECT);
         logger.error(ExceptionServiceCode.GROUP + "===>>>Error al tratar de seleccionar lista del Catálogo de Especialidad paginable - CODE: {}-{}", cee.getExceptionCode(), ex);
         throw cee;
      }
   }


}
