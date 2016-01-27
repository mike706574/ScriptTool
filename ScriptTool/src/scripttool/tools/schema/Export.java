package scripttool.tools.schema;

import java.util.List;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.StatementVisitor;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.Join;
import net.sf.jsqlparser.statement.update.Update;

public class Export implements Statement {

	private Table table;
	private Expression where;
	private List<Expression> expressions;
	private FromItem fromItem;
	private List<Join> joins;
	
	public Export(Update u) {
		this.CopyFromUpdate(u);
	}
	
	public Export(Delete d) {
		this.CopyFromDelete(d);
	}
	
	@Override
	public void accept(StatementVisitor statementVisitor) {

	}
	
	public Table getTable() {
		return table;
	}
	
	public Expression getWhere() {
		return where;
	}
	
	public void AddExpressions(List<Expression> exps) {
		this.expressions.addAll(exps);
	}
	
	public void CopyFromUpdate(Update u) {
		this.table = u.getTable();
		this.where = u.getWhere();
		this.expressions = u.getExpressions();
		this.fromItem = u.getFromItem();
		this.joins = u.getJoins();
	}
	
	public void CopyFromDelete(Delete d) {
		this.table = d.getTable();
		this.where = d.getWhere();
	}
	
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder("EXPORT ");
		b.append(this.table);

		if (fromItem != null) {
			b.append(" FROM ").append(fromItem);
			if (joins != null) {
				for (Join join : joins) {
					if (join.isSimple()) {
						b.append(", ").append(join);
					} else {
						b.append(" ").append(join);
					}
				}
			}
		}

		if (where != null) {
			b.append(" WHERE ");
			b.append(where);
		}
		return b.toString();
	}
}
