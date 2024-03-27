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
@Table(name = "pnp_actividad_deportiva", indexes = {@Index(name = "idx_actividad_deportiva_id_hcg", columnList = "id_actividad_deportiva,id_historia_clinica")})
public class ActividadDeportiva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_actividad_deportiva")
    Long idActivadDeportiva;
    @NotNull(message = "id de historia clinica general vacio")
    @Column(name = "id_historia_clinica")
    Long idHistoriaClinica;
    Boolean haceActividad;
    String frecuencia;
    String tipo;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "actividadDeportiva")
    PersonalesNoPatologicos personalesNoPatologicos;

    @Override
    public String toString() {
        return "ActividadDeportiva{" +
                "idActivadDeportiva=" + idActivadDeportiva +
                ", idHistoriaClinica=" + idHistoriaClinica +
                ", haceActividad=" + haceActividad +
                ", frecuencia='" + frecuencia + '\'' +
                ", tipo='" + tipo + '\'' +
                '}';
    }
}
