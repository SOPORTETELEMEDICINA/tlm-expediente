package net.amentum.niomedic.expediente.rest;

import net.amentum.common.BaseController;
import net.amentum.niomedic.expediente.service.DatosClinicosService;
import net.amentum.niomedic.expediente.exception.DatosClinicosException;
import net.amentum.niomedic.expediente.views.DatosClinicosView;
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
@RequestMapping("datos-clinicos")
public class DatosClinicosRest extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(DatosClinicosRest.class);
    private DatosClinicosService datosClinicosService;

    @Autowired
    public void setDatosClinicosService(DatosClinicosService datosClinicosService) {
        this.datosClinicosService = datosClinicosService;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void createDatosClinicos(@RequestBody @Valid DatosClinicosView datosClinicosView) throws DatosClinicosException {
        try {
            logger.info("Guardar nuevo Datos Clinicos: {}", datosClinicosView);
            datosClinicosService.createDatosClinicos(datosClinicosView);
        } catch (DatosClinicosException dce) {
            throw dce;
        } catch (Exception ex) {
            DatosClinicosException dce = new DatosClinicosException("No fue posible agregar Datos Clinicos", DatosClinicosException.LAYER_DAO, DatosClinicosException.ACTION_INSERT);
            dce.addError("Ocurrio un error al agregar Datos Clinicos");
            logger.error("Error al insertar nuevo Datos Clinicos - CODE {} - {}", dce.getExceptionCode(), dce);
            throw dce;
        }
    }

    @RequestMapping(value = "{idPaciente}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void updateDatosClinicos(@PathVariable() String idPaciente, @RequestBody @Valid DatosClinicosView datosClinicosView) throws DatosClinicosException {
        try {
            datosClinicosView.setIdPaciente(idPaciente);
            logger.info("Editar Datos Clinicos: {}", datosClinicosView);
            datosClinicosService.updateDatosClinicos(datosClinicosView);
        } catch (DatosClinicosException dce) {
            throw dce;
        } catch (Exception ex) {
            DatosClinicosException dce = new DatosClinicosException("No fue posible modificar Datos Clinicos", DatosClinicosException.LAYER_DAO, DatosClinicosException.ACTION_UPDATE);
            dce.addError("Ocurrio un error al modificar Datos Clinicos");
            logger.error("Error al modificar Datos Clinicos - CODE {} - {}", dce.getExceptionCode(), dce);
            throw dce;
        }
    }

    @RequestMapping(value = "{idPaciente}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void deleteDatosClinicos(@PathVariable() String idPaciente) throws DatosClinicosException {
        logger.info("Eliminar Datos Clinicos: {}", idPaciente);
        datosClinicosService.deleteDatosClinicos(idPaciente);
    }

    @RequestMapping(value = "{idPaciente}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public DatosClinicosView getDetailsByIdDatosClinicos(@PathVariable() String idPaciente) throws DatosClinicosException {
        try {
            logger.info("Obtener detalles Datos Clinicos por Id: {}", idPaciente);
            return datosClinicosService.getDetailsByIdDatosClinicos(idPaciente);
        } catch (DatosClinicosException dce) {
            throw dce;
        } catch (Exception ex) {
            DatosClinicosException dce = new DatosClinicosException("No fue posible obtener los detalles Datos Clinicos por Id", DatosClinicosException.LAYER_DAO, DatosClinicosException.ACTION_SELECT);
            dce.addError("Ocurrio un error al obtener los detalles Datos Clinicos por Id");
            logger.error("Error al obtener los detalles Datos Clinicos por Id - CODE {} - {} ", dce.getExceptionCode(), dce);
            throw dce;
        }
    }

    @RequestMapping(value = "findAll", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<DatosClinicosView> findAll(@RequestParam(required = false) Boolean active) throws DatosClinicosException {
        return datosClinicosService.findAll(active);
    }

    @RequestMapping(value = "page", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Page<DatosClinicosView> getDatosClinicosPage(@RequestParam(required = false) Boolean active, @RequestParam(required = false, defaultValue = "") String name,
                                                        @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size,
                                                        @RequestParam(required = false) String orderColumn, @RequestParam(required = false) String orderType) throws DatosClinicosException {
        logger.info("- Obtener listado Datos Clinicos paginable: {} - page {} - size: {} - orderColumn: {} - orderType: {} - active: {}", name, page, size, orderColumn, orderType, active);
        if (page == null)
            page = 0;
        if (size == null)
            size = 10;
        if (orderType == null && orderType.isEmpty()) {
            orderType = "asc";
        }
        return datosClinicosService.getDatosClinicosPage(active, name != null ? name : "", page, size, orderColumn, orderType);
    }
}
