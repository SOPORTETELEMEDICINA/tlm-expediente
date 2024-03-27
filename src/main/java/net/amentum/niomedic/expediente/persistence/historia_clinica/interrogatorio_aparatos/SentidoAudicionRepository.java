package net.amentum.niomedic.expediente.persistence.historia_clinica.interrogatorio_aparatos;

import net.amentum.niomedic.expediente.model.historia_clinica.interrogatorio_aparatos.SentidoAudicion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SentidoAudicionRepository extends JpaRepository<SentidoAudicion, Long> {
}
