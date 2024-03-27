package net.amentum.niomedic.expediente.persistence.historia_clinica.vacunacion;

import net.amentum.niomedic.expediente.model.historia_clinica.vacunacion.Vacunacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VacunacionRepository extends JpaRepository<Vacunacion, Long> {
    List<Vacunacion> findByIdHistoriaClinica(Long idHistoriaClinica);
}
