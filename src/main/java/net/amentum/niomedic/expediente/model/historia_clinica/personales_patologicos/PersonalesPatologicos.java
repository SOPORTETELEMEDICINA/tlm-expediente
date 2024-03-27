package net.amentum.niomedic.expediente.model.historia_clinica.personales_patologicos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "personal_patologico", indexes = {@Index(name = "idx_personal_patologico_id_hcg", columnList = "id_personal_patologico,id_historia_clinica")})
public class PersonalesPatologicos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_personal_patologico")
    Long id;
    @NotNull(message = "id de historia clinica general vacio")
    @Column(name = "id_historia_clinica")
    Long idHistoriaClinica;
    Boolean alergias;
    String inputAlergias;
    Boolean cirugias;
    String inputCirugias;
    Boolean transfusiones;
    String inputTransfusiones;
    Boolean hospitalizacion;
    String inputHospitalizacion;
    String comentarios;
    String notas;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "personalesPatologicos")
    Collection<PadecimientoPersonalPatologico> padecimientos = new ArrayList<>();

    @Override
    public String toString() {
        return "PersonalesPatologicos{" +
                "id=" + id +
                ", idHistoriaClinica=" + idHistoriaClinica +
                ", alergias=" + alergias +
                ", inputAlergias='" + inputAlergias + '\'' +
                ", cirugias=" + cirugias +
                ", inputCirugias='" + inputCirugias + '\'' +
                ", transfusiones=" + transfusiones +
                ", inputTransfusiones='" + inputTransfusiones + '\'' +
                ", hospitalizacion=" + hospitalizacion +
                ", inputHospitalizacion='" + inputHospitalizacion + '\'' +
                ", comentarios='" + comentarios + '\'' +
                ", notas='" + notas + '\'' +
                '}';
    }
}
