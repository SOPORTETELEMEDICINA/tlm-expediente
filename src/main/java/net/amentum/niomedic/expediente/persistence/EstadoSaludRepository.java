package net.amentum.niomedic.expediente.persistence;

import net.amentum.niomedic.expediente.model.EstadoSalud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Repository
public interface EstadoSaludRepository extends JpaRepository<EstadoSalud, Long>, JpaSpecificationExecutor {

   @Query(value = "select es.* from estado_salud es where es.id_paciente = :idPaciente order by fecha_creacion desc limit 1", nativeQuery = true)
   EstadoSalud getLastEstadoSalud(@NotNull @Param("idPaciente") UUID idPaciente) throws Exception;

}
