package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import service.DatasourceConnection;
import utils.EncriptarTexto;
import utils.LabelUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

public class LoginController extends LabelUtils implements Initializable {
    @FXML
    private TextField txtServidor;
    @FXML
    private TextField txtBD;
    @FXML
    private TextField txtUsuario;
    @FXML
    private TextField txtPassword;
    @FXML
    private Button btnGuardar;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnGuardar.setOnAction(event -> {
            if (!isAllGood())
                return;
            saveConnectionParams();
        });
        btnGuardar.setDefaultButton(true);
    }

    private boolean isAllGood(){
        String servidor = txtServidor.getText();
        String bd = txtBD.getText();
        String usuario = txtUsuario.getText();
        String password = txtPassword.getText();
        if (servidor.isEmpty()){
            toast("Parámetro vacío", "Ingrese un nombre de servidor", Alert.AlertType.WARNING);
            txtServidor.requestFocus();
            return false;
        }
        if (bd.isEmpty()){
            toast("Parámetro vacío", "Ingrese un nombre de base de datos", Alert.AlertType.WARNING);
            txtBD.requestFocus();
            return false;
        }
        if (usuario.isEmpty()){
            toast("Parámetro vacío", "Ingrese un nombre de usuario", Alert.AlertType.WARNING);
            txtUsuario.requestFocus();
            return false;
        }
        if (password.isEmpty()){
            toast("Parámetro vacío", "Ingrese una contraseña", Alert.AlertType.WARNING);
            txtPassword.requestFocus();
            return false;
        }
        return true;
    }

    private void saveConnectionParams(){
        OutputStream outputStream = null;
        try{
            Properties prop = new Properties();
            outputStream = new FileOutputStream(new File(DB_PROPERTIES_FILEPATH));
            prop.setProperty(SERVER_KEY, EncriptarTexto.encryptText(txtServidor.getText()));
            prop.setProperty(BD_KEY, EncriptarTexto.encryptText(txtBD.getText()));
            prop.setProperty(USER_KEY, EncriptarTexto.encryptText(txtUsuario.getText()));
            prop.setProperty(PWD_KEY, EncriptarTexto.encryptText(txtPassword.getText()));
            prop.store(outputStream, SUB_APP_NAME);
            System.out.println("Parámetros de conexión guardados");
            //all good redirect to HOME
        }catch (Exception ex){
            toast("Error", "No se pudo guardar los datos de conexión", Alert.AlertType.ERROR);
            ex.printStackTrace();
        }finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            redirectToHome();
        }
    }
    private void redirectToHome(){
       try {
           Stage stage = (Stage) btnGuardar.getScene().getWindow();
           //Stage stage = new Stage();
           FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ui/home.fxml"));
           Parent root = fxmlLoader.load();
           stage.setTitle(APP_HOME_TITLE);
           stage.setScene(new Scene(root));
           stage.show();

           stage.setOnCloseRequest(e->{
               DatasourceConnection.shutdownConnection();
           });

//           Stage currentStage = (Stage) btnGuardar.getScene().getWindow();
//           currentStage.close();
       }catch (Exception ex){
           ex.printStackTrace();
           toast("!", ex.getMessage(), Alert.AlertType.ERROR);
       }
    }
//    private void closeCurrentWindow(){
//        Stage stage = (Stage) btnGuardar.getScene().getWindow();
//        stage.close();
//    }
}
