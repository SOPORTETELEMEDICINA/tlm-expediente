package net.amentum.niomedic.expediente.converter.historia_clinica_general.exploracion_fisica;

import net.amentum.niomedic.expediente.model.historia_clinica.exploracion_fisica.*;
import net.amentum.niomedic.expediente.views.historia_clinica_general.exploracion_fisica.*;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Component;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import java.lang.reflect.InvocationTargetException;

@Component
public class ExploracionFisicaConverter {

    public ExploracionFisica toEntity(ExploracionFisicaView view) {
        ExploracionFisica entity = new ExploracionFisica();
        entity.setIdExploracionFisica(view.getIdExploracionFisica());
        entity.setIdHistoriaClinica(view.getIdHistoriaClinica());
        entity.setPeso(view.getPeso());
        entity.setTalla(view.getTalla());
        entity.setTemperatura(view.getTemperatura());
        entity.setImc(view.getImc());
        entity.setFrecuenciaCardiaca(view.getFrecuenciaCardiaca());
        entity.setFrecuenciaRespiratoria(view.getFrecuenciaRespiratoria());
        entity.setSaturacionOxigeno(view.getSaturacionOxigeno());
        entity.setTensionArterial(toEntity(view.getTensionArterial()));
        entity.setEstadoConciencia(toEntity(view.getEstadoConciencia()));
        entity.setHabitusExterior(toEntity(view.getHabitusExterior()));
        entity.setSistemaNervioso(toEntity(view.getSistemaNervioso()));
        entity.setCabeza(toEntity(view.getCabeza()));
        entity.setCuello(toEntity(view.getCuello()));
        entity.setTorax(toEntity(view.getTorax()));
        entity.setAbdomen(toEntity(view.getAbdomen()));
        entity.setPiel(toEntity(view.getPiel()));
        entity.setGenitales(toEntity(view.getGenitales()));
        entity.setExtremidades(toEntity(view.getExtremidades()));
        return entity;
    }

    public ExploracionFisicaView toView(ExploracionFisica entity) {
        try {
            ExploracionFisicaView view = new ExploracionFisicaView();
            view.setPeso(entity.getPeso());
            view.setTalla(entity.getTalla());
            view.setTemperatura(entity.getTemperatura());
            view.setImc(entity.getImc());
            view.setFrecuenciaCardiaca(entity.getFrecuenciaCardiaca());
            view.setFrecuenciaRespiratoria(entity.getFrecuenciaRespiratoria());
            view.setSaturacionOxigeno(entity.getSaturacionOxigeno());
            TensionArterialView tensionArterialView = new TensionArterialView();
            BeanUtils.copyProperties(tensionArterialView, entity.getTensionArterial());
            view.setTensionArterial(tensionArterialView);
            EstadoConscienciaView estadoConcienciaView = new EstadoConscienciaView();
            BeanUtils.copyProperties(estadoConcienciaView, entity.getEstadoConciencia());
            view.setEstadoConciencia(estadoConcienciaView);
            HabitusExteriorView habitusExteriorView = new HabitusExteriorView();
            BeanUtils.copyProperties(habitusExteriorView, entity.getHabitusExterior());
            view.setHabitusExterior(habitusExteriorView);
            SistemaNerviosoView sistemaNerviosoView = new SistemaNerviosoView();
            BeanUtils.copyProperties(sistemaNerviosoView, entity.getSistemaNervioso());
            view.setSistemaNervioso(sistemaNerviosoView);
            CabezaView cabezaView = new CabezaView();
            BeanUtils.copyProperties(cabezaView, entity.getCabeza());
            view.setCabeza(cabezaView);
            CuelloView cuelloView = new CuelloView();
            BeanUtils.copyProperties(cuelloView, entity.getCuello());
            view.setCuello(cuelloView);
            ToraxView toraxView = new ToraxView();
            BeanUtils.copyProperties(toraxView, entity.getTorax());
            view.setTorax(toraxView);
            AbdomenView abdomenView = new AbdomenView();
            BeanUtils.copyProperties(abdomenView, entity.getAbdomen());
            view.setAbdomen(abdomenView);
            PielView pielView = new PielView();
            BeanUtils.copyProperties(pielView, entity.getPiel());
            view.setPiel(pielView);
            GenitalesView genitalesView = new GenitalesView();
            BeanUtils.copyProperties(genitalesView, entity.getGenitales());
            view.setGenitales(genitalesView);
            ExtremidadesView extremidadesView = new ExtremidadesView();
            BeanUtils.copyProperties(extremidadesView, entity.getExtremidades());
            view.setExtremidades(extremidadesView);
            return view;
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }


    private TensionArterial toEntity(TensionArterialView view) {
        TensionArterial entity = new TensionArterial();
        entity.setIdTensionArterial(view.getIdTensionArterial());
        entity.setIdHistoriaClinica(view.getIdHistoriaClinica());
        entity.setDiastolica(view.getDiastolica());
        entity.setSistolica(view.getSistolica());
        return entity;
    }

    /*private TensionArterialView toView(TensionArterial entity) {
        TensionArterialView view = new TensionArterialView();
        view.setIdTensionArterial(entity.getIdTensionArterial());
        view.setIdHistoriaClinica(entity.getIdHistoriaClinica());
        view.setDiastolica(entity.getDiastolica());
        view.setSistolica(entity.getSistolica());
        return view;
    }*/

    public EstadoConciencia toEntity(EstadoConscienciaView view) {
        EstadoConciencia entity = new EstadoConciencia();
        entity.setIdEstadoConciencia(view.getIdEstadoConciencia());
        entity.setIdHistoriaClinica(view.getIdHistoriaClinica());
        entity.setConsciente(view.getConsciente());
        entity.setComentarios(view.getComentarios());
        return entity;
    }

    public HabitusExterior toEntity(HabitusExteriorView view) {
        HabitusExterior entity = new HabitusExterior();
        entity.setIdHabitusExterior(view.getIdHabitusExterior());
        entity.setIdHistoriaClinica(view.getIdHistoriaClinica());
        entity.setHabitus(view.getHabitus());
        entity.setComentarios(view.getComentarios());
        return entity;
    }

    public SistemaNervioso toEntity(SistemaNerviosoView view) {
        SistemaNervioso entity = new SistemaNervioso();
        entity.setIdSistemaNervioso(view.getIdSistemaNervioso());
        entity.setIdHistoriaClinica(view.getIdHistoriaClinica());
        entity.setOrientadoenTiempo(view.getOrientadoenTiempo());
        entity.setOrientadoenEspacio(view.getOrientadoenEspacio());
        entity.setOrientadoenPersona(view.getOrientadoenPersona());
        entity.setFuerzaMuscularBrazoIzq(view.getFuerzaMuscularBrazoIzq());
        entity.setFuerzaMuscularBrazoDer(view.getFuerzaMuscularBrazoDer());
        entity.setFuerzaMuscularPiernaIzq(view.getFuerzaMuscularPiernaIzq());
        entity.setFuerzaMuscularPiernaDer(view.getFuerzaMuscularPiernaDer());
        entity.setTonoMuscular(view.getTonoMuscular());
        entity.setReflejoOsteoBrazoizqEstado(view.getReflejoOsteoBrazoizqEstado());
        entity.setReflejoOsteoBrazoizqComentario(view.getReflejoOsteoBrazoizqComentario());
        entity.setReflejoOsteoBrazoderEstado(view.getReflejoOsteoBrazoderEstado());
        entity.setReflejoOsteoBrazoderComentario(view.getReflejoOsteoBrazoderComentario());
        entity.setReflejoOsteoPiernaizqEstado(view.getReflejoOsteoPiernaizqEstado());
        entity.setReflejoOsteoPiernaizqComentario(view.getReflejoOsteoPiernaizqComentario());
        entity.setReflejoOsteoPiernaderEstado(view.getReflejoOsteoPiernaderEstado());
        entity.setReflejoOsteoPiernaderComentario(view.getReflejoOsteoPiernaderComentario());
        entity.setMarcha(view.getMarcha());
        return entity;
    }

    public Cabeza toEntity(CabezaView view) {
        Cabeza entity = new Cabeza();
        entity.setIdCabeza(view.getIdCabeza());
        entity.setIdHistoriaClinica(view.getIdHistoriaClinica());
        entity.setTipo(view.getTipo());
        entity.setCueroCabelludo(view.getCueroCabelludo());
        entity.setEndomorficos(view.getEndomorficos());
        entity.setOjoIzquierdoCejasParpadoPestana(view.getOjoIzquierdoCejasParpadoPestana());
        entity.setOjoIzquierdoConjuntivas(view.getOjoIzquierdoConjuntivas());
        entity.setOjoIzquierdoConjuntivasDescripcion(view.getOjoIzquierdoConjuntivasDescripcion());
        entity.setOjoIzquierdoPupilas(view.getOjoIzquierdoPupilas());
        entity.setOjoIzquierdoReflejosPupulas(view.getOjoIzquierdoReflejosPupulas());
        entity.setOjoIzquierdoMovimientos(view.getOjoIzquierdoMovimientos());
        entity.setOjoIzquierdoTonoOcular(view.getOjoIzquierdoTonoOcular());
        entity.setOjoDerechoCejasParpadoPestana(view.getOjoDerechoCejasParpadoPestana());
        entity.setOjoDerechoConjuntivas(view.getOjoDerechoConjuntivas());
        entity.setOjoDerechoConjuntivasDescripcion(view.getOjoDerechoConjuntivasDescripcion());
        entity.setOjoDerechoPupilas(view.getOjoDerechoPupilas());
        entity.setOjoDerechoReflejosPupulas(view.getOjoDerechoReflejosPupulas());
        entity.setOjoDerechoMovimientos(view.getOjoDerechoMovimientos());
        entity.setOjoDerechoTonoOcular(view.getOjoDerechoTonoOcular());
        entity.setNarizPermeabilidad(view.getNarizPermeabilidad());
        entity.setNarizSenoFrontal(view.getNarizSenoFrontal());
        entity.setNarizSenoMaxilar(view.getNarizSenoMaxilar());
        entity.setNarizSecreciones(view.getNarizSecreciones());
        entity.setNarizLesiones(view.getNarizLesiones());
        entity.setNarizMasas(view.getNarizMasas());
        entity.setNarizCuerposExtranos(view.getNarizCuerposExtranos());
        entity.setOidoPabellonAuricular(view.getOidoPabellonAuricular());
        entity.setOidoConductoAuditivo(view.getOidoConductoAuditivo());
        entity.setOidoConductoAuditivoDescripcion(view.getOidoConductoAuditivoDescripcion());
        entity.setOidoMembranaTimpanica(view.getOidoMembranaTimpanica());
        entity.setOidoMembranaTimpanicaDescripcion(view.getOidoMembranaTimpanicaDescripcion());
        entity.setBocaTrismus(view.getBocaTrismus());
        entity.setBocaHalitosis(view.getBocaHalitosis());
        entity.setBocaMucosa(view.getBocaMucosa());
        entity.setBocaMucosaTipo(view.getBocaMucosaTipo());
        entity.setBocaCandidiasis(view.getBocaCandidiasis());
        entity.setBocaLengua(view.getBocaLengua());
        entity.setBocaOrofaringeDolor(view.getBocaOrofaringeDolor());
        entity.setBocaOrofaringeHiperemia(view.getBocaOrofaringeHiperemia());
        entity.setBocaOrofaringeAmigdalas(view.getBocaOrofaringeAmigdalas());
        entity.setBocaOrofaringeAmigdalasTipo(view.getBocaOrofaringeAmigdalasTipo());
        entity.setBocaOrofaringeCuerpoExtrano(view.getBocaOrofaringeCuerpoExtrano());
        entity.setBocaOrofaringeHemorragia(view.getBocaOrofaringeHemorragia());
        entity.setBocaDientes(view.getBocaDientes());
        entity.setBocaEnciasGingivorragia(view.getBocaEnciasGingivorragia());
        entity.setBocaEnciasGingivitis(view.getBocaEnciasGingivitis());
        entity.setConjuntiva(view.getConjuntiva());
        entity.setNarinas(view.getNarinas());
        entity.setOrofaringe(view.getOrofaringe());
        entity.setOidos(view.getOidos());
        entity.setCavidadOral(view.getCavidadOral());
        entity.setDentadura(view.getDentadura());
        entity.setComentarios(view.getComentarios());
        entity.setPupilas1(view.getPupilas1());
        entity.setPupilas2(view.getPupilas2());
        return entity;
    }

    public Cuello toEntity(CuelloView view) {
        Cuello entity = new Cuello();
        entity.setIdCuello(view.getIdCuello());
        entity.setIdHistoriaClinica(view.getIdHistoriaClinica());
        entity.setMasas(view.getMasas());
        entity.setMovil(view.getMovil());
        entity.setPulso(view.getPulso());
        entity.setTiroides(view.getTiroides());
        entity.setLinfaticos(view.getLinfaticos());
        entity.setVasos(view.getVasos());
        entity.setAcantosis(view.getAcantosis());
        entity.setComentarios(view.getComentarios());
        return entity;
    }

    public Torax toEntity(ToraxView view) {
        Torax entity = new Torax();
        entity.setIdTorax(view.getIdTorax());
        entity.setIdHistoriaClinica(view.getIdHistoriaClinica());
        entity.setTorax(view.getTorax());
        entity.setRespiracion(view.getRespiracion());
        entity.setGanglios(view.getGanglios());
        entity.setRuidosCardiacos(view.getRuidosCardiacos());
        entity.setRuidosCardiacosOtros(view.getRuidosCardiacosOtros());
        entity.setMamasSimetria(view.getMamasSimetria());
        entity.setMamasGinecomastia(view.getMamasGinecomastia());
        entity.setMamasMasas(view.getMamasMasas());
        entity.setMamasSecreciones(view.getMamasSecreciones());
        entity.setMamasOtros(view.getMamasOtros());
        entity.setMurmulloVesicular(view.getMurmulloVesicular());
        entity.setMurmulloVesicularLocalizacion(view.getMurmulloVesicularLocalizacion());
        entity.setRuidosSoplos(view.getRuidosSoplos());
        entity.setRuidosSoplosLocalizacion(view.getRuidosSoplosLocalizacion());
        entity.setRuidosEstertores(view.getRuidosEstertores());
        entity.setRuidosEstertoresLocalizacion(view.getRuidosEstertoresLocalizacion());
        entity.setPosteriorPulmonaresAlteraciones(view.getPosteriorPulmonaresAlteraciones());
        entity.setMusculosParavertebralesAtroficos(view.getMusculosParavertebralesAtroficos());
        entity.setMusculosParavertebralesDolor(view.getMusculosParavertebralesDolor());
        entity.setEscoliosis(view.getEscoliosis());
        entity.setCifosis(view.getCifosis());
        entity.setLordosis(view.getLordosis());
        entity.setRuidosRespiratorios(view.getRuidosRespiratorios());
        entity.setCardiacos(view.getCardiacos());
        entity.setComentarios(view.getComentarios());
        return entity;
    }

    public Abdomen toEntity(AbdomenView view) {
        Abdomen entity = new Abdomen();
        entity.setIdAbdomen(view.getIdAbdomen());
        entity.setIdHistoriaClinica(view.getIdHistoriaClinica());
        entity.setCaracteristicas(view.getCaracteristicas());
        entity.setRuidosPeristalticos(view.getRuidosPeristalticos());
        entity.setRuidosPeristalticosTipo(view.getRuidosPeristalticosTipo());
        entity.setPercusion(view.getPercusion());
        entity.setPercusionLocalizacion(view.getPercusionLocalizacion());
        entity.setPalpacionDolor(view.getPalpacionDolor());
        entity.setPalpacionDolorLocalizacion(view.getPalpacionDolorLocalizacion());
        entity.setMasas(view.getMasas());
        entity.setVisceromegalias(view.getVisceromegalias());
        entity.setAscitis(view.getAscitis());
        entity.setHernias(view.getHernias());
        entity.setSignos(view.getSignos());
        entity.setComentarios(view.getComentarios());
        entity.setForma(view.getForma());
        entity.setRuidos(view.getRuidos());
        entity.setDolor(view.getDolor());
        return entity;
    }

    public Piel toEntity(PielView view) {
        Piel entity = new Piel();
        entity.setIdPiel(view.getIdPiel());
        entity.setIdHistoriaClinica(view.getIdHistoriaClinica());
        entity.setPlanasMacula(view.getPlanasMacula());
        entity.setPlanasTelangiectasias(view.getPlanasTelangiectasias());
        entity.setPlanasEsclerosis(view.getPlanasEsclerosis());
        entity.setElevadasPapula(view.getElevadasPapula());
        entity.setElevadasPlaca(view.getElevadasPlaca());
        entity.setElevadasNodulo(view.getElevadasNodulo());
        entity.setElevadasVesicula(view.getElevadasVesicula());
        entity.setElevadasAmpolla(view.getElevadasAmpolla());
        entity.setElevadasAbsceso(view.getElevadasAbsceso());
        entity.setElevadasEscara(view.getElevadasEscara());
        entity.setElevadasCicatriz(view.getElevadasCicatriz());
        entity.setDeprimidasAtrofia(view.getDeprimidasAtrofia());
        entity.setDeprimidasExcoriacion(view.getDeprimidasExcoriacion());
        entity.setDeprimidasErosion(view.getDeprimidasErosion());
        entity.setDeprimidasUlcera(view.getDeprimidasUlcera());
        entity.setComentarios(view.getComentarios());
        entity.setManchas(view.getManchas());
        entity.setMasas(view.getMasas());
        entity.setLesiones(view.getLesiones());
        return entity;
    }

    public Genitales toEntity(GenitalesView view) {
        Genitales entity = new Genitales();
        entity.setIdGenitales(view.getIdGenitales());
        entity.setIdHistoriaClinica(view.getIdHistoriaClinica());
        entity.setSecreciones(view.getSecreciones());
        entity.setMasas(view.getMasas());
        entity.setLesiones(view.getLesiones());
        entity.setComentarios(view.getComentarios());
        return entity;
    }

    public Extremidades toEntity(ExtremidadesView view) {
        Extremidades entity = new Extremidades();
        entity.setIdExtremidades(view.getIdExtremidades());
        entity.setIdHistoriaClinica(view.getIdHistoriaClinica());
        entity.setToracicosIzquierdo(view.getToracicosIzquierdo());
        entity.setToracicosDerecho(view.getToracicosDerecho());
        entity.setPelvicosIzquierdo(view.getPelvicosIzquierdo());
        entity.setPelvicosDerecho(view.getPelvicosDerecho());
        entity.setSimetria(view.getSimetria());
        entity.setPulsosCarotideoPresente(view.getPulsosCarotideoPresente());
        entity.setPulsosCarotideoRitmo(view.getPulsosCarotideoRitmo());
        entity.setPulsosCarotideoSimetria(view.getPulsosCarotideoSimetria());
        entity.setPulsosCarotideoIntensidad(view.getPulsosCarotideoIntensidad());
        entity.setPulsosRadialPresente(view.getPulsosRadialPresente());
        entity.setPulsosRadialRitmo(view.getPulsosRadialRitmo());
        entity.setPulsosRadialSimetria(view.getPulsosRadialSimetria());
        entity.setPulsosRadialIntensidad(view.getPulsosRadialIntensidad());
        entity.setPulsosBraquialPresente(view.getPulsosBraquialPresente());
        entity.setPulsosBraquialRitmo(view.getPulsosBraquialRitmo());
        entity.setPulsosBraquialSimetria(view.getPulsosBraquialSimetria());
        entity.setPulsosBraquialIntensidad(view.getPulsosBraquialIntensidad());
        entity.setPulsosFemoralPresente(view.getPulsosFemoralPresente());
        entity.setPulsosFemoralRitmo(view.getPulsosFemoralRitmo());
        entity.setPulsosFemoralSimetria(view.getPulsosFemoralSimetria());
        entity.setPulsosFemoralIntensidad(view.getPulsosFemoralIntensidad());
        entity.setPulsosPopitleoPresente(view.getPulsosPopitleoPresente());
        entity.setPulsosPopitleoRitmo(view.getPulsosPopitleoRitmo());
        entity.setPulsosPopitleoSimetria(view.getPulsosPopitleoSimetria());
        entity.setPulsosPopitleoIntensidad(view.getPulsosPopitleoIntensidad());
        entity.setPulsosTibialPresente(view.getPulsosTibialPresente());
        entity.setPulsosTibialRitmo(view.getPulsosTibialRitmo());
        entity.setPulsosTibialSimetria(view.getPulsosTibialSimetria());
        entity.setPulsosTibialIntensidad(view.getPulsosTibialIntensidad());
        entity.setPulsosDorsalisPedisPresente(view.getPulsosDorsalisPedisPresente());
        entity.setPulsosDorsalisPedisRitmo(view.getPulsosDorsalisPedisRitmo());
        entity.setPulsosDorsalisPedisSimetria(view.getPulsosDorsalisPedisSimetria());
        entity.setPulsosDorsalisPedisIntensidad(view.getPulsosDorsalisPedisIntensidad());
        entity.setMasasPalpables(view.getMasasPalpables());
        entity.setTonoMuscular(view.getTonoMuscular());
        entity.setDolor(view.getDolor());
        entity.setDolorEvn(view.getDolorEvn());
        entity.setDolorLocalizacion(view.getDolorLocalizacion());
        entity.setDolorCaraceristicas(view.getDolorCaraceristicas());
        entity.setCrepitacion(view.getCrepitacion());
        entity.setCrepitacionLocalizacion(view.getCrepitacionLocalizacion());
        entity.setDeformidades(view.getDeformidades());
        entity.setArticulacionesEdema(view.getArticulacionesEdema());
        entity.setArticulacionesHiperemia(view.getArticulacionesHiperemia());
        entity.setArticulacionesMovimientos(view.getArticulacionesMovimientos());
        entity.setArticulacionesMovimientosDetalles(view.getArticulacionesMovimientosDetalles());
        entity.setFuerza(view.getFuerza());
        entity.setEdema(view.getEdema());
        entity.setSignosGodet(view.getSignosGodet());
        entity.setSignosHomans(view.getSignosHomans());
        entity.setSignosColumnaMusculos(view.getSignosColumnaMusculos());
        entity.setSignosColumnaEscoliosis(view.getSignosColumnaEscoliosis());
        entity.setSignosColumnaCifosis(view.getSignosColumnaCifosis());
        entity.setSignosColumnaLordosis(view.getSignosColumnaLordosis());
        entity.setEstado(view.getEstado());
        entity.setEdemaLocalizacion(view.getEdemaLocalizacion());
        entity.setEdemaConsistencia(view.getEdemaConsistencia());
        entity.setEdemaGodette(view.getEdemaGodette());
        entity.setSimetricos(view.getSimetricos());
        entity.setIntensidad(view.getIntensidad());
        entity.setComentarios(view.getComentarios());
        return entity;
    }
}
