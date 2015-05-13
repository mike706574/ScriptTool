package provade.tools.script;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.statement.Statement;

public class ToolUtils {
	
	
	public static void WriteBackupToFile(File newFile, File sourceFile) throws IOException, JSQLParserException {
		Script script = new Script(sourceFile);
		
		List<Statement> eStmts = script.CreateBackup();
		List<Statement> bkStmts = script.CreateBackup();
		List<String> currentStrings = script.currentStmtStrings;
		
		List<String> fileStrings = new LinkedList<String>();
		eStmts.forEach(s -> fileStrings.add(ToolUtils.addEndLine(s.toString())));
		currentStrings.forEach(s -> fileStrings.add(s += System.lineSeparator()));
		bkStmts.forEach(s -> fileStrings.add("--" + ToolUtils.addEndLine(s.toString())));

		String scriptStr = StringUtils.join(fileStrings, null);
		FileUtils.writeStringToFile(newFile, scriptStr, "UTF-8", true);
	}
	
	public static File CreateFileFromSource(File sourceFile) throws IOException {
		String base = FilenameUtils.getBaseName(sourceFile.getAbsolutePath());
		String newPath = base + "_out.dms";
		
		File newFile = new File(newPath);
		return newFile;
	}
	
	public static String addEndLine(String str) {
		return str + ";" + System.lineSeparator();
	}

}
