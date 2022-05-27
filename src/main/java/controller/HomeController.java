package controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Actos;
import model.Papel;
import service.DatasourceConnection;
import utils.LabelUtils;
import utils.QueryUtils;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

public class HomeController extends LabelUtils implements Initializable {
    private int logId = 0;
    private List<Actos> actosDuplicadosList = new ArrayList<>();
    private List<Actos> actosPorNombreList = new ArrayList<>();

    private List<Papel> papelesDuplicadosList = new ArrayList<>();
    private List<Papel> papelPorNombreList = new ArrayList<>();
    private Connection connection = null;
    @FXML
    private Button btnRemoverActos;
    @FXML
    private Button btnRemoverPapeles;
    @FXML
    private Text lblConexion;
    @FXML
    private TextArea txaLog;
    @FXML
    private Hyperlink linkEditarParams;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        disableControls(true);
        System.out.println("init");
        init();
        //btnLoadActos.setOnAction(this::loadActosDuplicados);
        btnRemoverActos.setOnAction(e -> {
            disableControls(true);
            new Thread(() -> {
                moverYeliminarActosDuplicados();
            }).start();
        });

        btnRemoverPapeles.setOnAction(e -> {
            disableControls(true);
            new Thread(() -> {
                moverYeliminarPapelesDuplicados();
            }).start();
        });
        linkEditarParams.setOnAction(e->{
            redirectToLogin();
        });
    }
    private void disableControls(boolean disable){
        btnRemoverActos.setDisable(disable);
        btnRemoverPapeles.setDisable(disable);
        linkEditarParams.setDisable(disable);
    }
    private void moverYeliminarPapelesDuplicados() {
        logId = 0;
        papelesDuplicadosList.clear();
        papelPorNombreList.clear();
        try {
            ResultSet papelesSet = DatasourceConnection.getPapelesDuplicados();
            addLog("Buscando papeles duplicados");
            if (papelesSet == null) {
                addLog("ERROR de DB!");
                return;
            }
            while (papelesSet.next()) {
                Papel papel = new Papel();
                papel.setRepeticiones(papelesSet.getInt(1));
                papel.setPapel(papelesSet.getString(2));
                papel.setActo(papelesSet.getString(3));
                papel.setCodActo(papelesSet.getInt(4));
                papel.setLibro(papelesSet.getString(5));
                papel.setCodLibro(papelesSet.getInt(6));
                papelesDuplicadosList.add(papel);
            }
            if (papelesDuplicadosList.isEmpty()) {
                addLog("No se encontraron papeles duplicados");
                return;
            }
            int totalPapelesDuplicados = getTotalPapelesDuplicados(papelesDuplicadosList);
            addLog(String.format("Se han encontado %d papeles duplicados", totalPapelesDuplicados));
            for (Papel papelesDuplicados : papelesDuplicadosList) {
                papelPorNombreList.clear();
                ResultSet papelPorNombreSet = DatasourceConnection.
                        getPapelDuplicadoPorNombre(papelesDuplicados.getPapel(), papelesDuplicados.getCodActo());
                if (papelPorNombreSet == null) {
                    addLog("ERROR de DB!");
                    return;
                }
                while (papelPorNombreSet.next()) {
                    Papel papel = new Papel();
                    papel.setCodPapel(papelPorNombreSet.getInt(1));
                    papel.setPapel(papelPorNombreSet.getString(2));
                    papel.setCodActo(papelPorNombreSet.getInt(3));
                    papel.setActo(papelPorNombreSet.getString(4));
                    papel.setCantIntervinientes(papelPorNombreSet.getInt(5));
                    papelPorNombreList.add(papel);
                }
                Papel papelOriginal = papelPorNombreList.get(0);
                papelPorNombreList.remove(0);
                for (Papel papelDuplicado : papelPorNombreList) {
                    int updateIntervCode = DatasourceConnection.
                            moverPapelDuplicados(papelOriginal.getCodPapel(), papelDuplicado.getCodPapel());
                    int eliminarPapelCode = DatasourceConnection.
                            eliminarPapelDuplicado(papelDuplicado.getCodPapel());
                    addLog(String.format("Intervinientes actualizados: [%d] | Papel [%s] [%d] del " +
                                    "Acto [%s] ha sido eliminado", updateIntervCode, papelDuplicado.getPapel(),
                            papelDuplicado.getCodPapel(), papelDuplicado.getActo()));
                }
            }
            addLog(String.format("HECHO! Se han eliminado [%d] papeles duplicados", totalPapelesDuplicados));

        } catch (Exception ex) {
            addLog("ERROR: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            Platform.runLater(() -> {
                disableControls(false);
                linkEditarParams.setDisable(true);
            });
        }
    }

    private void moverYeliminarActosDuplicados() {
        actosDuplicadosList.clear();
        actosPorNombreList.clear();
        try {
            ResultSet actosSetDuplicados = DatasourceConnection.getDuplicatesActos();
            addLog("Buscando actos duplicados...");
            if (actosSetDuplicados == null) {
                addLog("ERROR de DB!");
                return;
            }
            while (actosSetDuplicados.next()) {
                Actos actos = new Actos();
                actos.setActo(actosSetDuplicados.getString(1));
                actos.setCodLibro(actosSetDuplicados.getInt(2));
                actos.setLibro(actosSetDuplicados.getString(3));
                actos.setRepeticiones(actosSetDuplicados.getInt(4));
                actosDuplicadosList.add(actos);
            }
            if (actosDuplicadosList.isEmpty()) {
                addLog("No se encontraron actos duplicados");
                return;
            }
            int totalActosDuplicado = getTotalActosDuplicados(actosDuplicadosList);
            addLog(String.format("Se han encontado %d actos duplicados", totalActosDuplicado));
            for (Actos actosDuplicados : actosDuplicadosList) {
                actosPorNombreList.clear();
                ResultSet actosSet = DatasourceConnection.
                        getActosByName(actosDuplicados.getActo(), actosDuplicados.getCodLibro());
                if (actosSet == null) {
                    addLog("ERROR de DB!");
                    return;
                }
                while (actosSet.next()) {
                    Actos actos = new Actos();
                    actos.setCodActo(actosSet.getInt(1));
                    actos.setActo(actosSet.getString(2));
                    actos.setCodLibro(actosSet.getInt(3));
                    actos.setLibro(actosSet.getString(4));
                    actos.setPapelCantidad(actosSet.getInt(5));
                    actos.setInscripcionesCantidad(actosSet.getInt(6));
                    actosPorNombreList.add(actos);
                }//se llena la lista con los actos duplicados
                Actos actoOriginal = actosPorNombreList.get(0); //se obtiene el primero / el acto original
                actosPorNombreList.remove(0); // removemos el acto original de la lista
                for (Actos actoDuplicado : actosPorNombreList) {
                    int updatePapelCode = DatasourceConnection.moverActosDuplicados(actoOriginal.getCodActo(), actoDuplicado.getCodActo(), QueryUtils.MOVER_PAPELES_ACTOS_DUPLICADOS);
                    int updateInscCode = DatasourceConnection.moverActosDuplicados(actoOriginal.getCodActo(), actoDuplicado.getCodActo(), QueryUtils.MOVER_INSCRIPCIONES_ACTOS_DUPLICADOS);
                    int deleteCode = DatasourceConnection.eliminarActosDuplicados(actoDuplicado.getCodActo());
                    addLog(String.format("Acto [%d] asignado a [%d] inscripciones ha sido cambiado por el Acto [%d] - Libro [%s]",
                            actoDuplicado.getCodActo(), actoDuplicado.getInscripcionesCantidad(),
                            actoOriginal.getCodActo(), actoOriginal.getLibro()) + "\n" +
                            String.format("Papeles actualizados: [%d] | Inscripciones actualizadas: [%d] | Acto [%d - %s] eliminado",
                                    updatePapelCode, updateInscCode, actoDuplicado.getCodActo(), actoDuplicado.getActo()));
                    //executeUpdate returns amount or rows updated
                }
            }
            addLog(String.format("HECHO! Se han eliminado [%d] actos duplicados", totalActosDuplicado));
        } catch (Exception ex) {
            Platform.runLater(() -> {
                addLog("ERROR: " + ex.getMessage());
            });
            ex.printStackTrace();
        } finally {
            Platform.runLater(() -> {
                disableControls(false);
                linkEditarParams.setDisable(true);
            });
        }
    }

    private void init(){
        new Thread(()->{
            HashMap params = getConexionParams();
            if (params == null){
                Platform.runLater(()->{
                    lblConexion.setText("ERROR");
                });
                return;
            }
            String servidor = ""+params.get(SERVER_KEY);
            String bd = ""+params.get(BD_KEY);
            String user = ""+params.get(USER_KEY);
            String pwd = ""+params.get(PWD_KEY);

            if (servidor.isEmpty() || bd.isEmpty() || user.isEmpty() || pwd.isEmpty()){
                Platform.runLater(()->{
                    lblConexion.setText("ERROR con los parámetros");
                    toast("Error", "Ninguno de los parámetros de conexión puede ser vacío\n", Alert.AlertType.ERROR);
                });
                return;
            }
            String jdbcUrl = String.format("jdbc:sqlserver://%s;database=%s;", servidor, bd);
            startDBConnection(jdbcUrl, user, pwd);

        }).start();
    }

    private void startDBConnection(String jdbcUrl, String user, String pwd) {
        try {
            DatasourceConnection.jdbcUrl = jdbcUrl;
            DatasourceConnection.user = user;
            DatasourceConnection.pwd = pwd;
            connection = DatasourceConnection.createConnection();
            Platform.runLater(() -> {
                lblConexion.setText("Conectado a la BD");
                disableControls(false);
                linkEditarParams.setDisable(true);
            });

        } catch (Exception ex) {
            ex.printStackTrace();
            Platform.runLater(() -> {
                lblConexion.setText("Error de conexión!");
                linkEditarParams.setDisable(false);
                txaLog.setText(ex.getMessage());
            });
        }
    }

    private int getTotalActosDuplicados(List<Actos> actos) {
        int sum = actos.stream().mapToInt(Actos::getRepeticiones).sum();
        return sum - actos.size();
    }

    private int getTotalPapelesDuplicados(List<Papel> papelList) {
        int sum = papelList.stream().mapToInt(Papel::getRepeticiones).sum();
        return sum - papelList.size();
    }

    private void addLog(String msj) {
        Platform.runLater(() -> {
            String log = String.format("%d. %s \n%s", logId, msj, txaLog.getText());
            txaLog.setText(log);
            logId++;
        });
    }
    public void redirectToLogin(){
        DatasourceConnection.shutdownConnection();
        try {
            Stage stage = (Stage) btnRemoverPapeles.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ui/login.fxml"));
            Parent root = fxmlLoader.load();
            stage.setTitle(LabelUtils.APP_LOGIN_TITLE);
            stage.setScene(new Scene(root));
            stage.show();
//            ((Stage) btnRemoverPapeles.getScene().getWindow()).close();
        }catch (Exception ex){
            ex.printStackTrace();
            LabelUtils.toast("!", ex.getMessage(), Alert.AlertType.ERROR);
        }
    }
}
