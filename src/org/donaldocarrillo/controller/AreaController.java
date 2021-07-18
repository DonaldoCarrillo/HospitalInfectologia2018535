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
import org.donaldocarrillo.bean.Area;
import org.donaldocarrillo.db.Conexion;
import org.donaldocarrillo.sistema.Principal;

public class AreaController implements Initializable {
    private enum operaciones{AGREGAR, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private Principal escenarioPrincipal;
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Area> listaArea;
    @FXML TextField txtNombreArea;
   // @FXML TextField txtCodigoArea;
    @FXML TableColumn colCodArea;
    @FXML TableColumn colNombreArea;
    @FXML TableView tblArea; 
    @FXML Button btnAgregar;
    @FXML Button btnEliminar;
    @FXML Button btnEditar;
    @FXML Button btnReportes;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    }

    public void cargarDatos(){
        tblArea.setItems(getArea());
        colCodArea.setCellValueFactory(new PropertyValueFactory<Area, Integer>("codigoArea"));
        colNombreArea.setCellValueFactory(new PropertyValueFactory<Area, String>("nombreArea"));
    }
    
    public ObservableList<Area>getArea(){
        ArrayList<Area> lista = new ArrayList<Area>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarAreas()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()){
                lista.add(new Area (resultado.getInt("codigoArea"),
                        resultado.getString("nombreArea")));   
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaArea = FXCollections.observableList(lista);
    } 
            
    public void seleccionarElemento(){
     //   txtCodigoArea.setText(String.valueOf(((Area)tblArea.getSelectionModel().getSelectedItem()).getCodigoArea()));
        txtNombreArea.setText(((Area)tblArea.getSelectionModel().getSelectedItem()).getNombreArea());
    }
    
    public Area buscarArea(int codigoArea){
        Area resultado = null;
     try{
         PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarArea(?)}");
         procedimiento.setInt(1, codigoArea);
         ResultSet registro = procedimiento.executeQuery();
         while (registro.next()){
          resultado = new Area(registro.getInt("codigoArea"),
          registro.getString("nombreArea"));   
         }
        }catch(Exception e){
            e.printStackTrace();
        }
            return resultado;
    }
    
    public void editarArea(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if(tblArea.getSelectionModel().getSelectedItem() !=null){
                    btnEditar.setText("Actualizar");
                    btnReportes.setText("Cancelar");
                    btnAgregar.setDisable(true);
                    btnEliminar.setDisable(true);
                    activarControles();
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                }else {
                    JOptionPane.showMessageDialog(null, "Seleccionar un elemento.");
                }
                break;
            case ACTUALIZAR:
                 actualizar();
                btnEditar.setText("Editar");
                btnReportes.setText("Reportes");
                btnAgregar.setDisable(false);
                btnEliminar.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
                break;
        }
    }
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EditarAreas(?,?)}");
            Area registro = (Area)tblArea.getSelectionModel().getSelectedItem();
//            registro.setCodigoArea(Integer.parseInt(txtCodigoArea.getText()));
            registro.setNombreArea(txtNombreArea.getText());
            procedimiento.setInt(1, registro.getCodigoArea());
            procedimiento.setString(2, registro.getNombreArea());
            procedimiento.execute();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    
    public void eliminarAreas(){
        switch (tipoDeOperacion){
            case GUARDAR:
                desactivarControles();
                limpiarControles();
                btnAgregar.setText("Agregar");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReportes.setDisable(false);
                tipoDeOperacion = operaciones.AGREGAR;
                break;
            default:
                if(tblArea.getSelectionModel().getSelectedItem() !=null){
                 int respuesta = JOptionPane.showConfirmDialog(null, "¿Esta seguro de eliminar el registro?", "Eliminar Areas", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                 if (respuesta == JOptionPane.YES_OPTION){
                     try{
                         PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarAreas(?)}");
                         procedimiento.setInt(1, ((Area)tblArea.getSelectionModel().getSelectedItem()).getCodigoArea());
                         procedimiento.execute();
                         listaArea.remove(tblArea.getSelectionModel().getSelectedIndex());
                         limpiarControles();
                     }catch (Exception e){
                         e.printStackTrace();
                     }
                 }
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
           }
        }
    }
    
    public void nuevo(){
        switch (tipoDeOperacion){
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
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
                break;
        }
    }
    
    public void guardar(){
        Area registro = new Area();
//         registro.setCodigoArea(Integer.parseInt(txtCodigoArea.getText()));
         registro.setNombreArea(txtNombreArea.getText());
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarAreas(?)}");
            procedimiento.setString(1, registro.getNombreArea());
            listaArea.add(registro);
            procedimiento.execute();
        }catch (Exception e){
            e.printStackTrace();
            e.getStackTrace();
        }
    }
    
    public void desactivarControles(){
//        txtCodigoArea.setEditable(false);
        txtNombreArea.setEditable(false);
    }
    
    public void activarControles(){
//        txtCodigoArea.setEditable(true);
        txtNombreArea.setEditable(true);
    }
    
    public void limpiarControles(){
//        txtCodigoArea.setText("");
        txtNombreArea.setText("");
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
    