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
@Table(name = "catalogo_dosis_vacuna")
public class CatalogoDosisVacuna implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_catalogo_dosis_vacuna")
    private Long idCatalogoDosisVacuna;
    private String dosis;
    private Boolean activo;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinColumn(name = "catalogoVacunaId")
    private CatalogoVacuna catalogoVacunaId;

    @Override
    public String toString() {
        return "CatalogoDosisVacuna{" +
                "idCatalogoDosisVacuna=" + idCatalogoDosisVacuna +
                ", dosis='" + dosis + '\'' +
                ", activo=" + activo +
                '}';
    }
}
