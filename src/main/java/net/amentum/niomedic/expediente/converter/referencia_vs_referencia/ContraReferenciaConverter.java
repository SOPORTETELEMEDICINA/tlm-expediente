package net.amentum.niomedic.expediente.converter.referencia_vs_referencia;

import net.amentum.niomedic.expediente.model.referecia_vs_referencia.ContraReferencia;
import net.amentum.niomedic.expediente.views.referencia_vs_referencia.ContraReferenciaView;
import org.springframework.stereotype.Component;

@Component
public class ContraReferenciaConverter {

    public ContraReferencia toEntity(ContraReferenciaView view) {
        ContraReferencia entity = new ContraReferencia();
        entity.setId(view.getId());
        entity.setIdReferencia(view.getIdReferencia());
        entity.setResultadoClinico(view.getResultadoClinico());
        entity.setIngresoDx1(view.getIngresoDx1());
        entity.setIngresoDx2(view.getIngresoDx2());
        entity.setIngresoDx3(view.getIngresoDx3());
        entity.setIngresoDx4(view.getIngresoDx4());
        entity.setIngresoDx5(view.getIngresoDx5());
        entity.setIngresoDx6(view.getIngresoDx6());
        entity.setEgresoDx1(view.getEgresoDx1());
        entity.setEgresoDx2(view.getEgresoDx2());
        entity.setEgresoDx3(view.getEgresoDx3());
        entity.setEgresoDx4(view.getEgresoDx4());
        entity.setEgresoDx5(view.getEgresoDx5());
        entity.setEgresoDx6(view.getEgresoDx6());
        entity.setProced1(view.getProced1());
        entity.setProced2(view.getProced2());
        entity.setProced3(view.getProced3());
        entity.setProced4(view.getProced4());
        entity.setProced5(view.getProced5());
        entity.setProced6(view.getProced6());
        entity.setManejoPaciente(view.getManejoPaciente());
        entity.setTratamientoConc(view.getTratamientoConc());
        entity.setCont(view.getCont());
        entity.setConsultaSubsecuente(view.getConsultaSubsecuente());
        entity.setIdMedicoCref(view.getIdMedicoCref());
        entity.setUnidadMedica(view.getUnidadMedica());
        entity.setRegionSanitaria(view.getRegionSanitaria());
        return entity;
    }

    public ContraReferenciaView toView(ContraReferencia entity) {
        ContraReferenciaView view = new ContraReferenciaView();
        view.setId(entity.getId());
        view.setIdReferencia(entity.getIdReferencia());
        view.setFechaCreacion(entity.getFechaCreacion());
        view.setResultadoClinico(entity.getResultadoClinico());
        view.setIngresoDx1(entity.getIngresoDx1());
        view.setIngresoDx2(entity.getIngresoDx2());
        view.setIngresoDx3(entity.getIngresoDx3());
        view.setIngresoDx4(entity.getIngresoDx4());
        view.setIngresoDx5(entity.getIngresoDx5());
        view.setIngresoDx6(entity.getIngresoDx6());
        view.setEgresoDx1(entity.getEgresoDx1());
        view.setEgresoDx2(entity.getEgresoDx2());
        view.setEgresoDx3(entity.getEgresoDx3());
        view.setEgresoDx4(entity.getEgresoDx4());
        view.setEgresoDx5(entity.getEgresoDx5());
        view.setEgresoDx6(entity.getEgresoDx6());
        view.setProced1(entity.getProced1());
        view.setProced2(entity.getProced2());
        view.setProced3(entity.getProced3());
        view.setProced4(entity.getProced4());
        view.setProced5(entity.getProced5());
        view.setProced6(entity.getProced6());
        view.setManejoPaciente(entity.getManejoPaciente());
        view.setTratamientoConc(entity.getTratamientoConc());
        view.setCont(entity.getCont());
        view.setConsultaSubsecuente(entity.getConsultaSubsecuente());
        view.setIdMedicoCref(entity.getIdMedicoCref());
        view.setUnidadMedica(entity.getUnidadMedica());
        view.setRegionSanitaria(entity.getRegionSanitaria());
        return view;
    }
}
