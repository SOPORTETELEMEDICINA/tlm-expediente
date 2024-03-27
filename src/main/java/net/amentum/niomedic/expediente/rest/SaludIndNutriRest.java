package net.amentum.niomedic.expediente.rest;


import net.amentum.common.BaseController;
import net.amentum.niomedic.expediente.service.SaludIndNutriService;
import net.amentum.niomedic.expediente.exception.SaludIndNutriException;
import net.amentum.niomedic.expediente.views.SaludIndNutriView;
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
@RequestMapping("SaludIndNutri")
public class SaludIndNutriRest extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(SaludIndNutriRest.class);
    private SaludIndNutriService SaludIndNutriService;

    @Autowired
    public void setSaludIndNutriService(SaludIndNutriService SaludIndNutriService) {
        this.SaludIndNutriService = SaludIndNutriService;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void createSaludIndNutri(@RequestBody @Valid SaludIndNutriView SaludIndNutriView) throws SaludIndNutriException {
        try {
            logger.info("Guardar nuevo Indicaciones Nutricion: {}", SaludIndNutriView);
            SaludIndNutriService.createSaludIndNutri(SaludIndNutriView);
        } catch (SaludIndNutriException dce) {
            throw dce;
        } catch (Exception ex) {
            SaludIndNutriException dce = new SaludIndNutriException("No fue posible agregar Indicaciones Nutricion", SaludIndNutriException.LAYER_DAO, SaludIndNutriException.ACTION_INSERT);
            dce.addError("Ocurrio un error al agregar Indicaciones Nutricion");
            logger.error("Error al insertar nuevo Indicaciones Nutricion - CODE {} - {}", dce.getExceptionCode(), dce);
            throw dce;
        }
    }

    @RequestMapping(value = "{pacidfk}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void updateSaludIndNutri(@PathVariable() String pacidfk, @RequestBody @Valid SaludIndNutriView SaludIndNutriView) throws SaludIndNutriException {
        try {
            SaludIndNutriView.setPacidfk(pacidfk);
            logger.info("Editar Indicaciones Nutricion: {}", SaludIndNutriView);
            SaludIndNutriService.updateSaludIndNutri(SaludIndNutriView);
        } catch (SaludIndNutriException dce) {
            throw dce;
        } catch (Exception ex) {
            SaludIndNutriException dce = new SaludIndNutriException("No fue posible modificar Indicaciones Nutricion", SaludIndNutriException.LAYER_DAO, SaludIndNutriException.ACTION_UPDATE);
            dce.addError("Ocurrio un error al modificar Indicaciones Nutricion");
            logger.error("Error al modificar Indicaciones Nutricion - CODE {} - {}", dce.getExceptionCode(), dce);
            throw dce;
        }
    }

    @RequestMapping(value = "{pacidfk}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void deleteSaludIndNutri(@PathVariable() String pacidfk) throws SaludIndNutriException {
        logger.info("Eliminar Indicaciones Nutricion: {}", pacidfk);
        SaludIndNutriService.deleteSaludIndNutri(pacidfk);
    }

    @RequestMapping(value = "{pacidfk}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public SaludIndNutriView getDetailsByPacidfk(@PathVariable() String pacidfk) throws SaludIndNutriException {
        try {
            logger.info("Obtener detalles Indicaciones Nutricion por Id: {}", pacidfk);
            return SaludIndNutriService.getDetailsByPacidfk(pacidfk);
        } catch (SaludIndNutriException dce) {
            throw dce;
        } catch (Exception ex) {
            SaludIndNutriException dce = new SaludIndNutriException("No fue posible obtener los detalles Indicaciones Nutricion por Id", SaludIndNutriException.LAYER_DAO, SaludIndNutriException.ACTION_SELECT);
            dce.addError("Ocurrio un error al obtener los detalles Indicaciones Nutricion por Id");
            logger.error("Error al obtener los detalles Indicaciones Nutricion por Id - CODE {} - {} ", dce.getExceptionCode(), dce);
            throw dce;
        }
    }

    @RequestMapping(value = "findAll", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<SaludIndNutriView> findAll() throws SaludIndNutriException {
        return SaludIndNutriService.findAll();
    }

    @RequestMapping(value = "page", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Page<SaludIndNutriView> getSaludIndNutriPage(@RequestParam(required = false) Boolean active, @RequestParam(required = false, defaultValue = "") String name,
                                                      @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size,
                                                      @RequestParam(required = false) String orderColumn, @RequestParam(required = false) String orderType) throws SaludIndNutriException {
        logger.info("- Obtener listado Indicaciones Nutricion paginable: {} - page {} - size: {} - orderColumn: {} - orderType: {} - active: {}", name, page, size, orderColumn, orderType, active);
        if (page == null)
            page = 0;
        if (size == null)
            size = 10;
        if (orderType == null && orderType.isEmpty()) {
            orderType = "asc";
        }
        return SaludIndNutriService.getSaludIndNutriPage(active, name != null ? name : "", page, size, orderColumn, orderType);
    }
}
