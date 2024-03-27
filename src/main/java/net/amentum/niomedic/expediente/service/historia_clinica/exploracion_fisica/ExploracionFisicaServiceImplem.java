package net.amentum.niomedic.expediente.service.historia_clinica.exploracion_fisica;

import net.amentum.niomedic.expediente.converter.historia_clinica_general.exploracion_fisica.ExploracionFisicaConverter;
import net.amentum.niomedic.expediente.exception.HistoriaClinicaGeneralException;
import net.amentum.niomedic.expediente.model.historia_clinica.exploracion_fisica.*;
import net.amentum.niomedic.expediente.persistence.historia_clinica.exploracion_fisica.ExploracionFisicaRepository;
import net.amentum.niomedic.expediente.views.historia_clinica_general.exploracion_fisica.ExploracionFisicaView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExploracionFisicaServiceImplem implements ExploracionFisicaService {

    private final Logger log = LoggerFactory.getLogger(ExploracionFisicaServiceImplem.class);

    @Autowired
    ExploracionFisicaRepository repository;

    @Autowired
    ExploracionFisicaConverter converter;

    @Override
    public void createExploracionFisica(ExploracionFisicaView view, Long idHistoriaClinica) throws HistoriaClinicaGeneralException {
        try {
            if(repository.findByIdHistoriaClinica(idHistoriaClinica) != null) {
                HistoriaClinicaGeneralException hcge = new HistoriaClinicaGeneralException("Error al insertar Exploración física", HistoriaClinicaGeneralException.LAYER_DAO, HistoriaClinicaGeneralException.ACTION_INSERT);
                hcge.addError("Exploración física ya registrada para la historia clínica " + idHistoriaClinica);
                log.error("Exploración física ya registrada para la historia clínica {}", idHistoriaClinica);
                throw hcge;
            }
            view.setIdHistoriaClinica(idHistoriaClinica);
            view.getTensionArterial().setIdHistoriaClinica(idHistoriaClinica);
            view.getEstadoConciencia().setIdHistoriaClinica(idHistoriaClinica);
            view.getHabitusExterior().setIdHistoriaClinica(idHistoriaClinica);
            view.getSistemaNervioso().setIdHistoriaClinica(idHistoriaClinica);
            view.getCabeza().setIdHistoriaClinica(idHistoriaClinica);
            view.getCuello().setIdHistoriaClinica(idHistoriaClinica);
            view.getTorax().setIdHistoriaClinica(idHistoriaClinica);
            view.getAbdomen().setIdHistoriaClinica(idHistoriaClinica);
            view.getPiel().setIdHistoriaClinica(idHistoriaClinica);
            view.getGenitales().setIdHistoriaClinica(idHistoriaClinica);
            view.getExtremidades().setIdHistoriaClinica(idHistoriaClinica);

            ExploracionFisica exploracionFisica = converter.toEntity(view);
            TensionArterial tensionArterial = exploracionFisica.getTensionArterial();
            EstadoConciencia estadoConciencia = exploracionFisica.getEstadoConciencia();
            HabitusExterior habitusExterior = exploracionFisica.getHabitusExterior();
            SistemaNervioso sistemaNervioso = exploracionFisica.getSistemaNervioso();
            Cabeza cabeza = exploracionFisica.getCabeza();
            Cuello cuello = exploracionFisica.getCuello();
            Torax torax = exploracionFisica.getTorax();
            Abdomen abdomen = exploracionFisica.getAbdomen();
            Piel piel = exploracionFisica.getPiel();
            Genitales genitales = exploracionFisica.getGenitales();
            Extremidades extremidades = exploracionFisica.getExtremidades();

            tensionArterial.setExploracionFisica(exploracionFisica);
            estadoConciencia.setExploracionFisica(exploracionFisica);
            habitusExterior.setExploracionFisica(exploracionFisica);
            sistemaNervioso.setExploracionFisica(exploracionFisica);
            cabeza.setExploracionFisica(exploracionFisica);
            cuello.setExploracionFisica(exploracionFisica);
            torax.setExploracionFisica(exploracionFisica);
            abdomen.setExploracionFisica(exploracionFisica);
            piel.setExploracionFisica(exploracionFisica);
            genitales.setExploracionFisica(exploracionFisica);
            extremidades.setExploracionFisica(exploracionFisica);

            repository.save(exploracionFisica);
            log.info("Agregando Exploración fisica - HistoriaClinica: {}", view.getIdHistoriaClinica());
        } catch(HistoriaClinicaGeneralException ex) {
            throw ex;
        } catch (Exception ex) {
            HistoriaClinicaGeneralException hcge = new HistoriaClinicaGeneralException("Error al insertar Exploración física", HistoriaClinicaGeneralException.LAYER_DAO, HistoriaClinicaGeneralException.ACTION_INSERT);
            hcge.addError(ex.getCause().toString());
            ex.printStackTrace();
            throw hcge;
        }
    }

    @Override
    public void updateExploracionFisica(ExploracionFisicaView view, Long idHistoriaClinica) throws HistoriaClinicaGeneralException {
        try {
            ExploracionFisica exploracionFisica = converter.toEntity(view);
            exploracionFisica.setIdHistoriaClinica(idHistoriaClinica);
            TensionArterial tensionArterial = exploracionFisica.getTensionArterial();
            tensionArterial.setIdHistoriaClinica(idHistoriaClinica);
            EstadoConciencia estadoConciencia = exploracionFisica.getEstadoConciencia();
            estadoConciencia.setIdHistoriaClinica(idHistoriaClinica);
            HabitusExterior habitusExterior = exploracionFisica.getHabitusExterior();
            habitusExterior.setIdHistoriaClinica(idHistoriaClinica);
            SistemaNervioso sistemaNervioso = exploracionFisica.getSistemaNervioso();
            sistemaNervioso.setIdHistoriaClinica(idHistoriaClinica);
            Cabeza cabeza = exploracionFisica.getCabeza();
            cabeza.setIdHistoriaClinica(idHistoriaClinica);
            Cuello cuello = exploracionFisica.getCuello();
            cuello.setIdHistoriaClinica(idHistoriaClinica);
            Torax torax = exploracionFisica.getTorax();
            torax.setIdHistoriaClinica(idHistoriaClinica);
            Abdomen abdomen = exploracionFisica.getAbdomen();
            abdomen.setIdHistoriaClinica(idHistoriaClinica);
            Piel piel = exploracionFisica.getPiel();
            piel.setIdHistoriaClinica(idHistoriaClinica);
            Genitales genitales = exploracionFisica.getGenitales();
            genitales.setIdHistoriaClinica(idHistoriaClinica);
            Extremidades extremidades = exploracionFisica.getExtremidades();
            extremidades.setIdHistoriaClinica(idHistoriaClinica);

            ExploracionFisica prevEntity = repository.findByIdHistoriaClinica(idHistoriaClinica);
            if(prevEntity == null)
                log.error("Nuevo/Update - Exploración física no registrada para la historia clínica {}", idHistoriaClinica);
            else {
                exploracionFisica.setIdExploracionFisica(prevEntity.getIdExploracionFisica());
                tensionArterial.setIdTensionArterial(prevEntity.getTensionArterial().getIdTensionArterial());
                estadoConciencia.setIdEstadoConciencia(prevEntity.getEstadoConciencia().getIdEstadoConciencia());
                habitusExterior.setIdHabitusExterior(prevEntity.getHabitusExterior().getIdHabitusExterior());
                sistemaNervioso.setIdSistemaNervioso(prevEntity.getSistemaNervioso().getIdSistemaNervioso());
                cabeza.setIdCabeza(prevEntity.getCabeza().getIdCabeza());
                cuello.setIdCuello(prevEntity.getCuello().getIdCuello());
                torax.setIdTorax(prevEntity.getTorax().getIdTorax());
                abdomen.setIdAbdomen(prevEntity.getAbdomen().getIdAbdomen());
                piel.setIdPiel(prevEntity.getPiel().getIdPiel());
                genitales.setIdGenitales(prevEntity.getGenitales().getIdGenitales());
                extremidades.setIdExtremidades(prevEntity.getExtremidades().getIdExtremidades());
            }

            exploracionFisica.setTensionArterial(tensionArterial);
            exploracionFisica.setEstadoConciencia(estadoConciencia);
            exploracionFisica.setHabitusExterior(habitusExterior);
            exploracionFisica.setSistemaNervioso(sistemaNervioso);
            exploracionFisica.setCabeza(cabeza);
            exploracionFisica.setCuello(cuello);
            exploracionFisica.setTorax(torax);
            exploracionFisica.setAbdomen(abdomen);
            exploracionFisica.setPiel(piel);
            exploracionFisica.setGenitales(genitales);
            exploracionFisica.setExtremidades(extremidades);

            tensionArterial.setExploracionFisica(exploracionFisica);
            estadoConciencia.setExploracionFisica(exploracionFisica);
            habitusExterior.setExploracionFisica(exploracionFisica);
            sistemaNervioso.setExploracionFisica(exploracionFisica);
            cabeza.setExploracionFisica(exploracionFisica);
            cuello.setExploracionFisica(exploracionFisica);
            torax.setExploracionFisica(exploracionFisica);
            abdomen.setExploracionFisica(exploracionFisica);
            piel.setExploracionFisica(exploracionFisica);
            genitales.setExploracionFisica(exploracionFisica);
            extremidades.setExploracionFisica(exploracionFisica);

            repository.save(exploracionFisica);
            log.info("Editando Exploración fisica - Id: {} - HistoriaClinica: {}", exploracionFisica.getIdExploracionFisica(), view.getIdHistoriaClinica());
        } catch (Exception ex) {
            HistoriaClinicaGeneralException hcge = new HistoriaClinicaGeneralException("Error al actualizar Exploración física", HistoriaClinicaGeneralException.LAYER_DAO, HistoriaClinicaGeneralException.ACTION_UPDATE);
            hcge.addError(ex.toString());
            ex.printStackTrace();
            throw hcge;
        }
    }

    @Override
    public ExploracionFisicaView getExploracionFisica(Long idHistoriaClinica) throws HistoriaClinicaGeneralException {
        try {
            ExploracionFisica entity = repository.findByIdHistoriaClinica(idHistoriaClinica);
            if(entity == null) {
                HistoriaClinicaGeneralException hcge = new HistoriaClinicaGeneralException("Error al obtener Exploración física", HistoriaClinicaGeneralException.LAYER_DAO, HistoriaClinicaGeneralException.ACTION_SELECT);
                hcge.addError("Exploración física no registrada para la historia clínica " + idHistoriaClinica);
                log.error("Exploración física no registrada para la historia clínica {}", idHistoriaClinica);
                throw hcge;
            }
            return converter.toView(entity);
        } catch(HistoriaClinicaGeneralException ex) {
            throw ex;
        } catch (Exception ex) {
            HistoriaClinicaGeneralException hcge = new HistoriaClinicaGeneralException("Error al obtener Exploración física", HistoriaClinicaGeneralException.LAYER_DAO, HistoriaClinicaGeneralException.ACTION_SELECT);
            hcge.addError(ex.getCause().toString());
            ex.printStackTrace();
            throw hcge;
        }
    }

    @Override
    public void deleteExploracionFisica(Long idHistoriaClinica) throws HistoriaClinicaGeneralException {
        try {
            ExploracionFisica exploracionFisica = repository.findByIdHistoriaClinica(idHistoriaClinica);
            if(exploracionFisica != null) {
                repository.delete(exploracionFisica);
                log.error("Datos exploración física borrados para " + idHistoriaClinica);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            HistoriaClinicaGeneralException hcge = new HistoriaClinicaGeneralException("Error al eliminar Exploración física", HistoriaClinicaGeneralException.LAYER_DAO, HistoriaClinicaGeneralException.ACTION_DELETE);
            hcge.addError(ex.getLocalizedMessage());
            throw hcge;
        }
    }
}
