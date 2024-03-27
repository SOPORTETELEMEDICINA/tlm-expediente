package net.amentum.niomedic.expediente.service.impl;

import net.amentum.niomedic.expediente.converter.VacacionesConverter;
import net.amentum.niomedic.expediente.exception.VacacionesException;
import net.amentum.niomedic.expediente.model.Vacaciones;
import net.amentum.niomedic.expediente.persistence.VacacionesRepository;
import net.amentum.niomedic.expediente.service.VacacionesService;
import net.amentum.niomedic.expediente.views.VacacionesView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class VacacionesServiceImpl implements VacacionesService {
   private final Logger logger = LoggerFactory.getLogger(VacacionesServiceImpl.class);
   private VacacionesRepository vacacionesRepository;
   private VacacionesConverter vacacionesConverter;

   @Autowired
   public void setVacacionesRepository(VacacionesRepository vacacionesRepository) {
      this.vacacionesRepository = vacacionesRepository;
   }

   @Autowired
   public void setVacacionesConverter(VacacionesConverter vacacionesConverter) {
      this.vacacionesConverter = vacacionesConverter;
   }

   @Transactional(readOnly = false, rollbackFor = {VacacionesException.class})
   @Override
   public void createVacaciones(VacacionesView vacacionesView) throws VacacionesException {
      try {
         if (vacacionesView.getIdUsuario() == null) {
            logger.error("===>>>getIdUsuario  NULO/VACIO: {}", vacacionesView.getIdUsuario());
            VacacionesException vaE = new VacacionesException("Existe un Error", VacacionesException.LAYER_DAO, VacacionesException.ACTION_VALIDATE);
            vaE.addError("getIdUsuario  NULO/VACIO: " + vacacionesView.getIdUsuario());
            throw vaE;
         }

         if (vacacionesView.getInicio() == null) {
            logger.error("===>>>inicio  NULO/VACIO: {}", vacacionesView.getInicio());
            VacacionesException vaE = new VacacionesException("Existe un Error", VacacionesException.LAYER_DAO, VacacionesException.ACTION_VALIDATE);
            vaE.addError("inicio  NULO/VACIO: " + vacacionesView.getInicio());
            throw vaE;
         }

         if (vacacionesView.getFin() == null) {
            logger.error("===>>>fin  NULO/VACIO: {}", vacacionesView.getFin());
            VacacionesException vaE = new VacacionesException("Existe un Error", VacacionesException.LAYER_DAO, VacacionesException.ACTION_VALIDATE);
            vaE.addError("fin  NULO/VACIO: " + vacacionesView.getFin());
            throw vaE;
         }

         Vacaciones vacaciones = vacacionesConverter.toEntity(vacacionesView, new Vacaciones(), Boolean.FALSE);
         logger.debug("===>>>Insertar nuevo Vacaciones: {}", vacaciones);
         vacacionesRepository.save(vacaciones);

      } catch (VacacionesException vaE) {
         throw vaE;
      } catch (ConstraintViolationException cve) {
         logger.error("===>>>Error en la validacion");
         VacacionesException vaE = new VacacionesException("Error en la validacion", VacacionesException.LAYER_DAO, VacacionesException.ACTION_VALIDATE);
         final Set<ConstraintViolation<?>> violaciones = cve.getConstraintViolations();
         for (Iterator<ConstraintViolation<?>> iterator = violaciones.iterator(); iterator.hasNext(); ) {
            ConstraintViolation<?> siguiente = iterator.next();
            vaE.addError(siguiente.getPropertyPath() + " " + siguiente.getMessage());
         }
         throw vaE;
      } catch (DataIntegrityViolationException dive) {
         VacacionesException vaE = new VacacionesException("No fue posible agregar  Vacaciones", VacacionesException.LAYER_DAO, VacacionesException.ACTION_INSERT);
         vaE.addError("Ocurrio un error al agregar Vacaciones");
         logger.error("===>>>Error al insertar nuevo Vacaciones - CODE: {} - {}", vaE.getExceptionCode(), vacacionesView, dive);
         throw vaE;
      } catch (Exception ex) {
         VacacionesException vaE = new VacacionesException("Error inesperado al agregar  Vacaciones", VacacionesException.LAYER_DAO, VacacionesException.ACTION_INSERT);
         vaE.addError("Ocurrio un error al agregar Vacaciones");
         logger.error("===>>>Error al insertar nuevo Vacaciones - CODE: {} - {}", vaE.getExceptionCode(), vacacionesView, ex);
         throw vaE;
      }
   }

   @Transactional(readOnly = false, rollbackFor = {VacacionesException.class})
   @Override
   public void updateVacaciones(VacacionesView vacacionesView) throws VacacionesException {
      try {
         if (!vacacionesRepository.exists(vacacionesView.getIdVacaciones())) {
            logger.error("===>>>idVacaciones no encontrado: {}", vacacionesView.getIdVacaciones());
            VacacionesException vaE = new VacacionesException("No se encuentra en el sistema Vacaciones", VacacionesException.LAYER_DAO, VacacionesException.ACTION_VALIDATE);
            vaE.addError("idVacaciones no encontrado: " + vacacionesView.getIdVacaciones());
            throw vaE;
         }

         if (vacacionesView.getIdUsuario() == null) {
            logger.error("===>>>getIdUsuario  NULO/VACIO: {}", vacacionesView.getIdUsuario());
            VacacionesException vaE = new VacacionesException("Existe un Error", VacacionesException.LAYER_DAO, VacacionesException.ACTION_VALIDATE);
            vaE.addError("getIdUsuario  NULO/VACIO: " + vacacionesView.getIdUsuario());
            throw vaE;
         }

         if (vacacionesView.getInicio() == null) {
            logger.error("===>>>inicio  NULO/VACIO: {}", vacacionesView.getInicio());
            VacacionesException vaE = new VacacionesException("Existe un Error", VacacionesException.LAYER_DAO, VacacionesException.ACTION_VALIDATE);
            vaE.addError("inicio  NULO/VACIO: " + vacacionesView.getInicio());
            throw vaE;
         }

         if (vacacionesView.getFin() == null) {
            logger.error("===>>>fin  NULO/VACIO: {}", vacacionesView.getFin());
            VacacionesException vaE = new VacacionesException("Existe un Error", VacacionesException.LAYER_DAO, VacacionesException.ACTION_VALIDATE);
            vaE.addError("fin  NULO/VACIO: " + vacacionesView.getFin());
            throw vaE;
         }

         Vacaciones vacaciones = vacacionesRepository.findOne(vacacionesView.getIdVacaciones());

         vacaciones = vacacionesConverter.toEntity(vacacionesView, vacaciones, Boolean.TRUE);
         logger.debug("===>>>Editar Vacaciones: {}", vacaciones);
         vacacionesRepository.save(vacaciones);

      } catch (VacacionesException vaE) {
         throw vaE;
      } catch (ConstraintViolationException cve) {
         logger.error("===>>>Error en la validacion");
         VacacionesException vaE = new VacacionesException("Error en la validacion", VacacionesException.LAYER_DAO, VacacionesException.ACTION_VALIDATE);
         final Set<ConstraintViolation<?>> violaciones = cve.getConstraintViolations();
         for (Iterator<ConstraintViolation<?>> iterator = violaciones.iterator(); iterator.hasNext(); ) {
            ConstraintViolation<?> siguiente = iterator.next();
            vaE.addError(siguiente.getPropertyPath() + " " + siguiente.getMessage());
         }
         throw vaE;
      } catch (DataIntegrityViolationException dive) {
         VacacionesException vaE = new VacacionesException("No fue posible modificar  Vacaciones", VacacionesException.LAYER_DAO, VacacionesException.ACTION_INSERT);
         vaE.addError("Ocurrio un error al modificar Vacaciones");
         logger.error("===>>>Error al modificar Vacaciones - CODE: {} - {}", vaE.getExceptionCode(), vacacionesView, dive);
         throw vaE;
      } catch (Exception ex) {
         VacacionesException vaE = new VacacionesException("Error inesperado al modificar  Vacaciones", VacacionesException.LAYER_DAO, VacacionesException.ACTION_INSERT);
         vaE.addError("Ocurrio un error al modificar Vacaciones");
         logger.error("===>>>Error al modificar Vacaciones - CODE: {} - {}", vaE.getExceptionCode(), vacacionesView, ex);
         throw vaE;
      }
   }


   @Override
   public List<VacacionesView> getDetailsByIdUsuario(Long idUsuario) throws VacacionesException {
      try {

         if (idUsuario == null) {
            logger.error("===>>>getIdUsuario  NULO/VACIO: {}", idUsuario);
            VacacionesException vaE = new VacacionesException("Existe un Error", VacacionesException.LAYER_DAO, VacacionesException.ACTION_VALIDATE);
            vaE.addError("getIdUsuario  NULO/VACIO: " + idUsuario);
            throw vaE;
         }

         if (vacacionesRepository.findByIdUsuario(idUsuario) == null) {
            logger.error("===>>>idVacaciones no encontrado: {}", idUsuario);
            VacacionesException vaE = new VacacionesException("No se encuentra en el sistema Vacaciones", VacacionesException.LAYER_DAO, VacacionesException.ACTION_VALIDATE);
            vaE.addError("idVacaciones no encontrado: " + idUsuario);
            throw vaE;
         }

         List<Vacaciones> vacaciones = vacacionesRepository.findByIdUsuario(idUsuario);
         List<VacacionesView> vacacionesViewList = new ArrayList<>();

         for (Vacaciones vaca : vacaciones) {
            vacacionesViewList.add(vacacionesConverter.toView(vaca, Boolean.TRUE));
         }

         return vacacionesViewList;

      } catch (VacacionesException vaE) {
         throw vaE;
      } catch (ConstraintViolationException cve) {
         logger.error("===>>>Error en la validacion");
         VacacionesException vaE = new VacacionesException("Error en la validacion", VacacionesException.LAYER_DAO, VacacionesException.ACTION_VALIDATE);
         final Set<ConstraintViolation<?>> violaciones = cve.getConstraintViolations();
         for (Iterator<ConstraintViolation<?>> iterator = violaciones.iterator(); iterator.hasNext(); ) {
            ConstraintViolation<?> siguiente = iterator.next();
            vaE.addError(siguiente.getPropertyPath() + ": " + siguiente.getMessage());
         }
         throw vaE;
      } catch (DataIntegrityViolationException dive) {
         VacacionesException vaE = new VacacionesException("No fue posible obtener  Vacaciones", VacacionesException.LAYER_DAO, VacacionesException.ACTION_INSERT);
         vaE.addError("Ocurrio un error al obtener Vacaciones");
         logger.error("===>>>Error al obtener nuevo Vacaciones - CODE: {} - {}", vaE.getExceptionCode(), idUsuario, dive);
         throw vaE;
      } catch (Exception ex) {
         VacacionesException vaE = new VacacionesException("Error inesperado al obtener  Vacaciones", VacacionesException.LAYER_DAO, VacacionesException.ACTION_INSERT);
         vaE.addError("Ocurrio un error al obtener Vacaciones");
         logger.error("===>>>Error al obtener nuevo Vacaciones - CODE: {} - {}", vaE.getExceptionCode(), idUsuario, ex);
         throw vaE;
      }
   }


}
