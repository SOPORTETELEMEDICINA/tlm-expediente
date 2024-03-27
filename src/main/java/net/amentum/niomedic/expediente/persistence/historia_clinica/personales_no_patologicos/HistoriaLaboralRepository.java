package net.amentum.niomedic.expediente.persistence.historia_clinica.personales_no_patologicos;

import net.amentum.niomedic.expediente.model.historia_clinica.personales_no_patologicos.HistoriaLaboral;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoriaLaboralRepository extends JpaRepository<HistoriaLaboral, Long>, JpaSpecificationExecutor {

    HistoriaLaboral findByIdHistoriaClinica(Long idHistoriaClinica);

    HistoriaLaboral findByIdHistoriaLaboral(Long idHistoriaLaboral);

    List<HistoriaLaboral> findAllByIdHistoriaClinica(Long idHistoriaClinica);

}
