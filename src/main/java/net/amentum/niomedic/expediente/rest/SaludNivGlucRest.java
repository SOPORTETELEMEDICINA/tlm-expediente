package net.amentum.niomedic.expediente.rest;

import net.amentum.common.BaseController;
import net.amentum.niomedic.expediente.exception.SaludNivGlucException;
import net.amentum.niomedic.expediente.service.SaludNivGlucService;
import net.amentum.niomedic.expediente.views.SaludNivGlucView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("SaludNivGluc")
public class SaludNivGlucRest extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(SaludNivGlucRest.class);
    private SaludNivGlucService saludNivGlucService;

    @Autowired
    public void setSaludNivGlucService(SaludNivGlucService saludNivGlucService) {
        this.saludNivGlucService = saludNivGlucService;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void createSaludNivGluc(@RequestBody @Valid SaludNivGlucView saludNivGlucView) throws SaludNivGlucException {
        try {
            logger.info("Guardar nuevo Niveles Glucosa: {}", saludNivGlucView);
            saludNivGlucService.createSaludNivGluc(saludNivGlucView);
        } catch (SaludNivGlucException ex) {
            throw ex;
        } catch (Exception ex) {
            logger.error("Error al insertar nuevo Niveles Glucosa", ex);
            SaludNivGlucException exc = new SaludNivGlucException(
                    "No fue posible agregar Niveles Glucosa",
                    SaludNivGlucException.LAYER_DAO,
                    SaludNivGlucException.ACTION_INSERT
            );
            exc.addError("Ocurrió un error al agregar Niveles Glucosa: " + ex.getMessage());
            throw exc;
        }
    }

    @RequestMapping(value = "{pacidfk}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void updateSaludNivGluc(@PathVariable String pacidfk, @RequestBody @Valid SaludNivGlucView saludNivGlucView) throws SaludNivGlucException {
        try {
            saludNivGlucView.setPacidfk(pacidfk);
            logger.info("Editar Niveles Glucosa: {}", saludNivGlucView);
            saludNivGlucService.updateSaludNivGluc(saludNivGlucView);
        } catch (SaludNivGlucException ex) {
            throw ex;
        } catch (Exception ex) {
            logger.error("Error al modificar Niveles Glucosa", ex);
            SaludNivGlucException exc = new SaludNivGlucException(
                    "No fue posible modificar Niveles Glucosa",
                    SaludNivGlucException.LAYER_DAO,
                    SaludNivGlucException.ACTION_UPDATE
            );
            exc.addError("Ocurrió un error al modificar Niveles Glucosa: " + ex.getMessage());
            throw exc;
        }
    }

    @RequestMapping(value = "{pacidfk}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void deleteSaludNivGluc(@PathVariable String pacidfk) throws SaludNivGlucException {
        logger.info("Eliminar Niveles Glucosa: {}", pacidfk);
        saludNivGlucService.deleteSaludNivGluc(pacidfk);
    }

    @RequestMapping(value = "findAll", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<SaludNivGlucView> findAll() throws SaludNivGlucException {
        return saludNivGlucService.findAll();
    }

    @RequestMapping(value = "search", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Page<SaludNivGlucView> getSaludNivGlucSearch(
            @RequestParam(required = true) String pacidfk,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String orderColumn,
            @RequestParam(required = false) String orderType) throws SaludNivGlucException {

        logger.info("getSaludNivGlucSearch(): pacidfk={}, page={}, size={}, orderColumn={}, orderType={}",
                pacidfk, page, size, orderColumn, orderType);

        if (pacidfk == null || pacidfk.isEmpty()) {
            SaludNivGlucException exc = new SaludNivGlucException(
                    "Ocurrió un error al obtener SaludNivGluc",
                    SaludNivGlucException.LAYER_REST,
                    SaludNivGlucException.ACTION_VALIDATE
            );
            exc.addError("pacidfk es requerido");
            throw exc;
        }

        if (page == null) page = 0;
        if (size == null) size = 120;
        if (orderType == null || orderType.isEmpty()) orderType = "DESC";
        if (orderColumn == null || orderColumn.isEmpty()) orderColumn = "idnivelglucosa";

        return saludNivGlucService.getSaludNivGlucSearch(pacidfk, page, size, orderColumn, orderType);
    }

    @RequestMapping(value = "busqueda", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Page<SaludNivGlucView> getSaludNivGlucPeriodo(
            @RequestParam String pacidfk,
            @RequestParam int periodo,
            @RequestParam String fechaInicio,
            @RequestParam String fechaFin,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String orderColumn,
            @RequestParam(required = false) String orderType) throws SaludNivGlucException {

        logger.info("getSaludNivGlucPeriodo(): pacidfk={}, periodo={}, fechaInicio={}, fechaFin={}",
                pacidfk, periodo, fechaInicio, fechaFin);

        if (pacidfk == null || pacidfk.isEmpty()) {
            SaludNivGlucException exc = new SaludNivGlucException(
                    "Error al obtener valores SaludNivGluc",
                    SaludNivGlucException.LAYER_REST,
                    SaludNivGlucException.ACTION_VALIDATE
            );
            exc.addError("Pacidfk vacío");
            throw exc;
        }

        if (fechaInicio == null || fechaInicio.isEmpty()) {
            SaludNivGlucException exc = new SaludNivGlucException(
                    "Error al obtener valores SaludNivGluc",
                    SaludNivGlucException.LAYER_REST,
                    SaludNivGlucException.ACTION_VALIDATE
            );
            exc.addError("fechaInicio vacío");
            throw exc;
        }

        if (fechaFin == null || fechaFin.isEmpty()) {
            SaludNivGlucException exc = new SaludNivGlucException(
                    "Error al obtener valores SaludNivGluc",
                    SaludNivGlucException.LAYER_REST,
                    SaludNivGlucException.ACTION_VALIDATE
            );
            exc.addError("fechaFin vacío");
            throw exc;
        }

        if (page == null) page = 0;
        if (size == null) size = 120;
        if (orderType == null || orderType.isEmpty()) orderType = "asc";
        if (orderColumn == null || orderColumn.isEmpty()) orderColumn = "idnivelglucosa";

        return saludNivGlucService.getSaludNivGlucfechaSearch(pacidfk, periodo, fechaInicio, fechaFin, page, size, orderColumn, orderType);
    }
}