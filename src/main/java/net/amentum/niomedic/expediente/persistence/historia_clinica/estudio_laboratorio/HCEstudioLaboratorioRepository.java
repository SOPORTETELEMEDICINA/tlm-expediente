package net.amentum.niomedic.expediente.persistence.historia_clinica.estudio_laboratorio;

import net.amentum.niomedic.expediente.model.historia_clinica.estudio_laboratorio.HCEstudioLaboratorio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.List;

@Repository
public interface HCEstudioLaboratorioRepository extends JpaRepository<HCEstudioLaboratorio, Long>, JpaSpecificationExecutor {

    HCEstudioLaboratorio findByidHistoriaClinica(@NotNull Long idHistoriaClinica);

    List<HCEstudioLaboratorio> findAllByidHistoriaClinica(@NotNull Long idHistoriaClinica);
}