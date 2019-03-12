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

@WebServlet("/GetColumnNameList")
public class GetColumnNameList extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
    public GetColumnNameList() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		DBUtil dbUtil = DB.getDBUtil(request);
		String sql = "";
		JSONObject data = new JSONObject();
		String tableName = JSONUtil.getJSONObject(request).getString("tableName");
		// 获取数据
		sql = "SELECT column_name FROM all_tab_cols WHERE table_name=?";
		ResultSet resultSet = dbUtil.getQueryResultSet(sql, tableName);
		JSONArray jsonArray = JSONUtil.getJSONArray(resultSet, "column_name");
		data.put("columnNameList", jsonArray);
		// 响应请求
		ResponseUtil.sendJSON(response, data);
		// 关闭连接
		dbUtil.destoryConnection();
	}

}
