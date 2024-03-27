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
@Table(name = "frm_cref")
public class ContraReferencia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;
    String idReferencia;
    @Temporal(TemporalType.TIMESTAMP)
    Date fechaCreacion = new Date();
    String resultadoClinico;
    Integer ingresoDx1;
    Integer ingresoDx2;
    Integer ingresoDx3;
    Integer ingresoDx4;
    Integer ingresoDx5;
    Integer ingresoDx6;
    Integer egresoDx1;
    Integer egresoDx2;
    Integer egresoDx3;
    Integer egresoDx4;
    Integer egresoDx5;
    Integer egresoDx6;
    Integer proced1;
    Integer proced2;
    Integer proced3;
    Integer proced4;
    Integer proced5;
    Integer proced6;
    String manejoPaciente;
    Integer tratamientoConc;
    String cont;
    String consultaSubsecuente;
    String idMedicoCref;
    String unidadMedica;
    Integer regionSanitaria;
}
