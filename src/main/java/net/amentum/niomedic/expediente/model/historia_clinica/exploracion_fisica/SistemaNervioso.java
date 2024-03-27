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
@Table(name = "ef_sistema_nervioso", indexes =  {@Index(name = "idx_ef_sistema_nervioso_id_hcg", columnList = "id_ef_sistema_nervioso,id_historia_clinica")})
public class SistemaNervioso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ef_sistema_nervioso")
    Long idSistemaNervioso;
    @NotNull(message = "id de historia clinica general vacio")
    @Column(name = "id_historia_clinica")
    Long idHistoriaClinica;
    Boolean orientadoenTiempo;
    Boolean orientadoenEspacio;
    Boolean orientadoenPersona;
    String fuerzaMuscularBrazoIzq;
    String fuerzaMuscularBrazoDer;
    String fuerzaMuscularPiernaIzq;
    String fuerzaMuscularPiernaDer;
    String tonoMuscular;
    String reflejoOsteoBrazoizqEstado;
    String reflejoOsteoBrazoizqComentario;
    String reflejoOsteoBrazoderEstado;
    String reflejoOsteoBrazoderComentario;
    String reflejoOsteoPiernaizqEstado;
    String reflejoOsteoPiernaizqComentario;
    String reflejoOsteoPiernaderEstado;
    String reflejoOsteoPiernaderComentario;
    String marcha;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "sistemaNervioso")
    ExploracionFisica exploracionFisica;

    @Override
    public String toString() {
        return "SistemaNervioso{" +
                "idSistemaNervioso=" + idSistemaNervioso +
                ", idHistoriaClinica=" + idHistoriaClinica +
                ", orientadoenTiempo=" + orientadoenTiempo +
                ", orientadoenEspacio=" + orientadoenEspacio +
                ", orientadoenPersona=" + orientadoenPersona +
                ", fuerzaMuscularBrazoIzq='" + fuerzaMuscularBrazoIzq + '\'' +
                ", fuerzaMuscularBrazoDer='" + fuerzaMuscularBrazoDer + '\'' +
                ", fuerzaMuscularPiernaIzq='" + fuerzaMuscularPiernaIzq + '\'' +
                ", fuerzaMuscularPiernaDer='" + fuerzaMuscularPiernaDer + '\'' +
                ", tonoMuscular='" + tonoMuscular + '\'' +
                ", reflejoOsteoBrazoizqEstado='" + reflejoOsteoBrazoizqEstado + '\'' +
                ", reflejoOsteoBrazoizqComentario='" + reflejoOsteoBrazoizqComentario + '\'' +
                ", reflejoOsteoBrazoderEstado='" + reflejoOsteoBrazoderEstado + '\'' +
                ", reflejoOsteoBrazoderComentario='" + reflejoOsteoBrazoderComentario + '\'' +
                ", reflejoOsteoPiernaizqEstado='" + reflejoOsteoPiernaizqEstado + '\'' +
                ", reflejoOsteoPiernaizqComentario='" + reflejoOsteoPiernaizqComentario + '\'' +
                ", reflejoOsteoPiernaderEstado='" + reflejoOsteoPiernaderEstado + '\'' +
                ", reflejoOsteoPiernaderComentario='" + reflejoOsteoPiernaderComentario + '\'' +
                ", marcha='" + marcha + '\'' +
                '}';
    }
}
