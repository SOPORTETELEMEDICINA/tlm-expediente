package net.amentum.niomedic.expediente.rest;


import net.amentum.common.BaseController;
import net.amentum.niomedic.expediente.service.SaludIndGlucService;
import net.amentum.niomedic.expediente.exception.SaludIndGlucException;
import net.amentum.niomedic.expediente.views.SaludIndGlucView;
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
@RequestMapping("SaludIndGluc")
public class SaludIndGlucRest extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(SaludIndGlucRest.class);
    private SaludIndGlucService SaludIndGlucService;

    @Autowired
    public void setSaludIndGlucService(SaludIndGlucService SaludIndGlucService) {
        this.SaludIndGlucService = SaludIndGlucService;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void createSaludIndGluc(@RequestBody @Valid SaludIndGlucView SaludIndGlucView) throws SaludIndGlucException {
        try {
            logger.info("Guardar nuevo Indicaciones Glucosa: {}", SaludIndGlucView);
            SaludIndGlucService.createSaludIndGluc(SaludIndGlucView);
        } catch (SaludIndGlucException dce) {
            throw dce;
        } catch (Exception ex) {
            SaludIndGlucException dce = new SaludIndGlucException("No fue posible agregar Indicaciones Glucosa", SaludIndGlucException.LAYER_DAO, SaludIndGlucException.ACTION_INSERT);
            dce.addError("Ocurrio un error al agregar Indicaciones Glucosa");
            logger.error("Error al insertar nuevo Indicaciones Glucosa - CODE {} - {}", dce.getExceptionCode(), dce);
            throw dce;
        }
    }

    @RequestMapping(value = "{pacidfk}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void updateSaludIndGluc(@PathVariable() String pacidfk, @RequestBody @Valid SaludIndGlucView SaludIndGlucView) throws SaludIndGlucException {
        try {
            SaludIndGlucView.setPacidfk(pacidfk);
            logger.info("Editar Indicaciones Glucosa: {}", SaludIndGlucView);
            SaludIndGlucService.updateSaludIndGluc(SaludIndGlucView);
        } catch (SaludIndGlucException dce) {
            throw dce;
        } catch (Exception ex) {
            SaludIndGlucException dce = new SaludIndGlucException("No fue posible modificar Indicaciones Glucosa", SaludIndGlucException.LAYER_DAO, SaludIndGlucException.ACTION_UPDATE);
            dce.addError("Ocurrio un error al modificar Indicaciones Glucosa");
            logger.error("Error al modificar Indicaciones Glucosa - CODE {} - {}", dce.getExceptionCode(), dce);
            throw dce;
        }
    }

    @RequestMapping(value = "{pacidfk}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void deleteSaludIndGluc(@PathVariable() String pacidfk) throws SaludIndGlucException {
        logger.info("Eliminar Indicaciones Glucosa: {}", pacidfk);
        SaludIndGlucService.deleteSaludIndGluc(pacidfk);
    }

    @RequestMapping(value = "{pacidfk}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public SaludIndGlucView getDetailsByPacidfk(@PathVariable() String pacidfk) throws SaludIndGlucException {
        try {
            logger.info("Obtener detalles Indicaciones Glucosa por Id: {}", pacidfk);
            return SaludIndGlucService.getDetailsByPacidfk(pacidfk);
        } catch (SaludIndGlucException dce) {
            throw dce;
        } catch (Exception ex) {
            SaludIndGlucException dce = new SaludIndGlucException("No fue posible obtener los detalles Indicaciones Glucosa por Id", SaludIndGlucException.LAYER_DAO, SaludIndGlucException.ACTION_SELECT);
            dce.addError("Ocurrio un error al obtener los detalles Indicaciones Glucosa por Id");
            logger.error("Error al obtener los detalles Indicaciones Glucosa por Id - CODE {} - {} ", dce.getExceptionCode(), dce);
            throw dce;
        }
    }

    @RequestMapping(value = "findAll", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<SaludIndGlucView> findAll() throws SaludIndGlucException {
        return SaludIndGlucService.findAll();
    }

    @RequestMapping(value = "page", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Page<SaludIndGlucView> getSaludIndGlucPage(@RequestParam(required = false) Boolean active, @RequestParam(required = false, defaultValue = "") String name,
                                                        @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size,
                                                        @RequestParam(required = false) String orderColumn, @RequestParam(required = false) String orderType) throws SaludIndGlucException {
        logger.info("- Obtener listado Indicaciones Glucosa paginable: {} - page {} - size: {} - orderColumn: {} - orderType: {} - active: {}", name, page, size, orderColumn, orderType, active);
        if (page == null)
            page = 0;
        if (size == null)
            size = 10;
        if (orderType == null && orderType.isEmpty()) {
            orderType = "asc";
        }
        return SaludIndGlucService.getSaludIndGlucPage(active, name != null ? name : "", page, size, orderColumn, orderType);
    }
}
