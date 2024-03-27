package net.amentum.niomedic.expediente.service.impl;

import net.amentum.niomedic.expediente.converter.ConsultaConverter;
import net.amentum.niomedic.expediente.converter.PadecimientoConverter;
import net.amentum.niomedic.expediente.exception.ExceptionServiceCode;
import net.amentum.niomedic.expediente.exception.PadecimientoException;
import net.amentum.niomedic.expediente.model.CatCie10;
import net.amentum.niomedic.expediente.model.Consulta;
import net.amentum.niomedic.expediente.model.Documentos;
import net.amentum.niomedic.expediente.model.EstudioLaboratorio;
import net.amentum.niomedic.expediente.model.Padecimiento;
import net.amentum.niomedic.expediente.persistence.CatCie10Repository;
import net.amentum.niomedic.expediente.persistence.ConsultaRepository;
import net.amentum.niomedic.expediente.persistence.DocumentosRepository;
import net.amentum.niomedic.expediente.persistence.EstudioLaboratorioRepository;
import net.amentum.niomedic.expediente.persistence.PadecimientoRepository;
import net.amentum.niomedic.expediente.service.PadecimientoService;
import net.amentum.niomedic.expediente.views.ConsultaView;
import net.amentum.niomedic.expediente.views.PadecimientoView;
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

import java.math.BigInteger;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class PadecimientoServiceImpl implements PadecimientoService {

   private final Logger logger = LoggerFactory.getLogger(PadecimientoServiceImpl.class);
   private final Map<String, Object> colOrderNames = new HashMap<>();
   private PadecimientoRepository padecimientoRepository;
   private PadecimientoConverter padecimientoConverter;
   private EstudioLaboratorioRepository estudioLaboratorioRepository;
   private ConsultaRepository consultaRepository;
   private DocumentosRepository documentosRepository;
   private CatCie10Repository catCie10Repository;
   private ConsultaConverter consultaConverter;

   {
      colOrderNames.put("fechaCreacion", "fechaCreacion");
      colOrderNames.put("estatus", "estatus");
      colOrderNames.put("nombreMedicoTratante", "nombreMedicoTratante");
      colOrderNames.put("medicoTratante", "nombre_medico_tratante");
      colOrderNames.put("fechaConsulta", "fecha_consulta");
      colOrderNames.put("idConsulta", "id_consulta");
   }

   @Autowired
   public void setPadecimientoRepository(PadecimientoRepository padecimientoRepository) {
      this.padecimientoRepository = padecimientoRepository;
   }

   @Autowired
   public void setConsultaConverter(ConsultaConverter consultaConverter) {
      this.consultaConverter = consultaConverter;
   }

   @Autowired
   public void setPadecimientoConverter(PadecimientoConverter padecimientoConverter) {
      this.padecimientoConverter = padecimientoConverter;
   }

   @Autowired
   public void setEstudioLaboratorioRepository(EstudioLaboratorioRepository estudioLaboratorioRepository) {
      this.estudioLaboratorioRepository = estudioLaboratorioRepository;
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
   public void setCatCie10Repository(CatCie10Repository catCie10Repository) {
      this.catCie10Repository = catCie10Repository;
   }

   @Transactional(readOnly = false, rollbackFor = {PadecimientoException.class})
   @Override
   public void createPadecimiento(PadecimientoView padecimientoView) throws PadecimientoException {
      try {/* TODO: checar si ya existe un id_paciente*/
         CatCie10 catCie10 = catCie10Repository.findOne(padecimientoView.getCie10Id());
         Padecimiento encontrado = padecimientoRepository.findByCatCie10AndIdPaciente(catCie10, padecimientoView.getIdPaciente());
         if (encontrado == null) {
            Padecimiento padecimiento = padecimientoConverter.toEntity(padecimientoView, new Padecimiento(), Boolean.FALSE);
            logger.debug("Insertar nuevo Padecimiento: {}", padecimiento);
            padecimientoRepository.save(padecimiento);
         } else {
            padecimientoView.setIdPadecimiento(encontrado.getIdPadecimiento());
            updatePadecimiento(padecimientoView);
         }/*         Padecimiento padecimiento = padecimientoConverter.toEntity(padecimientoView, new Padecimiento(), Boolean.FALSE); logger.debug("Insertar nuevo Padecimiento: {}", padecimiento); padecimientoRepository.save(padecimiento);*/
      } catch (DataIntegrityViolationException dive) {
         PadecimientoException pe = new PadecimientoException("No fue posible agregar Padecimiento", PadecimientoException.LAYER_DAO, PadecimientoException.ACTION_INSERT);
         pe.addError("Ocurrio un error al agregar Padecimiento");
         logger.error("Error al insertar nuevo Padecimiento - CODE {} - {}", pe.getExceptionCode(), padecimientoView, pe);
         throw pe;
      } catch (Exception ex) {
         PadecimientoException pe = new PadecimientoException("Error inesperado al agregar Padecimiento", PadecimientoException.LAYER_DAO, PadecimientoException.ACTION_INSERT);
         pe.addError("Ocurrió un error al agregar Padecimiento");
         logger.error("Error al insertar nuevo Padecimiento - CODE {} - {}", pe.getExceptionCode(), padecimientoView, pe);
         throw pe;
      }
   }

   @Transactional(readOnly = false, rollbackFor = {PadecimientoException.class})
   @Override
   public void updatePadecimiento(PadecimientoView padecimientoView) throws PadecimientoException {
      try {/*         if (padecimientoRepository.findByIdPaciente(padecimientoView.getIdPaciente()) == null) {*/
         if (padecimientoRepository.findByIdPadecimiento(padecimientoView.getIdPadecimiento()) == null) {
            PadecimientoException pe = new PadecimientoException("No se encuentra en el sistema Padecimiento.", PadecimientoException.LAYER_DAO, PadecimientoException.ACTION_VALIDATE);
            pe.addError("Padecimiento no encontrado");
            throw pe;
         }/*         Padecimiento padecimiento = padecimientoRepository.findByIdPaciente(padecimientoView.getIdPaciente());*/
         Padecimiento padecimiento = padecimientoRepository.findByIdPadecimiento(padecimientoView.getIdPadecimiento());
         Collection<EstudioLaboratorio> estudioLaboratorioArrayList = padecimiento.getEstudioLaboratorioList().stream().collect(Collectors.toList());/*         padecimiento.setEstudioLaboratorioList(new ArrayList<>());*/
         for (EstudioLaboratorio el : estudioLaboratorioArrayList)
            if (!existEstudioLaboratorio(el.getPadecimiento().getIdPadecimiento(), padecimiento.getEstudioLaboratorioList())) {
               el.setPadecimiento(null);
               estudioLaboratorioRepository.delete(el);
            }
//         Collection<Consulta> consultaArrayList = padecimiento.getConsultaList().stream().collect(Collectors.toList());
//         System.out.println("======>SERVICEIMPL->" + consultaArrayList);/*         padecimiento.setConsultaList(new ArrayList<>());*/
//         for (Consulta eve : consultaArrayList)
//            if (!existConsulta(eve.getPadecimiento().getIdPadecimiento(), padecimiento.getConsultaList())) {
//               System.out.println("======>SERVICEIMPL->entro para borrar la consulta");
//               eve.setPadecimiento(null);
//               consultaRepository.delete(eve);
//            }
         Collection<Documentos> documentosArrayList = padecimiento.getDocumentosList().stream().collect(Collectors.toList());/*         padecimiento.setDocumentosList(new ArrayList<>());*/
         for (Documentos doc : documentosArrayList)
            if (!existDocumentos(doc.getPadecimiento().getIdPadecimiento(), padecimiento.getDocumentosList())) {
               doc.setPadecimiento(null);
               documentosRepository.delete(doc);
            }/*         limpiar las listas*/
//         padecimiento.setConsultaList(new ArrayList<>());
         padecimiento.setDocumentosList(new ArrayList<>());
         padecimiento.setEstudioLaboratorioList(new ArrayList<>());
         padecimiento = padecimientoConverter.toEntity(padecimientoView, padecimiento, Boolean.TRUE);
         logger.debug("Editar Padecimiento: {}", padecimiento);
         padecimientoRepository.save(padecimiento);
      } catch (DataIntegrityViolationException dive) {
         PadecimientoException pe = new PadecimientoException("No fue posible editar Padecimiento", PadecimientoException.LAYER_DAO, PadecimientoException.ACTION_UPDATE);
         pe.addError("Ocurrió un error al editar Padecimiento");
         logger.error("Error al editar Padecimiento - CODE {} - {}", pe.getExceptionCode(), padecimientoView, pe);
         throw pe;
      } catch (Exception ex) {
         PadecimientoException pe = new PadecimientoException("Error inesperado al editar Padecimiento", PadecimientoException.LAYER_DAO, PadecimientoException.ACTION_UPDATE);
         pe.addError("Ocurrió un error al editar Padecimiento");
         logger.error("Error al editar Padecimiento - CODE {} - {}", pe.getExceptionCode(), padecimientoView, pe);
         throw pe;
      }
   }

   private Boolean existEstudioLaboratorio(Long padecimientoId, Collection<EstudioLaboratorio> extraInfoList) {
      Collection<EstudioLaboratorio> infoList = new ArrayList<>();
      for (EstudioLaboratorio u : extraInfoList)
         if (u.getPadecimiento() != null && u.getPadecimiento().getIdPadecimiento() == (padecimientoId))
            infoList.add(u);
      return !infoList.isEmpty();
   }

//   private Boolean existConsulta(Long padecimientoId, Collection<Consulta> extraInfoList) {
//      Collection<Consulta> infoList = new ArrayList<>();
//      for (Consulta u : extraInfoList)
//         if (u.getPadecimiento() != null && u.getPadecimiento().getIdPadecimiento() == (padecimientoId))
//            infoList.add(u);
//      return !infoList.isEmpty();
//   }

   private Boolean existDocumentos(Long padecimientoId, Collection<Documentos> extraInfoList) {
      Collection<Documentos> infoList = new ArrayList<>();
      for (Documentos u : extraInfoList)
         if (u.getPadecimiento() != null && u.getPadecimiento().getIdPadecimiento() == (padecimientoId))
            infoList.add(u);
      return !infoList.isEmpty();
   }

   @Override/*   public PadecimientoView getDetailsByIdPadecimiento(String idPadecimiento) throws PadecimientoException {*/ public PadecimientoView getDetailsByIdPadecimiento(Long idPadecimiento) throws PadecimientoException {
      try {/*         if (padecimientoRepository.findByIdPaciente(idPadecimiento) == null) {*/
         if (padecimientoRepository.findByIdPadecimiento(idPadecimiento) == null) {
            PadecimientoException pe = new PadecimientoException("No se encuentra en el sistema Padecimiento.", PadecimientoException.LAYER_DAO, PadecimientoException.ACTION_VALIDATE);
            pe.addError("Padecimiento no encontrado");
            throw pe;
         }/*         Padecimiento padecimiento = padecimientoRepository.findByIdPaciente(idPadecimiento);*/
         Padecimiento padecimiento = padecimientoRepository.findByIdPadecimiento(idPadecimiento);
         return padecimientoConverter.toView(padecimiento, Boolean.TRUE);
      } catch (DataIntegrityViolationException dive) {
         PadecimientoException pe = new PadecimientoException("No fue posible editar Padecimiento", PadecimientoException.LAYER_DAO, PadecimientoException.ACTION_SELECT);
         pe.addError("No fue posible obtener detalles Padecimiento");
         logger.error("Error al obtener detalles Padecimiento - CODE {} - {}", pe.getExceptionCode(), idPadecimiento, pe);
         throw pe;
      } catch (Exception ex) {
         PadecimientoException pe = new PadecimientoException("Error inesperado al obtener detalles Padecimiento", PadecimientoException.LAYER_DAO, PadecimientoException.ACTION_SELECT);
         pe.addError("Ocurrió un error al obtener detalles Padecimiento");
         logger.error("Error al obtener detalles Padecimiento - CODE {} - {}", pe.getExceptionCode(), idPadecimiento, pe);
         throw pe;
      }
   }

   @Override
   public PadecimientoView getDetailsByIdPAdecimientoAndIdPaciente(Long idPadecimiento, String idPaciente) throws PadecimientoException {
      try {
         if (padecimientoRepository.findByIdPadecimientoAndIdPaciente(idPadecimiento, idPaciente) == null) {
            PadecimientoException pe = new PadecimientoException("No se encuentra en el sistema Padecimiento.", PadecimientoException.LAYER_DAO, PadecimientoException.ACTION_VALIDATE);
            pe.addError("Padecimiento no encontrado");
            throw pe;
         }
         Padecimiento padecimiento = padecimientoRepository.findByIdPadecimientoAndIdPaciente(idPadecimiento, idPaciente);
         return padecimientoConverter.toView(padecimiento, Boolean.TRUE);
      } catch (DataIntegrityViolationException dive) {
         PadecimientoException pe = new PadecimientoException("No fue posible editar Padecimiento", PadecimientoException.LAYER_DAO, PadecimientoException.ACTION_SELECT);
         pe.addError("No fue posible obtener detalles Padecimiento");
         logger.error("Error al obtener detalles Padecimiento - CODE {} - {}", pe.getExceptionCode(), idPadecimiento, pe);
         throw pe;
      } catch (Exception ex) {
         PadecimientoException pe = new PadecimientoException("Error inesperado al obtener detalles Padecimiento", PadecimientoException.LAYER_DAO, PadecimientoException.ACTION_SELECT);
         pe.addError("Ocurrió un error al obtener detalles Padecimiento");
         logger.error("Error al obtener detalles Padecimiento - CODE {} - {}", pe.getExceptionCode(), idPadecimiento, pe);
         throw pe;
      }
   }

   @Override
   public List<PadecimientoView> findAll() throws PadecimientoException {
      try {
         List<Padecimiento> padecimientoList = new ArrayList<>();
         List<PadecimientoView> padecimientoViewList = new ArrayList<>();
         padecimientoList = padecimientoRepository.findAll();
         for (Padecimiento pad : padecimientoList)
            padecimientoViewList.add(padecimientoConverter.toView(pad, Boolean.TRUE));
         return padecimientoViewList;
      } catch (Exception ex) {
         PadecimientoException pe = new PadecimientoException("Ocurrio un error al seleccionar lista Padecimiento", PadecimientoException.LAYER_SERVICE, PadecimientoException.ACTION_SELECT);
         logger.error(ExceptionServiceCode.GROUP + "- Error al tratar de seleccionar lista Padecimiento - CODE: {}-{}", pe.getExceptionCode(), ex);
         throw pe;
      }
   }

   @Override
   public Page<PadecimientoView> getCursoClinicoSearch(String idPaciente, String datosBusqueda, Integer page, Integer size, String orderColumn, String orderType) throws PadecimientoException {
      try {
         logger.info("- Obtener listado Padecimiento paginable: {} - page {} - size: {} - orderColumn: {} - orderType: {} - active: {}", idPaciente, page, size, orderColumn, orderType);
         List<PadecimientoView> padecimientoViewList = new ArrayList<>();
         Page<Padecimiento> padecimientoPage = null;
         Sort sort = new Sort(Sort.Direction.ASC, (String) colOrderNames.get("fechaCreacion"));
         if (orderColumn != null && orderType != null) if (orderType.equalsIgnoreCase("asc"))
            sort = new Sort(Sort.Direction.ASC, (String) colOrderNames.get(orderColumn));
         else sort = new Sort(Sort.Direction.DESC, (String) colOrderNames.get(orderColumn));
         PageRequest request = new PageRequest(page, size, sort);
         final String patternSearch = "%" + idPaciente.toLowerCase() + "%";
         final String patternSearchDB = "%" + datosBusqueda.toLowerCase() + "%";
         Specifications<Padecimiento> spec = Specifications.where((root, query, cb) -> {
            Predicate tc = null;
            if (idPaciente != null && !idPaciente.isEmpty())
               tc = (tc != null ? cb.and(tc, cb.like(cb.lower(root.get("idPaciente")), patternSearch)) : cb.like(cb.lower(root.get("idPaciente")), patternSearch));
            if (datosBusqueda != null && !datosBusqueda.isEmpty())
               tc = (tc != null ? cb.and(tc, cb.like(cb.function("unaccent", String.class, cb.lower(root.get("datosBusqueda"))), sinAcentos(patternSearchDB))) : cb.like(cb.function("unaccent", String.class, cb.lower(root.get("datosBusqueda"))), sinAcentos(patternSearchDB)));
            return tc;
         });
         if (spec == null) padecimientoPage = padecimientoRepository.findAll(request);
         else padecimientoPage = padecimientoRepository.findAll(spec, request);
         padecimientoPage.getContent().forEach(padecimiento -> {
            padecimientoViewList.add(padecimientoConverter.toView(padecimiento, Boolean.FALSE));
         });
         PageImpl<PadecimientoView> padecimientoViewPage = new PageImpl<>(padecimientoViewList, request, padecimientoPage.getTotalElements());
         return padecimientoViewPage;
      } catch (Exception ex) {
         PadecimientoException pe = new PadecimientoException("Ocurrio un error al seleccionar lista Padecimiento paginable", PadecimientoException.LAYER_SERVICE, PadecimientoException.ACTION_SELECT);
         logger.error(ExceptionServiceCode.GROUP + "- Error al tratar de seleccionar lista Padecimiento paginable - CODE: {}-{}", pe.getExceptionCode(), ex);
         throw pe;
      }
   }


   @Override
   public Page<PadecimientoView> getPadecimientoPage(String name, Integer page, Integer size, String orderColumn, String orderType) throws PadecimientoException {
      try {
         logger.info("- Obtener listado Padecimiento paginable: {} - page {} - size: {} - orderColumn: {} - orderType: {} - active: {}", name, page, size, orderColumn, orderType);
         List<PadecimientoView> padecimientoViewList = new ArrayList<>();
         Page<Padecimiento> padecimientoPage = null;
         Sort sort = new Sort(Sort.Direction.ASC, (String) colOrderNames.get("idPaciente"));

         if ((orderColumn != null && orderColumn.isEmpty()) && orderType != null) {
            if (orderType.equalsIgnoreCase("asc")) {
               sort = new Sort(Sort.Direction.ASC, (String) colOrderNames.get(orderColumn));
            } else {
               sort = new Sort(Sort.Direction.DESC, (String) colOrderNames.get(orderColumn));
            }

         }
         PageRequest request = new PageRequest(page, size, sort);
         final String patternSearch = "%" + name.toLowerCase() + "%";
         Specifications<Padecimiento> spec = Specifications.where(
            (root, query, cb) -> {
               Predicate tc = null;
               if (name != null && !name.isEmpty()) {
                  tc = (tc != null ? cb.and(tc, cb.like(cb.lower(root.get("idPaciente")), patternSearch)) : cb.like(cb.lower(root.get("idPaciente")), patternSearch));
               }
               return tc;
            }
         );

         if (spec == null) {
            padecimientoPage = padecimientoRepository.findAll(request);
         } else {
            padecimientoPage = padecimientoRepository.findAll(spec, request);
         }

         padecimientoPage.getContent().forEach(padecimiento -> {
            padecimientoViewList.add(padecimientoConverter.toView(padecimiento, Boolean.TRUE));
         });
         PageImpl<PadecimientoView> padecimientoViewPage = new PageImpl<PadecimientoView>(padecimientoViewList, request, padecimientoPage.getTotalElements());
         return padecimientoViewPage;
      } catch (Exception ex) {
         PadecimientoException pe = new PadecimientoException("Ocurrio un error al seleccionar lista Padecimiento paginable", PadecimientoException.LAYER_SERVICE, PadecimientoException.ACTION_SELECT);
         logger.error(ExceptionServiceCode.GROUP + "- Error al tratar de seleccionar lista Padecimiento paginable - CODE: {}-{}", pe.getExceptionCode(), ex);
         throw pe;
      }
   }




   private String sinAcentos(String cadena) {
      return Normalizer.normalize(cadena, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
   }

   @Override
   public Page<ConsultaView> listaConsulta(Long idPadecimiento, Integer page, Integer size, String orderColumn, String orderType, String datosBusqueda) throws PadecimientoException {
      try {
         List<ConsultaView> consultaViewList = new ArrayList<>();
         Page<Object[]> consultaPage = null;
         if(idPadecimiento==null) {
        	 PadecimientoException pe = new PadecimientoException("No se puedo obtener las consultas.", PadecimientoException.LAYER_DAO, PadecimientoException.ACTION_VALIDATE);
             pe.addError("La propiedad: idPadecimiento viene nulo");
             throw pe;
         }
         
         Sort sort = new Sort(Sort.Direction.ASC, (String) colOrderNames.get("idConsulta"));
         if (orderColumn != null  && orderType != null) {
        	 if (orderType.equalsIgnoreCase("asc")) {
        		 sort = new Sort(Sort.Direction.ASC, (String) colOrderNames.get(orderColumn));
        	 }else {
        		 sort = new Sort(Sort.Direction.DESC, (String) colOrderNames.get(orderColumn));
        	 }
         }
         PageRequest request = new PageRequest(page, size, sort);
         if (datosBusqueda.isEmpty() || datosBusqueda == null) {
        	 datosBusqueda = "%%";
         }else {
        	 datosBusqueda = "%" + datosBusqueda.toLowerCase() + "%";
         }
         consultaPage = consultaRepository.findByIdPacienteAndIdPadecimiento(idPadecimiento, datosBusqueda, request);
         
         consultaPage.getContent().forEach((consultaV) -> {
        	Consulta c= new Consulta();
        	c.setIdConsulta(((BigInteger)consultaV[0]).longValue());
        	c.setEstadoConsulta((String)consultaV[1]);
        	c.setFechaConsulta((Date)consultaV[2]);
        	c.setResumen((String)consultaV[3]);
        	c.setNombreMedico((String)consultaV[4]);
        	
        	
            consultaViewList.add(consultaConverter.toView(c, Boolean.FALSE));
         });
         PageImpl<ConsultaView> consultaViewPage = new PageImpl<>(consultaViewList, request, consultaPage.getTotalElements());
         return consultaViewPage;
      } catch (PadecimientoException pe) {
         throw pe;
      } catch (Exception ce) {
         PadecimientoException pe = new PadecimientoException("Ocurrio un error al seleccionar lista Padecimiento por Consulta", PadecimientoException.LAYER_SERVICE, PadecimientoException.ACTION_SELECT);
         logger.error(ExceptionServiceCode.GROUP + "- Error al tratar de seleccionar lista Padecimiento paginable - CODE: {}-{}", pe.getExceptionCode(), ce);
         throw pe;
      }
   }

   @Override
   @Transactional(rollbackFor = {PadecimientoException.class})
   public void updateStatusPadecimiento(Long idPadecimiento) throws PadecimientoException {
      try {
         Padecimiento padecimiento = padecimientoRepository.findByIdPadecimiento(idPadecimiento);
         if(padecimiento == null) {
            PadecimientoException exception = new PadecimientoException("Ocurrió un error al seleccionar el Padecimiento por Consulta", PadecimientoException.LAYER_SERVICE, PadecimientoException.ACTION_SELECT);
            exception.addError("Padecimiento no encontrado");
            logger.error("Padecimiento no encontrado");
            throw exception;
         }
         padecimiento.setEstatus(false);
         padecimiento.setFechaAlta(new Date());
         padecimientoRepository.save(padecimiento);
      } catch(PadecimientoException pe) {
         throw pe;
      } catch(Exception ce) {
         PadecimientoException pe = new PadecimientoException("Ocurrió un error al actualizar el Padecimiento por Consulta", PadecimientoException.LAYER_SERVICE, PadecimientoException.ACTION_UPDATE);
         pe.addError("Error al actualizar el padecimiento " + idPadecimiento);
         logger.error("Error al actualizar el padecimiento {}", idPadecimiento);
         ce.printStackTrace();
         throw pe;
      }
   }

}