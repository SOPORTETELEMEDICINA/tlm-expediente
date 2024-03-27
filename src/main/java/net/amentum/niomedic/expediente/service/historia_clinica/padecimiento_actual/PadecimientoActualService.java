package net.amentum.niomedic.expediente.service.historia_clinica.padecimiento_actual;

import net.amentum.niomedic.expediente.exception.HistoriaClinicaGeneralException;
import net.amentum.niomedic.expediente.views.historia_clinica_general.padecimiento_actual.PadecimientoActualView;

import javax.validation.constraints.NotNull;

public interface PadecimientoActualService {
    void createPadecimientoActual(PadecimientoActualView view, @NotNull Long idHistoriaClinica) throws HistoriaClinicaGeneralException;

    void updatePadecimientoActual(PadecimientoActualView view, @NotNull Long idHistoriaClinica) throws HistoriaClinicaGeneralException;

    PadecimientoActualView getPadecimientoActual(@NotNull Long idHistoriaClinica) throws HistoriaClinicaGeneralException;

    void deletePadecimientoActual(@NotNull Long idHistoriaClinica) throws HistoriaClinicaGeneralException;
}
