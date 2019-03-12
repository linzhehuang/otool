package util;

import database.DB;
import database.DBUtil;

public class ValidateUtil {
	public static boolean validatePassword(String password) {
		String user = "sys as sysdba";
		DBUtil dbUtil = DBUtil.getInstance(DBUtil.ORACLE, DB.host, DB.port, DB.dbName);
		return dbUtil.createConnection(user, password);
	}
}
