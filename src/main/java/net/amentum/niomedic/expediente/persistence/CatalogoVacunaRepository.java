package net.amentum.niomedic.expediente.persistence;

import net.amentum.niomedic.expediente.model.CatalogoVacuna;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CatalogoVacunaRepository extends JpaRepository<CatalogoVacuna, Long>, JpaSpecificationExecutor {
}
