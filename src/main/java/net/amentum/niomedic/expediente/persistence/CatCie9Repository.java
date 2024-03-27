package net.amentum.niomedic.expediente.persistence;

import net.amentum.niomedic.expediente.model.CatCie9;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CatCie9Repository extends JpaRepository<CatCie9, Long>, JpaSpecificationExecutor {
}
