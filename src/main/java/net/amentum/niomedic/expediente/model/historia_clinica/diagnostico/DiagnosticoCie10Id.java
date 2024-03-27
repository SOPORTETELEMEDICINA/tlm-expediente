package net.amentum.niomedic.expediente.model.historia_clinica.diagnostico;

import net.amentum.niomedic.expediente.model.CatCie10;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Embeddable
public class DiagnosticoCie10Id implements Serializable {
    private static final long serialVersionUID = 3497518890608589583L;

    @ManyToOne
    Diagnostico diagnostico;

    @ManyToOne
    CatCie10 catCie10;

    public Diagnostico getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(Diagnostico diagnostico) {
        this.diagnostico = diagnostico;
    }

    public CatCie10 getCatCie10() {
        return catCie10;
    }

    public void setCatCie10(CatCie10 catCie10) {
        this.catCie10 = catCie10;
    }
}
