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
@Table(name = "salud_indicaciones_gluc")
public class SaludIndGluc implements Serializable {
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
    private Integer urgenciabaja;
    private Integer alertaalta;
    private Integer urgenciaalta;
    private Time glu1hora;
    private Boolean glu1lu;
    private Boolean glu1ma;
    private Boolean glu1mi;
    private Boolean glu1ju;
    private Boolean glu1vi;
    private Boolean glu1sa;
    private Boolean glu1do;
    private Time glu2hora;
    private Boolean glu2lu;
    private Boolean glu2ma;
    private Boolean glu2mi;
    private Boolean glu2ju;
    private Boolean glu2vi;
    private Boolean glu2sa;
    private Boolean glu2do;
    private Time glu3hora;
    private Boolean glu3lu;
    private Boolean glu3ma;
    private Boolean glu3mi;
    private Boolean glu3ju;
    private Boolean glu3vi;
    private Boolean glu3sa;
    private Boolean glu3do;
    private Time glu4hora;
    private Boolean glu4lu;
    private Boolean glu4ma;
    private Boolean glu4mi;
    private Boolean glu4ju;
    private Boolean glu4vi;
    private Boolean glu4sa;
    private Boolean glu4do;
    private Time glu5hora;
    private Boolean glu5lu;
    private Boolean glu5ma;
    private Boolean glu5mi;
    private Boolean glu5ju;
    private Boolean glu5vi;
    private Boolean glu5sa;
    private Boolean glu5do;
    private Time glu6hora;
    private Boolean glu6lu;
    private Boolean glu6ma;
    private Boolean glu6mi;
    private Boolean glu6ju;
    private Boolean glu6vi;
    private Boolean glu6sa;
    private Boolean glu6do;
    private Time glu7hora;
    private Boolean glu7lu;
    private Boolean glu7ma;
    private Boolean glu7mi;
    private Boolean glu7ju;
    private Boolean glu7vi;
    private Boolean glu7sa;
    private Boolean glu7do;
    private Time glu8hora;
    private Boolean glu8lu;
    private Boolean glu8ma;
    private Boolean glu8mi;
    private Boolean glu8ju;
    private Boolean glu8vi;
    private Boolean glu8sa;
    private Boolean glu8do;
}
