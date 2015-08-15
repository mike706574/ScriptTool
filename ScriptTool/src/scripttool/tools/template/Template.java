package scripttool.tools.template;

import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root
public class Template {
	@ElementList
	public List<UserInput> inputs;
	@ElementList(required=false)
	public List<Bind> binds;
	@Element
	public String statement;
	
	public Template() {
		
	}
	
	public void setValue(int id, String value) {
		UserInput in = this.findById(id);
		if (in != null) {
			in.value = value;
		}		
	}

	public UserInput findById(int id) {
		UserInput result = null;
		for(UserInput i : inputs) {
			if (id == i.id)  {
				result = i;
				break;
			}
		}
		return result;
	}
}
