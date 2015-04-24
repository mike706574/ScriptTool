package provade.tools.script;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScriptMain {

	public static Table getTableFromString(String stmt) {
		Table retTable;
		String opReg = "";
		for (Operation o : Operation.values()) {
			opReg += o.opStr + "|";
		}
		opReg = opReg.substring(0, (opReg.length() - 1));
		String opStr = null;
		Matcher opMatch = Pattern.compile(opReg).matcher(stmt);
		if (opMatch.find()) {
			opStr = opMatch.group();
		}
		if (opStr == null) {
			System.out.println("Did not get an operation");
			return null;
		}

		retTable = new Table(opStr);

		String tName;
		String tRegEx = retTable.op.regEx;
		Matcher tMatch = Pattern.compile(tRegEx).matcher(stmt);
		if (!tMatch.find()) {
			System.out.println("Found no table");
			return null;
		}
		tName = tMatch.group(1);
		retTable.name = tName;
		if (tMatch.groupCount() == 1) {
			return retTable;
		}
		
		String valsStr = tMatch.group(2);
		String[] valsArr = Pattern.compile("\\s*,\\s*").split(valsStr);
		if (valsArr.length == 0) {
			// TODO: do something
			System.out.println("No values found");
		}

		List<Field> opFields = new ArrayList<Field>();
		List<Field> whFields = new ArrayList<Field>();
		Pattern opPat = Pattern.compile("\\s*(\\S+)\\s*=\\s*(\\S+)");
		Matcher oMatch = opPat.matcher(valsStr);

		for (String s : valsArr) {
			Matcher m = opPat.matcher(s);
			if (m.find()) {
				opFields.add(new Field(m.group(1), m.group(2)));
			} else {
				opFields.add(new Field(s));
			}
		}

		retTable.whereFields = whFields;
		retTable.opFields = opFields;

		return retTable;
	}

	public static void main(String[] args) {
		ArrayList<Field> fields = new ArrayList<Field>();
		fields.add(new Field("OPRID", "'TIM'"));
		fields.add(new Field("PWRD", "'zebra99'"));

		ArrayList<Field> whereFields = new ArrayList<Field>();
		whereFields
				.add(new Field("OPRID", new String[] { "'TIM'", "'Andra'" }));
		whereFields.add(new Field("EMAIL", "'tim@tim.com'"));

		Table t = new Table(Operation.INSERT, "PSOPRDEFN", fields, whereFields);
		String str = t.buildString();
		System.out.println(str);

		String inStmt = " INSERT  INTO PSOPRDEFN SELECT 'TIM', 'zebra99' FROM PS_INSTALLATION";
		String upStmt = "UPDATE PSOPRDEFN SET OPRID = 'TIM', EMAIL = 'a@a.com' WHERE";
		String delStmt = "DELETE FROM PSOPRDEFN WHERE ";
		String selStmt = "SELECT * FROM PSOPRDEFN WHERE";
		Table ta = ScriptMain.getTableFromString(selStmt);
		System.out.println("Table op: " + ta.op.opStr + " name: " + ta.name);
		if(ta.opFields == null) {
			System.out.println("No op fields");
		} else
			System.out.println(ta.opFields.toString());
	}

}
