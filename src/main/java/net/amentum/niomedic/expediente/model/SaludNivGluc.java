package net.amentum.niomedic.expediente.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "salud_niveles_glucosa")
public class SaludNivGluc implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_nivel_glucosa")
    private Integer idnivelglucosa;
    @Column(name = "med_id_fk")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String 	medidfk;
    @Column(name = "pac_id_fk")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String 	pacidfk;
    private Integer gluperiodo;
    @Column(name = "glu_fecha_hora")
    private Timestamp glufechahora;
    private Double glumedida;

}
