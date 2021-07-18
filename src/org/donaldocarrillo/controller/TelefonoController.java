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
import org.donaldocarrillo.bean.Medicos;
import org.donaldocarrillo.bean.TelefonoMedico;
import org.donaldocarrillo.db.Conexion;
import org.donaldocarrillo.sistema.Principal;

public class TelefonoController implements Initializable{
    private enum operaciones{AGREGAR, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private Principal escenarioPrincipal;
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<TelefonoMedico> listaTelefono;
    private ObservableList<Medicos> listaMedico;
    @FXML private TextField txtTelefonoTrabajo;
    @FXML private TextField txtTelefonoPersonal;
    @FXML private ComboBox cmbCodMedico;
    @FXML private TableView tblTelefono;
    @FXML private TableColumn colCodTelefonoMedico;
    @FXML private TableColumn colTelefonoPersonal;
    @FXML private TableColumn colTelefonoTrabajo;
    @FXML private TableColumn colCodMedico;
    @FXML private Button btnAgregar;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReportes;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbCodMedico.setItems(getMedicos());
    }
    
    public void cargarDatos(){
        tblTelefono.setItems(getTelefonosMedico());
        colCodTelefonoMedico.setCellValueFactory(new PropertyValueFactory<TelefonoMedico, Integer>("codigoTelefonoMedico"));
        colTelefonoPersonal.setCellValueFactory(new PropertyValueFactory<TelefonoMedico, String>("telefonoPersonal"));
        colTelefonoTrabajo.setCellValueFactory(new PropertyValueFactory<TelefonoMedico, String>("telefonoTrabajo"));
        colCodMedico.setCellValueFactory(new PropertyValueFactory<TelefonoMedico, Integer>("codigoMedico"));
        }
    
    public ObservableList<TelefonoMedico> getTelefonosMedico(){
        ArrayList<TelefonoMedico> lista = new ArrayList<TelefonoMedico>();
        try{
           PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarTelefonosMedico}");
           ResultSet resultado = procedimiento.executeQuery();
           while (resultado.next()){
               lista.add(new TelefonoMedico(resultado.getInt("codigoTelefonoMedico"),
                                             resultado.getString("telefonoPersonal"),
                                             resultado.getString("telefonoTrabajo"),
                                             resultado.getInt("codigoMedico")));
           }
        }catch(Exception e){
            e.printStackTrace();
        }
    return listaTelefono = FXCollections.observableList(lista);
}
    
    
    public ObservableList<Medicos> getMedicos(){
        ArrayList<Medicos> lista = new ArrayList<Medicos>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarMedicos()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()){
                lista.add(new Medicos(resultado.getInt("codigoMedico"),
                                     resultado.getInt("licenciaMedica"),
                                     resultado.getString("nombres"),
                                     resultado.getString("apellidos"),
                                     resultado.getString("horaEntrada"),
                                     resultado.getString("horaSalida"),
                                      resultado.getInt("turnoMaximo"),
                                     resultado.getString("sexo")));
                }
            }catch(Exception e){
            e.printStackTrace();
            }    
        return listaMedico = FXCollections.observableList(lista);
    }
    
    public void seleccionarElementos(){
        txtTelefonoPersonal.setText(((TelefonoMedico)tblTelefono.getSelectionModel().getSelectedItem()).getTelefonoPersonal());
        txtTelefonoTrabajo.setText(((TelefonoMedico)tblTelefono.getSelectionModel().getSelectedItem()).getTelefonoTrabajo());
        cmbCodMedico.getSelectionModel().select(buscarMedico(((TelefonoMedico)tblTelefono.getSelectionModel().getSelectedItem()).getCodigoMedico()));
    }
    
    public TelefonoMedico buscarTelefonosMedico(int codigoTelefonoMedico){
        TelefonoMedico resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarTelefonoMedico(?)}");
            procedimiento.setInt(1, codigoTelefonoMedico);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new TelefonoMedico(registro.getInt("codigoTelefonoMedico"),
                                                registro.getString("telefonoPersonal"),
                                                registro.getString("telefonoTrabajo"),
                                                registro.getInt("codigoMedico"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
    
    public Medicos buscarMedico(int codigoMedico){
        Medicos resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarMedicos(?)}");
            procedimiento.setInt(1, codigoMedico);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Medicos(registro.getInt("codigoMedico"),
                                        registro.getInt("licenciaMedica"),
                                        registro.getString("nombres"),
                                        registro.getString("apellidos"),
                                        registro.getString("horaEntrada"),
                                        registro.getString("horaSalida"),
                                        registro.getInt("turnoMaximo"),
                                        registro.getString("sexo"));
                 }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
    
    public void editarTelefono(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if (tblTelefono.getSelectionModel().getSelectedItem() != null){
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
           PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EditarTelefonosMedico(?,?,?,?)}");
           TelefonoMedico registro = (TelefonoMedico)tblTelefono.getSelectionModel().getSelectedItem();
           registro.setTelefonoPersonal(txtTelefonoPersonal.getText());
           registro.setTelefonoTrabajo(txtTelefonoTrabajo.getText());
           registro.setCodigoMedico(((Medicos)cmbCodMedico.getSelectionModel().getSelectedItem()).getCodigoMedico());
           procedimiento.setInt(1, registro.getCodigoTelefonoMedico());
           procedimiento.setString(2, registro.getTelefonoPersonal());
           procedimiento.setString(3, registro.getTelefonoTrabajo());
           procedimiento.setInt(4, registro.getCodigoMedico());
           procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void eliminarTelefono(){
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
                if (tblTelefono.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Esta seguro de eliminar este registro?", "Eliminar Telefono Medico", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarTelefonosMedico(?)}");
                            procedimiento.setInt(1, ((TelefonoMedico)tblTelefono.getSelectionModel().getSelectedItem()).getCodigoTelefonoMedico());
                            procedimiento.execute();
                            listaTelefono.remove(tblTelefono.getSelectionModel().getFocusedIndex());
                            limpiarControles();
                        }catch(Exception e){
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
        }
    }
     
     public void guardar(){
         TelefonoMedico registro = new TelefonoMedico();
         registro.setTelefonoPersonal(txtTelefonoPersonal.getText());
         registro.setTelefonoTrabajo(txtTelefonoTrabajo.getText());
         registro.setCodigoMedico(((Medicos)cmbCodMedico.getSelectionModel().getSelectedItem()).getCodigoMedico());
         try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarTelefonosMedico(?,?)}");
            procedimiento.setString(1, registro.getTelefonoPersonal());
            procedimiento.setString(2, registro.getTelefonoTrabajo());
            procedimiento.execute();
            listaTelefono.add(registro);
         }catch(Exception e){
             e.printStackTrace();
         }
     }
    
     public void desactivarControles(){
        txtTelefonoPersonal.setEditable(false);
        txtTelefonoTrabajo.setEditable(false);
        cmbCodMedico.setDisable(true);
        }
    
    public void activarControles(){
        txtTelefonoPersonal.setEditable(true);
        txtTelefonoTrabajo.setEditable(true);
        cmbCodMedico.setDisable(false);
    }
    
    public void limpiarControles(){
        txtTelefonoPersonal.setText("");
        txtTelefonoTrabajo.setText("");
        cmbCodMedico.getSelectionModel().clearSelection();
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
