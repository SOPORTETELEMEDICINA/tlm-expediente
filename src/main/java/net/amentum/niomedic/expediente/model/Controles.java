package net.amentum.niomedic.expediente.model;

import java.io.Serializable;

import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import javax.persistence.*;
import javax.validation.constraints.NotNull;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="control",uniqueConstraints={
		@UniqueConstraint(columnNames = {"idPaciente"})})
public class Controles implements Serializable {

	private static final long serialVersionUID = 8676073490499610813L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_control")
	private Long idControl;
	@NotNull
	private UUID idPaciente;
	//Ranogos Diabetes
	private Integer dLimiteRangoBajo;
	private Integer dRangoBajo;
	private Integer dRangoAlto;
	private Integer dLimiteRangoAlto;
	//Ranogos Oximetria
		//frecuencia de pulso
	private Integer oxPrLimiteRangoBajo;
	private Integer oxPrRangoBajo;
	private Integer oxPrRangoAlto;
	private Integer oxPrLimiteRangoAlto;
		//saturación de oxígeno
	private Integer oxSpo2LimiteRangoBajo;
	private Integer oxSpo2RangoBajo;
	private Integer oxSpo2RangoAlto;
	private Integer oxSpo2LimiteRangoAlto;
//	private Integer oxLimiteRangoBajo;
//	private Integer oxRangoBajo;
//	private Integer oxRangoAlto;
//	private Integer oxLimiteRangoAlto;
	//Ranogos Hipertension
		//diastolica
	@Column(name = "hp_d_limite_rango_bajo")
	private Integer hpDLimiteRangoBajo;
	@Column(name = "hp_d_rango_bajo")
	private Integer hpDRangoBajo;
	@Column(name = "hp_d_rango_alto")
	private Integer hpDRangoAlto;
	@Column(name = "hp_d_limite_rango_alto")
	private Integer hpDLimiteRangoAlto;
		//sistolica
	@Column(name = "hp_s_limite_rango_bajo")
	private Integer hpSLimiteRangoBajo;
	@Column(name = "hp_s_rango_bajo")
	private Integer hpSRangoBajo;
	@Column(name = "hp_s_rango_alto")
	private Integer hpSRangoAlto;
	@Column(name = "hp_s_limite_rango_alto")
	private Integer hpSLimiteRangoAlto;
		//pulso
	@Column(name = "hp_p_limite_rango_bajo")
	private Integer hpPLimiteRangoBajo;
	@Column(name = "hp_p_rango_bajo")
	private Integer hpPRangoBajo;
	@Column(name = "hp_p_rango_alto")
	private Integer hpPRangoAlto;
	@Column(name = "hp_p_limite_rango_alto")
	private Integer hpPLimiteRangoAlto;
	@Temporal(TemporalType.TIMESTAMP)
	private Date fechaCreacion;
	private Long IdUsuarioQuienCreo;
	private String nombreQuienCreo;
	@Temporal(TemporalType.TIMESTAMP)
	private Date fechaUltimaModificacion;
	private Long IdUsuarioQuienModificia;
	private String nombreQuienModifica;
	private Boolean hipertension;
	private Boolean oximetria;
	private Boolean diabetes;
	
	//relacion
	@OneToMany(fetch= FetchType.LAZY, cascade= CascadeType.ALL, mappedBy = "controles")
	private Set<PeriodosControles> periodosControles= new HashSet<PeriodosControles>();
	
	@Override
	   public int hashCode() {
	       return Objects.hash(idControl);
	   }

	
	@Override
	public String toString() {
		return "Controles { " + "idControl=" + idControl + 
				", idPaciente=" + idPaciente + 
				", dLimiteRangoBajo=" + dLimiteRangoBajo +
				", dRangoBajo="	+ dRangoBajo + 
				", dRangoAlto=" + dRangoAlto + 
				", dLimiteRangoAlto=" + dLimiteRangoAlto + 
				", oxPrLimiteRangoBajo="+ oxPrLimiteRangoBajo +
				", oxPrRangoBajo=" + oxPrRangoBajo + 
				", oxPrRangoAlto=" + oxPrRangoAlto + 
				", oxPrLimiteRangoAlto=" + oxPrLimiteRangoAlto +
				", oxSpo2LimiteRangoBajo=" + oxSpo2LimiteRangoBajo + 
				", oxSpo2RangoBajo=" + oxSpo2RangoBajo + 
				", oxSpo2RangoAlto=" + oxSpo2RangoAlto + 
				", oxSpo2LimiteRangoAlto" + oxSpo2LimiteRangoAlto + 
//				", oxLimiteRangoBajo=" + oxLimiteRangoBajo + 
//				", oxRangoBajo=" + oxRangoBajo + 
//				", oxRangoAlto=" + oxRangoAlto + 
//				", oxLimiteRangoAlto=" + oxLimiteRangoAlto + 
				", hpDLimiteRangoBajo="	+ hpDLimiteRangoBajo + 
				", hpDRangoBajo=" + hpDRangoBajo + 
				", hpDRangoAlto=" + hpDRangoAlto	+
				", hpDLimiteRangoAlto=" + hpDLimiteRangoAlto + 
				", hpSLimiteRangoBajo=" + hpSLimiteRangoBajo+ 
				", hpSRangoBajo=" + hpSRangoBajo +
				", hpSRangoAlto=" + hpSRangoAlto + 
				", hpSLimiteRangoAlto=" + hpSLimiteRangoAlto +
				", hpPLimiteRangoBajo=" + hpPLimiteRangoBajo + 
				", hpPRangoBajo=" + hpPRangoBajo + 
				", hpPRangoAlto=" + hpPRangoAlto +
				", hpPLimiteRangoAlto=" + hpPLimiteRangoAlto + 
				", fechaCreacion=" + fechaCreacion + 
				", IdUsuarioQuienCreo=" + IdUsuarioQuienCreo + 
				", nombreQuienCreo=" + nombreQuienCreo +
				", fechaUltimaModificacion=" + fechaUltimaModificacion + 
				", nombreQuienModifica="+ nombreQuienModifica + 
				", hipertension=" + hipertension + 
				", oximetria=" + oximetria + 
				", diabetes="+ diabetes + 
				
				"}";
	}
	

}
