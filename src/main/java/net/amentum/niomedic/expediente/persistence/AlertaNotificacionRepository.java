package net.amentum.niomedic.expediente.persistence;

import net.amentum.niomedic.expediente.model.AlertaNotificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertaNotificacionRepository extends JpaRepository<AlertaNotificacion, Long> {

    List<AlertaNotificacion> findTop20ByIdMedicoAndEstatusOrderByFechaCreacionDesc(String idMedico, String estatus);

    long countByIdMedicoAndEstatus(String idMedico, String estatus);
}