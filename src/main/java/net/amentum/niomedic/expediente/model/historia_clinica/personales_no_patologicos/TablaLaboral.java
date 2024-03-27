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
@Table(name = "pnp_tabla_laboral", indexes = {@Index(name = "idx_tabla_laboral_id_hcg", columnList = "id_tabla_laboral,id_historia_clinica")})
public class TablaLaboral {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tabla_laboral")
    Long idTablaLaboral;
    @NotNull(message = "id de historia clinica general vacio")
    @Column(name = "id_historia_clinica")
    Long idHistoriaClinica;
    String agentesQuimicos;
    String periodo;
    String puesto;
    String empresa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "personal_no_patologico_id", referencedColumnName = "id_personales_no_patologicos")
    PersonalesNoPatologicos personalesNoPatologicos;

    @Override
    public String toString() {
        return "TablaLaboral{" +
                "idTablaLaboral=" + idTablaLaboral +
                ", idHistoriaClinica=" + idHistoriaClinica +
                ", agentesQuimicos='" + agentesQuimicos + '\'' +
                ", periodo='" + periodo + '\'' +
                ", puesto='" + puesto + '\'' +
                ", empresa='" + empresa + '\'' +
                '}';
    }
}
