package net.amentum.niomedic.expediente.converter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Base64;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import net.amentum.niomedic.expediente.exception.ResultadoEstudioException;
import net.amentum.niomedic.expediente.model.Consulta;
import net.amentum.niomedic.expediente.model.ResultadoEstudio;
import net.amentum.niomedic.expediente.views.ResultadoEstudioView;

@Component
@Slf4j
public class ResultadoEstudioConverter {

	@Value("${resultado-estudios}")
	private String direccion;

	public ResultadoEstudioView toView(ResultadoEstudio entity) {
		ResultadoEstudioView view = new ResultadoEstudioView();
		view.setIdResultadoEstudio(entity.getIdResultadoEstudio());
		view.setMimeType(entity.getMimeType());
		String file = obtenerArchivo(entity.getArchivo());
		if(file!= null) {
			view.setBase64(file);
		}else {return null;}
		view.setFechaCreacion(entity.getFechaCreacion());
		view.setIdPaciente(entity.getIdPaciente());
		view.setIdConsulta(entity.getConsulta().getIdConsulta());
		return view;
	}
	public ResultadoEstudio toEntity(ResultadoEstudio entity,ResultadoEstudioView view) throws ResultadoEstudioException {
		entity.setIdResultadoEstudio(view.getIdResultadoEstudio());
		entity.setMimeType(view.getMimeType().toLowerCase().trim());
		entity.setArchivo(guardarArchivo(view.getBase64(),view.getMimeType().toLowerCase().trim()));
		entity.setFechaCreacion(view.getFechaCreacion());
		entity.setIdPaciente(view.getIdPaciente());
		Consulta consulta= new Consulta();
		consulta.setIdConsulta(view.getIdConsulta());
		entity.setConsulta(consulta);
		return entity;
	}



	static public UUID idImgane() {
		UUID idOne = UUID.randomUUID();
		return idOne;
	}

	private String guardarArchivo(String archivoB64, String mime) throws ResultadoEstudioException {
		try {
			String fotmatoarchivo = mime.substring(mime.indexOf('/') + 1, mime.length()).trim();
			String idimage = idImgane() + "." + fotmatoarchivo;
			String dir = direccion + "" + idimage;
			byte[] decodedBytes = Base64.getDecoder().decode(archivoB64.getBytes());
			FileOutputStream out;
			out = new FileOutputStream(dir);
			out.write(decodedBytes);
			out.close();
			return dir;
		}catch(IllegalArgumentException iae) {
			log.error("Formato de base64 incorrecto - error: {}", iae);
			throw new ResultadoEstudioException(HttpStatus.BAD_REQUEST, ResultadoEstudioException.BASE64_ERROR);	
		} catch (Exception ex) {
			log.error("Error inensperdado al guardar el archivo - error: {}", ex);
			throw new ResultadoEstudioException(HttpStatus.INTERNAL_SERVER_ERROR, ResultadoEstudioException.ARCHIVO_ERROR);	
		}
	}

	private String obtenerArchivo(String imagenBase64) {

		String encodedBase64;
		try {
			// se obtiene la imagen
			File inputFile = new File(imagenBase64);
			if(inputFile.exists()) {
				FileInputStream fileInputStreamReader = new FileInputStream(inputFile);
				// se conbierte a bytes
				byte[] bytes = new byte[(int) inputFile.length()];
				fileInputStreamReader.read(bytes);
				// el conjunto de bytes se le da formato base64
				encodedBase64 = Base64.getEncoder().encodeToString(bytes);

				return encodedBase64;
			}else {
				log.error("No existe el archivo en la ruta: {}",imagenBase64);
			}
		} catch (Exception ex) {
			log.error(" Ocurrio un error al obtener el archivo- {}", ex);
		}
		return null;
	}
}
