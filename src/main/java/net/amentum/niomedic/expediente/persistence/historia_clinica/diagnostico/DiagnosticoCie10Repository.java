package net.amentum.niomedic.expediente.persistence.historia_clinica.diagnostico;

import net.amentum.niomedic.expediente.model.historia_clinica.diagnostico.DiagnosticoCie10;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DiagnosticoCie10Repository extends JpaRepository<DiagnosticoCie10, Long> {
}
