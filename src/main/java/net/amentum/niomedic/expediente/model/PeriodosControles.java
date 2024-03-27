package net.amentum.niomedic.expediente.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="Periodo_control")
public class PeriodosControles implements Serializable {
	private static final long serialVersionUID = 5204247167847870477L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_periodo_control")
	private Long idPeriodoControl;
	@NotNull
	private Integer diaSemana;
	@NotNull
	@Temporal(TemporalType.TIME)
	private Date horario;
	private Boolean diabetes;
	private Boolean hipertension;
	private Boolean oximetria;
	
	//relaciones
	@ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	@JoinColumn(name="id_control",referencedColumnName = "id_control",nullable=false)
	private Controles controles;
	
	@Override
	public String toString() {
		return "Periodos {"	+ 
				"idPeriodo=" + idPeriodoControl +
				", diaSemana=" + diaSemana +
				", diabetes=" + diabetes + 
				", horario=" + horario + 
				", hipertension=" + hipertension + 
				", Oximetria=" + oximetria +
				"}";
	}
	
	
	
}
