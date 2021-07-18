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
// import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.donaldocarrillo.bean.Cargos;
import org.donaldocarrillo.db.Conexion;
import org.donaldocarrillo.sistema.Principal;

public class CargoController implements Initializable{
 private enum operaciones{NUEVO,AGREGAR,GUARDAR,ELIMINAR,EDITAR,ACTUALIZAR,CANCELAR,NINGUNO};
 private Principal escenarioPrincipal; 
 private operaciones tipoDeOperacion = operaciones.NINGUNO; 
 private ObservableList<Cargos> listaCargo;
    //@FXML private ComboBox cmbCodCargo; 
    @FXML private TextField txtNombreCargos;
    @FXML private TableView tblCargos;
    @FXML private TableColumn colNombreCargos;
    @FXML private TableColumn colCodCargos; 
    @FXML private Button btnReportes; 
    @FXML private Button btnEliminar; 
    @FXML private Button btnEditar; 
    @FXML private Button btnAgregar;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    }
    
    public void cargarDatos(){
        tblCargos.setItems(getCargos());
        colCodCargos.setCellValueFactory(new PropertyValueFactory<Cargos, Integer>("codigoCargo"));
        colNombreCargos.setCellValueFactory(new PropertyValueFactory<Cargos, String>("nombreCargo"));
    }
    
    public ObservableList <Cargos> getCargos(){
        ArrayList <Cargos> lista = new ArrayList <Cargos> ();
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarCargos()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Cargos(resultado.getInt("codigoCargo"),
                        resultado.getString("nombreCargo")));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return listaCargo = FXCollections.observableList(lista);
    }
    
    public void seleccionarElementos(){
        txtNombreCargos.setText(((Cargos)tblCargos.getSelectionModel().getSelectedItem()).getNombreCargo());
    }
    
    public Cargos buscarCargo(int codigoCargo){
        Cargos resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarCargos(?)}");
            procedimiento.setInt(1, codigoCargo);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Cargos (registro.getInt("codigoCargo"),
                registro.getString("nombreCargo"));
            }        
        }catch (Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
    
    public void editarCargos(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if(tblCargos.getSelectionModel().getSelectedItem() !=null){
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
                actualizar ();
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
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EditarCargos(?,?)}");
            Cargos registro = (Cargos)tblCargos.getSelectionModel().getSelectedItem();
            registro.setCodigoCargo(((Cargos)tblCargos.getSelectionModel().getSelectedItem()).getCodigoCargo());
            registro.setNombreCargo(txtNombreCargos.getText());
            procedimiento.setInt(1, registro.getCodigoCargo());
            procedimiento.setString(2, registro.getNombreCargo());
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void eliminarCargos(){
        switch(tipoDeOperacion){
            case GUARDAR :
                desactivarControles();
                limpiarControles();
                btnAgregar.setText("Agregar");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReportes.setDisable(false);
                tipoDeOperacion = operaciones.NUEVO;
                break;
            default :
                if (tblCargos.getSelectionModel().getSelectedItem() != null ){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar el registro", "Eliminar cargo", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_OPTION)
                        try{
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarCargos(?)}");
                            procedimiento.setInt(1, ((Cargos)tblCargos.getSelectionModel().getSelectedItem()).getCodigoCargo());
                            procedimiento.execute();
                            listaCargo.remove(tblCargos.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                }else{
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
                tipoDeOperacion = CargoController.operaciones.NINGUNO;
                cargarDatos();
            break;
        }
    }
    
    public void guardar(){
        Cargos registro = new Cargos(); 
        registro.setNombreCargo(txtNombreCargos.getText());
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarCargos(?)}");
            procedimiento.setString(1, registro.getNombreCargo());
            procedimiento.execute();
            listaCargo.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void desactivarControles(){
        txtNombreCargos.setEditable(false);
    }
    public void activarControles(){
        txtNombreCargos.setEditable(true);
    }
    public void limpiarControles(){
        txtNombreCargos.setText("");
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
