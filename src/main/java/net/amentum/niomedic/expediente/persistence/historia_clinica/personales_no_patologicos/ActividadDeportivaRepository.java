package net.amentum.niomedic.expediente.persistence.historia_clinica.personales_no_patologicos;

import net.amentum.niomedic.expediente.model.historia_clinica.personales_no_patologicos.ActividadDeportiva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActividadDeportivaRepository extends JpaRepository<ActividadDeportiva, Long>, JpaSpecificationExecutor {

    ActividadDeportiva findByIdHistoriaClinica(Long idHistoriaClinica);

    List<ActividadDeportiva> findAllByIdHistoriaClinica(Long idHistoriaClinica);
}
