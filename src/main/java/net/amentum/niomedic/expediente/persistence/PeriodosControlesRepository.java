package net.amentum.niomedic.expediente.persistence;

import java.util.List;
import java.util.UUID;

import javax.validation.constraints.NotNull;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import net.amentum.niomedic.expediente.model.Controles;
import net.amentum.niomedic.expediente.model.PeriodosControles;

@Repository
public interface PeriodosControlesRepository extends JpaRepository<PeriodosControles, Long>, JpaSpecificationExecutor{
	List<PeriodosControles> findByControles(@NotNull Controles controles)throws Exception;

	@Query(value ="SELECT pc.*  FROM  periodo_control pc"
			+ " INNER JOIN control c  on pc.id_control = c.id_control "
			+ "WHERE c.id_paciente  = :idPaciente AND pc.dia_semana= :diaSemana ORDER BY pc.horario ASC ",nativeQuery=true)
	List<PeriodosControles> findByIdPaciente(@NotNull @Param ("idPaciente") UUID idPaciente,@NotNull @Param ("diaSemana") Integer diaSemana)throws Exception;

	@Query(value ="SELECT pc.*  FROM  periodo_control pc"
			+ " INNER JOIN control c  on pc.id_control = c.id_control "
			+ "WHERE c.id_paciente  = :idPaciente AND pc.dia_semana= :diaSemana AND pc.id_periodo_control NOT IN :idPeriodoControl ORDER BY pc.horario ASC ",nativeQuery=true)
	List<PeriodosControles> findByIdPacienteAndIdPeriodoList(@NotNull @Param ("idPaciente") UUID idPaciente,@NotNull @Param ("diaSemana") Integer diaSemana, @Param("idPeriodoControl") List<Long>idPeriodoControl)throws Exception;

}
