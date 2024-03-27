package net.amentum.niomedic.expediente.model.historia_clinica.interrogatorio_aparatos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "interrogatorio_genitourinario", indexes =  {@Index(name = "idx_interrogatorio_genitourinario_id_hcg", columnList = "id_interrogatorio,id_historia_clinica")})
public class InterrogatorioGenitourinario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_interrogatorio")
    Long idInterrogatorio;

    @NotNull(message = "id de historia clinica general vacio")
    @Column(name = "id_historia_clinica")
    Long idHistoriaClinica;

    Boolean genitoAsintomatico;
    Boolean pujo;
    Boolean pujoMiccional;
    Boolean pujoTerminal;
    Boolean disuria;
    Boolean poliaquiria;
    Boolean incontinencia;
    String incontinenciaTipo;
    String incontinenciaTiempoEvol;
    String incontinenciaTratamiento;
    Boolean colicoRenoureteral;
    String colicoRenoureteralEvn;
    Boolean dolorRenal;
    String dolorRenalCaracteristicas;
    String dolorRenalTiempoEvol;
    String dolorRenalTratamiento;
    String dolorRenalEva;
    Boolean chorroInterrumpido;
    Boolean vaciamientoIncompleto;
    Boolean retencionUrinaria;
    String ritmoMiccional;
    String calibreDescripcion;
    Boolean prostatitis;
    String prostatitisCaracteristicas;
    String prostatitisTiempoEvol;
    String prostatitisTratamiento;
    Boolean incontinenciaHombre;
    String incontinenciaHombreTipo;
    String incontinenciaHombreTiempo;
    String incontinenciaHombreTratamiento;
    Boolean impotenciaHombre;
    String impotenciaHombreTipo;
    String impotenciaHombreTiempo;
    String impotenciaHombreTratamiento;
    Boolean criptorquidia;
    String criptorquidiaCausa;
    String criptorquidiaTiempoEvol;
    Boolean priapismoSi;
    String  priapismo;
    Boolean tactoRectal;
    Date tactoRectalFecha;
    Boolean tactoRectalNormal;
    Boolean tactoRectalAnormal;
    String tactoRectalDescripcion;
    Boolean precoz;
    String precozTiempoEvol;
    String precozOtros;
    Boolean antigeno;
    Date antigenoFecha;
    Boolean antigenoResultadoNormal;
    Boolean antigenoResultadoAnormal;
    String antigenoDescripcion;
    Boolean polaquiuria;
    Boolean nicturia;
    Boolean urgenciaUrinaria;
    Boolean tenesmoVesical;
    Boolean incontinenciaUrgencia;
    Boolean chorroDebil;
    Boolean vacilacion;
    Boolean chorroIntermitente;
    Boolean goteoPostmiccional;
    Boolean retencionOrina;
    Boolean incontinenciaRebosamiento;
    String coloracion;
    Boolean hematuria;
    String olor;
    Boolean sedimento;
    String hombresEreccionDisfuncion;
    String hombresEreccionEvolucion;
    String hombresEreccionCalidad;
    String hombresEreccionMasturbacion;
    Boolean hombresEyaculacionDolor;
    Boolean hombresEyaculacionSangrado;
    Boolean hombresEyaculacionAneyaculacion;
    Boolean hombresEyaculacionRetardada;
    Boolean hombresEyaculacionPrecoz;
    String hombresEyaculacionOtro;
    String hombresLibido;
    Boolean hombresLesionesVerrugas;
    Boolean hombresLesionesUlceras;
    Boolean hombresLesionesVesiculas;
    Boolean hombresLesionesLunares;
    Boolean hombresLesionesCurvaturas;
    String comentarios;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "interrogatorioGenitourinario")
    InterrogatorioAparatos interrogatorioAparatos;

    @Override
    public String toString() {
        return "InterrogatorioGenitourinario{" +
                "idInterrogatorio=" + idInterrogatorio +
                ", idHistoriaClinica=" + idHistoriaClinica +
                ", genitoAsintomatico=" + genitoAsintomatico +
                ", pujo=" + pujo +
                ", pujoMiccional=" + pujoMiccional +
                ", pujoTerminal=" + pujoTerminal +
                ", disuria=" + disuria +
                ", poliaquiria=" + poliaquiria +
                ", incontinencia=" + incontinencia +
                ", incontinenciaTipo='" + incontinenciaTipo + '\'' +
                ", incontinenciaTiempoEvol='" + incontinenciaTiempoEvol + '\'' +
                ", incontinenciaTratamiento='" + incontinenciaTratamiento + '\'' +
                ", colicoRenoureteral=" + colicoRenoureteral +
                ", colicoRenoureteralEvn='" + colicoRenoureteralEvn + '\'' +
                ", dolorRenal=" + dolorRenal +
                ", dolorRenalCaracteristicas='" + dolorRenalCaracteristicas + '\'' +
                ", dolorRenalTiempoEvol='" + dolorRenalTiempoEvol + '\'' +
                ", dolorRenalTratamiento='" + dolorRenalTratamiento + '\'' +
                ", dolorRenalEva='" + dolorRenalEva + '\'' +
                ", chorroInterrumpido=" + chorroInterrumpido +
                ", vaciamientoIncompleto=" + vaciamientoIncompleto +
                ", retencionUrinaria=" + retencionUrinaria +
                ", ritmoMiccional='" + ritmoMiccional + '\'' +
                ", calibreDescripcion='" + calibreDescripcion + '\'' +
                ", prostatitis=" + prostatitis +
                ", prostatitisCaracteristicas='" + prostatitisCaracteristicas + '\'' +
                ", prostatitisTiempoEvol='" + prostatitisTiempoEvol + '\'' +
                ", prostatitisTratamiento='" + prostatitisTratamiento + '\'' +
                ", incontinenciaHombre=" + incontinenciaHombre +
                ", incontinenciaHombreTipo='" + incontinenciaHombreTipo + '\'' +
                ", incontinenciaHombreTiempo='" + incontinenciaHombreTiempo + '\'' +
                ", incontinenciaHombreTratamiento='" + incontinenciaHombreTratamiento + '\'' +
                ", impotenciaHombre=" + impotenciaHombre +
                ", impotenciaHombreTipo='" + impotenciaHombreTipo + '\'' +
                ", impotenciaHombreTiempo='" + impotenciaHombreTiempo + '\'' +
                ", impotenciaHombreTratamiento='" + impotenciaHombreTratamiento + '\'' +
                ", criptorquidia=" + criptorquidia +
                ", criptorquidiaCausa='" + criptorquidiaCausa + '\'' +
                ", criptorquidiaTiempoEvol='" + criptorquidiaTiempoEvol + '\'' +
                ", priapismoSi=" + priapismoSi +
                ", priapismo='" + priapismo + '\'' +
                ", tactoRectal=" + tactoRectal +
                ", tactoRectalFecha=" + tactoRectalFecha +
                ", tactoRectalNormal=" + tactoRectalNormal +
                ", tactoRectalAnormal=" + tactoRectalAnormal +
                ", tactoRectalDescripcion='" + tactoRectalDescripcion + '\'' +
                ", precoz=" + precoz +
                ", precozTiempoEvol='" + precozTiempoEvol + '\'' +
                ", precozOtros='" + precozOtros + '\'' +
                ", antigeno=" + antigeno +
                ", antigenoFecha=" + antigenoFecha +
                ", antigenoResultadoNormal=" + antigenoResultadoNormal +
                ", antigenoResultadoAnormal=" + antigenoResultadoAnormal +
                ", antigenoDescripcion='" + antigenoDescripcion + '\'' +
                ", polaquiuria=" + polaquiuria +
                ", nicturia=" + nicturia +
                ", urgenciaUrinaria=" + urgenciaUrinaria +
                ", tenesmoVesical=" + tenesmoVesical +
                ", incontinenciaUrgencia=" + incontinenciaUrgencia +
                ", chorroDebil=" + chorroDebil +
                ", vacilacion=" + vacilacion +
                ", chorroIntermitente=" + chorroIntermitente +
                ", goteoPostmiccional=" + goteoPostmiccional +
                ", retencionOrina=" + retencionOrina +
                ", incontinenciaRebosamiento=" + incontinenciaRebosamiento +
                ", coloracion='" + coloracion + '\'' +
                ", hematuria=" + hematuria +
                ", olor='" + olor + '\'' +
                ", sedimento=" + sedimento +
                ", hombresEreccionDisfuncion='" + hombresEreccionDisfuncion + '\'' +
                ", hombresEreccionEvolucion='" + hombresEreccionEvolucion + '\'' +
                ", hombresEreccionCalidad='" + hombresEreccionCalidad + '\'' +
                ", hombresEreccionMasturbacion='" + hombresEreccionMasturbacion + '\'' +
                ", hombresEyaculacionDolor=" + hombresEyaculacionDolor +
                ", hombresEyaculacionSangrado=" + hombresEyaculacionSangrado +
                ", hombresEyaculacionAneyaculacion=" + hombresEyaculacionAneyaculacion +
                ", hombresEyaculacionRetardada=" + hombresEyaculacionRetardada +
                ", hombresEyaculacionPrecoz=" + hombresEyaculacionPrecoz +
                ", hombresEyaculacionOtro='" + hombresEyaculacionOtro + '\'' +
                ", hombresLibido='" + hombresLibido + '\'' +
                ", hombresLesionesVerrugas=" + hombresLesionesVerrugas +
                ", hombresLesionesUlceras=" + hombresLesionesUlceras +
                ", hombresLesionesVesiculas=" + hombresLesionesVesiculas +
                ", hombresLesionesLunares=" + hombresLesionesLunares +
                ", hombresLesionesCurvaturas=" + hombresLesionesCurvaturas +
                ", comentarios='" + comentarios + '\'' +
                '}';
    }
}
