package net.amentum.niomedic.expediente.persistence;

import net.amentum.niomedic.expediente.model.AlertaNotificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertaNotificacionRepository extends JpaRepository<AlertaNotificacion, Long> {

    // A) TODAS las activas por MÉDICO (sin límite)
    List<AlertaNotificacion> findByIdMedicoAndEstatusOrderByFechaCreacionDesc(String idMedico, String estatus);

    long countByIdMedicoAndEstatus(String idMedico, String estatus);

    // B) NUEVO: TODAS las activas por GRUPO
    List<AlertaNotificacion> findByIdGroupAndEstatusOrderByFechaCreacionDesc(Integer idGroup, String estatus);

    long countByIdGroupAndEstatus(Integer idGroup, String estatus);
}