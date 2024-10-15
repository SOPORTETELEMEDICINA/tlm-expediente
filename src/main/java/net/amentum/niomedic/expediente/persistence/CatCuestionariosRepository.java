package net.amentum.niomedic.expediente.persistence;

import net.amentum.niomedic.expediente.model.CatCuestionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CatCuestionariosRepository extends JpaRepository<CatCuestionario, Integer>, JpaSpecificationExecutor {

    CatCuestionario findByIdCuestionario(Integer idCuestionario);

    @Query(value = "SELECT c.* FROM catalogo_cuestionarios c " +
            "INNER JOIN cat_cuestionario_paciente cp ON c.id_cuestionario = cp.id_cat_cuestionario " +
            "WHERE cp.id_paciente = CAST(:idPaciente AS uuid) AND cp.status = :status " +
            "ORDER BY c.id_cuestionario",
            nativeQuery = true)
    List<CatCuestionario> findByIdPacienteAndStatus(@Param("idPaciente") String idPaciente,
                                                    @Param("status") Integer status);

    @Query(value = "SELECT c.* FROM catalogo_cuestionarios c " +
            "INNER JOIN cat_cuestionario_paciente cp ON c.id_cuestionario = cp.id_cat_cuestionario " +
            "WHERE cp.id_paciente = CAST(:idPaciente AS uuid) AND cp.status IN (1, 3, 4) " +
            "ORDER BY c.id_cuestionario",
            nativeQuery = true)
    List<CatCuestionario> findByIdPacienteAndStatusIn(@Param("idPaciente") String idPaciente);

    @Query(value = "SELECT c.* FROM catalogo_cuestionarios c " +
            "INNER JOIN cat_cuestionario_paciente cp ON c.id_cuestionario = cp.id_cat_cuestionario " +
            "WHERE cp.status = :status " +
            "ORDER BY c.id_cuestionario",
            nativeQuery = true)
    List<CatCuestionario> findByStatus(@Param("status") Integer status);

}
