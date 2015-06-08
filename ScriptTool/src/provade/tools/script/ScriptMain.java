package provade.tools.script;

import java.io.File;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import javafx.application.Application;
import provade.tools.gui.MainWindow;
import provade.tools.template.Bind;
import provade.tools.template.Template;
import provade.tools.template.UserInput;

public class ScriptMain  {
	
	public static void main(String[] args)  {
		Application.launch(MainWindow.class, args);
	}

}
