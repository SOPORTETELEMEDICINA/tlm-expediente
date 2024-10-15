package net.amentum.niomedic.expediente.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cat_cuestionario_paciente")
public class CatCuestionarioPaciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Integer idCatCuestionarioPaciente;

    @NotNull(message = "Ingrese el id del paciente")
    @Column(name = "id_paciente")
    UUID idPaciente;

    @NotNull(message = "Ingrese el status")
    @Column(name = "status")
    Integer status;

    @NotNull(message = "Ingrese el id del cuestionario")
    @Column(name = "id_cat_cuestionario")
    Integer idCatCuestionario;

}
