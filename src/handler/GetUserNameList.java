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

@WebServlet("/GetUserNameList")
public class GetUserNameList extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
    public GetUserNameList() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String sql = "";
		JSONObject data = new JSONObject();
		DBUtil dbUtil = DB.getDBUtil(request);
		// 获取数据
		sql = "SELECT username FROM dba_users";
		ResultSet resultSet = dbUtil.getQueryResultSet(sql);
		JSONArray jsonArray = JSONUtil.getJSONArray(resultSet, "username");
		// 生成数据
		data.put("userNameList", jsonArray);
		// 响应请求
		ResponseUtil.sendJSON(response, data);
		// 关闭连接
		dbUtil.destoryConnection();
	}

}
