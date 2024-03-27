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

@Data // @Getter, @Setter, @ToString, @EqualsAndHashCode and @RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "notificaciones")
public class Notificaciones implements Serializable {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id_notificaciones")
   private Long idNotificaciones;
   private String tipo;
   @NotNull(message = "No puede venir vacio")
   private Long idUsuario;
   @Size(max = 100)
   private String subject;
   @Size(max = 255)
   private String descripcion;
   @Temporal(TemporalType.TIMESTAMP)
   private Date _fechaCreacion;
   @Temporal(TemporalType.TIMESTAMP)
   private Date fechaEnvio;
   @Temporal(TemporalType.TIMESTAMP)
   private Date fechaLeido;
   @Temporal(TemporalType.TIMESTAMP)
   private Date fechaBorrado;
   private byte[] adjuntos;
   private String accion;
   private Integer prioridad;
   private Integer estatus;
}
