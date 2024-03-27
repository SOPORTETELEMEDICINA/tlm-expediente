package net.amentum.niomedic.expediente.converter;

import net.amentum.niomedic.expediente.model.DatosClinicos;
import net.amentum.niomedic.expediente.views.DatosClinicosView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component
public class DatosClinicosConverter {
    private final Logger logger = LoggerFactory.getLogger(DatosClinicosConverter.class);

    public DatosClinicos toEntity(DatosClinicosView datosClinicosView, DatosClinicos datosClinicos, Boolean update) {
        datosClinicos.setIdPaciente(datosClinicosView.getIdPaciente());
        datosClinicos.setFechaCreacion((!update) ? new Date() : datosClinicos.getFechaCreacion());
        datosClinicos.setUltimaModificacion((!update) ? new Date() : datosClinicosView.getUltimaModificacion());
        datosClinicos.setCreadoPor(datosClinicosView.getCreadoPor());
        datosClinicos.setGrupoSanguineo(datosClinicosView.getGrupoSanguineo());
        datosClinicos.setFactorRh(datosClinicosView.getFactorRh());
        datosClinicos.setAlergias(datosClinicosView.getAlergias());
        datosClinicos.setDiscapacidad(datosClinicosView.getDiscapacidad());
        datosClinicos.setInstitucionSalud(datosClinicosView.getInstitucionSalud());
        datosClinicos.setNsss(datosClinicosView.getNsss());
        datosClinicos.setActivo(datosClinicosView.getActivo());
        logger.debug("convertir DatosClinicosView to Entity: {}", datosClinicos);
        return datosClinicos;
    }

    public DatosClinicosView toView(DatosClinicos datosClinicos, Boolean complete) {
        DatosClinicosView datosClinicosView = new DatosClinicosView();
        datosClinicosView.setIdDatosClinicos(datosClinicos.getIdDatosClinicos());
        datosClinicosView.setIdPaciente(datosClinicos.getIdPaciente());
        datosClinicosView.setFechaCreacion(datosClinicos.getFechaCreacion());
        datosClinicosView.setUltimaModificacion(datosClinicos.getUltimaModificacion());
        datosClinicosView.setCreadoPor(datosClinicos.getCreadoPor());
        datosClinicosView.setGrupoSanguineo(datosClinicos.getGrupoSanguineo());
        datosClinicosView.setFactorRh(datosClinicos.getFactorRh());
        datosClinicosView.setAlergias(datosClinicos.getAlergias());
        datosClinicosView.setDiscapacidad(datosClinicos.getDiscapacidad());
        datosClinicosView.setInstitucionSalud(datosClinicos.getInstitucionSalud());
        datosClinicosView.setNsss(datosClinicos.getNsss());
        datosClinicosView.setActivo(datosClinicos.getActivo());
        logger.debug("convertir DatosClinicos to View: {}", datosClinicosView);
        return datosClinicosView;
    }
}
