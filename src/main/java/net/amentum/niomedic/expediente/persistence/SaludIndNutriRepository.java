package net.amentum.niomedic.expediente.persistence;

import net.amentum.niomedic.expediente.model.SaludIndNutri;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface SaludIndNutriRepository extends JpaRepository<SaludIndNutri, Long>, JpaSpecificationExecutor {

    SaludIndNutri findByPacidfk( String pacidfk) throws Exception;

    List<SaludIndNutri> findByNutri1luAndPacidfk(Boolean param, String pacidfk);
    List<SaludIndNutri> findByNutri2luAndPacidfk(Boolean param, String pacidfk);
    List<SaludIndNutri> findByNutri3luAndPacidfk(Boolean param, String pacidfk);

    List<SaludIndNutri> findByNutri1maAndPacidfk(Boolean param, String pacidfk);
    List<SaludIndNutri> findByNutri2maAndPacidfk(Boolean param, String pacidfk);
    List<SaludIndNutri> findByNutri3maAndPacidfk(Boolean param, String pacidfk);

    List<SaludIndNutri> findByNutri1miAndPacidfk(Boolean param, String pacidfk);
    List<SaludIndNutri> findByNutri2miAndPacidfk(Boolean param, String pacidfk);
    List<SaludIndNutri> findByNutri3miAndPacidfk(Boolean param, String pacidfk);

    List<SaludIndNutri> findByNutri1juAndPacidfk(Boolean param, String pacidfk);
    List<SaludIndNutri> findByNutri2juAndPacidfk(Boolean param, String pacidfk);
    List<SaludIndNutri> findByNutri3juAndPacidfk(Boolean param, String pacidfk);

    List<SaludIndNutri> findByNutri1viAndPacidfk(Boolean param, String pacidfk);
    List<SaludIndNutri> findByNutri2viAndPacidfk(Boolean param, String pacidfk);
    List<SaludIndNutri> findByNutri3viAndPacidfk(Boolean param, String pacidfk);

    List<SaludIndNutri> findByNutri1saAndPacidfk(Boolean param, String pacidfk);
    List<SaludIndNutri> findByNutri2saAndPacidfk(Boolean param, String pacidfk);
    List<SaludIndNutri> findByNutri3saAndPacidfk(Boolean param, String pacidfk);

    List<SaludIndNutri> findByNutri1doAndPacidfk(Boolean param, String pacidfk);
    List<SaludIndNutri> findByNutri2doAndPacidfk(Boolean param, String pacidfk);
    List<SaludIndNutri> findByNutri3doAndPacidfk(Boolean param, String pacidfk);


    List<SaludIndNutri> findByNutri1lu(Boolean param);
    List<SaludIndNutri> findByNutri2lu(Boolean param);
    List<SaludIndNutri> findByNutri3lu(Boolean param);

    List<SaludIndNutri> findByNutri1ma(Boolean param);
    List<SaludIndNutri> findByNutri2ma(Boolean param);
    List<SaludIndNutri> findByNutri3ma(Boolean param);

    List<SaludIndNutri> findByNutri1mi(Boolean param);
    List<SaludIndNutri> findByNutri2mi(Boolean param);
    List<SaludIndNutri> findByNutri3mi(Boolean param);

    List<SaludIndNutri> findByNutri1ju(Boolean param);
    List<SaludIndNutri> findByNutri2ju(Boolean param);
    List<SaludIndNutri> findByNutri3ju(Boolean param);

    List<SaludIndNutri> findByNutri1vi(Boolean param);
    List<SaludIndNutri> findByNutri2vi(Boolean param);
    List<SaludIndNutri> findByNutri3vi(Boolean param);

    List<SaludIndNutri> findByNutri1sa(Boolean param);
    List<SaludIndNutri> findByNutri2sa(Boolean param);
    List<SaludIndNutri> findByNutri3sa(Boolean param);

    List<SaludIndNutri> findByNutri1do(Boolean param);
    List<SaludIndNutri> findByNutri2do(Boolean param);
    List<SaludIndNutri> findByNutri3do(Boolean param);
}
