package net.amentum.niomedic.expediente.service.impl;


import lombok.extern.slf4j.Slf4j;
import net.amentum.niomedic.expediente.converter.CatCie10Converter;
import net.amentum.niomedic.expediente.converter.CatCie10FiltradoConverter;
import net.amentum.niomedic.expediente.exception.CatCie10Exception;
import net.amentum.niomedic.expediente.model.CatCie10;
import net.amentum.niomedic.expediente.persistence.CatCie10Repository;
import net.amentum.niomedic.expediente.service.CatCie10Service;
import net.amentum.niomedic.expediente.views.CatCie10FiltradoView;
import net.amentum.niomedic.expediente.views.CatCie10View;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@Transactional(readOnly = true)
@Slf4j
public class CatCie10ServiceImpl implements CatCie10Service {
//   private final Logger logger = LoggerFactory.getLogger(CatCie10ServiceImpl.class);

   private final Map<String, Object> colOrderNames = new HashMap<>();
   private CatCie10Repository catCie10Repository;
   private CatCie10Converter catCie10Converter;
   private CatCie10FiltradoConverter catCie10FiltradoConverter;

   {
      colOrderNames.put("idCie10", "idCie10");
      colOrderNames.put("catalogKey", "catalogKey");
      colOrderNames.put("nombre", "nombre");
      colOrderNames.put("descripcionCapitulo", "descripcionCapitulo");
   }

   @Autowired
   public void setCatCie10Repository(CatCie10Repository catCie10Repository) {
      this.catCie10Repository = catCie10Repository;
   }

   @Autowired
   public void setCatCie10Converter(CatCie10Converter catCie10Converter) {
      this.catCie10Converter = catCie10Converter;
   }

   @Autowired
   public void setCatCie10FiltradoConverter(CatCie10FiltradoConverter catCie10FiltradoConverter) {
      this.catCie10FiltradoConverter = catCie10FiltradoConverter;
   }

   private CatCie10 exists(Long idCatCie10) throws CatCie10Exception {
      log.info("===>>>exists() - revisando si existe el cie10 por id: {}", idCatCie10);
      if (!catCie10Repository.exists(idCatCie10)) {
         log.error("===>>>exists() - No se encuentra el idCatCie10: {}", idCatCie10);
         throw new CatCie10Exception(HttpStatus.NOT_FOUND, String.format(CatCie10Exception.ITEM_NOT_FOUND, idCatCie10));
      }
      return catCie10Repository.findOne(idCatCie10);
   }


   @Override
   public CatCie10View getDetailsByIdCie10(Long idCatCie10) throws CatCie10Exception {
      try {
         CatCie10 catCie10 = exists(idCatCie10);
         log.info("===>>>getDetailsByIdCie10() - {}", catCie10);
         return catCie10Converter.toView(catCie10, Boolean.TRUE);
      } catch (CatCie10Exception cie10e) {
         throw cie10e;
      } catch (Exception ex) {
         log.error("Error al obtener registro - error: {}", ex);
         throw new CatCie10Exception(HttpStatus.INTERNAL_SERVER_ERROR, String.format(CatCie10Exception.SERVER_ERROR, "obtener por ID"));
      }
   }

   @Override
   public List<CatCie10FiltradoView> findAll() throws CatCie10Exception {
      try {
         List<CatCie10> catCie10List = catCie10Repository.findAll();
         List<CatCie10FiltradoView> catCie10FiltradoViewList = new ArrayList<>();
         for (CatCie10 cpl : catCie10List) {
            catCie10FiltradoViewList.add(catCie10FiltradoConverter.toView(cpl, Boolean.TRUE));
         }
         return catCie10FiltradoViewList;
      } catch (Exception ex) {
         log.error("Error al obtener todos los registros - error: {}", ex);
         throw new CatCie10Exception(HttpStatus.INTERNAL_SERVER_ERROR, "No fue posible obtener todos los registros");
      }
   }

   @Override
   public Page<CatCie10FiltradoView> getCatCie10Search(String datosBusqueda, Boolean activo, Integer page, Integer size, String orderColumn, String orderType, String sexo, Integer edad) throws CatCie10Exception {
      try {
         log.info("===>>>getCatCie10Search(): datosBusqueda: {} - activo: {} - page: {} - size: {} - orderColumn: {} - orderType: {}",
            datosBusqueda, activo, page, size, orderColumn, orderType);

         if (!colOrderNames.containsKey(orderColumn)) {
            orderColumn = "idCie10";
         }

         List<CatCie10FiltradoView> catCie10FiltradoViewList = new ArrayList<>();
         Page<CatCie10> catCie10Page = null;
         log.info("===>>>getCatCie10Page() - creando SORT");
         Sort sort = new Sort(Sort.Direction.ASC, (String) colOrderNames.get(orderColumn));

         if (orderType.equalsIgnoreCase("asc")) {
            sort = new Sort(Sort.Direction.ASC, (String) colOrderNames.get(orderColumn));
         } else {
            sort = new Sort(Sort.Direction.DESC, (String) colOrderNames.get(orderColumn));
         }

         PageRequest request = new PageRequest(page, size, sort);
         final String patternSearch = "%" + datosBusqueda.toLowerCase() + "%";
         Specifications<CatCie10> spec = Specifications.where(
            (root, query, cb) -> {
               Predicate tc = null;
               if (!datosBusqueda.isEmpty()) {
                  tc = cb.like(cb.function("unaccent", String.class, cb.lower(root.get("datosBusqueda"))), sinAcentos(patternSearch));
               }
               if (activo != null) {
                  tc = (tc != null ? cb.and(tc, cb.equal(root.get("activo"), activo)) : cb.equal(root.get("activo"), activo));
               }
               return tc;
            }
         );

         log.info("===>>>getCatCie10Page() - realizando la búsqueda");
         catCie10Page = catCie10Repository.findAll(spec, request);

         catCie10Page.getContent().forEach(catCie10 -> {
            if (catCie10.getLsex() == sexo) {
               Integer edadMinima = Integer.parseInt(catCie10.getLinf().replaceAll("[^0-9]", ""));
               Integer edadMaxima = Integer.parseInt(catCie10.getLsup().replaceAll("[^0-9]", ""));

               if (edad >= edadMinima && edad <= edadMaxima) {
                  catCie10FiltradoViewList.add(catCie10FiltradoConverter.toView(catCie10, Boolean.TRUE));
               }
            }
         });

         PageImpl<CatCie10FiltradoView> catCie10FiltradoViewPage = new PageImpl<CatCie10FiltradoView>(catCie10FiltradoViewList, request, catCie10Page.getTotalElements());
         return catCie10FiltradoViewPage;

      } catch (IllegalArgumentException iae) {
         log.error("===>>>Algún parámetro no es correcto: {}", iae);
         throw new CatCie10Exception(HttpStatus.BAD_REQUEST, "Algún parámetro es null, vacío o incorrecto");
      } catch (Exception ex) {
         log.error("Error al obtener la lista paginable - error: {}", ex);
         throw new CatCie10Exception(HttpStatus.INTERNAL_SERVER_ERROR, "No fue posible obtener obtener la lista paginable");
      }
   }

   private String sinAcentos(String cadena) {
      return Normalizer.normalize(cadena, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
   }

   @Transactional(readOnly = false, rollbackFor = {CatCie10Exception.class})
   @Override
   public CatCie10View createCatCie10(CatCie10View catCie10View) throws CatCie10Exception {
      try {

         CatCie10 catCie10 = catCie10Converter.toEntity(catCie10View, new CatCie10(), Boolean.FALSE);
         log.info("===>>>Insertar nuevo catCie10: {}", catCie10);
         catCie10Repository.save(catCie10);
         return catCie10Converter.toView(catCie10, Boolean.TRUE);

      } catch (ConstraintViolationException cve) {
         log.error("===>>>createCatCie10() - Ocurrió un error al validar algunos campos - error :{}", cve);
         throw new CatCie10Exception(HttpStatus.BAD_REQUEST, String.format(CatCie10Exception.SERVER_ERROR, "Crear"));
      } catch (DataIntegrityViolationException dive) {
//         log.error("===>>>createCatCie10() - Ocurrió un error de integridad - error :{}", dive.getMessage());
         log.error("===>>>createCatCie10() - Ocurrió un error de integridad - error :{}", dive);
//         throw new CatCie10Exception(HttpStatus.CONFLICT, dive.getCause().getMessage());
         throw new CatCie10Exception(HttpStatus.CONFLICT, dive.getMessage());
      } catch (Exception ex) {
         log.error("===>>>createCatCie10() -  Ocurrio un error inesperado - error:{}", ex);
         throw new CatCie10Exception(HttpStatus.INTERNAL_SERVER_ERROR, String.format(CatCie10Exception.SERVER_ERROR, "Crear"));
      }
   }

   @Transactional(readOnly = false, rollbackFor = {CatCie10Exception.class})
   @Override
   public CatCie10View updateCatCie10(CatCie10View catCie10View) throws CatCie10Exception {
      try {

         CatCie10 catCie10 = exists(catCie10View.getIdCie10());
         catCie10 = catCie10Converter.toEntity(catCie10View, catCie10, Boolean.TRUE);
         log.info("===>>>updateCatCie10() - Editar catCie10: {}", catCie10);
         catCie10Repository.save(catCie10);
         return catCie10Converter.toView(catCie10, Boolean.TRUE);

      } catch (CatCie10Exception cie10e) {
         throw cie10e;
      } catch (ConstraintViolationException cve) {
         log.error("===>>>updateCatCie10() - Ocurrió un error al validar algunos campos - error :{}", cve);
         throw new CatCie10Exception(HttpStatus.BAD_REQUEST, String.format(CatCie10Exception.SERVER_ERROR, "Crear"));
      } catch (DataIntegrityViolationException dive) {
         log.error("===>>>updateCatCie10() - Ocurrió un error de integridad - error :{}", dive);
         throw new CatCie10Exception(HttpStatus.CONFLICT, dive.getMessage());
      } catch (Exception ex) {
         log.error("===>>>updateCatCie10() -  Ocurrio un error inesperado - error:{}", ex);
         throw new CatCie10Exception(HttpStatus.INTERNAL_SERVER_ERROR, String.format(CatCie10Exception.SERVER_ERROR, "Crear"));
      }
   }

}

