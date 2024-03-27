package net.amentum.niomedic.expediente.service.referencia_vs_referencia;

import lombok.extern.slf4j.Slf4j;
import net.amentum.niomedic.expediente.converter.referencia_vs_referencia.ContraReferenciaConverter;
import net.amentum.niomedic.expediente.exception.ReferenciaException;
import net.amentum.niomedic.expediente.model.referecia_vs_referencia.ContraReferencia;
import net.amentum.niomedic.expediente.persistence.referencia_vs_referencia.ContraReferenciaRepository;
import net.amentum.niomedic.expediente.views.referencia_vs_referencia.ContraReferenciaView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
public class ContraReferenciaServiceImpl implements ContraReferenciaService {

    final String colOrder = "fechaCreacion";

    @Autowired
    ContraReferenciaConverter converter;

    @Autowired
    ContraReferenciaRepository repository;

    @Override
    @Transactional(rollbackFor = ReferenciaException.class)
    public void createContraReferencia(ContraReferenciaView view) throws ReferenciaException {
        try {
            repository.save(converter.toEntity(view));
        } catch(Exception ce) {
            ReferenciaException ref = new ReferenciaException("Ocurrió un error al insertar la contra-referencia", ReferenciaException.LAYER_SERVICE, ReferenciaException.ACTION_INSERT);
            ref.addError(ce.getCause().toString());
            ce.printStackTrace();
            throw ref;
        }
    }

    @Override
    public Page<ContraReferenciaView> getContraReferencia(Integer column, String value, Integer page, Integer size) throws ReferenciaException {
        try {
            log.debug("Obtener listado de contra-referencias por columna: {} - valor: {} - page: {} - size: {}", column, value, page, size);
            Sort sort = new Sort(Sort.Direction.DESC, colOrder);
            PageRequest request = new PageRequest(page, size, sort);
            Specifications<ContraReferencia> specifications = Specifications.where((creferencia, query, cb) -> {
                Predicate tc = null;
                switch (column) {
                    case 1:
                        tc = cb.equal(creferencia.get("idMedicoCref"), value);
                        break;
                    case 2:
                        tc = cb.equal(creferencia.get("unidadMedica"), value);
                        break;
                    case 3:
                        tc = cb.equal(creferencia.get("regionSanitaria"), value);
                        break;
                }
                return tc;
            });
            List<ContraReferenciaView> referenciaViewList = new ArrayList<>();
            Page<ContraReferencia> contraReferenciaPage = repository.findAll(specifications, request);
            contraReferenciaPage.getContent().forEach(contraReferencia -> referenciaViewList.add(converter.toView(contraReferencia)));
            return new PageImpl<>(referenciaViewList, request, contraReferenciaPage.getTotalElements());
        } catch(Exception ce) {
            ReferenciaException ref = new ReferenciaException("Ocurrió un error al buscar contra-referencias", ReferenciaException.LAYER_SERVICE, ReferenciaException.ACTION_SELECT);
            ref.addError(ce.toString());
            ce.printStackTrace();
            throw ref;
        }
    }
}
