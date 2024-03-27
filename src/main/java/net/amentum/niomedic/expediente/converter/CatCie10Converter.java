package net.amentum.niomedic.expediente.converter;

import net.amentum.niomedic.expediente.model.CatCie10;
import net.amentum.niomedic.expediente.views.CatCie10View;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CatCie10Converter {
   private final Logger logger = LoggerFactory.getLogger(CatCie10Converter.class);
   /*
   no hay toEntity porque no se añade nada al catalogo
    */

   public CatCie10 toEntity(CatCie10View catCie10View, CatCie10 catCie10, Boolean update){
      catCie10.setLetra(catCie10View.getLetra().toUpperCase());
      catCie10.setCatalogKey(catCie10View.getCatalogKey().toUpperCase());
      catCie10.setAsterisco(catCie10View.getAsterisco().toUpperCase());
      catCie10.setNombre(catCie10View.getNombre().toUpperCase());
      catCie10.setLsex(catCie10View.getLsex().toUpperCase());
      catCie10.setLinf(catCie10View.getLinf().toUpperCase());
      catCie10.setLsup(catCie10View.getLsup().toUpperCase());
      catCie10.setTrivial(catCie10View.getTrivial().toUpperCase());
      catCie10.setErradicado(catCie10View.getErradicado().toUpperCase());
      catCie10.setNInter(catCie10View.getNInter().toUpperCase());
      catCie10.setNin(catCie10View.getNin().toUpperCase());
      catCie10.setNinmtobs(catCie10View.getNinmtobs().toUpperCase());
      catCie10.setNoCbd(catCie10View.getNoCbd().toUpperCase());
      catCie10.setNoAph(catCie10View.getNoAph().toUpperCase());
      catCie10.setFetal(catCie10View.getFetal().toUpperCase());
      catCie10.setDiaCapituloType(catCie10View.getDiaCapituloType().toUpperCase());
      catCie10.setDescripcionCapitulo(catCie10View.getDescripcionCapitulo().toUpperCase());
      catCie10.setRubricaType(catCie10View.getRubricaType().toUpperCase());
      catCie10.setYearModifi(catCie10View.getYearModifi().toUpperCase());
      catCie10.setYearAplicacion(catCie10View.getYearAplicacion().toUpperCase());
      catCie10.setNotdiaria(catCie10View.getNotdiaria().toUpperCase());
      catCie10.setNotsemanal(catCie10View.getNotsemanal().toUpperCase());
      catCie10.setSistemaEspecial(catCie10View.getSistemaEspecial().toUpperCase());
      catCie10.setBirmm(catCie10View.getBirmm().toUpperCase());
      catCie10.setCveCausaType(catCie10View.getCveCausaType().toUpperCase());
      catCie10.setEpiMorta(catCie10View.getEpiMorta().toUpperCase());
      catCie10.setEpiMortaM5(catCie10View.getEpiMortaM5().toUpperCase());
      catCie10.setEdasEIrasEnM5(catCie10View.getEdasEIrasEnM5().toUpperCase());
      catCie10.setLista1(catCie10View.getLista1().toUpperCase());
      catCie10.setLista5(catCie10View.getLista5().toUpperCase());
      catCie10.setPrinmorta(catCie10View.getPrinmorta().toUpperCase());
      catCie10.setPrinmorbi(catCie10View.getPrinmorbi().toUpperCase());
      catCie10.setLmMorbi(catCie10View.getLmMorbi().toUpperCase());
      catCie10.setLmMorta(catCie10View.getLmMorta().toUpperCase());
      catCie10.setLgbd165(catCie10View.getLgbd165().toUpperCase());
      catCie10.setLomsbeck(catCie10View.getLomsbeck().toUpperCase());
      catCie10.setLgbd190(catCie10View.getLgbd190().toUpperCase());
      catCie10.setEsCauses(catCie10View.getEsCauses().toUpperCase());
      catCie10.setNumCauses(catCie10View.getNumCauses().toUpperCase());
      catCie10.setEsSuiveMorta(catCie10View.getEsSuiveMorta().toUpperCase());
      catCie10.setDaga(catCie10View.getDaga().toUpperCase());
      catCie10.setEpiClave(catCie10View.getEpiClave().toUpperCase());
      catCie10.setEpiClaveDesc(catCie10View.getEpiClaveDesc().toUpperCase());
      catCie10.setEsSuiveMorb(catCie10View.getEsSuiveMorb().toUpperCase());
      catCie10.setEsSuiveNotin(catCie10View.getEsSuiveNotin().toUpperCase());
      catCie10.setEsSuiveEstEpi(catCie10View.getEsSuiveEstEpi().toUpperCase());
      catCie10.setEsSuiveEstBrote(catCie10View.getEsSuiveEstBrote().toUpperCase());
      catCie10.setSinac(catCie10View.getSinac().toUpperCase());
      catCie10.setCodigox(catCie10View.getCodigox().toUpperCase());
      catCie10.setCodSitLesion(catCie10View.getCodSitLesion().toUpperCase());
      catCie10.setActivo(catCie10View.getActivo());
      catCie10.setDatosBusqueda(catCie10View.getCatalogKey().toUpperCase() + " " + catCie10View.getNombre().toUpperCase());

      logger.debug("convertir CatCie10View to Entity: {}", catCie10);
      return catCie10;
   }


   public CatCie10View toView(CatCie10 catCie10, Boolean complete) {

      CatCie10View catCie10View = new CatCie10View();
      catCie10View.setIdCie10(catCie10.getIdCie10());
      catCie10View.setLetra(catCie10.getLetra());
      catCie10View.setCatalogKey(catCie10.getCatalogKey());
      catCie10View.setAsterisco(catCie10.getAsterisco());
      catCie10View.setNombre(catCie10.getNombre());
      catCie10View.setLsex(catCie10.getLsex());
      catCie10View.setLinf(catCie10.getLinf());
      catCie10View.setLsup(catCie10.getLsup());
      catCie10View.setTrivial(catCie10.getTrivial());
      catCie10View.setErradicado(catCie10.getErradicado());
      catCie10View.setNInter(catCie10.getNInter());
      catCie10View.setNin(catCie10.getNin());
      catCie10View.setNinmtobs(catCie10.getNinmtobs());
      catCie10View.setNoCbd(catCie10.getNoCbd());
      catCie10View.setNoAph(catCie10.getNoAph());
      catCie10View.setFetal(catCie10.getFetal());
      catCie10View.setDiaCapituloType(catCie10.getDiaCapituloType());
      catCie10View.setDescripcionCapitulo(catCie10.getDescripcionCapitulo());
      catCie10View.setRubricaType(catCie10.getRubricaType());
      catCie10View.setYearModifi(catCie10.getYearModifi());
      catCie10View.setYearAplicacion(catCie10.getYearAplicacion());
      catCie10View.setNotdiaria(catCie10.getNotdiaria());
      catCie10View.setNotsemanal(catCie10.getNotsemanal());
      catCie10View.setSistemaEspecial(catCie10.getSistemaEspecial());
      catCie10View.setBirmm(catCie10.getBirmm());
      catCie10View.setCveCausaType(catCie10.getCveCausaType());
      catCie10View.setEpiMorta(catCie10.getEpiMorta());
      catCie10View.setEpiMortaM5(catCie10.getEpiMortaM5());
      catCie10View.setEdasEIrasEnM5(catCie10.getEdasEIrasEnM5());
      catCie10View.setLista1(catCie10.getLista1());
      catCie10View.setLista5(catCie10.getLista5());
      catCie10View.setPrinmorta(catCie10.getPrinmorta());
      catCie10View.setPrinmorbi(catCie10.getPrinmorbi());
      catCie10View.setLmMorbi(catCie10.getLmMorbi());
      catCie10View.setLmMorta(catCie10.getLmMorta());
      catCie10View.setLgbd165(catCie10.getLgbd165());
      catCie10View.setLomsbeck(catCie10.getLomsbeck());
      catCie10View.setLgbd190(catCie10.getLgbd190());
      catCie10View.setEsCauses(catCie10.getEsCauses());
      catCie10View.setNumCauses(catCie10.getNumCauses());
      catCie10View.setEsSuiveMorta(catCie10.getEsSuiveMorta());
      catCie10View.setDaga(catCie10.getDaga());
      catCie10View.setEpiClave(catCie10.getEpiClave());
      catCie10View.setEpiClaveDesc(catCie10.getEpiClaveDesc());
      catCie10View.setEsSuiveMorb(catCie10.getEsSuiveMorb());
      catCie10View.setEsSuiveNotin(catCie10.getEsSuiveNotin());
      catCie10View.setEsSuiveEstEpi(catCie10.getEsSuiveEstEpi());
      catCie10View.setEsSuiveEstBrote(catCie10.getEsSuiveEstBrote());
      catCie10View.setSinac(catCie10.getSinac());
      catCie10View.setCodigox(catCie10.getCodigox());
      catCie10View.setCodSitLesion(catCie10.getCodSitLesion());
      catCie10View.setActivo(catCie10.getActivo());

      logger.debug("convertir CatCie10 to View: {}", catCie10View);
      return catCie10View;
   }
}
