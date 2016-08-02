package scripttool.tools.script;

import java.util.ArrayList;
import java.util.List;

import net.sf.jsqlparser.expression.DoubleValue;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ItemsList;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectVisitor;
import net.sf.jsqlparser.statement.select.SubSelect;
import net.sf.jsqlparser.util.deparser.ExpressionDeParser;
import net.sf.jsqlparser.util.deparser.SelectDeParser;

public class WhereExpDeParser extends ExpressionDeParser {
	private List<Column> columns;
	private int count = 0;
	private Expression whereExp;
	private boolean isSubSelect;
	
	public WhereExpDeParser(List<Column> columns, StringBuilder buffer) {
		SelectDeParser sParser = new SelectDeParser(this, buffer);
		this.setBuffer(buffer);
		this.setSelectVisitor(sParser);
		this.columns = columns;
		for(Column c : columns) {
			System.out.println("Column: " + c.getFullyQualifiedName());
		}
	}
	
	public Expression getWhereExp(ItemsList iList) {
		if (iList instanceof SubSelect) {
			isSubSelect = true;
		}
		iList.accept(this);
		return whereExp;
	}
	
	@Override
	public void visit(SubSelect subSelect) {
		//TODO: pass to this a custom listener that can grab the static select strings
		SelectDeParser sVisitor = new SelectDeParser() {
		    @Override
		    public void visit(SelectExpressionItem selectExpressionItem) {
		    	selectExpressionItem.getExpression().accept(this.getExpressionVisitor());
		    }
		};
		subSelect.getSelectBody().accept(this.getSelectVisitor());
	}
	
	@Override
	public void visit(StringValue stringValue) {
		System.out.println("visit StringValue:" + stringValue.getValue());
		this.addColumn(stringValue);
	}
	
    @Override
    public void visit(DoubleValue doubleValue) {
    	System.out.println("visit DoubleValue:" + doubleValue.getValue());
    	this.addColumn(doubleValue);
    }
    
    @Override
    public void visit(LongValue longValue) {	
    	System.out.println("visit LongValue:" + longValue.getValue());
    	this.addColumn(longValue);
    }
    
    private void addColumn(Expression value) {
    	EqualsTo newExp = new EqualsTo();
    	newExp.setLeftExpression(this.columns.get(count));
    	newExp.setRightExpression(value);
    	if (this.whereExp == null) {
    		this.whereExp = newExp;
    	} else {
    		this.whereExp = new AndExpression(this.whereExp, newExp);
    	}
    	
    	count++;			    	
    }
}
