package provade.tools.script;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import provade.tools.template.Bind;
import provade.tools.template.Result;
import provade.tools.template.Template;
import provade.tools.template.UserInput;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.statement.Statement;

public class ToolUtils {
	
	public static Script CreateScriptFromTemplate(Template template) throws JSQLParserException {
		Script script = new Script();
		List<Bind> binds = template.binds;
		for (Bind b : binds) {
			Result r = b.result;
			UserInput in = template.findById(r.in);
			if (in.value != null){
				template.statement = template.statement.replace(b.to, in.value);
			}
		}
		script.AddStatement(template.statement);
		return script;
	}
	
	public static void WriteBackupToFile(File newFile, Script script) throws IOException, JSQLParserException {		
		List<Statement> eStmts = script.CreateBackup();
		List<Statement> bkStmts = script.CreateBackout();
		List<Statement> currentStmts = script.allStmts;
		
		List<String> fileStrings = new LinkedList<String>();
		if (eStmts != null) {
			eStmts.forEach(s -> fileStrings.add(ToolUtils.addEndLine(s.toString())));
			fileStrings.add(System.lineSeparator());			
		}
		if (currentStmts != null) {
			currentStmts.forEach(s -> fileStrings.add(s.toString() + System.lineSeparator()));
			fileStrings.add(System.lineSeparator());			
		}
		if (bkStmts != null) {
			bkStmts.forEach(s -> fileStrings.add("--" + ToolUtils.addEndLine(s.toString())));
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
