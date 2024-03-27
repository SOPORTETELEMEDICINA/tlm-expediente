package net.amentum.niomedic.expediente.persistence;

import net.amentum.niomedic.expediente.model.Vacaciones;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.List;

@Repository
public interface VacacionesRepository extends JpaRepository<Vacaciones, Long>, JpaSpecificationExecutor {

   //   List<Vacaciones> findByMedicoId(@NotNull UUID medicoId);
   List<Vacaciones> findByIdUsuario(@NotNull Long idUsuario);
}
