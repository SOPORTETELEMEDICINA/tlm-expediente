package net.amentum.niomedic.expediente.converter.historia_clinica_general.interrogatorio_aparatos;

import net.amentum.niomedic.expediente.model.historia_clinica.interrogatorio_aparatos.SentidoVision;
import net.amentum.niomedic.expediente.views.historia_clinica_general.interrogatorio_aparatos.SentidoVisionView;
import org.springframework.stereotype.Component;

@Component
public class SentidoVisionConverter {

    public SentidoVision toEntity(SentidoVisionView view) {
        SentidoVision entity = new SentidoVision();
        entity.setIdInterrogatorio(view.getIdInterrogatorio());
        entity.setIdHistoriaClinica(view.getIdHistoriaClinica());
        entity.setVisionNormal(view.getVisionNormal());
        entity.setVisionNormalOjo(view.getVisionNormalOjo());
        entity.setVisionBorrosa(view.getVisionBorrosa());
        entity.setVisionBorrosaOjo(view.getVisionBorrosaOjo());
        entity.setVisionOjo(view.getVisionOjo());
        entity.setCeguera(view.getCeguera());
        entity.setCegueraOjo(view.getCegueraOjo());
        entity.setCegueraDerecho(view.getCegueraderecho());
        entity.setCegueraIzquierdo(view.getCegueraizquierdo());
        entity.setCegueraBilateral(view.getCegueraBilateral());
        entity.setDolor(view.getDolor());
        entity.setDolorOjo(view.getDolorOjo());
        entity.setDolorDerecho(view.getDolorDerecho());
        entity.setDolorIzquierdo(view.getDolorIzquierdo());
        entity.setFotofobia(view.getFotofobia());
        entity.setDiplopia(view.getDiplopia());
        entity.setDiplopiaOjo(view.getDiplopiaOjo());
        entity.setDiplopiaDerecho(view.getDiplopiaderecho());
        entity.setDiplopiaIzquierdo(view.getDiplopiaizquierdo());
        entity.setPrurito(view.getPrurito());
        entity.setPruritoOjo(view.getPruritoOjo());
        entity.setPruritoIzquierdo(view.getPruritoizquierdo());
        entity.setPruritoDerecho(view.getPruritoderecho());
        entity.setPruritoBilateral(view.getPruritoBilateral());
        entity.setHipermetropia(view.getHipermetropia());
        entity.setMiopia(view.getMiopia());
        entity.setAstigmatismo(view.getAstigmatismo());
        entity.setPresbicia(view.getPresbicia());
        entity.setLentes(view.getLentes());
        entity.setLentesCausa(view.getLentesCausa());
        entity.setEnrojecimiento(view.getEnrojecimiento());;
        entity.setEnrojecimientoOjo(view.getEnrojecimientoOjo());
        entity.setEnrojecimientoIzquierdo(view.getEnrojecimientoIzquierdo());
        entity.setEnrojecimientoDerecho(view.getEnrojecimientoDerecho());
        entity.setCatarata(view.getCatarata());
        entity.setCatarataOjo(view.getCatarataOjo());
        entity.setCatarataBorrosa(view.getCatarataBorrosa());
        entity.setCatarataDerecho(view.getCatarataDerecho());
        entity.setCatarataIzquierdo(view.getCatarataIzquierdo());
        entity.setCatarataBilateral(view.getCatarataBilateral());
        entity.setResequedad(view.getResequedad());
        entity.setResequedadOjo(view.getResequedadOjo());
        entity.setResequedadDerecho(view.getResequedadDerecho());
        entity.setResequedadIzquierdo(view.getResequedadIzquierdo());
        entity.setResequedadBilateral(view.getResequedadBilateral());
        entity.setSecrecion(view.getSecrecion());
        entity.setSecrecionOjo(view.getSecrecionOjo());
        entity.setSecrecionIzquierdo(view.getSecrecionzquiedo());
        entity.setSecrecionDerecho(view.getSecrecionDerecho());
        entity.setSecrecionBilateral(view.getSecrecionBilateral());
        entity.setComentarios(view.getComentarios());
        return entity;
    }

    public SentidoVisionView toView(SentidoVision entity) {
        SentidoVisionView view = new SentidoVisionView();
        view.setIdInterrogatorio(entity.getIdInterrogatorio());
        view.setIdHistoriaClinica(entity.getIdHistoriaClinica());
        view.setVisionNormal(entity.getVisionNormal());
        view.setVisionNormalOjo(entity.getVisionNormalOjo());
        view.setVisionBorrosa(entity.getVisionBorrosa());
        view.setVisionBorrosaOjo(entity.getVisionBorrosaOjo());
        view.setVisionOjo(entity.getVisionOjo());
        view.setCeguera(entity.getCeguera());
        view.setCegueraOjo(entity.getCegueraOjo());
        view.setCegueraderecho(entity.getCegueraDerecho());
        view.setCegueraizquierdo(entity.getCegueraIzquierdo());
        view.setCegueraBilateral(entity.getCegueraBilateral());
        view.setDolor(entity.getDolor());
        view.setDolorOjo(entity.getDolorOjo());
        view.setDolorDerecho(entity.getDolorDerecho());
        view.setDolorIzquierdo(entity.getDolorIzquierdo());
        view.setFotofobia(entity.getFotofobia());
        view.setDiplopia(entity.getDiplopia());
        view.setDiplopiaOjo(entity.getDiplopiaOjo());
        view.setDiplopiaderecho(entity.getDiplopiaDerecho());
        view.setDiplopiaizquierdo(entity.getDiplopiaIzquierdo());
        view.setPrurito(entity.getPrurito());
        view.setPruritoOjo(entity.getPruritoOjo());
        view.setPruritoizquierdo(entity.getPruritoIzquierdo());
        view.setPruritoderecho(entity.getPruritoDerecho());
        view.setPruritoBilateral(entity.getPruritoBilateral());
        view.setHipermetropia(entity.getHipermetropia());
        view.setMiopia(entity.getMiopia());
        view.setAstigmatismo(entity.getAstigmatismo());
        view.setPresbicia(entity.getPresbicia());
        view.setLentes(entity.getLentes());
        view.setLentesCausa(entity.getLentesCausa());
        view.setEnrojecimiento(entity.getEnrojecimiento());;
        view.setEnrojecimientoOjo(entity.getEnrojecimientoOjo());
        view.setEnrojecimientoIzquierdo(entity.getEnrojecimientoIzquierdo());
        view.setEnrojecimientoDerecho(entity.getEnrojecimientoDerecho());
        view.setCatarata(entity.getCatarata());
        view.setCatarataOjo(entity.getCatarataOjo());
        view.setCatarataBorrosa(entity.getCatarataBorrosa());
        view.setCatarataDerecho(entity.getCatarataDerecho());
        view.setCatarataIzquierdo(entity.getCatarataIzquierdo());
        view.setCatarataBilateral(entity.getCatarataBilateral());
        view.setResequedad(entity.getResequedad());
        view.setResequedadOjo(entity.getResequedadOjo());
        view.setResequedadDerecho(entity.getResequedadDerecho());
        view.setResequedadIzquierdo(entity.getResequedadIzquierdo());
        view.setResequedadBilateral(entity.getResequedadBilateral());
        view.setSecrecion(entity.getSecrecion());
        view.setSecrecionOjo(entity.getSecrecionOjo());
        view.setSecrecionzquiedo(entity.getSecrecionIzquierdo());
        view.setSecrecionDerecho(entity.getSecrecionDerecho());
        view.setSecrecionBilateral(entity.getSecrecionBilateral());
        view.setComentarios(entity.getComentarios());
        return view;
    }

}
