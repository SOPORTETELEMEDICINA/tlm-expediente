package net.amentum.niomedic.expediente.model.historia_clinica.heredo_familiares;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "hc_heredo_familiares", indexes = {@Index(name = "idx_heredo_familiares_id_hcg", columnList = "id_heredo_familiares,id_historia_clinica")})
public class HeredoFamiliares {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_heredo_familiares")
    Long idHeredoFamiliares;
    @NotNull(message = "id de historia clinica general vacio")
    @Column(name = "id_historia_clinica")
    Long idHistoriaClinica;
    String comentarios;
    String notas;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "heredoFamiliares")
    List<Enfermedades> enfermedadesList;

    @Override
    public String toString() {
        return "HeredoFamiliares{" +
                "idHeredoFamiliares=" + idHeredoFamiliares +
                ", idHistoriaClinica=" + idHistoriaClinica +
                ", comentarios='" + comentarios + '\'' +
                ", notas='" + notas + '\'' +
                '}';
    }
}
