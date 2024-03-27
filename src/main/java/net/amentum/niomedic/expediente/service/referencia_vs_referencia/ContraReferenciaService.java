package net.amentum.niomedic.expediente.service.referencia_vs_referencia;

import net.amentum.niomedic.expediente.exception.ReferenciaException;
import net.amentum.niomedic.expediente.views.referencia_vs_referencia.ContraReferenciaView;
import org.springframework.data.domain.Page;

public interface ContraReferenciaService {

    void createContraReferencia(ContraReferenciaView view) throws ReferenciaException;

    Page<ContraReferenciaView> getContraReferencia(Integer column, String value, Integer page, Integer size) throws ReferenciaException;
}
