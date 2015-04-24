package provade.tools.script;

public enum Operation {
	UPDATE("UPDATE", "\\s*UPDATE\\s+(\\S+)\\s+SET\\s+(.*)WHERE"),
	DELETE("DELETE", "\\s*DELETE\\s+FROM\\s+(\\S+)\\s+WHERE"),
	INSERT("INSERT", "\\s*INSERT\\s+INTO\\s+(\\S+)\\s+(?:SELECT|VALUES(?:\\s+)\\x28)(.*)(?:FROM|\\x29)"),
	SELECT("SELECT", "\\s*SELECT.*FROM\\s+(\\S+)\\s+WHERE");
	
	public final String opStr;
	public final String regEx;
	private Operation(String opStr, String regEx) {
		this.opStr = opStr;
		this.regEx = regEx;
	}

}
