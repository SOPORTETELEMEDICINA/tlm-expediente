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
@Table(name = "ef_genitales", indexes =  {@Index(name = "idx_ef_genitales_id_hcg", columnList = "id_ef_genitales,id_historia_clinica")})
public class Genitales {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ef_genitales")
    Long idGenitales;
    @NotNull(message = "id de historia clinica general vacio")
    @Column(name = "id_historia_clinica")
    Long idHistoriaClinica;
    Boolean secreciones;
    Boolean masas;
    Boolean lesiones;
    String comentarios;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "genitales")
    ExploracionFisica exploracionFisica;

    @Override
    public String toString() {
        return "Genitales{" +
                "idGenitales=" + idGenitales +
                ", idHistoriaClinica=" + idHistoriaClinica +
                ", secreciones=" + secreciones +
                ", masas=" + masas +
                ", lesiones=" + lesiones +
                ", comentarios='" + comentarios + '\'' +
                '}';
    }
}
