package handler;

import java.io.IOException;
import java.sql.ResultSet;
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

@WebServlet("/GetUserSet")
public class GetUserSet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
    public GetUserSet() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONObject data = new JSONObject();
		DBUtil dbUtil = DB.getDBUtil(request);
		String user = JSONUtil.getJSONObject(request).getString("user");
		String getTableSQL = "SELECT table_name FROM dba_tables WHERE owner=?",
			   getColumnSQL = "SELECT column_name FROM user_tab_cols WHERE table_name=?";
		// 获取表名和列名
		ResultSet tableResultSet = dbUtil.getQueryResultSet(getTableSQL, user);
		JSONArray tables = JSONUtil.getJSONArray(tableResultSet, "table_name");
		List<JSONObject> tableSet = new ArrayList<JSONObject>();
		for(int i=0;i < tables.length();i++) {
			JSONObject table = new JSONObject();
			String tableName = tables.getString(i);
			ResultSet columnResultSet = dbUtil.getQueryResultSet(getColumnSQL, tableName);
			JSONArray columns = JSONUtil.getJSONArray(columnResultSet, "column_name");
			table.put("tableName", tableName);
			table.put("columnNameList", columns);
			tableSet.add(table);
		}
		data.put("tableSet", tableSet);
		// 响应数据
		ResponseUtil.sendJSON(response, data);
		// 关闭连接
		dbUtil.destoryConnection();
	}

}
