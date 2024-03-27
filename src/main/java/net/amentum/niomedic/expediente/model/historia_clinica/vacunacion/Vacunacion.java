package net.amentum.niomedic.expediente.model.historia_clinica.vacunacion;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "hc_vacunacion", indexes = {@Index(name = "idx_vacunacion_id_hcg", columnList = "id_vacunacion,id_historia_clinica")})
public class Vacunacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_vacunacion")
    Long idVacunacion;
    @NotNull(message = "id de historia clinica general vacio")
    @Column(name = "id_historia_clinica")
    Long idHistoriaClinica;
    String nombre;
    String enfermedad;
    String dosisOrdinal;
    String dosisEdad;
    Date fecha;
    String notas;
    String dosis;

    @Override
    public String toString() {
        return "Vacunacion{" +
                "idVacunacion=" + idVacunacion +
                ", idHistoriaClinica=" + idHistoriaClinica +
                ", nombre='" + nombre + '\'' +
                ", enfermedad='" + enfermedad + '\'' +
                ", dosisOrdinal='" + dosisOrdinal + '\'' +
                ", dosisEdad='" + dosisEdad + '\'' +
                ", fecha=" + fecha +
                ", notas='" + notas + '\'' +
                ", dosis='" + dosis + '\'' +
                '}';
    }
}