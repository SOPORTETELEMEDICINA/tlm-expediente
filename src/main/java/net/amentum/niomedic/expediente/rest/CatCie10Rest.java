package net.amentum.niomedic.expediente.rest;

import lombok.extern.slf4j.Slf4j;
import net.amentum.common.v2.RestBaseController;
import net.amentum.niomedic.expediente.exception.CatCie10Exception;
import net.amentum.niomedic.expediente.service.CatCie10Service;
import net.amentum.niomedic.expediente.views.CatCie10FiltradoView;
import net.amentum.niomedic.expediente.views.CatCie10View;
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
@RequestMapping("cat-cie10")
@Slf4j
public class CatCie10Rest extends RestBaseController {
   private final Logger logger = LoggerFactory.getLogger(CatCie10Rest.class);
   private CatCie10Service catCie10Service;

   @Autowired
   public void setCatCie10Service(CatCie10Service catCie10Service) {
      this.catCie10Service = catCie10Service;
   }

   @RequestMapping(value = "{idCie10}", method = RequestMethod.GET)
   @ResponseStatus(HttpStatus.OK)
   public CatCie10View getDetailsByIdCie10(@PathVariable() Long idCie10) throws CatCie10Exception {
      log.info("===>>>getDetailsByIdCie10() - GET - idCie10: {}", idCie10);
      return catCie10Service.getDetailsByIdCie10(idCie10);
   }

   @RequestMapping(value = "findAll", method = RequestMethod.GET)
   @ResponseStatus(HttpStatus.OK)
   public List<CatCie10FiltradoView> findAll() throws CatCie10Exception {
      logger.info("===>>>findAll() - GET - obteniendo todo el listado de CIE-9");
      return catCie10Service.findAll();
   }

   @RequestMapping(value = "search", method = RequestMethod.GET)
   @ResponseStatus(HttpStatus.OK)
   public Page<CatCie10FiltradoView> getCatCie10Search(@RequestParam(required = false, defaultValue = "") String datosBusqueda,
                                                       @RequestParam(required = false) Boolean activo,
                                                       @RequestParam(required = false, defaultValue = "0") Integer page,
                                                       @RequestParam(required = false, defaultValue = "10") Integer size,
                                                       @RequestParam(required = false, defaultValue = "nombre") String orderColumn,
                                                       @RequestParam(required = false, defaultValue = "asc") String orderType,
                                                       @RequestParam(required = false, defaultValue = "") String sexo,
                                                       @RequestParam(required = false, defaultValue = "0") Integer edad) throws CatCie10Exception {

      log.info("===>>>getCatCie10Search(): datosBusqueda: {} - activo: {} - page: {} - size: {} - orderColumn: {} - orderType: {}",
         datosBusqueda, activo, page, size, orderColumn, orderType);

      if (!orderType.equalsIgnoreCase("asc") && !orderType.equalsIgnoreCase("desc")) {
         orderType = "asc";
      }
      if (page < 0) {
         page = 0;
      }
      if (size < 0) {
         size = 10;
      }

      return catCie10Service.getCatCie10Search(datosBusqueda, activo, page, size, orderColumn, orderType, sexo, edad);
   }

   @RequestMapping(method = RequestMethod.POST)
   @ResponseStatus(HttpStatus.CREATED)
   public CatCie10View createCatCie10(@RequestBody @Valid CatCie10View catCie10View) throws CatCie10Exception {
      log.info("===>>>createCatCie10() - POST - catCieView: {}", catCie10View);
      return catCie10Service.createCatCie10(catCie10View);
   }

   @RequestMapping(value = "{idCie10}", method = RequestMethod.PUT)
   @ResponseStatus(HttpStatus.OK)
   public CatCie10View updateCatCie10(@PathVariable("idCie10") Long idCie10, @RequestBody @Valid CatCie10View catCie10View) throws CatCie10Exception {
      log.info("===>>>updateCatCie10() - PUT - idCie10: {} - catCie10View: {}", idCie10, catCie10View);
      catCie10View.setIdCie10(idCie10);
      return catCie10Service.updateCatCie10(catCie10View);
   }

}

