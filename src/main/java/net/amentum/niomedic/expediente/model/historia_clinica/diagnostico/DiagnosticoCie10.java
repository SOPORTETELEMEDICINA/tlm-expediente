package net.amentum.niomedic.expediente.model.historia_clinica.diagnostico;

import net.amentum.niomedic.expediente.model.CatCie10;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "hc_diagnostico_cie10")
@AssociationOverrides({
        @AssociationOverride(name = "diagnosticoCie10Id.diagnostico", joinColumns = @JoinColumn(name="id_diagnostico")),
        @AssociationOverride(name= "diagnosticoCie10Id.catCie10",joinColumns = @JoinColumn(name = "id_cie10"))
})
public class DiagnosticoCie10 implements Serializable {

    private static final long serialVersionUID = 3886881163852847639L;

    @EmbeddedId
    DiagnosticoCie10Id diagnosticoCie10Id = new DiagnosticoCie10Id();

    public DiagnosticoCie10Id getDiagnosticoCie10Id() {
        return diagnosticoCie10Id;
    }

    public void setDiagnosticoCie10Id(DiagnosticoCie10Id diagnosticoCie10Id) {
        this.diagnosticoCie10Id = diagnosticoCie10Id;
    }

    @Transient
    public Diagnostico getDiagnostico() {
        return diagnosticoCie10Id.getDiagnostico();
    }

    public void setDiagnostico(Diagnostico diagnostico) {
        this.diagnosticoCie10Id.setDiagnostico(diagnostico);
    }

    @Transient
    public CatCie10 getCie10() {
        return diagnosticoCie10Id.getCatCie10();
    }

    public void setCie10(CatCie10 catCie10) {
        this.diagnosticoCie10Id.setCatCie10(catCie10);
    }

}
