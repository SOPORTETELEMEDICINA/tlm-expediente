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
@Table(name = "catalogo_cuestionarios")
public class CatCuestionario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cuestionario")
    Integer idCuestionario;

    @NotEmpty(message = "Ingrese el nombre del cuestionario")
    @Column(name = "nombre")
    String nombre;

    @NotEmpty(message = "Ingrese el nombre del creador")
    @Column(name = "creado_por")
    String creadoPor;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_date")
    Date createdDate = new Date();

    @NotNull(message = "El campo no debe ser nulo")
    @Column(name = "active")
    Boolean active = Boolean.TRUE;

    @Column(name = "sort")
    Integer sort;
}
