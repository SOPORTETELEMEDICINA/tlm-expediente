package net.amentum.niomedic.expediente.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "catalogo_drogas")
public class CatalogoDrogas implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_catalogo_drogas")
    private Long idCatalogoDrogas;
    private String nombreDroga;
    private String unidadMedida;
    private Boolean activo;
    //relaciones
//  @OneToMany(cascade = CascadeType.ALL, mappedBy = "catalogoDrogas")
//  private Collection<ConsumoPaciente> consumoPacienteList = new ArrayList<>();
}