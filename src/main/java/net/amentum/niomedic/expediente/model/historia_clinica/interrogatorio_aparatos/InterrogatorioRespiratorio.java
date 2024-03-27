package net.amentum.niomedic.expediente.model.historia_clinica.interrogatorio_aparatos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "interrogatorio_respiratorio", indexes =  {@Index(name = "idx_interrogatorio_respiratorio_id_hcg", columnList = "id_interrogatorio,id_historia_clinica")})
public class InterrogatorioRespiratorio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_interrogatorio")
    Long idInterrogatorio;

    @NotNull(message = "id de historia clinica general vacio")
    @Column(name = "id_historia_clinica")
    Long idHistoriaClinica;

    Boolean resAsintomatico;
    Boolean dolorToracico;
    String dolorToracicoEvolucion;
    String dolorToracicoTratamiento;
    Boolean hemoptisis;
    String hemoptisisEvolucion;
    String hemoptisisTratamiento;
    Boolean cianosis;
    String cianosisEvolucion;
    String cianosisTratamiento;
    Boolean ortopnea;
    Boolean humpro;
    Boolean humnopro;
    Boolean tosSeca;
    Boolean hipo;
    Boolean silvidores;
    Integer almohadasDuerme;
    Boolean estornudos;
    String estornudosSalva;
    Boolean rinitis;
    String rinititsEvolucion;
    String rinitisTratamiento;
    Boolean dolorMandibular;
    Boolean mandibularDerecha;
    Boolean mandibularIzquierda;
    Boolean diaforesis;
    String diaforesisInput;
    Boolean disnea;
    String disneaMotivo;
    Boolean disneaGesfuerzos;
    Boolean disneaMesfuerzos;
    Boolean disneaPesfuerzos;
    String disneaEvolucion;
    Boolean usoOxigeno;
    String oxigenoEvolucion;
    Boolean rinorreaSi;
    String rinorrea;
    Boolean rinolaliaSi;
    String rinolalia;
    Boolean disfonia;
    String disfoniaEvolucion;
    String disfoniaTratamiento;
    Boolean amigdalitis;
    String amigdalitisFrecuencia;
    String amigdalitisEvolucion;
    String amigdalitisTratamiento;
    Boolean expectoracion;
    String expecTipo;
    Boolean expecVerde;
    Boolean expecAmarilla;
    Boolean expecTransparente;
    Boolean expecBlanquecina;
    String expecEvolucion;
    String expecTratamiento;
    Boolean faringitis;
    String faringitisEvolucion;
    String faringitisTratamiento;
    Boolean dolorTorax;
    String dolorToraxEvolucion;
    String dolorToraxTratamiento;
    Boolean ruidos;
    String ruidosFrecuencia;
    String ruidosEvolucion;
    String comentarios;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "interrogatorioRespiratorio")
    InterrogatorioAparatos interrogatorioAparatos;

    @Override
    public String toString() {
        return "InterrogatorioRespiratorio{" +
                "idInterrogatorio=" + idInterrogatorio +
                ", idHistoriaClinica=" + idHistoriaClinica +
                ", resAsintomatico=" + resAsintomatico +
                ", dolorToracico=" + dolorToracico +
                ", dolorToracicoEvolucion='" + dolorToracicoEvolucion + '\'' +
                ", dolorToracicoTratamiento='" + dolorToracicoTratamiento + '\'' +
                ", hemoptisis=" + hemoptisis +
                ", hemoptisisEvolucion='" + hemoptisisEvolucion + '\'' +
                ", hemoptisisTratamiento='" + hemoptisisTratamiento + '\'' +
                ", cianosis=" + cianosis +
                ", cianosisEvolucion='" + cianosisEvolucion + '\'' +
                ", cianosisTratamiento='" + cianosisTratamiento + '\'' +
                ", ortopnea=" + ortopnea +
                ", humpro=" + humpro +
                ", humnopro=" + humnopro +
                ", tosSeca=" + tosSeca +
                ", hipo=" + hipo +
                ", silvidores=" + silvidores +
                ", almohadasDuerme=" + almohadasDuerme +
                ", estornudos=" + estornudos +
                ", estornudosSalva='" + estornudosSalva + '\'' +
                ", rinitis=" + rinitis +
                ", rinititsEvolucion='" + rinititsEvolucion + '\'' +
                ", rinitisTratamiento='" + rinitisTratamiento + '\'' +
                ", dolorMandibular=" + dolorMandibular +
                ", mandibularDerecha=" + mandibularDerecha +
                ", mandibularIzquierda=" + mandibularIzquierda +
                ", diaforesis=" + diaforesis +
                ", diaforesisInput='" + diaforesisInput + '\'' +
                ", disnea=" + disnea +
                ", disneaMotivo='" + disneaMotivo + '\'' +
                ", disneaGesfuerzos=" + disneaGesfuerzos +
                ", disneaMesfuerzos=" + disneaMesfuerzos +
                ", disneaPesfuerzos=" + disneaPesfuerzos +
                ", disneaEvolucion='" + disneaEvolucion + '\'' +
                ", usoOxigeno=" + usoOxigeno +
                ", oxigenoEvolucion='" + oxigenoEvolucion + '\'' +
                ", rinorreaSi=" + rinorreaSi +
                ", rinorrea='" + rinorrea + '\'' +
                ", rinolaliaSi=" + rinolaliaSi +
                ", rinolalia='" + rinolalia + '\'' +
                ", disfonia=" + disfonia +
                ", disfoniaEvolucion='" + disfoniaEvolucion + '\'' +
                ", disfoniaTratamiento='" + disfoniaTratamiento + '\'' +
                ", amigdalitis=" + amigdalitis +
                ", amigdalitisFrecuencia='" + amigdalitisFrecuencia + '\'' +
                ", amigdalitisEvolucion='" + amigdalitisEvolucion + '\'' +
                ", amigdalitisTratamiento='" + amigdalitisTratamiento + '\'' +
                ", expectoracion=" + expectoracion +
                ", expecTipo='" + expecTipo + '\'' +
                ", expecVerde=" + expecVerde +
                ", expecAmarilla=" + expecAmarilla +
                ", expecTransparente=" + expecTransparente +
                ", expecBlanquecina=" + expecBlanquecina +
                ", expecEvolucion='" + expecEvolucion + '\'' +
                ", expecTratamiento='" + expecTratamiento + '\'' +
                ", faringitis=" + faringitis +
                ", faringitisEvolucion='" + faringitisEvolucion + '\'' +
                ", faringitisTratamiento='" + faringitisTratamiento + '\'' +
                ", dolorTorax=" + dolorTorax +
                ", dolorToraxEvolucion='" + dolorToraxEvolucion + '\'' +
                ", dolorToraxTratamiento='" + dolorToraxTratamiento + '\'' +
                ", ruidos=" + ruidos +
                ", ruidosFrecuencia='" + ruidosFrecuencia + '\'' +
                ", ruidosEvolucion='" + ruidosEvolucion + '\'' +
                ", comentarios='" + comentarios + '\'' +
                '}';
    }
}
