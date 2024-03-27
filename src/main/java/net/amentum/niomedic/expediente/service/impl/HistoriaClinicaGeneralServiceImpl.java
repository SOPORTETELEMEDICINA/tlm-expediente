package net.amentum.niomedic.expediente.service.impl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.amentum.niomedic.expediente.converter.HistoriaClinicaGeneralConverter;
import net.amentum.niomedic.expediente.exception.HistoriaClinicaGeneralException;
import net.amentum.niomedic.expediente.model.HistoriaClinicaGeneral;
import net.amentum.niomedic.expediente.persistence.HistoriaClinicaGeneralRepository;
import net.amentum.niomedic.expediente.service.historia_clinica.antecendentes_ginecobstetricos.AntecendentesGinecobstetricosService;
import net.amentum.niomedic.expediente.service.historia_clinica.diagnostico.DiagnosticoService;
import net.amentum.niomedic.expediente.service.HistoriaClinicaGeneralService;
import net.amentum.niomedic.expediente.service.historia_clinica.estudio_laboratorio.HCEstudioLaboratorioService;
import net.amentum.niomedic.expediente.service.historia_clinica.exploracion_fisica.ExploracionFisicaService;
import net.amentum.niomedic.expediente.service.historia_clinica.heredo_familiares.HeredoFamiliaresService;
import net.amentum.niomedic.expediente.service.historia_clinica.interrogatorio_aparatos.InterrogatorioAparatosService;
import net.amentum.niomedic.expediente.service.historia_clinica.padecimiento_actual.PadecimientoActualService;
import net.amentum.niomedic.expediente.service.historia_clinica.personales_no_patologicos.*;
import net.amentum.niomedic.expediente.service.historia_clinica.personales_patologicos.PersonalesPatologicosService;
import net.amentum.niomedic.expediente.service.historia_clinica.tratamiento.HCTratamientoService;
import net.amentum.niomedic.expediente.service.historia_clinica.vacunacion.VacunacionService;
import net.amentum.niomedic.expediente.views.HistoriaClinicaGeneralView;
import net.amentum.niomedic.expediente.views.historia_clinica_general.HCGEstadisticasView;
import net.amentum.niomedic.expediente.views.historia_clinica_general.antecendentes_ginecobstetricos.AntecendentesGinecobstetricosView;
import net.amentum.niomedic.expediente.views.historia_clinica_general.diagnostico.DiagnosticoHCGView;
import net.amentum.niomedic.expediente.views.historia_clinica_general.estudio_laboratorio.HCEstudioLaboratorioView;
import net.amentum.niomedic.expediente.views.historia_clinica_general.exploracion_fisica.ExploracionFisicaView;
import net.amentum.niomedic.expediente.views.historia_clinica_general.heredo_familiares.HeredoFamiliaresView;
import net.amentum.niomedic.expediente.views.historia_clinica_general.heredo_familiares.HfEnfermedadesView;
import net.amentum.niomedic.expediente.views.historia_clinica_general.interrogatorio_aparatos.InterrogatorioAparatosView;
import net.amentum.niomedic.expediente.views.historia_clinica_general.padecimiento_actual.PadecimientoActualView;
import net.amentum.niomedic.expediente.views.historia_clinica_general.personales_no_patologicos.*;
import net.amentum.niomedic.expediente.views.historia_clinica_general.personales_patologicos.PersonalesPatologicosView;
import net.amentum.niomedic.expediente.views.historia_clinica_general.tratamiento.HCTratamientoView;
import net.amentum.niomedic.expediente.views.historia_clinica_general.vacunacion.VacunacionView;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class HistoriaClinicaGeneralServiceImpl implements HistoriaClinicaGeneralService {
   private final Logger logger = LoggerFactory.getLogger(HistoriaClinicaGeneralServiceImpl.class);

   @Autowired
   private HistoriaClinicaGeneralConverter historiaClinicaGeneralConverter;

   @Autowired
   private HistoriaClinicaGeneralRepository historiaClinicaGeneralRepository;

   @Transactional(readOnly = false, rollbackFor = {HistoriaClinicaGeneralException.class})
   @Override
   public void createHistoriaClinicaGeneral(HistoriaClinicaGeneralView historiaClinicaGeneralView) throws HistoriaClinicaGeneralException {
      try {
         //TODO: buscar si viene vacio el idPaciente
         if (historiaClinicaGeneralView.getIdPaciente() == null) {
            logger.error("===>>>No debe venir vacio el idPaciente");
            HistoriaClinicaGeneralException hcge = new HistoriaClinicaGeneralException("No debe venir vacio el idPaciente", HistoriaClinicaGeneralException.LAYER_DAO, HistoriaClinicaGeneralException.ACTION_INSERT);
            hcge.addError("No debe venir vacio el idPaciente");
            throw hcge;
         }
         //TODO: buscar si ya existe el idPaciente
         if (historiaClinicaGeneralRepository.findByIdPaciente(historiaClinicaGeneralView.getIdPaciente()) != null) {
            logger.error("===>>>Ya existe ese idPaciente");
            HistoriaClinicaGeneralException hcge = new HistoriaClinicaGeneralException("Ya existe ese idPaciente", HistoriaClinicaGeneralException.LAYER_DAO, HistoriaClinicaGeneralException.ACTION_INSERT);
            hcge.addError("Ya existe ese idPaciente");
            throw hcge;
         }
         HistoriaClinicaGeneral historiaClinicaGeneral = historiaClinicaGeneralConverter.toEntity(historiaClinicaGeneralView, new HistoriaClinicaGeneral(), Boolean.FALSE);
         logger.info("Insertar nuevo Historia Clinica General: {}", historiaClinicaGeneral);
         historiaClinicaGeneralRepository.save(historiaClinicaGeneral);

         guardarHCG(convertJson(new JSONObject(historiaClinicaGeneral.getHcg()),
                 historiaClinicaGeneral.getIdHistoriaClinicaGeneral()));
      } catch (HistoriaClinicaGeneralException hcge) {
         throw hcge;
      } catch (DataIntegrityViolationException dive) {
         HistoriaClinicaGeneralException hcge = new HistoriaClinicaGeneralException("No fue posible agregar Historia Clinica General", HistoriaClinicaGeneralException.LAYER_DAO, HistoriaClinicaGeneralException.ACTION_INSERT);
         hcge.addError("Ocurrio un error al agregar Historia Clinica General");
         logger.error("===>>>Error al insertar nuevo Historia Clinica General - CODE {} - {}", hcge.getExceptionCode(), historiaClinicaGeneralView, hcge);
         throw hcge;
      } catch (ConstraintViolationException cve) {
         HistoriaClinicaGeneralException hcge = new HistoriaClinicaGeneralException("Error inesperado al agregar Historia Clinica General", HistoriaClinicaGeneralException.LAYER_DAO, HistoriaClinicaGeneralException.ACTION_INSERT);
         hcge.addError("Ocurrió un error al agregar Historia Clinica General:");
         hcge.addError("" + cve.getConstraintViolations());
         logger.error("===>>>Error al insertar nuevo Historia Clinica General - CODE {} - {}", hcge.getExceptionCode(), historiaClinicaGeneralView, cve.getMessage());
         throw hcge;
      } catch (Exception ex) {
         HistoriaClinicaGeneralException hcge = new HistoriaClinicaGeneralException("Error inesperado al agregar Historia Clinica General", HistoriaClinicaGeneralException.LAYER_DAO, HistoriaClinicaGeneralException.ACTION_INSERT);
         hcge.addError("Ocurrió un error al agregar Historia Clinica General:\n" + ex.getMessage());
         logger.error("===>>>Error al insertar nuevo Historia Clinica General - CODE {} - {}", hcge.getExceptionCode(), "", hcge);
         throw hcge;
      }
   }

   @Transactional(readOnly = false, rollbackFor = {HistoriaClinicaGeneralException.class})
   @Override
   public void updateHistoriaClinicaGeneral(HistoriaClinicaGeneralView historiaClinicaGeneralView) throws HistoriaClinicaGeneralException {
      try {
         if (historiaClinicaGeneralRepository.findByIdPaciente(historiaClinicaGeneralView.getIdPaciente()) == null) {
            logger.error("===>>>No existe un registro con ese idPaciente");
            HistoriaClinicaGeneralException hcge = new HistoriaClinicaGeneralException("No existe un registro con ese idPaciente", HistoriaClinicaGeneralException.LAYER_DAO, HistoriaClinicaGeneralException.ACTION_VALIDATE);
            hcge.addError("No existe un registro con ese idPaciente");
            throw hcge;
         }
         HistoriaClinicaGeneral historiaClinicaGeneral = historiaClinicaGeneralRepository.findByIdPaciente(historiaClinicaGeneralView.getIdPaciente());
         historiaClinicaGeneral = historiaClinicaGeneralConverter.toEntity(historiaClinicaGeneralView, historiaClinicaGeneral, Boolean.TRUE);
         logger.debug("Editar Historia Clinica General: {}", historiaClinicaGeneral);
         historiaClinicaGeneralRepository.save(historiaClinicaGeneral);
         updateHCG(convertJson(new JSONObject(historiaClinicaGeneral.getHcg()),
                 historiaClinicaGeneral.getIdHistoriaClinicaGeneral()));
      } catch (HistoriaClinicaGeneralException hcge) {
         throw hcge;
      } catch (DataIntegrityViolationException dive) {
         HistoriaClinicaGeneralException hcge = new HistoriaClinicaGeneralException("No fue posible editr Historia Clinica General", HistoriaClinicaGeneralException.LAYER_DAO, HistoriaClinicaGeneralException.ACTION_UPDATE);
         hcge.addError("Ocurrio un error al editar Historia Clinica General");
         logger.error("===>>>Error al editar nuevo Historia Clinica General - CODE {} - {}", hcge.getExceptionCode(), historiaClinicaGeneralView, hcge);
         throw hcge;
      } catch (ConstraintViolationException cve) {
         HistoriaClinicaGeneralException hcge = new HistoriaClinicaGeneralException("Error inesperado al editar Historia Clinica General", HistoriaClinicaGeneralException.LAYER_DAO, HistoriaClinicaGeneralException.ACTION_UPDATE);
         hcge.addError("Ocurrió un error al editar Historia Clinica General:");
         hcge.addError("" + cve.getConstraintViolations());
         logger.error("===>>>Error al editar nuevo Historia Clinica General - CODE {} - {}", hcge.getExceptionCode(), historiaClinicaGeneralView, cve.getMessage());
         throw hcge;
      } catch (Exception ex) {
         HistoriaClinicaGeneralException hcge = new HistoriaClinicaGeneralException("Error inesperado al editar Historia Clinica General", HistoriaClinicaGeneralException.LAYER_DAO, HistoriaClinicaGeneralException.ACTION_UPDATE);
         hcge.addError("Ocurrió un error al editar Historia Clinica General:\n" + ex.getMessage());
         logger.error("===>>>Error al editar nuevo Historia Clinica General - CODE {} - {}", hcge.getExceptionCode(), historiaClinicaGeneralView, hcge);
         throw hcge;
      }
   }

   @Transactional(readOnly = false, rollbackFor = {HistoriaClinicaGeneralException.class})
   @Override
   public HistoriaClinicaGeneralView getDetailsByIdPaciente(UUID idPaciente) throws HistoriaClinicaGeneralException {
      try {
         if (idPaciente == null) {
            logger.error("===>>>No debe venir vacio el idPaciente");
            HistoriaClinicaGeneralException hcge = new HistoriaClinicaGeneralException("No debe venir vacio el idPaciente", HistoriaClinicaGeneralException.LAYER_DAO, HistoriaClinicaGeneralException.ACTION_VALIDATE);
            hcge.addError("No debe venir vacio el idPaciente");
            throw hcge;
         }
         if (historiaClinicaGeneralRepository.findByIdPaciente(idPaciente) == null) {
            logger.error("===>>>No existe un registro con ese idPaciente");
            HistoriaClinicaGeneralException hcge = new HistoriaClinicaGeneralException("No existe un registro con ese idPaciente", HistoriaClinicaGeneralException.LAYER_DAO, HistoriaClinicaGeneralException.ACTION_VALIDATE);
            hcge.addError("No existe un registro con ese idPaciente");
            throw hcge;
         }
         HistoriaClinicaGeneral historiaClinicaGeneral = historiaClinicaGeneralRepository.findByIdPaciente(idPaciente);
         logger.debug("Obtener Historia Clinica General: {}", historiaClinicaGeneral);
         return historiaClinicaGeneralConverter.toView(historiaClinicaGeneral, Boolean.TRUE);
      } catch (HistoriaClinicaGeneralException hcge) {
         throw hcge;
      } catch (DataIntegrityViolationException dive) {
         HistoriaClinicaGeneralException hcge = new HistoriaClinicaGeneralException("No fue posible editr Historia Clinica General", HistoriaClinicaGeneralException.LAYER_DAO, HistoriaClinicaGeneralException.ACTION_SELECT);
         hcge.addError("Ocurrio un error al obtener detalles Historia Clinica General");
         logger.error("Error al obtener detalles nuevo Historia Clinica General - CODE {} - {}", hcge.getExceptionCode(), idPaciente, hcge);
         throw hcge;
      } catch (ConstraintViolationException cve) {
         HistoriaClinicaGeneralException hcge = new HistoriaClinicaGeneralException("Error inesperado al obtener detalles Historia Clinica General", HistoriaClinicaGeneralException.LAYER_DAO, HistoriaClinicaGeneralException.ACTION_SELECT);
         hcge.addError("Ocurrió un error al obtener detalles Historia Clinica General:");
         hcge.addError("" + cve.getConstraintViolations());
         logger.error("Error al obtener detalles nuevo Historia Clinica General - CODE {} - {}", hcge.getExceptionCode(), idPaciente, cve.getMessage());
         throw hcge;
      } catch (Exception ex) {
         HistoriaClinicaGeneralException hcge = new HistoriaClinicaGeneralException("Error inesperado al obtener detalles Historia Clinica General", HistoriaClinicaGeneralException.LAYER_DAO, HistoriaClinicaGeneralException.ACTION_SELECT);
         hcge.addError("Ocurrió un error al obtener detalles Historia Clinica General:\n" + ex.getMessage());
         logger.error("Error al obtener detalles nuevo Historia Clinica General - CODE {} - {}", hcge.getExceptionCode(), idPaciente, hcge);
         throw hcge;
      }
   }

   @Autowired
   PersonalesNoPatologicosService personalesNoPatologicosService;

   @Autowired
   PersonalesPatologicosService personalesPatologicosService;

   @Autowired
   HCEstudioLaboratorioService estudioLaboratorioService;

   @Autowired
   InterrogatorioAparatosService interrogatorioAparatosService;

   @Autowired
   ExploracionFisicaService exploracionFisicaService;

   @Autowired
   HeredoFamiliaresService heredoFamiliaresService;

   @Autowired
   AntecendentesGinecobstetricosService ginecobstetricosService;

   @Autowired
   PadecimientoActualService padecimientoActualService;

   @Autowired
   DiagnosticoService diagnosticoService;

   @Autowired
   HCTratamientoService HCTratamientoService;

   @Autowired
   VacunacionService vacunacionService;

   @Transactional(rollbackFor = {HistoriaClinicaGeneralException.class})
   public void guardarHCG(HCGEstadisticasView view) throws HistoriaClinicaGeneralException {
      personalesNoPatologicosService.createPersonalesNoPatologicos(view.getPantallaPersonalesNoPatologicos(), view.getIdHistoriaClinica());
      personalesPatologicosService.createPersonalesPatologicos(view.getPersonalesPatologicos(), view.getIdHistoriaClinica());
      estudioLaboratorioService.createEstudioLaboratorio(view.getEstudiosLaboratorio(), view.getIdHistoriaClinica());
      interrogatorioAparatosService.createInterrogatorioAparatos(view.getPantallaInterrogatorioAparatos(), view.getIdHistoriaClinica());
      exploracionFisicaService.createExploracionFisica(view.getExploracionFisica(), view.getIdHistoriaClinica());
      heredoFamiliaresService.createHeredoFamiliares(view.getHeredoFamiliares(), view.getIdHistoriaClinica());
      ginecobstetricosService.createAntecendentesGinecobstetricos(view.getGinecobstetricos(), view.getIdHistoriaClinica());
      padecimientoActualService.createPadecimientoActual(view.getPadecimientoActual(), view.getIdHistoriaClinica());
      diagnosticoService.createDiagnostico(view.getDiagnosticoHCG(), view.getIdHistoriaClinica());
      HCTratamientoService.createTratamiento(view.getTerapeuticaEmpleada(), view.getIdHistoriaClinica());
      vacunacionService.createVacunacion(view.getEsquemaVacunacion(), view.getIdHistoriaClinica());
   }

   @Transactional(rollbackFor = {HistoriaClinicaGeneralException.class})
   public void updateHCG(HCGEstadisticasView view) throws HistoriaClinicaGeneralException {
      personalesNoPatologicosService.updatePersonalesNoPatologicos(view.getPantallaPersonalesNoPatologicos(), view.getIdHistoriaClinica());
      personalesPatologicosService.updatePersonalesPatologicos(view.getPersonalesPatologicos(), view.getIdHistoriaClinica());
      estudioLaboratorioService.updateEstudioLaboratorio(view.getEstudiosLaboratorio(), view.getIdHistoriaClinica());
      interrogatorioAparatosService.updateInterrogatorioAparatos(view.getPantallaInterrogatorioAparatos(), view.getIdHistoriaClinica());
      exploracionFisicaService.updateExploracionFisica(view.getExploracionFisica(), view.getIdHistoriaClinica());
      heredoFamiliaresService.updateHeredoFamiliares(view.getHeredoFamiliares(), view.getIdHistoriaClinica());
      ginecobstetricosService.updateAntecendentesGinecobstetricos(view.getGinecobstetricos(), view.getIdHistoriaClinica());
      padecimientoActualService.updatePadecimientoActual(view.getPadecimientoActual(), view.getIdHistoriaClinica());
      diagnosticoService.updateDiagnostico(view.getDiagnosticoHCG(), view.getIdHistoriaClinica());
      HCTratamientoService.updateTratamiento(view.getTerapeuticaEmpleada(), view.getIdHistoriaClinica());
      vacunacionService.updateVacunacion(view.getEsquemaVacunacion(), view.getIdHistoriaClinica());
   }

   /*@Transactional(rollbackFor = {HistoriaClinicaGeneralException.class})
   public void deleteHCG(Long idHistoriaClinica) throws HistoriaClinicaGeneralException {
      personalesNoPatologicosService.deletePersonalesNoPatologicos(idHistoriaClinica);
      personalesPatologicosService.deletePersonalesPatologicos(idHistoriaClinica);
      estudioLaboratorioService.deleteEstudioLaboratorio(idHistoriaClinica);
      interrogatorioAparatosService.deleteInterrogatorioAparatos(idHistoriaClinica);
      exploracionFisicaService.deleteExploracionFisica(idHistoriaClinica);
      heredoFamiliaresService.deleteHeredoFamiliares(idHistoriaClinica);
      ginecobstetricosService.deleteAntecendentesGinecobstetricos(idHistoriaClinica);
      padecimientoActualService.deletePadecimientoActual(idHistoriaClinica);
      diagnosticoService.deleteDiagnostico(idHistoriaClinica);
      HCTratamientoService.deleteTratamiento(idHistoriaClinica);
      vacunacionService.deleteVacunacion(idHistoriaClinica);
   }*/

   /*@Transactional(rollbackFor = {HistoriaClinicaGeneralException.class})
   public HCGEstadisticasView pruebaGet(Long idHistoriaClinica) throws HistoriaClinicaGeneralException {
      HCGEstadisticasView view = new HCGEstadisticasView();
      view.setIdHistoriaClinica(idHistoriaClinica);
      view.setPersonalesNoPatologicosView(personalesNoPatologicosService.getPersonalesNoPatologicos(idHistoriaClinica));
      view.setPersonalesPatologicosView(personalesPatologicosService.getPersonalesPatologicos(idHistoriaClinica));
      view.setEstudioLaboratorio(estudioLaboratorioService.getEstudioLaboratorio(idHistoriaClinica));
      view.setInterrogatorioAparatosView(interrogatorioAparatosService.getInterrogatorioAparatos(idHistoriaClinica));
      view.setExploracionFisicaView(exploracionFisicaService.getExploracionFisica(idHistoriaClinica));
      view.setHeredoFamiliaresView(heredoFamiliaresService.getHeredoFamiliares(idHistoriaClinica));
      view.setAntecendentesGinecobstetricosView(ginecobstetricosService.getAntecendentesGinecobstetricos(idHistoriaClinica));
      view.setPadecimientoActualView(padecimientoActualService.getPadecimientoActual(idHistoriaClinica));
      view.setDiagnosticoHCGView(diagnosticoService.getDiagnostico(idHistoriaClinica));
      view.setTratamientoView(HCTratamientoService.getTratamiento(idHistoriaClinica));
      view.setVacunacionView(vacunacionService.getVacunacion(idHistoriaClinica));
      return view;
   }*/

   private HCGEstadisticasView convertJson(JSONObject jsonObject, Long idHistoriaClinica) throws Exception {
      try {
         HCGEstadisticasView view = new HCGEstadisticasView();
         ObjectMapper mapper = new ObjectMapper();
         view.setIdHistoriaClinica(idHistoriaClinica);
         mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
         view.setPantallaPersonalesNoPatologicos(noPatologicosView(mapper, jsonObject.getJSONObject("pantallaPersonalesNoPatologicos")));
         view.setPersonalesPatologicos(mapper.readValue(jsonObject.getJSONObject("personalesPatologicos")
                 .toString(), PersonalesPatologicosView.class));
         view.setEstudiosLaboratorio(mapper.readValue(jsonObject.getJSONObject("estudiosLaboratorio")
                 .toString(), HCEstudioLaboratorioView.class));
         view.setPantallaInterrogatorioAparatos(mapper.readValue(jsonObject.getJSONObject("pantallaInterrogatorioAparatos")
                 .toString(), InterrogatorioAparatosView.class));
         view.setExploracionFisica(mapper.readValue(jsonObject.getJSONObject("exploracionFisica")
                 .toString(), ExploracionFisicaView.class));
         view.setHeredoFamiliares(heredoFamiliaresView(mapper, jsonObject.getJSONObject("heredoFamiliares")));
         view.setGinecobstetricos(ginecobstetricosFunc(jsonObject.getJSONObject("ginecobstetricos")));
         view.setPadecimientoActual(mapper.readValue(jsonObject.getJSONObject("padecimientoActual")
                 .toString(), PadecimientoActualView.class));
         view.setDiagnosticoHCG(mapper.readValue(jsonObject.getJSONObject("diagnosticoHCG")
                 .toString(), DiagnosticoHCGView.class));
         view.setTerapeuticaEmpleada(mapper.readValue(jsonObject.getJSONObject("terapeuticaEmpleada")
                 .toString(), HCTratamientoView.class));
         view.setEsquemaVacunacion(esquemaVacunacion(mapper, jsonObject.getJSONArray("esquemaVacunacion")));
         return view;
      } catch (Exception ex) {
         HistoriaClinicaGeneralException hcge = new HistoriaClinicaGeneralException("Error al convertir cadena", HistoriaClinicaGeneralException.LAYER_DAO, HistoriaClinicaGeneralException.ACTION_SELECT);
         hcge.addError("Error al convertir cadena de HC a vista: " + ex.getMessage());
         logger.error("Error al convertir json de HCG, descripción: ", ex);
         throw hcge;
      }
   }

   private PersonalesNoPatologicosView noPatologicosView(ObjectMapper mapper, JSONObject jsonObject) throws IOException {
      PersonalesNoPatologicosView view = new PersonalesNoPatologicosView();
      view.setNotas(jsonObject.getString("notas"));
      view.setPersonalesNoPatologicosVivienda(mapper.readValue(jsonObject.getJSONObject("personalesNoPatologicosVivienda")
              .toString(), ViviendaView.class));
      view.setPersonalesNoPatologicosAcDeportiva(mapper.readValue(jsonObject.getJSONObject("personalesNoPatologicosAcDeportiva")
              .toString(), ActividadDeportivaView.class));
      view.setPersonalesNoPatologicosHabAlimenticios(mapper.readValue(jsonObject.getJSONObject("personalesNoPatologicosHabAlimenticios")
              .toString(), HabitoAlimenticioView.class));
      view.setPersonalesNoPatologicosHabHigienicos(mapper.readValue(jsonObject.getJSONObject("personalesNoPatologicosHabHigienicos")
              .toString(), HabitosHigienicosView.class));
      view.setPersonalesNoPatologicosHistoriaLaboral(mapper.readValue(jsonObject.getJSONObject("personalesNoPatologicosHistoriaLaboral")
              .toString(), HistoriaLaboralView.class));
      view.setPersonalesNoPatologicosMascotas(mapper.readValue(jsonObject.getJSONObject("personalesNoPatologicosMascotas")
              .toString(), MascotasView.class));
      ArrayList<ConsumoView> consumoViews = new ArrayList<>();
      JSONArray consumoList = jsonObject.getJSONArray("PersonalesNoPatologicosTablaConsumo");
      for(int i = 0; i<consumoList.length(); i++)
         consumoViews.add(mapper.readValue(consumoList.getJSONObject(i).toString(), ConsumoView.class));
      view.setPersonalesNoPatologicosTablaConsumo(consumoViews);
      ArrayList<TablaLaboralView> laboralViews = new ArrayList<>();
      JSONArray tablaLaboral = jsonObject.getJSONArray("personalesNoPatologicosTablaLaboral");
      for(int i = 0; i<tablaLaboral.length(); i++)
         laboralViews.add(mapper.readValue(tablaLaboral.getJSONObject(i).toString(), TablaLaboralView.class));
      view.setPersonalesNoPatologicosTablaLaboral(laboralViews);
      return view;
   }

   private HeredoFamiliaresView heredoFamiliaresView(ObjectMapper mapper, JSONObject jsonObject) throws IOException {
      HeredoFamiliaresView view = new HeredoFamiliaresView();
      view.setComentarios(jsonObject.getString("comentarios"));
      view.setNotas(jsonObject.getString("notas"));
      ArrayList<HfEnfermedadesView> enfermedadesViews = new ArrayList<>();
      view.setDatosTabla(enfermedadesViews);
      JSONArray jsonArray = jsonObject.getJSONArray("datosTabla");
      for(int i = 0; i<jsonArray.length(); i++) {
         HfEnfermedadesView view1 = new HfEnfermedadesView();
         view1.setTieneEnfermedad(jsonArray.getJSONObject(i).getBoolean("tieneEnfermedad"));
         view1.setEnfermedad(jsonArray.getJSONObject(i).getString("enfermedad"));
         view1.setPadre(jsonArray.getJSONObject(i).getBoolean("Padre"));
         view1.setInfoPadre(jsonArray.getJSONObject(i).getString("infoPadre"));
         view1.setMadre(jsonArray.getJSONObject(i).getBoolean("Madre"));
         view1.setInfoMadre(jsonArray.getJSONObject(i).getString("infoMadre"));
         view1.setHermanos(jsonArray.getJSONObject(i).getBoolean("Hermanos"));
         view1.setInfoHermanos(jsonArray.getJSONObject(i).getString("infoHermanos"));
         view1.setAbuPaternos(jsonArray.getJSONObject(i).getBoolean("abuPaternos"));
         view1.setInfoAbuPaternos(jsonArray.getJSONObject(i).getString("infoAbuPaternos"));
         view1.setAbuMaternos(jsonArray.getJSONObject(i).getBoolean("abuMaternos"));
         view1.setInfoAbuMaternos(jsonArray.getJSONObject(i).getString("infoAbuMaternos"));
         view1.setTiosPaternos(jsonArray.getJSONObject(i).getBoolean("tiosPaternos"));
         view1.setInfoTiosPaternos(jsonArray.getJSONObject(i).getString("infoTiosPaternos"));
         view1.setTiosMaternos(jsonArray.getJSONObject(i).getBoolean("tiosMaternos"));
         view1.setInfoTiosMaternos(jsonArray.getJSONObject(i).getString("infoTiosMaternos"));
         enfermedadesViews.add(view1);
      }
      return view;
   }

   private ArrayList<VacunacionView> esquemaVacunacion(ObjectMapper mapper, JSONArray jsonArray) throws IOException {
      ArrayList<VacunacionView> esquemaVacunacion = new ArrayList<>();
      for(int i = 0; i < jsonArray.length(); i++)
         esquemaVacunacion.add(mapper.readValue(jsonArray.get(i).toString(), VacunacionView.class));
      return esquemaVacunacion;
   }

   private AntecendentesGinecobstetricosView ginecobstetricosFunc(JSONObject jsonObject) {
      AntecendentesGinecobstetricosView view = new AntecendentesGinecobstetricosView();
      view.setDispareuniaInicial(jsonObject.has("dispareuniaInicial")? jsonObject.getBoolean("dispareuniaInicial") : false);
      view.setDispareuniaSiempre(jsonObject.has("dispareuniaSiempre")? jsonObject.getBoolean("dispareuniaSiempre") : false);
      view.setDispareuniaSangrado(jsonObject.has("dispareuniaSangrado")? jsonObject.getBoolean("dispareuniaSangrado") : false);
      view.setPapanicolauResultadoNormal(jsonObject.has("papanicolauResultadoNormal")? jsonObject.getBoolean("papanicolauResultadoNormal") : false);
      view.setMamografiaResultadoNormal(jsonObject.has("mamografiaResultadoNormal")? jsonObject.getBoolean("mamografiaResultadoNormal") : false);
      view.setAsherman(jsonObject.has("asherman")? jsonObject.getBoolean("asherman") : false);
      view.setRetrodesviacion(jsonObject.has("retrodesviacion")? jsonObject.getBoolean("retrodesviacion") : false);
      view.setCervicitis(jsonObject.has("cervicitis")? jsonObject.getBoolean("cervicitis") : false);
      view.setPolipoEndometrial(jsonObject.has("polipoEndometrial")? jsonObject.getBoolean("polipoEndometrial") : false);
      view.setAdherenciaUterina(jsonObject.has("adherenciaUterina")? jsonObject.getBoolean("adherenciaUterina") : false);
      view.setPeriodosIrregulares(jsonObject.has("periodosIrregulares")? jsonObject.getBoolean("periodosIrregulares") : false);
      view.setPeriodosRegulares(jsonObject.has("periodosRegulares")
              && !jsonObject.isNull("periodosRegulares") ? jsonObject.getBoolean("periodosRegulares") : null);
      view.setPeriodo28dias(jsonObject.has("periodo28dias")? jsonObject.getBoolean("periodo28dias") : false);
      view.setPeriodo30dias(jsonObject.has("periodo30dias")? jsonObject.getBoolean("periodo30dias") : false);
      view.setPeriodoOtroSi(jsonObject.has("periodoOtroSi")? jsonObject.getBoolean("periodoOtroSi") : false);
      view.setProlapsoUterino(jsonObject.has("prolapsoUterino")? jsonObject.getBoolean("prolapsoUterino") : false);
      view.setOvarios(jsonObject.has("ovarios")? jsonObject.getBoolean("ovarios") : false);
      view.setMiomasFibromas(jsonObject.has("miomasFibromas")? jsonObject.getBoolean("miomasFibromas") : false);
      view.setEndomeotriosis(jsonObject.has("Endomeotriosis")? jsonObject.getBoolean("Endomeotriosis") : false);
      view.setHiperplasiaEndometrial(jsonObject.has("hiperplasiaEndometrial")? jsonObject.getBoolean("hiperplasiaEndometrial") : false);
      view.setAmonorrea(jsonObject.has("amonorrea")? jsonObject.getBoolean("amonorrea") : false);
      view.setMetorragia(jsonObject.has("metorragia")? jsonObject.getBoolean("metorragia") : false);
      view.setMenorragia(jsonObject.has("menorragia")? jsonObject.getBoolean("menorragia") : false);
      view.setDismorrea(jsonObject.has("dismorrea")? jsonObject.getBoolean("dismorrea") : false);
      view.setSindromepremenstrual(jsonObject.has("sindromepremenstrual")? jsonObject.getBoolean("sindromepremenstrual") : false);
      view.setMenstracionCantidadNormal(jsonObject.has("menstracionCantidadNormal")? jsonObject.getBoolean("menstracionCantidadNormal") : false);
      view.setMenstracionCantidadHipermenorrea(jsonObject.has("menstracionCantidadHipermenorrea")? jsonObject.getBoolean("menstracionCantidadHipermenorrea") : false);
      view.setMenstracionCantidadHipomonorrea(jsonObject.has("menstracionCantidadHipomonorrea")? jsonObject.getBoolean("menstracionCantidadHipomonorrea") : false);
      view.setMenstracionCantidadAmenorrea(jsonObject.has("menstracionCantidadAmenorrea")? jsonObject.getBoolean("menstracionCantidadAmenorrea") : false);
      view.setMenstruacion3Dias(jsonObject.has("menstruacion3Dias")? jsonObject.getBoolean("menstruacion3Dias") : false);
      view.setMenstruacion4Dias(jsonObject.has("menstruacion4Dias")? jsonObject.getBoolean("menstruacion4Dias") : false);
      view.setMenstruacion5Dias(jsonObject.has("menstruacion5Dias")? jsonObject.getBoolean("menstruacion5Dias") : false);
      view.setFumDisminorrea(jsonObject.has("fumDisminorrea")? jsonObject.getBoolean("fumDisminorrea") : false);
      view.setIvsaSexualActivo(jsonObject.has("ivsaSexualActivo")? jsonObject.getBoolean("ivsaSexualActivo") : false);
      view.setAnticon(jsonObject.has("anticon")? jsonObject.getBoolean("anticon") : false);
      view.setEts(jsonObject.has("ets")? jsonObject.getBoolean("ets") : false);
      view.setMenopausiaBochornos(jsonObject.has("menopausiaBochornos")? jsonObject.getBoolean("menopausiaBochornos") : false);
      view.setMenopausiaInsomnio(jsonObject.has("menopausiaInsomnio")? jsonObject.getBoolean("menopausiaInsomnio") : false);
      view.setMenopausiaDepresion(jsonObject.has("menopausiaDepresion")? jsonObject.getBoolean("menopausiaDepresion") : false);
      view.setEmbarazada(jsonObject.has("embarazada")? jsonObject.getBoolean("embarazada") : false);
      view.setPeriodoOtro(jsonObject.has("periodoOtro")? jsonObject.getString("periodoOtro") : null);
      view.setPeriodoOtro(jsonObject.has("periodo")? jsonObject.getString("periodo") : null);
      view.setAnticonTiempo(jsonObject.has("anticonTiempo")? jsonObject.getString("anticonTiempo") : null);
      view.setEtsNombre(jsonObject.has("etsNombre")? jsonObject.getString("etsNombre") : null);
      view.setEtsTratamiento(jsonObject.has("etsTratamiento")? jsonObject.getString("etsTratamiento") : null);
      view.setEtsevolucion(jsonObject.has("etsevolucion")? jsonObject.getString("etsevolucion") : null);
      view.setTiempoevolucionDispareunia(jsonObject.has("tiempoevolucionDispareunia")? jsonObject.getString("tiempoevolucionDispareunia") : null);
      view.setFrecuencia(jsonObject.has("frecuencia")? jsonObject.getString("frecuencia") : null);
      view.setSecrecion(jsonObject.has("secrecion")? jsonObject.getBoolean("secrecion") : false);
      view.setSecrecionDescripcion(jsonObject.has("secrecionDescripcion")? jsonObject.getString("secrecionDescripcion") : null);
      view.setNotavidasexual(jsonObject.has("notavidasexual")? jsonObject.getString("notavidasexual") : null);
      view.setOlorvaginal(jsonObject.has("olorvaginal")? jsonObject.getBoolean("olorvaginal") : false);
      view.setSecrecionvaginal(jsonObject.has("secrecionvaginal")? jsonObject.getBoolean("secrecionvaginal") : false);
      view.setDescripcionsecrecion(jsonObject.has("descripcionsecrecion")? jsonObject.getString("descripcionsecrecion") : null);
      view.setPrurito(jsonObject.has("prurito")? jsonObject.getBoolean("prurito") : false);
      view.setResequedadvaginal(jsonObject.has("resequedadvaginal")? jsonObject.getBoolean("resequedadvaginal") : false);
      view.setDuracionsintomas(jsonObject.has("duracionsintomas")? jsonObject.getString("duracionsintomas") : null);
      view.setTratamientovaginal(jsonObject.has("tratamientovaginal")? jsonObject.getString("tratamientovaginal") : null);
      view.setMenarca(jsonObject.has("menarca")? jsonObject.getString("menarca") : null);
      view.setPeriodoTiempo(jsonObject.has("periodoTiempo")? jsonObject.getString("periodoTiempo") : null);
      view.setMamografiaInput(jsonObject.has("mamografiaInput")? jsonObject.getString("mamografiaInput") : null);
      view.setMenstracionCantidad(jsonObject.has("menstracionCantidad")? jsonObject.getString("menstracionCantidad") : null);
      view.setMenstruacionTiempoOtro(jsonObject.has("menstruacionTiempoOtro")? jsonObject.getString("menstruacionTiempoOtro") : null);
      view.setLeucorreaCaracteristicas(jsonObject.has("leucorreaCaracteristicas")? jsonObject.getString("leucorreaCaracteristicas") : null);
      view.setLeucorreaTiempoEvol(jsonObject.has("leucorreaTiempoEvol")? jsonObject.getString("leucorreaTiempoEvol") : null);
      view.setLeucorreaTratamiento(jsonObject.has("leucorreaTratamiento")? jsonObject.getString("leucorreaTratamiento") : null);
      view.setFumInput(jsonObject.has("fumInput")? jsonObject.getString("fumInput") : null);
      view.setIvsa(jsonObject.has("ivsa")? jsonObject.getString("ivsa") : null);
      view.setAnticonNombre(jsonObject.has("anticonNombre")? jsonObject.getString("anticonNombre") : null);
      view.setEtsTipo(jsonObject.has("etsTipo")? jsonObject.getString("etsTipo") : null);
      view.setEtsTiempoEvol(jsonObject.has("etsTiempoEvol")? jsonObject.getString("etsTiempoEvol") : null);
      view.setDispareunia(jsonObject.has("dispareunia")? jsonObject.getBoolean("dispareunia") : null);
      view.setPapanicolauInput(jsonObject.has("papanicolauInput")? jsonObject.getString("papanicolauInput") : null);
      view.setMetrorragia(jsonObject.has("metrorragia")? jsonObject.getString("metrorragia") : null);
      view.setDoccu(jsonObject.has("doccu")? jsonObject.getString("doccu") : null);
      view.setComplicacionesEmbarazo(jsonObject.has("complicacionesEmbarazo")? jsonObject.getString("complicacionesEmbarazo") : null);
      view.setComentarios(jsonObject.has("comentarios")? jsonObject.getString("comentarios") : null);
      view.setComentariosdoccma(jsonObject.has("comentariosdoccma")? jsonObject.getString("comentariosdoccma") : null);
      view.setDoccma(jsonObject.has("doccma")? jsonObject.getString("doccma") : null);
      view.setMenopausiaOtros(jsonObject.has("menopausiaOtros")? jsonObject.getString("menopausiaOtros") : null);
      view.setEmbarazoDescripcion(jsonObject.has("embarazoDescripcion")? jsonObject.getString("embarazoDescripcion") : null);
      view.setEmbarazo1trimestre(jsonObject.has("embarazo1trimestre")? jsonObject.getString("embarazo1trimestre") : null);
      view.setEmbarazo2trimestre(jsonObject.has("embarazo2trimestre")? jsonObject.getString("embarazo2trimestre") : null);
      view.setEmbarazo3trimestre(jsonObject.has("embarazo3trimestre")? jsonObject.getString("embarazo3trimestre") : null);
      view.setEmbarazoParto(jsonObject.has("embarazoParto")? jsonObject.getString("embarazoParto") : null);
      view.setEmbarazoOtros(jsonObject.has("embarazoOtros")? jsonObject.getString("embarazoOtros") : null);
      view.setNotas(jsonObject.has("notas")? jsonObject.getString("notas") : null);
      view.setNumeroParejas(jsonObject.has("numeroParejas")? jsonObject.getInt("numeroParejas") : null);
      view.setGestas(jsonObject.has("gestas")? jsonObject.getInt("gestas") : null);
      view.setCesareas(jsonObject.has("cesareas")? jsonObject.getInt("cesareas") : null);
      view.setOrbitos(jsonObject.has("orbitos")? jsonObject.getInt("orbitos") : null);
      view.setPartos(jsonObject.has("partos")? jsonObject.getInt("partos") : null);
      view.setAbortos(jsonObject.has("abortos")? jsonObject.getInt("abortos") : null);
      view.setMenopausiaEdad(jsonObject.has("menopausiaEdad")? jsonObject.getInt("menopausiaEdad") : null);
      return view;
   }
}
