package scripttool.tools.gui;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class MainWindow extends Application {
	public Stage mainStage;
	
	@Override
	public void start(Stage arg0) throws Exception {
		mainStage = arg0;
		mainStage.setTitle("Provade Script Tool (Beta)");

        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainWindow.class.getResource("/scriptToolScene.fxml"));
            AnchorPane rootLayout = (AnchorPane) loader.load();
            Scene scene = new Scene(rootLayout, 700, 375);
            BuildScriptController sContr = loader.getController();
            sContr.setStage(mainStage);
            mainStage.setScene(scene);
            mainStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

	}

}
