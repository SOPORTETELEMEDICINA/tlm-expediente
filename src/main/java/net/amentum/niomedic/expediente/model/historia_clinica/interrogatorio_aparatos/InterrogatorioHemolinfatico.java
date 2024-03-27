package net.amentum.niomedic.expediente.model.historia_clinica.interrogatorio_aparatos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "interrogatorio_hemolinfatico", indexes =  {@Index(name = "idx_interrogatorio_hemolinfatico_id_hcg", columnList = "id_interrogatorio,id_historia_clinica")})
public class InterrogatorioHemolinfatico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_interrogatorio")
    Long idInterrogatorio;

    @NotNull(message = "id de historia clinica general vacio")
    @Column(name = "id_historia_clinica")
    Long idHistoriaClinica;

    Boolean hemoAsintomatico;
    Boolean nodulos;
    String nodLocalizacion;
    Boolean petequias;
    Boolean astenia;
    Boolean adinamia;
    String comentarios;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "interrogatorioHemolinfatico")
    InterrogatorioAparatos interrogatorioAparatos;

    @Override
    public String toString() {
        return "InterrogatorioHemolinfatico{" +
                "idInterrogatorio=" + idInterrogatorio +
                ", idHistoriaClinica=" + idHistoriaClinica +
                ", hemoAsintomatico=" + hemoAsintomatico +
                ", nodulos=" + nodulos +
                ", nodLocalizacion='" + nodLocalizacion + '\'' +
                ", petequias=" + petequias +
                ", astenia=" + astenia +
                ", adinamia=" + adinamia +
                ", comentarios='" + comentarios + '\'' +
                '}';
    }
}
