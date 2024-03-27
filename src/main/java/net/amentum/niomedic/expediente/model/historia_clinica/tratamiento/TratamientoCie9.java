package net.amentum.niomedic.expediente.model.historia_clinica.tratamiento;

import net.amentum.niomedic.expediente.model.CatCie9;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "hc_tratamiento_cie9")
@AssociationOverrides({
        @AssociationOverride(name = "tratamientoCie9Id.HCTratamiento", joinColumns = @JoinColumn(name="id_diagnostico")),
        @AssociationOverride(name= "tratamientoCie9Id.catCie9",joinColumns = @JoinColumn(name = "id_cie9"))
})
public class TratamientoCie9 implements Serializable {

    private static final long serialVersionUID = 3886881163852847639L;

    @EmbeddedId
    TratamientoCie9Id tratamientoCie9Id = new TratamientoCie9Id();

    public TratamientoCie9Id getTratamientoCie9Id() {
        return tratamientoCie9Id;
    }

    public void setTratamientoCie9Id(TratamientoCie9Id tratamientoCie9Id) {
        this.tratamientoCie9Id = tratamientoCie9Id;
    }

    @Transient
    public HCTratamiento getTratamiento() {
        return tratamientoCie9Id.getTratamiento();
    }

    public void setTratamiento(HCTratamiento HCTratamiento) {
        this.tratamientoCie9Id.setTratamiento(HCTratamiento);
    }

    @Transient
    public CatCie9 getCatCie9() {
        return tratamientoCie9Id.getCatCie9();
    }

    public void setCatCie9(CatCie9 catCie9) {
        this.tratamientoCie9Id.setCatCie9(catCie9);
    }

}
