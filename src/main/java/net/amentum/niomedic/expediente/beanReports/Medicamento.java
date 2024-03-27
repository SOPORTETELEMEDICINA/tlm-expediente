package net.amentum.niomedic.expediente.beanReports;

public class Medicamento {
    String nombreComercial;
    String detalles;
    String dosis;
    String unidad;
    String via;
    String frecuencia;
    String periodo;
    String recomendaciones;

    public Medicamento() {
        this.nombreComercial = "";
        this.detalles = "";
        this.dosis = "";
        this.unidad = "";
        this.via = "";
        this.frecuencia = "";
        this.periodo = "";
        this.recomendaciones = "";
    }

    public String getNombreComercial() {
        return nombreComercial;
    }

    public void setNombreComercial(String nombreComercial) {
        this.nombreComercial = nombreComercial;
    }

    public String getDetalles() {
        return detalles;
    }

    public void setDetalles(String detalles) {
        this.detalles = detalles;
    }

    public String getDosis() {
        return dosis;
    }

    public void setDosis(String dosis) {
        this.dosis = dosis;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public String getVia() {
        return via;
    }

    public void setVia(String via) {
        this.via = via;
    }

    public String getFrecuencia() {
        return frecuencia;
    }

    public void setFrecuencia(String frecuencia) {
        this.frecuencia = frecuencia;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public String getRecomendaciones() {
        return recomendaciones;
    }

    public void setRecomendaciones(String recomendaciones) {
        this.recomendaciones = recomendaciones;
    }

    @Override
    public String toString() {
        return "Medicamento{" +
                "nombreComercial='" + nombreComercial + '\'' +
                ", detalles='" + detalles + '\'' +
                ", dosis='" + dosis + '\'' +
                ", unidad='" + unidad + '\'' +
                ", via='" + via + '\'' +
                ", frecuencia='" + frecuencia + '\'' +
                ", periodo='" + periodo + '\'' +
                ", recomendaciones='" + recomendaciones + '\'' +
                '}';
    }
}
