package net.amentum.niomedic.expediente.converter;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.amentum.niomedic.expediente.persistence.ConsultaRepository;
import net.amentum.niomedic.expediente.persistence.FormatRepository;
import net.amentum.niomedic.expediente.views.FormatResultListView;
import net.amentum.niomedic.expediente.views.FormatResultView;
import net.amentum.niomedic.expediente.views.FormatView;
import net.amentum.niomedic.expediente.exception.FormatException;
import net.amentum.niomedic.expediente.model.Consulta;
import net.amentum.niomedic.expediente.model.Format;
import net.amentum.niomedic.expediente.model.FormatResult;

import java.util.Map;;

/**
 * @author marellano
 * Componente para realizar conversiones de formatos a vistas y viceversa
 */
@Component
public class FormatConverter {

    private FormatRepository formatRepository;
    @Autowired
    public void setFormatRepository(FormatRepository formatRepository) {
        this.formatRepository = formatRepository;
    }
    
    private ConsultaRepository consultaRepository;
    @Autowired 
	public void SetConsultaRepository(ConsultaRepository ConsultaRepository){
		this.consultaRepository = ConsultaRepository;
	}

    private final Logger logger = LoggerFactory.getLogger(FormatConverter.class);
    //objeto para convertir de tipo map a JsonNode
    private ObjectMapper mapp= new ObjectMapper();
    public Format convertViewToEntity(FormatView view, Boolean active, Long version) throws FormatException {
        Format format = new Format();
        format.setActive(view.getActive());
        format.setFormatType(view.getFormatType());
        //conversion de mapp a jsonNode
        JsonNode formato = mapp.convertValue(view.getJsonFormat(), JsonNode.class);
        format.setJsonFormat(formato);
        format.setTitle(view.getTitle());
        if(active){
            format.setIdFormat(view.getIdFormat());
            format.setVersion(view.getVersion());
        } else {
            if(version != null){
                format.setVersion((int) (long) version +1);
            }else {
                format.setVersion(1);
            }
        }
        return format;
    }

    public FormatView convertEntityToView(Format format, Boolean all) {
        logger.debug("Format: "+format);
        FormatView view = new FormatView();
        view.setIdFormat(format.getIdFormat());
        view.setTitle(format.getTitle());
        view.setVersion(format.getVersion());
        view.setActive(format.getActive());
        if(all){
        	//convercion de jsonNode a MAP
        	view.setJsonFormat(mapp.convertValue(format.getJsonFormat(), Map.class));
            view.setFormatType(format.getFormatType());
        }

        return view;
    }

    public FormatResult convertFormatResultViewToEntity(FormatResultView view){
        FormatResult formatResult = new FormatResult();
        formatResult.setCreatedDate(view.getCreatedDate());
        formatResult.setUsername(view.getUsername());
        formatResult.setFormat(formatRepository.findOne(view.getFormatView().getIdFormat()));
        Consulta consulta=consultaRepository.findOne(view.getIdConsulta());
		if(consulta==null) {
//			FormatException formatException = new FormatException("Error al Agreger las Respuesta", FormatException.LAYER_SERVICE, FormatException.ACTION_VALIDATE);
//			formatException.addError("La consulta con el id: "+view.getIdConsulta()+" no existe");
         logger.error("===>>>convertFormatResultViewToEntity() - La consulta con el id: {} no existe.", view.getIdConsulta());
//         throw new FormatException(HttpStatus.NOT_FOUND, String.format(FormatException.ITEM_NO_ENCONTRADO, view.getIdConsulta()));
		}
        //conversion de map a json
		formatResult.setConsulta(consulta);
        JsonNode jsonAnswer = mapp.convertValue(view.getJsonAnswer(), JsonNode.class);
        formatResult.setJsonAnswer(jsonAnswer);
        return formatResult;
    }

    public FormatResultView convertFormatResultToView(FormatResult formatResult, Boolean getAll){
        FormatResultView view = new FormatResultView();
        logger.debug("fr: "+formatResult);
        if(getAll){
            view.setCreatedDate(formatResult.getCreatedDate());
            view.setUsername(formatResult.getUsername());
        }
        view.setIdFormatResult(formatResult.getIdFormatResult());
        view.setFormatView(convertToView(formatResult.getFormat(),getAll));
        view.setJsonAnswer(mapp.convertValue(formatResult.getJsonAnswer(), Map.class));
        view.setIdConsulta(formatResult.getConsulta().getIdConsulta());
        return view;
    }
    
    public FormatResultListView entityToViewResultList(FormatResult formatResult, FormatResultListView formatResultList ) {
    	formatResultList.setIdFormatResult(formatResult.getIdFormatResult());
    	formatResultList.setCreatedDate(formatResult.getCreatedDate());
    	//TODO agregar deviceId a formatos
    	formatResultList.setUsername(formatResult.getUsername());
    	formatResultList.setJsonAnswer(mapp.convertValue(formatResult.getJsonAnswer(), Map.class));
    	formatResultList.setIdFormat(formatResult.getFormat().getIdFormat());
    	formatResultList.setIdConsulta(formatResult.getConsulta().getIdConsulta());
    	return formatResultList;
    }
    
    private FormatView convertToView(Format format, Boolean getAll){
        FormatView view =  new FormatView();
        if(getAll){
            view.setFormatType(format.getFormatType());
          //convercion de jsonNode a MAP
            view.setJsonFormat(mapp.convertValue(format.getJsonFormat(), Map.class));
        }
        view.setIdFormat(format.getIdFormat());
        view.setTitle(format.getTitle());
        view.setVersion(format.getVersion());
        view.setActive(format.getActive());
        return view;

    }
    
}
