package provade.tools.script;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class Field {
	public String name;
	public List<String> values;
	public String qValue;
	public boolean useQuote;
	
	public Field() {
		this.values = new ArrayList<String>();
	}
	
	public Field(String value) {
		this();
		this.values.add(value);
	}
	
	public Field(String name, String value) {
		this();
		this.name = name;
		if(value != null) {
			values.add(value);
		}
	}
	
	public Field(String name, List<String> values) {
		this();
		this.name = name;
		this.values = values;
	}
	
	public Field(String name, String[] values) {
		this();
		this.name = name;
		for(String s : values) {
			this.values.add(s);
		}
	}
	
	public String buildString() {
		if(values.size() <= 0) return null;
		
		StringBuilder b = new StringBuilder(name);
		
		if (values.size() > 1) {
			b.append(" IN (" + StringUtils.join(values, ",") + ")");
		} else {
			b.append(" = " + values.get(0));
		}
		return b.toString();
	}
	
	@Override
	public String toString() {
		return "Name: " + this.name + " Values: " + StringUtils.join(values, ",");
	}
}
