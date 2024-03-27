package net.amentum.niomedic.expediente.converter.historia_clinica_general.interrogatorio_aparatos;

import net.amentum.niomedic.expediente.model.historia_clinica.interrogatorio_aparatos.InterrogatorioCardiovascular;
import net.amentum.niomedic.expediente.views.historia_clinica_general.interrogatorio_aparatos.InterrogatorioCardiovascularView;
import org.springframework.stereotype.Component;

@Component
public class InterrogatorioCardiovascularConverter {

    public InterrogatorioCardiovascular toEntity(InterrogatorioCardiovascularView view) {
        InterrogatorioCardiovascular entity = new InterrogatorioCardiovascular();
        entity.setIdInterrogatorio(view.getIdInterrogatorio());
        entity.setIdHistoriaClinica(view.getIdHistoriaClinica());
        entity.setHipertension(view.getHipertension());
        entity.setHipertensionEvolucion(view.getHipertensionEvolucion());
        entity.setHipertensionTratamiento(view.getHipertensionTratamiento());
        entity.setPalpitacion(view.getPalpitacion());
        entity.setPalpiEvolucion(view.getPalpiEvolucion());
        entity.setCardioAsintomatico(view.getCardioAsintomatico());
        entity.setPlapiTratamiento(view.getPlapiTratamiento());
        entity.setBradicardia(view.getBradicardia());
        entity.setBradicardiaEvolucion(view.getBradicardiaEvolucion());
        entity.setBradicardiaTratamiento(view.getBradicardiaTratamiento());
        entity.setDiaforesis(view.getDiaforesis());
        entity.setCardiopatia(view.getCardiopatia());
        entity.setCardiopatiaTipo(view.getCardiopatiaTipo());
        entity.setCardiopatiaEvolucion(view.getCardiopatiaEvolucion());
        entity.setCardiopatiaTratamiento(view.getCardiopatiaTratamiento());
        entity.setDolorOpresivo(view.getDolorOpresivo());
        entity.setTaquicardia(view.getTaquicardia());
        entity.setTaquicardiaEvolucion(view.getTaquicardiaEvolucion());
        entity.setTaquicardiaTratamiento(view.getTaquicardiaTratamiento());
        entity.setArritmias(view.getArritmias());
        entity.setArritmiasEvolucion(view.getArritmiasEvolucion());
        entity.setArritmiasTratamiento(view.getArritmiasTratamiento());
        entity.setMareos(view.getMareos());
        entity.setMareosEvolucion(view.getMareosEvolucion());
        entity.setMareosIncorporarse(view.getMareosIncorporarse());
        entity.setMareosFlexionarse(view.getMareosflexionarse());
        entity.setMareosTodoMomento(view.getMareosTodoMomento());
        entity.setCefalea(view.getCefalea());
        entity.setCefaleaGeneral(view.getCefaleaGeneral());
        entity.setCefaleaFrontal(view.getCefaleaFrontal());
        entity.setCefaleaOccipital(view.getCefaleaOccipital());
        entity.setCefaleaBiparietal(view.getCefaleaBiparietal());
        entity.setCefaleaUniDerecha(view.getCefaleaUniDerecha());
        entity.setCefaleaUniDerecha(view.getCefaleaUniDerecha());
        entity.setDisneaGesFuerzos(view.getDisneaGesfuerzos());
        entity.setDisneaMesFuerzos(view.getDisneaMesfuerzos());
        entity.setDisneaPesFuerzos(view.getDisneaPesfuerzos());
        entity.setDisneaEvolucion(view.getDisneaEvolucion());
        entity.setDisneaOxigenoterapia(view.getDisneaOxigenoterapia());
        entity.setSincope(view.getSincope());
        entity.setSincopeEvolucion(view.getSincopeEvolucion());
        entity.setSincopeTratamiento(view.getSincopeTratamiento());
        entity.setCianosisPeribucal(view.getCianosisPeribucal());
        entity.setCianosisDistales(view.getCianosisDistales());
        entity.setPetequias(view.getPetequias());
        entity.setPetequiasSi(view.getPetequiasSi());
        entity.setEdema(view.getEdema());
        entity.setEdemaLocalizacion(view.getEdemaLocalizacion());
        entity.setEdemaEvolucion(view.getEdemaEvolucion());
        entity.setEdemaTratamiento(view.getEdemaTratamiento());
        entity.setSoplo(view.getSoplo());
        entity.setSoploEvolucion(view.getSoploEvolucion());
        entity.setSoploTipo(view.getSoploTipo());
        entity.setHiperlipidemia(view.getHiperlipidemia());
        entity.setHiperTipo(view.getHiperTipo());
        entity.setHipercolesterol(view.getHipercolesterol());
        entity.setHiperglicerol(view.getHiperglicerol());
        entity.setHiperEvolucion(view.getHiperEvolucion());
        entity.setHiperTratamiento(view.getHiperTratamiento());
        entity.setComentarios(view.getComentarios());
        return entity;
    }

    public InterrogatorioCardiovascularView toView(InterrogatorioCardiovascular entity) {
        InterrogatorioCardiovascularView view = new InterrogatorioCardiovascularView();
        view.setIdInterrogatorio(entity.getIdInterrogatorio());
        view.setIdHistoriaClinica(entity.getIdHistoriaClinica());
        view.setHipertension(entity.getHipertension());
        view.setHipertensionEvolucion(entity.getHipertensionEvolucion());
        view.setHipertensionTratamiento(entity.getHipertensionTratamiento());
        view.setPalpitacion(entity.getPalpitacion());
        view.setPalpiEvolucion(entity.getPalpiEvolucion());
        view.setCardioAsintomatico(entity.getCardioAsintomatico());
        view.setPlapiTratamiento(entity.getPlapiTratamiento());
        view.setBradicardia(entity.getBradicardia());
        view.setBradicardiaEvolucion(entity.getBradicardiaEvolucion());
        view.setBradicardiaTratamiento(entity.getBradicardiaTratamiento());
        view.setDiaforesis(entity.getDiaforesis());
        view.setCardiopatia(entity.getCardiopatia());
        view.setCardiopatiaTipo(entity.getCardiopatiaTipo());
        view.setCardiopatiaEvolucion(entity.getCardiopatiaEvolucion());
        view.setCardiopatiaTratamiento(entity.getCardiopatiaTratamiento());
        view.setDolorOpresivo(entity.getDolorOpresivo());
        view.setTaquicardia(entity.getTaquicardia());
        view.setTaquicardiaEvolucion(entity.getTaquicardiaEvolucion());
        view.setTaquicardiaTratamiento(entity.getTaquicardiaTratamiento());
        view.setArritmias(entity.getArritmias());
        view.setArritmiasEvolucion(entity.getArritmiasEvolucion());
        view.setArritmiasTratamiento(entity.getArritmiasTratamiento());
        view.setMareos(entity.getMareos());
        view.setMareosEvolucion(entity.getMareosEvolucion());
        view.setMareosIncorporarse(entity.getMareosIncorporarse());
        view.setMareosflexionarse(entity.getMareosFlexionarse());
        view.setMareosTodoMomento(entity.getMareosTodoMomento());
        view.setCefalea(entity.getCefalea());
        view.setCefaleaGeneral(entity.getCefaleaGeneral());
        view.setCefaleaFrontal(entity.getCefaleaFrontal());
        view.setCefaleaOccipital(entity.getCefaleaOccipital());
        view.setCefaleaBiparietal(entity.getCefaleaBiparietal());
        view.setCefaleaUniDerecha(entity.getCefaleaUniDerecha());
        view.setCefaleaUniDerecha(entity.getCefaleaUniDerecha());
        view.setDisneaGesfuerzos(entity.getDisneaGesFuerzos());
        view.setDisneaMesfuerzos(entity.getDisneaMesFuerzos());
        view.setDisneaPesfuerzos(entity.getDisneaPesFuerzos());
        view.setDisneaEvolucion(entity.getDisneaEvolucion());
        view.setDisneaOxigenoterapia(entity.getDisneaOxigenoterapia());
        view.setSincope(entity.getSincope());
        view.setSincopeEvolucion(entity.getSincopeEvolucion());
        view.setSincopeTratamiento(entity.getSincopeTratamiento());
        view.setCianosisPeribucal(entity.getCianosisPeribucal());
        view.setCianosisDistales(entity.getCianosisDistales());
        view.setPetequias(entity.getPetequias());
        view.setPetequiasSi(entity.getPetequiasSi());
        view.setEdema(entity.getEdema());
        view.setEdemaLocalizacion(entity.getEdemaLocalizacion());
        view.setEdemaEvolucion(entity.getEdemaEvolucion());
        view.setEdemaTratamiento(entity.getEdemaTratamiento());
        view.setSoplo(entity.getSoplo());
        view.setSoploEvolucion(entity.getSoploEvolucion());
        view.setSoploTipo(entity.getSoploTipo());
        view.setHiperlipidemia(entity.getHiperlipidemia());
        view.setHiperTipo(entity.getHiperTipo());
        view.setHipercolesterol(entity.getHipercolesterol());
        view.setHiperglicerol(entity.getHiperglicerol());
        view.setHiperEvolucion(entity.getHiperEvolucion());
        view.setHiperTratamiento(entity.getHiperTratamiento());
        view.setComentarios(entity.getComentarios());
        return view;
    }



}
