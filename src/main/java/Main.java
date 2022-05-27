import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import service.DatasourceConnection;
import utils.LabelUtils;

import java.io.File;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        String propPath = getClass().getResource("disabled_tlsv1.properties").toString();
        System.setProperty("java.security.properties",propPath);
        //check for Params File if exists
        File paramsPath = new File(LabelUtils.DB_PROPERTIES_FILEPATH);
        String defScreen = paramsPath.exists() ? "ui/home.fxml" : "ui/login.fxml";
        FXMLLoader load = new FXMLLoader(getClass().getResource(defScreen));
        Parent root = load.load();

        primaryStage.setTitle(LabelUtils.APP_HOME_TITLE);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        primaryStage.setOnCloseRequest(e->{
            DatasourceConnection.shutdownConnection();
            System.exit(0);
        });
    }
    public static void main(String[] args) {
        launch(args);
    }
}
