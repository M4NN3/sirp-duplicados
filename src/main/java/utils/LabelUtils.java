package utils;

import javafx.scene.control.Alert;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class LabelUtils {
    public static final String APP_NAME = "SIRP - Remover actos y papeles duplicados";
    public static final String SUB_APP_NAME = "dev by Manne Rodriguez";
    public static final String APP_LOGIN_TITLE = String.format("%s | %s", APP_NAME, "Parámetros de conexión");
    public static final String APP_VERSION_TAG = "1.0.2";
    public static final String APP_HOME_TITLE = String.format("%s | v%s | %s", APP_NAME,APP_VERSION_TAG, SUB_APP_NAME);
    public static final int APP_VERSION_CODE = 102;
    public static final String MASTER_PASSWORD = "mannerod123";

    public static final String DB_PROPERTIES_FILEPATH = System.getProperty("user.home")
            + File.separator + "documents"
            + File.separator + "sirp-actos-papeles.manne";
    public static final String SERVER_KEY = "servidor";
    public static final String BD_KEY = "bd";
    public static final String USER_KEY = "usuario";
    public static final String PWD_KEY = "password";

    public static void toast(String title, String msg, Alert.AlertType alertTye){
        Alert alert = new Alert(alertTye);
        alert.setTitle(String.format("%s", title));
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.initStyle(StageStyle.UTILITY);
        alert.showAndWait();
    }
    public static HashMap<String, String> getConexionParams(){
        HashMap<String, String> params = new HashMap<String, String>();
        File storedProp = new File(LabelUtils.DB_PROPERTIES_FILEPATH);
        if (!storedProp.exists()) {
            return null;
        }
        Properties prop = new Properties();
        InputStream in = null;
        try {
            in = new FileInputStream(storedProp);
            prop.load(in);
            String servidor =prop.getProperty(SERVER_KEY, "");
            String bd =prop.getProperty(BD_KEY, "");
            String usuario =prop.getProperty(USER_KEY, "");
            String password =prop.getProperty(PWD_KEY, "");

            params.put(SERVER_KEY, EncriptarTexto.decrptText(servidor));
            params.put(BD_KEY, EncriptarTexto.decrptText(bd));
            params.put(USER_KEY, EncriptarTexto.decrptText(usuario));
            params.put(PWD_KEY, EncriptarTexto.decrptText(password));

            //params.add(EncriptarTexto.decrptText())
        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException io) {
                    io.printStackTrace();
                }
            }
        }
        return params;
    }
}
