package provade.tools.gui;

import java.io.File;
import java.io.IOException;

import net.sf.jsqlparser.JSQLParserException;
import provade.tools.script.ToolUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class BackupTab extends Tab {
	public static final String tabTitle = "Gen Export/Import";
	public static final String selectFileText = "...";
	public static final String genText = "Generate";
	public static final String srcLblText = "Source File";
	
	private GridPane grid;
	private FileChooser sourceFileChooser;
	private File sourceFile;
	private Stage stage;
	private TextField sourcePathText;
	private Label sourcePathLabel;
	private Button selectSource;

	public BackupTab(Stage stage) {
		super();
		this.stage = stage;
		this.initGui();
	}
	
	private void initGui() {
		sourceFileChooser = new FileChooser();
		sourcePathText = new TextField();
		sourcePathLabel = new Label(BackupTab.srcLblText);
		
        selectSource = new Button();
        selectSource.setText(BackupTab.selectFileText);
        selectSource.setOnAction(e -> {
        	sourceFile = sourceFileChooser.showOpenDialog(stage);
        	sourcePathText.setText(sourceFile.getAbsolutePath());
        });
        sourcePathLabel.setLabelFor(selectSource);
        
        Button doBtn = new Button();
        doBtn.setText(BackupTab.genText);
        doBtn.setOnAction(e -> {
        	try {
        		sourceFile = new File(sourcePathText.getText());
        		if ((sourceFile == null) || (!sourceFile.exists())) {
        			throw new IOException("Source File needed");
        		}
        		File newFile = ToolUtils.CreateFileFromSource(sourceFile);
				ToolUtils.WriteBackupToFile(newFile, sourceFile);
				new ScriptSuccess("Success", "Saved as " + newFile.getAbsolutePath());
			} catch (IOException e1) {
				e1.printStackTrace();
				new ScriptError("File error", e1.getMessage());
			} catch (JSQLParserException e2) {
				e2.printStackTrace();
				new ScriptError("Parsing error", e2.getMessage());
			}
        });
		
        grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.add(sourcePathLabel, 0, 1);
        grid.add(selectSource, 1, 2);
        grid.add(doBtn, 0, 4);
        grid.add(sourcePathText, 0, 2);
        
        this.setText(BackupTab.tabTitle);
        this.setContent(grid);
	}
}
