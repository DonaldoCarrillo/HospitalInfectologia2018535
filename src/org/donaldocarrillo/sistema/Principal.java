package org.donaldocarrillo.sistema;

import java.io.InputStream;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.donaldocarrillo.controller.AreaController;
import org.donaldocarrillo.controller.CargoController;
import org.donaldocarrillo.controller.ContactoController;
import org.donaldocarrillo.controller.ControlCitasController;
import org.donaldocarrillo.controller.EspecialidadesController;
import org.donaldocarrillo.controller.HorariosController;
import org.donaldocarrillo.controller.LoginController;
import org.donaldocarrillo.controller.MedicamentosController;
import org.donaldocarrillo.controller.MedicoController;
import org.donaldocarrillo.controller.MedicoEspecialidadController;
import org.donaldocarrillo.controller.MenuPrincipalController;
import org.donaldocarrillo.controller.PacientesController;
import org.donaldocarrillo.controller.ProgramadorController;
import org.donaldocarrillo.controller.RecetasController;
import org.donaldocarrillo.controller.ResponsableTurnoController;
import org.donaldocarrillo.controller.TelefonoController;
import org.donaldocarrillo.controller.TurnoController;
import org.donaldocarrillo.controller.UsuarioController;

public class Principal extends Application {
    private final String PAQUETE_VISTA = "/org/donaldocarrillo/view/";
    private Stage escenarioPrincipal;
    private Scene escena;
    @Override
    public void start(Stage escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
        escenarioPrincipal.setTitle("Hospital de Infectologia");
        ventanaLogin();
        escenarioPrincipal.show();
    }
    
    
    public void menuPrincipal(){
        try{
            MenuPrincipalController menuPrincipal = 
                    (MenuPrincipalController)
                    cambiarEscena("menuPrincipalView.fxml",600,400);
            menuPrincipal.setEscenarioPrincipal(this);
        }catch (Exception e){
            e.printStackTrace(); 
        }  
    }
    
    public void ventanaLogin(){
        try{
            LoginController loginController =
                    (LoginController)
                    cambiarEscena("loginView.fxml", 600,282);
            loginController.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void ventanaRecetas(){
        try{
            RecetasController recetasController =
                    (RecetasController)
                    cambiarEscena("recetasView.fxml", 600, 400);
            recetasController.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaUsuario(){
        try{
            UsuarioController usuarioController =
                    (UsuarioController)
                    cambiarEscena("UsuarioView.fxml", 627, 401);
            usuarioController.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
            
        public void ventanaControlCitas(){
            try{
                ControlCitasController controlCitasController =
                        (ControlCitasController)
                        cambiarEscena("controlCitasView.fxml", 568, 400);
                controlCitasController.setEscenarioPrincipal(this);
            }catch(Exception e){
                e.printStackTrace();
            }
        }    
            
        
        public void VentanaProgramador(){
        try{
            ProgramadorController ProgramadorController = 
                    (ProgramadorController)
                    cambiarEscena("programadorView.fxml",600,400);
            ProgramadorController.setEscenarioPrincipal(this);
        }catch (Exception e){
            e.printStackTrace(); 
        }
    }
        
       public void ventanaMedicos(){
           try{
                MedicoController medicoController =
                    (MedicoController)
                    cambiarEscena("MedicoView.fxml", 726, 400); 
                medicoController.setEscenarioPrincipal(this);
           }catch(Exception e){
               e.printStackTrace();
           }      
       } 
       
       public void ventanaPacientes(){
           try {
               PacientesController pacientesController =
                    (PacientesController)
                    cambiarEscena ("PacientesView.fxml", 600, 400);
               pacientesController.setEscenarioPrincipal(this);
            }catch(Exception e){
               e.printStackTrace();
           }
       }
        
       public void ventanaTurnos(){
           try{
               TurnoController turnoController =
                    (TurnoController)
                    cambiarEscena ("TurnoView.fxml", 654, 400);
               turnoController.setEscenarioPrincipal(this);
           }catch (Exception e){
               e.printStackTrace();
           }
       }
       
       public void ventanaAreas(){
          try{
           AreaController areaController =
                (AreaController)
                cambiarEscena ("AreaView.fxml", 599, 312);
           areaController.setEscenarioPrincipal(this);
           }catch (Exception e){
                e.printStackTrace();
           }
          
       }
       
       public void ventanaCargos(){
           try{
            CargoController cargoController =
                (CargoController)
                cambiarEscena("CargoView.fxml", 600, 303);
            cargoController.setEscenarioPrincipal(this);
           }catch (Exception e){
               e.printStackTrace();
           }
       }
       
       
       public void ventanaContactos(){
           try{
               ContactoController contactoController =
                       (ContactoController)
                       cambiarEscena("ContactoView.fxml", 605, 400);
               contactoController.setEscenarioPrincipal(this);
           }catch (Exception e){
               e.printStackTrace();
           }  
       }
       
       public void ventanaTelefonos(){
           try{
               TelefonoController telefonoController =
                       (TelefonoController)
                       cambiarEscena("TelefonoView.fxml", 600, 400);
                telefonoController.setEscenarioPrincipal(this);
           }catch (Exception e){
               e.printStackTrace();
           }    
       }
       
       public void ventanaEspecialidades(){
          try{
              EspecialidadesController especialidadesController =
                      (EspecialidadesController)
                      cambiarEscena("EspecialidadesView.fxml", 443, 314);
              especialidadesController.setEscenarioPrincipal(this);
          }catch(Exception e){
              e.printStackTrace();
          } 
       }
       
       public void ventanaHorarios(){
           try{
               HorariosController horariosController =
                      (HorariosController)
                       cambiarEscena("HorariosView.fxml", 600, 406);
               horariosController.setEscenarioPrincipal(this);
           }catch(Exception e){
               e.printStackTrace();
           }
       }
       
       public void ventanaMedicoEspecialidad(){
           try{
               MedicoEspecialidadController medicoEspecialidadController =
                       (MedicoEspecialidadController)
                       cambiarEscena("MedicoEspecialidadView.fxml", 600, 342);
               medicoEspecialidadController.setEscenarioPrincipal(this);
           }catch(Exception e){
               e.printStackTrace();
           }
       }
       
       public void ventanaResponsableTurno(){
           try{
               ResponsableTurnoController responsableTurnoController =
                       (ResponsableTurnoController)
                       cambiarEscena("ResponsableView.fxml", 557, 386);
               responsableTurnoController.setEscenarioPrincipal(this);
           }catch(Exception e){
               e.printStackTrace();
           }
       }
       
       public void ventanaMedicamentos(){
           try{
               MedicamentosController medicamentosController =
                       (MedicamentosController)
                       cambiarEscena("medicamentosView.fxml", 755, 608);
               medicamentosController.setEscenarioPrincipal(this);
           }catch(Exception e){
               e.printStackTrace();
           }
       }
       
    public Initializable cambiarEscena(String fxml, int ancho, int alto) throws Exception{
        Initializable resultado = null;            
        FXMLLoader cargadorFXML = new FXMLLoader();
        InputStream archivo = Principal.class.getResourceAsStream(PAQUETE_VISTA+fxml);
        cargadorFXML.setBuilderFactory(new JavaFXBuilderFactory());
        cargadorFXML.setLocation(Principal.class.getResource(PAQUETE_VISTA+fxml));
        escena = new Scene((AnchorPane)cargadorFXML.load(archivo),ancho,alto);
        escenarioPrincipal.setScene(escena);
        escenarioPrincipal.sizeToScene();
        resultado = (Initializable)cargadorFXML.getController();
                
                return resultado;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }   
}