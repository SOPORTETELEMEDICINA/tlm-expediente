package net.amentum.niomedic.expediente.persistence.historia_clinica.personales_no_patologicos;

import net.amentum.niomedic.expediente.model.historia_clinica.personales_no_patologicos.Vivienda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetalleViviendaRepository extends JpaRepository<Vivienda, Long>, JpaSpecificationExecutor {
    Vivienda findByIdHistoriaClinica(Long idHistoriaClinica);

    List<Vivienda> findAllByIdHistoriaClinica(Long idHistoriaClinica);
}
