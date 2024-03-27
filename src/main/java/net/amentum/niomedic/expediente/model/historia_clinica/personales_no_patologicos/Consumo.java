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
@Table(name = "pnp_consumo", indexes = {@Index(name = "idx_pnp_consumo_id_hcg", columnList = "id_consumo,id_historia_clinica")})
public class Consumo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_consumo")
    Long idConsumo;
    @NotNull(message = "id de historia clinica general vacio")
    @Column(name = "id_historia_clinica")
    Long idHistoriaClinica;
    Boolean droga;
    String nombre;
    String frecuencia;
    String cantidad;
    Integer edadInicio;
    Integer edadAbandono;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "personal_no_patologico_id", referencedColumnName = "id_personales_no_patologicos")
    PersonalesNoPatologicos personalesNoPatologicos;

    @Override
    public String toString() {
        return "Consumo{" +
                "idConsumo=" + idConsumo +
                ", id_historia_clinica=" + idHistoriaClinica +
                ", droga=" + droga +
                ", nombre='" + nombre + '\'' +
                ", frecuencia='" + frecuencia + '\'' +
                ", cantidad='" + cantidad + '\'' +
                ", edadInicio=" + edadInicio +
                ", edadAbandono=" + edadAbandono +
                '}';
    }
}
