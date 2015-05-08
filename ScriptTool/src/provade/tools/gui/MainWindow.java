package provade.tools.gui;

import java.io.File;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class MainWindow extends Application {
	public Stage mainStage;
	public DirectoryChooser chooser;
	public File directory;
	
	public MainWindow(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage arg0) throws Exception {
		mainStage = arg0;

		mainStage.setTitle("Hello World!");
        chooser = new DirectoryChooser();
        chooser.setTitle("Music directory");
        
        Button btn = new Button();
        btn.setText("Music directory");
        btn.setOnAction(e -> directory = chooser.showDialog(mainStage));
        
        Button doBtn = new Button();
        doBtn.setText("Sort");
        //doBtn.setOnAction(e -> doIt());
        
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.add(btn, 0, 1);
        grid.add(doBtn, 1, 1);

        Scene scene = new Scene(grid, 300, 275);
        mainStage.setScene(scene);
        mainStage.show();
	}

}
