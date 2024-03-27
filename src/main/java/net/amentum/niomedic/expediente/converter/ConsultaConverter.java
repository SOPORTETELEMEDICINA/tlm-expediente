package net.amentum.niomedic.expediente.converter;

import net.amentum.niomedic.catalogos.views.CatDolometroView;
import net.amentum.niomedic.expediente.exception.ConsultaException;
import net.amentum.niomedic.expediente.model.Consulta;
import net.amentum.niomedic.expediente.model.Padecimiento;
import net.amentum.niomedic.expediente.persistence.PadecimientoRepository;
import net.amentum.niomedic.expediente.views.ConsultaView;
import net.amentum.niomedic.expediente.views.PadecimientoView;
import net.amentum.niomedic.expediente.views.TratamientoView;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.Date;


@Component
public class ConsultaConverter {
   private final Logger logger = LoggerFactory.getLogger(ConsultaConverter.class);
   private ObjectMapper mapp= new ObjectMapper();
   private PadecimientoRepository padecimientoRepository;
   @Autowired
   public void setPadecimientoRepository(PadecimientoRepository padecimientoRepository) {
      this.padecimientoRepository = padecimientoRepository;
   }
   @Autowired
   PadecimientoConverter padecimientoConverter;
   @Autowired
   TratamientoConverter tratamientoConverter;
   
   public Consulta toEntity(ConsultaView consultaView, Consulta consulta, Boolean update) throws ConsultaException {
	   //campos para un post consulta
 	   ConsultaException ce=null;
	   consulta.setIdPaciente(update ? consulta.getIdPaciente() :consultaView.getIdPaciente());  
	   consulta.setIdUsuario(consultaView.getIdUsuario());
	   consulta.setCreadoPor(update ? consulta.getCreadoPor(): consultaView.getCreadoPor());
	   consulta.setFechaCrecion(update ? consulta.getFechaCrecion() : new Date());
	   consulta.setFechaConsulta(update ? consulta.getFechaConsulta() : consultaView.getFechaConsulta());
	   consulta.setIdEstadoConsulta(update ? consulta.getIdEstadoConsulta() : consultaView.getIdEstadoConsulta());
	   consulta.setEstadoConsulta(update ? consulta.getEstadoConsulta() : consultaView.getEstadoConsulta());
	   consulta.setTipoConsulta(consultaView.getTipoConsulta());
	   consulta.setIdTipoConsulta(update ? consulta.getIdTipoConsulta() : consultaView.getIdTipoConsulta());
	   consulta.setCanal(consultaView.getCanal());
	   consulta.setMotivoConsulta(consultaView.getMotivoConsulta());
	   consulta.setNombrePaciente(update ? consulta.getNombrePaciente() : consultaView.getNombrePaciente());
	   consulta.setResumen(consultaView.getResumen());
	   consulta.setPronostico(consultaView.getPronostico());
	   consulta.setNumeroConsulta(update ? consulta.getNumeroConsulta() : consultaView.getNumeroConsulta());
	   //nuevo campo para referencia y cumplir con los campos del formato impreso
	   JsonNode iT = mapp.convertValue( consultaView.getIncapacidadTemporal(), JsonNode.class );
	   consulta.setIncapacidadTemporal(update ? consulta.getIncapacidadTemporal() :  iT);
	   SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	   consulta.setDatosBusqueda(sdf.format(consulta.getFechaConsulta())+" "+consultaView.getNombreMedico()
	   +" "+consultaView.getTipoConsulta());
	   //terminar o update consulta
	   consulta.setAnalisis(update ? consultaView.getAnalisis() : null);
	   consulta.setSubjetivo(update ? consultaView.getSubjetivo(): null);
	   consulta.setObjetivo(update ? consultaView.getObjetivo() : null);
	   consulta.setPlanTerapeutico(update ? consultaView.getPlanTerapeutico() : null );
	   JsonNode sV= mapp.convertValue(consultaView.getSignosVitales(), JsonNode.class);
	   consulta.setSignosVitales((update || (!update && consultaView.getIdEstadoConsulta() == 1  && consultaView.getIdTipoConsulta() == 3)   )  ? sV : null);
	   JsonNode ef=mapp.convertValue(consultaView.getExploracionFisica(), JsonNode.class);
	   consulta.setExploracionFisica(ef);
	   
	   //Link de zoom
	   consulta.setMeeting(update ? consulta.getMeeting() : consultaView.getMeeting());
	   consulta.setIdUsurioZoom(update ? consulta.getIdUsurioZoom() : consultaView.getIdUsurioZoom());
	   consulta.setIdMeeting(update ? consulta.getIdMeeting() : consultaView.getIdMeeting());

	   consulta.setIdGroup(consultaView.getIdGroup());

	   if(consultaView.getCatDolometroView() != null) {
		   consulta.setIdCatDolometro(update ? consulta.getIdCatDolometro() : consultaView.getCatDolometroView().getIdCatDolometro());
		   consulta.setNivelDolorometro(update ?  consulta.getNivelDolorometro() : consultaView.getCatDolometroView().getNivel());
	   }
	  if(consulta.getIdTipoConsulta()==2 || consulta.getIdTipoConsulta()==3) {
		  /*
		   * cuando una consulta de tipo interconsulta o refeferencia
		   * el medico solicitado se vulve el propietario de la consulta por eso se guarda en  los campos
		   * idMedico, nombreMedico, especialidad
		   * del modelo entidad
		   */
		  consulta.setIdMedico(update ? consulta.getIdMedico() : consultaView.getIdMedicoSolicitado());
		  consulta.setNombreMedico(update ? consulta.getNombreMedico() : consultaView.getNombreMedicoSolicitado());
		  consulta.setEspecialidad(update ? consulta.getEspecialidad() : consultaView.getEspecialidadMedicoSolicitado());
		  /*
		   * los campos de de consultaView: idMedico, nombreMedico, especialidad
		   * se guardan en el modelo entidad como idMedicoSolicitante, especialidadMedicoSolicitante, nombreMedicoSolicitante
		   */
		  consulta.setIdMedicoSolicitante(update ?consulta.getIdMedicoSolicitante() : consultaView.getIdMedico());
		  consulta.setEspecialidadMedicoSolicitante(update ?consulta.getEspecialidadMedicoSolicitante() : consultaView.getEspecialidad());
		  consulta.setNombreMedicoSolicitante(update ? consulta.getNombreMedicoSolicitante() : consultaView.getNombreMedico());
		  
	  }else {
		  consulta.setIdMedico(update ? consulta.getIdMedico() : consultaView.getIdMedico());
		  consulta.setNombreMedico(update ? consulta.getNombreMedico() : consultaView.getNombreMedico());
		  consulta.setEspecialidad(update ? consulta.getEspecialidad() : consultaView.getEspecialidad());
	  }
	   consulta.setUrgente((update && consultaView.getIdTipoConsulta() == 3)  ? consulta.getUrgente() : consultaView.getUrgente());
	   consulta.setSamu((update && consultaView.getIdTipoConsulta() == 3)  ?  consulta.getSamu() : consultaView.getSamu());
	   consulta.setIdServicio( (update && consultaView.getIdTipoConsulta() == 3)  ? consulta.getIdServicio() : consultaView.getIdServicio());
	   consulta.setServicio( (update && consultaView.getIdTipoConsulta() == 3)  ? consulta.getServicio() :  consultaView.getServicio());
	   consulta.setIdMotivoEnvio( (update && consultaView.getIdTipoConsulta() == 3)  ? consulta.getIdMotivoEnvio() :  consultaView.getIdMotivoEnvio());
	   consulta.setMotivoEnvio((update && consultaView.getIdTipoConsulta() == 3)  ? consulta.getMotivoEnvio() : consultaView.getMotivoEnvio());
	   consulta.setReferencia1(consultaView.getReferencia1());
	   consulta.setReferencia2(consultaView.getReferencia2());
	   
	   if(consultaView.getIdEstadoConsulta()==4 || consultaView.getIdEstadoConsulta() == 1) {
		   for(PadecimientoView pdV:consultaView.getListaPadecimiento()) {	
			   try {
				   if(pdV!=null) {
					   if(consultaView.getIdEstadoConsulta() == 1 ? !padecimientoRepository.exists(pdV.getIdPadecimiento()) : false) {
						   ce= new ConsultaException("Error Inesperado al agregar Consulta.", ConsultaException.LAYER_DAO, ConsultaException.ACTION_VALIDATE);
						   ce.addError("El padecimiento con el id: "+pdV.getIdPadecimiento()+" no Existe");
						   logger.error("toEntity()-  no existe el padecimiento con el idPadecimiento{} ", pdV.getIdPadecimiento());
						   throw ce;
					   }
					   Padecimiento pd=new Padecimiento();
					   pd=padecimientoRepository.findByIdPadecimiento(pdV.getIdPadecimiento());
					   if(!pd.getIdPaciente().equals(consultaView.getIdPaciente().toString())) {
						   ce= new ConsultaException("Error Inesperado al agregar Consulta.", ConsultaException.LAYER_DAO, ConsultaException.ACTION_VALIDATE);
						   ce.addError("La informacion de la consulta y el padecimiento no coincide");
						   logger.error("toEntity()-  el idPaciente de consulta no es el mismo que esta en el padecimiento consulta.idPaciente: {} - padecimiento.idPaciente {}",consultaView.getIdPaciente(), pd.getIdPaciente());
						   throw ce;

					   }

					   consulta.getPadecimiento().add(pd);
				   }else {
					   ce= new ConsultaException("Error Inesperado en Consulta.", ConsultaException.LAYER_DAO, ConsultaException.ACTION_VALIDATE);
					   ce.addError("La lista de padecimiento contiene un objeto NULO");
					   logger.error("toEntity()-  El arreglo de Padecimiento contiene un Objeto NULL {}",consultaView.getListaPadecimiento());
					   throw ce;
				   }
			   }catch(ConsultaException e){
				   throw e;
			   } catch (Exception e) {	
				   ce = new ConsultaException("No se agrego ningun padecimiento a la consulta", ConsultaException.LAYER_DAO, ConsultaException.ACTION_UPDATE);
				   logger.error("toEntity()-Ocurrio un error al querer ligar el padecimientocon la consulta {}", e);
				   throw ce;
			  }
		   }
	   }
      logger.debug("convertir ConsultaView to Entity: {}", consulta);
      return consulta;
   }

   public ConsultaView toView(Consulta consulta, Boolean complete) {
	   ConsultaView consultaView = new ConsultaView();
	   if(complete) {
		   consultaView.setIdConsulta(consulta.getIdConsulta());
		   consultaView.setAnalisis(consulta.getAnalisis());
		   consultaView.setIdUsuario(consulta.getIdUsuario());
		   consultaView.setCanal(consulta.getCanal());
		   consultaView.setCreadoPor(consulta.getCreadoPor());
		   consultaView.setEspecialidad(consulta.getEspecialidad());
		   consultaView.setEstadoConsulta(consulta.getEstadoConsulta());
		   consultaView.setFeachaFin(consulta.getFeachaFin());
		   consultaView.setFechaCancelacion(consulta.getFechaCancelacion());
		   consultaView.setFechaConsulta(consulta.getFechaConsulta());
		   consultaView.setFechaCrecion(consulta.getFechaCrecion());
		   consultaView.setFechaInicio(consulta.getFechaInicio());
		   consultaView.setIdEstadoConsulta(consulta.getIdEstadoConsulta());
		   consultaView.setIdMedico(consulta.getIdMedico());
		   consultaView.setIdPaciente(consulta.getIdPaciente());
		   consultaView.setIdTipoConsulta(consulta.getIdTipoConsulta());
		   consultaView.setMotivoConsulta(consulta.getMotivoConsulta());
		   consultaView.setNombreMedico(consulta.getNombreMedico());
		   consultaView.setNombrePaciente(consulta.getNombrePaciente());
		   consultaView.setNumeroConsulta(consulta.getNumeroConsulta());
		   consultaView.setObjetivo(consulta.getObjetivo());
		   consultaView.setPlanTerapeutico(consulta.getPlanTerapeutico());
		   consultaView.setResumen(consulta.getResumen());
		   consultaView.setPronostico(consulta.getPronostico());
		   consultaView.setSubjetivo(consulta.getSubjetivo());
		   consultaView.setTipoConsulta(consulta.getTipoConsulta());
		   consultaView.setIdUsuario(consulta.getIdUsuario());
		   consultaView.setFechaConsultaFin(consulta.getFechaConsultaFin());
		   CatDolometroView catDolometroView = new CatDolometroView();
		   catDolometroView.setIdCatDolometro(consulta.getIdCatDolometro());
		   catDolometroView.setNivel(consulta.getNivelDolorometro());
		   consultaView.setCatDolometroView(catDolometroView);
		   
		   consultaView.setNombreMedicoSolicitado(consulta.getNombreMedicoSolicitante());
		   consultaView.setIdMedicoSolicitado(consulta.getIdMedicoSolicitante());
		   consultaView.setEspecialidadMedicoSolicitado(consulta.getEspecialidadMedicoSolicitante());
		   
		   consultaView.setUrgente(consulta.getUrgente());
		   consultaView.setSamu(consulta.getSamu());
		   consultaView.setIdServicio(consulta.getIdServicio());
		   consultaView.setServicio(consulta.getServicio());
		   consultaView.setIdMotivoEnvio(consulta.getIdMotivoEnvio());
		   consultaView.setMotivoEnvio(consulta.getMotivoEnvio());
		   
		   consultaView.setReferencia1(consulta.getReferencia1());
		   consultaView.setReferencia2(consulta.getReferencia2());
		   //Link de zoom
		   consultaView.setMeeting(consulta.getMeeting());
		   consultaView.setIdUsurioZoom(consulta.getIdUsurioZoom());
		   consultaView.setIdMeeting(consulta.getIdMeeting());
		   consultaView.setIdGroup(consulta.getIdGroup());
		   try {
			   if(consulta.getPadecimiento()!=null) {

				   for(Padecimiento pd: consulta.getPadecimiento()) {
			    		  consultaView.getListaCatCie10().add(pd.getCatCie10().getIdCie10());
				   }
			   }else {
				   consultaView.setListaCatCie10(null);  
			   }
		   } catch (Exception e) {
			   ConsultaException ce = new ConsultaException("Ocrrio un erro al buscar los padecimientos de la consulta", ConsultaException.LAYER_DAO, ConsultaException.ACTION_UPDATE);
			   ce.addError("Ocrrio un erro al buscar los padecimientos para el Idconsulta: "+consultaView.getIdConsulta());
			   logger.error("Error en metodo toView Consulta al buscar el padecimiento- CODE {} - {}", ce.getExceptionCode(), e.getMessage());
		   }
		   consulta.getTratamientoList().forEach(tratamiento -> {
			   consultaView.getListaTartamiento().add(tratamientoConverter.toView(new TratamientoView(), tratamiento));
		   });
		   if(consulta.getPadecimiento()!=null) {
			   for(Padecimiento pd: consulta.getPadecimiento()) {
				   consultaView.getListaPadecimiento().add(padecimientoConverter.toView(pd, Boolean.TRUE));
		   		}
		   }
		   consultaView.setSignosVitales(mapp.convertValue(consulta.getSignosVitales(), Map.class));
		   consultaView.setExploracionFisica(mapp.convertValue(consulta.getExploracionFisica(), Map.class));
		   consultaView.setIncapacidadTemporal(mapp.convertValue(consulta.getIncapacidadTemporal(), Map.class));

	   }else {
		   consultaView.setIdConsulta(consulta.getIdConsulta());
		   consultaView.setIdPaciente(consulta.getIdPaciente());
		   consultaView.setNombrePaciente(consulta.getNombrePaciente());
		   consultaView.setIdMedico(consulta.getIdMedico());
		   consultaView.setNombreMedico(consulta.getNombreMedico());
		   consultaView.setFechaConsulta(consulta.getFechaConsulta());
	       consultaView.setEstadoConsulta(consulta.getEstadoConsulta());
	       consultaView.setIdEstadoConsulta(consulta.getIdEstadoConsulta());
	       consultaView.setIdTipoConsulta(consulta.getIdTipoConsulta());
	       consultaView.setCanal(consulta.getCanal());
	       consultaView.setMotivoConsulta(consulta.getMotivoConsulta());
	       consultaView.setNumeroConsulta(consulta.getNumeroConsulta());
	       consultaView.setEspecialidad(consulta.getEspecialidad());
	   	   consultaView.setIdUsuario(consulta.getIdUsuario());
	   	   //Link de zoom
		   consultaView.setMeeting(consulta.getMeeting());
	   }
      logger.debug("convertir Consulta to View: {}", consultaView);
      return consultaView;

   }
   
   public JsonNode SignosVitalesToEntity(Map<String,Object> signosVitales) {
	   JsonNode sV= mapp.convertValue(signosVitales, JsonNode.class);
	   return sV;
   }

}
