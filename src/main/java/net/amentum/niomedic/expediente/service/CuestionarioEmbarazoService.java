package net.amentum.niomedic.expediente.service;

import net.amentum.niomedic.expediente.exception.CuestionarioEmbarazoException;
import net.amentum.niomedic.expediente.views.CuestionarioEmbarazoView;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CuestionarioEmbarazoService {

    void createCuestionarioEmbarazo(CuestionarioEmbarazoView view) throws CuestionarioEmbarazoException;

    void updateCuestionarioEmbarazo(CuestionarioEmbarazoView view) throws CuestionarioEmbarazoException;

    void deleteCuestionarioEmbarazo(CuestionarioEmbarazoView view) throws CuestionarioEmbarazoException;

    List<CuestionarioEmbarazoView> findAll() throws CuestionarioEmbarazoException;

    Page<CuestionarioEmbarazoView> getCuestionarioEmbarazoSearch(String pacidfk, Integer page, Integer size, String orderColumn, String orderType) throws CuestionarioEmbarazoException;

    Page<CuestionarioEmbarazoView> getCuestionarioEmbarazoFechaSearch(String pacidfk, String fechaInicio, String fechaFin , Integer page, Integer size, String orderColumn, String orderType) throws CuestionarioEmbarazoException;
}
