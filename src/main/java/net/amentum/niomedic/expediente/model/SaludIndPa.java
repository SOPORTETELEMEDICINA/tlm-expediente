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
@Table(name = "salud_indicaciones_pa")
public class SaludIndPa implements Serializable {

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
    private Integer alertabajasys;
    private Integer alertabajadia;
    private Integer urgenciaaltasys;
    private Integer urgenciaaltadia;
    private Time p1hora;
    private Boolean sys1lu;
    private Boolean sys1ma;
    private Boolean sys1mi;
    private Boolean sys1ju;
    private Boolean sys1vi;
    private Boolean sys1sa;
    private Boolean sys1do;
    private Time p2hora;
    private Boolean sys2lu;
    private Boolean sys2ma;
    private Boolean sys2mi;
    private Boolean sys2ju;
    private Boolean sys2vi;
    private Boolean sys2sa;
    private Boolean sys2do;
    private Time p3hora;
    private Boolean sys3lu;
    private Boolean sys3ma;
    private Boolean sys3mi;
    private Boolean sys3ju;
    private Boolean sys3vi;
    private Boolean sys3sa;
    private Boolean sys3do;
    private Time p4hora;
    private Boolean sys4lu;
    private Boolean sys4ma;
    private Boolean sys4mi;
    private Boolean sys4ju;
    private Boolean sys4vi;
    private Boolean sys4sa;
    private Boolean sys4do;
    private Time p5hora;
    private Boolean sys5lu;
    private Boolean sys5ma;
    private Boolean sys5mi;
    private Boolean sys5ju;
    private Boolean sys5vi;
    private Boolean sys5sa;
    private Boolean sys5do;
    private Time p6hora;
    private Boolean sys6lu;
    private Boolean sys6ma;
    private Boolean sys6mi;
    private Boolean sys6ju;
    private Boolean sys6vi;
    private Boolean sys6sa;
    private Boolean sys6do;
    private Time p7hora;
    private Boolean sys7lu;
    private Boolean sys7ma;
    private Boolean sys7mi;
    private Boolean sys7ju;
    private Boolean sys7vi;
    private Boolean sys7sa;
    private Boolean sys7do;

}
