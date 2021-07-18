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
import org.donaldocarrillo.bean.Turno;
import org.donaldocarrillo.db.Conexion;
import org.donaldocarrillo.sistema.Principal;

public class TurnoController implements Initializable {
     private enum operaciones {NUEVO,AGREGAR,GUARDAR,ELIMINAR,EDITAR,ACTUALIZAR,CANCELAR,NINGUNO}
        private Principal escenarioPrincipal; 
        private operaciones tipoDeOperacion = operaciones.NINGUNO;
        private ObservableList <Turno> listaTurno; 
    
    @FXML private ComboBox cmbCodigoTurnoResponsable; 
    @FXML private ComboBox cmbCodigoTurno; 
    @FXML private ComboBox cmbCodigoPaciente; 
    @FXML private ComboBox cmbCodigoMedicoEspecialidad;    
    @FXML private TextField txtValorCita; 
    @FXML private TextField txtFechaTurno;
    @FXML private TextField txtFechaCita;
    @FXML private TableView tblTurnoPaciente; 
    @FXML private TableColumn colValorCita; 
    @FXML private TableColumn colFechaCita;
    @FXML private TableColumn colCodTurnoR;
    @FXML private TableColumn colFechaTurno; 
    @FXML private TableColumn colCodTurno; 
    @FXML private TableColumn colCodPaciente; 
    @FXML private TableColumn colCodMedicoEspecialidad;   
    @FXML private Button btnAgregar; 
    @FXML private Button btnEliminar; 
    @FXML private Button btnEditar; 
    @FXML private Button btnReportes;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    }
  
    public void cargarDatos(){
    tblTurnoPaciente.setItems(getTurnos());
    colCodTurno.setCellValueFactory(new PropertyValueFactory <Turno, Integer> ("codigoTurno"));
    colFechaTurno.setCellValueFactory(new PropertyValueFactory <Turno, String > ("fechaTurno"));
    colFechaCita.setCellValueFactory(new PropertyValueFactory <Turno, String> ("fechaCita"));
    colValorCita.setCellValueFactory(new PropertyValueFactory <Turno , String > ("valorCita"));
    colCodMedicoEspecialidad.setCellValueFactory(new PropertyValueFactory <Turno, Integer> ("codigoMedicoEspecialidad"));
    colCodTurnoR.setCellValueFactory(new PropertyValueFactory <Turno, Integer> ("codigoResponsableTurno"));
    colCodPaciente.setCellValueFactory(new PropertyValueFactory <Turno, Integer> ("codigoPaciente"));
    }
    
    public ObservableList <Turno> getTurnos(){
        ArrayList <Turno> lista = new ArrayList <Turno>();
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarTurnos}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Turno(resultado.getInt("codigoTurno"),
                        resultado.getString("fechaTurno"),
                        resultado.getString("fechaCita"),
                        resultado.getString("valorCita"),
                        resultado.getInt("codigoMedicoEspecialidad"),
                        resultado.getInt("codigoResponsableTurno"),
                        resultado.getInt("codigoPaciente")));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return listaTurno = FXCollections.observableList(lista);
    }
    
    public void seleccionarElemento(){
        txtFechaTurno.setText(((Turno)tblTurnoPaciente.getSelectionModel().getSelectedItem()).getFechaTurno());
        txtFechaCita.setText(((Turno)tblTurnoPaciente.getSelectionModel().getSelectedItem()).getFechaCita());
        txtValorCita.setText(((Turno)tblTurnoPaciente.getSelectionModel().getSelectedItem()).getValorCita());
    }
    
    public Turno buscarTurno (int codigoTurno){
        Turno resultado = null;
        try { 
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarTurnos(?)}");
            procedimiento.setInt(1, codigoTurno);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()){
                resultado = new Turno (registro.getInt("codigoTurno"),
                        registro.getString("fechaTurno"),
                        registro.getString("fechaCita"),
                        registro.getString("valorCita"),
                        registro.getInt("codigoMedicoEspecialidad"),
                        registro.getInt("codigoResponsableTurno"),
                        registro.getInt("codigoPaciente"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
    
    public void editarTurno(){
        switch(tipoDeOperacion){
            case NINGUNO: 
                if(tblTurnoPaciente.getSelectionModel().getSelectedItem() != null){
                    btnEditar.setText("Actualizar");
                    btnReportes.setText("Cancelar");
                    btnAgregar.setDisable(true);
                    btnEliminar.setDisable(true);
                    activarControles();
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                }else {
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
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EditarTurno(?,?,?,?)}");
            Turno registro = (Turno)tblTurnoPaciente.getSelectionModel().getSelectedItem();
            registro.setCodigoTurno(((Turno)tblTurnoPaciente.getSelectionModel().getSelectedItem()).getCodigoTurno());
            registro.setFechaTurno(txtFechaTurno.getText());
            registro.setFechaCita(txtFechaCita.getText());
            registro.setValorCita(txtValorCita.getText());
            procedimiento.setInt(1, registro.getCodigoTurno());
            procedimiento.setString(2 ,registro.getFechaTurno());
            procedimiento.setString(3,registro.getFechaCita());
            procedimiento.setString(4, registro.getValorCita());
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void eliminarTurno(){
        switch(tipoDeOperacion){
            case GUARDAR: 
                desactivarControles ();
                limpiarControles ();
                btnAgregar.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReportes.setDisable(false);
                tipoDeOperacion = operaciones.NUEVO; 
                break;
            default: 
                if (tblTurnoPaciente.getSelectionModel().getSelectedItem() != null ) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar el registro?", "Eliminar Turno", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION )
                        try {
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarTurnos(?)}");
                            procedimiento.setInt(1, ((Turno)tblTurnoPaciente.getSelectionModel().getSelectedItem()).getCodigoTurno());
                            procedimiento.execute();
                            listaTurno.remove(tblTurnoPaciente.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                }else{
                    JOptionPane.showMessageDialog(null, "Debe Seleccionar un elemento");
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
                tipoDeOperacion = operaciones.NINGUNO; 
                cargarDatos();
            break;    
        }
    }
    
    public void guardar (){
        Turno registro = new Turno ();
        registro.setFechaTurno((txtFechaTurno.getText()));
        registro.setFechaCita(txtFechaCita.getText());
        registro.setValorCita(txtValorCita.getText());
        try{
                PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarTurno(?,?,?)}");
                procedimiento.setString(1, registro.getFechaTurno());
                procedimiento.setString(2, registro.getFechaCita());
                procedimiento.setString(3, registro.getValorCita());
                procedimiento.execute();
                listaTurno.add(registro);
        }catch (Exception e){
            e.printStackTrace();
        }
        
    }
    
    public void desactivarControles(){
        txtFechaTurno.setEditable(false);
        txtFechaCita.setEditable(false);
        txtValorCita.setEditable(false);
    }
    
    public void activarControles(){
        txtFechaTurno.setEditable(true);
        txtFechaCita.setEditable(true);
        txtValorCita.setEditable(true);
    }
    
    public void limpiarControles(){
        txtFechaTurno.setText("");
        txtFechaCita.setText("");
        txtValorCita.setText("");
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
