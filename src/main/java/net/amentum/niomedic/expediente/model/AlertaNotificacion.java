package net.amentum.niomedic.expediente.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "alerta_notificaciones")
public class AlertaNotificacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_alerta")
    private Long id;

    @Column(name = "id_medico", nullable = false)
    private UUID idMedico;

    @Column(name = "id_paciente", nullable = false)
    private UUID idPaciente;

    @Column(name = "tipo_notificacion", nullable = false, length = 30)
    private String tipoNotificacion;

    @Column(name = "severidad", nullable = false, length = 30)
    private String severidad;

    @Column(name = "mensaje", nullable = false)
    private String mensaje;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @Column(name = "estatus", nullable = false, length = 15)
    private String estatus = "ACTIVA";

    @Column(name = "id_group")
    private Integer idGroup;

    public Long getId() { return id; }
    public UUID getIdMedico() { return idMedico; }
    public void setIdMedico(UUID idMedico) { this.idMedico = idMedico; }
    public UUID getIdPaciente() { return idPaciente; }
    public void setIdPaciente(UUID idPaciente) { this.idPaciente = idPaciente; }
    public String getTipoNotificacion() { return tipoNotificacion; }
    public void setTipoNotificacion(String tipoNotificacion) { this.tipoNotificacion = tipoNotificacion; }
    public String getSeveridad() { return severidad; }
    public void setSeveridad(String severidad) { this.severidad = severidad; }
    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public String getEstatus() { return estatus; }
    public void setEstatus(String estatus) { this.estatus = estatus; }
    public Integer getIdGroup() { return idGroup; }
    public void setIdGroup(Integer idGroup) { this.idGroup = idGroup; }
}
