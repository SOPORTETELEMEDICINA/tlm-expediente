package net.amentum.niomedic.expediente.persistence;

import net.amentum.niomedic.expediente.model.EstudioLaboratorio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.math.BigInteger;
import java.util.List;

@Repository
public interface EstudioLaboratorioRepository extends JpaRepository<EstudioLaboratorio, Long>, JpaSpecificationExecutor {
   EstudioLaboratorio findByIdPaciente(@NotNull String idPaciente) throws Exception;

   @Query(value = "select cp.id_consulta from consulta_padecimiento cp where cp.id_padecimiento=:idPadecimiento", nativeQuery = true)
   List<BigInteger> obtenerIdConsultasByIdPadecimientos(@NotNull @Param("idPadecimiento")Long idPadecimiento) throws Exception;
}
