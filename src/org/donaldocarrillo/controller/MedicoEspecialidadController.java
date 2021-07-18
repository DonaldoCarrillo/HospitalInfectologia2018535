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
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.donaldocarrillo.bean.Especialidades;
import org.donaldocarrillo.bean.Horarios;
import org.donaldocarrillo.bean.Medicos;
import org.donaldocarrillo.bean.MedicoEspecialidad;
import org.donaldocarrillo.db.Conexion;
import org.donaldocarrillo.sistema.Principal;


public class MedicoEspecialidadController implements Initializable{
    private enum operaciones{AGREGAR, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR,CANCELAR,NINGUNO};
    private Principal escenarioPrincipal;
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<MedicoEspecialidad> listaMedicoEspecialidad;
    private ObservableList<Medicos> listaMedico;
    private ObservableList<Especialidades> listaEspecialidades;
    private ObservableList<Horarios> listaHorarios;
    @FXML private ComboBox cmbCodMedico;
    @FXML private ComboBox cmbCodEspecialidad;
    @FXML private ComboBox cmbCodHorario;
    @FXML private TableView tblMedicoEspecialidad;
    @FXML private TableColumn colCodMedicoEspecialidad;
    @FXML private TableColumn colCodMedico;
    @FXML private TableColumn colCodEspecialidad;
    @FXML private TableColumn colCodHorario;
    @FXML private Button btnAgregar;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReportes;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbCodMedico.setItems(getMedicos());
        cmbCodEspecialidad.setItems(getEspecialidades());
        cmbCodHorario.setItems(getHorarios());
    }
    
    public void cargarDatos(){
        tblMedicoEspecialidad.setItems(getMedicoEspecialidad());
        colCodMedicoEspecialidad.setCellValueFactory(new PropertyValueFactory<MedicoEspecialidad, Integer>("codigoMedicoEspecialidad"));
        colCodMedico.setCellValueFactory(new PropertyValueFactory<MedicoEspecialidad, Integer>("codigoMedico"));
        colCodEspecialidad.setCellValueFactory(new PropertyValueFactory<MedicoEspecialidad, Integer>("codigoEspecialidad"));
        colCodHorario.setCellValueFactory(new PropertyValueFactory<MedicoEspecialidad, Integer>("codigoHorario"));
    }
    
    public ObservableList<MedicoEspecialidad> getMedicoEspecialidad(){
        ArrayList<MedicoEspecialidad> lista = new ArrayList<MedicoEspecialidad>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarMedicoEspecialidad()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new MedicoEspecialidad(resultado.getInt("codigoMedicoEspecialidad"),
                                                 resultado.getInt("codigoMedico"),
                                                 resultado.getInt("codigoEspecialidad"),
                                                 resultado.getInt("codigoHorario")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
     return listaMedicoEspecialidad = FXCollections.observableList(lista);
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
     
     public ObservableList<Horarios> getHorarios(){
        ArrayList<Horarios> lista = new ArrayList<Horarios>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarHorarios()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Horarios(resultado.getInt("codigoHorario"),
                                       resultado.getString("horarioInicio"),
                                       resultado.getString("horarioSalida"),
                                       resultado.getInt("lunes"),
                                       resultado.getInt("martes"),
                                       resultado.getInt("miercoles"),
                                       resultado.getInt("jueves"),
                                       resultado.getInt("viernes")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaHorarios = FXCollections.observableList(lista);
    }
     
     public void seleccionarElemento(){
         cmbCodMedico.getSelectionModel().select(buscarMedico(((MedicoEspecialidad)tblMedicoEspecialidad.getSelectionModel().getSelectedItem()).getCodigoMedico()));
         cmbCodEspecialidad.getSelectionModel().select(buscarEspecialidades(((MedicoEspecialidad)tblMedicoEspecialidad.getSelectionModel().getSelectedItem()).getCodigoEspecialidad()));
         cmbCodHorario.getSelectionModel().select(buscarHorarios(((MedicoEspecialidad)tblMedicoEspecialidad.getSelectionModel().getSelectedItem()).getCodigoHorario()));
     }
     
     public MedicoEspecialidad buscarMedicoEspecialidad(int codigoMedicoEspecialidad){
         MedicoEspecialidad resultado = null;
         try{
             PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarMedicoEspecialidad(?)}");
             procedimiento.setInt(1, codigoMedicoEspecialidad);
             ResultSet registro = procedimiento.executeQuery();
             while(registro.next()){
                 resultado = new MedicoEspecialidad(registro.getInt("codigoMedicoEspecialidad"),
                                                    registro.getInt("codigoMedico"),
                                                    registro.getInt("codigoEspecialidad"),
                                                    registro.getInt("codigoHorario"));
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
                resultado = new Medicos (registro.getInt("codigoMedico"),
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
     
     public Horarios buscarHorarios(int codigoHorario){
        Horarios resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarHorarios(?)}");
            procedimiento.setInt(1, codigoHorario);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Horarios(registro.getInt("codigoHorario"),
                                        registro.getString("horarioInicio"),
                                        registro.getString("horarioSalida"),
                                        registro.getInt("lunes"),
                                        registro.getInt("martes"),
                                        registro.getInt("miercoles"),
                                        registro.getInt("jueves"),
                                        registro.getInt("viernes"));                                       
                 }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
     
     public void editarMedicoEspecialidad(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if (tblMedicoEspecialidad.getSelectionModel().getSelectedItem() != null){
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
             PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EditarMedicoEspecialidad(?,?,?,?)}");
             MedicoEspecialidad registro = (MedicoEspecialidad)tblMedicoEspecialidad.getSelectionModel().getSelectedItem();
             registro.setCodigoMedico(((Medicos)cmbCodMedico.getSelectionModel().getSelectedItem()).getCodigoMedico());
             registro.setCodigoEspecialidad(((Especialidades)cmbCodEspecialidad.getSelectionModel().getSelectedItem()).getCodigoEspecialidad());
             registro.setCodigoHorario(((Horarios)cmbCodHorario.getSelectionModel().getSelectedItem()).getCodigoHorario());
             procedimiento.setInt(1, registro.getCodigoMedicoEspecialidad());
             procedimiento.setInt(2, registro.getCodigoMedico());
             procedimiento.setInt(3, registro.getCodigoEspecialidad());
             procedimiento.setInt(4, registro.getCodigoHorario());
             procedimiento.execute();
         }catch(Exception e){
             e.printStackTrace();
         }
     }
     
     public void eliminarMedicoEspecialidad(){
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
                if (tblMedicoEspecialidad.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "Esta seguro de eliminar este registro?", "Eliminar Medico Especialidad", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarMedicoEspecialidad(?)}");
                            procedimiento.setInt(1, ((MedicoEspecialidad)tblMedicoEspecialidad.getSelectionModel().getSelectedItem()).getCodigoMedicoEspecialidad());
                            procedimiento.execute();
                            listaMedicoEspecialidad.remove(tblMedicoEspecialidad.getSelectionModel().getFocusedIndex());
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
                break;
        }
    }   
     
     public void guardar(){
         MedicoEspecialidad registro = new MedicoEspecialidad();
         registro.setCodigoMedico(((Medicos)cmbCodMedico.getSelectionModel().getSelectedItem()).getCodigoMedico());
         registro.setCodigoEspecialidad(((Especialidades)cmbCodEspecialidad.getSelectionModel().getSelectedItem()).getCodigoEspecialidad());
         registro.setCodigoHorario(((Horarios)cmbCodHorario.getSelectionModel().getSelectedItem()).getCodigoHorario());
         try{
             PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarMedicoEspecialidad(?,?,?)}");
             procedimiento.setInt(1, registro.getCodigoMedico());
             procedimiento.setInt(2, registro.getCodigoEspecialidad());
             procedimiento.setInt(3, registro.getCodigoHorario());
             procedimiento.execute();
             listaMedicoEspecialidad.add(registro);
         }catch(Exception e){
             e.printStackTrace();
         }
     }
     
     public void desactivarControles(){
         cmbCodMedico.setEditable(true);
         cmbCodEspecialidad.setEditable(true);
         cmbCodHorario.setEditable(true);
     }
     
     public void activarControles(){
         cmbCodMedico.setEditable(false);
         cmbCodEspecialidad.setEditable(false);
         cmbCodHorario.setEditable(false);
     }
     
     public void limpiarControles(){
         cmbCodMedico.getSelectionModel().clearSelection();
         cmbCodEspecialidad.getSelectionModel().clearSelection();
         cmbCodHorario.getSelectionModel().clearSelection();
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
