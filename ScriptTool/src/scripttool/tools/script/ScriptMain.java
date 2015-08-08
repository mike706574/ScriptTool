package scripttool.tools.script;

import java.io.File;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import javafx.application.Application;
import scripttool.tools.gui.MainWindow;
import scripttool.tools.template.Bind;
import scripttool.tools.template.Template;
import scripttool.tools.template.UserInput;

public class ScriptMain  {
	public static final double version = 0.1;
	
	public static void main(String[] args)  {
		Application.launch(MainWindow.class, args);
	}

}
