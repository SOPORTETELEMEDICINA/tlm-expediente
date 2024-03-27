package net.amentum.niomedic.expediente.persistence;

import net.amentum.niomedic.expediente.model.Consulta;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Long>, JpaSpecificationExecutor {
	Consulta findByIdPaciente(@NotNull String idPaciente) throws Exception;

	List<Consulta> findAllByPadecimiento_IdPadecimiento(@NotNull Long idPadecimiento) throws Exception;

	Consulta findByNumeroConsulta(@NotNull Long numeroConsulta) throws Exception;

	@Query(value = "select "+
			"c.id_consulta, c.tipo_consulta, c.fecha_consulta, c.resumen, c.nombre_medico "+
			"from consulta_padecimiento cp inner join consulta c on cp.id_consulta = c.id_consulta " + 
			"where cp.id_padecimiento= :idPadecimiento and lower(c.datos_busqueda) like lower(:datos_busqueda) --#pageable\n", 
			countQuery = "select count(consulta) "+
					"from consulta_padecimiento cp inner join consulta c on cp.id_consulta = c.id_consulta " + 
					"where cp.id_padecimiento= :idPadecimiento and lower(c.datos_busqueda) like lower(:datos_busqueda)"
					, nativeQuery=true)
	Page<Object[]> findByIdPacienteAndIdPadecimiento(@NotNull @Param("idPadecimiento") Long idPadecimiento,
			@Param ("datos_busqueda")String datos_busqueda, Pageable pageable) throws Exception;
	
	Consulta findByIdConsulta(@NotNull Long idConsulta)throws Exception;
	
	@Query(value ="select c.*  from consulta c " +
			"where c.id_paciente=:idPaciente " +
			"and c.id_estado_consulta=:idEstadoConsulta " +
			"and c.id_group=:idGroup " +
			"order by c.fecha_consulta desc limit 1",nativeQuery=true)
	Consulta findConsulta(@NotNull @Param ("idPaciente") UUID idPaciente,
						  @NotNull @Param("idEstadoConsulta") Integer idEstadoConsulta,
						  @NotNull @Param("idGroup") Integer idGroup)throws Exception;
	
	@Query(value="select c.* from consulta c where id_paciente=:idPaciente and (id_estado_consulta = 1 or id_estado_consulta = 2 )  and fecha_consulta >= :fechaActual order by c.fecha_consulta asc limit 1",nativeQuery=true)
	Consulta findConsultaSiguiente(@NotNull @Param("idPaciente")UUID idPaciente,@NotNull @Param("fechaActual") Date fechaActual)throws Exception;

	@Query(value ="select count(c.*)  from consulta c where c.id_estado_consulta in (1,2) and c.id_group = :idGroup",nativeQuery=true)
	Integer countEstadoConsultaNueva(@Param("idGroup") Integer idGroup) throws Exception;

	@Query(value ="select count(c.*)  from consulta c where c.id_estado_consulta = 4 and c.id_group = :idGroup",nativeQuery=true)
	Integer countEstadoConsultaOtras(@Param("idGroup") Integer idGroup) throws Exception;

	@Query(value ="select count(c.*)  from consulta c where c.id_estado_consulta in (1,2) and c.id_medico=:idMedico and c.id_group = :idGroup",nativeQuery=true)
	Integer countEstadoConsultaNuevaByMedico(@NotNull @Param("idMedico") UUID idMedico, @Param("idGroup") Integer idGroup) throws Exception;

	@Query(value ="select count(c.*)  from consulta c where c.id_estado_consulta = 4 and c.id_medico=:idMedico and c.id_group = :idGroup",nativeQuery=true)
	Integer countEstadoConsultaOtrasByMedico(@NotNull @Param("idMedico") UUID idMedico, @Param("idGroup") Integer idGroup) throws Exception;

}
