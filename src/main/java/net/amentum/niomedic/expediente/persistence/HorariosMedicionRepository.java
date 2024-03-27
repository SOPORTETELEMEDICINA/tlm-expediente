package net.amentum.niomedic.expediente.persistence;

import net.amentum.niomedic.expediente.model.HorariosMedicion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface HorariosMedicionRepository extends JpaRepository<HorariosMedicion, Long>, JpaSpecificationExecutor {

}
