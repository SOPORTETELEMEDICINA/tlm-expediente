package net.amentum.niomedic.expediente.model.historia_clinica.heredo_familiares;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "hf_enfermedades", indexes = {@Index(name = "idx_hf_enfermedades_id_hcg", columnList = "id_hf_enfermedades,id_historia_clinica")})
public class Enfermedades {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_hf_enfermedades")
    Long idHfEnfermedades;
    @NotNull(message = "id de historia clinica general vacio")
    @Column(name = "id_historia_clinica")
    Long idHistoriaClinica;
    Boolean tieneEnfermedad;
    String enfermedad;
    Boolean Padre;
    String infoPadre;
    Boolean Madre;
    String infoMadre;
    Boolean Hermanos;
    String infoHermanos;
    Boolean abuPaternos;
    String infoAbuPaternos;
    Boolean abuMaternos;
    String infoAbuMaternos;
    Boolean tiosPaternos;
    String infoTiosPaternos;
    Boolean tiosMaternos;
    String infoTiosMaternos;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_heredo_familiares", referencedColumnName = "id_heredo_familiares")
    HeredoFamiliares heredoFamiliares;

    @Override
    public String toString() {
        return "Enfermedades{" +
                "idHfEnfermedades=" + idHfEnfermedades +
                ", idHistoriaClinica=" + idHistoriaClinica +
                ", tieneEnfermedad=" + tieneEnfermedad +
                ", enfermedad='" + enfermedad + '\'' +
                ", Padre=" + Padre +
                ", infoPadre='" + infoPadre + '\'' +
                ", Madre=" + Madre +
                ", infoMadre='" + infoMadre + '\'' +
                ", Hermanos=" + Hermanos +
                ", infoHermanos='" + infoHermanos + '\'' +
                ", abuPaternos=" + abuPaternos +
                ", infoAbuPaternos='" + infoAbuPaternos + '\'' +
                ", abuMaternos=" + abuMaternos +
                ", infoAbuMaternos='" + infoAbuMaternos + '\'' +
                ", tiosPaternos=" + tiosPaternos +
                ", infoTiosPaternos='" + infoTiosPaternos + '\'' +
                ", tiosMaternos=" + tiosMaternos +
                ", infoTiosMaternos='" + infoTiosMaternos + '\'' +
                '}';
    }
}
