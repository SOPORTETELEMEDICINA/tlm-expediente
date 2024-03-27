package net.amentum.niomedic.expediente.rest;

import lombok.extern.slf4j.Slf4j;
import net.amentum.common.BaseController;
import net.amentum.niomedic.expediente.exception.ReferenciaException;
import net.amentum.niomedic.expediente.service.referencia_vs_referencia.ContraReferenciaService;
import net.amentum.niomedic.expediente.views.referencia_vs_referencia.ContraReferenciaView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("contra-referencia")
public class ContraReferenciaRest extends BaseController {

    @Autowired
    ContraReferenciaService service;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void createContraReferencia(@RequestBody @Valid ContraReferenciaView view) throws ReferenciaException {
        try {
            log.debug("Guardar nuevo registro de contra-referencia");
            service.createContraReferencia(view);
        } catch(ReferenciaException exc) {
            throw exc;
        } catch (Exception exception) {
            ReferenciaException referenciaException = new ReferenciaException("No fue posible agregar registro en contra-referencias", ReferenciaException.LAYER_REST, ReferenciaException.ACTION_INSERT);
            referenciaException.addError(exception.getCause().toString());
            exception.printStackTrace();
            throw exception;
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Page<ContraReferenciaView> getContraReferencias(@RequestParam Integer searchColumn,
                                               @RequestParam String value,
                                               @RequestParam(required = false) Integer page,
                                               @RequestParam(required = false) Integer size) throws ReferenciaException {
        try {
            log.info("Buscar contra-referencia: {} - {} - page {} - size {}", searchColumn, value, page, size);
            if (searchColumn == null) {
                ReferenciaException referenciaException = new ReferenciaException("No fue posible buscar contra-referencias", ReferenciaException.LAYER_REST, ReferenciaException.ACTION_VALIDATE);
                referenciaException.addError("searchColumn vacía/null");
                throw referenciaException;
            }
            if (value.isEmpty()) {
                ReferenciaException referenciaException = new ReferenciaException("No fue posible buscar contra-referencias", ReferenciaException.LAYER_REST, ReferenciaException.ACTION_VALIDATE);
                referenciaException.addError("value vacío/null");
                throw referenciaException;
            }
            if (page == null)
                page = 0;
            if (size == null)
                size = 10;
            return service.getContraReferencia(searchColumn, value, page, size);
        } catch (ReferenciaException exc) {
            throw exc;
        } catch (Exception exception) {
            ReferenciaException referenciaException = new ReferenciaException("No fue posible buscar contra-referencias", ReferenciaException.LAYER_REST, ReferenciaException.ACTION_SELECT);
            referenciaException.addError(exception.getCause().toString());
            exception.printStackTrace();
            throw exception;
        }
    }

}
