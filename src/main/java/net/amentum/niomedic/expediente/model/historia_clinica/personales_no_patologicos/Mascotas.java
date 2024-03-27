package net.amentum.niomedic.expediente.model.historia_clinica.personales_no_patologicos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pnp_mascotas", indexes = {@Index(name = "idx_pnp_mascotas_id_hcg", columnList = "id_mascotas,id_historia_clinica")})
public class Mascotas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mascotas")
    Long idMascotas;
    @NotNull(message = "id de historia clinica general vacio")
    @Column(name = "id_historia_clinica")
    Long idHistoriaClinica;
    Boolean mascotas;
    Boolean perro;
    Boolean gato;
    Boolean ave;
    Boolean conejo;
    Boolean tortuga;
    Boolean reptil;
    Boolean roedor;
    Boolean animalCorral;
    String nivelConvivencia;
    String comentarios;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "mascota")
    PersonalesNoPatologicos personalesNoPatologicos;

    @Override
    public String toString() {
        return "Mascotas{" +
                "idMascotas=" + idMascotas +
                ", idHistoriaClinica=" + idHistoriaClinica +
                ", mascotas=" + mascotas +
                ", perro=" + perro +
                ", gato=" + gato +
                ", ave=" + ave +
                ", conejo=" + conejo +
                ", tortuga=" + tortuga +
                ", reptil=" + reptil +
                ", roedor=" + roedor +
                ", animalCorral=" + animalCorral +
                ", nivelConvivencia='" + nivelConvivencia + '\'' +
                ", comentarios='" + comentarios + '\'' +
                '}';
    }
}
