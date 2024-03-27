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
@Table(name = "salud_niveles_peso")
public class SaludNivPeso implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_nivel_peso")
    private Integer idnivelpeso;
    @Column(name = "med_id_fk")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String 	medidfk;
    @Column(name = "pac_id_fk")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String 	pacidfk;
    private Integer pesoperiodo;
    @Column(name = "peso_fecha_hora")
    private Timestamp pesofechahora;
    private Double pesomedida;


    @Override
    public String toString() {
        return "SaludNivPeso {" +
                "idnivelpeso=" + idnivelpeso +
                ", medidfk=" + medidfk +
                ", pacidfk=" + pacidfk +
                ", pesoperiodo=" + pesoperiodo +
                ", pesofechahora=" + pesofechahora +
                ", pesomedida=" + pesomedida +
                "}";
    }

}
