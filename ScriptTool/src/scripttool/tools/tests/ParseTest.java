package scripttool.tools.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import scripttool.tools.script.Script;
import scripttool.tools.script.ScriptParseException;

public class ParseTest {

	@Test
	public void createBackupOfStatement() {
		String stmt1 = "INSERT INTO PS_SEC_BU_OPR (OPRID, BUSINESS_UNIT) (SELECT 'Tim', Z.BUSINESS_UNIT FROM PS_SEC_BU_OPR Z WHERE Z.OPRID = 'Andra' AND NOT EXISTS (SELECT X.BUSINESS_UNIT FROM PS_SEC_BU_OPR X WHERE X.OPRID = 'Tim' AND X.BUSINESS_UNIT = Z.BUSINESS_UNIT))";
		
		Script script = new Script();
		try {
			script.AddStatementString(stmt1);
		} catch (ScriptParseException e) {
			e.printStackTrace();
			fail("Could not add statement to script:\n" + stmt1);
		}
		script.CreateBackout();
	}
}
