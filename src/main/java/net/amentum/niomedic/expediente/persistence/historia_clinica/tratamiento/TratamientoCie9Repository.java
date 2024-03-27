package net.amentum.niomedic.expediente.persistence.historia_clinica.tratamiento;

import net.amentum.niomedic.expediente.model.historia_clinica.tratamiento.TratamientoCie9;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TratamientoCie9Repository extends JpaRepository<TratamientoCie9, Long> {
}
