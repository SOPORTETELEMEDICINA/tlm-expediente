package net.amentum.niomedic.expediente.persistence;

import net.amentum.niomedic.expediente.model.UserSignature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import javax.validation.constraints.NotNull;

public interface UserSignatureRepository extends JpaRepository<UserSignature, Long>, JpaSpecificationExecutor {
    UserSignature findByUserAppId(@NotNull Long userAppId);
}
