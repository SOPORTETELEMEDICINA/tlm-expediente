package net.amentum.niomedic.expediente.model.historia_clinica.diagnostico;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "hc_diagnostico", indexes = {@Index(name = "idx_diagnostico_id_hcg", columnList = "id_diagnostico, id_historia_clinica")})
public class Diagnostico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_diagnostico")
    Long idDiagnostico;
    @NotNull(message = "id de historia clinica general vacio")
    @Column(name = "id_historia_clinica")
    Long idHistoriaClinica;
    String comentarios;
    String notas;

    @OneToMany(mappedBy = "diagnosticoCie10Id.diagnostico", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    List<DiagnosticoCie10> diagnosticoCie10List = new ArrayList<>();

    @Override
    public String toString() {
        return "Diagnostico{" +
                "idDiagnosticoHC=" + idDiagnostico +
                ", idHistoriaClinica=" + idHistoriaClinica +
                ", comentarios='" + comentarios + '\'' +
                ", notas='" + notas + '\'' +
                '}';
    }
}
