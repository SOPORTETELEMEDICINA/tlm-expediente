package net.amentum.niomedic.expediente.service.historia_clinica.estudio_laboratorio;

import net.amentum.niomedic.expediente.exception.HistoriaClinicaGeneralException;
import net.amentum.niomedic.expediente.views.historia_clinica_general.estudio_laboratorio.HCEstudioLabEstudioView;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface HCEstudioLabTipoService {

    void createEstudioLabTpo(HCEstudioLabEstudioView view) throws HistoriaClinicaGeneralException;

    void updateEstudioLabTpo(HCEstudioLabEstudioView view) throws HistoriaClinicaGeneralException;

    List<HCEstudioLabEstudioView> getEstudioLabTipo(@NotNull Long idEstudioLab, @NotNull Long idHistoriaClinica) throws HistoriaClinicaGeneralException;

    void deleteEstudioLabTipo(@NotNull Long idEstudioLab, @NotNull Long idHistoriaClinica);
}
