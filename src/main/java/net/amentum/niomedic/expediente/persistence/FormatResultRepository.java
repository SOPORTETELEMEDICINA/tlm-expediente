package net.amentum.niomedic.expediente.persistence;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import net.amentum.niomedic.expediente.model.FormatResult;

/**
 * @author  by marellano on 14/06/17.
 */
@Repository
public interface FormatResultRepository extends JpaRepository<FormatResult, Long>, JpaSpecificationExecutor {
}
