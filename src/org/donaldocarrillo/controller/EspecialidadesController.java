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
import org.donaldocarrillo.bean.Especialidades;
import org.donaldocarrillo.db.Conexion;
import org.donaldocarrillo.sistema.Principal;


public class EspecialidadesController implements Initializable{
    private enum operaciones{AGREGAR, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR,CANCELAR,NINGUNO};
    private Principal escenarioPrincipal;
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Especialidades> listaEspecialidades;
    @FXML private TextField txtNombreEspecialidad;
    @FXML private TableView tblEspecialidad;
    @FXML private TableColumn colCodEspecialidad;
    @FXML private TableColumn colNombreEspecialidad;
    @FXML private Button btnAgregar;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReportes;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    }
    
    public void cargarDatos(){
        tblEspecialidad.setItems(getEspecialidades());
        colCodEspecialidad.setCellValueFactory(new PropertyValueFactory<Especialidades, Integer>("codigoEspecialidad"));
        colNombreEspecialidad.setCellValueFactory(new PropertyValueFactory<Especialidades, String>("nombreEspecialidad"));
    }
    
    public ObservableList<Especialidades> getEspecialidades(){
        ArrayList<Especialidades> lista = new ArrayList<Especialidades>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarEspecialidades()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Especialidades(resultado.getInt("codigoEspecialidad"),
                                             resultado.getString("nombreEspecialidad")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaEspecialidades = FXCollections.observableList(lista);
    }
    
    public void seleccionarElementos(){
      txtNombreEspecialidad.setText(((Especialidades)tblEspecialidad.getSelectionModel().getSelectedItem()).getNombreEspecialidad());
    }
    
    public Especialidades buscarEspecialidades(int codigoEspecialidad){
        Especialidades resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarEspecialidades(?)}");
            procedimiento.setInt(1, codigoEspecialidad);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Especialidades(registro.getInt("codigoEspecialidad"),
                                        registro.getString("nombreEspecialidad"));                                       
                 }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
    
    public void editarEspecialidades(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if (tblEspecialidad.getSelectionModel().getSelectedItem() != null){
                    btnEditar.setText("Actualizar");
                    btnReportes.setText("Cancelar");
                    btnAgregar.setDisable(true);
                    btnEliminar.setDisable(true);
                    activarControles();
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento.");
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
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EditarEspecialidades(?,?)}");
            Especialidades registro = (Especialidades)tblEspecialidad.getSelectionModel().getSelectedItem();
            registro.setNombreEspecialidad(txtNombreEspecialidad.getText());
            procedimiento.setInt(1, registro.getCodigoEspecialidad());
            procedimiento.setString(2, registro.getNombreEspecialidad());
            procedimiento.execute();
          }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void eliminarEspecialidades(){
        switch(tipoDeOperacion){
            case GUARDAR:
                desactivarControles();
                limpiarControles();
                btnAgregar.setText("Agregar");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReportes.setDisable(false);
                tipoDeOperacion = operaciones.GUARDAR;
                break;
            default:
                if (tblEspecialidad.getSelectionModel().getSelectedItem() != null){
                     int respuesta = JOptionPane.showConfirmDialog(null, "¿Esta seguro de eliminar el registro?", "Eliminar Especialidad", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarEspecialidades(?)}");
                            procedimiento.setInt(1, ((Especialidades)tblEspecialidad.getSelectionModel().getSelectedItem()).getCodigoEspecialidad());
                            procedimiento.execute();
                            listaEspecialidades.remove(tblEspecialidad.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }              
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento.");
                }
            }
        }
    
    public void nuevo(){
        switch(tipoDeOperacion){
            case NINGUNO:
                activarControles();
                btnAgregar.setText("Guardar");
                btnEditar.setDisable(true);
                btnEliminar.setText("Cancelar");
                btnReportes.setDisable(true);
                tipoDeOperacion = operaciones.GUARDAR;
                break;
            case GUARDAR:
                guardar();
                desactivarControles();
                limpiarControles();
                btnAgregar.setText("Agregar");
                btnEditar.setDisable(false);
                btnEliminar.setText("Eliminar");
                btnReportes.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
                break;
                }
        }
    
    public void guardar(){
        Especialidades registro = new Especialidades();
        registro.setNombreEspecialidad(txtNombreEspecialidad.getText());
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarEspecialidades(?)}");
            procedimiento.setString(1, registro.getNombreEspecialidad());
            procedimiento.execute();
            listaEspecialidades.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void desactivarControles(){
        txtNombreEspecialidad.setEditable(false);
    }
    
    public void activarControles(){
        txtNombreEspecialidad.setEditable(true);
    }
    
    public void limpiarControles(){
        txtNombreEspecialidad.setText("");
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
