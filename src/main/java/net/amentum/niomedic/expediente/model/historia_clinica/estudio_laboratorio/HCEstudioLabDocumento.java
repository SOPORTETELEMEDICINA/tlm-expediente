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
@Table(name = "hc_estudio_laboratorio_documento", indexes = {@Index(name = "idx_estudio_laboratorio_documento_id_hcg",
        columnList = "id_documento, id_historia_clinica, id_estudio_laboratorio")})
public class HCEstudioLabDocumento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_documento")
    Long idDocumento;
    @NotNull(message = "id de historia clinica general vacio")
    @Column(name = "id_historia_clinica")
    Long idHistoriaClinica;
    @NotNull(message = "id estudio laboratorio vacio")
    @Column(name = "id_estudio_laboratorio")
    Long idEstudioLaboratorio;
    String contentType;
    String nameFile;
    @Column(name = "file_base_64")
    byte[] fileBase64;
}