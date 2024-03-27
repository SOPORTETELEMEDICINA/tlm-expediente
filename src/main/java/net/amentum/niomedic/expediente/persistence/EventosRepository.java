package net.amentum.niomedic.expediente.persistence;

import net.amentum.niomedic.expediente.model.Eventos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Repository
public interface EventosRepository extends JpaRepository<Eventos, Long>, JpaSpecificationExecutor<Eventos> {
   Eventos findByIdUsuarioCreaAndInicio(@NotNull Long idUsuario, @NotNull Date inicio) throws Exception;

   @Query(value = "select e.* from eventos e where e.id_usuario_crea=:idUsuario and e.inicio=:inicio and e.tipo_evento_id= :tipo", nativeQuery = true)
   List<Eventos> buscarPorIdUsuarioCreaAndInicio(@NotNull @Param("idUsuario") Long idUsuario,
                                             @NotNull @Param("inicio") Date inicio,
                                             @NotNull @Param("tipo") Integer tipo) throws Exception;

   List<Eventos> findAllByInicioBetweenAndIdUsuarioCrea(@NotNull Date ini, @NotNull Date fin, @NotNull Long idUsuario) throws Exception;

//   @Query(value = "select e.* from eventos e where e.id_usuario= :idUsuario and e.tipo_evento_id= :tipo and inicio <= :nuevaFecha and fin >= :nuevaFecha", nativeQuery = true)
   @Query(value = "select e.* from eventos e where e.id_usuario_crea= :idUsuario and e.tipo_evento_id= :tipo and inicio < :nuevaFecha and fin > :nuevaFecha", nativeQuery = true)
   List<Eventos> buscarPorIdUsuarioCreaAndNuevaFechaEntreInicioFin(@NotNull @Param("idUsuario") Long idUsuario,
                                                               @NotNull @Param("nuevaFecha") Date nuevaFecha,
                                                               @NotNull @Param("tipo") Integer tipo) throws Exception;

   Eventos findByInicio(@NotNull Date ini) throws Exception;

   List<Eventos> findByIdConsulta(@NotNull Long idConsulta) throws Exception;

   List<Eventos> findByIdConsultaAndIdEventosIsNot(@NotNull Long idConsulta, @NotNull Long idEventos) throws Exception;

   @Query(value = "SELECT nextval('eventos_id_eventos_seq')", nativeQuery = true)
   Long nextValueSeq();
}
