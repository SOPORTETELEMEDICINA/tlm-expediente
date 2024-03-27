package net.amentum.niomedic.expediente.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "catalogo_preguntas")
public class CatPregunta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pregunta")
    Integer idPregunta;

    @Column(name = "id_cuestionario")
    Integer idCuestionario;

    @NotEmpty(message = "Ingrese la pregunta")
    @Column(name = "pregunta")
    String pregunta;

    @NotNull(message = "El campo no debe ser nulo")
    @Column(name = "active")
    Boolean active = Boolean.TRUE;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_date")
    Date createdDate = new Date();

    @Column(name = "sort")
    Integer sort;

    @Column(name = "tipo_pregunta")
    Integer tipoPregunta;

}
