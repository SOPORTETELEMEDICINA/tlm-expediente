package net.amentum.niomedic.expediente.service;

import net.amentum.niomedic.expediente.exception.CatCie9Exception;
import net.amentum.niomedic.expediente.views.CatCie9FiltradoView;
import net.amentum.niomedic.expediente.views.CatCie9View;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CatCie9Service {

   CatCie9View getDetailsByIdCie9(Long idCie9) throws CatCie9Exception;

   List<CatCie9FiltradoView> findAll() throws CatCie9Exception;

   Page<CatCie9FiltradoView> getCatCie9Search(String datosBusqueda, Boolean activo, Integer page, Integer size, String orderColumn, String orderType, String sexo, Integer edad) throws CatCie9Exception;

   CatCie9View createCatCie9(CatCie9View catCie9View) throws CatCie9Exception;

   CatCie9View updateCatCie9(CatCie9View catCie9View) throws CatCie9Exception;

}