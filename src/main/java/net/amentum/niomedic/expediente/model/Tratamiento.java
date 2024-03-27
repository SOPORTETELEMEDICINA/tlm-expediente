package net.amentum.niomedic.expediente.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

import javax.persistence.*;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tratamiento")
public class Tratamiento  implements Serializable{
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tratamiento")
    private Long idTratamiento;
  	//private Long catCie9Id;
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    private String proNombre;
    private String catalogKey;
    //Relaciones
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consulta_id", referencedColumnName = "id_consulta")
    private Consulta consulta;
    
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn(name = "cie9_id", referencedColumnName = "id_cie9")
    private CatCie9 catCie9;
    
	@Override
	public String toString() {
		return "Tratamiento {"
				+ ", idTratamiento='" + idTratamiento +'\''+ 
				", fechaCreacion='"+ fechaCreacion +'\''+
				", proNombre='"+ proNombre +'\''+
				", catalogKey='"+catalogKey+'\''+
				"}";
	} 
    
    
}
