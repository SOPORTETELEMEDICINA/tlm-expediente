package net.amentum.niomedic.expediente.persistence.historia_clinica.antecendentes_ginecobstetricos;

import net.amentum.niomedic.expediente.model.historia_clinica.antecendentes_ginecobstetricos.AntecendentesGinecobstetricos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AntecendentesGinecobstetricosRepository extends JpaRepository<AntecendentesGinecobstetricos, Long> {
    AntecendentesGinecobstetricos findByIdHistoriaClinica(Long idHistoriaClinica);
}
