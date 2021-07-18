package org.donaldocarrillo.bean;

public class Horarios {
    private Integer codigoHorario;
    private String horarioInicio;
    private String horarioSalida;
    private Integer lunes;
    private Integer martes;
    private Integer miercoles;
    private Integer jueves;
    private Integer viernes;

    public Horarios() {
    }

    public Horarios(Integer codigoHorario, String horarioInicio, String horarioSalida, Integer lunes, Integer martes, Integer miercoles, Integer jueves, Integer viernes) {
        this.codigoHorario = codigoHorario;
        this.horarioInicio = horarioInicio;
        this.horarioSalida = horarioSalida;
        this.lunes = lunes;
        this.martes = martes;
        this.miercoles = miercoles;
        this.jueves = jueves;
        this.viernes = viernes;
    }

    public Integer getCodigoHorario() {
        return codigoHorario;
    }

    public void setCodigoHorario(Integer codigoHorario) {
        this.codigoHorario = codigoHorario;
    }

    public String getHorarioInicio() {
        return horarioInicio;
    }

    public void setHorarioInicio(String horarioInicio) {
        this.horarioInicio = horarioInicio;
    }

    public String getHorarioSalida() {
        return horarioSalida;
    }

    public void setHorarioSalida(String horarioSalida) {
        this.horarioSalida = horarioSalida;
    }

    public Integer getLunes() {
        return lunes;
    }

    public void setLunes(Integer lunes) {
        this.lunes = lunes;
    }

    public Integer getMartes() {
        return martes;
    }

    public void setMartes(Integer martes) {
        this.martes = martes;
    }

    public Integer getMiercoles() {
        return miercoles;
    }

    public void setMiercoles(Integer miercoles) {
        this.miercoles = miercoles;
    }

    public Integer getJueves() {
        return jueves;
    }

    public void setJueves(Integer jueves) {
        this.jueves = jueves;
    }

    public Integer getViernes() {
        return viernes;
    }

    public void setViernes(Integer viernes) {
        this.viernes = viernes;
    }

    
   
}
