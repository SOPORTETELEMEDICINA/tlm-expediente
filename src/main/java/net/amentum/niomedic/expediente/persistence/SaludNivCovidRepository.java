package net.amentum.niomedic.expediente.persistence;

import net.amentum.niomedic.expediente.model.SaludNivCovid;
import net.amentum.niomedic.expediente.model.SaludNivCovid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.List;


@Repository
public interface SaludNivCovidRepository extends JpaRepository<SaludNivCovid, Long>, JpaSpecificationExecutor {
    @Query(value ="SELECT  nc.*  FROM  salud_niveles_covid nc "
            + "WHERE nc.pac_id_fk =':pacidfk' ",nativeQuery=true)
    List<SaludNivCovid> findBycovidpac(@NotNull @Param("pacidfk") String pacidfk)throws Exception;


    @Query(value ="SELECT  nc.*  FROM  salud_niveles_covid nc  "
            + "WHERE nc.pac_id_fk =':pacidfk' and nc.covidperiodo  between :periodo and :periodo2 and nc.covid_fecha_hora between ':fechaInicio' and ':fechaFin' ",nativeQuery=true)
    List<SaludNivCovid> findByfecha(@NotNull @Param("pacidfk") String pacidfk, @NotNull @Param ("periodo") int periodo, @NotNull @Param ("periodo2") int periodo2, @NotNull @Param ("fechaInicio") Timestamp fechaInicio, @NotNull @Param ("fechaFin") Timestamp  fechaFin)throws Exception;

    @Query( value ="SELECT  nc.*  FROM  salud_niveles_covid nc  "
            + "WHERE nc.pac_id_fk = ':pacidfk' and  nc.covidperiodo between :periodo and :periodo2 and nc.covid_fecha_hora between date_trunc(':dia', current_date :sentencia) and date_trunc('days', current_date) ",nativeQuery=true)
    List<SaludNivCovid> findByperiodo(@NotNull @Param("pacidfk") String pacidfk, @NotNull @Param ("periodo") int periodo, @NotNull @Param ("periodo2") int periodo2,@NotNull @Param ("dia") String dia,@NotNull @Param ("sentencia") String Sentencia )throws Exception;


    SaludNivCovid findByPacidfk(String pacidfk) throws Exception;


}
