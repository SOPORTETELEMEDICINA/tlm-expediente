package net.amentum.niomedic.expediente.service;

import net.amentum.niomedic.expediente.exception.CatalogoTipoEventoException;
import net.amentum.niomedic.expediente.views.CatalogoTipoEventoView;

import java.util.List;

public interface CatalogoTipoEventoService {
   void createCatalogoTipoEvento(CatalogoTipoEventoView catalogoTipoEventoView) throws CatalogoTipoEventoException;

   void updateCatalogoTipoEvento(CatalogoTipoEventoView catalogoTipoEventoView) throws CatalogoTipoEventoException;

   CatalogoTipoEventoView getDetailsByIdCatalogoTipoEvento(Integer idCatalogoTipoEvento) throws CatalogoTipoEventoException;

   List<CatalogoTipoEventoView> findAll() throws  CatalogoTipoEventoException;
}
