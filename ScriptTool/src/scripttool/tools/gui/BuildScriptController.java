package scripttool.tools.gui;

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

import scripttool.tools.script.Script;
import scripttool.tools.script.ScriptMain;
import scripttool.tools.script.ScriptParseException;
import scripttool.tools.script.ToolUtils;
import scripttool.tools.template.Template;
import scripttool.tools.template.UserInput;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;

/*
 * TODO: 
 *  1) modify gui so that the current statements pane will resize the text boxes based on input length
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
    private Button deleteAllBtn;
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
    private MenuItem aboutMenu;
    @FXML
    private MenuItem clearAllMenu;
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
        assert deleteAllBtn != null : "fx:id=\"deleteAllBtn\" was not injected: check your FXML file 'scriptToolScene.fxml'.";
        assert clearAllMenu != null : "fx:id=\"clearAllMenu\" was not injected: check your FXML file 'scriptToolScene.fxml'.";
        assert aboutMenu != null : "fx:id=\"aboutMenu\" was not injected: check your FXML file 'scriptToolScene.fxml'.";

        this.loadTemplateMenu.setOnAction(e -> loadTemplate());
        this.exportBackupMenu.setOnAction(e -> exportBackup());
        this.loadScriptMenu.setOnAction(e -> loadExistingScript());
        this.aboutMenu.setOnAction(e -> about());
        this.addStmtsBtn.setOnAction(e -> addStmtFromInput());
        this.deleteAllBtn.setOnAction(e -> removeAllStatements());
        this.inputGroup = new InputGroup();
        this.stmtList = new LinkedList<TextField>();
    }
    
    public void setStage(Stage stage) {
    	this.stage = stage;
    }
    
    private void about() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("About");
		alert.setContentText("Provade Script Tool\nVersion " + ScriptMain.version + "\nWritten by Tim Andracek");
		alert.show();
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
		} catch (ScriptParseException e) {
			e.printStackTrace();
			new ScriptError("Error", e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			new ScriptError("Error", e.getMessage());
		}	
    }
    
    private void loadExistingScript() {
    	try {
    		File sourceFile = this.loadFile();
    		if (sourceFile != null) {
        		Script script = new Script(sourceFile);
        		for(String s : script.currentStmtStrings) {
        			this.addStmt(s);
        		}
    		}
		} catch (IOException e1) {
			e1.printStackTrace();
			new ScriptError("File error", e1.getMessage());
		} catch (ScriptParseException e2) {
			e2.printStackTrace();
			new ScriptError("Parsing error", e2.getMessage());
		}
    }
    
    private void removeAllStatements() {
    	stmtList.clear();
    	currStmtsPane.getChildren().clear();
    }
    
    private void removeStatement(Node remNode, String stmt) {
    	stmtList.remove(stmt);
    	currStmtsPane.getChildren().removeAll(remNode);
    }
    
    private void addStmt(String stmt) {
    	HBox hbox = new HBox();
    	Button remBtn = new Button("x");
    	remBtn.setOnAction(e -> removeStatement(hbox, stmt));
		TextField stmtText = new TextField(stmt);
		HBox.setHgrow(stmtText, Priority.ALWAYS);
		hbox.getChildren().addAll(stmtText, remBtn);
		stmtList.add(stmtText);
		currStmtsPane.getChildren().add(hbox);
    }
    
    private void addStmtFromInput() {
    	inputGroup.setValues();
    	try {
			String currStmt = ToolUtils.getStatementFromTemplate(inputGroup.template).toString();
			this.addStmt(currStmt);
		} catch (ScriptParseException e) {
			e.printStackTrace();
			new ScriptError("Parsing error", e.getMessage());
		}
    }
    
    private void loadTemplate() {
    	File templateFile = this.loadFile();
    	if (templateFile != null) {
			Serializer serialize = new Persister();
			try {
				Template template = serialize.read(Template.class, templateFile);
				templateInputsPane.getChildren().clear();
				inputGroup.getInputs().clear();
				this.createInputs(template, templateInputsPane);
			} catch (Exception e) {
				e.printStackTrace();
				new ScriptError("Template Error", e.getMessage());
			}
    	}
    }
    
	private void createInputs(Template template, Pane pane) {
		List<UserInput> uInputs = template.inputs;
		for (UserInput u : uInputs) {
			TemplateInputGroup inGroup = new TemplateInputGroup(u);
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