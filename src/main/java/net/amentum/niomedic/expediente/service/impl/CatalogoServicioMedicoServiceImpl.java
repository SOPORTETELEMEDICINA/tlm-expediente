package net.amentum.niomedic.expediente.service.impl;

import net.amentum.niomedic.expediente.converter.CatalogoServicioMedicoConverter;
import net.amentum.niomedic.expediente.exception.CatalogoServicioMedicoException;
import net.amentum.niomedic.expediente.exception.ExceptionServiceCode;
import net.amentum.niomedic.expediente.model.CatalogoServicioMedico;
import net.amentum.niomedic.expediente.persistence.CatalogoServicioMedicoRepository;
import net.amentum.niomedic.expediente.service.CatalogoServicioMedicoService;
import net.amentum.niomedic.expediente.views.CatalogoServicioMedicoView;
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
public class CatalogoServicioMedicoServiceImpl implements CatalogoServicioMedicoService {
   private final Logger logger = LoggerFactory.getLogger(CatalogoServicioMedicoServiceImpl.class);
   private final Map<String, Object> colOrderNames = new HashMap<>();
   private CatalogoServicioMedicoRepository catalogoServicioMedicoRepository;
   private CatalogoServicioMedicoConverter catalogoServicioMedicoConverter;

   {
      colOrderNames.put("nombre", "nombre");
   }

   @Autowired
   public void setCatalogoServicioMedicoRepository(CatalogoServicioMedicoRepository catalogoServicioMedicoRepository) {
      this.catalogoServicioMedicoRepository = catalogoServicioMedicoRepository;
   }

   @Autowired
   public void setCatalogoServicioMedicoConverter(CatalogoServicioMedicoConverter catalogoServicioMedicoConverter) {
      this.catalogoServicioMedicoConverter = catalogoServicioMedicoConverter;
   }

   @Transactional(readOnly = false, rollbackFor = {CatalogoServicioMedicoException.class})
   @Override
   public void createCatalogoServicioMedico(CatalogoServicioMedicoView catalogoServicioMedicoView) throws CatalogoServicioMedicoException {
      try {
         CatalogoServicioMedico catalogoServicioMedico = catalogoServicioMedicoConverter.toEntity(catalogoServicioMedicoView, new CatalogoServicioMedico(), Boolean.FALSE);
         logger.debug("===>>>Insertar nuevo Catálogo de Servicio Médico: {}", catalogoServicioMedico);
         catalogoServicioMedicoRepository.save(catalogoServicioMedico);
      } catch (DataIntegrityViolationException dive) {
         CatalogoServicioMedicoException csme = new CatalogoServicioMedicoException("No fue posible agregar al Catálogo del Servicio Médico", CatalogoServicioMedicoException.LAYER_DAO, CatalogoServicioMedicoException.ACTION_INSERT);
         csme.addError("Ocurrio un error al agregar el Catálogo de Servicio Médico");
         logger.error("===>>>Error al insertar nuevo Catálogo de Servicio Médico - CODE {} - {}", csme.getExceptionCode(), catalogoServicioMedicoView, csme);
         throw csme;
      } catch (Exception ex) {
         CatalogoServicioMedicoException csme = new CatalogoServicioMedicoException("Error inesperado al agregar el Catálogo de Servicio Médico", CatalogoServicioMedicoException.LAYER_DAO, CatalogoServicioMedicoException.ACTION_INSERT);
         csme.addError("Ocurrió un error al agregar el Catálogo de Servicio Médico");
         logger.error("===>>>Error al insertar nuevo Catálogo de Servicio Médico - CODE {} - {}", csme.getExceptionCode(), catalogoServicioMedicoView, csme);
         throw csme;
      }
   }

   @Transactional(readOnly = false, rollbackFor = {CatalogoServicioMedicoException.class})
   @Override
   public void updateCatalogoServicioMedico(CatalogoServicioMedicoView catalogoServicioMedicoView) throws CatalogoServicioMedicoException {
      try {
         if (!catalogoServicioMedicoRepository.exists(catalogoServicioMedicoView.getIdCatalogoServicioMedico())) {
            logger.error("===>>>No se encuentra el idCatalogoServicioMedico");
            CatalogoServicioMedicoException csme = new CatalogoServicioMedicoException("No se encuentra en el sistema el Catálogo Servicio Médico.", CatalogoServicioMedicoException.LAYER_DAO, CatalogoServicioMedicoException.ACTION_VALIDATE);
            csme.addError("Catálogo de Servicio Médico no encontrado");
            throw csme;
         }
         CatalogoServicioMedico catalogoServicioMedico = catalogoServicioMedicoRepository.findOne(catalogoServicioMedicoView.getIdCatalogoServicioMedico());
         catalogoServicioMedico = catalogoServicioMedicoConverter.toEntity(catalogoServicioMedicoView, catalogoServicioMedico, Boolean.TRUE);
         logger.debug("===>>>Editar Catálogo de Servicio Médico: {}", catalogoServicioMedico);
         catalogoServicioMedicoRepository.save(catalogoServicioMedico);
      } catch (CatalogoServicioMedicoException csme) {
         throw csme;
      } catch (DataIntegrityViolationException dive) {
         CatalogoServicioMedicoException csme = new CatalogoServicioMedicoException("No fue posible editar al Catálogo del Servicio Médico", CatalogoServicioMedicoException.LAYER_DAO, CatalogoServicioMedicoException.ACTION_UPDATE);
         csme.addError("Ocurrió un error al editar el Catálogo de Servicio Médico");
         logger.error("===>>>Error al editar Catálogo de Servicio Médico - CODE {} - {}", csme.getExceptionCode(), catalogoServicioMedicoView, csme);
         throw csme;
      } catch (Exception ex) {
         CatalogoServicioMedicoException csme = new CatalogoServicioMedicoException("Error inesperado al agregar el Ctálogo de Servicio Médico", CatalogoServicioMedicoException.LAYER_DAO, CatalogoServicioMedicoException.ACTION_UPDATE);
         csme.addError("Ocurrió un error al editar el Catálogo de Servicio Médico");
         logger.error("===>>>Error al editar Catálogo de Servicio Médico - CODE {} - {}", csme.getExceptionCode(), catalogoServicioMedicoView, csme);
         throw csme;
      }
   }

   @Transactional(readOnly = false, rollbackFor = {CatalogoServicioMedicoException.class})
   @Override
   public void deleteCatalogoServicioMedico(Long idCatalogoServicioMedico) throws CatalogoServicioMedicoException {
      try {
         if (!catalogoServicioMedicoRepository.exists(idCatalogoServicioMedico)) {
            logger.error("===>>>No se encuentra el idCatalogoServicioMedico");
            CatalogoServicioMedicoException me = new CatalogoServicioMedicoException("No se encuentra en el sistema el Catálogo Servicio Médico.", CatalogoServicioMedicoException.LAYER_DAO, CatalogoServicioMedicoException.ACTION_VALIDATE);
            me.addError("Catálogo de Servicio Médico no encontrado");
            throw me;
         }
         CatalogoServicioMedico catalogoServicioMedico = catalogoServicioMedicoRepository.findOne(idCatalogoServicioMedico);
         catalogoServicioMedico.setActivo(Boolean.FALSE);
         catalogoServicioMedicoRepository.save(catalogoServicioMedico);
      } catch (CatalogoServicioMedicoException csme) {
         throw csme;
      } catch (DataIntegrityViolationException dive) {
         CatalogoServicioMedicoException csme = new CatalogoServicioMedicoException("No fue posible eliminar al Catálogo del Servicio Médico", CatalogoServicioMedicoException.LAYER_DAO, CatalogoServicioMedicoException.ACTION_UPDATE);
         csme.addError("Ocurrió un error al eliminar el Catálogo de Servicio Médico");
         logger.error("===>>>Error al eliminar Catálogo de Servicio Médico - CODE {} - {}", csme.getExceptionCode(), idCatalogoServicioMedico, csme);
         throw csme;
      } catch (Exception ex) {
         CatalogoServicioMedicoException csme = new CatalogoServicioMedicoException("Error inesperado al eliminar el Ctálogo de Servicio Médico", CatalogoServicioMedicoException.LAYER_DAO, CatalogoServicioMedicoException.ACTION_UPDATE);
         csme.addError("Ocurrió un error al eliminar el Catálogo de Servicio Médico");
         logger.error("===>>>Error al eliminar Catálogo de Servicio Médico - CODE {} - {}", csme.getExceptionCode(), idCatalogoServicioMedico, csme);
         throw csme;
      }
   }

   @Override
   public CatalogoServicioMedicoView getDetailsByIdCatalogoServicioMedico(Long idCatalogoServicioMedico) throws CatalogoServicioMedicoException {
      try {
         if (!catalogoServicioMedicoRepository.exists(idCatalogoServicioMedico)) {
            logger.error("===>>>No se encuentra el idCatalogoServicioMedico");
            CatalogoServicioMedicoException csme = new CatalogoServicioMedicoException("No se encuentra el el sistema el Catálogo del Servicio Médico.", CatalogoServicioMedicoException.LAYER_DAO, CatalogoServicioMedicoException.ACTION_VALIDATE);
            csme.addError("Catálogo del Servicio Médico no encontrado");
            throw csme;
         }
         CatalogoServicioMedico catalogoServicioMedico = catalogoServicioMedicoRepository.findOne(idCatalogoServicioMedico);
         return catalogoServicioMedicoConverter.toView(catalogoServicioMedico, Boolean.TRUE);
      } catch (CatalogoServicioMedicoException csme) {
         throw csme;
      } catch (DataIntegrityViolationException dive) {
         CatalogoServicioMedicoException csme = new CatalogoServicioMedicoException("No fue posible obtener detalles al Catálogo del Servicio Médico", CatalogoServicioMedicoException.LAYER_DAO, CatalogoServicioMedicoException.ACTION_SELECT);
         csme.addError("No fue posible obtener detalles del Catálogo de Servicio Médico");
         logger.error("===>>>Error al obtener detalles del  Catálogo de Servicio Médico - CODE {} - {}", csme.getExceptionCode(), idCatalogoServicioMedico, csme);
         throw csme;
      } catch (Exception ex) {
         CatalogoServicioMedicoException csme = new CatalogoServicioMedicoException("Error inesperado al obtener detalles del Catálogo de Servicio Médico", CatalogoServicioMedicoException.LAYER_DAO, CatalogoServicioMedicoException.ACTION_SELECT);
         csme.addError("Ocurrió un error al obtener detalles  el Catálogo de Servicio Médico");
         logger.error("===>>>Error al obtener detalles del  Catálogo de Servicio Médico - CODE {} - {}", csme.getExceptionCode(), idCatalogoServicioMedico, csme);
         throw csme;
      }
   }

   @Override
   public List<CatalogoServicioMedicoView> findAll(Boolean active) throws CatalogoServicioMedicoException {
      try {
         List<CatalogoServicioMedico> catalogoServicioMedicoList = new ArrayList<>();
         List<CatalogoServicioMedicoView> catalogoServicioMedicoViewList = new ArrayList<>();
         if (active == null) {
            catalogoServicioMedicoList = catalogoServicioMedicoRepository.findAll();
         } else {
            catalogoServicioMedicoList = catalogoServicioMedicoRepository.findAll().stream().filter(catalogoServicioMedico -> catalogoServicioMedico.getActivo().compareTo(active) == 0).collect(Collectors.toList());
         }
         for (CatalogoServicioMedico csm : catalogoServicioMedicoList) {
            catalogoServicioMedicoViewList.add(catalogoServicioMedicoConverter.toView(csm, Boolean.TRUE));
         }
         return catalogoServicioMedicoViewList;
      } catch (Exception ex) {
         CatalogoServicioMedicoException catalogoServicioMedicoException = new CatalogoServicioMedicoException("Ocurrio un error al seleccionar lista del Catálogo de Servicio Medico", CatalogoServicioMedicoException.LAYER_SERVICE, CatalogoServicioMedicoException.ACTION_SELECT);
         logger.error(ExceptionServiceCode.GROUP + "===>>>Error al tratar de seleccionar lista del Catálogo de Servicio Medico - CODE: {}-{}", catalogoServicioMedicoException.getExceptionCode(), ex);
         throw catalogoServicioMedicoException;
      }
   }


   @Override
   public Page<CatalogoServicioMedicoView> getCatalogoServicioMedicoPage(Boolean active, String name, Integer page, Integer size, String orderColumn, String orderType) throws CatalogoServicioMedicoException {
      try {
         logger.info("===>>>Obtener listado de Catálogo de Servicio Servicio paginable: active: {} - name {} - page: {} - size: {} - orderColumn: {} - orderType: {}",
            active, name, page, size, orderColumn, orderType);
         List<CatalogoServicioMedicoView> catalogoServicioMedicoViewList = new ArrayList<>();
         Page<CatalogoServicioMedico> catalogoServicioMedicoPage = null;
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
         Specifications<CatalogoServicioMedico> spec = Specifications.where(
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
            catalogoServicioMedicoPage = catalogoServicioMedicoRepository.findAll(request);
         } else {
            catalogoServicioMedicoPage = catalogoServicioMedicoRepository.findAll(spec, request);
         }

         catalogoServicioMedicoPage.getContent().forEach(catalogoServicioMedico -> {
            // catalogoServicioMedicoViewList.add(catalogoServicioMedicoConverter.toViewPage(catalogoServicioMedico));
            catalogoServicioMedicoViewList.add(catalogoServicioMedicoConverter.toView(catalogoServicioMedico, Boolean.TRUE));
         });
         PageImpl<CatalogoServicioMedicoView> catalogoServicioMedicoViewPage = new PageImpl<CatalogoServicioMedicoView>(catalogoServicioMedicoViewList, request, catalogoServicioMedicoPage.getTotalElements());
         return catalogoServicioMedicoViewPage;
      } catch (IllegalArgumentException iae) {
         logger.error("===>>>Algun parametro no es correcto");
         CatalogoServicioMedicoException csme = new CatalogoServicioMedicoException("Algun parametro no es correcto", CatalogoServicioMedicoException.LAYER_SERVICE, CatalogoServicioMedicoException.ACTION_VALIDATE);
         csme.addError("Puede que sea null, vacio o valor incorrecto");
         throw csme;
      } catch (Exception ex) {
         CatalogoServicioMedicoException catalogoServicioMedicoException = new CatalogoServicioMedicoException("Ocurrio un error al seleccionar lista del Catálogo de Servicio Medico paginable", CatalogoServicioMedicoException.LAYER_SERVICE, CatalogoServicioMedicoException.ACTION_SELECT);
         logger.error(ExceptionServiceCode.GROUP + "===>>>Error al tratar de seleccionar lista del Catálogo de Servicio Medico paginable - CODE: {}-{}", catalogoServicioMedicoException.getExceptionCode(), ex);
         throw catalogoServicioMedicoException;
      }
   }


}
