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
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "turnos_laborales")
public class TurnosLaborales implements Serializable {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id_turnos_laborales")
   private Long idTurnosLaborales;
//   @Column(unique = true, nullable = false)
//   private UUID medicoId;
   @Column(unique = true, nullable = false)
   private Long idUsuario;
//   @Size(max = 60)
//   private String nombreMedico;
   @Size(max = 100)
   private String nombreUsuario;
   @Temporal(TemporalType.TIMESTAMP)
   private Date inicio;
   @Temporal(TemporalType.TIMESTAMP)
   private Date fin;
   private Boolean lunes;
   private Boolean martes;
   private Boolean miercoles;
   private Boolean jueves;
   private Boolean viernes;
   private Boolean sabado;
   private Boolean domingo;
}
