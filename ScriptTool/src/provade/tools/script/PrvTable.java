package provade.tools.script;

import java.util.List;

public class PrvTable {
	public interface FieldBuilder {
		public String build(Field f);
	}
	
	public String name;
	public Operation op;
	public List<Field> opFields;
	public List<Field> whereFields;
	
	public static PrvTable inst;
	static {
		inst = new PrvTable(Operation.SELECT, "PS_INSTALLATION", null, null);
	}
	
	public PrvTable() {
		this(null, null, null, null);
	}
	
	public PrvTable(String op) {
		for(Operation o : Operation.values()) {
			if(o.opStr.equalsIgnoreCase(op)) {
				this.op = o;
				break;
			}
		}
	}
	
	public PrvTable(Operation op, String name, List<Field> opFields, List<Field> whereFields) {
		this.op = op;
		this.name = name;
		this.opFields = opFields;
		this.whereFields = whereFields;
	}
	
	public void addOpFields(String[] valArr) {
		
	}
	
	public String buildString() {
		StringBuilder b = new StringBuilder(this.op.opStr);
		b.append(" ");
		
		switch(this.op) {
		case UPDATE:
			b.append(this.name);
			b.append(this.buildFields(opFields," SET ", ","));
		case DELETE:
			b.append(" FROM " + this.name);
		case INSERT:
			PrvTable.inst.opFields = this.opFields;
			b.append(" INTO " + this.name + " " + PrvTable.inst.buildString());
			break;
		case SELECT:
			b.append(this.buildFields(opFields, "", ", "));
			b.append(" FROM " + this.name);
		default:
			if (whereFields != null) {
				b.append(this.buildFields(whereFields, " WHERE ", " AND "));
			}
			break;
		}
		
		return b.toString();
	}
	
	public String buildFields(List<Field> fields, String start, String delim) {
		StringBuilder b = new StringBuilder(start);
		int i = 1;
		for(Field f : fields) {
			b.append(f.buildString());
			if(i != fields.size()) b.append(delim);
			i++;
		}
		
		return b.toString();		
	}
}
