package net.amentum.niomedic.expediente.rest;



import net.amentum.common.BaseController;
import net.amentum.niomedic.expediente.service.SaludIndCovidService;
import net.amentum.niomedic.expediente.exception.SaludIndCovidException;
import net.amentum.niomedic.expediente.views.SaludIndCovidView;
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
@RequestMapping("SaludIndCovid")
public class SaludIndCovidRest extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(SaludIndCovidRest.class);
    private SaludIndCovidService SaludIndCovidService;

    @Autowired
    public void setSaludIndCovidService(SaludIndCovidService SaludIndCovidService) {
        this.SaludIndCovidService = SaludIndCovidService;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void createSaludIndCovid(@RequestBody @Valid SaludIndCovidView SaludIndCovidView) throws SaludIndCovidException {
        try {
            logger.info("Guardar nuevo Indicaciones Covid: {}", SaludIndCovidView);
            SaludIndCovidService.createSaludIndCovid(SaludIndCovidView);
        } catch (SaludIndCovidException dce) {
            throw dce;
        } catch (Exception ex) {
            SaludIndCovidException dce = new SaludIndCovidException("No fue posible agregar Indicaciones Covid", SaludIndCovidException.LAYER_DAO, SaludIndCovidException.ACTION_INSERT);
            dce.addError("Ocurrio un error al agregar Indicaciones Covid");
            logger.error("Error al insertar nuevo Indicaciones Covid - CODE {} - {}", dce.getExceptionCode(), dce);
            throw dce;
        }
    }

    @RequestMapping(value = "{pacidfk}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void updateSaludIndCovid(@PathVariable() String pacidfk, @RequestBody @Valid SaludIndCovidView SaludIndCovidView) throws SaludIndCovidException {
        try {
            SaludIndCovidView.setPacidfk(pacidfk);
            logger.info("Editar Indicaciones Covid: {}", SaludIndCovidView);
            SaludIndCovidService.updateSaludIndCovid(SaludIndCovidView);
        } catch (SaludIndCovidException dce) {
            throw dce;
        } catch (Exception ex) {
            SaludIndCovidException dce = new SaludIndCovidException("No fue posible modificar Indicaciones Covid", SaludIndCovidException.LAYER_DAO, SaludIndCovidException.ACTION_UPDATE);
            dce.addError("Ocurrio un error al modificar Indicaciones Covid");
            logger.error("Error al modificar Indicaciones Covid - CODE {} - {}", dce.getExceptionCode(), dce);
            throw dce;
        }
    }

    @RequestMapping(value = "{pacidfk}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void deleteSaludIndCovid(@PathVariable() String pacidfk) throws SaludIndCovidException {
        logger.info("Eliminar Indicaciones Covid: {}", pacidfk);
        SaludIndCovidService.deleteSaludIndCovid(pacidfk);
    }

    @RequestMapping(value = "{pacidfk}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public SaludIndCovidView getDetailsByPacidfk(@PathVariable() String pacidfk) throws SaludIndCovidException {
        try {
            logger.info("Obtener detalles Indicaciones Covid por Id: {}", pacidfk);
            return SaludIndCovidService.getDetailsByPacidfk(pacidfk);
        } catch (SaludIndCovidException dce) {
            throw dce;
        } catch (Exception ex) {
            SaludIndCovidException dce = new SaludIndCovidException("No fue posible obtener los detalles Indicaciones Covid por Id", SaludIndCovidException.LAYER_DAO, SaludIndCovidException.ACTION_SELECT);
            dce.addError("Ocurrio un error al obtener los detalles Indicaciones Covid por Id");
            logger.error("Error al obtener los detalles Indicaciones Covid por Id - CODE {} - {} ", dce.getExceptionCode(), dce);
            throw dce;
        }
    }

    @RequestMapping(value = "findAll", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<SaludIndCovidView> findAll() throws SaludIndCovidException {
        return SaludIndCovidService.findAll();
    }

    @RequestMapping(value = "page", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Page<SaludIndCovidView> getSaludIndCovidPage(@RequestParam(required = false) Boolean active, @RequestParam(required = false, defaultValue = "") String name,
                                                        @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size,
                                                        @RequestParam(required = false) String orderColumn, @RequestParam(required = false) String orderType) throws SaludIndCovidException {
        logger.info("- Obtener listado Indicaciones Covid paginable: {} - page {} - size: {} - orderColumn: {} - orderType: {} - active: {}", name, page, size, orderColumn, orderType, active);
        if (page == null)
            page = 0;
        if (size == null)
            size = 10;
        if (orderType == null && orderType.isEmpty()) {
            orderType = "asc";
        }
        return SaludIndCovidService.getSaludIndCovidPage(active, name != null ? name : "", page, size, orderColumn, orderType);
    }
}
