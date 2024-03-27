package net.amentum.niomedic.expediente.service.impl;

import net.amentum.common.TimeUtils;
import net.amentum.niomedic.expediente.converter.MedicionesPacienteConverter;
import net.amentum.niomedic.expediente.exception.ExceptionServiceCode;
import net.amentum.niomedic.expediente.exception.MedicionesPacienteException;
import net.amentum.niomedic.expediente.model.Controles;
import net.amentum.niomedic.expediente.model.MedicionesPaciente;
import net.amentum.niomedic.expediente.persistence.ControlesRepository;
import net.amentum.niomedic.expediente.persistence.MedicionesPacienteRepository;
import net.amentum.niomedic.expediente.service.MedicionesPacienteService;
import net.amentum.niomedic.expediente.views.MedicionesPacienteView;
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
public class MedicionesPacienteServiceImpl implements MedicionesPacienteService {
   private final Logger logger = LoggerFactory.getLogger(MedicionesPacienteServiceImpl.class);
   private final Map<String, Object> colOrderNames = new HashMap<>();
   private MedicionesPacienteRepository medicionesPacienteRepository;
   private MedicionesPacienteConverter medicionesPacienteConverter;
   private ControlesRepository controlesRepository;

   {
      colOrderNames.put("fechaCreacion", "fechaCreacion");
      colOrderNames.put("idPaciente", "idPaciente");
   }

   @Autowired
   public void setMedicionesPacienteRepository(MedicionesPacienteRepository medicionesPacienteRepository) {
      this.medicionesPacienteRepository = medicionesPacienteRepository;
   }

   @Autowired
   public void setMedicionesPacienteConverter(MedicionesPacienteConverter medicionesPacienteConverter) {
      this.medicionesPacienteConverter = medicionesPacienteConverter;
   }

   @Autowired
   public void setControlesRepository(ControlesRepository controlesRepository) {
      this.controlesRepository = controlesRepository;
   }

   @Transactional(readOnly = false, rollbackFor = {MedicionesPacienteException.class})
   @Override
   public MedicionesPacienteView createMedicionesPaciente(MedicionesPacienteView medicionesPacienteView) throws MedicionesPacienteException {
      try {
         Controles controles = controlesRepository.findByidPaciente(medicionesPacienteView.getIdPaciente());
         if (controles == null) {
            String textoError = "No existe en el Control los valores";
            logger.error("===>>>" + textoError);
            MedicionesPacienteException medPacE = new MedicionesPacienteException("Existe un Error", MedicionesPacienteException.LAYER_DAO, MedicionesPacienteException.ACTION_VALIDATE);
            medPacE.addError(textoError);
            throw medPacE;
         }
         if (!controles.getDiabetes()) {
            medicionesPacienteView.setDiabetes(null);
         }
         if (!controles.getOximetria()) {
            medicionesPacienteView.setOxPr(null);
            medicionesPacienteView.setOxSpo2(null);
         }
         if (!controles.getHipertension()) {
            medicionesPacienteView.setHiperSistolica(null);
            medicionesPacienteView.setHiperDiastolica(null);
            medicionesPacienteView.setHiperPulso(null);
         }

         MedicionesPaciente medicionesPaciente = medicionesPacienteConverter.toEntity(medicionesPacienteView, new MedicionesPaciente(), Boolean.FALSE);
         logger.debug("===>>>Insertar nuevo medicionesPaciente: {}", medicionesPaciente);
         medicionesPacienteRepository.save(medicionesPaciente);
         return medicionesPacienteConverter.toView(medicionesPaciente, Boolean.TRUE);

      } catch (MedicionesPacienteException medPacE) {
         throw medPacE;
      } catch (ConstraintViolationException cve) {
         logger.error("===>>>Error en la validacion");
         MedicionesPacienteException medPacE = new MedicionesPacienteException("Error en la validacion", MedicionesPacienteException.LAYER_DAO, MedicionesPacienteException.ACTION_VALIDATE);
         final Set<ConstraintViolation<?>> violaciones = cve.getConstraintViolations();
         for (Iterator<ConstraintViolation<?>> iterator = violaciones.iterator(); iterator.hasNext(); ) {
            ConstraintViolation<?> siguiente = iterator.next();
            medPacE.addError(siguiente.getPropertyPath() + " " + siguiente.getMessage());
         }
         throw medPacE;
      } catch (DataIntegrityViolationException dive) {
         MedicionesPacienteException medPacE = new MedicionesPacienteException("No fue posible agregar  MedicionesPaciente", MedicionesPacienteException.LAYER_DAO, MedicionesPacienteException.ACTION_INSERT);
         medPacE.addError("Ocurrio un error al agregar MedicionesPaciente");
         logger.error("===>>>Error al insertar nuevo MedicionesPaciente - CODE: {} - {}", medPacE.getExceptionCode(), medicionesPacienteView, dive);
         throw medPacE;
      } catch (Exception ex) {
         MedicionesPacienteException medPacE = new MedicionesPacienteException("Error inesperado al agregar  MedicionesPaciente", MedicionesPacienteException.LAYER_DAO, MedicionesPacienteException.ACTION_INSERT);
         medPacE.addError("Ocurrio un error al agregar MedicionesPaciente");
         logger.error("===>>>Error al insertar nuevo MedicionesPaciente - CODE: {} - {}", medPacE.getExceptionCode(), medicionesPacienteView, ex);
         throw medPacE;
      }
   }

   @Transactional(readOnly = false, rollbackFor = {MedicionesPacienteException.class})
   @Override
   public MedicionesPacienteView updateMedicionesPaciente(MedicionesPacienteView medicionesPacienteView) throws MedicionesPacienteException {
      try {
         if (!medicionesPacienteRepository.exists(medicionesPacienteView.getIdMedicionesPaciente())) {
            String textoError = "idMedicionesPaciente no encontrado " + medicionesPacienteView.getIdMedicionesPaciente();
            logger.error("===>>>" + textoError);
            MedicionesPacienteException medPacE = new MedicionesPacienteException("Existe un Error", MedicionesPacienteException.LAYER_DAO, MedicionesPacienteException.ACTION_VALIDATE);
            medPacE.addError(textoError);
            throw medPacE;
         }

         Controles controles = controlesRepository.findByidPaciente(medicionesPacienteView.getIdPaciente());
         if (controles == null) {
            String textoError = "No existe en el Control el idPaciente introducido";
            logger.error("===>>>" + textoError);
            MedicionesPacienteException medPacE = new MedicionesPacienteException("Existe un Error", MedicionesPacienteException.LAYER_DAO, MedicionesPacienteException.ACTION_VALIDATE);
            medPacE.addError(textoError);
            throw medPacE;
         }

         MedicionesPaciente medicionesPaciente = medicionesPacienteRepository.findOne(medicionesPacienteView.getIdMedicionesPaciente());

         if (!medicionesPaciente.getIdPaciente().equals(medicionesPacienteView.getIdPaciente())) {
            String textoError = "No puedes cambiar el idPaciente, el que existe es: " + medicionesPaciente.getIdPaciente() + ", el que enviaste es: " + medicionesPacienteView.getIdPaciente();
            logger.error("===>>>" + textoError);
            MedicionesPacienteException medPacE = new MedicionesPacienteException("Existe un Error", MedicionesPacienteException.LAYER_DAO, MedicionesPacienteException.ACTION_VALIDATE);
            medPacE.addError(textoError);
            throw medPacE;
         }

         if (!controles.getDiabetes()) {
            medicionesPacienteView.setDiabetes(null);
         }
         if (!controles.getOximetria()) {
        	 medicionesPacienteView.setOxPr(null);
             medicionesPacienteView.setOxSpo2(null);
         }
         if (!controles.getHipertension()) {
            medicionesPacienteView.setHiperSistolica(null);
            medicionesPacienteView.setHiperDiastolica(null);
            medicionesPacienteView.setHiperPulso(null);
         }

         medicionesPaciente = medicionesPacienteConverter.toEntity(medicionesPacienteView, medicionesPaciente, Boolean.TRUE);
         logger.debug("===>>>Editar medicionesPaciente: {}", medicionesPaciente);
         medicionesPacienteRepository.save(medicionesPaciente);
         return medicionesPacienteConverter.toView(medicionesPaciente, Boolean.TRUE);

      } catch (MedicionesPacienteException medPacE) {
         throw medPacE;
      } catch (ConstraintViolationException cve) {
         logger.error("===>>>Error en la validacion");
         MedicionesPacienteException medPacE = new MedicionesPacienteException("Error en la validacion", MedicionesPacienteException.LAYER_DAO, MedicionesPacienteException.ACTION_VALIDATE);
         final Set<ConstraintViolation<?>> violaciones = cve.getConstraintViolations();
         for (Iterator<ConstraintViolation<?>> iterator = violaciones.iterator(); iterator.hasNext(); ) {
            ConstraintViolation<?> siguiente = iterator.next();
            medPacE.addError(siguiente.getPropertyPath() + " " + siguiente.getMessage());
         }
         throw medPacE;
      } catch (DataIntegrityViolationException dive) {
         MedicionesPacienteException medPacE = new MedicionesPacienteException("No fue posible modificar  MedicionesPaciente", MedicionesPacienteException.LAYER_DAO, MedicionesPacienteException.ACTION_INSERT);
         medPacE.addError("Ocurrio un error al modificar MedicionesPaciente");
         logger.error("===>>>Error al modificar MedicionesPaciente - CODE: {} - {}", medPacE.getExceptionCode(), medicionesPacienteView, dive);
         throw medPacE;
      } catch (Exception ex) {
         MedicionesPacienteException medPacE = new MedicionesPacienteException("Error inesperado al modificar  MedicionesPaciente", MedicionesPacienteException.LAYER_DAO, MedicionesPacienteException.ACTION_INSERT);
         medPacE.addError("Ocurrio un error al modificar MedicionesPaciente");
         logger.error("===>>>Error al modificar MedicionesPaciente - CODE: {} - {}", medPacE.getExceptionCode(), medicionesPacienteView, ex);
         throw medPacE;
      }
   }

   @Override
   public Page<MedicionesPacienteView> getMedicionesPacienteSearch(UUID idPaciente, Long startDate, Long endDate, Integer page, Integer size, String orderColumn, String orderType) throws MedicionesPacienteException {
      try {
         logger.info("===>>>getMedicionesPacientePage(): - idPaciente: {} - startDate: {} - endDate: {} - page: {} - size: {} - orderColumn: {} - orderType: {}",
            idPaciente, startDate, endDate, page, size, orderColumn, orderType);

         if (idPaciente == null) {
            logger.error("===>>>idPaciente viene NULO/VACIO: {}", idPaciente);
            MedicionesPacienteException medPacE = new MedicionesPacienteException("No se encuentra en el sistema MedicionesPaciente", MedicionesPacienteException.LAYER_DAO, MedicionesPacienteException.ACTION_VALIDATE);
            medPacE.addError("idPaciente viene NULO/VACIO: " + idPaciente);
            throw medPacE;
         }

         List<MedicionesPacienteView> medicionesPacienteViewList = new ArrayList<>();
         Page<MedicionesPaciente> medicionesPacientePage = null;
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
         Specifications<MedicionesPaciente> spec = Specifications.where(
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
            medicionesPacientePage = medicionesPacienteRepository.findAll(request);
         } else {
            medicionesPacientePage = medicionesPacienteRepository.findAll(spec, request);
         }

         medicionesPacientePage.getContent().forEach(medicionesPaciente -> {
            medicionesPacienteViewList.add(medicionesPacienteConverter.toView(medicionesPaciente, Boolean.FALSE));
         });
         PageImpl<MedicionesPacienteView> medicionesPacienteViewPage = new PageImpl<MedicionesPacienteView>(medicionesPacienteViewList, request, medicionesPacientePage.getTotalElements());
         return medicionesPacienteViewPage;
      } catch (MedicionesPacienteException medPacE) {
         throw medPacE;
      } catch (IllegalArgumentException iae) {
         logger.error("===>>>Algun parametro no es correcto");
         MedicionesPacienteException pe = new MedicionesPacienteException("Algun parametro no es correcto:", MedicionesPacienteException.LAYER_SERVICE, MedicionesPacienteException.ACTION_VALIDATE);
         pe.addError("Puede que sea null, vacio o valor incorrecto");
         throw pe;
      } catch (Exception ex) {
         MedicionesPacienteException medPacE = new MedicionesPacienteException("Ocurrio un error al seleccionar lista MedicionesPaciente paginable", MedicionesPacienteException.LAYER_SERVICE, MedicionesPacienteException.ACTION_SELECT);
         logger.error(ExceptionServiceCode.GROUP + "===>>>Error al tratar de seleccionar lista MedicionesPaciente paginable - CODE: {}", medPacE.getExceptionCode(), ex);
         throw medPacE;
      }
   }

   private String sinAcentos(String cadena) {
      return Normalizer.normalize(cadena, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
   }


}
