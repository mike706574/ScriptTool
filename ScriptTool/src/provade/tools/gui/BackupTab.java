package provade.tools.gui;

import java.io.File;
import java.io.IOException;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.ParseException;
import provade.tools.script.Script;
import provade.tools.script.ToolUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class BackupTab extends Tab {
	public static final String tabTitle = "Gen Export/Import";
	public static final String selectFileText = "...";
	public static final String genText = "Generate";
	public static final String srcLblText = "Source File";
	
	private ToolFileChooser fChooser;
	private FlowPane flow;
	private File sourceFile;
	private Stage stage;

	public BackupTab(Stage stage) {
		super();
		this.stage = stage;
		this.initGui();
	}
	
	private void alterFile() {
    	try {
    		sourceFile = fChooser.getFile();
    		Script script = new Script(sourceFile);
    		File newFile = ToolUtils.CreateFileFromSource(sourceFile);
			ToolUtils.WriteBackupToFile(newFile, script);
			new ScriptSuccess("Success", "Saved as " + newFile.getAbsolutePath());
		} catch (IOException e1) {
			e1.printStackTrace();
			new ScriptError("File error", e1.getMessage());
		} catch (JSQLParserException e2) {
			e2.printStackTrace();
			new ScriptError("Parsing error", e2.getCause().getMessage());
			
		}		
	}
	
	private void initGui() {       
        Button doBtn = new Button();
        doBtn.setText(BackupTab.genText);
        doBtn.setOnAction(e -> this.alterFile());
        
        EventHandler<ActionEvent> doEvent = e -> this.alterFile();
        fChooser = new ToolFileChooser(this.stage, BackupTab.srcLblText, BackupTab.selectFileText, BackupTab.genText, doEvent);
		
        flow = new FlowPane();
        flow.setVgap(8);
        flow.setHgap(4);
        flow.setPrefWrapLength(300);
        flow.getChildren().add(fChooser.getPane());
        
        this.setText(BackupTab.tabTitle);
        this.setContent(flow);
	}
}
