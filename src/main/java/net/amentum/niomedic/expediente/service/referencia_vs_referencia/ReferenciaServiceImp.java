package net.amentum.niomedic.expediente.service.referencia_vs_referencia;

import lombok.extern.slf4j.Slf4j;
import net.amentum.niomedic.expediente.configuration.ApiConfiguration;
import net.amentum.niomedic.expediente.converter.referencia_vs_referencia.ReferenciaConverter;
import net.amentum.niomedic.expediente.exception.ReferenciaException;
import net.amentum.niomedic.expediente.model.referecia_vs_referencia.Referencia;
import net.amentum.niomedic.expediente.persistence.referencia_vs_referencia.ReferenciaRepository;
import net.amentum.niomedic.expediente.views.referencia_vs_referencia.ReferenciaView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Service
@Transactional
public class ReferenciaServiceImp implements ReferenciaService {

    final String colOrder = "fechaCreacion";

    @Autowired
    ApiConfiguration apiConfiguration;

    @Autowired
    ReferenciaConverter converter;

    @Autowired
    ReferenciaRepository repository;

    @Override
    @Transactional(rollbackFor = ReferenciaException.class)
    public Map<String, Object> createReferencia(ReferenciaView view) throws ReferenciaException {
        try {
            Referencia entity = converter.toEntity(view);
            entity.setFolio(String.format("REF-%s%s",
                    new SimpleDateFormat("yyMMdd").format(new Date()),
                    repository.nextValueRef()));
            repository.save(entity);
            Map<String, Object> map = new HashMap<>();
            map.put("folio", entity.getFolio());
            Map<String, Object> paciente = apiConfiguration.getPacieteByid(entity.getIdPaciente());
            map.put("nombre", String.format("%s %s %s",
                    paciente.get("nombre").toString().trim(),
                    paciente.get("apellidoPaterno").toString().trim(),
                    paciente.get("apellidoMaterno").toString().trim()));
            return map;
        } catch(Exception ce) {
            ReferenciaException ref = new ReferenciaException("Ocurrió un error al insertar la Referencia", ReferenciaException.LAYER_SERVICE, ReferenciaException.ACTION_INSERT);
            ref.addError(ce.getCause().toString());
            ce.printStackTrace();
            throw ref;
        }
    }

    @Override
    public Page<ReferenciaView> getReferencia(Integer column, Integer type, String value,Integer page, Integer size) throws ReferenciaException {
        try {
            log.debug("Obtener listado de referencias por columna: {} - valor: {} - tipo: {} - page: {} - size: {}", column, value, type, page, size);
            Sort sort = new Sort(Sort.Direction.DESC, colOrder);
            PageRequest request = new PageRequest(page, size, sort);
            Specifications<Referencia> specifications = Specifications.where((referencia, query, cb) -> {
                Predicate tc = null;
                switch (column) {
                    case 1:
                        tc = cb.equal(referencia.get("idPaciente"), value);
                        break;
                    case 2:
                        tc = cb.equal(referencia.get("regionSanitaria"), value);
                        break;
                    case 3:
                        tc = cb.equal(referencia.get("unidadMedica"), value);
                        break;
                    case 4:
                        tc = cb.equal(referencia.get("idMedicoRecibe"), value);
                        break;
                    case 5:
                        tc = cb.equal(referencia.get("idMedicoCrea"), value);
                        break;
                }
                tc = cb.and(tc, cb.equal(referencia.get("tipo"), type));
                return tc;
            });
            List<ReferenciaView> referenciaViewList = new ArrayList<>();
            Page<Referencia> referenciaPage = repository.findAll(specifications, request);
            referenciaPage.getContent().forEach(referencia -> referenciaViewList.add(converter.toView(referencia)));
            return new PageImpl<>(referenciaViewList, request, referenciaPage.getTotalElements());
        } catch(Exception ce) {
            ReferenciaException ref = new ReferenciaException("Ocurrió un error al buscar referencias", ReferenciaException.LAYER_SERVICE, ReferenciaException.ACTION_SELECT);
            ref.addError(ce.toString());
            ce.printStackTrace();
            throw ref;
        }
    }
}
