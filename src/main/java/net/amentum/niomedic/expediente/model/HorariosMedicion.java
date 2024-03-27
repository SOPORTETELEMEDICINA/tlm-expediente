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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Data // @Getter, @Setter, @ToString, @EqualsAndHashCode and @RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "horarios_medicion")
public class HorariosMedicion implements Serializable {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id_horarios_medicion")
   private Long idHorariosMedicion;
   @Temporal(TemporalType.TIMESTAMP)
   private Date Hora;
   private UUID idPaciente;
   //   relaciones
   @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
   @JoinColumn(name = "catalogo_momento_id", referencedColumnName = "id_catalogo_momento")
   private CatalogoMomento catalogoMomento;

   @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
   @JoinColumn(name = "catalogo_dia_id", referencedColumnName = "id_catalogo_dia")
   private CatalogoDia catalogoDia;

   @Override
   public String toString() {
      return "HorariosMedicion{" +
         "idHorariosMedicion=" + idHorariosMedicion +
         ", Hora=" + Hora +
         ", idPaciente=" + idPaciente +
         '}';
   }
}
