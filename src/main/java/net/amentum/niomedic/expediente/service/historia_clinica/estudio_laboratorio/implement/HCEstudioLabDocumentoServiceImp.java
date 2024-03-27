package net.amentum.niomedic.expediente.service.historia_clinica.estudio_laboratorio.implement;

import net.amentum.niomedic.expediente.converter.historia_clinica_general.estudio_laboratorio.HCEstudioLabDocumentoConverter;
import net.amentum.niomedic.expediente.exception.HistoriaClinicaGeneralException;
import net.amentum.niomedic.expediente.model.historia_clinica.estudio_laboratorio.HCEstudioLabDocumento;
import net.amentum.niomedic.expediente.persistence.historia_clinica.estudio_laboratorio.HCEstudioLabDocumentoRepository;
import net.amentum.niomedic.expediente.service.historia_clinica.estudio_laboratorio.HCEstudioLabDocumentoService;
import net.amentum.niomedic.expediente.views.historia_clinica_general.estudio_laboratorio.HCEstudioLabDocumentoView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HCEstudioLabDocumentoServiceImp implements HCEstudioLabDocumentoService {

    private final Logger logger = LoggerFactory.getLogger(HCEstudioLabDocumentoServiceImp.class);

    @Autowired
    HCEstudioLabDocumentoRepository repository;

    @Autowired
    HCEstudioLabDocumentoConverter converter;

    @Override
    public void createEstudioLabDocumento(HCEstudioLabDocumentoView view) throws HistoriaClinicaGeneralException {
        try {
            repository.save(converter.toEntity(view));
            logger.info("Agregando Estudio Laboratorio Documento - HistoriaClinica: {}", view.getIdHistoriaClinica());
        } catch (Exception ex) {
            HistoriaClinicaGeneralException hcge = new HistoriaClinicaGeneralException("Error insertar documento de estudio laboratorio", HistoriaClinicaGeneralException.LAYER_DAO, HistoriaClinicaGeneralException.ACTION_INSERT);
            hcge.addError(ex.getLocalizedMessage());
            logger.error("Error: " + ex);
            throw hcge;
        }
    }

    @Override
    public void updateEstudioLabDocumento(HCEstudioLabDocumentoView view) throws HistoriaClinicaGeneralException {
        try {
            HCEstudioLabDocumento prevDoc = repository.findByIdDocumento(view.getIdDocumento());
            if(prevDoc == null) {
                HistoriaClinicaGeneralException hcge = new HistoriaClinicaGeneralException("Error al actualizar documento de estudio laboratorio", HistoriaClinicaGeneralException.LAYER_DAO, HistoriaClinicaGeneralException.ACTION_SELECT);
                hcge.addError("Documento de estudio laboratorio no registrados para la historia clinica " + view.getIdHistoriaClinica());
                logger.error("Documento de estudio laboratorio no registrados para la historia clinica " + view.getIdHistoriaClinica());
                throw hcge;
            }
            HCEstudioLabDocumento updated = converter.toEntity(view);
            updated.setIdDocumento(prevDoc.getIdDocumento());
            updated.setIdHistoriaClinica(prevDoc.getIdHistoriaClinica());
            updated.setIdEstudioLaboratorio(prevDoc.getIdEstudioLaboratorio());
            repository.save(updated);
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
    public List<HCEstudioLabDocumentoView> getEstudioLabDocumento(Long idEstudioLab, Long idHistoriaClinica) throws HistoriaClinicaGeneralException {
        try {
            List<HCEstudioLabDocumento> entities = repository.findByIdHistoriaClinicaAndIdEstudioLaboratorio(idHistoriaClinica, idEstudioLab);
            List<HCEstudioLabDocumentoView> viewList = new ArrayList<>();
            entities.forEach(estudioLabDocumento -> viewList.add(converter.toView(estudioLabDocumento)));
            return viewList;
        } catch (Exception ex) {
            HistoriaClinicaGeneralException hcge = new HistoriaClinicaGeneralException("Error al buscar documento de estudio laboratorio", HistoriaClinicaGeneralException.LAYER_DAO, HistoriaClinicaGeneralException.ACTION_SELECT);
            hcge.addError(ex.getLocalizedMessage());
            logger.error("Error: " + ex);
            throw hcge;
        }
    }

    @Override
    public void deleteEstudioLabDocumento(Long idEstudioLab, Long idHistoriaClinica) throws HistoriaClinicaGeneralException {
        try {
            List<HCEstudioLabDocumento> list = repository.findByIdHistoriaClinicaAndIdEstudioLaboratorio(idHistoriaClinica, idEstudioLab);
            if(list != null && !list.isEmpty()) {
                repository.delete(list);
                logger.info("Documentos borrados para estudio {} en Historia Clinica {}", idEstudioLab, idHistoriaClinica);
            }
        } catch (Exception ex) {
            HistoriaClinicaGeneralException hcge = new HistoriaClinicaGeneralException("Error al eliminar documento de estudio laboratorio", HistoriaClinicaGeneralException.LAYER_DAO, HistoriaClinicaGeneralException.ACTION_DELETE);
            hcge.addError(ex.getLocalizedMessage());
            logger.error("Error: " + ex);
            throw hcge;
        }
    }
}
