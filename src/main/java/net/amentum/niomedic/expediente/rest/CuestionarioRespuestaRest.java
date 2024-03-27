package net.amentum.niomedic.expediente.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.amentum.common.BaseController;
import net.amentum.niomedic.expediente.exception.CuestionarioRespuestaException;
import net.amentum.niomedic.expediente.service.CuestionarioRespuestaService;
import net.amentum.niomedic.expediente.views.CuestionarioRespuestaView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("cuestionario_respuesta")
public class CuestionarioRespuestaRest extends BaseController {

    final Logger logger = LoggerFactory.getLogger(CuestionarioRespuestaRest.class);

    @Autowired
    CuestionarioRespuestaService service;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void createCuestionarioRespuesta(@RequestBody @Valid CuestionarioRespuestaView view) throws CuestionarioRespuestaException {
        try {
            logger.info("Insertando respuesta nueva: {}", view);
            if(view.getIdCuestionario() == null || view.getIdCuestionario() < 0) {
                logger.error("Id cuestionario vacío");
                CuestionarioRespuestaException exception = new CuestionarioRespuestaException("No fue posible agregar la respuesta nueva", CuestionarioRespuestaException.LAYER_REST, CuestionarioRespuestaException.ACTION_VALIDATE);
                exception.addError("Id cuestionario vacío");
                throw exception;
            }
            if(view.getIdPaciente() == null || view.getIdPaciente().isEmpty()) {
                CuestionarioRespuestaException exception = new CuestionarioRespuestaException("No fue posible agregar la respuesta nueva", CuestionarioRespuestaException.LAYER_REST, CuestionarioRespuestaException.ACTION_VALIDATE);
                exception.addError("Id paciente vacío");
                logger.error("Id paciente vacío");
                throw exception;
            }
            if(view.getCreadoPor() == null || view.getCreadoPor().isEmpty()) {
                CuestionarioRespuestaException exception = new CuestionarioRespuestaException("No fue posible agregar la respuesta nueva", CuestionarioRespuestaException.LAYER_REST, CuestionarioRespuestaException.ACTION_VALIDATE);
                exception.addError("Creado por vacío");
                logger.error("Creado por vacío");
                throw exception;
            }
            if(view.getJson() == null || view.getJson().isEmpty()) {
                CuestionarioRespuestaException exception = new CuestionarioRespuestaException("No fue posible agregar la respuesta nueva", CuestionarioRespuestaException.LAYER_REST, CuestionarioRespuestaException.ACTION_VALIDATE);
                exception.addError("Json vacío");
                logger.error("Json vacío");
                throw exception;
            }
            service.createCuestionarioRespuesta(view);
        } catch(CuestionarioRespuestaException ex) {
            throw ex;
        } catch(Exception ex) {
            CuestionarioRespuestaException exception = new CuestionarioRespuestaException("No fue posible agregar la respuesta nueva", CuestionarioRespuestaException.LAYER_REST, CuestionarioRespuestaException.ACTION_VALIDATE);
            exception.addError("Error al guardar una nueva respuesta: " + ex);
            logger.error("Error al guardar una nueva respuesta: {}", ex.toString());
            throw exception;
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<CuestionarioRespuestaView> getRespuestaSearch(@RequestParam() String idPaciente,
                                                              @RequestParam() Integer idCuestionario) throws CuestionarioRespuestaException {
        try {
            logger.info("getRespuestaSearch() - idPaciente: {} - idCuestionario: {}", idPaciente, idCuestionario);
            if(idPaciente == null || idPaciente.isEmpty()) {
                CuestionarioRespuestaException exception = new CuestionarioRespuestaException("No fue posible buscar la respuesta", CuestionarioRespuestaException.LAYER_REST, CuestionarioRespuestaException.ACTION_VALIDATE);
                exception.addError("Id paciente vacío");
                logger.error("Id paciente vacío");
                throw exception;
            }
            if(idCuestionario == null || idCuestionario < 0) {
                CuestionarioRespuestaException exception = new CuestionarioRespuestaException("No fue posible buscar la respuesta", CuestionarioRespuestaException.LAYER_REST, CuestionarioRespuestaException.ACTION_VALIDATE);
                exception.addError("Id cuestionario vacío");
                logger.error("Id cuestionario vacío");
                throw exception;
            }
            return service.getRespuestaSearch(idPaciente, idCuestionario);
        } catch(CuestionarioRespuestaException ex) {
            throw ex;
        } catch(Exception ex) {
            CuestionarioRespuestaException exception = new CuestionarioRespuestaException("No fue posible obtener las respuestas", CuestionarioRespuestaException.LAYER_REST, CuestionarioRespuestaException.ACTION_VALIDATE);
            exception.addError("Error al obtener las respuestas: " + ex);
            logger.error("Error al obtener las respuestas: {}", ex.toString());
            throw exception;
        }
    }
}
