package net.amentum.niomedic.expediente.persistence;

import java.sql.Timestamp;
import java.util.List;

import javax.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import net.amentum.niomedic.expediente.model.SaludNivPeso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;



@Repository
public interface SaludNivPesoRepository extends JpaRepository<SaludNivPeso, Long>, JpaSpecificationExecutor {

    @Query(value ="SELECT np.* FROM salud_niveles_peso np " +
            "WHERE np.pac_id_fk = :pacidfk",
            nativeQuery = true)
    List<SaludNivPeso> findByPesopac(@NotNull @Param("pacidfk") String pacidfk) throws Exception;

    @Query(value ="SELECT np.* FROM salud_niveles_peso np " +
            "WHERE np.pac_id_fk = :pacidfk " +
            "AND np.pesoperiodo BETWEEN :periodo AND :periodo2 " +
            "AND np.peso_fecha_hora BETWEEN :fechaInicio AND :fechaFin",
            nativeQuery = true)
    List<SaludNivPeso> findByfecha(@NotNull @Param("pacidfk") String pacidfk,
                                   @NotNull @Param("periodo") int periodo,
                                   @NotNull @Param("periodo2") int periodo2,
                                   @NotNull @Param("fechaInicio") Timestamp fechaInicio,
                                   @NotNull @Param("fechaFin") Timestamp fechaFin) throws Exception;

    SaludNivPeso findByPacidfk( String pacidfk) throws Exception;

}
