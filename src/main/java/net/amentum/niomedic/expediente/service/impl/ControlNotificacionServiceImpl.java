package net.amentum.niomedic.expediente.service.impl;

import net.amentum.common.v2.GenericException;
import net.amentum.niomedic.expediente.configuration.ApiConfiguration;
import net.amentum.niomedic.expediente.exception.ConsultaException;
import net.amentum.niomedic.expediente.exception.ControlNotificacionException;
import net.amentum.niomedic.expediente.model.SaludIndCovid;
import net.amentum.niomedic.expediente.model.SaludIndGluc;
import net.amentum.niomedic.expediente.model.SaludIndNutri;
import net.amentum.niomedic.expediente.model.SaludIndPa;
import net.amentum.niomedic.expediente.persistence.SaludIndCovidRepository;
import net.amentum.niomedic.expediente.persistence.SaludIndGlucRepository;
import net.amentum.niomedic.expediente.persistence.SaludIndNutriRepository;
import net.amentum.niomedic.expediente.persistence.SaludIndPaRepository;
import net.amentum.niomedic.expediente.service.ControlNotificacionService;
import net.amentum.niomedic.expediente.views.ControlNotificacionSimpView;
import net.amentum.niomedic.expediente.views.ControlNotificacionViewList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ControlNotificacionServiceImpl implements ControlNotificacionService {

    private final Logger logger = LoggerFactory.getLogger(ControlNotificacionServiceImpl.class);

    @Autowired
    ApiConfiguration apiConfiguration;

    @Autowired
    SaludIndCovidRepository covidRepository;

    @Autowired
    SaludIndGlucRepository glucRepository;

    @Autowired
    SaludIndNutriRepository nutriRepository;

    @Autowired
    SaludIndPaRepository paRepository;

    @Override
    public HashMap<String, List<ControlNotificacionViewList>> getNotificationsByPaciente(String idPaciente) throws ControlNotificacionException {
        try {
            Calendar calendar = Calendar.getInstance();
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            HashMap<String, List<ControlNotificacionViewList>> hashMapResponse = new HashMap<>();
            HashMap<String, HashMap<String, Object>> idPacienteList = new HashMap<>();
            switch(dayOfWeek) {
                case Calendar.SUNDAY:
                    List<ControlNotificacionViewList> covidDo;
                    covidDo = getCovidNotificationsCompl(1, dayOfWeek, covidRepository.findByCovid1doAndPacidfk(true, idPaciente), idPacienteList);
                    covidDo.addAll(getCovidNotificationsCompl(2, dayOfWeek, covidRepository.findByCovid2doAndPacidfk(true, idPaciente), idPacienteList));
                    covidDo.addAll(getCovidNotificationsCompl(3, dayOfWeek, covidRepository.findByCovid3doAndPacidfk(true, idPaciente), idPacienteList));
                    covidDo.addAll(getCovidNotificationsCompl(4, dayOfWeek, covidRepository.findByCovid4doAndPacidfk(true, idPaciente), idPacienteList));
                    hashMapResponse.put("covid", covidDo);

                    List<ControlNotificacionViewList> gluDo;
                    gluDo = getGlucosaNotificationsCompl(1, dayOfWeek, glucRepository.findByGlu1doAndPacidfk(true, idPaciente), idPacienteList);
                    gluDo.addAll(getGlucosaNotificationsCompl(2, dayOfWeek, glucRepository.findByGlu2doAndPacidfk(true, idPaciente), idPacienteList));
                    gluDo.addAll(getGlucosaNotificationsCompl(3, dayOfWeek, glucRepository.findByGlu3doAndPacidfk(true, idPaciente), idPacienteList));
                    gluDo.addAll(getGlucosaNotificationsCompl(4, dayOfWeek, glucRepository.findByGlu4doAndPacidfk(true, idPaciente), idPacienteList));
                    gluDo.addAll(getGlucosaNotificationsCompl(5, dayOfWeek, glucRepository.findByGlu5doAndPacidfk(true, idPaciente), idPacienteList));
                    gluDo.addAll(getGlucosaNotificationsCompl(6, dayOfWeek, glucRepository.findByGlu6doAndPacidfk(true, idPaciente), idPacienteList));
                    gluDo.addAll(getGlucosaNotificationsCompl(7, dayOfWeek, glucRepository.findByGlu7doAndPacidfk(true, idPaciente), idPacienteList));
                    gluDo.addAll(getGlucosaNotificationsCompl(10, dayOfWeek, glucRepository.findByGlu8doAndPacidfk(true, idPaciente), idPacienteList));
                    hashMapResponse.put("glucosa", gluDo);

                    List<ControlNotificacionViewList> nutriDo;
                    nutriDo = getNutriNotificationsCompl(1,dayOfWeek, nutriRepository.findByNutri1doAndPacidfk(true, idPaciente), idPacienteList);
                    nutriDo.addAll(getNutriNotificationsCompl(2,dayOfWeek, nutriRepository.findByNutri2doAndPacidfk(true, idPaciente), idPacienteList));
                    nutriDo.addAll(getNutriNotificationsCompl(3,dayOfWeek, nutriRepository.findByNutri3doAndPacidfk(true, idPaciente), idPacienteList));
                    hashMapResponse.put("nutricion", nutriDo);

                    List<ControlNotificacionViewList> presionArtDo;
                    presionArtDo = getPresionArterialNotificationsCompl(1, dayOfWeek, paRepository.findBySys1doAndPacidfk(true, idPaciente), idPacienteList);
                    presionArtDo.addAll(getPresionArterialNotificationsCompl(2, dayOfWeek, paRepository.findBySys2doAndPacidfk(true, idPaciente), idPacienteList));
                    presionArtDo.addAll(getPresionArterialNotificationsCompl(3, dayOfWeek, paRepository.findBySys3doAndPacidfk(true, idPaciente), idPacienteList));
                    presionArtDo.addAll(getPresionArterialNotificationsCompl(4, dayOfWeek, paRepository.findBySys4doAndPacidfk(true, idPaciente), idPacienteList));
                    presionArtDo.addAll(getPresionArterialNotificationsCompl(5, dayOfWeek, paRepository.findBySys5doAndPacidfk(true, idPaciente), idPacienteList));
                    presionArtDo.addAll(getPresionArterialNotificationsCompl(6, dayOfWeek, paRepository.findBySys6doAndPacidfk(true, idPaciente), idPacienteList));
                    presionArtDo.addAll(getPresionArterialNotificationsCompl(7, dayOfWeek, paRepository.findBySys7doAndPacidfk(true, idPaciente), idPacienteList));
                    hashMapResponse.put("presionArt", presionArtDo);
                    break;
                case Calendar.MONDAY:
                    List<ControlNotificacionViewList> covidLu;
                    covidLu = getCovidNotificationsCompl(1, dayOfWeek, covidRepository.findByCovid1luAndPacidfk(true, idPaciente), idPacienteList);
                    covidLu.addAll(getCovidNotificationsCompl(2, dayOfWeek, covidRepository.findByCovid2luAndPacidfk(true, idPaciente), idPacienteList));
                    covidLu.addAll(getCovidNotificationsCompl(3, dayOfWeek, covidRepository.findByCovid3luAndPacidfk(true, idPaciente), idPacienteList));
                    covidLu.addAll(getCovidNotificationsCompl(4, dayOfWeek, covidRepository.findByCovid4luAndPacidfk(true, idPaciente), idPacienteList));
                    hashMapResponse.put("covid", covidLu);

                    List<ControlNotificacionViewList> gluLu;
                    gluLu = getGlucosaNotificationsCompl(1, dayOfWeek, glucRepository.findByGlu1luAndPacidfk(true, idPaciente), idPacienteList);
                    gluLu.addAll(getGlucosaNotificationsCompl(2, dayOfWeek, glucRepository.findByGlu2luAndPacidfk(true, idPaciente), idPacienteList));
                    gluLu.addAll(getGlucosaNotificationsCompl(3, dayOfWeek, glucRepository.findByGlu3luAndPacidfk(true, idPaciente), idPacienteList));
                    gluLu.addAll(getGlucosaNotificationsCompl(4, dayOfWeek, glucRepository.findByGlu4luAndPacidfk(true, idPaciente), idPacienteList));
                    gluLu.addAll(getGlucosaNotificationsCompl(5, dayOfWeek, glucRepository.findByGlu5luAndPacidfk(true, idPaciente), idPacienteList));
                    gluLu.addAll(getGlucosaNotificationsCompl(6, dayOfWeek, glucRepository.findByGlu6luAndPacidfk(true, idPaciente), idPacienteList));
                    gluLu.addAll(getGlucosaNotificationsCompl(7, dayOfWeek, glucRepository.findByGlu7luAndPacidfk(true, idPaciente), idPacienteList));
                    gluLu.addAll(getGlucosaNotificationsCompl(10, dayOfWeek, glucRepository.findByGlu8luAndPacidfk(true, idPaciente), idPacienteList));
                    hashMapResponse.put("glucosa", gluLu);

                    List<ControlNotificacionViewList> nutriLu;
                    nutriLu = getNutriNotificationsCompl(1,dayOfWeek, nutriRepository.findByNutri1luAndPacidfk(true, idPaciente), idPacienteList);
                    nutriLu.addAll(getNutriNotificationsCompl(2,dayOfWeek, nutriRepository.findByNutri2luAndPacidfk(true, idPaciente), idPacienteList));
                    nutriLu.addAll(getNutriNotificationsCompl(3,dayOfWeek, nutriRepository.findByNutri3luAndPacidfk(true, idPaciente), idPacienteList));
                    hashMapResponse.put("nutricion", nutriLu);

                    List<ControlNotificacionViewList> presionArtLu;
                    presionArtLu = getPresionArterialNotificationsCompl(1, dayOfWeek, paRepository.findBySys1luAndPacidfk(true, idPaciente), idPacienteList);
                    presionArtLu.addAll(getPresionArterialNotificationsCompl(2, dayOfWeek, paRepository.findBySys2luAndPacidfk(true, idPaciente), idPacienteList));
                    presionArtLu.addAll(getPresionArterialNotificationsCompl(3, dayOfWeek, paRepository.findBySys3luAndPacidfk(true, idPaciente), idPacienteList));
                    presionArtLu.addAll(getPresionArterialNotificationsCompl(4, dayOfWeek, paRepository.findBySys4luAndPacidfk(true, idPaciente), idPacienteList));
                    presionArtLu.addAll(getPresionArterialNotificationsCompl(5, dayOfWeek, paRepository.findBySys5luAndPacidfk(true, idPaciente), idPacienteList));
                    presionArtLu.addAll(getPresionArterialNotificationsCompl(6, dayOfWeek, paRepository.findBySys6luAndPacidfk(true, idPaciente), idPacienteList));
                    presionArtLu.addAll(getPresionArterialNotificationsCompl(7, dayOfWeek, paRepository.findBySys7luAndPacidfk(true, idPaciente), idPacienteList));
                    hashMapResponse.put("presionArt", presionArtLu);
                    break;
                case Calendar.TUESDAY:
                    List<ControlNotificacionViewList> covidMa;
                    covidMa = getCovidNotificationsCompl(1, dayOfWeek, covidRepository.findByCovid1maAndPacidfk(true, idPaciente), idPacienteList);
                    covidMa.addAll(getCovidNotificationsCompl(2, dayOfWeek, covidRepository.findByCovid2maAndPacidfk(true, idPaciente), idPacienteList));
                    covidMa.addAll(getCovidNotificationsCompl(3, dayOfWeek, covidRepository.findByCovid3maAndPacidfk(true, idPaciente), idPacienteList));
                    covidMa.addAll(getCovidNotificationsCompl(4, dayOfWeek, covidRepository.findByCovid4maAndPacidfk(true, idPaciente), idPacienteList));
                    hashMapResponse.put("covid", covidMa);

                    List<ControlNotificacionViewList> gluMa;
                    gluMa = getGlucosaNotificationsCompl(1, dayOfWeek, glucRepository.findByGlu1maAndPacidfk(true, idPaciente), idPacienteList);
                    gluMa.addAll(getGlucosaNotificationsCompl(2, dayOfWeek, glucRepository.findByGlu2maAndPacidfk(true, idPaciente), idPacienteList));
                    gluMa.addAll(getGlucosaNotificationsCompl(3, dayOfWeek, glucRepository.findByGlu3maAndPacidfk(true, idPaciente), idPacienteList));
                    gluMa.addAll(getGlucosaNotificationsCompl(4, dayOfWeek, glucRepository.findByGlu4maAndPacidfk(true, idPaciente), idPacienteList));
                    gluMa.addAll(getGlucosaNotificationsCompl(5, dayOfWeek, glucRepository.findByGlu5maAndPacidfk(true, idPaciente), idPacienteList));
                    gluMa.addAll(getGlucosaNotificationsCompl(6, dayOfWeek, glucRepository.findByGlu6maAndPacidfk(true, idPaciente), idPacienteList));
                    gluMa.addAll(getGlucosaNotificationsCompl(7, dayOfWeek, glucRepository.findByGlu7maAndPacidfk(true, idPaciente), idPacienteList));
                    gluMa.addAll(getGlucosaNotificationsCompl(10, dayOfWeek, glucRepository.findByGlu8maAndPacidfk(true, idPaciente), idPacienteList));
                    hashMapResponse.put("glucosa", gluMa);

                    List<ControlNotificacionViewList> nutriMa;
                    nutriMa = getNutriNotificationsCompl(1,dayOfWeek, nutriRepository.findByNutri1maAndPacidfk(true, idPaciente), idPacienteList);
                    nutriMa.addAll(getNutriNotificationsCompl(2,dayOfWeek, nutriRepository.findByNutri2maAndPacidfk(true, idPaciente), idPacienteList));
                    nutriMa.addAll(getNutriNotificationsCompl(3,dayOfWeek, nutriRepository.findByNutri3maAndPacidfk(true, idPaciente), idPacienteList));
                    hashMapResponse.put("nutricion", nutriMa);

                    List<ControlNotificacionViewList> presionArtMa;
                    presionArtMa = getPresionArterialNotificationsCompl(1, dayOfWeek, paRepository.findBySys1maAndPacidfk(true, idPaciente), idPacienteList);
                    presionArtMa.addAll(getPresionArterialNotificationsCompl(2, dayOfWeek, paRepository.findBySys2maAndPacidfk(true, idPaciente), idPacienteList));
                    presionArtMa.addAll(getPresionArterialNotificationsCompl(3, dayOfWeek, paRepository.findBySys3maAndPacidfk(true, idPaciente), idPacienteList));
                    presionArtMa.addAll(getPresionArterialNotificationsCompl(4, dayOfWeek, paRepository.findBySys4maAndPacidfk(true, idPaciente), idPacienteList));
                    presionArtMa.addAll(getPresionArterialNotificationsCompl(5, dayOfWeek, paRepository.findBySys5maAndPacidfk(true, idPaciente), idPacienteList));
                    presionArtMa.addAll(getPresionArterialNotificationsCompl(6, dayOfWeek, paRepository.findBySys6maAndPacidfk(true, idPaciente), idPacienteList));
                    presionArtMa.addAll(getPresionArterialNotificationsCompl(7, dayOfWeek, paRepository.findBySys7maAndPacidfk(true, idPaciente), idPacienteList));
                    hashMapResponse.put("presionArt", presionArtMa);
                    break;
                case Calendar.WEDNESDAY:
                    List<ControlNotificacionViewList> covidMi;
                    covidMi = getCovidNotificationsCompl(1, dayOfWeek, covidRepository.findByCovid1miAndPacidfk(true, idPaciente), idPacienteList);
                    covidMi.addAll(getCovidNotificationsCompl(2, dayOfWeek, covidRepository.findByCovid2miAndPacidfk(true, idPaciente), idPacienteList));
                    covidMi.addAll(getCovidNotificationsCompl(3, dayOfWeek, covidRepository.findByCovid3miAndPacidfk(true, idPaciente), idPacienteList));
                    covidMi.addAll(getCovidNotificationsCompl(4, dayOfWeek, covidRepository.findByCovid4miAndPacidfk(true, idPaciente), idPacienteList));
                    hashMapResponse.put("covid", covidMi);

                    List<ControlNotificacionViewList> gluMi;
                    gluMi = getGlucosaNotificationsCompl(1, dayOfWeek, glucRepository.findByGlu1miAndPacidfk(true, idPaciente), idPacienteList);
                    gluMi.addAll(getGlucosaNotificationsCompl(2, dayOfWeek, glucRepository.findByGlu2miAndPacidfk(true, idPaciente), idPacienteList));
                    gluMi.addAll(getGlucosaNotificationsCompl(3, dayOfWeek, glucRepository.findByGlu3miAndPacidfk(true, idPaciente), idPacienteList));
                    gluMi.addAll(getGlucosaNotificationsCompl(4, dayOfWeek, glucRepository.findByGlu4miAndPacidfk(true, idPaciente), idPacienteList));
                    gluMi.addAll(getGlucosaNotificationsCompl(5, dayOfWeek, glucRepository.findByGlu5miAndPacidfk(true, idPaciente), idPacienteList));
                    gluMi.addAll(getGlucosaNotificationsCompl(6, dayOfWeek, glucRepository.findByGlu6miAndPacidfk(true, idPaciente), idPacienteList));
                    gluMi.addAll(getGlucosaNotificationsCompl(7, dayOfWeek, glucRepository.findByGlu7miAndPacidfk(true, idPaciente), idPacienteList));
                    gluMi.addAll(getGlucosaNotificationsCompl(10, dayOfWeek, glucRepository.findByGlu8miAndPacidfk(true, idPaciente), idPacienteList));
                    hashMapResponse.put("glucosa", gluMi);

                    List<ControlNotificacionViewList> nutriMi;
                    nutriMi = getNutriNotificationsCompl(1,dayOfWeek, nutriRepository.findByNutri1miAndPacidfk(true, idPaciente), idPacienteList);
                    nutriMi.addAll(getNutriNotificationsCompl(2,dayOfWeek, nutriRepository.findByNutri2miAndPacidfk(true, idPaciente), idPacienteList));
                    nutriMi.addAll(getNutriNotificationsCompl(3,dayOfWeek, nutriRepository.findByNutri3miAndPacidfk(true, idPaciente), idPacienteList));
                    hashMapResponse.put("nutricion", nutriMi);

                    List<ControlNotificacionViewList> presionArtMi;
                    presionArtMi = getPresionArterialNotificationsCompl(1, dayOfWeek, paRepository.findBySys1miAndPacidfk(true, idPaciente), idPacienteList);
                    presionArtMi.addAll(getPresionArterialNotificationsCompl(2, dayOfWeek, paRepository.findBySys2miAndPacidfk(true, idPaciente), idPacienteList));
                    presionArtMi.addAll(getPresionArterialNotificationsCompl(3, dayOfWeek, paRepository.findBySys3miAndPacidfk(true, idPaciente), idPacienteList));
                    presionArtMi.addAll(getPresionArterialNotificationsCompl(4, dayOfWeek, paRepository.findBySys4miAndPacidfk(true, idPaciente), idPacienteList));
                    presionArtMi.addAll(getPresionArterialNotificationsCompl(5, dayOfWeek, paRepository.findBySys5miAndPacidfk(true, idPaciente), idPacienteList));
                    presionArtMi.addAll(getPresionArterialNotificationsCompl(6, dayOfWeek, paRepository.findBySys6miAndPacidfk(true, idPaciente), idPacienteList));
                    presionArtMi.addAll(getPresionArterialNotificationsCompl(7, dayOfWeek, paRepository.findBySys7miAndPacidfk(true, idPaciente), idPacienteList));
                    hashMapResponse.put("presionArt", presionArtMi);
                    break;
                case Calendar.THURSDAY:
                    List<ControlNotificacionViewList> covidJu;
                    covidJu = getCovidNotificationsCompl(1, dayOfWeek, covidRepository.findByCovid1juAndPacidfk(true, idPaciente), idPacienteList);
                    covidJu.addAll(getCovidNotificationsCompl(2, dayOfWeek, covidRepository.findByCovid2juAndPacidfk(true, idPaciente), idPacienteList));
                    covidJu.addAll(getCovidNotificationsCompl(3, dayOfWeek, covidRepository.findByCovid3juAndPacidfk(true, idPaciente), idPacienteList));
                    covidJu.addAll(getCovidNotificationsCompl(4, dayOfWeek, covidRepository.findByCovid4juAndPacidfk(true, idPaciente), idPacienteList));
                    hashMapResponse.put("covid", covidJu);

                    List<ControlNotificacionViewList> gluJu;
                    gluJu = getGlucosaNotificationsCompl(1, dayOfWeek, glucRepository.findByGlu1juAndPacidfk(true, idPaciente), idPacienteList);
                    gluJu.addAll(getGlucosaNotificationsCompl(2, dayOfWeek, glucRepository.findByGlu2juAndPacidfk(true, idPaciente), idPacienteList));
                    gluJu.addAll(getGlucosaNotificationsCompl(3, dayOfWeek, glucRepository.findByGlu3juAndPacidfk(true, idPaciente), idPacienteList));
                    gluJu.addAll(getGlucosaNotificationsCompl(4, dayOfWeek, glucRepository.findByGlu4juAndPacidfk(true, idPaciente), idPacienteList));
                    gluJu.addAll(getGlucosaNotificationsCompl(5, dayOfWeek, glucRepository.findByGlu5juAndPacidfk(true, idPaciente), idPacienteList));
                    gluJu.addAll(getGlucosaNotificationsCompl(6, dayOfWeek, glucRepository.findByGlu6juAndPacidfk(true, idPaciente), idPacienteList));
                    gluJu.addAll(getGlucosaNotificationsCompl(7, dayOfWeek, glucRepository.findByGlu7juAndPacidfk(true, idPaciente), idPacienteList));
                    gluJu.addAll(getGlucosaNotificationsCompl(10, dayOfWeek, glucRepository.findByGlu8juAndPacidfk(true, idPaciente), idPacienteList));
                    hashMapResponse.put("glucosa", gluJu);

                    List<ControlNotificacionViewList> nutriJu;
                    nutriJu = getNutriNotificationsCompl(1,dayOfWeek, nutriRepository.findByNutri1juAndPacidfk(true, idPaciente), idPacienteList);
                    nutriJu.addAll(getNutriNotificationsCompl(2,dayOfWeek, nutriRepository.findByNutri2juAndPacidfk(true, idPaciente), idPacienteList));
                    nutriJu.addAll(getNutriNotificationsCompl(3,dayOfWeek, nutriRepository.findByNutri3juAndPacidfk(true, idPaciente), idPacienteList));
                    hashMapResponse.put("nutricion", nutriJu);

                    List<ControlNotificacionViewList> presionArtJu;
                    presionArtJu = getPresionArterialNotificationsCompl(1, dayOfWeek, paRepository.findBySys1juAndPacidfk(true, idPaciente), idPacienteList);
                    presionArtJu.addAll(getPresionArterialNotificationsCompl(2, dayOfWeek, paRepository.findBySys2juAndPacidfk(true, idPaciente), idPacienteList));
                    presionArtJu.addAll(getPresionArterialNotificationsCompl(3, dayOfWeek, paRepository.findBySys3juAndPacidfk(true, idPaciente), idPacienteList));
                    presionArtJu.addAll(getPresionArterialNotificationsCompl(4, dayOfWeek, paRepository.findBySys4juAndPacidfk(true, idPaciente), idPacienteList));
                    presionArtJu.addAll(getPresionArterialNotificationsCompl(5, dayOfWeek, paRepository.findBySys5juAndPacidfk(true, idPaciente), idPacienteList));
                    presionArtJu.addAll(getPresionArterialNotificationsCompl(6, dayOfWeek, paRepository.findBySys6juAndPacidfk(true, idPaciente), idPacienteList));
                    presionArtJu.addAll(getPresionArterialNotificationsCompl(7, dayOfWeek, paRepository.findBySys7juAndPacidfk(true, idPaciente), idPacienteList));
                    hashMapResponse.put("presionArt", presionArtJu);
                    break;
                case Calendar.FRIDAY:
                    List<ControlNotificacionViewList> covidVi;
                    covidVi = getCovidNotificationsCompl(1, dayOfWeek, covidRepository.findByCovid1viAndPacidfk(true, idPaciente), idPacienteList);
                    covidVi.addAll(getCovidNotificationsCompl(2, dayOfWeek, covidRepository.findByCovid2viAndPacidfk(true, idPaciente), idPacienteList));
                    covidVi.addAll(getCovidNotificationsCompl(3, dayOfWeek, covidRepository.findByCovid3viAndPacidfk(true, idPaciente), idPacienteList));
                    covidVi.addAll(getCovidNotificationsCompl(4, dayOfWeek, covidRepository.findByCovid4viAndPacidfk(true, idPaciente), idPacienteList));
                    hashMapResponse.put("covid", covidVi);

                    List<ControlNotificacionViewList> gluVi;
                    gluVi = getGlucosaNotificationsCompl(1, dayOfWeek, glucRepository.findByGlu1viAndPacidfk(true, idPaciente), idPacienteList);
                    gluVi.addAll(getGlucosaNotificationsCompl(2, dayOfWeek, glucRepository.findByGlu2viAndPacidfk(true, idPaciente), idPacienteList));
                    gluVi.addAll(getGlucosaNotificationsCompl(3, dayOfWeek, glucRepository.findByGlu3viAndPacidfk(true, idPaciente), idPacienteList));
                    gluVi.addAll(getGlucosaNotificationsCompl(4, dayOfWeek, glucRepository.findByGlu4viAndPacidfk(true, idPaciente), idPacienteList));
                    gluVi.addAll(getGlucosaNotificationsCompl(5, dayOfWeek, glucRepository.findByGlu5viAndPacidfk(true, idPaciente), idPacienteList));
                    gluVi.addAll(getGlucosaNotificationsCompl(6, dayOfWeek, glucRepository.findByGlu6viAndPacidfk(true, idPaciente), idPacienteList));
                    gluVi.addAll(getGlucosaNotificationsCompl(7, dayOfWeek, glucRepository.findByGlu7viAndPacidfk(true, idPaciente), idPacienteList));
                    gluVi.addAll(getGlucosaNotificationsCompl(10, dayOfWeek, glucRepository.findByGlu8viAndPacidfk(true, idPaciente), idPacienteList));
                    hashMapResponse.put("glucosa", gluVi);

                    List<ControlNotificacionViewList> nutrivi;
                    nutrivi = getNutriNotificationsCompl(1,dayOfWeek, nutriRepository.findByNutri1viAndPacidfk(true, idPaciente), idPacienteList);
                    nutrivi.addAll(getNutriNotificationsCompl(2,dayOfWeek, nutriRepository.findByNutri2viAndPacidfk(true, idPaciente), idPacienteList));
                    nutrivi.addAll(getNutriNotificationsCompl(3,dayOfWeek, nutriRepository.findByNutri3viAndPacidfk(true, idPaciente), idPacienteList));
                    hashMapResponse.put("nutricion", nutrivi);

                    List<ControlNotificacionViewList> presionArtVi;
                    presionArtVi = getPresionArterialNotificationsCompl(1, dayOfWeek, paRepository.findBySys1viAndPacidfk(true, idPaciente), idPacienteList);
                    presionArtVi.addAll(getPresionArterialNotificationsCompl(2, dayOfWeek, paRepository.findBySys2viAndPacidfk(true, idPaciente), idPacienteList));
                    presionArtVi.addAll(getPresionArterialNotificationsCompl(3, dayOfWeek, paRepository.findBySys3viAndPacidfk(true, idPaciente), idPacienteList));
                    presionArtVi.addAll(getPresionArterialNotificationsCompl(4, dayOfWeek, paRepository.findBySys4viAndPacidfk(true, idPaciente), idPacienteList));
                    presionArtVi.addAll(getPresionArterialNotificationsCompl(5, dayOfWeek, paRepository.findBySys5viAndPacidfk(true, idPaciente), idPacienteList));
                    presionArtVi.addAll(getPresionArterialNotificationsCompl(6, dayOfWeek, paRepository.findBySys6viAndPacidfk(true, idPaciente), idPacienteList));
                    presionArtVi.addAll(getPresionArterialNotificationsCompl(7, dayOfWeek, paRepository.findBySys7viAndPacidfk(true, idPaciente), idPacienteList));
                    hashMapResponse.put("presionArt", presionArtVi);
                    break;
                case Calendar.SATURDAY:
                    List<ControlNotificacionViewList> covidSa;
                    covidSa = getCovidNotificationsCompl(1, dayOfWeek, covidRepository.findByCovid1saAndPacidfk(true, idPaciente), idPacienteList);
                    covidSa.addAll(getCovidNotificationsCompl(2, dayOfWeek, covidRepository.findByCovid2saAndPacidfk(true, idPaciente), idPacienteList));
                    covidSa.addAll(getCovidNotificationsCompl(3, dayOfWeek, covidRepository.findByCovid3saAndPacidfk(true, idPaciente), idPacienteList));
                    covidSa.addAll(getCovidNotificationsCompl(4, dayOfWeek, covidRepository.findByCovid4saAndPacidfk(true, idPaciente), idPacienteList));
                    hashMapResponse.put("covid", covidSa);

                    List<ControlNotificacionViewList> gluSa;
                    gluSa = getGlucosaNotificationsCompl(1, dayOfWeek, glucRepository.findByGlu1saAndPacidfk(true, idPaciente), idPacienteList);
                    gluSa.addAll(getGlucosaNotificationsCompl(2, dayOfWeek, glucRepository.findByGlu2saAndPacidfk(true, idPaciente), idPacienteList));
                    gluSa.addAll(getGlucosaNotificationsCompl(3, dayOfWeek, glucRepository.findByGlu3saAndPacidfk(true, idPaciente), idPacienteList));
                    gluSa.addAll(getGlucosaNotificationsCompl(4, dayOfWeek, glucRepository.findByGlu4saAndPacidfk(true, idPaciente), idPacienteList));
                    gluSa.addAll(getGlucosaNotificationsCompl(5, dayOfWeek, glucRepository.findByGlu5saAndPacidfk(true, idPaciente), idPacienteList));
                    gluSa.addAll(getGlucosaNotificationsCompl(6, dayOfWeek, glucRepository.findByGlu6saAndPacidfk(true, idPaciente), idPacienteList));
                    gluSa.addAll(getGlucosaNotificationsCompl(7, dayOfWeek, glucRepository.findByGlu7saAndPacidfk(true, idPaciente), idPacienteList));
                    gluSa.addAll(getGlucosaNotificationsCompl(10, dayOfWeek, glucRepository.findByGlu8saAndPacidfk(true, idPaciente), idPacienteList));
                    hashMapResponse.put("glucosa", gluSa);

                    List<ControlNotificacionViewList> nutriSa;
                    nutriSa = getNutriNotificationsCompl(1,dayOfWeek, nutriRepository.findByNutri1saAndPacidfk(true, idPaciente), idPacienteList);
                    nutriSa.addAll(getNutriNotificationsCompl(2,dayOfWeek, nutriRepository.findByNutri2saAndPacidfk(true, idPaciente), idPacienteList));
                    nutriSa.addAll(getNutriNotificationsCompl(3,dayOfWeek, nutriRepository.findByNutri3saAndPacidfk(true, idPaciente), idPacienteList));
                    hashMapResponse.put("nutricion", nutriSa);

                    List<ControlNotificacionViewList> presionArtSa;
                    presionArtSa = getPresionArterialNotificationsCompl(1, dayOfWeek, paRepository.findBySys1saAndPacidfk(true, idPaciente), idPacienteList);
                    presionArtSa.addAll(getPresionArterialNotificationsCompl(2, dayOfWeek, paRepository.findBySys2saAndPacidfk(true, idPaciente), idPacienteList));
                    presionArtSa.addAll(getPresionArterialNotificationsCompl(3, dayOfWeek, paRepository.findBySys3saAndPacidfk(true, idPaciente), idPacienteList));
                    presionArtSa.addAll(getPresionArterialNotificationsCompl(4, dayOfWeek, paRepository.findBySys4saAndPacidfk(true, idPaciente), idPacienteList));
                    presionArtSa.addAll(getPresionArterialNotificationsCompl(5, dayOfWeek, paRepository.findBySys5saAndPacidfk(true, idPaciente), idPacienteList));
                    presionArtSa.addAll(getPresionArterialNotificationsCompl(6, dayOfWeek, paRepository.findBySys6saAndPacidfk(true, idPaciente), idPacienteList));
                    presionArtSa.addAll(getPresionArterialNotificationsCompl(7, dayOfWeek, paRepository.findBySys7saAndPacidfk(true, idPaciente), idPacienteList));
                    hashMapResponse.put("presionArt", presionArtSa);
                    break;
                default:
                    throw new ControlNotificacionException("Opcion de dia no es correcta", GenericException.LAYER_DAO, GenericException.ACTION_VALIDATE);
            }
            return hashMapResponse;
        } catch (Exception ex) {
            logger.error("Error: ", ex);
            throw new ControlNotificacionException("Error al obtener horarios para notificaciones - " + ex.getMessage(), GenericException.LAYER_DAO, GenericException.ACTION_SELECT);
        }
    }

    @Override
    public HashMap<String, List<ControlNotificacionSimpView>> getNotificationsByDay() throws ControlNotificacionException {
        try {
            Calendar calendar = Calendar.getInstance();
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            HashMap<String, List<ControlNotificacionSimpView>> hashMapResponse = new HashMap<>();
            switch(dayOfWeek) {
                case Calendar.SUNDAY:
                    List<ControlNotificacionSimpView> covidDo;
                    covidDo = getCovidNotifications(1, dayOfWeek, covidRepository.findByCovid1do(true));
                    covidDo.addAll(getCovidNotifications(2, dayOfWeek, covidRepository.findByCovid2do(true)));
                    covidDo.addAll(getCovidNotifications(3, dayOfWeek, covidRepository.findByCovid3do(true)));
                    covidDo.addAll(getCovidNotifications(4, dayOfWeek, covidRepository.findByCovid4do(true)));
                    hashMapResponse.put("covid", covidDo);

                    List<ControlNotificacionSimpView> gluDo;
                    gluDo = getGlucosaNotifications(1, dayOfWeek, glucRepository.findByGlu1do(true));
                    gluDo.addAll(getGlucosaNotifications(2, dayOfWeek, glucRepository.findByGlu2do(true)));
                    gluDo.addAll(getGlucosaNotifications(3, dayOfWeek, glucRepository.findByGlu3do(true)));
                    gluDo.addAll(getGlucosaNotifications(4, dayOfWeek, glucRepository.findByGlu4do(true)));
                    gluDo.addAll(getGlucosaNotifications(5, dayOfWeek, glucRepository.findByGlu5do(true)));
                    gluDo.addAll(getGlucosaNotifications(6, dayOfWeek, glucRepository.findByGlu6do(true)));
                    gluDo.addAll(getGlucosaNotifications(7, dayOfWeek, glucRepository.findByGlu7do(true)));
                    gluDo.addAll(getGlucosaNotifications(10, dayOfWeek, glucRepository.findByGlu8do(true)));
                    hashMapResponse.put("glucosa", gluDo);

                    List<ControlNotificacionSimpView> nutriDo;
                    nutriDo = getNutriNotifications(1,dayOfWeek, nutriRepository.findByNutri1do(true));
                    nutriDo.addAll(getNutriNotifications(2,dayOfWeek, nutriRepository.findByNutri2do(true)));
                    nutriDo.addAll(getNutriNotifications(3,dayOfWeek, nutriRepository.findByNutri3do(true)));
                    hashMapResponse.put("nutricion", nutriDo);

                    List<ControlNotificacionSimpView> presionArtDo;
                    presionArtDo = getPresionArterialNotifications(1, dayOfWeek, paRepository.findBySys1do(true));
                    presionArtDo.addAll(getPresionArterialNotifications(2, dayOfWeek, paRepository.findBySys2do(true)));
                    presionArtDo.addAll(getPresionArterialNotifications(3, dayOfWeek, paRepository.findBySys3do(true)));
                    presionArtDo.addAll(getPresionArterialNotifications(4, dayOfWeek, paRepository.findBySys4do(true)));
                    presionArtDo.addAll(getPresionArterialNotifications(5, dayOfWeek, paRepository.findBySys5do(true)));
                    presionArtDo.addAll(getPresionArterialNotifications(6, dayOfWeek, paRepository.findBySys6do(true)));
                    presionArtDo.addAll(getPresionArterialNotifications(7, dayOfWeek, paRepository.findBySys7do(true)));
                    hashMapResponse.put("presionArt", presionArtDo);
                    break;
                case Calendar.MONDAY:
                    List<ControlNotificacionSimpView> covidLu;
                    covidLu = getCovidNotifications(1, dayOfWeek, covidRepository.findByCovid1lu(true));
                    covidLu.addAll(getCovidNotifications(2, dayOfWeek, covidRepository.findByCovid2lu(true)));
                    covidLu.addAll(getCovidNotifications(3, dayOfWeek, covidRepository.findByCovid3lu(true)));
                    covidLu.addAll(getCovidNotifications(4, dayOfWeek, covidRepository.findByCovid4lu(true)));
                    hashMapResponse.put("covid", covidLu);

                    List<ControlNotificacionSimpView> gluLu;
                    gluLu = getGlucosaNotifications(1, dayOfWeek, glucRepository.findByGlu1lu(true));
                    gluLu.addAll(getGlucosaNotifications(2, dayOfWeek, glucRepository.findByGlu2lu(true)));
                    gluLu.addAll(getGlucosaNotifications(3, dayOfWeek, glucRepository.findByGlu3lu(true)));
                    gluLu.addAll(getGlucosaNotifications(4, dayOfWeek, glucRepository.findByGlu4lu(true)));
                    gluLu.addAll(getGlucosaNotifications(5, dayOfWeek, glucRepository.findByGlu5lu(true)));
                    gluLu.addAll(getGlucosaNotifications(6, dayOfWeek, glucRepository.findByGlu6lu(true)));
                    gluLu.addAll(getGlucosaNotifications(7, dayOfWeek, glucRepository.findByGlu7lu(true)));
                    gluLu.addAll(getGlucosaNotifications(10, dayOfWeek, glucRepository.findByGlu8lu(true)));
                    hashMapResponse.put("glucosa", gluLu);

                    List<ControlNotificacionSimpView> nutriLu;
                    nutriLu = getNutriNotifications(1,dayOfWeek, nutriRepository.findByNutri1lu(true));
                    nutriLu.addAll(getNutriNotifications(2,dayOfWeek, nutriRepository.findByNutri2lu(true)));
                    nutriLu.addAll(getNutriNotifications(3,dayOfWeek, nutriRepository.findByNutri3lu(true)));
                    hashMapResponse.put("nutricion", nutriLu);

                    List<ControlNotificacionSimpView> presionArtLu;
                    presionArtLu = getPresionArterialNotifications(1, dayOfWeek, paRepository.findBySys1lu(true));
                    presionArtLu.addAll(getPresionArterialNotifications(2, dayOfWeek, paRepository.findBySys2lu(true)));
                    presionArtLu.addAll(getPresionArterialNotifications(3, dayOfWeek, paRepository.findBySys3lu(true)));
                    presionArtLu.addAll(getPresionArterialNotifications(4, dayOfWeek, paRepository.findBySys4lu(true)));
                    presionArtLu.addAll(getPresionArterialNotifications(5, dayOfWeek, paRepository.findBySys5lu(true)));
                    presionArtLu.addAll(getPresionArterialNotifications(6, dayOfWeek, paRepository.findBySys6lu(true)));
                    presionArtLu.addAll(getPresionArterialNotifications(7, dayOfWeek, paRepository.findBySys7lu(true)));
                    hashMapResponse.put("presionArt", presionArtLu);
                    break;
                case Calendar.TUESDAY:
                    List<ControlNotificacionSimpView> covidMa;
                    covidMa = getCovidNotifications(1, dayOfWeek, covidRepository.findByCovid1ma(true));
                    covidMa.addAll(getCovidNotifications(2, dayOfWeek, covidRepository.findByCovid2ma(true)));
                    covidMa.addAll(getCovidNotifications(3, dayOfWeek, covidRepository.findByCovid3ma(true)));
                    covidMa.addAll(getCovidNotifications(4, dayOfWeek, covidRepository.findByCovid4ma(true)));
                    hashMapResponse.put("covid", covidMa);

                    List<ControlNotificacionSimpView> gluMa;
                    gluMa = getGlucosaNotifications(1, dayOfWeek, glucRepository.findByGlu1ma(true));
                    gluMa.addAll(getGlucosaNotifications(2, dayOfWeek, glucRepository.findByGlu2ma(true)));
                    gluMa.addAll(getGlucosaNotifications(3, dayOfWeek, glucRepository.findByGlu3ma(true)));
                    gluMa.addAll(getGlucosaNotifications(4, dayOfWeek, glucRepository.findByGlu4ma(true)));
                    gluMa.addAll(getGlucosaNotifications(5, dayOfWeek, glucRepository.findByGlu5ma(true)));
                    gluMa.addAll(getGlucosaNotifications(6, dayOfWeek, glucRepository.findByGlu6ma(true)));
                    gluMa.addAll(getGlucosaNotifications(7, dayOfWeek, glucRepository.findByGlu7ma(true)));
                    gluMa.addAll(getGlucosaNotifications(10, dayOfWeek, glucRepository.findByGlu8ma(true)));
                    hashMapResponse.put("glucosa", gluMa);

                    List<ControlNotificacionSimpView> nutriMa;
                    nutriMa = getNutriNotifications(1,dayOfWeek, nutriRepository.findByNutri1ma(true));
                    nutriMa.addAll(getNutriNotifications(2,dayOfWeek, nutriRepository.findByNutri2ma(true)));
                    nutriMa.addAll(getNutriNotifications(3,dayOfWeek, nutriRepository.findByNutri3ma(true)));
                    hashMapResponse.put("nutricion", nutriMa);

                    List<ControlNotificacionSimpView> presionArtMa;
                    presionArtMa = getPresionArterialNotifications(1, dayOfWeek, paRepository.findBySys1ma(true));
                    presionArtMa.addAll(getPresionArterialNotifications(2, dayOfWeek, paRepository.findBySys2ma(true)));
                    presionArtMa.addAll(getPresionArterialNotifications(3, dayOfWeek, paRepository.findBySys3ma(true)));
                    presionArtMa.addAll(getPresionArterialNotifications(4, dayOfWeek, paRepository.findBySys4ma(true)));
                    presionArtMa.addAll(getPresionArterialNotifications(5, dayOfWeek, paRepository.findBySys5ma(true)));
                    presionArtMa.addAll(getPresionArterialNotifications(6, dayOfWeek, paRepository.findBySys6ma(true)));
                    presionArtMa.addAll(getPresionArterialNotifications(7, dayOfWeek, paRepository.findBySys7ma(true)));
                    hashMapResponse.put("presionArt", presionArtMa);
                    break;
                case Calendar.WEDNESDAY:
                    List<ControlNotificacionSimpView> covidMi;
                    covidMi = getCovidNotifications(1, dayOfWeek, covidRepository.findByCovid1mi(true));
                    covidMi.addAll(getCovidNotifications(2, dayOfWeek, covidRepository.findByCovid2mi(true)));
                    covidMi.addAll(getCovidNotifications(3, dayOfWeek, covidRepository.findByCovid3mi(true)));
                    covidMi.addAll(getCovidNotifications(4, dayOfWeek, covidRepository.findByCovid4mi(true)));
                    hashMapResponse.put("covid", covidMi);

                    List<ControlNotificacionSimpView> gluMi;
                    gluMi = getGlucosaNotifications(1, dayOfWeek, glucRepository.findByGlu1mi(true));
                    gluMi.addAll(getGlucosaNotifications(2, dayOfWeek, glucRepository.findByGlu2mi(true)));
                    gluMi.addAll(getGlucosaNotifications(3, dayOfWeek, glucRepository.findByGlu3mi(true)));
                    gluMi.addAll(getGlucosaNotifications(4, dayOfWeek, glucRepository.findByGlu4mi(true)));
                    gluMi.addAll(getGlucosaNotifications(5, dayOfWeek, glucRepository.findByGlu5mi(true)));
                    gluMi.addAll(getGlucosaNotifications(6, dayOfWeek, glucRepository.findByGlu6mi(true)));
                    gluMi.addAll(getGlucosaNotifications(7, dayOfWeek, glucRepository.findByGlu7mi(true)));
                    gluMi.addAll(getGlucosaNotifications(10, dayOfWeek, glucRepository.findByGlu8mi(true)));
                    hashMapResponse.put("glucosa", gluMi);

                    List<ControlNotificacionSimpView> nutriMi;
                    nutriMi = getNutriNotifications(1,dayOfWeek, nutriRepository.findByNutri1mi(true));
                    nutriMi.addAll(getNutriNotifications(2,dayOfWeek, nutriRepository.findByNutri2mi(true)));
                    nutriMi.addAll(getNutriNotifications(3,dayOfWeek, nutriRepository.findByNutri3mi(true)));
                    hashMapResponse.put("nutricion", nutriMi);

                    List<ControlNotificacionSimpView> presionArtMi;
                    presionArtMi = getPresionArterialNotifications(1, dayOfWeek, paRepository.findBySys1mi(true));
                    presionArtMi.addAll(getPresionArterialNotifications(2, dayOfWeek, paRepository.findBySys2mi(true)));
                    presionArtMi.addAll(getPresionArterialNotifications(3, dayOfWeek, paRepository.findBySys3mi(true)));
                    presionArtMi.addAll(getPresionArterialNotifications(4, dayOfWeek, paRepository.findBySys4mi(true)));
                    presionArtMi.addAll(getPresionArterialNotifications(5, dayOfWeek, paRepository.findBySys5mi(true)));
                    presionArtMi.addAll(getPresionArterialNotifications(6, dayOfWeek, paRepository.findBySys6mi(true)));
                    presionArtMi.addAll(getPresionArterialNotifications(7, dayOfWeek, paRepository.findBySys7mi(true)));
                    hashMapResponse.put("presionArt", presionArtMi);
                    break;
                case Calendar.THURSDAY:
                    List<ControlNotificacionSimpView> covidJu;
                    covidJu = getCovidNotifications(1, dayOfWeek, covidRepository.findByCovid1ju(true));
                    covidJu.addAll(getCovidNotifications(2, dayOfWeek, covidRepository.findByCovid2ju(true)));
                    covidJu.addAll(getCovidNotifications(3, dayOfWeek, covidRepository.findByCovid3ju(true)));
                    covidJu.addAll(getCovidNotifications(4, dayOfWeek, covidRepository.findByCovid4ju(true)));
                    hashMapResponse.put("covid", covidJu);

                    List<ControlNotificacionSimpView> gluJu;
                    gluJu = getGlucosaNotifications(1, dayOfWeek, glucRepository.findByGlu1ju(true));
                    gluJu.addAll(getGlucosaNotifications(2, dayOfWeek, glucRepository.findByGlu2ju(true)));
                    gluJu.addAll(getGlucosaNotifications(3, dayOfWeek, glucRepository.findByGlu3ju(true)));
                    gluJu.addAll(getGlucosaNotifications(4, dayOfWeek, glucRepository.findByGlu4ju(true)));
                    gluJu.addAll(getGlucosaNotifications(5, dayOfWeek, glucRepository.findByGlu5ju(true)));
                    gluJu.addAll(getGlucosaNotifications(6, dayOfWeek, glucRepository.findByGlu6ju(true)));
                    gluJu.addAll(getGlucosaNotifications(7, dayOfWeek, glucRepository.findByGlu7ju(true)));
                    gluJu.addAll(getGlucosaNotifications(10, dayOfWeek, glucRepository.findByGlu8ju(true)));
                    hashMapResponse.put("glucosa", covidJu);

                    List<ControlNotificacionSimpView> nutriJu;
                    nutriJu = getNutriNotifications(1,dayOfWeek, nutriRepository.findByNutri1ju(true));
                    nutriJu.addAll(getNutriNotifications(2,dayOfWeek, nutriRepository.findByNutri2ju(true)));
                    nutriJu.addAll(getNutriNotifications(3,dayOfWeek, nutriRepository.findByNutri3ju(true)));
                    hashMapResponse.put("nutricion", nutriJu);

                    List<ControlNotificacionSimpView> presionArtJu;
                    presionArtJu = getPresionArterialNotifications(1, dayOfWeek, paRepository.findBySys1ju(true));
                    presionArtJu.addAll(getPresionArterialNotifications(2, dayOfWeek, paRepository.findBySys2ju(true)));
                    presionArtJu.addAll(getPresionArterialNotifications(3, dayOfWeek, paRepository.findBySys3ju(true)));
                    presionArtJu.addAll(getPresionArterialNotifications(4, dayOfWeek, paRepository.findBySys4ju(true)));
                    presionArtJu.addAll(getPresionArterialNotifications(5, dayOfWeek, paRepository.findBySys5ju(true)));
                    presionArtJu.addAll(getPresionArterialNotifications(6, dayOfWeek, paRepository.findBySys6ju(true)));
                    presionArtJu.addAll(getPresionArterialNotifications(7, dayOfWeek, paRepository.findBySys7ju(true)));
                    hashMapResponse.put("presionArt", presionArtJu);
                    break;
                case Calendar.FRIDAY:
                    List<ControlNotificacionSimpView> covidVi;
                    covidVi = getCovidNotifications(1, dayOfWeek, covidRepository.findByCovid1vi(true));
                    covidVi.addAll(getCovidNotifications(2, dayOfWeek, covidRepository.findByCovid2vi(true)));
                    covidVi.addAll(getCovidNotifications(3, dayOfWeek, covidRepository.findByCovid3vi(true)));
                    covidVi.addAll(getCovidNotifications(4, dayOfWeek, covidRepository.findByCovid4vi(true)));
                    hashMapResponse.put("covid", covidVi);

                    List<ControlNotificacionSimpView> gluVi;
                    gluVi = getGlucosaNotifications(1, dayOfWeek, glucRepository.findByGlu1vi(true));
                    gluVi.addAll(getGlucosaNotifications(2, dayOfWeek, glucRepository.findByGlu2vi(true)));
                    gluVi.addAll(getGlucosaNotifications(3, dayOfWeek, glucRepository.findByGlu3vi(true)));
                    gluVi.addAll(getGlucosaNotifications(4, dayOfWeek, glucRepository.findByGlu4vi(true)));
                    gluVi.addAll(getGlucosaNotifications(5, dayOfWeek, glucRepository.findByGlu5vi(true)));
                    gluVi.addAll(getGlucosaNotifications(6, dayOfWeek, glucRepository.findByGlu6vi(true)));
                    gluVi.addAll(getGlucosaNotifications(7, dayOfWeek, glucRepository.findByGlu7vi(true)));
                    gluVi.addAll(getGlucosaNotifications(10, dayOfWeek, glucRepository.findByGlu8vi(true)));
                    hashMapResponse.put("glucosa", gluVi);

                    List<ControlNotificacionSimpView> nutrivi;
                    nutrivi = getNutriNotifications(1,dayOfWeek, nutriRepository.findByNutri1vi(true));
                    nutrivi.addAll(getNutriNotifications(2,dayOfWeek, nutriRepository.findByNutri2vi(true)));
                    nutrivi.addAll(getNutriNotifications(3,dayOfWeek, nutriRepository.findByNutri3vi(true)));
                    hashMapResponse.put("nutricion", nutrivi);

                    List<ControlNotificacionSimpView> presionArtVi;
                    presionArtVi = getPresionArterialNotifications(1, dayOfWeek, paRepository.findBySys1vi(true));
                    presionArtVi.addAll(getPresionArterialNotifications(2, dayOfWeek, paRepository.findBySys2vi(true)));
                    presionArtVi.addAll(getPresionArterialNotifications(3, dayOfWeek, paRepository.findBySys3vi(true)));
                    presionArtVi.addAll(getPresionArterialNotifications(4, dayOfWeek, paRepository.findBySys4vi(true)));
                    presionArtVi.addAll(getPresionArterialNotifications(5, dayOfWeek, paRepository.findBySys5vi(true)));
                    presionArtVi.addAll(getPresionArterialNotifications(6, dayOfWeek, paRepository.findBySys6vi(true)));
                    presionArtVi.addAll(getPresionArterialNotifications(7, dayOfWeek, paRepository.findBySys7vi(true)));
                    hashMapResponse.put("presionArt", presionArtVi);
                    break;
                case Calendar.SATURDAY:
                    List<ControlNotificacionSimpView> covidSa;
                    covidSa = getCovidNotifications(1, dayOfWeek, covidRepository.findByCovid1sa(true));
                    covidSa.addAll(getCovidNotifications(2, dayOfWeek, covidRepository.findByCovid2sa(true)));
                    covidSa.addAll(getCovidNotifications(3, dayOfWeek, covidRepository.findByCovid3sa(true)));
                    covidSa.addAll(getCovidNotifications(4, dayOfWeek, covidRepository.findByCovid4sa(true)));
                    hashMapResponse.put("covid", covidSa);

                    List<ControlNotificacionSimpView> gluSa;
                    gluSa = getGlucosaNotifications(1, dayOfWeek, glucRepository.findByGlu1sa(true));
                    gluSa.addAll(getGlucosaNotifications(2, dayOfWeek, glucRepository.findByGlu2sa(true)));
                    gluSa.addAll(getGlucosaNotifications(3, dayOfWeek, glucRepository.findByGlu3sa(true)));
                    gluSa.addAll(getGlucosaNotifications(4, dayOfWeek, glucRepository.findByGlu4sa(true)));
                    gluSa.addAll(getGlucosaNotifications(5, dayOfWeek, glucRepository.findByGlu5sa(true)));
                    gluSa.addAll(getGlucosaNotifications(6, dayOfWeek, glucRepository.findByGlu6sa(true)));
                    gluSa.addAll(getGlucosaNotifications(7, dayOfWeek, glucRepository.findByGlu7sa(true)));
                    gluSa.addAll(getGlucosaNotifications(10, dayOfWeek, glucRepository.findByGlu8sa(true)));
                    hashMapResponse.put("glucosa", gluSa);

                    List<ControlNotificacionSimpView> nutriSa;
                    nutriSa = getNutriNotifications(1,dayOfWeek, nutriRepository.findByNutri1sa(true));
                    nutriSa.addAll(getNutriNotifications(2,dayOfWeek, nutriRepository.findByNutri2sa(true)));
                    nutriSa.addAll(getNutriNotifications(3,dayOfWeek, nutriRepository.findByNutri3sa(true)));
                    hashMapResponse.put("nutricion", nutriSa);

                    List<ControlNotificacionSimpView> presionArtSa;
                    presionArtSa = getPresionArterialNotifications(1, dayOfWeek, paRepository.findBySys1sa(true));
                    presionArtSa.addAll(getPresionArterialNotifications(2, dayOfWeek, paRepository.findBySys2sa(true)));
                    presionArtSa.addAll(getPresionArterialNotifications(3, dayOfWeek, paRepository.findBySys3sa(true)));
                    presionArtSa.addAll(getPresionArterialNotifications(4, dayOfWeek, paRepository.findBySys4sa(true)));
                    presionArtSa.addAll(getPresionArterialNotifications(5, dayOfWeek, paRepository.findBySys5sa(true)));
                    presionArtSa.addAll(getPresionArterialNotifications(6, dayOfWeek, paRepository.findBySys6sa(true)));
                    presionArtSa.addAll(getPresionArterialNotifications(7, dayOfWeek, paRepository.findBySys7sa(true)));
                    hashMapResponse.put("presionArt", presionArtSa);
                    break;
                default:
                    throw new ControlNotificacionException("Opcion de dia no es correcta", GenericException.LAYER_DAO, GenericException.ACTION_VALIDATE);
            }
            return hashMapResponse;
        } catch (Exception ex) {
            logger.error("Error: ", ex);
            throw new ControlNotificacionException("Error al obtener horarios para notificaciones - " + ex.getMessage(), GenericException.LAYER_DAO, GenericException.ACTION_SELECT);
        }
    }

    private List<ControlNotificacionSimpView> getPresionArterialNotifications(Integer periodo, Integer dayOfWeek, List<SaludIndPa> listPa) throws ControlNotificacionException {
        List<ControlNotificacionSimpView> response = new ArrayList<>();
        for(SaludIndPa element : listPa) {
            ControlNotificacionSimpView view = new ControlNotificacionSimpView();
            view.setIdPaciente(element.getPacidfk());
            switch(dayOfWeek) {
                case Calendar.SUNDAY:
                    if(element.getSys1do() && periodo == 1) {
                        view.setPeriodo(1);
                        view.setHora(element.getP1hora());
                    } else if(element.getSys2do() && periodo == 2) {
                        view.setPeriodo(2);
                        view.setHora(element.getP2hora());
                    } else if(element.getSys3do() && periodo == 3) {
                        view.setPeriodo(3);
                        view.setHora(element.getP3hora());
                    } else if(element.getSys4do() && periodo == 4) {
                        view.setPeriodo(4);
                        view.setHora(element.getP4hora());
                    } else if(element.getSys5do() && periodo == 5) {
                        view.setPeriodo(5);
                        view.setHora(element.getP5hora());
                    } else if(element.getSys6do() && periodo == 6) {
                        view.setPeriodo(6);
                        view.setHora(element.getP6hora());
                    } else if(element.getSys7do() && periodo == 7) {
                        view.setPeriodo(7);
                        view.setHora(element.getP7hora());
                    }
                    break;
                case Calendar.MONDAY:
                    if(element.getSys1lu() && periodo == 1) {
                        view.setPeriodo(1);
                        view.setHora(element.getP1hora());
                    } else if(element.getSys2lu() && periodo == 2) {
                        view.setPeriodo(2);
                        view.setHora(element.getP2hora());
                    } else if(element.getSys3lu() && periodo == 3) {
                        view.setPeriodo(3);
                        view.setHora(element.getP3hora());
                    } else if(element.getSys4lu() && periodo == 4) {
                        view.setPeriodo(4);
                        view.setHora(element.getP4hora());
                    } else if(element.getSys5lu() && periodo == 5) {
                        view.setPeriodo(5);
                        view.setHora(element.getP5hora());
                    } else if(element.getSys6lu() && periodo == 6) {
                        view.setPeriodo(6);
                        view.setHora(element.getP6hora());
                    } else if(element.getSys7lu() && periodo == 7) {
                        view.setPeriodo(7);
                        view.setHora(element.getP7hora());
                    }
                    break;
                case Calendar.TUESDAY:
                    if(element.getSys1ma() && periodo == 1) {
                        view.setPeriodo(1);
                        view.setHora(element.getP1hora());
                    } else if(element.getSys2ma() && periodo == 2) {
                        view.setPeriodo(2);
                        view.setHora(element.getP2hora());
                    } else if(element.getSys3ma() && periodo == 3) {
                        view.setPeriodo(3);
                        view.setHora(element.getP3hora());
                    } else if(element.getSys4ma() && periodo == 4) {
                        view.setPeriodo(4);
                        view.setHora(element.getP4hora());
                    } else if(element.getSys5ma() && periodo == 5) {
                        view.setPeriodo(5);
                        view.setHora(element.getP5hora());
                    } else if(element.getSys6ma() && periodo == 6) {
                        view.setPeriodo(6);
                        view.setHora(element.getP6hora());
                    } else if(element.getSys7ma() && periodo == 7) {
                        view.setPeriodo(7);
                        view.setHora(element.getP7hora());
                    }
                    break;
                case Calendar.WEDNESDAY:
                    if(element.getSys1mi() && periodo == 1) {
                        view.setPeriodo(1);
                        view.setHora(element.getP1hora());
                    } else if(element.getSys2mi() && periodo == 2) {
                        view.setPeriodo(2);
                        view.setHora(element.getP2hora());
                    } else if(element.getSys3mi() && periodo == 3) {
                        view.setPeriodo(3);
                        view.setHora(element.getP3hora());
                    } else if(element.getSys4mi() && periodo == 4) {
                        view.setPeriodo(4);
                        view.setHora(element.getP4hora());
                    } else if(element.getSys5mi() && periodo == 5) {
                        view.setPeriodo(5);
                        view.setHora(element.getP5hora());
                    } else if(element.getSys6mi() && periodo == 6) {
                        view.setPeriodo(6);
                        view.setHora(element.getP6hora());
                    } else if(element.getSys7mi() && periodo == 7) {
                        view.setPeriodo(7);
                        view.setHora(element.getP7hora());
                    }
                    break;
                case Calendar.THURSDAY:
                    if(element.getSys1ju() && periodo == 1) {
                        view.setPeriodo(1);
                        view.setHora(element.getP1hora());
                    } else if(element.getSys2ju() && periodo == 2) {
                        view.setPeriodo(2);
                        view.setHora(element.getP2hora());
                    } else if(element.getSys3ju() && periodo == 3) {
                        view.setPeriodo(3);
                        view.setHora(element.getP3hora());
                    } else if(element.getSys4ju() && periodo == 4) {
                        view.setPeriodo(4);
                        view.setHora(element.getP4hora());
                    } else if(element.getSys5ju() && periodo == 5) {
                        view.setPeriodo(5);
                        view.setHora(element.getP5hora());
                    } else if(element.getSys6ju() && periodo == 6) {
                        view.setPeriodo(6);
                        view.setHora(element.getP6hora());
                    } else if(element.getSys7ju() && periodo == 7) {
                        view.setPeriodo(7);
                        view.setHora(element.getP7hora());
                    }
                    break;
                case Calendar.FRIDAY:
                    if(element.getSys1vi() && periodo == 1) {
                        view.setPeriodo(1);
                        view.setHora(element.getP1hora());
                    } else if(element.getSys2vi() && periodo == 2) {
                        view.setPeriodo(2);
                        view.setHora(element.getP2hora());
                    } else if(element.getSys3vi() && periodo == 3) {
                        view.setPeriodo(3);
                        view.setHora(element.getP3hora());
                    } else if(element.getSys4vi() && periodo == 4) {
                        view.setPeriodo(4);
                        view.setHora(element.getP4hora());
                    } else if(element.getSys5vi() && periodo == 5) {
                        view.setPeriodo(5);
                        view.setHora(element.getP5hora());
                    } else if(element.getSys6vi() && periodo == 6) {
                        view.setPeriodo(6);
                        view.setHora(element.getP6hora());
                    } else if(element.getSys7vi() && periodo == 7) {
                        view.setPeriodo(7);
                        view.setHora(element.getP7hora());
                    }
                    break;
                case Calendar.SATURDAY:
                    if(element.getSys1sa() && periodo == 1) {
                        view.setPeriodo(1);
                        view.setHora(element.getP1hora());
                    } else if(element.getSys2sa() && periodo == 2) {
                        view.setPeriodo(2);
                        view.setHora(element.getP2hora());
                    } else if(element.getSys3sa() && periodo == 3) {
                        view.setPeriodo(3);
                        view.setHora(element.getP3hora());
                    } else if(element.getSys4sa() && periodo == 4) {
                        view.setPeriodo(4);
                        view.setHora(element.getP4hora());
                    } else if(element.getSys5sa() && periodo == 5) {
                        view.setPeriodo(5);
                        view.setHora(element.getP5hora());
                    } else if(element.getSys6sa() && periodo == 6) {
                        view.setPeriodo(6);
                        view.setHora(element.getP6hora());
                    } else if(element.getSys7sa() && periodo == 7) {
                        view.setPeriodo(7);
                        view.setHora(element.getP7hora());
                    }
                    break;
                default:
                    throw new ControlNotificacionException("Opcion de dia no es correcta", GenericException.LAYER_DAO, GenericException.ACTION_VALIDATE);
            }
            response.add(view);
        }
        return response;
    }

    private List<ControlNotificacionSimpView> getNutriNotifications(Integer periodo, Integer dayOfWeek, List<SaludIndNutri> listNutri) throws ControlNotificacionException {
        List<ControlNotificacionSimpView> response = new ArrayList<>();
        for(SaludIndNutri element : listNutri) {
            ControlNotificacionSimpView view = new ControlNotificacionSimpView();
            view.setIdPaciente(element.getPacidfk());
            switch(dayOfWeek) {
                case Calendar.SUNDAY:
                    if(element.getNutri1do() && periodo == 1) {
                        view.setPeriodo(1);
                        view.setHora(element.getP1hora());
                    } else if(element.getNutri2do() && periodo == 2) {
                        view.setPeriodo(2);
                        view.setHora(element.getP2hora());
                    } else if(element.getNutri3do() && periodo == 3) {
                        view.setPeriodo(3);
                        view.setHora(element.getP3hora());
                    }
                    break;
                case Calendar.MONDAY:
                    if(element.getNutri1lu() && periodo == 1) {
                        view.setPeriodo(1);
                        view.setHora(element.getP1hora());
                    } else if(element.getNutri2lu() && periodo == 2) {
                        view.setPeriodo(2);
                        view.setHora(element.getP2hora());
                    } else if(element.getNutri3lu() && periodo == 3) {
                        view.setPeriodo(3);
                        view.setHora(element.getP3hora());
                    }
                    break;
                case Calendar.TUESDAY:
                    if(element.getNutri1ma() && periodo == 1) {
                        view.setPeriodo(1);
                        view.setHora(element.getP1hora());
                    } else if(element.getNutri2ma() && periodo == 2) {
                        view.setPeriodo(2);
                        view.setHora(element.getP2hora());
                    } else if(element.getNutri3ma() && periodo == 3) {
                        view.setPeriodo(3);
                        view.setHora(element.getP3hora());
                    }
                    break;
                case Calendar.WEDNESDAY:
                    if(element.getNutri1mi() && periodo == 1) {
                        view.setPeriodo(1);
                        view.setHora(element.getP1hora());
                    } else if(element.getNutri2mi() && periodo == 2) {
                        view.setPeriodo(2);
                        view.setHora(element.getP2hora());
                    } else if(element.getNutri3mi() && periodo == 3) {
                        view.setPeriodo(3);
                        view.setHora(element.getP3hora());
                    }
                    break;
                case Calendar.THURSDAY:
                    if(element.getNutri1ju() && periodo == 1) {
                        view.setPeriodo(1);
                        view.setHora(element.getP1hora());
                    } else if(element.getNutri2ju() && periodo == 2) {
                        view.setPeriodo(2);
                        view.setHora(element.getP2hora());
                    } else if(element.getNutri3ju() && periodo == 3) {
                        view.setPeriodo(3);
                        view.setHora(element.getP3hora());
                    }
                    break;
                case Calendar.FRIDAY:
                    if(element.getNutri1vi() && periodo == 1) {
                        view.setPeriodo(1);
                        view.setHora(element.getP1hora());
                    } else if(element.getNutri2vi() && periodo == 2) {
                        view.setPeriodo(2);
                        view.setHora(element.getP2hora());
                    } else if(element.getNutri3vi() && periodo == 3) {
                        view.setPeriodo(3);
                        view.setHora(element.getP3hora());
                    }
                    break;
                case Calendar.SATURDAY:
                    if(element.getNutri1sa() && periodo == 1) {
                        view.setPeriodo(1);
                        view.setHora(element.getP1hora());
                    } else if(element.getNutri2sa() && periodo == 2) {
                        view.setPeriodo(2);
                        view.setHora(element.getP2hora());
                    } else if(element.getNutri3sa() && periodo == 3) {
                        view.setPeriodo(3);
                        view.setHora(element.getP3hora());
                    }
                    break;
                default:
                    throw new ControlNotificacionException("Opcion de dia no es correcta", GenericException.LAYER_DAO, GenericException.ACTION_VALIDATE);
            }
            response.add(view);
        }
        return response;
    }

    private List<ControlNotificacionSimpView> getGlucosaNotifications(Integer periodo, Integer dayOfWeek,  List<SaludIndGluc> listGluc) throws ControlNotificacionException {
        List<ControlNotificacionSimpView> response = new ArrayList<>();
        for(SaludIndGluc element : listGluc) {
            ControlNotificacionSimpView view = new ControlNotificacionSimpView();
            view.setIdPaciente(element.getPacidfk());
            switch(dayOfWeek) {
                case Calendar.SUNDAY:
                    if(element.getGlu1do() && periodo == 1) {
                        view.setPeriodo(1);
                        view.setHora(element.getGlu1hora());
                    } else if(element.getGlu2do() && periodo == 2) {
                        view.setPeriodo(2);
                        view.setHora(element.getGlu2hora());
                    } else if(element.getGlu3do() && periodo == 3) {
                        view.setPeriodo(3);
                        view.setHora(element.getGlu3hora());
                    } else if(element.getGlu4do() && periodo == 4) {
                        view.setPeriodo(4);
                        view.setHora(element.getGlu4hora());
                    } else if(element.getGlu5do() && periodo == 5) {
                        view.setPeriodo(5);
                        view.setHora(element.getGlu5hora());
                    } else if(element.getGlu6do() && periodo == 6) {
                        view.setPeriodo(6);
                        view.setHora(element.getGlu6hora());
                    } else if(element.getGlu7do() && periodo == 7) {
                        view.setPeriodo(7);
                        view.setHora(element.getGlu7hora());
                    } else if(element.getGlu8do() && periodo == 10) {
                        view.setPeriodo(9);
                        view.setHora(element.getGlu8hora());
                    }
                    break;
                case Calendar.MONDAY:
                    if(element.getGlu1lu() && periodo == 1) {
                        view.setPeriodo(1);
                        view.setHora(element.getGlu1hora());
                    } else if(element.getGlu2lu() && periodo == 2) {
                        view.setPeriodo(2);
                        view.setHora(element.getGlu2hora());
                    } else if(element.getGlu3lu() && periodo == 3) {
                        view.setPeriodo(3);
                        view.setHora(element.getGlu3hora());
                    } else if(element.getGlu4lu() && periodo == 4) {
                        view.setPeriodo(4);
                        view.setHora(element.getGlu4hora());
                    } else if(element.getGlu5lu() && periodo == 5) {
                        view.setPeriodo(5);
                        view.setHora(element.getGlu5hora());
                    } else if(element.getGlu6lu() && periodo == 6) {
                        view.setPeriodo(6);
                        view.setHora(element.getGlu6hora());
                    } else if(element.getGlu7lu() && periodo == 7) {
                        view.setPeriodo(7);
                        view.setHora(element.getGlu7hora());
                    } else if(element.getGlu8lu() && periodo == 10) {
                        view.setPeriodo(9);
                        view.setHora(element.getGlu8hora());
                    }
                    break;
                case Calendar.TUESDAY:
                    if(element.getGlu1ma() && periodo == 1) {
                        view.setPeriodo(1);
                        view.setHora(element.getGlu1hora());
                    } else if(element.getGlu2ma() && periodo == 2) {
                        view.setPeriodo(2);
                        view.setHora(element.getGlu2hora());
                    } else if(element.getGlu3ma() && periodo == 3) {
                        view.setPeriodo(3);
                        view.setHora(element.getGlu3hora());
                    } else if(element.getGlu4ma() && periodo == 4) {
                        view.setPeriodo(4);
                        view.setHora(element.getGlu4hora());
                    } else if(element.getGlu5ma() && periodo == 5) {
                        view.setPeriodo(5);
                        view.setHora(element.getGlu5hora());
                    } else if(element.getGlu6ma() && periodo == 6) {
                        view.setPeriodo(6);
                        view.setHora(element.getGlu6hora());
                    } else if(element.getGlu7ma() && periodo == 7) {
                        view.setPeriodo(7);
                        view.setHora(element.getGlu7hora());
                    } else if(element.getGlu8ma() && periodo == 10) {
                        view.setPeriodo(9);
                        view.setHora(element.getGlu8hora());
                    }
                    break;
                case Calendar.WEDNESDAY:
                    if(element.getGlu1mi() && periodo == 1) {
                        view.setPeriodo(1);
                        view.setHora(element.getGlu1hora());
                    } else if(element.getGlu2mi() && periodo == 2) {
                        view.setPeriodo(2);
                        view.setHora(element.getGlu2hora());
                    } else if(element.getGlu3mi() && periodo == 3) {
                        view.setPeriodo(3);
                        view.setHora(element.getGlu3hora());
                    } else if(element.getGlu4mi() && periodo == 4) {
                        view.setPeriodo(4);
                        view.setHora(element.getGlu4hora());
                    } else if(element.getGlu5mi() && periodo == 5) {
                        view.setPeriodo(5);
                        view.setHora(element.getGlu5hora());
                    } else if(element.getGlu6mi() && periodo == 6) {
                        view.setPeriodo(6);
                        view.setHora(element.getGlu6hora());
                    } else if(element.getGlu7mi() && periodo == 7) {
                        view.setPeriodo(7);
                        view.setHora(element.getGlu7hora());
                    } else if(element.getGlu8mi() && periodo == 10) {
                        view.setPeriodo(9);
                        view.setHora(element.getGlu8hora());
                    }
                    break;
                case Calendar.THURSDAY:
                    if(element.getGlu1ju() && periodo == 1) {
                        view.setPeriodo(1);
                        view.setHora(element.getGlu1hora());
                    } else if(element.getGlu2ju() && periodo == 2) {
                        view.setPeriodo(2);
                        view.setHora(element.getGlu2hora());
                    } else if(element.getGlu3ju() && periodo == 3) {
                        view.setPeriodo(3);
                        view.setHora(element.getGlu3hora());
                    } else if(element.getGlu4ju() && periodo == 4) {
                        view.setPeriodo(4);
                        view.setHora(element.getGlu4hora());
                    } else if(element.getGlu5ju() && periodo == 5) {
                        view.setPeriodo(5);
                        view.setHora(element.getGlu5hora());
                    } else if(element.getGlu6ju() && periodo == 6) {
                        view.setPeriodo(6);
                        view.setHora(element.getGlu6hora());
                    } else if(element.getGlu7ju() && periodo == 7) {
                        view.setPeriodo(7);
                        view.setHora(element.getGlu7hora());
                    } else if(element.getGlu8ju() && periodo == 10) {
                        view.setPeriodo(9);
                        view.setHora(element.getGlu8hora());
                    }
                    break;
                case Calendar.FRIDAY:
                    if(element.getGlu1vi() && periodo == 1) {
                        view.setPeriodo(1);
                        view.setHora(element.getGlu1hora());
                    } else if(element.getGlu2vi() && periodo == 2) {
                        view.setPeriodo(2);
                        view.setHora(element.getGlu2hora());
                    } else if(element.getGlu3vi() && periodo == 3) {
                        view.setPeriodo(3);
                        view.setHora(element.getGlu3hora());
                    } else if(element.getGlu4vi() && periodo == 4) {
                        view.setPeriodo(4);
                        view.setHora(element.getGlu4hora());
                    } else if(element.getGlu5vi() && periodo == 5) {
                        view.setPeriodo(5);
                        view.setHora(element.getGlu5hora());
                    } else if(element.getGlu6vi() && periodo == 6) {
                        view.setPeriodo(6);
                        view.setHora(element.getGlu6hora());
                    } else if(element.getGlu7vi() && periodo == 7) {
                        view.setPeriodo(7);
                        view.setHora(element.getGlu7hora());
                    } else if(element.getGlu8vi() && periodo == 10) {
                        view.setPeriodo(9);
                        view.setHora(element.getGlu8hora());
                    }
                    break;
                case Calendar.SATURDAY:
                    if(element.getGlu1sa() && periodo == 1) {
                        view.setPeriodo(1);
                        view.setHora(element.getGlu1hora());
                    } else if(element.getGlu2sa() && periodo == 2) {
                        view.setPeriodo(2);
                        view.setHora(element.getGlu2hora());
                    } else if(element.getGlu3sa() && periodo == 3) {
                        view.setPeriodo(3);
                        view.setHora(element.getGlu3hora());
                    } else if(element.getGlu4sa() && periodo == 4) {
                        view.setPeriodo(4);
                        view.setHora(element.getGlu4hora());
                    } else if(element.getGlu5sa() && periodo == 5) {
                        view.setPeriodo(5);
                        view.setHora(element.getGlu5hora());
                    } else if(element.getGlu6sa() && periodo == 6) {
                        view.setPeriodo(6);
                        view.setHora(element.getGlu6hora());
                    } else if(element.getGlu7sa() && periodo == 7) {
                        view.setPeriodo(7);
                        view.setHora(element.getGlu7hora());
                    } else if(element.getGlu8sa() && periodo == 10) {
                        view.setPeriodo(9);
                        view.setHora(element.getGlu8hora());
                    }
                    break;
                default:
                    throw new ControlNotificacionException("Opcion de dia no es correcta", GenericException.LAYER_DAO, GenericException.ACTION_VALIDATE);
            }
            response.add(view);
        }
        return response;
    }

    private List<ControlNotificacionSimpView> getCovidNotifications(Integer periodo, Integer dayOfWeek, List<SaludIndCovid> listCovid) throws ControlNotificacionException {
        List<ControlNotificacionSimpView> response = new ArrayList<>();
        for(SaludIndCovid element : listCovid) {
            ControlNotificacionSimpView view = new ControlNotificacionSimpView();
            view.setIdPaciente(element.getPacidfk());
            switch(dayOfWeek) {
                case Calendar.SUNDAY:
                    if(element.getCovid1do() && periodo == 1) {
                        view.setPeriodo(1);
                        view.setHora(element.getP1hora());
                    } else if(element.getCovid2do() && periodo == 2) {
                        view.setPeriodo(2);
                        view.setHora(element.getP2hora());
                    } else if(element.getCovid3do() && periodo == 3) {
                        view.setPeriodo(3);
                        view.setHora(element.getP3hora());
                    } else if(element.getCovid4do() && periodo == 4) {
                        view.setPeriodo(4);
                        view.setHora(element.getP4hora());
                    }
                    break;
                case Calendar.MONDAY:
                    if(element.getCovid1lu() && periodo == 1) {
                        view.setPeriodo(1);
                        view.setHora(element.getP1hora());
                    } else if(element.getCovid2lu() && periodo == 2) {
                        view.setPeriodo(2);
                        view.setHora(element.getP2hora());
                    } else if(element.getCovid3lu() && periodo == 3) {
                        view.setPeriodo(3);
                        view.setHora(element.getP3hora());
                    } else if(element.getCovid4lu() && periodo == 4) {
                        view.setPeriodo(4);
                        view.setHora(element.getP4hora());
                    }
                    break;
                case Calendar.TUESDAY:
                    if(element.getCovid1ma() && periodo == 1) {
                        view.setPeriodo(1);
                        view.setHora(element.getP1hora());
                    } else if(element.getCovid2ma() && periodo == 2) {
                        view.setPeriodo(2);
                        view.setHora(element.getP2hora());
                    } else if(element.getCovid3ma() && periodo == 3) {
                        view.setPeriodo(3);
                        view.setHora(element.getP3hora());
                    } else if(element.getCovid4ma() && periodo == 4) {
                        view.setPeriodo(4);
                        view.setHora(element.getP4hora());
                    }
                    break;
                case Calendar.WEDNESDAY:
                    if(element.getCovid1mi() && periodo == 1) {
                        view.setPeriodo(1);
                        view.setHora(element.getP1hora());
                    } else if(element.getCovid2mi() && periodo == 2) {
                        view.setPeriodo(2);
                        view.setHora(element.getP2hora());
                    } else if(element.getCovid3mi() && periodo == 3) {
                        view.setPeriodo(3);
                        view.setHora(element.getP3hora());
                    } else if(element.getCovid4mi() && periodo == 4) {
                        view.setPeriodo(4);
                        view.setHora(element.getP4hora());
                    }
                    break;
                case Calendar.THURSDAY:
                    if(element.getCovid1ju() && periodo == 1) {
                        view.setPeriodo(1);
                        view.setHora(element.getP1hora());
                    } else if(element.getCovid2ju() && periodo == 2) {
                        view.setPeriodo(2);
                        view.setHora(element.getP2hora());
                    } else if(element.getCovid3ju() && periodo == 3) {
                        view.setPeriodo(3);
                        view.setHora(element.getP3hora());
                    } else if(element.getCovid4ju() && periodo == 4) {
                        view.setPeriodo(4);
                        view.setHora(element.getP4hora());
                    }
                    break;
                case Calendar.FRIDAY:
                    if(element.getCovid1vi() && periodo == 1) {
                        view.setPeriodo(1);
                        view.setHora(element.getP1hora());
                    } else if(element.getCovid2vi() && periodo == 2) {
                        view.setPeriodo(2);
                        view.setHora(element.getP2hora());
                    } else if(element.getCovid3vi() && periodo == 3) {
                        view.setPeriodo(3);
                        view.setHora(element.getP3hora());
                    } else if(element.getCovid4vi() && periodo == 4) {
                        view.setPeriodo(4);
                        view.setHora(element.getP4hora());
                    }
                    break;
                case Calendar.SATURDAY:
                    if(element.getCovid1sa() && periodo == 1) {
                        view.setPeriodo(1);
                        view.setHora(element.getP1hora());
                    } else if(element.getCovid2sa() && periodo == 2) {
                        view.setPeriodo(2);
                        view.setHora(element.getP2hora());
                    } else if(element.getCovid3sa() && periodo == 3) {
                        view.setPeriodo(3);
                        view.setHora(element.getP3hora());
                    } else if(element.getCovid4sa() && periodo == 4) {
                        view.setPeriodo(4);
                        view.setHora(element.getP4hora());
                    }
                    break;
                default:
                    throw new ControlNotificacionException("Opcion de dia no es correcta", GenericException.LAYER_DAO, GenericException.ACTION_VALIDATE);
            }
            response.add(view);
        }
        return response;
    }


    private List<ControlNotificacionViewList> getPresionArterialNotificationsCompl(Integer periodo, Integer dayOfWeek, List<SaludIndPa> listPa, HashMap<String, HashMap<String, Object>> idPacienteList) throws ConsultaException, ControlNotificacionException {
        List<ControlNotificacionViewList> responseJu = new ArrayList<>();
        for(SaludIndPa element : listPa) {
            ControlNotificacionViewList view = new ControlNotificacionViewList();
            HashMap<String, Object> paciente = idPacienteList.get(element.getPacidfk());
            if(paciente != null) {
                if((Boolean) paciente.get("active") && paciente.get("idDevice") != null
                        && !((String)paciente.get("idDevice")).equalsIgnoreCase("null")) {
                    view.setIdDevice((String) paciente.get("idDevice"));
                    view.setIdUserApp(((Integer) paciente.get("idUserApp")).longValue());
                } else
                    continue;
            } else {
                Map<String, Object> responsePaciente = null;
                try {
                    responsePaciente = apiConfiguration.getPacieteByid(element.getPacidfk());
                } catch (Exception ex) {
                    logger.error("Paciente no encontrado en el sistema - {}", element.getPacidfk());
                    continue;
                }
                if(responsePaciente != null || !responsePaciente.isEmpty()) {
                    Map<String, Object> responseUserApp = null;
                    try {
                        responseUserApp = apiConfiguration.getUserAppById((int)responsePaciente.get("idUsuario"));
                    } catch (Exception ex) {
                        logger.error("Usuario no encontrado en el sistema - {}", responsePaciente.get("idUsuario"));
                        continue;
                    }
                    HashMap<String, Object> nuevoPaciente = new HashMap<>();
                    nuevoPaciente.put("active", String.valueOf(responseUserApp.get("status")).equalsIgnoreCase("activo"));
                    nuevoPaciente.put("idDevice", String.valueOf(responsePaciente.get("idDevice")));
                    nuevoPaciente.put("idUserApp", responseUserApp.get("idUserApp"));
                    idPacienteList.put(element.getPacidfk(), nuevoPaciente);
                    if((boolean)nuevoPaciente.get("active") && nuevoPaciente.get("idDevice") != null) {
                        view.setIdUserApp(((Integer) responseUserApp.get("idUserApp")).longValue());
                        view.setIdDevice(String.valueOf(responsePaciente.get("idDevice")));
                    }
                    else {
                        logger.error("Usuario no activo o idDevice vacio: {}, {}", responseUserApp.get("idUserApp"), responsePaciente.get("idDevice"));
                        continue;
                    }
                } else {
                    logger.error("El paciente {} no ha podido ser agregado a las notificaciones de presión arterial", element.getPacidfk());
                }
            }
            switch(dayOfWeek) {
                case Calendar.SUNDAY:
                    if(element.getSys1do() && periodo == 1) {
                        view.setPeriodo(1);
                        view.setHora(element.getP1hora());
                    } else if(element.getSys2do() && periodo == 2) {
                        view.setPeriodo(2);
                        view.setHora(element.getP2hora());
                    } else if(element.getSys3do() && periodo == 3) {
                        view.setPeriodo(3);
                        view.setHora(element.getP3hora());
                    } else if(element.getSys4do() && periodo == 4) {
                        view.setPeriodo(4);
                        view.setHora(element.getP4hora());
                    } else if(element.getSys5do() && periodo == 5) {
                        view.setPeriodo(5);
                        view.setHora(element.getP5hora());
                    } else if(element.getSys6do() && periodo == 6) {
                        view.setPeriodo(6);
                        view.setHora(element.getP6hora());
                    } else if(element.getSys7do() && periodo == 7) {
                        view.setPeriodo(7);
                        view.setHora(element.getP7hora());
                    }
                    break;
                case Calendar.MONDAY:
                    if(element.getSys1lu() && periodo == 1) {
                        view.setPeriodo(1);
                        view.setHora(element.getP1hora());
                    } else if(element.getSys2lu() && periodo == 2) {
                        view.setPeriodo(2);
                        view.setHora(element.getP2hora());
                    } else if(element.getSys3lu() && periodo == 3) {
                        view.setPeriodo(3);
                        view.setHora(element.getP3hora());
                    } else if(element.getSys4lu() && periodo == 4) {
                        view.setPeriodo(4);
                        view.setHora(element.getP4hora());
                    } else if(element.getSys5lu() && periodo == 5) {
                        view.setPeriodo(5);
                        view.setHora(element.getP5hora());
                    } else if(element.getSys6lu() && periodo == 6) {
                        view.setPeriodo(6);
                        view.setHora(element.getP6hora());
                    } else if(element.getSys7lu() && periodo == 7) {
                        view.setPeriodo(7);
                        view.setHora(element.getP7hora());
                    }
                    break;
                case Calendar.TUESDAY:
                    if(element.getSys1ma() && periodo == 1) {
                        view.setPeriodo(1);
                        view.setHora(element.getP1hora());
                    } else if(element.getSys2ma() && periodo == 2) {
                        view.setPeriodo(2);
                        view.setHora(element.getP2hora());
                    } else if(element.getSys3ma() && periodo == 3) {
                        view.setPeriodo(3);
                        view.setHora(element.getP3hora());
                    } else if(element.getSys4ma() && periodo == 4) {
                        view.setPeriodo(4);
                        view.setHora(element.getP4hora());
                    } else if(element.getSys5ma() && periodo == 5) {
                        view.setPeriodo(5);
                        view.setHora(element.getP5hora());
                    } else if(element.getSys6ma() && periodo == 6) {
                        view.setPeriodo(6);
                        view.setHora(element.getP6hora());
                    } else if(element.getSys7ma() && periodo == 7) {
                        view.setPeriodo(7);
                        view.setHora(element.getP7hora());
                    }
                    break;
                case Calendar.WEDNESDAY:
                    if(element.getSys1mi() && periodo == 1) {
                        view.setPeriodo(1);
                        view.setHora(element.getP1hora());
                    } else if(element.getSys2mi() && periodo == 2) {
                        view.setPeriodo(2);
                        view.setHora(element.getP2hora());
                    } else if(element.getSys3mi() && periodo == 3) {
                        view.setPeriodo(3);
                        view.setHora(element.getP3hora());
                    } else if(element.getSys4mi() && periodo == 4) {
                        view.setPeriodo(4);
                        view.setHora(element.getP4hora());
                    } else if(element.getSys5mi() && periodo == 5) {
                        view.setPeriodo(5);
                        view.setHora(element.getP5hora());
                    } else if(element.getSys6mi() && periodo == 6) {
                        view.setPeriodo(6);
                        view.setHora(element.getP6hora());
                    } else if(element.getSys7mi() && periodo == 7) {
                        view.setPeriodo(7);
                        view.setHora(element.getP7hora());
                    }
                    break;
                case Calendar.THURSDAY:
                    if(element.getSys1ju() && periodo == 1) {
                        view.setPeriodo(1);
                        view.setHora(element.getP1hora());
                    } else if(element.getSys2ju() && periodo == 2) {
                        view.setPeriodo(2);
                        view.setHora(element.getP2hora());
                    } else if(element.getSys3ju() && periodo == 3) {
                        view.setPeriodo(3);
                        view.setHora(element.getP3hora());
                    } else if(element.getSys4ju() && periodo == 4) {
                        view.setPeriodo(4);
                        view.setHora(element.getP4hora());
                    } else if(element.getSys5ju() && periodo == 5) {
                        view.setPeriodo(5);
                        view.setHora(element.getP5hora());
                    } else if(element.getSys6ju() && periodo == 6) {
                        view.setPeriodo(6);
                        view.setHora(element.getP6hora());
                    } else if(element.getSys7ju() && periodo == 7) {
                        view.setPeriodo(7);
                        view.setHora(element.getP7hora());
                    }
                    break;
                case Calendar.FRIDAY:
                    if(element.getSys1vi() && periodo == 1) {
                        view.setPeriodo(1);
                        view.setHora(element.getP1hora());
                    } else if(element.getSys2vi() && periodo == 2) {
                        view.setPeriodo(2);
                        view.setHora(element.getP2hora());
                    } else if(element.getSys3vi() && periodo == 3) {
                        view.setPeriodo(3);
                        view.setHora(element.getP3hora());
                    } else if(element.getSys4vi() && periodo == 4) {
                        view.setPeriodo(4);
                        view.setHora(element.getP4hora());
                    } else if(element.getSys5vi() && periodo == 5) {
                        view.setPeriodo(5);
                        view.setHora(element.getP5hora());
                    } else if(element.getSys6vi() && periodo == 6) {
                        view.setPeriodo(6);
                        view.setHora(element.getP6hora());
                    } else if(element.getSys7vi() && periodo == 7) {
                        view.setPeriodo(7);
                        view.setHora(element.getP7hora());
                    }
                    break;
                case Calendar.SATURDAY:
                    if(element.getSys1sa() && periodo == 1) {
                        view.setPeriodo(1);
                        view.setHora(element.getP1hora());
                    } else if(element.getSys2sa() && periodo == 2) {
                        view.setPeriodo(2);
                        view.setHora(element.getP2hora());
                    } else if(element.getSys3sa() && periodo == 3) {
                        view.setPeriodo(3);
                        view.setHora(element.getP3hora());
                    } else if(element.getSys4sa() && periodo == 4) {
                        view.setPeriodo(4);
                        view.setHora(element.getP4hora());
                    } else if(element.getSys5sa() && periodo == 5) {
                        view.setPeriodo(5);
                        view.setHora(element.getP5hora());
                    } else if(element.getSys6sa() && periodo == 6) {
                        view.setPeriodo(6);
                        view.setHora(element.getP6hora());
                    } else if(element.getSys7sa() && periodo == 7) {
                        view.setPeriodo(7);
                        view.setHora(element.getP7hora());
                    }
                    break;
                default:
                    throw new ControlNotificacionException("Opcion de dia no es correcta", GenericException.LAYER_DAO, GenericException.ACTION_VALIDATE);
            }
            responseJu.add(view);
        }
        return responseJu;
    }

    private List<ControlNotificacionViewList> getNutriNotificationsCompl(Integer periodo, Integer dayOfWeek, List<SaludIndNutri> listNutri, HashMap<String, HashMap<String, Object>> idPacienteList) throws ConsultaException, ControlNotificacionException {
        List<ControlNotificacionViewList> responseJu = new ArrayList<>();
        for(SaludIndNutri element : listNutri) {
            ControlNotificacionViewList view = new ControlNotificacionViewList();
            HashMap<String, Object> paciente = idPacienteList.get(element.getPacidfk());
            if(paciente != null) {
                if((Boolean) paciente.get("active") && paciente.get("idDevice") != null
                        && !((String)paciente.get("idDevice")).equalsIgnoreCase("null")) {
                    view.setIdDevice((String) paciente.get("idDevice"));
                    view.setIdUserApp(((Integer) paciente.get("idUserApp")).longValue());
                } else
                    continue;
            } else {
                Map<String, Object> responsePaciente = null;
                try {
                    responsePaciente = apiConfiguration.getPacieteByid(element.getPacidfk());
                } catch (Exception ex) {
                    logger.error("Paciente no encontrado en el sistema - {}", element.getPacidfk());
                    continue;
                }
                if(responsePaciente != null || !responsePaciente.isEmpty()) {
                    Map<String, Object> responseUserApp = null;
                    try {
                        responseUserApp = apiConfiguration.getUserAppById((int)responsePaciente.get("idUsuario"));
                    } catch (Exception ex) {
                        logger.error("Usuario no encontrado en el sistema - {}", responsePaciente.get("idUsuario"));
                        continue;
                    }
                    HashMap<String, Object> nuevoPaciente = new HashMap<>();
                    nuevoPaciente.put("active", String.valueOf(responseUserApp.get("status")).equalsIgnoreCase("activo"));
                    nuevoPaciente.put("idDevice", String.valueOf(responsePaciente.get("idDevice")));
                    nuevoPaciente.put("idUserApp", responseUserApp.get("idUserApp"));
                    idPacienteList.put(element.getPacidfk(), nuevoPaciente);
                    if((boolean)nuevoPaciente.get("active") && nuevoPaciente.get("idDevice") != null) {
                        view.setIdUserApp(((Integer) responseUserApp.get("idUserApp")).longValue());
                        view.setIdDevice(String.valueOf(responsePaciente.get("idDevice")));
                    }
                    else {
                        logger.error("Usuario no activo o idDevice vacio: {}, {}", responseUserApp.get("idUserApp"), responsePaciente.get("idDevice"));
                        continue;
                    }
                } else {
                    logger.error("El paciente {} no ha podido ser agregado a las notificaciones de covid", element.getPacidfk());
                }
            }
            switch(dayOfWeek) {
                case Calendar.SUNDAY:
                    if(element.getNutri1do() && periodo == 1) {
                        view.setPeriodo(1);
                        view.setHora(element.getP1hora());
                    } else if(element.getNutri2do() && periodo == 2) {
                        view.setPeriodo(2);
                        view.setHora(element.getP2hora());
                    } else if(element.getNutri3do() && periodo == 3) {
                        view.setPeriodo(3);
                        view.setHora(element.getP3hora());
                    }
                    break;
                case Calendar.MONDAY:
                    if(element.getNutri1lu() && periodo == 1) {
                        view.setPeriodo(1);
                        view.setHora(element.getP1hora());
                    } else if(element.getNutri2lu() && periodo == 2) {
                        view.setPeriodo(2);
                        view.setHora(element.getP2hora());
                    } else if(element.getNutri3lu() && periodo == 3) {
                        view.setPeriodo(3);
                        view.setHora(element.getP3hora());
                    }
                    break;
                case Calendar.TUESDAY:
                    if(element.getNutri1ma() && periodo == 1) {
                        view.setPeriodo(1);
                        view.setHora(element.getP1hora());
                    } else if(element.getNutri2ma() && periodo == 2) {
                        view.setPeriodo(2);
                        view.setHora(element.getP2hora());
                    } else if(element.getNutri3ma() && periodo == 3) {
                        view.setPeriodo(3);
                        view.setHora(element.getP3hora());
                    }
                    break;
                case Calendar.WEDNESDAY:
                    if(element.getNutri1mi() && periodo == 1) {
                        view.setPeriodo(1);
                        view.setHora(element.getP1hora());
                    } else if(element.getNutri2mi() && periodo == 2) {
                        view.setPeriodo(2);
                        view.setHora(element.getP2hora());
                    } else if(element.getNutri3mi() && periodo == 3) {
                        view.setPeriodo(3);
                        view.setHora(element.getP3hora());
                    }
                    break;
                case Calendar.THURSDAY:
                    if(element.getNutri1ju() && periodo == 1) {
                        view.setPeriodo(1);
                        view.setHora(element.getP1hora());
                    } else if(element.getNutri2ju() && periodo == 2) {
                        view.setPeriodo(2);
                        view.setHora(element.getP2hora());
                    } else if(element.getNutri3ju() && periodo == 3) {
                        view.setPeriodo(3);
                        view.setHora(element.getP3hora());
                    }
                    break;
                case Calendar.FRIDAY:
                    if(element.getNutri1vi() && periodo == 1) {
                        view.setPeriodo(1);
                        view.setHora(element.getP1hora());
                    } else if(element.getNutri2vi() && periodo == 2) {
                        view.setPeriodo(2);
                        view.setHora(element.getP2hora());
                    } else if(element.getNutri3vi() && periodo == 3) {
                        view.setPeriodo(3);
                        view.setHora(element.getP3hora());
                    }
                    break;
                case Calendar.SATURDAY:
                    if(element.getNutri1sa() && periodo == 1) {
                        view.setPeriodo(1);
                        view.setHora(element.getP1hora());
                    } else if(element.getNutri2sa() && periodo == 2) {
                        view.setPeriodo(2);
                        view.setHora(element.getP2hora());
                    } else if(element.getNutri3sa() && periodo == 3) {
                        view.setPeriodo(3);
                        view.setHora(element.getP3hora());
                    }
                    break;
                default:
                    throw new ControlNotificacionException("Opcion de dia no es correcta", GenericException.LAYER_DAO, GenericException.ACTION_VALIDATE);
            }
            responseJu.add(view);
        }
        return responseJu;
    }

    private List<ControlNotificacionViewList> getGlucosaNotificationsCompl(Integer periodo, Integer dayOfWeek,  List<SaludIndGluc> listGluc, HashMap<String, HashMap<String, Object>> idPacienteList) throws ConsultaException, ControlNotificacionException {
        List<ControlNotificacionViewList> responseJu = new ArrayList<>();
        for(SaludIndGluc element : listGluc) {
            ControlNotificacionViewList view = new ControlNotificacionViewList();
            HashMap<String, Object> paciente = idPacienteList.get(element.getPacidfk());
            if(paciente != null) {
                if((Boolean) paciente.get("active") && paciente.get("idDevice") != null
                        && !((String)paciente.get("idDevice")).equalsIgnoreCase("null")) {
                    view.setIdDevice((String) paciente.get("idDevice"));
                    view.setIdUserApp(((Integer) paciente.get("idUserApp")).longValue());
                } else
                    continue;
            } else {
                Map<String, Object> responsePaciente;
                try {
                    responsePaciente = apiConfiguration.getPacieteByid(element.getPacidfk());
                } catch(Exception ex) {
                    logger.error("Paciente no encontrado en el sistema - {}", element.getPacidfk());
                    continue;
                }
                if(responsePaciente != null || !responsePaciente.isEmpty()) {
                    Map<String, Object> responseUserApp = null;
                    try {
                        responseUserApp = apiConfiguration.getUserAppById((int)responsePaciente.get("idUsuario"));
                    } catch (Exception ex) {
                        logger.error("Usuario no encontrado en el sistema - {}", responsePaciente.get("idUsuario"));
                        continue;
                    }
                    HashMap<String, Object> nuevoPaciente = new HashMap<>();
                    nuevoPaciente.put("active", String.valueOf(responseUserApp.get("status")).equalsIgnoreCase("activo"));
                    nuevoPaciente.put("idDevice", String.valueOf(responsePaciente.get("idDevice")));
                    nuevoPaciente.put("idUserApp", responseUserApp.get("idUserApp"));
                    idPacienteList.put(element.getPacidfk(), nuevoPaciente);
                    if((boolean)nuevoPaciente.get("active") && nuevoPaciente.get("idDevice") != null) {
                        view.setIdUserApp(((Integer) responseUserApp.get("idUserApp")).longValue());
                        view.setIdDevice(String.valueOf(responsePaciente.get("idDevice")));
                    }
                    else {
                        logger.error("Usuario no activo o idDevice vacio: {}, {}", responseUserApp.get("idUserApp"), responsePaciente.get("idDevice"));
                        continue;
                    }
                } else {
                    logger.error("El paciente {} no ha podido ser agregado a las notificaciones de glucosa", element.getPacidfk());
                }
            }
            switch(dayOfWeek) {
                case Calendar.SUNDAY:
                    if(element.getGlu1do() && periodo == 1) {
                        view.setPeriodo(1);
                        view.setHora(element.getGlu1hora());
                    } else if(element.getGlu2do() && periodo == 2) {
                        view.setPeriodo(2);
                        view.setHora(element.getGlu2hora());
                    } else if(element.getGlu3do() && periodo == 3) {
                        view.setPeriodo(3);
                        view.setHora(element.getGlu3hora());
                    } else if(element.getGlu4do() && periodo == 4) {
                        view.setPeriodo(4);
                        view.setHora(element.getGlu4hora());
                    } else if(element.getGlu5do() && periodo == 5) {
                        view.setPeriodo(5);
                        view.setHora(element.getGlu5hora());
                    } else if(element.getGlu6do() && periodo == 6) {
                        view.setPeriodo(6);
                        view.setHora(element.getGlu6hora());
                    } else if(element.getGlu7do() && periodo == 7) {
                        view.setPeriodo(7);
                        view.setHora(element.getGlu7hora());
                    } else if(element.getGlu8do() && periodo == 10) {
                        view.setPeriodo(9);
                        view.setHora(element.getGlu8hora());
                    }
                    break;
                case Calendar.MONDAY:
                    if(element.getGlu1lu() && periodo == 1) {
                        view.setPeriodo(1);
                        view.setHora(element.getGlu1hora());
                    } else if(element.getGlu2lu() && periodo == 2) {
                        view.setPeriodo(2);
                        view.setHora(element.getGlu2hora());
                    } else if(element.getGlu3lu() && periodo == 3) {
                        view.setPeriodo(3);
                        view.setHora(element.getGlu3hora());
                    } else if(element.getGlu4lu() && periodo == 4) {
                        view.setPeriodo(4);
                        view.setHora(element.getGlu4hora());
                    } else if(element.getGlu5lu() && periodo == 5) {
                        view.setPeriodo(5);
                        view.setHora(element.getGlu5hora());
                    } else if(element.getGlu6lu() && periodo == 6) {
                        view.setPeriodo(6);
                        view.setHora(element.getGlu6hora());
                    } else if(element.getGlu7lu() && periodo == 7) {
                        view.setPeriodo(7);
                        view.setHora(element.getGlu7hora());
                    } else if(element.getGlu8lu() && periodo == 10) {
                        view.setPeriodo(9);
                        view.setHora(element.getGlu8hora());
                    }
                    break;
                case Calendar.TUESDAY:
                    if(element.getGlu1ma() && periodo == 1) {
                        view.setPeriodo(1);
                        view.setHora(element.getGlu1hora());
                    } else if(element.getGlu2ma() && periodo == 2) {
                        view.setPeriodo(2);
                        view.setHora(element.getGlu2hora());
                    } else if(element.getGlu3ma() && periodo == 3) {
                        view.setPeriodo(3);
                        view.setHora(element.getGlu3hora());
                    } else if(element.getGlu4ma() && periodo == 4) {
                        view.setPeriodo(4);
                        view.setHora(element.getGlu4hora());
                    } else if(element.getGlu5ma() && periodo == 5) {
                        view.setPeriodo(5);
                        view.setHora(element.getGlu5hora());
                    } else if(element.getGlu6ma() && periodo == 6) {
                        view.setPeriodo(6);
                        view.setHora(element.getGlu6hora());
                    } else if(element.getGlu7ma() && periodo == 7) {
                        view.setPeriodo(7);
                        view.setHora(element.getGlu7hora());
                    } else if(element.getGlu8ma() && periodo == 10) {
                        view.setPeriodo(9);
                        view.setHora(element.getGlu8hora());
                    }
                    break;
                case Calendar.WEDNESDAY:
                    if(element.getGlu1mi() && periodo == 1) {
                        view.setPeriodo(1);
                        view.setHora(element.getGlu1hora());
                    } else if(element.getGlu2mi() && periodo == 2) {
                        view.setPeriodo(2);
                        view.setHora(element.getGlu2hora());
                    } else if(element.getGlu3mi() && periodo == 3) {
                        view.setPeriodo(3);
                        view.setHora(element.getGlu3hora());
                    } else if(element.getGlu4mi() && periodo == 4) {
                        view.setPeriodo(4);
                        view.setHora(element.getGlu4hora());
                    } else if(element.getGlu5mi() && periodo == 5) {
                        view.setPeriodo(5);
                        view.setHora(element.getGlu5hora());
                    } else if(element.getGlu6mi() && periodo == 6) {
                        view.setPeriodo(6);
                        view.setHora(element.getGlu6hora());
                    } else if(element.getGlu7mi() && periodo == 7) {
                        view.setPeriodo(7);
                        view.setHora(element.getGlu7hora());
                    } else if(element.getGlu8mi() && periodo == 10) {
                        view.setPeriodo(9);
                        view.setHora(element.getGlu8hora());
                    }
                    break;
                case Calendar.THURSDAY:
                    if(element.getGlu1ju() && periodo == 1) {
                        view.setPeriodo(1);
                        view.setHora(element.getGlu1hora());
                    } else if(element.getGlu2ju() && periodo == 2) {
                        view.setPeriodo(2);
                        view.setHora(element.getGlu2hora());
                    } else if(element.getGlu3ju() && periodo == 3) {
                        view.setPeriodo(3);
                        view.setHora(element.getGlu3hora());
                    } else if(element.getGlu4ju() && periodo == 4) {
                        view.setPeriodo(4);
                        view.setHora(element.getGlu4hora());
                    } else if(element.getGlu5ju() && periodo == 5) {
                        view.setPeriodo(5);
                        view.setHora(element.getGlu5hora());
                    } else if(element.getGlu6ju() && periodo == 6) {
                        view.setPeriodo(6);
                        view.setHora(element.getGlu6hora());
                    } else if(element.getGlu7ju() && periodo == 7) {
                        view.setPeriodo(7);
                        view.setHora(element.getGlu7hora());
                    } else if(element.getGlu8ju() && periodo == 10) {
                        view.setPeriodo(9);
                        view.setHora(element.getGlu8hora());
                    }
                    break;
                case Calendar.FRIDAY:
                    if(element.getGlu1vi() && periodo == 1) {
                        view.setPeriodo(1);
                        view.setHora(element.getGlu1hora());
                    } else if(element.getGlu2vi() && periodo == 2) {
                        view.setPeriodo(2);
                        view.setHora(element.getGlu2hora());
                    } else if(element.getGlu3vi() && periodo == 3) {
                        view.setPeriodo(3);
                        view.setHora(element.getGlu3hora());
                    } else if(element.getGlu4vi() && periodo == 4) {
                        view.setPeriodo(4);
                        view.setHora(element.getGlu4hora());
                    } else if(element.getGlu5vi() && periodo == 5) {
                        view.setPeriodo(5);
                        view.setHora(element.getGlu5hora());
                    } else if(element.getGlu6vi() && periodo == 6) {
                        view.setPeriodo(6);
                        view.setHora(element.getGlu6hora());
                    } else if(element.getGlu7vi() && periodo == 7) {
                        view.setPeriodo(7);
                        view.setHora(element.getGlu7hora());
                    } else if(element.getGlu8vi() && periodo == 10) {
                        view.setPeriodo(9);
                        view.setHora(element.getGlu8hora());
                    }
                    break;
                case Calendar.SATURDAY:
                    if(element.getGlu1sa() && periodo == 1) {
                        view.setPeriodo(1);
                        view.setHora(element.getGlu1hora());
                    } else if(element.getGlu2sa() && periodo == 2) {
                        view.setPeriodo(2);
                        view.setHora(element.getGlu2hora());
                    } else if(element.getGlu3sa() && periodo == 3) {
                        view.setPeriodo(3);
                        view.setHora(element.getGlu3hora());
                    } else if(element.getGlu4sa() && periodo == 4) {
                        view.setPeriodo(4);
                        view.setHora(element.getGlu4hora());
                    } else if(element.getGlu5sa() && periodo == 5) {
                        view.setPeriodo(5);
                        view.setHora(element.getGlu5hora());
                    } else if(element.getGlu6sa() && periodo == 6) {
                        view.setPeriodo(6);
                        view.setHora(element.getGlu6hora());
                    } else if(element.getGlu7sa() && periodo == 7) {
                        view.setPeriodo(7);
                        view.setHora(element.getGlu7hora());
                    } else if(element.getGlu8sa() && periodo == 10) {
                        view.setPeriodo(9);
                        view.setHora(element.getGlu8hora());
                    }
                    break;
                default:
                    throw new ControlNotificacionException("Opcion de dia no es correcta", GenericException.LAYER_DAO, GenericException.ACTION_VALIDATE);
            }
            responseJu.add(view);
        }
        return responseJu;
    }

    private List<ControlNotificacionViewList> getCovidNotificationsCompl(Integer periodo, Integer dayOfWeek, List<SaludIndCovid> listCovid, HashMap<String, HashMap<String, Object>> idPacienteList) throws ConsultaException, ControlNotificacionException {
        List<ControlNotificacionViewList> responseJu = new ArrayList<>();
        for(SaludIndCovid element : listCovid) {
            ControlNotificacionViewList view = new ControlNotificacionViewList();
            HashMap<String, Object> paciente = idPacienteList.get(element.getPacidfk());
            if(paciente != null) {
                if((Boolean) paciente.get("active") && paciente.get("idDevice") != null
                        && !((String)paciente.get("idDevice")).equalsIgnoreCase("null")) {
                    view.setIdDevice((String) paciente.get("idDevice"));
                    view.setIdUserApp(((Integer) paciente.get("idUserApp")).longValue());
                } else
                    continue;
            } else {
                Map<String, Object> responsePaciente = null;
                try {
                    responsePaciente = apiConfiguration.getPacieteByid(element.getPacidfk());
                } catch(Exception ex) {
                    logger.error("Paciente no encontrado en el sistema - {}", element.getPacidfk());
                    continue;
                }
                if(responsePaciente != null || !responsePaciente.isEmpty()) {
                    Map<String, Object> responseUserApp = null;
                    try {
                        responseUserApp = apiConfiguration.getUserAppById((int)responsePaciente.get("idUsuario"));
                    } catch (Exception ex) {
                        logger.error("Usuario no encontrado en el sistema - {}", responsePaciente.get("idUsuario"));
                        continue;
                    }
                    HashMap<String, Object> nuevoPaciente = new HashMap<>();
                    nuevoPaciente.put("active", String.valueOf(responseUserApp.get("status")).equalsIgnoreCase("activo"));
                    nuevoPaciente.put("idDevice", responsePaciente.get("idDevice"));
                    nuevoPaciente.put("idUserApp", responseUserApp.get("idUserApp"));
                    idPacienteList.put(element.getPacidfk(), nuevoPaciente);
                    if((boolean)nuevoPaciente.get("active") &&
                            nuevoPaciente.get("idDevice") != null) {
                        view.setIdUserApp(((Integer) responseUserApp.get("idUserApp")).longValue());
                        view.setIdDevice(String.valueOf(responsePaciente.get("idDevice")));
                    }
                    else {
                        logger.error("Usuario no activo o idDevice vacio: {}, {}", responseUserApp.get("idUserApp"), responsePaciente.get("idDevice"));
                        continue;
                    }
                } else {
                    logger.error("El paciente {} no ha podido ser agregado a las notificaciones de covid", element.getPacidfk());
                }
            }
            switch(dayOfWeek) {
                case Calendar.SUNDAY:
                    if(element.getCovid1do() && periodo == 1) {
                        view.setPeriodo(1);
                        view.setHora(element.getP1hora());
                    } else if(element.getCovid2do() && periodo == 2) {
                        view.setPeriodo(2);
                        view.setHora(element.getP2hora());
                    } else if(element.getCovid3do() && periodo == 3) {
                        view.setPeriodo(3);
                        view.setHora(element.getP3hora());
                    } else if(element.getCovid4do() && periodo == 4) {
                        view.setPeriodo(4);
                        view.setHora(element.getP4hora());
                    }
                    break;
                case Calendar.MONDAY:
                    if(element.getCovid1lu() && periodo == 1) {
                        view.setPeriodo(1);
                        view.setHora(element.getP1hora());
                    } else if(element.getCovid2lu() && periodo == 2) {
                        view.setPeriodo(2);
                        view.setHora(element.getP2hora());
                    } else if(element.getCovid3lu() && periodo == 3) {
                        view.setPeriodo(3);
                        view.setHora(element.getP3hora());
                    } else if(element.getCovid4lu() && periodo == 4) {
                        view.setPeriodo(4);
                        view.setHora(element.getP4hora());
                    }
                    break;
                case Calendar.TUESDAY:
                    if(element.getCovid1ma() && periodo == 1) {
                        view.setPeriodo(1);
                        view.setHora(element.getP1hora());
                    } else if(element.getCovid2ma() && periodo == 2) {
                        view.setPeriodo(2);
                        view.setHora(element.getP2hora());
                    } else if(element.getCovid3ma() && periodo == 3) {
                        view.setPeriodo(3);
                        view.setHora(element.getP3hora());
                    } else if(element.getCovid4ma() && periodo == 4) {
                        view.setPeriodo(4);
                        view.setHora(element.getP4hora());
                    }
                    break;
                case Calendar.WEDNESDAY:
                    if(element.getCovid1mi() && periodo == 1) {
                        view.setPeriodo(1);
                        view.setHora(element.getP1hora());
                    } else if(element.getCovid2mi() && periodo == 2) {
                        view.setPeriodo(2);
                        view.setHora(element.getP2hora());
                    } else if(element.getCovid3mi() && periodo == 3) {
                        view.setPeriodo(3);
                        view.setHora(element.getP3hora());
                    } else if(element.getCovid4mi() && periodo == 4) {
                        view.setPeriodo(4);
                        view.setHora(element.getP4hora());
                    }
                    break;
                case Calendar.THURSDAY:
                    if(element.getCovid1ju() && periodo == 1) {
                        view.setPeriodo(1);
                        view.setHora(element.getP1hora());
                    } else if(element.getCovid2ju() && periodo == 2) {
                        view.setPeriodo(2);
                        view.setHora(element.getP2hora());
                    } else if(element.getCovid3ju() && periodo == 3) {
                        view.setPeriodo(3);
                        view.setHora(element.getP3hora());
                    } else if(element.getCovid4ju() && periodo == 4) {
                        view.setPeriodo(4);
                        view.setHora(element.getP4hora());
                    }
                    break;
                case Calendar.FRIDAY:
                    if(element.getCovid1vi() && periodo == 1) {
                        view.setPeriodo(1);
                        view.setHora(element.getP1hora());
                    } else if(element.getCovid2vi() && periodo == 2) {
                        view.setPeriodo(2);
                        view.setHora(element.getP2hora());
                    } else if(element.getCovid3vi() && periodo == 3) {
                        view.setPeriodo(3);
                        view.setHora(element.getP3hora());
                    } else if(element.getCovid4vi() && periodo == 4) {
                        view.setPeriodo(4);
                        view.setHora(element.getP4hora());
                    }
                    break;
                case Calendar.SATURDAY:
                    if(element.getCovid1sa() && periodo == 1) {
                        view.setPeriodo(1);
                        view.setHora(element.getP1hora());
                    } else if(element.getCovid2sa() && periodo == 2) {
                        view.setPeriodo(2);
                        view.setHora(element.getP2hora());
                    } else if(element.getCovid3sa() && periodo == 3) {
                        view.setPeriodo(3);
                        view.setHora(element.getP3hora());
                    } else if(element.getCovid4sa() && periodo == 4) {
                        view.setPeriodo(4);
                        view.setHora(element.getP4hora());
                    }
                    break;
                default:
                    throw new ControlNotificacionException("Opcion de dia no es correcta", GenericException.LAYER_DAO, GenericException.ACTION_VALIDATE);
            }
            responseJu.add(view);
        }
        return responseJu;
    }



}
