package scripttool.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import net.sf.jsqlparser.statement.Statement;

import org.junit.Before;
import org.junit.Test;

import scripttool.tools.network.NetworkUtils;
import scripttool.tools.script.Script;
import scripttool.tools.script.ScriptParseException;

public class MainTest {

	public MainTest() {
	}

	@Before
	public void setUp() throws Exception {
	}

	
	public void test() {
		/* TODO: for below, use export statement
		 * 		 -if select has a select column, have it delete all based on the static values then can import what was exported to get the originals */
		String stmt2 = "INSERT INTO PS_SEC_BU_OPR (OPRID, BUSINESS_UNIT) (SELECT 'Tim', Z.BUSINESS_UNIT FROM PS_SEC_BU_OPR Z WHERE Z.OPRID = 'Andra' AND NOT EXISTS (SELECT X.BUSINESS_UNIT FROM PS_SEC_BU_OPR X WHERE X.OPRID = 'Tim' AND X.BUSINESS_UNIT = Z.BUSINESS_UNIT))";
		
		String stmt1 = "DELETE FROM PSOPRDEFN WHERE OPRID = 'Andra'";
		Script testScript = new Script();
		try {
			testScript.AddStatementString(stmt2);
		} catch (ScriptParseException e) {
			fail("Parse exception thrown: " + e.toString());
		}
		List<Statement> exStmts = testScript.CreateBackup();
		assertNotNull(exStmts);
		for (Statement s : exStmts) {
			System.out.println(s.toString());
		}
		List<Statement> bkStmts = testScript.CreateBackout();
		assertNotNull(bkStmts);
		for (Statement s : bkStmts) {
			System.out.println(s.toString());
		}
	}

	@Test
	public void ftpTest() {
		String url = "192.168.1.4";
		ArrayList<String> files = null;
		try {
			files = NetworkUtils.getTemplateListFromRemote(url, "milwbest91@hotmail.com", "ksrj2n$56", "zip");
		} catch (SocketException e) {
			e.printStackTrace();
			fail("Socket exception: " + e.toString());
		} catch (IOException e) {
			e.printStackTrace();
			fail("IOException: " + e.toString());
		}
		
		for (String file : files) {
			System.out.println("Found file: " + file);
		}
	}
}
