package net.amentum.niomedic.expediente.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import net.amentum.niomedic.expediente.model.ResultadoEstudio;

@Repository
public interface ResultadoEstudioRepository extends JpaRepository<ResultadoEstudio,Long>, JpaSpecificationExecutor<ResultadoEstudio>{
	
	List<ResultadoEstudio> findByConsultaIdConsulta(Long idConsulta);
	
}
