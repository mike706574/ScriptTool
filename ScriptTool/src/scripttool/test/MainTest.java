package scripttool.test;

import static org.junit.Assert.*;

import java.util.List;

import net.sf.jsqlparser.statement.Statement;

import org.junit.Before;
import org.junit.Test;

import scripttool.tools.script.Script;
import scripttool.tools.script.ScriptParseException;

public class MainTest {

	public MainTest() {
	}

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		String stmt2 = "INSERT INTO PS_SEC_BU_OPR (OPRID, BUSINESS_UNIT) (SELECT 'Tim', Z.BUSINESS_UNIT FROM PS_SEC_BU_OPR Z WHERE Z.OPRID = 'Andra' AND NOT EXISTS (SELECT X.BUSINESS_UNIT FROM PS_SEC_BU_OPR X WHERE X.OPRID = 'Tim' AND X.BUSINESS_UNIT = Z.BUSINESS_UNIT))";
		
		String stmt1 = "DELETE FROM PSOPRDEFN WHERE OPRID = 'Andra'";
		Script testScript = new Script();
		try {
			testScript.AddStatementString(stmt1);
		} catch (ScriptParseException e) {
			fail("Parse exception thrown: " + e.toString());
		}
		
		List<Statement> bkStmts = testScript.CreateBackout();
		assertNotNull(bkStmts);
		for (Statement s : bkStmts) {
			System.out.println(s.toString());
		}
	}

}
