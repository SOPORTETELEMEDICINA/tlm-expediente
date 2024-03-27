package net.amentum.niomedic.expediente.persistence;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import net.amentum.niomedic.expediente.model.CatCie9;
import net.amentum.niomedic.expediente.model.Consulta;
import net.amentum.niomedic.expediente.model.Tratamiento;

public interface TratamientoRepository extends JpaRepository <Tratamiento, Long>, JpaSpecificationExecutor{
	
	Tratamiento findByConsultaAndCatCie9(@NotNull Consulta cosnulta,@NotNull CatCie9 CatCie9)throws Exception;
	
	List<Tratamiento> findByConsulta(@NotNull Consulta consulta)throws Exception;
}
