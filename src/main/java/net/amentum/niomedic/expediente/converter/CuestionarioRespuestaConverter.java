package net.amentum.niomedic.expediente.converter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.amentum.niomedic.expediente.model.CuestionarioRespuesta;
import net.amentum.niomedic.expediente.views.CuestionarioRespuestaView;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class CuestionarioRespuestaConverter {

    private ObjectMapper mapper = new ObjectMapper();

    public CuestionarioRespuesta toEntity(CuestionarioRespuestaView view) {
        CuestionarioRespuesta entity = new CuestionarioRespuesta();
        entity.setIdRespuesta(view.getIdRespuesta());
        entity.setIdCuestionario(view.getIdCuestionario());
        entity.setCreadoPor(view.getCreadoPor());
        entity.setJson(mapper.convertValue(view.getJson(), JsonNode.class));
        entity.setIdPaciente(view.getIdPaciente());
        return entity;
    }


    public CuestionarioRespuestaView toView(CuestionarioRespuesta entity) {
        CuestionarioRespuestaView view = new CuestionarioRespuestaView();
        view.setIdRespuesta(entity.getIdRespuesta());
        view.setIdCuestionario(entity.getIdCuestionario());
        view.setCreadoPor(entity.getCreadoPor());
        view.setJson(mapper.convertValue(entity.getJson(), ArrayList.class));
        view.setCreatedDate(entity.getCreatedDate());
        view.setIdPaciente(entity.getIdPaciente());
        return view;
    }

}
