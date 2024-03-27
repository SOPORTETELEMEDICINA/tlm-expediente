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
@Table(name = "catalogo_institucion_medica")
public class CatalogoInstitucionMedica implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_catalogo_institucion_medica")
    private Long idCatalogoInstitucionMedica;
    private String clave;
    private String claveCorta;
    private String nombre;
    private Boolean activo;
}
