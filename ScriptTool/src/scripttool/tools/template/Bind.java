package scripttool.tools.template;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

public class Bind {
	@Attribute
	public String to;
	@Element
	public Result result;
	
	public String value;
	
	public Bind() {
		
	}
}
