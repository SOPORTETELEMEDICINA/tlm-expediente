package net.amentum.niomedic.expediente.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
//import net.amentum.niomedic.expediente.model.json.Hcg;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "historia_clinica_general", indexes = {@Index(name = "idx_historia_clinica_general_id_paciente", columnList = "id_paciente")})
public class HistoriaClinicaGeneral implements Serializable {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id_historia_clinica_general")
   private Long idHistoriaClinicaGeneral;
   @Column(name = "id_paciente", unique = true)
   private UUID idPaciente;
   @Temporal(TemporalType.TIMESTAMP)
   private Date fechaCreacion;
   private Float version;
   @Temporal(TemporalType.TIMESTAMP)
   private Date vigencia;
   @Temporal(TemporalType.TIMESTAMP)
   private Date ultimaModificacion;
   private Boolean activo;
   @Size(max = 50)
   private String creadoPor;
   @Column(columnDefinition = "TEXT")
   private String hcg;
   private Integer idGroup;
}
