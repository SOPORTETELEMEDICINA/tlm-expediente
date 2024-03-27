package net.amentum.niomedic.expediente.converter;

import net.amentum.niomedic.expediente.model.CuestionarioEmbarazo;
import net.amentum.niomedic.expediente.views.CuestionarioEmbarazoView;
import org.springframework.stereotype.Component;

@Component
public class CuestionarioEmbarazoConverter {

    public CuestionarioEmbarazo toEntity(CuestionarioEmbarazoView view, CuestionarioEmbarazo entity) {
        entity.setIdCuestionario(view.getIdCuestionario());
        entity.setMedidfk(view.getMedidfk());
        entity.setPacidfk(view.getPacidfk());
        entity.setDolorDeCabeza(view.isDolorDeCabeza());
        entity.setVisionBorrosa(view.isVisionBorrosa());
        entity.setMareos(view.isMareos());
        entity.setZumbidoEnOidos(view.isZumbidoEnOidos());
        entity.setDolorAbdominal(view.isDolorAbdominal());
        entity.setSangradoVaginal(view.isSangradoVaginal());
        entity.setSinMovimientoBebe(view.isSinMovimientoBebe());
        entity.setSalidaLiquidoVaginal(view.isSalidaLiquidoVaginal());
        entity.setHinchazones(view.isHinchazones());
        entity.setVomitoDiarrea(view.isVomitoDiarrea());
        entity.setFiebreEscalofrios(view.isFiebreEscalofrios());
        entity.setPeso(view.isPeso());
        entity.setDolorAlOrinar(view.isDolorAlOrinar());
        entity.setAbdomenDuro(view.isAbdomenDuro());
        entity.setTotalSi(view.getTotalSi());
        entity.setTotalNo(view.getTotalNo());
        entity.setHoraAplicacion(view.getHoraAplicacion());
        return entity;
    }

    public CuestionarioEmbarazoView toView(CuestionarioEmbarazo entity) {
        CuestionarioEmbarazoView view = new CuestionarioEmbarazoView();
        view.setIdCuestionario(entity.getIdCuestionario());
        view.setMedidfk(entity.getMedidfk());
        view.setPacidfk(entity.getPacidfk());
        view.setDolorDeCabeza(entity.isDolorDeCabeza());
        view.setVisionBorrosa(entity.isVisionBorrosa());
        view.setMareos(entity.isMareos());
        view.setZumbidoEnOidos(entity.isZumbidoEnOidos());
        view.setDolorAbdominal(entity.isDolorAbdominal());
        view.setSangradoVaginal(entity.isSangradoVaginal());
        view.setSinMovimientoBebe(entity.isSinMovimientoBebe());
        view.setSalidaLiquidoVaginal(entity.isSalidaLiquidoVaginal());
        view.setHinchazones(entity.isHinchazones());
        view.setVomitoDiarrea(entity.isVomitoDiarrea());
        view.setFiebreEscalofrios(entity.isFiebreEscalofrios());
        view.setPeso(entity.isPeso());
        view.setDolorAlOrinar(entity.isDolorAlOrinar());
        view.setAbdomenDuro(entity.isAbdomenDuro());
        view.setTotalSi(entity.getTotalSi());
        view.setTotalNo(entity.getTotalNo());
        view.setHoraAplicacion(entity.getHoraAplicacion());
        return view;
    }
}
