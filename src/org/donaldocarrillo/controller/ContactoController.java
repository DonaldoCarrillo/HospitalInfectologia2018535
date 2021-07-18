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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.donaldocarrillo.bean.ContactoUrgencia;
import org.donaldocarrillo.db.Conexion;
import org.donaldocarrillo.sistema.Principal;

public class ContactoController implements Initializable{
     private enum operaciones {NUEVO,AGREGAR,GUARDAR,ELIMINAR,EDITAR,ACTUALIZAR,CANCELAR,NINGUNO};
     private Principal escenarioPrincipal;
     private operaciones tipoDeOperacion = operaciones.NINGUNO;
     private ObservableList <ContactoUrgencia> listaContactoUrgencia;
    @FXML private ComboBox cmbCodPaciente; 
    @FXML private ComboBox cmbCodContactoUrgencia;
    @FXML private TextField txtNumeroContactos;
    @FXML private TextField txtNombre;
    @FXML private TextField txtApellidos; 
    @FXML private TableView tblContactoUrgencia; 
    @FXML private TableColumn colNumeroContacto;
    @FXML private TableColumn colNombre; 
    @FXML private TableColumn colCodPaciente; 
    @FXML private TableColumn colCodContacto; 
    @FXML private TableColumn colApellido;
    @FXML private Button btnAgregar;
    @FXML private Button btnEditar; 
    @FXML private Button btnEliminar; 
    @FXML private Button btnReportes;    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    }

        public void cargarDatos(){
    tblContactoUrgencia.setItems(getContactoUrgencia());
    colCodContacto.setCellValueFactory(new PropertyValueFactory <ContactoUrgencia, Integer> ("codigoContacto"));
    colNombre.setCellValueFactory(new PropertyValueFactory <ContactoUrgencia, String> ("nombres"));
    colApellido.setCellValueFactory(new PropertyValueFactory <ContactoUrgencia, String> ("apellidos"));
    colNumeroContacto.setCellValueFactory(new PropertyValueFactory <ContactoUrgencia, String> ("numeroContacto"));
    colCodPaciente.setCellValueFactory(new PropertyValueFactory <ContactoUrgencia, Integer> ("codigoPaciente"));
    }
    
    public ObservableList <ContactoUrgencia> getContactoUrgencia(){
        ArrayList <ContactoUrgencia> lista = new ArrayList <ContactoUrgencia>();
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarContactoUrgencia()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()){
                lista.add(new ContactoUrgencia(resultado.getInt("contactoUrgencia"),
                        resultado.getString("nombres"),
                        resultado.getString("apellidos"),
                        resultado.getString("numeroContacto"),
                        resultado.getInt("codigoPaciente")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaContactoUrgencia = FXCollections.observableList(lista);
    }
    
    public void seleccionarElementos(){
        txtNumeroContactos.setText(((ContactoUrgencia)tblContactoUrgencia.getSelectionModel().getSelectedItem()).getNumeroContacto());
        txtNombre.setText(((ContactoUrgencia)tblContactoUrgencia.getSelectionModel().getSelectedItem()).getNombres());
        txtApellidos.setText(((ContactoUrgencia)tblContactoUrgencia.getSelectionModel().getSelectedItem()).getApellidos());
    }
    
    public ContactoUrgencia buscarContactoUrgencia(int codigoContactoUrgencia){
        ContactoUrgencia resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarContactoUrgencia(?)}");
            procedimiento.setInt(1, codigoContactoUrgencia);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new ContactoUrgencia (registro.getInt("codigoContactoUrgencia"),
                        registro.getString("nombres"),
                        registro.getString("apellidos"),
                        registro.getString("numeroContacto"),
                        registro.getInt("codigoPaciente"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado; 
    }
    
    public void editarContactos(){
        switch(tipoDeOperacion){
            case NINGUNO: 
                if(tblContactoUrgencia.getSelectionModel().getSelectedItem() != null){
                    btnEditar.setText("Actualizar");
                    btnReportes.setText("Cancelar");
                    btnAgregar.setDisable(true);
                    btnEliminar.setDisable(true);
                    activarControles();
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                }else{ 
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                }
                break; 
            case ACTUALIZAR: 
            actualizar();
            btnEditar.setText("Editar");
            btnReportes.setText("Reporte");
            btnAgregar.setDisable(false);
            btnEliminar.setDisable(false);
            tipoDeOperacion = operaciones.NINGUNO;
            cargarDatos();
            break;
        }
    }
    
    public void actualizar(){
        try{ 
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EditarContactoUrgencia(?,?,?,?)}");
            ContactoUrgencia registro =(((ContactoUrgencia)tblContactoUrgencia.getSelectionModel().getSelectedItem()));
            registro.setCodigoContactoUrgencia(((ContactoUrgencia)tblContactoUrgencia.getSelectionModel().getSelectedItem()).getCodigoContactoUrgencia());
            registro.setNombres(txtNombre.getText());
            registro.setApellidos(txtApellidos.getText());
            registro.setNumeroContacto(txtNumeroContactos.getText());
            //registro.setCodigoPaciente(((ContactoUrgencia)tblContactoUrgencia.getSelectionModel().getSelectedItem()).getCodigoPaciente());
            procedimiento.setInt(1, registro.getCodigoContactoUrgencia());
            procedimiento.setString(2, registro.getNombres());
            procedimiento.setString(3, registro.getApellidos());
            procedimiento.setString(4, registro.getNumeroContacto());
           // procedimiento.setInt(5, registro.getCodigoPaciente());
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void eliminarContacto(){
        switch(tipoDeOperacion){
            case GUARDAR:
                desactivarControles();
                limpiarControles();
                btnAgregar.setText("Agregar");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReportes.setDisable(false);
                tipoDeOperacion = operaciones.NUEVO;
                break; 
            default:
                if (tblContactoUrgencia.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar el registro?", "Eliminar Contacto Urgencia", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_OPTION)
                        try {
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarContactoUrgencia(?)}");
                            procedimiento.setInt(1, ((ContactoUrgencia)tblContactoUrgencia.getSelectionModel().getSelectedItem()).getCodigoContactoUrgencia());
                            procedimiento.execute();
                            listaContactoUrgencia.remove(tblContactoUrgencia.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                }else {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                }
        }
    }
    
    public void nuevo(){
        switch(tipoDeOperacion){
            case NINGUNO:
                activarControles();
                btnAgregar.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReportes.setDisable(true);
                tipoDeOperacion = operaciones.GUARDAR;
            break;
            case GUARDAR:
                guardar();
                desactivarControles();
                limpiarControles();
                btnAgregar.setText("Agregar");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReportes.setDisable(false);
                tipoDeOperacion = ContactoController.operaciones.NINGUNO;
                cargarDatos();
            break;
        }
        
    }
    
    public void guardar (){
        ContactoUrgencia registro =new ContactoUrgencia();
        registro.setNombres(txtNombre.getText());
        registro.setApellidos(txtApellidos.getText());
        registro.setNumeroContacto(txtNumeroContactos.getText());
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarContactoUrgencia(?,?,?)}");
            procedimiento.setString(1, registro.getNombres());
            procedimiento.setString(2, registro.getApellidos());
            procedimiento.setString(3, registro.getNumeroContacto());
            procedimiento.execute();
            listaContactoUrgencia.add(registro);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void desactivarControles(){
        txtNombre.setEditable(false);
        txtApellidos.setEditable(false);
        txtNumeroContactos.setEditable(false);
    }
    public void activarControles(){
        txtNombre.setEditable(true);
        txtApellidos.setEditable(true);
        txtNumeroContactos.setEditable(true);
    }
    public void limpiarControles(){
        txtNombre.setText("");
        txtApellidos.setText("");
        txtNumeroContactos.setText("");
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
    
}
