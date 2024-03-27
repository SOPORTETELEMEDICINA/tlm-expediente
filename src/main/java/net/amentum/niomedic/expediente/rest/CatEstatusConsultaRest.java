package net.amentum.niomedic.expediente.rest;

import net.amentum.common.BaseController;
import net.amentum.niomedic.expediente.views.CatEstatusConsultaView;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("cat-estatus-consulta")
public class CatEstatusConsultaRest extends BaseController {

   @RequestMapping(method = RequestMethod.GET)
   @ResponseStatus(HttpStatus.OK)
   public List<CatEstatusConsultaView> getAll(){
      return Arrays.asList(
         new CatEstatusConsultaView(1L,"No Iniciada"),
         new CatEstatusConsultaView(2L,"Consulta en Turno"),
         new CatEstatusConsultaView(3L,"Cancelada"),
         new CatEstatusConsultaView(4L,"Finalizada")
      );
   }

}

