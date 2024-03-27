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
@Table(name = "ef_extremidades", indexes =  {@Index(name = "idx_ef_extremidades_id_hcg", columnList = "id_ef_extremidades,id_historia_clinica")})
public class Extremidades {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ef_extremidades")
    Long idExtremidades;
    @NotNull(message = "id de historia clinica general vacio")
    @Column(name = "id_historia_clinica")
    Long idHistoriaClinica;
    Boolean toracicosIzquierdo;
    Boolean toracicosDerecho;
    Boolean pelvicosIzquierdo;
    Boolean pelvicosDerecho;
    Boolean simetria;
    Boolean pulsosCarotideoPresente;
    String pulsosCarotideoRitmo ;
    String pulsosCarotideoSimetria;
    String pulsosCarotideoIntensidad;
    Boolean pulsosRadialPresente;
    String pulsosRadialRitmo ;
    String pulsosRadialSimetria;
    String pulsosRadialIntensidad;
    Boolean pulsosBraquialPresente;
    String pulsosBraquialRitmo ;
    String pulsosBraquialSimetria;
    String pulsosBraquialIntensidad;
    Boolean pulsosFemoralPresente;
    String pulsosFemoralRitmo ;
    String pulsosFemoralSimetria;
    String pulsosFemoralIntensidad;
    Boolean pulsosPopitleoPresente;
    String pulsosPopitleoRitmo ;
    String pulsosPopitleoSimetria;
    String pulsosPopitleoIntensidad;
    Boolean pulsosTibialPresente;
    String pulsosTibialRitmo ;
    String pulsosTibialSimetria;
    String pulsosTibialIntensidad;
    Boolean pulsosDorsalisPedisPresente;
    String pulsosDorsalisPedisRitmo ;
    String pulsosDorsalisPedisSimetria;
    String pulsosDorsalisPedisIntensidad;
    String masasPalpables;
    String tonoMuscular;
    Boolean dolor;
    String dolorEvn;
    String dolorLocalizacion;
    String dolorCaraceristicas;
    Boolean crepitacion;
    String crepitacionLocalizacion;
    String deformidades;
    Boolean articulacionesEdema;
    Boolean articulacionesHiperemia;
    String articulacionesMovimientos;
    String articulacionesMovimientosDetalles;
    String fuerza;
    Boolean edema;
    String signosGodet;
    Boolean signosHomans;
    String signosColumnaMusculos;
    Boolean signosColumnaEscoliosis;
    Boolean signosColumnaCifosis;
    Boolean signosColumnaLordosis;
    String estado;
    String edemaLocalizacion;
    String edemaConsistencia;
    String edemaGodette;
    Boolean simetricos;
    Boolean intensidad;
    String comentarios;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "extremidades")
    ExploracionFisica exploracionFisica;

    @Override
    public String toString() {
        return "Extremidades{" +
                "idExtremidades=" + idExtremidades +
                ", idHistoriaClinica=" + idHistoriaClinica +
                ", toracicosIzquierdo=" + toracicosIzquierdo +
                ", toracicosDerecho=" + toracicosDerecho +
                ", pelvicosIzquierdo=" + pelvicosIzquierdo +
                ", pelvicosDerecho=" + pelvicosDerecho +
                ", simetria=" + simetria +
                ", pulsosCarotideoPresente=" + pulsosCarotideoPresente +
                ", pulsosCarotideoRitmo='" + pulsosCarotideoRitmo + '\'' +
                ", pulsosCarotideoSimetria='" + pulsosCarotideoSimetria + '\'' +
                ", pulsosCarotideoIntensidad='" + pulsosCarotideoIntensidad + '\'' +
                ", pulsosRadialPresente=" + pulsosRadialPresente +
                ", pulsosRadialRitmo='" + pulsosRadialRitmo + '\'' +
                ", pulsosRadialSimetria='" + pulsosRadialSimetria + '\'' +
                ", pulsosRadialIntensidad='" + pulsosRadialIntensidad + '\'' +
                ", pulsosBraquialPresente=" + pulsosBraquialPresente +
                ", pulsosBraquialRitmo='" + pulsosBraquialRitmo + '\'' +
                ", pulsosBraquialSimetria='" + pulsosBraquialSimetria + '\'' +
                ", pulsosBraquialIntensidad='" + pulsosBraquialIntensidad + '\'' +
                ", pulsosFemoralPresente=" + pulsosFemoralPresente +
                ", pulsosFemoralRitmo='" + pulsosFemoralRitmo + '\'' +
                ", pulsosFemoralSimetria='" + pulsosFemoralSimetria + '\'' +
                ", pulsosFemoralIntensidad='" + pulsosFemoralIntensidad + '\'' +
                ", pulsosPopitleoPresente=" + pulsosPopitleoPresente +
                ", pulsosPopitleoRitmo='" + pulsosPopitleoRitmo + '\'' +
                ", pulsosPopitleoSimetria='" + pulsosPopitleoSimetria + '\'' +
                ", pulsosPopitleoIntensidad='" + pulsosPopitleoIntensidad + '\'' +
                ", pulsosTibialPresente=" + pulsosTibialPresente +
                ", pulsosTibialRitmo='" + pulsosTibialRitmo + '\'' +
                ", pulsosTibialSimetria='" + pulsosTibialSimetria + '\'' +
                ", pulsosTibialIntensidad='" + pulsosTibialIntensidad + '\'' +
                ", pulsosDorsalisPedisPresente=" + pulsosDorsalisPedisPresente +
                ", pulsosDorsalisPedisRitmo='" + pulsosDorsalisPedisRitmo + '\'' +
                ", pulsosDorsalisPedisSimetria='" + pulsosDorsalisPedisSimetria + '\'' +
                ", pulsosDorsalisPedisIntensidad='" + pulsosDorsalisPedisIntensidad + '\'' +
                ", masasPalpables='" + masasPalpables + '\'' +
                ", tonoMuscular='" + tonoMuscular + '\'' +
                ", dolor=" + dolor +
                ", dolorEvn='" + dolorEvn + '\'' +
                ", dolorLocalizacion='" + dolorLocalizacion + '\'' +
                ", dolorCaraceristicas='" + dolorCaraceristicas + '\'' +
                ", crepitacion=" + crepitacion +
                ", crepitacionLocalizacion='" + crepitacionLocalizacion + '\'' +
                ", deformidades='" + deformidades + '\'' +
                ", articulacionesEdema=" + articulacionesEdema +
                ", articulacionesHiperemia=" + articulacionesHiperemia +
                ", articulacionesMovimientos='" + articulacionesMovimientos + '\'' +
                ", articulacionesMovimientosDetalles='" + articulacionesMovimientosDetalles + '\'' +
                ", fuerza='" + fuerza + '\'' +
                ", edema=" + edema +
                ", signosGodet='" + signosGodet + '\'' +
                ", signosHomans=" + signosHomans +
                ", signosColumnaMusculos='" + signosColumnaMusculos + '\'' +
                ", signosColumnaEscoliosis=" + signosColumnaEscoliosis +
                ", signosColumnaCifosis=" + signosColumnaCifosis +
                ", signosColumnaLordosis=" + signosColumnaLordosis +
                ", estado='" + estado + '\'' +
                ", edemaLocalizacion='" + edemaLocalizacion + '\'' +
                ", edemaConsistencia='" + edemaConsistencia + '\'' +
                ", edemaGodette='" + edemaGodette + '\'' +
                ", simetricos=" + simetricos +
                ", intensidad=" + intensidad +
                ", comentarios='" + comentarios + '\'' +
                '}';
    }
}
