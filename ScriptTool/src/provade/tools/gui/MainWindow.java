package provade.tools.gui;

import java.io.File;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MainWindow extends Application {
	public Stage mainStage;
	public DirectoryChooser chooser;
	public FileChooser sourceFileChooser;
	public File directory;
	public String sourceFilePath;
	public String newFilePath;
	
	@Override
	public void start(Stage arg0) throws Exception {
		mainStage = arg0;
		mainStage.setTitle("Provade Script Tool");

        TabPane tabPane = new TabPane();
        tabPane.getTabs().add(new BackupTab(mainStage));

        Scene scene = new Scene(tabPane, 300, 275);
        mainStage.setScene(scene);
        mainStage.show();
	}

}
