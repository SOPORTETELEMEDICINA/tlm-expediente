package net.amentum.niomedic.expediente.service.historia_clinica.personales_no_patologicos;

import net.amentum.niomedic.expediente.converter.historia_clinica_general.personales_no_patologicos.*;
import net.amentum.niomedic.expediente.exception.HistoriaClinicaGeneralException;
import net.amentum.niomedic.expediente.model.historia_clinica.personales_no_patologicos.*;
import net.amentum.niomedic.expediente.persistence.historia_clinica.personales_no_patologicos.ConsumoRepository;
import net.amentum.niomedic.expediente.persistence.historia_clinica.personales_no_patologicos.PersonalesNoPatologicosRepository;
import net.amentum.niomedic.expediente.persistence.historia_clinica.personales_no_patologicos.TablaLaboralRepository;
import net.amentum.niomedic.expediente.views.historia_clinica_general.personales_no_patologicos.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;

@Service
public class PersonalesNoPatologicosServiceImpl implements PersonalesNoPatologicosService {
    private final Logger logger = LoggerFactory.getLogger(PersonalesNoPatologicosServiceImpl.class);
    @Autowired
    ViviendaConverter viviendaConverter;
    @Autowired
    MascotasConverter mascotasConverter;
    @Autowired
    ActividadDeportivaConverter actDeportivaConverter;
    @Autowired
    HabitoAlimenticioConverter habAlimenticioConverter;
    @Autowired
    HabitoHigienicoConverter habHigienicoConverter;
    @Autowired
    ConsumoConverter consumoConverter;
    @Autowired
    HistoriaLaboralConverter historiaLaboralConverter;
    @Autowired
    TablaLaboralConverter tablaLaboralConverter;
    @Autowired
    ConsumoRepository consumoRepository;
    @Autowired
    TablaLaboralRepository tablaLaboralRepository;

    @Autowired
    PersonalesNoPatologicosRepository repository;

    @Autowired
    PersonalesNoPatologicosConverter converter;

    @Override
    public void createPersonalesNoPatologicos(PersonalesNoPatologicosView view, Long idHistoriaClinica) throws HistoriaClinicaGeneralException {
        try {
            if(repository.findByIdHistoriaClinica(idHistoriaClinica) != null) {
                HistoriaClinicaGeneralException hcge = new HistoriaClinicaGeneralException("Error insertar datos personales no patologicos", HistoriaClinicaGeneralException.LAYER_DAO, HistoriaClinicaGeneralException.ACTION_INSERT);
                hcge.addError("Datos personales no patologicos previamente registrados");
                logger.error("Datos personales no patologicos previamente registrados");
                throw hcge;
            }
            view.setIdHistoriaClinica(idHistoriaClinica);
            view.getPersonalesNoPatologicosVivienda().setIdHistoriaClinica(idHistoriaClinica);
            view.getPersonalesNoPatologicosMascotas().setIdHistoriaClinica(idHistoriaClinica);
            view.getPersonalesNoPatologicosAcDeportiva().setIdHistoriaClinica(idHistoriaClinica);
            view.getPersonalesNoPatologicosHabAlimenticios().setIdHistoriaClinica(idHistoriaClinica);
            view.getPersonalesNoPatologicosHabHigienicos().setIdHistoriaClinica(idHistoriaClinica);
            for(ConsumoView element : view.getPersonalesNoPatologicosTablaConsumo())
                element.setIdHistoriaClinica(idHistoriaClinica);
            view.getPersonalesNoPatologicosHistoriaLaboral().setIdHistoriaClinica(idHistoriaClinica);
            for(TablaLaboralView element: view.getPersonalesNoPatologicosTablaLaboral())
                element.setIdHistoriaClinica(idHistoriaClinica);

            PersonalesNoPatologicos personalesNoPatologicos = converter.toEntity(view);
            Vivienda vivienda = viviendaConverter.toEntity(view.getPersonalesNoPatologicosVivienda());
            Mascotas mascotas = mascotasConverter.toEntity(view.getPersonalesNoPatologicosMascotas());
            ActividadDeportiva actividadDeportiva = actDeportivaConverter.toEntity(view.getPersonalesNoPatologicosAcDeportiva());
            HabitoAlimenticio habitoAlimenticio = habAlimenticioConverter.toEntity(view.getPersonalesNoPatologicosHabAlimenticios());
            HabitoHigienico habitoHigienico = habHigienicoConverter.toEntity(view.getPersonalesNoPatologicosHabHigienicos());
            ArrayList<Consumo> consumoArrayList = consumoConverter.toArrayEntities(view.getPersonalesNoPatologicosTablaConsumo());
            HistoriaLaboral historiaLaboral = historiaLaboralConverter.toEntity(view.getPersonalesNoPatologicosHistoriaLaboral());
            ArrayList<TablaLaboral> tablaLaboralArrayList = tablaLaboralConverter.toArrayEntities(view.getPersonalesNoPatologicosTablaLaboral());

            personalesNoPatologicos.setVivienda(vivienda);
            personalesNoPatologicos.setMascota(mascotas);
            personalesNoPatologicos.setActividadDeportiva(actividadDeportiva);
            personalesNoPatologicos.setHabitoAlimenticio(habitoAlimenticio);
            personalesNoPatologicos.setHabitoHigienico(habitoHigienico);
            personalesNoPatologicos.setConsumoList(consumoArrayList);
            personalesNoPatologicos.setHistoriaLaboral(historiaLaboral);
            personalesNoPatologicos.setTablaLaboralList(tablaLaboralArrayList);

            vivienda.setPersonalesNoPatologicos(personalesNoPatologicos);
            mascotas.setPersonalesNoPatologicos(personalesNoPatologicos);
            actividadDeportiva.setPersonalesNoPatologicos(personalesNoPatologicos);
            habitoAlimenticio.setPersonalesNoPatologicos(personalesNoPatologicos);
            habitoHigienico.setPersonalesNoPatologicos(personalesNoPatologicos);
            for(Consumo element : consumoArrayList)
                element.setPersonalesNoPatologicos(personalesNoPatologicos);
            historiaLaboral.setPersonalesNoPatologicos(personalesNoPatologicos);
            for(TablaLaboral element : tablaLaboralArrayList)
                element.setPersonalesNoPatologicos(personalesNoPatologicos);
            repository.save(personalesNoPatologicos);
            logger.info("Agregando Personales no Patologicos - HistoriaClinica: {}",view.getIdHistoriaClinica());
        } catch (HistoriaClinicaGeneralException ex) {
            throw ex;
        } catch (Exception ex) {
            HistoriaClinicaGeneralException hcge = new HistoriaClinicaGeneralException("Error insertar datos personales no patologicos", HistoriaClinicaGeneralException.LAYER_DAO, HistoriaClinicaGeneralException.ACTION_INSERT);
            hcge.addError(ex.getLocalizedMessage());
            logger.error("Este es un error: ", ex);
            ex.printStackTrace();
            throw hcge;
        }
    }

    @Override
    public void updatePersonalesNoPatologicos(PersonalesNoPatologicosView view, Long idHistoriaClinica) throws HistoriaClinicaGeneralException {
        try {
            PersonalesNoPatologicos personalesNoPatologicos = converter.toEntity(view);
            personalesNoPatologicos.setIdHistoriaClinica(idHistoriaClinica);
            Vivienda vivienda = viviendaConverter.toEntity(view.getPersonalesNoPatologicosVivienda());
            vivienda.setIdHistoriaClinica(idHistoriaClinica);
            Mascotas mascotas = mascotasConverter.toEntity(view.getPersonalesNoPatologicosMascotas());
            mascotas.setIdHistoriaClinica(idHistoriaClinica);
            ActividadDeportiva actividadDeportiva = actDeportivaConverter.toEntity(view.getPersonalesNoPatologicosAcDeportiva());
            actividadDeportiva.setIdHistoriaClinica(idHistoriaClinica);
            HabitoAlimenticio habitoAlimenticio = habAlimenticioConverter.toEntity(view.getPersonalesNoPatologicosHabAlimenticios());
            habitoAlimenticio.setIdHistoriaClinica(idHistoriaClinica);
            HabitoHigienico habitoHigienico = habHigienicoConverter.toEntity(view.getPersonalesNoPatologicosHabHigienicos());
            habitoHigienico.setIdHistoriaClinica(idHistoriaClinica);
            ArrayList<Consumo> consumoArrayList = consumoConverter.toArrayEntities(view.getPersonalesNoPatologicosTablaConsumo());
            HistoriaLaboral historiaLaboral = historiaLaboralConverter.toEntity(view.getPersonalesNoPatologicosHistoriaLaboral());
            historiaLaboral.setIdHistoriaClinica(idHistoriaClinica);
            ArrayList<TablaLaboral> tablaLaboralArrayList = tablaLaboralConverter.toArrayEntities(view.getPersonalesNoPatologicosTablaLaboral());
            PersonalesNoPatologicos entityPrev = repository.findByIdHistoriaClinica(idHistoriaClinica);
            if(entityPrev == null)
                logger.error("Nuevo/Update - Datos personales no patologicos no registrados para la historia clinica " + view.getIdHistoriaClinica());
            else {
                personalesNoPatologicos.setId(entityPrev.getId());
                vivienda.setIdVivienda(entityPrev.getVivienda().getIdVivienda());
                mascotas.setIdMascotas(entityPrev.getMascota().getIdMascotas());
                actividadDeportiva.setIdActivadDeportiva(entityPrev.getActividadDeportiva().getIdActivadDeportiva());
                habitoAlimenticio.setIdHabito(entityPrev.getHabitoAlimenticio().getIdHabito());
                habitoHigienico.setIdHabito(entityPrev.getHabitoHigienico().getIdHabito());
                consumoRepository.delete(entityPrev.getConsumoList());
                historiaLaboral.setIdHistoriaLaboral(entityPrev.getHistoriaLaboral().getIdHistoriaLaboral());
                tablaLaboralRepository.delete(entityPrev.getTablaLaboralList());
            }
            personalesNoPatologicos.setVivienda(vivienda);
            personalesNoPatologicos.setMascota(mascotas);
            personalesNoPatologicos.setActividadDeportiva(actividadDeportiva);
            personalesNoPatologicos.setHabitoAlimenticio(habitoAlimenticio);
            personalesNoPatologicos.setHabitoHigienico(habitoHigienico);
            personalesNoPatologicos.setConsumoList(consumoArrayList);
            personalesNoPatologicos.setHistoriaLaboral(historiaLaboral);
            personalesNoPatologicos.setTablaLaboralList(tablaLaboralArrayList);

            vivienda.setPersonalesNoPatologicos(personalesNoPatologicos);
            mascotas.setPersonalesNoPatologicos(personalesNoPatologicos);
            actividadDeportiva.setPersonalesNoPatologicos(personalesNoPatologicos);
            habitoAlimenticio.setPersonalesNoPatologicos(personalesNoPatologicos);
            habitoHigienico.setPersonalesNoPatologicos(personalesNoPatologicos);
            for(Consumo element : consumoArrayList) {
                element.setPersonalesNoPatologicos(personalesNoPatologicos);
                element.setIdHistoriaClinica(idHistoriaClinica);
            }
            historiaLaboral.setPersonalesNoPatologicos(personalesNoPatologicos);
            for(TablaLaboral element : tablaLaboralArrayList) {
                element.setPersonalesNoPatologicos(personalesNoPatologicos);
                element.setIdHistoriaClinica(idHistoriaClinica);
            }
            repository.save(personalesNoPatologicos);
            logger.info("Editando Personales no Patologicos - Id: {} - HistoriaClinica: {}",personalesNoPatologicos.getId(), view.getIdHistoriaClinica());
        } catch (Exception ex) {
            HistoriaClinicaGeneralException hcge = new HistoriaClinicaGeneralException("Error al actualizar datos personales no patologicos", HistoriaClinicaGeneralException.LAYER_DAO, HistoriaClinicaGeneralException.ACTION_INSERT);
            hcge.addError(ex.toString());
            logger.error("Error: " + ex);
            throw hcge;
        }
    }

    @Override
    public PersonalesNoPatologicosView getPersonalesNoPatologicos(Long idHistoriaClinica) throws HistoriaClinicaGeneralException {
        try {
            PersonalesNoPatologicos entity = repository.findByIdHistoriaClinica(idHistoriaClinica);
            if(entity == null) {
                HistoriaClinicaGeneralException hcge = new HistoriaClinicaGeneralException("Error al buscar datos personales no patológicos", HistoriaClinicaGeneralException.LAYER_DAO, HistoriaClinicaGeneralException.ACTION_SELECT);
                hcge.addError("Datos personales no patológicos no registrados para la historia clínica " + idHistoriaClinica);
                logger.error("Datos personales no patológicos no registrados para la historia clínica " + idHistoriaClinica);
                throw hcge;
            }
            PersonalesNoPatologicosView view = converter.toView(entity);
            view.setPersonalesNoPatologicosVivienda(viviendaConverter.toView(entity.getVivienda()));
            view.setPersonalesNoPatologicosMascotas(mascotasConverter.toEntity(entity.getMascota()));
            view.setPersonalesNoPatologicosAcDeportiva(actDeportivaConverter.toView(entity.getActividadDeportiva()));
            view.setPersonalesNoPatologicosHabAlimenticios(habAlimenticioConverter.toView(entity.getHabitoAlimenticio()));
            view.setPersonalesNoPatologicosHabHigienicos(habHigienicoConverter.toView(entity.getHabitoHigienico()));
            view.setPersonalesNoPatologicosTablaConsumo(consumoConverter.toArrayViews(entity.getConsumoList()));
            view.setPersonalesNoPatologicosHistoriaLaboral(historiaLaboralConverter.toView(entity.getHistoriaLaboral()));
            view.setPersonalesNoPatologicosTablaLaboral(tablaLaboralConverter.toArrayView(entity.getTablaLaboralList()));
            return view;
        } catch (HistoriaClinicaGeneralException ex) {
            throw ex;
        } catch (Exception ex) {
            HistoriaClinicaGeneralException hcge = new HistoriaClinicaGeneralException("Error al buscar datos personales no patológicos", HistoriaClinicaGeneralException.LAYER_DAO, HistoriaClinicaGeneralException.ACTION_SELECT);
            hcge.addError(ex.getLocalizedMessage());
            logger.error("Error: " + ex);
            throw hcge;
        }
    }

    @Override
    public void deletePersonalesNoPatologicos(Long idHistoriaClinica) {
        try {
            PersonalesNoPatologicos personalesNoPatologicos = repository.findByIdHistoriaClinica(idHistoriaClinica);
            if(personalesNoPatologicos != null) {
                repository.delete(personalesNoPatologicos);
                logger.error("Datos personales no patológicos borrado para " + idHistoriaClinica);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
