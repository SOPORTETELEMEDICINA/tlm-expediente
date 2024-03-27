package net.amentum.niomedic.expediente.rest;
import net.amentum.common.BaseController;
import net.amentum.niomedic.expediente.exception.SaludNivPesoException;
import net.amentum.niomedic.expediente.service.SaludNivPesoService;
import net.amentum.niomedic.expediente.views.SaludNivPesoView;
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
@RequestMapping("SaludNivPeso")
public class SaludNivPesoRest extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(SaludNivPesoRest.class);
    private SaludNivPesoService SaludNivPesoService;

    @Autowired
    public void setSaludNivPesoService(SaludNivPesoService SaludNivPesoService) {
        this.SaludNivPesoService = SaludNivPesoService;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void createSaludNivPeso(@RequestBody @Valid SaludNivPesoView SaludNivPesoView) throws SaludNivPesoException {
        try {
            logger.info("Guardar nuevo Niveles Peso: {}", SaludNivPesoView);
            SaludNivPesoService.createSaludNivPeso(SaludNivPesoView);
        } catch (SaludNivPesoException dce) {
            throw dce;
        } catch (Exception ex) {
            SaludNivPesoException dce = new SaludNivPesoException("No fue posible agregar Niveles Peso", SaludNivPesoException.LAYER_DAO, SaludNivPesoException.ACTION_INSERT);
            dce.addError("Ocurrio un error al agregar Niveles Peso");
            logger.error("Error al insertar nuevo Niveles Peso - CODE {} - {}", dce.getExceptionCode(), dce);
            throw dce;
        }
    }

    @RequestMapping(value = "{pacidfk}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void updateSaludNivPeso(@PathVariable() String pacidfk, @RequestBody @Valid SaludNivPesoView SaludNivPesoView) throws SaludNivPesoException {
        try {
            SaludNivPesoView.setPacidfk(pacidfk);
            logger.info("Editar Niveles Peso: {}", SaludNivPesoView);
            SaludNivPesoService.updateSaludNivPeso(SaludNivPesoView);
        } catch (SaludNivPesoException dce) {
            throw dce;
        } catch (Exception ex) {
            SaludNivPesoException dce = new SaludNivPesoException("No fue posible modificar Niveles Peso", SaludNivPesoException.LAYER_DAO, SaludNivPesoException.ACTION_UPDATE);
            dce.addError("Ocurrio un error al modificar Niveles Peso");
            logger.error("Error al modificar Niveles Peso - CODE {} - {}", dce.getExceptionCode(), dce);
            throw dce;
        }
    }

    @RequestMapping(value = "{pacidfk}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void deleteSaludNivPeso(@PathVariable() String pacidfk) throws SaludNivPesoException {
        logger.info("Eliminar Niveles Peso: {}", pacidfk);
        SaludNivPesoService.deleteSaludNivPeso(pacidfk);
    }



    @RequestMapping(value = "findAll", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<SaludNivPesoView> findAll() throws SaludNivPesoException {
        return SaludNivPesoService.findAll();
    }

    @RequestMapping(value = "search", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Page<SaludNivPesoView> getSaludNivPesoSearch(@RequestParam(required = true) String pacidfk,
                                                                    @RequestParam(required = false) Integer page,
                                                                    @RequestParam(required = false) Integer size,
                                                                    @RequestParam(required = false) String orderColumn,
                                                                    @RequestParam(required = false) String orderType) throws SaludNivPesoException {

        logger.info("===>>>getSaludNivPesoSearch(): - pacidfk: {} - page: {} - size: {} - orderColumn: {} - orderType: {}",
                pacidfk,page, size, orderColumn, orderType);

        String upacidfk = null;
        try {
            if (pacidfk != null && !pacidfk.isEmpty())
                upacidfk = pacidfk;
        } catch (IllegalArgumentException iae) {
            logger.error("===>>>pacidfk {} tiene valores incorrectos", pacidfk);
            SaludNivPesoException medPacE = new SaludNivPesoException("Ocurrio un error al obtener SaludNivPeso", SaludNivPesoException.LAYER_REST, SaludNivPesoException.ACTION_VALIDATE);
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
            orderColumn = "idnivelpeso";
        return SaludNivPesoService.getSaludNivPesoSearch(upacidfk, page, size, orderColumn, orderType);
    }

    @RequestMapping(value = "busqueda", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Page<SaludNivPesoView> getSaludNivPesofechaSearch(@RequestParam(required = true) String pacidfk,
                                                             @RequestParam(required = true) int periodo,
                                                             @RequestParam(required = true) String fechaInicio,
                                                             @RequestParam(required = true) String fechaFin,
                                                        @RequestParam(required = false) Integer page,
                                                        @RequestParam(required = false) Integer size,
                                                        @RequestParam(required = false) String orderColumn,
                                                        @RequestParam(required = false) String orderType) throws SaludNivPesoException {

        logger.info("===>>>getSaludNivPesoFechaSearch(): - pacidfk: {} - idTipoEvento: {}  - page: {} - size: {} - orderColumn: {} - orderType: {}",
                pacidfk,periodo,fechaInicio,fechaFin,page, size, orderColumn, orderType);

        String upacidfk = null;
        try {
            if (pacidfk != null && !pacidfk.isEmpty()) {
                upacidfk = pacidfk;
            }
        } catch (IllegalArgumentException iae) {
            logger.error("===>>>pacidfk tiene valores incorrectos", pacidfk);
            SaludNivPesoException medPacE = new SaludNivPesoException("Ocurrio un error al obtener SaludNivPeso", SaludNivPesoException.LAYER_REST, SaludNivPesoException.ACTION_VALIDATE);
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
            orderColumn = "pesofechahora";

        return SaludNivPesoService.getSaludNivPesofechaSearch(upacidfk,periodo, fechaInicio, fechaFin, page, size, orderColumn, orderType);
    }

}
