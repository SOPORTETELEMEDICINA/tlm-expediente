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

    @Query(value ="SELECT  npa.*  FROM  salud_niveles_presion_arterial npa "
            + "WHERE npa.pac_id_fk =':pacidfk' ",nativeQuery=true)
    List<SaludNivPreart> findBypapac(@NotNull @Param("pacidfk") String pacidfk)throws Exception;


    @Query(value ="SELECT  npa.*  FROM  salud_niveles_presion_arterial npa "
            + "WHERE npa.pac_id_fk =':pacidfk' and npa.paperiodo  between :periodo and :periodo2 and npa.pa_fecha_hora between ':fechaInicio' and ':fechaFin' ",nativeQuery=true)
    List<SaludNivPreart> findByfecha(@NotNull @Param("pacidfk") String pacidfk, @NotNull @Param ("periodo") int periodo, @NotNull @Param ("periodo2") int periodo2, @NotNull @Param ("fechaInicio") Timestamp  fechaInicio, @NotNull @Param ("fechaFin") Timestamp fechaFin)throws Exception;



    SaludNivPreart findByPacidfk( String pacidfk) throws Exception;

}
