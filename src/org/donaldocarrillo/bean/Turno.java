package org.donaldocarrillo.bean;

public class Turno {

private int codigoTurno;
private String fechaTurno;
private String fechaCita;
private String valorCita;
private int codigoMedicoEspecialidad;
private int codigoTurnoResponsable;
private int codigoPaciente;

    public Turno() {
    }

    public Turno(int codigoTurno, String fechaTurno, String fechaCita, String valorCita, int codigoMedicoEspecialidad, int codigoTurnoResponsable, int codigoPaciente) {
        this.codigoTurno = codigoTurno;
        this.fechaTurno = fechaTurno;
        this.fechaCita = fechaCita;
        this.valorCita = valorCita;
        this.codigoMedicoEspecialidad = codigoMedicoEspecialidad;
        this.codigoTurnoResponsable = codigoTurnoResponsable;
        this.codigoPaciente = codigoPaciente;
    }

    public int getCodigoTurno() {
        return codigoTurno;
    }

    public void setCodigoTurno(int codigoTurno) {
        this.codigoTurno = codigoTurno;
    }

    public String getFechaTurno() {
        return fechaTurno;
    }

    public void setFechaTurno(String fechaTurno) {
        this.fechaTurno = fechaTurno;
    }

    public String getFechaCita() {
        return fechaCita;
    }

    public void setFechaCita(String fechaCita) {
        this.fechaCita = fechaCita;
    }

    public String getValorCita() {
        return valorCita;
    }

    public void setValorCita(String valorCita) {
        this.valorCita = valorCita;
    }

    public int getCodigoMedicoEspecialidad() {
        return codigoMedicoEspecialidad;
    }

    public void setCodigoMedicoEspecialidad(int codigoMedicoEspecialidad) {
        this.codigoMedicoEspecialidad = codigoMedicoEspecialidad;
    }

    public int getCodigoTurnoResponsable() {
        return codigoTurnoResponsable;
    }

    public void setCodigoTurnoResponsable(int codigoTurnoResponsable) {
        this.codigoTurnoResponsable = codigoTurnoResponsable;
    }

    public int getCodigoPaciente() {
        return codigoPaciente;
    }

    public void setCodigoPaciente(int codigoPaciente) {
        this.codigoPaciente = codigoPaciente;
    }


}
