package net.amentum.niomedic.expediente.persistence.historia_clinica.personales_no_patologicos;

import net.amentum.niomedic.expediente.model.historia_clinica.personales_no_patologicos.HabitoAlimenticio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HabitoAlimenticioRepository extends JpaRepository<HabitoAlimenticio, Long>, JpaSpecificationExecutor {
    HabitoAlimenticio findByIdHistoriaClinica(Long idHistoriaClinica);

    List<HabitoAlimenticio> findAllByIdHistoriaClinica(Long idHistoriaClinica);
}
