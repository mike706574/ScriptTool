package provade.tools.gui;

import javafx.scene.Group;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;

public class TemplateInputGroup extends Group {
	public int id;
	public Control input;
	public Label label;
	
	public TemplateInputGroup(int id, Control input, Label label) {
		this.id = id;
		this.input = input;
		this.label = label;
		this.init();
	}
	
	private void init() {
		this.getChildren().add(this.label);
		this.getChildren().add(this.input);
	}
	
	public String getValue() {
		String ret = null;
		if (input instanceof TextInputControl) {
			TextInputControl txtIn = (TextInputControl) input;
			ret = txtIn.getText();
		}
		return ret;
	}
	
	@Override
	public void layoutChildren() {
		super.layoutChildren();
		this.label.setLayoutX(0);
		this.input.setLayoutX(this.label.getWidth() + 10);
	}
}
