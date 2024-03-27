package net.amentum.niomedic.expediente.rest;

import lombok.extern.slf4j.Slf4j;
import net.amentum.common.BaseController;
import net.amentum.niomedic.expediente.views.TomaDiaView;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("toma-dia")
@Slf4j
public class TomaDiaRest extends BaseController {


   @RequestMapping(value = "{idUsuario}", method = RequestMethod.GET)
   @ResponseStatus(HttpStatus.OK)
   public List<TomaDiaView> getTomasDia(@PathVariable() Long idUsuario) {

      ArrayList<TomaDiaView> tomaDiaViews = new ArrayList<>();
      Date hoy = new Date();
      long ONE_MINUTE_IN_MILLIS = 60000;//millisecs
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(hoy);
      Long temporal = calendar.getTimeInMillis();
      Integer diaHoy = calendar.get(Calendar.DAY_OF_WEEK);

      for (long i = 1; i <= 3; i++) {
         TomaDiaView td = new TomaDiaView();
         td.setIdTomaDia(i);
         td.setNombreMedicamento("Bactrim");
         td.setDosis(1);
         td.setViaAdministracion("Oral");
         td.setUnidad("pastilla");
         td.setIdUsuario(idUsuario);
         td.setFrecuencia(8);
         td.setPeriodo("15 dias");
//         Date sumandoHoras = new Date(temporal + (480 * ONE_MINUTE_IN_MILLIS));
         Date sumandoHoras = variarFecha(hoy, Calendar.HOUR, 8);
         td.setHoraToma(sumandoHoras);
         calendar.setTime(td.getHoraToma());
         temporal = calendar.getTimeInMillis();
         hoy = new Date(temporal);
         if (calendar.get(Calendar.DAY_OF_WEEK) == diaHoy) {
            tomaDiaViews.add(td);
         }
      }

      hoy = new Date();
      ONE_MINUTE_IN_MILLIS = 60000;//millisecs
      calendar = Calendar.getInstance();
      calendar.setTime(hoy);
      temporal = calendar.getTimeInMillis();
      diaHoy = calendar.get(Calendar.DAY_OF_WEEK);

      for (long i = 4; i <= 5; i++) {
         TomaDiaView td = new TomaDiaView();
         td.setIdTomaDia(i);
         td.setNombreMedicamento("Bedoyecta");
         td.setDosis(1);
         td.setViaAdministracion("Intramuscular");
         td.setUnidad("Jeringa");
         td.setIdUsuario(idUsuario);
         td.setFrecuencia(12);
         td.setPeriodo("7 dias");
//         Date sumandoHoras = new Date(temporal + (480 * ONE_MINUTE_IN_MILLIS));
         Date sumandoHoras = variarFecha(hoy, Calendar.HOUR, 12);
         td.setHoraToma(sumandoHoras);
         calendar.setTime(td.getHoraToma());
         temporal = calendar.getTimeInMillis();
         hoy = new Date(temporal);
         if (calendar.get(Calendar.DAY_OF_WEEK) == diaHoy) {
            tomaDiaViews.add(td);
         }
      }

      hoy = new Date();
      ONE_MINUTE_IN_MILLIS = 60000;//millisecs
      calendar = Calendar.getInstance();
      calendar.setTime(hoy);
      temporal = calendar.getTimeInMillis();
      diaHoy = calendar.get(Calendar.DAY_OF_WEEK);

      for (long i = 6; i <= 9; i++) {
         TomaDiaView td = new TomaDiaView();
         td.setIdTomaDia(i);
         td.setNombreMedicamento("Naproxeno");
         td.setDosis(1);
         td.setViaAdministracion("Oral");
         td.setUnidad("Capsula");
         td.setIdUsuario(idUsuario);
         td.setFrecuencia(12);
         td.setPeriodo("7 dias");
//         Date sumandoHoras = new Date(temporal + (480 * ONE_MINUTE_IN_MILLIS));
         Date sumandoHoras = variarFecha(hoy, Calendar.HOUR, 6);
         td.setHoraToma(sumandoHoras);
         calendar.setTime(td.getHoraToma());
         temporal = calendar.getTimeInMillis();
         hoy = new Date(temporal);
         if (calendar.get(Calendar.DAY_OF_WEEK) == diaHoy) {
            tomaDiaViews.add(td);
         }
      }

      hoy = new Date();
      ONE_MINUTE_IN_MILLIS = 60000;//millisecs
      calendar = Calendar.getInstance();
      calendar.setTime(hoy);
      temporal = calendar.getTimeInMillis();
      diaHoy = calendar.get(Calendar.DAY_OF_WEEK);

      for (long i = 10; i <= 21; i++) {
         TomaDiaView td = new TomaDiaView();
         td.setIdTomaDia(i);
         td.setNombreMedicamento("Sensibit XP");
         td.setDosis(1);
         td.setViaAdministracion("Oral");
         td.setUnidad("Cucharadita");
         td.setIdUsuario(idUsuario);
         td.setFrecuencia(2);
         td.setPeriodo("7 dias");
//         Date sumandoHoras = new Date(temporal + (480 * ONE_MINUTE_IN_MILLIS));
         Date sumandoHoras = variarFecha(hoy, Calendar.HOUR, 2);
         td.setHoraToma(sumandoHoras);
         calendar.setTime(td.getHoraToma());
         temporal = calendar.getTimeInMillis();
         hoy = new Date(temporal);
         if (calendar.get(Calendar.DAY_OF_WEEK) == diaHoy) {
            tomaDiaViews.add(td);
         }
      }
      return tomaDiaViews;
   }

   public Date variarFecha(Date fecha, int campo, int valor) {
      if (valor == 0) return fecha;
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(fecha);
      calendar.add(campo, valor);
      return calendar.getTime();
   }
}
