package net.amentum.niomedic.expediente.service.impl;

import net.amentum.niomedic.expediente.converter.CatalogoTipoEventoConverter;
import net.amentum.niomedic.expediente.exception.CatalogoTipoEventoException;
import net.amentum.niomedic.expediente.model.CatalogoTipoEvento;
import net.amentum.niomedic.expediente.persistence.CatalogoTipoEventoRepository;
import net.amentum.niomedic.expediente.service.CatalogoTipoEventoService;
import net.amentum.niomedic.expediente.views.CatalogoTipoEventoView;
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
public class CatalogoTipoEventoServiceImpl implements CatalogoTipoEventoService {
   private final Logger logger = LoggerFactory.getLogger(CatalogoTipoEventoServiceImpl.class);
   private CatalogoTipoEventoRepository catalogoTipoEventoRepository;
   private CatalogoTipoEventoConverter catalogoTipoEventoConverter;

   @Autowired
   public void setCatalogoTipoEventoRepository(CatalogoTipoEventoRepository catalogoTipoEventoRepository) {
      this.catalogoTipoEventoRepository = catalogoTipoEventoRepository;
   }

   @Autowired
   public void setCatalogoTipoEventoConverter(CatalogoTipoEventoConverter catalogoTipoEventoConverter) {
      this.catalogoTipoEventoConverter = catalogoTipoEventoConverter;
   }

   @Transactional(readOnly = false, rollbackFor = {CatalogoTipoEventoException.class})
   @Override
   public void createCatalogoTipoEvento(CatalogoTipoEventoView catalogoTipoEventoView) throws CatalogoTipoEventoException {
      try {
         if (catalogoTipoEventoView.getDescripcion().trim().isEmpty()) {
            logger.error("===>>>descripcion  NULO/VACIO: {}", catalogoTipoEventoView.getDescripcion());
            CatalogoTipoEventoException cteE = new CatalogoTipoEventoException("Existe un Error", CatalogoTipoEventoException.LAYER_DAO, CatalogoTipoEventoException.ACTION_VALIDATE);
            cteE.addError("descripcion  NULO/VACIO: " + catalogoTipoEventoView.getDescripcion());
            throw cteE;
         }

         if (catalogoTipoEventoView.getColor().trim().isEmpty()) {
            logger.error("===>>>color  NULO/VACIO: {}", catalogoTipoEventoView.getColor());
            CatalogoTipoEventoException cteE = new CatalogoTipoEventoException("Existe un Error", CatalogoTipoEventoException.LAYER_DAO, CatalogoTipoEventoException.ACTION_VALIDATE);
            cteE.addError("color  NULO/VACIO: " + catalogoTipoEventoView.getColor());
            throw cteE;
         }

         CatalogoTipoEvento catalogoTipoEvento = catalogoTipoEventoConverter.toEntity(catalogoTipoEventoView, new CatalogoTipoEvento(), Boolean.FALSE);
         logger.debug("===>>>Insertar nuevo CatalogoTipoEvento: {}", catalogoTipoEvento);
         catalogoTipoEventoRepository.save(catalogoTipoEvento);

      } catch (CatalogoTipoEventoException cteE) {
         throw cteE;
      } catch (ConstraintViolationException cve) {
         logger.error("===>>>Error en la validacion");
         CatalogoTipoEventoException cteE = new CatalogoTipoEventoException("Error en la validacion", CatalogoTipoEventoException.LAYER_DAO, CatalogoTipoEventoException.ACTION_VALIDATE);
         final Set<ConstraintViolation<?>> violaciones = cve.getConstraintViolations();
         for (Iterator<ConstraintViolation<?>> iterator = violaciones.iterator(); iterator.hasNext(); ) {
            ConstraintViolation<?> siguiente = iterator.next();
            cteE.addError(siguiente.getPropertyPath() + " " + siguiente.getMessage());
         }
         throw cteE;
      } catch (DataIntegrityViolationException dive) {
         CatalogoTipoEventoException cteE = new CatalogoTipoEventoException("No fue posible agregar  CatalogoTipoEvento", CatalogoTipoEventoException.LAYER_DAO, CatalogoTipoEventoException.ACTION_INSERT);
         cteE.addError("Ocurrio un error al agregar CatalogoTipoEvento");
         logger.error("===>>>Error al insertar nuevo CatalogoTipoEvento - CODE: {} - {}", cteE.getExceptionCode(), catalogoTipoEventoView, dive);
         throw cteE;
      } catch (Exception ex) {
         CatalogoTipoEventoException cteE = new CatalogoTipoEventoException("Error inesperado al agregar  CatalogoTipoEvento", CatalogoTipoEventoException.LAYER_DAO, CatalogoTipoEventoException.ACTION_INSERT);
         cteE.addError("Ocurrio un error al agregar CatalogoTipoEvento");
         logger.error("===>>>Error al insertar nuevo CatalogoTipoEvento - CODE: {} - {}", cteE.getExceptionCode(), catalogoTipoEventoView, ex);
         throw cteE;
      }
   }

   @Transactional(readOnly = false, rollbackFor = {CatalogoTipoEventoException.class})
   @Override
   public void updateCatalogoTipoEvento(CatalogoTipoEventoView catalogoTipoEventoView) throws CatalogoTipoEventoException {
      try {
         if (!catalogoTipoEventoRepository.exists(catalogoTipoEventoView.getIdTipoEvento())) {
            logger.error("===>>>idCatalogoTipoEvento no encontrado: {}", catalogoTipoEventoView.getIdTipoEvento());
            CatalogoTipoEventoException cteE = new CatalogoTipoEventoException("No se encuentra en el sistema CatalogoTipoEvento", CatalogoTipoEventoException.LAYER_DAO, CatalogoTipoEventoException.ACTION_VALIDATE);
            cteE.addError("idCatalogoTipoEvento no encontrado: " + catalogoTipoEventoView.getIdTipoEvento());
            throw cteE;
         }

         CatalogoTipoEvento catalogoTipoEvento = catalogoTipoEventoRepository.findOne(catalogoTipoEventoView.getIdTipoEvento());

         catalogoTipoEvento = catalogoTipoEventoConverter.toEntity(catalogoTipoEventoView, catalogoTipoEvento, Boolean.TRUE);
         logger.debug("===>>>Editar CatalogoTipoEvento: {}", catalogoTipoEvento);
         catalogoTipoEventoRepository.save(catalogoTipoEvento);

      } catch (CatalogoTipoEventoException cteE) {
         throw cteE;
      } catch (ConstraintViolationException cve) {
         logger.error("===>>>Error en la validacion");
         CatalogoTipoEventoException cteE = new CatalogoTipoEventoException("Error en la validacion", CatalogoTipoEventoException.LAYER_DAO, CatalogoTipoEventoException.ACTION_VALIDATE);
         final Set<ConstraintViolation<?>> violaciones = cve.getConstraintViolations();
         for (Iterator<ConstraintViolation<?>> iterator = violaciones.iterator(); iterator.hasNext(); ) {
            ConstraintViolation<?> siguiente = iterator.next();
            cteE.addError(siguiente.getPropertyPath() + " " + siguiente.getMessage());
         }
         throw cteE;
      } catch (DataIntegrityViolationException dive) {
         CatalogoTipoEventoException cteE = new CatalogoTipoEventoException("No fue posible modificar  CatalogoTipoEvento", CatalogoTipoEventoException.LAYER_DAO, CatalogoTipoEventoException.ACTION_INSERT);
         cteE.addError("Ocurrio un error al modificar CatalogoTipoEvento");
         logger.error("===>>>Error al modificar CatalogoTipoEvento - CODE: {} - {}", cteE.getExceptionCode(), catalogoTipoEventoView, dive);
         throw cteE;
      } catch (Exception ex) {
         CatalogoTipoEventoException cteE = new CatalogoTipoEventoException("Error inesperado al modificar  CatalogoTipoEvento", CatalogoTipoEventoException.LAYER_DAO, CatalogoTipoEventoException.ACTION_INSERT);
         cteE.addError("Ocurrio un error al modificar CatalogoTipoEvento");
         logger.error("===>>>Error al modificar CatalogoTipoEvento - CODE: {} - {}", cteE.getExceptionCode(), catalogoTipoEventoView, ex);
         throw cteE;
      }
   }


   @Override
   public CatalogoTipoEventoView getDetailsByIdCatalogoTipoEvento(Integer idCatalogoTipoEvento) throws CatalogoTipoEventoException {
      try {
         if (!catalogoTipoEventoRepository.exists(idCatalogoTipoEvento)) {
            logger.error("===>>>idCatalogoTipoEvento no encontrado: {}", idCatalogoTipoEvento);
            CatalogoTipoEventoException cteE = new CatalogoTipoEventoException("No se encuentra en el sistema CatalogoTipoEvento", CatalogoTipoEventoException.LAYER_DAO, CatalogoTipoEventoException.ACTION_VALIDATE);
            cteE.addError("idCatalogoTipoEvento no encontrado: " + idCatalogoTipoEvento);
            throw cteE;
         }

         CatalogoTipoEvento catalogoTipoEvento = catalogoTipoEventoRepository.findOne(idCatalogoTipoEvento);
         return catalogoTipoEventoConverter.toView(catalogoTipoEvento, Boolean.TRUE);

      } catch (CatalogoTipoEventoException cteE) {
         throw cteE;
      } catch (ConstraintViolationException cve) {
         logger.error("===>>>Error en la validacion");
         CatalogoTipoEventoException cteE = new CatalogoTipoEventoException("Error en la validacion", CatalogoTipoEventoException.LAYER_DAO, CatalogoTipoEventoException.ACTION_VALIDATE);
         final Set<ConstraintViolation<?>> violaciones = cve.getConstraintViolations();
         for (Iterator<ConstraintViolation<?>> iterator = violaciones.iterator(); iterator.hasNext(); ) {
            ConstraintViolation<?> siguiente = iterator.next();
            cteE.addError(siguiente.getPropertyPath() + ": " + siguiente.getMessage());
         }
         throw cteE;
      } catch (DataIntegrityViolationException dive) {
         CatalogoTipoEventoException cteE = new CatalogoTipoEventoException("No fue posible obtener  CatalogoTipoEvento", CatalogoTipoEventoException.LAYER_DAO, CatalogoTipoEventoException.ACTION_INSERT);
         cteE.addError("Ocurrio un error al obtener CatalogoTipoEvento");
         logger.error("===>>>Error al obtener nuevo CatalogoTipoEvento - CODE: {} - {}", cteE.getExceptionCode(), idCatalogoTipoEvento, dive);
         throw cteE;
      } catch (Exception ex) {
         CatalogoTipoEventoException cteE = new CatalogoTipoEventoException("Error inesperado al obtener  CatalogoTipoEvento", CatalogoTipoEventoException.LAYER_DAO, CatalogoTipoEventoException.ACTION_INSERT);
         cteE.addError("Ocurrio un error al obtener CatalogoTipoEvento");
         logger.error("===>>>Error al obtener nuevo CatalogoTipoEvento - CODE: {} - {}", cteE.getExceptionCode(), idCatalogoTipoEvento, ex);
         throw cteE;
      }
   }

   @Override
   public List<CatalogoTipoEventoView> findAll() throws  CatalogoTipoEventoException{
      try {
         List<CatalogoTipoEvento> catalogoTipoEventoList = catalogoTipoEventoRepository.findAll();
         List<CatalogoTipoEventoView> catalogoTipoEventoViewList = new ArrayList<>();
         for (CatalogoTipoEvento temp : catalogoTipoEventoList) {
            catalogoTipoEventoViewList.add(catalogoTipoEventoConverter.toView(temp, Boolean.TRUE));
         }
         return catalogoTipoEventoViewList;
      } catch (Exception ex) {
         CatalogoTipoEventoException cteE = new CatalogoTipoEventoException("Error inesperado al obtener todos los registros de  CatalogoTipoEvento", CatalogoTipoEventoException.LAYER_DAO, CatalogoTipoEventoException.ACTION_INSERT);
         cteE.addError("Ocurrio un error al obtener CatalogoTipoEvento");
         logger.error("===>>>Error al obtener todos los registros de CatalogoTipoEvento - CODE: {} - {}", cteE.getExceptionCode(), ex);
         throw cteE;
      }
   }

}
