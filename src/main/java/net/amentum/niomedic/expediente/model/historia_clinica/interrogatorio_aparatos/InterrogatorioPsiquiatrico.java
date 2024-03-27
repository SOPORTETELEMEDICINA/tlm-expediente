package net.amentum.niomedic.expediente.model.historia_clinica.interrogatorio_aparatos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.amentum.niomedic.expediente.model.historia_clinica.interrogatorio_aparatos.InterrogatorioAparatos;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "interrogatorio_psiquiatrico", indexes =  {@Index(name = "idx_interrogatorio_psiquiatrico_id_hcg", columnList = "id_interrogatorio,id_historia_clinica")})
public class InterrogatorioPsiquiatrico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_interrogatorio")
    Long idInterrogatorio;

    @NotNull(message = "id de historia clinica general vacio")
    @Column(name = "id_historia_clinica")
    Long idHistoriaClinica;

    Boolean psiquiAsintomatico;
    Boolean depresion;
    String depresionTiempoEvol;
    String depresionCaracteristicas;
    String depresionTratamiento;
    Boolean trastornosAlimentacion;
    Boolean ingestaCompulsiva;
    String ingestaCompulsivaTiempoEvol;
    String ingestaCompulsivaCaracteristicas;
    String ingestaCompulsivaTratamiento;
    Boolean trastornoAlimentacionOtro;
    String trastornoAlimentacionOtroDescripcion;
    String trastornoAlimentacionOtroTiempoEvol;
    String trastornoAlimentacionOtroCaracteristicas;
    String trastornoAlimentacionOtroTratamiento;
    Boolean bulimia;
    String bulimiaTiempoEvol;
    String bulimiaCaracteristicas;
    String bulimiaTratamiento;
    Boolean perdidaInteresSex;
    String perdidaInteresSexTiempoEvol;
    String perdidaInteresSexCaracteristicas;
    String perdidaInteresSexTratamiento;
    Boolean muySensible;
    String muySensibleTiempoEvol;
    String muySensibleCaracteristicas;
    String muySensibleTratamiento;
    Boolean anorexiaNerviosa;
    String anorexiaNerviosaTiempoEvol;
    String anorexiaNerviosaCaracteristicas;
    String anorexiaNerviosaTratamiento;
    Boolean ansiedad;
    String ansiedadTiempoEvol;
    String ansiedadCaracteristicas;
    String ansiedadTratamiento;
    Boolean llantoFacil;
    String llantoFacilTiempoEvol;
    String llantoFacilCaracteristicas;
    String llantoFacilTratamiento;
    Boolean irritabilidad;
    Boolean irritabilidadAlto;
    Boolean irritabilidadMedio;
    Boolean irritabilidadBajo;
    Boolean pensamientosSuicidas;
    String pensamientosSuicidasTiempoEvol;
    String pensamientosSuicidasCaracteristicas;
    String pensamientosSuicidasTratamiento;
    Boolean pobreConcentra;
    Boolean alucinaciones;
    String alucinacionesTipo;
    String alucinacionesTiempoEvol;
    String alucinacionesCaracteristicas;
    String alucinacionesTratamiento;
    Boolean estres;
    Boolean estresAlto;
    Boolean estresMedio;
    Boolean estresBajo;
    Boolean pensamientoApresu;
    String pensamientoApresuTiempoEvol;
    String pensamientoApresuCaracteristicas;
    Boolean paranoia;
    String paranoiaTiempoEvol;
    String paranoiaCaracteristicas;
    String paranoiaTratamiento;
    Boolean cambiosAnimoSi;
    String cambiosAnimo;
    Boolean cambiosAnimoNegado;
    Boolean comportamientoArriesgadoSi;
    Boolean fobias;
    String fobiasTipo;
    String fobiasTiempoEvol;
    Boolean psicosis;
    String psicosisTipo;
    String psicosisTiempoEvol;
    String psicosisCaracteristicas;
    String psicosisTratamiento;
    String comentarios;
    Boolean panico;
    Boolean mania;
    Boolean apatia;
    Boolean trastornosComportamiento;
    Boolean hiperactividad;
    Boolean hiposomnia;
    Boolean hipersomnia;
    Boolean parasomnia;
    Boolean delirio;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "interrogatorioPsiquiatrico")
    InterrogatorioAparatos interrogatorioAparatos;

    @Override
    public String toString() {
        return "InterrogatorioPsiquiatrico{" +
                "idInterrogatorio=" + idInterrogatorio +
                ", idHistoriaClinica=" + idHistoriaClinica +
                ", psiquiAsintomatico=" + psiquiAsintomatico +
                ", depresion=" + depresion +
                ", depresionTiempoEvol='" + depresionTiempoEvol + '\'' +
                ", depresionCaracteristicas='" + depresionCaracteristicas + '\'' +
                ", depresionTratamiento='" + depresionTratamiento + '\'' +
                ", trastornosAlimentacion=" + trastornosAlimentacion +
                ", ingestaCompulsiva=" + ingestaCompulsiva +
                ", ingestaCompulsivaTiempoEvol='" + ingestaCompulsivaTiempoEvol + '\'' +
                ", ingestaCompulsivaCaracteristicas='" + ingestaCompulsivaCaracteristicas + '\'' +
                ", ingestaCompulsivaTratamiento='" + ingestaCompulsivaTratamiento + '\'' +
                ", trastornoAlimentacionOtro=" + trastornoAlimentacionOtro +
                ", trastornoAlimentacionOtroDescripcion='" + trastornoAlimentacionOtroDescripcion + '\'' +
                ", trastornoAlimentacionOtroTiempoEvol='" + trastornoAlimentacionOtroTiempoEvol + '\'' +
                ", trastornoAlimentacionOtroCaracteristicas='" + trastornoAlimentacionOtroCaracteristicas + '\'' +
                ", trastornoAlimentacionOtroTratamiento='" + trastornoAlimentacionOtroTratamiento + '\'' +
                ", bulimia=" + bulimia +
                ", bulimiaTiempoEvol='" + bulimiaTiempoEvol + '\'' +
                ", bulimiaCaracteristicas='" + bulimiaCaracteristicas + '\'' +
                ", bulimiaTratamiento='" + bulimiaTratamiento + '\'' +
                ", perdidaInteresSex=" + perdidaInteresSex +
                ", perdidaInteresSexTiempoEvol='" + perdidaInteresSexTiempoEvol + '\'' +
                ", perdidaInteresSexCaracteristicas='" + perdidaInteresSexCaracteristicas + '\'' +
                ", perdidaInteresSexTratamiento='" + perdidaInteresSexTratamiento + '\'' +
                ", muySensible=" + muySensible +
                ", muySensibleTiempoEvol='" + muySensibleTiempoEvol + '\'' +
                ", muySensibleCaracteristicas='" + muySensibleCaracteristicas + '\'' +
                ", muySensibleTratamiento='" + muySensibleTratamiento + '\'' +
                ", anorexiaNerviosa=" + anorexiaNerviosa +
                ", anorexiaNerviosaTiempoEvol='" + anorexiaNerviosaTiempoEvol + '\'' +
                ", anorexiaNerviosaCaracteristicas='" + anorexiaNerviosaCaracteristicas + '\'' +
                ", anorexiaNerviosaTratamiento='" + anorexiaNerviosaTratamiento + '\'' +
                ", ansiedad=" + ansiedad +
                ", ansiedadTiempoEvol='" + ansiedadTiempoEvol + '\'' +
                ", ansiedadCaracteristicas='" + ansiedadCaracteristicas + '\'' +
                ", ansiedadTratamiento='" + ansiedadTratamiento + '\'' +
                ", llantoFacil=" + llantoFacil +
                ", llantoFacilTiempoEvol='" + llantoFacilTiempoEvol + '\'' +
                ", llantoFacilCaracteristicas='" + llantoFacilCaracteristicas + '\'' +
                ", llantoFacilTratamiento='" + llantoFacilTratamiento + '\'' +
                ", irritabilidad=" + irritabilidad +
                ", irritabilidadAlto=" + irritabilidadAlto +
                ", irritabilidadMedio=" + irritabilidadMedio +
                ", irritabilidadBajo=" + irritabilidadBajo +
                ", pensamientosSuicidas=" + pensamientosSuicidas +
                ", pensamientosSuicidasTiempoEvol='" + pensamientosSuicidasTiempoEvol + '\'' +
                ", pensamientosSuicidasCaracteristicas='" + pensamientosSuicidasCaracteristicas + '\'' +
                ", pensamientosSuicidasTratamiento='" + pensamientosSuicidasTratamiento + '\'' +
                ", pobreConcentra=" + pobreConcentra +
                ", alucinaciones=" + alucinaciones +
                ", alucinacionesTipo='" + alucinacionesTipo + '\'' +
                ", alucinacionesTiempoEvol='" + alucinacionesTiempoEvol + '\'' +
                ", alucinacionesCaracteristicas='" + alucinacionesCaracteristicas + '\'' +
                ", alucinacionesTratamiento='" + alucinacionesTratamiento + '\'' +
                ", estres=" + estres +
                ", estresAlto=" + estresAlto +
                ", estresMedio=" + estresMedio +
                ", estresBajo=" + estresBajo +
                ", pensamientoApresu=" + pensamientoApresu +
                ", pensamientoApresuTiempoEvol='" + pensamientoApresuTiempoEvol + '\'' +
                ", pensamientoApresuCaracteristicas='" + pensamientoApresuCaracteristicas + '\'' +
                ", paranoia=" + paranoia +
                ", paranoiaTiempoEvol='" + paranoiaTiempoEvol + '\'' +
                ", paranoiaCaracteristicas='" + paranoiaCaracteristicas + '\'' +
                ", paranoiaTratamiento='" + paranoiaTratamiento + '\'' +
                ", cambiosAnimoSi=" + cambiosAnimoSi +
                ", cambiosAnimo='" + cambiosAnimo + '\'' +
                ", cambiosAnimoNegado=" + cambiosAnimoNegado +
                ", comportamientoArriesgadoSi=" + comportamientoArriesgadoSi +
                ", fobias=" + fobias +
                ", fobiasTipo='" + fobiasTipo + '\'' +
                ", fobiasTiempoEvol='" + fobiasTiempoEvol + '\'' +
                ", psicosis=" + psicosis +
                ", psicosisTipo='" + psicosisTipo + '\'' +
                ", psicosisTiempoEvol='" + psicosisTiempoEvol + '\'' +
                ", psicosisCaracteristicas='" + psicosisCaracteristicas + '\'' +
                ", psicosisTratamiento='" + psicosisTratamiento + '\'' +
                ", comentarios='" + comentarios + '\'' +
                ", panico=" + panico +
                ", mania=" + mania +
                ", apatia=" + apatia +
                ", trastornosComportamiento=" + trastornosComportamiento +
                ", hiperactividad=" + hiperactividad +
                ", hiposomnia=" + hiposomnia +
                ", hipersomnia=" + hipersomnia +
                ", parasomnia=" + parasomnia +
                ", delirio=" + delirio +
                '}';
    }
}
