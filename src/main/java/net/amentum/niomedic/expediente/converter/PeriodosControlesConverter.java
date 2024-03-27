package net.amentum.niomedic.expediente.converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.jfree.util.Log;
import org.springframework.stereotype.Component;

import net.amentum.niomedic.expediente.model.Controles;
import net.amentum.niomedic.expediente.model.PeriodosControles;
import net.amentum.niomedic.expediente.views.PeriodosControlesView;;

@Component
public class PeriodosControlesConverter {

	public List<PeriodosControles> toEntity(Set<PeriodosControlesView> periodosControlesViewList, Controles controles) {
		List<PeriodosControles>  periodoControlestList = new ArrayList<PeriodosControles>();
		for(PeriodosControlesView periodosControlesView:periodosControlesViewList){
			PeriodosControles periodosControles = new PeriodosControles();
			periodosControles.setIdPeriodoControl(periodosControlesView.getIdPeriodoControl());
			periodosControles.setDiaSemana(periodosControlesView.getDiaSemana());
			periodosControles.setHorario(periodosControlesView.getHorario());
			periodosControles.setDiabetes(periodosControlesView.getDiabetes());
			periodosControles.setHipertension(periodosControlesView.getHipertension());
			periodosControles.setOximetria(periodosControlesView.getOximetria());
			//agrega la relacion de controles
			periodosControles.setControles(controles);
			periodoControlestList.add(periodosControles);
		}
		return periodoControlestList;
	}


	public Set<PeriodosControlesView> toEntView(List<PeriodosControles> periodosControlesList, Boolean fechaCompleta) {
		Set<PeriodosControlesView> periodosControlesViewList = new LinkedHashSet<PeriodosControlesView>();
		for(PeriodosControles periodosControles:periodosControlesList) {
			PeriodosControlesView periodosControlesView = new PeriodosControlesView();
			periodosControlesView.setIdPeriodoControl(periodosControles.getIdPeriodoControl());
			periodosControlesView.setDiaSemana(periodosControles.getDiaSemana());
			if(fechaCompleta) {
				periodosControlesView.setHorario(periodosControles.getHorario());
			}else {
				periodosControlesView.setHorario(fechaCompleta(periodosControles.getHorario()));
			}
			periodosControlesView.setDiabetes(periodosControles.getDiabetes());
			periodosControlesView.setHipertension(periodosControles.getHipertension());
			periodosControlesView.setOximetria(periodosControles.getOximetria());
			periodosControlesView.setIdControles(periodosControles.getControles().getIdControl());
			periodosControlesViewList.add(periodosControlesView);
		}
		return periodosControlesViewList;
	}
	
	public Date fechaCompleta(Date horario) {
		try {
			String horas= horario.toString();
			Date fecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2000-01-01 "+horas);
			return fecha;
		} catch (ParseException e) {
			Log.error("fechaCompleta() -  Ocurrio un error al convertir el horario del periodo - error{}",e);
		}
		return null;
		
	}
}
