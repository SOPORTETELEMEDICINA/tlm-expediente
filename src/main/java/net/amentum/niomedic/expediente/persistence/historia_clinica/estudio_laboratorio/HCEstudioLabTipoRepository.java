package net.amentum.niomedic.expediente.persistence.historia_clinica.estudio_laboratorio;

import net.amentum.niomedic.expediente.model.historia_clinica.estudio_laboratorio.HCEstudioLabTipo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HCEstudioLabTipoRepository extends JpaRepository<HCEstudioLabTipo, Long>, JpaSpecificationExecutor {
    HCEstudioLabTipo findByidTipoEstudio(Long idTipoEstudio);
    List<HCEstudioLabTipo> findByIdHistoriaClinicaAndIdEstudioLaboratorio(Long idHistoriaClinica, Long idEstudioLaboratorio);
    List<HCEstudioLabTipo> findByIdHistoriaClinica(Long idHistoriaClinica);
}
