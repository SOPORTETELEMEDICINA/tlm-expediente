package net.amentum.niomedic.expediente.service.historia_clinica.interrogatorio_aparatos;

import lombok.extern.slf4j.Slf4j;
import net.amentum.niomedic.expediente.converter.historia_clinica_general.interrogatorio_aparatos.*;
import net.amentum.niomedic.expediente.exception.HistoriaClinicaGeneralException;
import net.amentum.niomedic.expediente.model.historia_clinica.interrogatorio_aparatos.*;
import net.amentum.niomedic.expediente.persistence.historia_clinica.interrogatorio_aparatos.InterrogatorioAparatosRepository;
import net.amentum.niomedic.expediente.views.historia_clinica_general.interrogatorio_aparatos.InterrogatorioAparatosView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class InterrogatorioAparatosServiceImpl implements InterrogatorioAparatosService {

    @Autowired
    InterrogatorioAparatosRepository repository;

    @Autowired
    SentidoVisionConverter visionConverter;
    @Autowired
    SentidoAudicionConverter audicionConverter;
    @Autowired
    SentidoOlfatoConverter olfatoConverter;
    @Autowired
    SentidoGustoConverter gustoConverter;
    @Autowired
    SentidoTactoConverter tactoConverter;
    @Autowired
    InterrogatorioRespiratorioConverter respiratorioConverter;
    @Autowired
    InterrogatorioCardiovascularConverter cardiovascularConverter;
    @Autowired
    InterrogatorioDigestivoConverter digestivoConverter;
    @Autowired
    InterrogatorioEndocrinoConverter endocrinoConverter;
    @Autowired
    InterrogatorioSistemaNerviosoConverter sistemaNerviosoConverter;
    @Autowired
    InterrogatorioPielConverter pielConverter;
    @Autowired
    InterrogatorioPsiquiatricoConverter psiquiatricoConverter;
    @Autowired
    InterrogatorioGenitourinarioConverter genitourinarioConverter;
    @Autowired
    InterrogatorioUrinarioConverter urinarioConverter;
    @Autowired
    InterrogatorioReproductorConverter reproductorConverter;
    @Autowired
    InterrogatorioHemolinfaticoConverter hemolinfaticoConverter;
    @Autowired
    InterrogatorioMusculoEsqueleticoConverter musculoEsqueleticoConverter;
    @Autowired
    InterrogatorioOncologicoConverter oncologicoConverter;
    @Autowired
    InterrogatorioHematologicoConverter hematologicoConverter;

    @Transactional()
    @Override
    public void createInterrogatorioAparatos(InterrogatorioAparatosView view, Long idHistoriaClinica) throws HistoriaClinicaGeneralException {
        try {
            if(repository.findByIdHistoriaClinica(idHistoriaClinica) != null) {
                HistoriaClinicaGeneralException hcge = new HistoriaClinicaGeneralException("Error al insertar Interrogatorio aparatos", HistoriaClinicaGeneralException.LAYER_DAO, HistoriaClinicaGeneralException.ACTION_INSERT);
                hcge.addError("Interrogatorio aparatos ya registrado para la historia clínica " + idHistoriaClinica);
                log.error("Interrogatorio aparatos ya registrado para la historia clínica {}", idHistoriaClinica);
                throw hcge;
            }
            view.getInterrogatorioOrganoSentidosAudicion().setIdHistoriaClinica(idHistoriaClinica);
            view.getInterrogatorioOrganoSentidosVision().setIdHistoriaClinica(idHistoriaClinica);
            view.getInterrogatorioOrganoSentidosOlfato().setIdHistoriaClinica(idHistoriaClinica);
            view.getInterrogatorioOrganoSentidosGusto().setIdHistoriaClinica(idHistoriaClinica);
            view.getInterrogatorioOrganoSentidosTacto().setIdHistoriaClinica(idHistoriaClinica);
            view.getInterrogatorioRespiratorio().setIdHistoriaClinica(idHistoriaClinica);
            view.getInterrogatorioCardiovascular().setIdHistoriaClinica(idHistoriaClinica);
            view.getInterrogatorioDigestivo().setIdHistoriaClinica(idHistoriaClinica);
            view.getInterrogatorioEndocrino().setIdHistoriaClinica(idHistoriaClinica);
            view.getInterrogatorioSistemaNervioso().setIdHistoriaClinica(idHistoriaClinica);
            view.getInterrogatorioPielyAnexos().setIdHistoriaClinica(idHistoriaClinica);
            view.getInterrogatorioPsiquiatrico().setIdHistoriaClinica(idHistoriaClinica);
            view.getInterrogatorioGenitourinario().setIdHistoriaClinica(idHistoriaClinica);
            view.getInterrogatorioUrinario().setIdHistoriaClinica(idHistoriaClinica);
            view.getInterrogatorioReproductor().setIdHistoriaClinica(idHistoriaClinica);
            view.getInterrogatorioHemolinfatico().setIdHistoriaClinica(idHistoriaClinica);
            view.getInterrogatorioMusculoEsqueletico().setIdHistoriaClinica(idHistoriaClinica);
            view.getInterrogatorioOncologico().setIdHistoriaClinica(idHistoriaClinica);
            view.getInterrogatorioHematologico().setIdHistoriaClinica(idHistoriaClinica);

            SentidoAudicion entityAudicion = audicionConverter.toEntity(view.getInterrogatorioOrganoSentidosAudicion());
            SentidoVision entityVision = visionConverter.toEntity(view.getInterrogatorioOrganoSentidosVision());
            SentidoOlfato entityOlfato = olfatoConverter.toEntity(view.getInterrogatorioOrganoSentidosOlfato());
            SentidoGusto entityGusto = gustoConverter.toEntity(view.getInterrogatorioOrganoSentidosGusto());
            SentidoTacto entityTacto = tactoConverter.toEntity(view.getInterrogatorioOrganoSentidosTacto());
            InterrogatorioRespiratorio entityRespiratorio = respiratorioConverter.toEntity(view.getInterrogatorioRespiratorio());
            InterrogatorioCardiovascular entityCardiovascular = cardiovascularConverter.toEntity(view.getInterrogatorioCardiovascular());
            InterrogatorioDigestivo entityDigestivo = digestivoConverter.toEntity(view.getInterrogatorioDigestivo());
            InterrogatorioEndocrino entityEndocrino = endocrinoConverter.toEntity(view.getInterrogatorioEndocrino());
            InterrogatorioSistemaNervioso entitySistemaNervioso = sistemaNerviosoConverter.toEntity(view.getInterrogatorioSistemaNervioso());
            InterrogatorioPiel entityPiel = pielConverter.toEntity(view.getInterrogatorioPielyAnexos());
            InterrogatorioPsiquiatrico entityPsiquiatrico = psiquiatricoConverter.toEntity(view.getInterrogatorioPsiquiatrico());
            InterrogatorioGenitourinario entityGenitourinario = genitourinarioConverter.toEntity(view.getInterrogatorioGenitourinario());
            InterrogatorioUrinario entityUrinario = urinarioConverter.toEntity(view.getInterrogatorioUrinario());
            InterrogatorioReproductor entityReproductor = reproductorConverter.toEntity(view.getInterrogatorioReproductor());
            InterrogatorioHemolinfatico entityHemolinfatico = hemolinfaticoConverter.toEntity(view.getInterrogatorioHemolinfatico());
            InterrogatorioMusculoEsqueletico entityMusculoEsqueletico = musculoEsqueleticoConverter.toEntity(view.getInterrogatorioMusculoEsqueletico());
            InterrogatorioOncologico entityOncologico = oncologicoConverter.toEntity(view.getInterrogatorioOncologico());
            InterrogatorioHematologico entityHematologico = hematologicoConverter.toEntity(view.getInterrogatorioHematologico());

            InterrogatorioAparatos aparatos = new InterrogatorioAparatos();
            aparatos.setIdHistoriaClinica(idHistoriaClinica);
            aparatos.setNotas(view.getNotas());
            aparatos.setSentidoAudicion(entityAudicion);
            aparatos.setSentidoVision(entityVision);
            aparatos.setSentidoOlfato(entityOlfato);
            aparatos.setSentidoGusto(entityGusto);
            aparatos.setSentidoTacto(entityTacto);
            aparatos.setInterrogatorioRespiratorio(entityRespiratorio);
            aparatos.setInterrogatorioCardiovascular(entityCardiovascular);
            aparatos.setInterrogatorioDigestivo(entityDigestivo);
            aparatos.setInterrogatorioEndocrino(entityEndocrino);
            aparatos.setInterrogatorioSistemaNervioso(entitySistemaNervioso);
            aparatos.setInterrogatorioPiel(entityPiel);
            aparatos.setInterrogatorioPsiquiatrico(entityPsiquiatrico);
            aparatos.setInterrogatorioGenitourinario(entityGenitourinario);
            aparatos.setInterrogatorioUrinario(entityUrinario);
            aparatos.setInterrogatorioReproductor(entityReproductor);
            aparatos.setInterrogatorioHemolinfatico(entityHemolinfatico);
            aparatos.setInterrogatorioMusculoEsqueletico(entityMusculoEsqueletico);
            aparatos.setInterrogatorioOncologico(entityOncologico);
            aparatos.setInterrogatorioHematologico(entityHematologico);

            entityAudicion.setInterrogatorioAparatos(aparatos);
            entityVision.setInterrogatorioAparatos(aparatos);
            entityOlfato.setInterrogatorioAparatos(aparatos);
            entityGusto.setInterrogatorioAparatos(aparatos);
            entityTacto.setInterrogatorioAparatos(aparatos);
            entityRespiratorio.setInterrogatorioAparatos(aparatos);
            entityCardiovascular.setInterrogatorioAparatos(aparatos);
            entityDigestivo.setInterrogatorioAparatos(aparatos);
            entityEndocrino.setInterrogatorioAparatos(aparatos);
            entitySistemaNervioso.setInterrogatorioAparatos(aparatos);
            entityPiel.setInterrogatorioAparatos(aparatos);
            entityPsiquiatrico.setInterrogatorioAparatos(aparatos);
            entityGenitourinario.setInterrogatorioAparatos(aparatos);
            entityUrinario.setInterrogatorioAparatos(aparatos);
            entityReproductor.setInterrogatorioAparatos(aparatos);
            entityHemolinfatico.setInterrogatorioAparatos(aparatos);
            entityMusculoEsqueletico.setInterrogatorioAparatos(aparatos);
            entityOncologico.setInterrogatorioAparatos(aparatos);
            entityHematologico.setInterrogatorioAparatos(aparatos);
            repository.save(aparatos);
            log.info("Agregando Interrogatorio aparatos - HistoriaClinica: {}", view.getIdHistoriaClinica());
        } catch (HistoriaClinicaGeneralException ex) {
            throw ex;
        } catch (Exception ex) {
            HistoriaClinicaGeneralException hcge = new HistoriaClinicaGeneralException("Error al insertar Interrogatorio aparatos", HistoriaClinicaGeneralException.LAYER_DAO, HistoriaClinicaGeneralException.ACTION_INSERT);
            hcge.addError(ex.getCause().toString());
            ex.printStackTrace();
            throw hcge;
        }
    }

    @Override
    public void updateInterrogatorioAparatos(InterrogatorioAparatosView view, Long idHistoriaClinica) throws HistoriaClinicaGeneralException {
        try {
            SentidoAudicion entityAudicion = audicionConverter.toEntity(view.getInterrogatorioOrganoSentidosAudicion());
            entityAudicion.setIdHistoriaClinica(idHistoriaClinica);
            SentidoVision entityVision = visionConverter.toEntity(view.getInterrogatorioOrganoSentidosVision());
            entityVision.setIdHistoriaClinica(idHistoriaClinica);
            SentidoOlfato entityOlfato = olfatoConverter.toEntity(view.getInterrogatorioOrganoSentidosOlfato());
            entityOlfato.setIdHistoriaClinica(idHistoriaClinica);
            SentidoGusto entityGusto = gustoConverter.toEntity(view.getInterrogatorioOrganoSentidosGusto());
            entityGusto.setIdHistoriaClinica(idHistoriaClinica);
            SentidoTacto entityTacto = tactoConverter.toEntity(view.getInterrogatorioOrganoSentidosTacto());
            entityTacto.setIdHistoriaClinica(idHistoriaClinica);
            InterrogatorioRespiratorio entityRespiratorio = respiratorioConverter.toEntity(view.getInterrogatorioRespiratorio());
            entityRespiratorio.setIdHistoriaClinica(idHistoriaClinica);
            InterrogatorioCardiovascular entityCardiovascular = cardiovascularConverter.toEntity(view.getInterrogatorioCardiovascular());
            entityCardiovascular.setIdHistoriaClinica(idHistoriaClinica);
            InterrogatorioDigestivo entityDigestivo = digestivoConverter.toEntity(view.getInterrogatorioDigestivo());
            entityDigestivo.setIdHistoriaClinica(idHistoriaClinica);
            InterrogatorioEndocrino entityEndocrino = endocrinoConverter.toEntity(view.getInterrogatorioEndocrino());
            entityEndocrino.setIdHistoriaClinica(idHistoriaClinica);
            InterrogatorioSistemaNervioso entitySistemaNervioso = sistemaNerviosoConverter.toEntity(view.getInterrogatorioSistemaNervioso());
            entitySistemaNervioso.setIdHistoriaClinica(idHistoriaClinica);
            InterrogatorioPiel entityPiel = pielConverter.toEntity(view.getInterrogatorioPielyAnexos());
            entityPiel.setIdHistoriaClinica(idHistoriaClinica);
            InterrogatorioPsiquiatrico entityPsiquiatrico = psiquiatricoConverter.toEntity(view.getInterrogatorioPsiquiatrico());
            entityPsiquiatrico.setIdHistoriaClinica(idHistoriaClinica);
            InterrogatorioGenitourinario entityGenitourinario = genitourinarioConverter.toEntity(view.getInterrogatorioGenitourinario());
            entityGenitourinario.setIdHistoriaClinica(idHistoriaClinica);
            InterrogatorioUrinario entityUrinario = urinarioConverter.toEntity(view.getInterrogatorioUrinario());
            entityUrinario.setIdHistoriaClinica(idHistoriaClinica);
            InterrogatorioReproductor entityReproductor = reproductorConverter.toEntity(view.getInterrogatorioReproductor());
            entityReproductor.setIdHistoriaClinica(idHistoriaClinica);
            InterrogatorioHemolinfatico entityHemolinfatico = hemolinfaticoConverter.toEntity(view.getInterrogatorioHemolinfatico());
            entityHemolinfatico.setIdHistoriaClinica(idHistoriaClinica);
            InterrogatorioMusculoEsqueletico entityMusculoEsqueletico = musculoEsqueleticoConverter.toEntity(view.getInterrogatorioMusculoEsqueletico());
            entityMusculoEsqueletico.setIdHistoriaClinica(idHistoriaClinica);
            InterrogatorioOncologico entityOncologico = oncologicoConverter.toEntity(view.getInterrogatorioOncologico());
            entityOncologico.setIdHistoriaClinica(idHistoriaClinica);
            InterrogatorioHematologico entityHematologico = hematologicoConverter.toEntity(view.getInterrogatorioHematologico());
            entityHematologico.setIdHistoriaClinica(idHistoriaClinica);
            InterrogatorioAparatos aparatos = new InterrogatorioAparatos();
            InterrogatorioAparatos prevEntity = repository.findByIdHistoriaClinica(idHistoriaClinica);
            if(prevEntity == null)
                log.error("Nuevo/Update - Interrogatorio aparatos no registrado para la historia clínica {}", idHistoriaClinica);
            else {
                aparatos.setIdInterrogatorio(prevEntity.getIdInterrogatorio());
                entityAudicion.setIdInterrogatorio(prevEntity.getSentidoAudicion().getIdInterrogatorio());
                entityVision.setIdInterrogatorio(prevEntity.getSentidoVision().getIdInterrogatorio());
                entityOlfato.setIdInterrogatorio(prevEntity.getSentidoOlfato().getIdInterrogatorio());
                entityGusto.setIdInterrogatorio(prevEntity.getSentidoGusto().getIdInterrogatorio());
                entityTacto.setIdInterrogatorio(prevEntity.getSentidoTacto().getIdInterrogatorio());
                entityRespiratorio.setIdInterrogatorio(prevEntity.getInterrogatorioRespiratorio().getIdInterrogatorio());
                entityCardiovascular.setIdInterrogatorio(prevEntity.getInterrogatorioCardiovascular().getIdInterrogatorio());
                entityDigestivo.setIdInterrogatorio(prevEntity.getInterrogatorioDigestivo().getIdInterrogatorio());
                entityEndocrino.setIdInterrogatorio(prevEntity.getInterrogatorioEndocrino().getIdInterrogatorio());
                entitySistemaNervioso.setIdInterrogatorio(prevEntity.getInterrogatorioSistemaNervioso().getIdInterrogatorio());
                entityPiel.setIdInterrogatorio(prevEntity.getInterrogatorioPiel().getIdInterrogatorio());
                entityPsiquiatrico.setIdInterrogatorio(prevEntity.getInterrogatorioPsiquiatrico().getIdInterrogatorio());
                entityGenitourinario.setIdInterrogatorio(prevEntity.getInterrogatorioGenitourinario().getIdInterrogatorio());
                entityUrinario.setIdInterrogatorio(prevEntity.getInterrogatorioUrinario().getIdInterrogatorio());
                entityReproductor.setIdInterrogatorio(prevEntity.getInterrogatorioReproductor().getIdInterrogatorio());
                entityHemolinfatico.setIdInterrogatorio(prevEntity.getInterrogatorioHemolinfatico().getIdInterrogatorio());
                entityMusculoEsqueletico.setIdInterrogatorio(prevEntity.getInterrogatorioMusculoEsqueletico().getIdInterrogatorio());
                entityOncologico.setIdInterrogatorio(prevEntity.getInterrogatorioOncologico().getIdInterrogatorio());
                entityHematologico.setIdInterrogatorio(prevEntity.getInterrogatorioHematologico().getIdInterrogatorio());
            }
            aparatos.setIdHistoriaClinica(idHistoriaClinica);
            aparatos.setNotas(view.getNotas());
            aparatos.setSentidoAudicion(entityAudicion);
            aparatos.setSentidoVision(entityVision);
            aparatos.setSentidoOlfato(entityOlfato);
            aparatos.setSentidoGusto(entityGusto);
            aparatos.setSentidoTacto(entityTacto);
            aparatos.setInterrogatorioRespiratorio(entityRespiratorio);
            aparatos.setInterrogatorioCardiovascular(entityCardiovascular);
            aparatos.setInterrogatorioDigestivo(entityDigestivo);
            aparatos.setInterrogatorioEndocrino(entityEndocrino);
            aparatos.setInterrogatorioSistemaNervioso(entitySistemaNervioso);
            aparatos.setInterrogatorioPiel(entityPiel);
            aparatos.setInterrogatorioPsiquiatrico(entityPsiquiatrico);
            aparatos.setInterrogatorioGenitourinario(entityGenitourinario);
            aparatos.setInterrogatorioUrinario(entityUrinario);
            aparatos.setInterrogatorioReproductor(entityReproductor);
            aparatos.setInterrogatorioHemolinfatico(entityHemolinfatico);
            aparatos.setInterrogatorioMusculoEsqueletico(entityMusculoEsqueletico);
            aparatos.setInterrogatorioOncologico(entityOncologico);
            aparatos.setInterrogatorioHematologico(entityHematologico);

            entityAudicion.setInterrogatorioAparatos(aparatos);
            entityVision.setInterrogatorioAparatos(aparatos);
            entityOlfato.setInterrogatorioAparatos(aparatos);
            entityGusto.setInterrogatorioAparatos(aparatos);
            entityTacto.setInterrogatorioAparatos(aparatos);
            entityRespiratorio.setInterrogatorioAparatos(aparatos);
            entityCardiovascular.setInterrogatorioAparatos(aparatos);
            entityDigestivo.setInterrogatorioAparatos(aparatos);
            entityEndocrino.setInterrogatorioAparatos(aparatos);
            entitySistemaNervioso.setInterrogatorioAparatos(aparatos);
            entityPiel.setInterrogatorioAparatos(aparatos);
            entityPsiquiatrico.setInterrogatorioAparatos(aparatos);
            entityGenitourinario.setInterrogatorioAparatos(aparatos);
            entityUrinario.setInterrogatorioAparatos(aparatos);
            entityReproductor.setInterrogatorioAparatos(aparatos);
            entityHemolinfatico.setInterrogatorioAparatos(aparatos);
            entityMusculoEsqueletico.setInterrogatorioAparatos(aparatos);
            entityOncologico.setInterrogatorioAparatos(aparatos);
            entityHematologico.setInterrogatorioAparatos(aparatos);
            repository.save(aparatos);
            log.info("Editando Interrogatorio aparatos - Id: {} - HistoriaClinica: {}", aparatos.getIdInterrogatorio(), view.getIdHistoriaClinica());
        } catch (Exception ex) {
            HistoriaClinicaGeneralException hcge = new HistoriaClinicaGeneralException("Error al actualizar Interrogatorio aparatos", HistoriaClinicaGeneralException.LAYER_DAO, HistoriaClinicaGeneralException.ACTION_UPDATE);
            hcge.addError(ex.toString());
            log.error("Error: " + ex);
            throw hcge;
        }
    }

    @Override
    public InterrogatorioAparatosView getInterrogatorioAparatos(Long idHistoriaClinica) throws HistoriaClinicaGeneralException {
        try {
            InterrogatorioAparatos aparatos = repository.findByIdHistoriaClinica(idHistoriaClinica);
            if(aparatos == null) {
                HistoriaClinicaGeneralException hcge = new HistoriaClinicaGeneralException("Error al obtener Interrogatorio aparatos", HistoriaClinicaGeneralException.LAYER_DAO, HistoriaClinicaGeneralException.ACTION_SELECT);
                hcge.addError("Interrogatorio aparatos no registrado para la historia clínica " + idHistoriaClinica);
                log.error("Interrogatorio aparatos no registrado para la historia clínica {}", idHistoriaClinica);
                throw hcge;
            }
            InterrogatorioAparatosView view = new InterrogatorioAparatosView();
            view.setIdInterrogatorio(aparatos.getIdInterrogatorio());
            view.setNotas(aparatos.getNotas());
            view.setInterrogatorioOrganoSentidosVision(visionConverter.toView(aparatos.getSentidoVision()));
            view.setInterrogatorioOrganoSentidosAudicion(audicionConverter.toView(aparatos.getSentidoAudicion()));
            view.setInterrogatorioOrganoSentidosOlfato(olfatoConverter.toView(aparatos.getSentidoOlfato()));
            view.setInterrogatorioOrganoSentidosGusto(gustoConverter.toView(aparatos.getSentidoGusto()));
            view.setInterrogatorioOrganoSentidosTacto(tactoConverter.toView(aparatos.getSentidoTacto()));
            view.setInterrogatorioRespiratorio(respiratorioConverter.toView(aparatos.getInterrogatorioRespiratorio()));
            view.setInterrogatorioCardiovascular(cardiovascularConverter.toView(aparatos.getInterrogatorioCardiovascular()));
            view.setInterrogatorioDigestivo(digestivoConverter.toView(aparatos.getInterrogatorioDigestivo()));
            view.setInterrogatorioEndocrino(endocrinoConverter.toView(aparatos.getInterrogatorioEndocrino()));
            view.setInterrogatorioSistemaNervioso(sistemaNerviosoConverter.toView(aparatos.getInterrogatorioSistemaNervioso()));
            view.setInterrogatorioPielyAnexos(pielConverter.toView(aparatos.getInterrogatorioPiel()));
            view.setInterrogatorioPsiquiatrico(psiquiatricoConverter.toView(aparatos.getInterrogatorioPsiquiatrico()));
            view.setInterrogatorioGenitourinario(genitourinarioConverter.toView(aparatos.getInterrogatorioGenitourinario()));
            view.setInterrogatorioUrinario(urinarioConverter.toView(aparatos.getInterrogatorioUrinario()));
            view.setInterrogatorioReproductor(reproductorConverter.toView(aparatos.getInterrogatorioReproductor()));
            view.setInterrogatorioHemolinfatico(hemolinfaticoConverter.toView(aparatos.getInterrogatorioHemolinfatico()));
            view.setInterrogatorioMusculoEsqueletico(musculoEsqueleticoConverter.toView(aparatos.getInterrogatorioMusculoEsqueletico()));
            view.setInterrogatorioOncologico(oncologicoConverter.toView(aparatos.getInterrogatorioOncologico()));
            view.setInterrogatorioHematologico(hematologicoConverter.toView(aparatos.getInterrogatorioHematologico()));
            return view;
        } catch (HistoriaClinicaGeneralException ex) {
            throw ex;
        } catch (Exception ex) {
            HistoriaClinicaGeneralException hcge = new HistoriaClinicaGeneralException("Error al obtener Interrogatorio aparatos", HistoriaClinicaGeneralException.LAYER_DAO, HistoriaClinicaGeneralException.ACTION_SELECT);
            hcge.addError(ex.getLocalizedMessage());
            log.error("Error: " + ex);
            throw hcge;
        }
    }

    @Override
    public void deleteInterrogatorioAparatos(Long idHistoriaClinica) throws HistoriaClinicaGeneralException {
        try {
            InterrogatorioAparatos aparatos = repository.findByIdHistoriaClinica(idHistoriaClinica);
            if(aparatos != null)
                repository.delete(aparatos);
        } catch (Exception ex) {
            HistoriaClinicaGeneralException hcge = new HistoriaClinicaGeneralException("Error al eliminar Interrogatorio aparatos", HistoriaClinicaGeneralException.LAYER_DAO, HistoriaClinicaGeneralException.ACTION_DELETE);
            hcge.addError(ex.getLocalizedMessage());
            log.error("Error: " + ex);
            throw hcge;
        }
    }
}
