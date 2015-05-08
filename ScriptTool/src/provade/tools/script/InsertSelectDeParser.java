package provade.tools.script;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.relational.ExistsExpression;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.util.deparser.ExpressionDeParser;
import net.sf.jsqlparser.util.deparser.SelectDeParser;

public class InsertSelectDeParser extends SelectDeParser {
	public Expression subSelectWhere;
	
	@Override
    public void visit(PlainSelect plainSelect) {
		subSelectWhere = plainSelect.getWhere();
		
		/*
		 * TODO: deparser should go through where exp and find all equality exps so oprid = 'tim' and
		 *       add them to the base exp, recursively go through any not exists clauses 
		 */
		ExpressionDeParser eDparse = new ExpressionDeParser() {
			@Override
			public void visit(ExistsExpression existsExpression) {
				if (existsExpression.isNot()) {
					Expression rExp = existsExpression.getRightExpression();
				}
			}
		};
	}
}
