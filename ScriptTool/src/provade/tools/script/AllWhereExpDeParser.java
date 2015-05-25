package provade.tools.script;


import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.relational.ExistsExpression;
import net.sf.jsqlparser.expression.operators.relational.ItemsList;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.SubSelect;
import net.sf.jsqlparser.util.deparser.ExpressionDeParser;

public class AllWhereExpDeParser extends ExpressionDeParser {
	public Expression whereExp;
	public StringBuilder whereExpStr;
	
	public AllWhereExpDeParser(StringBuilder buffer) {
		InsertSelectDeParser selDeParser = new InsertSelectDeParser();
		this.setBuffer(buffer);
		selDeParser.setBuffer(buffer);
		this.setSelectVisitor(selDeParser);
		this.whereExpStr = new StringBuilder();
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
		    	Expression wExp = visitor.getWhere(subSel.getSelectBody());
		    	
		    	//Need to remove any alias in the where clause
		    	AllWhereExpDeParser whereDeParse = new AllWhereExpDeParser(this.whereExpStr);
		    	wExp.accept(whereDeParse);
		    	
		    	try {
					this.whereExp = CCJSqlParserUtil.parseCondExpression(whereDeParse.getBuffer().toString());
				} catch (JSQLParserException e) {
					e.printStackTrace();
					this.whereExp = null;
				}
			}
		}
	}

    @Override
    public void visit(Column tableColumn) {
        this.getBuffer().append(tableColumn.getColumnName());
    }
}
