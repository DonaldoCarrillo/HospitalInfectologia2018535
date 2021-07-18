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
import org.donaldocarrillo.bean.Horarios;
import org.donaldocarrillo.db.Conexion;
import org.donaldocarrillo.sistema.Principal;


public class HorariosController implements Initializable{
    private enum operaciones{AGREGAR, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR,CANCELAR,NINGUNO};
    private Principal escenarioPrincipal; 
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Horarios> listaHorarios; 
    @FXML private TextField txtHoraEntrada;
    @FXML private TextField txtHoraSalida;
    @FXML private TextField txtLunes;
    @FXML private TextField txtMartes;
    @FXML private TextField txtMiercoles;
    @FXML private TextField txtJueves;
    @FXML private TextField txtViernes;    
    @FXML private TableView tblHorarios;
    @FXML private TableColumn colCodHorario;
    @FXML private TableColumn colHoraEntrada;
    @FXML private TableColumn colHoraSalida;
    @FXML private TableColumn colLunes;
    @FXML private TableColumn colMartes;
    @FXML private TableColumn colMiercoles;
    @FXML private TableColumn colJueves;
    @FXML private TableColumn colViernes;
    @FXML private Button btnAgregar;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReportes;
    @Override
    
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    }
    
    public void cargarDatos(){
        tblHorarios.setItems(getHorarios());
        colCodHorario.setCellValueFactory(new PropertyValueFactory<Horarios, Integer>("codigoHorario"));
        colHoraEntrada.setCellValueFactory(new PropertyValueFactory<Horarios, String>("horarioInicio"));
        colHoraSalida.setCellValueFactory(new PropertyValueFactory<Horarios, String>("horarioSalida"));
        colLunes.setCellValueFactory(new PropertyValueFactory<Horarios, Integer>("lunes"));
        colMartes.setCellValueFactory(new PropertyValueFactory<Horarios, Integer>("martes"));
        colMiercoles.setCellValueFactory(new PropertyValueFactory<Horarios, Integer>("miercoles"));
        colJueves.setCellValueFactory(new PropertyValueFactory<Horarios, Integer>("jueves"));
        colViernes.setCellValueFactory(new PropertyValueFactory<Horarios, Integer>("viernes"));
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
    
    public void seleccionarElementos(){
        txtHoraEntrada.setText(((Horarios)tblHorarios.getSelectionModel().getSelectedItem()).getHorarioInicio());
        txtHoraSalida.setText(((Horarios)tblHorarios.getSelectionModel().getSelectedItem()).getHorarioSalida());
        txtLunes.setText(String.valueOf(((Horarios)tblHorarios.getSelectionModel().getSelectedItem()).getLunes()));
        txtMartes.setText(String.valueOf(((Horarios)tblHorarios.getSelectionModel().getSelectedItem()).getMartes()));
        txtMiercoles.setText(String.valueOf(((Horarios)tblHorarios.getSelectionModel().getSelectedItem()).getMiercoles()));
        txtJueves.setText(String.valueOf(((Horarios)tblHorarios.getSelectionModel().getSelectedItem()).getJueves()));
        txtViernes.setText(String.valueOf(((Horarios)tblHorarios.getSelectionModel().getSelectedItem()).getViernes()));
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
     
     public void editarHorarios(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if (tblHorarios.getSelectionModel().getSelectedItem() != null){
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
             PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EditarHorarios(?,?,?,?,?,?,?,?)}");
             Horarios registro = (Horarios)tblHorarios.getSelectionModel().getSelectedItem();
             registro.setHorarioInicio(txtHoraEntrada.getText());
             registro.setHorarioSalida(txtHoraSalida.getText());
             registro.setLunes(Integer.parseInt(txtLunes.getText()));
             registro.setMartes(Integer.parseInt(txtMartes.getText()));
             registro.setMiercoles(Integer.parseInt(txtMiercoles.getText()));
             registro.setJueves(Integer.parseInt(txtJueves.getText()));
             registro.setViernes(Integer.parseInt(txtViernes.getText()));
             procedimiento.setInt(1, registro.getCodigoHorario());
             procedimiento.setString(2, registro.getHorarioInicio());
             procedimiento.setString(3, registro.getHorarioSalida());
             procedimiento.setInt(4, registro.getLunes());
             procedimiento.setInt(5, registro.getMartes());
             procedimiento.setInt(6, registro.getMiercoles());
             procedimiento.setInt(7, registro.getJueves());
             procedimiento.setInt(8, registro.getViernes());
             procedimiento.execute();
         }catch(Exception e){
             e.printStackTrace();
         }
     }
     
     public void eliminarHorarios(){
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
                if (tblHorarios.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Esta seguro de eliminar este registro?", "Eliminar Horario", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_OPTION){
                        try{
                           PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarHorarios(?)}");
                           procedimiento.setInt(1, ((Horarios)tblHorarios.getSelectionModel().getSelectedItem()).getCodigoHorario());
                           procedimiento.execute();
                           listaHorarios.remove(tblHorarios.getSelectionModel().getFocusedIndex());
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
         Horarios registro = new Horarios();
         registro.setHorarioInicio(txtHoraEntrada.getText());
         registro.setHorarioSalida(txtHoraSalida.getText());
         registro.setLunes(Integer.parseInt(txtLunes.getText()));
         registro.setMartes(Integer.parseInt(txtMartes.getText()));
         registro.setMiercoles(Integer.parseInt(txtMiercoles.getText()));
         registro.setJueves(Integer.parseInt(txtJueves.getText()));
         registro.setViernes(Integer.parseInt(txtViernes.getText()));
         try{
             PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarHorarios(?,?,?,?,?,?,?)}");
             procedimiento.setString(1, registro.getHorarioInicio());
             procedimiento.setString(2, registro.getHorarioSalida());
             procedimiento.setInt(3, registro.getLunes());
             procedimiento.setInt(4, registro.getMartes());
             procedimiento.setInt(5, registro.getMiercoles());
             procedimiento.setInt(6, registro.getJueves());
             procedimiento.setInt(7, registro.getViernes());
             procedimiento.execute();
             listaHorarios.add(registro);
         }catch(Exception e){
             e.printStackTrace();
         }
     }
     
    public void desactivarControles(){
        txtHoraEntrada.setEditable(false);
        txtHoraSalida.setEditable(false);
        txtLunes.setEditable(false);
        txtMartes.setEditable(false);
        txtMiercoles.setEditable(false);
        txtJueves.setEditable(false);
        txtViernes.setEditable(false);
    }
    
    public void activarControles(){
        txtHoraEntrada.setEditable(true);
        txtHoraSalida.setEditable(true);
        txtLunes.setEditable(true);
        txtMartes.setEditable(true);
        txtMiercoles.setEditable(true);
        txtJueves.setEditable(true);
        txtViernes.setEditable(true);
    }
    
    public void limpiarControles(){
        txtHoraEntrada.setText("");
        txtHoraSalida.setText("");
        txtLunes.setText("");
        txtMartes.setText("");
        txtMiercoles.setText("");
        txtJueves.setText("");
        txtViernes.setText("");
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
