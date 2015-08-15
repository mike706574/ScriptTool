package scripttool.tools.gui;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class ScriptError {
	
	public ScriptError(String title, String text) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle(title);
		alert.setContentText(text);
		alert.show();
	}
}
