package net.amentum.niomedic.expediente.service.historia_clinica.heredo_familiares;

import lombok.extern.slf4j.Slf4j;
import net.amentum.niomedic.expediente.converter.historia_clinica_general.heredo_familiares.HeredoFamiliaresConverter;
import net.amentum.niomedic.expediente.exception.HistoriaClinicaGeneralException;
import net.amentum.niomedic.expediente.model.historia_clinica.exploracion_fisica.ExploracionFisica;
import net.amentum.niomedic.expediente.model.historia_clinica.heredo_familiares.Enfermedades;
import net.amentum.niomedic.expediente.model.historia_clinica.heredo_familiares.HeredoFamiliares;
import net.amentum.niomedic.expediente.persistence.historia_clinica.heredo_familiares.EnfermedadesRepository;
import net.amentum.niomedic.expediente.persistence.historia_clinica.heredo_familiares.HeredoFamiliaresRepository;
import net.amentum.niomedic.expediente.service.historia_clinica.heredo_familiares.HeredoFamiliaresService;
import net.amentum.niomedic.expediente.views.historia_clinica_general.heredo_familiares.HeredoFamiliaresView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class HeredoFamiliaresServiceImplement implements HeredoFamiliaresService {

    @Autowired
    HeredoFamiliaresRepository repository;

    @Autowired
    EnfermedadesRepository enfermedadesRepository;

    @Autowired
    HeredoFamiliaresConverter converter;

    @Override
    public void createHeredoFamiliares(HeredoFamiliaresView view, Long idHistoriaClinica) throws HistoriaClinicaGeneralException {
        try {
            if(repository.findByIdHistoriaClinica(idHistoriaClinica) != null) {
                HistoriaClinicaGeneralException hcge = new HistoriaClinicaGeneralException("Error al insertar Heredo familiares", HistoriaClinicaGeneralException.LAYER_DAO, HistoriaClinicaGeneralException.ACTION_INSERT);
                hcge.addError("Datos heredo familiares ya registrados para la historia clínica " + idHistoriaClinica);
                log.error("Datos heredo familiares ya registrados para la historia clínica {}", idHistoriaClinica);
                throw hcge;
            }
            view.setIdHistoriaClinica(idHistoriaClinica);
            HeredoFamiliares entity = converter.toEntity(view);
            for(Enfermedades element : entity.getEnfermedadesList()) {
                element.setIdHistoriaClinica(idHistoriaClinica);
                element.setHeredoFamiliares(entity);
            }
            repository.save(entity);
            log.info("Agregando Heredo familiares - HistoriaClinica: {}", view.getIdHistoriaClinica());
        } catch(HistoriaClinicaGeneralException ex) {
            throw ex;
        } catch (Exception ex) {
            HistoriaClinicaGeneralException hcge = new HistoriaClinicaGeneralException("Error al insertar Interrogatorio aparatos", HistoriaClinicaGeneralException.LAYER_DAO, HistoriaClinicaGeneralException.ACTION_INSERT);
            hcge.addError(ex.getCause().toString());
            ex.printStackTrace();
            throw hcge;
        }
    }

    @Override
    public void updateHeredoFamiliares(HeredoFamiliaresView view, Long idHistoriaClinica) throws HistoriaClinicaGeneralException {
        try {
            HeredoFamiliares entity = converter.toEntity(view);
            HeredoFamiliares prevEntity = repository.findByIdHistoriaClinica(idHistoriaClinica);
            if(prevEntity == null)
                log.error("Nuevo/Update - Datos heredo familiares no registrados para la historia clínica {}", idHistoriaClinica);
            else {
                entity.setIdHeredoFamiliares(prevEntity.getIdHeredoFamiliares());
                enfermedadesRepository.delete(prevEntity.getEnfermedadesList());
            }
            entity.setIdHistoriaClinica(idHistoriaClinica);
            for(Enfermedades element : entity.getEnfermedadesList()) {
                element.setIdHistoriaClinica(idHistoriaClinica);
                element.setHeredoFamiliares(entity);
            }
            repository.save(entity);
            log.info("Editando Heredo familiares - Id: {} - HistoriaClinica: {}", entity.getIdHeredoFamiliares(), view.getIdHistoriaClinica());
        } catch (Exception ex) {
            HistoriaClinicaGeneralException hcge = new HistoriaClinicaGeneralException("Error al insertar Interrogatorio aparatos", HistoriaClinicaGeneralException.LAYER_DAO, HistoriaClinicaGeneralException.ACTION_INSERT);
            hcge.addError(ex.toString());
            ex.printStackTrace();
            throw hcge;
        }
    }

    @Override
    public HeredoFamiliaresView getHeredoFamiliares(Long idHistoriaClinica) throws HistoriaClinicaGeneralException {
        try {
            HeredoFamiliares heredoFamiliares = repository.findByIdHistoriaClinica(idHistoriaClinica);
            if(heredoFamiliares == null) {
                HistoriaClinicaGeneralException hcge = new HistoriaClinicaGeneralException("Error al obtener datos Heredo familiares", HistoriaClinicaGeneralException.LAYER_DAO, HistoriaClinicaGeneralException.ACTION_SELECT);
                hcge.addError("Heredo familiares no registrados para la historia clínica " + idHistoriaClinica);
                log.error("Heredo familiares no registrados para la historia clínica {}", idHistoriaClinica);
                throw hcge;
            }
            return converter.toView(heredoFamiliares);
        } catch(HistoriaClinicaGeneralException ex) {
            throw ex;
        }catch (Exception ex) {
            HistoriaClinicaGeneralException hcge = new HistoriaClinicaGeneralException("Error al obtener datos Heredo familiares", HistoriaClinicaGeneralException.LAYER_DAO, HistoriaClinicaGeneralException.ACTION_SELECT);
            hcge.addError(ex.getCause().toString());
            ex.printStackTrace();
            throw hcge;
        }
    }

    @Override
    public void deleteHeredoFamiliares(Long idHistoriaClinica) throws HistoriaClinicaGeneralException {
        try {
            HeredoFamiliares heredoFamiliares = repository.findByIdHistoriaClinica(idHistoriaClinica);
            if(heredoFamiliares != null) {
                repository.delete(heredoFamiliares);
                log.error("Datos heredo familiares borrados para " + idHistoriaClinica);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            HistoriaClinicaGeneralException hcge = new HistoriaClinicaGeneralException("Error al eliminar Heredo familiares", HistoriaClinicaGeneralException.LAYER_DAO, HistoriaClinicaGeneralException.ACTION_DELETE);
            hcge.addError(ex.getLocalizedMessage());
            throw hcge;
        }
    }
}
