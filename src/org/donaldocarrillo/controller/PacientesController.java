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
import org.donaldocarrillo.bean.Paciente;
import org.donaldocarrillo.db.Conexion;
import org.donaldocarrillo.sistema.Principal;

public class PacientesController implements Initializable {
    private enum operaciones {AGREGAR, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private Principal escenarioPrincipal;
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Paciente> listaPaciente;
    @FXML TextField txtDireccion;
    @FXML TextField txtDPI;
    @FXML TextField txtNombres;
    @FXML TextField txtApellidos;
    @FXML TextField txtSexo;
    @FXML TextField txtOcupacion;
    @FXML TextField txtFechaNacimiento;
    @FXML TextField txtEdad;
    @FXML TableView tblPacientes;
    @FXML TableColumn colCodPaciente;
    @FXML TableColumn colDireccion;
    @FXML TableColumn colDPI;
    @FXML TableColumn colNombre;
    @FXML TableColumn colApellido;
    @FXML TableColumn colSexo;
    @FXML TableColumn colOcupacion;
    @FXML TableColumn colFechaNacimiento;
    @FXML TableColumn colEdad;
    @FXML Button btnAgregar;
    @FXML Button btnEliminar;
    @FXML Button btnEditar;
    @FXML Button btnReportes;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    }

    public void cargarDatos(){
        tblPacientes.setItems(getPaciente());
        colCodPaciente.setCellValueFactory(new PropertyValueFactory<Paciente, Integer>("codigoPaciente"));
        colDPI.setCellValueFactory(new PropertyValueFactory<Paciente, String>("DPI"));
        colApellido.setCellValueFactory(new PropertyValueFactory<Paciente, String>("apellidos"));
        colNombre.setCellValueFactory(new PropertyValueFactory<Paciente, String>("nombres"));
        colFechaNacimiento.setCellValueFactory(new PropertyValueFactory<Paciente, String>("fechaNacimiento"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<Paciente, String>("direccion"));
        colOcupacion.setCellValueFactory(new PropertyValueFactory<Paciente, String>("ocupacion"));
        colSexo.setCellValueFactory(new PropertyValueFactory<Paciente, String>("sexo"));
        colEdad.setCellValueFactory(new PropertyValueFactory<Paciente, Integer>("edad"));
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
    
    public void seleccionarElemento(){
        txtDireccion.setText(((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getDireccion());
        txtDPI.setText(((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getDPI());
        txtNombres.setText(((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getNombres());
        txtApellidos.setText(((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getApellidos());
        txtFechaNacimiento.setText(((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getFechaNacimiento());
        txtEdad.setText(String.valueOf(((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getEdad()));
        txtDireccion.setText(((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getDireccion());
        txtOcupacion.setText(((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getOcupacion());
        txtSexo.setText(((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getSexo());
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
    
        public void editarPacientes(){
        switch (tipoDeOperacion){
            case NINGUNO:
                if(tblPacientes.getSelectionModel().getSelectedItem() != null){
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
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EditarPacientes(?,?,?,?,?,?,?,?,?)}");
            Paciente registro = (Paciente)tblPacientes.getSelectionModel().getSelectedItem();
            registro.setDPI(txtDPI.getText());
            registro.setApellidos(txtApellidos.getText());
            registro.setNombres(txtNombres.getText());
            registro.setFechaNacimiento(txtFechaNacimiento.getText());
            registro.setEdad(Integer.parseInt(txtEdad.getText()));
            registro.setDireccion(txtDireccion.getText());
            registro.setOcupacion(txtOcupacion.getText());
            registro.setSexo(txtSexo.getText());
            procedimiento.setInt(1, registro.getCodigoPaciente());
            procedimiento.setString(2, registro.getDPI());
            procedimiento.setString(3, registro.getApellidos());
            procedimiento.setString(4, registro.getNombres());
            procedimiento.setString(5, registro.getFechaNacimiento());
            procedimiento.setInt(6, registro.getEdad());
            procedimiento.setString(7, registro.getDireccion());
            procedimiento.setString(8, registro.getOcupacion());
            procedimiento.setString(9, registro.getSexo());
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void eliminarPacientes(){
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
                if(tblPacientes.getSelectionModel().getSelectedItem() != null ){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Esta seguro de eliminar el registro?", "Eliminar Medico", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_OPTION ){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarPacientes(?)}");
                            procedimiento.setInt(1,((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getCodigoPaciente());
                            procedimiento.execute();
                            listaPaciente.remove(tblPacientes.getSelectionModel().getSelectedIndex());
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
                tipoDeOperacion = PacientesController.operaciones.GUARDAR;
                break;
            case GUARDAR:
                guardar();
                desactivarControles();
                limpiarControles();
                btnAgregar.setText("Agregar");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReportes.setDisable(false);
                tipoDeOperacion = PacientesController.operaciones.NINGUNO;
                cargarDatos();
               break; 
         }  
    }
        
    public void guardar(){
         Paciente registro = new Paciente();
         registro.setDPI(txtDPI.getText());
         registro.setApellidos(txtApellidos.getText());
         registro.setNombres(txtNombres.getText());
         registro.setFechaNacimiento(txtFechaNacimiento.getText());
         registro.setEdad(Integer.parseInt(txtEdad.getText()));
         registro.setDireccion(txtDireccion.getText());
         registro.setOcupacion(txtOcupacion.getText());
         registro.setSexo(txtSexo.getText());
        try{
           PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarPacientes(?,?,?,?,?,?,?,?)}");
           procedimiento.setString(1, registro.getDPI());
           procedimiento.setString(2, registro.getApellidos());
           procedimiento.setString(3, registro.getNombres());
           procedimiento.setString(4, registro.getFechaNacimiento());
           procedimiento.setInt(5, registro.getEdad());
           procedimiento.setString(6, registro.getDireccion());
           procedimiento.setString(7, registro.getOcupacion());
           procedimiento.setString(8, registro.getSexo());
           procedimiento.execute();
           listaPaciente.add(registro);
        }catch(Exception e){
            e.printStackTrace();
         }
    }    
        
        
    
    public void desactivarControles(){
        txtDireccion.setDisable(true);
        txtDPI.setDisable(true);
        txtNombres.setDisable(true);
        txtApellidos.setDisable(true);
        txtFechaNacimiento.setDisable(true);
        txtEdad.setDisable(true);
        txtDireccion.setDisable(true);
        txtOcupacion.setDisable(true);
        txtSexo.setDisable(true); 
    }
    
    public void activarControles(){
        txtDireccion.setDisable(false);
        txtDPI.setDisable(false);
        txtNombres.setDisable(false);
        txtApellidos.setDisable(false);
        txtFechaNacimiento.setDisable(false);
        txtEdad.setDisable(false);
        txtDireccion.setDisable(false);
        txtOcupacion.setDisable(false);
        txtSexo.setDisable(false);
    }
    
    
    public void limpiarControles(){
        txtDireccion.setText("");
        txtDPI.setText("");
        txtNombres.setText("");
        txtApellidos.setText("");
        txtFechaNacimiento.setText("");
        txtEdad.setText("");
        txtDireccion.setText("");
        txtOcupacion.setText("");
        txtSexo.setText("");
    }
    
    public Principal getEscenarioPrincipal(){
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void menuPrincipal (){
        escenarioPrincipal.menuPrincipal();
    }
}
