package net.amentum.niomedic.expediente.converter.historia_clinica_general.padecimiento_actual;

import net.amentum.niomedic.expediente.model.historia_clinica.padecimiento_actual.PadecimientoActual;
import net.amentum.niomedic.expediente.views.historia_clinica_general.padecimiento_actual.PadecimientoActualView;
import org.springframework.stereotype.Component;

@Component
public class PadecimientoActualConverter {

    public PadecimientoActual toEntity(PadecimientoActualView view) {
        PadecimientoActual entity = new PadecimientoActual();
        entity.setIdPadecimientoActual(view.getIdPadecimientoActual());
        entity.setIdHistoriaClinica(view.getIdHistoriaClinica());
        entity.setMotivoConsulta(view.getMotivoConsulta());
        entity.setDescripcionPadecimiento(view.getDescripcionPadecimiento());
        entity.setEsVistoOtroMedico(view.getEsVistoOtroMedico());
        entity.setMedicamentoUsado(view.getMedicamentoUsado());
        entity.setNotas(view.getNotas());
        return entity;
    }

    public PadecimientoActualView toView(PadecimientoActual entity) {
        PadecimientoActualView view = new PadecimientoActualView();
        view.setIdPadecimientoActual(entity.getIdPadecimientoActual());
        view.setIdHistoriaClinica(entity.getIdHistoriaClinica());
        view.setMotivoConsulta(entity.getMotivoConsulta());
        view.setDescripcionPadecimiento(entity.getDescripcionPadecimiento());
        view.setEsVistoOtroMedico(entity.getEsVistoOtroMedico());
        view.setMedicamentoUsado(entity.getMedicamentoUsado());
        view.setNotas(entity.getNotas());
        return view;
    }

}
