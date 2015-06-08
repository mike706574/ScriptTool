package provade.tools.template;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

public class UserInput {
	@Attribute
	public Type as;
	@Element
	public String label;
	@Attribute
	public int id;
	
	public String value;
	
	public UserInput() {
		
	}
	
	public UserInput(int id, String label, Type as) {
		this.as = as;
		this.label = label;
		this.id = id;
	}
}
