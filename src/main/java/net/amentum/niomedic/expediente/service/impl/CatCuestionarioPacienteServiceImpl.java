package net.amentum.niomedic.expediente.service.impl;

import net.amentum.niomedic.expediente.converter.CatCuestionarioPacienteConverter;
import net.amentum.niomedic.expediente.exception.CatCuestionarioPacienteException;
import net.amentum.niomedic.expediente.model.CatCuestionarioPaciente;
import net.amentum.niomedic.expediente.persistence.CatCuestionarioPacienteRepository;
import net.amentum.niomedic.expediente.service.CatCuestionarioPacienteService;
import net.amentum.niomedic.expediente.views.CatCuestionarioPacienteView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
class CatCuestionarioPacienteServiceImpl implements CatCuestionarioPacienteService {
    final Logger logger = LoggerFactory.getLogger(CatCuestionariosServiceImpl.class);

    @Autowired
    CatCuestionarioPacienteRepository repository;

    @Autowired
    CatCuestionarioPacienteConverter converter;

    @Transactional(rollbackFor = {CatCuestionarioPacienteException.class})
    @Override
    public void createCuestionarioPaciente(CatCuestionarioPacienteView view) throws CatCuestionarioPacienteException {
        try {
            CatCuestionarioPaciente cuestionario = converter.toEntity(view);
            logger.info("Cuestionario nuevo: {}", cuestionario);
            repository.save(cuestionario);
        } catch(DataIntegrityViolationException ex) {
            CatCuestionarioPacienteException exception = new CatCuestionarioPacienteException("No fue posible agregar un cuestionario nuevo", CatCuestionarioPacienteException.LAYER_DAO, CatCuestionarioPacienteException.ACTION_INSERT );
            exception.addError("Error al insertar un nuevo cuestionario - " + ex);
            logger.error("Error DIVE al insertar un nuevo cuestionario  - {} - CODE {} - {}", view, exception.getExceptionCode(), ex.getMessage());
            throw exception;
        } catch(Exception ex) {
            CatCuestionarioPacienteException exception = new CatCuestionarioPacienteException("Error inesperado al agregar un nuevo cuestionario", CatCuestionarioPacienteException.LAYER_DAO, CatCuestionarioPacienteException.ACTION_INSERT );
            exception.addError("Error al insertar un nuevo cuestionario - " + ex);
            logger.error("Error al insertar un nuevo cuestionario - {} - CODE {} - {}", view, exception.getExceptionCode(), ex.getMessage());
            throw exception;
        }
    }
}
