package net.amentum.niomedic.expediente.model.historia_clinica.exploracion_fisica;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ef_abdomen", indexes =  {@Index(name = "idx_ef_abdomen_id_hcg", columnList = "id_ef_abdomen,id_historia_clinica")})
public class Abdomen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ef_abdomen")
    Long idAbdomen;
    @NotNull(message = "id de historia clinica general vacio")
    @Column(name = "id_historia_clinica")
    Long idHistoriaClinica;
    String caracteristicas;
    Boolean ruidosPeristalticos;
    String ruidosPeristalticosTipo;
    String percusion;
    String percusionLocalizacion;
    Boolean palpacionDolor;
    String palpacionDolorLocalizacion;
    Boolean masas;
    Boolean visceromegalias;
    Boolean ascitis;
    String hernias;
    String signos;
    String comentarios;
    String forma;
    String ruidos;
    Boolean dolor;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "abdomen")
    ExploracionFisica exploracionFisica;

    @Override
    public String toString() {
        return "Abdomen{" +
                "idAbdomen=" + idAbdomen +
                ", idHistoriaClinica=" + idHistoriaClinica +
                ", caracteristicas='" + caracteristicas + '\'' +
                ", ruidosPeristalticos=" + ruidosPeristalticos +
                ", ruidosPeristalticosTipo='" + ruidosPeristalticosTipo + '\'' +
                ", percusion='" + percusion + '\'' +
                ", percusionLocalizacion='" + percusionLocalizacion + '\'' +
                ", palpacionDolor=" + palpacionDolor +
                ", palpacionDolorLocalizacion='" + palpacionDolorLocalizacion + '\'' +
                ", masas=" + masas +
                ", visceromegalias=" + visceromegalias +
                ", ascitis=" + ascitis +
                ", hernias='" + hernias + '\'' +
                ", signos='" + signos + '\'' +
                ", comentarios='" + comentarios + '\'' +
                ", forma='" + forma + '\'' +
                ", ruidos='" + ruidos + '\'' +
                ", dolor=" + dolor +
                '}';
    }
}
