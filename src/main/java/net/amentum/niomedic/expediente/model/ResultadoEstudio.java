package net.amentum.niomedic.expediente.model;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table (name="resultado_estudio")
@ToString
public class ResultadoEstudio implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7187566988813880338L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_resultado_estudio")
	private Long idResultadoEstudio;
	@Size(max=100)
	@NotNull()
	private String mimeType;
	@NotNull
	private String archivo;
	@NotNull
	private Date fechaCreacion;
	@NotNull
	private UUID idPaciente;
	
	//relacion con consulta
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_consulta", referencedColumnName = "id_consulta", nullable=false, updatable=false)
	private Consulta consulta;

}
