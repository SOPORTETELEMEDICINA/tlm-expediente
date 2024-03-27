package net.amentum.niomedic.expediente.model.historia_clinica.antecendentes_ginecobstetricos;

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
@Table(name = "hc_antecendentes_ginecobstetricos", indexes =  {@Index(name = "idx_antecendentes_ginecobstetricos_id_hcg", columnList = "id_antecendentes_ginecobstetricos,id_historia_clinica")})
public class AntecendentesGinecobstetricos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_antecendentes_ginecobstetricos")
    Long idAntecendentesGinecobstetricos;

    @NotNull(message = "id de historia clinica general vacio")
    @Column(name = "id_historia_clinica")
    Long idHistoriaClinica;

    Boolean asherman;
    Boolean retrodesviacion;
    Boolean cervicitis;
    Boolean polipoEndometrial;
    Boolean adherenciaUterina;
    Boolean periodosIrregulares;
    Boolean periodosRegulares;
    Boolean periodo28dias;
    Boolean periodo30dias;
    String periodoOtro;
    Boolean periodoOtroSi;
    String periodo;
    Boolean prolapsoUterino;
    Boolean ovarios;
    Boolean miomasFibromas;
    Boolean endomeotriosis;
    Boolean hiperplasiaEndometrial;
    String anticonTiempo;
    String etsNombre;
    String etsTratamiento;
    String etsEvolucion;
    String tiempoEvolucionDispareunia;
    String frecuencia;
    Boolean secrecion;
    String secrecionDescripcion;
    String notavidasexual;
    Boolean amonorrea;
    Boolean metorragia;
    Boolean menorragia;
    Boolean dismorrea;
    Boolean olorvaginal;
    Boolean secrecionvaginal;
    String descripcionsecrecion;
    Boolean prurito;
    Boolean resequedadvaginal;
    String duracionsintomas;
    String tratamientovaginal;
    Boolean sindromepremenstrual;
    String menarca;
    String periodoTiempo;
    Date mamografiaFecha;
    Boolean mamografiaResultadoNormal;
    String mamografiaInput;
    String menstracionCantidad;
    Boolean menstracionCantidadNormal;
    Boolean menstracionCantidadHipermenorrea;
    Boolean menstracionCantidadHipomonorrea;
    Boolean menstracionCantidadAmenorrea;
    Boolean menstruacion3Dias;
    Boolean menstruacion4Dias;
    Boolean menstruacion5Dias;
    String menstruacionTiempoOtro;
    String leucorreaCaracteristicas;
    String leucorreaTiempoEvol;
    String leucorreaTratamiento;
    String fumInput;
    Boolean fumDisminorrea;
    String ivsa;
    Boolean ivsaSexualActivo;
    Integer numeroParejas;
    Boolean anticon;
    String  anticonNombre;
    Boolean ets;
    String  etsTipo;
    String  etsTiempoEvol;
    Boolean  dispareunia;
    Boolean dispareuniaInicial;
    Boolean dispareuniaSiempre;
    Boolean dispareuniaSangrado;
    Date papanicolauFecha;
    Boolean papanicolauResultadoNormal;
    String papanicolauInput;
    String metrorragia;
    String doccu;
    Integer gestas;
    Integer cesareas;
    Integer orbitos;
    Integer partos;
    Integer abortos;
    String complicacionesEmbarazo;
    String comentarios;
    String comentariosdoccma;
    String doccma;
    Integer menopausiaEdad;
    Boolean menopausiaBochornos;
    Boolean menopausiaInsomnio;
    Boolean menopausiaDepresion;
    String menopausiaOtros;
    Boolean embarazada;
    String embarazoDescripcion;
    String embarazo1trimestre;
    String embarazo2trimestre;
    String embarazo3trimestre;
    String embarazoParto;
    String embarazoOtros;
    String notas;

    @Override
    public String toString() {
        return "AntecendentesGinecobstetricos{" +
                "idAntecendentesGinecobstetricos=" + idAntecendentesGinecobstetricos +
                ", idHistoriaClinica=" + idHistoriaClinica +
                ", asherman=" + asherman +
                ", retrodesviacion=" + retrodesviacion +
                ", cervicitis=" + cervicitis +
                ", polipoEndometrial=" + polipoEndometrial +
                ", adherenciaUterina=" + adherenciaUterina +
                ", periodosIrregulares=" + periodosIrregulares +
                ", periodosRegulares=" + periodosRegulares +
                ", periodo28dias=" + periodo28dias +
                ", periodo30dias=" + periodo30dias +
                ", periodoOtro='" + periodoOtro + '\'' +
                ", periodoOtroSi=" + periodoOtroSi +
                ", periodo='" + periodo + '\'' +
                ", prolapsoUterino=" + prolapsoUterino +
                ", ovarios=" + ovarios +
                ", miomasFibromas=" + miomasFibromas +
                ", endomeotriosis=" + endomeotriosis +
                ", hiperplasiaEndometrial=" + hiperplasiaEndometrial +
                ", anticonTiempo='" + anticonTiempo + '\'' +
                ", etsNombre='" + etsNombre + '\'' +
                ", etsTratamiento='" + etsTratamiento + '\'' +
                ", etsEvolucion='" + etsEvolucion + '\'' +
                ", tiempoEvolucionDispareunia='" + tiempoEvolucionDispareunia + '\'' +
                ", frecuencia='" + frecuencia + '\'' +
                ", secrecion='" + secrecion + '\'' +
                ", secrecionDescripcion='" + secrecionDescripcion + '\'' +
                ", notavidasexual='" + notavidasexual + '\'' +
                ", amonorrea=" + amonorrea +
                ", metorragia=" + metorragia +
                ", menorragia=" + menorragia +
                ", dismorrea=" + dismorrea +
                ", olorvaginal='" + olorvaginal + '\'' +
                ", secrecionvaginal='" + secrecionvaginal + '\'' +
                ", descripcionsecrecion='" + descripcionsecrecion + '\'' +
                ", prurito='" + prurito + '\'' +
                ", resequedadvaginal='" + resequedadvaginal + '\'' +
                ", duracionsintomas='" + duracionsintomas + '\'' +
                ", tratamientovaginal='" + tratamientovaginal + '\'' +
                ", sindromepremenstrual=" + sindromepremenstrual +
                ", menarca='" + menarca + '\'' +
                ", periodoTiempo='" + periodoTiempo + '\'' +
                ", mamografiaFecha=" + mamografiaFecha +
                ", mamografiaResultadoNormal=" + mamografiaResultadoNormal +
                ", mamografiaInput='" + mamografiaInput + '\'' +
                ", menstracionCantidad='" + menstracionCantidad + '\'' +
                ", menstracionCantidadNormal=" + menstracionCantidadNormal +
                ", menstracionCantidadHipermenorrea=" + menstracionCantidadHipermenorrea +
                ", menstracionCantidadHipomonorrea=" + menstracionCantidadHipomonorrea +
                ", menstracionCantidadAmenorrea=" + menstracionCantidadAmenorrea +
                ", menstruacion3Dias=" + menstruacion3Dias +
                ", menstruacion4Dias=" + menstruacion4Dias +
                ", menstruacion5Dias=" + menstruacion5Dias +
                ", menstruacionTiempoOtro='" + menstruacionTiempoOtro + '\'' +
                ", leucorreaCaracteristicas='" + leucorreaCaracteristicas + '\'' +
                ", leucorreaTiempoEvol='" + leucorreaTiempoEvol + '\'' +
                ", leucorreaTratamiento='" + leucorreaTratamiento + '\'' +
                ", fumInput='" + fumInput + '\'' +
                ", fumDisminorrea=" + fumDisminorrea +
                ", ivsa='" + ivsa + '\'' +
                ", ivsaSexualActivo=" + ivsaSexualActivo +
                ", numeroParejas=" + numeroParejas +
                ", anticon=" + anticon +
                ", anticonNombre='" + anticonNombre + '\'' +
                ", ets=" + ets +
                ", etsTipo='" + etsTipo + '\'' +
                ", etsTiempoEvol='" + etsTiempoEvol + '\'' +
                ", dispareunia='" + dispareunia + '\'' +
                ", dispareuniaInicial=" + dispareuniaInicial +
                ", dispareuniaSiempre=" + dispareuniaSiempre +
                ", dispareuniaSangrado=" + dispareuniaSangrado +
                ", papanicolauFecha=" + papanicolauFecha +
                ", papanicolauResultadoNormal=" + papanicolauResultadoNormal +
                ", papanicolauInput='" + papanicolauInput + '\'' +
                ", metrorragia='" + metrorragia + '\'' +
                ", doccu='" + doccu + '\'' +
                ", gestas=" + gestas +
                ", cesareas=" + cesareas +
                ", orbitos=" + orbitos +
                ", partos=" + partos +
                ", abortos=" + abortos +
                ", complicacionesEmbarazo='" + complicacionesEmbarazo + '\'' +
                ", comentarios='" + comentarios + '\'' +
                ", comentariosdoccma='" + comentariosdoccma + '\'' +
                ", doccma='" + doccma + '\'' +
                ", menopausiaEdad=" + menopausiaEdad +
                ", menopausiaBochornos=" + menopausiaBochornos +
                ", menopausiaInsomnio=" + menopausiaInsomnio +
                ", menopausiaDepresion=" + menopausiaDepresion +
                ", menopausiaOtros='" + menopausiaOtros + '\'' +
                ", embarazada=" + embarazada +
                ", embarazoDescripcion='" + embarazoDescripcion + '\'' +
                ", embarazo1trimestre='" + embarazo1trimestre + '\'' +
                ", embarazo2trimestre='" + embarazo2trimestre + '\'' +
                ", embarazo3trimestre='" + embarazo3trimestre + '\'' +
                ", embarazoParto='" + embarazoParto + '\'' +
                ", embarazoOtros='" + embarazoOtros + '\'' +
                ", notas='" + notas + '\'' +
                '}';
    }
}
