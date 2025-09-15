package net.amentum.niomedic.expediente.persistence;

import net.amentum.niomedic.expediente.model.SaludNivPreart;
import net.amentum.niomedic.expediente.model.SaludNivPreart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.List;


@Repository
public interface SaludNivPreartRepository extends JpaRepository<SaludNivPreart, Long>, JpaSpecificationExecutor {

    @Query(value = "SELECT np.* FROM salud_niveles_presion_arterial np " +
            "WHERE np.pac_id_fk = :pacidfk",
            nativeQuery = true)
    List<SaludNivPreart> findByPapac(@Param("pacidfk") String pacidfk);

    @Query(value = "SELECT np.* FROM salud_niveles_presion_arterial np " +
            "WHERE np.pac_id_fk = :pacidfk " +
            "AND np.paperiodo BETWEEN :periodo AND :periodo2 " +
            "AND np.pa_fecha_hora BETWEEN :fechaInicio AND :fechaFin",
            nativeQuery = true)
    List<SaludNivPreart> findByfecha(@Param("pacidfk") String pacidfk,
                                     @Param("periodo") int periodo,
                                     @Param("periodo2") int periodo2,
                                     @Param("fechaInicio") java.sql.Timestamp fechaInicio,
                                     @Param("fechaFin") java.sql.Timestamp fechaFin);

    SaludNivPreart findByPacidfk( String pacidfk) throws Exception;

}
