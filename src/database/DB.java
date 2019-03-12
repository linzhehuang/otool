package database;

import javax.servlet.http.HttpServletRequest;

public class DB {
	public static String host = "127.0.0.1", port = "1521", dbName = "XE";
	public static DBUtil getDBUtil(HttpServletRequest request) {
		// Following line use for test.
		String user = "sys as sysdba", password = "oracle";
		// Uncomment the line when run in production environment.
		//String password = (String)request.getSession().getAttribute("password"); 
		DBUtil dbUtil = DBUtil.getInstance(DBUtil.ORACLE, host, port, dbName);
		dbUtil.createConnection(user, password);
		return dbUtil;
	}
}
