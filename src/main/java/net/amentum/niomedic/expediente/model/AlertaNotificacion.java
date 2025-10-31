package net.amentum.niomedic.expediente.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "alerta_notificaciones")
public class AlertaNotificacion implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_alerta")
    private Long id;

    @Column(name = "id_medico")
    private String idMedico;

    @Column(name = "id_paciente")
    private String idPaciente;

    @Column(name = "tipo_notificacion")
    private String tipoNotificacion;

    @Column(name = "severidad")
    private String severidad;

    @Column(name = "mensaje")
    private String mensaje;

    @Column(name = "fecha_creacion")
    private Timestamp fechaCreacion;

    @Column(name = "estatus")
    private String estatus;

    @Column(name = "id_group")
    private Integer idGroup;

    @PrePersist
    protected void onCreate() {
        if (fechaCreacion == null) {
            fechaCreacion = new Timestamp(System.currentTimeMillis());
        }
        if (estatus == null) {
            estatus = "ACTIVA";
        }
    }
}