package org.donaldocarrillo.bean;

public class Recetas {
    private int codigoReceta;
    private String descripcionReceta;
    private int codigoContolCita;

    public Recetas() {
    }

    public Recetas(int codigoReceta, String descripcionReceta, int codigoContolCita) {
        this.codigoReceta = codigoReceta;
        this.descripcionReceta = descripcionReceta;
        this.codigoContolCita = codigoContolCita;
    }

    public int getCodigoReceta() {
        return codigoReceta;
    }

    public void setCodigoReceta(int codigoReceta) {
        this.codigoReceta = codigoReceta;
    }

    public String getDescripcionReceta() {
        return descripcionReceta;
    }

    public void setDescripcionReceta(String descripcionReceta) {
        this.descripcionReceta = descripcionReceta;
    }

    public int getCodigoContolCita() {
        return codigoContolCita;
    }

    public void setCodigoContolCita(int codigoContolCita) {
        this.codigoContolCita = codigoContolCita;
    }
    
    
}
