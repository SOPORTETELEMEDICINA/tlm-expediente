package net.amentum.niomedic.expediente.service.impl;

import net.amentum.niomedic.expediente.configuration.ApiConfiguration;
import net.amentum.niomedic.expediente.converter.EstudioLaboratorioConverter;
import net.amentum.niomedic.expediente.exception.EstudioLaboratorioException;
import net.amentum.niomedic.expediente.exception.ExceptionServiceCode;
import net.amentum.niomedic.expediente.model.EstudioLaboratorio;
import net.amentum.niomedic.expediente.persistence.EstudioLaboratorioRepository;
import net.amentum.niomedic.expediente.service.EstudioLaboratorioService;
import net.amentum.niomedic.expediente.views.EstudioLaboratorioListView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@Transactional(readOnly = true)
public class EstudioLaboratorioServiceImpl implements EstudioLaboratorioService {
   private final Logger logger = LoggerFactory.getLogger(EstudioLaboratorioServiceImpl.class);
   private final Map<String, Object> colOrderNames = new HashMap<>();
   private EstudioLaboratorioRepository estudioLaboratorioRepository;
   private EstudioLaboratorioConverter estudioLaboratorioConverter;
   private ApiConfiguration apiConfiguration;

   {
      colOrderNames.put("consultaId", "consultaId");
      colOrderNames.put("fechaCreacion", "fechaCreacion");
      colOrderNames.put("idPaciente", "idPaciente");
   }

   @Autowired
   public void setEstudioLaboratorioRepository(EstudioLaboratorioRepository estudioLaboratorioRepository) {
      this.estudioLaboratorioRepository = estudioLaboratorioRepository;
   }

   @Autowired
   public void setEstudioLaboratorioConverter(EstudioLaboratorioConverter estudioLaboratorioConverter) {
      this.estudioLaboratorioConverter = estudioLaboratorioConverter;
   }

   @Autowired
   public void setApiConfiguration(ApiConfiguration apiConfiguration) {
      this.apiConfiguration = apiConfiguration;
   }


   @Override
//    public Page<EstudioLaboratorioListView> getEstudioLaboratorioPage(Boolean active, String name, Integer page, Integer size, String orderColumn, String orderType, String idPaciente) throws EstudioLaboratorioException {
   public Page<EstudioLaboratorioListView> getEstudioLaboratorioPage(Boolean active, String name, Integer page, Integer size, String orderColumn, String orderType) throws EstudioLaboratorioException {
      try {
         logger.info("- Obtener listado de EstudioLaboratorio paginable: {} - page {} - size: {} - orderColumn: {} - orderType: {} - active: {}", name, page, size, orderColumn, orderType, active);
//            List<EstudioLaboratorioView> estudioLaboratorioViewList = new ArrayList<>();
         List<EstudioLaboratorioListView> estudioLaboratorioViewList = new ArrayList<>();
         Page<EstudioLaboratorio> estudioLaboratorioPage = null;
         Sort sort = new Sort(Sort.Direction.ASC, (String) colOrderNames.get("idPaciente"));

         if (orderColumn != null && orderType != null) {
            if (orderType.equalsIgnoreCase("asc")) {
               sort = new Sort(Sort.Direction.ASC, (String) colOrderNames.get(orderColumn));
            } else {
               sort = new Sort(Sort.Direction.DESC, (String) colOrderNames.get(orderColumn));
            }

         }
         PageRequest request = new PageRequest(page, size, sort);
         final String patternSearch = "%" + name.toLowerCase() + "%";
         Specifications<EstudioLaboratorio> spec = Specifications.where(
            (root, query, cb) -> {
               Predicate tc = null;
               if (name != null && !name.isEmpty()) {
                  tc = (tc != null ? cb.and(tc, cb.like(cb.lower(root.get("idPaciente")), patternSearch)) : cb.like(cb.lower(root.get("idPaciente")), patternSearch));
               }
               if (active != null) {
                  tc = (tc != null ? cb.and(tc, cb.equal(root.get("activo"), active)) : cb.equal(root.get("activo"), active));
               }
               return tc;
            }
         );

         if (spec == null) {
            estudioLaboratorioPage = estudioLaboratorioRepository.findAll(request);

         } else {
            estudioLaboratorioPage = estudioLaboratorioRepository.findAll(spec, request);
         }

         estudioLaboratorioPage.getContent().forEach(estudioLaboratorio -> {
            // estudioLaboratorioViewList.add(estudioLaboratorioConverter.toViewPage(estudioLaboratorio));
            estudioLaboratorioViewList.add(estudioLaboratorioConverter.toView(estudioLaboratorio, Boolean.TRUE));
         });
//            PageImpl<EstudioLaboratorioView> estudioLaboratorioViewPage = new PageImpl<EstudioLaboratorioView>(estudioLaboratorioViewList, request, estudioLaboratorioPage.getTotalElements());
         PageImpl<EstudioLaboratorioListView> estudioLaboratorioViewPage = new PageImpl<EstudioLaboratorioListView>(estudioLaboratorioViewList, request, estudioLaboratorioPage.getTotalElements());
         return estudioLaboratorioViewPage;
      } catch (Exception ex) {
         EstudioLaboratorioException ele = new EstudioLaboratorioException("Ocurrio un error al seleccionar lista EstudioLaboratorio paginable", EstudioLaboratorioException.LAYER_SERVICE, EstudioLaboratorioException.ACTION_SELECT);
         logger.error(ExceptionServiceCode.GROUP + "- Error al tratar de seleccionar lista EstudioLaboratorio paginable - CODE: {}-{}", ele.getExceptionCode(), ex);
         throw ele;
      }
   }


   @Override
   public List<List<Map<String, Object>>> getEstudioLaboratorioPorPadecimiento(Long idPadecimiento) throws EstudioLaboratorioException {
      try {
         List<BigInteger> lista = estudioLaboratorioRepository.obtenerIdConsultasByIdPadecimientos(idPadecimiento);
//         System.out.println("===>>>" + lista);

         if (lista == null || lista.isEmpty()) {
            logger.info("getEstudioLaboratorioPorPadecimiento() - No se encontro el Estudio con el idPadecimiento: {}", idPadecimiento);
            EstudioLaboratorioException el = new EstudioLaboratorioException("Ocurrio un error al obtener los estudio laboratorio", EstudioLaboratorioException.LAYER_SERVICE, EstudioLaboratorioException.ACTION_SELECT);
            el.addError("No se encontro el Estudio con el idPadecimiento:" + idPadecimiento);
            throw el;
         }

         List<List<Map<String, Object>>> estudioList = new ArrayList<>();

         for (BigInteger temp : lista) {
            List<Map<String, Object>> estudioX = apiConfiguration.getEstudioByIdConsulta(temp.longValue());
               estudioList.add(estudioX);
         }
         return estudioList;
      } catch (EstudioLaboratorioException el) {
         throw el;
      } catch (Exception ex) {
         logger.info("getEstudioLaboratorioPorPadecimiento() - idPadecimiento: {} - error: {}", idPadecimiento, ex.getMessage());
         EstudioLaboratorioException el = new EstudioLaboratorioException("Ocurrio un error al obtener los estudio laboratorio", EstudioLaboratorioException.LAYER_SERVICE, EstudioLaboratorioException.ACTION_SELECT);
         el.addError(ex.getMessage());
         throw el;
      }
   }
}
