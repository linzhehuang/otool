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

@WebServlet("/GetTableNameList")
public class GetTableNameList extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
    public GetTableNameList() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String sql = "";
		JSONObject data = new JSONObject();
		DBUtil dbUtil = DB.getDBUtil(request);
		String userName = JSONUtil.getJSONObject(request).getString("userName");
		// 获取数据
		sql = "SELECT table_name FROM dba_tables WHERE owner=?";
		ResultSet resultSet = dbUtil.getQueryResultSet(sql, userName);
		JSONArray jsonArray = JSONUtil.getJSONArray(resultSet, "table_name");
		data.put("tableNameList", jsonArray);
		// 响应请求
		ResponseUtil.sendJSON(response, data);
		// 关闭连接
		dbUtil.destoryConnection();
	}

}
