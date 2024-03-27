package net.amentum.niomedic.expediente.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "imagen_laboratorio")
public class ImagenLaboratorio implements Serializable {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id_imagen_laboratorio")
   private Long idImagenLaboratorio;
   private String contentType;
   private String imageName;
   private String idPaciente;
   @Temporal(TemporalType.TIMESTAMP)
   private Date fechaCreacion;
   private Boolean activo;

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "estudio_laboratorio_id", referencedColumnName = "id_estudio_laboratorio")
   private EstudioLaboratorio estudioLaboratorio;

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "consulta_id", referencedColumnName = "id_consulta")
   private Consulta consulta;

   @Override
   public String toString() {
      return "ImagenLaboratorio{" +
         "idImagenLaboratorio=" + idImagenLaboratorio +
         ", contentType='" + contentType + '\'' +
         ", imageName='" + imageName + '\'' +
         ", idPaciente='" + idPaciente + '\'' +
         ", fechaCreacion=" + fechaCreacion + '\''+
         ", activo='" + activo + '\''+
         '}';
   }
}
