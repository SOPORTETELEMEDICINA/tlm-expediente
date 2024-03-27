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
@Table(name = "interrogatorio_organo_sentidos_vision", indexes =  {@Index(name = "idx_interrogatorio_organo_sentidos_vision_id_hcg", columnList = "id_interrogatorio,id_historia_clinica")})
public class SentidoVision {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_interrogatorio")
    Long idInterrogatorio;
    @NotNull(message = "id de historia clinica general vacio")
    @Column(name = "id_historia_clinica")
    Long idHistoriaClinica;
    Boolean visionNormal;
    String visionNormalOjo;
    Boolean visionBorrosa;
    String visionBorrosaOjo;
    String visionOjo;
    Boolean ceguera;
    String cegueraOjo;
    Boolean cegueraDerecho;
    Boolean cegueraIzquierdo;
    Boolean cegueraBilateral;
    Boolean dolor;
    String dolorOjo;
    Boolean dolorDerecho;
    Boolean dolorIzquierdo;
    Boolean fotofobia;
    Boolean diplopia;
    String diplopiaOjo;
    Boolean diplopiaDerecho;
    Boolean diplopiaIzquierdo;
    Boolean prurito;
    String pruritoOjo;
    Boolean pruritoDerecho;
    Boolean pruritoIzquierdo;
    Boolean pruritoBilateral;
    Boolean hipermetropia;
    Boolean miopia;
    Boolean astigmatismo;
    Boolean presbicia;
    Boolean lentes;
    String lentesCausa;
    Boolean enrojecimiento;
    String enrojecimientoOjo;
    Boolean enrojecimientoDerecho;
    Boolean enrojecimientoIzquierdo;
    Boolean catarata;
    String catarataOjo;
    Boolean catarataBorrosa;
    Boolean catarataDerecho;
    Boolean catarataIzquierdo;
    Boolean catarataBilateral;
    Boolean resequedad;
    String resequedadOjo;
    Boolean resequedadDerecho;
    Boolean resequedadIzquierdo;
    Boolean resequedadBilateral;
    Boolean secrecion;
    String secrecionOjo;
    Boolean secrecionDerecho;
    Boolean secrecionIzquierdo;
    Boolean secrecionBilateral;
    String comentarios;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "sentidoVision")
    InterrogatorioAparatos interrogatorioAparatos;

    @Override
    public String toString() {
        return "SentidoVision{" +
                "idInterrogatorio=" + idInterrogatorio +
                ", idHistoriaClinica=" + idHistoriaClinica +
                ", visionNormal=" + visionNormal +
                ", visionNormalOjo=" + visionNormalOjo +
                ", visionBorrosa=" + visionBorrosa +
                ", visionBorrosaOjo='" + visionBorrosaOjo + '\'' +
                ", visionOjo='" + visionOjo + '\'' +
                ", ceguera=" + ceguera +
                ", cegueraOjo='" + cegueraOjo + '\'' +
                ", cegueraDerecho=" + cegueraDerecho +
                ", cegueraIzquierdo=" + cegueraIzquierdo +
                ", cegueraBilateral=" + cegueraBilateral +
                ", dolor=" + dolor +
                ", dolorOjo='" + dolorOjo + '\'' +
                ", dolorDerecho=" + dolorDerecho +
                ", dolorIzquierdo=" + dolorIzquierdo +
                ", fotofobia=" + fotofobia +
                ", diplopia=" + diplopia +
                ", diplopiaOjo='" + diplopiaOjo + '\'' +
                ", diplopiaDerecho=" + diplopiaDerecho +
                ", diplopiaIzquierdo=" + diplopiaIzquierdo +
                ", prurito=" + prurito +
                ", pruritoOjo='" + pruritoOjo + '\'' +
                ", pruritoDerecho=" + pruritoDerecho +
                ", pruritoIzquierdo=" + pruritoIzquierdo +
                ", pruritoBilateral=" + pruritoBilateral +
                ", hipermetropia=" + hipermetropia +
                ", miopia=" + miopia +
                ", astigmatismo=" + astigmatismo +
                ", presbicia=" + presbicia +
                ", lentes=" + lentes +
                ", lentesCausa='" + lentesCausa + '\'' +
                ", enrojecimiento=" + enrojecimiento +
                ", enrojecimientoOjo='" + enrojecimientoOjo + '\'' +
                ", enrojecimientoDerecho=" + enrojecimientoDerecho +
                ", enrojecimientoIzquierdo=" + enrojecimientoIzquierdo +
                ", catarata=" + catarata +
                ", catarataOjo='" + catarataOjo + '\'' +
                ", catarataBorrosa=" + catarataBorrosa +
                ", catarataDerecho=" + catarataDerecho +
                ", catarataIzquierdo=" + catarataIzquierdo +
                ", catarataBilateral=" + catarataBilateral +
                ", resequedad=" + resequedad +
                ", resequedadOjo='" + resequedadOjo + '\'' +
                ", resequedadDerecho=" + resequedadDerecho +
                ", resequedadIzquierdo=" + resequedadIzquierdo +
                ", resequedadBilateral=" + resequedadBilateral +
                ", secrecion=" + secrecion +
                ", secrecionOjo='" + secrecionOjo + '\'' +
                ", secrecionDerecho=" + secrecionDerecho +
                ", secrecionIzquierdo=" + secrecionIzquierdo +
                ", secrecionBilateral=" + secrecionBilateral +
                ", comentarios='" + comentarios + '\'' +
                '}';
    }
}
