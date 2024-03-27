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
@Table(name = "pnp_historia_laboral", indexes = {@Index(name = "idx_historia_laboral_id_hcg", columnList = "id_historia_laboral,id_historia_clinica")})
public class HistoriaLaboral {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_historia_laboral")
    Long idHistoriaLaboral;
    @NotNull(message = "id de historia clinica general vacio")
    @Column(name = "id_historia_clinica")
    Long idHistoriaClinica;
    Boolean trabaja;
    Boolean jubilado;
    Boolean jubilacionEdad;
    Boolean jubilacionEnfermedad;
    String jubilacionInput;
    String comentarios;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "historiaLaboral")
    PersonalesNoPatologicos personalesNoPatologicos;

    @Override
    public String toString() {
        return "HistoriaLaboral{" +
                "idHistoriaLaboral=" + idHistoriaLaboral +
                ", idHistoriaClinica=" + idHistoriaClinica +
                ", trabaja=" + trabaja +
                ", jubilado=" + jubilado +
                ", jubilacionEdad=" + jubilacionEdad +
                ", jubilacionEnfermedad=" + jubilacionEnfermedad +
                ", jubilacionInput='" + jubilacionInput + '\'' +
                ", comentarios='" + comentarios + '\'' +
                '}';
    }
}
