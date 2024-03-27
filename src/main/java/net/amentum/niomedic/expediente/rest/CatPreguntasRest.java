package net.amentum.niomedic.expediente.rest;

import net.amentum.common.BaseController;
import net.amentum.niomedic.expediente.exception.CatPreguntasException;
import net.amentum.niomedic.expediente.exception.CatalogoDiaException;
import net.amentum.niomedic.expediente.service.CatPreguntasService;
import net.amentum.niomedic.expediente.views.CatCuestionarioHeader;
import net.amentum.niomedic.expediente.views.CatPreguntaView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;

@RestController
@RequestMapping("cat_preguntas")
public class CatPreguntasRest extends BaseController {

    final Logger logger = LoggerFactory.getLogger(CatPreguntasRest.class);

    @Autowired
    CatPreguntasService service;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void createPregunta(@RequestBody @Valid CatPreguntaView view) throws CatPreguntasException {
        try {
            logger.info("Insertando nueva pregunta: {}", view);
            if (view.getPregunta() == null || view.getPregunta().isEmpty()) {
                logger.error("Texto de pregunta vacío");
                CatPreguntasException ex = new CatPreguntasException("Ocurrió un error al agregar la pregunta", CatPreguntasException.LAYER_REST, CatPreguntasException.ACTION_VALIDATE);
                ex.addError("El texto de la pregunta viene vacío");
                throw ex;
            }
            service.createPregunta(view);
        } catch (CatPreguntasException ex) {
            throw ex;
        } catch(Exception ex) {
            logger.error("Error al insertar una pregunta : ", ex);
            CatPreguntasException catEx = new CatPreguntasException("Ocurrió un error al agregar la pregunta", CatPreguntasException.LAYER_REST, CatPreguntasException.ACTION_VALIDATE);
            catEx.addError("Error al insertar una pregunta: " + ex);
            throw catEx;

        }
    }

    @RequestMapping(value = "{idPregunta}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void updatePregunta(@PathVariable() Integer idPregunta, @RequestBody @Valid CatPreguntaView view) throws CatPreguntasException {
        try {
            logger.info("Modificando pregunta: {}", view);
            if (view.getPregunta() == null || view.getPregunta().isEmpty()) {
                logger.error("Texto de pregunta vacío");
                CatPreguntasException ex = new CatPreguntasException("Ocurrió un error al modificar la pregunta", CatPreguntasException.LAYER_REST, CatPreguntasException.ACTION_VALIDATE);
                ex.addError("El texto de la pregunta viene vacío");
                throw ex;
            }
            if (idPregunta == null || idPregunta <= 0) {
                logger.error("Id de pregunta vacío");
                CatPreguntasException ex = new CatPreguntasException("Ocurrió un error al modificar la pregunta", CatPreguntasException.LAYER_REST, CatPreguntasException.ACTION_VALIDATE);
                ex.addError("El id de la pregunta viene vacío");
                throw ex;
            }
            service.updatePregunta(idPregunta, view);
        } catch (CatPreguntasException ex) {
            throw ex;
        } catch(Exception ex) {
            logger.error("Error al modificar una pregunta : ", ex);
            CatPreguntasException catEx = new CatPreguntasException("Ocurrió un error al modificar la pregunta", CatPreguntasException.LAYER_REST, CatPreguntasException.ACTION_VALIDATE);
            catEx.addError("Error al modificar una pregunta: " + ex);
            throw catEx;

        }
    }

    @RequestMapping(value = "find", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Page<CatCuestionarioHeader> getPreguntaSearch(@RequestParam(required = false) Integer idCuestionario,
                                                          @RequestParam(required = false) Integer page,
                                                          @RequestParam(required = false) Integer size,
                                                          @RequestParam(required = false) String orderColumn,
                                                          @RequestParam(required = false) String orderType) throws CatPreguntasException {
        try {
            logger.info("getPreguntaSearch(): idCuestionario: {} - page: {} - size: {} - orderColumn: {} - orderType: {}",
                    idCuestionario, page, size,orderColumn, orderType);
            if(idCuestionario == null ) {
                logger.error("idCuestionario es nulo");
                idCuestionario = 0;
            }
            if(page == null)
                page = 0;
            if(size == null)
                size = 10;
            if(orderType == null || orderType.isEmpty())
                orderType = "ASC";
            if(orderColumn == null || orderColumn.isEmpty())
                orderColumn = "sort";
            return service.getPreguntaSearch(idCuestionario,page,size,orderColumn,orderType);
        } catch(CatPreguntasException ex) {
            throw ex;
        } catch (Exception ex) {
            logger.error("Error al obtener preguntas - {}", ex.toString());
            CatPreguntasException exception = new CatPreguntasException("Ocurrió un error al buscar las preguntas", CatPreguntasException.LAYER_REST, CatPreguntasException.ACTION_VALIDATE);
            exception.addError("Error - " + ex);
            throw exception;
        }
    }

    @RequestMapping(value = "search", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Page<CatPreguntaView> getPreguntaSearchPage(@RequestParam(required = false) Integer page,
                                                         @RequestParam(required = false) Integer size,
                                                         @RequestParam(required = false) String orderColumn,
                                                         @RequestParam(required = false) String orderType) throws CatPreguntasException {
        try {
            logger.info("getPreguntaSearch(): page: {} - size: {} - orderColumn: {} - orderType: {}",
                    page, size,orderColumn, orderType);
            if(page == null)
                page = 0;
            if(size == null)
                size = 10;
            if(orderType == null || orderType.isEmpty())
                orderType = "ASC";
            if(orderColumn == null || orderColumn.isEmpty())
                orderColumn = "sort";
            return service.getPreguntaSearchPage(page,size,orderColumn,orderType);
        } catch(CatPreguntasException ex) {
            throw ex;
        } catch (Exception ex) {
            logger.error("Error al obtener preguntas - {}", ex.toString());
            CatPreguntasException exception = new CatPreguntasException("Ocurrió un error al buscar las preguntas", CatPreguntasException.LAYER_REST, CatPreguntasException.ACTION_VALIDATE);
            exception.addError("Error - " + ex);
            throw exception;
        }
    }

    @RequestMapping(value = "{idPregunta}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public CatPreguntaView getPregunta(@PathVariable() Integer idPregunta) throws CatPreguntasException {
        try {
            logger.info("Buscar pregunta: {}", idPregunta);
            if(idPregunta == null || idPregunta < 0) {
                logger.error("Error al buscar una pregunta: id nulo/vacío");
                CatPreguntasException catEx = new CatPreguntasException("Ocurrió un error al buscar la pregunta", CatPreguntasException.LAYER_REST, CatPreguntasException.ACTION_VALIDATE);
                catEx.addError("Error al buscar una pregunta: id nulo/vacío");
                throw catEx;
            }
            return service.getPregunta(idPregunta);
        } catch (CatPreguntasException ex) {
            throw ex;
        } catch(Exception ex) {
            logger.error("Error al buscar una pregunta : ", ex);
            CatPreguntasException catEx = new CatPreguntasException("Ocurrió un error al buscar la pregunta", CatPreguntasException.LAYER_REST, CatPreguntasException.ACTION_VALIDATE);
            catEx.addError("Error al buscar una pregunta: " + ex);
            throw catEx;
        }
    }

}
