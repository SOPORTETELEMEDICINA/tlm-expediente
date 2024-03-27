package net.amentum.niomedic.expediente.persistence;

import net.amentum.niomedic.expediente.model.CatCie10;
import net.amentum.niomedic.expediente.model.Padecimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;

import java.util.List;

@Repository
public interface PadecimientoRepository extends JpaRepository<Padecimiento, Long>, JpaSpecificationExecutor {
   Padecimiento findByIdPaciente(@NotNull String idPaciente) throws Exception;

   Padecimiento findByIdPadecimiento(@NotNull Long idPadecimiento) throws Exception;

   Padecimiento findByIdPadecimientoAndIdPaciente(@NotNull Long idPadecimiento, @NotNull String idPaciente) throws Exception;

   Padecimiento findByCatCie10AndIdPaciente(@NotNull CatCie10 catCie10, @NotNull String idPaciente)throws Exception;

   List<Padecimiento> findAllByIdPaciente(@NotNull String idPaciente) throws Exception;
}
