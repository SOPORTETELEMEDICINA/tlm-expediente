package net.amentum.niomedic.expediente.service;

import net.amentum.niomedic.expediente.exception.CatPreguntasException;
import net.amentum.niomedic.expediente.views.CatCuestionarioHeader;
import net.amentum.niomedic.expediente.views.CatPreguntaView;
import org.springframework.data.domain.Page;

public interface CatPreguntasService {

    void createPregunta(CatPreguntaView view) throws CatPreguntasException;

    void updatePregunta(Integer idPregunta, CatPreguntaView view) throws CatPreguntasException;

    CatPreguntaView getPregunta(Integer idPregunta) throws CatPreguntasException;

    Page<CatCuestionarioHeader> getPreguntaSearch(Integer idCuestionario, Integer page, Integer size, String orderColumn, String orderType) throws CatPreguntasException;

    Page<CatPreguntaView> getPreguntaSearchPage(Integer page, Integer size, String orderColumn, String orderType) throws CatPreguntasException;
}
