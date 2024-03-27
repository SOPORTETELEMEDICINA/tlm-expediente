package net.amentum.niomedic.expediente.persistence.historia_clinica.exploracion_fisica;

import net.amentum.niomedic.expediente.model.historia_clinica.exploracion_fisica.ExploracionFisica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExploracionFisicaRepository extends JpaRepository<ExploracionFisica, Long> {

    ExploracionFisica findByIdHistoriaClinica(Long idHistoriaClinica);

}
