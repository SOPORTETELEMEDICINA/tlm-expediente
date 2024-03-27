package net.amentum.niomedic.expediente.service;

import net.amentum.niomedic.expediente.exception.AsuetosException;
import net.amentum.niomedic.expediente.views.AsuetosView;

import java.util.List;

public interface AsuetosService {
   void createAsuetos(AsuetosView asuetosView) throws AsuetosException;

   void updateAsuetos(AsuetosView asuetosView) throws AsuetosException;

   AsuetosView getDetailsByIdAsuetos(Long idAsuetos) throws AsuetosException;

   void deleteAsuetos(Long idAsuetos) throws AsuetosException;

   List<AsuetosView> findAll() throws  AsuetosException;
}
