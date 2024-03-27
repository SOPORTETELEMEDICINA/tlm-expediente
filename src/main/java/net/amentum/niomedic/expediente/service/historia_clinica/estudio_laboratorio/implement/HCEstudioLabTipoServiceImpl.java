package net.amentum.niomedic.expediente.service.historia_clinica.estudio_laboratorio.implement;

import net.amentum.niomedic.expediente.converter.historia_clinica_general.estudio_laboratorio.HCEstudioLabTipoConverter;
import net.amentum.niomedic.expediente.exception.HistoriaClinicaGeneralException;
import net.amentum.niomedic.expediente.model.historia_clinica.estudio_laboratorio.HCEstudioLabTipo;
import net.amentum.niomedic.expediente.persistence.historia_clinica.estudio_laboratorio.HCEstudioLabTipoRepository;
import net.amentum.niomedic.expediente.service.historia_clinica.estudio_laboratorio.HCEstudioLabTipoService;
import net.amentum.niomedic.expediente.views.historia_clinica_general.estudio_laboratorio.HCEstudioLabEstudioView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HCEstudioLabTipoServiceImpl implements HCEstudioLabTipoService {

    private final Logger logger = LoggerFactory.getLogger(HCEstudioLabTipoServiceImpl.class);

    @Autowired
    HCEstudioLabTipoConverter converter;

    @Autowired
    HCEstudioLabTipoRepository repository;

    @Override
    public void createEstudioLabTpo(HCEstudioLabEstudioView view) throws HistoriaClinicaGeneralException {
        try {
            repository.save(converter.toEntity(view));
            logger.info("Agregando Estudio Laboratorio Tipo - HistoriaClinica: {}", view.getIdHistoriaClinica());
        } catch (Exception ex) {
            HistoriaClinicaGeneralException hcge = new HistoriaClinicaGeneralException("Error insertar tipo de estudio laboratorio", HistoriaClinicaGeneralException.LAYER_DAO, HistoriaClinicaGeneralException.ACTION_INSERT);
            hcge.addError(ex.getLocalizedMessage());
            logger.error("Error: " + ex);
            throw hcge;
        }
    }

    @Override
    public void updateEstudioLabTpo(HCEstudioLabEstudioView view) throws HistoriaClinicaGeneralException {
        try {
            HCEstudioLabTipo entity = repository.findByidTipoEstudio(view.getId());
            if(entity == null) {
                HistoriaClinicaGeneralException hcge = new HistoriaClinicaGeneralException("Error al actualizar tipo de estudio laboratorio", HistoriaClinicaGeneralException.LAYER_DAO, HistoriaClinicaGeneralException.ACTION_SELECT);
                hcge.addError("Tipo de estudio laboratorio no registrados para la historia clinica " + view.getIdHistoriaClinica());
                logger.error("Tipo de estudio laboratorio no registrados para la historia clinica " + view.getIdHistoriaClinica());
                throw hcge;
            }
            entity.setTipo(view.getTipo());
            entity.setDescripcion(view.getDescripcion());
            entity.setComentarios(view.getComentarios());
            repository.save(entity);
        } catch (HistoriaClinicaGeneralException ex) {
            throw ex;
        } catch (Exception ex) {
            HistoriaClinicaGeneralException hcge = new HistoriaClinicaGeneralException("Error al actualizar documento de estudio laboratorio", HistoriaClinicaGeneralException.LAYER_DAO, HistoriaClinicaGeneralException.ACTION_UPDATE);
            hcge.addError(ex.getLocalizedMessage());
            logger.error("Error: " + ex);
            throw hcge;
        }
    }

    @Override
    public List<HCEstudioLabEstudioView> getEstudioLabTipo(Long idEstudioLab, Long idHistoriaClinica) throws HistoriaClinicaGeneralException {
        try {
            List<HCEstudioLabTipo> entities = repository.findByIdHistoriaClinicaAndIdEstudioLaboratorio(idHistoriaClinica, idEstudioLab);
            List<HCEstudioLabEstudioView> viewList = new ArrayList<>();
            entities.forEach(estudioLabTipo -> viewList.add(converter.toView(estudioLabTipo)));
            return viewList;
        } catch (Exception ex) {
            HistoriaClinicaGeneralException hcge = new HistoriaClinicaGeneralException("Error al buscar tipo de estudio laboratorio", HistoriaClinicaGeneralException.LAYER_DAO, HistoriaClinicaGeneralException.ACTION_SELECT);
            hcge.addError(ex.getLocalizedMessage());
            logger.error("Error: " + ex);
            throw hcge;
        }
    }

    @Override
    public void deleteEstudioLabTipo(Long idEstudioLab, Long idHistoriaClinica) {
        repository.delete(repository.findByIdHistoriaClinicaAndIdEstudioLaboratorio(idHistoriaClinica, idEstudioLab));
        logger.info("Tipo de estudio lab borrados para estudio {} en Historia Clinica {}", idEstudioLab, idHistoriaClinica);
    }
}
