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
@Table(name = "interrogatorio_organo_sentidos_gusto", indexes =  {@Index(name = "idx_interrogatorio_organo_sentidos_gusto_id_hcg", columnList = "id_interrogatorio,id_historia_clinica")})
public class SentidoGusto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_interrogatorio")
    Long idInterrogatorio;
    @NotNull(message = "id de historia clinica general vacio")
    @Column(name = "id_historia_clinica")
    Long idHistoriaClinica;
    Boolean normal;
    Boolean ardosa;
    Boolean aftas;
    Boolean ageusia;
    Boolean hipoageusia;
    Boolean halitosis;
    Boolean gingivitis;
    Boolean gingivorrea;
    Boolean gingivorragia;
    Boolean glositis;
    String tiempoEvolucion;
    String comentarios;
    Boolean alterado;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "sentidoGusto")
    InterrogatorioAparatos interrogatorioAparatos;

    @Override
    public String toString() {
        return "SentidoGusto{" +
                "idInterrogatorio=" + idInterrogatorio +
                ", idHistoriaClinica=" + idHistoriaClinica +
                ", normal=" + normal +
                ", ardosa=" + ardosa +
                ", aftas=" + aftas +
                ", ageusia=" + ageusia +
                ", hipoageusia=" + hipoageusia +
                ", halitosis=" + halitosis +
                ", gingivitis=" + gingivitis +
                ", gingivorrea=" + gingivorrea +
                ", gingivorragia=" + gingivorragia +
                ", glositis=" + glositis +
                ", tiempoEvolucion='" + tiempoEvolucion + '\'' +
                ", comentarios='" + comentarios + '\'' +
                ", alterado='" + alterado + '\'' +
                '}';
    }
}
