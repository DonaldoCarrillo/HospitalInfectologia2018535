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
import org.donaldocarrillo.bean.ControlCitas;
import org.donaldocarrillo.bean.Medicos;
import org.donaldocarrillo.bean.Paciente;
import org.donaldocarrillo.db.Conexion;
import org.donaldocarrillo.sistema.Principal;

public class ControlCitasController implements Initializable{
    private enum operaciones{AGREGAR, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private Principal escenarioPrincipal;
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<ControlCitas> listaControlCitas;
    private ObservableList<Medicos> listaMedico;
    private ObservableList<Paciente> listaPaciente;
    @FXML TextField txtFecha;
    @FXML TextField txtHoraInicio;
    @FXML TextField txtHoraFin;
    @FXML ComboBox cmbCodMedico;
    @FXML ComboBox cmbCodPaciente;
    @FXML Button btnAgregar;
    @FXML Button btnEliminar;
    @FXML Button btnEditar;
    @FXML Button btnReporte;
    @FXML TableView tblControlCita;
    @FXML TableColumn colControl;
    @FXML TableColumn colHoraInicio;
    @FXML TableColumn colHoraFin;
    @FXML TableColumn colCodigoMedico;
    @FXML TableColumn colCodPaciente;
    @FXML TableColumn colFecha;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbCodMedico.setItems(getMedicos());
        cmbCodPaciente.setItems(getPaciente());
    }

    public void cargarDatos(){
        tblControlCita.setItems(getControlCitas());
        colControl.setCellValueFactory(new PropertyValueFactory<ControlCitas, Integer>("codigoControlCita"));
        colHoraInicio.setCellValueFactory(new PropertyValueFactory<ControlCitas, String>("horaInicio"));
        colHoraFin.setCellValueFactory(new PropertyValueFactory<ControlCitas, String>("horaFin"));
        colCodigoMedico.setCellValueFactory(new PropertyValueFactory<ControlCitas, Integer>("codigoMedico"));
        colCodPaciente.setCellValueFactory(new PropertyValueFactory<ControlCitas, Integer>("codigoPaciente"));
        colFecha.setCellValueFactory(new PropertyValueFactory<ControlCitas, String>("fecha"));
    }
    
    public ObservableList<ControlCitas> getControlCitas(){
        ArrayList<ControlCitas> lista = new ArrayList<ControlCitas>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarControlCitas()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new ControlCitas(resultado.getInt("codigoControlCita"),
                                           resultado.getString("fecha"),
                                           resultado.getString("horaInicio"),
                                           resultado.getString("horaFin"),
                                           resultado.getInt("codigoMedico"),
                                           resultado.getInt("codigoPaciente")));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return listaControlCitas = FXCollections.observableList(lista);
    }
    
    public void seleccionarElementos(){
        txtFecha.setText(((ControlCitas)tblControlCita.getSelectionModel().getSelectedItem()).getFecha());
        txtHoraInicio.setText(((ControlCitas)tblControlCita.getSelectionModel().getSelectedItem()).getHoraInicio());
        txtHoraFin.setText(((ControlCitas)tblControlCita.getSelectionModel().getSelectedItem()).getHoraFin());
        cmbCodMedico.getSelectionModel().select(bucarMedicos(((ControlCitas)tblControlCita.getSelectionModel().getSelectedItem()).getCodigoMedico()));       
        cmbCodPaciente.getSelectionModel().select(buscarPaciente(((ControlCitas)tblControlCita.getSelectionModel().getSelectedItem()).getCodigoPaciente()));
    }
    
    public ControlCitas buscarControlCitas(int codigoControlCita){
        ControlCitas resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarControlCitas}");
            procedimiento.setInt(1, codigoControlCita);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new ControlCitas(registro.getInt("codigoControlCita"),
                                             registro.getString("fecha"),
                                             registro.getString("horaInicio"),
                                             registro.getString("horaFin"),
                                             registro.getInt("codigoMedico"),
                                             registro.getInt("codigoPaciente"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
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
        }catch (Exception e){
            e.printStackTrace();
        }
        return listaMedico = FXCollections.observableList(lista);
    }
    public ObservableList<Paciente>getPaciente(){
        ArrayList<Paciente> lista = new ArrayList<Paciente>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarPacientes()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()){
                lista.add(new Paciente(resultado.getInt("codigoPaciente"),
                        resultado.getString("DPI"),
                        resultado.getString("apellidos"),
                        resultado.getString("nombres"),
                        resultado.getString("fechaNacimiento"),
                        resultado.getInt("edad"), 
                        resultado.getString("direccion"),
                        resultado.getString("ocupacion"),
                        resultado.getString("sexo")));         
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return listaPaciente = FXCollections.observableList(lista);
    }        
        
    public Medicos bucarMedicos(int codigoMedico){
        Medicos resultado = null;
        try{
         PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarMedicos(?)}");
         procedimiento.setInt(1, codigoMedico);
         ResultSet registro = procedimiento.executeQuery();
         while(registro.next()){
             resultado = new Medicos( registro.getInt("codigoMedico"),
             registro.getInt("licenciaMedica"),
             registro.getString("nombres"),
             registro.getString("apellidos"),
             registro.getString("horaEntrada"),        
             registro.getString("horaSalida"),
             registro.getInt("turnoMaximo"),
             registro.getString("Sexo")
             );
         }
     }catch (Exception e){
         e.printStackTrace();
     }   
        return resultado;
    }    
    
    public Paciente buscarPaciente(int codigoPaciente){
         Paciente resultado = null;
     try{
         PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarPaciente(?)}");
                 procedimiento.setInt(1, codigoPaciente);
                 ResultSet registro = procedimiento.executeQuery();
                 while(registro.next()){
                     resultado = new Paciente( registro.getInt("codigoPaciente"),
                     registro.getString("DPI"),
                     registro.getString("apellidos"),
                     registro.getString("nombres"),
                     registro.getString("fechaNacimiento"),
                     registro.getInt("edad"),
                     registro.getString("direccion"),
                     registro.getString("ocupacion"),
                     registro.getString("sexo")
                     ); 
               }
                 }catch (Exception e){
                    e.printStackTrace();
                }   
                   return resultado;
            }    
    public void editarControlCitas(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if (tblControlCita.getSelectionModel().getSelectedItem() != null){
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
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
                btnReporte.setText("Reporte");
                btnAgregar.setDisable(false);
                btnEliminar.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
                break;
        }
    }

    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EditarControlCitas(?,?,?,?,?)}");
            ControlCitas registro = (ControlCitas)tblControlCita.getSelectionModel().getSelectedItem();
            registro.setFecha(txtFecha.getText());
            registro.setHoraInicio(txtHoraInicio.getText());
            registro.setHoraFin(txtHoraFin.getText());
           registro.setCodigoMedico(((Medicos)cmbCodMedico.getSelectionModel().getSelectedItem()).getCodigoMedico());
            procedimiento.setInt(1, registro.getCodigoControlCita());
            procedimiento.setString(2, registro.getFecha());
            procedimiento.setString(3, registro.getHoraInicio());
            procedimiento.setString(4, registro.getHoraFin());
            procedimiento.setInt(5, registro.getCodigoMedico());
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
        public void eliminarControlCita(){
        switch(tipoDeOperacion){
            case GUARDAR:
                desactivarControles();
                limpiarControles();
                btnAgregar.setText("Agregar");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tipoDeOperacion = operaciones.GUARDAR;
                break;
            default:
                if (tblControlCita.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Esta seguro de eliminar este registro?", "Eliminar control de cita", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarControlCitas(?)}");
                            procedimiento.setInt(1, ((ControlCitas)tblControlCita.getSelectionModel().getSelectedItem()).getCodigoControlCita());
                            procedimiento.execute();
                            listaControlCitas.remove(tblControlCita.getSelectionModel().getFocusedIndex());
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
                limpiarControles();
                btnAgregar.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                tipoDeOperacion = operaciones.GUARDAR;
                break;
            case GUARDAR:
                guardar();
                desactivarControles();
                limpiarControles();
                btnAgregar.setText("Agregar");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
        }
    }
     
     public void guardar(){
         ControlCitas registro = new ControlCitas();
         registro.setFecha(txtFecha.getText());
         registro.setHoraInicio(txtHoraInicio.getText());
         registro.setHoraFin(txtHoraFin.getText());
         registro.setCodigoMedico(((Medicos)cmbCodMedico.getSelectionModel().getSelectedItem()).getCodigoMedico());
         registro.setCodigoPaciente(((Paciente)cmbCodPaciente.getSelectionModel().getSelectedItem()).getCodigoPaciente());
         try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarControlCitas(?,?,?,?,?)}");
            procedimiento.setString(1, registro.getFecha());
            procedimiento.setString(2, registro.getHoraInicio());
            procedimiento.setString(3, registro.getHoraFin());
            procedimiento.setInt(4, registro.getCodigoMedico());
            procedimiento.setInt(5, registro.getCodigoPaciente());
            procedimiento.execute();
            listaControlCitas.add(registro);
         }catch(Exception e){
             e.printStackTrace();
         }
     }
     
    public void desactivarControles(){
        txtFecha.setEditable(false);
        txtHoraInicio.setEditable(false);
        txtHoraFin.setEditable(false);
        cmbCodMedico.setDisable(true);
    }
    
    public void activarControles(){
        txtFecha.setEditable(true);
        txtHoraInicio.setEditable(true);
        txtHoraFin.setEditable(true);
        cmbCodMedico.setDisable(false);

    }    
    
    public void limpiarControles(){
        txtFecha.setText("");
        txtHoraInicio.setText("");
        txtHoraFin.setText("");
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
