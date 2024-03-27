package net.amentum.niomedic.expediente.model.historia_clinica.interrogatorio_aparatos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "interrogatorio_organo_sentidos_olfato", indexes =  {@Index(name = "idx_interrogatorio_organo_sentidos_olfato_id_hcg", columnList = "id_interrogatorio,id_historia_clinica")})
public class SentidoOlfato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_interrogatorio")
    Long idInterrogatorio;
    @NotNull(message = "id de historia clinica general vacio")
    @Column(name = "id_historia_clinica")
    Long idHistoriaClinica;
    Boolean normal;
    Boolean alterado;
    String tiempoEvolucion;
    Boolean anosmia;
    Boolean cacosomia;
    Boolean hiposmia;
    Boolean parosmia;
    Boolean congestion;
    Boolean rinorrea;
    Boolean epitaxis;
    Boolean pruritoNasal;
    String comentarios;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "sentidoOlfato")
    InterrogatorioAparatos interrogatorioAparatos;

    @Override
    public String toString() {
        return "SentidoOlfato{" +
                "idInterrogatorio=" + idInterrogatorio +
                ", idHistoriaClinica=" + idHistoriaClinica +
                ", normal=" + normal +
                ", alterado=" + alterado +
                ", tiempo_evolucion='" + tiempoEvolucion + '\'' +
                ", anosmia=" + anosmia +
                ", cacosomia=" + cacosomia +
                ", hiposmia=" + hiposmia +
                ", parosmia=" + parosmia +
                ", congestion=" + congestion +
                ", rinorrea=" + rinorrea +
                ", epitaxis=" + epitaxis +
                ", prurito_nasal=" + pruritoNasal +
                ", comentarios='" + comentarios + '\'' +
                '}';
    }
}
