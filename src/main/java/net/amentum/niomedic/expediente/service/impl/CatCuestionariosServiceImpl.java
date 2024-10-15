package net.amentum.niomedic.expediente.service.impl;

import net.amentum.niomedic.expediente.converter.CatCuestionariosConverter;
import net.amentum.niomedic.expediente.exception.CatCuestionariosException;
import net.amentum.niomedic.expediente.model.CatCuestionario;
import net.amentum.niomedic.expediente.persistence.CatCuestionariosRepository;
import net.amentum.niomedic.expediente.service.CatCuestionariosService;
import net.amentum.niomedic.expediente.views.CatCuestionarioView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Transactional(readOnly = true)
public class CatCuestionariosServiceImpl implements CatCuestionariosService {

    final Logger logger = LoggerFactory.getLogger(CatCuestionariosServiceImpl.class);

    @Autowired
    CatCuestionariosRepository repository;

    @Autowired
    CatCuestionariosConverter converter;

    @Transactional(rollbackFor = {CatCuestionariosException.class})
    @Override
    public void createCuestionario(CatCuestionarioView view) throws CatCuestionariosException {
        try {
            CatCuestionario cuestionario = converter.toEntity(view);
            logger.info("Cuestionario nuevo: {}", cuestionario);
            repository.save(cuestionario);
        } catch(DataIntegrityViolationException ex) {
            CatCuestionariosException exception = new CatCuestionariosException("No fue posible agregar un cuestionario nuevo", CatCuestionariosException.LAYER_DAO, CatCuestionariosException.ACTION_INSERT );
            exception.addError("Error al insertar un nuevo cuestionario - " + ex);
            logger.error("Error DIVE al insertar un nuevo cuestionario  - {} - CODE {} - {}", view, exception.getExceptionCode(), ex.getMessage());
            throw exception;
        } catch(Exception ex) {
            CatCuestionariosException exception = new CatCuestionariosException("Error inesperado al agregar un nuevo cuestionario", CatCuestionariosException.LAYER_DAO, CatCuestionariosException.ACTION_INSERT );
            exception.addError("Error al insertar un nuevo cuestionario - " + ex);
            logger.error("Error al insertar un nuevo cuestionario - {} - CODE {} - {}", view, exception.getExceptionCode(), ex.getMessage());
            throw exception;
        }
    }

    @Transactional(rollbackFor = {CatCuestionariosException.class})
    @Override
    public void updateCuestionario(Integer idCuestionario, CatCuestionarioView view) throws CatCuestionariosException {
        try {
            CatCuestionario cuestionario = repository.findByIdCuestionario(idCuestionario);
            if(cuestionario == null) {
                CatCuestionariosException exception = new CatCuestionariosException("Error inesperado al modificar un cuestionario", CatCuestionariosException.LAYER_DAO, CatCuestionariosException.ACTION_UPDATE );
                exception.addError("Error al modificar un cuestionario, no se encuentra registrado");
                logger.error("Error al modificar un cuestionario - {} - CODE {}", view, exception.getExceptionCode());
                throw exception;
            }
            cuestionario.setNombre(view.getNombre());
            cuestionario.setActive(view.getActive());
            cuestionario.setSort(view.getSort());
            logger.info("Cuestionario modificado con el id: {} - contenido: {}", idCuestionario, cuestionario);
            repository.save(cuestionario);
        } catch(DataIntegrityViolationException ex) {
            CatCuestionariosException exception = new CatCuestionariosException("No fue posible modificar un cuestionario ", CatCuestionariosException.LAYER_DAO, CatCuestionariosException.ACTION_UPDATE );
            exception.addError("Error al modificar un cuestionario - " + ex);
            logger.error("Error DIVE al modificar un cuestionario - {} - CODE {} - {}", view, exception.getExceptionCode(), ex.getMessage());
            throw exception;
        } catch(Exception ex) {
            CatCuestionariosException exception = new CatCuestionariosException("Error inesperado al agregar un nuevo cuestionario", CatCuestionariosException.LAYER_DAO, CatCuestionariosException.ACTION_INSERT );
            exception.addError("Error al modificar un cuestionario - " + ex);
            logger.error("Error al modificar un cuestionario - {} - CODE {} - {}", view, exception.getExceptionCode(), ex.getMessage());
            throw exception;
        }
    }

    @Override
    public CatCuestionarioView getCuestionario(Integer idCuestionario) throws CatCuestionariosException {
        try {
            CatCuestionario cuestionario = repository.findByIdCuestionario(idCuestionario);
            if(cuestionario == null) {
                CatCuestionariosException exception = new CatCuestionariosException("Error inesperado al buscar un cuestionario", CatCuestionariosException.LAYER_DAO, CatCuestionariosException.ACTION_SELECT );
                exception.addError("Error al buscar un cuestionario, no se encuentra registrado");
                logger.error("Error al buscar un cuestionario - {} - CODE {}", idCuestionario, exception.getExceptionCode());
                throw exception;
            }
            return converter.toView(cuestionario);
        } catch(Exception ex) {
            CatCuestionariosException exception = new CatCuestionariosException("Error inesperado al buscar un cuestionario", CatCuestionariosException.LAYER_DAO, CatCuestionariosException.ACTION_SELECT );
            exception.addError("Error al buscar un cuestionario - " + ex);
            logger.error("Error al buscar un cuestionario - {} - CODE {} - {}", idCuestionario, exception.getExceptionCode(), ex.getMessage());
            throw exception;
        }
    }

    @Override
    public Page<CatCuestionarioView> getCuestionarioSearch(Integer page, Integer size, String orderColumn, String orderType, String idPaciente, Integer idStatus) throws CatCuestionariosException {
        logger.info("search: - idPaciente: {}- idStatus: {} - page: {} - size: {} - orderColumn: {} - orderType: {}", idPaciente, idStatus, page, size,orderColumn, orderType);
        Sort sort = null;
        if(orderType.equalsIgnoreCase("DESC"))
            sort = new Sort(Sort.Direction.DESC, orderColumn);
        else
            sort = new Sort(Sort.Direction.ASC, orderColumn);
        PageRequest request = new PageRequest(page, size, sort);
        List<CatCuestionarioView> viewList = new ArrayList<>();
logger.info("Vamos");
        if (StringUtils.hasText(idPaciente) && idStatus != 0) {
            logger.info("1");
            List<CatCuestionario> catCuestionarioList = repository.findByIdPacienteAndStatus(idPaciente, idStatus);
            convertToViewList(catCuestionarioList, viewList);
        }
        else if (StringUtils.hasText(idPaciente) && idStatus == 0) {
            logger.info("2");

            List<CatCuestionario> catCuestionarioList = repository.findByIdPacienteAndStatusIn(idPaciente);
            convertToViewList(catCuestionarioList, viewList);
        }
        else if (!StringUtils.hasText(idPaciente) && idStatus != 0) {
            logger.info("3");

            List<CatCuestionario> catCuestionarioList = repository.findByStatus(idStatus);
            convertToViewList(catCuestionarioList, viewList);
        }
        else {
            Page<CatCuestionario> cuestionarioPage = repository.findAll(request);
            cuestionarioPage.forEach(catCuestionario -> viewList.add(converter.toView(catCuestionario)));
        }

        return new PageImpl<>(viewList, request, viewList.size());
    }

    private void convertToViewList(List<CatCuestionario> catCuestionarioList, List<CatCuestionarioView> viewList) {
        for (CatCuestionario temp : catCuestionarioList) {
            viewList.add(converter.toView(temp));
        }
    }
}
