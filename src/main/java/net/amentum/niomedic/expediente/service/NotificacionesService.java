package net.amentum.niomedic.expediente.service;

import net.amentum.niomedic.expediente.exception.NotificacionesException;
import net.amentum.niomedic.expediente.views.NotificacionesView;

import java.util.List;

public interface NotificacionesService {

   NotificacionesView createNotificaciones(NotificacionesView notificacionesView) throws NotificacionesException;

   NotificacionesView updateNotificaciones(NotificacionesView notificacionesView) throws NotificacionesException;

   NotificacionesView getDetailsByIdNotificaciones(Long idNotificaciones) throws NotificacionesException;

//   Page<NotificacionesView> getNotificacionesSearch(Long idUsuario, Integer estatus, Long startDate, Long endDate, Integer page, Integer size, String orderColumn, String orderType) throws NotificacionesException;
   List<NotificacionesView> getNotificacionesSearch(Long idUsuario, Integer estatus) throws NotificacionesException;

   void deleteNotificaciones(Long idNotificaciones) throws NotificacionesException;

   void readedNotificaciones(Long idNotificaciones) throws NotificacionesException;
}
