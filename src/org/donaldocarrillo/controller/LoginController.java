package org.donaldocarrillo.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javax.swing.JOptionPane;
import org.donaldocarrillo.bean.Login;
import org.donaldocarrillo.bean.TipoUsuario;
import org.donaldocarrillo.db.Conexion;
import org.donaldocarrillo.report.GenerarReporte;
import org.donaldocarrillo.sistema.Principal;

public class LoginController implements Initializable {
private Principal escenarioPrincipal;
private ObservableList<TipoUsuario>listaUsuario;
private ObservableList<Login>listaLogin;
@FXML private ComboBox cmbTipoUsuario;
@FXML private TextField txtUsuario;
@FXML private PasswordField passFPassword;
@FXML private Button btnLogin;
@FXML private Button btnRegister;
@FXML private Button btnCancel;
@FXML private Button btnReporte;
        
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cmbTipoUsuario.setItems(getTipoUsuario());
    }
    
    public ObservableList<TipoUsuario>getTipoUsuario(){
        ArrayList<TipoUsuario> lista = new ArrayList<TipoUsuario>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_ListartipoDeUsuarios");
            ResultSet resultado =  procedimiento.executeQuery();
        while(resultado.next()){
            lista.add(new TipoUsuario(resultado.getInt("codigoTipoUsuario"),
                    resultado.getString("nombre"),
                    resultado.getString("descripcion")));
        }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaUsuario = FXCollections.observableList(lista);
    }
    
    
    public TipoUsuario BuscarTipoDeUsuario(int codigoUsuario){
      TipoUsuario resultado = null;
      try{
          PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EditarTipoDeUsuario(?)}");
          ResultSet registro = procedimiento.executeQuery();
          while(registro.next()){
              resultado = new TipoUsuario(registro.getInt("codigoTipoUsuario"),
                      registro.getString("nombre"),
                      registro.getString("descripcion"));
          }
      }catch (Exception e){
          e.printStackTrace();
      }
        return resultado;
     }
    
    public void registro(){
        String usuarioLogin= txtUsuario.getText();
        String usuarioContrasena = passFPassword.getText();
        String tipo = (String.valueOf(cmbTipoUsuario.getSelectionModel().getSelectedItem()));
        if(usuarioLogin.equals("Donaldo")&&usuarioContrasena.equals("9aMjUqkn")){//&&tipo.equals("1 | Administrador")){
            JOptionPane.showMessageDialog(null, "Bienvenido");
        this.setEscenarioPrincipal(escenarioPrincipal);
        menuPrincipal();
        }else{
            if(usuarioLogin.equals("Sebastian")&&usuarioContrasena.equals("admin")){//&& tipo.equals("2 / root")){
                JOptionPane.showMessageDialog(null, "Bienvenido");
                this.setEscenarioPrincipal(escenarioPrincipal);
                 menuPrincipal();
        }else{
            if(tipo.equals("3/Invitado")){
                JOptionPane.showMessageDialog(null, "No tiene permisos para ingresar ");
                 ventanaUsuario();
        }else{ 
            if(usuarioLogin.equals("")&&usuarioContrasena.equals("")){
            JOptionPane.showMessageDialog(null, "Debe registrarse como nuevo usuario");
             ventanaUsuario();   
            }
          }
        }
    }
    }   
    
    public void activarControles(){
        txtUsuario.setEditable(false);
    }
    
    public void desactivarControles(){
        txtUsuario.setDisable(true);
    }
    
    public void limpiarControles(){
        txtUsuario.setText("");
    }
    
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
    
    public void ventanaLogin(){
        escenarioPrincipal.ventanaLogin();
    }
    
    public void ventanaUsuario(){
        escenarioPrincipal.ventanaUsuario();
    }

    public void reporteUsuarios(){
        Map parametros = new HashMap();
        GenerarReporte.mostrarReporte("Usuarios.jasper", "Reporte Usuarios", parametros);
    }
}