package net.amentum.niomedic.expediente.service.referencia_vs_referencia;

import net.amentum.niomedic.expediente.exception.ReferenciaException;
import net.amentum.niomedic.expediente.views.referencia_vs_referencia.ReferenciaView;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface ReferenciaService {

    Map<String, Object> createReferencia(ReferenciaView view) throws ReferenciaException;

    Page<ReferenciaView> getReferencia(Integer column, Integer type, String value, Integer page, Integer size) throws ReferenciaException;
}
