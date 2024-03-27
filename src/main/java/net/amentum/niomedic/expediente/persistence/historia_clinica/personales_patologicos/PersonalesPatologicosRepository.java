package net.amentum.niomedic.expediente.persistence.historia_clinica.personales_patologicos;

import net.amentum.niomedic.expediente.model.historia_clinica.personales_patologicos.PersonalesPatologicos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonalesPatologicosRepository extends JpaRepository<PersonalesPatologicos, Long>, JpaSpecificationExecutor {
    PersonalesPatologicos findByIdHistoriaClinica(Long idHistoriaClinica);

    List<PersonalesPatologicos> findAllByIdHistoriaClinica(Long idHistoriaClinica);
}
