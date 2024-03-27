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
@Table(name = "interrogatorio_digestivo", indexes =  {@Index(name = "idx_interrogatorio_digestivo_id_hcg", columnList = "id_interrogatorio,id_historia_clinica")})
public class InterrogatorioDigestivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_interrogatorio")
    Long idInterrogatorio;
    @NotNull(message = "id de historia clinica general vacio")
    @Column(name = "id_historia_clinica")
    Long idHistoriaClinica;
    Boolean hipertension;
    Boolean evacuaciones;
    String evacuacionesNumeroConsistencia;
    String evacuacionesCaracteristicas;
    Boolean evacuacuacionesPujo;
    Boolean evacuacionTenesmo;
    Boolean digesAsintomatico;
    Boolean constipacion;
    String constipacionTipo;
    Boolean constipaSiempre;
    Boolean constipaOcasional;
    Boolean constipaRaramente;
    Boolean constipaNunca;
    Boolean flatulenciasSi;
    String flatulencias;
    Boolean dolorAbdo;
    String dolorAbdoLocalizacion;
    String dolorAbdoTiempoEvol;
    Boolean dolorAbdoTipoColico;
    Boolean dolorAbdoTipoLocalizado;
    Boolean dolorAbdoTipoDifuso;
    String dolorAbdoTratamiento;
    String evn;
    Boolean distension;
    Boolean incontinenciaSi;
    Boolean rectorragia;
    Boolean hematoquecia;
    Boolean diarrea;
    String diarreaFrecuencia;
    String diarreaCaracteristicas;
    Boolean mixorrea;
    String mixorreaTipo;
    Boolean mixorreaSiempre;
    Boolean mixorreaOcasional;
    Boolean mixorreaRaramente;
    Boolean mixorreaNunca;
    Boolean nauseas;
    Boolean nauseasPostPrandiales;
    Boolean nauseasPostTrata;
    String nauseasCual;
    String nauseasTiempoEvol;
    Boolean vomito;
    Boolean vomitoPostPrandiales;
    Boolean vomitoPostTratamiento;
    String vomitoCual;
    String vomitoTiempoEvol;
    String vomitoAntiEmetico;
    Boolean reflujo;
    Boolean reflujoContinuo;
    Boolean reflujoIntermitente;
    Boolean reflujoNocturno;
    String reflujoTiempoEvol;
    String reflujoEndoscopia;
    Boolean reflujoTos;
    String reflujoTratamiento;
    Boolean bocaSeca;
    Boolean hematemesis;
    String hematemesisTiempoEvol;
    String hematemesisTrata;
    Boolean episgas;
    String episgasComentarios;
    Boolean epigasPirosis;
    Boolean epigasHipo;
    String epigasTiempoEvol;
    Boolean sialorrea;
    String sialorreaTiempoEvol;
    String sialorreaTrata;
    Boolean xersotomia;
    String xersotomiaTiempoEvol;
    String xerostomiaTrata;
    Boolean hiporexia;
    String hiporexiaTiempoEvol;
    String hiporexiaTrata;
    Boolean hiperfagia;
    String hiperfagiaTiempoEvol;
    String hiperfagiaTrata;
    Boolean melena;
    String melenaTiempoEvol;
    String melenaCaracteristicas;
    Boolean acolia;
    String acoliaTiempoEvol;
    String acoliaCaracteristicas;
    Boolean anorexia;
    String anorexiaTiempoEvol;
    String anorexiaTrata;
    Boolean ictericia;
    String ictericiaTiempoEvol;
    String ictericiaTrata;
    String ictericiaEstudios;
    Boolean pruritoAnal;
    Boolean pruritoAnalParasitos;
    String pruritoTipo;
    String pruritoTiempoEvol;
    String pruritoCaracteristicas;
    Boolean polipos;
    String poliposTiempoEvol;
    String poliposCaracteristicas;
    String poliposTratamiento;
    Boolean colecistitis;
    String colecistitisTiempoEvol;
    String colecistitisCaracteristicas;
    String colecistitisTratamiento;
    Boolean odinofagia;
    String odinofagiaTiempoEvol;
    String odinofagiaTratamiento;
    Boolean anasarca;
    String anasarcaInput;
    Boolean hemorroides;
    String hemorroidesTiempoEvol;
    String hemorroidesCaracteristicas;
    String hemorroidesTratamiento;
    Boolean colelitiasis;
    String colelitiasisTiempoEvol;
    String colelitiasisCaracteristicas;
    String colelitiasisTratamiento;
    Boolean disfagia;
    String disfagiaTiempoEvol;
    String disfagiaTratamiento;
    String comentarios;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "interrogatorioDigestivo")
    InterrogatorioAparatos interrogatorioAparatos;

    @Override
    public String toString() {
        return "InterrogatorioDigestivo{" +
                "idInterrogatorio=" + idInterrogatorio +
                ", idHistoriaClinica=" + idHistoriaClinica +
                ", hipertension=" + hipertension +
                ", evacuaciones=" + evacuaciones +
                ", evacuacionesNumeroConsistencia='" + evacuacionesNumeroConsistencia + '\'' +
                ", evacuacionesCaracteristicas='" + evacuacionesCaracteristicas + '\'' +
                ", evacuacuacionesPujo=" + evacuacuacionesPujo +
                ", evacuacionTenesmo=" + evacuacionTenesmo +
                ", digesAsintomatico=" + digesAsintomatico +
                ", constipacion=" + constipacion +
                ", constipacionTipo='" + constipacionTipo + '\'' +
                ", constipaSiempre=" + constipaSiempre +
                ", constipaOcasional=" + constipaOcasional +
                ", constipaRaramente=" + constipaRaramente +
                ", constipaNunca=" + constipaNunca +
                ", flatulenciasSi=" + flatulenciasSi +
                ", flatulencias='" + flatulencias + '\'' +
                ", dolorAbdo=" + dolorAbdo +
                ", dolorAbdoLocalizacion='" + dolorAbdoLocalizacion + '\'' +
                ", dolorAbdoTiempoEvol='" + dolorAbdoTiempoEvol + '\'' +
                ", dolorAbdoTipoColico=" + dolorAbdoTipoColico +
                ", dolorAbdoTipoLocalizado=" + dolorAbdoTipoLocalizado +
                ", dolorAbdoTipoDifuso=" + dolorAbdoTipoDifuso +
                ", dolorAbdoTratamiento='" + dolorAbdoTratamiento + '\'' +
                ", evn='" + evn + '\'' +
                ", distension=" + distension +
                ", incontinenciaSi=" + incontinenciaSi +
                ", rectorragia=" + rectorragia +
                ", hematoquecia=" + hematoquecia +
                ", diarrea=" + diarrea +
                ", diarreaFrecuencia='" + diarreaFrecuencia + '\'' +
                ", diarreaCaracteristicas='" + diarreaCaracteristicas + '\'' +
                ", mixorrea=" + mixorrea +
                ", mixorreaTipo='" + mixorreaTipo + '\'' +
                ", mixorreaSiempre=" + mixorreaSiempre +
                ", mixorreaOcasional=" + mixorreaOcasional +
                ", mixorreaRaramente=" + mixorreaRaramente +
                ", mixorreaNunca=" + mixorreaNunca +
                ", nauseas=" + nauseas +
                ", nauseasPostPrandiales=" + nauseasPostPrandiales +
                ", nauseasPostTrata=" + nauseasPostTrata +
                ", nauseasCual='" + nauseasCual + '\'' +
                ", nauseasTiempoEvol='" + nauseasTiempoEvol + '\'' +
                ", vomito=" + vomito +
                ", vomitoPostPrandiales=" + vomitoPostPrandiales +
                ", vomitoPostTratamiento=" + vomitoPostTratamiento +
                ", vomitoCual='" + vomitoCual + '\'' +
                ", vomitoTiempoEvol='" + vomitoTiempoEvol + '\'' +
                ", vomitoAntiEmetico='" + vomitoAntiEmetico + '\'' +
                ", reflujo=" + reflujo +
                ", reflujoContinuo=" + reflujoContinuo +
                ", reflujoIntermitente=" + reflujoIntermitente +
                ", reflujoNocturno=" + reflujoNocturno +
                ", reflujoTiempoEvol='" + reflujoTiempoEvol + '\'' +
                ", reflujoEndoscopia='" + reflujoEndoscopia + '\'' +
                ", reflujoTos=" + reflujoTos +
                ", reflujoTratamiento='" + reflujoTratamiento + '\'' +
                ", bocaSeca=" + bocaSeca +
                ", hematemesis=" + hematemesis +
                ", hematemesisTiempoEvol='" + hematemesisTiempoEvol + '\'' +
                ", hematemesisTrata='" + hematemesisTrata + '\'' +
                ", episgas=" + episgas +
                ", episgasComentarios='" + episgasComentarios + '\'' +
                ", epigasPirosis=" + epigasPirosis +
                ", epigasHipo=" + epigasHipo +
                ", epigasTiempoEvol='" + epigasTiempoEvol + '\'' +
                ", sialorrea=" + sialorrea +
                ", sialorreaTiempoEvol='" + sialorreaTiempoEvol + '\'' +
                ", sialorreaTrata='" + sialorreaTrata + '\'' +
                ", xersotomia=" + xersotomia +
                ", xersotomiaTiempoEvol='" + xersotomiaTiempoEvol + '\'' +
                ", xerostomiaTrata='" + xerostomiaTrata + '\'' +
                ", hiporexia=" + hiporexia +
                ", hiporexiaTiempoEvol='" + hiporexiaTiempoEvol + '\'' +
                ", hiporexiaTrata='" + hiporexiaTrata + '\'' +
                ", hiperfagia=" + hiperfagia +
                ", hiperfagiaTiempoEvol='" + hiperfagiaTiempoEvol + '\'' +
                ", hiperfagiaTrata='" + hiperfagiaTrata + '\'' +
                ", melena=" + melena +
                ", melenaTiempoEvol='" + melenaTiempoEvol + '\'' +
                ", melenaCaracteristicas='" + melenaCaracteristicas + '\'' +
                ", acolia=" + acolia +
                ", acoliaTiempoEvol='" + acoliaTiempoEvol + '\'' +
                ", acoliaCaracteristicas='" + acoliaCaracteristicas + '\'' +
                ", anorexia=" + anorexia +
                ", anorexiaTiempoEvol='" + anorexiaTiempoEvol + '\'' +
                ", anorexiaTrata='" + anorexiaTrata + '\'' +
                ", ictericia=" + ictericia +
                ", ictericiaTiempoEvol='" + ictericiaTiempoEvol + '\'' +
                ", ictericiaTrata='" + ictericiaTrata + '\'' +
                ", ictericiaEstudios='" + ictericiaEstudios + '\'' +
                ", pruritoAnal=" + pruritoAnal +
                ", pruritoAnalParasitos='" + pruritoAnalParasitos + '\'' +
                ", pruritoTipo='" + pruritoTipo + '\'' +
                ", pruritoTiempoEvol='" + pruritoTiempoEvol + '\'' +
                ", pruritoCaracteristicas='" + pruritoCaracteristicas + '\'' +
                ", polipos=" + polipos +
                ", poliposTiempoEvol='" + poliposTiempoEvol + '\'' +
                ", poliposCaracteristicas='" + poliposCaracteristicas + '\'' +
                ", poliposTratamiento='" + poliposTratamiento + '\'' +
                ", colecistitis=" + colecistitis +
                ", colecistitisTiempoEvol='" + colecistitisTiempoEvol + '\'' +
                ", colecistitisCaracteristicas='" + colecistitisCaracteristicas + '\'' +
                ", colecistitisTratamiento='" + colecistitisTratamiento + '\'' +
                ", odinofagia=" + odinofagia +
                ", odinofagiaTiempoEvol='" + odinofagiaTiempoEvol + '\'' +
                ", odinofagiaTratamiento='" + odinofagiaTratamiento + '\'' +
                ", anasarca=" + anasarca +
                ", anasarcaInput='" + anasarcaInput + '\'' +
                ", hemorroides=" + hemorroides +
                ", hemorroidesTiempoEvol='" + hemorroidesTiempoEvol + '\'' +
                ", hemorroidesCaracteristicas='" + hemorroidesCaracteristicas + '\'' +
                ", hemorroidesTratamiento='" + hemorroidesTratamiento + '\'' +
                ", colelitiasis=" + colelitiasis +
                ", colelitiasisTiempoEvol='" + colelitiasisTiempoEvol + '\'' +
                ", colelitiasisCaracteristicas='" + colelitiasisCaracteristicas + '\'' +
                ", colelitiasisTratamiento='" + colelitiasisTratamiento + '\'' +
                ", disfagia=" + disfagia +
                ", disfagiaTiempoEvol='" + disfagiaTiempoEvol + '\'' +
                ", disfagiaTratamiento='" + disfagiaTratamiento + '\'' +
                ", comentarios='" + comentarios + '\'' +
                '}';
    }
}
