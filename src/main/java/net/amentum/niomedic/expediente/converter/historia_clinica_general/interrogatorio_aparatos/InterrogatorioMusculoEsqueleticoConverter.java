package net.amentum.niomedic.expediente.converter.historia_clinica_general.interrogatorio_aparatos;

import net.amentum.niomedic.expediente.model.historia_clinica.interrogatorio_aparatos.InterrogatorioMusculoEsqueletico;
import net.amentum.niomedic.expediente.views.historia_clinica_general.interrogatorio_aparatos.InterrogatorioMusculoEsqueleticoView;
import org.springframework.stereotype.Component;

@Component
public class InterrogatorioMusculoEsqueleticoConverter {

    public InterrogatorioMusculoEsqueletico toEntity(InterrogatorioMusculoEsqueleticoView view) {
        InterrogatorioMusculoEsqueletico entity = new InterrogatorioMusculoEsqueletico();
        entity.setIdInterrogatorio(view.getIdInterrogatorio());
        entity.setIdHistoriaClinica(view.getIdHistoriaClinica());
        entity.setAsintomatico(view.getAsintomatico());
        entity.setInflamacion(view.getInflamacion());
        entity.setInflamacionLocalizacion(view.getInflamacionLocalizacion());
        entity.setInflamacionTiempoEvol(view.getInflamacionTiempoEvol());
        entity.setDolor(view.getDolor());
        entity.setDolorLocalizacion(view.getDolorLocalizacion());
        entity.setDolorTiempoEvol(view.getDolorTiempoEvol());
        entity.setDolorEvn(view.getDolorEvn());
        entity.setDebilidadMuscular(view.getDebilidadMuscular());
        entity.setDebilidadMuscularLocalizacion(view.getDebilidadMuscularLocalizacion());
        entity.setDebilidadMuscularTiempoEvol(view.getDebilidadMuscularTiempoEvol());
        entity.setSecrecion(view.getSecrecion());
        entity.setLimitacionMovimientos(view.getLimitacionMovimientos());
        entity.setLimitacionMovimientosLocalizacion(view.getLimitacionMovimientosLocalizacion());
        entity.setLimitacionMovimientosNormal(view.getLimitacionMovimientosNormal());
        entity.setLimitacionMovimientosRestringido(view.getLimitacionMovimientosRestringido());
        entity.setLimitacionMovimientosAumentado(view.getLimitacionMovimientosAumentado());
        entity.setDeformidades(view.getDeformidades());
        entity.setDeformidadesLocalizacion(view.getDeformidadesLocalizacion());
        entity.setDeformidadesTiempoEvol(view.getDeformidadesTiempoEvol());
        entity.setCalor(view.getCalor());
        entity.setCalorLocalizacion(view.getCalorLocalizacion());
        entity.setEritema(view.getEritema());
        entity.setEritemaLocalizacion(view.getEritemaLocalizacion());
        entity.setCrepitaciones(view.getCrepitaciones());
        entity.setCrepitacionesLocalizacion(view.getCrepitacionesLocalizacion());
        entity.setComentarios(view.getComentarios());
        return entity;
    }

    public InterrogatorioMusculoEsqueleticoView toView(InterrogatorioMusculoEsqueletico entity) {
        InterrogatorioMusculoEsqueleticoView view = new InterrogatorioMusculoEsqueleticoView();
        view.setIdInterrogatorio(entity.getIdInterrogatorio());
        view.setIdHistoriaClinica(entity.getIdHistoriaClinica());
        view.setAsintomatico(entity.getAsintomatico());
        view.setInflamacion(entity.getInflamacion());
        view.setInflamacionLocalizacion(entity.getInflamacionLocalizacion());
        view.setInflamacionTiempoEvol(entity.getInflamacionTiempoEvol());
        view.setDolor(entity.getDolor());
        view.setDolorLocalizacion(entity.getDolorLocalizacion());
        view.setDolorTiempoEvol(entity.getDolorTiempoEvol());
        view.setDolorEvn(entity.getDolorEvn());
        view.setDebilidadMuscular(entity.getDebilidadMuscular());
        view.setDebilidadMuscularLocalizacion(entity.getDebilidadMuscularLocalizacion());
        view.setDebilidadMuscularTiempoEvol(entity.getDebilidadMuscularTiempoEvol());
        view.setSecrecion(entity.getSecrecion());
        view.setLimitacionMovimientos(entity.getLimitacionMovimientos());
        view.setLimitacionMovimientosLocalizacion(entity.getLimitacionMovimientosLocalizacion());
        view.setLimitacionMovimientosNormal(entity.getLimitacionMovimientosNormal());
        view.setLimitacionMovimientosRestringido(entity.getLimitacionMovimientosRestringido());
        view.setLimitacionMovimientosAumentado(entity.getLimitacionMovimientosAumentado());
        view.setDeformidades(entity.getDeformidades());
        view.setDeformidadesLocalizacion(entity.getDeformidadesLocalizacion());
        view.setDeformidadesTiempoEvol(entity.getDeformidadesTiempoEvol());
        view.setCalor(entity.getCalor());
        view.setCalorLocalizacion(entity.getCalorLocalizacion());
        view.setEritema(entity.getEritema());
        view.setEritemaLocalizacion(entity.getEritemaLocalizacion());
        view.setCrepitaciones(entity.getCrepitaciones());
        view.setCrepitacionesLocalizacion(entity.getCrepitacionesLocalizacion());
        view.setComentarios(entity.getComentarios());
        return view;
    }

}
