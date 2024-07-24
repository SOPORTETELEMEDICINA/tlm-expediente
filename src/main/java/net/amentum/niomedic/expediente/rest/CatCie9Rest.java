package net.amentum.niomedic.expediente.rest;

import lombok.extern.slf4j.Slf4j;
import net.amentum.common.v2.RestBaseController;
import net.amentum.niomedic.expediente.exception.CatCie9Exception;
import net.amentum.niomedic.expediente.service.CatCie9Service;
import net.amentum.niomedic.expediente.views.CatCie9FiltradoView;
import net.amentum.niomedic.expediente.views.CatCie9View;
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

//import net.amentum.niomedic.expediente.exception.CatCie9Exception;
//import net.amentum.niomedic.expediente.views.CatCie9View;

@RestController
@RequestMapping("cat-cie9")
@Slf4j
//public class CatCie9Rest extends BaseController {
public class CatCie9Rest extends RestBaseController {
   private final Logger logger = LoggerFactory.getLogger(CatCie9Rest.class);
   private CatCie9Service catCie9Service;

   @Autowired
   public void setCatCie9Service(CatCie9Service catCie9Service) {
      this.catCie9Service = catCie9Service;
   }

   @RequestMapping(value = "{idCie9}", method = RequestMethod.GET)
   @ResponseStatus(HttpStatus.OK)
   public CatCie9View getDetailsByIdCie9(@PathVariable() Long idCie9) throws CatCie9Exception {
      log.info("===>>>getDetailsByIdCie9() - GET - idCie9: {}", idCie9);
      return catCie9Service.getDetailsByIdCie9(idCie9);
   }

   @RequestMapping(value = "findAll", method = RequestMethod.GET)
   @ResponseStatus(HttpStatus.OK)
   public List<CatCie9FiltradoView> findAll() throws CatCie9Exception {
      logger.info("===>>>findAll() - GET - obteniendo todo el listado de CIE-9");
      return catCie9Service.findAll();
   }

   @RequestMapping(value = "search", method = RequestMethod.GET)
   @ResponseStatus(HttpStatus.OK)
   public Page<CatCie9FiltradoView> getCatCie9Search(@RequestParam(required = false, defaultValue = "") String datosBusqueda,
                                                     @RequestParam(required = false) Boolean activo,
                                                     @RequestParam(required = false, defaultValue = "0") Integer page,
                                                     @RequestParam(required = false, defaultValue = "10") Integer size,
                                                     @RequestParam(required = false, defaultValue = "proNombre") String orderColumn,
                                                     @RequestParam(required = false, defaultValue = "asc") String orderType,
                                                     @RequestParam(required = false, defaultValue = "", value = "sexo") String sexo,
                                                     @RequestParam(required = false, defaultValue = "0", value = "edad") Integer edad) throws CatCie9Exception {

      log.info("===>>>getCatCie9Search(): datosBusqueda: {} - activo: {} - page: {} - size: {} - orderColumn: {} - orderType: {}",
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

      return catCie9Service.getCatCie9Search(datosBusqueda, activo, page, size, orderColumn, orderType, sexo, edad);
   }

   @RequestMapping(method = RequestMethod.POST)
   @ResponseStatus(HttpStatus.CREATED)
   public CatCie9View createCatCie9(@RequestBody @Valid CatCie9View catCie9View) throws CatCie9Exception {
      log.info("===>>>createCatCie9() - POST - catCieView: {}", catCie9View);
      return catCie9Service.createCatCie9(catCie9View);
   }

   @RequestMapping(value = "{idCie9}", method = RequestMethod.PUT)
   @ResponseStatus(HttpStatus.OK)
   public CatCie9View updateCatCie9(@PathVariable("idCie9") Long idCie9, @RequestBody @Valid CatCie9View catCie9View) throws CatCie9Exception {
      log.info("===>>>updateCatCie9() - PUT - idCie9: {} - catCie9View: {}", idCie9, catCie9View);
      catCie9View.setIdCie9(idCie9);
      return catCie9Service.updateCatCie9(catCie9View);
   }

}

