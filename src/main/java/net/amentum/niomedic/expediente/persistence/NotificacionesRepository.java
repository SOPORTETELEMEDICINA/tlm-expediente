package net.amentum.niomedic.expediente.persistence;

import net.amentum.niomedic.expediente.model.Notificaciones;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.List;

@Repository
public interface NotificacionesRepository extends JpaRepository<Notificaciones, Long>, JpaSpecificationExecutor {

   List<Notificaciones> findByIdUsuarioAndEstatus(@NotNull Long idUsuario, @NotNull Integer estatus) throws Exception;
}
