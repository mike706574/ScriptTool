package scripttool.tools.script;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import scripttool.tools.template.Bind;
import scripttool.tools.template.Result;
import scripttool.tools.template.TemplStatement;
import scripttool.tools.template.Template;
import scripttool.tools.template.UserInput;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;

/*
 * TODO: allow ability to have multiple statements in a template, so create a whole script
 */
public class ToolUtils {
	
	public static Script CreateScriptFromList(List<String> statements) throws ScriptParseException {
		Script script = new Script();
		for(String s : statements) {
			script.AddStatementString(s);
		}
		return script;
	}
	/*
	public static Statement getStatementFromTemplate(Template template) throws ScriptParseException {
		String stmtStr = template.statement;
		List<Bind> binds = template.binds;
		for (Bind b : binds) {
			Result r = b.result;
			UserInput in = template.findById(r.in);
			if (in.value != null){
				stmtStr = stmtStr.replace(b.to, in.value);
			}
		}
		try {
			return CCJSqlParserUtil.parse(stmtStr);
		} catch (JSQLParserException e) {
			e.printStackTrace();
			throw new ScriptParseException(stmtStr, e.getCause());
		}
	}*/
	
	public static List<Statement> getStatementsFromTemplate(Template template) throws ScriptParseException {
		ArrayList<Statement> templStmts = new ArrayList<Statement>();
		List<TemplStatement> stmts = template.statements;
		List<Bind> binds = template.binds;
		for (TemplStatement stmt : stmts) {
			String stmtStr = stmt.stmtString;
			for (Bind b : binds) {
				Result r = b.result;
				UserInput in = template.findById(r.in);
				if (in.value != null){
					stmtStr = stmtStr.replace(b.to, in.value);
				}
			}
			try {
				templStmts.add(CCJSqlParserUtil.parse(stmtStr));
			} catch (JSQLParserException e) {
				e.printStackTrace();
				throw new ScriptParseException(stmtStr, e.getCause());
			}
		}
		return templStmts;
	}
	
	public static List<String> getStatementStringsFromTemplate(Template template) throws ScriptParseException {
		List<Statement> stmts = ToolUtils.getStatementsFromTemplate(template);
		ArrayList<String> stmtStrs = new ArrayList<String>(stmts.size());
		for (Statement s : stmts) {
			stmtStrs.add(s.toString());
		}
		return stmtStrs;
	}
	
	public static Script CreateScriptFromTemplate(Template template) throws ScriptParseException {
		Script script = new Script();
		List<Statement> formedStmts = ToolUtils.getStatementsFromTemplate(template);
		for (Statement s : formedStmts) {
			script.AddStatement(s);
		}
		//Statement formedStmt = ToolUtils.getStatementFromTemplate(template);
		return script;
	}
	
	public static void WriteBackupToFile(File newFile, Script script) throws IOException {		
		List<Statement> eStmts = script.CreateBackup();
		//List<Statement> bkStmts = script.CreateBackout();
		List<Statement> currentStmts = script.allStmts;
		
		List<String> fileStrings = new LinkedList<String>();
		if (eStmts != null) {
			eStmts.forEach(s -> fileStrings.add(ToolUtils.addEndLine(s.toString())));
			fileStrings.add(System.lineSeparator());			
		}
		if (currentStmts != null) {
			currentStmts.forEach(s -> fileStrings.add(ToolUtils.addEndLine(s.toString())));
			fileStrings.add(System.lineSeparator());			
		}
		/*if (bkStmts != null) {
			bkStmts.forEach(s -> fileStrings.add("--" + ToolUtils.addEndLine(s.toString())));
		}*/
		// Instead of deleting what was exported, changed to just update the dups
		if (eStmts != null) {
			fileStrings.add("--Backout" + System.lineSeparator());
			fileStrings.add("--SET UPDATE_DUPS" + ";" + System.lineSeparator());
		}
		String scriptStr = StringUtils.join(fileStrings, null);
		FileUtils.writeStringToFile(newFile, scriptStr, "UTF-8", true);
	}
	
	public static File CreateFileFromSource(File sourceFile) throws IOException {
		String fileAbsPath = sourceFile.getAbsolutePath();
		String base = FilenameUtils.getBaseName(fileAbsPath);
		String path = FilenameUtils.getFullPath(fileAbsPath);
		String newPath = path + base + "_out.dms";
		
		File newFile = new File(newPath);
		if (newFile.exists()) {
			newFile.delete();
		}
		return newFile;
	}
	
	public static String addEndLine(String str) {
		return str + ";" + System.lineSeparator();
	}

}
