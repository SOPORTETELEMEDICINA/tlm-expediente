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
@Table(name = "pnp_habitos_higienicos", indexes = {@Index(name = "idx_habitos_higienicos_id_hcg", columnList = "id_habito,id_historia_clinica")})
public class HabitoHigienico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_habito")
    Long idHabito;
    @NotNull(message = "id de historia clinica general vacio")
    @Column(name = "id_historia_clinica")
    Long idHistoriaClinica;
    String aseoBucal;
    String banio;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "habitoHigienico")
    PersonalesNoPatologicos personalesNoPatologicos;

    @Override
    public String toString() {
        return "HabitoHigienico{" +
                "idHabito=" + idHabito +
                ", idHistoriaClinica=" + idHistoriaClinica +
                ", aseoBucal='" + aseoBucal + '\'' +
                ", banio='" + banio + '\'' +
                '}';
    }
}
