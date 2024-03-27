package net.amentum.niomedic.expediente.persistence;

import net.amentum.niomedic.expediente.model.Asuetos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AsuetosRepository extends JpaRepository<Asuetos, Long>, JpaSpecificationExecutor {

}
