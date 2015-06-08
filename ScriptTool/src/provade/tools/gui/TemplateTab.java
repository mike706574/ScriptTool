package provade.tools.gui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.sf.jsqlparser.JSQLParserException;

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
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class TemplateTab extends Tab {
	public static final String tabTitle = "Create from template";
	public static final String createBtnLabel = "Create script";
	
	public List<UserInput> userInputs;
	private Stage stage;
	private ToolFileChooser fChooser;
	private FlowPane flow;
	private Button createScriptBtn;
	private InputGroup inputGroup;

	public TemplateTab(Stage stage) {
		super();
		this.stage = stage;
		this.inputGroup = new InputGroup();
		this.createScriptBtn = new Button(TemplateTab.createBtnLabel);
		this.initGui();
	}
	
	private void createScript() {
		try {
			FileChooser fChooser = new FileChooser();
			fChooser.getExtensionFilters().addAll(new ExtensionFilter("dms", "*.dms"));
			fChooser.setTitle("Save as");
			File saveAsFile = fChooser.showSaveDialog(this.stage);
			if (saveAsFile != null) {
				inputGroup.setValues();
				Script script = ToolUtils.CreateScriptFromTemplate(inputGroup.template);
				ToolUtils.WriteBackupToFile(saveAsFile, script);				
			}
		} catch (JSQLParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void loadTemplate() {
		try {
			File sourceFile = fChooser.getFile();
			Serializer serialize = new Persister();
			Template template = serialize.read(Template.class, sourceFile);
			flow.getChildren().add(this.createInputs(template));
			flow.getChildren().add(this.createScriptBtn);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initGui() {
		EventHandler<ActionEvent> doClick = e -> this.loadTemplate();
		fChooser = new ToolFileChooser(this.stage, "Template Path", "...", "Load", doClick);
		createScriptBtn.setOnAction(e -> this.createScript());
		
		flow = new FlowPane();
		flow.setVgap(8);
		flow.setHgap(4);
		flow.setPrefWrapLength(300);
		flow.getChildren().add(fChooser.getPane());
		
		this.setText(TemplateTab.tabTitle);
		this.setContent(flow);
	}

	private Pane createInputs(Template template) {
		GridPane inputsGrid = new GridPane();
		inputsGrid.setAlignment(Pos.CENTER);
		inputsGrid.setHgap(10);
		inputsGrid.setVgap(10);
		inputsGrid.setPadding(new Insets(25, 25, 25, 25));

		int currRow = 0;
		userInputs = template.inputs;
		for (UserInput i : userInputs) {
			TextField txtField = new TextField();
			Label lbl = new Label(i.label);
			lbl.setLabelFor(txtField);
			TemplateInputGroup inGroup = new TemplateInputGroup(i.id, txtField, lbl);
			inputsGrid.add(inGroup, 0, currRow);
			inputGroup.addInput(inGroup);
			currRow++;
		}
		inputGroup.template = template;
		return inputsGrid;
	}
}
