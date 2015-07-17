package provade.tools.gui;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import net.sf.jsqlparser.JSQLParserException;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import provade.tools.script.Script;
import provade.tools.script.ToolUtils;
import provade.tools.template.Template;
import provade.tools.template.UserInput;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;

/*
 * TODO: 
 *  1) Create my own exception for the controllers to catch, will be thrown in classes like script that is more informative
 *  2) add a delete option on the current statements pane to delete individual statements, or a clear all button
 *  3) modify gui so that the current statements pane will resize the text boxes based on input length
 */
public class BuildScriptController {
	
	public List<TextField> stmtList;
	private Stage stage;
	private InputGroup inputGroup;
	
    @FXML
    private ResourceBundle resources;
    @FXML
    private URL location;
    @FXML
    private Button addStmtsBtn;
    @FXML
    private VBox currStmtsPane;
    @FXML
    private VBox templateInputsPane;
    @FXML
    private MenuItem loadScriptMenu;
    @FXML
    private MenuItem loadTemplateMenu;
    @FXML
    private MenuItem exportBackupMenu;
    @FXML
    private MenuBar mainMenu;


    @FXML
    void initialize() {
        assert addStmtsBtn != null : "fx:id=\"addStmtsBtn\" was not injected: check your FXML file 'scriptToolScene.fxml'.";
        assert currStmtsPane != null : "fx:id=\"currStmtsPane\" was not injected: check your FXML file 'scriptToolScene.fxml'.";
        assert loadScriptMenu != null : "fx:id=\"loadScriptMenu\" was not injected: check your FXML file 'scriptToolScene.fxml'.";
        assert loadTemplateMenu != null : "fx:id=\"loadTemplateMenu\" was not injected: check your FXML file 'scriptToolScene.fxml'.";
        assert mainMenu != null : "fx:id=\"mainMenu\" was not injected: check your FXML file 'scriptToolScene.fxml'.";
        assert templateInputsPane != null : "fx:id=\"templateInputsPane\" was not injected: check your FXML file 'scriptToolScene.fxml'.";
        assert exportBackupMenu != null : "fx:id=\"exportBackupMenu\" was not injected: check your FXML file 'scriptToolScene.fxml'.";

        this.loadTemplateMenu.setOnAction(e -> loadTemplate());
        this.exportBackupMenu.setOnAction(e -> exportBackup());
        this.loadScriptMenu.setOnAction(e -> loadExistingScript());
        this.addStmtsBtn.setOnAction(e -> addStmtFromInput());
        this.inputGroup = new InputGroup();
        this.stmtList = new LinkedList<TextField>();
    }
    
    public void setStage(Stage stage) {
    	this.stage = stage;
    }
    
    //TODO: if file already exists, ask if user wants to append to the file and append new statements to end
    private void exportBackup() {
		try {
			FileChooser fChooser = new FileChooser();
			fChooser.getExtensionFilters().addAll(new ExtensionFilter("dms", "*.dms"));
			fChooser.setTitle("Save as");
			File saveAsFile = fChooser.showSaveDialog(this.stage);
			if (saveAsFile != null) {
				List<String> statements = new ArrayList<String>(stmtList.size());
				stmtList.forEach(e -> statements.add(e.getText()));
				Script script = ToolUtils.CreateScriptFromList(statements);
				ToolUtils.WriteBackupToFile(saveAsFile, script);
				new ScriptSuccess("Success", "Script created: " + saveAsFile.getAbsolutePath());
			}
		} catch (JSQLParserException e) {
			e.printStackTrace();
			new ScriptError("Error", e.getCause().getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			new ScriptError("Error", e.getMessage());
		}	
    }
    
    private void loadExistingScript() {
    	try {
    		File sourceFile = this.loadFile();
    		Script script = new Script(sourceFile);
    		for(String s : script.currentStmtStrings) {
    			this.addStmt(s);
    		}
		} catch (IOException e1) {
			e1.printStackTrace();
			new ScriptError("File error", e1.getMessage());
		} catch (JSQLParserException e2) {
			e2.printStackTrace();
			new ScriptError("Parsing error", e2.getCause().getMessage());
		}
    }
    
    private void addStmt(String stmt) {
		TextField stmtText = new TextField(stmt);
		stmtList.add(stmtText);
		currStmtsPane.getChildren().add(stmtText);
    }
    
    private void addStmtFromInput() {
    	inputGroup.setValues();
    	try {
			String currStmt = ToolUtils.getStatementFromTemplate(inputGroup.template).toString();
			this.addStmt(currStmt);
		} catch (JSQLParserException e) {
			e.printStackTrace();
			new ScriptError("Parsing error", e.getCause().getMessage());
		}
    }
    
    private void loadTemplate() {
    	File templateFile = this.loadFile();
    	if (templateFile != null) {
			Serializer serialize = new Persister();
			try {
				Template template = serialize.read(Template.class, templateFile);
				this.createInputs(template, templateInputsPane);
			} catch (Exception e) {
				e.printStackTrace();
				new ScriptError("Template Error", e.getMessage());
			}
    	}
    }
    
	private void createInputs(Template template, Pane pane) {
		List<UserInput> userInputs = template.inputs;
		for (UserInput i : userInputs) {
			TextField txtField = new TextField();
			Label lbl = new Label(i.label);
			lbl.setLabelFor(txtField);
			TemplateInputGroup inGroup = new TemplateInputGroup(i.id, txtField, lbl);
			pane.getChildren().add(inGroup);
			inputGroup.addInput(inGroup);
		}
		inputGroup.template = template;
	}
    
    private File loadFile() {
    	FileChooser fChooser = new FileChooser();
    	return fChooser.showOpenDialog(stage);
    }
}