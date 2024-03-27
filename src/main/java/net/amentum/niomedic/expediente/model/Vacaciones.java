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
@Table(name = "vacaciones")
public class Vacaciones implements Serializable {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id_vacaciones")
   private Long idVacaciones;
   //   private UUID medicoId;
   private Long idUsuario;
   //   @Size(max = 60)
//   private String nombreMedico;
   @Size(max = 100)
   private String nombreUsuario;
   @Temporal(TemporalType.TIMESTAMP)
   private Date inicio;
   @Temporal(TemporalType.TIMESTAMP)
   private Date fin;
}
