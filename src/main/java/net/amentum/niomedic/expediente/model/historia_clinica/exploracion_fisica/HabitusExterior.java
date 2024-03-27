package net.amentum.niomedic.expediente.model.historia_clinica.exploracion_fisica;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ef_habitus_exterior", indexes =  {@Index(name = "idx_ef_habitus_exterior_id_hcg", columnList = "id_ef_habitus_exterior,id_historia_clinica")})
public class HabitusExterior {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ef_habitus_exterior")
    Long idHabitusExterior;
    @NotNull(message = "id de historia clinica general vacio")
    @Column(name = "id_historia_clinica")
    Long idHistoriaClinica;
    String habitus;
    String comentarios;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "habitusExterior")
    ExploracionFisica exploracionFisica;

    @Override
    public String toString() {
        return "HabitusExterior{" +
                "idHabitusExterior=" + idHabitusExterior +
                ", idHistoriaClinica=" + idHistoriaClinica +
                ", habitus='" + habitus + '\'' +
                ", comentarios='" + comentarios + '\'' +
                '}';
    }
}
