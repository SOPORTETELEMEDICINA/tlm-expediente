package net.amentum.niomedic.expediente.persistence.referencia_vs_referencia;

import net.amentum.niomedic.expediente.model.referecia_vs_referencia.ContraReferencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ContraReferenciaRepository extends JpaRepository<ContraReferencia, Long>, JpaSpecificationExecutor<ContraReferencia> {
}
