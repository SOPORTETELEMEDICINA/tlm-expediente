package net.amentum.niomedic.expediente.service.historia_clinica.padecimiento_actual;

import lombok.extern.slf4j.Slf4j;
import net.amentum.niomedic.expediente.converter.historia_clinica_general.padecimiento_actual.PadecimientoActualConverter;
import net.amentum.niomedic.expediente.exception.HistoriaClinicaGeneralException;
import net.amentum.niomedic.expediente.model.historia_clinica.padecimiento_actual.PadecimientoActual;
import net.amentum.niomedic.expediente.persistence.historia_clinica.padecimiento_actual.PadecimientoActualRepository;
import net.amentum.niomedic.expediente.views.historia_clinica_general.padecimiento_actual.PadecimientoActualView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PadecimientoActualServiceImpl implements PadecimientoActualService {

    @Autowired
    PadecimientoActualRepository repository;

    @Autowired
    PadecimientoActualConverter converter;

    @Override
    public void createPadecimientoActual(PadecimientoActualView view, Long idHistoriaClinica) throws HistoriaClinicaGeneralException {
        try {
            if(repository.findByIdHistoriaClinica(idHistoriaClinica) != null) {
                HistoriaClinicaGeneralException hcge = new HistoriaClinicaGeneralException("Error al insertar Padecimiento actual", HistoriaClinicaGeneralException.LAYER_DAO, HistoriaClinicaGeneralException.ACTION_INSERT);
                hcge.addError("Padecimiento actual ya registrado para la historia clínica " + idHistoriaClinica);
                log.error("Padecimiento actual ya registrado para la historia clínica {}", idHistoriaClinica);
                throw hcge;
            }
            view.setIdHistoriaClinica(idHistoriaClinica);
            repository.save(converter.toEntity(view));
            log.info("Agregando Padecimiento actual HistoriaClinica: {}",view.getIdHistoriaClinica());
        } catch (HistoriaClinicaGeneralException ex) {
            throw ex;
        } catch (Exception ex) {
            HistoriaClinicaGeneralException hcge = new HistoriaClinicaGeneralException("Error al insertar Padecimiento actual", HistoriaClinicaGeneralException.LAYER_DAO, HistoriaClinicaGeneralException.ACTION_INSERT);
            hcge.addError(ex.getCause().getMessage());
            ex.printStackTrace();
            throw hcge;
        }
    }

    @Override
    public void updatePadecimientoActual(PadecimientoActualView view, Long idHistoriaClinica) throws HistoriaClinicaGeneralException {
        try {
            PadecimientoActual updated = converter.toEntity(view);
            PadecimientoActual prevEntity = repository.findByIdHistoriaClinica(idHistoriaClinica);
            if(prevEntity == null)
                log.error("Nuevo/Update - Padecimiento actual no registrado para la historia clínica {}", idHistoriaClinica);
            else
                updated.setIdPadecimientoActual(prevEntity.getIdPadecimientoActual());
            updated.setIdHistoriaClinica(idHistoriaClinica);
            repository.save(updated);
            log.info("Editando Padecimiento actual Id: {} - HistoriaClinica: {}",updated.getIdPadecimientoActual(), view.getIdHistoriaClinica());
        } catch (Exception ex) {
            HistoriaClinicaGeneralException hcge = new HistoriaClinicaGeneralException("Error al actualizar Padecimiento actual", HistoriaClinicaGeneralException.LAYER_DAO, HistoriaClinicaGeneralException.ACTION_UPDATE);
            hcge.addError(ex.getMessage());
            ex.printStackTrace();
            throw hcge;
        }
    }

    @Override
    public PadecimientoActualView getPadecimientoActual(Long idHistoriaClinica) throws HistoriaClinicaGeneralException {
        try {
            PadecimientoActual entity = repository.findByIdHistoriaClinica(idHistoriaClinica);
            if(entity == null) {
                HistoriaClinicaGeneralException hcge = new HistoriaClinicaGeneralException("Error al buscar Padecimiento actual", HistoriaClinicaGeneralException.LAYER_DAO, HistoriaClinicaGeneralException.ACTION_SELECT);
                hcge.addError("Padecimiento actual no registrado para la historia clínica " + idHistoriaClinica);
                log.error("Padecimiento actual no registrado para la historia clínica {}", idHistoriaClinica);
                throw hcge;
            }
            return converter.toView(entity);
        } catch (HistoriaClinicaGeneralException ex) {
            throw ex;
        } catch (Exception ex) {
            HistoriaClinicaGeneralException hcge = new HistoriaClinicaGeneralException("Error al actualizar Padecimiento actual", HistoriaClinicaGeneralException.LAYER_DAO, HistoriaClinicaGeneralException.ACTION_SELECT);
            hcge.addError(ex.getCause().getMessage());
            ex.printStackTrace();
            throw hcge;
        }
    }

    @Override
    public void deletePadecimientoActual(Long idHistoriaClinica) throws HistoriaClinicaGeneralException {
        try {
            PadecimientoActual entity = repository.findByIdHistoriaClinica(idHistoriaClinica);
            if(entity != null) {
                repository.delete(entity);
                log.error("Padecimiento actual eliminado para la historia clinica {}", idHistoriaClinica);
            }
        } catch (Exception ex) {
            HistoriaClinicaGeneralException hcge = new HistoriaClinicaGeneralException("Error al actualizar Padecimiento actual", HistoriaClinicaGeneralException.LAYER_DAO, HistoriaClinicaGeneralException.ACTION_SELECT);
            hcge.addError(ex.getCause().getMessage());
            ex.printStackTrace();
            throw hcge;
        }
    }
}
