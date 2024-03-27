package net.amentum.niomedic.expediente.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Data // @Getter, @Setter, @ToString, @EqualsAndHashCode and @RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "mediciones_paciente")
public class MedicionesPaciente implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_mediciones_paciente")
	private Long idMedicionesPaciente;
	@Temporal(TemporalType.TIMESTAMP)
	private Date fechaCreacion;
	private UUID idPaciente;
	private Integer diabetes;
	//   private Integer oximetria;
	private Integer oxSpo2;
	private Integer oxPr;
	private Integer hiperSistolica;
	private Integer hiperDiastolica;
	private Integer hiperPulso;
	private Long idUsuario;
	@Size(max = 255)
	private String nombrePaciente;
	@Size(max = 255)
	private String nombreUsuario;
	private Boolean fueraLimites;
	@NotNull
	private Long idPeriodoControles;
	@Override
	public String toString() {
		return "MedicionesPaciente {" +
				"idMedicionesPaciente=" + idMedicionesPaciente + 
				", fechaCreacion=" + fechaCreacion +
				", idPaciente=" + idPaciente + 
				", diabetes=" + diabetes + 
				", oxSpo2=" + oxSpo2 + 
				", oxpr=" + oxPr +
				", hiperSistolica=" + hiperSistolica + 
				", hiperDiastolica=" + hiperDiastolica + 
				", hiperPulso=" + hiperPulso + 
				", idUsuario=" + idUsuario + 
				", nombrePaciente=" + nombrePaciente + 
				", nombreUsuario=" + nombreUsuario + 
				", fueraLimites=" + fueraLimites + 
				", idPeriodoControles=" + idPeriodoControles + 
				"}";
	}

}
