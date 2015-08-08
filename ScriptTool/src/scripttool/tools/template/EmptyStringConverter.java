package scripttool.tools.template;

import org.simpleframework.xml.convert.Converter;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.OutputNode;

public class EmptyStringConverter implements Converter<String> {

	@Override
	public String read(InputNode arg0) throws Exception {
		if (arg0 == null) {
			return "";
		}
		return arg0.getValue();
	}

	@Override
	public void write(OutputNode arg0, String arg1) throws Exception {
		arg0.setValue(arg1);
	}

}
