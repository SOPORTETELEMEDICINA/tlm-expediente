package net.amentum.niomedic.expediente.model;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cuestionario_respuesta")
public class CuestionarioRespuesta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_respuesta")
    Integer idRespuesta;

    @Column(name = "id_cuestionario")
    Integer idCuestionario;

    @Column(name = "creado_por")
    String creadoPor;

    @Type( type = "json" )
    @Column(columnDefinition = "json")
    private JsonNode json;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_date")
    Date createdDate = new Date();

    @Column(name = "id_paciente")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    String idPaciente;

}
