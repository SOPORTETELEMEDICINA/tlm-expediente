package net.amentum.niomedic.expediente.persistence.referencia_vs_referencia;

import net.amentum.niomedic.expediente.model.referecia_vs_referencia.Referencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReferenciaRepository extends JpaRepository<Referencia, Long>, JpaSpecificationExecutor<Referencia> {

    @Query(value = "SELECT nextval('folio_frm_ref_seq')", nativeQuery = true)
    Long nextValueRef();

}
