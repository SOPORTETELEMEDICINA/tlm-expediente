package net.amentum.niomedic.expediente.converter.historia_clinica_general.estudio_laboratorio;

import net.amentum.niomedic.expediente.model.historia_clinica.estudio_laboratorio.HCEstudioLabDocumento;
import net.amentum.niomedic.expediente.views.historia_clinica_general.estudio_laboratorio.HCEstudioLabDocumentoView;
import org.springframework.stereotype.Component;

import org.apache.commons.codec.binary.Base64;

@Component
public class HCEstudioLabDocumentoConverter {

    public HCEstudioLabDocumento toEntity(HCEstudioLabDocumentoView view) {
        HCEstudioLabDocumento entity = new HCEstudioLabDocumento();
        entity.setIdDocumento(view.getIdDocumento());
        entity.setIdHistoriaClinica(view.getIdHistoriaClinica());
        entity.setIdEstudioLaboratorio(view.getIdEstudioLaboratorio());
        entity.setContentType(view.getContentType());
        entity.setNameFile(view.getNameFile());
        entity.setFileBase64(Base64.decodeBase64(view.getFileBase64()));
        return entity;
    }

    public HCEstudioLabDocumentoView toView(HCEstudioLabDocumento entity) {
        HCEstudioLabDocumentoView view = new HCEstudioLabDocumentoView();
        view.setIdDocumento(entity.getIdDocumento());
        view.setIdHistoriaClinica(entity.getIdHistoriaClinica());
        view.setIdEstudioLaboratorio(entity.getIdEstudioLaboratorio());
        view.setContentType(entity.getContentType());
        view.setNameFile(entity.getNameFile());
        view.setFileBase64(Base64.encodeBase64String(entity.getFileBase64()));
        return view;
    }

}
