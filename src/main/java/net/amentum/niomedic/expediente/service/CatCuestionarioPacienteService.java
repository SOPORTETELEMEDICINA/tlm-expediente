package net.amentum.niomedic.expediente.service;

import net.amentum.niomedic.expediente.exception.CatCuestionarioPacienteException;
import net.amentum.niomedic.expediente.views.CatCuestionarioPacienteView;

public interface CatCuestionarioPacienteService {

    void createCuestionarioPaciente(CatCuestionarioPacienteView view) throws CatCuestionarioPacienteException;

}
