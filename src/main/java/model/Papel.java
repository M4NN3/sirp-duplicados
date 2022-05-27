package model;

public class Papel {
    private int repeticiones;
    private String papel;
    private String acto;
    private int codActo;
    private String libro;
    private int codLibro;
    private int codPapel;
    private int cantIntervinientes;

    public int getCantIntervinientes() {
        return cantIntervinientes;
    }

    public void setCantIntervinientes(int cantIntervinientes) {
        this.cantIntervinientes = cantIntervinientes;
    }

    public int getCodPapel() {
        return codPapel;
    }

    public void setCodPapel(int codPapel) {
        this.codPapel = codPapel;
    }

    public String getLibro() {
        return libro;
    }

    public void setLibro(String libro) {
        this.libro = libro;
    }

    public int getCodLibro() {
        return codLibro;
    }

    public void setCodLibro(int codLibro) {
        this.codLibro = codLibro;
    }

    public String getPapel() {
        return papel;
    }

    public void setPapel(String papel) {
        this.papel = papel;
    }

    public int getRepeticiones() {
        return repeticiones;
    }

    public void setRepeticiones(int repeticiones) {
        this.repeticiones = repeticiones;
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
}
