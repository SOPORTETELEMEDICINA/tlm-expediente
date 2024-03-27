package net.amentum.niomedic.expediente.rest;

import net.amentum.common.BaseController;
import net.amentum.niomedic.expediente.exception.CatCuestionariosException;
import net.amentum.niomedic.expediente.service.CatCuestionariosService;
import net.amentum.niomedic.expediente.views.CatCuestionarioView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("cat_cuestionario")
public class CatCuestionarioRest extends BaseController {

    final Logger logger = LoggerFactory.getLogger(CatCuestionarioRest.class);

    @Autowired
    CatCuestionariosService service;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void createCuestionario(@RequestBody @Valid CatCuestionarioView view) throws CatCuestionariosException {
        try {
            logger.info("Insertando nuevo cuestionario: {}", view);
            if(view.getNombre() == null || view.getNombre().isEmpty()) {
                logger.error("Nombre de cuestionario vacío");
                CatCuestionariosException catEx = new CatCuestionariosException("Ocurrió un error al agregar el cuestionario", CatCuestionariosException.LAYER_REST, CatCuestionariosException.ACTION_VALIDATE);
                catEx.addError("Nombre de cuestionario vacío");
                throw catEx;
            }
            if(view.getCreadoPor() == null || view.getCreadoPor().isEmpty()) {
                logger.error("Campo creado-por vacío");
                CatCuestionariosException catEx = new CatCuestionariosException("Ocurrió un error al agregar el cuestionario", CatCuestionariosException.LAYER_REST, CatCuestionariosException.ACTION_VALIDATE);
                catEx.addError("Campo creado-por vacío");
                throw catEx;
            }
            if(view.getSort() == null || view.getSort() < 0) {
                logger.error("Campo sort vacío");
                CatCuestionariosException catEx = new CatCuestionariosException("Ocurrió un error al agregar el cuestionario", CatCuestionariosException.LAYER_REST, CatCuestionariosException.ACTION_VALIDATE);
                catEx.addError("Campo sort vacío");
                throw catEx;
            }
            service.createCuestionario(view);
        } catch (CatCuestionariosException ex) {
            throw ex;
        } catch(Exception ex) {
            logger.error("Error al insertar un cuestionario : ", ex);
            CatCuestionariosException catEx = new CatCuestionariosException("Ocurrió un error al agregar el cuestionario", CatCuestionariosException.LAYER_REST, CatCuestionariosException.ACTION_INSERT);
            catEx.addError("Error al insertar un cuestionario: " + ex);
            throw catEx;
        }
    }

    @RequestMapping(value = "{idCuestionario}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void updateCuestionario(@PathVariable() Integer idCuestionario, @RequestBody @Valid CatCuestionarioView view) throws CatCuestionariosException {
        try {
            logger.info("Modificando cuestionario: {}", view);
            if(view.getNombre() == null || view.getNombre().isEmpty()) {
                logger.error("Nombre de cuestionario vacío");
                CatCuestionariosException catEx = new CatCuestionariosException("Ocurrió un error al agregar el cuestionario", CatCuestionariosException.LAYER_REST, CatCuestionariosException.ACTION_VALIDATE);
                catEx.addError("Nombre de cuestionario vacío");
                throw catEx;
            }
            if(view.getCreadoPor() == null || view.getCreadoPor().isEmpty()) {
                logger.error("Campo creado-por vacío");
                CatCuestionariosException catEx = new CatCuestionariosException("Ocurrió un error al agregar el cuestionario", CatCuestionariosException.LAYER_REST, CatCuestionariosException.ACTION_VALIDATE);
                catEx.addError("Campo creado-por vacío");
                throw catEx;
            }
            if(view.getSort() == null || view.getSort() < 0) {
                logger.error("Campo sort vacío");
                CatCuestionariosException catEx = new CatCuestionariosException("Ocurrió un error al agregar el cuestionario", CatCuestionariosException.LAYER_REST, CatCuestionariosException.ACTION_VALIDATE);
                catEx.addError("Campo sort vacío");
                throw catEx;
            }
            if(view.getActive() == null) {
                logger.error("Campo active vacío");
                CatCuestionariosException catEx = new CatCuestionariosException("Ocurrió un error al agregar el cuestionario", CatCuestionariosException.LAYER_REST, CatCuestionariosException.ACTION_VALIDATE);
                catEx.addError("Campo active vacío");
                throw catEx;
            }
            service.updateCuestionario(idCuestionario, view);
        } catch (CatCuestionariosException ex) {
            throw ex;
        } catch(Exception ex) {
            logger.error("Error al insertar un cuestionario : ", ex);
            CatCuestionariosException catEx = new CatCuestionariosException("Ocurrió un error al agregar el cuestionario", CatCuestionariosException.LAYER_REST, CatCuestionariosException.ACTION_INSERT);
            catEx.addError("Error al insertar un cuestionario: " + ex);
            throw catEx;
        }
    }

    @RequestMapping(value = "{idCuestionario}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public CatCuestionarioView getCuestionario(@PathVariable() Integer idCuestionario) throws CatCuestionariosException {
        try {
            logger.info("Buscando cuestionario: {}", idCuestionario);
            if(idCuestionario == null || idCuestionario < 0) {
                logger.error("Id Cuestionario vacío/nulo");
                CatCuestionariosException catEx = new CatCuestionariosException("Ocurrió un error al buscar el cuestionario", CatCuestionariosException.LAYER_REST, CatCuestionariosException.ACTION_VALIDATE);
                catEx.addError("Id cuestionario vacío/nulo");
                throw catEx;
            }
            return service.getCuestionario(idCuestionario);
        } catch (CatCuestionariosException ex) {
            throw ex;
        } catch(Exception ex) {
            logger.error("Error al buscar un cuestionario : ", ex);
            CatCuestionariosException catEx = new CatCuestionariosException("Ocurrió un error al buscar el cuestionario", CatCuestionariosException.LAYER_REST, CatCuestionariosException.ACTION_INSERT);
            catEx.addError("Error al buscar un cuestionario: " + ex);
            throw catEx;
        }
    }

    @RequestMapping(value = "find", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Page<CatCuestionarioView> getCuestionarioSearch(@RequestParam(required = false) Integer page,
                                                            @RequestParam(required = false) Integer size,
                                                            @RequestParam(required = false) String orderColumn,
                                                            @RequestParam(required = false) String orderType) throws CatCuestionariosException {
        try {
            logger.info("getCuestionarioSearch(): - page: {} - size: {} - orderColumn: {} - orderType: {}", page, size,orderColumn, orderType);
            if(page == null)
                page = 0;
            if(size == null)
                size = 10;
            if(orderType == null || orderType.isEmpty())
                orderType = "ASC";
            if(orderColumn == null || orderColumn.isEmpty())
                orderColumn = "sort";
            return service.getCuestionarioSearch(page,size,orderColumn,orderType);
        } catch(CatCuestionariosException ex) {
            throw ex;
        } catch (Exception ex) {
            logger.error("Error al obtener cuestinarios - {}", ex.toString());
            CatCuestionariosException exception = new CatCuestionariosException("Ocurrió un error al buscar los cuestionarios", CatCuestionariosException.LAYER_REST, CatCuestionariosException.ACTION_SELECT);
            exception.addError("Error - " + ex);
            throw exception;
        }
    }

}
