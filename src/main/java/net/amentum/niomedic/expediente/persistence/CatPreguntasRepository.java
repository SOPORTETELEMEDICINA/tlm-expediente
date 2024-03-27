package net.amentum.niomedic.expediente.persistence;

import net.amentum.niomedic.expediente.model.CatPregunta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CatPreguntasRepository extends JpaRepository<CatPregunta, Integer>, JpaSpecificationExecutor {

    CatPregunta findByIdPregunta(Integer idPregunta);

    @Query(value = "SELECT DISTINCT id_cuestionario FROM catalogo_preguntas ORDER BY id_cuestionario ASC", nativeQuery = true)
    List<Integer> findUniqueIdCuestionario();

    @Query(value = "SELECT * FROM catalogo_preguntas WHERE id_cuestionario = :idCuestionario AND active = true " +
            "ORDER BY id_cuestionario, sort ASC", nativeQuery = true)
    List<CatPregunta> findByIdCuestionario(@Param("idCuestionario") Integer idCuestionario);
}
