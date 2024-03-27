package net.amentum.niomedic.expediente.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "padecimiento")
public class Padecimiento implements Serializable {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id_padecimiento")
   private Long idPadecimiento;

   @Column(name = "id_paciente")
   private String idPaciente;
   @Column(name = "id_medico")
   private String idMedico;
   @Temporal(TemporalType.TIMESTAMP)
   private Date fechaCreacion;
   private String creadoPor;
   @Column(columnDefinition = "TEXT")
   private String resumen;

   @Column(columnDefinition = "TEXT")
   private String diagnostico;
   
 //nuevos campos
   @Temporal(TemporalType.TIMESTAMP)
   private Date fechaAlta;
   private Boolean presuntivo;
   private String idMedicoTratante;
   private String nombreMedicoTratante;
   private String nombrePadecimiento;
   private String catalogKey;
   private Boolean estatus;
   //   campo compuesto de busqueda
   @Column(columnDefinition = "TEXT")
   private String datosBusqueda;

      @OneToMany(cascade = CascadeType.ALL, mappedBy = "padecimiento")
   private Collection<EstudioLaboratorio> estudioLaboratorioList = new ArrayList<>();

      @ManyToMany(cascade = CascadeType.ALL, mappedBy = "padecimiento")
      private Set<Consulta> consultaList;

   @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
   @JoinColumn(name = "cie10_id", referencedColumnName = "id_cie10")
   private CatCie10 catCie10;

   @OneToMany(cascade = CascadeType.ALL, mappedBy = "padecimiento")
   private Collection<Documentos> documentosList = new ArrayList<>();
   
   @Override
   public int hashCode() {
       return Objects.hash(idPadecimiento);
   }
   
   @Override
   public String toString() {
      return "PadecimientoView{" +
         "idPadecimiento=" + idPadecimiento +
         ", idPaciente='" + idPaciente + '\'' +
         ", idMedico='" + idMedico + '\'' +
         ", fechaCreacion=" + fechaCreacion +
         ", creadoPor='" + creadoPor + '\'' +
         ", resumen='" + resumen + '\'' +
         ", diagnostico='" + diagnostico + '\'' +
         ", fechaAlta=" + fechaAlta +
         ", presuntivo=" + presuntivo +
         ", idMedicoTratante='" + idMedicoTratante + '\'' +
         ", nombreMedicoTratante='" + nombreMedicoTratante + '\'' +
         ", estatus=" + estatus +
         ", nombrePadecimiento='" + nombrePadecimiento + '\'' +
         ", catalogKey='"+catalogKey+'\''+
         '}';
   }


}
