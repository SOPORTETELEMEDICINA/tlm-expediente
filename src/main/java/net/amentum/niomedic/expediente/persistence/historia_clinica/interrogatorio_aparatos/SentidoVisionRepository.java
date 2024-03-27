package net.amentum.niomedic.expediente.persistence.historia_clinica.interrogatorio_aparatos;

import net.amentum.niomedic.expediente.model.historia_clinica.interrogatorio_aparatos.SentidoVision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.List;

@Repository
public interface SentidoVisionRepository extends JpaRepository<SentidoVision, Long> {

    SentidoVision findByIdHistoriaClinica(@NotNull Long idHistoriaClinica);
    List<SentidoVision> findAllByIdHistoriaClinica(@NotNull Long idHistoriaClinica);

}
