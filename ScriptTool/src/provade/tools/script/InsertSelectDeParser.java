package provade.tools.script;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.util.deparser.SelectDeParser;

public class InsertSelectDeParser extends SelectDeParser {
	public Expression subSelectWhere;
	
	public Expression getWhere(SelectBody body) {
		body.accept(this);
		return subSelectWhere;
	}
	
	@Override
    public void visit(PlainSelect plainSelect) {
		subSelectWhere = plainSelect.getWhere();
	}
}
