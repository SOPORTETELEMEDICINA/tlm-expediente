package net.amentum.niomedic.expediente.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "datos_clinicos", indexes = {@Index(name = "idx_datos_clinicos_id_paciente", columnList = "id_paciente")})
public class DatosClinicos implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_datos_clinicos")
    private Long idDatosClinicos;
    private String grupoSanguineo;
    private String factorRh;
    private String alergias;
    private String discapacidad;
    private String institucionSalud;
    private String nsss;
    @Column(name = "id_paciente")
    private String idPaciente;
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Temporal(TemporalType.TIMESTAMP)
    private Date ultimaModificacion;
    private String creadoPor;
    private Boolean activo;
}
