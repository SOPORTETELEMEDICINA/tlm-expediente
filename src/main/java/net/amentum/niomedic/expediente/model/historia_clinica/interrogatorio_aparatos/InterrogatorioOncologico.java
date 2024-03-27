package net.amentum.niomedic.expediente.model.historia_clinica.interrogatorio_aparatos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "interrogatorio_oncologico", indexes =  {@Index(name = "idx_interrogatorio_oncologico_id_hcg", columnList = "id_interrogatorio,id_historia_clinica")})
public class InterrogatorioOncologico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_interrogatorio")
    Long idInterrogatorio;

    @NotNull(message = "id de historia clinica general vacio")
    @Column(name = "id_historia_clinica")
    Long idHistoriaClinica;

    Boolean neoplasias;
    String neoplasiasLocalizacion;
    String neoplasiasTipo;
    String neoplasiasTiempoEvol;
    String neoplasiasTratamiento;
    String neoplasiasComplicaciones;
    String pronostico;
    String dolorLocalizacion;
    Boolean dolorOncologico;
    Boolean dolorPorEnfermedad;
    Boolean dolorPorCompresion;
    String dolorOtro;
    String dolorEvn;
    String estadio;
    String conociminetoPatologia;
    String enfermedadesInmunoCuales;
    String enfermedadesInmunoTiempoEvol;
    String enfermedadesInmunoTratamiento;
    String comentarios;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "interrogatorioOncologico")
    InterrogatorioAparatos interrogatorioAparatos;

    @Override
    public String toString() {
        return "InterrogatorioOncologico{" +
                "idInterrogatorio=" + idInterrogatorio +
                ", idHistoriaClinica=" + idHistoriaClinica +
                ", neoplasias=" + neoplasias +
                ", neoplasiasLocalizacion='" + neoplasiasLocalizacion + '\'' +
                ", neoplasiasTipo='" + neoplasiasTipo + '\'' +
                ", neoplasiasTiempoEvol='" + neoplasiasTiempoEvol + '\'' +
                ", neoplasiasTratamiento='" + neoplasiasTratamiento + '\'' +
                ", neoplasiasComplicaciones='" + neoplasiasComplicaciones + '\'' +
                ", pronostico='" + pronostico + '\'' +
                ", dolorLocalizacion='" + dolorLocalizacion + '\'' +
                ", dolorOncologico=" + dolorOncologico +
                ", dolorPorEnfermedad=" + dolorPorEnfermedad +
                ", dolorPorCompresion=" + dolorPorCompresion +
                ", dolorOtro='" + dolorOtro + '\'' +
                ", dolorEVN='" + dolorEvn + '\'' +
                ", estadio='" + estadio + '\'' +
                ", conociminetoPatologia='" + conociminetoPatologia + '\'' +
                ", enfermedadesInmunoCuales='" + enfermedadesInmunoCuales + '\'' +
                ", enfermedadesInmunoTiempoEvol='" + enfermedadesInmunoTiempoEvol + '\'' +
                ", enfermedadesInmunoTratamiento='" + enfermedadesInmunoTratamiento + '\'' +
                ", comentarios='" + comentarios + '\'' +
                '}';
    }
}
