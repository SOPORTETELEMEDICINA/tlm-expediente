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
@Table(name = "interrogatorio_cardiovascular", indexes =  {@Index(name = "idx_interrogatorio_cardiovascular_id_hcg", columnList = "id_interrogatorio,id_historia_clinica")})
public class InterrogatorioCardiovascular {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_interrogatorio")
    Long idInterrogatorio;

    @NotNull(message = "id de historia clinica general vacio")
    @Column(name = "id_historia_clinica")
    Long idHistoriaClinica;

    Boolean hipertension;
    String hipertensionEvolucion;
    String hipertensionTratamiento;
    Boolean palpitacion;
    String palpiEvolucion;
    Boolean cardioAsintomatico;
    String plapiTratamiento;
    Boolean bradicardia;
    String bradicardiaEvolucion;
    String bradicardiaTratamiento;
    Boolean cardiopatia;
    Boolean diaforesis;
    String cardiopatiaTipo;
    String cardiopatiaEvolucion;
    String cardiopatiaTratamiento;
    Boolean dolorOpresivo;
    Boolean taquicardia;
    String taquicardiaEvolucion;
    String taquicardiaTratamiento;
    Boolean arritmias;
    String arritmiasEvolucion;
    String arritmiasTratamiento;
    Boolean mareos;
    String mareosEvolucion;
    Boolean mareosIncorporarse;
    Boolean mareosFlexionarse;
    Boolean mareosTodoMomento;
    Boolean cefalea;
    Boolean cefaleaGeneral;
    Boolean cefaleaFrontal;
    Boolean cefaleaOccipital;
    Boolean cefaleaBiparietal;
    Boolean cefaleaBitemporal;
    Boolean cefaleaUniDerecha;
    Boolean cefaleaUniIzquierda;
    Boolean disneaGesFuerzos;
    Boolean disneaMesFuerzos;
    Boolean disneaPesFuerzos;
    String disneaEvolucion;
    Boolean disneaOxigenoterapia;
    Boolean sincope;
    String sincopeEvolucion;
    String sincopeTratamiento;
    Boolean cianosisPeribucal;
    Boolean cianosisDistales;
    String petequiasSi;
    String petequias;
    Boolean edema;
    String edemaLocalizacion;
    String edemaEvolucion;
    String edemaTratamiento;
    Boolean soplo;
    String soploEvolucion;
    String soploTipo;
    Boolean hiperlipidemia;
    Boolean hiperTipo;
    Boolean hipercolesterol;
    Boolean hiperglicerol;
    String hiperEvolucion;
    String hiperTratamiento;
    String comentarios;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "interrogatorioCardiovascular")
    InterrogatorioAparatos interrogatorioAparatos;

    @Override
    public String toString() {
        return "InterrogatorioCardiovascular{" +
                "idInterrogatorio=" + idInterrogatorio +
                ", idHistoriaClinica=" + idHistoriaClinica +
                ", hipertension=" + hipertension +
                ", hipertensionEvolucion='" + hipertensionEvolucion + '\'' +
                ", hipertensionTratamiento='" + hipertensionTratamiento + '\'' +
                ", palpitacion=" + palpitacion +
                ", palpiEvolucion='" + palpiEvolucion + '\'' +
                ", cardioAsintomatico=" + cardioAsintomatico +
                ", plapiTratamiento='" + plapiTratamiento + '\'' +
                ", bradicardia=" + bradicardia +
                ", bradicardiaEvolucion='" + bradicardiaEvolucion + '\'' +
                ", bradicardiaTratamiento='" + bradicardiaTratamiento + '\'' +
                ", cardiopatia=" + cardiopatia +
                ", diaforesis=" + diaforesis +
                ", cardiopatiaTipo='" + cardiopatiaTipo + '\'' +
                ", cardiopatiaEvolucion='" + cardiopatiaEvolucion + '\'' +
                ", cardiopatiaTratamiento='" + cardiopatiaTratamiento + '\'' +
                ", dolorOpresivo=" + dolorOpresivo +
                ", taquicardia=" + taquicardia +
                ", taquicardiaEvolucion='" + taquicardiaEvolucion + '\'' +
                ", taquicardiaTratamiento='" + taquicardiaTratamiento + '\'' +
                ", arritmias=" + arritmias +
                ", arritmiasEvolucion='" + arritmiasEvolucion + '\'' +
                ", arritmiasTratamiento='" + arritmiasTratamiento + '\'' +
                ", mareos=" + mareos +
                ", mareosEvolucion='" + mareosEvolucion + '\'' +
                ", mareosIncorporarse=" + mareosIncorporarse +
                ", mareosFlexionarse=" + mareosFlexionarse +
                ", mareosTodoMomento=" + mareosTodoMomento +
                ", cefalea=" + cefalea +
                ", cefaleaGeneral=" + cefaleaGeneral +
                ", cefaleaFrontal=" + cefaleaFrontal +
                ", cefaleaOccipital=" + cefaleaOccipital +
                ", cefaleaBiparietal=" + cefaleaBiparietal +
                ", cefaleaBitemporal=" + cefaleaBitemporal +
                ", cefaleaUniDerecha=" + cefaleaUniDerecha +
                ", cefaleaUniIzquierda=" + cefaleaUniIzquierda +
                ", disneaGesFuerzos=" + disneaGesFuerzos +
                ", disneaMesFuerzos=" + disneaMesFuerzos +
                ", disneaPesFuerzos=" + disneaPesFuerzos +
                ", disneaEvolucion='" + disneaEvolucion + '\'' +
                ", disneaOxigenoterapia=" + disneaOxigenoterapia +
                ", sincope=" + sincope +
                ", sincopeEvolucion='" + sincopeEvolucion + '\'' +
                ", sincopeTratamiento='" + sincopeTratamiento + '\'' +
                ", cianosisPeribucal=" + cianosisPeribucal +
                ", cianosisDistales=" + cianosisDistales +
                ", petequiasSi='" + petequiasSi + '\'' +
                ", petequias='" + petequias + '\'' +
                ", edema=" + edema +
                ", edemaLocalizacion='" + edemaLocalizacion + '\'' +
                ", edemaEvolucion='" + edemaEvolucion + '\'' +
                ", edemaTratamiento='" + edemaTratamiento + '\'' +
                ", soplo=" + soplo +
                ", soploEvolucion='" + soploEvolucion + '\'' +
                ", soploTipo='" + soploTipo + '\'' +
                ", hiperlipidemia=" + hiperlipidemia +
                ", hiperTipo=" + hiperTipo +
                ", hipercolesterol=" + hipercolesterol +
                ", hiperglicerol=" + hiperglicerol +
                ", hiperEvolucion='" + hiperEvolucion + '\'' +
                ", hiperTratamiento='" + hiperTratamiento + '\'' +
                ", comentarios='" + comentarios + '\'' +
                '}';
    }
}
