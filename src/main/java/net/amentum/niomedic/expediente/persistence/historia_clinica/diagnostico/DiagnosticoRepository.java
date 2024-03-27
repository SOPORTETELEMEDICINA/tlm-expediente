package net.amentum.niomedic.expediente.persistence.historia_clinica.diagnostico;

import net.amentum.niomedic.expediente.model.historia_clinica.diagnostico.Diagnostico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiagnosticoRepository extends JpaRepository<Diagnostico, Long> {
    Diagnostico findByIdHistoriaClinica(Long idHistoriaClinica);
}
