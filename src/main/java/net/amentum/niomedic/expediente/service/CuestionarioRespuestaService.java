package net.amentum.niomedic.expediente.service;

import net.amentum.niomedic.expediente.exception.CuestionarioRespuestaException;
import net.amentum.niomedic.expediente.views.CuestionarioRespuestaView;

import java.util.List;

public interface CuestionarioRespuestaService {

    void createCuestionarioRespuesta(CuestionarioRespuestaView view) throws CuestionarioRespuestaException;

    List<CuestionarioRespuestaView> getRespuestaSearch(String idPaciente, Integer idCuestionario) throws CuestionarioRespuestaException;
}
