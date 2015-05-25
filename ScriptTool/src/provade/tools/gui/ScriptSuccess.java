package provade.tools.gui;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class ScriptSuccess {
	
	public ScriptSuccess(String title, String text) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setContentText(text);
		alert.show();
	}
}
