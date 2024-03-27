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
@Table(name = "ef_cuello", indexes =  {@Index(name = "idx_ef_cuello_id_hcg", columnList = "id_ef_cuello,id_historia_clinica")})
public class Cuello {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ef_cuello")
    Long idCuello;
    @NotNull(message = "id de historia clinica general vacio")
    @Column(name = "id_historia_clinica")
    Long idHistoriaClinica;
    Boolean masas;
    Boolean movil;
    Boolean pulso;
    String tiroides;
    String linfaticos;
    String vasos;
    Boolean acantosis;
    String comentarios;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "cuello")
    ExploracionFisica exploracionFisica;

    @Override
    public String toString() {
        return "Cuello{" +
                "idCuello=" + idCuello +
                ", idHistoriaClinica=" + idHistoriaClinica +
                ", masas=" + masas +
                ", movil=" + movil +
                ", pulso=" + pulso +
                ", tiroides='" + tiroides + '\'' +
                ", linfaticos='" + linfaticos + '\'' +
                ", vasos='" + vasos + '\'' +
                ", acantosis=" + acantosis +
                ", comentarios='" + comentarios + '\'' +
                '}';
    }
}
