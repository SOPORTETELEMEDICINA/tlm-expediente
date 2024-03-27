package net.amentum.niomedic.expediente.model.referecia_vs_referencia;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "frm_ref")
public class Referencia {

    static final Integer TYPE_REFERENCIA = 1;
    static final Integer TYPE_INTERCONSULTA = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;
    String folio;
    Integer vez;
    String umRefClues;
    Integer regionSanitaria;
    @Temporal(TemporalType.TIMESTAMP)
    Date fechaCreacion = new Date();
    String idPaciente;
    Integer dx1;
    Integer dx2;
    Integer dx3;
    Integer dx4;
    Integer dx5;
    Integer dx6;
    Integer urgente;
    String unidadMedica;
    Integer espReq;
    //medico que recibe la referencia/interconsulta
    String idMedicoRecibe;
    String refSamu;
    @Temporal(TemporalType.TIMESTAMP)
    Date refSamuFecha;
    String refMotivo;
    String refResClin;
    Integer refSvTaSys;
    Integer refSvTaDia;
    Double refSvTemp;
    Integer refSvFr;
    Integer refSvFc;
    Integer refSvPeso;
    Integer refSvTalla;
    //medico que crea la referencia/interconsulta
    String idMedicoCrea;
    Integer estado;
    Integer activo;
    Integer tipo = TYPE_REFERENCIA;

    public void setInterconsulta() {
        tipo = TYPE_INTERCONSULTA;
    }

    public void setReferencia() {
        tipo = TYPE_REFERENCIA;
    }
}
