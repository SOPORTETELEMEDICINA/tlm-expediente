package net.amentum.niomedic.expediente.converter;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.amentum.niomedic.expediente.model.CatCie9;
import net.amentum.niomedic.expediente.model.Consulta;
import net.amentum.niomedic.expediente.model.Tratamiento;
import net.amentum.niomedic.expediente.persistence.CatCie9Repository;
import net.amentum.niomedic.expediente.views.TratamientoView;
@Component
public class TratamientoConverter {
	@Autowired
	private CatCie9Repository catCie9Repository;
	public Tratamiento toEntity(TratamientoView tratamientoView, Tratamiento tratamiento) {
		CatCie9 catCie9 = catCie9Repository.findOne(tratamientoView.getCatCie9Id());
		tratamiento.setCatCie9(catCie9);
		
		Consulta consulta= new Consulta();
		consulta.setIdConsulta(tratamientoView.getConsultaId());
		tratamiento.setConsulta(consulta);
		
		tratamiento.setCatalogKey(catCie9.getCatalogKey());
		tratamiento.setProNombre(catCie9.getProNombre());
		tratamiento.setFechaCreacion(new Date());	
		return tratamiento;
	}
	public TratamientoView toView(TratamientoView tratamientoView, Tratamiento tratamiento) {
		tratamientoView.setIdTratamiento(tratamiento.getIdTratamiento());
		tratamientoView.setCatCie9Id(tratamiento.getCatCie9().getIdCie9());
		tratamientoView.setConsultaId(tratamiento.getConsulta().getIdConsulta());
		tratamientoView.setFechaCreacion(tratamiento.getFechaCreacion());
		tratamientoView.setCatalogKey(tratamiento.getCatalogKey());
		tratamientoView.setProNombre(tratamiento.getProNombre());
		return tratamientoView;
		
	}
}
