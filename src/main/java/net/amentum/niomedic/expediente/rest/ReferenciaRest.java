package net.amentum.niomedic.expediente.rest;

import lombok.extern.slf4j.Slf4j;
import net.amentum.common.BaseController;
import net.amentum.niomedic.expediente.exception.ReferenciaException;
import net.amentum.niomedic.expediente.service.referencia_vs_referencia.ReferenciaService;
import net.amentum.niomedic.expediente.views.referencia_vs_referencia.ReferenciaView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("referencia")
public class ReferenciaRest extends BaseController {

    @Autowired
    ReferenciaService service;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> createReferencia(@RequestBody @Valid ReferenciaView view) throws ReferenciaException {
        try {
            log.debug("Guardar nuevo registro de referencia");
            return service.createReferencia(view);
        } catch(ReferenciaException exc) {
            throw exc;
        } catch (Exception exception) {
            ReferenciaException referenciaException = new ReferenciaException("No fue posible agregar registro en Referencias", ReferenciaException.LAYER_REST, ReferenciaException.ACTION_INSERT);
            referenciaException.addError(exception.getCause().toString());
            exception.printStackTrace();
            throw exception;
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Page<ReferenciaView> getReferencias(@RequestParam Integer searchColumn,
                                               @RequestParam Integer type,
                                               @RequestParam String value,
                                               @RequestParam(required = false) Integer page,
                                               @RequestParam(required = false) Integer size) throws ReferenciaException {
        try {
            log.info("Buscar referencia: {} - {} - {} - page {} - size {}", searchColumn, type, value, page, size);
            if(searchColumn == null) {
                ReferenciaException referenciaException = new ReferenciaException("No fue posible buscar referencias", ReferenciaException.LAYER_REST, ReferenciaException.ACTION_VALIDATE);
                referenciaException.addError("searchColumn vacía/null");
                throw referenciaException;
            }
            if(type == null) {
                ReferenciaException referenciaException = new ReferenciaException("No fue posible buscar referencias", ReferenciaException.LAYER_REST, ReferenciaException.ACTION_VALIDATE);
                referenciaException.addError("type vacío/null");
                throw referenciaException;
            }
            if(value.isEmpty()) {
                ReferenciaException referenciaException = new ReferenciaException("No fue posible buscar referencias", ReferenciaException.LAYER_REST, ReferenciaException.ACTION_VALIDATE);
                referenciaException.addError("value vacío/null");
                throw referenciaException;
            }
            if(page == null)
                page = 0;
            if(size == null)
                size = 10;
            return service.getReferencia(searchColumn, type, value, page, size);
        } catch(ReferenciaException exc) {
            throw exc;
        } catch (Exception exception) {
            ReferenciaException referenciaException = new ReferenciaException("No fue posible buscar referencias", ReferenciaException.LAYER_REST, ReferenciaException.ACTION_SELECT);
            referenciaException.addError(exception.getCause().toString());
            exception.printStackTrace();
            throw exception;
        }
    }
}
