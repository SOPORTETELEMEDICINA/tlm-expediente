package net.amentum.niomedic.expediente.persistence;

import net.amentum.niomedic.expediente.model.MedicionesPaciente;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.validation.constraints.NotNull;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicionesPacienteRepository extends JpaRepository<MedicionesPaciente, Long>, JpaSpecificationExecutor {
	

	@Query(value ="SELECT mp.*  FROM  mediciones_paciente mp "
			+ "WHERE mp.id_paciente = :idPaciente and mp.fecha_creacion between :fechaInicio and :fechaFin ",nativeQuery=true)
	List<MedicionesPaciente> findByFechaCreacionAndIdPaciente(@NotNull @Param ("idPaciente") UUID idPaciente,@NotNull @Param ("fechaInicio") Date fechaInicio,@NotNull @Param ("fechaFin") Date fechaFin)throws Exception;

}
