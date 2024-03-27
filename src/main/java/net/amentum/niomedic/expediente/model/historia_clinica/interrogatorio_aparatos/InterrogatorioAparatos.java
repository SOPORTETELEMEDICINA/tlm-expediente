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
@Table(name = "interrogatorio_aparatos", indexes =  {@Index(name = "idx_interrogatorio_aparatos_id_hcg", columnList = "id_interrogatorio,id_historia_clinica")})
public class InterrogatorioAparatos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_interrogatorio")
    Long idInterrogatorio;

    @NotNull(message = "id de historia clinica general vacio")
    @Column(name = "id_historia_clinica")
    Long idHistoriaClinica;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_interrogatorio_sentido_vision")
    SentidoVision sentidoVision;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_interrogatorio_sentido_audicion")
    SentidoAudicion sentidoAudicion;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_interrogatorio_sentido_olfato")
    SentidoOlfato sentidoOlfato;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_interrogatorio_sentido_gusto")
    SentidoGusto sentidoGusto;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_interrogatorio_sentido_tacto")
    SentidoTacto sentidoTacto;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_interrogatorio_respiratorio")
    InterrogatorioRespiratorio interrogatorioRespiratorio;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_interrogatorio_cardiovascular")
    InterrogatorioCardiovascular interrogatorioCardiovascular;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_interrogatorio_digestivo")
    InterrogatorioDigestivo interrogatorioDigestivo;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_interrogatorio_endocrino")
    InterrogatorioEndocrino interrogatorioEndocrino;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_interrogatorio_sistema_nervioso")
    InterrogatorioSistemaNervioso interrogatorioSistemaNervioso;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_interrogatorio_piel_anexos")
    InterrogatorioPiel interrogatorioPiel;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_interrogatorio_psiquiatrico")
    InterrogatorioPsiquiatrico interrogatorioPsiquiatrico;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_interrogatorio_genitourinario")
    InterrogatorioGenitourinario interrogatorioGenitourinario;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_interrogatorio_urinario")
    InterrogatorioUrinario interrogatorioUrinario;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_interrogatorio_reproductor")
    InterrogatorioReproductor interrogatorioReproductor;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_interrogatorio_hemolinfatico")
    InterrogatorioHemolinfatico interrogatorioHemolinfatico;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_interrogatorio_musculo_esqueletico")
    InterrogatorioMusculoEsqueletico interrogatorioMusculoEsqueletico;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_interrogatorio_oncologico")
    InterrogatorioOncologico interrogatorioOncologico;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_interrogatorio_hematologico")
    InterrogatorioHematologico interrogatorioHematologico;

    String notas;

    @Override
    public String toString() {
        return "InterrogatorioAparatos{" +
                "idInterrogatorio=" + idInterrogatorio +
                ", idHistoriaClinica=" + idHistoriaClinica +
                ", notas='" + notas + '\'' +
                '}';
    }
}
