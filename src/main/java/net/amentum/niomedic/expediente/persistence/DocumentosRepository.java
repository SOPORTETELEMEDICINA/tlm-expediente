package net.amentum.niomedic.expediente.persistence;

import net.amentum.niomedic.expediente.exception.DocumentosException;
import net.amentum.niomedic.expediente.model.Documentos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Stream;

import javax.validation.constraints.NotNull;

@Repository
public interface DocumentosRepository extends JpaRepository<Documentos, Long>, JpaSpecificationExecutor<Documentos> {
	Documentos findByIdPaciente(@NotNull String idPaciente) throws Exception;

	Documentos findByIdPacienteAndDocumentoName(@NotNull String idPaciente, @NotNull String documentoName) throws DocumentosException;

	@Query(value = "select d.* from documentos d inner "+
		   "join consulta c on d.consulta_id = c.id_consulta "+
		   "inner join consulta_padecimiento cp on cp.id_consulta = c.id_consulta " +
		   "where cp.id_padecimiento = :idPadecimiento", nativeQuery=true)
	List<Documentos> documentosByIdPadecimiento(@Param("idPadecimiento") Long idPadecimiento);

}
