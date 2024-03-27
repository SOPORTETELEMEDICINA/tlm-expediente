package net.amentum.niomedic.expediente.model.historia_clinica.exploracion_fisica;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ef_piel", indexes =  {@Index(name = "idx_ef_piel_id_hcg", columnList = "id_ef_piel,id_historia_clinica")})
public class Piel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ef_piel")
    Long idPiel;
    @NotNull(message = "id de historia clinica general vacio")
    @Column(name = "id_historia_clinica")
    Long idHistoriaClinica;
    Boolean planasMacula;
    Boolean planasTelangiectasias;
    Boolean planasEsclerosis;
    Boolean elevadasPapula;
    Boolean elevadasPlaca;
    Boolean elevadasNodulo;
    Boolean elevadasVesicula;
    Boolean elevadasAmpolla;
    Boolean elevadasAbsceso;
    Boolean elevadasEscara;
    Boolean elevadasCicatriz;
    Boolean deprimidasAtrofia;
    Boolean deprimidasExcoriacion;
    Boolean deprimidasErosion;
    Boolean deprimidasUlcera;
    String comentarios;
    Boolean manchas;
    Boolean masas;
    Boolean lesiones;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "piel")
    ExploracionFisica exploracionFisica;

    @Override
    public String toString() {
        return "Piel{" +
                "idPiel=" + idPiel +
                ", idHistoriaClinica=" + idHistoriaClinica +
                ", planasMacula=" + planasMacula +
                ", planasTelangiectasias=" + planasTelangiectasias +
                ", planasEsclerosis=" + planasEsclerosis +
                ", elevadasPapula=" + elevadasPapula +
                ", elevadasPlaca=" + elevadasPlaca +
                ", elevadasNodulo=" + elevadasNodulo +
                ", elevadasVesicula=" + elevadasVesicula +
                ", elevadasAmpolla=" + elevadasAmpolla +
                ", elevadasAbsceso=" + elevadasAbsceso +
                ", elevadasEscara=" + elevadasEscara +
                ", elevadasCicatriz=" + elevadasCicatriz +
                ", deprimidasAtrofia=" + deprimidasAtrofia +
                ", deprimidasExcoriacion=" + deprimidasExcoriacion +
                ", deprimidasErosion=" + deprimidasErosion +
                ", deprimidasUlcera=" + deprimidasUlcera +
                ", comentarios='" + comentarios + '\'' +
                ", manchas=" + manchas +
                ", masas=" + masas +
                ", lesiones=" + lesiones +
                '}';
    }
}
