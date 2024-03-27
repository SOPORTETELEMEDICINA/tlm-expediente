package net.amentum.niomedic.expediente.persistence;

import net.amentum.niomedic.expediente.exception.ImagenLaboratorioException;
import net.amentum.niomedic.expediente.model.ImagenLaboratorio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
//import java.util.stream.Stream;

import javax.validation.constraints.NotNull;
import java.util.List;

@Repository
public interface ImagenLaboratorioRepository extends JpaRepository<ImagenLaboratorio, Long>, JpaSpecificationExecutor<ImagenLaboratorio> {
   ImagenLaboratorio findByIdPaciente(@NotNull String idPaciente) throws Exception;

   ImagenLaboratorio findByIdPacienteAndImageName(@NotNull String idPaciente, @NotNull String imageName) throws ImagenLaboratorioException;

   @Query(value = "select il.* from imagen_laboratorio il inner " +
      "join consulta c on il.consulta_id = c.id_consulta " +
      "inner join consulta_padecimiento cp on cp.id_consulta = c.id_consulta " +
      "where cp.id_padecimiento = :idPadecimiento", nativeQuery = true)
   List<ImagenLaboratorio> imagenLaboratorioByIdPadecimiento(@Param("idPadecimiento") Long idPadecimiento);

}
