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
import org.donaldocarrillo.bean.Medicos;
import org.donaldocarrillo.db.Conexion;
import org.donaldocarrillo.sistema.Principal;

public class MedicoController implements Initializable {
   private enum operaciones{AGREGAR, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private Principal escenarioPrincipal;
   private operaciones tipoDeOperacion = operaciones.NINGUNO; 
    private ObservableList<Medicos> listaMedico;
    @FXML private TextField txtCodMedico;
    @FXML private TextField txtLicenciaMedica;
    @FXML private TextField txtNombres;
    @FXML private TextField txtApellidos;
    @FXML private TextField txtSexo;
    @FXML private TextField txtTurnoMaximo;
    @FXML private TextField txtHoraEntrada;
    @FXML private TextField txtHoraSalida;
    @FXML private TableView tblMedicos;
    @FXML private TableColumn colCodMedico;
    @FXML private TableColumn colLicenciaMedica;
    @FXML private TableColumn colNombre;
    @FXML private TableColumn colApellido;
    @FXML private TableColumn colHoraEntrada;
    @FXML private TableColumn colHoraSalida;
    @FXML private TableColumn colTurno;
    @FXML private TableColumn colSexo;
    @FXML private Button btnAgregar;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReportes;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    }
    
    public void cargarDatos(){
        tblMedicos.setItems(getMedicos());
        colCodMedico.setCellValueFactory(new PropertyValueFactory<Medicos, Integer>("codigoMedico"));
        colLicenciaMedica.setCellValueFactory(new PropertyValueFactory<Medicos, Integer>("licenciaMedica"));
        colNombre.setCellValueFactory(new PropertyValueFactory<Medicos, String>("nombres"));
        colApellido.setCellValueFactory(new PropertyValueFactory<Medicos, String>("apellidos"));
        colHoraEntrada.setCellValueFactory(new PropertyValueFactory<Medicos, String>("horaEntrada"));
        colHoraSalida.setCellValueFactory(new PropertyValueFactory<Medicos, String>("horaSalida"));
        colTurno.setCellValueFactory(new PropertyValueFactory<Medicos, Integer>("turnoMaximo"));
        colSexo.setCellValueFactory(new PropertyValueFactory<Medicos, String>("sexo"));
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

    public void  seleccionarElemento(){
        txtCodMedico.setText(String.valueOf(((Medicos)tblMedicos.getSelectionModel().getSelectedItem()).getCodigoMedico()));
        txtLicenciaMedica.setText(String.valueOf(((Medicos)tblMedicos.getSelectionModel().getSelectedItem()).getLicenciaMedica()));
        txtNombres.setText(((Medicos)tblMedicos.getSelectionModel().getSelectedItem()).getNombres());
        txtApellidos.setText(((Medicos)tblMedicos.getSelectionModel().getSelectedItem()).getApellidos());
        txtHoraEntrada.setText(((Medicos)tblMedicos.getSelectionModel().getSelectedItem()).getHoraEntrada());
        txtHoraSalida.setText(((Medicos)tblMedicos.getSelectionModel().getSelectedItem()).getHoraSalida());
        txtTurnoMaximo.setText(String.valueOf(((Medicos)tblMedicos.getSelectionModel().getSelectedItem()).getTurnoMaximo()));
        txtSexo.setText(((Medicos)tblMedicos.getSelectionModel().getSelectedItem()).getSexo());
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
    
    public void editarMedicos(){
        switch (tipoDeOperacion){
            case NINGUNO:
                if(tblMedicos.getSelectionModel().getSelectedItem() != null){
                    btnEditar.setText("Actualizar");
                    btnReportes.setText("Cancelar");
                    btnAgregar.setDisable(true);
                    btnEliminar.setDisable(true);
                    activarControles();
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                }else {
                    JOptionPane.showMessageDialog(null, "Selecionar un elemento.");
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
           PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EditarMedicos(?,?,?,?,?,?,?)}");
           Medicos registro = (Medicos)tblMedicos.getSelectionModel().getSelectedItem();
           registro.setLicenciaMedica(Integer.parseInt(txtLicenciaMedica.getText()));
           registro.setNombres(txtNombres.getText());
           registro.setApellidos(txtApellidos.getText());
           registro.setHoraEntrada(txtHoraEntrada.getText());
           registro.setHoraSalida(txtHoraSalida.getText());
           registro.setSexo(txtSexo.getText());
           procedimiento.setInt(1, registro.getCodigoMedico());
           procedimiento.setInt(2, registro.getLicenciaMedica());
           procedimiento.setString(3, registro.getNombres());
           procedimiento.setString(4, registro.getApellidos());
           procedimiento.setString(5, registro.getHoraEntrada());
           procedimiento.setString(6, registro.getHoraSalida());
           procedimiento.setString(7, registro.getSexo());
           procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void eliminarMedicos(){
        switch(tipoDeOperacion){
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
                if(tblMedicos.getSelectionModel().getSelectedItem() != null ){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Esta seguro de eliminar el registro?", "Eliminar Medico", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_OPTION ){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarMedicos(?)}");
                            procedimiento.setInt(1,((Medicos)tblMedicos.getSelectionModel().getSelectedItem()).getCodigoMedico());
                            procedimiento.execute();
                            listaMedico.remove(tblMedicos.getSelectionModel().getSelectedIndex());
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
        Medicos registro = new Medicos();
         registro.setLicenciaMedica(Integer.parseInt(txtLicenciaMedica.getText()));
         registro.setNombres(txtNombres.getText());
         registro.setApellidos(txtApellidos.getText());
         registro.setHoraEntrada(txtHoraEntrada.getText());
         registro.setHoraSalida(txtHoraSalida.getText());
         registro.setSexo(txtSexo.getText());
        try{
             PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarMedicos(?,?,?,?,?,?)}");
             procedimiento.setInt(1, registro.getLicenciaMedica());
             procedimiento.setString(2, registro.getNombres());
             procedimiento.setString(3, registro.getApellidos());
             procedimiento.setString(4, registro.getHoraEntrada());
             procedimiento.setString(5, registro.getHoraSalida());
             procedimiento.setString(6, registro.getSexo());
             procedimiento.execute();
             listaMedico.add(registro);
        }catch (Exception e){
             e.printStackTrace();
         }
    }
    
    public void desactivarControles(){
        txtCodMedico.setEditable(false);
        txtLicenciaMedica.setEditable(false);
        txtNombres.setEditable(false);
        txtApellidos.setEditable(false);
        txtSexo.setEditable(false);
        txtTurnoMaximo.setEditable(false);
        txtHoraEntrada.setEditable(false);
        txtHoraSalida.setEditable(false);
    }
    
    public void activarControles(){
        txtCodMedico.setEditable(false);
        txtLicenciaMedica.setEditable(true);
        txtNombres.setEditable(true);
        txtApellidos.setEditable(true);
        txtSexo.setEditable(true);
        txtTurnoMaximo.setEditable(false);
        txtHoraEntrada.setEditable(true);
        txtHoraSalida.setEditable(true);
    }
    
    public void limpiarControles(){
        txtCodMedico.setText("");
        txtLicenciaMedica.setText("");
        txtNombres.setText("");
        txtApellidos.setText("");
        txtSexo.setText("");
        txtTurnoMaximo.setText("");
        txtHoraEntrada.setText("");
        txtHoraSalida.setText("");
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