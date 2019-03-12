package handler;

import java.io.IOException;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import database.DB;
import database.DBUtil;
import util.JSONUtil;
import util.ResponseUtil;

@WebServlet("/GetQueryResult")
public class GetQueryResult extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
    public GetQueryResult() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		DBUtil dbUtil = DB.getDBUtil(request);
		JSONObject data = new JSONObject();
		JSONArray columnSet = new JSONArray();
		JSONObject queryStatementObject = JSONUtil.getJSONObject(request);
		JSONArray columnNameList = queryStatementObject.getJSONArray("columnNameList");
		String sql = generateQueryStatement(queryStatementObject);
		ResultSet resultSet = dbUtil.getQueryResultSet(sql);
		System.out.println(sql);
		for(int i = 0;i < columnNameList.length();i++) {
			JSONObject column = new JSONObject();
			String columnName = columnNameList.getString(i);
			JSONArray columnValueList = JSONUtil.getJSONArray(resultSet, columnName.split("[.]")[1]);
			column.put("columnName", columnName);
			column.put("columnValueList", columnValueList);
			//
			columnSet.put(column);
		}
		data.put("columnSet", columnSet);
		// 响应请求
		ResponseUtil.sendJSON(response, data);
		// 关闭连接
		dbUtil.destoryConnection();
	}
	protected String generateQueryStatement(JSONObject queryStatementObject) {
		String userName = queryStatementObject.getString("userName");
		JSONArray tableNameList = queryStatementObject.getJSONArray("tableNameList");
		JSONArray columnNameList = queryStatementObject.getJSONArray("columnNameList");
		JSONArray conconjunctionList = queryStatementObject.getJSONObject("whereClause").getJSONArray("conjunctionList");
		JSONArray conditionSet = queryStatementObject.getJSONObject("whereClause").getJSONArray("conditionSet");
		String result = "SELECT ";
		// 查询语句主句前半部分
		for(int i = 0;i < columnNameList.length();i++) {
			if(i == (columnNameList.length()-1)) {
				result += columnNameList.getString(i);
			} else {
				result += columnNameList.getString(i) + ",";
			}
		}
		result += " FROM ";
		// 查询语句主句后半部分
		for(int i = 0;i < tableNameList.length();i++) {
			if(i == (tableNameList.length()-1)) {
				result += userName + "." + tableNameList.getString(i);
			} else {
				result += userName + "." + tableNameList.getString(i) + ",";
			}
		}
		// WHERE子句
		if(conditionSet.length() != 0) result += " WHERE ";
		for(int i = 0;i < conditionSet.length();i++) {
			if(i > 0) {
				result += " " + conconjunctionList.getString(i-1) + " ";
 			}
			JSONObject condition = conditionSet.getJSONObject(i);
			int conditionType = condition.getInt("conditionType");
			boolean not = condition.getBoolean("not");
			String columnName = condition.getString("columnName");
			JSONArray argList = condition.getJSONArray("argList");
			String firstArg,secondArg,notStr;
			
			if(not == true) {
				notStr = " NOT ";
			} else {
				notStr = "";
			}
			switch(conditionType) {
				case 1:
				firstArg = argList.getString(0);
				secondArg = argList.getString(1);
				result += " ( " + notStr + " ";
				result += columnName + firstArg + secondArg;
				result += " )";
				break;
				
				case 2:
				firstArg = argList.getString(0);
				secondArg = argList.getString(1);
				result += " ( ";
				result += columnName + notStr + " BETWEEN " + firstArg  + " AND " + secondArg;
				result += " ) ";
				break;
				
				case 3:
				firstArg = argList.getString(0);
				result += " ( ";
				result += columnName + notStr + " LIKE " + "'" + firstArg + "'";
				result += " )";
				break;
				
				case 4:
				result += " ( ";
				result += columnName + " IS " + notStr + " NULL ";
				result += " )";
				break;
				
				case 5:
				firstArg = argList.getString(0);
				result += " ( " + notStr + " ";
				result += columnName + " = " + firstArg;
				result += " )";
				break;
			}
		}
		return result;
	}
}
