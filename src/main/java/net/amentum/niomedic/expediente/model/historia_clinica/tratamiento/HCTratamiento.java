package net.amentum.niomedic.expediente.model.historia_clinica.tratamiento;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "hc_tratamiento", indexes = {@Index(name = "idx_tratamiento_id_hcg", columnList = "id_tratamiento, id_historia_clinica")})
public class HCTratamiento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tratamiento")
    Long idTratamiento;
    @NotNull(message = "id de historia clinica general vacio")
    @Column(name = "id_historia_clinica")
    Long idHistoriaClinica;
    String comentarios;
    String notas;

    @OneToMany(mappedBy = "tratamientoCie9Id.HCTratamiento", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    List<TratamientoCie9> tratamientoCie9s = new ArrayList<>();

    @Override
    public String toString() {
        return "Tratamiento{" +
                "idTerapeuticaEmpleada=" + idTratamiento +
                ", idHistoriaClinica=" + idHistoriaClinica +
                ", comentarios='" + comentarios + '\'' +
                ", notas='" + notas + '\'' +
                '}';
    }
}
