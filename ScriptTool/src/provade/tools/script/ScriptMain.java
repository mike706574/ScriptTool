package provade.tools.script;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import provade.tools.schema.Export;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.update.Update;
import net.sf.jsqlparser.util.TablesNamesFinder;

public class ScriptMain {

	public static void main(String[] args)  {
		
		ToolUtils utils = new ToolUtils();
		String path = "C:\\Users\\tim\\sql_script.txt";
		try {
			utils.ReadFromFile(path);
		} catch (JSQLParserException e) {
			System.out.println("There was an error parsing your script.  Check the format.");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.toString());
		} 
		String newPath = "C:\\Users\\tim\\update_script.txt";
		utils.SaveBackupToFile(newPath);
	}

}
