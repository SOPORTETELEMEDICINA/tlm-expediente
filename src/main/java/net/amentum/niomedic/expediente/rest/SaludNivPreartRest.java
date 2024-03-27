package net.amentum.niomedic.expediente.rest;


import net.amentum.common.BaseController;
import net.amentum.niomedic.expediente.exception.SaludNivGlucException;
import net.amentum.niomedic.expediente.exception.SaludNivPreartException;
import net.amentum.niomedic.expediente.service.SaludNivPreartService;
import net.amentum.niomedic.expediente.views.SaludNivGlucView;
import net.amentum.niomedic.expediente.views.SaludNivPreartView;
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
@RequestMapping("SaludNivPreart")
public class SaludNivPreartRest extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(SaludNivPreartRest.class);
    private SaludNivPreartService SaludNivPreartService;

    @Autowired
    public void setSaludNivPreartService(SaludNivPreartService SaludNivPreartService) {
        this.SaludNivPreartService = SaludNivPreartService;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void createSaludNivPreart(@RequestBody @Valid SaludNivPreartView SaludNivPreartView) throws SaludNivPreartException {
        try {
            logger.info("Guardar nuevo Niveles Presion Arterial: {}", SaludNivPreartView);
            SaludNivPreartService.createSaludNivPreart(SaludNivPreartView);
        } catch (SaludNivPreartException dce) {
            throw dce;
        } catch (Exception ex) {
            SaludNivPreartException dce = new SaludNivPreartException("No fue posible agregar Niveles Presion Arterial", SaludNivPreartException.LAYER_DAO, SaludNivPreartException.ACTION_INSERT);
            dce.addError("Ocurrio un error al agregar Niveles Presion Arterial");
            logger.error("Error al insertar nuevo Niveles Presion Arterial - CODE {} - {}", dce.getExceptionCode(), dce);
            throw dce;
        }
    }

    @RequestMapping(value = "{pacidfk}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void updateSaludNivPreart(@PathVariable() String pacidfk, @RequestBody @Valid SaludNivPreartView SaludNivPreartView) throws SaludNivPreartException {
        try {
            SaludNivPreartView.setPacidfk(pacidfk);
            logger.info("Editar Niveles Presion Arterial: {}", SaludNivPreartView);
            SaludNivPreartService.updateSaludNivPreart(SaludNivPreartView);
        } catch (SaludNivPreartException dce) {
            throw dce;
        } catch (Exception ex) {
            SaludNivPreartException dce = new SaludNivPreartException("No fue posible modificar Niveles Presion Arterial", SaludNivPreartException.LAYER_DAO, SaludNivPreartException.ACTION_UPDATE);
            dce.addError("Ocurrio un error al modificar Niveles Presion Arterial");
            logger.error("Error al modificar Niveles Presion Arterial - CODE {} - {}", dce.getExceptionCode(), dce);
            throw dce;
        }
    }

    @RequestMapping(value = "{pacidfk}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void deleteSaludNivPreart(@PathVariable() String pacidfk) throws SaludNivPreartException {
        logger.info("Eliminar Niveles Presion Arterial: {}", pacidfk);
        SaludNivPreartService.deleteSaludNivPreart(pacidfk);
    }


    @RequestMapping(value = "findAll", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<SaludNivPreartView> findAll() throws SaludNivPreartException {
        logger.info("Obteniendo todos los datos de presión arterial");
        return SaludNivPreartService.findAll();
    }

    @RequestMapping(value = "search", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Page<SaludNivPreartView> getSaludNivPreartSearch(@RequestParam(required = true) String pacidfk,
                                                        @RequestParam(required = false) Integer page,
                                                        @RequestParam(required = false) Integer size,
                                                        @RequestParam(required = false) String orderColumn,
                                                        @RequestParam(required = false) String orderType) throws SaludNivPreartException {

        logger.info("===>>>getSaludNivPreartSearch(): - pacidfk: {} - page: {} - size: {} - orderColumn: {} - orderType: {}",
                pacidfk,page, size, orderColumn, orderType);

        String upacidfk = null;
        try {
            if (pacidfk != null && !pacidfk.isEmpty())
                upacidfk = pacidfk;
        } catch (IllegalArgumentException iae) {
            logger.error("===>>>pacidfk {} tiene valores incorrectos", pacidfk);
            SaludNivPreartException medPacE = new SaludNivPreartException("Ocurrio un error al obtener SaludNivPreart", SaludNivPreartException.LAYER_REST, SaludNivPreartException.ACTION_VALIDATE);
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
            orderColumn = "idnivelpa";
        return SaludNivPreartService.getSaludNivPreartSearch(upacidfk, page, size, orderColumn, orderType);
    }

    @RequestMapping(value = "busqueda", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Page<SaludNivPreartView> getSaludNivPreartPeriodo(@RequestParam() String pacidfk,
                                                         @RequestParam() int periodo,
                                                         @RequestParam() String fechaInicio,
                                                         @RequestParam() String fechaFin,
                                                         @RequestParam(required = false) Integer page,
                                                         @RequestParam(required = false) Integer size,
                                                         @RequestParam(required = false) String orderColumn,
                                                         @RequestParam(required = false) String orderType) throws SaludNivPreartException {
        logger.info("===>>>getSaludNivPreartPeriodo(): - pacidfk: {} - periodo: {}  - fechaInicio: {} - fechaFin: {} - page: {} - size: {} - orderColumn: {} - orderType: {}",
                pacidfk,periodo,fechaInicio,fechaFin,page, size, orderColumn, orderType);

        if(pacidfk == null || pacidfk.isEmpty()) {
            logger.error("pacidfk sin valor");
            SaludNivPreartException ex = new SaludNivPreartException("Error al obtener valores SaludNivGluc", SaludNivGlucException.LAYER_REST, SaludNivGlucException.ACTION_VALIDATE);
            ex.addError("Pacidfk vacío");
            throw  ex;
        }
        if(fechaInicio == null || fechaInicio.isEmpty()) {
            logger.error("fechaInicio sin valor");
            SaludNivPreartException ex = new SaludNivPreartException("Error al obtener valores SaludNivGluc", SaludNivGlucException.LAYER_REST, SaludNivGlucException.ACTION_VALIDATE);
            ex.addError("fechaInicio vacío");
            throw  ex;
        }
        if(fechaFin == null || fechaFin.isEmpty()) {
            logger.error("fechaFin sin valor");
            SaludNivPreartException ex = new SaludNivPreartException("Error al obtener valores SaludNivGluc", SaludNivGlucException.LAYER_REST, SaludNivGlucException.ACTION_VALIDATE);
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
            orderColumn = "idnivelpa";
        return SaludNivPreartService.getSaludNivPreartfechaSearch(pacidfk,periodo,fechaInicio,fechaFin,page,size,orderColumn,orderType);
    }
}
