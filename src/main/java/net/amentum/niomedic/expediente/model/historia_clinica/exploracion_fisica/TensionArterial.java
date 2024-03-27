package net.amentum.niomedic.expediente.model.historia_clinica.exploracion_fisica;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ef_tension_arterial", indexes =  {@Index(name = "idx_ef_tension_arterial_id_hcg", columnList = "id_ef_tension_arterial,id_historia_clinica")})
public class TensionArterial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ef_tension_arterial")
    Long idTensionArterial;
    @NotNull(message = "id de historia clinica general vacio")
    @Column(name = "id_historia_clinica")
    Long idHistoriaClinica;
    Integer sistolica;
    Integer diastolica;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "tensionArterial")
    ExploracionFisica exploracionFisica;

    @Override
    public String toString() {
        return "TensionArterial{" +
                "idTensionArterial=" + idTensionArterial +
                ", idHistoriaClinica=" + idHistoriaClinica +
                ", sistolica=" + sistolica +
                ", diastolica=" + diastolica +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(idTensionArterial, idHistoriaClinica);
    }
}
