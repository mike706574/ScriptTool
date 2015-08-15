package scripttool.tools.template;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Text;

public class Option {

	@Attribute
	public String value;
	@Text
	public String text;
	
	public Option() {
		
	}
	
	public Option(String value, String text) {
		this.value = value;
		this.text = text;
	}
	
	public String getText() {
		return this.text;
	}
	
	@Override
	public String toString() {
		return this.text;
	}
}
