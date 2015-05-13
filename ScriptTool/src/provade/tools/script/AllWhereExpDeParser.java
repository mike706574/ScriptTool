package provade.tools.script;


import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.relational.ExistsExpression;
import net.sf.jsqlparser.expression.operators.relational.ItemsList;
import net.sf.jsqlparser.statement.select.SubSelect;
import net.sf.jsqlparser.util.deparser.ExpressionDeParser;

public class AllWhereExpDeParser extends ExpressionDeParser {
	public Expression whereExp;
	
	public AllWhereExpDeParser(StringBuilder buffer) {
		InsertSelectDeParser selDeParser = new InsertSelectDeParser();
		this.setBuffer(buffer);
		selDeParser.setBuffer(buffer);
		this.setSelectVisitor(selDeParser);
	}
	
	public Expression getOptimizedWhere(ItemsList iList) {
		iList.accept(this);
		return whereExp;
	}
	
    @Override
    public void visit(SubSelect subSelect) {
    	InsertSelectDeParser visitor = (InsertSelectDeParser) this.getSelectVisitor();
        Expression wExp = visitor.getWhere(subSelect.getSelectBody());
        wExp.accept(this);
        if (this.whereExp == null) {
        	this.whereExp = wExp;
        }
    }
    
	@Override
	public void visit(ExistsExpression existsExpression) {
		if (existsExpression.isNot()) {
			Expression rExp = existsExpression.getRightExpression();
			if (rExp instanceof SubSelect) {
		    	InsertSelectDeParser visitor = (InsertSelectDeParser) this.getSelectVisitor();
		    	SubSelect subSel = (SubSelect) rExp;
		    	this.whereExp = visitor.getWhere(subSel.getSelectBody());
			}
		}
	}
}
