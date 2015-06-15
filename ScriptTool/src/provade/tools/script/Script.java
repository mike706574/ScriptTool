package provade.tools.script;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import provade.tools.schema.Export;
import provade.tools.schema.Import;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.update.Update;

/*
 * TODO: join together like statements, if 2 stmts update same table try to convert into 1 with an 'in'
 */
public class Script {
	public List<Statement> allStmts;
	public List<Update> updateStmts;
	public List<Insert> insertStmts;
	public List<Export> exportStmts;
	public List<Delete> deleteStmts;
	public List<String> currentStmtStrings;

	public Script() {
		allStmts = new LinkedList<Statement>();
		updateStmts = new LinkedList<Update>();
		insertStmts = new LinkedList<Insert>();
		exportStmts = new LinkedList<Export>();
		deleteStmts = new LinkedList<Delete>();
	}

	public Script(File scriptFile) throws IOException, JSQLParserException {
		this();
		currentStmtStrings = FileUtils.readLines(scriptFile);
		StringBuilder stmt = new StringBuilder();
		for (String s : currentStmtStrings) {
			stmt.append(s + " ");
			if (!s.isEmpty() && StringUtils.endsWith(s, ";")) {
				this.AddStatementString(stmt.toString());
				stmt = new StringBuilder();
			}
		}
	}

	public List<Statement> CreateBackup() {
		List<Statement> bkUpStmts = new LinkedList<Statement>();
		for (Update s : updateStmts) {
			Export e = new Export(s);
			exportStmts.add(e);
			bkUpStmts.add(e);
		}

		return bkUpStmts;
	}

	public List<Statement> CreateBackout() {
		List<Statement> bkStmts = new LinkedList<Statement>();
		if (exportStmts.size() > 0) {
			List<Delete> eDeletes = Script.GetDeletesFromExports(exportStmts);
			bkStmts.addAll(eDeletes);
			bkStmts.add(new Import());
		}
		this.CreateDeletes();
		bkStmts.addAll(deleteStmts);
		return bkStmts;
	}
	
	public void AddStatement(Statement stmt) {
		this.AddToList(stmt);
	}

	public void AddStatementString(String stmt) throws JSQLParserException {
		Statement pStmt = CCJSqlParserUtil.parse(stmt);
		this.AddToList(pStmt);
	}

	public static List<Delete> GetDeletesFromExports(List<Export> exports) {
		List<Delete> deletes = new LinkedList<Delete>();
		for (Export e : exports) {
			Delete d = new Delete();
			d.setTable(e.getTable());
			d.setWhere(e.getWhere());
			deletes.add(d);
		}
		return deletes;
	}
	
	private void AddToList(Statement stmt) {
		allStmts.add(stmt);
		if (stmt instanceof Update) {
			updateStmts.add((Update)stmt);
		} else if (stmt instanceof Insert) {
			insertStmts.add((Insert)stmt);
		}
	}

	private void CreateDeletes() {
		for (Insert inStmt : insertStmts) {
			Delete d = new Delete();
			d.setTable(inStmt.getTable());

			StringBuilder b = new StringBuilder();
			List<Column> cols = inStmt.getColumns();
			Expression whereExp = null;
			if (cols == null) {
				AllWhereExpDeParser dParse = new AllWhereExpDeParser(b);
				whereExp = dParse.getOptimizedWhere(inStmt.getItemsList());
			} else {
				WhereExpDeParser eParse = new WhereExpDeParser(cols, b);
				whereExp = eParse.getWhereExp(inStmt.getItemsList());
			}

			d.setWhere(whereExp);
			deleteStmts.add(d);
		}
	}

}
