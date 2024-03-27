package net.amentum.niomedic.expediente.converter.historia_clinica_general.interrogatorio_aparatos;

import net.amentum.niomedic.expediente.model.historia_clinica.interrogatorio_aparatos.InterrogatorioEndocrino;
import net.amentum.niomedic.expediente.views.historia_clinica_general.interrogatorio_aparatos.InterrogatorioEndocrinoView;
import org.springframework.stereotype.Component;

@Component
public class InterrogatorioEndocrinoConverter {

    public InterrogatorioEndocrino toEntity(InterrogatorioEndocrinoView view) {
        InterrogatorioEndocrino entity = new InterrogatorioEndocrino();
        entity.setIdInterrogatorio(view.getIdInterrogatorio());
        entity.setIdHistoriaClinica(view.getIdHistoriaClinica());
        entity.setEndocriAsintomatico(view.getEndocriAsintomatico());
        entity.setGananciaPeso(view.getGananciaPeso());
        entity.setGananciaPesoCuanto(view.getGananciaPesoCuanto());
        entity.setGananciaPesoEvol(view.getGananciaPesoEvol());
        entity.setNerviosis(view.getNerviosis());
        entity.setDiabetesTipo(view.getDiabetesTipo());
        entity.setDiabetesTiempoEvol(view.getDiabetesTiempoEvol());
        entity.setDiabetesTratamiento(view.getDiabetesTratamiento());
        entity.setPielseca(view.getPielseca());
        entity.setHipertiroiTiempoEvol(view.getHipertiroiTiempoEvol());
        entity.setHipertiroiCaracteristicas(view.getHipertiroiCaracteristicas());
        entity.setHipertiroiTratamiento(view.getHipertiroiTratamiento());
        entity.setPoliuria(view.getPoliuria());
        entity.setEnanismoCaracteristicas(view.getEnanismoCaracteristicas());
        entity.setEnanismoFamiliares(view.getEnanismoFamiliares());
        entity.setPolidipsia(view.getPolidipsia());
        entity.setPoliuriaTiempoEvol(view.getPoliuriaTiempoEvol());
        entity.setPoliuriaCaracteristicas(view.getPoliuriaCaracteristicas());
        entity.setPoliuriaTratamiento(view.getPoliuriaTratamiento());
        entity.setPerdidaPeso1(view.getPerdidaPeso1());
        entity.setPerdidaPesoCuanto(view.getPerdidaPesoCuanto());
        entity.setPerdidaPesoEvol(view.getPerdidaPesoEvol());
        entity.setIntoleranciaFrio(view.getIntoleranciaFrio());
        entity.setIntoleranciaCalor(view.getIntoleranciaCalor());
        entity.setAcumulacionGrasaDorsocervical(view.getAcumulacionGrasaDorsocervical());
        entity.setTemblor(view.getTemblor());
        entity.setHipotiroiTiempoEvol(view.getHipotiroiTiempoEvol());
        entity.setHipotiroiCaracteristicas(view.getHipotiroiCaracteristicas());
        entity.setHipotiroiTratamiento(view.getHipotiroiTratamiento());
        entity.setAcromegalia(view.getAcromegalia());
        entity.setAcromegaliaTiempoEvol(view.getAcromegaliaTiempoEvol());
        entity.setAcromegaliaCaracteristicas(view.getAcromegaliaCaracteristicas());
        entity.setAcromegaliaTratamiento(view.getAcromegaliaTratamiento());
        entity.setPolifagia(view.getPolifagia());
        entity.setHipofisiariosTipo(view.getHipofisiariosTipo());
        entity.setHipofisiariosTiempoEvol(view.getHipofisiariosTiempoEvol());
        entity.setHipofisiariosCaracteristicas(view.getHipofisiariosCaracteristicas());
        entity.setHipofisiariosTratamiento(view.getHipofisiariosTratamiento());
        entity.setComentarios(view.getComentarios());
        entity.setConvulsiones(view.getConvulsiones());
        entity.setEpilepsia(view.getEpilepsia());
        entity.setMigrania(view.getMigrania());
        entity.setPerdidaEquilibrio(view.getPerdidaEquilibrio());
        entity.setPerdidaConciencia(view.getPerdidaConciencia());
        entity.setPerdidaMemoria(view.getPerdidaMemoria());
        entity.setParkinson(view.getParkinson());
        entity.setDemencia(view.getDemencia());
        entity.setHiposensibilidad(view.getHiposensibilidad());
        entity.setHipersensibilidad(view.getHipersensibilidad());
        entity.setAlodinia(view.getAlodinia());
        entity.setHiperalgesia(view.getHiperalgesia());
        entity.setParestesias(view.getParestesias());
        return entity;
    }

    public InterrogatorioEndocrinoView toView(InterrogatorioEndocrino entity) {
        InterrogatorioEndocrinoView view = new InterrogatorioEndocrinoView();
        view.setIdInterrogatorio(entity.getIdInterrogatorio());
        view.setIdHistoriaClinica(entity.getIdHistoriaClinica());
        view.setEndocriAsintomatico(entity.getEndocriAsintomatico());
        view.setGananciaPeso(entity.getGananciaPeso());
        view.setGananciaPesoCuanto(entity.getGananciaPesoCuanto());
        view.setGananciaPesoEvol(entity.getGananciaPesoEvol());
        view.setNerviosis(entity.getNerviosis());
        view.setDiabetesTipo(entity.getDiabetesTipo());
        view.setDiabetesTiempoEvol(entity.getDiabetesTiempoEvol());
        view.setDiabetesTratamiento(entity.getDiabetesTratamiento());
        view.setPielseca(entity.getPielseca());
        view.setHipertiroiTiempoEvol(entity.getHipertiroiTiempoEvol());
        view.setHipertiroiCaracteristicas(entity.getHipertiroiCaracteristicas());
        view.setHipertiroiTratamiento(entity.getHipertiroiTratamiento());
        view.setPoliuria(entity.getPoliuria());
        view.setEnanismoCaracteristicas(entity.getEnanismoCaracteristicas());
        view.setEnanismoFamiliares(entity.getEnanismoFamiliares());
        view.setPolidipsia(entity.getPolidipsia());
        view.setPoliuriaTiempoEvol(entity.getPoliuriaTiempoEvol());
        view.setPoliuriaCaracteristicas(entity.getPoliuriaCaracteristicas());
        view.setPoliuriaTratamiento(entity.getPoliuriaTratamiento());
        view.setPerdidaPeso1(entity.getPerdidaPeso1());
        view.setPerdidaPesoCuanto(entity.getPerdidaPesoCuanto());
        view.setPerdidaPesoEvol(entity.getPerdidaPesoEvol());
        view.setIntoleranciaFrio(entity.getIntoleranciaFrio());
        view.setIntoleranciaCalor(entity.getIntoleranciaCalor());
        view.setAcumulacionGrasaDorsocervical(entity.getAcumulacionGrasaDorsocervical());
        view.setTemblor(entity.getTemblor());
        view.setHipotiroiTiempoEvol(entity.getHipotiroiTiempoEvol());
        view.setHipotiroiCaracteristicas(entity.getHipotiroiCaracteristicas());
        view.setHipotiroiTratamiento(entity.getHipotiroiTratamiento());
        view.setAcromegalia(entity.getAcromegalia());
        view.setAcromegaliaTiempoEvol(entity.getAcromegaliaTiempoEvol());
        view.setAcromegaliaCaracteristicas(entity.getAcromegaliaCaracteristicas());
        view.setAcromegaliaTratamiento(entity.getAcromegaliaTratamiento());
        view.setPolifagia(entity.getPolifagia());
        view.setHipofisiariosTipo(entity.getHipofisiariosTipo());
        view.setHipofisiariosTiempoEvol(entity.getHipofisiariosTiempoEvol());
        view.setHipofisiariosCaracteristicas(entity.getHipofisiariosCaracteristicas());
        view.setHipofisiariosTratamiento(entity.getHipofisiariosTratamiento());
        view.setComentarios(entity.getComentarios());
        view.setConvulsiones(entity.getConvulsiones());
        view.setEpilepsia(entity.getEpilepsia());
        view.setMigrania(entity.getMigrania());
        view.setPerdidaEquilibrio(entity.getPerdidaEquilibrio());
        view.setPerdidaConciencia(entity.getPerdidaConciencia());
        view.setPerdidaMemoria(entity.getPerdidaMemoria());
        view.setParkinson(entity.getParkinson());
        view.setDemencia(entity.getDemencia());
        view.setHiposensibilidad(entity.getHiposensibilidad());
        view.setHipersensibilidad(entity.getHipersensibilidad());
        view.setAlodinia(entity.getAlodinia());
        view.setHiperalgesia(entity.getHiperalgesia());
        view.setParestesias(entity.getParestesias());
        return view;
    }

}
