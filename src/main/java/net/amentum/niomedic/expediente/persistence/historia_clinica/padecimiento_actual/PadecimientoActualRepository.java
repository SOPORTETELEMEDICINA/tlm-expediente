package net.amentum.niomedic.expediente.persistence.historia_clinica.padecimiento_actual;

import net.amentum.niomedic.expediente.model.historia_clinica.padecimiento_actual.PadecimientoActual;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PadecimientoActualRepository extends JpaRepository<PadecimientoActual, Long> {
    PadecimientoActual findByIdHistoriaClinica(Long idHistoriaClinica);
}
