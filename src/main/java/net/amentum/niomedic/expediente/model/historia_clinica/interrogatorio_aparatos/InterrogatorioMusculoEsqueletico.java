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
@Table(name = "interrogatorio_musculo_esqueletico",
        indexes = {@Index(name = "idx_interrogatorio_musculo_esqueletico_id_hcg", columnList = "id_interrogatorio,id_historia_clinica")})
public class InterrogatorioMusculoEsqueletico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_interrogatorio")
    Long idInterrogatorio;

    @NotNull(message = "id de historia clinica general vacio")
    @Column(name = "id_historia_clinica")
    Long idHistoriaClinica;

    Boolean asintomatico;
    Boolean inflamacion;
    String inflamacionLocalizacion;
    String inflamacionTiempoEvol;
    Boolean dolor;
    String dolorLocalizacion;
    String dolorTiempoEvol;
    String dolorEvn;
    Boolean debilidadMuscular;
    String debilidadMuscularLocalizacion;
    String debilidadMuscularTiempoEvol;
    Boolean secrecion;
    Boolean limitacionMovimientos;
    String limitacionMovimientosLocalizacion;
    Boolean limitacionMovimientosNormal;
    Boolean limitacionMovimientosRestringido;
    Boolean limitacionMovimientosAumentado;
    Boolean deformidades;
    String deformidadesLocalizacion;
    String deformidadesTiempoEvol;
    Boolean calor;
    String calorLocalizacion;
    Boolean eritema;
    String eritemaLocalizacion;
    Boolean crepitaciones;
    String crepitacionesLocalizacion;
    String comentarios;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "interrogatorioMusculoEsqueletico")
    InterrogatorioAparatos interrogatorioAparatos;

    @Override
    public String toString() {
        return "InterrogatorioMusculoEsqueletico{" +
                "idInterrogatorio=" + idInterrogatorio +
                ", idHistoriaClinica=" + idHistoriaClinica +
                ", asintomatico=" + asintomatico +
                ", inflamacion=" + inflamacion +
                ", inflamacionLocalizacion='" + inflamacionLocalizacion + '\'' +
                ", inflamacionTiempoEvol='" + inflamacionTiempoEvol + '\'' +
                ", dolor=" + dolor +
                ", dolorLocalizacion='" + dolorLocalizacion + '\'' +
                ", dolorTiempoEvol='" + dolorTiempoEvol + '\'' +
                ", dolorEvn=" + dolorEvn +
                ", debilidadMuscular=" + debilidadMuscular +
                ", debilidadMuscularLocalizacion='" + debilidadMuscularLocalizacion + '\'' +
                ", debilidadMuscularTiempoEvol='" + debilidadMuscularTiempoEvol + '\'' +
                ", secrecion=" + secrecion +
                ", limitacionMovimientos=" + limitacionMovimientos +
                ", limitacionMovimientosLocalizacion='" + limitacionMovimientosLocalizacion + '\'' +
                ", limitacionMovimientosNormal=" + limitacionMovimientosNormal +
                ", limitacionMovimientosRestringido=" + limitacionMovimientosRestringido +
                ", limitacionMovimientosAumentado=" + limitacionMovimientosAumentado +
                ", deformidades=" + deformidades +
                ", deformidadesLocalizacion='" + deformidadesLocalizacion + '\'' +
                ", deformidadesTiempoEvol='" + deformidadesTiempoEvol + '\'' +
                ", calor=" + calor +
                ", calorLocalizacion='" + calorLocalizacion + '\'' +
                ", eritema=" + eritema +
                ", eritemaLocalizacion='" + eritemaLocalizacion + '\'' +
                ", crepitaciones=" + crepitaciones +
                ", crepitacionesLocalizacion='" + crepitacionesLocalizacion + '\'' +
                ", comentarios='" + comentarios + '\'' +
                '}';
    }
}
