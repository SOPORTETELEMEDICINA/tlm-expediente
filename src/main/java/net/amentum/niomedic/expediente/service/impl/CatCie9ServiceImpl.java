package net.amentum.niomedic.expediente.service.impl;


import lombok.extern.slf4j.Slf4j;
import net.amentum.niomedic.expediente.converter.CatCie9Converter;
import net.amentum.niomedic.expediente.converter.CatCie9FiltradoConverter;
import net.amentum.niomedic.expediente.exception.CatCie9Exception;
import net.amentum.niomedic.expediente.model.CatCie9;
import net.amentum.niomedic.expediente.persistence.CatCie9Repository;
import net.amentum.niomedic.expediente.service.CatCie9Service;
import net.amentum.niomedic.expediente.views.CatCie9FiltradoView;
import net.amentum.niomedic.expediente.views.CatCie9View;
import org.apache.tomcat.util.descriptor.web.SecurityRoleRef;
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
import java.util.*;


@Service
@Transactional(readOnly = true)
@Slf4j
public class CatCie9ServiceImpl implements CatCie9Service {
//   private final Logger logger = LoggerFactory.getLogger(CatCie9ServiceImpl.class);

   private final Map<String, Object> colOrderNames = new HashMap<>();
   private CatCie9Repository catCie9Repository;
   private CatCie9Converter catCie9Converter;
   private CatCie9FiltradoConverter catCie9FiltradoConverter;

   {
      colOrderNames.put("idCie9", "idCie9");
      colOrderNames.put("catalogKey", "catalogKey");
      colOrderNames.put("proNombre", "proNombre");
      colOrderNames.put("procedimientoType", "procedimientoType");
   }

   @Autowired
   public void setCatCie9Repository(CatCie9Repository catCie9Repository) {
      this.catCie9Repository = catCie9Repository;
   }

   @Autowired
   public void setCatCie9Converter(CatCie9Converter catCie9Converter) {
      this.catCie9Converter = catCie9Converter;
   }

   @Autowired
   public void setCatCie9FiltradoConverter(CatCie9FiltradoConverter catCie9FiltradoConverter) {
      this.catCie9FiltradoConverter = catCie9FiltradoConverter;
   }

   private CatCie9 exists(Long idCatCie9) throws CatCie9Exception {
      log.info("===>>>exists() - revisando si existe el cie9 por id: {}", idCatCie9);
      if (!catCie9Repository.exists(idCatCie9)) {
         log.error("===>>>exists() - No se encuentra el idCatCie9: {}", idCatCie9);
         throw new CatCie9Exception(HttpStatus.NOT_FOUND, String.format(CatCie9Exception.ITEM_NOT_FOUND, idCatCie9));
      }
      return catCie9Repository.findOne(idCatCie9);
   }


   @Override
   public CatCie9View getDetailsByIdCie9(Long idCatCie9) throws CatCie9Exception {
      try {
         CatCie9 catCie9 = exists(idCatCie9);
         log.info("===>>>getDetailsByIdCie9() - {}", catCie9);
         return catCie9Converter.toView(catCie9, Boolean.TRUE);
      } catch (CatCie9Exception cie9e) {
         throw cie9e;
      } catch (Exception ex) {
         log.error("Error al obtener registro - error: {}", ex);
         throw new CatCie9Exception(HttpStatus.INTERNAL_SERVER_ERROR, String.format(CatCie9Exception.SERVER_ERROR, "obtener por ID"));
      }
   }

   @Override
   public List<CatCie9FiltradoView> findAll() throws CatCie9Exception {
      try {
         List<CatCie9> catCie9List = catCie9Repository.findAll();
         List<CatCie9FiltradoView> catCie9FiltradoViewList = new ArrayList<>();
         for (CatCie9 cpl : catCie9List) {
            catCie9FiltradoViewList.add(catCie9FiltradoConverter.toView(cpl, Boolean.TRUE));
         }
         return catCie9FiltradoViewList;
      } catch (Exception ex) {
         log.error("Error al obtener todos los registros - error: {}", ex);
         throw new CatCie9Exception(HttpStatus.INTERNAL_SERVER_ERROR, "No fue posible obtener todos los registros");
      }
   }

   @Override
   public Page<CatCie9FiltradoView> getCatCie9Search(String datosBusqueda, Boolean activo, Integer page, Integer size, String orderColumn, String orderType, String sexo, Integer edad) throws CatCie9Exception {
      try {
         log.info("===>>>getCatCie9Search(): datosBusqueda: {} - activo: {} - page: {} - size: {} - orderColumn: {} - orderType: {}",
            datosBusqueda, activo, page, size, orderColumn, orderType);

         if (!colOrderNames.containsKey(orderColumn)) {
            orderColumn = "idCie9";
         }

         List<CatCie9FiltradoView> catCie9FiltradoViewList = new ArrayList<>();
         Page<CatCie9> catCie9Page = null;
         log.info("===>>>getCatCie9Page() - creando SORT");
         Sort sort = new Sort(Sort.Direction.ASC, (String) colOrderNames.get(orderColumn));

         if (orderType.equalsIgnoreCase("asc")) {
            sort = new Sort(Sort.Direction.ASC, (String) colOrderNames.get(orderColumn));
         } else {
            sort = new Sort(Sort.Direction.DESC, (String) colOrderNames.get(orderColumn));
         }

         PageRequest request = new PageRequest(page, size, sort);
         final String patternSearch = "%" + datosBusqueda.toLowerCase() + "%";
         Specifications<CatCie9> spec = Specifications.where(
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

         log.info("===>>>getCatCie9Page() - realizando la búsqueda");
         catCie9Page = catCie9Repository.findAll(spec, request);

         catCie9Page.getContent().forEach(catCie9 -> {
            boolean coincideSexo = true;
            boolean coincideEdad = true;

            // Verificar restricción de sexo
            if (sexo != null && !sexo.isEmpty()) {
               String sexType = catCie9.getSexType();
               System.err.println("Sexo ingresado: " + sexo);
               System.err.println("Sexo del registro: " + sexType);

               if (sexType != null && !sexType.isEmpty()) {
                  if ((Objects.equals(sexType, "2.0") && Objects.equals(sexo.toUpperCase(), "MUJER")) ||
                          (Objects.equals(sexType, "1.0") && Objects.equals(sexo.toUpperCase(), "HOMBRE"))) {
                     coincideSexo = true;
                  } else {
                     coincideSexo = false;
                  }
               }
               // Si sexType es null, vacío o "0", lo consideramos sin restricción y coincideSexo permanece true
            }

            // Verificar restricción de edad
            if (edad != null && edad != 0) {
               String proEdadIa = catCie9.getProEdadIa();
               String proEdadFa = catCie9.getProEdadFa();

               if (proEdadIa != null && !proEdadIa.isEmpty() && !"0".equals(proEdadIa) &&
                       proEdadFa != null && !proEdadFa.isEmpty() && !"0".equals(proEdadFa)) {

                  Integer edadMinima = Integer.parseInt(proEdadIa.replaceAll("[^0-9]", ""));
                  Integer edadMaxima = Integer.parseInt(proEdadFa.replaceAll("[^0-9]", ""));

                  System.err.println("Edad mínima: " + edadMinima);
                  System.err.println("Edad máxima: " + edadMaxima);

                  coincideEdad = edad >= edadMinima && edad <= edadMaxima;
               }
               // Si proEdadIa o proEdadFa son null, están vacíos o son "0", lo consideramos sin restricción y coincideEdad permanece true
            }

            // Agregar a la lista si cumple las condiciones de sexo y edad
            if (coincideSexo && coincideEdad) {
               catCie9FiltradoViewList.add(catCie9FiltradoConverter.toView(catCie9, Boolean.TRUE));
            }
         });

         PageImpl<CatCie9FiltradoView> catCie9FiltradoViewPage = new PageImpl<CatCie9FiltradoView>(catCie9FiltradoViewList, request, catCie9Page.getTotalElements());
         return catCie9FiltradoViewPage;

      } catch (IllegalArgumentException iae) {
         log.error("===>>>Algún parámetro no es correcto: {}", iae);
         throw new CatCie9Exception(HttpStatus.BAD_REQUEST, "Algún parámetro es null, vacío o incorrecto");
      } catch (Exception ex) {
         log.error("Error al obtener la lista paginable - error: {}", ex);
         throw new CatCie9Exception(HttpStatus.INTERNAL_SERVER_ERROR, "No fue posible obtener obtener la lista paginable");
      }
   }

   private String sinAcentos(String cadena) {
      return Normalizer.normalize(cadena, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
   }

   @Transactional(readOnly = false, rollbackFor = {CatCie9Exception.class})
   @Override
   public CatCie9View createCatCie9(CatCie9View catCie9View) throws CatCie9Exception {
      try {

         CatCie9 catCie9 = catCie9Converter.toEntity(catCie9View, new CatCie9(), Boolean.FALSE);
         log.info("===>>>Insertar nuevo catCie9: {}", catCie9);
         catCie9Repository.save(catCie9);
         return catCie9Converter.toView(catCie9, Boolean.TRUE);

      } catch (ConstraintViolationException cve) {
         log.error("===>>>createCatCie9() - Ocurrió un error al validar algunos campos - error :{}", cve);
         throw new CatCie9Exception(HttpStatus.BAD_REQUEST, String.format(CatCie9Exception.SERVER_ERROR, "Crear"));
      } catch (DataIntegrityViolationException dive) {
         log.error("===>>>createCatCie9() - Ocurrió un error de integridad - error :{}", dive);
//         throw new CatCie9Exception(HttpStatus.CONFLICT, dive.getCause().getMessage());
         throw new CatCie9Exception(HttpStatus.CONFLICT, dive.getMessage());
      } catch (Exception ex) {
         log.error("===>>>createCatCie9() -  Ocurrio un error inesperado - error:{}", ex);
         throw new CatCie9Exception(HttpStatus.INTERNAL_SERVER_ERROR, String.format(CatCie9Exception.SERVER_ERROR, "Crear"));
      }
   }

   @Transactional(readOnly = false, rollbackFor = {CatCie9Exception.class})
   @Override
   public CatCie9View updateCatCie9(CatCie9View catCie9View) throws CatCie9Exception {
      try {

         CatCie9 catCie9 = exists(catCie9View.getIdCie9());
         catCie9 = catCie9Converter.toEntity(catCie9View, catCie9, Boolean.TRUE);
         log.info("===>>>updateCatCie9() - Editar catCie9: {}", catCie9);
         catCie9Repository.save(catCie9);
         return catCie9Converter.toView(catCie9, Boolean.TRUE);

      } catch (CatCie9Exception cie9e) {
         throw cie9e;
      } catch (ConstraintViolationException cve) {
         log.error("===>>>updateCatCie9() - Ocurrió un error al validar algunos campos - error :{}", cve);
         throw new CatCie9Exception(HttpStatus.BAD_REQUEST, String.format(CatCie9Exception.SERVER_ERROR, "Crear"));
      } catch (DataIntegrityViolationException dive) {
         log.error("===>>>updateCatCie9() - Ocurrió un error de integridad - error :{}", dive);
//         throw new CatCie9Exception(HttpStatus.CONFLICT, dive.getCause().getMessage());
         throw new CatCie9Exception(HttpStatus.CONFLICT, dive.getMessage());
      } catch (Exception ex) {
         log.error("===>>>updateCatCie9() -  Ocurrio un error inesperado - error:{}", ex);
         throw new CatCie9Exception(HttpStatus.INTERNAL_SERVER_ERROR, String.format(CatCie9Exception.SERVER_ERROR, "Crear"));
      }
   }
}

