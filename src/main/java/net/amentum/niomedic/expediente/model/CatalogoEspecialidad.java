package net.amentum.niomedic.expediente.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "catalogo_especialidad")
public class CatalogoEspecialidad implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_catalogo_especialidad")
    private Long idCatalogoEspecialidad;
    private Integer claveEspecialidad;
    private String descripcionEspecialidad;
    private Boolean activo;
}
