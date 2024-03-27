package net.amentum.niomedic.expediente.service.impl;

import net.amentum.niomedic.expediente.converter.CuestionarioEmbarazoConverter;
import net.amentum.niomedic.expediente.exception.CuestionarioEmbarazoException;
import net.amentum.niomedic.expediente.model.CuestionarioEmbarazo;
import net.amentum.niomedic.expediente.persistence.CuestionarioEmbarazoRepository;
import net.amentum.niomedic.expediente.service.CuestionarioEmbarazoService;
import net.amentum.niomedic.expediente.views.CuestionarioEmbarazoView;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional(readOnly = true)
public class CuestionarioEmbarazoServiceImpl implements CuestionarioEmbarazoService {

    final Logger logger = LoggerFactory.getLogger(CuestionarioEmbarazoServiceImpl.class);
    final Map<String, Object> columnOrderNames = new HashMap<>();
    CuestionarioEmbarazoRepository repository;
    CuestionarioEmbarazoConverter converter;

    {
        columnOrderNames.put("id", "idCuestionario");
        columnOrderNames.put("med", "medidfk");
        columnOrderNames.put("pac", "pacidfk");
        columnOrderNames.put("fecha", "horaAplicacion");
    }

    @Autowired
    public void setRepository(CuestionarioEmbarazoRepository repository) {
        this.repository = repository;
    }

    @Autowired
    public void setConverter(CuestionarioEmbarazoConverter converter) {
        this.converter = converter;
    }

    @Transactional(readOnly = false, rollbackFor = {CuestionarioEmbarazoException.class})
    @Override
    public void createCuestionarioEmbarazo(CuestionarioEmbarazoView view) throws CuestionarioEmbarazoException {
        try {
            CuestionarioEmbarazo cuestionario = converter.toEntity(view, new CuestionarioEmbarazo());
            logger.info("Registro nuevo - {} ", cuestionario);
            logger.info("Añadiendo nuevo cuestionario - Pac: {} - Med: {} ", cuestionario.getPacidfk(), cuestionario.getMedidfk());
            repository.save(cuestionario);
        } catch(DataIntegrityViolationException ex) {
            CuestionarioEmbarazoException exception = new CuestionarioEmbarazoException("No fue posible agregar un cuestionario nuevo", CuestionarioEmbarazoException.LAYER_DAO, CuestionarioEmbarazoException.ACTION_INSERT );
            exception.addError("Error al insertar un nuevo cuestionario - " + ex.toString());
            logger.error("Error al insertar un nuevo cuestionario - {} - CODE {} - {}", view, exception.getExceptionCode(), ex);
            throw exception;
        } catch(Exception ex) {
            CuestionarioEmbarazoException exception = new CuestionarioEmbarazoException("Error inesperado al agregar un cuestionario nuevo", CuestionarioEmbarazoException.LAYER_DAO, CuestionarioEmbarazoException.ACTION_INSERT );
            exception.addError("Error al insertar un nuevo cuestionario - " + ex.toString());
            logger.error("Error al insertar un nuevo cuestionario - {} - CODE {} - {}", view, exception.getExceptionCode(), ex);
            throw exception;
        }
    }

    @Override
    public void updateCuestionarioEmbarazo(CuestionarioEmbarazoView view) throws CuestionarioEmbarazoException {
        throw new CuestionarioEmbarazoException("Metodo no implementado", CuestionarioEmbarazoException.LAYER_SERVICE, CuestionarioEmbarazoException.ACTION_UPDATE);
    }

    @Override
    public void deleteCuestionarioEmbarazo(CuestionarioEmbarazoView view) throws CuestionarioEmbarazoException {
        throw new CuestionarioEmbarazoException("Metodo no implementado", CuestionarioEmbarazoException.LAYER_SERVICE, CuestionarioEmbarazoException.ACTION_DELETE);
    }

    @Override
    public List<CuestionarioEmbarazoView> findAll() throws CuestionarioEmbarazoException {
        try {
            List<CuestionarioEmbarazo> cuestionariosList = repository.findAll();
            List<CuestionarioEmbarazoView> viewList = new ArrayList<>();
            for(CuestionarioEmbarazo entity: cuestionariosList)
                viewList.add(converter.toView(entity));
            return viewList;
        } catch(Exception ex) {
            CuestionarioEmbarazoException exception = new CuestionarioEmbarazoException("Error al obtener la lista de cuestionarios", CuestionarioEmbarazoException.LAYER_SERVICE, CuestionarioEmbarazoException.ACTION_SELECT );
            exception.addError("Error al obtener la lista de cuestionarios - " + ex.toString());
            logger.error("Error al insertar un nuevo cuestionario - CODE {} - {}", exception.getExceptionCode(), exception);
            throw exception;
        }
    }

    @Override
    public Page<CuestionarioEmbarazoView> getCuestionarioEmbarazoSearch(String pacidfk, Integer page, Integer size, String orderColumn, String orderType) throws CuestionarioEmbarazoException {
        try {
            logger.info("search - pacidfk: {} - page: {} - size: {} - orderColumn: {} - orderType: {}", pacidfk, page, size, orderColumn, orderType);
            if(pacidfk == null ) {
                logger.error("pacidfk vacio/null - {} ", pacidfk);
                CuestionarioEmbarazoException exception = new CuestionarioEmbarazoException("Error al obtener cuestionarios", CuestionarioEmbarazoException.LAYER_DAO, CuestionarioEmbarazoException.ACTION_VALIDATE);
                exception.addError("El campo pacidfk viene vacío/null");
                throw exception;
            }
            List<CuestionarioEmbarazoView> viewList = new ArrayList<>();
            Page<CuestionarioEmbarazo> cuestionarioPage = null;
            Sort sort = new Sort(Sort.Direction.DESC, (String) columnOrderNames.get("id"));
            if(orderColumn != null && orderType != null) {
                if(orderType.equalsIgnoreCase("DESC"))
                    sort = new Sort(Sort.Direction.DESC, (String) columnOrderNames.get(orderColumn));
                else
                    sort = new Sort(Sort.Direction.ASC, (String) columnOrderNames.get(orderColumn));
            }
            PageRequest request = new PageRequest(page, size, sort);
            Specifications<CuestionarioEmbarazo> spec = Specifications.where((root, query, cb) ->  cb.and(cb.equal(root.get("pacidfk"), pacidfk)));
            if(spec == null)
                cuestionarioPage = repository.findAll(request);
            else
                cuestionarioPage = repository.findAll(spec, request);
            cuestionarioPage.forEach(cuestionarioEmbarazo ->  viewList.add(converter.toView(cuestionarioEmbarazo)) );
            logger.info("Respuesta: {}", viewList);
            return new PageImpl<>(viewList, request, cuestionarioPage.getTotalElements());
        } catch(CuestionarioEmbarazoException excep) {
            throw excep;
        } catch(IllegalArgumentException excep) {
            logger.error("Parametros no correctos - " + excep);
            CuestionarioEmbarazoException cuestionarioExcep = new CuestionarioEmbarazoException("Algún parámetro no es correcto", CuestionarioEmbarazoException.LAYER_SERVICE, CuestionarioEmbarazoException.ACTION_VALIDATE);
            cuestionarioExcep.addError("Puede que el valor sea null, vacio o valor incorrecto");
            throw cuestionarioExcep;
        } catch(Exception ex) {
            CuestionarioEmbarazoException exception = new CuestionarioEmbarazoException("Ocurrió un error al seleccionar la lista paginable", CuestionarioEmbarazoException.LAYER_SERVICE, CuestionarioEmbarazoException.ACTION_SELECT );
            exception.addError("Error al obtener la lista de cuestionarios - " + ex.toString());
            logger.error("Error al obtener cuestionarios - {}", ex.toString());
            throw exception;
        }
    }

    @Override
    public Page<CuestionarioEmbarazoView> getCuestionarioEmbarazoFechaSearch(String pacidfk, String fechaInicio, String fechaFin, Integer page, Integer size, String orderColumn, String orderType) throws CuestionarioEmbarazoException {
        try {
            logger.info("search - pacidfk: {} - fechaInicio: {} - fechaFin: {} - page: {} - size: {} - orderColumn: {} - orderType: {}", pacidfk, fechaInicio, fechaFin, page, size, orderColumn, orderType);
            if(pacidfk == null) {
                logger.error("pacidfk vacio/null - {} ", pacidfk);
                CuestionarioEmbarazoException exception = new CuestionarioEmbarazoException("Error al obtener cuestionarios", CuestionarioEmbarazoException.LAYER_DAO, CuestionarioEmbarazoException.ACTION_VALIDATE);
                exception.addError("El campo pacidfk viene vacío/null");
                throw exception;
            } else if(fechaInicio == null) {
                logger.error("fechaInicio vacio/null - {} ", pacidfk);
                CuestionarioEmbarazoException exception = new CuestionarioEmbarazoException("Error al obtener cuestionarios", CuestionarioEmbarazoException.LAYER_DAO, CuestionarioEmbarazoException.ACTION_VALIDATE);
                exception.addError("El campo fechaInicio viene vacío/null");
                throw exception;
            }else if(fechaFin == null) {
                logger.error("fechaFin vacio/null - {} ", pacidfk);
                CuestionarioEmbarazoException exception = new CuestionarioEmbarazoException("Error al obtener cuestionarios", CuestionarioEmbarazoException.LAYER_DAO, CuestionarioEmbarazoException.ACTION_VALIDATE);
                exception.addError("El campo fechaFin viene vacío/null");
                throw exception;
            }
            List<CuestionarioEmbarazoView> viewList = new ArrayList<>();
            Page<CuestionarioEmbarazo> cuestionarioPage = null;
            Sort sort = new Sort(Sort.Direction.DESC, (String) columnOrderNames.get("id"));
            if(orderColumn != null && orderType != null) {
                if(orderType.equalsIgnoreCase("DESC"))
                    sort = new Sort(Sort.Direction.DESC, (String) columnOrderNames.get(orderColumn));
                else
                    sort = new Sort(Sort.Direction.ASC, (String) columnOrderNames.get(orderColumn));
            }
            PageRequest request = new PageRequest(page, size, sort);
            Specifications<CuestionarioEmbarazo> spec = Specifications.where((root, query, cb) -> {
                Predicate predicate = cb.and(cb.equal(root.get("pacidfk"), pacidfk));
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyy-MM-dd");
                try {
                    Date fechaInicial = dateFormat.parse(fechaInicio);
                    Date fechaFinal = dateFormat.parse(fechaFin);
                    predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get("horaAplicacion"), fechaInicial), cb.lessThanOrEqualTo(root.get("horaAplicacion"), fechaFinal));
                } catch (ParseException e) {
                    logger.error("Error al parsear las fechas - {} ", e.toString());
                }
                return predicate;
            });
            if(spec == null)
                cuestionarioPage = repository.findAll(request);
            else
                cuestionarioPage = repository.findAll(spec, request);
            cuestionarioPage.forEach(cuestionarioEmbarazo ->  viewList.add(converter.toView(cuestionarioEmbarazo)) );
            logger.info("Respuesta: {}", viewList);
            return new PageImpl<>(viewList, request, cuestionarioPage.getTotalElements());
        } catch(CuestionarioEmbarazoException excep) {
            throw excep;
        } catch(IllegalArgumentException excep) {
            logger.error("Parametros no correctos - " + excep);
            CuestionarioEmbarazoException cuestionarioExcep = new CuestionarioEmbarazoException("Algún parámetro no es correcto", CuestionarioEmbarazoException.LAYER_SERVICE, CuestionarioEmbarazoException.ACTION_VALIDATE);
            cuestionarioExcep.addError("Puede que el valor sea null, vacio o valor incorrecto");
            throw cuestionarioExcep;
        } catch(Exception ex) {
            CuestionarioEmbarazoException exception = new CuestionarioEmbarazoException("Ocurrió un error al seleccionar la lista paginable", CuestionarioEmbarazoException.LAYER_SERVICE, CuestionarioEmbarazoException.ACTION_SELECT );
            exception.addError("Error al obtener la lista de cuestionarios - " + ex.toString());
            logger.error("Error al obtener cuestionarios - {}", ex.toString());
            throw exception;
        }
    }
}
