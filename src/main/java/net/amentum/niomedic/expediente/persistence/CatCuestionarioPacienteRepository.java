package net.amentum.niomedic.expediente.persistence;

import net.amentum.niomedic.expediente.model.CatCuestionario;
import net.amentum.niomedic.expediente.model.CatCuestionarioPaciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CatCuestionarioPacienteRepository  extends JpaRepository<CatCuestionarioPaciente, Integer>, JpaSpecificationExecutor {

    CatCuestionarioPaciente findByIdCatCuestionario(Integer idCatCuestionario);

}
