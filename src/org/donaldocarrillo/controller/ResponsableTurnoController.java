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
import org.donaldocarrillo.bean.Area;
import org.donaldocarrillo.bean.Cargos;
import org.donaldocarrillo.bean.ResponsableTurno;
import org.donaldocarrillo.db.Conexion;
import org.donaldocarrillo.sistema.Principal;


public class ResponsableTurnoController implements Initializable{
    private enum operaciones{AGREGAR, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR,CANCELAR,NINGUNO};
    private Principal escenarioPrincipal;
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<ResponsableTurno> listaResponsableTurno;
    private ObservableList<Area> listaArea;
    private ObservableList<Cargos> listaCargo;
    @FXML private TextField txtNombreResponsableTurno;
    @FXML private TextField txtApellidosResponsableTurno;
    @FXML private TextField txtTelefonoPersonal;
    @FXML private ComboBox cmbCodArea;
    @FXML private ComboBox cmbCodCargo;
    @FXML private TableView tblResponsableTurno;
    @FXML private TableColumn colCodResponsableTurno;
    @FXML private TableColumn colNombreResponsableTurno;
    @FXML private TableColumn colApellidosResponsableTurno;
    @FXML private TableColumn colTelefonoPersonal;
    @FXML private TableColumn colCodArea;
    @FXML private TableColumn colCodCargo;
    @FXML private Button btnAgregar;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReportes;
 
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbCodArea.setItems(getArea());
        cmbCodCargo.setItems(getCargos());
    }
    
    public void cargarDatos(){
        tblResponsableTurno.setItems(getResponsableTurno());
        colCodResponsableTurno.setCellValueFactory(new PropertyValueFactory<ResponsableTurno, Integer>("codigoResponsableTurno"));
        colNombreResponsableTurno.setCellValueFactory(new PropertyValueFactory<ResponsableTurno, String>("nombreResponsable"));
        colApellidosResponsableTurno.setCellValueFactory(new PropertyValueFactory<ResponsableTurno, String>("apellidosResponsable"));
        colTelefonoPersonal.setCellValueFactory(new PropertyValueFactory<ResponsableTurno, String>("telefonoPersonal"));
        colCodArea.setCellValueFactory(new PropertyValueFactory<ResponsableTurno, Integer>("codigoArea"));
        colCodCargo.setCellValueFactory(new PropertyValueFactory<ResponsableTurno, Integer>("codigoCargo"));
    }
    
    public ObservableList<ResponsableTurno> getResponsableTurno(){
        ArrayList<ResponsableTurno> lista = new ArrayList<ResponsableTurno>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarResponsableTurno()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
               lista.add(new ResponsableTurno(resultado.getInt("codigoResponsableTurno"),
                                              resultado.getString("nombreResponsable"),
                                              resultado.getString("apellidosResponsable"),
                                              resultado.getString("telefonoPersonal"),
                                              resultado.getInt("codigoArea"),
                                              resultado.getInt("codigoCargo")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaResponsableTurno = FXCollections.observableList(lista);
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
    
    public ObservableList <Cargos> getCargos(){
        ArrayList<Cargos> lista = new ArrayList<Cargos>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarCargos()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Cargos(resultado.getInt("codigoCargo"),
                                     resultado.getString("nombreCargo")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaCargo = FXCollections.observableList(lista);
    }
    
    public void seleccionarElementos(){
        txtNombreResponsableTurno.setText(((ResponsableTurno)tblResponsableTurno.getSelectionModel().getSelectedItem()).getNombreResponsable());
        txtApellidosResponsableTurno.setText(((ResponsableTurno)tblResponsableTurno.getSelectionModel().getSelectedItem()).getApellidosResponsable());
        txtTelefonoPersonal.setText(((ResponsableTurno)tblResponsableTurno.getSelectionModel().getSelectedItem()).getTelefonoPersonal());
        cmbCodArea.getSelectionModel().select(buscarArea(((ResponsableTurno)tblResponsableTurno.getSelectionModel().getSelectedItem()).getCodigoArea()));
        cmbCodCargo.getSelectionModel().select(buscarCargo(((ResponsableTurno)tblResponsableTurno.getSelectionModel().getSelectedItem()).getCodigoCargo()));
    }
    
    public ResponsableTurno buscarResponsableTurno(int codigoResponsableTurno){
        ResponsableTurno resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarResponsableTurno(?)}");
            procedimiento.setInt(1, codigoResponsableTurno);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new ResponsableTurno(registro.getInt("codigoResponsableTurno"),
                                                 registro.getString("nombreResponsable"),
                                                 registro.getString("apellidosResponsable"),
                                                 registro.getString("telefonoPersonal"),
                                                 registro.getInt("codigoArea"),
                                                 registro.getInt("codigoCargo"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
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
    
    public Cargos buscarCargo(int codigoCargo){
        Cargos resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarCargo(?)}");
            procedimiento.setInt(1, codigoCargo);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Cargos(registro.getInt("codigoCargo"),
                                      registro.getString("nombreCargo"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
    
    public void editarResponsableTurno(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if (tblResponsableTurno.getSelectionModel().getSelectedItem() != null){
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
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EditarResponsableTurno(?,?,?,?,?,?)}");
            ResponsableTurno registro = (ResponsableTurno)tblResponsableTurno.getSelectionModel().getSelectedItem();
            registro.setNombreResponsable(txtNombreResponsableTurno.getText());
            registro.setApellidosResponsable(txtApellidosResponsableTurno.getText());
            registro.setTelefonoPersonal(txtTelefonoPersonal.getText());
            registro.setCodigoArea(((Area)cmbCodArea.getSelectionModel().getSelectedItem()).getCodigoArea());
            registro.setCodigoCargo(((Cargos)cmbCodCargo.getSelectionModel().getSelectedItem()).getCodigoCargo());
            procedimiento.setInt(1, registro.getCodigoResponsableTurno());
            procedimiento.setString(2, registro.getNombreResponsable());
            procedimiento.setString(3, registro.getApellidosResponsable());
            procedimiento.setString(4, registro.getTelefonoPersonal());
            procedimiento.setInt(5, registro.getCodigoArea());
            procedimiento.setInt(6, registro.getCodigoCargo());
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
     public void eliminarResponsableTurno(){
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
                if (tblResponsableTurno.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "Esta seguro de eliminar este registro?", "Eliminar Responsable Turno", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarResponsableTurno(?)}");
                            procedimiento.setInt(1, ((ResponsableTurno)tblResponsableTurno.getSelectionModel().getSelectedItem()).getCodigoResponsableTurno());
                            procedimiento.execute();
                            listaResponsableTurno.remove(tblResponsableTurno.getSelectionModel().getFocusedIndex());
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
         ResponsableTurno registro = new ResponsableTurno();
         registro.setNombreResponsable(txtNombreResponsableTurno.getText());
         registro.setApellidosResponsable(txtApellidosResponsableTurno.getText());
         registro.setTelefonoPersonal(txtTelefonoPersonal.getText());
         registro.setCodigoArea(((Area)cmbCodArea.getSelectionModel().getSelectedItem()).getCodigoArea());
         registro.setCodigoCargo(((Cargos)cmbCodCargo.getSelectionModel().getSelectedItem()).getCodigoCargo());
         try{
             PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarResponsableTurno(?,?,?,?,?)}");
             procedimiento.setString(1, registro.getNombreResponsable());
             procedimiento.setString(2, registro.getApellidosResponsable());
             procedimiento.setString(3, registro.getTelefonoPersonal());
             procedimiento.setInt(4, registro.getCodigoArea());
             procedimiento.setInt(5, registro.getCodigoCargo());
             procedimiento.execute();
             listaResponsableTurno.add(registro);
         }catch(Exception e){
             e.printStackTrace();
         }
     }
     
    public void desactivarControles(){
        txtNombreResponsableTurno.setEditable(false);
        txtApellidosResponsableTurno.setEditable(false);
        txtTelefonoPersonal.setEditable(false);
        cmbCodArea.setEditable(true);
        cmbCodCargo.setEditable(true);
    }
    
    public void activarControles(){
        txtNombreResponsableTurno.setEditable(true);
        txtApellidosResponsableTurno.setEditable(true);
        txtTelefonoPersonal.setEditable(true);
        cmbCodArea.setEditable(false);
        cmbCodCargo.setEditable(false);
    }
    
    public void limpiarControles(){
        txtNombreResponsableTurno.setText("");
        txtApellidosResponsableTurno.setText("");
        txtTelefonoPersonal.setText("");
        cmbCodArea.getSelectionModel().clearSelection();
        cmbCodCargo.getSelectionModel().clearSelection();
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
