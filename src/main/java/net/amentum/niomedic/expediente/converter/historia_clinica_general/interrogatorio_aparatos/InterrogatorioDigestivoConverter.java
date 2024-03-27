package net.amentum.niomedic.expediente.converter.historia_clinica_general.interrogatorio_aparatos;

import net.amentum.niomedic.expediente.model.historia_clinica.interrogatorio_aparatos.InterrogatorioDigestivo;
import net.amentum.niomedic.expediente.views.historia_clinica_general.interrogatorio_aparatos.InterrogatorioDigestivoView;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;

@Component
public class InterrogatorioDigestivoConverter {

    public InterrogatorioDigestivo toEntity(InterrogatorioDigestivoView view) {
        try {
            InterrogatorioDigestivo entity = new InterrogatorioDigestivo();
            BeanUtils.copyProperties(entity, view);
            return entity;
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }

    public InterrogatorioDigestivoView toView(InterrogatorioDigestivo entity) {
        try {
            InterrogatorioDigestivoView view = new InterrogatorioDigestivoView();
            BeanUtils.copyProperties(view, entity);
            return view;
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }

}
