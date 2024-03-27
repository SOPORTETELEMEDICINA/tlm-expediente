package net.amentum.niomedic.expediente.reportClass;

public class CursoClinicoTabla {
    private String id;
    private String tipo;
    private String fecha;
    private String consulta;
    private String doctor;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getConsulta() {
        return consulta;
    }

    public void setConsulta(String consulta) {
        this.consulta = consulta;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    @Override
    public String toString() {
        return "CursoClinicoTabla{" +
                "id=" + id +
                ", tipo='" + tipo + '\'' +
                ", fecha='" + fecha + '\'' +
                ", consulta='" + consulta + '\'' +
                ", doctor='" + doctor + '\'' +
                '}';
    }
}
