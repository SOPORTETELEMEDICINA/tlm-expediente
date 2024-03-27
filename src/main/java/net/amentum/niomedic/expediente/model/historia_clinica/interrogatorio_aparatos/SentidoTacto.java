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
@Table(name = "interrogatorio_organo_sentidos_tacto", indexes =  {@Index(name = "idx_interrogatorio_organo_sentidos_tacto_id_hcg", columnList = "id_interrogatorio,id_historia_clinica")})
public class SentidoTacto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_interrogatorio")
    Long idInterrogatorio;
    @NotNull(message = "id de historia clinica general vacio")
    @Column(name = "id_historia_clinica")
    Long idHistoriaClinica;
    Boolean normal;
    Boolean hiposensible;
    String hiposensibleLocalizacion;
    Boolean hipersensible;
    String hipersensibleLocalizacion;
    String localizacion;
    String tiempoEvolucion;
    Boolean esterognosia;
    String esterogInput;
    String comentarios;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "sentidoTacto")
    InterrogatorioAparatos interrogatorioAparatos;

    @Override
    public String toString() {
        return "SentidoTacto{" +
                "idInterrogatorio=" + idInterrogatorio +
                ", idHistoriaClinica=" + idHistoriaClinica +
                ", normal=" + normal +
                ", hiposensible=" + hiposensible +
                ", hiposensibleLocalizacion='" + hiposensibleLocalizacion + '\'' +
                ", hipersensible=" + hipersensible +
                ", hipersensible_localizacion='" + hipersensibleLocalizacion + '\'' +
                ", localizacion='" + localizacion + '\'' +
                ", tiempoEvolucion='" + tiempoEvolucion + '\'' +
                ", esterognosia=" + esterognosia +
                ", esterogInput='" + esterogInput + '\'' +
                ", comentarios='" + comentarios + '\'' +
                '}';
    }
}
