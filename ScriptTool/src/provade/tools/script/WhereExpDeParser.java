package provade.tools.script;

import java.util.ArrayList;
import java.util.List;

import net.sf.jsqlparser.expression.DoubleValue;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.util.deparser.ExpressionDeParser;

public class WhereExpDeParser extends ExpressionDeParser {
	
	public List<String> whereStrList;
	private List<Column> columns;
	private int count = 0;
	
	public WhereExpDeParser(List<Column> columns) {
		this.columns = columns;
		this.whereStrList = new ArrayList<String>(columns.size());
	}
	
	@Override
	public void visit(StringValue stringValue) {
		this.addColumn(stringValue.toString());
	}
	
    @Override
    public void visit(DoubleValue doubleValue) {
    	this.addColumn(doubleValue.toString());
    }
    
    @Override
    public void visit(LongValue longValue) {	
    	this.addColumn(longValue.toString());
    }
    
    private void addColumn(String value) {
    	String itemStr = this.columns.get(count).getColumnName() + " = " + value;
    	whereStrList.add(itemStr);
    	count++;			    	
    }
}
