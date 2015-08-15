package scripttool.tools.gui;

import java.util.ArrayList;
import java.util.List;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import scripttool.tools.template.Bind;
import scripttool.tools.template.Result;
import scripttool.tools.template.Template;
import scripttool.tools.template.UserInput;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Label;

public class InputGroup {
	public Template template;
	private List<TemplateInputGroup> inputs;
	
	public InputGroup() {
		this.inputs = new ArrayList<TemplateInputGroup>();
	}
	
	public List<TemplateInputGroup> getInputs() {
		return this.inputs;
	}
	
	public void addInput(TemplateInputGroup input) {
		this.inputs.add(input);
	}
	
	public void setValues() {
		for(TemplateInputGroup i : inputs) {
			template.setValue(i.id, i.getValue());
		}
	}
}
