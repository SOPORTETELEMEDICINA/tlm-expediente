package net.amentum.niomedic.expediente.persistence.historia_clinica.personales_no_patologicos;

import net.amentum.niomedic.expediente.model.historia_clinica.personales_no_patologicos.HabitoHigienico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HabitoHigienicoRepository extends JpaRepository<HabitoHigienico, Long>, JpaSpecificationExecutor {
    HabitoHigienico findByIdHistoriaClinica(Long idHistoriaClinica);

    List<HabitoHigienico> findAllByIdHistoriaClinica(Long idHistoriaClinica);
}
