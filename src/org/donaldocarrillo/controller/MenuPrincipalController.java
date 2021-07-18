package org.donaldocarrillo.controller;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import org.donaldocarrillo.report.GenerarReporte;
import org.donaldocarrillo.sistema.Principal;

public class MenuPrincipalController implements Initializable{
    private Principal EscenarioPrincipal;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public Principal getEscenarioPrincipal() {
        return EscenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal EscenarioPrincipal) {

        this.EscenarioPrincipal = EscenarioPrincipal;
    }
    public void VentanaProgramador(){
        EscenarioPrincipal.VentanaProgramador();
    }
   
     public void ventanaMedicos(){
         EscenarioPrincipal.ventanaMedicos();
     }
     
     public void ventanaPacientes(){
         EscenarioPrincipal.ventanaPacientes();
     }
     
     
     
     public void ventanaTurno(){
         EscenarioPrincipal.ventanaTurnos();
     }
     
     public void  ventanaAreas(){
         EscenarioPrincipal.ventanaAreas();
     }
     
     public void ventanaCargos(){
         EscenarioPrincipal.ventanaCargos();
     }
     
     public void ventanaContactos(){
         EscenarioPrincipal.ventanaContactos();
     }
     
     public void ventanaTelefonos(){
         EscenarioPrincipal.ventanaTelefonos();
     }
     
     public void ventanaEspecialidades(){
         EscenarioPrincipal.ventanaEspecialidades();
     }
     
     public void ventanaHorarios(){
         EscenarioPrincipal.ventanaHorarios(); 
     }
     
     public void ventanaMedicoEspecialidad(){
         EscenarioPrincipal.ventanaMedicoEspecialidad();
     }
     
     public void ventanaResponsableTurno(){
         EscenarioPrincipal.ventanaResponsableTurno();
     }
     
     public void reporteMedicos(){
         Map parametros = new HashMap();
         GenerarReporte.mostrarReporte("Medicos3.jasper", "Reporte de Medicos", parametros);
     }
     
     public void reportePaciente(){
         Map parametros = new HashMap();
         GenerarReporte.mostrarReporte("Pacientes.jasper", "Reporte Pacientes", parametros);
     }
     
     public void reporteAreas(){
         Map parametros = new HashMap();
         GenerarReporte.mostrarReporte("Areas.jasper", "Reporte Areas", parametros);
     }    
     
     public void reporteCargos(){
        Map parametros = new HashMap();
        GenerarReporte.mostrarReporte("Cargo.jasper", "Reporte Cargos", parametros);
    }
    
     public void reporteTurnos(){
        Map parametros = new HashMap();
        GenerarReporte.mostrarReporte("Turnos.jasper", "Reporte Turnos", parametros);
    }
    
     public void reporteContactoUrgencia(){
        Map parametros = new HashMap();
        GenerarReporte.mostrarReporte("ContactoUrgencia.jasper", "Reporte Contacto Urgencia", parametros);
    }
    
     public void reporteTelefonoMedico(){
        Map parametros = new HashMap();
        GenerarReporte.mostrarReporte("TelefonoMedico.jasper", "Reporte Telefono Medico", parametros);
    }
    
     public void reporteEspecialidades(){
       Map parametros = new HashMap();
       GenerarReporte.mostrarReporte("Especialidaes.jasper", "Reporte Especialidad", parametros);   
   }
   
     public void reporteHorarios(){
       Map parametros = new HashMap();
       GenerarReporte.mostrarReporte("Horarios.jasper", "Reporte Horarios", parametros);
   }
   
     public void reporteMedicoEspecialidad(){
       Map parametros = new HashMap();
       GenerarReporte.mostrarReporte("MedicoEspecialidad.jasper", "Reporte Medico Especialidad", parametros);
   }
   
     public void reporteResponsableTurno(){
       Map parametros = new HashMap();
       GenerarReporte.mostrarReporte("ResponsableTurno.jasper", "Reporte Contacto Urgencia", parametros);
   }
     
     public void ventanaLogin(){
         EscenarioPrincipal.ventanaLogin();
     }
     
     public void venanaUsuario(){
         EscenarioPrincipal.ventanaUsuario();
     }
     
     public void ventanaControlCita(){
         EscenarioPrincipal.ventanaControlCitas();
     }
}


