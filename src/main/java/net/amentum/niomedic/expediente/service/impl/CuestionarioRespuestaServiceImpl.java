package net.amentum.niomedic.expediente.service.impl;

import net.amentum.niomedic.expediente.converter.CuestionarioRespuestaConverter;
import net.amentum.niomedic.expediente.exception.CuestionarioRespuestaException;
import net.amentum.niomedic.expediente.model.CuestionarioRespuesta;
import net.amentum.niomedic.expediente.persistence.CuestionarioRespuestaRepository;
import net.amentum.niomedic.expediente.service.CuestionarioRespuestaService;
import net.amentum.niomedic.expediente.views.CuestionarioRespuestaView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class CuestionarioRespuestaServiceImpl implements CuestionarioRespuestaService {

    final Logger logger = LoggerFactory.getLogger(CuestionarioRespuestaServiceImpl.class);

    @Autowired
    CuestionarioRespuestaRepository repository;

    @Autowired
    CuestionarioRespuestaConverter converter;

    @Transactional(rollbackFor = {CuestionarioRespuestaException.class})
    @Override
    public void createCuestionarioRespuesta(CuestionarioRespuestaView view) throws CuestionarioRespuestaException {
        try {
            CuestionarioRespuesta respuesta = converter.toEntity(view);
            logger.info("Respuesta nueva: {}", respuesta);
            repository.save(respuesta);
        } catch(DataIntegrityViolationException ex) {
            CuestionarioRespuestaException exception = new CuestionarioRespuestaException("No fue posible agregar la respuesta nueva", CuestionarioRespuestaException.LAYER_DAO, CuestionarioRespuestaException.ACTION_INSERT);
            exception.addError("DataIntegrityViolationException - Error al insertar la respuesta: " + ex);
            logger.error("DataIntegrityViolationException - Error al insertar la respuesta - {} - CODE {} - {}", view, exception.getExceptionCode(), ex);
            throw exception;
        } catch(Exception ex) {
            CuestionarioRespuestaException exception = new CuestionarioRespuestaException("No fue posible agregar la respuesta nueva", CuestionarioRespuestaException.LAYER_DAO, CuestionarioRespuestaException.ACTION_INSERT);
            exception.addError("Error al insertar la respuesta: " + ex);
            logger.error("Error al insertar la respuesta - {} - CODE {} - {}", view, exception.getExceptionCode(), ex);
            throw exception;
        }
    }

    @Override
    public List<CuestionarioRespuestaView> getRespuestaSearch(String idPaciente, Integer idCuestionario) throws CuestionarioRespuestaException {
        try {
            List<CuestionarioRespuestaView> viewList = new ArrayList<>();
            List<CuestionarioRespuesta> respuestaList = repository.findByIdPacienteAndIdCuestionario(idPaciente,idCuestionario);
            if(respuestaList != null && !respuestaList.isEmpty())
                respuestaList.forEach(cuestionarioRespuesta -> viewList.add(converter.toView(cuestionarioRespuesta)));
            return viewList;
        } catch(Exception ex) {
            CuestionarioRespuestaException exception = new CuestionarioRespuestaException("No fue posible agregar la respuesta nueva", CuestionarioRespuestaException.LAYER_DAO, CuestionarioRespuestaException.ACTION_SELECT);
            exception.addError("Error al obtener las respuestas: " + ex);
            logger.error("Error al obtener las respuestas - {} ", ex.toString());
            throw exception;
        }
    }

}
