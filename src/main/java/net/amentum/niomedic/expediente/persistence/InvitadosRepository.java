package net.amentum.niomedic.expediente.persistence;

import net.amentum.niomedic.expediente.model.Invitados;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface InvitadosRepository extends JpaRepository<Invitados, Long>, JpaSpecificationExecutor {
}
