package net.amentum.niomedic.expediente.rest;

import net.amentum.common.BaseController;
import net.amentum.niomedic.expediente.views.CatTipoConsultaView;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("cat-tipo-consulta")
public class CatTipoConsultaRest extends BaseController {

   @RequestMapping(method = RequestMethod.GET)
   @ResponseStatus(HttpStatus.OK)
   public List<CatTipoConsultaView> getAll(){
      return Arrays.asList(
         new CatTipoConsultaView(1L,"Presencial"),
         new CatTipoConsultaView(2L,"Video consulta")
      );
   }

}

