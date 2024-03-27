package net.amentum.niomedic.expediente.service.historia_clinica.antecendentes_ginecobstetricos;

import lombok.extern.slf4j.Slf4j;
import net.amentum.niomedic.expediente.converter.historia_clinica_general.antecendentes_ginecobstetricos.AntecendentesGinecobstetricosConverter;
import net.amentum.niomedic.expediente.exception.HistoriaClinicaGeneralException;
import net.amentum.niomedic.expediente.model.historia_clinica.antecendentes_ginecobstetricos.AntecendentesGinecobstetricos;
import net.amentum.niomedic.expediente.persistence.historia_clinica.antecendentes_ginecobstetricos.AntecendentesGinecobstetricosRepository;
import net.amentum.niomedic.expediente.views.historia_clinica_general.antecendentes_ginecobstetricos.AntecendentesGinecobstetricosView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AntecendentesGinecobstetricosServiceImpl implements AntecendentesGinecobstetricosService {

    @Autowired
    AntecendentesGinecobstetricosRepository repository;
    @Autowired
    AntecendentesGinecobstetricosConverter converter;

    @Override
    public void createAntecendentesGinecobstetricos(AntecendentesGinecobstetricosView view, Long idHistoriaClinica) throws HistoriaClinicaGeneralException {
        try {
            if(repository.findByIdHistoriaClinica(idHistoriaClinica) != null) {
                HistoriaClinicaGeneralException hcge = new HistoriaClinicaGeneralException("Error al insertar Antecendentes Ginecobstetricos", HistoriaClinicaGeneralException.LAYER_DAO, HistoriaClinicaGeneralException.ACTION_INSERT);
                hcge.addError("Antecendentes Ginecobstetricos ya registrado para la historia clínica " + idHistoriaClinica);
                log.error("Antecendentes Ginecobstetricos ya registrado para la historia clínica {}", idHistoriaClinica);
                throw hcge;
            }
            view.setIdHistoriaClinica(idHistoriaClinica);
            repository.save(converter.toEntity(view));
            log.info("Agregando Antecendentes Ginecobstetricos - HistoriaClinica: {}", idHistoriaClinica);
        } catch (HistoriaClinicaGeneralException ex) {
            throw ex;
        } catch (Exception ex) {
            HistoriaClinicaGeneralException hcge = new HistoriaClinicaGeneralException("Error al insertar Antecendentes Ginecobstetricos", HistoriaClinicaGeneralException.LAYER_DAO, HistoriaClinicaGeneralException.ACTION_INSERT);
            hcge.addError(ex.getCause().toString());
            ex.printStackTrace();
            throw hcge;
        }
    }

    @Override
    public void updateAntecendentesGinecobstetricos(AntecendentesGinecobstetricosView view, Long idHistoriaClinica) throws HistoriaClinicaGeneralException {
        try {
            AntecendentesGinecobstetricos updated = converter.toEntity(view);
            AntecendentesGinecobstetricos prevEntity = repository.findByIdHistoriaClinica(idHistoriaClinica);
            if(prevEntity == null)
                log.error("Nueva/Update - Antecendentes Ginecobstetricos no registrados para la historia clínica {}", idHistoriaClinica);
            else
                updated.setIdAntecendentesGinecobstetricos(prevEntity.getIdAntecendentesGinecobstetricos());
            updated.setIdHistoriaClinica(idHistoriaClinica);
            repository.save(updated);
            log.info("Editando Antecendentes Ginecobstetricos - Id: {} - HistoriaClinica: {}",
                    updated.getIdAntecendentesGinecobstetricos(), idHistoriaClinica);
        } catch (Exception ex) {
            HistoriaClinicaGeneralException hcge = new HistoriaClinicaGeneralException("Error al actualizar Interrogatorio aparatos", HistoriaClinicaGeneralException.LAYER_DAO, HistoriaClinicaGeneralException.ACTION_UPDATE);
            hcge.addError(ex.toString());
            ex.printStackTrace();
            throw hcge;
        }
    }

    @Override
    public AntecendentesGinecobstetricosView getAntecendentesGinecobstetricos(Long idHistoriaClinica) throws HistoriaClinicaGeneralException {
        try {
            AntecendentesGinecobstetricos entity = repository.findByIdHistoriaClinica(idHistoriaClinica);
            if(entity == null) {
                HistoriaClinicaGeneralException hcge = new HistoriaClinicaGeneralException("Error al obtener Antecendentes Ginecobstetricos", HistoriaClinicaGeneralException.LAYER_DAO, HistoriaClinicaGeneralException.ACTION_SELECT);
                hcge.addError("Antecendentes Ginecobstetricos no registrados para la historia clínica " + idHistoriaClinica);
                log.error("Antecendentes Ginecobstetricos no registrados para la historia clínica {}", idHistoriaClinica);
                throw hcge;
            }
            return converter.toView(entity);
        } catch (HistoriaClinicaGeneralException ex) {
            throw ex;
        } catch (Exception ex) {
            HistoriaClinicaGeneralException hcge = new HistoriaClinicaGeneralException("Error al obtener Antecendentes Ginecobstetricos", HistoriaClinicaGeneralException.LAYER_DAO, HistoriaClinicaGeneralException.ACTION_UPDATE);
            hcge.addError(ex.getCause().toString());
            ex.printStackTrace();
            throw hcge;
        }
    }

    @Override
    public void deleteAntecendentesGinecobstetricos(Long idHistoriaClinica) throws HistoriaClinicaGeneralException {
        try {
            AntecendentesGinecobstetricos entity = repository.findByIdHistoriaClinica(idHistoriaClinica);
            if(entity != null) {
                repository.delete(entity);
                log.error("Antecendentes Ginecobstetricos eliminados para la historia clinica {}", idHistoriaClinica);
            }
        } catch (Exception ex) {
            HistoriaClinicaGeneralException hcge = new HistoriaClinicaGeneralException("Error al eliminar Antecendentes Ginecobstetricos", HistoriaClinicaGeneralException.LAYER_DAO, HistoriaClinicaGeneralException.ACTION_DELETE);
            hcge.addError(ex.getLocalizedMessage());
            log.error("Error: " + ex);
            throw hcge;
        }
    }
}
