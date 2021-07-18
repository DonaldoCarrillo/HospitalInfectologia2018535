package org.donaldocarrillo.bean;

/**
 *
 * @author Sebastian Carrillo
 */
public class Medicamentos {
    private int idMedicamento;
    private String nombreMedicamento;
    private int cantidad; 
    private String presentacion;
    private String viaDeAdministracion;

    public Medicamentos() {
    }

    public Medicamentos(int idMedicamento, String nombreMedicamento, int cantidad, String presentacion, String viaDeAdministracion) {
        this.idMedicamento = idMedicamento;
        this.nombreMedicamento = nombreMedicamento;
        this.cantidad = cantidad;
        this.presentacion = presentacion;
        this.viaDeAdministracion = viaDeAdministracion;
    }

    public int getIdMedicamento() {
        return idMedicamento;
    }

    public void setIdMedicamento(int idMedicamento) {
        this.idMedicamento = idMedicamento;
    }

    public String getNombreMedicamento() {
        return nombreMedicamento;
    }

    public void setNombreMedicamento(String nombreMedicamento) {
        this.nombreMedicamento = nombreMedicamento;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getPresentacion() {
        return presentacion;
    }

    public void setPresentacion(String presentacion) {
        this.presentacion = presentacion;
    }

    public String getViaDeAdministracion() {
        return viaDeAdministracion;
    }

    public void setViaDeAdministracion(String viaDeAdministracion) {
        this.viaDeAdministracion = viaDeAdministracion;
    }
    
    
}
