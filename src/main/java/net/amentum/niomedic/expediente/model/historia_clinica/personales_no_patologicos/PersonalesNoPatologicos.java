package net.amentum.niomedic.expediente.model.historia_clinica.personales_no_patologicos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "personales_no_patologicos", indexes = {@Index(name = "idx_personales_no_patologicos_id_hcg", columnList = "id_personales_no_patologicos,id_historia_clinica")})
public class PersonalesNoPatologicos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_personales_no_patologicos")
    Long id;
    @NotNull(message = "id de historia clinica general vacio")
    @Column(name = "id_historia_clinica")
    Long idHistoriaClinica;
    String notas;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_vivienda")
    Vivienda vivienda;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_mascotas")
    Mascotas mascota;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_actividad_deportiva")
    ActividadDeportiva actividadDeportiva;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_habito_alimenticio")
    HabitoAlimenticio habitoAlimenticio;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_habito_higienico")
    HabitoHigienico habitoHigienico;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "personalesNoPatologicos")
    Collection<Consumo> consumoList = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_historia_laboral")
    HistoriaLaboral historiaLaboral;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "personalesNoPatologicos")
    Collection<TablaLaboral> tablaLaboralList = new ArrayList<>();

    @Override
    public String toString() {
        return "PersonalesNoPatologicos{" +
                "id=" + id +
                ", idHistoriaClinica=" + idHistoriaClinica +
                ", notas='" + notas + '\'' +
                '}';
    }
}
