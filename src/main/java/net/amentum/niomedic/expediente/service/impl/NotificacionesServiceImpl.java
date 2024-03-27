package net.amentum.niomedic.expediente.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.amentum.niomedic.expediente.converter.NotificacionesConverter;
import net.amentum.niomedic.expediente.exception.ExceptionServiceCode;
import net.amentum.niomedic.expediente.exception.NotificacionesException;
import net.amentum.niomedic.expediente.model.Notificaciones;
import net.amentum.niomedic.expediente.persistence.NotificacionesRepository;
import net.amentum.niomedic.expediente.service.NotificacionesService;
import net.amentum.niomedic.expediente.views.NotificacionesView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//import javax.validation.ConstraintViolationException;
import org.hibernate.exception.ConstraintViolationException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import net.amentum.niomedic.expediente.views.CatEstadoConsultaView;


//@SuppressWarnings("ALL")
@Service
@Transactional(readOnly = true)
@Slf4j
public class NotificacionesServiceImpl implements NotificacionesService {
   private final Map<String, Object> colOrderNames = new HashMap<>();
   private NotificacionesRepository notificacionesRepository;
   private NotificacionesConverter notificacionesConverter;

   @Autowired
   public void setNotificacionesRepository(NotificacionesRepository notificacionesRepository) {
      this.notificacionesRepository = notificacionesRepository;
   }

   @Autowired
   public void setNotificacionesConverter(NotificacionesConverter notificacionesConverter) {
      this.notificacionesConverter = notificacionesConverter;
   }


   @Transactional(readOnly = false, rollbackFor = {NotificacionesException.class})
   @Override
   public NotificacionesView createNotificaciones(NotificacionesView notificacionesView) throws NotificacionesException {
      try {

         notificacionesView.setEstatus(1);
         Notificaciones notificaciones = notificacionesConverter.toEntity(notificacionesView, new Notificaciones(), Boolean.FALSE);
         log.info("===>>>Insertar nuevo Notificaciones: {}", notificaciones);
         notificacionesRepository.save(notificaciones);
         return notificacionesConverter.toView(notificaciones, Boolean.TRUE);

      } catch (ConstraintViolationException cve) {
         log.error("===>>>createNotificaciones() - Ocurrio un error al Crear los periodos en la DB - error :{}", cve);
         throw new NotificacionesException(HttpStatus.BAD_REQUEST, String.format(NotificacionesException.SERVER_ERROR, "Crear"));
      } catch (DataIntegrityViolationException dive) {
         log.error("===>>>createNotificaciones() - Ocurrio un error al Crear las Notificaciones en la DB - error :{}", dive);
         throw new NotificacionesException(HttpStatus.CONFLICT, String.format(NotificacionesException.SERVER_ERROR, "Crear"));
      } catch (IllegalArgumentException iae) {
         log.error("===>>>createNotificaciones() - adjuntos no esta bien creado: {}", iae);
         throw new NotificacionesException(HttpStatus.BAD_REQUEST, "Adjuntos no esta bien creado");
      } catch (Exception ex) {
         log.error("===>>>" + ExceptionServiceCode.NOTIFICACIONES + " createNotificaciones() -  Ocurrio Un error al Crear los Periodos - error:{}", ex);
         throw new NotificacionesException(HttpStatus.INTERNAL_SERVER_ERROR, String.format(NotificacionesException.SERVER_ERROR, "Crear"));
      }
   }

   @Transactional(readOnly = false, rollbackFor = {NotificacionesException.class})
   @Override
   public NotificacionesView updateNotificaciones(NotificacionesView notificacionesView) throws NotificacionesException {
      try {
//         DONE: que exista el registro
         if (!notificacionesRepository.exists(notificacionesView.getIdNotificaciones())) {
            log.error("===>>>updateNotificaciones() - No encontrado el id : {}", notificacionesView.getIdNotificaciones());
            throw new NotificacionesException(HttpStatus.NOT_FOUND, String.format(NotificacionesException.ITEM_NO_ENCONTRADO, notificacionesView.getIdNotificaciones()));
         }

         Long idNotificaciones = notificacionesView.getIdNotificaciones();
         Notificaciones notificaciones = notificacionesRepository.findOne(idNotificaciones);

         if (notificaciones.getEstatus() == 3) {
            log.error("===>>>readedNotificaciones() - No se puede modificar una Notificación id: {}, con estatus Borrado", idNotificaciones);
            throw new NotificacionesException(HttpStatus.PRECONDITION_FAILED, String.format(NotificacionesException.ESTATUS_BORRADO, idNotificaciones));
         }

         if (notificaciones.getEstatus() == 2) {
            log.error("===>>>readedNotificaciones() - No se puede modificar una Notificación id: {}, con estatus Leído", idNotificaciones);
            throw new NotificacionesException(HttpStatus.PRECONDITION_FAILED, String.format(NotificacionesException.ESTATUS_LEIDO, idNotificaciones));
         }

         notificacionesView.setEstatus(notificaciones.getEstatus());
         notificaciones = notificacionesConverter.toEntity(notificacionesView, notificaciones, Boolean.TRUE);
         log.info("===>>>updateNotificaciones() - Editar Notificaciones: {}", notificaciones);
         notificacionesRepository.save(notificaciones);
         return notificacionesConverter.toView(notificaciones, Boolean.TRUE);

      } catch (NotificacionesException notifE) {
         throw notifE;
      } catch (ConstraintViolationException cve) {
         log.error("===>>>updateNotificaciones() - Ocurrio un error al Modificar las Notificaciones en la DB - error :{}", cve);
         throw new NotificacionesException(HttpStatus.BAD_REQUEST, String.format(NotificacionesException.SERVER_ERROR, "Modificar"));
      } catch (DataIntegrityViolationException dive) {
         log.error("===>>>updateNotificaciones() - Ocurrio un error al Modificar las Notificaciones en la DB - error :{}", dive);
         throw new NotificacionesException(HttpStatus.CONFLICT, String.format(NotificacionesException.SERVER_ERROR, "Modificar"));
      } catch (IllegalArgumentException iae) {
         log.error("===>>>updateNotificaciones() - adjuntos no esta bien creado: {}", iae);
         throw new NotificacionesException(HttpStatus.BAD_REQUEST, "Adjuntos no esta bien creado");
      } catch (Exception ex) {
         log.error("===>>>" + ExceptionServiceCode.NOTIFICACIONES + " updateNotificaciones() -  Ocurrio Un error al Modificar las Notificaciones - error:{}", ex);
         throw new NotificacionesException(HttpStatus.INTERNAL_SERVER_ERROR, String.format(NotificacionesException.SERVER_ERROR, "Modificar"));
      }
   }

   @Override
   public NotificacionesView getDetailsByIdNotificaciones(Long idNotificaciones) throws NotificacionesException {
      try {
         //         DONE: que exista el registro
         if (!notificacionesRepository.exists(idNotificaciones)) {
            log.error("===>>>updateNotificaciones() - No encontrado el id : {}", idNotificaciones);
            throw new NotificacionesException(HttpStatus.NOT_FOUND, String.format(NotificacionesException.ITEM_NO_ENCONTRADO, idNotificaciones));
         }

         Notificaciones notificaciones = notificacionesRepository.findOne(idNotificaciones);
         return notificacionesConverter.toView(notificaciones, Boolean.TRUE);

      } catch (NotificacionesException notifE) {
         throw notifE;
      } catch (Exception ex) {
         log.error("===>>>" + ExceptionServiceCode.NOTIFICACIONES + " updateNotificaciones() -  Ocurrio Un error al Obtener las Notificaciones - error:{}", ex);
         throw new NotificacionesException(HttpStatus.INTERNAL_SERVER_ERROR, String.format(NotificacionesException.SERVER_ERROR, "Obtener"));
      }
   }

   @Override
   public List<NotificacionesView> getNotificacionesSearch(Long idUsuario, Integer estatus) throws NotificacionesException {
      try {
         List<Notificaciones> notificacionesList = notificacionesRepository.findByIdUsuarioAndEstatus(idUsuario, estatus);
         //         DONE: que exista el registro
         if (notificacionesList == null || notificacionesList.isEmpty()) {
            String mensaje = "No encontrado el idUsuario: " + idUsuario + ", por estatus: " + estatus;
            log.error("===>>>getNotificacionesSearch() - " + mensaje);
            throw new NotificacionesException(HttpStatus.NOT_FOUND, mensaje);
         }

         List<NotificacionesView> notificacionesViewList = new ArrayList<>();
         for (Notificaciones notif : notificacionesList) {
            notificacionesViewList.add(notificacionesConverter.toView(notif, Boolean.TRUE));
         }

         return notificacionesViewList;

      } catch (NotificacionesException notifE) {
         throw notifE;
      } catch (Exception ex) {
         log.error("===>>>" + ExceptionServiceCode.NOTIFICACIONES + " getNotificacionesSearch() -  Ocurrio Un error al Obtener las Notificaciones - error:{}", ex);
         throw new NotificacionesException(HttpStatus.INTERNAL_SERVER_ERROR, String.format(NotificacionesException.SERVER_ERROR, "Obtener"));
      }
   }

   @Transactional(readOnly = false, rollbackFor = {NotificacionesException.class})
   @Override
   public void deleteNotificaciones(Long idNotificaciones) throws NotificacionesException {
      try {
//         DONE: que exista el registro
         if (!notificacionesRepository.exists(idNotificaciones)) {
            log.error("===>>>deleteNotificaciones() - No encontrado el id : {}", idNotificaciones);
            throw new NotificacionesException(HttpStatus.NOT_FOUND, String.format(NotificacionesException.ITEM_NO_ENCONTRADO, idNotificaciones));
         }

         Notificaciones notificaciones = notificacionesRepository.findOne(idNotificaciones);
         notificaciones.setEstatus(3);

         log.info("===>>>deleteNotificaciones() - Editar Notificaciones: {}", notificaciones);
         notificacionesRepository.save(notificaciones);

      } catch (NotificacionesException notifE) {
         throw notifE;
      } catch (ConstraintViolationException cve) {
         log.error("===>>>deleteNotificaciones() - Ocurrio un error al Modificar las Notificaciones en la DB - error :{}", cve);
         throw new NotificacionesException(HttpStatus.BAD_REQUEST, String.format(NotificacionesException.SERVER_ERROR, "Modificar"));
      } catch (DataIntegrityViolationException dive) {
         log.error("===>>>deleteNotificaciones() - Ocurrio un error al Modificar las Notificaciones en la DB - error :{}", dive);
         throw new NotificacionesException(HttpStatus.CONFLICT, String.format(NotificacionesException.SERVER_ERROR, "Modificar"));
      } catch (IllegalArgumentException iae) {
         log.error("===>>>deleteNotificaciones() - adjuntos no esta bien creado: {}", iae);
         throw new NotificacionesException(HttpStatus.BAD_REQUEST, "Adjuntos no esta bien creado");
      } catch (Exception ex) {
         log.error("===>>>" + ExceptionServiceCode.NOTIFICACIONES + " deleteNotificaciones() -  Ocurrio Un error al Modificar las Notificaciones - error:{}", ex);
         throw new NotificacionesException(HttpStatus.INTERNAL_SERVER_ERROR, String.format(NotificacionesException.SERVER_ERROR, "Modificar"));
      }
   }

   @Transactional(readOnly = false, rollbackFor = {NotificacionesException.class})
   @Override
   public void readedNotificaciones(Long idNotificaciones) throws NotificacionesException {
      try {
//         DONE: que exista el registro
         if (!notificacionesRepository.exists(idNotificaciones)) {
            log.error("===>>>readedNotificaciones() - No encontrado el id : {}", idNotificaciones);
            throw new NotificacionesException(HttpStatus.NOT_FOUND, String.format(NotificacionesException.ITEM_NO_ENCONTRADO, idNotificaciones));
         }

         Notificaciones notificaciones = notificacionesRepository.findOne(idNotificaciones);

         if (notificaciones.getEstatus() == 3) {
            log.error("===>>>readedNotificaciones() - No se puede modificar una Notificacion id: {}, con estatus Borrado", idNotificaciones);
            throw new NotificacionesException(HttpStatus.PRECONDITION_FAILED, String.format(NotificacionesException.ESTATUS_BORRADO, idNotificaciones));
         }

         notificaciones.setEstatus(2);
         notificaciones.setFechaLeido(new Date());

         log.info("===>>>readedNotificaciones() - Editar Notificaciones: {}", notificaciones);
         notificacionesRepository.save(notificaciones);

      } catch (NotificacionesException notifE) {
         throw notifE;
      } catch (ConstraintViolationException cve) {
         log.error("===>>>readedNotificaciones() - Ocurrio un error al Modificar las Notificaciones en la DB - error :{}", cve);
         throw new NotificacionesException(HttpStatus.BAD_REQUEST, String.format(NotificacionesException.SERVER_ERROR, "Modificar"));
      } catch (DataIntegrityViolationException dive) {
         log.error("===>>>readedNotificaciones() - Ocurrio un error al Modificar las Notificaciones en la DB - error :{}", dive);
         throw new NotificacionesException(HttpStatus.CONFLICT, String.format(NotificacionesException.SERVER_ERROR, "Modificar"));
      } catch (IllegalArgumentException iae) {
         log.error("===>>>readedNotificaciones() - adjuntos no esta bien creado: {}", iae);
         throw new NotificacionesException(HttpStatus.BAD_REQUEST, "Adjuntos no esta bien creado");
      } catch (Exception ex) {
         log.error("===>>>" + ExceptionServiceCode.NOTIFICACIONES + " readedNotificaciones() -  Ocurrio Un error al Modificar las Notificaciones - error:{}", ex);
         throw new NotificacionesException(HttpStatus.INTERNAL_SERVER_ERROR, String.format(NotificacionesException.SERVER_ERROR, "Modificar"));
      }
   }
}
