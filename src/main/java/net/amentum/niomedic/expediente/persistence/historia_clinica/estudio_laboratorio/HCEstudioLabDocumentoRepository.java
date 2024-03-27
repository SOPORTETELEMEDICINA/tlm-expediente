package net.amentum.niomedic.expediente.persistence.historia_clinica.estudio_laboratorio;

import net.amentum.niomedic.expediente.model.historia_clinica.estudio_laboratorio.HCEstudioLabDocumento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HCEstudioLabDocumentoRepository extends JpaRepository<HCEstudioLabDocumento, Long>, JpaSpecificationExecutor {
    HCEstudioLabDocumento findByIdDocumento(Long idDocumento);
    List<HCEstudioLabDocumento> findByIdHistoriaClinicaAndIdEstudioLaboratorio(Long idHistoriaClinica, Long idEstudioLaboratorio);
    List<HCEstudioLabDocumento> findByIdHistoriaClinica(Long idHistoriaClinica);
}
