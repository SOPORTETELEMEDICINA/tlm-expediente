package net.amentum.niomedic.expediente.service.historia_clinica.estudio_laboratorio.implement;

import net.amentum.niomedic.expediente.configuration.ApiConfiguration;
import net.amentum.niomedic.expediente.converter.historia_clinica_general.estudio_laboratorio.HCEstudioLaboratorioConverter;
import net.amentum.niomedic.expediente.exception.HistoriaClinicaGeneralException;
import net.amentum.niomedic.expediente.model.historia_clinica.estudio_laboratorio.HCEstudioLaboratorio;
import net.amentum.niomedic.expediente.persistence.historia_clinica.estudio_laboratorio.HCEstudioLaboratorioRepository;
import net.amentum.niomedic.expediente.service.historia_clinica.estudio_laboratorio.HCEstudioLabDocumentoService;
import net.amentum.niomedic.expediente.service.historia_clinica.estudio_laboratorio.HCEstudioLabTipoService;
import net.amentum.niomedic.expediente.service.historia_clinica.estudio_laboratorio.HCEstudioLaboratorioService;
import net.amentum.niomedic.expediente.views.historia_clinica_general.estudio_laboratorio.HCEstudioLabDocumentoView;
import net.amentum.niomedic.expediente.views.historia_clinica_general.estudio_laboratorio.HCEstudioLabEstudioView;
import net.amentum.niomedic.expediente.views.historia_clinica_general.estudio_laboratorio.HCEstudioLaboratorioView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;


@Service
@Transactional(readOnly = true)
public class HCEstudioLaboratorioServiceImpl implements HCEstudioLaboratorioService {
    private final Logger logger = LoggerFactory.getLogger(HCEstudioLaboratorioServiceImpl.class);
    private final Map<String, Object> colOrderNames = new HashMap<>();
    private HCEstudioLaboratorioRepository estudioLaboratorioRepository;
    private HCEstudioLaboratorioConverter estudioLaboratorioConverter;
    private ApiConfiguration apiConfiguration;

    {
        colOrderNames.put("consultaId", "consultaId");
        colOrderNames.put("fechaCreacion", "fechaCreacion");
        colOrderNames.put("idPaciente", "idPaciente");
    }

    @Autowired
    public void setEstudioLaboratorioRepository(HCEstudioLaboratorioRepository estudioLaboratorioRepository) {
        this.estudioLaboratorioRepository = estudioLaboratorioRepository;
    }

    @Autowired
    public void setEstudioLaboratorioConverter(HCEstudioLaboratorioConverter estudioLaboratorioConverter) {
        this.estudioLaboratorioConverter = estudioLaboratorioConverter;
    }

    @Autowired
    public void setApiConfiguration(ApiConfiguration apiConfiguration) {
        this.apiConfiguration = apiConfiguration;
    }

    @Autowired
    HCEstudioLabDocumentoService estudioLabDocumentoService;

    @Autowired
    HCEstudioLabTipoService estudioLabTipoService;

    @Transactional()
    @Override
    public void createEstudioLaboratorio(HCEstudioLaboratorioView view, Long idHistoriaClinica) throws HistoriaClinicaGeneralException {
        try{
            view.setIdHistoriaClinica(idHistoriaClinica);
            HCEstudioLaboratorio entity = estudioLaboratorioConverter.toEntity(view);
            estudioLaboratorioRepository.save(entity);
            logger.info("Agregando Estudio Laboratorio - HistoriaClinica: {}", idHistoriaClinica);
            if(view.getListaEstudios() != null) {
                for(HCEstudioLabEstudioView estudioView : view.getListaEstudios()) {
                    estudioView.setIdEstudioLaboratorio(entity.getIdEstudioLaboratorio());
                    estudioView.setIdHistoriaClinica(idHistoriaClinica);
                    estudioLabTipoService.createEstudioLabTpo(estudioView);
                }
            }
            if(view.getListaDocumentos() != null) {
                for(HCEstudioLabDocumentoView documentoView : view.getListaDocumentos()) {
                    documentoView.setIdEstudioLaboratorio(entity.getIdEstudioLaboratorio());
                    documentoView.setIdHistoriaClinica(idHistoriaClinica);
                    estudioLabDocumentoService.createEstudioLabDocumento(documentoView);
                }
            }
        } catch (HistoriaClinicaGeneralException ex) {
            throw ex;
        } catch (Exception ex) {
            HistoriaClinicaGeneralException hcge = new HistoriaClinicaGeneralException("Error insertar datos estudio laboratorio", HistoriaClinicaGeneralException.LAYER_DAO, HistoriaClinicaGeneralException.ACTION_INSERT);
            hcge.addError(ex.getLocalizedMessage());
            logger.error("Error: " + ex);
            throw hcge;
        }
    }

    @Override
    public void updateEstudioLaboratorio(HCEstudioLaboratorioView view, Long idHistoriaClinica) throws HistoriaClinicaGeneralException {
        try {
            HCEstudioLaboratorio updated = estudioLaboratorioConverter.toEntity(view);
            HCEstudioLaboratorio entity = estudioLaboratorioRepository.findByidHistoriaClinica(idHistoriaClinica);
            if(entity == null)
                logger.error("Nuevo/Update - No hay datos registrados en estudios laboratorio para la historia clinica: " + idHistoriaClinica);
            else
                updated.setIdEstudioLaboratorio(entity.getIdEstudioLaboratorio());
            updated.setIdHistoriaClinica(idHistoriaClinica);
            estudioLaboratorioRepository.save(updated);
            logger.info("Editando Estudio Laboratorio - Id: {} - HistoriaClinica: {}", updated.getIdEstudioLaboratorio(), idHistoriaClinica);
            if(view.getListaEstudios() != null) {
                estudioLabTipoService.deleteEstudioLabTipo(updated.getIdEstudioLaboratorio(), idHistoriaClinica);
                for (HCEstudioLabEstudioView estudioView : view.getListaEstudios()) {
                    estudioView.setIdEstudioLaboratorio(updated.getIdEstudioLaboratorio());
                    estudioView.setIdHistoriaClinica(idHistoriaClinica);
                    estudioLabTipoService.createEstudioLabTpo(estudioView);
                }
            }

            if(view.getListaDocumentos() != null) {
                estudioLabDocumentoService.deleteEstudioLabDocumento(updated.getIdEstudioLaboratorio(), idHistoriaClinica);
                for(HCEstudioLabDocumentoView documentoView : view.getListaDocumentos()) {
                    documentoView.setIdEstudioLaboratorio(updated.getIdEstudioLaboratorio());
                    documentoView.setIdHistoriaClinica(idHistoriaClinica);
                    estudioLabDocumentoService.createEstudioLabDocumento(documentoView);
                }
            }
        } catch (HistoriaClinicaGeneralException ex) {
            throw ex;
        } catch (Exception ex) {
            HistoriaClinicaGeneralException hcge = new HistoriaClinicaGeneralException("Error insertar datos personales patologicos", HistoriaClinicaGeneralException.LAYER_DAO, HistoriaClinicaGeneralException.ACTION_INSERT);
            hcge.addError(ex.toString());
            logger.error("Error: " + ex);
            throw hcge;
        }
    }

    @Override
    public HCEstudioLaboratorioView getEstudioLaboratorio(Long idHistoriaClinica) throws HistoriaClinicaGeneralException {
        try {
            HCEstudioLaboratorio entity = estudioLaboratorioRepository.findByidHistoriaClinica(idHistoriaClinica);
            if(entity == null) {
                HistoriaClinicaGeneralException hcge = new HistoriaClinicaGeneralException("Error obtener datos estudio laboratorio", HistoriaClinicaGeneralException.LAYER_DAO, HistoriaClinicaGeneralException.ACTION_SELECT);
                hcge.addError("No hay datos registrados en estudios laboratorio para la historia clinica: " + idHistoriaClinica);
                logger.error("No hay datos registrados en estudios laboratorio para la historia clinica: " + idHistoriaClinica);
                throw hcge;
            }
            HCEstudioLaboratorioView view = estudioLaboratorioConverter.toView(entity);
            view.setListaEstudios(estudioLabTipoService.getEstudioLabTipo(view.getIdEstudioLaboratorio(), idHistoriaClinica));
            view.setListaDocumentos(estudioLabDocumentoService.getEstudioLabDocumento(view.getIdEstudioLaboratorio(), idHistoriaClinica));
            return view;
        } catch (Exception ex) {
            HistoriaClinicaGeneralException hcge = new HistoriaClinicaGeneralException("Error insertar datos personales patologicos", HistoriaClinicaGeneralException.LAYER_DAO, HistoriaClinicaGeneralException.ACTION_INSERT);
            hcge.addError(ex.getLocalizedMessage());
            logger.error("Error: " + ex);
            throw hcge;
        }
    }

    @Override
    public void deleteEstudioLaboratorio(Long idHistoriaClinica) {
        try {
            HCEstudioLaboratorio entity = estudioLaboratorioRepository.findByidHistoriaClinica(idHistoriaClinica);
            estudioLabTipoService.deleteEstudioLabTipo(entity.getIdEstudioLaboratorio(), idHistoriaClinica);
            estudioLabDocumentoService.deleteEstudioLabDocumento(entity.getIdEstudioLaboratorio(), idHistoriaClinica);
            estudioLaboratorioRepository.delete(estudioLaboratorioRepository.findAllByidHistoriaClinica(idHistoriaClinica));
        } catch (Exception ex) {
            logger.error("Error: " + ex);
        }
    }
}
