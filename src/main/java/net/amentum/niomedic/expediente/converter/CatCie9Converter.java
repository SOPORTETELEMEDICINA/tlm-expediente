package net.amentum.niomedic.expediente.converter;

import net.amentum.niomedic.expediente.model.CatCie9;
import net.amentum.niomedic.expediente.views.CatCie9View;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CatCie9Converter {
   private final Logger logger = LoggerFactory.getLogger(CatCie9Converter.class);

   public CatCie9 toEntity(CatCie9View catCie9View, CatCie9 catCie9, Boolean update) {
      catCie9.setCatalogKey(catCie9View.getCatalogKey().toUpperCase());
      catCie9.setProNombre(catCie9View.getProNombre().toUpperCase());
      catCie9.setProCveEdia(catCie9View.getProCveEdia().toUpperCase());
      catCie9.setProEdadIa(catCie9View.getProEdadIa().toUpperCase());
      catCie9.setProCveEdfa(catCie9View.getProCveEdfa().toUpperCase().toUpperCase());
      catCie9.setProEdadFa(catCie9View.getProEdadFa().toUpperCase());
      catCie9.setSexType(catCie9View.getSexType().toUpperCase());
      catCie9.setPorNivela(catCie9View.getPorNivela().toUpperCase());
      catCie9.setProcedimientoType(catCie9View.getProcedimientoType().toUpperCase());
      catCie9.setProPrincipal(catCie9View.getProPrincipal().toUpperCase());
      catCie9.setProCapitulo(catCie9View.getProCapitulo().toUpperCase());
      catCie9.setProSeccion(catCie9View.getProSeccion().toUpperCase());
      catCie9.setProCategoria(catCie9View.getProCategoria().toUpperCase());
      catCie9.setProSubcateg(catCie9View.getProSubcateg().toUpperCase());
      catCie9.setProGrupoLc(catCie9View.getProGrupoLc().toUpperCase());
      catCie9.setProEsCauses(catCie9View.getProEsCauses().toUpperCase());
      catCie9.setProNumCauses(catCie9View.getProNumCauses().toUpperCase());
      catCie9.setActivo(catCie9View.getActivo());
      catCie9.setDatosBusqueda(catCie9View.getCatalogKey().toUpperCase() + " " + catCie9View.getProNombre().toUpperCase());

      logger.debug("convertir CatCie9View to Entity: {}", catCie9);
      return catCie9;
   }

   public CatCie9View toView(CatCie9 catCie9, Boolean complete) {
      CatCie9View catCie9View = new CatCie9View();
      catCie9View.setIdCie9(catCie9.getIdCie9());
      catCie9View.setCatalogKey(catCie9.getCatalogKey());
      catCie9View.setProNombre(catCie9.getProNombre());
      catCie9View.setProCveEdia(catCie9.getProCveEdia());
      catCie9View.setProEdadIa(catCie9.getProEdadIa());
      catCie9View.setProCveEdfa(catCie9.getProCveEdfa());
      catCie9View.setProEdadFa(catCie9.getProEdadFa());
      catCie9View.setSexType(catCie9.getSexType());
      catCie9View.setPorNivela(catCie9.getPorNivela());
      catCie9View.setProcedimientoType(catCie9.getProcedimientoType());
      catCie9View.setProPrincipal(catCie9.getProPrincipal());
      catCie9View.setProCapitulo(catCie9.getProCapitulo());
      catCie9View.setProSeccion(catCie9.getProSeccion());
      catCie9View.setProCategoria(catCie9.getProCategoria());
      catCie9View.setProSubcateg(catCie9.getProSubcateg());
      catCie9View.setProGrupoLc(catCie9.getProGrupoLc());
      catCie9View.setProEsCauses(catCie9.getProEsCauses());
      catCie9View.setProNumCauses(catCie9.getProNumCauses());
      catCie9View.setActivo(catCie9.getActivo());

      logger.debug("convertir CatCie9 to View: {}", catCie9View);
      return catCie9View;
   }
}
