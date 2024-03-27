package net.amentum.niomedic.expediente.converter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

import net.amentum.niomedic.expediente.model.Controles;
import net.amentum.niomedic.expediente.model.PeriodosControles;
import net.amentum.niomedic.expediente.views.ControlesView;

@Component
@Slf4j
public class ControlesConverter {
	
	@Autowired
	PeriodosControlesConverter periodosControlesConverter;
	
	
	/**
	 * Método para convertir una vista en un entidad
	 * @param controlesView vista a convertir 
	 * @param controles entidad a settear
	 * @param update si el valor es TRUE indica que es una actualizacion de una entidad ya existente
	 * @return Controles entidad
	 * **/
	public Controles toEntity(ControlesView controlesView, Controles controles, Boolean update) {
		controles.setIdPaciente(controlesView.getIdPaciente());
		
		controles.setDLimiteRangoBajo(controlesView.getDLimiteRangoBajo());
		controles.setDRangoBajo(controlesView.getDRangoBajo());
		controles.setDRangoAlto(controlesView.getDRangoAlto());
		controles.setDLimiteRangoAlto(controlesView.getDLimiteRangoAlto());
		
		controles.setOxPrLimiteRangoBajo(controlesView.getOxPrLimiteRangoBajo());
		controles.setOxPrRangoBajo(controlesView.getOxPrRangoBajo());
		controles.setOxPrRangoAlto(controlesView.getOxPrRangoAlto());
		controles.setOxPrLimiteRangoAlto(controlesView.getOxPrLimiteRangoAlto());
		
		controles.setOxSpo2LimiteRangoBajo(controlesView.getOxSpo2LimiteRangoBajo());
		controles.setOxSpo2RangoBajo(controlesView.getOxSpo2RangoBajo());
		controles.setOxSpo2RangoAlto(controlesView.getOxSpo2RangoAlto());
		controles.setOxSpo2LimiteRangoAlto(controlesView.getOxSpo2LimiteRangoAlto());
		
		controles.setHpDLimiteRangoBajo(controlesView.getHpDLimiteRangoBajo());
		controles.setHpDRangoBajo(controlesView.getHpDRangoBajo());
		controles.setHpDRangoAlto(controlesView.getHpDRangoAlto());
		controles.setHpDLimiteRangoAlto(controlesView.getHpDLimiteRangoAlto());
		
		controles.setHpSLimiteRangoBajo(controlesView.getHpSLimiteRangoBajo());
		controles.setHpSRangoBajo(controlesView.getHpSRangoBajo());
		controles.setHpSRangoAlto(controlesView.getHpSRangoAlto());
		controles.setHpSLimiteRangoAlto(controlesView.getHpSLimiteRangoAlto());
		
		controles.setHpPLimiteRangoBajo(controlesView.getHpPLimiteRangoBajo());
		controles.setHpPRangoBajo(controlesView.getHpPRangoBajo());
		controles.setHpPRangoAlto(controlesView.getHpPRangoAlto());
		controles.setHpPLimiteRangoAlto(controlesView.getHpPLimiteRangoAlto());
		
		if(!update) {
			controles.setFechaCreacion(new Date());
			controles.setIdUsuarioQuienCreo(controlesView.getIdUsuarioQuienCreo());
			controles.setNombreQuienCreo(controlesView.getNombreQuienCreo());
		}else {
			controles.setFechaUltimaModificacion(new Date());
			controles.setIdUsuarioQuienModificia(controlesView.getIdUsuarioQuienModificia());
			controles.setNombreQuienModifica(controlesView.getNombreQuienModifica());
		}
		
		controles.setHipertension(controlesView.getHipertension());
		controles.setOximetria(controlesView.getOximetria());
		controles.setDiabetes(controlesView.getDiabetes());
		
		return controles;
		
	}
	
	/**
	 * Método para convertir una enidad a una vista
	 * @param controles entidad a convertir 
	 * @param controlesView vista a settear
	 * @return ControlesView Vista
	 * **/
	public ControlesView toViews(ControlesView controlesView, Controles controles){
		controlesView.setIdControl(controles.getIdControl());
		
		controlesView.setIdPaciente(controles.getIdPaciente());

		controlesView.setDLimiteRangoBajo(controles.getDLimiteRangoBajo());
		controlesView.setDRangoBajo(controles.getDRangoBajo());
		controlesView.setDRangoAlto(controles.getDRangoAlto());
		controlesView.setDLimiteRangoAlto(controles.getDLimiteRangoAlto());

		controlesView.setOxPrLimiteRangoBajo(controles.getOxPrLimiteRangoBajo());
		controlesView.setOxPrRangoBajo(controles.getOxPrRangoBajo());
		controlesView.setOxPrRangoAlto(controles.getOxPrRangoAlto());
		controlesView.setOxPrLimiteRangoAlto(controles.getOxPrLimiteRangoAlto());
		
		controlesView.setOxSpo2LimiteRangoBajo(controles.getOxSpo2LimiteRangoBajo());
		controlesView.setOxSpo2RangoBajo(controles.getOxSpo2RangoBajo());
		controlesView.setOxSpo2RangoAlto(controles.getOxSpo2RangoAlto());
		controlesView.setOxSpo2LimiteRangoAlto(controles.getOxSpo2LimiteRangoAlto());

		controlesView.setHpDLimiteRangoBajo(controles.getHpDLimiteRangoBajo());
		controlesView.setHpDRangoBajo(controles.getHpDRangoBajo());
		controlesView.setHpDRangoAlto(controles.getHpDRangoAlto());
		controlesView.setHpDLimiteRangoAlto(controles.getHpDLimiteRangoAlto());

		controlesView.setHpSLimiteRangoBajo(controles.getHpSLimiteRangoBajo());
		controlesView.setHpSRangoBajo(controles.getHpSRangoBajo());
		controlesView.setHpSRangoAlto(controles.getHpSRangoAlto());
		controlesView.setHpSLimiteRangoAlto(controles.getHpSLimiteRangoAlto());

		controlesView.setHpPLimiteRangoBajo(controles.getHpPLimiteRangoBajo());
		controlesView.setHpPRangoBajo(controles.getHpPRangoBajo());
		controlesView.setHpPRangoAlto(controles.getHpPRangoAlto());
		controlesView.setHpPLimiteRangoAlto(controles.getHpPLimiteRangoAlto());


		controlesView.setFechaCreacion(controles.getFechaCreacion());
		controlesView.setIdUsuarioQuienCreo(controles.getIdUsuarioQuienCreo());
		controlesView.setNombreQuienCreo(controles.getNombreQuienCreo());

		controlesView.setFechaUltimaModificacion(controles.getFechaUltimaModificacion());
		controlesView.setIdUsuarioQuienModificia(controles.getIdUsuarioQuienModificia());
		controlesView.setNombreQuienModifica(controles.getNombreQuienModifica());

		controlesView.setHipertension(controles.getHipertension());
		controlesView.setOximetria(controles.getOximetria());
		controlesView.setDiabetes(controles.getDiabetes());
		
		if(!controles.getPeriodosControles().isEmpty()) {
			List<PeriodosControles> pc = new ArrayList<>(controles.getPeriodosControles());
			controlesView.setPeriodosControlesView(periodosControlesConverter.toEntView(pc,Boolean.FALSE));
		}
		return controlesView;
	}
}
