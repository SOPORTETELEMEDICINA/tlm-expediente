package net.amentum.niomedic.expediente.persistence;

import net.amentum.niomedic.expediente.model.CatalogoInstitucionMedica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CatalogoInstitucionMedicaRepository extends JpaRepository<CatalogoInstitucionMedica, Long>, JpaSpecificationExecutor {
}
