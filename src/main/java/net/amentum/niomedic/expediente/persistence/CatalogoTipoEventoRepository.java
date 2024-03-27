package net.amentum.niomedic.expediente.persistence;

import net.amentum.niomedic.expediente.model.CatalogoTipoEvento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;

@Repository
public interface CatalogoTipoEventoRepository extends JpaRepository<CatalogoTipoEvento, Integer>, JpaSpecificationExecutor {

   CatalogoTipoEvento findByDescripcion(@NotNull String descripcion);

}
