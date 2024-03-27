package net.amentum.niomedic.expediente.persistence;

import net.amentum.niomedic.expediente.model.SaludIndGluc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface SaludIndGlucRepository extends JpaRepository<SaludIndGluc, Long>, JpaSpecificationExecutor {
       SaludIndGluc findByPacidfk( String pacidfk) throws Exception;

       List<SaludIndGluc> findByGlu1luAndPacidfk(Boolean glu1lu, String pacidfk);
       List<SaludIndGluc> findByGlu2luAndPacidfk(Boolean glu2lu, String pacidfk);
       List<SaludIndGluc> findByGlu3luAndPacidfk(Boolean glu3lu, String pacidfk);
       List<SaludIndGluc> findByGlu4luAndPacidfk(Boolean glu4lu, String pacidfk);
       List<SaludIndGluc> findByGlu5luAndPacidfk(Boolean glu5lu, String pacidfk);
       List<SaludIndGluc> findByGlu6luAndPacidfk(Boolean glu6lu, String pacidfk);
       List<SaludIndGluc> findByGlu7luAndPacidfk(Boolean glu7lu, String pacidfk);
       List<SaludIndGluc> findByGlu8luAndPacidfk(Boolean glu8lu, String pacidfk);

       List<SaludIndGluc> findByGlu1maAndPacidfk(Boolean glu1ma, String pacidfk);
       List<SaludIndGluc> findByGlu2maAndPacidfk(Boolean glu2ma, String pacidfk);
       List<SaludIndGluc> findByGlu3maAndPacidfk(Boolean glu3ma, String pacidfk);
       List<SaludIndGluc> findByGlu4maAndPacidfk(Boolean glu4ma, String pacidfk);
       List<SaludIndGluc> findByGlu5maAndPacidfk(Boolean glu5ma, String pacidfk);
       List<SaludIndGluc> findByGlu6maAndPacidfk(Boolean glu6ma, String pacidfk);
       List<SaludIndGluc> findByGlu7maAndPacidfk(Boolean glu7ma, String pacidfk);
       List<SaludIndGluc> findByGlu8maAndPacidfk(Boolean glu8ma, String pacidfk);

       List<SaludIndGluc> findByGlu1miAndPacidfk(Boolean glu1mi, String pacidfk);
       List<SaludIndGluc> findByGlu2miAndPacidfk(Boolean glu2mi, String pacidfk);
       List<SaludIndGluc> findByGlu3miAndPacidfk(Boolean glu3mi, String pacidfk);
       List<SaludIndGluc> findByGlu4miAndPacidfk(Boolean glu4mi, String pacidfk);
       List<SaludIndGluc> findByGlu5miAndPacidfk(Boolean glu5mi, String pacidfk);
       List<SaludIndGluc> findByGlu6miAndPacidfk(Boolean glu6mi, String pacidfk);
       List<SaludIndGluc> findByGlu7miAndPacidfk(Boolean glu7mi, String pacidfk);
       List<SaludIndGluc> findByGlu8miAndPacidfk(Boolean glu8mi, String pacidfk);

       List<SaludIndGluc> findByGlu1juAndPacidfk(Boolean glu1ju, String pacidfk);
       List<SaludIndGluc> findByGlu2juAndPacidfk(Boolean glu2ju, String pacidfk);
       List<SaludIndGluc> findByGlu3juAndPacidfk(Boolean glu3ju, String pacidfk);
       List<SaludIndGluc> findByGlu4juAndPacidfk(Boolean glu4ju, String pacidfk);
       List<SaludIndGluc> findByGlu5juAndPacidfk(Boolean glu5ju, String pacidfk);
       List<SaludIndGluc> findByGlu6juAndPacidfk(Boolean glu6ju, String pacidfk);
       List<SaludIndGluc> findByGlu7juAndPacidfk(Boolean glu7ju, String pacidfk);
       List<SaludIndGluc> findByGlu8juAndPacidfk(Boolean glu8ju, String pacidfk);

       List<SaludIndGluc> findByGlu1viAndPacidfk(Boolean glu1vi, String pacidfk);
       List<SaludIndGluc> findByGlu2viAndPacidfk(Boolean glu2vi, String pacidfk);
       List<SaludIndGluc> findByGlu3viAndPacidfk(Boolean glu3vi, String pacidfk);
       List<SaludIndGluc> findByGlu4viAndPacidfk(Boolean glu4vi, String pacidfk);
       List<SaludIndGluc> findByGlu5viAndPacidfk(Boolean glu5vi, String pacidfk);
       List<SaludIndGluc> findByGlu6viAndPacidfk(Boolean glu6vi, String pacidfk);
       List<SaludIndGluc> findByGlu7viAndPacidfk(Boolean glu7vi, String pacidfk);
       List<SaludIndGluc> findByGlu8viAndPacidfk(Boolean glu8vi, String pacidfk);

       List<SaludIndGluc> findByGlu1saAndPacidfk(Boolean glu1sa, String pacidfk);
       List<SaludIndGluc> findByGlu2saAndPacidfk(Boolean glu2sa, String pacidfk);
       List<SaludIndGluc> findByGlu3saAndPacidfk(Boolean glu3sa, String pacidfk);
       List<SaludIndGluc> findByGlu4saAndPacidfk(Boolean glu4sa, String pacidfk);
       List<SaludIndGluc> findByGlu5saAndPacidfk(Boolean glu5sa, String pacidfk);
       List<SaludIndGluc> findByGlu6saAndPacidfk(Boolean glu6sa, String pacidfk);
       List<SaludIndGluc> findByGlu7saAndPacidfk(Boolean glu7sa, String pacidfk);
       List<SaludIndGluc> findByGlu8saAndPacidfk(Boolean glu8sa, String pacidfk);

       List<SaludIndGluc> findByGlu1doAndPacidfk(Boolean glu1do, String pacidfk);
       List<SaludIndGluc> findByGlu2doAndPacidfk(Boolean glu2do, String pacidfk);
       List<SaludIndGluc> findByGlu3doAndPacidfk(Boolean glu3do, String pacidfk);
       List<SaludIndGluc> findByGlu4doAndPacidfk(Boolean glu4do, String pacidfk);
       List<SaludIndGluc> findByGlu5doAndPacidfk(Boolean glu5do, String pacidfk);
       List<SaludIndGluc> findByGlu6doAndPacidfk(Boolean glu6do, String pacidfk);
       List<SaludIndGluc> findByGlu7doAndPacidfk(Boolean glu7do, String pacidfk);
       List<SaludIndGluc> findByGlu8doAndPacidfk(Boolean glu8do, String pacidfk);

       List<SaludIndGluc> findByGlu1lu(Boolean glu1lu);
       List<SaludIndGluc> findByGlu2lu(Boolean glu2lu);
       List<SaludIndGluc> findByGlu3lu(Boolean glu3lu);
       List<SaludIndGluc> findByGlu4lu(Boolean glu4lu);
       List<SaludIndGluc> findByGlu5lu(Boolean glu5lu);
       List<SaludIndGluc> findByGlu6lu(Boolean glu6lu);
       List<SaludIndGluc> findByGlu7lu(Boolean glu7lu);
       List<SaludIndGluc> findByGlu8lu(Boolean glu8lu);

       List<SaludIndGluc> findByGlu1ma(Boolean glu1ma);
       List<SaludIndGluc> findByGlu2ma(Boolean glu2ma);
       List<SaludIndGluc> findByGlu3ma(Boolean glu3ma);
       List<SaludIndGluc> findByGlu4ma(Boolean glu4ma);
       List<SaludIndGluc> findByGlu5ma(Boolean glu5ma);
       List<SaludIndGluc> findByGlu6ma(Boolean glu6ma);
       List<SaludIndGluc> findByGlu7ma(Boolean glu7ma);
       List<SaludIndGluc> findByGlu8ma(Boolean glu8ma);

       List<SaludIndGluc> findByGlu1mi(Boolean glu1mi);
       List<SaludIndGluc> findByGlu2mi(Boolean glu2mi);
       List<SaludIndGluc> findByGlu3mi(Boolean glu3mi);
       List<SaludIndGluc> findByGlu4mi(Boolean glu4mi);
       List<SaludIndGluc> findByGlu5mi(Boolean glu5mi);
       List<SaludIndGluc> findByGlu6mi(Boolean glu6mi);
       List<SaludIndGluc> findByGlu7mi(Boolean glu7mi);
       List<SaludIndGluc> findByGlu8mi(Boolean glu8mi);

       List<SaludIndGluc> findByGlu1ju(Boolean glu1ju);
       List<SaludIndGluc> findByGlu2ju(Boolean glu2ju);
       List<SaludIndGluc> findByGlu3ju(Boolean glu3ju);
       List<SaludIndGluc> findByGlu4ju(Boolean glu4ju);
       List<SaludIndGluc> findByGlu5ju(Boolean glu5ju);
       List<SaludIndGluc> findByGlu6ju(Boolean glu6ju);
       List<SaludIndGluc> findByGlu7ju(Boolean glu7ju);
       List<SaludIndGluc> findByGlu8ju(Boolean glu8ju);

       List<SaludIndGluc> findByGlu1vi(Boolean glu1vi);
       List<SaludIndGluc> findByGlu2vi(Boolean glu2vi);
       List<SaludIndGluc> findByGlu3vi(Boolean glu3vi);
       List<SaludIndGluc> findByGlu4vi(Boolean glu4vi);
       List<SaludIndGluc> findByGlu5vi(Boolean glu5vi);
       List<SaludIndGluc> findByGlu6vi(Boolean glu6vi);
       List<SaludIndGluc> findByGlu7vi(Boolean glu7vi);
       List<SaludIndGluc> findByGlu8vi(Boolean glu8vi);

       List<SaludIndGluc> findByGlu1sa(Boolean glu1sa);
       List<SaludIndGluc> findByGlu2sa(Boolean glu2sa);
       List<SaludIndGluc> findByGlu3sa(Boolean glu3sa);
       List<SaludIndGluc> findByGlu4sa(Boolean glu4sa);
       List<SaludIndGluc> findByGlu5sa(Boolean glu5sa);
       List<SaludIndGluc> findByGlu6sa(Boolean glu6sa);
       List<SaludIndGluc> findByGlu7sa(Boolean glu7sa);
       List<SaludIndGluc> findByGlu8sa(Boolean glu8sa);

       List<SaludIndGluc> findByGlu1do(Boolean glu1do);
       List<SaludIndGluc> findByGlu2do(Boolean glu2do);
       List<SaludIndGluc> findByGlu3do(Boolean glu3do);
       List<SaludIndGluc> findByGlu4do(Boolean glu4do);
       List<SaludIndGluc> findByGlu5do(Boolean glu5do);
       List<SaludIndGluc> findByGlu6do(Boolean glu6do);
       List<SaludIndGluc> findByGlu7do(Boolean glu7do);
       List<SaludIndGluc> findByGlu8do(Boolean glu8do);
}
