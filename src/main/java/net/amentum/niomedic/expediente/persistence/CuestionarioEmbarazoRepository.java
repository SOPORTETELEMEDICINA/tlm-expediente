package net.amentum.niomedic.expediente.persistence;

import net.amentum.niomedic.expediente.model.CuestionarioEmbarazo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CuestionarioEmbarazoRepository extends JpaRepository<CuestionarioEmbarazo, Long>, JpaSpecificationExecutor {

    List<CuestionarioEmbarazo> findByPacidfk(String pacidfk) throws Exception;

    CuestionarioEmbarazo findByIdCuestionario(Integer idCuestionario) throws Exception;

    /*List<CuestionarioEmbarazo> findByHoraAplicacion() throws Exception;*/
}
