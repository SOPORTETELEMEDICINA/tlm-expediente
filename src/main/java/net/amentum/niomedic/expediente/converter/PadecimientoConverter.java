package net.amentum.niomedic.expediente.converter;

import net.amentum.niomedic.expediente.exception.ExceptionServiceCode;
import net.amentum.niomedic.expediente.exception.PadecimientoException;
import net.amentum.niomedic.expediente.model.CatCie10;
import net.amentum.niomedic.expediente.model.CatCie9;
import net.amentum.niomedic.expediente.model.Consulta;
import net.amentum.niomedic.expediente.model.Documentos;
import net.amentum.niomedic.expediente.model.EstudioLaboratorio;
import net.amentum.niomedic.expediente.model.ImagenLaboratorio;
import net.amentum.niomedic.expediente.model.Padecimiento;
import net.amentum.niomedic.expediente.persistence.CatCie10Repository;
import net.amentum.niomedic.expediente.persistence.ConsultaRepository;
import net.amentum.niomedic.expediente.views.ConsultaView;
import net.amentum.niomedic.expediente.views.DocumentosView;
import net.amentum.niomedic.expediente.views.EstudioLaboratorioView;
import net.amentum.niomedic.expediente.views.ImagenLaboratorioView;
import net.amentum.niomedic.expediente.views.PadecimientoView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class PadecimientoConverter {
   private final Logger logger = LoggerFactory.getLogger(PadecimientoConverter.class);
   private ConsultaRepository consultaRepository;
   @Autowired
   private CatCie10Repository catCie10Repository;
   @Autowired
   public void setConsultaRepository(ConsultaRepository consultaRepository) {
      this.consultaRepository = consultaRepository;
   }
   public Padecimiento toEntity(PadecimientoView padecimientoView, Padecimiento padecimiento, Boolean update) {
      padecimiento.setIdPaciente(padecimientoView.getIdPaciente());
      padecimiento.setIdMedico(padecimientoView.getIdMedico());
      padecimiento.setFechaCreacion((!update) ? new Date() : padecimiento.getFechaCreacion());
      padecimiento.setCreadoPor(padecimientoView.getCreadoPor());
      padecimiento.setResumen(padecimientoView.getResumen());
      padecimiento.setDiagnostico(padecimientoView.getDiagnostico());
      //Nuevos campos
      padecimiento.setFechaAlta(padecimientoView.getFechaAlta());
      padecimiento.setPresuntivo(padecimientoView.getPresuntivo());
      padecimiento.setIdMedicoTratante(padecimientoView.getIdMedicoTratante());
      padecimiento.setNombreMedicoTratante(padecimientoView.getNombreMedicoTratante());
      padecimiento.setEstatus(padecimientoView.getEstatus());

      CatCie10 cd = catCie10Repository.findOne(padecimientoView.getCie10Id());
      padecimiento.setCatCie10(cd);
      padecimiento.setCatalogKey(cd.getCatalogKey());
      padecimiento.setNombrePadecimiento(cd.getNombre());
      
      DateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
      String fechaSinTiempo = formato.format(padecimiento.getFechaCreacion());
      String datosBusqueda = fechaSinTiempo + " " + (padecimiento.getEstatus() ? "Abierto" : "Cerrado") + " " + padecimiento.getNombreMedicoTratante();
      padecimiento.setDatosBusqueda(datosBusqueda);


//			Para ligar los padecimientos con las consultas
      try {
    	  Set<Consulta> consulta=new HashSet<>();
//       TODO validar que consultaId no venga vacio
    	  consulta.add(consultaRepository.findByIdConsulta(padecimientoView.getConsultaId()));
		padecimiento.setConsultaList(consulta);
	} catch (Exception e) {
		 PadecimientoException pe = new PadecimientoException("Ocurrio un error", PadecimientoException.LAYER_SERVICE, PadecimientoException.ACTION_SELECT);
		 pe.addError("Ocurrio un error al buscar la consulta para crear un nuevo padecimiento");
		 logger.error(ExceptionServiceCode.GROUP + "- Error en PadcimientoConverter, toEntity - CODE: {}-{}", pe.getExceptionCode(), e);
	}

//        PARA LOS ---> ESTUDIOS DE LABORATORIO
      if (padecimientoView.getEstudioLaboratorioViewList() != null && !padecimientoView.getEstudioLaboratorioViewList().isEmpty()) {
         padecimiento.getEstudioLaboratorioList().addAll(
            padecimientoView.getEstudioLaboratorioViewList().stream()
               .map(elView -> {
                  EstudioLaboratorio el = new EstudioLaboratorio();
                  if (elView.getIdEstudioLaboratorio() != null)
                     el.setIdEstudioLaboratorio(elView.getIdEstudioLaboratorio());
                  el.setIdMedico(elView.getIdMedico());
                  el.setFechaCreacion((!update) ? new Date() : padecimiento.getFechaCreacion());
                  el.setTipoEstudio(elView.getTipoEstudio());
                  el.setObservaciones(elView.getObservaciones());
                  el.setNombreLaboratorio(elView.getNombreLaboratorio());
                  el.setIdPaciente(elView.getIdPaciente());
                  el.setPadecimiento(padecimiento);
                  if (elView.getImagenLaboratorioViewList() != null && !elView.getImagenLaboratorioViewList().isEmpty()) {
                     imagenLaboratorioToEntity(el, elView, update);
                  }
                  return el;
               }).collect(Collectors.toList())
         );
      }

//      PARA LOS ---> DOCUMENTOS
      if (padecimientoView.getDocumentosViewList() != null && !padecimientoView.getDocumentosViewList().isEmpty()) {
         padecimiento.getDocumentosList().addAll(
            padecimientoView.getDocumentosViewList().stream()
               .map(dView -> {
                  Documentos d = new Documentos();
                  if (dView.getIdDocumentos() != null)
                     d.setIdDocumentos(dView.getIdDocumentos());
                  d.setContentType(dView.getContentType());
                  d.setDocumentoName(dView.getDocumentoName());
                  d.setPadecimiento(padecimiento);
                  d.setConsulta(null);
                  d.setFechaCreacion((!update) ? new Date() : dView.getFechaCreacion());
                  return d;
               }).collect(Collectors.toList())
         );
      }
      logger.debug("convertir PadecimientoView to Entity: {}", padecimiento);
      return padecimiento;
   }


   private void imagenLaboratorioToEntity(EstudioLaboratorio el, EstudioLaboratorioView elView, Boolean update) {
      el.getImagenLaboratorioList().addAll(
         elView.getImagenLaboratorioViewList().stream()
            .map(ilView -> {
               ImagenLaboratorio il = new ImagenLaboratorio();
               if (ilView.getIdImagenLaboratorio() != null)
                  il.setIdImagenLaboratorio(ilView.getIdImagenLaboratorio());
               il.setContentType(ilView.getContentType());
               il.setImageName(ilView.getImageName());
               il.setIdPaciente(ilView.getIdPaciente());
               il.setEstudioLaboratorio(el);
               il.setFechaCreacion((!update) ? new Date() : ilView.getFechaCreacion());
               il.setConsulta(null);
               return il;
            }).collect(Collectors.toList())
      );
   }


   public PadecimientoView toView(Padecimiento padecimiento, Boolean complete) {
      PadecimientoView padecimientoView = new PadecimientoView();
      padecimientoView.setIdPadecimiento(padecimiento.getIdPadecimiento());
      padecimientoView.setIdPaciente(padecimiento.getIdPaciente());
      padecimientoView.setIdMedico(padecimiento.getIdMedico());
      padecimientoView.setFechaCreacion(padecimiento.getFechaCreacion());
      padecimientoView.setCreadoPor(padecimiento.getCreadoPor());
      padecimientoView.setResumen(padecimiento.getResumen());

      padecimientoView.setDiagnostico(padecimiento.getDiagnostico());
      //nuevos campos
      padecimientoView.setFechaAlta(padecimiento.getFechaAlta());
      padecimientoView.setPresuntivo(padecimiento.getPresuntivo());
      padecimientoView.setIdMedicoTratante(padecimiento.getIdMedicoTratante());
      padecimientoView.setCie10Id(padecimiento.getCatCie10().getIdCie10());

      padecimientoView.setEstatus(padecimiento.getEstatus());
      padecimientoView.setNombreMedicoTratante(padecimiento.getNombreMedicoTratante());

      padecimientoView.setNombrePadecimiento(padecimiento.getNombrePadecimiento());
      padecimientoView.setCatalogKey(padecimiento.getCatalogKey());
      
      if (complete) {


         padecimientoView.getEstudioLaboratorioViewList().addAll(
            padecimiento.getEstudioLaboratorioList().stream()
               .map(el -> {
                  EstudioLaboratorioView elView = new EstudioLaboratorioView();
                  elView.setIdEstudioLaboratorio(el.getIdEstudioLaboratorio());
                  elView.setIdMedico(el.getIdMedico());
                  elView.setFechaCreacion(el.getFechaCreacion());
                  elView.setTipoEstudio(el.getTipoEstudio());
                  elView.setObservaciones(el.getObservaciones());
                  elView.setNombreLaboratorio(el.getNombreLaboratorio());
                  elView.setIdPaciente(el.getIdPaciente());
                  imagenLaboratorioToView(elView, el);
                  return elView;
               }).collect(Collectors.toList())
         );

         padecimientoView.getDocumentosViewList().addAll(
            padecimiento.getDocumentosList().stream()
               .map(d -> {
                  DocumentosView dView = new DocumentosView();
                  dView.setIdDocumentos(d.getIdDocumentos());
                  dView.setContentType(d.getContentType());
                  dView.setDocumentoName(d.getDocumentoName());
                  dView.setIdPaciente(d.getIdPaciente());
                  return dView;
               }).collect(Collectors.toList())
         );
      }

      logger.debug("convertir Padecimiento to View: {}", padecimientoView);
      return padecimientoView;
   }


   private void imagenLaboratorioToView(EstudioLaboratorioView elView, EstudioLaboratorio el) {
      elView.getImagenLaboratorioViewList().addAll(
         el.getImagenLaboratorioList().stream()
            .map(il -> {
               ImagenLaboratorioView ilView = new ImagenLaboratorioView();
               ilView.setIdImagenLaboratorio(il.getIdImagenLaboratorio());
               ilView.setContentType(il.getContentType());
               ilView.setImageName(il.getImageName());
               ilView.setIdPaciente(il.getIdPaciente());
               ilView.setFechaCreacion(il.getFechaCreacion());
               return ilView;
            }).collect(Collectors.toList())
      );
   }
}
