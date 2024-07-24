package net.amentum.niomedic.expediente.service;

import net.amentum.niomedic.expediente.exception.CatCie10Exception;
import net.amentum.niomedic.expediente.views.CatCie10FiltradoView;
import net.amentum.niomedic.expediente.views.CatCie10View;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CatCie10Service {

   CatCie10View getDetailsByIdCie10(Long idCie10) throws CatCie10Exception;

   List<CatCie10FiltradoView> findAll() throws CatCie10Exception;

   Page<CatCie10FiltradoView> getCatCie10Search(String datosBusqueda, Boolean activo, Integer page, Integer size, String orderColumn, String orderType, String sexo, Integer edad) throws CatCie10Exception;

   CatCie10View createCatCie10(CatCie10View catCie9View) throws CatCie10Exception;

   CatCie10View updateCatCie10(CatCie10View catCie9View) throws CatCie10Exception;

}