package net.amentum.niomedic.expediente.service.historia_clinica.personales_patologicos;

import net.amentum.niomedic.expediente.converter.historia_clinica_general.personales_patologicos.PadecimientoPersonalPatologicoConverter;
import net.amentum.niomedic.expediente.converter.historia_clinica_general.personales_patologicos.PersonalesPatologicosConverter;
import net.amentum.niomedic.expediente.exception.HistoriaClinicaGeneralException;
import net.amentum.niomedic.expediente.model.historia_clinica.personales_patologicos.PadecimientoPersonalPatologico;
import net.amentum.niomedic.expediente.model.historia_clinica.personales_patologicos.PersonalesPatologicos;
import net.amentum.niomedic.expediente.persistence.historia_clinica.personales_patologicos.PadecimientoPersonalPatologicoRepository;
import net.amentum.niomedic.expediente.persistence.historia_clinica.personales_patologicos.PersonalesPatologicosRepository;
import net.amentum.niomedic.expediente.views.historia_clinica_general.personales_patologicos.PersonalesPatologicosView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;

@Service
public class PersonalesPatologicosServiceImpl implements PersonalesPatologicosService {
    private final Logger logger = LoggerFactory.getLogger(PersonalesPatologicosServiceImpl.class);

    @Autowired
    PersonalesPatologicosConverter converter;
    @Autowired
    PadecimientoPersonalPatologicoConverter padecimientoConverter;
    @Autowired
    PadecimientoPersonalPatologicoRepository padecimientoRepository;
    @Autowired
    PersonalesPatologicosRepository repository;

    @Override
    public void createPersonalesPatologicos(PersonalesPatologicosView view, Long idHistoriaClinica) throws HistoriaClinicaGeneralException {
        try {
            if(repository.findByIdHistoriaClinica(view.getIdHistoriaClinica()) != null) {
                HistoriaClinicaGeneralException hcge = new HistoriaClinicaGeneralException("Error insertar datos personales patologicos", HistoriaClinicaGeneralException.LAYER_DAO, HistoriaClinicaGeneralException.ACTION_INSERT);
                hcge.addError("Datos personales patologicos previamente registrados");
                logger.error("Datos personales patologicos previamente registrados");
                throw hcge;
            }
            view.setIdHistoriaClinica(idHistoriaClinica);
            PersonalesPatologicos personalesPatologicos = converter.toEntity(view);
            ArrayList<PadecimientoPersonalPatologico> padecimientoList = padecimientoConverter.toArrayEntity(view.getDatosTabla());
            personalesPatologicos.setPadecimientos(padecimientoList);
            for(PadecimientoPersonalPatologico element : padecimientoList) {
                element.setIdHistoriaClinica(view.getIdHistoriaClinica());
                element.setPersonalesPatologicos(personalesPatologicos);
            }
            repository.save(personalesPatologicos);
            logger.info("Agregando Personales Patologicos - HistoriaClinica: {}",view.getIdHistoriaClinica());
        } catch (HistoriaClinicaGeneralException ex) {
            throw ex;
        } catch (Exception ex) {
            HistoriaClinicaGeneralException hcge = new HistoriaClinicaGeneralException("Error insertar datos personales patologicos", HistoriaClinicaGeneralException.LAYER_DAO, HistoriaClinicaGeneralException.ACTION_INSERT);
            hcge.addError(ex.getLocalizedMessage());
            ex.printStackTrace();
            throw hcge;
        }
    }

    @Override
    public void updatePersonalesPatologicos(PersonalesPatologicosView view, Long idHistoriaClinica) throws HistoriaClinicaGeneralException {
        try {
            PersonalesPatologicos personalesPatologicos = converter.toEntity(view);
            PersonalesPatologicos entityPrev = repository.findByIdHistoriaClinica(idHistoriaClinica);
            if(entityPrev == null)
                logger.error("Nuevo/Update - Datos personales patologicos no registrados para la historia clinica " + view.getIdHistoriaClinica());
            else {
                personalesPatologicos.setId(entityPrev.getId());
                padecimientoRepository.delete(entityPrev.getPadecimientos());
            }
            personalesPatologicos.setIdHistoriaClinica(idHistoriaClinica);
            ArrayList<PadecimientoPersonalPatologico> padecimientoList = padecimientoConverter.toArrayEntity(view.getDatosTabla());
            personalesPatologicos.setPadecimientos(padecimientoList);
            for(PadecimientoPersonalPatologico element : padecimientoList) {
                element.setIdHistoriaClinica(idHistoriaClinica);
                element.setPersonalesPatologicos(personalesPatologicos);
            }
            repository.save(personalesPatologicos);
            logger.info("Editando Personales Patologicos - Id: {} - HistoriaClinica: {}",personalesPatologicos.getId(), view.getIdHistoriaClinica());
        } catch (Exception ex) {
            HistoriaClinicaGeneralException hcge = new HistoriaClinicaGeneralException("Error al actualizar datos personales patologicos", HistoriaClinicaGeneralException.LAYER_DAO, HistoriaClinicaGeneralException.ACTION_UPDATE);
            hcge.addError(ex.toString());
            logger.error("Error: " + ex);
            throw hcge;
        }
    }

    @Override
    public PersonalesPatologicosView getPersonalesPatologicos(Long idHistoriaClinica) throws HistoriaClinicaGeneralException {
        try {
            PersonalesPatologicos entity = repository.findByIdHistoriaClinica(idHistoriaClinica);
            if(entity == null) {
                HistoriaClinicaGeneralException hcge = new HistoriaClinicaGeneralException("Error al buscar datos personales patologicos", HistoriaClinicaGeneralException.LAYER_DAO, HistoriaClinicaGeneralException.ACTION_SELECT);
                hcge.addError("Datos personales patologicos no registrados para la historia clinica " + idHistoriaClinica);
                logger.error("Datos personales patologicos no registrados para la historia clinica " + idHistoriaClinica);
                throw hcge;
            }
            PersonalesPatologicosView view = converter.toView(entity);
            view.setDatosTabla(padecimientoConverter.toArrayView(entity.getPadecimientos()));
            return view;
        } catch (HistoriaClinicaGeneralException ex) {
            throw ex;
        } catch (Exception ex) {
            HistoriaClinicaGeneralException hcge = new HistoriaClinicaGeneralException("Error al buscar habitos alimenticios", HistoriaClinicaGeneralException.LAYER_DAO, HistoriaClinicaGeneralException.ACTION_SELECT);
            hcge.addError(ex.getLocalizedMessage());
            logger.error("Error: " + ex);
            throw hcge;
        }
    }

    @Override
    public void deletePersonalesPatologicos(Long idHistoriaClinica) {
        try {
            PersonalesPatologicos personalesPatologicos = repository.findByIdHistoriaClinica(idHistoriaClinica);
            if(personalesPatologicos != null) {
                repository.delete(personalesPatologicos);
                logger.error("Datos personales patológicos borrado para " + idHistoriaClinica);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
