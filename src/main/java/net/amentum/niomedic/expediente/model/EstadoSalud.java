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
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Data // @Getter, @Setter, @ToString, @EqualsAndHashCode and @RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "estado_salud")
public class EstadoSalud implements Serializable {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id_estado_salud")
   private Long idEstadoSalud;
   private String motivo;
   private Integer dolometroId;
   private Long idUsuario;
   private UUID idPaciente;
   @Temporal(TemporalType.TIMESTAMP)
   private Date fechaCreacion;
   //   campo que sirve para regresar valor
   private String _doloDescripcion;
}
