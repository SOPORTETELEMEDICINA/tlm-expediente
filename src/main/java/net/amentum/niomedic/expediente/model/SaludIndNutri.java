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


import java.sql.Time;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "salud_indicaciones_nutri")
public class SaludIndNutri implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_indic")
    private Integer idindic;
    @Column(name = "med_id_fk")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String 	medidfk;
    @Column(name = "pac_id_fk")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String 	pacidfk;
    private Float pesoinicial;
    private Float tallainicial;
    private Time p1hora;
    private Boolean nutri1lu;
    private Boolean nutri1ma;
    private Boolean nutri1mi;
    private Boolean nutri1ju;
    private Boolean nutri1vi;
    private Boolean nutri1sa;
    private Boolean nutri1do;
    private Time p2hora;
    private Boolean nutri2lu;
    private Boolean nutri2ma;
    private Boolean nutri2mi;
    private Boolean nutri2ju;
    private Boolean nutri2vi;
    private Boolean nutri2sa;
    private Boolean nutri2do;
    private Time p3hora;
    private Boolean nutri3lu;
    private Boolean nutri3ma;
    private Boolean nutri3mi;
    private Boolean nutri3ju;
    private Boolean nutri3vi;
    private Boolean nutri3sa;
    private Boolean nutri3do;

}
