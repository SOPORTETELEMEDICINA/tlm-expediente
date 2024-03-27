package net.amentum.niomedic.expediente.persistence.historia_clinica.personales_patologicos;

import net.amentum.niomedic.expediente.model.historia_clinica.personales_patologicos.PadecimientoPersonalPatologico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PadecimientoPersonalPatologicoRepository extends JpaRepository<PadecimientoPersonalPatologico, Long>, JpaSpecificationExecutor {
    PadecimientoPersonalPatologico findByIdHistoriaClinica(Long idHistoriaClinica);
    PadecimientoPersonalPatologico findById(Long idPadecimiento);
    List<PadecimientoPersonalPatologico> findAllByIdHistoriaClinica(Long idHistoriaClinica);
}
