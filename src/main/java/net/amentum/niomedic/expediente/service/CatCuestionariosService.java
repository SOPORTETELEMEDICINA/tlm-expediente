package net.amentum.niomedic.expediente.service;

import net.amentum.niomedic.expediente.exception.CatCuestionariosException;
import net.amentum.niomedic.expediente.views.CatCuestionarioView;
import org.springframework.data.domain.Page;


public interface CatCuestionariosService {

    void createCuestionario(CatCuestionarioView view) throws CatCuestionariosException;

    void updateCuestionario(Integer idCuestionario, CatCuestionarioView view) throws CatCuestionariosException;

    CatCuestionarioView getCuestionario(Integer idCuestionario) throws CatCuestionariosException;

    Page<CatCuestionarioView> getCuestionarioSearch(Integer page, Integer size, String orderColumn, String orderType) throws CatCuestionariosException;

}
