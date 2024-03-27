package net.amentum.niomedic.expediente.service.impl;

import net.amentum.niomedic.expediente.converter.CatalogoDrogasConverter;
import net.amentum.niomedic.expediente.exception.CatalogoDrogasException;
import net.amentum.niomedic.expediente.exception.ExceptionServiceCode;
import net.amentum.niomedic.expediente.model.CatalogoDrogas;
import net.amentum.niomedic.expediente.persistence.CatalogoDrogasRepository;
import net.amentum.niomedic.expediente.service.CatalogoDrogasService;
import net.amentum.niomedic.expediente.views.CatalogoDrogasView;
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
public class CatalogoDrogasServiceImpl implements CatalogoDrogasService {
   private final Logger logger = LoggerFactory.getLogger(CatalogoDrogasServiceImpl.class);
   private final Map<String, Object> colOrderNames = new HashMap<>();
   private CatalogoDrogasRepository catalogoDrogasRepository;
   private CatalogoDrogasConverter catalogoDrogasConverter;

   {
      colOrderNames.put("nombreDroga", "nombreDroga");
      colOrderNames.put("unidadMedida", "unidadMedida");
   }

   @Autowired
   public void setCatalogoDrogasRepository(CatalogoDrogasRepository catalogoDrogasRepository) {
      this.catalogoDrogasRepository = catalogoDrogasRepository;
   }

   @Autowired
   public void setCatalogoDrogasConverter(CatalogoDrogasConverter catalogoDrogasConverter) {
      this.catalogoDrogasConverter = catalogoDrogasConverter;
   }

   @Transactional(readOnly = false, rollbackFor = {CatalogoDrogasException.class})
   @Override
   public void createCatalogoDrogas(CatalogoDrogasView catalogoDrogasView) throws CatalogoDrogasException {
      try {
         CatalogoDrogas catalogoDrogas = catalogoDrogasConverter.toEntity(catalogoDrogasView, new CatalogoDrogas(), Boolean.FALSE);
         logger.debug("===>>>Insertar nuevo Catálogo Drogas: {}", catalogoDrogas);
         catalogoDrogasRepository.save(catalogoDrogas);
      } catch (DataIntegrityViolationException dive) {
         CatalogoDrogasException cde = new CatalogoDrogasException("No fue posible agregar Catálogo Drogas", CatalogoDrogasException.LAYER_DAO, CatalogoDrogasException.ACTION_INSERT);
         cde.addError("Ocurrio un error al agregar Catálogo Drogas");
         logger.error("===>>>Error al insertar nuevo Catálogo Drogas - CODE {} - {}", cde.getExceptionCode(), catalogoDrogasView, cde);
         throw cde;
      } catch (Exception ex) {
         CatalogoDrogasException cde = new CatalogoDrogasException("Error inesperado al agregar  Catálogo Drogas", CatalogoDrogasException.LAYER_DAO, CatalogoDrogasException.ACTION_INSERT);
         cde.addError("Ocurrió un error al agregar Catálogo Drogas");
         logger.error("===>>>Error al insertar nuevo Catálogo Drogas - CODE {} - {}", cde.getExceptionCode(), catalogoDrogasView, cde);
         throw cde;
      }
   }

   @Transactional(readOnly = false, rollbackFor = {CatalogoDrogasException.class})
   @Override
   public void updateCatalogoDrogas(CatalogoDrogasView catalogoDrogasView) throws CatalogoDrogasException {
      try {
         if (!catalogoDrogasRepository.exists(catalogoDrogasView.getIdCatalogoDrogas())) {
            logger.error("===>>>No se encuentra el idCatalogoDroga");
            CatalogoDrogasException cde = new CatalogoDrogasException("No se encuentra en el sistema Catálogo Drogas.", CatalogoDrogasException.LAYER_DAO, CatalogoDrogasException.ACTION_VALIDATE);
            cde.addError("Catálogo Drogas no encontrado");
            throw cde;
         }
         CatalogoDrogas catalogoDrogas = catalogoDrogasRepository.findOne(catalogoDrogasView.getIdCatalogoDrogas());
         catalogoDrogas = catalogoDrogasConverter.toEntity(catalogoDrogasView, catalogoDrogas, Boolean.TRUE);
         logger.debug("===>>>Editar Catálogo Drogas: {}", catalogoDrogas);
         catalogoDrogasRepository.save(catalogoDrogas);
      } catch (CatalogoDrogasException cde) {
         throw cde;
      } catch (DataIntegrityViolationException dive) {
         CatalogoDrogasException cde = new CatalogoDrogasException("No fue posible editar Catálogo Drogas", CatalogoDrogasException.LAYER_DAO, CatalogoDrogasException.ACTION_UPDATE);
         cde.addError("Ocurrió un error al editar Catálogo Drogas");
         logger.error("===>>>Error al editar Catálogo Drogas - CODE {} - {}", cde.getExceptionCode(), catalogoDrogasView, cde);
         throw cde;
      } catch (Exception ex) {
         CatalogoDrogasException cde = new CatalogoDrogasException("Error inesperado al agregar Catálogo Drogas", CatalogoDrogasException.LAYER_DAO, CatalogoDrogasException.ACTION_UPDATE);
         cde.addError("Ocurrió un error al editar Catálogo  Drogas");
         logger.error("===>>>Error al editar Catálogo Drogas - CODE {} - {}", cde.getExceptionCode(), catalogoDrogasView, cde);
         throw cde;
      }
   }

   @Transactional(readOnly = false, rollbackFor = {CatalogoDrogasException.class})
   @Override
   public void deleteCatalogoDrogas(Long idCatalogoDroga) throws CatalogoDrogasException {
      try {
         if (!catalogoDrogasRepository.exists(idCatalogoDroga)) {
            logger.error("===>>>No se encuentra el idCatalogoDroga");
            CatalogoDrogasException cde = new CatalogoDrogasException("No se encuentra en el sistema Catálogo Drogas.", CatalogoDrogasException.LAYER_DAO, CatalogoDrogasException.ACTION_VALIDATE);
            cde.addError("Catálogo Drogas no encontrado");
            throw cde;
         }
         CatalogoDrogas catalogoDrogas = catalogoDrogasRepository.findOne(idCatalogoDroga);
         catalogoDrogas.setActivo(Boolean.FALSE);
         catalogoDrogasRepository.save(catalogoDrogas);
      } catch (CatalogoDrogasException cde) {
         throw cde;
      } catch (DataIntegrityViolationException dive) {
         CatalogoDrogasException cde = new CatalogoDrogasException("No fue posible eliminar Catálogo Drogas", CatalogoDrogasException.LAYER_DAO, CatalogoDrogasException.ACTION_UPDATE);
         cde.addError("Ocurrió un error al eliminar Catálogo Drogas");
         logger.error("===>>>Error al eliminar Catálogo Drogas - CODE {} - {}", cde.getExceptionCode(), idCatalogoDroga, cde);
         throw cde;
      } catch (Exception ex) {
         CatalogoDrogasException cde = new CatalogoDrogasException("Error inesperado al eliminar Catálogo Drogas", CatalogoDrogasException.LAYER_DAO, CatalogoDrogasException.ACTION_UPDATE);
         cde.addError("Ocurrió un error al eliminar Catálogo Drogas");
         logger.error("===>>>Error al eliminar Catálogo Drogas - CODE {} - {}", cde.getExceptionCode(), idCatalogoDroga, cde);
         throw cde;
      }
   }

   @Override
   public CatalogoDrogasView getDetailsByIdCatalogoDrogas(Long idCatalogoDroga) throws CatalogoDrogasException {
      try {
         if (!catalogoDrogasRepository.exists(idCatalogoDroga)) {
            logger.error("===>>>No se encuentra el idCatalogoDroga");
            CatalogoDrogasException cde = new CatalogoDrogasException("No se encuentra el el sistema Catálogo Drogas.", CatalogoDrogasException.LAYER_DAO, CatalogoDrogasException.ACTION_VALIDATE);
            cde.addError("Catálogo Drogas no encontrado");
            throw cde;
         }
         CatalogoDrogas catalogoDrogas = catalogoDrogasRepository.findOne(idCatalogoDroga);
         return catalogoDrogasConverter.toView(catalogoDrogas, Boolean.TRUE);
      } catch (CatalogoDrogasException cde) {
         throw cde;
      } catch (DataIntegrityViolationException dive) {
         CatalogoDrogasException cde = new CatalogoDrogasException("No fue posible obtener detalles  Catálogo Drogas", CatalogoDrogasException.LAYER_DAO, CatalogoDrogasException.ACTION_SELECT);
         cde.addError("No fue posible obtener detalles Catálogo Drogas");
         logger.error("===>>>Error al obtener detalles Catálogo Drogas - CODE {} - {}", cde.getExceptionCode(), idCatalogoDroga, cde);
         throw cde;
      } catch (Exception ex) {
         CatalogoDrogasException cde = new CatalogoDrogasException("Error inesperado al obtener detalles Catálogo Drogas", CatalogoDrogasException.LAYER_DAO, CatalogoDrogasException.ACTION_SELECT);
         cde.addError("Ocurrió un error al obtener detalles Catálogo Drogas");
         logger.error("===>>>Error al obtener detalles Catálogo Drogas - CODE {} - {}", cde.getExceptionCode(), idCatalogoDroga, cde);
         throw cde;
      }
   }

   @Override
   public List<CatalogoDrogasView> findAll(Boolean active) throws CatalogoDrogasException {
      try {
         List<CatalogoDrogas> catalogoDrogasList = new ArrayList<>();
         List<CatalogoDrogasView> catalogoDrogasViewList = new ArrayList<>();
         if (active == null) {
            catalogoDrogasList = catalogoDrogasRepository.findAll();
         } else {
            catalogoDrogasList = catalogoDrogasRepository.findAll().stream().filter(catalogoDrogas -> catalogoDrogas.getActivo().compareTo(active) == 0).collect(Collectors.toList());
         }
         for (CatalogoDrogas csm : catalogoDrogasList) {
            catalogoDrogasViewList.add(catalogoDrogasConverter.toView(csm, Boolean.TRUE));
         }
         return catalogoDrogasViewList;
      } catch (Exception ex) {
         CatalogoDrogasException cde = new CatalogoDrogasException("Ocurrio un error al seleccionar lista Catálogo Drogas", CatalogoDrogasException.LAYER_SERVICE, CatalogoDrogasException.ACTION_SELECT);
         logger.error(ExceptionServiceCode.GROUP + "===>>>Error al tratar de seleccionar lista Catálogo Drogas - CODE: {}-{}", cde.getExceptionCode(), ex);
         throw cde;
      }
   }

   @Override
   public Page<CatalogoDrogasView> getCatalogoDrogasPage(Boolean active, String name, Integer page, Integer size, String orderColumn, String orderType) throws CatalogoDrogasException {
      try {
         logger.info("===>>>Obtener listado de Catálogo de Vacuna paginable: active: {} - name {} - page: {} - size: {} - orderColumn: {} - orderType: {}",
            active, name, page, size, orderColumn, orderType);
         List<CatalogoDrogasView> catalogoDrogasViewList = new ArrayList<>();
         Page<CatalogoDrogas> catalogoDrogasPage = null;
         Sort sort = new Sort(Sort.Direction.ASC, (String) colOrderNames.get("nombreDroga"));

         if (orderColumn != null && orderType != null) {
            if (orderType.equalsIgnoreCase("asc")) {
               sort = new Sort(Sort.Direction.ASC, (String) colOrderNames.get(orderColumn));
            } else {
               sort = new Sort(Sort.Direction.DESC, (String) colOrderNames.get(orderColumn));
            }

         }
         PageRequest request = new PageRequest(page, size, sort);
         final String patternSearch = "%" + name.toLowerCase() + "%";
         Specifications<CatalogoDrogas> spec = Specifications.where(
            (root, query, cb) -> {
               Predicate tc = null;
               if (name != null && !name.isEmpty()) {
                  tc = (tc != null ? cb.and(tc, cb.like(cb.lower(root.get("nombreDroga")), patternSearch)) : cb.like(cb.lower(root.get("nombreDroga")), patternSearch));
               }
               if (active != null) {
                  tc = (tc != null ? cb.and(tc, cb.equal(root.get("activo"), active)) : cb.equal(root.get("activo"), active));
               }
               return tc;
            }
         );

         if (spec == null) {
            catalogoDrogasPage = catalogoDrogasRepository.findAll(request);
         } else {
            catalogoDrogasPage = catalogoDrogasRepository.findAll(spec, request);
         }

         catalogoDrogasPage.getContent().forEach(catalogoDrogas -> {
            // catalogoDrogasViewList.add(catalogoDrogasConverter.toViewPage(catalogoDrogas));
            catalogoDrogasViewList.add(catalogoDrogasConverter.toView(catalogoDrogas, Boolean.TRUE));
         });
         PageImpl<CatalogoDrogasView> catalogoDrogasViewPage = new PageImpl<CatalogoDrogasView>(catalogoDrogasViewList, request, catalogoDrogasPage.getTotalElements());
         return catalogoDrogasViewPage;
      }catch (IllegalArgumentException iae) {
         logger.error("===>>>Algun parametro no es correcto");
         CatalogoDrogasException cde = new CatalogoDrogasException("Algun parametro no es correcto", CatalogoDrogasException.LAYER_SERVICE, CatalogoDrogasException.ACTION_VALIDATE);
         cde.addError("Puede que sea null, vacio o valor incorrecto");
         throw cde;
      }
      catch (Exception ex) {
         CatalogoDrogasException cde = new CatalogoDrogasException("Ocurrio un error al seleccionar lista Catálogo Drogas paginable", CatalogoDrogasException.LAYER_SERVICE, CatalogoDrogasException.ACTION_SELECT);
         logger.error(ExceptionServiceCode.GROUP + "===>>>Error al tratar de seleccionar lista Catálogo Drogas paginable - CODE: {}-{}", cde.getExceptionCode(), ex);
         throw cde;
      }
   }

}
