package provade.tools.script;

public class ScriptParseException extends Exception {
	private static final long serialVersionUID = 1L;
	private Throwable cause = null;

	public ScriptParseException() {
		super();
	}

	public ScriptParseException(String arg0) {
		super(arg0);
	}

	public ScriptParseException(Throwable arg0) {
		this.cause = arg0;
	}

	public ScriptParseException(String arg0, Throwable arg1) {
		super(arg0);
		this.cause = arg1;
	}
	
	@Override
	public String getMessage() {
		return "Error parsing statement: " + super.getMessage();
	}

	@Override
	public Throwable getCause() {
		return cause;
	}
}
