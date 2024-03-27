package net.amentum.niomedic.expediente.service;


import net.amentum.niomedic.expediente.model.Format;
import org.springframework.data.domain.Page;

import net.amentum.niomedic.expediente.exception.FormatException;
import net.amentum.niomedic.expediente.views.FormatResultListView;
import net.amentum.niomedic.expediente.views.FormatResultView;
import net.amentum.niomedic.expediente.views.FormatView;

import java.util.List;

/**
 * @author  by marellano on 14/06/17.
 * Interface para administrar formatos, permite: crear, editar, obtener y eliminar un formato.
 */
public interface FormatService {

    /**
     * Método para crear nuevo formato
     * @param formatView nuevo formato a guardar en base de datos
     * @throws FormatException <br/> 1.- si no es posible guardar el nuevo formato en la bd
     * */
//    void addFormat(FormatView formatView) throws FormatException;
    FormatView addFormat(FormatView formatView) throws FormatException;

    /**
     * Método para editar un formato
     * @param formatView editar formato  en base de datos
     * @throws FormatException <br/> 1.- si no es posible modificar el formato en la base de datos
     * */
//    void editFormat(FormatView formatView) throws FormatException;
    FormatView editFormat(FormatView formatView) throws FormatException;

    /**
     * Método para eliminar un formato
     * @param idFormat eliminar un formato en base de datos
     * @throws FormatException <br/> 1.- si no es posible eliminar el formato en la base de datos
     * */
    void deleteFormat(Long idFormat) throws FormatException ;

    /**
     * Método para modificar el estatus de un formato
     * @param idFormat modificar estatus de un formato en base de datos
     * @throws FormatException <br/> 1.- si no es posible modificar el estatus del formato en la base de datos
     * */
    void updateStatusFormat(Long idFormat) throws FormatException ;

    /**
     * Método para obtener detalles de un formato
     * @param idFormat obtener formato  de base de datos
     * @return FormatView detalles del formato
     * @throws FormatException <br/> 1.- si no es posible obtener detalles del formato en la base de datos
     * */
    FormatView getDetailsFormat(Long idFormat) throws FormatException;

    /**
     * Método para obtener toda la lista de formatos
     * @return List<FormatView> lista de formatos
     * @throws FormatException <br/> 1.- si no es posible obtener la lista de formatos en la base de datos
     * */
    List<FormatView> getAllFormats() throws FormatException;

    /**
     * Método para obtener toda la lista de formatos de forma paginada
     * @return Page<FormatView> lista de formatos paginada
     * @throws FormatException <br/> 1.- si no es posible obtener la lista de formatos paginada en la base de datos
     * */
    Page<FormatView> getFormatsPage(String title, Integer page,Integer size,String orderColumn,String orderType,
                                    Boolean active,Integer version,
                                    Boolean general, String search) throws FormatException;

    /**
     * Método para crear un formato result
     * @param formatResultView formato result a guardar en base de datos
     * @throws FormatException <br/> 1.- si no es posible guardar el nuevo formato en la bd
     * */
//    void addFormatResult(FormatResultView formatResultView) throws FormatException;
    FormatResultView addFormatResult(FormatResultView formatResultView) throws FormatException;

    /**
     * Método para obtener los formatos de acuerdo al idTicket o idTask
     * @param idTask identificador unico de la tarea
     * @param idTicket identificador unico del ticket
     * @throws FormatException <br/> 1.- si no es posible guardar el nuevo formato en la bd
     * */
    List<FormatResultView> getFormatResult(Long idConsulta, Boolean active) throws FormatException;

    /**
     * Método para obtener los detalles del resultado de formato
     * @param idFormatResult identificador unico del resultado del formato
     * @throws FormatException <br/> 1.- si no es posible obtener el resultado de formato
     * */
    FormatResultView getFormatResultDetails(Long idFormatResult) throws FormatException;

    /**
     * Método para obtener los formatos de acuerdo a una categoria
     * @param idTaskCategory identificador unico de la categoria de la tarea
     * @param idTicketCategory identificador unico de la categoria del ticket
     * @throws FormatException <br/> 1.- si no es posible obtener los formatos
     * */
   // List<FormatView> getFormatByCategory(Long idTaskCategory, Long idTicketCategory) throws FormatException;
    /**
     * Método para obtener listado de resultado filtrado por consulta
     * @param idTask identificador unico de la tarea
     * @param idTicket identificador unico del ticket
     * @throws FormatException se usa para validaciones
     * */
    
    List<FormatResultListView> getFormatResultListView(Long idConsulta) throws FormatException;

}
