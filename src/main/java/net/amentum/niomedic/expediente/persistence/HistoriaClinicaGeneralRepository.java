package net.amentum.niomedic.expediente.persistence;

import net.amentum.niomedic.expediente.model.HistoriaClinicaGeneral;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Repository
public interface HistoriaClinicaGeneralRepository extends JpaRepository<HistoriaClinicaGeneral,Long>, JpaSpecificationExecutor {
   HistoriaClinicaGeneral findByIdPaciente(@NotNull UUID idPaciente) throws Exception;
}
