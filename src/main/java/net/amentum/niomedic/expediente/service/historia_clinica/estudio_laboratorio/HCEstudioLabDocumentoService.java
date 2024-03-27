package net.amentum.niomedic.expediente.service.historia_clinica.estudio_laboratorio;

import net.amentum.niomedic.expediente.exception.HistoriaClinicaGeneralException;
import net.amentum.niomedic.expediente.views.historia_clinica_general.estudio_laboratorio.HCEstudioLabDocumentoView;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface HCEstudioLabDocumentoService {

    void createEstudioLabDocumento(HCEstudioLabDocumentoView view) throws HistoriaClinicaGeneralException;

    void updateEstudioLabDocumento(HCEstudioLabDocumentoView view) throws HistoriaClinicaGeneralException;

    List<HCEstudioLabDocumentoView> getEstudioLabDocumento(@NotNull Long idEstudioLab, @NotNull Long idHistoriaClinica) throws HistoriaClinicaGeneralException;

    void deleteEstudioLabDocumento(@NotNull Long idEstudioLab, @NotNull Long idHistoriaClinica) throws HistoriaClinicaGeneralException;
}
