package provade.tools.script;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import provade.tools.schema.Export;
import provade.tools.schema.Import;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.DoubleValue;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.ItemsListVisitor;
import net.sf.jsqlparser.expression.operators.relational.MultiExpressionList;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.SubSelect;
import net.sf.jsqlparser.statement.update.Update;
import net.sf.jsqlparser.util.TablesNamesFinder;
import net.sf.jsqlparser.util.deparser.ExpressionDeParser;
import net.sf.jsqlparser.util.deparser.InsertDeParser;

public class ToolUtils {
	public List<Statement> allStmts;
	public List<Statement> updateStmts;
	public List<Statement> insertStmts;
	public List<Statement> exportStmts;
	public List<Statement> deleteStmts;
	public List<String> currentStmtStrings;
	public File scriptFile;

	public ToolUtils() {
		allStmts = new LinkedList<Statement>();
		updateStmts = new LinkedList<Statement>();
		insertStmts = new LinkedList<Statement>();
		exportStmts = new LinkedList<Statement>();
		deleteStmts = new LinkedList<Statement>();
	}
	
	public void ReadFromFile(String path) throws IOException, JSQLParserException {
		this.scriptFile = new File(path);
		currentStmtStrings = FileUtils.readLines(new File(path));
		for (String s : currentStmtStrings) {
			if (!s.isEmpty()) {
				this.AddStatement(s);
			}
		}
	}

	public void AddStatement(String stmt) throws JSQLParserException {
		Statement pStmt = CCJSqlParserUtil.parse(stmt);

		allStmts.add(pStmt);
		if (pStmt instanceof Update) {
			updateStmts.add(pStmt);
		} else if (pStmt instanceof Insert) {
			insertStmts.add(pStmt);
		}
	}

	public List<Statement> GetBackup() {
		for (Statement s : updateStmts) {
			Export e = new Export((Update) s);
			exportStmts.add(e);
		}

		return exportStmts;
	}
	
	public List<Statement> GetBackout() {
		List<Statement> bkStmts = new LinkedList<Statement>();
		if (exportStmts.size() > 0) {
			bkStmts.add(new Import());
		}
		this.CreateDeletes();
		bkStmts.addAll(deleteStmts);
		return bkStmts;
	}
	
	public void SaveBackupToFile(String path) {
		File newScript = new File(path);
		
		try {
			ToolUtils.WriteAllToFile(this.currentStmtStrings, newScript, this.GetBackup(), this.GetBackout());
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Error writing to file");
		}
	}
	
	public static void WriteAllToFile(List<String> currentStrings, File newFile, List<Statement> eStmts, List<Statement> bkStmts) throws IOException {
		List<String> fileStrings = new LinkedList<String>();
		eStmts.forEach(s -> fileStrings.add(ToolUtils.addEndLine(s.toString())));
		currentStrings.forEach(s -> fileStrings.add(s += System.lineSeparator()));
		bkStmts.forEach(s -> fileStrings.add("--" + ToolUtils.addEndLine(s.toString())));

		String scriptStr = StringUtils.join(fileStrings, null);
		FileUtils.writeStringToFile(newFile, scriptStr, "UTF-8", true);
	}
	
	public static String addEndLine(String str) {
		return str + ";" + System.lineSeparator();
	}

	private void CreateDeletes() {		
		for (Statement s : insertStmts) {
			Insert inStmt = (Insert) s;
			Delete d = new Delete();
			d.setTable(inStmt.getTable());

			List<Column> cols = inStmt.getColumns();
			WhereExpDeParser eParse = new WhereExpDeParser(cols);
			StringBuilder b = new StringBuilder();
			eParse.setBuffer(b);
			inStmt.getItemsList().accept(eParse);
			
			List<String> whereStrList = eParse.whereStrList;
			String whereStr = StringUtils.join(whereStrList, " AND ");
			Expression parseExpression = null;
			try {
				parseExpression = CCJSqlParserUtil.parseCondExpression(whereStr);
				d.setWhere(parseExpression);
			} catch (JSQLParserException e) {
			}
			deleteStmts.add(d);
		}
	}
}
