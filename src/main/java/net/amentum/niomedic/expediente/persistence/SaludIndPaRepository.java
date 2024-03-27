package net.amentum.niomedic.expediente.persistence;

import net.amentum.niomedic.expediente.model.SaludIndPa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface SaludIndPaRepository extends JpaRepository<SaludIndPa, Long>, JpaSpecificationExecutor {

    SaludIndPa findByPacidfk(String pacidfk) throws Exception;

    List<SaludIndPa> findBySys1luAndPacidfk(Boolean param, String pacidfk);
    List<SaludIndPa> findBySys2luAndPacidfk(Boolean param, String pacidfk);
    List<SaludIndPa> findBySys3luAndPacidfk(Boolean param, String pacidfk);
    List<SaludIndPa> findBySys4luAndPacidfk(Boolean param, String pacidfk);
    List<SaludIndPa> findBySys5luAndPacidfk(Boolean param, String pacidfk);
    List<SaludIndPa> findBySys6luAndPacidfk(Boolean param, String pacidfk);
    List<SaludIndPa> findBySys7luAndPacidfk(Boolean param, String pacidfk);

    List<SaludIndPa> findBySys1maAndPacidfk(Boolean param, String pacidfk);
    List<SaludIndPa> findBySys2maAndPacidfk(Boolean param, String pacidfk);
    List<SaludIndPa> findBySys3maAndPacidfk(Boolean param, String pacidfk);
    List<SaludIndPa> findBySys4maAndPacidfk(Boolean param, String pacidfk);
    List<SaludIndPa> findBySys5maAndPacidfk(Boolean param, String pacidfk);
    List<SaludIndPa> findBySys6maAndPacidfk(Boolean param, String pacidfk);
    List<SaludIndPa> findBySys7maAndPacidfk(Boolean param, String pacidfk);

    List<SaludIndPa> findBySys1miAndPacidfk(Boolean param, String pacidfk);
    List<SaludIndPa> findBySys2miAndPacidfk(Boolean param, String pacidfk);
    List<SaludIndPa> findBySys3miAndPacidfk(Boolean param, String pacidfk);
    List<SaludIndPa> findBySys4miAndPacidfk(Boolean param, String pacidfk);
    List<SaludIndPa> findBySys5miAndPacidfk(Boolean param, String pacidfk);
    List<SaludIndPa> findBySys6miAndPacidfk(Boolean param, String pacidfk);
    List<SaludIndPa> findBySys7miAndPacidfk(Boolean param, String pacidfk);

    List<SaludIndPa> findBySys1juAndPacidfk(Boolean param, String pacidfk);
    List<SaludIndPa> findBySys2juAndPacidfk(Boolean param, String pacidfk);
    List<SaludIndPa> findBySys3juAndPacidfk(Boolean param, String pacidfk);
    List<SaludIndPa> findBySys4juAndPacidfk(Boolean param, String pacidfk);
    List<SaludIndPa> findBySys5juAndPacidfk(Boolean param, String pacidfk);
    List<SaludIndPa> findBySys6juAndPacidfk(Boolean param, String pacidfk);
    List<SaludIndPa> findBySys7juAndPacidfk(Boolean param, String pacidfk);

    List<SaludIndPa> findBySys1viAndPacidfk(Boolean param, String pacidfk);
    List<SaludIndPa> findBySys2viAndPacidfk(Boolean param, String pacidfk);
    List<SaludIndPa> findBySys3viAndPacidfk(Boolean param, String pacidfk);
    List<SaludIndPa> findBySys4viAndPacidfk(Boolean param, String pacidfk);
    List<SaludIndPa> findBySys5viAndPacidfk(Boolean param, String pacidfk);
    List<SaludIndPa> findBySys6viAndPacidfk(Boolean param, String pacidfk);
    List<SaludIndPa> findBySys7viAndPacidfk(Boolean param, String pacidfk);

    List<SaludIndPa> findBySys1saAndPacidfk(Boolean param, String pacidfk);
    List<SaludIndPa> findBySys2saAndPacidfk(Boolean param, String pacidfk);
    List<SaludIndPa> findBySys3saAndPacidfk(Boolean param, String pacidfk);
    List<SaludIndPa> findBySys4saAndPacidfk(Boolean param, String pacidfk);
    List<SaludIndPa> findBySys5saAndPacidfk(Boolean param, String pacidfk);
    List<SaludIndPa> findBySys6saAndPacidfk(Boolean param, String pacidfk);
    List<SaludIndPa> findBySys7saAndPacidfk(Boolean param, String pacidfk);

    List<SaludIndPa> findBySys1doAndPacidfk(Boolean param, String pacidfk);
    List<SaludIndPa> findBySys2doAndPacidfk(Boolean param, String pacidfk);
    List<SaludIndPa> findBySys3doAndPacidfk(Boolean param, String pacidfk);
    List<SaludIndPa> findBySys4doAndPacidfk(Boolean param, String pacidfk);
    List<SaludIndPa> findBySys5doAndPacidfk(Boolean param, String pacidfk);
    List<SaludIndPa> findBySys6doAndPacidfk(Boolean param, String pacidfk);
    List<SaludIndPa> findBySys7doAndPacidfk(Boolean param, String pacidfk);

    List<SaludIndPa> findBySys1lu(Boolean param);
    List<SaludIndPa> findBySys2lu(Boolean param);
    List<SaludIndPa> findBySys3lu(Boolean param);
    List<SaludIndPa> findBySys4lu(Boolean param);
    List<SaludIndPa> findBySys5lu(Boolean param);
    List<SaludIndPa> findBySys6lu(Boolean param);
    List<SaludIndPa> findBySys7lu(Boolean param);

    List<SaludIndPa> findBySys1ma(Boolean param);
    List<SaludIndPa> findBySys2ma(Boolean param);
    List<SaludIndPa> findBySys3ma(Boolean param);
    List<SaludIndPa> findBySys4ma(Boolean param);
    List<SaludIndPa> findBySys5ma(Boolean param);
    List<SaludIndPa> findBySys6ma(Boolean param);
    List<SaludIndPa> findBySys7ma(Boolean param);

    List<SaludIndPa> findBySys1mi(Boolean param);
    List<SaludIndPa> findBySys2mi(Boolean param);
    List<SaludIndPa> findBySys3mi(Boolean param);
    List<SaludIndPa> findBySys4mi(Boolean param);
    List<SaludIndPa> findBySys5mi(Boolean param);
    List<SaludIndPa> findBySys6mi(Boolean param);
    List<SaludIndPa> findBySys7mi(Boolean param);

    List<SaludIndPa> findBySys1ju(Boolean param);
    List<SaludIndPa> findBySys2ju(Boolean param);
    List<SaludIndPa> findBySys3ju(Boolean param);
    List<SaludIndPa> findBySys4ju(Boolean param);
    List<SaludIndPa> findBySys5ju(Boolean param);
    List<SaludIndPa> findBySys6ju(Boolean param);
    List<SaludIndPa> findBySys7ju(Boolean param);

    List<SaludIndPa> findBySys1vi(Boolean param);
    List<SaludIndPa> findBySys2vi(Boolean param);
    List<SaludIndPa> findBySys3vi(Boolean param);
    List<SaludIndPa> findBySys4vi(Boolean param);
    List<SaludIndPa> findBySys5vi(Boolean param);
    List<SaludIndPa> findBySys6vi(Boolean param);
    List<SaludIndPa> findBySys7vi(Boolean param);

    List<SaludIndPa> findBySys1sa(Boolean param);
    List<SaludIndPa> findBySys2sa(Boolean param);
    List<SaludIndPa> findBySys3sa(Boolean param);
    List<SaludIndPa> findBySys4sa(Boolean param);
    List<SaludIndPa> findBySys5sa(Boolean param);
    List<SaludIndPa> findBySys6sa(Boolean param);
    List<SaludIndPa> findBySys7sa(Boolean param);

    List<SaludIndPa> findBySys1do(Boolean param);
    List<SaludIndPa> findBySys2do(Boolean param);
    List<SaludIndPa> findBySys3do(Boolean param);
    List<SaludIndPa> findBySys4do(Boolean param);
    List<SaludIndPa> findBySys5do(Boolean param);
    List<SaludIndPa> findBySys6do(Boolean param);
    List<SaludIndPa> findBySys7do(Boolean param);
}
