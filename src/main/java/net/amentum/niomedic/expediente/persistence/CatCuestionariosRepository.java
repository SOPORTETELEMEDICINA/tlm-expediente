package net.amentum.niomedic.expediente.persistence;

import net.amentum.niomedic.expediente.model.CatCuestionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CatCuestionariosRepository extends JpaRepository<CatCuestionario, Integer>, JpaSpecificationExecutor {

    CatCuestionario findByIdCuestionario(Integer idCuestionario);

}
