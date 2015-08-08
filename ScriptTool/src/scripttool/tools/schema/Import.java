package scripttool.tools.schema;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.StatementVisitor;
import net.sf.jsqlparser.statement.update.Update;

public class Import implements Statement {

	private Table table;
	private Expression where;
	
	@Override
	public void accept(StatementVisitor statementVisitor) {
		
	}
	
	public void setTable(Table t) {
		this.table = t;
	}
	
	public void CopyFromUpdate(Update u) {
		this.table = u.getTable();
		this.where = u.getWhere();
	}
	
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder("IMPORT ");
		
		if (where != null) {
			b.append(this.table);
			b.append(" WHERE ");
			b.append(where);
		} else {
			b.append("*");
		}
		return b.toString();
	}

}
