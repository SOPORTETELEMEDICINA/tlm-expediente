package net.amentum.niomedic.expediente.service.historia_clinica.tratamiento;

import lombok.extern.slf4j.Slf4j;
import net.amentum.niomedic.expediente.converter.historia_clinica_general.tratamiento.HCTratamientoConverter;
import net.amentum.niomedic.expediente.exception.HistoriaClinicaGeneralException;
import net.amentum.niomedic.expediente.model.historia_clinica.tratamiento.HCTratamiento;
import net.amentum.niomedic.expediente.model.historia_clinica.tratamiento.TratamientoCie9;
import net.amentum.niomedic.expediente.persistence.historia_clinica.tratamiento.HCTratamientoRepository;
import net.amentum.niomedic.expediente.persistence.historia_clinica.tratamiento.TratamientoCie9Repository;
import net.amentum.niomedic.expediente.views.historia_clinica_general.tratamiento.HCTratamientoView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class HCTratamientoServiceImpl implements HCTratamientoService {

    @Autowired
    HCTratamientoRepository repository;

    @Autowired
    TratamientoCie9Repository tratamientoCie9Repository;

    @Autowired
    HCTratamientoConverter converter;

    @Override
    public void createTratamiento(HCTratamientoView view, Long idHistoriaClinica) throws HistoriaClinicaGeneralException {
        try {
            if(repository.findByIdHistoriaClinica(idHistoriaClinica) != null) {
                HistoriaClinicaGeneralException hcge = new HistoriaClinicaGeneralException("Error al insertar tratamiento", HistoriaClinicaGeneralException.LAYER_DAO, HistoriaClinicaGeneralException.ACTION_INSERT);
                hcge.addError("Tratamiento ya registrado para la historia clínica " + idHistoriaClinica);
                log.error("Tratamiento ya registrado para la historia clínica {}", idHistoriaClinica);
                throw hcge;
            }
            view.setIdHistoriaClinica(idHistoriaClinica);
            repository.save(converter.toEntity(view));
            log.info("Agregando tratamiento - HistoriaClinica: {}",view.getIdHistoriaClinica());
        } catch (HistoriaClinicaGeneralException ex) {
            throw ex;
        } catch (Exception ex) {
            HistoriaClinicaGeneralException hcge = new HistoriaClinicaGeneralException("Error al insertar Tratamiento", HistoriaClinicaGeneralException.LAYER_DAO, HistoriaClinicaGeneralException.ACTION_INSERT);
            hcge.addError(ex.getCause().getMessage());
            ex.printStackTrace();
            throw hcge;
        }
    }

    @Override
    public void updateTratamiento(HCTratamientoView view, Long idHistoriaClinica) throws HistoriaClinicaGeneralException {
        try {
            HCTratamiento updated = converter.toEntity(view);
            HCTratamiento prevEntity = repository.findByIdHistoriaClinica(idHistoriaClinica);
            if(prevEntity == null)
                log.error("Nuevo/Update - Tratamiento no registrado para la historia clínica {}", idHistoriaClinica);
            else {
                updated.setIdTratamiento(prevEntity.getIdTratamiento());
                for(TratamientoCie9 element : prevEntity.getTratamientoCie9s()) {
                    if(!existCie9(element.getTratamiento().getIdTratamiento(), element.getCatCie9().getIdCie9(), updated.getTratamientoCie9s()))
                        tratamientoCie9Repository.delete(element);
                }
                prevEntity.getTratamientoCie9s().clear();
            }
            updated.setIdHistoriaClinica(idHistoriaClinica);
            repository.save(updated);
            log.info("Editando tratamiento - Id: {} - HistoriaClinica: {}",updated.getIdTratamiento(), view.getIdHistoriaClinica());
        } catch (Exception ex) {
            HistoriaClinicaGeneralException hcge = new HistoriaClinicaGeneralException("Error al actualizar Tratamiento", HistoriaClinicaGeneralException.LAYER_DAO, HistoriaClinicaGeneralException.ACTION_UPDATE);
            hcge.addError(ex.toString());
            ex.printStackTrace();
            throw hcge;
        }
    }

    @Override
    public HCTratamientoView getTratamiento(Long idHistoriaClinica) throws HistoriaClinicaGeneralException {
        try {
            HCTratamiento entity = repository.findByIdHistoriaClinica(idHistoriaClinica);
            if(entity == null) {
                HistoriaClinicaGeneralException hcge = new HistoriaClinicaGeneralException("Error al buscar tratamiento", HistoriaClinicaGeneralException.LAYER_DAO, HistoriaClinicaGeneralException.ACTION_SELECT);
                hcge.addError("Tratamiento no registrado para la historia clínica " + idHistoriaClinica);
                log.error("Tratamiento no registrado para la historia clínica {}", idHistoriaClinica);
                throw hcge;
            }
            return converter.toView(entity);
        } catch (HistoriaClinicaGeneralException ex) {
            throw ex;
        } catch (Exception ex) {
            HistoriaClinicaGeneralException hcge = new HistoriaClinicaGeneralException("Error al actualizar diagnostico", HistoriaClinicaGeneralException.LAYER_DAO, HistoriaClinicaGeneralException.ACTION_SELECT);
            hcge.addError(ex.getCause().getMessage());
            ex.printStackTrace();
            throw hcge;
        }
    }

    @Override
    public void deleteTratamiento(Long idHistoriaClinica) throws HistoriaClinicaGeneralException {
        try {
            HCTratamiento entity = repository.findByIdHistoriaClinica(idHistoriaClinica);
            if(entity != null) {
                repository.delete(entity);
                log.error("Tratamiento eliminado para la historia clinica {}", idHistoriaClinica);
            }
        } catch (Exception ex) {
            HistoriaClinicaGeneralException hcge = new HistoriaClinicaGeneralException("Error al eliminar tratamiento", HistoriaClinicaGeneralException.LAYER_DAO, HistoriaClinicaGeneralException.ACTION_DELETE);
            hcge.addError(ex.getCause().getMessage());
            ex.printStackTrace();
            throw hcge;
        }
    }

    private Boolean existCie9(Long idTratamiento, Long idCie9, List<TratamientoCie9> tratamientoCie9s) {
        List<TratamientoCie9> list = tratamientoCie9s.stream().filter(tratamientoCie9 -> tratamientoCie9.getTratamiento().getIdTratamiento().compareTo(idTratamiento) == 0
                && tratamientoCie9.getCatCie9().getIdCie9().compareTo(idCie9) == 0).collect(Collectors.toList());
        return !list.isEmpty();
    }
}
