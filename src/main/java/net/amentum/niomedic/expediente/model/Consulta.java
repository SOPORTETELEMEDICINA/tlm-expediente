package net.amentum.niomedic.expediente.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.databind.JsonNode;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "consulta")
@TypeDef(
		name = "json", typeClass = JsonBinaryType.class
	)
public class Consulta implements Serializable {
   /**
	 * 
	 */
	private static final long serialVersionUID = 1223514178913097409L;
@Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id_consulta")
   private Long idConsulta;
   private UUID idPaciente;
	private UUID idMedico;
	@Size(max = 50)
	private String creadoPor;
	@Temporal(TemporalType.TIMESTAMP)
	private Date fechaCrecion;
	@Temporal(TemporalType.TIMESTAMP)
	private Date fechaConsulta;
	@Temporal(TemporalType.TIMESTAMP)
	private Date fechaConsultaFin;
	@Temporal(TemporalType.TIMESTAMP)
	private Date fechaInicio;
	@Temporal(TemporalType.TIMESTAMP)
	private Date fechaCancelacion;
	@Temporal(TemporalType.TIMESTAMP)
	private Date feachaFin;
	private Integer idEstadoConsulta;
	@Size(max = 20)
	private String estadoConsulta;
	@Size(max = 20)
	@NotBlank
	private String tipoConsulta;
	@NotNull
	private Integer idTipoConsulta;
	@Size(max = 20)
	private String canal;
	@Column(columnDefinition = "TEXT")
	private String motivoConsulta;
	@Column(columnDefinition = "TEXT")
	private String analisis;
	@Column(columnDefinition = "TEXT")
	private String subjetivo;
	@Column(columnDefinition = "TEXT")
	private String planTerapeutico;
	@Column(columnDefinition = "TEXT")
	private String objetivo;
	@Column(columnDefinition = "TEXT")
	private String resumen;
   @Column(columnDefinition = "TEXT")
   private String pronostico;
   	private String datosBusqueda;
	private String nombreMedico;
	private String nombrePaciente;
   @Column(name = "numero_consulta", columnDefinition = "serial")
   @Generated(GenerationTime.INSERT)
	private Long numeroConsulta;
	private String especialidad;
	//campos tipo json
	@Type( type = "json" )
	@Column(columnDefinition = "json")
	private JsonNode signosVitales;
	@Type( type = "json" )
	@Column(columnDefinition = "json")
	private JsonNode exploracionFisica;
	private Long idUsuario;
	//Nuevo campo
	private Integer idCatDolometro;
	private Integer nivelDolorometro;
	// referencia e interconsulta
	//TODO cambiar de  solicitante a solicitado
	private UUID idMedicoSolicitante;
	private String nombreMedicoSolicitante;
	private String especialidadMedicoSolicitante;
	//consulta tipo referencia
	private Boolean urgente;
	private Boolean samu;
	private Integer idServicio;
	private String servicio;
	private Integer idMotivoEnvio;
	private String motivoEnvio;
		//campo tipo json
	@Type(type = "json")
	@Column(columnDefinition = "json")
	private JsonNode incapacidadTemporal;
	//campos para consulta his
	private String referencia1;//idConsulta his
	private String referencia2;//idPaciente his
	//info de zoom
	private String meeting;
	private String idUsurioZoom;
	private Long idMeeting;
	private Integer idGroup;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "consulta")
	   private Collection<Tratamiento> tratamientoList = new ArrayList<>();
	
	
   @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
   @JoinTable(
		   name = "consulta_padecimiento",	   
		   joinColumns = { @JoinColumn(name = "id_consulta", referencedColumnName = "id_consulta", nullable = true)},
		   inverseJoinColumns ={@JoinColumn(name = "id_padecimiento", referencedColumnName = "id_padecimiento", nullable = true)}
		   )
   private Set<Padecimiento> padecimiento = new HashSet<>();

   //   nuevas relaciones por nuevas ventanas
   @OneToMany(cascade = CascadeType.ALL, mappedBy = "consulta")
   private Collection<Documentos> documentosList = new ArrayList<>();

   @OneToMany(cascade = CascadeType.ALL, mappedBy = "consulta")
   private Collection<EstudioLaboratorio> estudioLaboratorioList = new ArrayList<>();

   @OneToMany(cascade = CascadeType.ALL, mappedBy = "consulta")
   private Collection<ImagenLaboratorio> imagenLaboratorioList = new ArrayList<>();
   
   @OneToMany(cascade = CascadeType.ALL, mappedBy = "consulta")
   private Set<ResultadoEstudio> resultadoEstudio = new HashSet<>();

   @Override
   public int hashCode() {
       return Objects.hash(idConsulta);
   }

	@Override
	public String toString() {
		return "Consulta{" +
				"idConsulta=" + idConsulta +
				", idPaciente=" + idPaciente +
				", idMedico=" + idMedico +
				", creadoPor='" + creadoPor + '\'' +
				", fechaCrecion=" + fechaCrecion +
				", fechaConsulta=" + fechaConsulta +
				", fechaConsultaFin=" + fechaConsultaFin +
				", fechaInicio=" + fechaInicio +
				", fechaCancelacion=" + fechaCancelacion +
				", feachaFin=" + feachaFin +
				", idEstadoConsulta=" + idEstadoConsulta +
				", estadoConsulta='" + estadoConsulta + '\'' +
				", tipoConsulta='" + tipoConsulta + '\'' +
				", idTipoConsulta=" + idTipoConsulta +
				", canal='" + canal + '\'' +
				", motivoConsulta='" + motivoConsulta + '\'' +
				", analisis='" + analisis + '\'' +
				", subjetivo='" + subjetivo + '\'' +
				", planTerapeutico='" + planTerapeutico + '\'' +
				", objetivo='" + objetivo + '\'' +
				", resumen='" + resumen + '\'' +
				", pronostico='" + pronostico + '\'' +
				", datosBusqueda='" + datosBusqueda + '\'' +
				", nombreMedico='" + nombreMedico + '\'' +
				", nombrePaciente='" + nombrePaciente + '\'' +
				", numeroConsulta=" + numeroConsulta +
				", especialidad='" + especialidad + '\'' +
				", signosVitales=" + signosVitales +
				", exploracionFisica=" + exploracionFisica +
				", idUsuario=" + idUsuario +
				", idCatDolometro=" + idCatDolometro +
				", nivelDolorometro=" + nivelDolorometro +
				", idMedicoSolicitante=" + idMedicoSolicitante +
				", nombreMedicoSolicitante='" + nombreMedicoSolicitante + '\'' +
				", especialidadMedicoSolicitante='" + especialidadMedicoSolicitante + '\'' +
				", urgente=" + urgente +
				", samu=" + samu +
				", idServicio=" + idServicio +
				", servicio='" + servicio + '\'' +
				", idMotivoEnvio=" + idMotivoEnvio +
				", motivoEnvio='" + motivoEnvio + '\'' +
				", incapacidadTemporal=" + incapacidadTemporal +
				", referencia1='" + referencia1 + '\'' +
				", referencia2='" + referencia2 + '\'' +
				", meeting='" + meeting + '\'' +
				", idUsurioZoom='" + idUsurioZoom + '\'' +
				", idMeeting=" + idMeeting +
				", idGroup=" + idGroup +
				", tratamientoList=" + tratamientoList +
				", padecimiento=" + padecimiento +
				", documentosList=" + documentosList +
				", estudioLaboratorioList=" + estudioLaboratorioList +
				", imagenLaboratorioList=" + imagenLaboratorioList +
				", resultadoEstudio=" + resultadoEstudio +
				'}';
	}
}
