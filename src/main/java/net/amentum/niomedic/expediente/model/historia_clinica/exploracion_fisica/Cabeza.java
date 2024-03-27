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
@Table(name = "ef_cabeza", indexes =  {@Index(name = "idx_ef_cabeza_id_hcg", columnList = "id_ef_cabeza,id_historia_clinica")})
public class Cabeza {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ef_cabeza")
    Long idCabeza;
    @NotNull(message = "id de historia clinica general vacio")
    @Column(name = "id_historia_clinica")
    Long idHistoriaClinica;
    String tipo;
    String cueroCabelludo;
    Boolean endomorficos;
    String ojoIzquierdoCejasParpadoPestana;
    String ojoIzquierdoConjuntivas;
    String ojoIzquierdoConjuntivasDescripcion;
    String ojoIzquierdoPupilas;
    String ojoIzquierdoReflejosPupulas;
    String ojoIzquierdoMovimientos;
    String ojoIzquierdoTonoOcular;
    String ojoDerechoCejasParpadoPestana;
    String ojoDerechoConjuntivas;
    String ojoDerechoConjuntivasDescripcion;
    String ojoDerechoPupilas;
    String ojoDerechoReflejosPupulas;
    String ojoDerechoMovimientos;
    String ojoDerechoTonoOcular;
    String narizPermeabilidad;
    Boolean narizSenoFrontal;
    Boolean narizSenoMaxilar;
    String narizSecreciones;
    String narizLesiones;
    String narizMasas;
    String narizCuerposExtranos;
    String oidoPabellonAuricular;
    String oidoConductoAuditivo;
    String oidoConductoAuditivoDescripcion;
    String oidoMembranaTimpanica;
    String oidoMembranaTimpanicaDescripcion;
    Boolean bocaTrismus;
    Boolean bocaHalitosis;
    Boolean bocaMucosa;
    String bocaMucosaTipo;
    Boolean bocaCandidiasis;
    String bocaLengua;
    Boolean bocaOrofaringeDolor;
    Boolean bocaOrofaringeHiperemia;
    Boolean bocaOrofaringeAmigdalas;
    String  bocaOrofaringeAmigdalasTipo;
    Boolean bocaOrofaringeCuerpoExtrano;
    Boolean bocaOrofaringeHemorragia;
    String  bocaDientes;
    Boolean bocaEnciasGingivorragia;
    Boolean bocaEnciasGingivitis;
    String conjuntiva;
    String narinas;
    String orofaringe;
    String oidos;
    String cavidadOral;
    String dentadura;
    String comentarios;
    String pupilas1;
    String pupilas2;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "cabeza")
    ExploracionFisica exploracionFisica;

    @Override
    public String toString() {
        return "Cabeza{" +
                "idCabeza=" + idCabeza +
                ", idHistoriaClinica=" + idHistoriaClinica +
                ", tipo='" + tipo + '\'' +
                ", cueroCabelludo='" + cueroCabelludo + '\'' +
                ", endomorficos=" + endomorficos +
                ", ojoIzquierdoCejasParpadoPestana='" + ojoIzquierdoCejasParpadoPestana + '\'' +
                ", ojoIzquierdoConjuntivas='" + ojoIzquierdoConjuntivas + '\'' +
                ", ojoIzquierdoConjuntivasDescripcion='" + ojoIzquierdoConjuntivasDescripcion + '\'' +
                ", ojoIzquierdoPupilas='" + ojoIzquierdoPupilas + '\'' +
                ", ojoIzquierdoReflejosPupulas='" + ojoIzquierdoReflejosPupulas + '\'' +
                ", ojoIzquierdoMovimientos='" + ojoIzquierdoMovimientos + '\'' +
                ", ojoIzquierdoTonoOcular='" + ojoIzquierdoTonoOcular + '\'' +
                ", ojoDerechoCejasParpadoPestana='" + ojoDerechoCejasParpadoPestana + '\'' +
                ", ojoDerechoConjuntivas='" + ojoDerechoConjuntivas + '\'' +
                ", ojoDerechoConjuntivasDescripcion='" + ojoDerechoConjuntivasDescripcion + '\'' +
                ", ojoDerechoPupilas='" + ojoDerechoPupilas + '\'' +
                ", ojoDerechoReflejosPupulas='" + ojoDerechoReflejosPupulas + '\'' +
                ", ojoDerechoMovimientos='" + ojoDerechoMovimientos + '\'' +
                ", ojoDerechoTonoOcular='" + ojoDerechoTonoOcular + '\'' +
                ", narizPermeabilidad='" + narizPermeabilidad + '\'' +
                ", narizSenoFrontal=" + narizSenoFrontal +
                ", narizSenoMaxilar=" + narizSenoMaxilar +
                ", narizSecreciones='" + narizSecreciones + '\'' +
                ", narizLesiones='" + narizLesiones + '\'' +
                ", narizMasas='" + narizMasas + '\'' +
                ", narizCuerposExtranos='" + narizCuerposExtranos + '\'' +
                ", oidoPabellonAuricular='" + oidoPabellonAuricular + '\'' +
                ", oidoConductoAuditivo='" + oidoConductoAuditivo + '\'' +
                ", oidoConductoAuditivoDescripcion='" + oidoConductoAuditivoDescripcion + '\'' +
                ", oidoMembranaTimpanica='" + oidoMembranaTimpanica + '\'' +
                ", oidoMembranaTimpanicaDescripcion='" + oidoMembranaTimpanicaDescripcion + '\'' +
                ", bocaTrismus=" + bocaTrismus +
                ", bocaHalitosis=" + bocaHalitosis +
                ", bocaMucosa=" + bocaMucosa +
                ", bocaMucosaTipo='" + bocaMucosaTipo + '\'' +
                ", bocaCandidiasis=" + bocaCandidiasis +
                ", bocaLengua='" + bocaLengua + '\'' +
                ", bocaOrofaringeDolor=" + bocaOrofaringeDolor +
                ", bocaOrofaringeHiperemia=" + bocaOrofaringeHiperemia +
                ", bocaOrofaringeAmigdalas=" + bocaOrofaringeAmigdalas +
                ", bocaOrofaringeAmigdalasTipo='" + bocaOrofaringeAmigdalasTipo + '\'' +
                ", bocaOrofaringeCuerpoExtrano=" + bocaOrofaringeCuerpoExtrano +
                ", bocaOrofaringeHemorragia=" + bocaOrofaringeHemorragia +
                ", bocaDientes='" + bocaDientes + '\'' +
                ", bocaEnciasGingivorragia=" + bocaEnciasGingivorragia +
                ", bocaEnciasGingivitis=" + bocaEnciasGingivitis +
                ", conjuntiva='" + conjuntiva + '\'' +
                ", narinas='" + narinas + '\'' +
                ", orofaringe='" + orofaringe + '\'' +
                ", oidos='" + oidos + '\'' +
                ", cavidadOral='" + cavidadOral + '\'' +
                ", dentadura='" + dentadura + '\'' +
                ", comentarios='" + comentarios + '\'' +
                ", pupilas1='" + pupilas1 + '\'' +
                ", pupilas2='" + pupilas2 + '\'' +
                '}';
    }
}
