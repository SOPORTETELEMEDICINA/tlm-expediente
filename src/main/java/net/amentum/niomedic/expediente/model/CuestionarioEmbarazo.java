package net.amentum.niomedic.expediente.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cuestionario_embarazo")
public class CuestionarioEmbarazo implements Serializable  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cuestionario")
    private Integer idCuestionario;

    @Column(name = "med_id_fk")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String medidfk;

    @Column(name = "pac_id_fk")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String pacidfk;

    @Column(name = "dolor_de_cabeza")
    private boolean dolorDeCabeza;

    @Column(name = "vision_borrosa")
    private boolean visionBorrosa;

    @Column(name = "mareos")
    private boolean mareos;

    @Column(name = "zumbido_en_oidos")
    private boolean zumbidoEnOidos;

    @Column(name = "dolor_abdominal")
    private boolean dolorAbdominal;

    @Column(name = "sangrado_vaginal")
    private boolean sangradoVaginal;

    @Column(name = "sin_movimiento_bebe ")
    private boolean sinMovimientoBebe;

    @Column(name = "salida_liquido_vaginal")
    private boolean salidaLiquidoVaginal;

    @Column(name = "hinchazones")
    private boolean hinchazones;

    @Column(name = "vomito_diarrea")
    private boolean vomitoDiarrea;

    @Column(name = "fiebre_escalofrios")
    private boolean fiebreEscalofrios;

    @Column(name = "peso")
    private boolean peso;

    @Column(name = "dolor_al_orinar")
    private boolean dolorAlOrinar;

    @Column(name = "abdomen_duro")
    private boolean abdomenDuro;

    @Column(name = "total_si")
    private Integer totalSi;

    @Column(name = "total_no")
    private Integer totalNo;

    @Column(name = "hora_aplicacion")
    private Timestamp horaAplicacion;

    @Override
    public String toString() {
        return "CuestionarioEmbarazo {" +
                "idCuestionario=" + idCuestionario +
                ", medidfk='" + medidfk +
                ", pacidfk='" + pacidfk +
                ", dolorDeCabeza=" + dolorDeCabeza +
                ", visionBorrosa=" + visionBorrosa +
                ", mareos=" + mareos +
                ", zumbidoEnOidos=" + zumbidoEnOidos +
                ", dolorAbdominal=" + dolorAbdominal +
                ", sangradoVaginal=" + sangradoVaginal +
                ", sinMovimientoBebe=" + sinMovimientoBebe +
                ", salidaLiquidoVaginal=" + salidaLiquidoVaginal +
                ", hinchazones=" + hinchazones +
                ", vomitoDiarrea=" + vomitoDiarrea +
                ", fiebreEscalofrios=" + fiebreEscalofrios +
                ", peso=" + peso +
                ", dolorAlOrinar=" + dolorAlOrinar +
                ", abdomenDuro=" + abdomenDuro +
                ", totalSi=" + totalSi +
                ", totalNo=" + totalNo +
                ", horaAplicacion=" + horaAplicacion +
                '}';
    }
}
