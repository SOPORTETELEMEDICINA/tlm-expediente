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
@Table(name = "interrogatorio_piel", indexes =  {@Index(name = "idx_interrogatorio_piel_id_hcg", columnList = "id_interrogatorio,id_historia_clinica")})
public class InterrogatorioPiel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_interrogatorio")
    Long idInterrogatorio;

    @NotNull(message = "id de historia clinica general vacio")
    @Column(name = "id_historia_clinica")
    Long idHistoriaClinica;

    String ulceras;
    String ulcerasLocalizacion;
    String ulcerasCausa;
    Boolean anexoAsintomatico;
    Boolean pielManchas;
    String pielManchasLocalizacion;
    String pielTiempoEvol;
    String pielCaracteristicas;
    Boolean nevos;
    String nevosComentarios;
    Boolean nevosEfelides;
    Boolean rash;
    String rashLocalizacion;
    String rashTiempoEvol;
    String rashAgenteCausal;
    Boolean nodulos;
    String nodulosLocalizacion;
    String nodulosTiempoEvol;
    String nodulosCaracteristicas;
    Boolean vesiculas;
    String vesiculasLocalizacion;
    String vesiculasTiempoEvol;
    String vesiculasCaracteristicas;
    Boolean vesiculasVerrugas;
    String vesiculasVerugasTiempoEvol;
    Boolean enrojecimiento;
    String enrojecimientoLocalizacion;
    String enrojecimientoTiempoEvol;
    Boolean urticaria;
    String urticariaCausa;
    String urticariaTiempoEvol;
    Boolean cicatrices;
    String cicatricesLocalizacion;
    String cicatricesCausa;
    Boolean unias;
    Boolean uniasOnicofagia;
    Boolean uniasOnicomicosis;
    Boolean uniasEleva;
    Boolean uniasManchas;
    Boolean uniasGruesas;
    Boolean uniasDelgadas;
    Boolean uniasBuenCrecimiento;
    String uniasComentarios;
    Boolean pelo;
    Boolean peloAbundante;
    Boolean peloPoco;
    Boolean peloGrueso;
    Boolean peloDelgado;
    Boolean peloQuebradizo;
    Boolean peloSeborrea;
    Boolean peloAlopecia;
    Boolean peloCaspa;
    Boolean peloPrurito;
    Boolean peloPediculosis;
    Boolean peloTiniaCabeza;
    String peloComentarios;
    String comentarios;
    Boolean papulas;
    String papulasLocalizacion;
    Boolean ampollas;
    String ampollasLocalizacion;
    Boolean pustula;
    String pustulaLocalizacion;
    Boolean escama;
    String escamaLocalizacion;
    Boolean queratosis;
    String queratosisLocalizacion;
    Boolean tuberculo;
    String tuberculoLocalizacion;
    Boolean placa;
    String placaLocalizacion;
    Boolean costra;
    String costraLocalizacion;
    Boolean escara;
    String escaraLocalizacion;
    Boolean excoriacion;
    String excoriacionLocalizacion;
    Boolean erosion;
    String erosionLocalizacion;
    Boolean fisura;
    String fisuraLocalizacion;
    Boolean hiperpigmentacion;
    String hiperpigmentacionLocalizacion;
    Boolean hipopigmentacion;
    String hipopigmentacionLocalizacion;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "interrogatorioPiel")
    InterrogatorioAparatos interrogatorioAparatos;

    @Override
    public String toString() {
        return "InterrogatorioPiel{" +
                "idInterrogatorio=" + idInterrogatorio +
                ", idHistoriaClinica=" + idHistoriaClinica +
                ", ulceras='" + ulceras + '\'' +
                ", ulcerasLocalizacion='" + ulcerasLocalizacion + '\'' +
                ", ulcerasCausa='" + ulcerasCausa + '\'' +
                ", anexoAsintomatico=" + anexoAsintomatico +
                ", pielManchas=" + pielManchas +
                ", pielManchasLocalizacion='" + pielManchasLocalizacion + '\'' +
                ", pielTiempoEvol='" + pielTiempoEvol + '\'' +
                ", pielCaracteristicas='" + pielCaracteristicas + '\'' +
                ", nevos=" + nevos +
                ", nevosComentarios='" + nevosComentarios + '\'' +
                ", nevosEfelides=" + nevosEfelides +
                ", rash=" + rash +
                ", rashLocalizacion='" + rashLocalizacion + '\'' +
                ", rashTiempoEvol='" + rashTiempoEvol + '\'' +
                ", rashAgenteCausal='" + rashAgenteCausal + '\'' +
                ", nodulos=" + nodulos +
                ", nodulosLocalizacion='" + nodulosLocalizacion + '\'' +
                ", nodulosTiempoEvol='" + nodulosTiempoEvol + '\'' +
                ", nodulosCaracteristicas='" + nodulosCaracteristicas + '\'' +
                ", vesiculas=" + vesiculas +
                ", vesiculasLocalizacion='" + vesiculasLocalizacion + '\'' +
                ", vesiculasTiempoEvol='" + vesiculasTiempoEvol + '\'' +
                ", vesiculasCaracteristicas='" + vesiculasCaracteristicas + '\'' +
                ", vesiculasVerrugas=" + vesiculasVerrugas +
                ", vesiculasVerugasTiempoEvol='" + vesiculasVerugasTiempoEvol + '\'' +
                ", enrojecimiento=" + enrojecimiento +
                ", enrojecimientoLocalizacion='" + enrojecimientoLocalizacion + '\'' +
                ", enrojecimientoTiempoEvol='" + enrojecimientoTiempoEvol + '\'' +
                ", urticaria=" + urticaria +
                ", urticariaCausa='" + urticariaCausa + '\'' +
                ", urticariaTiempoEvol='" + urticariaTiempoEvol + '\'' +
                ", cicatrices=" + cicatrices +
                ", cicatricesLocalizacion='" + cicatricesLocalizacion + '\'' +
                ", cicatricesCausa='" + cicatricesCausa + '\'' +
                ", unias=" + unias +
                ", uniasOnicofagia=" + uniasOnicofagia +
                ", uniasOnicomicosis=" + uniasOnicomicosis +
                ", uniasEleva=" + uniasEleva +
                ", uniasManchas=" + uniasManchas +
                ", uniasGruesas=" + uniasGruesas +
                ", uniasDelgadas=" + uniasDelgadas +
                ", uniasBuenCrecimiento=" + uniasBuenCrecimiento +
                ", uniasComentarios='" + uniasComentarios + '\'' +
                ", pelo=" + pelo +
                ", peloAbundante=" + peloAbundante +
                ", peloPoco=" + peloPoco +
                ", peloGrueso=" + peloGrueso +
                ", peloDelgado=" + peloDelgado +
                ", peloQuebradizo=" + peloQuebradizo +
                ", peloSeborrea=" + peloSeborrea +
                ", peloAlopecia=" + peloAlopecia +
                ", peloCaspa=" + peloCaspa +
                ", peloPrurito=" + peloPrurito +
                ", peloPediculosis=" + peloPediculosis +
                ", peloTiniaCabeza=" + peloTiniaCabeza +
                ", peloComentarios='" + peloComentarios + '\'' +
                ", comentarios='" + comentarios + '\'' +
                ", papulas=" + papulas +
                ", papulasLocalizacion='" + papulasLocalizacion + '\'' +
                ", ampollas=" + ampollas +
                ", ampollasLocalizacion='" + ampollasLocalizacion + '\'' +
                ", pustula=" + pustula +
                ", pustulaLocalizacion='" + pustulaLocalizacion + '\'' +
                ", escama=" + escama +
                ", escamaLocalizacion='" + escamaLocalizacion + '\'' +
                ", queratosis=" + queratosis +
                ", queratosisLocalizacion='" + queratosisLocalizacion + '\'' +
                ", tuberculo=" + tuberculo +
                ", tuberculoLocalizacion='" + tuberculoLocalizacion + '\'' +
                ", placa=" + placa +
                ", placaLocalizacion='" + placaLocalizacion + '\'' +
                ", costra=" + costra +
                ", costraLocalizacion='" + costraLocalizacion + '\'' +
                ", escara=" + escara +
                ", escaraLocalizacion='" + escaraLocalizacion + '\'' +
                ", excoriacion=" + excoriacion +
                ", excoriacionLocalizacion='" + excoriacionLocalizacion + '\'' +
                ", erosion=" + erosion +
                ", erosionLocalizacion='" + erosionLocalizacion + '\'' +
                ", fisura=" + fisura +
                ", fisuraLocalizacion='" + fisuraLocalizacion + '\'' +
                ", hiperpigmentacion=" + hiperpigmentacion +
                ", hiperpigmentacionLocalizacion='" + hiperpigmentacionLocalizacion + '\'' +
                ", hipopigmentacion=" + hipopigmentacion +
                ", hipopigmentacionLocalizacion='" + hipopigmentacionLocalizacion + '\'' +
                '}';
    }
}
