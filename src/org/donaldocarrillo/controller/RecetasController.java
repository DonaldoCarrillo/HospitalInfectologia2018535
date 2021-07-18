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
import org.donaldocarrillo.bean.ControlCitas;
import org.donaldocarrillo.bean.Recetas;
import org.donaldocarrillo.db.Conexion;
import org.donaldocarrillo.sistema.Principal;

public class RecetasController implements Initializable{
    private enum operaciones{AGREGAR, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR,CANCELAR,NINGUNO};
    private Principal escenarioPrincipal;
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Recetas> listaRecetas;
    private ObservableList<ControlCitas> listaControlCitas;
    @FXML TextField txtDescripcion;
    @FXML ComboBox cmbCodControlCita;
    @FXML TableView tblReceta;
    @FXML TableColumn colCodReceta;
    @FXML TableColumn colDescripcion;
    @FXML TableColumn colCodControlCita;
    @FXML Button btnAgregar;
    @FXML Button btnEliminar;
    @FXML Button btnEditar;
    @FXML Button btnReporte;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbCodControlCita.setItems(getControlCitas());
    }

    
        public void cargarDatos(){
            tblReceta.setItems(getRecetas());
            
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
        
        public ObservableList<Recetas> getRecetas(){
        ArrayList<Recetas> lista = new ArrayList<Recetas>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarRecetas()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Recetas(resultado.getInt("codigoReceta"),
                                      resultado.getString("descripcionReceta"),
                                      resultado.getInt("codigoControlCita") ));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaRecetas = FXCollections.observableList(lista);
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
