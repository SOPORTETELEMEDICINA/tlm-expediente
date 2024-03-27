package net.amentum.niomedic.expediente.persistence.historia_clinica.heredo_familiares;

import net.amentum.niomedic.expediente.model.historia_clinica.heredo_familiares.HeredoFamiliares;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HeredoFamiliaresRepository extends JpaRepository<HeredoFamiliares, Long> {
    HeredoFamiliares findByIdHistoriaClinica(Long idHistoriaClinica);
}
