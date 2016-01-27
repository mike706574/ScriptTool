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
		String stmt1 = "INSERT INTO PSROLEUSER (ROLEUSER, ROLENAME, DYNAMIC_SW) VALUES ('IUA019', 'EOPP_USER', 'N')";
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
