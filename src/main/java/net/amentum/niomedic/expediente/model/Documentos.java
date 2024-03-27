package net.amentum.niomedic.expediente.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "documentos")
public class Documentos implements Serializable {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id_documentos")
   private Long idDocumentos;
   private String contentType;
   private String documentoName;
   private String idPaciente;
   @Temporal(TemporalType.TIMESTAMP)
   private Date fechaCreacion;
   private Boolean activo;

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "padecimiento_id", referencedColumnName = "id_padecimiento")
   private Padecimiento padecimiento;

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "consulta_id", referencedColumnName = "id_consulta")
   private Consulta consulta;


   @Override
   public String toString() {
      return "Documentos{" +
         "idDocumentos=" + idDocumentos +
         ", contentType='" + contentType + '\'' +
         ", documentoName='" + documentoName + '\'' +
         ", idPaciente='" + idPaciente + '\'' +
         ", fechaCreacion=" + fechaCreacion +
         '}';
   }


}
