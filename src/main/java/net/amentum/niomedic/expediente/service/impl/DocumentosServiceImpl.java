package net.amentum.niomedic.expediente.service.impl;

import net.amentum.niomedic.expediente.converter.DocumentosConverter;
import net.amentum.niomedic.expediente.exception.DocumentosException;
import net.amentum.niomedic.expediente.exception.ExceptionServiceCode;
import net.amentum.niomedic.expediente.exception.ImagenLaboratorioException;
import net.amentum.niomedic.expediente.model.Documentos;
import net.amentum.niomedic.expediente.persistence.ConsultaRepository;
import net.amentum.niomedic.expediente.persistence.DocumentosRepository;
import net.amentum.niomedic.expediente.service.DocumentosService;
import net.amentum.niomedic.expediente.views.DocumentosListView;
import net.amentum.niomedic.expediente.views.DocumentosView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@Transactional(readOnly = true)
public class DocumentosServiceImpl implements DocumentosService {
	private final Logger logger = LoggerFactory.getLogger(DocumentosServiceImpl.class);
	private final Map<String, Object> colOrderNames = new HashMap<>();
	private DocumentosRepository documentosRepository;
	private DocumentosConverter documentosConverter;
	private ConsultaRepository consultaRepository;
	{
		colOrderNames.put("consultaId", "consultaId");
		colOrderNames.put("fechaCreacion", "fechaCreacion");
		colOrderNames.put("idPaciente", "idPaciente");
	}
	@Autowired
	public void setConsultaRepository(ConsultaRepository consultaRepository) {
		this.consultaRepository = consultaRepository;
	}


	@Autowired
	public void setDocumentosRepository(DocumentosRepository documentosRepository) {
		this.documentosRepository = documentosRepository;
	}

	@Autowired
	public void setDocumentosConverter(DocumentosConverter documentosConverter) {
		this.documentosConverter = documentosConverter;
	}


	@Override
	//    public Page<DocumentosListView> getDocumentosPage(Boolean active, String name, Integer page, Integer size, String orderColumn, String orderType, String idPaciente) throws DocumentosException {
	public Page<DocumentosListView> getDocumentosPage(Boolean active, String idPaciente, Long idConsulta, Integer page, Integer size, String orderColumn, String orderType) throws DocumentosException {
		try {
			logger.info("- Obtener listado documentos paginable: - active {} - idPaciente {} - idConsulta {} - page {} - size: {} - orderColumn: {} - orderType: {}",
					active, idPaciente, idConsulta, page, size, orderColumn, orderType);
			//            List<DocumentosView> documentosViewList = new ArrayList<>();
			List<DocumentosListView> documentosViewList = new ArrayList<>();
			Page<Documentos> documentosPage = null;
			Sort sort = new Sort(Sort.Direction.ASC, (String) colOrderNames.get("idPaciente"));

			if (orderColumn != null && orderType != null) {
				if (orderType.equalsIgnoreCase("asc")) {
					sort = new Sort(Sort.Direction.ASC, (String) colOrderNames.get(orderColumn));
				} else {
					sort = new Sort(Sort.Direction.DESC, (String) colOrderNames.get(orderColumn));
				}

			}
			PageRequest request = new PageRequest(page, size, sort);
			final String patternSearch = "%" + idPaciente.toLowerCase() + "%";
			//            final String patternSearchConsulta = "%"+idConsulta.toString()+"%";
			Specifications<Documentos> spec = Specifications.where(
					(root, query, cb) -> {
						Predicate tc = null;
						if (idPaciente != null && !idPaciente.isEmpty()) {
							tc = (tc != null ?
									cb.and(tc, cb.like(cb.lower(root.get("idPaciente")), patternSearch)) :
										cb.like(cb.lower(root.get("idPaciente")), patternSearch));
						}

						if (idConsulta != null && idConsulta != 0) {
							tc = (tc != null ?
									cb.and(tc, cb.equal(root.get("consulta"), idConsulta)) :
										cb.equal(root.get("consulta"), idConsulta));
						}
						tc = (tc != null ? cb.and(tc, cb.equal(root.get("activo"), active)) : cb.equal(root.get("activo"), active));

						return tc;
					}
					);

			if (spec == null) {
				documentosPage = documentosRepository.findAll(request);

			} else {
				documentosPage = documentosRepository.findAll(spec, request);
			}

			documentosPage.getContent().forEach(documentos -> {
				// documentosViewList.add(documentosConverter.toViewPage(documentos));
				documentosViewList.add(documentosConverter.toView(documentos, Boolean.TRUE));
			});
			//            PageImpl<DocumentosView> documentosViewPage = new PageImpl<DocumentosView>(documentosViewList, request, documentosPage.getTotalElements());
			PageImpl<DocumentosListView> documentosViewPage = new PageImpl<DocumentosListView>(documentosViewList, request, documentosPage.getTotalElements());
			return documentosViewPage;
		} catch (Exception ex) {
			DocumentosException doce = new DocumentosException("Ocurrio un error al seleccionar lista Documentos paginable", DocumentosException.LAYER_SERVICE, DocumentosException.ACTION_SELECT);
			logger.error(ExceptionServiceCode.GROUP + "- Error al tratar de seleccionar lista Documentos paginable - CODE: {}-{}", doce.getExceptionCode(), ex);
			throw doce;
		}
	}

	@Transactional(readOnly = false, rollbackFor = {DocumentosException.class})
	@Override
	public void createDocumentos(DocumentosView documentosView) throws DocumentosException {
		try {
			Documentos encontrado = documentosRepository.findByIdPacienteAndDocumentoName(documentosView.getIdPaciente(), documentosView.getDocumentoName());
			if (encontrado == null) {
				Documentos documentos = documentosConverter.toEntity(documentosView, new Documentos(), Boolean.FALSE);
				logger.debug("Insertar nuevo Documento: {}", documentos);
				documentosRepository.save(documentos);
			}else{
				DocumentosException doce = new DocumentosException("======> Ya existe un archivo con ese nombre en la DB", DocumentosException.LAYER_DAO, DocumentosException.ACTION_INSERT);
				doce.addError("======> Ya existe un archivo con ese nombre en la DB");
				logger.error("======> Ya existe un archivo con ese nombre en la DB - CODE {} - {}", doce.getExceptionCode(), documentosView, doce);
				throw doce;
			}
		} catch (DataIntegrityViolationException dive) {
			DocumentosException doce = new DocumentosException("No fue posible agregar Documentos", DocumentosException.LAYER_DAO, DocumentosException.ACTION_INSERT);
			doce.addError("Ocurrio un error al agregar Documentos");
			logger.error("Error al insertar nuevo Documentos - CODE {} - {}", doce.getExceptionCode(), documentosView, doce);
			throw doce;
		}
		//      catch (Exception ex) {
		//         DocumentosException doce = new DocumentosException("Error inesperado al agregar Documentos", DocumentosException.LAYER_DAO, DocumentosException.ACTION_INSERT);
		//         doce.addError("Ocurrió un error al agregar Documentos");
		//         logger.error("Error al insertar nuevo Documentos - CODE {} - {}", doce.getExceptionCode(), documentosView, doce);
		//         throw doce;
		//      }
	}

	@Transactional(readOnly = false, rollbackFor = {DocumentosException.class})
	@Override
	public void createDocumentoBase64(DocumentosView documentosView) throws DocumentosException {
		try {
			Documentos encontrado = documentosRepository.findByIdPacienteAndDocumentoName(documentosView.getIdPaciente(), documentosView.getDocumentoName());
			if(!consultaRepository.exists(documentosView.getConsultaId())) {
				DocumentosException dce = new DocumentosException("No se encuentra en el sistema al la consulta.", ImagenLaboratorioException.LAYER_DAO, ImagenLaboratorioException.ACTION_VALIDATE);
				dce.addError("consulta no Existe");
				throw dce;
			}


			if (encontrado == null) {
				Documentos documentos = documentosConverter.toEntity(documentosView, new Documentos(), Boolean.FALSE);
				logger.debug("Insertar nuevo Documento: {}-{}", documentos.getIdPaciente() , documentos);
				documentos.setActivo(Boolean.TRUE);
				documentosRepository.save(documentos);
			}else{
				DocumentosException doce = new DocumentosException("======> Ya existe un archivo con ese nombre en la DB", DocumentosException.LAYER_DAO, DocumentosException.ACTION_INSERT);
				doce.addError("======> Ya existe un archivo con ese nombre en la DB");
				logger.error("======> Ya existe un archivo con ese nombre en la DB - CODE {} - {}", doce.getExceptionCode(), documentosView, doce);
				throw doce;
			}
		} catch (DataIntegrityViolationException dive) {
			DocumentosException doce = new DocumentosException("No fue posible agregar Documentos", DocumentosException.LAYER_DAO, DocumentosException.ACTION_INSERT);
			doce.addError("Ocurrio un error al agregar Documentos");
			logger.error("Error al insertar nuevo Documentos - CODE {} - {}", doce.getExceptionCode(), documentosView, doce);
			throw doce;
		}

	}

	@Transactional(readOnly = false, rollbackFor = {DocumentosException.class})
	@Override
	public void eliminarDocumento(Long idDocumento) throws DocumentosException {
		try {
			Documentos documento=documentosRepository.findOne(idDocumento);	
			if(documento==null) {
				DocumentosException de = new DocumentosException("Ocurrio un error al eliminar el Documento", DocumentosException.LAYER_DAO, DocumentosException.ACTION_INSERT);
				de.addError("No existe el documento");
				throw de;
			}
			documento.setActivo(Boolean.FALSE);
			logger.info("eliminarDocumento() - Eliminado logico del documento con el id:{}", idDocumento);
			documentosRepository.save(documento);
		}catch(DocumentosException de) {
			throw de;
		}catch(Exception e) {
			DocumentosException de = new DocumentosException("Ocurrio un error al eliminar el Documento", DocumentosException.LAYER_DAO, DocumentosException.ACTION_INSERT);
			//de.addError("No existe el documento");
			logger.info("eliminarDocumento() - Ocurrio un error al eliminar el Documento con el id:{} - error:{}",idDocumento, e);
			throw de;

		}

	}


	@Override
	public List<DocumentosListView> getDocumentosPorPadecimiento(Long idPadecimiento) throws DocumentosException {
		try {
		logger.info("getDocumentosPorPadecimiento() -  Obteniendo documentos por idPadecimiento: {}", idPadecimiento);
		List<Documentos> documentos = documentosRepository.documentosByIdPadecimiento(idPadecimiento);
		List<DocumentosListView> documentoList = new ArrayList<>();
		documentos.forEach(documento -> {
			DocumentosListView doc = documentosConverter.toView(documento, true);
			if(doc!=null) {
				documentoList.add(doc);
			}
		});
		if(documentoList.isEmpty()) {
			logger.info("getDocumentosPorPadecimiento() - No se encotraron documentos relacionados al idPadecimiento: {}",idPadecimiento);
		}
		return documentoList;
		}catch(Exception e) {
			logger.info("getDocumentosPorPadecimiento - Ocurrio un error inesperdo al obtener documentos por idPadecimiento: {} - error: {}",idPadecimiento,e);
			DocumentosException de =new DocumentosException("Ocurrio un error al obtener los documentos", DocumentosException.LAYER_DAO, DocumentosException.ACTION_SELECT);
			throw de;
		}
	}

}



























