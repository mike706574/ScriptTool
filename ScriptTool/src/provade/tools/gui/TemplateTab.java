package provade.tools.gui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.statement.Statement;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import provade.tools.script.Script;
import provade.tools.script.ToolUtils;
import provade.tools.template.Template;
import provade.tools.template.Type;
import provade.tools.template.UserInput;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class TemplateTab extends Tab {
	public static final String tabTitle = "Create from template";
	public static final String createBtnLabel = "Create script";
	
	public List<TextField> stmtList;
	public List<UserInput> userInputs;
	private Stage stage;
	private ToolFileChooser fChooser;
	private FlowPane flow;
	private VBox currStmtsPane;
	private GridPane createAddBtnsPane;
	private Button createScriptBtn;
	private Button addStmtBtn;
	private InputGroup inputGroup;
	private BorderPane borderPane;

	public TemplateTab(Stage stage) {
		super();
		this.stage = stage;
		this.inputGroup = new InputGroup();
		this.createScriptBtn = new Button(TemplateTab.createBtnLabel);
		this.addStmtBtn = new Button("Add");
		this.stmtList = new LinkedList<TextField>();
		this.initGui();
	}
	
	private void addStatement() {
		inputGroup.setValues();
		try {
			String currStmt = ToolUtils.getStatementFromTemplate(inputGroup.template).toString();
			TextField stmtText = new TextField(currStmt);
			stmtList.add(stmtText);
			currStmtsPane.getChildren().add(stmtText);
		} catch (JSQLParserException e) {
			e.printStackTrace();
			new ScriptError("Error", e.getCause().getMessage());
		}
	}
	
	//TODO: if file already exists, ask if user wants to append to the file and append new statements to end
	private void createScript() {
		try {
			FileChooser fChooser = new FileChooser();
			fChooser.getExtensionFilters().addAll(new ExtensionFilter("dms", "*.dms"));
			fChooser.setTitle("Save as");
			File saveAsFile = fChooser.showSaveDialog(this.stage);
			List<String> statements = new ArrayList<String>(stmtList.size());
			stmtList.forEach(e -> statements.add(e.getText()));
			Script script = ToolUtils.CreateScriptFromList(statements);
			ToolUtils.WriteBackupToFile(saveAsFile, script);
			new ScriptSuccess("Success", "Script created: " + saveAsFile.getAbsolutePath());
		} catch (JSQLParserException e) {
			e.printStackTrace();
			new ScriptError("Error", e.getCause().getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			new ScriptError("Error", e.getMessage());
		}
	}
	
	private void loadTemplate() {
		try {
			File sourceFile = fChooser.getFile();
			Serializer serialize = new Persister();
			Template template = serialize.read(Template.class, sourceFile);
			flow.getChildren().add(this.createInputs(template));
			flow.getChildren().add(this.createAddBtnsPane);
			this.stage.setHeight(this.createAddBtnsPane.getLayoutY());
		} catch (IOException e) {
			e.printStackTrace();
			new ScriptError("Error", e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			new ScriptError("Error", e.getMessage());
		}
	}

	/*
	 * TODO: -add the ability to add new 'rows'
	 *       -or theres a side 'panel' that shows created statements, a button takes the inputs and creates a statement and stores it
	 *        on the side panel. Another button takes all statements on the panel and creates the script
	 */
	private void initGui() {
		EventHandler<ActionEvent> doClick = e -> this.loadTemplate();
		fChooser = new ToolFileChooser(this.stage, "Template Path", "...", "Load", doClick);
		createScriptBtn.setOnAction(e -> this.createScript());
		addStmtBtn.setOnAction(e -> this.addStatement());
		
		flow = new FlowPane();
		//flow.setVgap(10);
		flow.setHgap(10);
		flow.setPrefWrapLength(300);
		flow.getChildren().add(fChooser.getPane());
		
		Label currStmtsLabel = new Label("Current Statements");
		currStmtsPane = new VBox();
		currStmtsPane.setPadding(new Insets(25, 25, 25, 25));
		currStmtsPane.getChildren().add(currStmtsLabel);
		
		borderPane = new BorderPane();
		borderPane.setLeft(flow);
		borderPane.setCenter(currStmtsPane);
		
		createAddBtnsPane = new GridPane();
		createAddBtnsPane.setAlignment(Pos.CENTER);
		createAddBtnsPane.setHgap(5);
		createAddBtnsPane.setVgap(10);
		createAddBtnsPane.setPadding(new Insets(10, 10, 10, 10));
		createAddBtnsPane.add(addStmtBtn, 0, 0);
		createAddBtnsPane.add(createScriptBtn, 0, 1);
		
		this.setText(TemplateTab.tabTitle);
		this.setContent(borderPane);
	}

	private Pane createInputs(Template template) {
		VBox inputsGrid = new VBox(8);
		inputsGrid.setPadding(new Insets(25, 25, 25, 25));

		userInputs = template.inputs;
		for (UserInput i : userInputs) {
			TextField txtField = new TextField();
			Label lbl = new Label(i.label);
			lbl.setLabelFor(txtField);
			TemplateInputGroup inGroup = new TemplateInputGroup(i.id, txtField, lbl);
			inputsGrid.getChildren().add(inGroup);
			inputGroup.addInput(inGroup);
		}
		inputGroup.template = template;
		return inputsGrid;
	}
}
