package net.amentum.niomedic.expediente.model.historia_clinica.personales_patologicos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pp_padecimiento_personal_patologico", indexes = {@Index(name = "idx_padecimiento_personal_patologico_id_hcg", columnList = "id_padecimiento_personal_patologico,id_historia_clinica")})
public class PadecimientoPersonalPatologico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_padecimiento_personal_patologico")
    Long id;
    @NotNull(message = "id de historia clinica general vacio")
    @Column(name = "id_historia_clinica")
    Long idHistoriaClinica;
    Boolean tieneEnfermedad;
    String enfermedad;
    String tiempoEvolucion;
    String tratamiento;
    String notas;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "personal_patologico_id", referencedColumnName = "id_personal_patologico")
    PersonalesPatologicos personalesPatologicos;

    @Override
    public String toString() {
        return "PadecimientoPersonalPatologico{" +
                "id=" + id +
                ", idHistoriaClinica=" + idHistoriaClinica +
                ", tieneEnfermedad=" + tieneEnfermedad +
                ", enfermedad='" + enfermedad + '\'' +
                ", tiempoEvolucion='" + tiempoEvolucion + '\'' +
                ", tratamiento='" + tratamiento + '\'' +
                ", notas='" + notas + '\'' +
                '}';
    }
}
