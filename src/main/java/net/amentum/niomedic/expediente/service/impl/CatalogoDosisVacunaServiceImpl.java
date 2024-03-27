package net.amentum.niomedic.expediente.service.impl;

import net.amentum.niomedic.expediente.converter.CatalogoDosisVacunaConverter;
import net.amentum.niomedic.expediente.exception.CatalogoDosisVacunaException;
import net.amentum.niomedic.expediente.exception.ExceptionServiceCode;
import net.amentum.niomedic.expediente.model.CatalogoDosisVacuna;
import net.amentum.niomedic.expediente.model.CatalogoVacuna;
import net.amentum.niomedic.expediente.persistence.CatalogoDosisVacunaRepository;
import net.amentum.niomedic.expediente.persistence.CatalogoVacunaRepository;
import net.amentum.niomedic.expediente.service.CatalogoDosisVacunaService;
import net.amentum.niomedic.expediente.views.CatalogoDosisVacunaView;
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
public class CatalogoDosisVacunaServiceImpl implements CatalogoDosisVacunaService {
   private final Logger logger = LoggerFactory.getLogger(CatalogoDosisVacunaServiceImpl.class);
   private final Map<String, Object> colOrderNames = new HashMap<>();
   private CatalogoDosisVacunaRepository catalogoDosisVacunaRepository;
   private CatalogoDosisVacunaConverter catalogoDosisVacunaConverter;
   private CatalogoVacunaRepository catalogoVacunaRepository;

   {
      colOrderNames.put("dosis", "dosis");
   }

   @Autowired
   public void setCatalogoDosisVacunaRepository(CatalogoDosisVacunaRepository catalogoDosisVacunaRepository) {
      this.catalogoDosisVacunaRepository = catalogoDosisVacunaRepository;
   }

   @Autowired
   public void setCatalogoDosisVacunaConverter(CatalogoDosisVacunaConverter catalogoDosisVacunaConverter) {
      this.catalogoDosisVacunaConverter = catalogoDosisVacunaConverter;
   }

   @Autowired
   public void setCatalogoVacunaRepository(CatalogoVacunaRepository catalogoVacunaRepository) {
      this.catalogoVacunaRepository = catalogoVacunaRepository;
   }

   @Transactional(readOnly = false, rollbackFor = {CatalogoDosisVacunaException.class})
   @Override
   public void createCatalogoDosisVacuna(CatalogoDosisVacunaView catalogoDosisVacunaView, Long idCatalogoVacuna) throws CatalogoDosisVacunaException {
      try {
         if (!catalogoVacunaRepository.exists(idCatalogoVacuna)) {
            logger.error("===>>>No se encuentra el idCatalogoVacuna");
            CatalogoDosisVacunaException cdve = new CatalogoDosisVacunaException("No se encuentra en el sistema al Catalogo de Vacuna.", CatalogoDosisVacunaException.LAYER_DAO, CatalogoDosisVacunaException.ACTION_VALIDATE);
            cdve.addError("Catálogo Vacuna no encontrado");
            throw cdve;
         }
         CatalogoVacuna catalogoVacuna = catalogoVacunaRepository.findOne(idCatalogoVacuna);
         CatalogoDosisVacuna catalogoDosisVacunaOld = catalogoVacuna.getCatalogoDosisVacuna();
         if (catalogoDosisVacunaOld != null) {
            catalogoDosisVacunaOld.setActivo(Boolean.FALSE);
            catalogoDosisVacunaOld.setCatalogoVacunaId(null);
         }
         CatalogoDosisVacuna catalogoDosisVacuna = catalogoDosisVacunaConverter.toEntity(catalogoDosisVacunaView, new CatalogoDosisVacuna(), Boolean.FALSE);
         catalogoDosisVacuna.setCatalogoVacunaId(catalogoVacuna);
         logger.debug("===>>>Insertar nuevo Catalogo Dosis Vacuna: {}", catalogoDosisVacuna);
         catalogoDosisVacunaRepository.save(catalogoDosisVacuna);
      } catch (CatalogoDosisVacunaException cdve) {
         throw cdve;
      } catch (DataIntegrityViolationException dive) {
         CatalogoDosisVacunaException cdve = new CatalogoDosisVacunaException("No fue posible agregar al Catálogo de Dosis Vacuna", CatalogoDosisVacunaException.LAYER_DAO, CatalogoDosisVacunaException.ACTION_INSERT);
         cdve.addError("Ocurrio un error al agregar el Catálogo de Dosis Vacuna");
         logger.error("===>>>Error al insertar nuevo Catálogo de Dosis Vacuna - CODE {} - {}", cdve.getExceptionCode(), catalogoDosisVacunaView, cdve);
         throw cdve;
      } catch (Exception ex) {
         CatalogoDosisVacunaException cdve = new CatalogoDosisVacunaException("Error inesperado al agregar el Catálogo de Dosis Vacuna", CatalogoDosisVacunaException.LAYER_DAO, CatalogoDosisVacunaException.ACTION_INSERT);
         cdve.addError("Ocurrió un error al agregar el Catálogo de Dosis Vacuna");
         logger.error("===>>>Error al insertar nuevo Catálogo de Dosis Vacuna - CODE {} - {}", cdve.getExceptionCode(), catalogoDosisVacunaView, cdve);
         throw cdve;
      }
   }

   @Transactional(readOnly = false, rollbackFor = {CatalogoDosisVacunaException.class})
   @Override
   public void updateCatalogoDosisVacuna(CatalogoDosisVacunaView catalogoDosisVacunaView, Long idCatalogoVacuna) throws CatalogoDosisVacunaException {
      try {
         if (!catalogoVacunaRepository.exists(idCatalogoVacuna)) {
            logger.error("===>>>No se encuentra el idCatalogoVacuna");
            CatalogoDosisVacunaException cdve = new CatalogoDosisVacunaException("No se encuentra en el sistema al Catalogo de Vacuna.", CatalogoDosisVacunaException.LAYER_DAO, CatalogoDosisVacunaException.ACTION_VALIDATE);
            cdve.addError("Catalogo de Vacuna no encotrado");
            throw cdve;
         }
         CatalogoVacuna catalogoVacuna = catalogoVacunaRepository.findOne(idCatalogoVacuna);
         CatalogoDosisVacuna catalogoDosisVacuna;
         if (catalogoVacuna.getCatalogoDosisVacuna() != null) {
            catalogoDosisVacuna = catalogoVacuna.getCatalogoDosisVacuna();
            catalogoDosisVacuna = catalogoDosisVacunaConverter.toEntity(catalogoDosisVacunaView, catalogoDosisVacuna, Boolean.TRUE);
         } else {
            CatalogoDosisVacunaException cdve = new CatalogoDosisVacunaException("No se encuentra en el sistema al Catalogo de Dosis Vacuna.", CatalogoDosisVacunaException.LAYER_DAO, CatalogoDosisVacunaException.ACTION_VALIDATE);
            cdve.addError("Catalogo de Dosis Vacuna no encotrado");
            throw cdve;
         }
         logger.debug("===>>>Editar Catálogo Dosis Vacuna: {}", catalogoDosisVacuna);
         catalogoDosisVacunaRepository.save(catalogoDosisVacuna);
      } catch (CatalogoDosisVacunaException cdve) {
         throw cdve;
      } catch (DataIntegrityViolationException dive) {
         CatalogoDosisVacunaException cdve = new CatalogoDosisVacunaException("No fue posible editar al Catálogo Dosis Vacuna", CatalogoDosisVacunaException.LAYER_DAO, CatalogoDosisVacunaException.ACTION_UPDATE);
         cdve.addError("Ocurrió un error al editar el Catálogo Dosis Vacuna");
         logger.error("===>>>Error al editar Catálogo Dosis Vacuna - CODE {} - {}", cdve.getExceptionCode(), catalogoDosisVacunaView, cdve);
         throw cdve;
      } catch (Exception ex) {
         CatalogoDosisVacunaException cdve = new CatalogoDosisVacunaException("Error inesperado al editar el Catálogo Vacuna", CatalogoDosisVacunaException.LAYER_DAO, CatalogoDosisVacunaException.ACTION_UPDATE);
         cdve.addError("Ocurrió un error al editar el Catálogo Dosis Vacuna");
         logger.error("===>>>Error al editar Catálogo Dosis Vacuna - CODE {} - {}", cdve.getExceptionCode(), catalogoDosisVacunaView, cdve);
         throw cdve;
      }
   }

   @Transactional(readOnly = false, rollbackFor = {CatalogoDosisVacunaException.class})
   @Override
   public void deleteCatalogoDosisVacuna(Long idCatalogoVacuna) throws CatalogoDosisVacunaException {
      try {
         if (!catalogoVacunaRepository.exists(idCatalogoVacuna)) {
            logger.error("===>>>No se encuentra el idCatalogoVacuna");
            CatalogoDosisVacunaException cdve = new CatalogoDosisVacunaException("No se encuentra en el sistema al Catalogo de Vacuna.", CatalogoDosisVacunaException.LAYER_DAO, CatalogoDosisVacunaException.ACTION_VALIDATE);
            cdve.addError("Catalogo de Vacuna no encotrado");
            throw cdve;
         }
         CatalogoVacuna catalogoVacuna = catalogoVacunaRepository.findOne(idCatalogoVacuna);
         CatalogoDosisVacuna catalogoDosisVacuna;
         if (catalogoVacuna.getCatalogoDosisVacuna() != null) {
            catalogoDosisVacuna = catalogoVacuna.getCatalogoDosisVacuna();
            //logger.info("--> " + catalogoDosisVacuna.getIdCatalogoDosisVacuna());
            catalogoDosisVacuna.setActivo(Boolean.FALSE);
            //catalogoDosisVacuna = catalogoDosisVacunaConverter.toEntity(catalogoDosisVacunaView, catalogoDosisVacuna, Boolean.TRUE);
         } else {
            logger.error("===>>>Catalogo de Dosis Vacuna no encotrado");
            CatalogoDosisVacunaException cdve = new CatalogoDosisVacunaException("No se encuentra en el sistema al Catalogo de Dosis Vacuna.", CatalogoDosisVacunaException.LAYER_DAO, CatalogoDosisVacunaException.ACTION_VALIDATE);
            cdve.addError("Catalogo de Dosis Vacuna no encotrado");
            throw cdve;
         }
         logger.debug("===>>>Borrar Catálogo Dosis Vacuna: {}", catalogoDosisVacuna);
         catalogoDosisVacunaRepository.save(catalogoDosisVacuna);
      } catch (CatalogoDosisVacunaException cdve) {
         throw cdve;
      } catch (DataIntegrityViolationException dive) {
         CatalogoDosisVacunaException cdve = new CatalogoDosisVacunaException("No fue posible eliminar al Catálogo de Dosis Vacuna", CatalogoDosisVacunaException.LAYER_DAO, CatalogoDosisVacunaException.ACTION_UPDATE);
         cdve.addError("Ocurrió un error al eliminar el Catálogo de Dosis Vacuna");
         logger.error("===>>>Error al eliminar Catálogo de Dosis Vacuna - CODE {} - {}", cdve.getExceptionCode(), idCatalogoVacuna, cdve);
         throw cdve;
      } catch (Exception ex) {
         CatalogoDosisVacunaException cdve = new CatalogoDosisVacunaException("Error inesperado al eliminar el Catálogo de Dosis Vacuna", CatalogoDosisVacunaException.LAYER_DAO, CatalogoDosisVacunaException.ACTION_UPDATE);
         cdve.addError("Ocurrió un error al eliminar el Catálogo de Dosis Vacuna");
         logger.error("===>>>Error al eliminar Catálogo de Dosis Vacuna - CODE {} - {}", cdve.getExceptionCode(), idCatalogoVacuna, cdve);
         throw cdve;
      }
   }

   @Override
   public CatalogoDosisVacunaView getDetailsByIdCatalogoDosisVacuna(Long idCatalogoVacuna) throws CatalogoDosisVacunaException {
      try {
         if (!catalogoVacunaRepository.exists(idCatalogoVacuna)) {
            logger.error("===>>>No se encuentra el idCatalogoVacuna");
            CatalogoDosisVacunaException cdve = new CatalogoDosisVacunaException("No se encuentra en el sistema al Catalogo de Vacuna.", CatalogoDosisVacunaException.LAYER_DAO, CatalogoDosisVacunaException.ACTION_VALIDATE);
            cdve.addError("Catalogo de Vacuna no encotrado");
            throw cdve;
         }
         CatalogoVacuna catalogoVacuna = catalogoVacunaRepository.findOne(idCatalogoVacuna);
         CatalogoDosisVacuna catalogoDosisVacuna;
         if (catalogoVacuna.getCatalogoDosisVacuna() != null) {
            catalogoDosisVacuna = catalogoVacuna.getCatalogoDosisVacuna();
            return catalogoDosisVacunaConverter.toView(catalogoDosisVacuna, Boolean.TRUE);
         } else {
            logger.error("===>>>Catalogo de Dosis Vacuna no encotrado");
            CatalogoDosisVacunaException cdve = new CatalogoDosisVacunaException("No se encuentra en el sistema al Catalogo de Dosis Vacuna.", CatalogoDosisVacunaException.LAYER_DAO, CatalogoDosisVacunaException.ACTION_VALIDATE);
            cdve.addError("Catalogo de Dosis Vacuna no encotrado");
            throw cdve;
         }
      } catch (CatalogoDosisVacunaException cdve) {
         throw cdve;
      } catch (DataIntegrityViolationException dive) {
         CatalogoDosisVacunaException cdve = new CatalogoDosisVacunaException("No fue posible obtgener detalles del Catálogo de Dosis Vacuna", CatalogoDosisVacunaException.LAYER_DAO, CatalogoDosisVacunaException.ACTION_SELECT);
         cdve.addError("No fue posible obtener detalles del Catálogo de Dosis Vacuna");
         logger.error("===>>>Error al obtener detalles del  Catálogo de Dosis Vacuna - CODE {} - {}", cdve.getExceptionCode(), idCatalogoVacuna, cdve);
         throw cdve;
      } catch (Exception ex) {
         CatalogoDosisVacunaException cdve = new CatalogoDosisVacunaException("Error inesperado al obtener detalles del Catálogo de Dosis Vacuna", CatalogoDosisVacunaException.LAYER_DAO, CatalogoDosisVacunaException.ACTION_SELECT);
         cdve.addError("Ocurrió un error al obtener detalles  del Catálogo de Dosis Vacuna");
         logger.error("===>>>Error al obtener detalles del  Catálogo de Dosis Vacuna - CODE {} - {}", cdve.getExceptionCode(), idCatalogoVacuna, cdve);
         throw cdve;
      }
   }

   @Override
   public List<CatalogoDosisVacunaView> findAll(Boolean active) throws CatalogoDosisVacunaException {
      try {
         List<CatalogoDosisVacuna> catalogoDosisVacunaList = new ArrayList<>();
         List<CatalogoDosisVacunaView> catalogoDosisVacunaViewList = new ArrayList<>();
         if (active == null) {
            catalogoDosisVacunaList = catalogoDosisVacunaRepository.findAll();
         } else {
            catalogoDosisVacunaList = catalogoDosisVacunaRepository.findAll().stream().filter(catalogoDosisVacuna -> catalogoDosisVacuna.getActivo().compareTo(active) == 0).collect(Collectors.toList());
         }
         for (CatalogoDosisVacuna csm : catalogoDosisVacunaList) {
            catalogoDosisVacunaViewList.add(catalogoDosisVacunaConverter.toView(csm, Boolean.TRUE));
         }
         return catalogoDosisVacunaViewList;
      } catch (Exception ex) {
         CatalogoDosisVacunaException cdve = new CatalogoDosisVacunaException("Ocurrio un error al seleccionar lista del Catálogo de Dosis Vacuna", CatalogoDosisVacunaException.LAYER_SERVICE, CatalogoDosisVacunaException.ACTION_SELECT);
         logger.error(ExceptionServiceCode.GROUP + "===>>>Error al tratar de seleccionar lista del Catálogo de Dosis Vacuna - CODE: {}-{}", cdve.getExceptionCode(), ex);
         throw cdve;
      }
   }

   @Override
   public Page<CatalogoDosisVacunaView> getCatalogoDosisVacunaPage(Boolean active, String name, Integer page, Integer size, String orderColumn, String orderType) throws CatalogoDosisVacunaException {
      try {
         logger.info("===>>>Obtener listado de Catálogo de Dosis Vacuna paginable: active: {} - name {} - page: {} - size: {} - orderColumn: {} - orderType: {}",
            active, name, page, size, orderColumn, orderType);
         List<CatalogoDosisVacunaView> catalogoDosisVacunaViewList = new ArrayList<>();
         Page<CatalogoDosisVacuna> catalogoDosisVacunaPage = null;
         Sort sort = new Sort(Sort.Direction.ASC, (String) colOrderNames.get("dosis"));

         if (orderColumn != null && orderType != null) {
            if (orderType.equalsIgnoreCase("asc")) {
               sort = new Sort(Sort.Direction.ASC, (String) colOrderNames.get(orderColumn));
            } else {
               sort = new Sort(Sort.Direction.DESC, (String) colOrderNames.get(orderColumn));
            }

         }
         PageRequest request = new PageRequest(page, size, sort);
         final String patternSearch = "%" + name.toLowerCase() + "%";
         Specifications<CatalogoDosisVacuna> spec = Specifications.where(
            (root, query, cb) -> {
               Predicate tc = null;
               if (name != null && !name.isEmpty()) {
                  tc = (tc != null ? cb.and(tc, cb.like(cb.lower(root.get("dosis")), patternSearch)) : cb.like(cb.lower(root.get("dosis")), patternSearch));
               }
               if (active != null) {
                  tc = (tc != null ? cb.and(tc, cb.equal(root.get("activo"), active)) : cb.equal(root.get("activo"), active));
               }
               return tc;
            }
         );

         if (spec == null) {
            catalogoDosisVacunaPage = catalogoDosisVacunaRepository.findAll(request);
         } else {
            catalogoDosisVacunaPage = catalogoDosisVacunaRepository.findAll(spec, request);
         }

         catalogoDosisVacunaPage.getContent().forEach(catalogoDosisVacuna -> {
            // catalogoDosisVacunaViewList.add(catalogoDosisVacunaConverter.toViewPage(catalogoDosisVacuna));
            catalogoDosisVacunaViewList.add(catalogoDosisVacunaConverter.toView(catalogoDosisVacuna, Boolean.TRUE));
         });
         PageImpl<CatalogoDosisVacunaView> catalogoDosisVacunaViewPage = new PageImpl<CatalogoDosisVacunaView>(catalogoDosisVacunaViewList, request, catalogoDosisVacunaPage.getTotalElements());
         return catalogoDosisVacunaViewPage;
      } catch (IllegalArgumentException iae) {
         logger.error("===>>>Algun parametro no es correcto");
         CatalogoDosisVacunaException cdve = new CatalogoDosisVacunaException("Algun parametro no es correcto", CatalogoDosisVacunaException.LAYER_SERVICE, CatalogoDosisVacunaException.ACTION_VALIDATE);
         cdve.addError("Puede que sea null, vacio o valor incorrecto");
         throw cdve;
      } catch (Exception ex) {
         CatalogoDosisVacunaException cdve = new CatalogoDosisVacunaException("Ocurrio un error al seleccionar lista del Catálogo de Dosis Vacuna paginable", CatalogoDosisVacunaException.LAYER_SERVICE, CatalogoDosisVacunaException.ACTION_SELECT);
         logger.error(ExceptionServiceCode.GROUP + "- Error al tratar de seleccionar lista del Catálogo de Dosis Vacuna paginable - CODE: {}-{}", cdve.getExceptionCode(), ex);
         throw cdve;
      }
   }


}