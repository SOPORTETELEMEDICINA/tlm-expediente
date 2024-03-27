package net.amentum.niomedic.expediente.converter.referencia_vs_referencia;

import net.amentum.niomedic.expediente.model.referecia_vs_referencia.Referencia;
import net.amentum.niomedic.expediente.views.referencia_vs_referencia.ReferenciaView;
import org.springframework.stereotype.Component;

@Component
public class ReferenciaConverter {

    public Referencia toEntity(ReferenciaView view) {
        Referencia entity = new Referencia();
        entity.setId(view.getId());
        entity.setFolio(view.getFolio());
        entity.setVez(view.getVez());
        entity.setUmRefClues(view.getUmRefClues());
        entity.setRegionSanitaria(view.getRegionSanitaria());
        entity.setIdPaciente(view.getIdPaciente());
        entity.setDx1(view.getDx1());
        entity.setDx2(view.getDx2());
        entity.setDx3(view.getDx3());
        entity.setDx4(view.getDx4());
        entity.setDx5(view.getDx5());
        entity.setDx6(view.getDx6());
        entity.setUrgente(view.getUrgente());
        entity.setUnidadMedica(view.getUnidadMedica());
        entity.setEspReq(view.getEspReq());
        entity.setIdMedicoRecibe(view.getIdMedicoRecibe());
        entity.setRefSamu(view.getRefSamu());
        entity.setRefSamuFecha(view.getRefSamuFecha());
        entity.setRefMotivo(view.getRefMotivo());
        entity.setRefResClin(view.getRefResClin());
        entity.setRefSvTaSys(view.getRefSvTaSys());
        entity.setRefSvTaDia(view.getRefSvTaDia());
        entity.setRefSvTemp(view.getRefSvTemp());
        entity.setRefSvFr(view.getRefSvFr());
        entity.setRefSvFc(view.getRefSvFc());
        entity.setRefSvPeso(view.getRefSvPeso());
        entity.setRefSvTalla(view.getRefSvTalla());
        entity.setIdMedicoCrea(view.getIdMedicoCrea());
        entity.setEstado(view.getEstado());
        entity.setActivo(view.getActivo());
        if(view.getTipo() != null) {
            if(view.getTipo() == 1)
                entity.setReferencia();
            else
                entity.setInterconsulta();
        }
        return entity;
    }

    public ReferenciaView toView(Referencia entity) {
        ReferenciaView view = new ReferenciaView();
        view.setId(entity.getId());
        view.setFolio(entity.getFolio());
        view.setVez(entity.getVez());
        view.setUmRefClues(entity.getUmRefClues());
        view.setRegionSanitaria(entity.getRegionSanitaria());
        view.setIdPaciente(entity.getIdPaciente());
        view.setDx1(entity.getDx1());
        view.setDx2(entity.getDx2());
        view.setDx3(entity.getDx3());
        view.setDx4(entity.getDx4());
        view.setDx5(entity.getDx5());
        view.setDx6(entity.getDx6());
        view.setUrgente(entity.getUrgente());
        view.setUnidadMedica(entity.getUnidadMedica());
        view.setEspReq(entity.getEspReq());
        view.setIdMedicoRecibe(entity.getIdMedicoRecibe());
        view.setRefSamu(entity.getRefSamu());
        view.setRefSamuFecha(entity.getRefSamuFecha());
        view.setRefMotivo(entity.getRefMotivo());
        view.setRefResClin(entity.getRefResClin());
        view.setRefSvTaSys(entity.getRefSvTaSys());
        view.setRefSvTaDia(entity.getRefSvTaDia());
        view.setRefSvTemp(entity.getRefSvTemp());
        view.setRefSvFr(entity.getRefSvFr());
        view.setRefSvFc(entity.getRefSvFc());
        view.setRefSvPeso(entity.getRefSvPeso());
        view.setRefSvTalla(entity.getRefSvTalla());
        view.setIdMedicoCrea(entity.getIdMedicoCrea());
        view.setEstado(entity.getEstado());
        view.setActivo(entity.getActivo());
        view.setTipo(entity.getTipo());
        return view;
    }
}
