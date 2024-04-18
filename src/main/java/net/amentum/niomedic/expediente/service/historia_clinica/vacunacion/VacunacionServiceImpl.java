package net.amentum.niomedic.expediente.service.historia_clinica.vacunacion;

import lombok.extern.slf4j.Slf4j;
import net.amentum.niomedic.expediente.converter.historia_clinica_general.vacunacion.VacunacionConverter;
import net.amentum.niomedic.expediente.exception.HistoriaClinicaGeneralException;
import net.amentum.niomedic.expediente.model.historia_clinica.vacunacion.Vacunacion;
import net.amentum.niomedic.expediente.persistence.historia_clinica.vacunacion.VacunacionRepository;
import net.amentum.niomedic.expediente.views.historia_clinica_general.vacunacion.VacunacionView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class VacunacionServiceImpl implements VacunacionService {

    @Autowired
    VacunacionRepository repository;

    @Autowired
    VacunacionConverter converter;

    @Override
    public void createVacunacion(ArrayList<VacunacionView> vacunacionViews, Long idHistoriaClinica) throws HistoriaClinicaGeneralException {
        try {
            for(VacunacionView element : vacunacionViews) {
                element.setIdHistoriaClinica(idHistoriaClinica);
                repository.save(converter.toEntity(element));
            }
            log.info("Agregando vacunación - HistoriaClinica: {}", idHistoriaClinica);
        } catch (Exception ex) {
            HistoriaClinicaGeneralException hcge = new HistoriaClinicaGeneralException("Error al insertar vacunación", HistoriaClinicaGeneralException.LAYER_DAO, HistoriaClinicaGeneralException.ACTION_INSERT);
            hcge.addError(ex.getCause().getMessage());
            ex.printStackTrace();
            throw hcge;
        }
    }

    @Override
    public void updateVacunacion(ArrayList<VacunacionView> vacunacionViews, Long idHistoriaClinica) throws HistoriaClinicaGeneralException {
        try {
            deleteVacunacion(idHistoriaClinica);
            for(VacunacionView element : vacunacionViews) {
                element.setIdHistoriaClinica(idHistoriaClinica);
                repository.save(converter.toEntity(element));
            }
        } catch (HistoriaClinicaGeneralException ex) {
            throw ex;
        } catch (Exception ex) {
            HistoriaClinicaGeneralException hcge = new HistoriaClinicaGeneralException("Error al actualizar vacunación", HistoriaClinicaGeneralException.LAYER_DAO, HistoriaClinicaGeneralException.ACTION_UPDATE);
            hcge.addError(ex.getCause().getMessage());
            ex.printStackTrace();
            throw hcge;
        }
    }
   // DEFAULT 'nextval('hc_vacunacion_id_vacunacion_seq'::regclass)'
    @Override
    public VacunacionView getVacunacion(Long idHistoriaClinica) throws HistoriaClinicaGeneralException {
        //eturn null;
        try {
            Vacunacion entity = (Vacunacion) repository.findByIdHistoriaClinica(idHistoriaClinica);
            if(entity == null) {
                HistoriaClinicaGeneralException hcge = new HistoriaClinicaGeneralException("Error al obtener vacunación", HistoriaClinicaGeneralException.LAYER_DAO, HistoriaClinicaGeneralException.ACTION_SELECT);
                hcge.addError("Vacunación no registrada para la historia clínica " + idHistoriaClinica);
                log.error("Vacunación no registrada para la historia clínica {}", idHistoriaClinica);
                throw hcge;
            }
            return converter.toView(entity);
        } catch (HistoriaClinicaGeneralException ex) {
            throw ex;
        } catch (Exception ex) {
            HistoriaClinicaGeneralException hcge = new HistoriaClinicaGeneralException("Error al obtener vacunación", HistoriaClinicaGeneralException.LAYER_DAO, HistoriaClinicaGeneralException.ACTION_SELECT);
            hcge.addError(ex.getCause().getMessage());
            ex.printStackTrace();
            throw hcge;
        }
    }

    @Override
    public void deleteVacunacion(Long idHistoriaClinica) throws HistoriaClinicaGeneralException {
        try {
            List<Vacunacion> entity = repository.findByIdHistoriaClinica(idHistoriaClinica);
            if(entity != null) {
                repository.delete(entity);
                log.error("Esquema vacunacion eliminado para la historia clinica {}", idHistoriaClinica);
            }
        } catch (Exception ex) {
            HistoriaClinicaGeneralException hcge = new HistoriaClinicaGeneralException("Error al eliminar vacunación", HistoriaClinicaGeneralException.LAYER_DAO, HistoriaClinicaGeneralException.ACTION_SELECT);
            hcge.addError(ex.getCause().getMessage());
            ex.printStackTrace();
            throw hcge;
        }
    }
}
