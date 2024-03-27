package net.amentum.niomedic.expediente.service.impl;

import net.amentum.niomedic.expediente.converter.AsuetosConverter;
import net.amentum.niomedic.expediente.exception.AsuetosException;
import net.amentum.niomedic.expediente.model.Asuetos;
import net.amentum.niomedic.expediente.persistence.AsuetosRepository;
import net.amentum.niomedic.expediente.service.AsuetosService;
import net.amentum.niomedic.expediente.views.AsuetosView;
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

@Service
@Transactional(readOnly = true)
public class AsuetosServiceImpl implements AsuetosService {
   private final Logger logger = LoggerFactory.getLogger(AsuetosServiceImpl.class);
   private AsuetosRepository asuetosRepository;
   private AsuetosConverter asuetosConverter;

   @Autowired
   public void setAsuetosRepository(AsuetosRepository asuetosRepository) {
      this.asuetosRepository = asuetosRepository;
   }

   @Autowired
   public void setAsuetosConverter(AsuetosConverter asuetosConverter) {
      this.asuetosConverter = asuetosConverter;
   }

   @Transactional(readOnly = false, rollbackFor = {AsuetosException.class})
   @Override
   public void createAsuetos(AsuetosView asuetosView) throws AsuetosException {
      try {
         if (asuetosView.getFecha() == null) {
            logger.error("===>>>fecha  NULO/VACIO: {}", asuetosView.getFecha());
            AsuetosException asE = new AsuetosException("Existe un Error", AsuetosException.LAYER_DAO, AsuetosException.ACTION_VALIDATE);
            asE.addError("fecha  NULO/VACIO: " + asuetosView.getFecha());
            throw asE;
         }

         Asuetos asuetos = asuetosConverter.toEntity(asuetosView, new Asuetos(), Boolean.FALSE);
         logger.debug("===>>>Insertar nuevo Asuetos: {}", asuetos);
         asuetosRepository.save(asuetos);

      } catch (AsuetosException asE) {
         throw asE;
      } catch (ConstraintViolationException cve) {
         logger.error("===>>>Error en la validacion");
         AsuetosException asE = new AsuetosException("Error en la validacion", AsuetosException.LAYER_DAO, AsuetosException.ACTION_VALIDATE);
         final Set<ConstraintViolation<?>> violaciones = cve.getConstraintViolations();
         for (Iterator<ConstraintViolation<?>> iterator = violaciones.iterator(); iterator.hasNext(); ) {
            ConstraintViolation<?> siguiente = iterator.next();
            asE.addError(siguiente.getPropertyPath() + " " + siguiente.getMessage());
         }
         throw asE;
      } catch (DataIntegrityViolationException dive) {
         AsuetosException asE = new AsuetosException("No fue posible agregar  Asuetos", AsuetosException.LAYER_DAO, AsuetosException.ACTION_INSERT);
         asE.addError("Ocurrio un error al agregar Asuetos");
         logger.error("===>>>Error al insertar nuevo Asuetos - CODE: {} - {}", asE.getExceptionCode(), asuetosView, dive);
         throw asE;
      } catch (Exception ex) {
         AsuetosException asE = new AsuetosException("Error inesperado al agregar  Asuetos", AsuetosException.LAYER_DAO, AsuetosException.ACTION_INSERT);
         asE.addError("Ocurrio un error al agregar Asuetos");
         logger.error("===>>>Error al insertar nuevo Asuetos - CODE: {} - {}", asE.getExceptionCode(), asuetosView, ex);
         throw asE;
      }
   }

   @Transactional(readOnly = false, rollbackFor = {AsuetosException.class})
   @Override
   public void updateAsuetos(AsuetosView asuetosView) throws AsuetosException {
      try {
         if (!asuetosRepository.exists(asuetosView.getIdAsuetos())) {
            logger.error("===>>>idAsuetos no encontrado: {}", asuetosView.getIdAsuetos());
            AsuetosException asE = new AsuetosException("No se encuentra en el sistema Asuetos", AsuetosException.LAYER_DAO, AsuetosException.ACTION_VALIDATE);
            asE.addError("idAsuetos no encontrado: " + asuetosView.getIdAsuetos());
            throw asE;
         }

         if (asuetosView.getFecha() == null) {
            logger.error("===>>>fecha  NULO/VACIO: {}", asuetosView.getFecha());
            AsuetosException asE = new AsuetosException("Existe un Error", AsuetosException.LAYER_DAO, AsuetosException.ACTION_VALIDATE);
            asE.addError("fecha  NULO/VACIO: " + asuetosView.getFecha());
            throw asE;
         }

         Asuetos asuetos = asuetosRepository.findOne(asuetosView.getIdAsuetos());

         asuetos = asuetosConverter.toEntity(asuetosView, asuetos, Boolean.TRUE);
         logger.debug("===>>>Editar Asuetos: {}", asuetos);
         asuetosRepository.save(asuetos);

      } catch (AsuetosException asE) {
         throw asE;
      } catch (ConstraintViolationException cve) {
         logger.error("===>>>Error en la validacion");
         AsuetosException asE = new AsuetosException("Error en la validacion", AsuetosException.LAYER_DAO, AsuetosException.ACTION_VALIDATE);
         final Set<ConstraintViolation<?>> violaciones = cve.getConstraintViolations();
         for (Iterator<ConstraintViolation<?>> iterator = violaciones.iterator(); iterator.hasNext(); ) {
            ConstraintViolation<?> siguiente = iterator.next();
            asE.addError(siguiente.getPropertyPath() + " " + siguiente.getMessage());
         }
         throw asE;
      } catch (DataIntegrityViolationException dive) {
         AsuetosException asE = new AsuetosException("No fue posible modificar  Asuetos", AsuetosException.LAYER_DAO, AsuetosException.ACTION_INSERT);
         asE.addError("Ocurrio un error al modificar Asuetos");
         logger.error("===>>>Error al modificar Asuetos - CODE: {} - {}", asE.getExceptionCode(), asuetosView, dive);
         throw asE;
      } catch (Exception ex) {
         AsuetosException asE = new AsuetosException("Error inesperado al modificar  Asuetos", AsuetosException.LAYER_DAO, AsuetosException.ACTION_INSERT);
         asE.addError("Ocurrio un error al modificar Asuetos");
         logger.error("===>>>Error al modificar Asuetos - CODE: {} - {}", asE.getExceptionCode(), asuetosView, ex);
         throw asE;
      }
   }

   @Override
   public AsuetosView getDetailsByIdAsuetos(Long idAsuetos) throws AsuetosException {
      try {
         if (!asuetosRepository.exists(idAsuetos)) {
            logger.error("===>>>idAsuetos no encontrado: {}", idAsuetos);
            AsuetosException asE = new AsuetosException("No se encuentra en el sistema Asuetos", AsuetosException.LAYER_DAO, AsuetosException.ACTION_VALIDATE);
            asE.addError("idAsuetos no encontrado: " + idAsuetos);
            throw asE;
         }

         Asuetos asuetos = asuetosRepository.findOne(idAsuetos);
         return asuetosConverter.toView(asuetos, Boolean.TRUE);

      } catch (AsuetosException asE) {
         throw asE;
      } catch (ConstraintViolationException cve) {
         logger.error("===>>>Error en la validacion");
         AsuetosException asE = new AsuetosException("Error en la validacion", AsuetosException.LAYER_DAO, AsuetosException.ACTION_VALIDATE);
         final Set<ConstraintViolation<?>> violaciones = cve.getConstraintViolations();
         for (Iterator<ConstraintViolation<?>> iterator = violaciones.iterator(); iterator.hasNext(); ) {
            ConstraintViolation<?> siguiente = iterator.next();
            asE.addError(siguiente.getPropertyPath() + ": " + siguiente.getMessage());
         }
         throw asE;
      } catch (DataIntegrityViolationException dive) {
         AsuetosException asE = new AsuetosException("No fue posible obtener  Asuetos", AsuetosException.LAYER_DAO, AsuetosException.ACTION_INSERT);
         asE.addError("Ocurrio un error al obtener Asuetos");
         logger.error("===>>>Error al obtener nuevo Asuetos - CODE: {} - {}", asE.getExceptionCode(), idAsuetos, dive);
         throw asE;
      } catch (Exception ex) {
         AsuetosException asE = new AsuetosException("Error inesperado al obtener  Asuetos", AsuetosException.LAYER_DAO, AsuetosException.ACTION_INSERT);
         asE.addError("Ocurrio un error al obtener Asuetos");
         logger.error("===>>>Error al obtener nuevo Asuetos - CODE: {} - {}", asE.getExceptionCode(), idAsuetos, ex);
         throw asE;
      }
   }

   @Transactional(readOnly = false, rollbackFor = {AsuetosException.class})
   @Override
   public void deleteAsuetos(Long idAsuetos) throws AsuetosException {
      try {
         if (!asuetosRepository.exists(idAsuetos)) {
            logger.error("===>>>idAsuetos no encontrado: {}", idAsuetos);
            AsuetosException asE = new AsuetosException("No se encuentra en el sistema Asuetos", AsuetosException.LAYER_DAO, AsuetosException.ACTION_VALIDATE);
            asE.addError("idAsuetos no encontrado: " + idAsuetos);
            throw asE;
         }

         Asuetos asuetos = asuetosRepository.findOne(idAsuetos);
         asuetosRepository.delete(asuetos);

      } catch (AsuetosException asE) {
         throw asE;
      } catch (ConstraintViolationException cve) {
         logger.error("===>>>Error en la validacion");
         AsuetosException asE = new AsuetosException("Error en la validacion", AsuetosException.LAYER_DAO, AsuetosException.ACTION_VALIDATE);
         final Set<ConstraintViolation<?>> violaciones = cve.getConstraintViolations();
         for (Iterator<ConstraintViolation<?>> iterator = violaciones.iterator(); iterator.hasNext(); ) {
            ConstraintViolation<?> siguiente = iterator.next();
            asE.addError(siguiente.getPropertyPath() + ": " + siguiente.getMessage());
         }
         throw asE;
      } catch (DataIntegrityViolationException dive) {
         AsuetosException asE = new AsuetosException("No fue posible obtener  Asuetos", AsuetosException.LAYER_DAO, AsuetosException.ACTION_INSERT);
         asE.addError("Ocurrio un error al obtener Asuetos");
         logger.error("===>>>Error al obtener nuevo Asuetos - CODE: {} - {}", asE.getExceptionCode(), idAsuetos, dive);
         throw asE;
      } catch (Exception ex) {
         AsuetosException asE = new AsuetosException("Error inesperado al obtener  Asuetos", AsuetosException.LAYER_DAO, AsuetosException.ACTION_INSERT);
         asE.addError("Ocurrio un error al obtener Asuetos");
         logger.error("===>>>Error al obtener nuevo Asuetos - CODE: {} - {}", asE.getExceptionCode(), idAsuetos, ex);
         throw asE;
      }
   }

   @Override
   public List<AsuetosView> findAll() throws  AsuetosException{
      try {
         List<Asuetos> asuetosList = asuetosRepository.findAll();
         List<AsuetosView> asuetosViewList = new ArrayList<>();
         for (Asuetos temp : asuetosList) {
            asuetosViewList.add(asuetosConverter.toView(temp, Boolean.TRUE));
         }
         return asuetosViewList;
      } catch (Exception ex) {
         AsuetosException asE = new AsuetosException("Error inesperado al obtener todos los registros de  Asuetos", AsuetosException.LAYER_DAO, AsuetosException.ACTION_INSERT);
         asE.addError("Ocurrio un error al obtener Asuetos");
         logger.error("===>>>Error al obtener todos los registros de Asuetos - CODE: {} - {}", asE.getExceptionCode(), ex);
         throw asE;
      }
   }
}
