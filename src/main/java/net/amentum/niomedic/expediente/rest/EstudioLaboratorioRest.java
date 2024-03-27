package net.amentum.niomedic.expediente.rest;

import net.amentum.common.BaseController;
import net.amentum.niomedic.expediente.exception.EstudioLaboratorioException;
import net.amentum.niomedic.expediente.service.EstudioLaboratorioService;
import net.amentum.niomedic.expediente.views.EstudioLaboratorioListView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("estudio-laboratorio")
public class EstudioLaboratorioRest extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(EstudioLaboratorioRest.class);
    private EstudioLaboratorioService estudioLaboratorioService;

    @Autowired
    public void setEstudioLaboratorioService(EstudioLaboratorioService estudioLaboratorioService) {
        this.estudioLaboratorioService = estudioLaboratorioService;
    }

    @RequestMapping(value = "page", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Page<EstudioLaboratorioListView> getEstudioLaboratorioPage(@RequestParam(required = false) Boolean active, @RequestParam(required = false, defaultValue = "") String name,
                                                      @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size,
                                                      @RequestParam(required = false) String orderColumn, @RequestParam(required = false) String orderType) throws EstudioLaboratorioException {
        logger.info("- Obtener listado de estudio laboratorio paginable: {} - page {} - size: {} - orderColumn: {} - orderType: {} - active: {}", name, page, size, orderColumn, orderType, active);
        if (page == null)
            page = 0;
        if (size == null)
            size = 10;
        if (orderType == null && orderType.isEmpty()) {
            orderType = "asc";
        }
        return estudioLaboratorioService.getEstudioLaboratorioPage(active, name != null ? name : "", page, size, orderColumn, orderType);
    }


   @RequestMapping(value = "por-padecimiento/{idPadecimiento}", method = RequestMethod.GET)
   @ResponseStatus(HttpStatus.OK)
   public List<List<Map<String, Object>>> getEstudioLaboratorioPorPadecimiento(@PathVariable("idPadecimiento") Long idPadecimiento) throws EstudioLaboratorioException {
       return estudioLaboratorioService.getEstudioLaboratorioPorPadecimiento(idPadecimiento);
   }



}
