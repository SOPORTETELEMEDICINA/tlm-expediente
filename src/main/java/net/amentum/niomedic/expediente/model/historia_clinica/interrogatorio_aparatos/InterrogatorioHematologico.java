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
@Table(name = "interrogatorio_hematologico", indexes =  {@Index(name = "idx_interrogatorio_hematologico_id_hcg", columnList = "id_interrogatorio,id_historia_clinica")})
public class InterrogatorioHematologico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_interrogatorio")
    Long idInterrogatorio;

    @NotNull(message = "id de historia clinica general vacio")
    @Column(name = "id_historia_clinica")
    Long idHistoriaClinica;

    Boolean anemia;
    String anemiaTiempoEvol;
    String anemiaCaracteristicas;
    Boolean sangrados;
    String sangradosLocalizacion;
    String sangradosTiempoEvol;
    String sangradosCaracteristicas;
    Boolean coagulos;
    String coagulosTiempoEvol;
    String coagulosCaracteristicas;
    Boolean hematomas;
    String hematomasLocalizacion;
    String hematomasTiempoEvol;
    String hematomasCaracteristicas;
    String comentarios;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "interrogatorioHematologico")
    InterrogatorioAparatos interrogatorioAparatos;

    @Override
    public String toString() {
        return "InterrogatorioHematologico{" +
                "idInterrogatorio=" + idInterrogatorio +
                ", idHistoriaClinica=" + idHistoriaClinica +
                ", anemia=" + anemia +
                ", anemiaTiempoEvol='" + anemiaTiempoEvol + '\'' +
                ", anemiaCaracteristicas='" + anemiaCaracteristicas + '\'' +
                ", sangrados=" + sangrados +
                ", sangradosLocalizacion='" + sangradosLocalizacion + '\'' +
                ", sangradosTiempoEvol='" + sangradosTiempoEvol + '\'' +
                ", sangradosCaracteristicas='" + sangradosCaracteristicas + '\'' +
                ", coagulos=" + coagulos +
                ", coagulosTiempoEvol='" + coagulosTiempoEvol + '\'' +
                ", coagulosCaracteristicas='" + coagulosCaracteristicas + '\'' +
                ", hematomas=" + hematomas +
                ", hematomasLocalizacion='" + hematomasLocalizacion + '\'' +
                ", hematomasTiempoEvol='" + hematomasTiempoEvol + '\'' +
                ", hematomasCaracteristicas='" + hematomasCaracteristicas + '\'' +
                ", comentarios='" + comentarios + '\'' +
                '}';
    }
}
