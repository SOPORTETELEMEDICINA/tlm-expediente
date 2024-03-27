package net.amentum.niomedic.expediente.rest;

import net.amentum.common.BaseController;
import net.amentum.niomedic.expediente.exception.SaludNivCovidException;
import net.amentum.niomedic.expediente.service.SaludNivCovidService;
import net.amentum.niomedic.expediente.views.SaludNivCovidView;
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
@RequestMapping("SaludNivCovid")
public class SaludNivCovidRest extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(SaludNivCovidRest.class);
    private SaludNivCovidService SaludNivCovidService;

    @Autowired
    public void setSaludNivCovidService(SaludNivCovidService SaludNivCovidService) {
        this.SaludNivCovidService = SaludNivCovidService;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void createSaludNivCovid(@RequestBody @Valid SaludNivCovidView SaludNivCovidView) throws SaludNivCovidException {
        try {
            logger.info("Guardar nuevo Niveles Covid: {}", SaludNivCovidView);
            SaludNivCovidService.createSaludNivCovid(SaludNivCovidView);
        } catch (SaludNivCovidException dce) {
            throw dce;
        } catch (Exception ex) {
            SaludNivCovidException dce = new SaludNivCovidException("No fue posible agregar Niveles Covid", SaludNivCovidException.LAYER_DAO, SaludNivCovidException.ACTION_INSERT);
            dce.addError("Ocurrio un error al agregar Niveles Covid");
            logger.error("Error al insertar nuevo Niveles Covid - CODE {} - {}", dce.getExceptionCode(), dce);
            throw dce;
        }
    }

    @RequestMapping(value = "{pacidfk}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void updateSaludNivCovid(@PathVariable() String pacidfk, @RequestBody @Valid SaludNivCovidView SaludNivCovidView) throws SaludNivCovidException {
        try {
            SaludNivCovidView.setPacidfk(pacidfk);
            logger.info("Editar Niveles Covid: {}", SaludNivCovidView);
            SaludNivCovidService.updateSaludNivCovid(SaludNivCovidView);
        } catch (SaludNivCovidException dce) {
            throw dce;
        } catch (Exception ex) {
            SaludNivCovidException dce = new SaludNivCovidException("No fue posible modificar Niveles Covid", SaludNivCovidException.LAYER_DAO, SaludNivCovidException.ACTION_UPDATE);
            dce.addError("Ocurrio un error al modificar Niveles Covid");
            logger.error("Error al modificar Niveles Covid - CODE {} - {}", dce.getExceptionCode(), dce);
            throw dce;
        }
    }

    @RequestMapping(value = "{pacidfk}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void deleteSaludNivCovid(@PathVariable() String pacidfk) throws SaludNivCovidException {
        logger.info("Eliminar Niveles Covid: {}", pacidfk);
        SaludNivCovidService.deleteSaludNivCovid(pacidfk);
    }


    
    @RequestMapping(value = "findAll", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<SaludNivCovidView> findAll() throws SaludNivCovidException {
        return SaludNivCovidService.findAll();
    }


    @RequestMapping(value = "search", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Page<SaludNivCovidView> getSaludNivCovidSearch(@RequestParam(required = true) String pacidfk,
                                                        @RequestParam(required = false) Integer page,
                                                        @RequestParam(required = false) Integer size,
                                                        @RequestParam(required = false) String orderColumn,
                                                        @RequestParam(required = false) String orderType) throws SaludNivCovidException {

        logger.info("===>>>getSaludNivCovidSearch(): - pacidfk: {} - page: {} - size: {} - orderColumn: {} - orderType: {}",
                pacidfk,page, size, orderColumn, orderType);
        String upacidfk = null;
        try {
            if (pacidfk != null && !pacidfk.isEmpty())
                upacidfk = pacidfk;
        } catch (IllegalArgumentException iae) {
            logger.error("===>>>pacidfk tiene valores incorrectos", pacidfk);
            SaludNivCovidException medPacE = new SaludNivCovidException("Ocurrio un error al obtener SaludNivCovid", SaludNivCovidException.LAYER_REST, SaludNivCovidException.ACTION_VALIDATE);
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
            orderColumn = "idnivelcovid";
        return SaludNivCovidService.getSaludNivCovidSearch(upacidfk, page, size, orderColumn, orderType);
    }

    @RequestMapping(value = "busqueda", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Page<SaludNivCovidView> getSaludNivCovidfechaSearch(@RequestParam(required = true) String pacidfk,
                                                             @RequestParam(required = true) int periodo,
                                                             @RequestParam(required = true) String fechaInicio,
                                                             @RequestParam(required = true) String fechaFin,
                                                             @RequestParam(required = false) Integer page,
                                                             @RequestParam(required = false) Integer size,
                                                             @RequestParam(required = false) String orderColumn,
                                                             @RequestParam(required = false) String orderType) throws SaludNivCovidException {

        logger.info("===>>>getSaludNivCovidFechaSearch(): - pacidfk: {} - idTipoEvento: {}  - page: {} - size: {} - orderColumn: {} - orderType: {}",
                pacidfk,periodo,fechaInicio,fechaFin,page, size, orderColumn, orderType);

        String upacidfk = null;
        try {
            if (pacidfk != null && !pacidfk.isEmpty()) {
                upacidfk = pacidfk;
            }
        } catch (IllegalArgumentException iae) {
            logger.error("===>>>pacidfk tiene valores incorrectos", pacidfk);
            SaludNivCovidException medPacE = new SaludNivCovidException("Ocurrio un error al obtener SaludNivCovid", SaludNivCovidException.LAYER_REST, SaludNivCovidException.ACTION_VALIDATE);
            medPacE.addError("pacidfk tiene valores incorrectos: " + pacidfk);
            throw medPacE;
        }

        if (page == null)
            page = 0;
        if (size == null)
            size = 120;
        if (orderType == null || orderType.isEmpty())
            orderType = "asc";
        if (orderColumn == null || orderColumn.isEmpty())
            orderColumn = "covidfechahora";

        return SaludNivCovidService.getSaludNivCovidfechaSearch(upacidfk,periodo, fechaInicio, fechaFin, page, size, orderColumn, orderType);
    }



    @RequestMapping(value = "periodo", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Page<SaludNivCovidView> getSaludNivCovidperiodoSearch(@RequestParam(required = true) String pacidfk,
                                                               @RequestParam(required = true) int periodo,
                                                               @RequestParam(required = true) int PeriodoF,
                                                               @RequestParam(required = false) Integer page,
                                                               @RequestParam(required = false) Integer size,
                                                               @RequestParam(required = false) String orderColumn,
                                                               @RequestParam(required = false) String orderType) throws SaludNivCovidException {

        logger.info("===>>>getSaludNivCovidFechaSearch(): - pacidfk: {} - periodo: {} PeriodoF: {}  - page: {} - size: {} - orderColumn: {} - orderType: {}",
                pacidfk,periodo,PeriodoF,page, size, orderColumn, orderType);

        String upacidfk = null;
        try {
            if (pacidfk != null && !pacidfk.isEmpty()) {
                upacidfk = pacidfk;
            }
        } catch (IllegalArgumentException iae) {
            logger.error("===>>>pacidfk tiene valores incorrectos", pacidfk);
            SaludNivCovidException medPacE = new SaludNivCovidException("Ocurrio un error al obtener SaludNivCovid", SaludNivCovidException.LAYER_REST, SaludNivCovidException.ACTION_VALIDATE);
            medPacE.addError("pacidfk tiene valores incorrectos: " + pacidfk);
            throw medPacE;
        }

        if (page == null)
            page = 0;
        if (size == null)
            size = 10;
        if (orderType == null || orderType.isEmpty())
            orderType = "asc";
        if (orderColumn == null || orderColumn.isEmpty())
            orderColumn = "pacidfk";

        return SaludNivCovidService.getSaludNivCovidperiodoSearch(upacidfk,periodo, PeriodoF, page, size, orderColumn, orderType);
    }





}
