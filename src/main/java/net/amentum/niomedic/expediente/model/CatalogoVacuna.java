package net.amentum.niomedic.expediente.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "catalogo_vacuna")
public class CatalogoVacuna implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_catalogo_vacuna")
    private Long idCatalogoVacuna;
    private String nombreVacuna;
    private String enfermedades;
    private Boolean activo;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.ALL},mappedBy = "catalogoVacunaId")
    private CatalogoDosisVacuna catalogoDosisVacuna;

//    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.ALL},mappedBy = "catalogoVacunaId")
//    private EsquemaVacunacion esquemaVacunacion;


    @Override
    public String toString() {
        return "CatalogoVacuna{" +
                "idCatalogoVacuna=" + idCatalogoVacuna +
                ", nombreVacuna='" + nombreVacuna + '\'' +
                ", enfermedades='" + enfermedades + '\'' +
                ", activo=" + activo +
                '}';
    }
}
