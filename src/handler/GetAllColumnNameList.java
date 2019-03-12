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

@WebServlet("/GetAllColumnNameList")
public class GetAllColumnNameList extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
    public GetAllColumnNameList() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONObject data = new JSONObject();
		JSONArray allColumnNameList = new JSONArray();
		JSONArray tableNameList = JSONUtil.getJSONObject(request).getJSONArray("tableNameList");
		// 获取数据及生成数据
		for(int i = 0;i < tableNameList.length();i++) {
			String tableName = tableNameList.getString(i);
			JSONArray columnNameList = getColumnNameList(request, tableName);
			for(int j = 0;j < columnNameList.length();j++) {
				allColumnNameList.put(tableName + "." + columnNameList.getString(j));
			}
		}
		data.put("allColumnNameList", allColumnNameList);
		// 响应请求
		ResponseUtil.sendJSON(response, data);
	}
	protected JSONArray getColumnNameList(HttpServletRequest request, String tableName) {
		DBUtil dbUtil = DB.getDBUtil(request);
		String sql = "SELECT column_name FROM all_tab_cols WHERE table_name=?";
		ResultSet resultSet = dbUtil.getQueryResultSet(sql, tableName);
		JSONArray jsonArray = JSONUtil.getJSONArray(resultSet, "column_name");
		return jsonArray;
	}
}
