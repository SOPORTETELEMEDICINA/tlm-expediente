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
@Table(name = "interrogatorio_organo_sentidos_audicion", indexes =  {@Index(name = "idx_interrogatorio_organo_sentidos_audicion_id_hcg", columnList = "id_interrogatorio,id_historia_clinica")})
public class SentidoAudicion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_interrogatorio")
    Long idInterrogatorio;
    @NotNull(message = "id de historia clinica general vacio")
    @Column(name = "id_historia_clinica")
    Long idHistoriaClinica;
    Boolean audicionNormal;
    String audicionNormalOido;
    Boolean audicionHipo = false;
    String audicionHipoOido;
    Boolean audicionHiper = false;
    String audicionHiperOido;
    Boolean audicionAnacusia = false;
    String audicioAnacusiaOido;
    Boolean audicionOtalgia = false;
    String audicionOtalgiaOido;
    Boolean audicionTinitus = false;
    String audicionTinitusOido;
    Boolean secrecionOtorrea = false;
    String secrecionOtorreaOido;
    Boolean secrecionOtorraquia = false;
    String secrecionOtorraquiaOido;
    Boolean secrecionOtorragia = false;
    String secrecionOtorragiaOido;
    String secrecionInput;
    String comentarios;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "sentidoAudicion")
    InterrogatorioAparatos interrogatorioAparatos;

    @Override
    public String toString() {
        return "SentidoAudicion{" +
                "idInterrogatorio=" + idInterrogatorio +
                ", idHistoriaClinica=" + idHistoriaClinica +
                ", audicionNormalOido='" + audicionNormalOido + '\'' +
                ", audicionHipo=" + audicionHipo +
                ", audicionHipoOido='" + audicionHipoOido + '\'' +
                ", audicionHiper=" + audicionHiper +
                ", audicionHiperOido='" + audicionHiperOido + '\'' +
                ", audicionAnacusia=" + audicionAnacusia +
                ", audicioAnacusiaOido='" + audicioAnacusiaOido + '\'' +
                ", audicionOtalgia=" + audicionOtalgia +
                ", audicionOtalgiaOido='" + audicionOtalgiaOido + '\'' +
                ", audicionTinitus=" + audicionTinitus +
                ", audicionTinitusOido='" + audicionTinitusOido + '\'' +
                ", secrecionOtorrea=" + secrecionOtorrea +
                ", secrecionOtorreaOido='" + secrecionOtorreaOido + '\'' +
                ", secrecionOtorraquia=" + secrecionOtorraquia +
                ", secrecionOtorraquiaOido='" + secrecionOtorraquiaOido + '\'' +
                ", secrecionOtorragia=" + secrecionOtorragia +
                ", secrecionOtorragiaOido='" + secrecionOtorragiaOido + '\'' +
                ", secrecionInput='" + secrecionInput + '\'' +
                ", comentarios='" + comentarios + '\'' +
                '}';
    }
}
