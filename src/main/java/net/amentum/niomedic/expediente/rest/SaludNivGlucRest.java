package net.amentum.niomedic.expediente.rest;

import net.amentum.common.BaseController;
import net.amentum.niomedic.expediente.exception.SaludNivGlucException;
import net.amentum.niomedic.expediente.service.SaludNivGlucService;
import net.amentum.niomedic.expediente.exception.SaludNivGlucException;
import net.amentum.niomedic.expediente.views.SaludNivGlucView;
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
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("SaludNivGluc")
public class SaludNivGlucRest extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(SaludNivGlucRest.class);
    private SaludNivGlucService SaludNivGlucService;

    @Autowired
    public void setSaludNivGlucService(SaludNivGlucService SaludNivGlucService) {
        this.SaludNivGlucService = SaludNivGlucService;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void createSaludNivGluc(@RequestBody @Valid SaludNivGlucView SaludNivGlucView) throws SaludNivGlucException {
        try {
            logger.info("Guardar nuevo Niveles Glucosa: {}", SaludNivGlucView);
            SaludNivGlucService.createSaludNivGluc(SaludNivGlucView);
        } catch (SaludNivGlucException dce) {
            throw dce;
        } catch (Exception ex) {
            SaludNivGlucException dce = new SaludNivGlucException("No fue posible agregar Niveles Glucosa", SaludNivGlucException.LAYER_DAO, SaludNivGlucException.ACTION_INSERT);
            dce.addError("Ocurrio un error al agregar Niveles Glucosa");
            logger.error("Error al insertar nuevo Niveles Glucosa - CODE {} - {}", dce.getExceptionCode(), dce);
            throw dce;
        }
    }

    @RequestMapping(value = "{pacidfk}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void updateSaludNivGluc(@PathVariable() String pacidfk, @RequestBody @Valid SaludNivGlucView SaludNivGlucView) throws SaludNivGlucException {
        try {
            SaludNivGlucView.setPacidfk(pacidfk);
            logger.info("Editar Niveles Glucosa: {}", SaludNivGlucView);
            SaludNivGlucService.updateSaludNivGluc(SaludNivGlucView);
        } catch (SaludNivGlucException dce) {
            throw dce;
        } catch (Exception ex) {
            SaludNivGlucException dce = new SaludNivGlucException("No fue posible modificar Niveles Glucosa", SaludNivGlucException.LAYER_DAO, SaludNivGlucException.ACTION_UPDATE);
            dce.addError("Ocurrio un error al modificar Niveles Glucosa");
            logger.error("Error al modificar Niveles Glucosa - CODE {} - {}", dce.getExceptionCode(), dce);
            throw dce;
        }
    }

    @RequestMapping(value = "{pacidfk}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void deleteSaludNivGluc(@PathVariable() String pacidfk) throws SaludNivGlucException {
        logger.info("Eliminar Niveles Glucosa: {}", pacidfk);
        SaludNivGlucService.deleteSaludNivGluc(pacidfk);
    }

   
    @RequestMapping(value = "findAll", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<SaludNivGlucView> findAll() throws SaludNivGlucException {
        return SaludNivGlucService.findAll();
    }

    @RequestMapping(value = "search", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Page<SaludNivGlucView> getSaludNivGlucSearch(@RequestParam(required = true) String pacidfk,
                                                          @RequestParam(required = false) Integer page,
                                                          @RequestParam(required = false) Integer size,
                                                          @RequestParam(required = false) String orderColumn,
                                                          @RequestParam(required = false) String orderType) throws SaludNivGlucException {

        logger.info("===>>>getSaludNivGlucSearch(): - pacidfk: {} - page: {} - size: {} - orderColumn: {} - orderType: {}",
                pacidfk,page, size, orderColumn, orderType);
        String upacidfk = null;
        try {
            if (pacidfk != null && !pacidfk.isEmpty())
                upacidfk = pacidfk;
        } catch (IllegalArgumentException iae) {
            logger.error("===>>>pacidfk {} tiene valores incorrectos", pacidfk);
            SaludNivGlucException medPacE = new SaludNivGlucException("Ocurrio un error al obtener SaludNivGluc", SaludNivGlucException.LAYER_REST, SaludNivGlucException.ACTION_VALIDATE);
            medPacE.addError("pacidfk tiene valores incorrectos: " + pacidfk);
            throw medPacE;
        }
        if (page == null)
            page = 0;
        if (size == null)
            size = 120;
        if (orderType == null || orderType.isEmpty())
            orderType = "DESC";
        if (orderColumn == null || orderColumn.isEmpty())
            orderColumn = "idnivelglucosa";
        return SaludNivGlucService.getSaludNivGlucSearch(upacidfk, page, size, orderColumn, orderType);
    }

    @RequestMapping(value = "busqueda", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Page<SaludNivGlucView> getSaludNivGlucPeriodo(@RequestParam() String pacidfk,
                                                         @RequestParam() int periodo,
                                                         @RequestParam() String fechaInicio,
                                                         @RequestParam() String fechaFin,
                                                         @RequestParam(required = false) Integer page,
                                                         @RequestParam(required = false) Integer size,
                                                         @RequestParam(required = false) String orderColumn,
                                                         @RequestParam(required = false) String orderType) throws SaludNivGlucException {
        logger.info("===>>>getSaludNivCovidFechaSearch(): - pacidfk: {} - periodo: {}  - fechaInicio: {} - fechaFin: {} - page: {} - size: {} - orderColumn: {} - orderType: {}",
                pacidfk,periodo,fechaInicio,fechaFin,page, size, orderColumn, orderType);

        if(pacidfk == null || pacidfk.isEmpty()) {
            logger.error("pacidfk sin valor");
            SaludNivGlucException ex = new SaludNivGlucException("Error al obtener valores SaludNivGluc", SaludNivGlucException.LAYER_REST, SaludNivGlucException.ACTION_VALIDATE);
            ex.addError("Pacidfk vacío");
            throw  ex;
        }
        if(fechaInicio == null || fechaInicio.isEmpty()) {
            logger.error("fechaInicio sin valor");
            SaludNivGlucException ex = new SaludNivGlucException("Error al obtener valores SaludNivGluc", SaludNivGlucException.LAYER_REST, SaludNivGlucException.ACTION_VALIDATE);
            ex.addError("fechaInicio vacío");
            throw  ex;
        }
        if(fechaFin == null || fechaFin.isEmpty()) {
            logger.error("fechaFin sin valor");
            SaludNivGlucException ex = new SaludNivGlucException("Error al obtener valores SaludNivGluc", SaludNivGlucException.LAYER_REST, SaludNivGlucException.ACTION_VALIDATE);
            ex.addError("fechaFin vacío");
            throw  ex;
        }
        if (page == null)
            page = 0;
        if (size == null)
            size = 120;
        if (orderType == null || orderType.isEmpty())
            orderType = "asc";
        if (orderColumn == null || orderColumn.isEmpty())
            orderColumn = "idnivelglucosa";
        return SaludNivGlucService.getSaludNivGlucfechaSearch(pacidfk,periodo,fechaInicio,fechaFin,page,size,orderColumn,orderType);
    }
}
