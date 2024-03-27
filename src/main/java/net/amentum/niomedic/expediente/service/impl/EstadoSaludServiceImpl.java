package net.amentum.niomedic.expediente.service.impl;

import net.amentum.common.TimeUtils;
import net.amentum.niomedic.expediente.converter.EstadoSaludConverter;
import net.amentum.niomedic.expediente.exception.EstadoSaludException;
import net.amentum.niomedic.expediente.exception.ExceptionServiceCode;
import net.amentum.niomedic.expediente.model.EstadoSalud;
import net.amentum.niomedic.expediente.persistence.EstadoSaludRepository;
import net.amentum.niomedic.expediente.service.EstadoSaludService;
import net.amentum.niomedic.expediente.views.EstadoSaludView;
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
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static net.amentum.common.TimeUtils.parseDate;


@Service
@Transactional(readOnly = true)
public class EstadoSaludServiceImpl implements EstadoSaludService {
   private final Logger logger = LoggerFactory.getLogger(EstadoSaludServiceImpl.class);
   private final Map<String, Object> colOrderNames = new HashMap<>();
   private EstadoSaludRepository estadoSaludRepository;
   private EstadoSaludConverter estadoSaludConverter;


   {
      colOrderNames.put("fechaCreacion", "fechaCreacion");
      colOrderNames.put("idPaciente", "idPaciente");
   }

   @Autowired
   public void setEstadoSaludRepository(EstadoSaludRepository estadoSaludRepository) {
      this.estadoSaludRepository = estadoSaludRepository;
   }

   @Autowired
   public void setEstadoSaludConverter(EstadoSaludConverter estadoSaludConverter) {
      this.estadoSaludConverter = estadoSaludConverter;
   }

   @Transactional(readOnly = false, rollbackFor = {EstadoSaludException.class})
   @Override
   public EstadoSaludView createEstadoSalud(EstadoSaludView estadoSaludView) throws EstadoSaludException {
      try {
         if (estadoSaludView.getDolometroId() < 0 || estadoSaludView.getDolometroId() > 5) {
            String textoError = "No existe en el catalogo el valor introducido (del 1 al 5)";
            logger.error("===>>>" + textoError);
            EstadoSaludException edoSalE = new EstadoSaludException("Existe un Error", EstadoSaludException.LAYER_DAO, EstadoSaludException.ACTION_VALIDATE);
            edoSalE.addError(textoError);
            throw edoSalE;
         }

         if (estadoSaludView.getDolometroId() != estadoSaludView.getCatDolometroView().getIdCatDolometro()) {
            String textoError = "No concuerdan los valores con el catalogo y el valor introducido";
            logger.error("===>>>" + textoError);
            EstadoSaludException edoSalE = new EstadoSaludException("Existe un Error", EstadoSaludException.LAYER_DAO, EstadoSaludException.ACTION_VALIDATE);
            edoSalE.addError(textoError);
            throw edoSalE;
         }

         estadoSaludView.set_doloDescripcion(estadoSaludView.getCatDolometroView().getDoloDescripcion());

         EstadoSalud estadoSalud = estadoSaludConverter.toEntity(estadoSaludView, new EstadoSalud(), Boolean.FALSE);
         logger.debug("===>>>Insertar nuevo estadoSalud: {}", estadoSalud);
         estadoSaludRepository.save(estadoSalud);
         return estadoSaludConverter.toView(estadoSalud, Boolean.TRUE);

      } catch (EstadoSaludException edoSalE) {
         throw edoSalE;
      } catch (ConstraintViolationException cve) {
         logger.error("===>>>Error en la validacion");
         EstadoSaludException edoSalE = new EstadoSaludException("Error en la validacion", EstadoSaludException.LAYER_DAO, EstadoSaludException.ACTION_VALIDATE);
         final Set<ConstraintViolation<?>> violaciones = cve.getConstraintViolations();
         for (Iterator<ConstraintViolation<?>> iterator = violaciones.iterator(); iterator.hasNext(); ) {
            ConstraintViolation<?> siguiente = iterator.next();
            edoSalE.addError(siguiente.getPropertyPath() + " " + siguiente.getMessage());
         }
         throw edoSalE;
      } catch (DataIntegrityViolationException dive) {
         EstadoSaludException edoSalE = new EstadoSaludException("No fue posible agregar  EstadoSalud", EstadoSaludException.LAYER_DAO, EstadoSaludException.ACTION_INSERT);
         edoSalE.addError("Ocurrio un error al agregar EstadoSalud");
         logger.error("===>>>Error al insertar nuevo EstadoSalud - CODE: {} - {}", edoSalE.getExceptionCode(), estadoSaludView, dive);
         throw edoSalE;
      } catch (Exception ex) {
         EstadoSaludException edoSalE = new EstadoSaludException("Error inesperado al agregar  EstadoSalud", EstadoSaludException.LAYER_DAO, EstadoSaludException.ACTION_INSERT);
         edoSalE.addError("Ocurrio un error al agregar EstadoSalud");
         logger.error("===>>>Error al insertar nuevo EstadoSalud - CODE: {} - {}", edoSalE.getExceptionCode(), estadoSaludView, ex);
         throw edoSalE;
      }
   }

   @Transactional(readOnly = false, rollbackFor = {EstadoSaludException.class})
   @Override
   public EstadoSaludView updateEstadoSalud(EstadoSaludView estadoSaludView) throws EstadoSaludException {
      try {
         if (estadoSaludView.getDolometroId() < 0 || estadoSaludView.getDolometroId() > 5) {
            String textoError = "No existe en el catalogo el valor introducido (del 1 al 5)";
            logger.error("===>>>" + textoError);
            EstadoSaludException edoSalE = new EstadoSaludException("Existe un Error", EstadoSaludException.LAYER_DAO, EstadoSaludException.ACTION_VALIDATE);
            edoSalE.addError(textoError);
            throw edoSalE;
         }

         if (estadoSaludView.getDolometroId() != estadoSaludView.getCatDolometroView().getIdCatDolometro()) {
            String textoError = "No concuerdan los valores con el catalogo y el valor introducido";
            logger.error("===>>>" + textoError);
            EstadoSaludException edoSalE = new EstadoSaludException("Existe un Error", EstadoSaludException.LAYER_DAO, EstadoSaludException.ACTION_VALIDATE);
            edoSalE.addError(textoError);
            throw edoSalE;
         }

         if (!estadoSaludRepository.exists(estadoSaludView.getIdEstadoSalud())) {
            String textoError = "idEstadoSalud no encontrado " + estadoSaludView.getIdEstadoSalud();
            logger.error("===>>>" + textoError);
            EstadoSaludException edoSalE = new EstadoSaludException("Existe un Error", EstadoSaludException.LAYER_DAO, EstadoSaludException.ACTION_VALIDATE);
            edoSalE.addError(textoError);
            throw edoSalE;
         }

         estadoSaludView.set_doloDescripcion(estadoSaludView.getCatDolometroView().getDoloDescripcion());

         EstadoSalud estadoSalud = estadoSaludRepository.findOne(estadoSaludView.getIdEstadoSalud());

         estadoSalud = estadoSaludConverter.toEntity(estadoSaludView, estadoSalud, Boolean.TRUE);
         logger.debug("===>>>Editar estadoSalud: {}", estadoSalud);
         estadoSaludRepository.save(estadoSalud);
         return estadoSaludConverter.toView(estadoSalud, Boolean.TRUE);

      } catch (EstadoSaludException edoSalE) {
         throw edoSalE;
      } catch (ConstraintViolationException cve) {
         logger.error("===>>>Error en la validacion");
         EstadoSaludException edoSalE = new EstadoSaludException("Error en la validacion", EstadoSaludException.LAYER_DAO, EstadoSaludException.ACTION_VALIDATE);
         final Set<ConstraintViolation<?>> violaciones = cve.getConstraintViolations();
         for (Iterator<ConstraintViolation<?>> iterator = violaciones.iterator(); iterator.hasNext(); ) {
            ConstraintViolation<?> siguiente = iterator.next();
            edoSalE.addError(siguiente.getPropertyPath() + " " + siguiente.getMessage());
         }
         throw edoSalE;
      } catch (DataIntegrityViolationException dive) {
         EstadoSaludException edoSalE = new EstadoSaludException("No fue posible modificar  EstadoSalud", EstadoSaludException.LAYER_DAO, EstadoSaludException.ACTION_INSERT);
         edoSalE.addError("Ocurrio un error al modificar EstadoSalud");
         logger.error("===>>>Error al modificar EstadoSalud - CODE: {} - {}", edoSalE.getExceptionCode(), estadoSaludView, dive);
         throw edoSalE;
      } catch (Exception ex) {
         EstadoSaludException edoSalE = new EstadoSaludException("Error inesperado al modificar  EstadoSalud", EstadoSaludException.LAYER_DAO, EstadoSaludException.ACTION_INSERT);
         edoSalE.addError("Ocurrio un error al modificar EstadoSalud");
         logger.error("===>>>Error al modificar EstadoSalud - CODE: {} - {}", edoSalE.getExceptionCode(), estadoSaludView, ex);
         throw edoSalE;
      }
   }


   @Override
   public Page<EstadoSaludView> getEstadoSaludPage(UUID idPaciente, Long startDate, Long endDate, Integer page, Integer size, String orderColumn, String orderType) throws EstadoSaludException {
      try {
         logger.info("===>>>getEstadoSaludPage(): - idPaciente: {} - startDate: {} - endDate: {} - page: {} - size: {} - orderColumn: {} - orderType: {}",
            idPaciente, startDate, endDate, page, size, orderColumn, orderType);

         if (idPaciente == null) {
            logger.error("===>>>idPaciente viene NULO/VACIO: {}", idPaciente);
            EstadoSaludException edoSalE = new EstadoSaludException("No se encuentra en el sistema EstadoSalud", EstadoSaludException.LAYER_DAO, EstadoSaludException.ACTION_VALIDATE);
            edoSalE.addError("idPaciente viene NULO/VACIO: " + idPaciente);
            throw edoSalE;
         }

         List<EstadoSaludView> estadoSaludViewList = new ArrayList<>();
         Page<EstadoSalud> estadoSaludPage = null;
         Sort sort = new Sort(Sort.Direction.ASC, (String) colOrderNames.get("fechaCreacion"));

         if (orderColumn != null && orderType != null) {
            if (orderType.equalsIgnoreCase("asc")) {
               sort = new Sort(Sort.Direction.ASC, (String) colOrderNames.get(orderColumn));
            } else {
               sort = new Sort(Sort.Direction.DESC, (String) colOrderNames.get(orderColumn));
            }

         }
         PageRequest request = new PageRequest(page, size, sort);
         final UUID patternSearch = idPaciente;
         Specifications<EstadoSalud> spec = Specifications.where(
            (root, query, cb) -> {
               Predicate tc = null;

               if (idPaciente != null) {
                  tc = (tc != null ?
                     cb.and(tc, cb.equal(root.get("idPaciente"), patternSearch)) : cb.equal(root.get("idPaciente"), patternSearch));
               }
//               if (active != null) {
//                  tc = (tc != null ? cb.and(tc, cb.equal(root.get("activo"), active)) : cb.equal(root.get("activo"), active));
//               }
               if (startDate != null && endDate != null) {
                  try {
                     SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                     Date inicialDate = parseDate(sdf.format(startDate) + " 00:00:00", TimeUtils.LONG_DATE);
                     Date finalDate = parseDate(sdf.format(endDate) + " 23:59:59", TimeUtils.LONG_DATE);
                     tc = (tc != null) ?
                        cb.and(tc, cb.greaterThanOrEqualTo(root.get("fechaCreacion"), inicialDate), cb.lessThanOrEqualTo(root.get("fechaCreacion"), finalDate)) :
                        cb.and(cb.greaterThanOrEqualTo(root.get("fechaCreacion"), inicialDate), cb.lessThanOrEqualTo(root.get("fechaCreacion"), finalDate));
                  } catch (Exception ex) {
                     logger.warn("Error al convertir fechas", ex);
                  }
               }
               return tc;
            }
         );

         if (spec == null) {
            estadoSaludPage = estadoSaludRepository.findAll(request);
         } else {
            estadoSaludPage = estadoSaludRepository.findAll(spec, request);
         }

         estadoSaludPage.getContent().forEach(estadoSalud -> {
            estadoSaludViewList.add(estadoSaludConverter.toView(estadoSalud, Boolean.FALSE));
         });
         PageImpl<EstadoSaludView> estadoSaludViewPage = new PageImpl<EstadoSaludView>(estadoSaludViewList, request, estadoSaludPage.getTotalElements());
         return estadoSaludViewPage;
      } catch (EstadoSaludException edoSalE) {
         throw edoSalE;
      } catch (IllegalArgumentException iae) {
         logger.error("===>>>Algun parametro no es correcto");
         EstadoSaludException pe = new EstadoSaludException("Algun parametro no es correcto:", EstadoSaludException.LAYER_SERVICE, EstadoSaludException.ACTION_VALIDATE);
         pe.addError("Puede que sea null, vacio o valor incorrecto");
         throw pe;
      } catch (Exception ex) {
         EstadoSaludException edoSalE = new EstadoSaludException("Ocurrio un error al seleccionar lista EstadoSalud paginable", EstadoSaludException.LAYER_SERVICE, EstadoSaludException.ACTION_SELECT);
         logger.error(ExceptionServiceCode.GROUP + "===>>>Error al tratar de seleccionar lista EstadoSalud paginable - CODE: {}", edoSalE.getExceptionCode(), ex);
         throw edoSalE;
      }
   }

   private String sinAcentos(String cadena) {
      return Normalizer.normalize(cadena, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
   }

   @Override
   public EstadoSaludView getLastEstadoSalud(UUID idPaciente) throws EstadoSaludException{
      try {
         EstadoSalud estadoSalud = estadoSaludRepository.getLastEstadoSalud(idPaciente);
         if (estadoSalud==null) {
            String textoError = "No se encuentra registro";
            logger.error("===>>>" + textoError);
            EstadoSaludException edoSalE = new EstadoSaludException("Existe un Error", EstadoSaludException.LAYER_DAO, EstadoSaludException.ACTION_VALIDATE);
            edoSalE.addError(textoError);
            throw edoSalE;
         }

         return estadoSaludConverter.toView(estadoSalud, Boolean.TRUE);

      } catch (EstadoSaludException edoSalE) {
         throw edoSalE;
      } catch (ConstraintViolationException cve) {
         logger.error("===>>>Error en la validacion");
         EstadoSaludException edoSalE = new EstadoSaludException("Error en la validacion", EstadoSaludException.LAYER_DAO, EstadoSaludException.ACTION_VALIDATE);
         final Set<ConstraintViolation<?>> violaciones = cve.getConstraintViolations();
         for (Iterator<ConstraintViolation<?>> iterator = violaciones.iterator(); iterator.hasNext(); ) {
            ConstraintViolation<?> siguiente = iterator.next();
            edoSalE.addError(siguiente.getPropertyPath() + ": " + siguiente.getMessage());
         }
         throw edoSalE;
      } catch (DataIntegrityViolationException dive) {
         EstadoSaludException edoSalE = new EstadoSaludException("No fue posible obtener  EstadoSalud", EstadoSaludException.LAYER_DAO, EstadoSaludException.ACTION_INSERT);
         edoSalE.addError("Ocurrio un error al obtener EstadoSalud");
         logger.error("===>>>Error al obtener nuevo EstadoSalud - CODE: {} - {}", edoSalE.getExceptionCode(), idPaciente, dive);
         throw edoSalE;
      } catch (Exception ex) {
         EstadoSaludException edoSalE = new EstadoSaludException("Error inesperado al obtener  EstadoSalud", EstadoSaludException.LAYER_DAO, EstadoSaludException.ACTION_INSERT);
         edoSalE.addError("Ocurrio un error al obtener EstadoSalud");
         logger.error("===>>>Error al obtener nuevo EstadoSalud - CODE: {} - {}", edoSalE.getExceptionCode(), idPaciente, ex);
         throw edoSalE;
      }
   }
}
