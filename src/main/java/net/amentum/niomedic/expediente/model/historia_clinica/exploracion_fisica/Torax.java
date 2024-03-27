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
@Table(name = "ef_torax", indexes =  {@Index(name = "idx_ef_torax_id_hcg", columnList = "id_ef_torax,id_historia_clinica")})
public class Torax {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ef_torax")
    Long idTorax;
    @NotNull(message = "id de historia clinica general vacio")
    @Column(name = "id_historia_clinica")
    Long idHistoriaClinica;
    String torax;
    String respiracion;
    String ganglios;
    Boolean ruidosCardiacos;
    String ruidosCardiacosOtros;
    String mamasSimetria;
    Boolean mamasGinecomastia;
    String mamasMasas;
    String mamasSecreciones;
    String mamasOtros;
    String murmulloVesicular;
    String murmulloVesicularLocalizacion;
    String ruidosSoplos;
    String ruidosSoplosLocalizacion;
    String ruidosEstertores;
    String ruidosEstertoresLocalizacion;
    String posteriorPulmonaresAlteraciones;
    Boolean musculosParavertebralesAtroficos;
    Boolean musculosParavertebralesDolor;
    Boolean escoliosis;
    Boolean cifosis;
    Boolean lordosis;
    String ruidosRespiratorios;
    String cardiacos;
    String comentarios;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "torax")
    ExploracionFisica exploracionFisica;

    @Override
    public String toString() {
        return "Torax{" +
                "idTorax=" + idTorax +
                ", idHistoriaClinica=" + idHistoriaClinica +
                ", torax='" + torax + '\'' +
                ", respiracion='" + respiracion + '\'' +
                ", ganglios='" + ganglios + '\'' +
                ", ruidosCardiacos=" + ruidosCardiacos +
                ", ruidosCardiacosOtros='" + ruidosCardiacosOtros + '\'' +
                ", mamasSimetria='" + mamasSimetria + '\'' +
                ", mamasGinecomastia=" + mamasGinecomastia +
                ", mamasMasas='" + mamasMasas + '\'' +
                ", mamasSecreciones='" + mamasSecreciones + '\'' +
                ", mamasOtros='" + mamasOtros + '\'' +
                ", murmulloVesicular='" + murmulloVesicular + '\'' +
                ", murmulloVesicularLocalizacion='" + murmulloVesicularLocalizacion + '\'' +
                ", ruidosSoplos='" + ruidosSoplos + '\'' +
                ", ruidosSoplosLocalizacion='" + ruidosSoplosLocalizacion + '\'' +
                ", ruidosEstertores='" + ruidosEstertores + '\'' +
                ", ruidosEstertoresLocalizacion='" + ruidosEstertoresLocalizacion + '\'' +
                ", posteriorPulmonaresAlteraciones='" + posteriorPulmonaresAlteraciones + '\'' +
                ", musculosParavertebralesAtroficos=" + musculosParavertebralesAtroficos +
                ", musculosParavertebralesDolor=" + musculosParavertebralesDolor +
                ", escoliosis=" + escoliosis +
                ", cifosis=" + cifosis +
                ", lordosis=" + lordosis +
                ", ruidosRespiratorios='" + ruidosRespiratorios + '\'' +
                ", cardiacos='" + cardiacos + '\'' +
                ", comentarios='" + comentarios + '\'' +
                '}';
    }
}
