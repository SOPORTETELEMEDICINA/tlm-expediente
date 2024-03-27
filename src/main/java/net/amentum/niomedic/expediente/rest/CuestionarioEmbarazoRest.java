package net.amentum.niomedic.expediente.rest;

import net.amentum.common.BaseController;
import net.amentum.niomedic.expediente.exception.CuestionarioEmbarazoException;
import net.amentum.niomedic.expediente.service.CuestionarioEmbarazoService;
import net.amentum.niomedic.expediente.views.CuestionarioEmbarazoView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("Cuestionario")
public class CuestionarioEmbarazoRest extends BaseController {

    final Logger logger = LoggerFactory.getLogger(CuestionarioEmbarazoRest.class);
    CuestionarioEmbarazoService service;

    @Autowired
    public void setService(CuestionarioEmbarazoService service) {
        this.service = service;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void createCuestionario(@RequestBody @Valid CuestionarioEmbarazoView view) throws CuestionarioEmbarazoException {
        try {
            logger.info("Insertando nuevo cuestionario - {}", view);
            if(view.getPacidfk() == null || view.getPacidfk().isEmpty()) {
                logger.error("pacidfk es vacío/null");
                CuestionarioEmbarazoException exception = new CuestionarioEmbarazoException("Ocurrió un error al obtener cuestionarios", CuestionarioEmbarazoException.LAYER_REST, CuestionarioEmbarazoException.ACTION_VALIDATE);
                exception.addError("El valor pacidfk es vacio/null - " + view.getPacidfk());
                throw exception;
            }
            service.createCuestionarioEmbarazo(view);
        } catch (CuestionarioEmbarazoException excep) {
            throw excep;
        } catch (Exception ex) {
            CuestionarioEmbarazoException exception = new CuestionarioEmbarazoException("No fue posible agregar cuestionario", CuestionarioEmbarazoException.LAYER_DAO, CuestionarioEmbarazoException.ACTION_INSERT);
            exception.addError("Ocurrió un error al insertar un cuestionario - " + ex);
            logger.error("Error al insertar un nuevo cuestionario - " + ex);
            throw exception;
        }
    }

    @RequestMapping(value = "{idCuestionario}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void updateCuestionarioEmbarazo(@PathVariable() String idCuestionario, CuestionarioEmbarazoView view) throws CuestionarioEmbarazoException {
        throw new CuestionarioEmbarazoException("Metodo no implementado", CuestionarioEmbarazoException.LAYER_SERVICE, CuestionarioEmbarazoException.ACTION_UPDATE);
    }

    @RequestMapping(value = "{idCuestionario}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void deleteCuestionarioEmbarazo(@PathVariable() String idCuestionario) throws CuestionarioEmbarazoException {
        throw new CuestionarioEmbarazoException("Metodo no implementado", CuestionarioEmbarazoException.LAYER_SERVICE, CuestionarioEmbarazoException.ACTION_DELETE);
    }

    @RequestMapping(value = "findAll", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<CuestionarioEmbarazoView> findAll() throws CuestionarioEmbarazoException {
        return service.findAll();
    }

    @RequestMapping(value = "find", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Page<CuestionarioEmbarazoView> getCuestionarioSearch(@RequestParam() String pacidfk,
                                                                @RequestParam(required = false) Integer page,
                                                                @RequestParam(required = false) Integer size,
                                                                @RequestParam(required = false) String orderColumn,
                                                                @RequestParam(required = false) String orderType) throws CuestionarioEmbarazoException {
        try {
            logger.info("getCuestionarioSearch(): - pacidfk: {} - page: {} - size: {} - orderColumn: {} - orderType: {}",
                    pacidfk,page, size, orderColumn, orderType);
            if(pacidfk == null || pacidfk.isEmpty()) {
                logger.error("pacidfk es vacío/null");
                CuestionarioEmbarazoException exception = new CuestionarioEmbarazoException("Ocurrió un error al obtener cuestionarios", CuestionarioEmbarazoException.LAYER_REST, CuestionarioEmbarazoException.ACTION_VALIDATE);
                exception.addError("El valor pacidfk tiene valores incorrectos: " + pacidfk);
                throw exception;
            }
            if(page == null)
                page = 0;
            if(size == null)
                size = 10;
            if(orderType == null || orderType.isEmpty())
                orderType = "DESC";
            if (orderColumn == null || orderColumn.isEmpty())
                orderColumn = "fecha";
            return service.getCuestionarioEmbarazoSearch(pacidfk, page, size, orderColumn, orderType);
        } catch (CuestionarioEmbarazoException excep) {
            throw excep;
        } catch (Exception ex) {
            CuestionarioEmbarazoException exception = new CuestionarioEmbarazoException("No fue posible obtener cuestionario", CuestionarioEmbarazoException.LAYER_DAO, CuestionarioEmbarazoException.ACTION_INSERT);
            exception.addError("Ocurrió un error al obtener cuestionarios - " + ex);
            logger.error("Error al obtener cuestionarios - " + ex);
            throw exception;
        }
    }

    @RequestMapping(value = "findByDate", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Page<CuestionarioEmbarazoView> getCuestionarioFechaSearch(@RequestParam() String pacidfk,
                                                                     @RequestParam() String fechaInicio,
                                                                     @RequestParam() String fechaFin,
                                                                     @RequestParam(required = false) Integer page,
                                                                     @RequestParam(required = false) Integer size,
                                                                     @RequestParam(required = false) String orderColumn,
                                                                     @RequestParam(required = false) String orderType) throws CuestionarioEmbarazoException {
        try {
            logger.info("getCuestionarioFechaSearch(): - pacidfk: {} - page: {} - size: {} - orderColumn: {} - orderType: {}",
                    pacidfk,page, size, orderColumn, orderType);
            if(pacidfk == null || pacidfk.isEmpty()) {
                logger.error("pacidfk es vacío/null");
                CuestionarioEmbarazoException exception = new CuestionarioEmbarazoException("Ocurrió un error al obtener cuestionarios", CuestionarioEmbarazoException.LAYER_REST, CuestionarioEmbarazoException.ACTION_VALIDATE);
                exception.addError("El valor pacidfk tiene valores incorrectos: " + pacidfk);
                throw exception;
            } else if(fechaInicio == null || fechaInicio.isEmpty()) {
                logger.error("fechaInicio es vacío/null");
                CuestionarioEmbarazoException exception = new CuestionarioEmbarazoException("Ocurrió un error al obtener cuestionarios", CuestionarioEmbarazoException.LAYER_REST, CuestionarioEmbarazoException.ACTION_VALIDATE);
                exception.addError("El valor fechaInicio tiene valores incorrectos: " + pacidfk);
                throw exception;
            }
            if(fechaFin == null || fechaFin.isEmpty()) {
                logger.error("fechaFin es vacío/null");
                CuestionarioEmbarazoException exception = new CuestionarioEmbarazoException("Ocurrió un error al obtener cuestionarios", CuestionarioEmbarazoException.LAYER_REST, CuestionarioEmbarazoException.ACTION_VALIDATE);
                exception.addError("El valor fechaFin tiene valores incorrectos: " + pacidfk);
                throw exception;
            }
            if(page == null)
                page = 0;
            if(size == null)
                size = 10;
            if(orderType == null || orderType.isEmpty())
                orderType = "DESC";
            if (orderColumn == null || orderColumn.isEmpty())
                orderColumn = "fecha";
            return service.getCuestionarioEmbarazoFechaSearch(pacidfk, fechaInicio, fechaFin, page, size, orderColumn, orderType);
        } catch (CuestionarioEmbarazoException excep) {
            throw excep;
        } catch (Exception ex) {
            CuestionarioEmbarazoException exception = new CuestionarioEmbarazoException("No fue posible obtener cuestionario", CuestionarioEmbarazoException.LAYER_DAO, CuestionarioEmbarazoException.ACTION_INSERT);
            exception.addError("Ocurrió un error al obtener cuestionarios - " + ex);
            logger.error("Error al obtener cuestionarios - " + ex);
            throw exception;
        }
    }
}
