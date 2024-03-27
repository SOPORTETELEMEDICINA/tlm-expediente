package net.amentum.niomedic.expediente.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "estudio_laboratorio", indexes = {@Index(name = "idx_estudio_laboratorio_id_medico", columnList = "id_medico")})
public class EstudioLaboratorio implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="id_estudio_laboratorio")
    private Long idEstudioLaboratorio;
    @Column(name = "id_medico")
    private String idMedico;
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    private String tipoEstudio;
    private String observaciones;
   //   por nuevas pantallas
   private String nombreLaboratorio;
   private String idPaciente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "padecimiento_id", referencedColumnName = "id_padecimiento")
    private Padecimiento padecimiento;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "estudioLaboratorio")
    private Collection<ImagenLaboratorio> imagenLaboratorioList = new ArrayList<>();

   //   nuevas relaciones por nuevas ventanas
   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "consulta_id", referencedColumnName = "id_consulta")
   private Consulta consulta;


   @Override
   public String toString() {
      return "EstudioLaboratorioView{" +
         "idEstudioLaboratorio=" + idEstudioLaboratorio +
         ", idMedico='" + idMedico + '\'' +
         ", fechaCreacion=" + fechaCreacion +
         ", tipoEstudio='" + tipoEstudio + '\'' +
         ", observaciones='" + observaciones + '\'' +
         ", nombreLaboratorio='" + nombreLaboratorio + '\'' +
         '}';
   }
}

