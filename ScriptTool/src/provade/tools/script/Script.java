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
import net.sf.jsqlparser.util.deparser.ExpressionDeParser;

public class Script {
	public List<Statement> allStmts;
	public List<Statement> updateStmts;
	public List<Statement> insertStmts;
	public List<Statement> exportStmts;
	public List<Statement> deleteStmts;
	public List<String> currentStmtStrings;
	
	public Script() {
		allStmts = new LinkedList<Statement>();
		updateStmts = new LinkedList<Statement>();
		insertStmts = new LinkedList<Statement>();
		exportStmts = new LinkedList<Statement>();
		deleteStmts = new LinkedList<Statement>();		
	}
	
	public Script(File scriptFile) throws IOException, JSQLParserException {
		this();
		currentStmtStrings = FileUtils.readLines(scriptFile);
		for (String s : currentStmtStrings) {
			if (!s.isEmpty()) {
				this.AddStatement(s);
			}
		}		
	}
	
	public List<Statement> CreateBackup() {
		for (Statement s : updateStmts) {
			Export e = new Export((Update) s);
			exportStmts.add(e);
		}

		return exportStmts;
	}
	
	public List<Statement> CreateBackout() {
		List<Statement> bkStmts = new LinkedList<Statement>();
		if (exportStmts.size() > 0) {
			bkStmts.add(new Import());
		}
		this.CreateDeletes();
		bkStmts.addAll(deleteStmts);
		return bkStmts;
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
	
	private void CreateDeletes() {		
		for (Statement s : insertStmts) {
			Insert inStmt = (Insert) s;
			Delete d = new Delete();
			d.setTable(inStmt.getTable());

			StringBuilder b = new StringBuilder();
			List<Column> cols = inStmt.getColumns();
			Expression whereExp = null;
			if (cols == null) {
				InsertSelectDeParser dParse = new InsertSelectDeParser();
				ExpressionDeParser eParse = new ExpressionDeParser(dParse, b);
				dParse.setBuffer(b);
				inStmt.getItemsList().accept(eParse);
				whereExp = dParse.subSelectWhere;
			} else {
				WhereExpDeParser eParse = new WhereExpDeParser(cols);
				
				eParse.setBuffer(b);
				inStmt.getItemsList().accept(eParse);
				
				List<String> whereStrList = eParse.whereStrList;
				String whereStr = StringUtils.join(whereStrList, " AND ");
				try {
					whereExp = CCJSqlParserUtil.parseCondExpression(whereStr);
				} catch (JSQLParserException e) {
					//TODO: do something, maybe add it to a list of error statements that is read back to the user
				}				
			}
			
			d.setWhere(whereExp);
			deleteStmts.add(d);
		}
	}
	
}
