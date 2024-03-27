package net.amentum.niomedic.expediente.model.historia_clinica.tratamiento;

import net.amentum.niomedic.expediente.model.CatCie9;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Embeddable
public class TratamientoCie9Id implements Serializable {
    private static final long serialVersionUID = 3497518890608589584L;

    @ManyToOne
    HCTratamiento HCTratamiento;

    @ManyToOne
    CatCie9 catCie9;

    public HCTratamiento getTratamiento() {
        return HCTratamiento;
    }

    public void setTratamiento(HCTratamiento HCTratamiento) {
        this.HCTratamiento = HCTratamiento;
    }

    public CatCie9 getCatCie9() {
        return catCie9;
    }

    public void setCatCie9(CatCie9 catCie9) {
        this.catCie9 = catCie9;
    }
}
