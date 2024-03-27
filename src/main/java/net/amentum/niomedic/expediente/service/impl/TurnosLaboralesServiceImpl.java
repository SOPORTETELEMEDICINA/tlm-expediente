package net.amentum.niomedic.expediente.service.impl;

import net.amentum.niomedic.expediente.converter.TurnosLaboralesConverter;
import net.amentum.niomedic.expediente.exception.TurnosLaboralesException;
import net.amentum.niomedic.expediente.model.TurnosLaborales;
import net.amentum.niomedic.expediente.persistence.TurnosLaboralesRepository;
import net.amentum.niomedic.expediente.service.TurnosLaboralesService;
import net.amentum.niomedic.expediente.views.TurnosLaboralesView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Iterator;
import java.util.Set;


@Service
@Transactional(readOnly = true)
public class TurnosLaboralesServiceImpl implements TurnosLaboralesService {
   private final Logger logger = LoggerFactory.getLogger(TurnosLaboralesServiceImpl.class);
   private TurnosLaboralesRepository turnosLaboralesRepository;
   private TurnosLaboralesConverter turnosLaboralesConverter;

   @Autowired
   public void setTurnosLaboralesRepository(TurnosLaboralesRepository turnosLaboralesRepository) {
      this.turnosLaboralesRepository = turnosLaboralesRepository;
   }

   @Autowired
   public void setTurnosLaboralesConverter(TurnosLaboralesConverter turnosLaboralesConverter) {
      this.turnosLaboralesConverter = turnosLaboralesConverter;
   }

   @Transactional(readOnly = false, rollbackFor = {TurnosLaboralesException.class})
   @Override
   public void createTurnosLaborales(TurnosLaboralesView turnosLaboralesView) throws TurnosLaboralesException {
      try {
         if (turnosLaboralesView.getIdUsuario() == null) {
            logger.error("===>>>getIdUsuario  NULO/VACIO: {}", turnosLaboralesView.getIdUsuario());
            TurnosLaboralesException tlE = new TurnosLaboralesException("Existe un Error", TurnosLaboralesException.LAYER_DAO, TurnosLaboralesException.ACTION_VALIDATE);
            tlE.addError("getIdUsuario  NULO/VACIO: " + turnosLaboralesView.getIdUsuario());
            throw tlE;
         }

         if (turnosLaboralesView.getInicio() == null) {
            logger.error("===>>>inicio  NULO/VACIO: {}", turnosLaboralesView.getInicio());
            TurnosLaboralesException tlE = new TurnosLaboralesException("Existe un Error", TurnosLaboralesException.LAYER_DAO, TurnosLaboralesException.ACTION_VALIDATE);
            tlE.addError("inicio  NULO/VACIO: " + turnosLaboralesView.getInicio());
            throw tlE;
         }

         if (turnosLaboralesView.getFin() == null) {
            logger.error("===>>>fin  NULO/VACIO: {}", turnosLaboralesView.getFin());
            TurnosLaboralesException tlE = new TurnosLaboralesException("Existe un Error", TurnosLaboralesException.LAYER_DAO, TurnosLaboralesException.ACTION_VALIDATE);
            tlE.addError("fin  NULO/VACIO: " + turnosLaboralesView.getFin());
            throw tlE;
         }

         TurnosLaborales otraTurnosLaborales = turnosLaboralesRepository.findByIdUsuario(turnosLaboralesView.getIdUsuario());
         if (otraTurnosLaborales != null) {
            String textoError = "TurnosLaborales DUPLICADO en otro registro: idUsuario: " + turnosLaboralesView.getIdUsuario();
            logger.error("===>>>" + textoError);
            TurnosLaboralesException tlE = new TurnosLaboralesException("Existe un Error", TurnosLaboralesException.LAYER_DAO, TurnosLaboralesException.ACTION_VALIDATE);
            tlE.addError(textoError);
            throw tlE;
         }

         TurnosLaborales turnosLaborales = turnosLaboralesConverter.toEntity(turnosLaboralesView, new TurnosLaborales(), Boolean.FALSE);
         logger.debug("===>>>Insertar nuevo TurnosLaborales: {}", turnosLaborales);
         turnosLaboralesRepository.save(turnosLaborales);

      } catch (TurnosLaboralesException tlE) {
         throw tlE;
      } catch (ConstraintViolationException cve) {
         logger.error("===>>>Error en la validacion");
         TurnosLaboralesException tlE = new TurnosLaboralesException("Error en la validacion", TurnosLaboralesException.LAYER_DAO, TurnosLaboralesException.ACTION_VALIDATE);
         final Set<ConstraintViolation<?>> violaciones = cve.getConstraintViolations();
         for (Iterator<ConstraintViolation<?>> iterator = violaciones.iterator(); iterator.hasNext(); ) {
            ConstraintViolation<?> siguiente = iterator.next();
            tlE.addError(siguiente.getPropertyPath() + " " + siguiente.getMessage());
         }
         throw tlE;
      } catch (DataIntegrityViolationException dive) {
         TurnosLaboralesException tlE = new TurnosLaboralesException("No fue posible agregar  TurnosLaborales", TurnosLaboralesException.LAYER_DAO, TurnosLaboralesException.ACTION_INSERT);
         tlE.addError("Ocurrio un error al agregar TurnosLaborales");
         logger.error("===>>>Error al insertar nuevo TurnosLaborales - CODE: {} - {}", tlE.getExceptionCode(), turnosLaboralesView, dive);
         throw tlE;
      } catch (Exception ex) {
         TurnosLaboralesException tlE = new TurnosLaboralesException("Error inesperado al agregar  TurnosLaborales", TurnosLaboralesException.LAYER_DAO, TurnosLaboralesException.ACTION_INSERT);
         tlE.addError("Ocurrio un error al agregar TurnosLaborales");
         logger.error("===>>>Error al insertar nuevo TurnosLaborales - CODE: {} - {}", tlE.getExceptionCode(), turnosLaboralesView, ex);
         throw tlE;
      }
   }

   @Transactional(readOnly = false, rollbackFor = {TurnosLaboralesException.class})
   @Override
   public void updateTurnosLaborales(TurnosLaboralesView turnosLaboralesView) throws TurnosLaboralesException {
      try {
         if (!turnosLaboralesRepository.exists(turnosLaboralesView.getIdTurnosLaborales())) {
            logger.error("===>>>idTurnosLaborales no encontrado: {}", turnosLaboralesView.getIdTurnosLaborales());
            TurnosLaboralesException tlE = new TurnosLaboralesException("No se encuentra en el sistema TurnosLaborales", TurnosLaboralesException.LAYER_DAO, TurnosLaboralesException.ACTION_VALIDATE);
            tlE.addError("idTurnosLaborales no encontrado: " + turnosLaboralesView.getIdTurnosLaborales());
            throw tlE;
         }

         if (turnosLaboralesView.getIdUsuario() == null) {
            logger.error("===>>>getIdUsuario  NULO/VACIO: {}", turnosLaboralesView.getIdUsuario());
            TurnosLaboralesException tlE = new TurnosLaboralesException("Existe un Error", TurnosLaboralesException.LAYER_DAO, TurnosLaboralesException.ACTION_VALIDATE);
            tlE.addError("getIdUsuario  NULO/VACIO: " + turnosLaboralesView.getIdUsuario());
            throw tlE;
         }

         if (turnosLaboralesView.getInicio() == null) {
            logger.error("===>>>inicio  NULO/VACIO: {}", turnosLaboralesView.getInicio());
            TurnosLaboralesException tlE = new TurnosLaboralesException("Existe un Error", TurnosLaboralesException.LAYER_DAO, TurnosLaboralesException.ACTION_VALIDATE);
            tlE.addError("inicio  NULO/VACIO: " + turnosLaboralesView.getInicio());
            throw tlE;
         }

         if (turnosLaboralesView.getFin() == null) {
            logger.error("===>>>fin  NULO/VACIO: {}", turnosLaboralesView.getFin());
            TurnosLaboralesException tlE = new TurnosLaboralesException("Existe un Error", TurnosLaboralesException.LAYER_DAO, TurnosLaboralesException.ACTION_VALIDATE);
            tlE.addError("fin  NULO/VACIO: " + turnosLaboralesView.getFin());
            throw tlE;
         }

         TurnosLaborales turnosLaborales = turnosLaboralesRepository.findOne(turnosLaboralesView.getIdTurnosLaborales());

         TurnosLaborales otraTurnosLaborales = turnosLaboralesRepository.findByIdUsuario(turnosLaboralesView.getIdUsuario());
         if (otraTurnosLaborales != null) {
            if (turnosLaborales.getIdTurnosLaborales() != otraTurnosLaborales.getIdTurnosLaborales()) {
               String textoError = "TurnosLaborales DUPLICADO en otro registro: idUsuario: " + turnosLaboralesView.getIdUsuario();
               logger.error("===>>>" + textoError);
               TurnosLaboralesException tlE = new TurnosLaboralesException("Existe un Error", TurnosLaboralesException.LAYER_DAO, TurnosLaboralesException.ACTION_VALIDATE);
               tlE.addError(textoError);
               throw tlE;
            }
         }

         turnosLaborales = turnosLaboralesConverter.toEntity(turnosLaboralesView, turnosLaborales, Boolean.TRUE);
         logger.debug("===>>>Editar TurnosLaborales: {}", turnosLaborales);
         turnosLaboralesRepository.save(turnosLaborales);

      } catch (TurnosLaboralesException tlE) {
         throw tlE;
      } catch (ConstraintViolationException cve) {
         logger.error("===>>>Error en la validacion");
         TurnosLaboralesException tlE = new TurnosLaboralesException("Error en la validacion", TurnosLaboralesException.LAYER_DAO, TurnosLaboralesException.ACTION_VALIDATE);
         final Set<ConstraintViolation<?>> violaciones = cve.getConstraintViolations();
         for (Iterator<ConstraintViolation<?>> iterator = violaciones.iterator(); iterator.hasNext(); ) {
            ConstraintViolation<?> siguiente = iterator.next();
            tlE.addError(siguiente.getPropertyPath() + " " + siguiente.getMessage());
         }
         throw tlE;
      } catch (DataIntegrityViolationException dive) {
         TurnosLaboralesException tlE = new TurnosLaboralesException("No fue posible modificar  TurnosLaborales", TurnosLaboralesException.LAYER_DAO, TurnosLaboralesException.ACTION_INSERT);
         tlE.addError("Ocurrio un error al modificar TurnosLaborales");
         logger.error("===>>>Error al modificar TurnosLaborales - CODE: {} - {}", tlE.getExceptionCode(), turnosLaboralesView, dive);
         throw tlE;
      } catch (Exception ex) {
         TurnosLaboralesException tlE = new TurnosLaboralesException("Error inesperado al modificar  TurnosLaborales", TurnosLaboralesException.LAYER_DAO, TurnosLaboralesException.ACTION_INSERT);
         tlE.addError("Ocurrio un error al modificar TurnosLaborales");
         logger.error("===>>>Error al modificar TurnosLaborales - CODE: {} - {}", tlE.getExceptionCode(), turnosLaboralesView, ex);
         throw tlE;
      }
   }

   @Override
   public TurnosLaboralesView getDetailsByIdUsuario(Long idUsuario) throws TurnosLaboralesException {
      try {

         if (idUsuario == null) {
            logger.error("===>>>getIdUsuario  NULO/VACIO: {}", idUsuario);
            TurnosLaboralesException tlE = new TurnosLaboralesException("Existe un Error", TurnosLaboralesException.LAYER_DAO, TurnosLaboralesException.ACTION_VALIDATE);
            tlE.addError("getIdUsuario  NULO/VACIO: " + idUsuario);
            throw tlE;
         }

         if (turnosLaboralesRepository.findByIdUsuario(idUsuario) == null) {
            logger.error("===>>>idTurnosLaborales no encontrado: {}", idUsuario);
            TurnosLaboralesException tlE = new TurnosLaboralesException("No se encuentra en el sistema TurnosLaborales", TurnosLaboralesException.LAYER_DAO, TurnosLaboralesException.ACTION_VALIDATE);
            tlE.addError("idTurnosLaborales no encontrado: " + idUsuario);
            throw tlE;
         }

         TurnosLaborales turnosLaborales = turnosLaboralesRepository.findByIdUsuario(idUsuario);
         return turnosLaboralesConverter.toView(turnosLaborales, Boolean.TRUE);

      } catch (TurnosLaboralesException tlE) {
         throw tlE;
      } catch (ConstraintViolationException cve) {
         logger.error("===>>>Error en la validacion");
         TurnosLaboralesException tlE = new TurnosLaboralesException("Error en la validacion", TurnosLaboralesException.LAYER_DAO, TurnosLaboralesException.ACTION_VALIDATE);
         final Set<ConstraintViolation<?>> violaciones = cve.getConstraintViolations();
         for (Iterator<ConstraintViolation<?>> iterator = violaciones.iterator(); iterator.hasNext(); ) {
            ConstraintViolation<?> siguiente = iterator.next();
            tlE.addError(siguiente.getPropertyPath() + ": " + siguiente.getMessage());
         }
         throw tlE;
      } catch (DataIntegrityViolationException dive) {
         TurnosLaboralesException tlE = new TurnosLaboralesException("No fue posible obtener  TurnosLaborales", TurnosLaboralesException.LAYER_DAO, TurnosLaboralesException.ACTION_INSERT);
         tlE.addError("Ocurrio un error al obtener TurnosLaborales");
         logger.error("===>>>Error al obtener nuevo TurnosLaborales - CODE: {} - {}", tlE.getExceptionCode(), idUsuario, dive);
         throw tlE;
      } catch (Exception ex) {
         TurnosLaboralesException tlE = new TurnosLaboralesException("Error inesperado al obtener  TurnosLaborales", TurnosLaboralesException.LAYER_DAO, TurnosLaboralesException.ACTION_INSERT);
         tlE.addError("Ocurrio un error al obtener TurnosLaborales");
         logger.error("===>>>Error al obtener nuevo TurnosLaborales - CODE: {} - {}", tlE.getExceptionCode(), idUsuario, ex);
         throw tlE;
      }
   }


}
