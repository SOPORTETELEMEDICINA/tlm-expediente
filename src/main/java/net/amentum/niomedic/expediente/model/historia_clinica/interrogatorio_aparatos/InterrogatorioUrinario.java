package net.amentum.niomedic.expediente.model.historia_clinica.interrogatorio_aparatos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "interrogatorio_urinario", indexes =  {@Index(name = "idx_interrogatorio_urinario_id_hcg", columnList = "id_interrogatorio,id_historia_clinica")})
public class InterrogatorioUrinario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_interrogatorio")
    Long idInterrogatorio;

    @NotNull(message = "id de historia clinica general vacio")
    @Column(name = "id_historia_clinica")
    Long idHistoriaClinica;

    Boolean disuria;
    Boolean hematuria;
    Boolean poliuria;
    Boolean nicturia;
    Boolean incontinencia;
    String comentarios;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "interrogatorioUrinario")
    InterrogatorioAparatos interrogatorioAparatos;

    @Override
    public String toString() {
        return "InterrogatorioUrinario{" +
                "idInterrogatorio=" + idInterrogatorio +
                ", idHistoriaClinica=" + idHistoriaClinica +
                ", disuria=" + disuria +
                ", hematuria=" + hematuria +
                ", poliuria=" + poliuria +
                ", nicturia=" + nicturia +
                ", incontinencia=" + incontinencia +
                ", comentarios='" + comentarios + '\'' +
                '}';
    }
}
