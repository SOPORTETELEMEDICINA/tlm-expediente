package net.amentum.niomedic.expediente.rest;

import net.amentum.common.BaseController;
import net.amentum.niomedic.expediente.exception.CatCuestionarioPacienteException;
import net.amentum.niomedic.expediente.service.CatCuestionarioPacienteService;
import net.amentum.niomedic.expediente.views.CatCuestionarioPacienteView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("cat_cuestionario_paciente")
public class CatCuestionarioPacienteRest extends BaseController {

    final Logger logger = LoggerFactory.getLogger(CatCuestionarioRest.class);

    @Autowired
    CatCuestionarioPacienteService service;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void createCuestionarioPaciente(@RequestBody @Valid CatCuestionarioPacienteView view) throws CatCuestionarioPacienteException {
        try {
            logger.info("Insertando nuevo cuestionario-paciente: {}", view);
            if(view.getIdPaciente() == null || view.getIdPaciente().isEmpty()) {
                logger.error("id_paciente vacío");
                CatCuestionarioPacienteException catEx = new CatCuestionarioPacienteException("Ocurrió un error al agregar la relación", CatCuestionarioPacienteException.LAYER_REST, CatCuestionarioPacienteException.ACTION_VALIDATE);
                catEx.addError("id_paciente vacío");
                throw catEx;
            }
            if(view.getStatus() == null || view.getIdCatCuestionario() < 0) {
                logger.error("Campo status vacío");
                CatCuestionarioPacienteException catEx = new CatCuestionarioPacienteException("Ocurrió un error al agregar la relación", CatCuestionarioPacienteException.LAYER_REST, CatCuestionarioPacienteException.ACTION_VALIDATE);
                catEx.addError("Campo status vacío");
                throw catEx;
            }
            if(view.getIdCatCuestionario() == null || view.getIdCatCuestionario() < 0) {
                logger.error("Campo id_cat_Cuestionario vacío");
                CatCuestionarioPacienteException catEx = new CatCuestionarioPacienteException("Ocurrió un error al agregar la relación", CatCuestionarioPacienteException.LAYER_REST, CatCuestionarioPacienteException.ACTION_VALIDATE);
                catEx.addError("Campo id_cat_Cuestionario vacío");
                throw catEx;
            }
            service.createCuestionarioPaciente(view);
        } catch (CatCuestionarioPacienteException ex) {
            throw ex;
        } catch(Exception ex) {
            logger.error("Error al agregar la relación : ", ex);
            CatCuestionarioPacienteException catEx = new CatCuestionarioPacienteException("Ocurrió un error al agregar la relación", CatCuestionarioPacienteException.LAYER_REST, CatCuestionarioPacienteException.ACTION_INSERT);
            catEx.addError("Error al agregar la relación: " + ex);
            throw catEx;
        }
    }
}
