package provade.tools.gui;

import java.io.File;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ToolFileChooser {
	private FileChooser fChooser;
	private TextField pathText;
	private Label label;
	private Stage stage;
	private Button chooseBtn;
	private Button doBtn;
	private EventHandler<ActionEvent> doAction;

	public ToolFileChooser(Stage stage, String fieldLabel, String chooseBtnLabel, String doBtnLabel, EventHandler<ActionEvent> value) {
		this.stage = stage;
		fChooser = new FileChooser();
		pathText = new TextField();
		label = new Label(fieldLabel);
		chooseBtn = new Button(chooseBtnLabel);
		doBtn = new Button(doBtnLabel);
		doAction = value;
		this.init();
	}
	
	public File getFile() throws IOException {
		File sourceFile = new File(this.getPathText());
		if ((sourceFile == null) || (!sourceFile.exists())) {
			throw new IOException("Source File needed");
		}
		
		return sourceFile;
	}
	
	public String getPathText() {
		return pathText.getText();
	}
	
	public void setDoBtnAction(EventHandler<ActionEvent> value) {
		doAction = value;
	}
	
	public Pane getPane() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.add(label, 0, 1);
        grid.add(chooseBtn, 1, 2);
        grid.add(doBtn, 0, 4);
        grid.add(pathText, 0, 2);
		return grid;
	}
	
	private void init() {
		label.setLabelFor(pathText);
		chooseBtn.setOnAction(e -> {
        	File sourceFile = fChooser.showOpenDialog(stage);
        	if (sourceFile != null) {
        		pathText.setText(sourceFile.getAbsolutePath());
        	}
        });
		
        doBtn.setOnAction(this.doAction);
	}
}
