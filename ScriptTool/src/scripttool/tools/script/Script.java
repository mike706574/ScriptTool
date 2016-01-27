package scripttool.tools.script;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import scripttool.tools.schema.Export;
import scripttool.tools.schema.Import;
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
	public List<Delete> bkDeleteStmts;
	public List<String> currentStmtStrings;

	public Script() {
		allStmts = new LinkedList<Statement>();
		updateStmts = new LinkedList<Update>();
		insertStmts = new LinkedList<Insert>();
		exportStmts = new LinkedList<Export>();
		deleteStmts = new LinkedList<Delete>();
		bkDeleteStmts = new LinkedList<Delete>();
	}

	/*
	 * TODO: Disregard anything thats not a valid statement
	 */
	public Script(File scriptFile) throws IOException, ScriptParseException {
		this();
		currentStmtStrings = FileUtils.readLines(scriptFile);
		StringBuilder stmt = new StringBuilder();
		for (String s : currentStmtStrings) {
			stmt.append(s + " ");
			if (this.isStringValidStmt(s)) {
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
		for (Delete d : deleteStmts) {
			Export e = new Export(d);
			exportStmts.add(e);
			bkUpStmts.add(d);
		}

		return bkUpStmts;
	}

	public List<Statement> CreateBackout() {
		List<Statement> bkStmts = new LinkedList<Statement>();
		if (exportStmts.size() > 0) {
			/* Convert to update_dups, should add that statement here
			List<Delete> eDeletes = Script.GetDeletesFromExports(exportStmts);
			bkStmts.addAll(eDeletes);*/
			bkStmts.add(new Import());
		}
		this.CreateDeletes();
		bkStmts.addAll(bkDeleteStmts);
		return bkStmts;
	}
	
	public void AddStatement(Statement stmt) {
		this.AddToList(stmt);
	}

	public void AddStatementString(String stmt) throws ScriptParseException {
		Statement pStmt;
		try {
			pStmt = CCJSqlParserUtil.parse(stmt);
		} catch (JSQLParserException e) {
			e.printStackTrace();
			throw new ScriptParseException(stmt, e.getCause());
		}
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
		} else if (stmt instanceof Delete) {
			deleteStmts.add((Delete)stmt);
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
			bkDeleteStmts.add(d);
		}
	}

	private boolean isStringValidStmt(String s) {
		if (s.isEmpty()) {
			return false;
		}
		if (StringUtils.startsWithAny(s, new String[] {"DELETE", "INSERT", "UPDATE"})) {
			return true;
		}
		return false;
	}
}
