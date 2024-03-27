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
@Table(name = "hc_exploracion_fisica", indexes =  {@Index(name = "idx_exploracion_fisica_id_hcg", columnList = "id_exploracion_fisica,id_historia_clinica")})
public class ExploracionFisica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_exploracion_fisica")
    Long idExploracionFisica;
    @NotNull(message = "id de historia clinica general vacio")
    @Column(name = "id_historia_clinica")
    Long idHistoriaClinica;
    Integer peso;
    Integer talla;
    Double temperatura;
    Double imc;
    Double frecuenciaCardiaca;
    Double frecuenciaRespiratoria;
    Integer saturacionOxigeno;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ef_tension_arterial")
    TensionArterial tensionArterial;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ef_estado_consciencia")
    EstadoConciencia estadoConciencia;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ef_habitus_exterior")
    HabitusExterior habitusExterior;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ef_sistema_nervioso")
    SistemaNervioso sistemaNervioso;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ef_cabeza")
    Cabeza cabeza;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ef_cuello")
    Cuello cuello;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ef_torax")
    Torax torax;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ef_abdomen")
    Abdomen abdomen;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ef_piel")
    Piel piel;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ef_genitales")
    Genitales genitales;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ef_extremidades")
    Extremidades extremidades;

    String notas;

    @Override
    public String toString() {
        return "ExploracionFisica{" +
                "idExploracionFisica=" + idExploracionFisica +
                ", idHistoriaClinica=" + idHistoriaClinica +
                ", peso=" + peso +
                ", talla=" + talla +
                ", temperatura=" + temperatura +
                ", imc=" + imc +
                ", frecuenciaCardiaca=" + frecuenciaCardiaca +
                ", frecuenciaRespiratoria=" + frecuenciaRespiratoria +
                ", saturacionOxigeno=" + saturacionOxigeno +
                ", notas='" + notas + '\'' +
                '}';
    }
}
