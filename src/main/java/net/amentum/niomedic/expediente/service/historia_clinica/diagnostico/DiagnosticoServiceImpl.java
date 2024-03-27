package net.amentum.niomedic.expediente.service.historia_clinica.diagnostico;

import lombok.extern.slf4j.Slf4j;
import net.amentum.niomedic.expediente.converter.historia_clinica_general.diagnostico.DiagnosticoConverter;
import net.amentum.niomedic.expediente.exception.HistoriaClinicaGeneralException;
import net.amentum.niomedic.expediente.model.historia_clinica.diagnostico.Diagnostico;
import net.amentum.niomedic.expediente.model.historia_clinica.diagnostico.DiagnosticoCie10;
import net.amentum.niomedic.expediente.persistence.historia_clinica.diagnostico.DiagnosticoCie10Repository;
import net.amentum.niomedic.expediente.persistence.historia_clinica.diagnostico.DiagnosticoRepository;
import net.amentum.niomedic.expediente.views.historia_clinica_general.diagnostico.DiagnosticoHCGView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DiagnosticoServiceImpl implements DiagnosticoService {

    @Autowired
    DiagnosticoRepository repository;

    @Autowired
    DiagnosticoCie10Repository diagnosticoCie10Repository;

    @Autowired
    DiagnosticoConverter converter;

    @Override
    public void createDiagnostico(DiagnosticoHCGView view, Long idHistoriaClinica) throws HistoriaClinicaGeneralException {
        try {
            if(repository.findByIdHistoriaClinica(idHistoriaClinica) != null) {
                HistoriaClinicaGeneralException hcge = new HistoriaClinicaGeneralException("Error al insertar diagnostico", HistoriaClinicaGeneralException.LAYER_DAO, HistoriaClinicaGeneralException.ACTION_INSERT);
                hcge.addError("Diagnostico ya registrado para la historia clínica " + idHistoriaClinica);
                log.error("Diagnostico ya registrado para la historia clínica {}", idHistoriaClinica);
                throw hcge;
            }
            view.setIdHistoriaClinica(idHistoriaClinica);
            repository.save(converter.toEntity(view));
            log.info("Agregando Diagnostico - HistoriaClinica: {}", idHistoriaClinica);
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
    public void updateDiagnostico(DiagnosticoHCGView view, Long idHistoriaClinica) throws HistoriaClinicaGeneralException {
        try {
            Diagnostico updated = converter.toEntity(view);
            Diagnostico prevEntity = repository.findByIdHistoriaClinica(idHistoriaClinica);
            if(prevEntity == null)
                log.error("Nuevo/Update - Diagnostico no registrado para la historia clínica {}", idHistoriaClinica);
            else {
                view.setIdDiagnosticoHC(prevEntity.getIdDiagnostico());
                updated = converter.toEntity(view);
                for(DiagnosticoCie10 element : prevEntity.getDiagnosticoCie10List()) {
                    if(!existCie10(element.getDiagnostico().getIdDiagnostico(), element.getCie10().getIdCie10(), updated.getDiagnosticoCie10List()))
                        diagnosticoCie10Repository.delete(element);
                }
                prevEntity.getDiagnosticoCie10List().clear();
            }
            updated.setIdHistoriaClinica(idHistoriaClinica);
            repository.save(updated);
            log.info("Editando Diagnostico - Id: {} - HistoriaClinica: {}", updated.getIdDiagnostico(), idHistoriaClinica);
        } catch (Exception ex) {
            HistoriaClinicaGeneralException hcge = new HistoriaClinicaGeneralException("Error al actualizar diagnostico", HistoriaClinicaGeneralException.LAYER_DAO, HistoriaClinicaGeneralException.ACTION_UPDATE);
            hcge.addError(ex.toString());
            ex.printStackTrace();
            throw hcge;
        }
    }

    @Override
    public DiagnosticoHCGView getDiagnostico(Long idHistoriaClinica) throws HistoriaClinicaGeneralException {
        try {
            Diagnostico entity = repository.findByIdHistoriaClinica(idHistoriaClinica);
            if(entity == null) {
                HistoriaClinicaGeneralException hcge = new HistoriaClinicaGeneralException("Error al buscar diagnostico", HistoriaClinicaGeneralException.LAYER_DAO, HistoriaClinicaGeneralException.ACTION_SELECT);
                hcge.addError("Diagnostico no registrado para la historia clínica " + idHistoriaClinica);
                log.error("Diagnostico no registrado para la historia clínica {}", idHistoriaClinica);
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
    public void deleteDiagnostico(Long idHistoriaClinica) throws HistoriaClinicaGeneralException {
        try {
            Diagnostico entity = repository.findByIdHistoriaClinica(idHistoriaClinica);
            if(entity != null) {
                repository.delete(entity);
                log.error("Diagnostico eliminado para la historia clinica {}", idHistoriaClinica);
            }
        } catch (Exception ex) {
            HistoriaClinicaGeneralException hcge = new HistoriaClinicaGeneralException("Error al eliminar Diagnostico", HistoriaClinicaGeneralException.LAYER_DAO, HistoriaClinicaGeneralException.ACTION_DELETE);
            hcge.addError(ex.getCause().getMessage());
            ex.printStackTrace();
            throw hcge;
        }
    }

    private Boolean existCie10(Long idDiagnostico, Long idCie10, List<DiagnosticoCie10> diagnosticoCie10List) {
        List<DiagnosticoCie10> list = diagnosticoCie10List.stream().filter(diagnosticoCie10 -> diagnosticoCie10.getDiagnostico().getIdDiagnostico().compareTo(idDiagnostico) == 0
                && diagnosticoCie10.getCie10().getIdCie10().compareTo(idCie10) == 0).collect(Collectors.toList());
        return !list.isEmpty();
    }
}
