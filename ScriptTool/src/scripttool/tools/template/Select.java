package scripttool.tools.template;

import java.util.List;

import org.simpleframework.xml.ElementList;

public class Select {
	@ElementList
	public List<Option> options;
	
	public Select() {
		
	}
}
