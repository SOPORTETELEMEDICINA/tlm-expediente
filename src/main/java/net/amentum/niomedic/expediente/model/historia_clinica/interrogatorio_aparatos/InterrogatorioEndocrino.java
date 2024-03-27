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
@Table(name = "interrogatorio_endocrino", indexes =  {@Index(name = "idx_interrogatorio_endocrino_id_hcg", columnList = "id_interrogatorio,id_historia_clinica")})
public class InterrogatorioEndocrino {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_interrogatorio")
    Long idInterrogatorio;

    @NotNull(message = "id de historia clinica general vacio")
    @Column(name = "id_historia_clinica")
    Long idHistoriaClinica;

    Boolean endocriAsintomatico;
    Boolean gananciaPeso;
    Integer gananciaPesoCuanto;
    String gananciaPesoEvol;
    Boolean nerviosis;
    String diabetesTipo;
    String diabetesTiempoEvol;
    String diabetesTratamiento;
    Boolean pielseca;
    String hipertiroiTiempoEvol;
    String hipertiroiCaracteristicas;
    String hipertiroiTratamiento;
    Boolean poliuria;
    String enanismoCaracteristicas;
    Integer enanismoFamiliares;
    Boolean polidipsia;
    String poliuriaTiempoEvol;
    String poliuriaCaracteristicas;
    String poliuriaTratamiento;
    Boolean perdidaPeso1;
    Integer perdidaPesoCuanto;
    String perdidaPesoEvol;
    String intoleranciaFrio;
    String intoleranciaCalor;
    Boolean acumulacionGrasaDorsocervical;
    Boolean temblor;
    String hipotiroiTiempoEvol;
    String hipotiroiCaracteristicas;
    String hipotiroiTratamiento;
    Boolean acromegalia;
    String acromegaliaTiempoEvol;
    String acromegaliaCaracteristicas;
    String acromegaliaTratamiento;
    Boolean polifagia;
    String hipofisiariosTipo;
    String hipofisiariosTiempoEvol;
    String hipofisiariosCaracteristicas;
    String hipofisiariosTratamiento;
    String comentarios;
    Boolean convulsiones;
    Boolean epilepsia;
    Boolean migrania;
    Boolean perdidaEquilibrio;
    Boolean perdidaConciencia;
    Boolean perdidaMemoria;
    Boolean parkinson;
    Boolean demencia;
    Boolean hiposensibilidad;
    Boolean hipersensibilidad;
    Boolean alodinia;
    Boolean hiperalgesia;
    Boolean parestesias;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "interrogatorioEndocrino")
    InterrogatorioAparatos interrogatorioAparatos;

    @Override
    public String toString() {
        return "InterrogatorioEndocrino{" +
                "idInterrogatorio=" + idInterrogatorio +
                ", idHistoriaClinica=" + idHistoriaClinica +
                ", endocriAsintomatico=" + endocriAsintomatico +
                ", gananciaPeso=" + gananciaPeso +
                ", gananciaPesoCuanto=" + gananciaPesoCuanto +
                ", gananciaPesoEvol='" + gananciaPesoEvol + '\'' +
                ", nerviosis=" + nerviosis +
                ", diabetesTipo='" + diabetesTipo + '\'' +
                ", diabetesTiempoEvol='" + diabetesTiempoEvol + '\'' +
                ", diabetesTratamiento='" + diabetesTratamiento + '\'' +
                ", pielseca=" + pielseca +
                ", hipertiroiTiempoEvol='" + hipertiroiTiempoEvol + '\'' +
                ", hipertiroiCaracteristicas='" + hipertiroiCaracteristicas + '\'' +
                ", hipertiroiTratamiento='" + hipertiroiTratamiento + '\'' +
                ", poliuria=" + poliuria +
                ", enanismoCaracteristicas='" + enanismoCaracteristicas + '\'' +
                ", enanismoFamiliares=" + enanismoFamiliares +
                ", polidipsia=" + polidipsia +
                ", poliuriaTiempoEvol='" + poliuriaTiempoEvol + '\'' +
                ", poliuriaCaracteristicas='" + poliuriaCaracteristicas + '\'' +
                ", poliuriaTratamiento='" + poliuriaTratamiento + '\'' +
                ", perdidaPeso1=" + perdidaPeso1 +
                ", perdidaPesoCuanto=" + perdidaPesoCuanto +
                ", perdidaPesoEvol='" + perdidaPesoEvol + '\'' +
                ", intoleranciaFrio='" + intoleranciaFrio + '\'' +
                ", intoleranciaCalor='" + intoleranciaCalor + '\'' +
                ", acumulacionGrasaDorsocervical=" + acumulacionGrasaDorsocervical +
                ", temblor=" + temblor +
                ", hipotiroiTiempoEvol='" + hipotiroiTiempoEvol + '\'' +
                ", hipotiroiCaracteristicas='" + hipotiroiCaracteristicas + '\'' +
                ", hipotiroiTratamiento='" + hipotiroiTratamiento + '\'' +
                ", acromegalia=" + acromegalia +
                ", acromegaliaTiempoEvol='" + acromegaliaTiempoEvol + '\'' +
                ", acromegaliaCaracteristicas='" + acromegaliaCaracteristicas + '\'' +
                ", acromegaliaTratamiento='" + acromegaliaTratamiento + '\'' +
                ", polifagia=" + polifagia +
                ", hipofisiariosTipo='" + hipofisiariosTipo + '\'' +
                ", hipofisiariosTiempoEvol='" + hipofisiariosTiempoEvol + '\'' +
                ", hipofisiariosCaracteristicas='" + hipofisiariosCaracteristicas + '\'' +
                ", hipofisiariosTratamiento='" + hipofisiariosTratamiento + '\'' +
                ", comentarios='" + comentarios + '\'' +
                ", convulsiones=" + convulsiones +
                ", epilepsia=" + epilepsia +
                ", migrania=" + migrania +
                ", perdidaEquilibrio=" + perdidaEquilibrio +
                ", perdidaConciencia=" + perdidaConciencia +
                ", perdidaMemoria=" + perdidaMemoria +
                ", parkinson=" + parkinson +
                ", demencia=" + demencia +
                ", hiposensibilidad=" + hiposensibilidad +
                ", hipersensibilidad=" + hipersensibilidad +
                ", alodinia=" + alodinia +
                ", hiperalgesia=" + hiperalgesia +
                ", parestesias=" + parestesias +
                '}';
    }
}
