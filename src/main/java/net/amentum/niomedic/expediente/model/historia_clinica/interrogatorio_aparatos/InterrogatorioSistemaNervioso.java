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
@Table(name = "interrogatorio_sistema_nervioso", indexes =  {@Index(name = "idx_interrogatorio_sistema_nervioso_id_hcg", columnList = "id_interrogatorio,id_historia_clinica")})
public class InterrogatorioSistemaNervioso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_interrogatorio")
    Long idInterrogatorio;

    @NotNull(message = "id de historia clinica general vacio")
    @Column(name = "id_historia_clinica")
    Long idHistoriaClinica;

    Boolean nervioAsintomatico;
    String suenioCantidad;
    Boolean suenioReparador;
    Boolean somnolencia;
    Boolean epilepsia;
    String insomnioTiempoEvol;
    String insomnioCaracteristicas;
    String insomnioTratamiento;
    String dolorTipoSelect;
    String convulsionesFrecuencia;
    Integer convulsionesNumero;
    String convulsionesOtro;
    String migraniaLocalizacion;
    String migraniaCaracteristicas;
    String migraniaTiempoEvol;
    String migraniaTratamiento;
    String perdidaEquilibrioCaracteristicas;
    String perdidaEquilibrioTiempoEvol;
    String perdidaEquilibrioTratamiento;
    String hiposensibilidadLocalizacion;
    String hiposensibilidadTiempoEvol;
    String alodiniaLocalizacion;
    String alodiniaTiempoEvol;
    String parestesiasLocalizacion;
    String parestesiasTiempoEvol;
    Boolean parkinson;
    String parkinsonCaracteristicas;
    String parkinsonTiempoEvol;
    String parkinsonTratamiento;
    String perdidaConcienciaCaracteristicas;
    String perdidaConcienciaTiempoEvol;
    String perdidaConcienciaCausa;
    String perdidaConcienciaTratamiento;
    Boolean hipersensibilidad;
    String hipersensibilidadLocalizacion;
    String hipersensibilidadTiempoEvol;
    String hiperalgesiaLocalizacion;
    String hiperalgesiaTiempoEvol;
    String perdidaMemoriaCaracteristicas;
    String perdidaMemoriaTiempoEvol;
    String perdidaMemoriaCausa;
    String perdidaMemoriaTratamiento;
    String comentarios;
    Boolean estuporoso;
    Boolean mareos;
    Boolean vertigo;
    Boolean hipersomnia;
    Boolean hiposomnia;
    Boolean cefalea;
    Boolean movimientosInvoluntarios;
    Boolean tics;
    Boolean nistagmo;
    Boolean marchaInestable;
    Boolean temblor;
    Boolean calambresMusculares;
    Boolean debilidadMuscular;
    Boolean hiperestesia;
    Boolean hipoestesia;
    Boolean disestesia;
    Boolean hiperpatia;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "interrogatorioSistemaNervioso")
    InterrogatorioAparatos interrogatorioAparatos;

    @Override
    public String toString() {
        return "InterrogatorioSistemaNervioso{" +
                "idInterrogatorio=" + idInterrogatorio +
                ", idHistoriaClinica=" + idHistoriaClinica +
                ", nervioAsintomatico=" + nervioAsintomatico +
                ", suenioCantidad='" + suenioCantidad + '\'' +
                ", suenioReparador=" + suenioReparador +
                ", somnolencia=" + somnolencia +
                ", epilepsia=" + epilepsia +
                ", insomnioTiempoEvol='" + insomnioTiempoEvol + '\'' +
                ", insomnioCaracteristicas='" + insomnioCaracteristicas + '\'' +
                ", insomnioTratamiento='" + insomnioTratamiento + '\'' +
                ", dolorTipoSelect='" + dolorTipoSelect + '\'' +
                ", convulsionesFrecuencia='" + convulsionesFrecuencia + '\'' +
                ", convulsionesNumero=" + convulsionesNumero +
                ", convulsionesOtro='" + convulsionesOtro + '\'' +
                ", migraniaLocalizacion='" + migraniaLocalizacion + '\'' +
                ", migraniaCaracteristicas='" + migraniaCaracteristicas + '\'' +
                ", migraniaTiempoEvol='" + migraniaTiempoEvol + '\'' +
                ", migraniaTratamiento='" + migraniaTratamiento + '\'' +
                ", perdidaEquilibrioCaracteristicas='" + perdidaEquilibrioCaracteristicas + '\'' +
                ", perdidaEquilibrioTiempoEvol='" + perdidaEquilibrioTiempoEvol + '\'' +
                ", perdidaEquilibrioTratamiento='" + perdidaEquilibrioTratamiento + '\'' +
                ", hiposensibilidadLocalizacion='" + hiposensibilidadLocalizacion + '\'' +
                ", hiposensibilidadTiempoEvol='" + hiposensibilidadTiempoEvol + '\'' +
                ", alodiniaLocalizacion='" + alodiniaLocalizacion + '\'' +
                ", alodiniaTiempoEvol='" + alodiniaTiempoEvol + '\'' +
                ", parestesiasLocalizacion='" + parestesiasLocalizacion + '\'' +
                ", parestesiasTiempoEvol='" + parestesiasTiempoEvol + '\'' +
                ", parkinson=" + parkinson +
                ", parkinsonCaracteristicas='" + parkinsonCaracteristicas + '\'' +
                ", parkinsonTiempoEvol='" + parkinsonTiempoEvol + '\'' +
                ", parkinsonTratamiento='" + parkinsonTratamiento + '\'' +
                ", perdidaConcienciaCaracteristicas='" + perdidaConcienciaCaracteristicas + '\'' +
                ", perdidaConcienciaTiempoEvol='" + perdidaConcienciaTiempoEvol + '\'' +
                ", perdidaConcienciaCausa='" + perdidaConcienciaCausa + '\'' +
                ", perdidaConcienciaTratamiento='" + perdidaConcienciaTratamiento + '\'' +
                ", hipersensibilidad=" + hipersensibilidad +
                ", hipersensibilidadLocalizacion='" + hipersensibilidadLocalizacion + '\'' +
                ", hipersensibilidadTiempoEvol='" + hipersensibilidadTiempoEvol + '\'' +
                ", hiperalgesiaLocalizacion='" + hiperalgesiaLocalizacion + '\'' +
                ", hiperalgesiaTiempoEvol='" + hiperalgesiaTiempoEvol + '\'' +
                ", perdidaMemoriaCaracteristicas='" + perdidaMemoriaCaracteristicas + '\'' +
                ", perdidaMemoriaTiempoEvol='" + perdidaMemoriaTiempoEvol + '\'' +
                ", perdidaMemoriaCausa='" + perdidaMemoriaCausa + '\'' +
                ", perdidaMemoriaTratamiento='" + perdidaMemoriaTratamiento + '\'' +
                ", comentarios='" + comentarios + '\'' +
                ", estuporoso=" + estuporoso +
                ", mareos=" + mareos +
                ", vertigo=" + vertigo +
                ", hipersomnia=" + hipersomnia +
                ", hiposomnia=" + hiposomnia +
                ", cefalea=" + cefalea +
                ", movimientosInvoluntarios=" + movimientosInvoluntarios +
                ", tics=" + tics +
                ", nistagmo=" + nistagmo +
                ", marchaInestable=" + marchaInestable +
                ", temblor=" + temblor +
                ", calambresMusculares=" + calambresMusculares +
                ", debilidadMuscular=" + debilidadMuscular +
                ", hiperestesia=" + hiperestesia +
                ", hipoestesia=" + hipoestesia +
                ", disestesia=" + disestesia +
                ", hiperpatia=" + hiperpatia +
                '}';
    }
}
