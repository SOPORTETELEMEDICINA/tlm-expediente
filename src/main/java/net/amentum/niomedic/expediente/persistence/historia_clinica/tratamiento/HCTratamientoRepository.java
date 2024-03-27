package net.amentum.niomedic.expediente.persistence.historia_clinica.tratamiento;

import net.amentum.niomedic.expediente.model.historia_clinica.tratamiento.HCTratamiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HCTratamientoRepository extends JpaRepository<HCTratamiento, Long> {
    HCTratamiento findByIdHistoriaClinica(Long idHistoriaClinica);
}
