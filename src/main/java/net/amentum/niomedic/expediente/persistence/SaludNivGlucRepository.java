package net.amentum.niomedic.expediente.persistence;

import net.amentum.niomedic.expediente.model.SaludNivGluc;
import net.amentum.niomedic.expediente.model.SaludNivGluc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.List;


@Repository
public interface SaludNivGlucRepository  extends JpaRepository<SaludNivGluc, Long>, JpaSpecificationExecutor {
    @Query(value ="SELECT  ng.*  FROM  salud_niveles_glucosa ng "
            + "WHERE ng.pac_id_fk =':pacidfk' ",nativeQuery=true)
    List<SaludNivGluc> findByglupac(@NotNull @Param("pacidfk") String pacidfk)throws Exception;


    @Query(value ="SELECT  ng.*  FROM  salud_niveles_glucosa ng "
            + "WHERE ng.pac_id_fk =':pacidfk' and gluperiodo  between :periodo and :periodo2 and ng.glu_fecha_hora between ':fechaInicio' and ':fechaFin' ",nativeQuery=true)
    List<SaludNivGluc> findByfecha(@NotNull @Param("pacidfk") String pacidfk, @NotNull @Param ("periodo") int periodo, @NotNull @Param ("periodo2") int periodo2, @NotNull @Param ("fechaInicio") Timestamp fechaInicio, @NotNull @Param ("fechaFin") Timestamp  fechaFin)throws Exception;


    SaludNivGluc findByPacidfk( String pacidfk) throws Exception;


}
