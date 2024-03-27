package net.amentum.niomedic.expediente.persistence;


import net.amentum.niomedic.expediente.model.SaludIndCovid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.List;


@Repository
public interface SaludIndCovidRepository extends JpaRepository<SaludIndCovid, Long>, JpaSpecificationExecutor {

    SaludIndCovid findByPacidfk(String pacidfk) throws Exception;

    List<SaludIndCovid> findByCovid1luAndPacidfk(Boolean param, String pacidfk);
    List<SaludIndCovid> findByCovid1maAndPacidfk(Boolean param, String pacidfk);
    List<SaludIndCovid> findByCovid1miAndPacidfk(Boolean param, String pacidfk);
    List<SaludIndCovid> findByCovid1juAndPacidfk(Boolean param, String pacidfk);
    List<SaludIndCovid> findByCovid1viAndPacidfk(Boolean param, String pacidfk);
    List<SaludIndCovid> findByCovid1saAndPacidfk(Boolean param, String pacidfk);
    List<SaludIndCovid> findByCovid1doAndPacidfk(Boolean param, String pacidfk);

    List<SaludIndCovid> findByCovid2luAndPacidfk(Boolean param, String pacidfk);
    List<SaludIndCovid> findByCovid2maAndPacidfk(Boolean param, String pacidfk);
    List<SaludIndCovid> findByCovid2miAndPacidfk(Boolean param, String pacidfk);
    List<SaludIndCovid> findByCovid2juAndPacidfk(Boolean param, String pacidfk);
    List<SaludIndCovid> findByCovid2viAndPacidfk(Boolean param, String pacidfk);
    List<SaludIndCovid> findByCovid2saAndPacidfk(Boolean param, String pacidfk);
    List<SaludIndCovid> findByCovid2doAndPacidfk(Boolean param, String pacidfk);

    List<SaludIndCovid> findByCovid3luAndPacidfk(Boolean param, String pacidfk);
    List<SaludIndCovid> findByCovid3maAndPacidfk(Boolean param, String pacidfk);
    List<SaludIndCovid> findByCovid3miAndPacidfk(Boolean param, String pacidfk);
    List<SaludIndCovid> findByCovid3juAndPacidfk(Boolean param, String pacidfk);
    List<SaludIndCovid> findByCovid3viAndPacidfk(Boolean param, String pacidfk);
    List<SaludIndCovid> findByCovid3saAndPacidfk(Boolean param, String pacidfk);
    List<SaludIndCovid> findByCovid3doAndPacidfk(Boolean param, String pacidfk);

    List<SaludIndCovid> findByCovid4luAndPacidfk(Boolean param, String pacidfk);
    List<SaludIndCovid> findByCovid4maAndPacidfk(Boolean param, String pacidfk);
    List<SaludIndCovid> findByCovid4miAndPacidfk(Boolean param, String pacidfk);
    List<SaludIndCovid> findByCovid4juAndPacidfk(Boolean param, String pacidfk);
    List<SaludIndCovid> findByCovid4viAndPacidfk(Boolean param, String pacidfk);
    List<SaludIndCovid> findByCovid4saAndPacidfk(Boolean param, String pacidfk);
    List<SaludIndCovid> findByCovid4doAndPacidfk(Boolean param, String pacidfk);

    List<SaludIndCovid> findByCovid1lu(Boolean param);
    List<SaludIndCovid> findByCovid1ma(Boolean param);
    List<SaludIndCovid> findByCovid1mi(Boolean param);
    List<SaludIndCovid> findByCovid1ju(Boolean param);
    List<SaludIndCovid> findByCovid1vi(Boolean param);
    List<SaludIndCovid> findByCovid1sa(Boolean param);
    List<SaludIndCovid> findByCovid1do(Boolean param);

    List<SaludIndCovid> findByCovid2lu(Boolean param);
    List<SaludIndCovid> findByCovid2ma(Boolean param);
    List<SaludIndCovid> findByCovid2mi(Boolean param);
    List<SaludIndCovid> findByCovid2ju(Boolean param);
    List<SaludIndCovid> findByCovid2vi(Boolean param);
    List<SaludIndCovid> findByCovid2sa(Boolean param);
    List<SaludIndCovid> findByCovid2do(Boolean param);

    List<SaludIndCovid> findByCovid3lu(Boolean param);
    List<SaludIndCovid> findByCovid3ma(Boolean param);
    List<SaludIndCovid> findByCovid3mi(Boolean param);
    List<SaludIndCovid> findByCovid3ju(Boolean param);
    List<SaludIndCovid> findByCovid3vi(Boolean param);
    List<SaludIndCovid> findByCovid3sa(Boolean param);
    List<SaludIndCovid> findByCovid3do(Boolean param);

    List<SaludIndCovid> findByCovid4lu(Boolean param);
    List<SaludIndCovid> findByCovid4ma(Boolean param);
    List<SaludIndCovid> findByCovid4mi(Boolean param);
    List<SaludIndCovid> findByCovid4ju(Boolean param);
    List<SaludIndCovid> findByCovid4vi(Boolean param);
    List<SaludIndCovid> findByCovid4sa(Boolean param);
    List<SaludIndCovid> findByCovid4do(Boolean param);
}
