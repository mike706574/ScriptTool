package provade.tools.script;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import provade.tools.schema.Export;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.relational.ItemsList;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.update.Update;
import net.sf.jsqlparser.util.TablesNamesFinder;

public class ScriptMain  {
	
	public static void main(String[] args)  {
		/*
		String path = "C:\\Users\\tim\\sql_script.txt";
		String newPath = "C:\\Users\\tim\\update_script.txt";
		
		Script currScript = null;
		File scriptFile = new File(path);
		try {
			currScript = new Script(scriptFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSQLParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		File newFile = new File(newPath);
		try {
			ToolUtils.WriteBackupToFile(newFile, currScript);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		String stmt = "INSERT INTO psoprdefn select 'hi', 'ho', 'he' from ps_installation where oprid = 'tim'";
		Script script = new Script();
		try {
			script.AddStatement(stmt);
		} catch (JSQLParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<Statement> dStmts = script.CreateBackout();
		dStmts.forEach(s -> System.out.println(s.toString()));
	}

}
