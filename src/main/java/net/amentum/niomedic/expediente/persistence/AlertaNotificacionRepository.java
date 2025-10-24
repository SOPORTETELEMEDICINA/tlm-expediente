package net.amentum.niomedic.expediente.persistence;

import net.amentum.niomedic.expediente.model.AlertaNotificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AlertaNotificacionRepository extends JpaRepository<AlertaNotificacion, Long> {

    List<AlertaNotificacion> findTop20ByIdMedicoAndEstatusOrderByFechaCreacionDesc(UUID idMedico, String estatus);

    long countByIdMedicoAndEstatus(UUID idMedico, String estatus);
}
