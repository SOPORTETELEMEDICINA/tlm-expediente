package net.amentum.niomedic.expediente.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data // @Getter, @Setter, @ToString, @EqualsAndHashCode and @RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "invitados")
public class Invitados implements Serializable {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id_invitados")
   private Long idInvitados;
   private Long idUsuario;
   @Size(max = 120)
   private String nombreCompleto;
   //   relaciones
   @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
   @JoinColumn(name = "eventos_id", referencedColumnName = "id_eventos")
   private Eventos eventos;

   @Override
   public String toString() {
      return "Invitados{" +
         "idInvitados=" + idInvitados +
         ", idUsuario=" + idUsuario +
         ", nombreCompleto='" + nombreCompleto + '\'' +
         '}';
   }
}
