package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import oracle.jdbc.driver.OracleConnection;
import oracle.jdbc.pool.OracleDataSource;

/**
 * DBUtil用于数据库连接
 * @author LINZHEHUANG
 * @version 1.04,18/05/06
 */

public class DBUtil {
	private String dbType, url;
	private static DBUtil instance = null;
	private Connection connection = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;
	private String statementType = "";
	
	// 数据库类型
	public static String SQL_SERVER = "SQL_SERVER", ORACLE = "ORACLE";
	// 语句类型
	private static String STATEMENT_QUERY = "STATEMENT_QUERY", STATEMENT_UPDATE = "STATEMENT_UPDATE";
	
	public static DBUtil getInstance(String dbType, String host, String port, String dbName) throws NullPointerException {
		if(instance != null) {	// 防止重复创建实例
			return instance;
		} else {
			if((instance = new DBUtil(dbType, host, port, dbName)) != null) {
				return instance;
			} else {
				throw new NullPointerException();
			}
		}
	}
	
	public boolean createConnection(String user, String password) {
		String dbDriver = "";
		if(dbType == SQL_SERVER) dbDriver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
		else if(dbType == ORACLE) dbDriver = "oracle.jdbc.driver.OracleDriver";

		try {
			Class.forName(dbDriver);
			connection = DriverManager.getConnection(url, user, password);
		} catch(ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (SQLException e) {
			return false;
		}
		return true;
	}
	
	public void destoryConnection() {
		try {
			if(connection != null) {
				if(statementType == STATEMENT_QUERY) {
					resultSet.close();
					preparedStatement.close();
				}
				else if(statementType == STATEMENT_UPDATE) {
					
				}
				connection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public ResultSet getQueryResultSet(String sql, Object... args) {
		try {
			preparedStatement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			for(int i = 0;i < args.length;i++) {
				preparedStatement.setObject(i+1, args[i]);
			}
			resultSet = preparedStatement.executeQuery();
			statementType = STATEMENT_QUERY;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// TODO:
		return resultSet;
	}
	public void executeUpdate(String sql, Object... args) {
		try{
			preparedStatement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			for(int i = 0;i < args.length;i++) {
				preparedStatement.setObject(i+1, args[i]);
			}
			preparedStatement.executeUpdate();
			statementType = STATEMENT_QUERY;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void startup() {
		// via:http://www.oracledistilled.com/java/database-startup-and-shutdown-through-jdbc/
		// Refactor	
		Properties prop = new Properties();
		prop.setProperty("user","sys");
		prop.setProperty("password","oracle");
		prop.setProperty("internal_logon","sysdba");
		prop.setProperty("prelim_auth","true");
	    
		OracleDataSource ods = null;
		try {
			ods = new OracleDataSource();
			ods.setURL("jdbc:oracle:thin:@127.0.0.1:1521:XE");
			ods.setConnectionProperties(prop);
			OracleConnection ocon = (OracleConnection)ods.getConnection();
			ocon.startup(OracleConnection.DatabaseStartupMode.NO_RESTRICTION);
			//
			prop.clear();
		    prop.setProperty("user","sys");
		    prop.setProperty("password","oracle");
		    prop.setProperty("internal_logon","sysdba");
		       
		    ods = new OracleDataSource();
		    ods.setConnectionProperties(prop);
		    ods.setURL("jdbc:oracle:thin:@127.0.0.1:1521:XE");
		    ocon = (OracleConnection)ods.getConnection();
			//
			Statement stmt = ocon.createStatement();
			stmt.execute("alter database mount");
			stmt.execute("alter database open");
			stmt.close();
			ocon.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void shutdown() {
		Properties prop = new Properties();
	    prop.setProperty("user","sys");
	    prop.setProperty("password","oracle");
	    prop.setProperty("internal_logon","sysdba");
	       
	    OracleDataSource ods;
		try {
			ods = new OracleDataSource();
		    ods.setConnectionProperties(prop);
		    ods.setURL("jdbc:oracle:thin:@127.0.0.1:1521:XE");
		    OracleConnection ocon = (OracleConnection)ods.getConnection();
		    ocon.shutdown(OracleConnection.DatabaseShutdownMode.IMMEDIATE);
		    //
		    Statement stmt = ocon.createStatement();
		    stmt.execute("alter database close normal");
		    stmt.execute("alter database dismount");
		    stmt.close();
		    //
		    ocon.shutdown(OracleConnection.DatabaseShutdownMode.FINAL);
		    ocon.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	// 
	private DBUtil(String dbType, String host, String port, String dbName) {
		if(dbType != SQL_SERVER || dbType != ORACLE) {
			// TODO:增加数据库类型异常抛出
		}
		this.dbType = dbType;
		this.url = createURL(host, port, dbName);
	}
	
	private String createURL(String host, String port, String dbName) {
		String url = "";
		if(dbType == SQL_SERVER) {
			url += "jdbc:sqlserver://";
			url += host + ":";
			url += port + ";";
			url += "DatabaseName=" + dbName;
		} else if(dbType == ORACLE) {
			url += "jdbc:oracle:thin:@";
			url += host + ":";
			url += port + ":";
			url += dbName;
		}
		return url;
	}
}
