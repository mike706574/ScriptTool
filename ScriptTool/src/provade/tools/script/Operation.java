package provade.tools.script;

public enum Operation {
	UPDATE("UPDATE", "\\s*UPDATE\\s+(\\S+)\\s+SET\\s+(.*)WHERE", ".*WHERE\\s+(.*);"),
	DELETE("DELETE", "\\s*DELETE\\s+FROM\\s+(\\S+)\\s+WHERE", ".*WHERE\\s+(.*);"),
	INSERT("INSERT", "\\s*INSERT\\s+INTO\\s+(\\S+)\\s+(?:SELECT|VALUES(?:\\s+)\\x28)(.*)(?:FROM|\\x29).*", ".*WHERE\\s+(.*);"),
	SELECT("SELECT", "\\s*SELECT.*FROM\\s+(\\S+)\\s+WHERE", "\\s+(.*);");
	
	public final String opStr;
	public final String tblOpregEx;
	public final String whereRegEx;
	private Operation(String opStr, String tblOpregEx, String whereRegEx) {
		this.opStr = opStr;
		this.tblOpregEx = tblOpregEx;
		this.whereRegEx = whereRegEx;
	}

}
	