package net.amentum.niomedic.expediente.service.impl;

import net.amentum.niomedic.expediente.converter.CatPreguntasConverter;
import net.amentum.niomedic.expediente.exception.CatPreguntasException;
import net.amentum.niomedic.expediente.model.CatCuestionario;
import net.amentum.niomedic.expediente.model.CatPregunta;
import net.amentum.niomedic.expediente.persistence.CatCuestionariosRepository;
import net.amentum.niomedic.expediente.persistence.CatPreguntasRepository;
import net.amentum.niomedic.expediente.service.CatPreguntasService;
import net.amentum.niomedic.expediente.views.CatCuestionarioHeader;
import net.amentum.niomedic.expediente.views.CatPreguntaView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class CatPreguntasServiceImpl implements CatPreguntasService {

    final Logger logger = LoggerFactory.getLogger(CatPreguntasServiceImpl.class);

    @Autowired
    CatPreguntasRepository repository;

    @Autowired
    CatCuestionariosRepository cuestionariosRepository;

    @Autowired
    CatPreguntasConverter converter;

    @Transactional(rollbackFor = {CatPreguntasException.class})
    @Override
    public void createPregunta(CatPreguntaView view) throws CatPreguntasException {
        try {
            CatPregunta pregunta = converter.toEntity(view);
            logger.info("Pregunta nueva: {}", pregunta);
            repository.save(pregunta);
        } catch(DataIntegrityViolationException ex) {
            CatPreguntasException exception = new CatPreguntasException("No fue posible agregar una pregunta nuevo", CatPreguntasException.LAYER_DAO, CatPreguntasException.ACTION_INSERT );
            exception.addError("Error al insertar una nueva pregunta - " + ex.toString());
            logger.error("Error al insertar una nueva pregunta - {} - CODE {} - {}", view, exception.getExceptionCode(), ex);
            throw exception;
        } catch(Exception ex) {
            CatPreguntasException exception = new CatPreguntasException("Error inesperado al agregar una pregunta nueva", CatPreguntasException.LAYER_DAO, CatPreguntasException.ACTION_INSERT );
            exception.addError("Error al insertar una nueva pregunta - " + ex.toString());
            logger.error("Error al insertar una nueva pregunta - {} - CODE {} - {}", view, exception.getExceptionCode(), ex);
            throw exception;
        }
    }

    @Transactional(rollbackFor = {CatPreguntasException.class})
    @Override
    public void updatePregunta(Integer idPregunta, CatPreguntaView view) throws CatPreguntasException {
        try {
            CatPregunta pregunta = repository.findByIdPregunta(idPregunta);
            if(pregunta == null) {
                CatPreguntasException exception = new CatPreguntasException("Error inesperado al modificar una pregunta", CatPreguntasException.LAYER_DAO, CatPreguntasException.ACTION_UPDATE );
                exception.addError("Error al modificar una pregunta, no se encuentra registrada");
                logger.error("Error al modificar una pregunta - {} - CODE {}", view, exception.getExceptionCode());
                throw exception;
            }
            pregunta.setIdCuestionario(view.getIdCuestionario());
            pregunta.setPregunta(view.getPregunta());
            pregunta.setActive(view.getActive());
            pregunta.setSort(view.getSort());
            pregunta.setTipoPregunta(view.getTipoPregunta());
            logger.info("Pregunta modificada con id: {} - contenido: {}", idPregunta, pregunta);
            repository.save(pregunta);
        } catch(DataIntegrityViolationException ex) {
            CatPreguntasException exception = new CatPreguntasException("No fue posible modificar una pregunta", CatPreguntasException.LAYER_DAO, CatPreguntasException.ACTION_UPDATE );
            exception.addError("Error al modificar una pregunta - " + ex.toString());
            logger.error("Error al modificar una pregunta - {} - CODE {} - {}", view, exception.getExceptionCode(), ex);
            throw exception;
        } catch(Exception ex) {
            CatPreguntasException exception = new CatPreguntasException("Error inesperado al modificar una pregunta", CatPreguntasException.LAYER_DAO, CatPreguntasException.ACTION_UPDATE );
            exception.addError("Error al modificar una pregunta - " + ex.toString());
            logger.error("Error al modificar una pregunta - {} - CODE {} - {}", view, exception.getExceptionCode(), ex);
            throw exception;
        }
    }

    @Override
    public CatPreguntaView getPregunta(Integer idPregunta) throws CatPreguntasException {
        try {
            CatPregunta pregunta = repository.findByIdPregunta(idPregunta);
            if(pregunta == null) {
                CatPreguntasException exception = new CatPreguntasException("Error inesperado al buscar una pregunta", CatPreguntasException.LAYER_DAO, CatPreguntasException.ACTION_SELECT );
                exception.addError("Error al buscar la pregunta, no se encuentra registrada");
                logger.error("Error al buscar la pregunta - {} - CODE {}", idPregunta, exception.getExceptionCode());
                throw exception;
            }
            return converter.toView(pregunta);
        } catch(Exception ex) {
            CatPreguntasException exception = new CatPreguntasException("Error inesperado al buscar una pregunta", CatPreguntasException.LAYER_DAO, CatPreguntasException.ACTION_SELECT );
            exception.addError("Error al buscar una pregunta - " + ex.toString());
            logger.error("Error al buscar una pregunta - {} - CODE {} - {}", idPregunta, exception.getExceptionCode(), ex);
            throw exception;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Page<CatCuestionarioHeader> getPreguntaSearch(Integer idCuestionario, Integer page, Integer size, String orderColumn, String orderType) throws CatPreguntasException {
        try {
            logger.info("search - idCuestionario: {} - page: {} - size: {} - orderColumn: {} - orderType: {}",
                    idCuestionario, page, size, orderColumn, orderType);
            List<CatCuestionarioHeader> headerList = new ArrayList<>();
            Page<CatPregunta> preguntasPage = null;
            Integer totalElements = 0;
            Sort sort = null;
            if(orderType.equalsIgnoreCase("DESC"))
                sort = new Sort(Sort.Direction.DESC, orderColumn);
            else
                sort = new Sort(Sort.Direction.ASC, orderColumn);
            PageRequest request = new PageRequest(page, size, sort);
            if(idCuestionario > 0) {
                CatCuestionarioHeader response = new CatCuestionarioHeader();
                CatCuestionario cuestionario = cuestionariosRepository.findByIdCuestionario(idCuestionario);
                if(cuestionario == null) {
                    CatPreguntasException exception = new CatPreguntasException("No fue posible buscar las preguntas", CatPreguntasException.LAYER_DAO, CatPreguntasException.ACTION_SELECT);
                    exception.addError("Error al buscar las preguntas - idCuestionario no existente");
                    logger.error("Parametros no correctos - idCuestionario no existente");
                    throw exception;
                }
                response.setIdCuestionario(cuestionario.getIdCuestionario());
                response.setNombre(cuestionario.getNombre());
                List<CatPregunta> preguntaList = repository.findByIdCuestionario(idCuestionario);
                if(preguntaList != null && !preguntaList.isEmpty()) {
                    List<CatPreguntaView> list = new ArrayList<>();
                    for (CatPregunta entity : preguntaList)
                        list.add(converter.toView(entity));
                    response.setListaPreguntas(list);
                } else
                    logger.info("No se han encontrado respuestas registradas para el idCuestionario: {}", idCuestionario);
                headerList.add(response);
            } else {
                List<Integer> idCuestionarios = repository.findUniqueIdCuestionario();
                if(idCuestionarios != null && !idCuestionarios.isEmpty()) {
                    CatCuestionarioHeader response = null;
                    for (Integer id : idCuestionarios) {
                        response = new CatCuestionarioHeader();
                        CatCuestionario cuestionario = cuestionariosRepository.findByIdCuestionario(id);
                        if(cuestionario == null) {
                            logger.error("idCuestionario no existente: {}", id);
                            continue;
                        }
                        response.setIdCuestionario(cuestionario.getIdCuestionario());
                        response.setNombre(cuestionario.getNombre());
                        Specifications<CatPregunta> spec = Specifications.where((root, query, cb) -> {
                            Predicate predicate = cb.and(cb.equal(root.get("idCuestionario"), id));
                            predicate = cb.and(predicate, cb.equal(root.get("active"), Boolean.TRUE));
                            return predicate;
                        });
                        preguntasPage = repository.findAll(spec, request);
                        List<CatPreguntaView> list = new ArrayList<>();
                        if(preguntasPage != null)
                            preguntasPage.forEach(catPregunta -> list.add(converter.toView(catPregunta)));
                        else
                            logger.info("No se han encontrado respuestas registradas para el idCuestionario: {}", id);
                        response.setListaPreguntas(list);
                        headerList.add(response);
                    }
                } else
                    logger.info("No se han encontrado cuestionarios registrados");
            }
            logger.info("Respuesta: {}", headerList);
            return new PageImpl<>(headerList, request, headerList.size());
        } catch(IllegalArgumentException ex) {
            CatPreguntasException exception = new CatPreguntasException("No fue posible buscar las preguntas", CatPreguntasException.LAYER_DAO, CatPreguntasException.ACTION_SELECT);
            exception.addError("Error al buscar las preguntas - " + ex);
            logger.error("Parametros no correctos - {}", ex.toString());
            throw exception;
        } catch(Exception ex) {
            CatPreguntasException exception = new CatPreguntasException("Error inesperado al buscar las preguntas", CatPreguntasException.LAYER_DAO, CatPreguntasException.ACTION_SELECT);
            exception.addError("Error al buscar las preguntas - " + ex);
            logger.error("Error al buscar las preguntas - {} - CODE {} - {}", idCuestionario, exception.getExceptionCode(), ex);
            throw exception;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Page<CatPreguntaView> getPreguntaSearchPage(Integer page, Integer size, String orderColumn, String orderType) throws CatPreguntasException {
        try {
            logger.info("search - page: {} - size: {} - orderColumn: {} - orderType: {}",
                    page, size, orderColumn, orderType);
            List<CatPreguntaView> viewList = new ArrayList<>();
            Page<CatPregunta> preguntasPage = null;
            Sort sort = null;
            if(orderType.equalsIgnoreCase("DESC"))
                sort = new Sort(Sort.Direction.DESC, "idCuestionario", "sort");
            else
                sort = new Sort(Sort.Direction.ASC, "idCuestionario", "sort");
            PageRequest request = new PageRequest(page, size, sort);
            preguntasPage = repository.findAll(request);
            preguntasPage.forEach(catPregunta -> {
                CatCuestionario cuestionario = cuestionariosRepository.findByIdCuestionario(catPregunta.getIdCuestionario());
                if(cuestionario == null)
                    logger.error("idCuestionario no existente: {}", catPregunta.getIdCuestionario());
                else {
                    CatPreguntaView view = converter.toView(catPregunta);
                    view.setNombreCuestionario(cuestionario.getNombre());
                    viewList.add(view);
                }
            });
            return new PageImpl<>(viewList, request, preguntasPage.getTotalElements());
        } catch(IllegalArgumentException ex) {
            CatPreguntasException exception = new CatPreguntasException("No fue posible buscar las preguntas", CatPreguntasException.LAYER_DAO, CatPreguntasException.ACTION_SELECT);
            exception.addError("Error al buscar las preguntas - " + ex);
            logger.error("Parametros no correctos - {}", ex.toString());
            throw exception;
        } catch(Exception ex) {
            CatPreguntasException exception = new CatPreguntasException("Error inesperado al buscar las preguntas", CatPreguntasException.LAYER_DAO, CatPreguntasException.ACTION_SELECT);
            exception.addError("Error al buscar las preguntas - " + ex);
            logger.error("Error al buscar las preguntas - CODE {} - {}", exception.getExceptionCode(), ex);
            throw exception;
        }
    }

}
