package handler;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

@WebServlet("/GetTableInformation")
public class GetTableInformation extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
    public GetTableInformation() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//
		JSONObject data = new JSONObject();
		DBUtil dbUtil = DB.getDBUtil(request);
		ResultSet resultSet = null;
		String tableName = JSONUtil.getJSONObject(request).getString("tableName");
		// 生成数据
		List<String> columnNameList = getColumnNameList(dbUtil, tableName);
		// 生成列名
		String columnNames = "";
		for(int i = 0;i < columnNameList.size();i++) {
			String columnName = columnNameList.get(i);
			if(i != (columnNameList.size()-1)) {
				columnNames += columnName + ",";
			} else {
				columnNames += columnName;
			}
		}
		String sql = "SELECT " + columnNames + " FROM " + tableName;
		// 
		JSONArray columnSet = new JSONArray();
		resultSet = dbUtil.getQueryResultSet(sql);
		// 
		for(int i = 0;i < columnNameList.size();i++) {
			String columnName = columnNameList.get(i);
			JSONArray valueList = JSONUtil.getJSONArray(resultSet, columnName);
			JSONObject column = new JSONObject();
			column.put("columnName", columnName);
			column.put("columnValueList", valueList);
			columnSet.put(column);
		}
		data.put("columnSet", columnSet);
		//
		ResponseUtil.sendJSON(response, data);
		//
		dbUtil.destoryConnection();
	}
	// 获取表格的列名
	protected List<String> getColumnNameList(DBUtil dbUtil, String tableName) {
		List<String> list = new ArrayList<String>();
		String sql = "SELECT * FROM " + tableName;
		System.out.println(tableName);
		ResultSet resultSet = dbUtil.getQueryResultSet(sql);
		ResultSetMetaData metaData = null;
		try {
			metaData = resultSet.getMetaData();
			for(int i = 1;i <= metaData.getColumnCount();i++) {
				list.add(metaData.getColumnName(i));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
}
