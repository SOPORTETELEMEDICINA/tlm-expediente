package net.amentum.niomedic.expediente.service.impl;

import net.amentum.niomedic.expediente.converter.CatalogoVacunaConverter;
import net.amentum.niomedic.expediente.exception.CatalogoVacunaException;
import net.amentum.niomedic.expediente.exception.ExceptionServiceCode;
import net.amentum.niomedic.expediente.model.CatalogoDosisVacuna;
import net.amentum.niomedic.expediente.model.CatalogoVacuna;
import net.amentum.niomedic.expediente.persistence.CatalogoDosisVacunaRepository;
import net.amentum.niomedic.expediente.persistence.CatalogoVacunaRepository;
import net.amentum.niomedic.expediente.service.CatalogoVacunaService;
import net.amentum.niomedic.expediente.views.CatalogoVacunaView;
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
public class CatalogoVacunaServiceImpl implements CatalogoVacunaService {
   private final Logger logger = LoggerFactory.getLogger(CatalogoVacunaServiceImpl.class);
   private final Map<String, Object> colOrderNames = new HashMap<>();
   private CatalogoVacunaRepository catalogoVacunaRepository;
   private CatalogoVacunaConverter catalogoVacunaConverter;
   private CatalogoDosisVacunaRepository catalogoDosisVacunaRepository;

   {
      colOrderNames.put("enfermedades", "enfermedades");
      colOrderNames.put("nombreVacuna", "nombreVacuna");
   }

   @Autowired
   public void setCatalogoVacunaRepository(CatalogoVacunaRepository catalogoVacunaRepository) {
      this.catalogoVacunaRepository = catalogoVacunaRepository;
   }

   @Autowired
   public void setCatalogoVacunaConverter(CatalogoVacunaConverter catalogoVacunaConverter) {
      this.catalogoVacunaConverter = catalogoVacunaConverter;
   }

   @Autowired
   public void setCatalogoDosisVacunaRepository(CatalogoDosisVacunaRepository catalogoDosisVacunaRepository) {
      this.catalogoDosisVacunaRepository = catalogoDosisVacunaRepository;
   }

   @Transactional(readOnly = false, rollbackFor = {CatalogoVacunaException.class})
   @Override
   public void createCatalogoVacuna(CatalogoVacunaView catalogoVacunaView) throws CatalogoVacunaException {
      try {
         CatalogoVacuna catalogoVacuna = catalogoVacunaConverter.toEntity(catalogoVacunaView, new CatalogoVacuna(), Boolean.FALSE);
         logger.debug("===>>>Insertar nuevo Catálogo Vacuna: {}", catalogoVacuna);
         catalogoVacunaRepository.save(catalogoVacuna);
      } catch (DataIntegrityViolationException dive) {
         CatalogoVacunaException cve = new CatalogoVacunaException("No fue posible agregar al Catálogo Vacuna", CatalogoVacunaException.LAYER_DAO, CatalogoVacunaException.ACTION_INSERT);
         cve.addError("Ocurrio un error al agregar el Catálogo Vacuna");
         logger.error("===>>>Error al insertar nuevo Catálogo Vacuna - CODE {} - {}", cve.getExceptionCode(), catalogoVacunaView, cve);
         throw cve;
      } catch (Exception ex) {
         CatalogoVacunaException cve = new CatalogoVacunaException("Error inesperado al agregar el Catálogo Vacuna", CatalogoVacunaException.LAYER_DAO, CatalogoVacunaException.ACTION_INSERT);
         cve.addError("Ocurrió un error al agregar el Catálogo Vacuna");
         logger.error("===>>>Error al insertar nuevo Catálogo Vacuna - CODE {} - {}", cve.getExceptionCode(), catalogoVacunaView, cve);
         throw cve;
      }
   }

   @Transactional(readOnly = false, rollbackFor = {CatalogoVacunaException.class})
   @Override
   public void updateCatalogoVacuna(CatalogoVacunaView catalogoVacunaView) throws CatalogoVacunaException {
      try {
         if (!catalogoVacunaRepository.exists(catalogoVacunaView.getIdCatalogoVacuna())) {
            logger.error("===>>>No se encuentra el idCatalogoVacuna");
            CatalogoVacunaException cve = new CatalogoVacunaException("No se encuentra en el sistema el Catálogo Vacuna.", CatalogoVacunaException.LAYER_DAO, CatalogoVacunaException.ACTION_VALIDATE);
            cve.addError("Catálogo Vacuna no encontrado");
            throw cve;
         }
         CatalogoVacuna catalogoVacuna = catalogoVacunaRepository.findOne(catalogoVacunaView.getIdCatalogoVacuna());
         CatalogoDosisVacuna catalogoDosisVacunaOld = catalogoVacuna.getCatalogoDosisVacuna();
         if (catalogoDosisVacunaOld != null) {
            catalogoVacunaView.getCatalogoDosisVacunaView().setIdCatalogoDosisVacuna(catalogoDosisVacunaOld.getIdCatalogoDosisVacuna());
         }
         catalogoVacuna = catalogoVacunaConverter.toEntity(catalogoVacunaView, catalogoVacuna, Boolean.TRUE);
         logger.debug("===>>>Editar Catálogo Vacuna: {}", catalogoVacuna);
         catalogoVacunaRepository.save(catalogoVacuna);
      } catch (CatalogoVacunaException cve) {
         throw cve;
      } catch (DataIntegrityViolationException dive) {
         CatalogoVacunaException cve = new CatalogoVacunaException("No fue posible editar al Catálogo Vacuna", CatalogoVacunaException.LAYER_DAO, CatalogoVacunaException.ACTION_UPDATE);
         cve.addError("Ocurrió un error al editar el Catálogo Vacuna");
         logger.error("===>>>Error al editar Catálogo Vacuna - CODE {} - {}", cve.getExceptionCode(), catalogoVacunaView, cve);
         throw cve;
      } catch (Exception ex) {
         CatalogoVacunaException cve = new CatalogoVacunaException("Error inesperado al editar el Catálogo Vacuna", CatalogoVacunaException.LAYER_DAO, CatalogoVacunaException.ACTION_UPDATE);
         cve.addError("Ocurrió un error al editar el Catálogo Vacuna");
         logger.error("===>>>Error al editar Catálogo Vacuna - CODE {} - {}", cve.getExceptionCode(), catalogoVacunaView, cve);
         throw cve;
      }
   }

   @Transactional(readOnly = false, rollbackFor = {CatalogoVacunaException.class})
   @Override
   public void deleteCatalogoVacuna(Long idCatalogoVacuna) throws CatalogoVacunaException {
      try {
         if (!catalogoVacunaRepository.exists(idCatalogoVacuna)) {
            logger.error("===>>>No se encuentra el idCatalogoVacuna");
            CatalogoVacunaException cve = new CatalogoVacunaException("No se encuentra en el sistema el Catálogo Vacuna.", CatalogoVacunaException.LAYER_DAO, CatalogoVacunaException.ACTION_VALIDATE);
            cve.addError("Catálogo de Vacuna no encontrado");
            throw cve;
         }
         CatalogoVacuna catalogoVacuna = catalogoVacunaRepository.findOne(idCatalogoVacuna);
         catalogoVacuna.setActivo(Boolean.FALSE);

         CatalogoDosisVacuna catalogoDosisVacuna = catalogoVacuna.getCatalogoDosisVacuna();
         if (catalogoDosisVacuna != null) {
            catalogoDosisVacuna.setActivo(Boolean.FALSE);
         }
         catalogoVacunaRepository.save(catalogoVacuna);
      } catch (CatalogoVacunaException cve) {
         throw cve;
      } catch (DataIntegrityViolationException dive) {
         CatalogoVacunaException cve = new CatalogoVacunaException("No fue posible eliminar al Catálogo de Vacuna", CatalogoVacunaException.LAYER_DAO, CatalogoVacunaException.ACTION_UPDATE);
         cve.addError("Ocurrió un error al eliminar el Catálogo de Vacuna");
         logger.error("===>>>Error al eliminar Catálogo de Vacuna - CODE {} - {}", cve.getExceptionCode(), idCatalogoVacuna, cve);
         throw cve;
      } catch (Exception ex) {
         CatalogoVacunaException cve = new CatalogoVacunaException("Error inesperado al eliminar el Catálogo de Vacuna", CatalogoVacunaException.LAYER_DAO, CatalogoVacunaException.ACTION_UPDATE);
         cve.addError("Ocurrió un error al eliminar el Catálogo de Vacuna");
         logger.error("===>>>Error al eliminar Catálogo de Vacuna - CODE {} - {}", cve.getExceptionCode(), idCatalogoVacuna, cve);
         throw cve;
      }
   }

   @Override
   public CatalogoVacunaView getDetailsByIdCatalogoVacuna(Long idCatalogoVacuna) throws CatalogoVacunaException {
      try {
         if (!catalogoVacunaRepository.exists(idCatalogoVacuna)) {
            logger.error("===>>>No se encuentra el idCatalogoVacuna");
            CatalogoVacunaException cve = new CatalogoVacunaException("No se encuentra en el sistema el Catálogo de Vacuna.", CatalogoVacunaException.LAYER_DAO, CatalogoVacunaException.ACTION_VALIDATE);
            cve.addError("Catálogo del Vacuna no encontrado");
            throw cve;
         }
         CatalogoVacuna catalogoVacuna = catalogoVacunaRepository.findOne(idCatalogoVacuna);
         return catalogoVacunaConverter.toView(catalogoVacuna, Boolean.TRUE);
      } catch (CatalogoVacunaException cve) {
         throw cve;
      } catch (DataIntegrityViolationException dive) {
         CatalogoVacunaException cve = new CatalogoVacunaException("No fue posible obtener detalles del Catálogo de Vacuna", CatalogoVacunaException.LAYER_DAO, CatalogoVacunaException.ACTION_SELECT);
         cve.addError("No fue posible obtener detalles del Catálogo de Vacuna");
         logger.error("===>>>Error al obtener detalles del  Catálogo de Vacuna - CODE {} - {}", cve.getExceptionCode(), idCatalogoVacuna, cve);
         throw cve;
      } catch (Exception ex) {
         CatalogoVacunaException cve = new CatalogoVacunaException("Error inesperado al obtener detalles del Catálogo de Vacuna", CatalogoVacunaException.LAYER_DAO, CatalogoVacunaException.ACTION_SELECT);
         cve.addError("Ocurrió un error al obtener detalles  del Catálogo de Vacuna");
         logger.error("===>>>Error al obtener detalles del  Catálogo de Vacuna - CODE {} - {}", cve.getExceptionCode(), idCatalogoVacuna, cve);
         throw cve;
      }
   }

   @Override
   public List<CatalogoVacunaView> findAll(Boolean active) throws CatalogoVacunaException {
      try {
         List<CatalogoVacuna> catalogoVacunaList = new ArrayList<>();
         List<CatalogoVacunaView> catalogoVacunaViewList = new ArrayList<>();
         if (active == null) {
            catalogoVacunaList = catalogoVacunaRepository.findAll();
         } else {
            catalogoVacunaList = catalogoVacunaRepository.findAll().stream().filter(catalogoVacuna -> catalogoVacuna.getActivo().compareTo(active) == 0).collect(Collectors.toList());
         }
         for (CatalogoVacuna csm : catalogoVacunaList) {
            catalogoVacunaViewList.add(catalogoVacunaConverter.toView(csm, Boolean.TRUE));
         }
         return catalogoVacunaViewList;
      } catch (Exception ex) {
         CatalogoVacunaException cve = new CatalogoVacunaException("Ocurrio un error al seleccionar lista del Catálogo de Vacuna", CatalogoVacunaException.LAYER_SERVICE, CatalogoVacunaException.ACTION_SELECT);
         logger.error(ExceptionServiceCode.GROUP + "===>>>Error al tratar de seleccionar lista del Catálogo de Vacuna - CODE: {}-{}", cve.getExceptionCode(), ex);
         throw cve;
      }
   }

   @Override
   public Page<CatalogoVacunaView> getCatalogoVacunaPage(Boolean active, String name, Integer page, Integer size, String orderColumn, String orderType) throws CatalogoVacunaException {
      try {
         logger.info("===>>>Obtener listado de Catálogo de Vacuna paginable: active: {} - name {} - page: {} - size: {} - orderColumn: {} - orderType: {}",
            active, name, page, size, orderColumn, orderType);
         List<CatalogoVacunaView> catalogoVacunaViewList = new ArrayList<>();
         Page<CatalogoVacuna> catalogoVacunaPage = null;
         Sort sort = new Sort(Sort.Direction.ASC, (String) colOrderNames.get("nombreVacuna"));

         if (orderColumn != null && orderType != null) {
            if (orderType.equalsIgnoreCase("asc")) {
               sort = new Sort(Sort.Direction.ASC, (String) colOrderNames.get(orderColumn));
            } else {
               sort = new Sort(Sort.Direction.DESC, (String) colOrderNames.get(orderColumn));
            }

         }
         PageRequest request = new PageRequest(page, size, sort);
         final String patternSearch = "%" + name.toLowerCase() + "%";
         Specifications<CatalogoVacuna> spec = Specifications.where(
            (root, query, cb) -> {
               Predicate tc = null;
               if (name != null && !name.isEmpty()) {
                  tc = (tc != null ? cb.and(tc, cb.like(cb.lower(root.get("nombreVacuna")), patternSearch)) : cb.like(cb.lower(root.get("nombreVacuna")), patternSearch));
               }
               if (active != null) {
                  tc = (tc != null ? cb.and(tc, cb.equal(root.get("activo"), active)) : cb.equal(root.get("activo"), active));
               }
               return tc;
            }
         );

         if (spec == null) {
            catalogoVacunaPage = catalogoVacunaRepository.findAll(request);
         } else {
            catalogoVacunaPage = catalogoVacunaRepository.findAll(spec, request);
         }

         catalogoVacunaPage.getContent().forEach(catalogoVacuna -> {
            // catalogoVacunaViewList.add(catalogoVacunaConverter.toViewPage(catalogoVacuna));
            catalogoVacunaViewList.add(catalogoVacunaConverter.toView(catalogoVacuna, Boolean.TRUE));
         });
         PageImpl<CatalogoVacunaView> catalogoVacunaViewPage = new PageImpl<CatalogoVacunaView>(catalogoVacunaViewList, request, catalogoVacunaPage.getTotalElements());
         return catalogoVacunaViewPage;
      } catch (IllegalArgumentException iae) {
         logger.error("===>>>Algun parametro no es correcto");
         CatalogoVacunaException cve = new CatalogoVacunaException("Algun parametro no es correcto", CatalogoVacunaException.LAYER_SERVICE, CatalogoVacunaException.ACTION_VALIDATE);
         cve.addError("Puede que sea null, vacio o valor incorrecto");
         throw cve;
      } catch (Exception ex) {
         CatalogoVacunaException cve = new CatalogoVacunaException("Ocurrio un error al seleccionar lista del Catálogo de Vacuna paginable", CatalogoVacunaException.LAYER_SERVICE, CatalogoVacunaException.ACTION_SELECT);
         logger.error(ExceptionServiceCode.GROUP + "===>>>Error al tratar de seleccionar lista del Catálogo de Vacuna paginable - CODE: {}-{}", cve.getExceptionCode(), ex);
         throw cve;
      }
   }


}
