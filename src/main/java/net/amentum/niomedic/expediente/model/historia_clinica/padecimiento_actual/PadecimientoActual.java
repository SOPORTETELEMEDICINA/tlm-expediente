package net.amentum.niomedic.expediente.model.historia_clinica.padecimiento_actual;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "hc_padecimiento_actual", indexes = {@Index(name = "idx_padecimiento_actual_id_hcg", columnList = "id_padecimiento_actual,id_historia_clinica")})
public class PadecimientoActual {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_padecimiento_actual")
    Long idPadecimientoActual;
    @NotNull(message = "id de historia clinica general vacio")
    @Column(name = "id_historia_clinica")
    Long idHistoriaClinica;
    String motivoConsulta;
    String descripcionPadecimiento;
    Boolean esVistoOtroMedico;
    String medicamentoUsado;
    String notas;

    @Override
    public String toString() {
        return "PadecimientoActual{" +
                "idPadecimientoActual=" + idPadecimientoActual +
                ", idHistoriaClinica=" + idHistoriaClinica +
                ", motivoConsulta='" + motivoConsulta + '\'' +
                ", descripcionPadecimiento='" + descripcionPadecimiento + '\'' +
                ", esVistoOtroMedico=" + esVistoOtroMedico +
                ", medicamentoUsado='" + medicamentoUsado + '\'' +
                ", notas='" + notas + '\'' +
                '}';
    }
}
