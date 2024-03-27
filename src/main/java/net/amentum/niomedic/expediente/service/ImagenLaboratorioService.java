package net.amentum.niomedic.expediente.service;

import net.amentum.niomedic.expediente.exception.ImagenLaboratorioException;
import net.amentum.niomedic.expediente.views.ArchivoView;
import net.amentum.niomedic.expediente.views.ImagenLaboratorioListView;
import net.amentum.niomedic.expediente.views.ImagenLaboratorioView;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ImagenLaboratorioService {
   //   Page<ImagenLaboratorioListView> getImagenLaboratorioPage(Boolean active, String name, Integer page, Integer size, String orderColumn, String orderType) throws ImagenLaboratorioException;
   Page<ImagenLaboratorioListView> getImagenLaboratorioPage(Boolean active, String idPaciente, Long idConsulta, Integer page, Integer size, String orderColumn, String orderType) throws ImagenLaboratorioException;

   //   public void surbirArchivo(String idPaciente, ArchivoView archivobase64, String direccion) throws ImagenLaboratorioException;
   public void surbirArchivo(String idPaciente, ImagenLaboratorioView archivobase64, String direccion) throws ImagenLaboratorioException;

   void createImagenLaboratorio(ImagenLaboratorioView documentosView) throws ImagenLaboratorioException;
   
   void eliminarImagen(Long idImagenLaboratorio) throws ImagenLaboratorioException;

   List<ImagenLaboratorioListView> getImagenLaboratorioPorPadecimiento(Long idPadecimiento) throws ImagenLaboratorioException;
}
