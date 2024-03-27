package net.amentum.niomedic.expediente.persistence;

import net.amentum.niomedic.expediente.model.CuestionarioRespuesta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CuestionarioRespuestaRepository extends JpaRepository<CuestionarioRespuesta, Integer>, JpaSpecificationExecutor {

    @Query(value = "SELECT * FROM cuestionario_respuesta WHERE id_paciente = :idPaciente AND id_cuestionario = :idCuestionario ORDER BY created_date DESC LIMIT 5", nativeQuery = true)
    List<CuestionarioRespuesta> findByIdPacienteAndIdCuestionario(@Param("idPaciente") String idPaciente, @Param("idCuestionario") Integer idCuestionario);

}
