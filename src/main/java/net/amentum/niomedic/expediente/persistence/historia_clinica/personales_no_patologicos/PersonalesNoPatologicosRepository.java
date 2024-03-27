package net.amentum.niomedic.expediente.persistence.historia_clinica.personales_no_patologicos;

import net.amentum.niomedic.expediente.model.historia_clinica.personales_no_patologicos.PersonalesNoPatologicos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonalesNoPatologicosRepository extends JpaRepository<PersonalesNoPatologicos, Long>, JpaSpecificationExecutor {
    PersonalesNoPatologicos findByIdHistoriaClinica(Long idHistoriaClinica);

    List<PersonalesNoPatologicos> findAllByIdHistoriaClinica(Long idHistoriaClinica);
}
