package org.donaldocarrillo.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.donaldocarrillo.bean.Login;
import org.donaldocarrillo.db.Conexion;
import org.donaldocarrillo.sistema.Principal;

public class UsuarioController implements Initializable{

private enum operaciones{AGREGAR, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO} 
    private Principal escenarioPrincipal;
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Login> listaLogin;
@FXML private TextField txtUsuarioLogin;
@FXML private TextField txtContrasena;
@FXML private TextField txtEstado;
@FXML private TextField txtUsuarioFecha;
@FXML private TextField txtUsuarioHora;
@FXML private TableView tblUsuarios;
@FXML private TableColumn colCodUsuario;
@FXML private TableColumn colUsuario;
@FXML private TableColumn colContrasena;
@FXML private TableColumn colUsuarioHora;
@FXML private TableColumn colUsuarioFecha;
@FXML private TableColumn colEstado;
@FXML private Button btnAgregar;
@FXML private Button btnEliminar;
@FXML private Button btnEditar;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        } 
    
    public void cargarDatos(){
        tblUsuarios.setItems(getLogin());
        colCodUsuario.setCellValueFactory(new PropertyValueFactory<Login, Integer>("codigoUsuario"));
        colUsuario.setCellValueFactory(new PropertyValueFactory<Login, String>("usuarioLogin"));
        colContrasena.setCellValueFactory(new PropertyValueFactory<Login, String>("usuarioContrasena"));
        colEstado.setCellValueFactory(new PropertyValueFactory<Login, String>("usuarioEstado"));
        colUsuarioFecha.setCellValueFactory(new PropertyValueFactory<Login, String>("usuarioFecha"));
        colUsuarioHora.setCellValueFactory(new PropertyValueFactory<Login, String>("usuarioHora"));
    }
    
    public ObservableList<Login>getLogin(){
        ArrayList<Login> lista = new ArrayList<Login>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarUsuarios}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()){
                lista.add(new Login (resultado.getInt("codigoUsuario"),
                        resultado.getString("usuarioLogin"),
                        resultado.getString("usuarioContrasena"),
                        resultado.getString("usuarioEstado"),
                        resultado.getString("usuarioFecha"),
                        resultado.getString("usuarioHora")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaLogin = FXCollections.observableList(lista);
    }

    public void seleccionarElementos(){
        txtUsuarioLogin.setText(((Login)tblUsuarios.getSelectionModel().getSelectedItem()).getUsuarioLogin());
        txtContrasena.setText(((Login)tblUsuarios.getSelectionModel().getSelectedItem()).getUsuarioContrasena());
        txtEstado.setText(((Login)tblUsuarios.getSelectionModel().getSelectedItem()).getUsuarioEstado());
        txtUsuarioFecha.setText(((Login)tblUsuarios.getSelectionModel().getSelectedItem()).getUsuarioFecha());
        txtUsuarioHora.setText(((Login)tblUsuarios.getSelectionModel().getSelectedItem()).getUsuarioHora());
    }

    public Login buscarUsuario(int codigoUsuario){
        Login resultado = null;
      try{
          PreparedStatement procedimiento =  Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarUsurios(?)}");
          procedimiento.setInt(1, codigoUsuario);
          ResultSet registro = procedimiento.executeQuery();
          while (registro.next()){
           resultado = new Login(registro.getInt("codigoUsuario"),
                   registro.getString("usuarioLogin"),
                   registro.getString("usuarioContrasena"),
                   registro.getString("usuarioEstado"),
                   registro.getString("usuarioFecha"),
                   registro.getString("usuarioHora"));
          }
      }catch(Exception e){
          e.printStackTrace();
        }
      return resultado;
    } 
    
    public void editarUsuario(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if(tblUsuarios.getSelectionModel().getSelectedItem() !=null){
                    btnEditar.setText("Actualizar");
                    btnEliminar.setText("Cancelar");
                    btnAgregar.setDisable(true);
                    activarControles();
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                }else {
                    JOptionPane.showMessageDialog(null, "Seleccione un elemento.");
                }
                break;
            case ACTUALIZAR:
                actualizar();
                btnEditar.setText("Editar");
                btnEliminar.setText("Eliminar");
                btnAgregar.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
                break;
        }
    }
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EditarUsuario(?,?,?,?,?,?)}");
            Login registro = (Login)tblUsuarios.getSelectionModel().getSelectedItem();
            registro.setUsuarioLogin(txtUsuarioLogin.getText());
            registro.setUsuarioContrasena(txtContrasena.getText());
            registro.setUsuarioEstado(txtEstado.getText());
            registro.setUsuarioFecha(txtUsuarioFecha.getText());
            registro.setUsuarioHora(txtUsuarioHora.getText());
            procedimiento.setInt(1, registro.getCodigoUsuario());
            procedimiento.setString(2, registro.getUsuarioLogin());
            procedimiento.setString(3, registro.getUsuarioContrasena());
            procedimiento.setString(4, registro.getUsuarioEstado());
            procedimiento.setString(5, registro.getUsuarioFecha());
            procedimiento.setString(6, registro.getUsuarioHora());
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void eliminarUsuario(){
        switch(tipoDeOperacion){
            case GUARDAR:
                desactivarControles();
                limpiarControles();
                btnAgregar.setText("Agregar");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                tipoDeOperacion = operaciones.AGREGAR;
                break;
            default:
                if(tblUsuarios.getSelectionModel().getSelectedItem() !=null){
                  int respuesta = JOptionPane.showConfirmDialog(null, "¿Seguto  de eliminar el registro?", "Eliminar Usuario", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                  if (respuesta == JOptionPane.YES_OPTION){
                   try{
                       PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarUsuarios(?)}");
                       procedimiento.setInt(1, ((Login)tblUsuarios.getSelectionModel().getSelectedItem()).getCodigoUsuario());
                       procedimiento.execute();
                       listaLogin.remove(tblUsuarios.getSelectionModel().getSelectedIndex());
                       limpiarControles();
                   }catch(Exception e){
                       e.printStackTrace();
                   }   
                  }
                }else{
                    JOptionPane.showMessageDialog(null, "Debe Seleccionar un usario");
                }
        }    
    }
    
    public void nuevo(){
        switch (tipoDeOperacion){
            case NINGUNO:
                activarControles();
                limpiarControles();
                btnAgregar.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                tipoDeOperacion = operaciones.GUARDAR;
                break;
            case GUARDAR:
                guardar();
                limpiarControles();
                desactivarControles();
                btnAgregar.setText("Agregar");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
                break;
        }
    }
    
    public void guardar(){
        Login registro = new Login();
        registro.setUsuarioLogin(txtUsuarioLogin.getText());
        registro.setUsuarioContrasena(txtContrasena.getText());
        registro.setUsuarioEstado(txtEstado.getText());
        registro.setUsuarioFecha(txtUsuarioFecha.getText());
        registro.setUsuarioHora(txtUsuarioHora.getText());
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarUsuario(?,?,?,?,?)}");
            procedimiento.setString(1, registro.getUsuarioLogin());
            procedimiento.setString(2, registro.getUsuarioContrasena());
            procedimiento.setString(3, registro.getUsuarioEstado());
            procedimiento.setString(4, registro.getUsuarioFecha());
            procedimiento.setString(5, registro.getUsuarioHora());
            listaLogin.add(registro);
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void activarControles(){
        txtUsuarioLogin.setEditable(true);
        txtContrasena.setEditable(true);
        txtEstado.setEditable(true);
        txtUsuarioFecha.setEditable(true);
        txtUsuarioHora.setEditable(true);
    }
    
    public void desactivarControles(){
        txtUsuarioLogin.setEditable(false);
        txtContrasena.setEditable(false);
        txtEstado.setEditable(false);
        txtUsuarioFecha.setEditable(false);
        txtUsuarioHora.setEditable(false);       
    }
    
    public void limpiarControles(){
        txtUsuarioLogin.setText("");
        txtContrasena.setText("");
        txtEstado.setText("");
        txtUsuarioFecha.setText("");
        txtUsuarioHora.setText("");        
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
    
    public void ventanaUsuario(){
        escenarioPrincipal.ventanaUsuario();
    }
    
    public void ventanaLogin(){
        escenarioPrincipal.ventanaLogin();
    }
}    