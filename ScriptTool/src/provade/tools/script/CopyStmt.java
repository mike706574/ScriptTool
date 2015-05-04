package provade.tools.script;

import net.sf.jsqlparser.statement.Statement;

public interface CopyStmt {
	void CopyFields(Statement stmt);
}
