package net.amentum.niomedic.expediente.model.historia_clinica.estudio_laboratorio;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "hc_tipo_estudio_laboratorio", indexes = {@Index(name = "idx_tipo_estudio_laboratorio_id_hcg",
        columnList = "id_tipo_estudio_lab, id_historia_clinica, id_estudio_laboratorio")})
public class HCEstudioLabTipo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_estudio_lab")
    Long idTipoEstudio;
    @NotNull(message = "id de historia clinica general vacio")
    @Column(name = "id_historia_clinica")
    Long idHistoriaClinica;
    @NotNull(message = "id estudio laboratorio vacio")
    @Column(name = "id_estudio_laboratorio")
    Long idEstudioLaboratorio;
    String tipo;
    String descripcion;
    String comentarios;
}
