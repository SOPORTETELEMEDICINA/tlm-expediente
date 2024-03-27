package net.amentum.niomedic.expediente.model.historia_clinica.personales_no_patologicos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pnp_vivienda", indexes = {@Index(name = "idx_pnp_vivienda_id_hcg", columnList = "id_vivienda,id_historia_clinica")})
public class Vivienda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_vivienda")
    Long idVivienda;
    @NotNull(message = "id de historia clinica general vacio")
    @Column(name = "id_historia_clinica")
    Long idHistoriaClinica;
    Boolean servicioAgua;
    Boolean servicioElectricidad;
    Boolean servicioRecoleccionBasura;
    Boolean servicioAlcantarillado;
    Boolean pisoTierra;
    Boolean pisoAzulejo;
    Boolean pisoCemento;
    Boolean pisoOtro;
    Boolean techoLadrillo;
    Boolean techoEnjarradas;
    Boolean techoOtro;
    Boolean sinBanio;
    Boolean banio1;
    Boolean banio2;
    Boolean banioMas;
    Boolean habitaciones1;
    Boolean habitaciones2;
    Boolean habitacionesMas;
    Boolean cocinaConGas;
    Boolean cocinaConLenia;
    Boolean cocinaConCarbon;
    Boolean productosToxicos;
    String productosToxicosInput;
    Boolean faunaNociva;
    Boolean faunaAranias;
    Boolean faunasAlacranes;
    Boolean faunaSerpientes;
    Boolean faunaOtro;
    String faunaOtroInput;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "vivienda")
    PersonalesNoPatologicos personalesNoPatologicos;

    @Override
    public String toString() {
        return "Vivienda{" +
                "idVivienda=" + idVivienda +
                ", idHistoriaClinica=" + idHistoriaClinica +
                ", servicioAgua=" + servicioAgua +
                ", servicioElectricidad=" + servicioElectricidad +
                ", servicioRecoleccionBasura=" + servicioRecoleccionBasura +
                ", servicioAlcantarillado=" + servicioAlcantarillado +
                ", pisoTierra=" + pisoTierra +
                ", pisoAzulejo=" + pisoAzulejo +
                ", pisoCemento=" + pisoCemento +
                ", pisoOtro=" + pisoOtro +
                ", techoLadrillo=" + techoLadrillo +
                ", techoEnjarradas=" + techoEnjarradas +
                ", techoOtro=" + techoOtro +
                ", sinBanio=" + sinBanio +
                ", banio1=" + banio1 +
                ", banio2=" + banio2 +
                ", banioMas=" + banioMas +
                ", habitaciones1=" + habitaciones1 +
                ", habitaciones2=" + habitaciones2 +
                ", habitacionesMas=" + habitacionesMas +
                ", cocinaConGas=" + cocinaConGas +
                ", cocinaConLenia=" + cocinaConLenia +
                ", cocinaConCarbon=" + cocinaConCarbon +
                ", productosToxicos=" + productosToxicos +
                ", productosToxicosInput='" + productosToxicosInput + '\'' +
                ", faunaNociva=" + faunaNociva +
                ", faunaAranias=" + faunaAranias +
                ", faunasAlacranes=" + faunasAlacranes +
                ", faunaSerpientes=" + faunaSerpientes +
                ", faunaOtro=" + faunaOtro +
                ", faunaOtroInput='" + faunaOtroInput + '\'' +
                '}';
    }
}
