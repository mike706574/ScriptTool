package provade.tools.gui;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import provade.tools.template.Option;
import provade.tools.template.Template;
import provade.tools.template.UserInput;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;

//TODO: create more exceptions for better feedback on bad xml
public class TemplateInputGroup extends Group {
	public int id;
	public Control input;
	public Label label;

	public TemplateInputGroup(UserInput uInput) {
		this.id = uInput.id;
		this.createInputsFromTemplate(uInput);
		this.init();
	}

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

	@SuppressWarnings("unchecked")
	public String getValue() {
		String ret = null;
		if (input instanceof TextInputControl) {
			TextInputControl txtIn = (TextInputControl) input;
			ret = txtIn.getText();
		} else
		if (input instanceof ComboBox) {
			ComboBox<Option> box = (ComboBox<Option>) input;
			SingleSelectionModel<Option> sel = ((ComboBox<Option>) input).getSelectionModel();
			List<Option> ops = box.getItems();
			Option o = ops.get(IntStream.range(0, ops.size()).filter(i -> sel.isSelected(i)).findFirst().getAsInt());
			ret = o.value;
		}
		return ret;
	}

	private void createInputsFromTemplate(UserInput uInput) {

		this.label = new Label(uInput.label);
		if (uInput.input != null) {
			// TODO: create enum type instead of exact string
			if (uInput.input.type.equals("text")) {
				this.input = new TextField();
			}
		}
		if (uInput.select != null) {
			List<Option> ops = uInput.select.options;
			ObservableList<Option> options = FXCollections
					.observableArrayList(ops);
			this.input = new ComboBox<Option>(options);
		}
	}

	@Override
	public void layoutChildren() {
		super.layoutChildren();
		this.label.setLayoutX(0);
		this.input.setLayoutX(this.label.getWidth() + 10);
	}
}
