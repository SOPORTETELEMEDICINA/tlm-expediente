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
@Table(name = "interrogatorio_reproductor", indexes =  {@Index(name = "idx_interrogatorio_reproductor_id_hcg", columnList = "id_interrogatorio,id_historia_clinica")})
public class InterrogatorioReproductor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_interrogatorio")
    Long idInterrogatorio;

    @NotNull(message = "id de historia clinica general vacio")
    @Column(name = "id_historia_clinica")
    Long idHistoriaClinica;

    Boolean desechos;
    Boolean dolorGenital;
    Boolean poliuria;
    String comentarios;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "interrogatorioReproductor")
    InterrogatorioAparatos interrogatorioAparatos;

    @Override
    public String toString() {
        return "InterrogatorioReproductor{" +
                "idInterrogatorio=" + idInterrogatorio +
                ", idHistoriaClinica=" + idHistoriaClinica +
                ", desechos=" + desechos +
                ", dolorGenital=" + dolorGenital +
                ", poliuria=" + poliuria +
                ", comentarios='" + comentarios + '\'' +
                '}';
    }
}
