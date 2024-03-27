package net.amentum.niomedic.expediente.persistence;

import java.util.UUID;

import javax.validation.constraints.NotNull;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import net.amentum.niomedic.expediente.model.Controles;


public interface ControlesRepository extends JpaRepository<Controles, Long>, JpaSpecificationExecutor{
	Controles findByidPaciente(@NotNull UUID idPaciente)throws Exception;
}
