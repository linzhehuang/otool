package database;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;


//import java.sql.ResultSet;
//import java.sql.SQLException;

public class DBUtilTest {

	public static void main(String[] args) throws SQLException {
		DBUtil dbUtil = null;
		try {
			dbUtil = DBUtil.getInstance(DBUtil.ORACLE, "127.0.0.1", "1521", "XE");
		} catch (Exception e) {
			e.printStackTrace();
		}
		dbUtil.createConnection("sys as sysdba", "oracle");
		ResultSet rs = dbUtil.getQueryResultSet("select * from mydba.video");
		ResultSetMetaData md = rs.getMetaData();
		for(int i = 1;i <= md.getColumnCount();i++) {
			System.out.println(md.getColumnName(i));
		}
		//dbUtil.createConnection(user, password);
//		dbUtil.shutdown();
//		System.out.println("shutdown");
//		try {
//			Thread.sleep(5000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		dbUtil.startup();
//		System.out.println("startup");
//		String item = "VIDEO.V_NAME";
//		System.out.println(item.split(".").length);
//		ResultSet result = dbUtil.getQueryResultSet("show parameter spfile");
//		
//		
//		try {
//			while(result.next()) {
//				System.out.println(result.getString(1));
//			}
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		//dbUtil.destoryConnection();
	}

}
