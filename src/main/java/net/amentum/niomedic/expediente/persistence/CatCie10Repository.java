package net.amentum.niomedic.expediente.persistence;

import net.amentum.niomedic.expediente.model.CatCie10;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CatCie10Repository extends JpaRepository<CatCie10, Long>, JpaSpecificationExecutor {
}
