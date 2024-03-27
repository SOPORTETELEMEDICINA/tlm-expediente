package net.amentum.niomedic.expediente.service.impl;

import net.amentum.niomedic.expediente.converter.CatalogoInstitucionMedicaConverter;
import net.amentum.niomedic.expediente.exception.CatalogoInstitucionMedicaException;
import net.amentum.niomedic.expediente.exception.ExceptionServiceCode;
import net.amentum.niomedic.expediente.model.CatalogoInstitucionMedica;
import net.amentum.niomedic.expediente.persistence.CatalogoInstitucionMedicaRepository;
import net.amentum.niomedic.expediente.service.CatalogoInstitucionMedicaService;
import net.amentum.niomedic.expediente.views.CatalogoInstitucionMedicaView;
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
public class CatalogoInstitucionMedicaServiceImpl implements CatalogoInstitucionMedicaService {
   private final Logger logger = LoggerFactory.getLogger(CatalogoInstitucionMedicaServiceImpl.class);
   private final Map<String, Object> colOrderNames = new HashMap<>();
   private CatalogoInstitucionMedicaRepository catalogoInstitucionMedicaRepository;
   private CatalogoInstitucionMedicaConverter catalogoInstitucionMedicaConverter;

   {
      colOrderNames.put("clave", "clave");
      colOrderNames.put("claveCorta", "claveCorta");
      colOrderNames.put("nombre", "nombre");
   }

   @Autowired
   public void setCatalogoInstitucionMedicaRepository(CatalogoInstitucionMedicaRepository catalogoInstitucionMedicaRepository) {
      this.catalogoInstitucionMedicaRepository = catalogoInstitucionMedicaRepository;
   }

   @Autowired
   public void setCatalogoInstitucionMedicaConverter(CatalogoInstitucionMedicaConverter catalogoInstitucionMedicaConverter) {
      this.catalogoInstitucionMedicaConverter = catalogoInstitucionMedicaConverter;
   }

   @Transactional(readOnly = false, rollbackFor = {CatalogoInstitucionMedicaException.class})
   @Override
   public void createCatalogoInstitucionMedica(CatalogoInstitucionMedicaView catalogoInstitucionMedicaView) throws CatalogoInstitucionMedicaException {
      try {
         CatalogoInstitucionMedica catalogoInstitucionMedica = catalogoInstitucionMedicaConverter.toEntity(catalogoInstitucionMedicaView, new CatalogoInstitucionMedica(), Boolean.FALSE);
         logger.debug("===>>>Insertar nuevo Catálogo de Institución Médica: {}", catalogoInstitucionMedica);
         catalogoInstitucionMedicaRepository.save(catalogoInstitucionMedica);
      } catch (DataIntegrityViolationException dive) {
         CatalogoInstitucionMedicaException cime = new CatalogoInstitucionMedicaException("No fue posible agregar al Catálogo de Institución Médica", CatalogoInstitucionMedicaException.LAYER_DAO, CatalogoInstitucionMedicaException.ACTION_INSERT);
         cime.addError("Ocurrio un error al agregar el Catálogo de Institución Médica");
         logger.error("===>>>Error al insertar nuevo Catálogo de Institución Médica - CODE {} - {}", cime.getExceptionCode(), catalogoInstitucionMedicaView, cime);
         throw cime;
      } catch (Exception ex) {
         CatalogoInstitucionMedicaException cime = new CatalogoInstitucionMedicaException("Error inesperado al agregar el Catálogo de Institución Médica", CatalogoInstitucionMedicaException.LAYER_DAO, CatalogoInstitucionMedicaException.ACTION_INSERT);
         cime.addError("Ocurrió un error al agregar el Catálogo de Institución Médica");
         logger.error("===>>>Error al insertar nuevo Catálogo de Institución Médica - CODE {} - {}", cime.getExceptionCode(), catalogoInstitucionMedicaView, cime);
         throw cime;
      }
   }

   @Transactional(readOnly = false, rollbackFor = {CatalogoInstitucionMedicaException.class})
   @Override
   public void updateCatalogoInstitucionMedica(CatalogoInstitucionMedicaView catalogoInstitucionMedicaView) throws CatalogoInstitucionMedicaException {
      try {
         if (!catalogoInstitucionMedicaRepository.exists(catalogoInstitucionMedicaView.getIdCatalogoInstitucionMedica())) {
            logger.error("===>>>No se encuentra el idCatalogoInstitucionMedica");
            CatalogoInstitucionMedicaException cime = new CatalogoInstitucionMedicaException("No se encuentra en el sistema el Catálogo Institución Médica.", CatalogoInstitucionMedicaException.LAYER_DAO, CatalogoInstitucionMedicaException.ACTION_VALIDATE);
            cime.addError("Catálogo de Institución Médica no encontrado");
            throw cime;
         }
         CatalogoInstitucionMedica catalogoInstitucionMedica = catalogoInstitucionMedicaRepository.findOne(catalogoInstitucionMedicaView.getIdCatalogoInstitucionMedica());
         catalogoInstitucionMedica = catalogoInstitucionMedicaConverter.toEntity(catalogoInstitucionMedicaView, catalogoInstitucionMedica, Boolean.TRUE);
         logger.debug("===>>>Editar Catálogo de Institución Médica: {}", catalogoInstitucionMedica);
         catalogoInstitucionMedicaRepository.save(catalogoInstitucionMedica);
      } catch (CatalogoInstitucionMedicaException cime) {
         throw cime;
      } catch (DataIntegrityViolationException dive) {
         CatalogoInstitucionMedicaException cime = new CatalogoInstitucionMedicaException("No fue posible editar al Catálogo de Institución Médica", CatalogoInstitucionMedicaException.LAYER_DAO, CatalogoInstitucionMedicaException.ACTION_UPDATE);
         cime.addError("Ocurrió un error al editar el Catálogo de Institución Médica");
         logger.error("===>>>Error al editar Catálogo de Institución Médica - CODE {} - {}", cime.getExceptionCode(), catalogoInstitucionMedicaView, cime);
         throw cime;
      } catch (Exception ex) {
         CatalogoInstitucionMedicaException cime = new CatalogoInstitucionMedicaException("Error inesperado al agregar el Catálogo de Institución Médica", CatalogoInstitucionMedicaException.LAYER_DAO, CatalogoInstitucionMedicaException.ACTION_UPDATE);
         cime.addError("Ocurrió un error al editar el Catálogo de Institución Médica");
         logger.error("===>>>Error al editar Catálogo de Institución Médica - CODE {} - {}", cime.getExceptionCode(), catalogoInstitucionMedicaView, cime);
         throw cime;
      }
   }

   @Transactional(readOnly = false, rollbackFor = {CatalogoInstitucionMedicaException.class})
   @Override
   public void deleteCatalogoInstitucionMedica(Long idCatalogoInstitucionMedica) throws CatalogoInstitucionMedicaException {
      try {
         if (!catalogoInstitucionMedicaRepository.exists(idCatalogoInstitucionMedica)) {
            logger.error("===>>>No se encuentra el idCatalogoInstitucionMedica");
            CatalogoInstitucionMedicaException cime = new CatalogoInstitucionMedicaException("No se encuentra en el sistema el Catálogo Institución Médica.", CatalogoInstitucionMedicaException.LAYER_DAO, CatalogoInstitucionMedicaException.ACTION_VALIDATE);
            cime.addError("Catálogo de Institución Médica no encontrado");
            throw cime;
         }
         CatalogoInstitucionMedica catalogoInstitucionMedica = catalogoInstitucionMedicaRepository.findOne(idCatalogoInstitucionMedica);
         catalogoInstitucionMedica.setActivo(Boolean.FALSE);
         catalogoInstitucionMedicaRepository.save(catalogoInstitucionMedica);
      } catch (CatalogoInstitucionMedicaException cime) {
         throw cime;
      } catch (DataIntegrityViolationException dive) {
         CatalogoInstitucionMedicaException cime = new CatalogoInstitucionMedicaException("No fue posible eliminar al Catálogo de Institución Médica", CatalogoInstitucionMedicaException.LAYER_DAO, CatalogoInstitucionMedicaException.ACTION_UPDATE);
         cime.addError("Ocurrió un error al eliminar el Catálogo de Institución Médica");
         logger.error("===>>>Error al eliminar Catálogo de Institución Médica - CODE {} - {}", cime.getExceptionCode(), idCatalogoInstitucionMedica, cime);
         throw cime;
      } catch (Exception ex) {
         CatalogoInstitucionMedicaException cime = new CatalogoInstitucionMedicaException("Error inesperado al eliminar el Catálogo de Institución Médica", CatalogoInstitucionMedicaException.LAYER_DAO, CatalogoInstitucionMedicaException.ACTION_UPDATE);
         cime.addError("Ocurrió un error al eliminar el Catálogo de Institución Médica");
         logger.error("===>>>Error al eliminar Catálogo de Institución Médica - CODE {} - {}", cime.getExceptionCode(), idCatalogoInstitucionMedica, cime);
         throw cime;
      }
   }

   @Override
   public CatalogoInstitucionMedicaView getDetailsByIdCatalogoInstitucionMedica(Long idCatalogoInstitucionMedica) throws CatalogoInstitucionMedicaException {
      try {
         if (!catalogoInstitucionMedicaRepository.exists(idCatalogoInstitucionMedica)) {
            logger.error("===>>>No se encuentra el idCatalogoInstitucionMedica");
            CatalogoInstitucionMedicaException cime = new CatalogoInstitucionMedicaException("No se encuentra el el sistema el Catálogo del Institución Médica.", CatalogoInstitucionMedicaException.LAYER_DAO, CatalogoInstitucionMedicaException.ACTION_VALIDATE);
            cime.addError("Catálogo del Institución Médica no encontrado");
            throw cime;
         }
         CatalogoInstitucionMedica catalogoInstitucionMedica = catalogoInstitucionMedicaRepository.findOne(idCatalogoInstitucionMedica);
         return catalogoInstitucionMedicaConverter.toView(catalogoInstitucionMedica, Boolean.TRUE);
      } catch (CatalogoInstitucionMedicaException cime) {
         throw cime;
      } catch (DataIntegrityViolationException dive) {
         CatalogoInstitucionMedicaException cime = new CatalogoInstitucionMedicaException("No fue posible editar al Catálogo de Institución Médica", CatalogoInstitucionMedicaException.LAYER_DAO, CatalogoInstitucionMedicaException.ACTION_SELECT);
         cime.addError("No fue posible obtener detalles del Catálogo de Institución Médica");
         logger.error("===>>>Error al obtener detalles del  Catálogo de Institución Médica - CODE {} - {}", cime.getExceptionCode(), idCatalogoInstitucionMedica, cime);
         throw cime;
      } catch (Exception ex) {
         CatalogoInstitucionMedicaException cime = new CatalogoInstitucionMedicaException("Error inesperado al obtener detalles del Catálogo de Institución Médica", CatalogoInstitucionMedicaException.LAYER_DAO, CatalogoInstitucionMedicaException.ACTION_SELECT);
         cime.addError("Ocurrió un error al obtener detalles  del Catálogo de Institución Médica");
         logger.error("===>>>Error al obtener detalles del  Catálogo de Institución Médica - CODE {} - {}", cime.getExceptionCode(), idCatalogoInstitucionMedica, cime);
         throw cime;
      }
   }

   @Override
   public List<CatalogoInstitucionMedicaView> findAll(Boolean active) throws CatalogoInstitucionMedicaException {
      try {
         List<CatalogoInstitucionMedica> catalogoInstitucionMedicaList = new ArrayList<>();
         List<CatalogoInstitucionMedicaView> catalogoInstitucionMedicaViewList = new ArrayList<>();
         if (active == null) {
            catalogoInstitucionMedicaList = catalogoInstitucionMedicaRepository.findAll();
         } else {
            catalogoInstitucionMedicaList = catalogoInstitucionMedicaRepository.findAll().stream().filter(catalogoInstitucionMedica -> catalogoInstitucionMedica.getActivo().compareTo(active) == 0).collect(Collectors.toList());
         }
         for (CatalogoInstitucionMedica csm : catalogoInstitucionMedicaList) {
            catalogoInstitucionMedicaViewList.add(catalogoInstitucionMedicaConverter.toView(csm, Boolean.TRUE));
         }
         return catalogoInstitucionMedicaViewList;
      } catch (Exception ex) {
         CatalogoInstitucionMedicaException cime = new CatalogoInstitucionMedicaException("Ocurrio un error al seleccionar lista del Catálogo de Institución Médica", CatalogoInstitucionMedicaException.LAYER_SERVICE, CatalogoInstitucionMedicaException.ACTION_SELECT);
         logger.error(ExceptionServiceCode.GROUP + "===>>>Error al tratar de seleccionar lista del Catálogo de Institución Médica - CODE: {}-{}", cime.getExceptionCode(), ex);
         throw cime;
      }
   }

   @Override
   public Page<CatalogoInstitucionMedicaView> getCatalogoInstitucionMedicaPage(Boolean active, String name, Integer page, Integer size, String orderColumn, String orderType) throws CatalogoInstitucionMedicaException {
      try {
         logger.info("===>>>Obtener listado de Catálogo de Institución Médica paginable: active: {} - name {} - page: {} - size: {} - orderColumn: {} - orderType: {}",
            active, name, page, size, orderColumn, orderType);
         List<CatalogoInstitucionMedicaView> catalogoInstitucionMedicaViewList = new ArrayList<>();
         Page<CatalogoInstitucionMedica> catalogoInstitucionMedicaPage = null;
         Sort sort = new Sort(Sort.Direction.ASC, (String) colOrderNames.get("nombre"));

         if (orderColumn != null && orderType != null) {
            if (orderType.equalsIgnoreCase("asc")) {
               sort = new Sort(Sort.Direction.ASC, (String) colOrderNames.get(orderColumn));
            } else {
               sort = new Sort(Sort.Direction.DESC, (String) colOrderNames.get(orderColumn));
            }

         }
         PageRequest request = new PageRequest(page, size, sort);
         final String patternSearch = "%" + name.toLowerCase() + "%";
         Specifications<CatalogoInstitucionMedica> spec = Specifications.where(
            (root, query, cb) -> {
               Predicate tc = null;
               if (name != null && !name.isEmpty()) {
                  tc = (tc != null ? cb.and(tc, cb.like(cb.lower(root.get("nombre")), patternSearch)) : cb.like(cb.lower(root.get("nombre")), patternSearch));
               }
               if (active != null) {
                  tc = (tc != null ? cb.and(tc, cb.equal(root.get("activo"), active)) : cb.equal(root.get("activo"), active));
               }
               return tc;
            }
         );

         if (spec == null) {
            catalogoInstitucionMedicaPage = catalogoInstitucionMedicaRepository.findAll(request);
         } else {
            catalogoInstitucionMedicaPage = catalogoInstitucionMedicaRepository.findAll(spec, request);
         }

         catalogoInstitucionMedicaPage.getContent().forEach(catalogoInstitucionMedica -> {
            // catalogoInstitucionMedicaViewList.add(catalogoInstitucionMedicaConverter.toViewPage(catalogoInstitucionMedica));
            catalogoInstitucionMedicaViewList.add(catalogoInstitucionMedicaConverter.toView(catalogoInstitucionMedica, Boolean.TRUE));
         });
         PageImpl<CatalogoInstitucionMedicaView> catalogoInstitucionMedicaViewPage = new PageImpl<CatalogoInstitucionMedicaView>(catalogoInstitucionMedicaViewList, request, catalogoInstitucionMedicaPage.getTotalElements());
         return catalogoInstitucionMedicaViewPage;
      }catch (IllegalArgumentException iae) {
         logger.error("===>>>Algun parametro no es correcto");
         CatalogoInstitucionMedicaException cime = new CatalogoInstitucionMedicaException("Algun parametro no es correcto", CatalogoInstitucionMedicaException.LAYER_SERVICE, CatalogoInstitucionMedicaException.ACTION_VALIDATE);
         cime.addError("Puede que sea null, vacio o valor incorrecto");
         throw cime;
      } catch (Exception ex) {
         CatalogoInstitucionMedicaException cime = new CatalogoInstitucionMedicaException("Ocurrio un error al seleccionar lista del Catálogo de Institución Médica paginable", CatalogoInstitucionMedicaException.LAYER_SERVICE, CatalogoInstitucionMedicaException.ACTION_SELECT);
         logger.error(ExceptionServiceCode.GROUP + "===>>>Error al tratar de seleccionar lista del Catálogo de Institución Médica paginable - CODE: {}-{}", cime.getExceptionCode(), ex);
         throw cime;
      }
   }

}
