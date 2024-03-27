package net.amentum.niomedic.expediente.rest;


import net.amentum.common.BaseController;
import net.amentum.niomedic.expediente.service.SaludIndPaService;
import net.amentum.niomedic.expediente.exception.SaludIndPaException;
import net.amentum.niomedic.expediente.views.SaludIndPaView;
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
@RequestMapping("SaludIndPa")
public class SaludIndPaRest extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(SaludIndPaRest.class);
    private SaludIndPaService SaludIndPaService;

    @Autowired
    public void setSaludIndPaService(SaludIndPaService SaludIndPaService) {
        this.SaludIndPaService = SaludIndPaService;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void createSaludIndPa(@RequestBody @Valid SaludIndPaView SaludIndPaView) throws SaludIndPaException {
        try {
            logger.info("Guardar nuevo Indicaciones Presion Arterial: {}", SaludIndPaView);
            SaludIndPaService.createSaludIndPa(SaludIndPaView);
        } catch (SaludIndPaException dce) {
            throw dce;
        } catch (Exception ex) {
            SaludIndPaException dce = new SaludIndPaException("No fue posible agregar Indicaciones Presion Arterial", SaludIndPaException.LAYER_DAO, SaludIndPaException.ACTION_INSERT);
            dce.addError("Ocurrio un error al agregar Indicaciones Presion Arterial");
            logger.error("Error al insertar nuevo Indicaciones Presion Arterial - CODE {} - {}", dce.getExceptionCode(), dce);
            throw dce;
        }
    }

    @RequestMapping(value = "{pacidfk}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void updateSaludIndPa(@PathVariable() String pacidfk, @RequestBody @Valid SaludIndPaView SaludIndPaView) throws SaludIndPaException {
        try {
            SaludIndPaView.setPacidfk(pacidfk);
            logger.info("Editar Indicaciones Presion Arterial: {}", SaludIndPaView);
            SaludIndPaService.updateSaludIndPa(SaludIndPaView);
        } catch (SaludIndPaException dce) {
            throw dce;
        } catch (Exception ex) {
            SaludIndPaException dce = new SaludIndPaException("No fue posible modificar Indicaciones Presion Arterial", SaludIndPaException.LAYER_DAO, SaludIndPaException.ACTION_UPDATE);
            dce.addError("Ocurrio un error al modificar Indicaciones Presion Arterial");
            logger.error("Error al modificar Indicaciones Presion Arterial - CODE {} - {}", dce.getExceptionCode(), dce);
            throw dce;
        }
    }

    @RequestMapping(value = "{pacidfk}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void deleteSaludIndPa(@PathVariable() String pacidfk) throws SaludIndPaException {
        logger.info("Eliminar Indicaciones Presion Arterial: {}", pacidfk);
        SaludIndPaService.deleteSaludIndPa(pacidfk);
    }

    @RequestMapping(value = "{pacidfk}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public SaludIndPaView getDetailsByPacidfk(@PathVariable() String pacidfk) throws SaludIndPaException {
        try {
            logger.info("Obtener detalles Indicaciones Presion Arterial por Id: {}", pacidfk);
            return SaludIndPaService.getDetailsByPacidfk(pacidfk);
        } catch (SaludIndPaException dce) {
            throw dce;
        } catch (Exception ex) {
            SaludIndPaException dce = new SaludIndPaException("No fue posible obtener los detalles Indicaciones Presion Arterial por Id", SaludIndPaException.LAYER_DAO, SaludIndPaException.ACTION_SELECT);
            dce.addError("Ocurrio un error al obtener los detalles Indicaciones Presion Arterial por Id");
            logger.error("Error al obtener los detalles Indicaciones Presion Arterial por Id - CODE {} - {} ", dce.getExceptionCode(), dce);
            throw dce;
        }
    }

    @RequestMapping(value = "findAll", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<SaludIndPaView> findAll() throws SaludIndPaException {
        return SaludIndPaService.findAll();
    }

    @RequestMapping(value = "page", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Page<SaludIndPaView> getSaludIndPaPage(@RequestParam(required = false) Boolean active, @RequestParam(required = false, defaultValue = "") String name,
                                                        @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size,
                                                        @RequestParam(required = false) String orderColumn, @RequestParam(required = false) String orderType) throws SaludIndPaException {
        logger.info("- Obtener listado Indicaciones Presion Arterial paginable: {} - page {} - size: {} - orderColumn: {} - orderType: {} - active: {}", name, page, size, orderColumn, orderType, active);
        if (page == null)
            page = 0;
        if (size == null)
            size = 10;
        if (orderType == null && orderType.isEmpty()) {
            orderType = "asc";
        }
        return SaludIndPaService.getSaludIndPaPage(active, name != null ? name : "", page, size, orderColumn, orderType);
    }
}
