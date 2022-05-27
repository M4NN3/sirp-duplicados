package model;

public class Actos {
    private int codActo;
    private String acto;
    private int codLibro;
    private String libro;
    private int repeticiones;
    private int papelCantidad;
    private int inscripcionesCantidad;

    public int getInscripcionesCantidad() {
        return inscripcionesCantidad;
    }

    public void setInscripcionesCantidad(int inscripcionesCantidad) {
        this.inscripcionesCantidad = inscripcionesCantidad;
    }

    public int getPapelCantidad() {
        return papelCantidad;
    }

    public void setPapelCantidad(int papelCantidad) {
        this.papelCantidad = papelCantidad;
    }

    public int getCodActo() {
        return codActo;
    }

    public void setCodActo(int codActo) {
        this.codActo = codActo;
    }

    public String getActo() {
        return acto;
    }

    public void setActo(String acto) {
        this.acto = acto;
    }

    public int getCodLibro() {
        return codLibro;
    }

    public void setCodLibro(int codLibro) {
        this.codLibro = codLibro;
    }

    public String getLibro() {
        return libro;
    }

    public void setLibro(String libro) {
        this.libro = libro;
    }

    public int getRepeticiones() {
        return repeticiones;
    }

    public void setRepeticiones(int repeticiones) {
        this.repeticiones = repeticiones;
    }
}
