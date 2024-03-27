package net.amentum.niomedic.expediente.rest;

import net.amentum.common.BaseController;
import net.amentum.niomedic.expediente.views.CatEstadoConsultaView;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("cat-estado-consulta")
public class CatEstadoConsultaRest extends BaseController {

   @RequestMapping(method = RequestMethod.GET)
   @ResponseStatus(HttpStatus.OK)
   public List<CatEstadoConsultaView> getAll(){
      return Arrays.asList(
         new CatEstadoConsultaView(1L,"Inicial"),
         new CatEstadoConsultaView(2L,"Sub secuente"),
         new CatEstadoConsultaView(3L,"Interconsulta"),
         new CatEstadoConsultaView(4L,"Referencia")
      );
   }

}

